/*
*
* The CIP4 Software License, Version 1.0
*
*
* Copyright (c) 2001-2012 The International Cooperation for the Integration of 
* Processes in  Prepress, Press and Postpress (CIP4).  All rights 
* reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* 1. Redistributions of source code must retain the above copyright
*    notice, this list of conditions and the following disclaimer. 
*
* 2. Redistributions in binary form must reproduce the above copyright
*    notice, this list of conditions and the following disclaimer in
*    the documentation and/or other materials provided with the
*    distribution.
*
* 3. The end-user documentation included with the redistribution,
*    if any, must include the following acknowledgment:  
*       "This product includes software developed by the
*        The International Cooperation for the Integration of 
*        Processes in  Prepress, Press and Postpress (www.cip4.org)"
*    Alternately, this acknowledgment may appear in the software itself,
*    if and wherever such third-party acknowledgments normally appear.
*
* 4. The names "CIP4" and "The International Cooperation for the Integration of 
*    Processes in  Prepress, Press and Postpress" must
*    not be used to endorse or promote products derived from this
*    software without prior written permission. For written 
*    permission, please contact info@cip4.org.
*
* 5. Products derived from this software may not be called "CIP4",
*    nor may "CIP4" appear in their name, without prior written
*    permission of the CIP4 organization
*
* Usage of this software in commercial products is subject to restrictions. For
* details please consult info@cip4.org.
*
* THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
* OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED.  IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR
* THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR
* ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
* LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
* USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
* OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGE.
* ====================================================================
*
* This software consists of voluntary contributions made by many
* individuals on behalf of the The International Cooperation for the Integration 
* of Processes in Prepress, Press and Postpress and was
* originally based on software 
* copyright (c) 1999-2001, Heidelberger Druckmaschinen AG 
* copyright (c) 1999-2001, Agfa-Gevaert N.V. 
*  
* For more information on The International Cooperation for the 
* Integration of Processes in  Prepress, Press and Postpress , please see
* <http://www.cip4.org/>.
*  
* 
*/
package org.cip4.tools.jdfeditor;

import java.util.Vector;

import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.cip4.jdflib.core.KElement;
import org.cip4.tools.jdfeditor.view.MainView;
import org.cip4.tools.jdfeditor.view.renderer.JDFTreeNode;

/**
 * AddRequiredElemEdit.java
 * @author Elena Skobchenko
 */
public class AddRequiredElemEdit extends EditorUndoableEdit
{
	private static final long serialVersionUID = -2778264565816334345L;

	private final TreePath path;
	private final JDFTreeNode intoNode;
	private final Vector<JDFTreeNode> addedVector;

	/**
	 * 
	 * @param treePath
	 * @param _intoNode
	 * @param _addedVector
	 */
	public AddRequiredElemEdit(final TreePath treePath, final JDFTreeNode _intoNode, final Vector<JDFTreeNode> _addedVector)
	{
		super();
		path = treePath;

		this.intoNode = _intoNode;
		this.addedVector = _addedVector;
		canUndo = _addedVector.size() > 0;
		MainView.getFrame().updateViews(path);
	}

	/**
	 * 
	 * @see org.cip4.tools.jdfeditor.EditorUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException
	{
		for (int i = 0; i < addedVector.size(); i++)
		{
			JDFTreeNode elemNode = addedVector.elementAt(i);
			TreePath elemPath = new TreePath(elemNode.getPath());
			MainView.getModel().deleteItem(elemPath);
		}
		MainView.getFrame().updateViews(path);
		super.undo();
	}

	/**
	 * 
	 * @see org.cip4.tools.jdfeditor.EditorUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException
	{
		KElement intoElement = intoNode.getElement();
		for (int i = 0; i < addedVector.size(); i++)
		{
			JDFTreeNode elemNode = addedVector.elementAt(i);
			KElement element = elemNode.getElement();
			MainView.getModel().insertInto(elemNode, intoNode, -1);
			intoElement.appendChild(element);
		}
		MainView.getFrame().updateViews(path);
		super.redo();
	}

	/**
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName()
	{
		return "AddRequiredElements";
	}

}