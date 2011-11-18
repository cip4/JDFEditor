/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2011 The International Cooperation for the Integration of 
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

package org.cip4.jdfeditor;

import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;

/**
 * InsertResourceAndLinkEdit.java
 * @author Elena Skobchenko
 */
public class InsertResourceAndLinkEdit extends AbstractUndoableEdit
{
	private static final long serialVersionUID = -2778264565816334345L;

	private final JDFTreeNode node;
	private final JDFTreeNode resNode;
	private final JDFTreeNode resLinkNode;
	private final JDFTreeNode resPoolNode;
	private JDFTreeNode resLinkPoolNode;
	private final JDFFrame parFrame;
	private TreePath path = null;
	private TreePath resPath = null;
	private TreePath resLinkPath = null;
	private final boolean hasResourcePool;
	private final boolean hasResourceLinkPool;
	private boolean resSuccess = true;
	private boolean resLinkSuccess = true;

	/**
	 * 
	 * @param parent
	 * @param _node
	 * @param _hasResourcePool
	 * @param _hasResourceLinkPool
	 * @param _resNode
	 * @param _resLinkNode
	 */
	public InsertResourceAndLinkEdit(final JDFFrame parent, final JDFTreeNode _node, boolean _hasResourcePool, boolean _hasResourceLinkPool, final JDFTreeNode _resNode, final JDFTreeNode _resLinkNode)
	{
		parFrame = parent;
		this.node = _node;
		this.resNode = _resNode;
		this.resLinkNode = _resLinkNode;
		this.hasResourcePool = _hasResourcePool;
		this.hasResourceLinkPool = _hasResourceLinkPool;
		path = new TreePath(node.getPath());

		resPath = new TreePath(resNode.getPath());
		resPoolNode = (JDFTreeNode) resNode.getParent();

		if (resLinkNode != null)
		{
			resLinkPath = new TreePath(resLinkNode.getPath());
			resLinkPoolNode = (JDFTreeNode) resLinkNode.getParent();
		}

		parFrame.updateViews(resPath);
	}

	/**
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException
	{
		final JDFTreeModel model = parFrame.getModel();
		resSuccess = model.deleteItem(resPath);
		if (!hasResourcePool)
		{
			model.deleteItem(resPath.getParentPath()); // delete ResPool if it did not exist before action started
		}

		if (resLinkPath != null)
		{
			resLinkSuccess = model.deleteItem(resLinkPath);
			if (!hasResourceLinkPool)
			{
				model.deleteItem(resLinkPath.getParentPath()); // delete ResLinkPool if it did not exist before action started
			}
		}

		if (hasResourcePool)
		{
			path = new TreePath(resPoolNode.getPath()); // path to select in a tree after undo
		}
		parFrame.updateViews(path);
	}

	/**
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException
	{
		final JDFTreeModel model = parFrame.getModel();
		JDFResource res = (JDFResource) resNode.getElement();
		JDFResourcePool resPool = (JDFResourcePool) resPoolNode.getElement();
		if (!hasResourcePool)
		{
			JDFNode jdfNode = (JDFNode) node.getElement();
			model.insertInto(resPoolNode, node, -1);
			jdfNode.appendChild(resPool);
		}
		model.insertInto(resNode, resPoolNode, -1);
		resPool.appendChild(res);
		if (resLinkNode != null)
		{
			JDFResourceLink resLink = (JDFResourceLink) resLinkNode.getElement();
			JDFResourceLinkPool resLinkPool = (JDFResourceLinkPool) resLinkPoolNode.getElement();
			if (!hasResourceLinkPool)
			{
				JDFNode jdfNode = (JDFNode) node.getElement();
				model.insertInto(resLinkPoolNode, node, -1);
				jdfNode.appendChild(resLinkPool);
			}
			model.insertInto(resLinkNode, resLinkPoolNode, -1);
			resLinkPool.appendChild(resLink);
			parFrame.updateViews(resLinkPath);
		}

		parFrame.updateViews(resPath);
	}

	/**
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#canUndo()
	 */
	@Override
	public boolean canUndo()
	{
		return resSuccess && resLinkSuccess;
	}

	/**
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#canRedo()
	 */
	@Override
	public boolean canRedo()
	{
		return resSuccess && resLinkSuccess;
	}

	/**
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName()
	{
		return "Insert Resource";
	}

}