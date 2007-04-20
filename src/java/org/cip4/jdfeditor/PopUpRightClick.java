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
package org.cip4.jdfeditor;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import org.cip4.jdfeditor.extensions.XJDF20;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.resource.devicecapability.JDFDeviceCap;

/**
 * Class to implement all the menu bar and menu related stuff
 * moved here from JDFFrame
 * @author prosirai
 *
 */
public class PopUpRightClick extends JPopupMenu implements ActionListener
{
    /**
     * 
     */
    private static final long serialVersionUID = -8488973695389593826L;

    private JMenuItem m_copyPopupItem;
    private JMenuItem m_renamePopupItem;
    private JMenuItem m_modifyAttrValuePopupItem;
    private JMenuItem m_requiredAttrPopupItem;
    private JMenuItem m_requiredElemPopupItem;
    private JMenuItem m_xpandPopupItem;
    private JMenuItem m_collapsePopupItem;
    private JMenuItem m_copyToClipBoardPopupItem;
    private JMenuItem m_insertElemBeforePopupItem;
    private JMenuItem m_insertElemAfterPopupItem;
    private JMenuItem m_insertElemIntoPopupItem;
    private JMenuItem m_insertInResLinkPopupItem;
    private JMenuItem m_insertOutResLinkPopupItem;
    private JMenuItem m_insertInResPopupItem;
    private JMenuItem m_insertOutResPopupItem;
    private JMenuItem m_insertResPopupItem;
    private JMenuItem m_insertAttrPopupItem;
    private JMenuItem m_cutPopupItem;
    private JMenuItem m_pastePopupItem;
    private JMenuItem m_deletePopupItem;
    private JMenuItem m_targetItem;
    private JMenuItem m_saveXJDF=null;
    private JMenuItem m_nodeFromCaps=null;
    private JMenuItem m_saveXJDFCaps=null;

