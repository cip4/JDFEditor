package org.cip4.jdfeditor;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.util.StringUtil;

/**
 * class to handle copy / paste actions from the frame
 * @author prosirai
 *
 */
public class JDFTreeCopyNode 
{

    private JDFTreeNode treeNode;
    private boolean m_isPastedBefore;
    private JDFFrame m_frame;
    public JDFTreeCopyNode(JDFTreeNode _treeNode, JDFFrame frame)
    {
        treeNode=_treeNode;
        m_frame=frame;
        m_isPastedBefore = false; // TODO fix id handling for doc to doc copy
        // TODO ensure that no nirvana links are created when copying resource links
    }
    /**
     * Method getChildrenForCopiedNode.
     * checks if the copied node has any children and inserts them into the
     * m_jdfTree
     * @param newChild
     * @param newNode
     */
    public void getChildrenForCopiedNode(JDFTreeNode newNode)
    {
        KElement newChild=newNode.getElement();
        final int pos = newChild.getAttributeVector().size();
        
        final VElement children = newChild.getChildElementVector(null, null, null, true, 0,false);
        
        for (int i = 0; i < children.size(); i++)
        {
            final KElement childElm = children.item(i);
            
            if (childElm.hasAttribute("ID") && m_isPastedBefore)
                childElm.setAttribute("ID", "E"+JDFElement.uniqueID(0));
            
            final JDFTreeNode childN = m_frame.getModel().createNewNode(childElm);
            m_frame.getModel().insertNodeInto(childN, newNode, pos + i);
            
            getChildrenForCopiedNode(childN);
            if(Editor.getIniFile().getAutoVal())
                m_frame.getModel().validate();
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
                
                newNode = m_frame.getModel().createNewNode(copiedElement);
                m_frame.getModel().insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                
                getChildrenForCopiedNode(newNode);
                if(Editor.getIniFile().getAutoVal())
                    m_frame.getModel().validate();
            }
            catch (Exception s)
            {
                s.printStackTrace();
                JOptionPane.showMessageDialog(m_frame, 
                        m_frame.m_littleBundle.getString("NodeInsertErrorKey"), m_frame.m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
               newNode = null;
            }
      }
        else
        {
            final String attributeName = StringUtil.token(m_frame.m_copyNode.toString(), 0, "=");
            String attributeValue = StringUtil.token(m_frame.m_copyNode.toString(), 1, "=");
            if (attributeValue.indexOf("\"") != -1)
            {
                attributeValue = attributeValue.substring(1,attributeValue.length()-1);
            }
            if (parentElement.hasAttribute(attributeName))
            {
                JOptionPane.showMessageDialog(m_frame, 
                        m_frame.m_littleBundle.getString("AttributeInsertErrorKey"), m_frame.m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
                newNode = null;
            }
            else
            {
                JDFTreeNode parent=(JDFTreeNode) path.getLastPathComponent();
                newNode = m_frame.getModel().setAttribute(parent, attributeName, attributeValue, null,false);
            }
        }
        return newNode;
    }
}
