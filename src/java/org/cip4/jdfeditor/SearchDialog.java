/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2008 The International Cooperation for the Integration of 
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFConstants;

/**
 * @author ThunellE
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SearchDialog extends JDialog implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -5193131794786297752L;
    
    private String searchComponent;
    private JButton m_findNextButton;
    private JButton m_cancelButton;    
    private JTextField m_searchTextField;
    private JRadioButton m_forwardRadioButton;
    private JRadioButton m_backwardRadioButton;
    private Vector m_vGlobalSearch = new Vector();
    private JDFFrame m_frame;
    private static String lastSearch=null;
//   private static VString recentSearches=new VString();
    /**
     * Constructor for SearchDialog.
     */
    public SearchDialog(JDFFrame frame) 
    {
        super(frame);
        m_frame=frame;
        
        setTitle(frame.m_littleBundle.getString("FindKey"));
        setSearchComponent(searchComponent);
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(Box.createHorizontalStrut(15));
        final Box middleBox = Box.createVerticalBox();
        
        middleBox.add(Box.createVerticalStrut(20));
        
        final Box box2 = Box.createHorizontalBox();
        final JLabel findLabel = new JLabel(frame.m_littleBundle.getString("FindKey"));
        box2.add(findLabel);
        
        m_searchTextField = new JTextField();
        if(lastSearch!=null)
            m_searchTextField.setText(lastSearch);
        
        m_searchTextField.setPreferredSize(new Dimension(60, 20));
        box2.add(m_searchTextField);
        middleBox.add(box2);
        
        middleBox.add(Box.createVerticalStrut(20));
        final ButtonGroup group = new ButtonGroup();
        middleBox.add(new JSeparator());
        
        m_forwardRadioButton = new JRadioButton(frame.m_littleBundle.getString("ForwardKey"));
        m_forwardRadioButton.setSelected(true);
        m_forwardRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        group.add(m_forwardRadioButton);
        final Box boxF = Box.createHorizontalBox();
        boxF.add(m_forwardRadioButton);
        boxF.add(Box.createHorizontalGlue());
        middleBox.add(boxF);
        
        m_backwardRadioButton = new JRadioButton(frame.m_littleBundle.getString("BackwardKey"));
        m_backwardRadioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        middleBox.add(m_backwardRadioButton);
        group.add(m_backwardRadioButton);
        final Box boxB = Box.createHorizontalBox();
        boxB.add(m_backwardRadioButton);
        boxB.add(Box.createHorizontalGlue());
        middleBox.add(boxB);
        
        middleBox.add(new JSeparator());
        
        final JLabel infoLabel = new JLabel(" ");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Box boxL = Box.createHorizontalBox();
        boxL.add(Box.createVerticalStrut(25));
        boxL.add(infoLabel);
        boxL.add(Box.createVerticalStrut(25));
        middleBox.add(boxL);
        
        final JPanel panel2 = new JPanel();
        panel2.setBorder(BorderFactory.createEmptyBorder());
        m_findNextButton = new JButton(frame.m_littleBundle.getString("FindNextKey"));
        m_findNextButton.addActionListener(this);
        m_findNextButton.setEnabled(true);
        panel2.add(m_findNextButton);
        
        m_cancelButton = new JButton(frame.m_littleBundle.getString("CancelKey"));
        m_cancelButton.addActionListener(this);
        m_cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);
        panel2.add(m_cancelButton);
        middleBox.add(panel2);
        
        getContentPane().add(middleBox);
        getContentPane().add(Box.createHorizontalStrut(15));
        setLocation(400, 400);
        pack();
        getRootPane().setDefaultButton(m_findNextButton);
        
        setVisible(true); 
        
    }
   
    
    public void actionPerformed(ActionEvent e)
    {
        final Object eSrc=e.getSource();
        if (eSrc == m_findNextButton)
        {
            findNext();
        }
        // close the search m_dialog
        else if (eSrc == m_cancelButton)
        {
            dispose();
            Editor.getFrame().m_dialog = null;
        }
    }
    
    public void setSearchComponent(String _searchComponent)
    {
        this.searchComponent = _searchComponent;
    }
    
    public String getSearchComponent()
    {
        return searchComponent;
    }

    public void findNext()
    {
        final boolean forwardDirection = m_forwardRadioButton.isSelected();
        
        if (getSearchComponent().equals("JDFTree"))
        {
            if (!forwardDirection)
            {
                if (m_vGlobalSearch.size() == 0 || m_frame.isDirty())
                {
                    m_vGlobalSearch.clear();
                    final Enumeration enumer = ((JDFTreeNode) m_frame.getRootNode().getFirstChild()).preorderEnumeration();
                    while (enumer.hasMoreElements())
                    {
                        m_vGlobalSearch.add(0, enumer.nextElement());
                    }
                }
            }
//            lastSearch = ((JTextField) ((Box) ((Box) getContentPane().getComponent(1)).getComponent(1)).getComponent(1)).getText();
            lastSearch = m_searchTextField.getText();
            findStringInTree(lastSearch, forwardDirection);
        }
                
        else if (getSearchComponent().equals("NeighbourTree"))
            m_frame.m_topTabs.m_inOutScrollPane.findStringInNeighbourTree(lastSearch, forwardDirection);
    }

    /**
     * Method findStringInTree.
     * search a string in the main tree view
     * @param inString
     * @param forwardDirection
     */
    private void findStringInTree(String inString, boolean forwardDirection)
    {
        if (inString != null && !inString.equals(JDFConstants.EMPTYSTRING))
        {
            Editor.setCursor(1,null);
            final String searchString = inString.toUpperCase();
            final TreePath selectionPath = m_frame.m_treeArea.getSelectionPath();
            final JDFTreeNode searchNode = selectionPath==null ? null : (JDFTreeNode) selectionPath.getLastPathComponent();
            TreePath path=null;
                    
            if (forwardDirection)
            {
                final Enumeration tmpEnumeration = ((JDFTreeNode) m_frame.getRootNode().getFirstChild()).preorderEnumeration();
                while (tmpEnumeration.hasMoreElements()&&!(tmpEnumeration.nextElement().equals(searchNode)))
                {
                    // eat up to here
                }
                while (tmpEnumeration.hasMoreElements())
                {
                    JDFTreeNode checkNode = (JDFTreeNode) tmpEnumeration.nextElement();
                    final String tmpString = checkNode.toString().toUpperCase();
                    if (tmpString.indexOf(searchString) != -1)
                    {
                        path = new TreePath(checkNode.getPath());
                        break;
                    }
                }
            }
            else // backward
            {
                int j = 0;
                for (j=0; j < m_vGlobalSearch.size() && !m_vGlobalSearch.elementAt(j).equals(searchNode); j++)
                {
                    // eat
                }
                for (j= j + 1; j < m_vGlobalSearch.size(); j++)
                {
                    JDFTreeNode checkNode = (JDFTreeNode) m_vGlobalSearch.elementAt(j);
                    final String tmpString = checkNode.toString().toUpperCase();
                    
                    if (tmpString.indexOf(searchString) != -1)
                    {
                        path =  new TreePath(checkNode.getPath());
                        break;
                    }
                }
            }
            if (path!=null)
            {
                Editor.getEditorDoc().setSelectionPath(path,true);
            }
            else
            {
                EditorUtils.errorBox("StringNotFoundKey",searchString);
            }
        }
        Editor.setCursor(0,null);
    }
    
}