    /**
     * Creates the popupmenu after a right mouse click on node in the Tree View.   
     * @param path - The path to the clicked node
     * @return A JPopupMenu
     */
    public PopUpRightClick(TreePath path)
    {
        super();
        final JSeparator separator = new JSeparator();

        
        final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
        final KElement elem = (node.isElement()) ? node.getElement() : null;
        final ResourceBundle m_littleBundle=Editor.getBundle();
        final INIReader ini=Editor.getIniFile();
        
        final JMenu insertPopupMenu = new JMenu(m_littleBundle.getString("InsertElKey"));
        insertPopupMenu.setEnabled(elem != null);

        JMenuItem xpath=new JMenuItem(node.getXPath());
        xpath.setBackground(Color.YELLOW);
        add(xpath);
        add(separator);
        
        m_insertElemBeforePopupItem = new JMenuItem(m_littleBundle.getString("BeforeKey"));
        m_insertElemBeforePopupItem.addActionListener(this);
        insertPopupMenu.add(m_insertElemBeforePopupItem);

        m_insertElemIntoPopupItem = new JMenuItem(m_littleBundle.getString("IntoKey"));
        m_insertElemIntoPopupItem.addActionListener(this);
        insertPopupMenu.add(m_insertElemIntoPopupItem);

        m_insertElemAfterPopupItem = new JMenuItem(m_littleBundle.getString("AfterKey"));
        m_insertElemAfterPopupItem.addActionListener(this);
        insertPopupMenu.add(m_insertElemAfterPopupItem);

        add(insertPopupMenu);

        final JMenu resMenu = new JMenu(m_littleBundle.getString("InsertResKey"));
        resMenu.setEnabled(elem != null && (elem instanceof JDFNode
                || elem.getNodeName().equals("ResourcePool")));

        m_insertInResPopupItem = new JMenuItem(m_littleBundle.getString("InputResourceKey"));
        m_insertInResPopupItem.addActionListener(this);
        resMenu.add(m_insertInResPopupItem);

        m_insertOutResPopupItem = new JMenuItem(m_littleBundle.getString("OutputResourceKey"));
        m_insertOutResPopupItem.addActionListener(this);
        resMenu.add(m_insertOutResPopupItem);

        resMenu.add(separator);

        m_insertResPopupItem = new JMenuItem(m_littleBundle.getString("ResourceKey"));
        m_insertResPopupItem.addActionListener(this);
        resMenu.add(m_insertResPopupItem);

        add(resMenu);

        final JMenu resLinkMenu = new JMenu(m_littleBundle.getString("InsertResLinkKey"));
        resLinkMenu.setEnabled(elem != null && (elem instanceof JDFNode
                || elem.getNodeName().equals("ResourceLinkPool")));

        m_insertInResLinkPopupItem = new JMenuItem(m_littleBundle.getString("ResourceInLinkKey"));
        m_insertInResLinkPopupItem.addActionListener(this);
        resLinkMenu.add(m_insertInResLinkPopupItem);

        m_insertOutResLinkPopupItem = new JMenuItem(m_littleBundle.getString("ResourceOutLinkKey"));
        m_insertOutResLinkPopupItem.addActionListener(this);
        resLinkMenu.add(m_insertOutResLinkPopupItem);

        add(resLinkMenu);

        m_insertAttrPopupItem = new JMenuItem(m_littleBundle.getString("InsertAttKey"));
        m_insertAttrPopupItem.addActionListener(this);
        add(m_insertAttrPopupItem);
        add(separator);

        m_cutPopupItem = new JMenuItem(m_littleBundle.getString("CutKey"));
//      m_cutPopupItem.setAccelerator(KeyStroke.getKeyStroke('X', menuKeyMask));
        m_cutPopupItem.addActionListener(this);
        add(m_cutPopupItem);

        m_copyPopupItem = new JMenuItem(m_littleBundle.getString("CopyKey"));
//      m_copyPopupItem.setAccelerator(KeyStroke.getKeyStroke('C', menuKeyMask));
        m_copyPopupItem.addActionListener(this);
        add(m_copyPopupItem);

        m_pastePopupItem = new JMenuItem(m_littleBundle.getString("PasteKey"));
//      m_pastePopupItem.setAccelerator(KeyStroke.getKeyStroke('P', menuKeyMask));
        m_pastePopupItem.addActionListener(this);
        add(m_pastePopupItem);

        m_deletePopupItem = new JMenuItem(m_littleBundle.getString("DeleteKey"));
        m_deletePopupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        m_deletePopupItem.addActionListener(this);
        add(m_deletePopupItem);

        add(separator);

        m_renamePopupItem = new JMenuItem(m_littleBundle.getString("RenameKey"));
        m_renamePopupItem.addActionListener(this);
        add(m_renamePopupItem);

        m_modifyAttrValuePopupItem = new JMenuItem(m_littleBundle.getString("ModifyAttValueKey"));
        m_modifyAttrValuePopupItem.addActionListener(this);
        add(m_modifyAttrValuePopupItem);

        m_targetItem = new JMenuItem(m_littleBundle.getString("GotoTargetKey"));
        m_targetItem.addActionListener(this);
        add(m_targetItem);
        
        add(separator);

        m_requiredAttrPopupItem = new JMenuItem(m_littleBundle.getString("AddRequiredAttKey"));
        m_requiredAttrPopupItem.addActionListener(this);
        add(m_requiredAttrPopupItem);

        m_requiredElemPopupItem = new JMenuItem(m_littleBundle.getString("AddRequiredElKey"));
        m_requiredElemPopupItem.addActionListener(this);
        add(m_requiredElemPopupItem);
        add(separator);

        // TODO add spawn
        if(elem instanceof JDFNode)
        {
            if(ini.getEnableExtensions())
            {
                m_saveXJDF=new JMenuItem(m_littleBundle.getString("SaveXJFDKey"));
                m_saveXJDF.addActionListener(this);
                add(m_saveXJDF);
            }
            add(separator);
        }
        else if(elem instanceof JDFDeviceCap)
        {
            m_nodeFromCaps=new JMenuItem(m_littleBundle.getString("NodeFromCapsKey"));
            m_nodeFromCaps.addActionListener(this);
            add(m_nodeFromCaps);
            add(separator);
        }
        else if(elem!=null && elem.getNodeName().equals(XJDF20.rootName))
        {
            m_saveXJDFCaps=new JMenuItem(m_littleBundle.getString("ExportToDevCapKey"));
            m_saveXJDFCaps.addActionListener(this);
            add(m_saveXJDFCaps);
        }

        m_xpandPopupItem = new JMenuItem(m_littleBundle.getString("ExpandKey"));
        m_xpandPopupItem.addActionListener(this);
        add(m_xpandPopupItem);

        m_collapsePopupItem = new JMenuItem(m_littleBundle.getString("CollapseKey"));
        m_collapsePopupItem.addActionListener(this);
        add(m_collapsePopupItem);

        //20040913 MRE
        m_copyToClipBoardPopupItem = new JMenuItem(m_littleBundle.getString("CopyNode"));
        m_copyToClipBoardPopupItem.addActionListener(this);
        add(m_copyToClipBoardPopupItem);

        setEnabledInMouseMenu(node, elem);
    }

