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
 * @author BIskey ThunellE AnderssonA
 * Choose format of new Golden Ticket file
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NewGTChooser extends JPanel implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 457494844472759130L;
    
    private String fileType;
//    private JRadioButton radioBaseButton;
//    private JRadioButton radioJMFButton;
//    private JRadioButton radioMISButton;
    private JRadioButton radioMISCPButton;
//    private JRadioButton radioGTButton;
    private ButtonGroup fileTypeGroup;
    
    
    /*
    private final JTextField MISLevel;
    private final JTextField JMFLevel;
    */
    
    


    public NewGTChooser()
    {
        super();
        //This is going to change to the number of GT's available + 1 (for heading). There are 1 ticket(s) right now.
        setLayout(new GridLayout(2,1)); 
        this.fileType = "Base";
        buttoninit();
        setVisible(true);
    }
    
    /**
     * Method init.
     * Create the new file chooser
     */
    private void buttoninit()
    {
        final ResourceBundle littleBundle = Editor.getBundle();
        final JLabel label = new JLabel(littleBundle.getString("ChooseNewGTKey"));
        add(label);
        
        fileTypeGroup = new ButtonGroup();
        
        /*
         * List GoldenTickets available.
         * Updated 2007-09-24
         * 1. MISCP
         */
    
        radioMISCPButton = new JRadioButton(littleBundle.getString("NewMISCPTicket"));
        radioMISCPButton.setActionCommand(littleBundle.getString("NewMISCPTicket"));
        radioMISCPButton.addActionListener(this);
        add(radioMISCPButton);
        fileTypeGroup.add(radioMISCPButton);
    }
    
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        final Object src=e.getSource();
        
        //here is where you would create the ticket instead of JDFFrame.java??
        if (src==radioMISCPButton)
        {
        	this.fileType = "MISCP";
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
