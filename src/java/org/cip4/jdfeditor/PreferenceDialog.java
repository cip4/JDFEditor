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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
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

    JPanel gen;
    JPanel lang;
    JPanel lnf;
    JPanel dir;
    JPanel icon;
    JPanel send;

    public boolean useSchema =false;
    private JCheckBox boxSchema = null;
    public File schemaFile=null;
    private JButton schemaBrowse; 
    private JTextField schemaPath;

    private JButton defaultIconButton;
    private JButton changeIconButton;
    private JButton applyLnFButton;
    private JCheckBox readOnly;
    private JCheckBox valOpen;
    private JComboBox boxFontSize;
    private int fontSize;
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
    private String currMethodSendToDevice;
    private boolean currValidate;
    private boolean currReadOnly;
    protected Cursor readyCursor;
    protected Cursor waitCursor;
    private final int GEN_INDEX = 0;
    private final int LANG_INDEX = 1;
    private final int LNF_INDEX = 2;
    private final int DIR_INDEX = 3;
    private final int ICON_INDEX = 4;
    private final int SEND_INDEX = 5;
    private final UIManager.LookAndFeelInfo aLnF[] = UIManager.getInstalledLookAndFeels();
    private ResourceBundle littleBundle;
    private String[] iconStrings;

        
    public PreferenceDialog(ResourceBundle bundle)
    {
        super();
        this.littleBundle = bundle;
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
        iniFile.writeINI(f.m_menuBar);
        f.applyLookAndFeel(this);
    }
    private void applyFontSize()
    {
        String s=(String)boxFontSize.getSelectedItem();
        fontSize=Integer.parseInt(s);
    }
    
    private void init()
    {
        final INIReader iniFile=Editor.getIniFile();
        this.readyCursor = Cursor.getDefaultCursor();
        this.waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        this.currLang = iniFile.getLanguage();
        this.currLNF = iniFile.getLookAndFeel();
        this.iconStrings = iniFile.getIconStrings();
        this.currValidate = iniFile.getAutoVal();
        this.currReadOnly = iniFile.getReadOnly();
        this.currMethodSendToDevice = iniFile.getMethodSendToDevice();
        useSchema=iniFile.getUseSchema(); 
        schemaFile=iniFile.getSchemaURL();

        
        this.setPreferredSize(new Dimension(390, 380));
        this.addMouseListener(new TabListener());
        drawPane();
        this.setVisible(true);
    }
    
    private void drawPane()
    {
        this.setCursor(waitCursor);
                
        gen = createGeneralPref();
        this.addTab(littleBundle.getString("GeneralKey"), null, gen, littleBundle.getString("GeneralKey"));
        this.setComponentAt(GEN_INDEX, gen);
        this.setSelectedIndex(GEN_INDEX);
        
        lang = createLanguagePref();
        this.addTab(littleBundle.getString("LanguageKey"), null, lang, littleBundle.getString("LanguageKey"));
        this.setComponentAt(LANG_INDEX, lang);
        

        lnf = createLnFPref();
        this.addTab(littleBundle.getString("LookAndFeelKey"), null, lnf, littleBundle.getString("LookAndFeelKey"));
        this.setComponentAt(LNF_INDEX, lnf);
        

        dir = createDirPref();
        this.addTab(littleBundle.getString("DirectoriesKey"), null, dir, littleBundle.getString("DirectoriesKey"));
        this.setComponentAt(DIR_INDEX, dir);
        
        icon = createIconPref();
        this.addTab(littleBundle.getString("IconsKey"), null, icon, littleBundle.getString("IconsKey"));
        this.setComponentAt(ICON_INDEX, icon);
        
        //20040905 MRE added
        send = createSendToDevicePref();
        this.addTab(littleBundle.getString("SendToDeviceKey"), null, send, littleBundle.getString("SendToDeviceKey"));
        this.setComponentAt(SEND_INDEX, send);
        
        this.setCursor(readyCursor);
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
        readOnly = new JCheckBox(littleBundle.getString("OpenReadOnlyKey"), currReadOnly);
        Dimension d = readOnly.getPreferredSize();
        readOnly.setBounds(10, y, d.width, d.height);
        readOnly.addActionListener(this);
        genPanel.add(readOnly);        
        
        y += d.height + 10;
        valOpen = new JCheckBox(littleBundle.getString("OpenAutoValKey"), currValidate);
        d = valOpen.getPreferredSize();
        valOpen.setBounds(10, y, d.width, d.height);
        valOpen.addActionListener(this);
        genPanel.add(valOpen);

       
        y += d.height + 10;
        boxSchema = new JCheckBox(littleBundle.getString("UseSchemaKey"), useSchema);
        boxSchema.addActionListener(this);
        d = boxSchema.getPreferredSize();
        boxSchema.setBounds(10, y, d.width, d.height);
        genPanel.add(boxSchema);
        
        schemaPath = new JTextField(35);
        if(schemaFile !=null)
            schemaPath.setText(schemaFile.getAbsolutePath());

        d = schemaPath.getPreferredSize();
        y += d.height + 10;
        schemaPath.setBounds(10, y, d.width, d.height);
        genPanel.add(schemaPath);
        
        schemaBrowse = new JButton(littleBundle.getString("BrowseKey"));
        schemaBrowse.setPreferredSize(new Dimension(85,22));
        schemaBrowse.addActionListener(this);
        d = schemaBrowse.getPreferredSize();
        y += d.height + 10;
        schemaBrowse.setBounds(10, y, d.width, d.height);
        genPanel.add(schemaBrowse);
        setVisible(true);
  
                
        Vector sizes=new Vector();
        sizes.add("10");
        sizes.add("12");
        sizes.add("14");
        
        y += d.height + 10;
        JLabel fl=new JLabel();
        fl.setText(littleBundle.getString("FontSizeKey"));
        d=fl.getPreferredSize();
        fl.setBounds(10,y,d.width,d.height);
        genPanel.add(fl);
        
        fontSize=Editor.getIniFile().getFontSize();
        y += d.height + 10;
        boxFontSize = new JComboBox(sizes);
        boxFontSize.setSelectedItem(String.valueOf(fontSize));
        d = boxFontSize.getPreferredSize();
        boxFontSize.setBounds(10, y, d.width, d.height);
        boxFontSize.addActionListener(this);
        genPanel.add(boxFontSize);
        
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
                iniFile.writeINI(Editor.getFrame().m_menuBar);
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
                        iniFile.writeINI(Editor.getFrame().m_menuBar);
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
            if (getSelectedIndex() == GEN_INDEX)
            {
                setCursor(waitCursor);
                setComponentAt(GEN_INDEX, gen);
            }
            
            else if(getSelectedIndex() == LANG_INDEX)
            {
                setCursor(waitCursor);
                setComponentAt(LANG_INDEX, lang);
            }
            
            else if(getSelectedIndex() == LNF_INDEX)
            {
                setCursor(waitCursor);
                setComponentAt(LNF_INDEX, lnf);
            }
            
            else if(getSelectedIndex() == DIR_INDEX)
            {
                setCursor(waitCursor);
                setComponentAt(DIR_INDEX, dir);
            }
            
            else if(getSelectedIndex() == ICON_INDEX)
            {
                setCursor(waitCursor);
                setComponentAt(ICON_INDEX, icon);
            }
            else if(getSelectedIndex() == SEND_INDEX)
            {
                setCursor(waitCursor);
                setComponentAt(SEND_INDEX, send);
            }
            setCursor(readyCursor);
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
        
        else if (source == readOnly)
            currReadOnly = readOnly.isSelected();
        
        else if (source == valOpen)
            currValidate = valOpen.isSelected();
        
        else if (source == boxSchema)
        {
            useSchema = boxSchema.isSelected();
        }
        else if (source == schemaBrowse)
        {
            final EditorFileChooser files = new EditorFileChooser(schemaFile,"xsd",littleBundle);
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

    ////////////////////////////////////////////////////////////
    /**
     * 
     */
    public void writeToIni()
    {
        final INIReader iniFile=Editor.getIniFile();
        iniFile.setUseSchema(useSchema);
        iniFile.setSchemaURL(schemaFile);
        iniFile.setFontSize(getFontSize());
        iniFile.setLanguage(getLanguage());
        iniFile.setReadOnly(getReadOnly());
        iniFile.setAutoVal(getAutoVal());
        iniFile.setIconStrings(getIconStrings());
        iniFile.setMethodSendToDevice(getMethodSendToDevice());
        iniFile.setLookAndFeel(getLNF());
        
    }
}