    /**
     * Disables the MenuItems in the MouseMenu that isn't selectable for the selected
     * JDFTreeNode.
     * @param node - The selected node
     * @param elem - The KElement for the selected node, can be null
     */
    private void setEnabledInMouseMenu(JDFTreeNode node, KElement elem)
    {
        JDFFrame m_frame=Editor.getFrame();
        final boolean isElement = node.isElement();
        if (isElement && elem != null)
        {
            if (!((JDFTreeNode) m_frame.getRootNode().getFirstChild()).equals(node))
            {
                m_insertElemBeforePopupItem.setEnabled(true);
                m_insertElemAfterPopupItem.setEnabled(true);

                if (elem.getTagName().equals("Comment"))
                    m_insertElemIntoPopupItem.setEnabled(false);
                else
                    m_insertElemIntoPopupItem.setEnabled(true);
            }
            else
            {
                m_insertElemBeforePopupItem.setEnabled(false);
                m_insertElemAfterPopupItem.setEnabled(false);
                m_insertElemIntoPopupItem.setEnabled(true);
            }

            final boolean bMayContainResources = elem instanceof JDFNode || elem.getNodeName().equals("ResourcePool");

            m_insertInResPopupItem.setEnabled(bMayContainResources);
            m_insertOutResPopupItem.setEnabled(bMayContainResources);
            m_insertResPopupItem.setEnabled(bMayContainResources);

            final boolean bNodeOK = (elem instanceof JDFNode && 
                    EditorUtils.getResourcesAllowedToLink((JDFNode) elem,null)!=null);

            boolean bResLinkPoolOK = (elem instanceof JDFResourceLinkPool);
            if(bResLinkPoolOK)
            {
                bResLinkPoolOK=EditorUtils.getResourcesAllowedToLink(((JDFResourceLinkPool)elem).getParentJDF(),null).size() != 0;
            }

            m_insertInResLinkPopupItem.setEnabled(bNodeOK || bResLinkPoolOK);
            m_insertOutResLinkPopupItem.setEnabled(bNodeOK || bResLinkPoolOK);

            boolean bHasChildNodes = elem.hasChildElements() || elem.hasAttributes();

            m_xpandPopupItem.setEnabled(bHasChildNodes);
            m_collapsePopupItem.setEnabled(bHasChildNodes);

            m_requiredAttrPopupItem.setEnabled(true);
            m_requiredElemPopupItem.setEnabled(true);
            m_modifyAttrValuePopupItem.setEnabled(false);
        }
        else
        {
            m_requiredAttrPopupItem.setEnabled(false);
            m_requiredElemPopupItem.setEnabled(false);
            m_modifyAttrValuePopupItem.setEnabled(true);
            m_xpandPopupItem.setEnabled(false);
            m_collapsePopupItem.setEnabled(false);
            if(!isElement)
            {
                // attribute
                m_targetItem.setEnabled(node.getName().toLowerCase().endsWith("ref"));
            }
        }

        m_pastePopupItem.setEnabled(m_frame.m_copyNode != null && elem!=null);
        final Object parent = ((JDFTreeNode) node.getParent()).getUserObject();
        m_renamePopupItem.setEnabled(parent!=null);

    }

