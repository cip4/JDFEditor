package org.cip4.tools.jdfeditor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ThunellE
 * Which string to search for.
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

    public void actionPerformed(final ActionEvent e)
    {
    }
}
