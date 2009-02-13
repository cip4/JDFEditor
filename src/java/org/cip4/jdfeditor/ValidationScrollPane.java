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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class ValidationScrollPane extends JScrollPane
{

    /**
     * 
     */
    private static final long serialVersionUID = -3269951200837982708L;
    JDFFrame m_frame;
    protected JTree m_reportTree;
    ValidationSelectionListener m_SelectionListener;


    public ValidationScrollPane(JDFFrame frame)
    {
        super();
        m_frame=frame;
        getVerticalScrollBar().setUnitIncrement(20);
        getHorizontalScrollBar().setUnitIncrement(20);
        getViewport().setBackground(Color.white);
    }

    public void clearReport() 
    {
        getViewport().setView(null);        
    }

    /**
     * Creates the popupmenu after a right mouse click on node in the DevCap Output View.   
     * @param path - The path to the clicked node
     * @return A JPopupMenu
     */
    JPopupMenu drawValidationMouseMenu(TreePath path)
    {
        final JPopupMenu rightMenu = new JPopupMenu();
        final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
        if (node == null) 
            return rightMenu;
        
        if (!node.isLeaf())
        {
            JMenuItem xpandDevCapOutput = new JMenuItem(m_frame.m_littleBundle.getString("ExpandKey"));
            xpandDevCapOutput.addActionListener(new ActionListener() 
                    {
                public void actionPerformed(ActionEvent ae)
                {
                    ae.getID(); // fool compiler
                    final Enumeration e = node.postorderEnumeration();
                    
                    while (e.hasMoreElements())
                    {
                        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) e.nextElement();
                        m_reportTree.expandPath(new TreePath(treeNode.getPath()));
                    }
                    m_reportTree.expandPath(m_reportTree.getSelectionPath());
                }
                    });
            
            rightMenu.add(xpandDevCapOutput);
            xpandDevCapOutput.setEnabled(true);
            
            JMenuItem collapseDevCapOutput = new JMenuItem(m_frame.m_littleBundle.getString("CollapseKey"));
            collapseDevCapOutput.addActionListener(new ActionListener() 
                    {
                public void actionPerformed(ActionEvent ae)
                {
                    ae.getID(); // fool compiler
                    final Enumeration e = node.postorderEnumeration();
                    
                    while (e.hasMoreElements())
                    {
                        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) e.nextElement();
                        m_reportTree.collapsePath(new TreePath(treeNode.getPath()));
                    }
                    m_reportTree.collapsePath(m_reportTree.getSelectionPath());
                }
                    });
            
            rightMenu.add(collapseDevCapOutput);
            collapseDevCapOutput.setEnabled(true);
        }
        
        return rightMenu;
    }
    
    class ValidationSelectionListener implements TreeSelectionListener
    {
        public void valueChanged(TreeSelectionEvent e) 
        {
            e.getPath(); // fool compiler
            final JDFTreeNode node = (JDFTreeNode)m_reportTree.getLastSelectedPathComponent();            
            if (node != null) 
            {            
                final String path = node.getXPathAttr();
                m_frame.m_treeArea.findInNode(path);
            }
        }
    }

    class ValidationPopupListener extends MouseAdapter
    {
        @Override
		public void mousePressed(MouseEvent e)
        {
            final TreePath path = m_reportTree.getPathForLocation(e.getX(), e.getY());
            
            if ((SwingUtilities.isRightMouseButton(e) || e.isControlDown()) && path != null)
            {
                m_reportTree.removeTreeSelectionListener(m_SelectionListener);
                m_reportTree.setSelectionPath(path);
                
                final JPopupMenu rightMenu = drawValidationMouseMenu(path);
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
                m_reportTree.addTreeSelectionListener(m_SelectionListener);
            }
        }
    }    
}
