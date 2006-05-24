package org.cip4.jdfeditor;
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFRefElement;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFPart;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFDevCaps;

/**
 * 
 * @author prosirai
 * This is a new dump for some of the JDFFrame classes that relate to the actual tree view
 * TODO move some of the routines here into the model where they belong and reduce the dependencies with JDFFrame
 */
public class JDFTreeArea extends JTextArea
{ 
    TreeSelectionListener m_treeSelectionListener;
    private static final long serialVersionUID = 2036935468347224324L;
    private JScrollPane m_treeScroll;
    public JViewport m_treeView;
    private JDFTreeRenderer m_renderer;
    public JDFFrame m_frame;
    private ResourceBundle m_littleBundle;
   
    
    public JDFTreeArea(ResourceBundle bundle, JDFFrame frame)
    {
        super();
        m_littleBundle=bundle;
        m_frame=frame;
        setEditable(false);
        m_treeScroll = new JScrollPane();
        m_treeScroll.getVerticalScrollBar().setUnitIncrement(20);
        m_treeScroll.getHorizontalScrollBar().setUnitIncrement(20);
        
        final JLabel treeLabel = new JLabel(" " + bundle.getString("TreeViewKey"));
        treeLabel.setPreferredSize(new Dimension(treeLabel.getPreferredSize().width, 23));
        treeLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        treeLabel.setToolTipText(bundle.getString("TreeViewKey"));
        treeLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        
        m_treeView = new JViewport();
        
        m_treeView.setView(this);
        m_treeScroll.setViewport(m_treeView);
        m_treeScroll.setColumnHeaderView(treeLabel);        
    }
    
    /**
     * deletes selected Node
     */
    public void deleteSelectedNode() 
    {
        final TreePath path = getSelectionPath();
        if (path != null)
        {
            final DeleteItemEdit edit = new DeleteItemEdit(m_frame, path);
            m_frame.undoSupport.postEdit( edit );
        }
    }
    
    
    /**
     * Expands the TreePath and all of its subelements.
     * @param p - The TreePath to expand
     */
    public void xpand(TreePath p)       
    {
        if(p==null)
            p=getSelectionPath();
        final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
        getJDFTree().expandPath(p);
        final Enumeration e = node.preorderEnumeration();
        
        while (e.hasMoreElements())
        {
            final JDFTreeNode treeNode = (JDFTreeNode) e.nextElement();
            
            if (treeNode.isElement())
                getJDFTree().expandPath(new TreePath(treeNode.getPath()));
        }
    }
    
    
    /**
     * Collapses the TreePath and all of its subelements. 
     * @param p - The TreePath to collapse
     */
    public void collapse(TreePath p)
    {
        if(p==null)
            p=getSelectionPath();
        
        final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
        final Enumeration e = node.postorderEnumeration();
        
        while (e.hasMoreElements())
        {
            final JDFTreeNode treeNode = (JDFTreeNode) e.nextElement();
            
            if (treeNode.isElement())
                getJDFTree().collapsePath(new TreePath(treeNode.getPath()));
        }
        getJDFTree().collapsePath(p);
    }
    
    
    public boolean jdfTreeIsNull()
    {
        if (getJDFTree() == null || m_treeView.getComponent(0).getClass().equals(JTextArea.class))
            return true;
        
        return false;
    }
    
    public JScrollPane getScrollPane()
    {
        return m_treeScroll;
    }
    
    /**
     * Method drawTreeView.
     * @param doc
     */
    public void drawTreeView(EditorDocument eDoc)
    {
        JDFDoc doc=eDoc.getJDFDoc();
        // TODO create a root that is not a null element!
        JDFTreeNode root = new JDFTreeNode();
        m_frame.createModel(doc,root);
        eDoc.setJDFTree(new JTree());
        final JTree jdfTree = getJDFTree();
        jdfTree.setModel(eDoc.getModel());
        
        final PopupListener popupListener = new PopupListener();
        jdfTree.addMouseListener(popupListener);
        
        jdfTree.setRootVisible(false);
        jdfTree.setEditable(false);
        jdfTree.setExpandsSelectedPaths(true);
        jdfTree.putClientProperty("JTree.lineStyle", "Angled");
        ToolTipManager.sharedInstance().registerComponent(jdfTree);
        m_renderer = new JDFTreeRenderer();
        jdfTree.setCellRenderer(m_renderer);
        jdfTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        m_treeSelectionListener = m_frame.getTreeSelectionListener();
        jdfTree.addTreeSelectionListener(m_treeSelectionListener);
        jdfTree.setRowHeight(18);
        
        jdfTree.expandPath(new TreePath(((JDFTreeNode) root.getFirstChild()).getPath()));
        new DropTarget(jdfTree, m_frame);
        m_treeView.setView(jdfTree);
        jdfTree.setBackground(Color.white);
        jdfTree.setShowsRootHandles(true);
    }
      
    
    
