package org.cip4.jdfeditor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ThunellE
 * Which string to search for
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SearchStringChooser extends JPanel implements ActionListener 
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4921363479038038809L;
    
    private ResourceBundle littleBundle;
    private String searchString;
    private JTextField textField;
    private JLabel infoLabel;
    //private JButton findNextButton, cancelButton;

    /**
     * Constructor for SearchStringChooser.
     */
    public SearchStringChooser(ResourceBundle _littleBundle)
    {
        super();
        setLayout(new GridLayout(3,1));
        this.littleBundle = _littleBundle;
        init();
        setVisible(true);
    }
    
    /**
     * Method init.
     * draw the stringChooser
     * 
     */
    public void init()
    {
        final JLabel label = new JLabel(littleBundle.getString("FindKey"));
        add(label);
        
        textField = new JTextField();
        add(textField);
        
        infoLabel = new JLabel();
        add(infoLabel);
    }
    
    /**
     * Method getSearchString.
     * the string to search for
     * @return String
     */
    public String getSearchString()
    {
        searchString = textField.getText();
        return searchString;
    }

    /**
     * Method setInfoLabel.
     */
    public void setInfoLabel()
    {
        infoLabel.setText(littleBundle.getString("StringNotFoundKey"));
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed (ActionEvent e)
    {
        e.getID(); // make compiler happy
        //
    }
}
