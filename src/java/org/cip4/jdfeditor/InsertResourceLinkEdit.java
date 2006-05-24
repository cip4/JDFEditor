package org.cip4.jdfeditor;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;

/*
 * InsertResourceLinkEdit.java
 * @author Elena Skobchenko
 */

public class InsertResourceLinkEdit extends AbstractUndoableEdit 
{
    private static final long serialVersionUID = -2778264565816334345L;
    
    private JDFTreeNode node;
    private JDFNode jdfNode;
    private JDFTreeNode resLinkNode;
    private JDFResourceLink resLink;
    private JDFTreeNode resLinkPoolNode;
    private JDFResourceLinkPool resLinkPool;
    private JDFFrame parFrame;
    private TreePath path = null;
    private TreePath resLinkPath = null; 
    private boolean hasResourceLinkPool;
    private boolean success = true;
    
    public InsertResourceLinkEdit(final JDFFrame parent, final JDFNode _jdfNode, final JDFTreeNode _node, 
                                  boolean _hasResourceLinkPool, final JDFResourceLink _resLink, final JDFTreeNode _resLinkNode) 
    {
        parFrame = parent;
        this.node = _node;
        this.jdfNode = _jdfNode;
        this.resLink = _resLink;
        this.resLinkNode = _resLinkNode;
        this.hasResourceLinkPool = _hasResourceLinkPool;
        path = new TreePath(node.getPath());
        
        resLinkPath = new TreePath(resLinkNode.getPath());
        resLinkPoolNode = (JDFTreeNode) resLinkNode.getParent();
        resLinkPool = (JDFResourceLinkPool) resLink.getParentNode_KElement();
                
        parFrame.updateViews(resLinkPath);
    }

    public void undo() throws CannotUndoException 
    {
        final JDFTreeModel model = parFrame.getModel();
        success = model.deleteItem(resLinkPath);
        if (!hasResourceLinkPool)
        {
            model.deleteItem(resLinkPath.getParentPath()); // delete ResLinkPool if it did not exist before action started
        }
        else
        {
            path = new TreePath(resLinkPoolNode.getPath()); // path to select in a tree after undo
        }
        parFrame.updateViews(path);
    }

    public void redo() throws CannotRedoException 
    {        
        final JDFTreeModel model = parFrame.getModel();
        if (!hasResourceLinkPool)
        {
            model.insertInto(resLinkPoolNode, node, -1);
            jdfNode.appendChild(resLinkPool);
        }
        model.insertInto(resLinkNode, resLinkPoolNode, -1);
        resLinkPool.appendChild(resLink);
        
        parFrame.updateViews(resLinkPath);
    }

    public boolean canUndo() 
    {
        return success;
    }

    public boolean canRedo() 
    {
        return success;
    }
    
    public String getPresentationName() 
    {
        return "Insert ResourceLink";
    }

}