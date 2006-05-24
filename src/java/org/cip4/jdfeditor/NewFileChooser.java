package org.cip4.jdfeditor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author ThunellE AnderssonA
 * Choose format of new file
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NewFileChooser extends JPanel implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 457494844472759130L;
    
    private String fileType;
    private ResourceBundle littleBundle;
    private JRadioButton radioJDFButton;
    private JRadioButton radioJMFButton;
    private ButtonGroup rbg;

    /**
     * Method NewFileChooser.
     * @param littleBundle
     */
    public NewFileChooser(ResourceBundle _littleBundle)
    {
        super();
        setLayout(new GridLayout(3,1));
        this.littleBundle = _littleBundle;
        this.fileType = "JDF";
        init();
        setVisible(true);
    }

    /**
     * Method init.
     * Create the new file chooser
     */
    public void init()
    {
        final JLabel label = new JLabel(littleBundle.getString("ChooseNewFileKey"));
        add(label);
        
        rbg = new ButtonGroup();
        
        radioJDFButton = new JRadioButton(littleBundle.getString("NewJDFKey"));
        radioJDFButton.setActionCommand(littleBundle.getString("NewJDFKey"));
        radioJDFButton.addActionListener(this);
        radioJDFButton.setSelected(true);
        add(radioJDFButton);
        rbg.add(radioJDFButton);
        
        radioJMFButton = new JRadioButton(littleBundle.getString("NewJMFKey"));
        radioJMFButton.setActionCommand(littleBundle.getString("NewJMFKey"));
        radioJMFButton.addActionListener(this);
        add(radioJMFButton);
        rbg.add(radioJMFButton);
    }
    
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        e.getID(); // fool compiler
        final String command = rbg.getSelection().getActionCommand();
            
            if (command.equals(littleBundle.getString("NewJDFKey")))
            {
                this.fileType = "JDF";
            }
            else
            {
                this.fileType = "JMF";
            }
        
    }
    
    /**
     * Method getSelection.
     * get the format of the file to open
     * @return String
     */
    public String getSelection()
    {
        return fileType;
    }

}
