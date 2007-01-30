package org.cip4.jdfeditor;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/*
 * InsertElementAfterEdit.java
 * @author Elena Skobchenko
 */

public class PasteItemEdit extends AbstractUndoableEdit 
{
    private static final long serialVersionUID = -2778264565816334345L;

    private TreePath path;
    private TreePath pasteNodePath;
    private JDFTreeNode pasteNode;
    private JDFTreeNode intoNode;
    private int pos;
    private boolean success = true;

    public PasteItemEdit(final TreePath treePath, 
            final JDFTreeNode _intoNode,  
            final JDFTreeNode _pasteNode) 
    {
        JDFFrame parFrame=Editor.getFrame();
        path = treePath;
        intoNode = _intoNode;
        pasteNode = _pasteNode;

        pos = intoNode.getIndex(pasteNode);
        pasteNodePath = new TreePath(pasteNode.getPath());

        parFrame.updateViews(pasteNodePath);
    }

    public void undo() throws CannotUndoException 
    { 
        JDFFrame parFrame=Editor.getFrame();
        success = parFrame.getModel().deleteItem(pasteNodePath);
        pasteNodePath = null;
        parFrame.updateViews(path); 
    }

    public void redo() throws CannotRedoException 
    {
        JDFFrame parFrame=Editor.getFrame();
        if (!pasteNode.isElement())
        {
            parFrame.getModel().setAttribute(intoNode, pasteNode.getName(), pasteNode.getValue(), null, false);
        }
        else 
        {
            intoNode.getElement().copyElement(pasteNode.getElement(),null);
            parFrame.getModel().insertInto(pasteNode, intoNode, pos);
        }
        pasteNodePath = new TreePath(pasteNode.getPath());
        parFrame.updateViews(pasteNodePath);
    }

    public boolean canUndo() 
    {
        return  success;
    }

    public boolean canRedo() 
    {
        return  success;
    }

    public String getPresentationName() 
    {
        return "Paste";
    }

}