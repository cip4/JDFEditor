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
    private JRadioButton radioJMFButton;
//    private JRadioButton radioMISButton;
    private JRadioButton radioMISCPButton;
//    private JRadioButton radioGTButton;
    private ButtonGroup fileTypeGroup;

    /**
     * Method NewFileChooser.
     * @param littleBundle
     */
    public NewGTChooser()
    {
        super();
        //This is going to change to the number of GT's available + 1 (for heading). There are 1 ticket(s) right now.
        setLayout(new GridLayout(2,1)); 
        this.fileType = "Base";
        init();
        setVisible(true);
    }

    /**
     * Method init.
     * Create the new file chooser
     */
    private void init()
    {
        final ResourceBundle littleBundle = Editor.getBundle();
        final JLabel label = new JLabel(littleBundle.getString("ChooseNewGTKey"));
        add(label);
        
        fileTypeGroup = new ButtonGroup();
        
        /*
         * List GoldenTickets available.
         * Updated 2007-09-24
         * 1. Base
         * 2. JMF
         * 3. MIS
         * 4. MISCP
         */
/*        
        radioBaseButton = new JRadioButton(littleBundle.getString("NewBaseTicket"));
        radioBaseButton.setActionCommand(littleBundle.getString("NewBaseTicket"));
        radioBaseButton.addActionListener(this);
        radioBaseButton.setSelected(true);
        add(radioBaseButton);
        fileTypeGroup.add(radioBaseButton);
       
        radioJMFButton = new JRadioButton(littleBundle.getString("NewJMFTicket"));
        radioJMFButton.setActionCommand(littleBundle.getString("NewJMFTicket"));
        radioJMFButton.addActionListener(this);
        add(radioJMFButton);
        fileTypeGroup.add(radioJMFButton);
        
        radioMISButton = new JRadioButton(littleBundle.getString("NewMISTicket"));
        radioMISButton.setActionCommand(littleBundle.getString("NewMISTicket"));
        radioMISButton.addActionListener(this);
        add(radioMISButton);
        fileTypeGroup.add(radioMISButton);
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
/*        if(src==radioBaseButton)
        {
            this.fileType = "Base";
        }
        else*/ 
        if (src==radioJMFButton)
        {
            this.fileType = "JMF";
        }
        /*else if (src==radioMISButton)
        {
            this.fileType = "MIS";
        }
        */
        else
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
