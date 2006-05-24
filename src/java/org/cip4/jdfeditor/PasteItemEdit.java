package org.cip4.jdfeditor;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.util.StringUtil;

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
    private KElement pasteElement;
    private JDFTreeNode intoNode;
    private KElement intoElement;
    private String attributeName;
    private String attributeValue;
    private JDFFrame parFrame;
    private int pos;
    private boolean success = true;
    
    public PasteItemEdit(final JDFFrame parentFrame, final TreePath treePath, 
			    		final JDFTreeNode _intoNode, final KElement _intoElement, 
			    		final JDFTreeNode _pasteNode) 
    {
         parFrame = parentFrame;
         path = treePath;
         this.intoNode = _intoNode;
         this.intoElement = _intoElement;
         
         this.pasteNode = _pasteNode;
 
         pos = intoNode.getIndex(pasteNode);
         if (!pasteNode.isElement())
         {
             attributeName = StringUtil.token(pasteNode.toString(), 0, "=");
             attributeValue = StringUtil.token(pasteNode.toString(), 1, "=");
             if (attributeValue.indexOf("\"") != -1)
             {
                 attributeValue = attributeValue.substring(1,attributeValue.length()-1);
             }
         }
         else 
         {
             pasteElement = (JDFElement) pasteNode.getElement();
         }
         pasteNodePath = new TreePath(pasteNode.getPath());
 
         parFrame.updateViews(pasteNodePath);
    }

    public void undo() throws CannotUndoException 
    { 
   		success = parFrame.getModel().deleteItem(pasteNodePath);
        pasteNodePath = null;
        parFrame.updateViews(path); 
    }

    public void redo() throws CannotRedoException 
    {
        parFrame.getModel().insertInto(pasteNode, intoNode, pos);
        if (!pasteNode.isElement())
        {
            intoElement.setAttribute(attributeName, attributeValue);
        }
        else 
        {
            intoElement.appendChild(pasteElement);
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