    /**
     * Sets focus on the correct node in the Tree View from a selected node in
     * the In & Output View. Called from method findNode().
     * @param p - The path which you want to show
     */
    public void goToPath(TreePath p)
    {
        if(p==null) 
            return;
        final JTree jdfTree = getJDFTree();
        jdfTree.makeVisible(p);
        jdfTree.removeTreeSelectionListener(m_treeSelectionListener);
        final int row = jdfTree.getRowForPath(p);
        jdfTree.setSelectionRow(row);
        jdfTree.scrollRowToVisible(row);
        JDFTreeNode node=(JDFTreeNode) p.getLastPathComponent();
        m_frame.m_errorTabbedPane.selectNodeWithXPath(new TreePath(node.getPath()));
        jdfTree.addTreeSelectionListener(m_treeSelectionListener);
        //        m_frame.m_errorTabbedPane.goToPath(p);
    }
    
    
    
    
    
    class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            EditorDocument ed=m_frame.getEditorDoc();
            if(ed==null)
                return;
            
            final JTree jdfTree = ed.getJDFTree();
            final TreePath path = jdfTree.getPathForLocation(e.getX(), e.getY());
            
            if ((SwingUtilities.isRightMouseButton(e) || e.isControlDown()) && path != null && !Editor.getIniFile().getReadOnly())
            {
                jdfTree.removeTreeSelectionListener(m_treeSelectionListener);
                ed.setSelectionPath(path,true);
                
                final JPopupMenu rightMenu = m_frame.m_menuBar.drawMouseMenu(path);
                final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                
                final Point pTree = e.getComponent().getLocationOnScreen();
                final Dimension d2 = rightMenu.getPreferredSize();
                
                final int x = (int) pTree.getX() + e.getX();
                final int y = (int) pTree.getY() + e.getY();
                final int x2 = d2.width;
                final int y2 = d2.height;
                int xStart = e.getX();
                int yStart = e.getY();
                
                if ((x + x2) > d.getWidth())
                    xStart = xStart - x2;
                
                if ((y + y2) > d.getHeight())
                    yStart = yStart - y2;
                
                rightMenu.show(e.getComponent(), xStart, yStart);
                jdfTree.addTreeSelectionListener(m_treeSelectionListener);
            }
        }
        
        public void mouseClicked(MouseEvent event)
        {          
            EditorDocument ed=m_frame.getEditorDoc();
            if(ed==null)
                return;
            
            final TreePath path = ed.getSelectionPath();
            if(path==null)
                return;
            final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
            
            KElement kElement = node.getElement();
            if (node.isElement())
            {
                try
                {
                    final JDFInOutScroll inOutScroll = m_frame.m_topTabs.m_inOutScrollPane;
                    if (event.getClickCount() == 2)
                    {
                        if ((kElement instanceof JDFResourceLink) || (kElement instanceof JDFRefElement))
                        {
                            kElement = getLinkTarget(kElement);                            
                        }
                        
                        if (kElement instanceof JDFPart)
                        {
                           kElement = getLinkTarget(kElement);                            
                        }
                        
                        if (kElement instanceof JDFDevCaps && kElement.hasAttribute(AttributeName.DEVCAPREF))
                        {
                            kElement = kElement.getTarget_KElement(
                                    kElement.getAttribute(AttributeName.DEVCAPREF), AttributeName.ID);
                        }
                        if (kElement instanceof JDFDevCap && kElement.hasAttribute(AttributeName.DEVCAPREFS))
                        {
                            kElement = kElement.getTarget_KElement(
                                    kElement.getAttribute(AttributeName.DEVCAPREFS), AttributeName.ID);
                        }
                    }
                    findNode(new JDFTreeNode(kElement));
                    inOutScroll.clearInOutView();
                    inOutScroll.initInOutView();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (event.getClickCount() == 2)
            {                    
                modifyAttribute();
                goToPath(path);
            }
            else
            {
                goToPath(path);                
            }
        }

        private KElement getLinkTarget(final KElement kElement)
        {
            KElement target=null;
            if(kElement instanceof JDFResourceLink)
            {
                target = ((JDFResourceLink) kElement).getTarget();
                if(target==null)
                    target=((JDFResourceLink) kElement).getLinkRoot();
            }
            else if(kElement instanceof JDFRefElement)
            {
                target = ((JDFRefElement) kElement).getTarget();
                if(target==null)
                    target=((JDFRefElement) kElement).getTargetRoot();
            }
            else if(kElement instanceof JDFPart)
            {
                KElement parentTarget=getLinkTarget(kElement.getParentNode_KElement());
                if(parentTarget instanceof JDFResource)
                {
                    JDFResource r=(JDFResource)parentTarget;
                    JDFPart part=(JDFPart) kElement;
                    target=r.getPartition(part.getPartMap(),null);
                    if(target==null) // at least go to root if it points to a missing partition
                        target=r;
                }
            }
            return target;
        }
    }
    
    protected void modifyAttribute()
    {
        final TreePath path = m_frame.getEditorDoc().getSelectionPath();
        if (path != null)
        {
            JDFTreeNode attrNode = (JDFTreeNode) path.getLastPathComponent();
            final String oldVal = attrNode.getValue();
            final String attributeName = attrNode.getName();
            if (!attrNode.isElement())
            {
//                if (attrNode.isInherited())
//                {
//                    JOptionPane.showMessageDialog(this, 
//                            "The attribute "+attributeName+" is inherited.\nOnly resource root can be modified", 
//                            "Error", JOptionPane.ERROR_MESSAGE);
//                }
//                else 
//                {
                    final KElement element = attrNode.getElement();
                    
                    VString vValues = element.getNamesVector(attributeName);                
                    String selectedValue = null;
                    
                    if (vValues!=null && !vValues.isEmpty())
                    {
                        if (vValues.contains("Unknown"))
                        {
                            vValues.removeElement("Unknown");
                        }
                        vValues.sort();
                        vValues.add("Other..");
                        
                        selectedValue = (String) JOptionPane.showInputDialog(
                                this, m_littleBundle.getString("ChooseAttValueKey"), 
                                m_littleBundle.getString("ModifyAttValueKey"),
                                JOptionPane.QUESTION_MESSAGE, null, 
                                vValues.toArray(), vValues.elementAt(0));
                    }
                    else
                    {    
                        selectedValue = JOptionPane.showInputDialog(this,
                                m_littleBundle.getString("InsertNewAttValueKey"), oldVal);
                    }
                    if (selectedValue != null && selectedValue.equals("Other.."))
                    {    
                        selectedValue = JOptionPane.showInputDialog(this,
                                m_littleBundle.getString("InsertNewAttValueKey"), oldVal);
                    }
                    if ((selectedValue != null)&&!(selectedValue.equals(oldVal)))
                    {
                        getModel().setAttribute((JDFTreeNode)attrNode.getParent(),attributeName,selectedValue,null,false);
                    }
//                }
            }
            
            if (attrNode != null)
            {
                final ModifyAttrEdit edit = new ModifyAttrEdit(m_frame, path, 
                        attrNode, oldVal);
                m_frame.undoSupport.postEdit( edit );
            }            
        }
    }
    /**
     * inserts element before selected node
     */
    public void insertElementBeforeSelectedNode() 
    {
        final TreePath path = m_frame.getEditorDoc().getSelectionPath();
        if (path != null)
        {
            final JDFTreeNode beforeNode = (JDFTreeNode) path.getLastPathComponent();            
            JDFTreeModel model = getModel();
            
            final KElement parentElement = model.getParentElement(path);
            final String insertElementName = chooseElementName(parentElement);
            
            JDFTreeNode newNode = null;
            if (insertElementName!=null && !insertElementName.equals(JDFConstants.EMPTYSTRING))                            
            {
                final KElement siblingElement =model.getSiblingElement(path);
                KElement newElement =  parentElement.insertBefore(insertElementName, 
                        siblingElement,  parentElement.getNamespaceURI());
                newNode = model.createNewNode(newElement);
                
                model.addRequiredAttributes(newNode);
                model.addRequiredElements(newNode);
                model.insertBefore(beforeNode, newNode);
                
                m_frame.updateViews(new TreePath(newNode.getPath()));
                if(Editor.getIniFile().getAutoVal())
                    model.validate();
                final InsertElementEdit edit = 
                    new InsertElementEdit(m_frame, beforeNode, newNode,"Insert before");
                m_frame.undoSupport.postEdit( edit );
            }
        }
    }
    
    /**
     * Find the node in the m_jdfTree after clicking in the validation list
     * @param path - path to the node you want to find
     */
    public void findInNode(final String path)
    {
        if (path != null && !path.equals(JDFConstants.EMPTYSTRING))
        {
            final JDFTreeNode theRoot = (JDFTreeNode) getModel().getRootNode().getFirstChild();
            
            if (path.equals(theRoot.getXPath()))
            {
                goToPath(new TreePath(theRoot.getPath()));
                return;
            }
            final Enumeration e = theRoot.depthFirstEnumeration();
            while (e.hasMoreElements())
            {
                final JDFTreeNode node = (JDFTreeNode) e.nextElement();                
                if (path.equals(node.getXPath()))
                {
                    goToPath(new TreePath(node.getPath()));
                    return;
                }
            }
        }
        
        // nothing found or null, clear it
        getJDFTree().clearSelection();
    }
    
    
    
    /**
     * Method chooseElementToInsert.
     * gets the valid element to insert into the parentElement
     * @param parentElement - 
     * @return the name of the selected element to insert
     */
    public String chooseElementName(KElement parentElement)
    {
        
        final VString validElementsVector = parentElement.knownElements();
        final VString uniqueElementsVector  = parentElement.uniqueElements();        
        
        final VString existingElementsVector = parentElement.getElementNameVector(); 
        for (int i = 0; i < existingElementsVector.size(); i++)
        {
            final String existingElementName = existingElementsVector.stringAt(i);
            if (uniqueElementsVector.contains(existingElementName))
            {
                // if element is unique and already in a parentElement - remove it from a valid list
                validElementsVector.remove(existingElementName);
            }
        }
        final int size = validElementsVector.size();
        final String validValues[] = new String[size + 1];
        for (int i = 0; i < size; i++)
        {
            validValues[i] = validElementsVector.get(i).toString();
        }
        validValues[size] = "zzzzzz";
        Arrays.sort(validValues);
        validValues[size] = "Other..";
        
        String selectedElementName = (String) JOptionPane.showInputDialog(this, "Choose an element to insert",
                "Insert new element", JOptionPane.PLAIN_MESSAGE, null, validValues, validValues[0]);
        
        if (selectedElementName != null && selectedElementName.equals("Other.."))
        {
            selectedElementName = JOptionPane.showInputDialog(this, "Choose element name", "");
        }
        
        return selectedElementName;
    }    
    
    
    
    
    /**
     * Possiblity for user to choose ResourceName he wants to insert into the parentNode
     * All resource names are the allowed resources for this parentNode + user defined "Other.." choice
     * 
     * @param parentNode - parentNode we want to insert the resource into 
     * @return String - resourceName
     */
    public String chooseResourceName(JDFNode parentNode)
    {
        
        VString vValid = new VString();
        // also allow any valid resource from children
        Vector allNodes= parentNode.getvJDFNode(null,null,false);
        for(int aN=0; aN<allNodes.size(); aN++)
        {
            JDFNode n=(JDFNode)allNodes.elementAt(aN);
            vValid.appendUnique(n.vLinkNames());
        }
        final int size = vValid.size();
        final String validValues[] = new String[size + 1];
        
        for (int i = 0; i < size; i++)
        {
            validValues[i] = vValid.stringAt(i);
        }
        validValues[size] = "zzzzzz";
        Arrays.sort(validValues);
        validValues[size] = "Other..";
        
        String selectedResource = (String) JOptionPane.showInputDialog(this, "Choose a resource to insert",
                "Insert new resource", JOptionPane.PLAIN_MESSAGE, null, validValues, validValues[0]);
        
        if (selectedResource != null && (selectedResource.equals("Other..")||selectedResource.equals("*")))
        {
            selectedResource = JOptionPane.showInputDialog(this, "Insert new resource", "");
        }
        return selectedResource;
    }
    
    
    /**
     * choose Resource to link
     * @param jdfNode - node where ResourceLink must be added
     * @return - selected ResourceName
     */
    public JDFResource chooseResourceToLink(final JDFNode jdfNode)
    {
        JDFResource resource = null;
        final VElement rVector = EditorUtils.getResourcesAllowedToLink(jdfNode, true);
        final String[] resourceName_Id = new String[rVector.size()];
        for (int i = 0; i < rVector.size(); i++)
        {
            final JDFResource res = (JDFResource) rVector.item(i);
            resourceName_Id[i] = res.getNodeName() + ", " + res.getAttribute("ID");
        }
        
        // we may have removed all...
        String selectedResource = null;
        if(resourceName_Id.length>0)
        {
            selectedResource= (String) JOptionPane.showInputDialog(this, 
                    "Choose the resource you want to link",
                    "Insert new resource link", JOptionPane.PLAIN_MESSAGE, 
                    null, resourceName_Id, resourceName_Id[0]);
        }
        
        if (selectedResource != null && !selectedResource.equals(JDFConstants.EMPTYSTRING))
        {
            for (int i = 0; i < resourceName_Id.length && resource==null; i++)
            {
                if (resourceName_Id[i].equals(selectedResource))
                {
                    resource = (JDFResource) rVector.item(i); // __Lena__ falsch if sort resourceName_Id !!!
                }
            }
        }
        return resource;
    }  
    
    
    public JViewport getTreeView()
    {
        return  m_treeView;
    }
    
    
    /**
     * Finds and sets focus on a node in the Tree View when a node is
     * selected in the In & Output View
     * @param searchNode - The Node from the In & Output View which
     * you want to find in the Tree View
     */
    void findNode(JDFTreeNode searchNode)
    {
        final JDFTreeNode theRoot = (JDFTreeNode) getModel().getRootNode().getFirstChild();
        
        boolean bRoot=false;
        final Enumeration e = theRoot.depthFirstEnumeration();
        JDFTreeNode nodeFound=null;
        if(theRoot.equals(searchNode))
        {
            nodeFound=searchNode;
            bRoot=true;
        }
        
        while (nodeFound==null && e.hasMoreElements())
        {
            final JDFTreeNode node = (JDFTreeNode) e.nextElement();
            if(node.equals(searchNode))
            {
                nodeFound=node;
            }
            else if (!node.isElement() && !searchNode.isElement()
                    && node.getParent() != null && searchNode.getParent() != null)
            {
                final KElement nElem = ((JDFTreeNode) node.getParent()).getElement();
                final KElement sElem = ((JDFTreeNode) searchNode.getParent()).getElement();
                if (nElem.equals(sElem))
                {
                    if (searchNode.isInherited())
                    {
                        nodeFound = getModel().getInhNode(node, searchNode);
                    }
                }
            }
        }
        
        if(nodeFound!=null)
        {
            goToPath(new TreePath(nodeFound.getPath()));
            if (m_frame.m_topTabs.getSelectedIndex() == m_frame.m_topTabs.m_PROC_INDEX)
                m_frame.m_buttonBar.m_upOneLevelButton.setEnabled(!bRoot);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    public TreePath getSelectionPath()
    {
        final EditorDocument ed= m_frame.getEditorDoc();
        return ed==null ? null : ed.getSelectionPath();
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Creates a new link, that links to an already existing resource in the m_jdfTree
     * @param input - resource link usage
     */
    public void insertResourceLink(boolean input)
    {
        final TreePath path = getJDFTree().getSelectionPath();
        if (path != null)
        {
            JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
            KElement parent = node.getElement();
            
            while (parent != null && !(parent instanceof JDFNode))
            {
                parent = parent.getParentNode_KElement();
                node = (JDFTreeNode) node.getParent();
            }
            
            if (parent != null && parent instanceof JDFNode)
            {
                final JDFNode jdfNode = (JDFNode) parent;
                final boolean hasResourceLinkPool = jdfNode.hasChildElement("ResourceLinkPool", "");
                
                JDFResource resource = chooseResourceToLink(jdfNode);                
                if (resource != null)
                {
                    //                  linkResource also moves Resource to the closest Ancestor!!! Lena
                    JDFResourceLink resLink = jdfNode.linkResource(resource, input, false);
                    if (resLink == null)     
                    {
                        int force = JOptionPane.showConfirmDialog(m_frame, "The chosen resource was already linked." +
                                "\nDo you want to force linking?", 
                                "Insert ResourceLink", JOptionPane.YES_NO_OPTION);
                        if (force == JOptionPane.YES_OPTION)
                        {
                            resLink = jdfNode.linkResource(resource, input, true);
                        }
                    }
                    if (resLink != null)
                    {
                        final JDFTreeNode resLinkNode = getModel().insertNewResourceLinkNode(jdfNode, node, resLink);
                        if (resLinkNode != null)
                        {
                            final InsertResourceLinkEdit edit = 
                                new InsertResourceLinkEdit(m_frame, jdfNode, node, hasResourceLinkPool, resLink, resLinkNode);
                            m_frame.undoSupport.postEdit( edit );
                        }
                        else 
                        {
                            JOptionPane.showMessageDialog(this, "Insert ResourceLink operation was not completed." +
                                    "\nInternal error occured", "Insert ResourceLink", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else 
                    {
                        JOptionPane.showMessageDialog(this, "Insert ResourceLink operation was aborted",
                                "Insert ResourceLink", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }
    
    /**
     * Method insertResourceWithLink
     * @param withLink - if true insert Resource + Link, false - insert only Resource
     * @param input - resource link usage, true - input, false - output
     */
    public void insertResourceWithLink(boolean withLink, boolean input)
    {
        TreePath path = m_frame.getEditorDoc().getSelectionPath();
        if (path != null)
        {
            JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
            KElement parent = node.getElement();
            
            while (parent != null && !(parent instanceof JDFNode))
            {
                parent = parent.getParentNode_KElement();
                node = (JDFTreeNode) node.getParent();
            }
            
            JDFTreeNode resNode = null;
            JDFTreeNode resLinkNode = null;
            if (parent != null && parent instanceof JDFNode)
            {
                final JDFNode jdfNode = (JDFNode) parent;
                final boolean hasResourcePool = jdfNode.hasChildElement("ResourcePool", "");
                final boolean hasResourceLinkPool = jdfNode.hasChildElement("ResourceLinkPool", "");
                
                final String selectedResource = chooseResourceName(jdfNode);
                
                if (selectedResource != null && !selectedResource.equals(JDFConstants.EMPTYSTRING))
                { 
                    resNode = getModel().insertNewResourceNode(jdfNode, node, selectedResource, 
                            hasResourcePool, withLink, input);
                    if (withLink)
                    {
                        VElement v = jdfNode.getResourceLinkPool().getPoolChildren(selectedResource+"Link", null, "");
                        if (v.size()>0)
                        {
                            final JDFResourceLink resLink = (JDFResourceLink) v.get(v.size()-1);
                            resLinkNode = getModel().insertNewResourceLinkNode(jdfNode, node, resLink);
                        }
                    }
                    if (resNode != null) // resLinkNode may be == null in case when withLink == false
                    {
                        final InsertResourceAndLinkEdit edit = 
                            new InsertResourceAndLinkEdit(m_frame, jdfNode, node,  hasResourcePool, hasResourceLinkPool, resNode, resLinkNode);
                        m_frame.undoSupport.postEdit( edit );
                    }
                    else 
                    {
                        JOptionPane.showMessageDialog(this, "Insert Resource operation was not completed." +
                                "\nInternal error occured", "Insert Resource", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    } 
    
    /**
     * inserts Attribute into node, implements undo, redo for this action
     */
    public void insertAttrItem()
    {
        final TreePath path = m_frame.getEditorDoc().getSelectionPath();
        if (path != null)
        {
            final JDFTreeNode intoNode = (JDFTreeNode) path.getLastPathComponent();
            final JDFTreeNode attrNode = getModel().insertAttributeIntoDoc(intoNode);
            if (attrNode != null)
            {
                final InsertAttrEdit edit = new InsertAttrEdit(m_frame, path, attrNode);
                m_frame.undoSupport.postEdit( edit );
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
 
    private JDFTreeModel getModel()
    {
        EditorDocument ed =Editor.getEditorDoc();
        return ed==null ? null : ed.getModel();
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private JTree getJDFTree()
    {
        final EditorDocument ed=Editor.getEditorDoc();
        return ed==null ? null : ed.getJDFTree();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
}
