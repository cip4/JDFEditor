package org.cip4.jdfeditor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.cip4.jdflib.core.JDFElement.EnumVersion;

/**
 * DeviceCapsDialog.java
 * @author Elena Skobchenko
 */

public class FixVersionDialog extends JPanel implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -276165456151780040L;
    
    private ResourceBundle littleBundle;
    private boolean bVersionKeyChosen=false;
    private JComboBox chooseVersion;
    private EnumVersion version = EnumVersion.Version_1_3;
    
    public FixVersionDialog(final JDFFrame parent, final ResourceBundle bundle)
    {
        super();
        this.littleBundle = bundle;

        init();
        
        final String[] options = { littleBundle.getString("FixVersionKey"), littleBundle.getString("CancelKey") };
        
        final int option = JOptionPane.showOptionDialog(parent, this, "Fix Version in file",
            JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            
        if (option == JOptionPane.OK_OPTION)
        {
//            __Lena__ TODO Uncomment in new JDFLib!
//            JDFVersions.setVersion(version.getName());
            bVersionKeyChosen = true;
        }
        else if (option == JOptionPane.CANCEL_OPTION)
        {
            bVersionKeyChosen = false;
        }
        
    }
    
    /**
     * Creates the fields and view for the Merge Dialog.
     */
    private void init()
    {
        final GridBagLayout outLayout = new GridBagLayout(); 
        final GridBagConstraints outConstraints = new GridBagConstraints();
        outConstraints.fill = GridBagConstraints.BOTH;
                
        final JPanel panel = new JPanel();
        outLayout.setConstraints(panel, outConstraints);
        panel.setLayout(outLayout);
      
        
        final JPanel versionPanel = new JPanel();
        versionPanel.setBorder(BorderFactory.createTitledBorder("JDFVersion"));
                
        final Vector allValues = new Vector();
        allValues.addElement("Retain Version");
        allValues.addElement(EnumVersion.Version_1_0.getName());
        allValues.addElement(EnumVersion.Version_1_1.getName());
        allValues.addElement(EnumVersion.Version_1_2.getName());
        allValues.addElement(EnumVersion.Version_1_3.getName());
        chooseVersion = new JComboBox(allValues);
        chooseVersion.setSelectedItem(EnumVersion.Version_1_3.getName());
        chooseVersion.addActionListener(this);
        versionPanel.add(Box.createHorizontalGlue());
        versionPanel.add(chooseVersion);
        versionPanel.add(Box.createHorizontalGlue());
        outLayout.setConstraints(versionPanel, outConstraints);
        
        panel.add(versionPanel);
        
        add(panel);
        setVisible(true);
 
    }
    
      
    public void actionPerformed(ActionEvent e)
    {
        final Object source = e.getSource();
        if (source == chooseVersion)
        {
            
            final String selectedItem = (String)chooseVersion.getSelectedItem();
            if(!selectedItem.startsWith("Retain"))
                version = EnumVersion.getEnum(selectedItem);
            else
                version=null;
        }
    }
 
    public EnumVersion getVersion()
    {
        return version;
    }
     
    public boolean getValidationKeyChosen()
    {
        return bVersionKeyChosen;
    }
}
