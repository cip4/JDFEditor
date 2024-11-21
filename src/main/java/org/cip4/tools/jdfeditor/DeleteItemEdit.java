/*
*
* The CIP4 Software License, Version 1.0
*
*
* Copyright (c) 2001-2006 The International Cooperation for the Integration of 
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

import org.cip4.tools.jdfeditor.util.EditorUtils;
import org.cip4.tools.jdfeditor.view.MainView;
import org.cip4.tools.jdfeditor.view.renderer.JDFTreeNode;

import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


/*
 * InsertElementAfterEdit.java
 * @author Elena Skobchenko
 */

public class DeleteItemEdit extends EditorUndoableEdit 
{
    private static final long serialVersionUID = -2778264565816334345L;
    
    private JDFTreeNode delNode;
    private JDFTreeNode parentNode;
    private int pos;
    
    public DeleteItemEdit(final TreePath treePath) 
    {
        super();
        delNode = (JDFTreeNode) treePath.getLastPathComponent();

        boolean bOK=false;
        final TreePath parentPath = treePath.getParentPath();
        if(parentPath!=null)
        {
            parentNode = (JDFTreeNode) parentPath.getLastPathComponent();
            if(parentNode!=null)
            {
                pos = parentNode.getIndex(delNode);                
                bOK= MainView.getModel().deleteItem(treePath);
            }
        }
        if(!bOK)
        {
            EditorUtils.errorBox("ErrorDeletingKey",delNode.toString());
        }
        else
        {
            MainView.getFrame().updateViews(parentPath);
        }
        canUndo=delNode!=null&&bOK;
    }


    @Override
	public void undo() throws CannotUndoException 
    {
        if (!delNode.isElement())
        {
            delNode = MainView.getModel().setAttribute(parentNode, delNode.getName(), delNode.getValue(), null, false);
        }
        else 
        {
            parentNode.getElement().appendChild(delNode.getElement());
            MainView.getModel().insertInto(delNode, parentNode, pos);
        }
        
        TreePath path=new TreePath(delNode.getPath());
        MainView.getFrame().updateViews(path);
        super.undo();
    }

    @Override
	public void redo() throws CannotRedoException 
    {
        TreePath path=new TreePath(delNode.getPath());
        MainView.getModel().deleteNode(delNode,path);
        MainView.getFrame().updateViews(path.getParentPath());
        super.redo();
    }

    @Override
	public String getPresentationName() 
    {
         return "Delete";
    }

}