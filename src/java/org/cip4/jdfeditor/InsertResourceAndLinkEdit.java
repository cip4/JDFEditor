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

/*
 * InsertResourceAndLinkEdit.java
 * @author Elena Skobchenko
 */

public class InsertResourceAndLinkEdit extends AbstractUndoableEdit 
{
    private static final long serialVersionUID = -2778264565816334345L;
    
    private JDFTreeNode node;
    private JDFTreeNode resNode;
    private JDFTreeNode resLinkNode;
    private JDFTreeNode resPoolNode;
    private JDFTreeNode resLinkPoolNode;
    private JDFFrame parFrame;
    private TreePath path = null;
    private TreePath resPath = null; 
    private TreePath resLinkPath = null; 
    private boolean hasResourcePool;
    private boolean hasResourceLinkPool;
    private boolean resSuccess = true;
    private boolean resLinkSuccess = true;
    
    public InsertResourceAndLinkEdit(final JDFFrame parent, 
                                     final JDFTreeNode _node, 
                                     boolean _hasResourcePool, boolean _hasResourceLinkPool, 
                                     final JDFTreeNode _resNode,	 final JDFTreeNode _resLinkNode) 
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

    public void redo() throws CannotRedoException 
    {
        final JDFTreeModel model = parFrame.getModel();
        JDFResource res = (JDFResource) resNode.getElement();
        JDFResourcePool resPool = (JDFResourcePool) resPoolNode.getElement();
        if (!hasResourcePool)
        {
            JDFNode jdfNode=(JDFNode)node.getElement();
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
                JDFNode jdfNode=(JDFNode)node.getElement();
                model.insertInto(resLinkPoolNode, node, -1);
                jdfNode.appendChild(resLinkPool);
            }
            model.insertInto(resLinkNode, resLinkPoolNode, -1);
            resLinkPool.appendChild(resLink);
            parFrame.updateViews(resLinkPath);
        }

        parFrame.updateViews(resPath);
    }

    public boolean canUndo() 
    {
        return resSuccess && resLinkSuccess;
    }

    public boolean canRedo() 
    {
        return resSuccess && resLinkSuccess;
    }
    
    public String getPresentationName() 
    {
        return "Insert Resource";
    }

}