    public void actionPerformed(ActionEvent e)
    {
        Editor.setCursor(1,null);

        final Object eSrc = e.getSource();
        final INIReader iniFile = Editor.getIniFile();
        final JDFFrame frame = Editor.getFrame();
        final JDFTreeArea ta=frame.m_treeArea;

        if (!iniFile.getReadOnly())
        {
            if (eSrc == m_cutPopupItem)
            {
                frame.cutSelectedNode();
            }
            else if (eSrc == m_copyPopupItem )
            {
                frame.copySelectedNode();
            }  
            else if (eSrc == m_insertElemBeforePopupItem)
            {
                ta.insertElementAtSelectedNode(-1);    
            }
            else if (eSrc == m_insertElemAfterPopupItem)
            {
                ta.insertElementAtSelectedNode(1);
            }
            else if (eSrc == m_insertElemIntoPopupItem)
            {
                ta.insertElementAtSelectedNode(0);
            }
            else if (eSrc == m_insertInResPopupItem)
            {
                ta.insertResourceWithLink(true, true);
            }
            else if (eSrc == m_insertOutResPopupItem)
            {
                ta.insertResourceWithLink(true, false);
            }
            else if (eSrc == m_insertResPopupItem)
            {
                ta.insertResourceWithLink(false, false);
            }
            else if (eSrc == m_insertInResLinkPopupItem)
            {
                ta.insertResourceLink(EnumUsage.Input);
            }
            else if (eSrc == m_insertOutResLinkPopupItem)
            {
                ta.insertResourceLink(EnumUsage.Output);
            }
            else if (eSrc == m_insertAttrPopupItem)
            {
                ta.insertAttrItem();
            }
            else if (eSrc == m_renamePopupItem)
            {
                frame.renameSelectedNode();
            }
            else if (eSrc == m_modifyAttrValuePopupItem)
            {
                ta.modifyAttribute();
            }
            else if (eSrc == m_requiredAttrPopupItem)
            {
                frame.addRequiredAttrsToSelectedNode();
            }
            else if (eSrc == m_requiredElemPopupItem)
            {
                frame.addRequiredElemsToSelectedNode();
            }       
            else if (eSrc == m_pastePopupItem)
            {
                frame.pasteCopiedNode();
            }
            else if (eSrc == m_deletePopupItem)
            {
                Editor.getModel().deleteSelectedNodes();
            }
        }
        if (eSrc == m_xpandPopupItem)
        {
            ta.xpand(null);
        }
        else if (eSrc == m_collapsePopupItem)
        {
            ta.collapse(null); 
        }
        else if (eSrc == m_copyToClipBoardPopupItem)
        {
            copyToClipBoard(ta.getSelectionPath()); 
        }
        else if (eSrc == m_targetItem)
        {
            ta.getPathTarget();
        }
        else if(eSrc == m_saveXJDF)
        {
            Editor.getModel().saveAsXJDF(ta.getSelectionPath());
        }
        else if(eSrc == m_saveXJDFCaps)
        {
            Editor.getModel().saveAsXJDFCaps(ta.getSelectionPath());
        }
        else if(eSrc == m_nodeFromCaps)
        {
            Editor.getModel().createNodeFromCaps(ta.getSelectionPath());
        }
        Editor.setCursor(0,null);

    }


    /**
     * copies the content of the marked node to the system clip board 
     * @param p - The TreePath to collapse
     */
    private void copyToClipBoard(TreePath p)
    {
        final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
        final Enumeration e = node.postorderEnumeration();

        while (e.hasMoreElements())
        {
            final JDFTreeNode treeNode = (JDFTreeNode) e.nextElement();

            if(treeNode.getUserObject()!=null && treeNode.isElement())
            {    
                final KElement elem = treeNode.getElement();

                //Copy XML representation of the selected node to clip board
                final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                final StringSelection contents = new StringSelection(elem.toXML());
                cb.setContents(contents, null);
            }    
        }
    }    
}
