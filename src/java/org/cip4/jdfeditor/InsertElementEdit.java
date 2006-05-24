package org.cip4.jdfeditor;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.cip4.jdflib.core.KElement;

/*
 * InsertElementAfterEdit.java
 * @author Elena Skobchenko
 */

public class InsertElementEdit extends EditorUndoableEdit 
{
    private static final long serialVersionUID = -2778264565816334345L;

    private JDFTreeNode newNode;
    private JDFTreeNode beforeNode;
    private TreePath newNodePath = null; 
    private String presentationString=null;

    ///////////////////////////////////////////////////////////////////////////////////////
        
    public InsertElementEdit(final JDFFrame parentFrame, final JDFTreeNode _beforeNode, 
            final JDFTreeNode _newNode, String _presentationString) 
    {
         super(parentFrame);
         this.beforeNode = _beforeNode;
         this.newNode = _newNode;
         presentationString=_presentationString;         
         newNodePath = new TreePath(_newNode.getPath());

         parFrame.updateViews(newNodePath);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    
    public void undo() throws CannotUndoException 
    {
        parFrame.getModel().deleteItem(newNodePath);
        parFrame.updateViews(newNodePath.getParentPath());
        super.undo();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    
    public void redo() throws CannotRedoException 
    {
        KElement newElement=newNode.getElement();
        final JDFTreeNode parentNode=(JDFTreeNode) newNodePath.getParentPath().getLastPathComponent();
        final KElement parentElement = parentNode.getElement();
        if(beforeNode!=null)
        {
            parFrame.getModel().insertBefore(beforeNode, newNode);
            KElement beforeElement= beforeNode.getElement();
            parentElement.insertBefore(newElement, beforeElement);
        }
        else
        {
            parFrame.getModel().insertInto(newNode,parentNode,-1);
            parentElement.appendChild(newElement);
        }
        
        parFrame.updateViews(newNodePath);
        super.redo();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    
    public String getPresentationName() 
    {
         return presentationString;
    }

}