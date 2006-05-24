package org.cip4.jdfeditor;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/*
 * InsertElementAfterEdit.java
 * @author Elena Skobchenko
 */

public class RenameNodeEdit extends EditorUndoableEdit 
{
    private static final long serialVersionUID = -2778264565816334345L;
    
    private JDFTreeNode node;
    private String previousNodeName;
    private TreePath path;
    
    public RenameNodeEdit(final JDFFrame parentFrame, TreePath _path,
            final JDFTreeNode _newTreeNode, String _previousNodeName) 
    {
        super(parentFrame);
        
        this.node = _newTreeNode;
        this.path=_path;
        this.previousNodeName=_previousNodeName;
        parFrame.updateViews(path);
        canUndo=canRedo=true;
    }
    
    public void undo() throws CannotUndoException 
    { 
        String keep=node.getName();
        if (node.isElement())
        {
            node.getElement().renameElement(previousNodeName,null);
        }
        else 
        {
            node=parFrame.getModel().renameAttribute(node,previousNodeName);
        }
        previousNodeName=keep;
        parFrame.updateViews(path);
    }
    
    public void redo() throws CannotRedoException 
    {
        undo();
    }
        
    public String getPresentationName() 
    {
        return "Rename";
    }
    
}