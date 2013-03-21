package org.cip4.tools.jdfeditor;
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
    private JRadioButton radioJDFButton;
    private JRadioButton radioJMFButton;
    private JRadioButton radioGTButton;
    private ButtonGroup fileTypeGroup;

    /**
     * Method NewFileChooser.
     * @param littleBundle
     */
    public NewFileChooser()
    {
        super();
        setLayout(new GridLayout(4,1));
        this.fileType = "JDF";
        init();
        setVisible(true);
    }

    /**
     * Method init.
     * Create the new file chooser
     */
    private void init()
    {
    	//BMI: Add option for Golden Ticket Option
        final ResourceBundle littleBundle = Editor.getBundle();
        /*
         * Need to add this getString "Golden Ticket" b/c it states right now Do you want to create a new JDF or JMF file?
         */
        final JLabel label = new JLabel(littleBundle.getString("ChooseNewFileKey"));
        add(label);
        
        fileTypeGroup = new ButtonGroup();
        
        radioJDFButton = new JRadioButton(littleBundle.getString("NewJDFKey"));
        radioJDFButton.setActionCommand(littleBundle.getString("NewJDFKey"));
        radioJDFButton.addActionListener(this);
        radioJDFButton.setSelected(true);
        add(radioJDFButton);
        fileTypeGroup.add(radioJDFButton);
        
        radioJMFButton = new JRadioButton(littleBundle.getString("NewJMFKey"));
        radioJMFButton.setActionCommand(littleBundle.getString("NewJMFKey"));
        radioJMFButton.addActionListener(this);
        add(radioJMFButton);
        fileTypeGroup.add(radioJMFButton);
        
        radioGTButton = new JRadioButton(littleBundle.getString("NewGoldenTicket"));
        radioGTButton.setActionCommand(littleBundle.getString("NewGoldenTicket"));
        radioGTButton.addActionListener(this);
        add(radioGTButton);
        fileTypeGroup.add(radioGTButton);
    }
    
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        final Object src=e.getSource();
        if(src==radioJDFButton)
        {
            this.fileType = "JDF";
        }
        else if (src==radioJMFButton)
        {
            this.fileType = "JMF";
        }
        else
        {
        	this.fileType = "GoldenTicket";
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
