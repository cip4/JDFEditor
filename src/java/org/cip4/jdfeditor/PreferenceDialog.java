package org.cip4.jdfeditor;
/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2008 The International Cooperation for the Integration of 
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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.KElement.EnumValidationLevel;
import org.cip4.jdflib.util.StringUtil;

/*
 * PreferenceDialog.java
 * @author SvenoniusI
 * 
 * History:
 * 20040906 MRE preferences for sending to device added
 */

public class PreferenceDialog extends JTabbedPane implements ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1350654061722234773L;

    final private ImageIcon sweFlag = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "SwedishFlag.gif");
    final private ImageIcon engFlag = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "GreatBrittainFlag.gif");
    final private ImageIcon freFlag = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "FrenchFlag.gif");
    final private ImageIcon gerFlag = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "GermanFlag.gif");
    final private ImageIcon spaFlag = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "SpanishFlag.gif");
    final private ImageIcon japFlag = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "JapanFlag.gif");

    protected JPanel[] panels;

    public boolean useSchema =false;
    private JCheckBox boxSchema = null;
    public File schemaFile=null;
    private JButton schemaBrowse; 
    private JTextField schemaPath;

    private JButton defaultIconButton;
    private JButton changeIconButton;
    private JButton applyLnFButton;
    private JCheckBox boxReadOnly;
    private JCheckBox boxNormalizeOpen;
    private JCheckBox boxRemDefault;
    private JCheckBox boxDispDefault;
    private JCheckBox boxEnableExtension;
    private JCheckBox boxLongID;
    private JCheckBox boxRemWhite;
    private JCheckBox boxCheckURL;
    private JCheckBox boxGenerateFull;
    private JCheckBox boxIgnoreDefaults;

    private JCheckBox boxValOpen;
    private JComboBox boxFontName;
    
    private JComboBox boxFontSize;
    private int fontSize;
    
    //Allow for Base, MIS, JMF Level under Golden Ticket Tab
    private JComboBox boxBaseLevel;
    private int BaseLevel; 
    private JComboBox boxMISLevel;
    private int MISLevel;     
    private JComboBox boxJMFLevel;
    private int JMFLevel;

    private JRadioButton swe;
    private JRadioButton eng;
    private JRadioButton fre;
    private JRadioButton ger;
    private JRadioButton spa;
    private JRadioButton jap;
    private JLabel iconPreview;
    private ButtonGroup langGroup = new ButtonGroup();
    private String currLang;
    private String currIcon;
    private String currLNF;
    public String fontName;
    private String currMethodSendToDevice;
    private boolean currRemoveDefault;
    private boolean currDispDefault;
    private boolean currRemoveWhite;
    private boolean currValidate;
    private boolean checkURL;
    private boolean currReadOnly;
    private boolean normalizeOpen;
    private boolean longID;
    private boolean enableExtensions;
    private boolean generateFull;
    private boolean ignoreDefaults;
    private boolean exportValidation;
  
    private JTextField fieldGenericStrings;
    private String genericStrings;

    private JTextField fieldMISURL;
    private String misURL;
    
    private final UIManager.LookAndFeelInfo aLnF[] = UIManager.getInstalledLookAndFeels();
    private ResourceBundle littleBundle;
    private String[] iconStrings;

    private JComboBox chooseValidLevel;
    private JComboBox chooseVersion;
    private JCheckBox boxExportValidation;
    
    public EnumValidationLevel validationLevel = null;
    public EnumVersion validationVersion = null;

    public PreferenceDialog()
    {
        super();
        this.littleBundle = Editor.getBundle();
        init();
    }

    public boolean getReadOnly()
    {
        return this.currReadOnly;
    }

    public boolean getAutoVal()
    {
        return this.currValidate;
    }
    public boolean getCheckURL()
    {
        return this.checkURL;
    }

    public String getLNF()
    {
        return this.currLNF;
    }

    public String getLanguage()
    {
        return this.currLang;
    }

    public String[] getIconStrings()
    {
        return this.iconStrings;
    }

    void setCurrentLNF(String _lnf)
    {
        this.currLNF = _lnf;
    }

    void setCurrentLang(String _lang)
    {
        this.currLang = _lang;
    }
    
    void setMethodSendToDevice(String methodSendToDevice)
    {
        this.currMethodSendToDevice = methodSendToDevice;
    }
    public String getMethodSendToDevice()
    {
        return this.currMethodSendToDevice;
    }

    private void applyLnF()
    {
        final JDFFrame f = Editor.getFrame();
        final INIReader iniFile=Editor.getIniFile();
        iniFile.setLookAndFeel(currLNF);
        iniFile.writeINIFile();
        f.applyLookAndFeel(this);
    }
    private void applyFontSize()
    {
        String s=(String)boxFontSize.getSelectedItem();
        fontSize=Integer.parseInt(s);
    }
    
    private void applyBaseLevel()
    {
    	String s=(String)boxBaseLevel.getSelectedItem();
    	BaseLevel=Integer.parseInt(s);
    }
    
    private void applyMISLevel()
    {
    	String s=(String)boxMISLevel.getSelectedItem();
    	MISLevel=Integer.parseInt(s);
    }
    
    private void applyJMFLevel()
    {
    	String s=(String)boxJMFLevel.getSelectedItem();
    	JMFLevel=Integer.parseInt(s);
    }
    
    private void applyFontName()
    {
        fontName=(String)boxFontName.getSelectedItem();
        if(fontName.equals("...Default"))
            fontName=null;
    }

    private void init()
    {
        final INIReader iniFile=Editor.getIniFile();
        this.currLang = iniFile.getLanguage();
        this.currLNF = iniFile.getLookAndFeel();
        this.iconStrings = iniFile.getIconStrings();
        this.currValidate = iniFile.getAutoVal();
        this.currReadOnly = iniFile.getReadOnly();
        this.currMethodSendToDevice = iniFile.getMethodSendToDevice();
        longID = iniFile.getLongID();
        useSchema=iniFile.getUseSchema(); 
        schemaFile=iniFile.getSchemaURL();
        currRemoveDefault=iniFile.getRemoveDefault();
        currRemoveWhite=iniFile.getRemoveWhite();
        currDispDefault=iniFile.getDisplayDefault();
        checkURL=iniFile.getCheckURL();
        enableExtensions=iniFile.getEnableExtensions();
        genericStrings=iniFile.getGenericAtts();
        generateFull=iniFile.getGenerateFull();
        normalizeOpen=iniFile.getNormalizeOpen();
        ignoreDefaults=iniFile.getIgnoreDefault();

        validationVersion=iniFile.getValidationVersion();
        validationLevel=iniFile.getValidationLevel();
        exportValidation=iniFile.getExportValidation();
        misURL=iniFile.getMISURL();
        
/*        BaseLevel=iniFile.getBaseLevel();
        MISLevel=iniFile.getMISLevel();
        JMFLevel=iniFile.getJMFLevel();*/

        this.setPreferredSize(new Dimension(390, 380));
        this.addMouseListener(new TabListener());
        drawPane();
        this.setVisible(true);
    }

    private void drawPane()
    {
        Editor.setCursor(0,null);

        panels=new JPanel[10];
        int n=0;

        JPanel gen = createGeneralPref();
        prepareTab(n++, gen,"GeneralKey");
        this.setSelectedIndex(0);

        JPanel lang = createLanguagePref();
        prepareTab(n++, lang,"LanguageKey");

        JPanel lnf = createLnFPref();
        prepareTab(n++, lnf,"LookAndFeelKey");

        JPanel dir = createDirPref();
        prepareTab(n++, dir,"DirectoriesKey");

        JPanel icon = createIconPref();
        prepareTab(n++, icon,"IconsKey");

        JPanel send = createSendToDevicePref();
        prepareTab(n++, send,"SendToDeviceKey");

        JPanel font = createFontPref();
        prepareTab(n++, font,"FontOptionsKey");

        JPanel ext = createExtPref();
        prepareTab(n++, ext,"ExtOptionsKey");

        JPanel valid = createValidatePref();
        prepareTab(n++, valid,"ValidateKey");

        JPanel goldenTicket = createGoldenTicketPref();
        prepareTab(n++, goldenTicket,"GoldenTicketKey");
        
        Editor.setCursor(0,null);
    }

    private void prepareTab(int n, JPanel gen, String resKey)
    {
        final String resString = littleBundle.getString(resKey);
        this.addTab(resString, null, gen, resString);
        this.setComponentAt(n, gen);
        panels[n]=gen;
    }

    JPanel createGeneralPref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel genPanel = new JPanel(null);
        genPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GeneralOptionsKey")));

        int y = 30;
        boxReadOnly = new JCheckBox(littleBundle.getString("OpenReadOnlyKey"), currReadOnly);
        Dimension d = boxReadOnly.getPreferredSize();
        boxReadOnly.setBounds(10, y, d.width, d.height);
        boxReadOnly.addActionListener(this);
        genPanel.add(boxReadOnly);        

        y += d.height + 3;
        boxNormalizeOpen = new JCheckBox(littleBundle.getString("NormalizeOpenKey"), normalizeOpen);
        d = boxNormalizeOpen.getPreferredSize();
        boxNormalizeOpen.setBounds(10, y, d.width, d.height);
        boxNormalizeOpen.addActionListener(this);
        genPanel.add(boxNormalizeOpen);        

        y += d.height + 3;
        boxValOpen = new JCheckBox(littleBundle.getString("OpenAutoValKey"), currValidate);
        d = boxValOpen.getPreferredSize();
        boxValOpen.setBounds(10, y, d.width, d.height);
        boxValOpen.addActionListener(this);
        genPanel.add(boxValOpen);

        y += d.height + 3;
        boxDispDefault = new JCheckBox(littleBundle.getString("DisplayDefaultsKey"), currDispDefault);
        d = boxDispDefault.getPreferredSize();
        boxDispDefault.setBounds(10, y, d.width, d.height);
        boxDispDefault.addActionListener(this);
        genPanel.add(boxDispDefault);

        y += d.height + 3;
        boxRemDefault = new JCheckBox(littleBundle.getString("SaveRemoveDefaultsKey"), currRemoveDefault);
        d = boxRemDefault.getPreferredSize();
        boxRemDefault.setBounds(10, y, d.width, d.height);
        boxRemDefault.addActionListener(this);
        genPanel.add(boxRemDefault);

        y += d.height + 3;
        boxRemWhite = new JCheckBox(littleBundle.getString("SaveRemoveWhiteKey"), currRemoveWhite);
        d = boxRemWhite.getPreferredSize();
        boxRemWhite.setBounds(10, y, d.width, d.height);
        boxRemWhite.addActionListener(this);
        genPanel.add(boxRemWhite);

        y += d.height + 3;
        boxLongID = new JCheckBox(littleBundle.getString("LongIDKey"), longID);
        d = boxLongID.getPreferredSize();
        boxLongID.setBounds(10, y, d.width, d.height);
        boxLongID.addActionListener(this);
        genPanel.add(boxLongID);


        y += d.height + 3;
        boxSchema = new JCheckBox(littleBundle.getString("UseSchemaKey"), useSchema);
        boxSchema.addActionListener(this);
        d = boxSchema.getPreferredSize();
        boxSchema.setBounds(10, y, d.width, d.height);
        genPanel.add(boxSchema);

        schemaPath = new JTextField(35);
        if(schemaFile !=null)
            schemaPath.setText(schemaFile.getAbsolutePath());

        d = schemaPath.getPreferredSize();
        y += d.height + 9;
        schemaPath.setBounds(10, y, d.width, d.height);
        genPanel.add(schemaPath);

        schemaBrowse = new JButton(littleBundle.getString("BrowseKey"));
        schemaBrowse.setPreferredSize(new Dimension(85,22));
        schemaBrowse.addActionListener(this);
        d = schemaBrowse.getPreferredSize();
        y += d.height + 9;
        schemaBrowse.setBounds(10, y, d.width, d.height);
        genPanel.add(schemaBrowse);
        setVisible(true);

        main.add(genPanel, BorderLayout.CENTER);

        return main;
    }

    JPanel createFontPref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel genPanel = new JPanel(null);
        genPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("FontOptionsKey")));

        int y = 30;

        Vector sizes=new Vector();
        sizes.add("10");
        sizes.add("12");
        sizes.add("14");

        JLabel fl=new JLabel();
        fl.setText(littleBundle.getString("FontSizeKey"));
        Dimension d=fl.getPreferredSize();
        fl.setBounds(10,y,d.width,d.height);
        genPanel.add(fl);

        fontSize=Editor.getIniFile().getFontSize();
        y += d.height + 9;
        boxFontSize = new JComboBox(sizes);
        boxFontSize.setSelectedItem(String.valueOf(fontSize));
        d = boxFontSize.getPreferredSize();
        boxFontSize.setBounds(10, y, d.width, d.height);
        boxFontSize.addActionListener(this);
        genPanel.add(boxFontSize);

        y += d.height + 9;
        JLabel fnl=new JLabel();
        fnl.setText(littleBundle.getString("FontNameKey"));
        d=fnl.getPreferredSize();
        fnl.setBounds(10,y,d.width,d.height);
        genPanel.add(fnl);

        fontName=Editor.getIniFile().getFontName();
        y += d.height + 9;
        String[] allFontNames_ = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String[] allFontNames=new String[allFontNames_.length+1];
        allFontNames[0]="...Default";
        for(int i=0;i<allFontNames_.length;i++)
            allFontNames[i+1]=allFontNames_[i];

        boxFontName = new JComboBox(allFontNames);

        if(fontName!=null)
            boxFontName.setSelectedItem(fontName);
        d = boxFontName.getPreferredSize();
        boxFontName.setBounds(10, y, d.width, d.height);
        boxFontName.addActionListener(this);
        genPanel.add(boxFontName);

        main.add(genPanel, BorderLayout.CENTER);

        return main;
    }
    /**
     * draw the flags etc. for the language preferences
     * @return
     */
    JPanel createLanguagePref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        final Box northBox = Box.createHorizontalBox();
        northBox.add(Box.createHorizontalStrut(10));
        final String txt = "<html><br>" + littleBundle.getString("LanguageTitleKey") + "<br></html>";
        final JLabel text = new JLabel(txt, SwingConstants.LEFT);
        northBox.add(text);

        main.add(northBox, BorderLayout.NORTH);
        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

        final JPanel langPanel = new JPanel(null);
        langPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("LangSelectKey")));

        int y = 30;
        final Box sweBox = createRadioLang(swe, sweFlag, "sv", y);
        langPanel.add(sweBox);
        y += sweBox.getHeight() + 10;
        final Box engBox = createRadioLang(eng, engFlag, "en", y);
        langPanel.add(engBox);
        y += engBox.getHeight() + 10;
        final Box gerBox = createRadioLang(ger, gerFlag, "de", y);
        langPanel.add(gerBox);
        y += gerBox.getHeight() + 10;
        final Box spaBox = createRadioLang(spa, spaFlag, "es", y);
        langPanel.add(spaBox);
        y += spaBox.getHeight() + 10;
        final Box freBox = createRadioLang(fre, freFlag, "fr", y);
        langPanel.add(freBox);
        y += freBox.getHeight() + 10;
        final Box japBox = createRadioLang(jap, japFlag, "jp", y);
        langPanel.add(japBox);

        main.add(langPanel, BorderLayout.CENTER);

        return main;
    }

    JPanel createLnFPref()
    {
        final ButtonGroup buttonGroup = new ButtonGroup();

        final JPanel main = new JPanel(new BorderLayout());

        final Box northBox = Box.createHorizontalBox();
        northBox.add(Box.createHorizontalStrut(10));
        final String txt = "<html><br>" + littleBundle.getString("LnFTitleKey") + "<br></html>";
        final JLabel text = new JLabel(txt, SwingConstants.LEFT);
        northBox.add(text);

        main.add(northBox, BorderLayout.NORTH);
        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

        final JPanel lnfPanel = new JPanel();
        lnfPanel.setLayout(null);
        lnfPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("LnFSelectKey")));
        int y = 30;

        for (int i = 0; i < aLnF.length; i++)
        {
            final String lnfStr = aLnF[i].getClassName();
            final boolean sel = lnfStr.equals(currLNF) ? true : false;
            final JRadioButton jrb = new JRadioButton(aLnF[i].getName(), sel);
            jrb.setActionCommand(lnfStr);
            jrb.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    setCurrentLNF(e.getActionCommand().toString());
                }
            });
            buttonGroup.add(jrb);
            final Dimension d = jrb.getPreferredSize();
            jrb.setBounds(10, y, d.width, d.height);
            lnfPanel.add(jrb);
            y += d.height + 10;
        }
        applyLnFButton = new JButton(littleBundle.getString("ApplyKey"));
        final Dimension d = applyLnFButton.getPreferredSize();
        applyLnFButton.setBounds(20, y + 10, d.width, d.height);
        applyLnFButton.addActionListener(this);
        lnfPanel.add(applyLnFButton);
        main.add(lnfPanel, BorderLayout.CENTER);

        return main;
    }


    JPanel createDirPref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel dirPanel = new JPanel(null);
        dirPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("DefaultDirsKey")));

        main.add(dirPanel, BorderLayout.CENTER);

        return main;
    }

    private JPanel createExtPref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("ExtOptionsKey")));

        int y = 30;
        boxEnableExtension = new JCheckBox(littleBundle.getString("EnableExtensionKey"), enableExtensions);
        Dimension d = boxEnableExtension.getPreferredSize();
        boxEnableExtension.setBounds(10, y, d.width, d.height);
        boxEnableExtension.addActionListener(this);
        panel.add(boxEnableExtension);        


        main.add(panel, BorderLayout.CENTER);       
        return main;
    }

    private JPanel createValidatePref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("ValidateKey")));

        int y = 30;
        JLabel label=new JLabel(littleBundle.getString("DevCapGenericAttrKey"));
        Dimension d = label.getPreferredSize();
        label.setBounds(10,y,d.width,d.height);
        panel.add(label);        
        y+=15;

        fieldGenericStrings = new JTextField(genericStrings);
        fieldGenericStrings.setAutoscrolls(true);
        fieldGenericStrings.setEditable(true);
