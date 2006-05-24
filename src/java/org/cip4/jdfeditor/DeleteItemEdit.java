package org.cip4.jdfeditor;
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
    
    public DeleteItemEdit(final JDFFrame parentFrame, final TreePath treePath) 
    {
        super(parentFrame);
        delNode = (JDFTreeNode) treePath.getLastPathComponent();

        boolean bOK=false;
        final TreePath parentPath = treePath.getParentPath();
        if(parentPath!=null)
        {
            parentNode = (JDFTreeNode) parentPath.getLastPathComponent();
            if(parentNode!=null)
            {
                pos = parentNode.getIndex(delNode);                
                bOK= parFrame.getModel().deleteItem(treePath);
            }
        }
        if(!bOK)
        {
            EditorUtils.errorBox("ErrorDeletingKey",delNode.toString());
        }
        else
        {
            parFrame.updateViews(parentPath);
        }
        canUndo=delNode!=null&&bOK;
    }


    public void undo() throws CannotUndoException 
    {
        if (!delNode.isElement())
        {
            delNode = parFrame.getModel().setAttribute(parentNode, delNode.getName(), delNode.getValue(), null, false);
        }
        else 
        {
            parentNode.getElement().appendChild(delNode.getElement());
            parFrame.getModel().insertInto(delNode, parentNode, pos);      
        }
        
        TreePath path=new TreePath(delNode.getPath());
        parFrame.updateViews(path);
        super.undo();
    }

    public void redo() throws CannotRedoException 
    {
        TreePath path=new TreePath(delNode.getPath());
        parFrame.getModel().deleteNode(delNode,path);
        parFrame.updateViews(path.getParentPath());
        super.redo();
    }

    public String getPresentationName() 
    {
         return "Delete";
    }

}