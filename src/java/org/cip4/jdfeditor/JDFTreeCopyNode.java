package org.cip4.jdfeditor;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;

/**
 * class to handle copy / paste actions from the frame
 * @author prosirai
 *
 */
public class JDFTreeCopyNode 
{

    private JDFTreeNode treeNode;
    private boolean m_isPastedBefore;
    public JDFTreeCopyNode(JDFTreeNode _treeNode, boolean bExists)
    {
        treeNode=_treeNode;
        m_isPastedBefore = bExists; // TODO fix id handling for doc to doc copy
        // TODO ensure that no nirvana links are created when copying resource links
    }
    /**
     * Method getChildrenForCopiedNode.
     * checks if the copied node has any children and inserts them into the
     * m_jdfTree
     * @param newChild
     * @param newNode
     */
    private void getChildrenForCopiedNode(JDFTreeNode newNode)
    {
        KElement newChild=newNode.getElement();
        final int pos = newNode.getChildCount();

        final VElement children = newChild.getChildElementVector(null, null, null, true, 0,false);

        final JDFTreeModel model = Editor.getModel();
        for (int i = 0; i < children.size(); i++)
        {
            final KElement childElm = children.item(i);
            if (childElm.hasAttribute("ID") && m_isPastedBefore)
                childElm.setAttribute("ID", "E"+JDFElement.uniqueID(0));

            final JDFTreeNode childN = model.createNewNode(childElm);
            model.insertNodeInto(childN, newNode, pos + i);

            getChildrenForCopiedNode(childN);
            if(Editor.getIniFile().getAutoVal())
                model.validate();
        }
    }
    /**
     * Method pasteNode.
     * inserts a copied or cutted node into the m_jdfTree and jdfDoc
     * @param path
     */
    public JDFTreeNode pasteNode(TreePath path)
    {
        JDFTreeNode parentNode = (JDFTreeNode) path.getLastPathComponent();
        JDFTreeNode newNode = null;
        KElement parentElement = parentNode.getElement();
        final JDFFrame m_frame=Editor.getFrame();
        final JDFTreeModel model = m_frame.getModel();
        if (treeNode.isElement())
        {
            final KElement newChild =  treeNode.getElement();
            try
            {
                final KElement copiedElement = parentElement.copyElement(newChild, null);

                if (copiedElement.hasAttribute("ID") && m_isPastedBefore)
                    copiedElement.setAttribute("ID", "E"+JDFElement.uniqueID(0));
                else if (!m_isPastedBefore)
                    m_isPastedBefore = true;

                newNode = model.createNewNode(copiedElement);
                model.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

                getChildrenForCopiedNode(newNode);
                if(Editor.getIniFile().getAutoVal())
                    model.validate();
            }
            catch (Exception s)
            {
                s.printStackTrace();
                JOptionPane.showMessageDialog(m_frame, 
                        m_frame.m_littleBundle.getString("NodeInsertErrorKey"), m_frame.m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            final String attributeName = treeNode.getName();
            final String attributeValue = treeNode.getValue();
            newNode = model.setAttribute(parentNode, attributeName, attributeValue, null,false);
        }
        return newNode;
    }
}
