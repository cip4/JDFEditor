package org.cip4.jdfeditor;
/*
*
* The CIP4 Software License, Version 1.0
*
*
* Copyright (c) 2001-2006 The International Cooperation for the Integration of 
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.KElement.EnumValidationLevel;
import org.cip4.jdflib.util.StringUtil;

/**
 * DeviceCapsDialog.java
 * @author Elena Skobchenko
 */

public class ValidationDialog extends JPanel implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -267165456151780040L;
    
    private ResourceBundle littleBundle;
    boolean bValidationKeyChosen = false;

    public boolean useSchema =false;
    private JCheckBox boxSchema = null;
    public File schemaFile=null;
    private JButton schemaBrowse; 
    private JTextField schemaPath;
    
    private JComboBox chooseValidLevel;
    private JComboBox chooseVersion;
    public EnumValidationLevel validationLevel = null;
    public EnumVersion version = null;
    private JDFFrame parFrame;
    
    
    public ValidationDialog(final JDFFrame parent, final ResourceBundle bundle)
    {
        super();
        this.littleBundle = bundle;
        parFrame=parent;
        INIReader inifile=Editor.getIniFile();
        version=inifile.getValidationVersion();
        validationLevel=inifile.getValidationLevel();
        useSchema=inifile.getUseSchema(); 
        schemaFile=inifile.getSchemaURL();
         
        init();
        
        final String[] options = { littleBundle.getString("ValidateKey"), littleBundle.getString("CancelKey") };
        
        final int option = JOptionPane.showOptionDialog(parent, this, "Validate file",
            JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            
        if (option == JOptionPane.OK_OPTION)
        {
            JDFElement.setDefaultJDFVersion(version);
            bValidationKeyChosen = true;
            schemaFile=new File(schemaPath.getText());
            if(!schemaFile.canRead())
                schemaFile=null;
        }
        else if (option == JOptionPane.CANCEL_OPTION)
        {
            bValidationKeyChosen = false;
        }
        
    }
    
    /**
     * Creates the fields and view for the Validation Dialog.
     */
    private void init()
    {
        final GridBagConstraints thisConstraints = new GridBagConstraints();
        thisConstraints.fill=GridBagConstraints.VERTICAL;
        thisConstraints.gridx=0;
        thisConstraints.gridy=0;
        
        final GridBagLayout outLayout = new GridBagLayout(); 
        setLayout(outLayout);
        
        final GridBagConstraints outConstraints = new GridBagConstraints();
         
       outConstraints.fill = GridBagConstraints.BOTH;
                   
        final JPanel panel = new JPanel();
        outLayout.setConstraints(panel, outConstraints);
        panel.setLayout(outLayout);
      
        final JPanel validLevelPanel = new JPanel();
        validLevelPanel.setBorder(BorderFactory.createTitledBorder(
                littleBundle.getString("ValidationLevelKey")));
                
        final Vector allowedValues = StringUtil.getNamesVector(EnumValidationLevel.class);
        allowedValues.removeElementAt(0);
        chooseValidLevel = new JComboBox(allowedValues);
        chooseValidLevel.setSelectedItem(validationLevel.getName());
        chooseValidLevel.addActionListener(this);
        validLevelPanel.add(chooseValidLevel);
        outLayout.setConstraints(validLevelPanel, outConstraints);
        
        panel.add(validLevelPanel);
        Dimension d=validLevelPanel.getPreferredSize();
        int y=d.height;
        
        final JPanel versionPanel = new JPanel();
        versionPanel.setBorder(BorderFactory.createTitledBorder("JDFVersion"));
                
        final Vector allValues = new Vector();
        allValues.addElement(EnumVersion.Version_1_0.getName());
        allValues.addElement(EnumVersion.Version_1_1.getName());
        allValues.addElement(EnumVersion.Version_1_2.getName());
        allValues.addElement(EnumVersion.Version_1_3.getName());
        chooseVersion = new JComboBox(allValues);
        chooseVersion.setSelectedItem(version.getName());
        chooseVersion.addActionListener(this);
        versionPanel.add(Box.createHorizontalGlue());
        versionPanel.add(chooseVersion);
        versionPanel.add(Box.createHorizontalGlue());
        outLayout.setConstraints(versionPanel, outConstraints);
        d=versionPanel.getPreferredSize();
        y=Math.max( y, d.height);
        panel.add(versionPanel);
        add(panel,thisConstraints);
        
        final JPanel schemaPanel = new JPanel();
        schemaPanel.setBorder(BorderFactory.createTitledBorder("Schema"));
       
        boxSchema = new JCheckBox(littleBundle.getString("UseSchemaKey"), useSchema);
        boxSchema.addActionListener(this);
        d = boxSchema.getPreferredSize();
        boxSchema.setBounds(10, y+10, d.width, d.height);

        schemaPanel.add(boxSchema);
        schemaPanel.add(Box.createHorizontalStrut(10));
        schemaPath = new JTextField(35);
        if(schemaFile !=null)
            schemaPath.setText(schemaFile.getAbsolutePath());

        schemaPanel.add(schemaPath);
        schemaPanel.add(Box.createHorizontalStrut(10));
        
        schemaBrowse = new JButton(littleBundle.getString("BrowseKey"));
        schemaBrowse.setPreferredSize(new Dimension(85,22));
        schemaBrowse.addActionListener(this);
        schemaPanel.add(schemaBrowse);
        thisConstraints.gridy=1;
   
        add(schemaPanel,thisConstraints);
        setVisible(true);
 
    }

    /////////////////////////////////////////////////////////////////////////////
      
    public void actionPerformed(ActionEvent e)
    {
        final Object source = e.getSource();
        if (source == chooseValidLevel)
        {
            validationLevel = EnumValidationLevel.getEnum(
                                      (String)chooseValidLevel.getSelectedItem());
        }
        else if (source == chooseVersion)
        {
            version = EnumVersion.getEnum((String)chooseVersion.getSelectedItem());
        }
        else if (source == boxSchema)
        {
            useSchema = boxSchema.isSelected();
        }
        else if (source == schemaBrowse)
        {
            final EditorFileChooser files = new EditorFileChooser(schemaFile,"xsd",littleBundle);
            final int option = files.showOpenDialog(parFrame);
            
            if (option == JFileChooser.APPROVE_OPTION)
            {
                schemaFile=files.getSelectedFile();
                schemaPath.setText(schemaFile.getAbsolutePath());
            }
            else if (option == JFileChooser.ERROR_OPTION) 
            {
                JOptionPane.showMessageDialog(parFrame, "File is not accepted", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
     
    public boolean getValidationKeyChosen()
    {
        return bValidationKeyChosen;
    }
}