//      TODO multiline...

        d = fieldGenericStrings.getPreferredSize();
        fieldGenericStrings.setBounds(10, y, Math.max(200,d.width), d.height);
        fieldGenericStrings.addActionListener(this);
        panel.add(fieldGenericStrings);        
        y+=30;

        boxGenerateFull = new JCheckBox(littleBundle.getString("GenerateFullKey"), generateFull);
        d = boxGenerateFull.getPreferredSize();
        boxGenerateFull.setBounds(10, y, d.width, d.height);
        boxGenerateFull.addActionListener(this);
        panel.add(boxGenerateFull);        

        y += d.height + 3;
        boxCheckURL = new JCheckBox(littleBundle.getString("CheckURLKey"), checkURL);
        d = boxCheckURL.getPreferredSize();
        boxCheckURL.setBounds(10, y, d.width, d.height);
        boxCheckURL.addActionListener(this);
        panel.add(boxCheckURL);

        y += d.height + 3;
        boxIgnoreDefaults = new JCheckBox(littleBundle.getString("IgnoreDefaultsKey"), ignoreDefaults);
        d = boxIgnoreDefaults.getPreferredSize();
        boxIgnoreDefaults.setBounds(10, y, d.width, d.height);
        boxIgnoreDefaults.addActionListener(this);
        panel.add(boxIgnoreDefaults);

        y += d.height + 3;
        boxExportValidation = new JCheckBox(littleBundle.getString("ExportValidationKey"), exportValidation);
        d = boxExportValidation.getPreferredSize();
        boxExportValidation.setBounds(10, y, d.width, d.height);
        boxExportValidation.addActionListener(this);
        panel.add(boxExportValidation);

        y += d.height + 3;
        final Vector allowedValues = StringUtil.getNamesVector(EnumValidationLevel.class);
        chooseValidLevel = new JComboBox(allowedValues);
        chooseValidLevel.setSelectedItem(validationLevel.getName());
        chooseValidLevel.addActionListener(this);
        d = chooseValidLevel.getPreferredSize();

        final JPanel validLevelPanel = new JPanel();
        validLevelPanel.setBorder(BorderFactory.createTitledBorder(
                littleBundle.getString("ValidationLevelKey")));
        validLevelPanel.add(chooseValidLevel);
        d = validLevelPanel.getPreferredSize();
        validLevelPanel.setBounds(10, y, d.width, d.height);
        panel.add(validLevelPanel);
        y+=d.height+3;

        final Vector<String> allValues = new Vector<String>();
        allValues.addElement(EnumVersion.Version_1_0.getName());
        allValues.addElement(EnumVersion.Version_1_1.getName());
        allValues.addElement(EnumVersion.Version_1_2.getName());
        allValues.addElement(EnumVersion.Version_1_3.getName());
        final JPanel versionPanel = new JPanel();
        versionPanel.setBorder(BorderFactory.createTitledBorder("JDFVersion"));

        chooseVersion = new JComboBox(allValues);
        chooseVersion.setSelectedItem(validationVersion==null ? EnumVersion.getEnum(null).getName() : validationVersion.getName());
        chooseVersion.addActionListener(this);
        versionPanel.add(Box.createHorizontalGlue());
        versionPanel.add(chooseVersion);
        versionPanel.add(Box.createHorizontalGlue());
        //   outLayout.setConstraints(versionPanel, outConstraints);
        d=versionPanel.getPreferredSize();
        versionPanel.setBounds(10, y, d.width, d.height);
        panel.add(versionPanel);

        main.add(panel, BorderLayout.CENTER);       
        return main;
    }
    
    private JPanel createGoldenTicketPref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GoldenTicketKey")));

        //Adds the MISURL to the GoldenTicket Tab
        int y = 30;
        JLabel label=new JLabel(littleBundle.getString("MISURLKey"));
        Dimension d = label.getPreferredSize();
        label.setBounds(10,y,d.width,d.height);
        panel.add(label);      
        y+=15;
       
        fieldMISURL = new JTextField(misURL);
        fieldMISURL.setEditable(true);
        fieldMISURL.setAutoscrolls(true);

        d = fieldMISURL.getPreferredSize();
        fieldMISURL.setBounds(10, y, Math.max(200,d.width), d.height);
        fieldMISURL.addActionListener(this);
        panel.add(fieldMISURL);
        y+=d.height+3;
        
        Vector level1=new Vector();
        level1.add("1");
        level1.add("2");
        
        Vector level2=new Vector();
        level2.add("1");
        level2.add("2");
        level2.add("3");
        
        //Allow user to enter default Base ICS Level
        JLabel bl=new JLabel();
        bl.setText(littleBundle.getString("BaseLevelKey"));
        d=bl.getPreferredSize();
        bl.setBounds(10,y,d.width,d.height);
        panel.add(bl);

        BaseLevel=Editor.getIniFile().getBaseLevel();
        y += d.height + 3;
        boxBaseLevel = new JComboBox(level1);
        boxBaseLevel.setSelectedItem(String.valueOf(BaseLevel));
        d = boxBaseLevel.getPreferredSize();
        boxBaseLevel.setBounds(10, y, d.width, d.height);
        boxBaseLevel.addActionListener(this);
        panel.add(boxBaseLevel);
        y += d.height + 3;

        //Allow user to enter default MIS ICS Level
        JLabel ml=new JLabel();
        ml.setText(littleBundle.getString("MISLevelKey"));
        d=ml.getPreferredSize();
        ml.setBounds(10,y,d.width,d.height);
        panel.add(ml);

        MISLevel=Editor.getIniFile().getMISLevel();
        y += d.height + 3;
        boxMISLevel = new JComboBox(level2);
        boxMISLevel.setSelectedItem(String.valueOf(MISLevel));
        d = boxMISLevel.getPreferredSize();
        boxMISLevel.setBounds(10, y, d.width, d.height);
        boxMISLevel.addActionListener(this);
        panel.add(boxMISLevel);
        y += d.height + 3;
        
        //Allow user to enter default JMF ICS Level
        JLabel jl=new JLabel();
        jl.setText(littleBundle.getString("JMFLevelKey"));
        d=jl.getPreferredSize();
        jl.setBounds(10,y,d.width,d.height);
        panel.add(jl);

        JMFLevel=Editor.getIniFile().getJMFLevel();
        y += d.height + 3;
        boxJMFLevel = new JComboBox(level1);
        boxJMFLevel.setSelectedItem(String.valueOf(JMFLevel));
        d = boxJMFLevel.getPreferredSize();
        boxJMFLevel.setBounds(10, y, d.width, d.height);
        boxJMFLevel.addActionListener(this);
        panel.add(boxJMFLevel);
        y += d.height + 3;
        
        main.add(panel, BorderLayout.CENTER);       
        return main;
    }

    private Box createRadioLang(JRadioButton jrb, ImageIcon flag, String language, int y)
    {
        String langStr = "";

        if (language.equalsIgnoreCase("sv"))
            langStr = littleBundle.getString("SwedishKey");

        else if (language.equalsIgnoreCase("en"))
            langStr = littleBundle.getString("EnglishKey");

        else if (language.equalsIgnoreCase("de"))
            langStr = littleBundle.getString("GermanKey");

        else if (language.equalsIgnoreCase("es"))
            langStr = littleBundle.getString("SpanishKey");

        else if (language.equalsIgnoreCase("fr"))
            langStr = littleBundle.getString("FrenchKey");

        else if (language.equalsIgnoreCase("jp"))
            langStr = littleBundle.getString("JapaneseKey");

        final boolean sel = language.equalsIgnoreCase(currLang);
        final Box langBox = Box.createHorizontalBox();

        langBox.add(Box.createHorizontalStrut(10));
        langBox.add(new JLabel(flag));
        jrb = new JRadioButton(langStr);
        jrb.setSelected(sel);
        jrb.setActionCommand(language);
        jrb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setCurrentLang(e.getActionCommand().toString());
            }
        });
        langBox.add(jrb);
        langGroup.add(jrb);
        langBox.add(Box.createGlue());

        final Dimension d = langBox.getPreferredSize();
        langBox.setBounds(10, y, d.width, d.height);

        return langBox;
    }

    JPanel createIconPref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel iconPanel = new JPanel(null);
        iconPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("EditorIconsKey")));

        final String[] iconNames = createIconNames();

        currIcon = iconNames[0];
        final JComboBox icoBox = new JComboBox(iconNames);
        icoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        icoBox.addActionListener(this);
        icoBox.setBounds(155, 40, 210, 24);
        iconPanel.add(icoBox);

        iconPreview = new JLabel();
        iconPreview.setHorizontalAlignment(SwingConstants.CENTER);
        setPic((String) icoBox.getSelectedItem());
        iconPreview.setBounds(20, 40, 80, 80);
        iconPreview.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        iconPanel.add(iconPreview);

        defaultIconButton = new JButton(littleBundle.getString("DefaultIconKey"));
        defaultIconButton.addActionListener(this);

        changeIconButton = new JButton(littleBundle.getString("ChangeIconKey"));
        changeIconButton.addActionListener(this);

        final Dimension defaultDim = defaultIconButton.getPreferredSize();
        final Dimension changeDim = changeIconButton.getPreferredSize();
        final int defaultX = 390 - (defaultDim.width + changeDim.width + 35);
        final int changeX = 390 - (changeDim.width + 25);

        defaultIconButton.setBounds(defaultX, 130, defaultDim.width, defaultDim.height);
        changeIconButton.setBounds(changeX, 130, changeDim.width, changeDim.height);
        iconPanel.add(defaultIconButton);
        iconPanel.add(changeIconButton);

        main.add(iconPanel, BorderLayout.CENTER);

        return main;
    }

    private String[] createIconNames()
    {
        final String[] tmp = new String[iconStrings.length];

        for (int i = 0; i < iconStrings.length; i++)
        {
            final int index = iconStrings[i].indexOf("=");
            String name = iconStrings[i].substring(0, index);
            tmp[i] = name;
        }
        Arrays.sort(tmp);

        return tmp;
    }

    private void setPic(String iconName)
    {
        final INIReader iniFile=Editor.getIniFile();
        if (iconName.equals("Attribute with Error"))
            iconPreview.setIcon(iniFile.errAttIcon);

        else if (iconName.equals("Attribute with Error (selected)"))
            iconPreview.setIcon(iniFile.errAttIconS);

        else if (iconName.equals("Element with Error"))
            iconPreview.setIcon(iniFile.errElemIcon);

        else if (iconName.equals("Element with Error (selected)"))
            iconPreview.setIcon(iniFile.errElemIconS);

        else if (iconName.equals("Attribute"))
            iconPreview.setIcon(iniFile.attIcon);

        else if (iconName.equals("Attribute (selected)"))
            iconPreview.setIcon(iniFile.attIconS);

        else if (iconName.equals("Inherited Attribute"))
            iconPreview.setIcon(iniFile.iAttIcon);

        else if (iconName.equals("Inherited Attribute (selected)"))
            iconPreview.setIcon(iniFile.iAttIconS);

        else if (iconName.equals("PartID Key Attribute"))
            iconPreview.setIcon(iniFile.pAttIcon);

        else if (iconName.equals("PartID Key Attribute (selected)"))
            iconPreview.setIcon(iniFile.pAttIconS);

        else if (iconName.equals("Inherited PartID Key Attribute"))
            iconPreview.setIcon(iniFile.iPAttIcon);

        else if (iconName.equals("Inherited PartID Key Attribute (selected)"))
            iconPreview.setIcon(iniFile.iPAttIconS);

        else if (iconName.equals("rRef Attribute"))
            iconPreview.setIcon(iniFile.refAttIcon);

        else if (iconName.equals("rRef Attriubte (selected)"))
            iconPreview.setIcon(iniFile.refAttIconS);

        else if (iconName.equals("Element"))
            iconPreview.setIcon(iniFile.elemIcon);

        else if (iconName.equals("Element (selected)"))
            iconPreview.setIcon(iniFile.elemIconS);

        else if (iconName.equals("JDF Element"))
            iconPreview.setIcon(iniFile.jdfElemIcon);

        else if (iconName.equals("JDF Element (selected)"))
            iconPreview.setIcon(iniFile.jdfElemIconS);

        else if (iconName.equals("Input rRef Element"))
            iconPreview.setIcon(iniFile.rRefInElemIcon);

        else if (iconName.equals("Input rRef Element (selected)"))
            iconPreview.setIcon(iniFile.rRefInElemIconS);

        else if (iconName.equals("Output rRef Element"))
            iconPreview.setIcon(iniFile.rRefOutElemIcon);

        else if (iconName.equals("Output rRef Element (selected)"))
            iconPreview.setIcon(iniFile.rRefOutElemIconS);

        else if (iconName.equals("rRef Element"))
            iconPreview.setIcon(iniFile.rRefElemIcon);

        else if (iconName.equals("rRef Element (selected)"))
            iconPreview.setIcon(iniFile.rRefElemIconS);
    }

    private void setDefaultPic(String iconName)
    {
        final INIReader iniFile=Editor.getIniFile();
        for (int i = 0; i < iconStrings.length; i++)
        {
            final String iconCheck = iconStrings[i].substring(0, iconStrings[i].indexOf("="));
            if (iconCheck.equals(iconName))
            {
                iconStrings[i] = iconName + "=default";
                iniFile.setIconStrings(iconStrings);
                iniFile.writeINIFile();
                iniFile.setIcons();
                setPic(iconName);
                break;
            }
        }

        if (iconName.equals("Attribute with Error"))
            iconPreview.setIcon(iniFile.defaultErrAttIcon);

        else if (iconName.equals("Attribute with Error (selected)"))
            iconPreview.setIcon(iniFile.defaultErrAttIconS);

        else if (iconName.equals("Element with Error"))
            iconPreview.setIcon(iniFile.defaultErrElemIcon);

        else if (iconName.equals("Element with Error (selected)"))
            iconPreview.setIcon(iniFile.defaultErrElemIconS);

        else if (iconName.equals("Attribute"))
            iconPreview.setIcon(iniFile.defaultAttIcon);

        else if (iconName.equals("Attribute (selected)"))
            iconPreview.setIcon(iniFile.defaultAttIconS);

        else if (iconName.equals("Inherited Attribute"))
            iconPreview.setIcon(iniFile.defaultIAttIcon);

        else if (iconName.equals("Inherited Attribute (selected)"))
            iconPreview.setIcon(iniFile.defaultIAttIconS);

        else if (iconName.equals("PartID Key Attribute"))
            iconPreview.setIcon(iniFile.defaultPAttIcon);

        else if (iconName.equals("PartID Key Attribute (selected)"))
            iconPreview.setIcon(iniFile.defaultPAttIconS);

        else if (iconName.equals("Inherited PartID Key Attribute"))
            iconPreview.setIcon(iniFile.defaultIPAttIcon);

        else if (iconName.equals("Inherited PartID Key Attribute (selected)"))
            iconPreview.setIcon(iniFile.defaultIPAttIconS);

        else if (iconName.equals("rRef Attribute"))
            iconPreview.setIcon(iniFile.defaultRefAttIcon);

        else if (iconName.equals("rRef Attriubte (selected)"))
            iconPreview.setIcon(iniFile.defaultRefAttIconS);

        else if (iconName.equals("Element"))
            iconPreview.setIcon(iniFile.defaultElemIcon);

        else if (iconName.equals("Element (selected)"))
            iconPreview.setIcon(iniFile.defaultElemIconS);

        else if (iconName.equals("JDF Element"))
            iconPreview.setIcon(iniFile.defaultJDFElemIcon);

        else if (iconName.equals("JDF Element (selected)"))
            iconPreview.setIcon(iniFile.defaultJDFElemIconS);

        else if (iconName.equals("Input rRef Element"))
            iconPreview.setIcon(iniFile.defaultRefInElemIcon);

        else if (iconName.equals("Input rRef Element (selected)"))
            iconPreview.setIcon(iniFile.defaultRefInElemIconS);

        else if (iconName.equals("Output rRef Element"))
            iconPreview.setIcon(iniFile.defaultRefOutElemIcon);

        else if (iconName.equals("Output rRef Element (selected)"))
            iconPreview.setIcon(iniFile.defaultRefOutElemIconS);

        else if (iconName.equals("rRef Element"))
            iconPreview.setIcon(iniFile.defaultRefElemIcon);

        else if (iconName.equals("rRef Element (selected)"))
            iconPreview.setIcon(iniFile.defaultRefElemIconS);
    }

    private void changeIcon(String s)
    {
        final JFileChooser jfc = new JFileChooser(littleBundle.getString("SelectIconKey"));
        jfc.setMultiSelectionEnabled(false);

        final int answer = jfc.showOpenDialog(null);

        if (answer == JFileChooser.APPROVE_OPTION)
        {
            final String path = jfc.getSelectedFile().getAbsolutePath();
            final INIReader iniFile=Editor.getIniFile();

            if (path != null)
            {
                for (int i = 0; i < iconStrings.length; i++)
                {
                    final String iconCheck = iconStrings[i].substring(0, iconStrings[i].indexOf("="));
                    if (iconCheck.equals(s))
                    {
                        iconStrings[i] = s + "=" + path;
                        iniFile.setIconStrings(iconStrings);
                        iniFile.writeINIFile();
                        iniFile.setIcons();
                        setPic(s);
                        break;
                    }
                }
            }
        }
    }

    // 20040906 MRE
    JPanel createSendToDevicePref()
    {
        final JPanel main = new JPanel(new BorderLayout());

        final ButtonGroup bgSendToDevice = new ButtonGroup();
        boolean selected = false;

        main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
        main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

        final JPanel sendPanel = new JPanel(null);
        sendPanel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("DefaultSendToDeviceKey")));


        if(getMethodSendToDevice().equals("JMF"))
            selected = true;
        final JRadioButton jrbSendJMF = new JRadioButton(littleBundle.getString("sendMethodJMF"), selected);
        Dimension d = jrbSendJMF.getPreferredSize();
        jrbSendJMF.setBounds(10, 40, d.width, d.height);
        bgSendToDevice.add(jrbSendJMF);
        jrbSendJMF.setActionCommand("JMF");
        jrbSendJMF.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setMethodSendToDevice(e.getActionCommand().toString());
            }
        });

        if(getMethodSendToDevice().equals("MIME"))
            selected = true;
        final JRadioButton jrbSendMIME = new JRadioButton(littleBundle.getString("sendMethodMIME"), selected);
        d = jrbSendMIME.getPreferredSize();
        jrbSendMIME.setBounds(10, 60, d.width, d.height);
        bgSendToDevice.add(jrbSendMIME);
        jrbSendMIME.setActionCommand("MIME");
        jrbSendMIME.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setMethodSendToDevice(e.getActionCommand().toString());
            }
        });

        if(getMethodSendToDevice().equals("User"))
            selected = true;
        final JRadioButton jrbSendUser = new JRadioButton(littleBundle.getString("sendMethodUser"), selected);
        d = jrbSendUser.getPreferredSize();
        jrbSendUser.setBounds(10, 80, d.width, d.height);
        bgSendToDevice.add(jrbSendUser);
        jrbSendUser.setActionCommand("User");
        jrbSendUser.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setMethodSendToDevice(e.getActionCommand().toString());
            }
        });

        sendPanel.add(jrbSendJMF);
        sendPanel.add(jrbSendMIME);
        sendPanel.add(jrbSendUser);

        main.add(sendPanel, BorderLayout.CENTER);

        return main;
    }


    class TabListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            e.getID(); // fool compiler;
            final int selectedIndex = getSelectedIndex();
            if(selectedIndex>=0 && selectedIndex<panels.length)
            {
                setComponentAt(selectedIndex, panels[selectedIndex]);
            }
        }
    }

    //////////////////////////////////////////////////////

    public void actionPerformed(ActionEvent e)
    {
        final Object source = e.getSource();
        if (source == defaultIconButton)
            setDefaultPic(currIcon);

        else if (source == changeIconButton)
            changeIcon(currIcon);

        else if (source == applyLnFButton)
            applyLnF();

        else if (source == boxFontSize)
            applyFontSize();
        else if(source==boxBaseLevel)
        {
            applyBaseLevel();
        }
        else if(source==boxMISLevel)
        {
            applyMISLevel();
        }
        else if(source==boxJMFLevel)
        {
            applyJMFLevel();
        }        
        else if (source == boxFontName)
            applyFontName();

        else if (source == boxReadOnly)
            currReadOnly = boxReadOnly.isSelected();

        else if (source == boxNormalizeOpen)
            normalizeOpen = boxNormalizeOpen.isSelected();

        else if (source == boxValOpen)
            currValidate = boxValOpen.isSelected();

        else if (source == boxCheckURL)
            checkURL = boxCheckURL.isSelected();

        else if (source == boxDispDefault)
            currDispDefault = boxDispDefault.isSelected();

        else if (source == boxRemDefault)
            currRemoveDefault = boxRemDefault.isSelected();

        else if (source == boxRemWhite)
            currRemoveWhite = boxRemWhite.isSelected();

        else if (source == boxLongID)
            longID = boxLongID.isSelected();

        else if (source == boxEnableExtension)
            enableExtensions = boxEnableExtension.isSelected();

        else if (source == boxIgnoreDefaults)
            ignoreDefaults = boxIgnoreDefaults.isSelected();

        else if (source == boxGenerateFull)
            generateFull = boxGenerateFull.isSelected();

        else if (source == boxSchema)
        {
            useSchema = boxSchema.isSelected();
        }
        else if (source == schemaBrowse)
        {
            final EditorFileChooser files = new EditorFileChooser(schemaFile,"xsd");
            final int option = files.showOpenDialog(this);

            if (option == JFileChooser.APPROVE_OPTION)
            {
                schemaFile=files.getSelectedFile();
                schemaPath.setText(schemaFile.getAbsolutePath());
            }
            else if (option == JFileChooser.ERROR_OPTION) 
            {
                JOptionPane.showMessageDialog(this, "File is not accepted", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(source==fieldGenericStrings)
        {
            genericStrings=fieldGenericStrings.getText();
        }
        if (source == chooseValidLevel)
        {
            validationLevel = EnumValidationLevel.getEnum((String)chooseValidLevel.getSelectedItem());
        }
        if (source == boxExportValidation)
        {
            exportValidation = boxExportValidation.isSelected();
        }
        else if (source == chooseVersion)
        {
            validationVersion = EnumVersion.getEnum((String)chooseVersion.getSelectedItem());
        }
        else if(source==fieldMISURL)
        {
            misURL=fieldMISURL.getText();
        }
        else if (source.getClass().equals(JComboBox.class))
        {
            final JComboBox jcb = (JComboBox) source;
            currIcon = (String) jcb.getSelectedItem();
            setPic(currIcon);
        }

    }

    /**
     * returns the currently selected font size
     * @return
     */
    public int getFontSize()
    {
        return fontSize;
    }

    public int getBaseLevel()
    {
    	return BaseLevel;
    }
    
    public int getMISLevel()
    {
    	return MISLevel;
    }
    
    public int getJMFLevel()
    {
    	return JMFLevel;
    }
    ////////////////////////////////////////////////////////////
    /**
     * 
     */
    public void writeToIni()
    {
        final INIReader iniFile=Editor.getIniFile();
        iniFile.setUseSchema(useSchema);
        iniFile.setSchemaURL(getSchemaURL());
        iniFile.setFontSize(getFontSize());
        
        iniFile.setBaseLevel(getBaseLevel());
        iniFile.setMISLevel(getMISLevel());
        iniFile.setJMFLevel(getJMFLevel());
        
        iniFile.setLanguage(getLanguage());
        iniFile.setReadOnly(getReadOnly());
        iniFile.setAutoVal(getAutoVal());
        iniFile.setIconStrings(getIconStrings());
        iniFile.setMethodSendToDevice(getMethodSendToDevice());
        iniFile.setLookAndFeel(getLNF());
        iniFile.setFontName(fontName);
        iniFile.setRemoveDefault(currRemoveDefault);
        iniFile.setDisplayDefault(currDispDefault);
        iniFile.setRemoveWhite(currRemoveWhite);
        iniFile.setCheckURL(checkURL);
        iniFile.setLongID(longID);        
        iniFile.setEnableExtensions(enableExtensions);    
        iniFile.setGenerateFull(generateFull);    
        iniFile.setNormalizeOpen(normalizeOpen);    
        iniFile.setIgnoreDefault(ignoreDefaults);   
        iniFile.setValidationLevel(validationLevel);
        iniFile.setValidationVersion(validationVersion);
        iniFile.setExportValidation(exportValidation);
        misURL=fieldMISURL.getText();
        iniFile.setMISURL(misURL);
        genericStrings =fieldGenericStrings.getText();
        final VString genericAttributes = new VString(genericStrings,null);
        genericAttributes.unify();
        iniFile.setGenericAtts(genericAttributes);
        iniFile.writeINIFile();
        //TODO add checkbox
        JDFElement.setLongID(iniFile.getLongID());

    }

    /**
     * @return the newly set schema file, if it is readable
     */
    private File getSchemaURL()
    {
        String s=schemaPath.getText();
        if(s!=null && s.length()!=0)
        {
            File f=new File(s);
            if(f.canRead())
                schemaFile=f;
        }
        return schemaFile;
    }

    /**
     * @return the ignoreDefaults
     */
    public boolean getIgnoreDefaults()
    {
        return ignoreDefaults;
    }

    /**
     * @param ignoreDefaults the ignoreDefaults to set
     */
    public void setIgnoreDefaults(boolean _ignoreDefaults)
    {
        this.ignoreDefaults = _ignoreDefaults;
    }
}
