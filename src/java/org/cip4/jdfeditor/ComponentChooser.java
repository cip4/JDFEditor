package org.cip4.jdfeditor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/*
 * ComponentChooser.java
 * @author SvenoniusI
 */

public class ComponentChooser extends JPanel implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8989231145248120133L;
    
    private String component;
    private ResourceBundle bundle;
    private JRadioButton processButton;
    private JRadioButton inOutButton;
    private JRadioButton jdfTreeButton;
    private ButtonGroup rbg;
    
    public ComponentChooser(ResourceBundle _bundle)
    {
        super();
        this.setLayout(new GridLayout(4,1));
        this.bundle = _bundle;
        this.component = _bundle.getString("ProcessViewKey");
        init();
        setVisible(true);
    }
    
    private void init()
    {
        final JLabel label = new JLabel("Choose Component to Print:");
        add(label);
        
        rbg = new ButtonGroup();
        
        processButton = new JRadioButton(bundle.getString("ProcessViewKey"));
        processButton.setActionCommand(bundle.getString("ProcessViewKey"));
        rbg.add(processButton);
        final JDFFrame frame = Editor.getFrame();
        if (frame.m_topTabs.processAreaIsNull())
            processButton.setEnabled(false);
        else
        {
            processButton.setEnabled(true);
            processButton.setSelected(true);
        }
        processButton.addActionListener(this);
        add(processButton);
        
        inOutButton = new JRadioButton(bundle.getString("NextNeighbourKey"));
        inOutButton.setActionCommand(bundle.getString("NextNeighbourKey"));
        rbg.add(inOutButton);
        if (frame.m_topTabs.inOutIsNull())
            inOutButton.setEnabled(false);
        else
        {
            inOutButton.setEnabled(true);
            if (frame.m_topTabs.processAreaIsNull())
                inOutButton.setSelected(true);
        }
        inOutButton.addActionListener(this);
        add(inOutButton);
        
        jdfTreeButton = new JRadioButton(bundle.getString("TreeViewKey"));
        jdfTreeButton.setActionCommand(bundle.getString("TreeViewKey"));
        rbg.add(jdfTreeButton);
        if (frame.m_treeArea.jdfTreeIsNull())
            jdfTreeButton.setEnabled(false);
        else
        {
            jdfTreeButton.setEnabled(true);
            if (frame.m_topTabs.processAreaIsNull()
                && frame.m_topTabs.inOutIsNull())
                jdfTreeButton.setSelected(true);
        }
        jdfTreeButton.addActionListener(this);
        add(jdfTreeButton);
    }
    
    public String getComponent()
    {
        return this.component;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        e.getClass(); // nop
        this.component = rbg.getSelection().getActionCommand();
    }
}
