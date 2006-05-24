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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFComment;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * this handles anything located in the top right tabbed panes
 * it has been misused as a dump from JDFFrame
 */
public class EditorTabbedPaneA extends JTabbedPane
{
    private static final long serialVersionUID = 136801779114140915L;

    protected JDFInOutScroll m_inOutScrollPane;
    protected JDFFrame m_frame;
    private ResourceBundle m_littleBundle;
    
    // one index per tab
    final public int m_IO_INDEX = 0;
    final public int m_PROC_INDEX = 1;
    final public int m_DC_INDEX = 2;
    final public int m_COM_INDEX = 3;

    private JTextArea m_commentArea;
    private JTextArea m_processArea;
    private JScrollPane m_commentScrollPane;
    private JScrollPane m_processScrollPane;
    private JDFDevCapScrollPane m_devCapScrollPane;

    ProcessPanel m_pArea = new ProcessPanel();
    // vectors for the in out view
    Vector m_vPaintedRes = new Vector();
    Vector m_vIn         = new Vector();

    /**
     * sets up the top right frame
     * @param frame
      */
    public EditorTabbedPaneA(JDFFrame frame)
    {
        super();
        m_frame=frame;
        m_littleBundle=frame.m_littleBundle;
                
        setBorder(BorderFactory.createLineBorder(Color.black));
        
        m_inOutScrollPane = new JDFInOutScroll(m_frame,m_littleBundle);
    
        addTab(m_littleBundle.getString("NextNeighbourKey"), null,
                m_inOutScrollPane, m_littleBundle.getString("NextNeighbourKey"));
        setComponentAt(m_IO_INDEX, m_inOutScrollPane);
        setSelectedIndex(m_IO_INDEX);
        
        m_processArea = new JTextArea();
        m_processArea.setEditable(false);
        m_processScrollPane = new JScrollPane();
        m_processScrollPane.getViewport().add(m_processArea, null);
        m_processScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        m_processScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        
        
        
        addTab(m_littleBundle.getString("ProcessViewKey"), null,
                m_processScrollPane, m_littleBundle.getString("ProcessViewKey"));
        setComponentAt(m_PROC_INDEX, m_processScrollPane);
        
        
        m_devCapScrollPane = new JDFDevCapScrollPane(m_frame);
    
        addTab(m_littleBundle.getString("DevCapViewKey"), null,
                m_devCapScrollPane, m_littleBundle.getString("DevCapViewKey"));
        setComponentAt(m_DC_INDEX, m_devCapScrollPane);
        
        m_commentArea = new JTextArea();
        m_commentArea.setEditable(true);
        m_commentArea.setBackground(Color.white);
        
        m_commentScrollPane = new JScrollPane();
        m_commentScrollPane.getViewport().add(m_commentArea, null);
        m_commentScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        m_commentScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        addTab(m_littleBundle.getString("CommentViewKey"), null,
                m_commentScrollPane, m_littleBundle.getString("CommentViewKey"));
        setComponentAt(m_COM_INDEX, m_commentScrollPane);
        setEnabledAt(m_COM_INDEX, false);
        
        
        final MouseAdapter tabbedPaneListener = new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                e.getID(); // fool compiler
                if (getSelectedIndex() == m_PROC_INDEX)
                    initProcessView();
                
                else if (getSelectedIndex() == m_COM_INDEX
                        || getSelectedIndex() == m_IO_INDEX
                        || getSelectedIndex() == m_DC_INDEX)
                {
                    m_frame.m_buttonBar.m_upOneLevelButton.setEnabled(false);
                    m_frame.m_buttonBar.m_zoomInButton.setEnabled(false);
                    m_frame.m_buttonBar.m_zoomOutButton.setEnabled(false);
                    m_frame.m_buttonBar.m_zoomOrigButton.setEnabled(false);
                    m_frame.m_buttonBar.m_zoomBestButton.setEnabled(false);
                    
                    if(getSelectedIndex() == m_IO_INDEX)
                    {
                        m_inOutScrollPane.clearInOutView();
                        m_inOutScrollPane.initInOutView();
                    }
                    else if (getSelectedIndex() == m_COM_INDEX)
                    {
                        showComment();
                    }
                    else if (getSelectedIndex() == m_DC_INDEX)
                    {    
                        // __Lena__ TBD - display DeviceCap Editor
                    }
                }
            }
        };
        
        addMouseListener(tabbedPaneListener);
    }
    public boolean processAreaIsNull()
    {
        if (m_pArea == null || getSelectedIndex() != m_PROC_INDEX
                || m_frame.m_treeArea.getTreeView().getComponent(0).getClass().equals(JTextArea.class))
            return true;
        
        return false;
    }
    
    
    public boolean inOutIsNull()
    {
        if (m_inOutScrollPane.m_inOutArea == null || getSelectedIndex() != m_IO_INDEX
                || m_frame.m_treeArea.getTreeView().getComponent(0).getClass().equals(JTextArea.class))
            return true;
        
        return false;
    }

    /**
     * Initiate the Process View.
     */
    void initProcessView()
    {
        m_pArea.clear();
        try
        {
            EditorDocument ed=m_frame.getEditorDoc();
            KElement kElement;
            
            JTree m_jdfTree = ed.getJDFTree();
            if (m_jdfTree != null && m_frame.getJDFDoc() != null)
            {
                if (m_jdfTree.getSelectionPath() == null)
                {
                    kElement = ((JDFTreeNode) m_frame.getRootNode().getFirstChild()).getElement();
                    final TreePath p = new TreePath(((JDFTreeNode) m_frame.getRootNode().getFirstChild()).getPath());
                    m_jdfTree.setSelectionPath(p);
                }
                
                else
                {
                    kElement = ((JDFTreeNode) m_jdfTree.getSelectionPath().getLastPathComponent()).getElement();
                }
                m_pArea.removeAll();
                
                m_vIn.clear();
                m_vPaintedRes.clear();
                
                if (kElement instanceof JDFNode)
                {
                    drawProcessView(kElement);
                }
                else if (kElement instanceof JDFResource)
                {
                    JDFResource r=(JDFResource)kElement;
                    final JDFNode jdf = r.getParentJDF();
                    if (jdf !=null )
                    {
                        drawProcessView(jdf);
                    }
                }
                if (kElement.equals(((JDFTreeNode) m_frame.getRootNode().getFirstChild()).getElement()))
                    m_frame.m_buttonBar.m_upOneLevelButton.setEnabled(false);
                else
                    m_frame.m_buttonBar.m_upOneLevelButton.setEnabled(true);
                
                m_frame.m_buttonBar.m_zoomInButton.setEnabled(true);
                m_frame.m_buttonBar.m_zoomOutButton.setEnabled(true);
                m_frame.m_buttonBar.m_zoomOrigButton.setEnabled(false);
                m_frame.m_buttonBar.m_zoomBestButton.setEnabled(true);
            }
        }
        catch (Exception s)
        {
            s.printStackTrace();
            EditorUtils.errorBox("ProcessViewErrorKey",null); 
        }        
    }

    /**
     * Shows the text in the Comment View, if there is any text to show.
     */
    void showComment()
    {
        EditorDocument ed=m_frame.getEditorDoc();
        if (ed.getSelectionPath() != null)
        {
            final TreePath path = ed.getSelectionPath();
            final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
            
            if (node.isElement())
            {
                final KElement jdfElem = node.getElement();
                if (jdfElem instanceof JDFComment)
                {
                    final String txt = jdfElem.getTextContent().trim();
                    m_commentArea.setText(txt.equals("") ? m_littleBundle.getString("EmptyCommentKey") : txt);
                    setEnabledAt(m_COM_INDEX, true);
                    setSelectedIndex(m_COM_INDEX);
                }
                else
                    setEnabledAt(m_COM_INDEX, false);
            }
            else
                setEnabledAt(m_COM_INDEX, false);
            
            m_commentScrollPane.getViewport().add(m_commentArea);
            setComponentAt(m_COM_INDEX, m_commentScrollPane);
            
            SwingUtilities.updateComponentTreeUI(m_commentScrollPane);
        }
    }
    
    public void refreshView(JDFDoc jdfDoc)
    {
        m_inOutScrollPane.clearInOutView();
        m_vIn.clear();
        m_vPaintedRes.clear();
        
        final KElement rootElement = jdfDoc.getRoot();
        if (rootElement instanceof JDFNode) 
        {
            drawProcessView(rootElement);
        }
        else 
        {
            m_pArea.clear();  // clear processView area
        } 
        m_inOutScrollPane.initInOutView();

    }

    /**
     * Get the node to search for in the Process View.
     * @param source - The location in the Process View that's been selected
     * @return the JDFTreeNode that is to be searched for.
     */
    void getProcessSearchNode(Object src)
    {
        final ProcessPart pp = (ProcessPart) src;
        JDFTreeNode node = null;
        try
        {
            final KElement kElement = pp.getElem();
            node = new JDFTreeNode(kElement);
        }
        catch (Exception s)
        {
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("FindErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
            s.printStackTrace();
        }
        if (node != null)
            m_frame.m_treeArea.findNode(node);
    }
    
    
    
    
    /**
     * Method clearViews.
     * clear all views before opening a new file
     */
    void clearViews()
    {
        m_inOutScrollPane.clearInOutView();
        m_pArea.removeAll();
        m_commentArea.setText("");
        setEnabledAt(m_COM_INDEX, false);
        m_vIn.clear();
        m_vPaintedRes.clear();
     }
    public void zoom(char c)
    {
        double zoom = m_pArea.getZoom();
        
        if (c == '+')
            zoom *= 1.1;
        else if (c == '-')
            zoom *= 0.9;
        else if (c == 'o')
            zoom = 1.0;
        else if (c == 'b')
        {
            final Dimension screen = getSize();
            
            final double wFactor = (screen.getWidth() - 15) / m_pArea.width;
            final double hFactor = (screen.getHeight() - 40) / m_pArea.height;
            
            zoom = wFactor < hFactor ? wFactor : hFactor;
        }
        m_pArea.setZoom(zoom);
        
        m_frame.m_buttonBar.setEnableZoom(zoom);
        
        m_pArea.zoom();
        m_processScrollPane.getViewport().add(m_pArea, null);
        setComponentAt(m_PROC_INDEX, m_processScrollPane);
        
        SwingUtilities.updateComponentTreeUI(m_processScrollPane);
    }
    /**
     * Method drawProcessoView.
     * draws the process view for the selected node in the m_jdfTree
     * @param kElement
     */
    void drawProcessView(KElement kElement) 
    {
        if (kElement==null)
            return;
        
        m_frame.setCursor(JDFFrame.m_waitCursor);
        m_pArea.clear();
        
        //create a popup menu for copy to clipboard
        final JPopupMenu jpmPopup = new JPopupMenu();
        
        //create a "copy top clipboard" item for the popup menus
        final JMenuItem jmiCopyToClipboard = new JMenuItem(m_littleBundle.getString("copyToClipboard"));
        jmiCopyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                e.getID(); // fool compiler
                //get the system clipboard
                final Clipboard cb =
                    Toolkit.getDefaultToolkit().getSystemClipboard();
                final Dimension size = m_pArea.getSize();
                
                final BufferedImage myImage = 
                    new BufferedImage(size.width, size.height,
                            BufferedImage.TYPE_INT_RGB);
                final Graphics2D g2 = myImage.createGraphics();
                m_pArea.paint(g2);
                g2.dispose();
                
                final ImageSelection icontents = new ImageSelection(myImage);
                
                cb.setContents(icontents, icontents);
            }
        });
        //add the item to the popup menu
        jpmPopup.add(jmiCopyToClipboard);
        
        //create a mouse event listener for the process area, because
        //the copy action is allowd only for this area
        //listen for both MousePressed and MouseReleased events to
        //ensure functionality on all OSes
        m_pArea.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    jpmPopup.show(e.getComponent(), e.getX(), e.getY());
            }
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    jpmPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        });    
        
        int x = 20;
        int y = 20;
        int xMAX = 0;
        int yMAX = 0;
        int pPartStart = 10;
        int outResStart = 0; // Initial values
        final int esX = 50;
        final int esY = 30; // The Empty Space between Nodes
        final Vector vJDFNodes = new Vector();
        Vector vInputLinks = null;
        Vector vOutputLinks = null;
        try
        {
            final NodeList nodeList = kElement.getChildNodes();
            
            vJDFNodes.add(kElement);
            
            // Loop to add the nodes to the correct Vector
            for (int j = 0; j < nodeList.getLength(); j++)
            {
                final Node node = nodeList.item(j);
                
                if (node != null && node instanceof KElement)
                {
                    final KElement child = (KElement) node;
                    
                    if (child instanceof JDFNode)
                    {
                        vJDFNodes.add(child);
                    }
                    else if (child instanceof JDFResourceLinkPool)
                    {
                        final JDFResourceLinkPool resourceLinkPool = (JDFResourceLinkPool) child;
                        vInputLinks = resourceLinkPool.getInOutLinks(true,false,null,null);
                        vOutputLinks = resourceLinkPool.getInOutLinks(false,false,null,null);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("ProcessViewErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
            this.setCursor(JDFFrame.m_readyCursor);
        }
        
        if (vInputLinks!=null && !vInputLinks.isEmpty())
        {
            int w = 0;
            
            for (int i = 0; i < vInputLinks.size(); i++)
            {
                final ProcessPart inputPart = new ProcessPart((KElement) vInputLinks.get(i), "ProcPart");
                
                inputPart.setGfxParams(m_pArea.getFM(inputPart), x, y);
                inputPart.addMouseListener(new ResourceListener());
                
                m_pArea.addPart(inputPart);
                m_vIn.add(inputPart);
                
                y += inputPart.height + esY;
                w = w < inputPart.width ? inputPart.width : w;
            }
            x += w + esX;
            
            xMAX = xMAX < x - esX ? x - esX : xMAX;
            yMAX = yMAX < y - esY ? y - esY : yMAX;
            pPartStart = x - 10;
        }
        
        if (!vJDFNodes.isEmpty())
        {
            final Vector vPrevNodes = new Vector();
            final JDFNode parent = (JDFNode) vJDFNodes.get(0);
            
            if (parent.hasChildElement("JDF", ""))
            {
                final Vector vParts = new Vector();
                int pWidth = x - esX;
                int pHeight = y - esY;
                xMAX = xMAX < x ? x : xMAX;
                yMAX = yMAX < y ? y : yMAX;
                
                for (int i = 0; i < m_vIn.size(); i++)
                {
                    final ProcessPart pp = (ProcessPart) m_vIn.get(i);
                    m_pArea.addPoint(pp.getInPoint());
                    m_pArea.addPoint(new Point(x - 10, (int) ((ProcessPart) m_vIn.get(0)).getInPoint().getY()));
                }
                for (int i = 1; i < vJDFNodes.size(); i++)
                {
                    final JDFNode nodeElem = (JDFNode) vJDFNodes.get(i);
                    final ProcessPart nodePart = new ProcessPart(nodeElem, "ProcPart");
                    
                    if (nodeElem.hasChildElement("ResourceLinkPool", null))
                    {
                        final JDFResourceLinkPool resLinkPool =  nodeElem.getResourceLinkPool();
                        final NodeList links = resLinkPool.getChildNodes();
                        
                        for (int j = 0; j < links.getLength(); j++)
                        {
                            if (links.item(j) instanceof JDFResourceLink)
                            {
                                final JDFResourceLink rL = (JDFResourceLink) links.item(j);
                                JDFResource resLink = rL.getTarget();
                                
                                if (resLink != null)
                                {                      
                                    resLink=resLink.getResourceRoot();
                                    final ProcessPart res = new ProcessPart(resLink, "ResPart");
                                    
                                    if (!nodePart.hasRes(res))
                                        nodePart.addTovAllRes(res);
                                    
                                    if (rL.getAttribute("Usage", "", "").equals("Input"))
                                    {
                                        if (!nodePart.hasRes(res))
                                            nodePart.addTovInRes(res);
                                    }
                                    
                                    else if (rL.getAttribute("Usage", "", "").equals("Output"))
                                    {
                                        if (!nodePart.hasRes(res))
                                            nodePart.addTovOutRes(res);
                                    }
                                }
                            }
                        }
                    }
                    nodePart.addMouseListener(new PartListener());
                    vParts.add(nodePart);
                }
                final Vector vSorted = m_pArea.sortPanels(vParts);
                
                y = 90;
                int yResIn  = y;
                int yResOut = y;
                int w       = 0;
                
                // Loop over all the Sorted JDF Nodes
                for (int i = 0; i < vSorted.size(); i++)
                {
                    final ProcessPart node = (ProcessPart) vSorted.get(i);
                    final Vector vInRes = node.getvInRes();
                    Vector vPrevParts = new Vector();
                    int yPos = 0;
                    
                    if (vPrevNodes.isEmpty())
                        pHeight = 90 - esY;
                    else
                        vPrevParts = m_pArea.comparePrevious(vPrevNodes, node);
                    
                    if (vInRes.isEmpty())
                    {
                        if (vPrevParts.isEmpty())
                        {
                            x = pPartStart + 10;
                            y = pHeight + esY;
                        }
                    }
                    else
                    {
                        final Vector vInPoints = new Vector();
                        final Vector vInDirPoints = new Vector();
                        
                        if (vPrevParts.isEmpty())
                        {
                            y = pHeight + esY;
                            yResIn = y;
                        }
                        else
                        {
                            yResIn = ((ProcessPart) vPrevParts.get(0)).getyResOut();
                            yResIn = yResIn == 0 ? y : yResIn;
                        }
                        
                        int yNext = y; // The y-value for the next resource
                        
                        for (int j = 0; j < vInRes.size(); j++)
                        {
                            final ProcessPart inputPart = (ProcessPart) vInRes.get(j);
                            
                            if (prevPainted(inputPart) == -1)
                            {
                                m_vPaintedRes.add(inputPart);
                                
                                if (vPrevParts.isEmpty())
                                {                                            
                                    pPartStart = pPartStart == 0 ? 10 : pPartStart;
                                    
                                    x = pPartStart + 10;
                                    
                                    inputPart.setGfxParams(m_pArea.getFM(inputPart), x, yResIn);
                                    inputPart.setLink(inputPart.getElem());
                                    inputPart.addMouseListener(new ResourceListener());
                                    m_pArea.addPart(inputPart);
                                    
                                    if (yPos == 0)
                                        yPos = inputPart.height / 2;
                                    
                                    yResIn += inputPart.height + esY;
                                    node.setyResIn(yResIn);
                                    yNext = yResIn;
                                    
                                    w = w < inputPart.width ? inputPart.width : w;
                                    
                                    vInPoints.add(inputPart.getInPoint());
                                }
                                else
                                {                                    
                                    final ProcessPart pp = (ProcessPart) vPrevParts.get(0);
                                    
                                    if (vPrevParts.size() == 1 && !pp.hasSharedInput())
                                    {                                        
                                        inputPart.setGfxParams(m_pArea.getFM(inputPart), x, pp.getyResOut());
                                        inputPart.setLink(inputPart.getElem());
                                        inputPart.addMouseListener(new ResourceListener());
                                        m_pArea.addPart(inputPart);
                                        
                                        if (yPos == 0)
                                            yPos = inputPart.height / 2;
                                        
                                        yNext = pp.getyResOut() + inputPart.height + esY;
                                        ((ProcessPart) vPrevParts.get(0)).setyResOut(yNext);
                                        
                                        w = w < inputPart.width ? inputPart.width : w;
                                        
                                        vInPoints.add(inputPart.getInPoint());
                                    }
                                    else if (vPrevParts.size() != 1)
                                    {
                                        final ProcessPart last = (ProcessPart) vPrevParts.lastElement();
                                        
                                        x = last.getxPos() + last.width + esX;
                                        
                                        inputPart.setGfxParams(m_pArea.getFM(inputPart), x, last.getyResOut());
                                        inputPart.setLink(inputPart.getElem());
                                        inputPart.addMouseListener(new ResourceListener());
                                        m_pArea.addPart(inputPart);
                                        
                                        if (yPos == 0)
                                            yPos = inputPart.height / 2;
                                        
                                        yNext = last.getyResOut() + inputPart.height + esY;
                                        ((ProcessPart) vPrevParts.lastElement()).setyResOut(yNext);
                                        
                                        w = w < inputPart.width ? inputPart.width : w;
                                        
                                        vInPoints.add(inputPart.getInPoint());
                                    }
                                }
                            }
                            else
                            {
                                final ProcessPart painted = (ProcessPart) m_vPaintedRes.get(prevPainted(inputPart));
                                vInDirPoints.add(painted.getInPoint());
                                
                                if (yPos == 0)
                                    yPos = painted.height / 2;
                            }
                        }
                        if (!vPrevParts.isEmpty() && w == 0)
                        {
                            x = ((ProcessPart) vPrevParts.lastElement()).getxPos()
                            + ((ProcessPart) vPrevParts.lastElement()).width;
                            y = ((ProcessPart) vPrevParts.lastElement()).getyPos()
                            + ((ProcessPart) vPrevParts.lastElement()).height + esY;
                        }
                        x += w + esX;
                        
                        for (int j = 0; j < vInPoints.size(); j++)
                        {
                            m_pArea.addPoint((Point) vInPoints.get(j));
                            m_pArea.addPoint(new Point(x, y + yPos));
                        }
                        for (int j = 0; j < vInDirPoints.size(); j++)
                        {
                            m_pArea.addDirPoint((Point) vInDirPoints.get(j));
                            m_pArea.addDirPoint(new Point(x, y + yPos));
                        }
                        
                        xMAX = xMAX < x - esX ? x - esX : xMAX;
                        yMAX = yMAX < yNext - esY? yNext - esY : yMAX;
                        pWidth = pWidth < x - esX ? x - esX : pWidth;
                        pHeight = pHeight < yNext - esY ? yNext - esY : pHeight;
                    }
                    node.setGfxParams(m_pArea.getFM(node), x, y);
                    
                    xMAX = xMAX < x + node.width ? x + node.width : xMAX;
                    yMAX = yMAX < y + node.height ? y + node.height : yMAX;
                    pWidth = pWidth < x + node.width ? x + node.width : pWidth;
                    pHeight = pHeight < y + node.height ? y + node.height : pHeight;
                    
                    x += node.width + esX;
                    m_pArea.addPart(node);
                    
                    final Vector vOutRes = node.getvOutRes();
                    
                    if (vOutRes.isEmpty())
                    {
                        w = yPos =0;
                        y += node.height;
                    }
                    else
                    {
                        w = yPos = 0;
                        yResOut = y;
                        final Vector vOutPoints = new Vector();
                        final Vector vOutDirPoints = new Vector();
                        
                        for (int j = 0; j < vOutRes.size(); j++)
                        {
                            final ProcessPart outputPart = (ProcessPart) vOutRes.get(j);
                            
                            if (prevPainted(outputPart) == -1)
                            {
                                m_vPaintedRes.add(outputPart);
                                
                                outputPart.setGfxParams(m_pArea.getFM(outputPart), x, yResOut);
                                outputPart.setLink(outputPart.getElem());
                                outputPart.addMouseListener(new ResourceListener());
                                m_pArea.addPart(outputPart);
                                
                                if (yPos == 0)
                                    yPos = outputPart.height / 2;
                                
                                yResOut += outputPart.height + esY;
                                
                                w = w < outputPart.width ? outputPart.width : w;
                                
                                vOutPoints.add(outputPart.getOutPoint());
                            }
                            else
                            {
                                final ProcessPart painted = (ProcessPart) m_vPaintedRes.get(prevPainted(outputPart));
                                vOutDirPoints.add(painted.getOutPoint());
                                
                                if(yPos == 0)
                                    yPos = painted.height / 2;
                            }
                        }
                        for (int j = 0; j < vOutPoints.size(); j++)
                        {
                            m_pArea.addPoint((Point) vOutPoints.get(j));
                            m_pArea.addPoint(new Point(x - esX, y + yPos));
                        }
                        for (int j = 0; j < vOutDirPoints.size(); j++)
                        {
                            m_pArea.addDirPoint((Point) vOutDirPoints.get(j));
                            m_pArea.addDirPoint(new Point(x - esX, y + yPos));
                        }
                        node.setyResOut(yResOut);
                        
                        xMAX = xMAX < x + w ? x + w : xMAX;
                        yMAX = yMAX < yResOut - esY ? yResOut - esY : yMAX;
                        pWidth = pWidth < x + w ? x + w : pWidth;
                        pHeight = pHeight < yResOut - esY ? yResOut - esY : pHeight;
                        
                        yResOut = y;
                    }
                    vPrevNodes.add(node);
                }
                pPartStart = pPartStart == 0 ? 10 : pPartStart;
                
                final ProcessPart parentPart = new ProcessPart(parent, "ParentPart");
                parentPart.setGfxParams(m_pArea.getFM(parentPart), pPartStart, 10);
                parentPart.width = pWidth - pPartStart + 10 < parentPart.width 
                ? parentPart.width 
                        : pWidth - pPartStart + 10;
                parentPart.height = pHeight;
                parentPart.addMouseListener(new ResourceListener());
                m_pArea.setParent(parentPart);
                
                xMAX = xMAX < parentPart.width ? parentPart.width : xMAX;
                yMAX = yMAX < pHeight ? pHeight : yMAX;
                outResStart = parentPart.width + parentPart.getxPos();
            }
            else
            {
                for (int i = 0; i < m_vIn.size(); i++)
                {
                    final ProcessPart pp = (ProcessPart) m_vIn.get(i);
                    m_pArea.addPoint(pp.getInPoint());
                    m_pArea.addPoint(new Point(x, (int) ((ProcessPart) m_vIn.get(0)).getInPoint().getY()));
                }
                y = 20;
                final ProcessPart procPart = new ProcessPart(parent, "ProcPart");
                procPart.setGfxParams(m_pArea.getFM(procPart), x, y);
                procPart.addMouseListener(new PartListener());
                m_pArea.addPart(procPart);
                
                x += procPart.width + esX;
                
                xMAX = xMAX < x - esX ? x - esX : xMAX;
                yMAX = yMAX < y + procPart.height ? y + procPart.height : yMAX;
            }
            if (vOutputLinks!=null && !vOutputLinks.isEmpty())
            {
                int yTemp = 0;
                int w = 0;
                y = 20;
                
                if (parent.hasChildElement("JDF", ""))
                    x = outResStart + 40;
                
                for (int i = 0; i < vOutputLinks.size(); i++)
                {
                    final JDFElement outputLink = (JDFElement) vOutputLinks.get(i);
                    if (outputLink==null)
                        continue;
                    
                    final ProcessPart outputPart = new ProcessPart(outputLink, "ProcPart");
                    
                    if (prevPainted(outputPart) == -1)
                    {
                        m_vPaintedRes.add(outputPart);
                        outputPart.setGfxParams(m_pArea.getFM(outputPart), x, y);
                        outputPart.addMouseListener(new ResourceListener());
                        m_pArea.addPart(outputPart);
                        
                        if (i == 0)
                            yTemp = y + outputPart.height / 2;
                        
                        m_pArea.addPoint(new Point(x, y + outputPart.height / 2));
                        final Point p = parent.hasChildElement("JDF", "") ?
                                new Point(outResStart, yTemp) : new Point(x - esX, yTemp);
                                m_pArea.addPoint(p);
                                
                                y += outputPart.height + esY;
                                w = w < outputPart.width ? outputPart.width : w;
                    }
                }
                x += w + esX;
                
                xMAX = xMAX < x - esX ? x - esX : xMAX;
                yMAX = yMAX < y - esY ? y - esY : yMAX;
            }
            m_pArea.setPreferredSize(new Dimension(xMAX + 20, yMAX + 20));
            m_pArea.repaint();
            m_processScrollPane.getViewport().add(m_pArea, null);
            setComponentAt(m_PROC_INDEX, m_processScrollPane);
            
            SwingUtilities.updateComponentTreeUI(m_processScrollPane);
        }
        this.setCursor(JDFFrame.m_readyCursor);
    }
    /**
     * Method goUpOneLevelInProcessView.
     * takes the selected node in the m_jdfTree an goes up one level in the
     * Process View
     */
    public void goUpOneLevelInProcessView()
    {
        KElement kElement = ((JDFTreeNode) m_frame.m_treeArea.getSelectionPath()
                .getLastPathComponent()).getElement();
        
        if (kElement != null && kElement instanceof JDFResource)
        {
            if (kElement instanceof JDFResourcePool)
                kElement = kElement.getParentNode_KElement();
            else
            {
                try
                {
                    //TODO fix
                    kElement = ( (JDFElement)((kElement.getParentNode_KElement()).getParentNode_KElement())).getParentJDF();
                }
                catch (JDFException j)
                {
                    kElement = (( kElement.getParentNode_KElement()).getParentNode_KElement());
                }
            }
        }
        else
        {
            kElement =((JDFTreeNode) ((JDFTreeNode) m_frame.m_treeArea.getSelectionPath()
                    .getLastPathComponent()).getParent()).getElement();
        }
        final JTextArea ta = new JTextArea();
        ta.setEditable(false);
        setComponentAt(m_PROC_INDEX, ta);
        m_vIn.clear();
        m_vPaintedRes.clear();
        drawProcessView(kElement);
        
        JDFTreeNode node;
        if (kElement instanceof JDFNode)
        {
            node = new JDFTreeNode(kElement);
            m_frame.m_treeArea.findNode(node);
        }
    }
    /**
     * Checks if the ProcessPart already has been added to the ProcessPanel.
     * @param pp - The ProcessPanel you want to check
     * @return true if pp already has been painted, false otherwise.
     */
    private int prevPainted(ProcessPart pp)
    {
        if (!m_vPaintedRes.isEmpty())
        {
            final KElement link = pp.getElem();
            
            for (int n = 0; n < m_vPaintedRes.size(); n++)
            {
                final ProcessPart pRes = (ProcessPart) m_vPaintedRes.get(n);
                
                if (pp.equals(pRes))
                    return n;
                
                final KElement pLink = pRes.getLink();
                
                if (pLink != null)
                {
                    if (link.equals(pLink))
                        return n;
                    
                    if (link.getAttribute("Usage", "", "").equals("Input")
                            && pLink.getAttribute("Usage", "", "").equals("Output"))
                    {
                        if (!pLink.getAttribute("ID").equals("") && pLink.getAttribute("ID").equals(link.getAttribute("ID")))
                            return n;
                    }
                    if (link.getAttribute("Usage", "", "").equals("Output")
                            && pLink.getAttribute("Usage", "", "").equals("Input"))
                    {
                        if (!pLink.getAttribute("ID").equals("") && pLink.getAttribute("ID").equals(link.getAttribute("ID")))
                            return n;
                    }
                    if (link.getAttribute("Usage", "", "").equals("Input")
                            && pLink.getAttribute("Usage", "", "").equals("Input"))
                    {
                        if (!pLink.getAttribute("ID").equals("") && pLink.getAttribute("ID").equals(link.getAttribute("ID")))
                        {
                            if (!link.hasAttribute("CombinedProcessIndex", "", false)
                                    && !pLink.hasAttribute("CombinedProcessIndex", "", false)
                                    && !link.hasAttribute("CombinedProcessType", "", false)
                                    && !pLink.hasAttribute("CombinedProcessType", "", false))
                                return n;
                        }
                    }
                }
            }
        }
        
        return -1;
    }
    
    
    class ResourceListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e))
            {
                getProcessSearchNode(e.getSource());
            }
        }
    }
    class PartListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e))
            {
                m_vIn.clear();
                m_vPaintedRes.clear();
                m_pArea.removeAll();
                final KElement element = ((ProcessPart) e.getSource()).getElem();
                
                drawProcessView(element);
                m_pArea.repaint();
                if ((element instanceof JDFNode) || (element instanceof JDFResource))
                {
                    JDFTreeNode node = new JDFTreeNode(element);
                    m_frame.m_treeArea.findNode(node);
                }
            }
        }
    }
}