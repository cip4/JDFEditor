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
    class MyDocumentListener implements DocumentListener
    {
        public void insertUpdate(DocumentEvent e)
        {
            changedUpdate(e);
        }

        public void removeUpdate(DocumentEvent e)
        {
            changedUpdate(e);
        }

        public void changedUpdate(DocumentEvent e)
        {
            e.getLength(); // fool compiler
            if (m_searchTextField.getText().length() == 0)
                m_findNextButton.setEnabled(false);
            else
            {
                m_findNextButton.setEnabled(true);
                m_findNextButton.setSelected(true);
            }
        }
    }

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -5193131794786297752L;
    
    String searchComponent;
    JButton m_findNextButton;
    JButton m_cancelButton;    
    JTextField m_searchTextField;
    private JRadioButton m_forwardRadioButton;
    private JRadioButton m_backwardRadioButton;
    private Vector m_vGlobalSearch = new Vector();
    private JDFFrame m_frame;
    
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
        final JLabel findLabel = new JLabel(frame.m_littleBundle.getString("FindWhatKey"));
        box2.add(findLabel);
        
        m_searchTextField = new JTextField();
        final MyDocumentListener searchTextFieldListener = new MyDocumentListener();
        m_searchTextField.getDocument().addDocumentListener(searchTextFieldListener);
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
        m_findNextButton.setEnabled(false);
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
                if (m_vGlobalSearch.size() == 0 || m_frame.fileIsEdited())
                {
                    m_vGlobalSearch.clear();
                    final Enumeration enumer = ((JDFTreeNode) m_frame.getRootNode().getFirstChild()).preorderEnumeration();
                    while (enumer.hasMoreElements())
                    {
                        m_vGlobalSearch.add(0, enumer.nextElement());
                    }
                }
            }
            findStringInTree(((JTextField) ((Box) ((Box) getContentPane().getComponent(1)).getComponent(1))
                    .getComponent(1)).getText(), forwardDirection);
        }
                
        else if (getSearchComponent().equals("NeighbourTree"))
            m_frame.m_topTabs.m_inOutScrollPane.findStringInNeighbourTree(((JTextField) ((Box) ((Box) getContentPane().getComponent(1))
                    .getComponent(1)).getComponent(1)).getText(), forwardDirection);
    }

    /**
     * Method findStringInTree.
     * search a string in the main tree view
     * @param inString
     * @param forwardDirection
     */
    private void findStringInTree(String inString, boolean forwardDirection)
    {
        if (inString != null || !inString.equals(JDFConstants.EMPTYSTRING))
        {
            getContentPane().setCursor(JDFFrame.m_waitCursor);
            final String searchString = inString.toUpperCase();
            final JDFTreeNode searchNode = (JDFTreeNode) (m_frame.m_treeArea.getSelectionPath()).getLastPathComponent();
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
            getContentPane().setCursor(JDFFrame.m_readyCursor);
        }
    }
    
}
