/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2014 The International Cooperation for the Integration of 
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
package org.cip4.tools.jdfeditor;

import org.cip4.jdflib.core.JDFElement.EnumValidationLevel;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.util.EnumUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceBundleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

/**
 * @author Dr. Rainer Prosi, Heidelberger Druckmaschinen AG
 * 
 * Jun 8, 2009
 */
/*
 * PreferenceDialog.java
 * 
 * @author SvenoniusI
 * 
 * History: 20040906 MRE preferences for sending to device added
 */

public class PreferenceDialog extends JTabbedPane implements ActionListener
{
	private final SettingService settingService = new SettingService();

	// TODO subclass
	private class ValidationTab
	{

		/**
		 * 
		 */
		public void writeToIni()
		{
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1350654061722234773L;

	final private ImageIcon sweFlag = Editor.getImageIcon(Editor.ICONS_PATH + "SwedishFlag.gif");
	final private ImageIcon engFlag = Editor.getImageIcon(Editor.ICONS_PATH + "GreatBrittainFlag.gif");
	final private ImageIcon freFlag = Editor.getImageIcon(Editor.ICONS_PATH + "FrenchFlag.gif");
	final private ImageIcon gerFlag = Editor.getImageIcon(Editor.ICONS_PATH + "GermanFlag.gif");
	final private ImageIcon spaFlag = Editor.getImageIcon(Editor.ICONS_PATH + "SpanishFlag.gif");
	final private ImageIcon japFlag = Editor.getImageIcon(Editor.ICONS_PATH + "JapanFlag.gif");

	protected JPanel[] panels;

	public boolean useSchema = false;
	private JCheckBox boxSchema = null;
	public File schemaFile = null;
	private JButton schemaBrowse;
	private JTextField schemaPath;

	private JButton defaultIconButton;
	private JButton changeIconButton;
	private JButton applyLnFButton;
	private JCheckBox boxReadOnly;
	private JCheckBox boxNormalizeOpen;
	private JCheckBox boxRemDefault;
	private JCheckBox boxDispDefault;
	private JCheckBox boxLongID;
	private JCheckBox boxUpdateJobID;
	private JCheckBox boxRemWhite;
	private JCheckBox boxCheckURL;
	private JCheckBox boxGenerateFull;
	private JCheckBox boxIgnoreDefaults;
	private JCheckBox cboxIndentSave;

	private JCheckBox boxValOpen;

	// Allow for Base, MIS, JMF Level under Golden Ticket Tab
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
	private final ButtonGroup langGroup = new ButtonGroup();
	private String currLang;
	private String currIcon;
	private String currLNF;
	private String currMethodSendToDevice;
	private boolean currRemoveDefault;
	private boolean currDispDefault;
	private boolean currRemoveWhite;
	private boolean currIndentSave;
	private boolean currValidate;
	private boolean checkURL;
	private boolean currReadOnly;
	private boolean normalizeOpen;
	private boolean longID;
	private boolean updateJobID;
	private boolean enableExtensions;
	private boolean structuredCaps;
	private boolean generateFull;
	private boolean ignoreDefaults;
	private boolean exportValidation;

	private JTextField fieldGenericStrings;
	private String genericStrings;

	private JTextField fieldMISURL;
	private String misURL;

	private final UIManager.LookAndFeelInfo aLnF[] = UIManager.getInstalledLookAndFeels();

	private JComboBox chooseValidLevel;
	private JComboBox chooseVersion;
	private JCheckBox boxExportValidation;

	public EnumValidationLevel validationLevel = null;
	public EnumVersion validationVersion = null;

	private final ValidationTab validTab;

	/**
	 * 
	 */
	public PreferenceDialog()
	{
		super();

		validTab = new ValidationTab();
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

	void setCurrentLNF(final String _lnf)
	{
		this.currLNF = _lnf;
	}

	void setCurrentLang(final String _lang)
	{
		this.currLang = _lang;
	}

	void setMethodSendToDevice(final String methodSendToDevice)
	{
		this.currMethodSendToDevice = methodSendToDevice;
	}

	/**
	 * @return
	 */
	public String getMethodSendToDevice()
	{
		return this.currMethodSendToDevice;
	}

	private void applyLnF()
	{
		final JDFFrame f = Editor.getFrame();
		settingService.setString(SettingKey.GENERAL_LOOK, currLNF);
		f.applyLookAndFeel(this);
	}

	private void applyBaseLevel()
	{
		final String s = (String) boxBaseLevel.getSelectedItem();
		BaseLevel = Integer.parseInt(s);
	}

	private void applyMISLevel()
	{
		final String s = (String) boxMISLevel.getSelectedItem();
		MISLevel = Integer.parseInt(s);
	}

	private void applyJMFLevel()
	{
		final String s = (String) boxJMFLevel.getSelectedItem();
		JMFLevel = Integer.parseInt(s);
	}

	private void init()
	{
		this.currLang = settingService.getString(SettingKey.GENERAL_LANGUAGE);
		this.currLNF = settingService.getString(SettingKey.GENERAL_LOOK);
		this.currValidate = settingService.getBoolean(SettingKey.GENERAL_AUTO_VALIDATE);
		this.currReadOnly = settingService.getBoolean(SettingKey.GENERAL_READ_ONLY);
		this.currMethodSendToDevice = settingService.getString(SettingKey.GENERAL_LOOK);
		this.longID = settingService.getBoolean(SettingKey.GENERAL_LONG_ID);
		this.updateJobID = settingService.getBoolean(SettingKey.GENERAL_UPDATE_JOBID);
		this.useSchema = settingService.getBoolean(SettingKey.GENERAL_USE_SCHEMA);

        if(settingService.getString(SettingKey.VALIDATION_SCHEMA_URL) != null) {
            this.schemaFile = new File(settingService.getString(SettingKey.VALIDATION_SCHEMA_URL));
        }

		this.currRemoveDefault = settingService.getBoolean(SettingKey.GENERAL_REMOVE_DEFAULT);
		this.currRemoveWhite = settingService.getBoolean(SettingKey.GENERAL_REMOVE_WHITE);
		this.currIndentSave = settingService.getBoolean(SettingKey.GENERAL_INDENT);
		this.currDispDefault = settingService.getBoolean(SettingKey.GENERAL_DISPLAY_DEFAULT);
		this.checkURL = settingService.getBoolean(SettingKey.VALIDATION_CHECK_URL);

		this.genericStrings = settingService.getString(SettingKey.VALIDATION_GENERIC_ATTR);
		this.generateFull = settingService.getBoolean(SettingKey.VALIDATION_GENERATE_FULL);
		this.normalizeOpen = settingService.getBoolean(SettingKey.GENERAL_NORMALIZE);
		this.ignoreDefaults = settingService.getBoolean(SettingKey.VALIDATION_IGNORE_DEFAULT);

		this.validationVersion = EnumVersion.getEnum(settingService.getString(SettingKey.VALIDATION_VERSION));
		this.validationLevel = EnumValidationLevel.getEnum(settingService.getString(SettingKey.VALIDATION_LEVEL));
		this.exportValidation = settingService.getBoolean(SettingKey.VALIDATION_EXPORT);
		this.misURL = settingService.getString(SettingKey.GOLDENTICKET_MISURL);

		/*
		 * BaseLevel=iniFile.getBaseLevel(); MISLevel=iniFile.getMISLevel(); JMFLevel=iniFile.getJMFLevel();
		 */

		this.setPreferredSize(new Dimension(390, 380));
		this.addMouseListener(new TabListener());
		drawPane();
		this.setVisible(true);
	}

	private void drawPane()
	{
		Editor.setCursor(0, null);

		panels = new JPanel[10];
		int n = 0;

		final JPanel gen = createGeneralPref();
		prepareTab(n++, gen, "GeneralKey");
		this.setSelectedIndex(0);

		final JPanel lang = createLanguagePref();
		prepareTab(n++, lang, "LanguageKey");

		final JPanel lnf = createLnFPref();
		prepareTab(n++, lnf, "LookAndFeelKey");

		final JPanel dir = createDirPref();
		prepareTab(n++, dir, "DirectoriesKey");

		final JPanel send = createSendToDevicePref();
		prepareTab(n++, send, "SendToDeviceKey");

		final JPanel valid = createValidatePref();
		prepareTab(n++, valid, "ValidateKey");

		final JPanel goldenTicket = createGoldenTicketPref();
		prepareTab(n++, goldenTicket, "GoldenTicketKey");

		Editor.setCursor(0, null);
	}

	private void prepareTab(final int n, final JPanel gen, final String resKey)
	{
		final String resString = ResourceBundleUtil.getMessage(resKey);
		this.addTab(resString, null, gen, resString);
		this.setComponentAt(n, gen);
		panels[n] = gen;
	}

	private JPanel createGeneralPref()
	{
		final JPanel main = new JPanel(new BorderLayout());

		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

		final JPanel genPanel = new JPanel(null);
		genPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("GeneralOptionsKey")));

		int y = 30;
		boxReadOnly = new JCheckBox(ResourceBundleUtil.getMessage("OpenReadOnlyKey"), currReadOnly);
		Dimension d = boxReadOnly.getPreferredSize();
		boxReadOnly.setBounds(10, y, d.width, d.height);
		boxReadOnly.addActionListener(this);
		genPanel.add(boxReadOnly);

		y += d.height + 3;
		boxNormalizeOpen = new JCheckBox(ResourceBundleUtil.getMessage("NormalizeOpenKey"), normalizeOpen);
		d = boxNormalizeOpen.getPreferredSize();
		boxNormalizeOpen.setBounds(10, y, d.width, d.height);
		boxNormalizeOpen.addActionListener(this);
		genPanel.add(boxNormalizeOpen);

		y += d.height + 3;
		boxValOpen = new JCheckBox(ResourceBundleUtil.getMessage("OpenAutoValKey"), currValidate);
		d = boxValOpen.getPreferredSize();
		boxValOpen.setBounds(10, y, d.width, d.height);
		boxValOpen.addActionListener(this);
		genPanel.add(boxValOpen);

		y += d.height + 3;
		boxDispDefault = new JCheckBox(ResourceBundleUtil.getMessage("DisplayDefaultsKey"), currDispDefault);
		d = boxDispDefault.getPreferredSize();
		boxDispDefault.setBounds(10, y, d.width, d.height);
		boxDispDefault.addActionListener(this);
		genPanel.add(boxDispDefault);

		y += d.height + 3;
		boxRemDefault = new JCheckBox(ResourceBundleUtil.getMessage("SaveRemoveDefaultsKey"), currRemoveDefault);
		d = boxRemDefault.getPreferredSize();
		boxRemDefault.setBounds(10, y, d.width, d.height);
		boxRemDefault.addActionListener(this);
		genPanel.add(boxRemDefault);

		y += d.height + 3;
		boxRemWhite = new JCheckBox(ResourceBundleUtil.getMessage("SaveRemoveWhiteKey"), currRemoveWhite);
		d = boxRemWhite.getPreferredSize();
		boxRemWhite.setBounds(10, y, d.width, d.height);
		boxRemWhite.addActionListener(this);
		genPanel.add(boxRemWhite);

		y += d.height + 3;
		cboxIndentSave = new JCheckBox(ResourceBundleUtil.getMessage("IndentSave"), currIndentSave);
		d = cboxIndentSave.getPreferredSize();
		cboxIndentSave.setBounds(10, y, d.width, d.height);
		cboxIndentSave.addActionListener(this);
		genPanel.add(cboxIndentSave);

		y += d.height + 3;
		boxLongID = new JCheckBox(ResourceBundleUtil.getMessage("LongIDKey"), longID);
		d = boxLongID.getPreferredSize();
		boxLongID.setBounds(10, y, d.width, d.height);
		boxLongID.addActionListener(this);
		genPanel.add(boxLongID);

		y += d.height + 3;
		boxUpdateJobID = new JCheckBox(ResourceBundleUtil.getMessage("UpdateJobIDKey"), updateJobID);
		d = boxUpdateJobID.getPreferredSize();
		boxUpdateJobID.setBounds(10, y, d.width, d.height);
		boxUpdateJobID.addActionListener(this);
		genPanel.add(boxUpdateJobID);

		y += d.height + 3;
		boxSchema = new JCheckBox(ResourceBundleUtil.getMessage("UseSchemaKey"), useSchema);
		boxSchema.addActionListener(this);
		d = boxSchema.getPreferredSize();
		boxSchema.setBounds(10, y, d.width, d.height);
		genPanel.add(boxSchema);

		schemaPath = new JTextField(35);
		if (schemaFile != null)
		{
			schemaPath.setText(schemaFile.getAbsolutePath());
		}

		d = schemaPath.getPreferredSize();
		y += d.height + 9;
		schemaPath.setBounds(10, y, d.width, d.height);
		genPanel.add(schemaPath);

		schemaBrowse = new JButton(ResourceBundleUtil.getMessage("BrowseKey"));
		schemaBrowse.setPreferredSize(new Dimension(85, 22));
		schemaBrowse.addActionListener(this);
		d = schemaBrowse.getPreferredSize();
		y += d.height + 9;
		schemaBrowse.setBounds(10, y, d.width, d.height);
		genPanel.add(schemaBrowse);
		setVisible(true);

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
		final String txt = "<html><br>" + ResourceBundleUtil.getMessage("LanguageTitleKey") + "<br></html>";
		final JLabel text = new JLabel(txt, SwingConstants.LEFT);
		northBox.add(text);

		main.add(northBox, BorderLayout.NORTH);
		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

		final JPanel langPanel = new JPanel(null);
		langPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("LangSelectKey")));

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
		final String txt = "<html><br>" + ResourceBundleUtil.getMessage("LnFTitleKey") + "<br></html>";
		final JLabel text = new JLabel(txt, SwingConstants.LEFT);
		northBox.add(text);

		main.add(northBox, BorderLayout.NORTH);
		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

		final JPanel lnfPanel = new JPanel();
		lnfPanel.setLayout(null);
		lnfPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("LnFSelectKey")));
		int y = 30;

		for (int i = 0; i < aLnF.length; i++)
		{
			final String lnfStr = aLnF[i].getClassName();
			final boolean sel = lnfStr.equals(currLNF) ? true : false;
			final JRadioButton jrb = new JRadioButton(aLnF[i].getName(), sel);
			jrb.setActionCommand(lnfStr);
			jrb.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent e)
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
		applyLnFButton = new JButton(ResourceBundleUtil.getMessage("ApplyKey"));
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
		dirPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("DefaultDirsKey")));

		main.add(dirPanel, BorderLayout.CENTER);

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
		panel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("ValidateKey")));

		int y = 30;
		final JLabel label = new JLabel(ResourceBundleUtil.getMessage("DevCapGenericAttrKey"));
		Dimension d = label.getPreferredSize();
		label.setBounds(10, y, d.width, d.height);
		panel.add(label);
		y += 15;

		fieldGenericStrings = new JTextField(genericStrings);
		fieldGenericStrings.setAutoscrolls(true);
		fieldGenericStrings.setEditable(true);
		// TODO multiline...

		d = fieldGenericStrings.getPreferredSize();
		fieldGenericStrings.setBounds(10, y, Math.max(200, d.width), d.height);
		fieldGenericStrings.addActionListener(this);
		panel.add(fieldGenericStrings);
		y += 30;

		boxGenerateFull = new JCheckBox(ResourceBundleUtil.getMessage("GenerateFullKey"), generateFull);
		d = boxGenerateFull.getPreferredSize();
		boxGenerateFull.setBounds(10, y, d.width, d.height);
		boxGenerateFull.addActionListener(this);
		panel.add(boxGenerateFull);

		y += d.height + 3;
		boxCheckURL = new JCheckBox(ResourceBundleUtil.getMessage("CheckURLKey"), checkURL);
		d = boxCheckURL.getPreferredSize();
		boxCheckURL.setBounds(10, y, d.width, d.height);
		boxCheckURL.addActionListener(this);
		panel.add(boxCheckURL);

		y += d.height + 3;
		boxIgnoreDefaults = new JCheckBox(ResourceBundleUtil.getMessage("IgnoreDefaultsKey"), ignoreDefaults);
		d = boxIgnoreDefaults.getPreferredSize();
		boxIgnoreDefaults.setBounds(10, y, d.width, d.height);
		boxIgnoreDefaults.addActionListener(this);
		panel.add(boxIgnoreDefaults);

		y += d.height + 3;
		boxExportValidation = new JCheckBox(ResourceBundleUtil.getMessage("ExportValidationKey"), exportValidation);
		d = boxExportValidation.getPreferredSize();
		boxExportValidation.setBounds(10, y, d.width, d.height);
		boxExportValidation.addActionListener(this);
		panel.add(boxExportValidation);

		y += d.height + 3;
		final VString allowedValues = EnumUtil.getNamesVector(EnumValidationLevel.class);
		chooseValidLevel = new JComboBox(allowedValues);
		chooseValidLevel.setSelectedItem(validationLevel.getName());
		chooseValidLevel.addActionListener(this);
		d = chooseValidLevel.getPreferredSize();

		final JPanel validLevelPanel = new JPanel();
		validLevelPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("ValidationLevelKey")));
		validLevelPanel.add(chooseValidLevel);
		d = validLevelPanel.getPreferredSize();
		validLevelPanel.setBounds(10, y, d.width, d.height);
		panel.add(validLevelPanel);
		y += d.height + 3;

		final Vector<String> allValues = new Vector<String>();
		allValues.addElement(EnumVersion.Version_1_0.getName());
		allValues.addElement(EnumVersion.Version_1_1.getName());
		allValues.addElement(EnumVersion.Version_1_2.getName());
		allValues.addElement(EnumVersion.Version_1_3.getName());
		allValues.addElement(EnumVersion.Version_1_4.getName());
		//		allValues.addElement("2.0");
		final JPanel versionPanel = new JPanel();
		versionPanel.setBorder(BorderFactory.createTitledBorder("JDFVersion"));

		chooseVersion = new JComboBox(allValues);
		chooseVersion.setSelectedItem(validationVersion == null ? EnumVersion.getEnum(null).getName() : validationVersion.getName());
		chooseVersion.addActionListener(this);
		versionPanel.add(Box.createHorizontalGlue());
		versionPanel.add(chooseVersion);
		versionPanel.add(Box.createHorizontalGlue());
		// outLayout.setConstraints(versionPanel, outConstraints);
		d = versionPanel.getPreferredSize();
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
		panel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("GoldenTicketKey")));

		// Adds the MISURL to the GoldenTicket Tab
		int y = 30;
		final JLabel label = new JLabel(ResourceBundleUtil.getMessage("MISURLKey"));
		Dimension d = label.getPreferredSize();
		label.setBounds(10, y, d.width, d.height);
		panel.add(label);
		y += 15;

		fieldMISURL = new JTextField(misURL);
		fieldMISURL.setEditable(true);
		fieldMISURL.setAutoscrolls(true);

		d = fieldMISURL.getPreferredSize();
		fieldMISURL.setBounds(10, y, Math.max(200, d.width), d.height);
		fieldMISURL.addActionListener(this);
		panel.add(fieldMISURL);
		y += d.height + 3;

		final VString level1 = new VString();
		level1.add("1");
		level1.add("2");

		final VString level2 = new VString();
		level2.add("1");
		level2.add("2");
		level2.add("3");

		// Allow user to enter default Base ICS Level
		final JLabel bl = new JLabel();
		bl.setText(ResourceBundleUtil.getMessage("BaseLevelKey"));
		d = bl.getPreferredSize();
		bl.setBounds(10, y, d.width, d.height);
		panel.add(bl);

		BaseLevel = settingService.getInteger(SettingKey.GOLDENTICKET_BASELEVEL);
		y += d.height + 3;
		boxBaseLevel = new JComboBox(level1);
		boxBaseLevel.setSelectedItem(String.valueOf(BaseLevel));
		d = boxBaseLevel.getPreferredSize();
		boxBaseLevel.setBounds(10, y, d.width, d.height);
		boxBaseLevel.addActionListener(this);
		panel.add(boxBaseLevel);
		y += d.height + 3;

		// Allow user to enter default MIS ICS Level
		final JLabel ml = new JLabel();
		ml.setText(ResourceBundleUtil.getMessage("MISLevelKey"));
		d = ml.getPreferredSize();
		ml.setBounds(10, y, d.width, d.height);
		panel.add(ml);

		MISLevel = settingService.getInteger(SettingKey.GOLDENTICKET_MISLEVEL);
		y += d.height + 3;
		boxMISLevel = new JComboBox(level2);
		boxMISLevel.setSelectedItem(String.valueOf(MISLevel));
		d = boxMISLevel.getPreferredSize();
		boxMISLevel.setBounds(10, y, d.width, d.height);
		boxMISLevel.addActionListener(this);
		panel.add(boxMISLevel);
		y += d.height + 3;

		// Allow user to enter default JMF ICS Level
		final JLabel jl = new JLabel();
		jl.setText(ResourceBundleUtil.getMessage("JMFLevelKey"));
		d = jl.getPreferredSize();
		jl.setBounds(10, y, d.width, d.height);
		panel.add(jl);

		JMFLevel = settingService.getInteger(SettingKey.GOLDENTICKET_JMFLEVEL);
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

	private Box createRadioLang(JRadioButton jrb, final ImageIcon flag, final String language, final int y)
	{
		String langStr = "";

		if (language.equalsIgnoreCase("sv"))
		{
			langStr = ResourceBundleUtil.getMessage("SwedishKey");
		}
		else if (language.equalsIgnoreCase("en"))
		{
			langStr = ResourceBundleUtil.getMessage("EnglishKey");
		}
		else if (language.equalsIgnoreCase("de"))
		{
			langStr = ResourceBundleUtil.getMessage("GermanKey");
		}
		else if (language.equalsIgnoreCase("es"))
		{
			langStr = ResourceBundleUtil.getMessage("SpanishKey");
		}
		else if (language.equalsIgnoreCase("fr"))
		{
			langStr = ResourceBundleUtil.getMessage("FrenchKey");
		}
		else if (language.equalsIgnoreCase("jp"))
		{
			langStr = ResourceBundleUtil.getMessage("JapaneseKey");
		}

		final boolean sel = language.equalsIgnoreCase(currLang);
		final Box langBox = Box.createHorizontalBox();

		langBox.add(Box.createHorizontalStrut(10));
		langBox.add(new JLabel(flag));
		jrb = new JRadioButton(langStr);
		jrb.setSelected(sel);
		jrb.setActionCommand(language);
		jrb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
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
		sendPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("DefaultSendToDeviceKey")));

		if (getMethodSendToDevice().equals("JMF"))
		{
			selected = true;
		}
		final JRadioButton jrbSendJMF = new JRadioButton(ResourceBundleUtil.getMessage("sendMethodJMF"), selected);
		Dimension d = jrbSendJMF.getPreferredSize();
		jrbSendJMF.setBounds(10, 40, d.width, d.height);
		bgSendToDevice.add(jrbSendJMF);
		jrbSendJMF.setActionCommand("JMF");
		jrbSendJMF.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				setMethodSendToDevice(e.getActionCommand().toString());
			}
		});

		if (getMethodSendToDevice().equals("MIME"))
		{
			selected = true;
		}
		final JRadioButton jrbSendMIME = new JRadioButton(ResourceBundleUtil.getMessage("sendMethodMIME"), selected);
		d = jrbSendMIME.getPreferredSize();
		jrbSendMIME.setBounds(10, 60, d.width, d.height);
		bgSendToDevice.add(jrbSendMIME);
		jrbSendMIME.setActionCommand("MIME");
		jrbSendMIME.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				setMethodSendToDevice(e.getActionCommand().toString());
			}
		});

		if (getMethodSendToDevice().equals("User"))
		{
			selected = true;
		}
		final JRadioButton jrbSendUser = new JRadioButton(ResourceBundleUtil.getMessage("sendMethodUser"), selected);
		d = jrbSendUser.getPreferredSize();
		jrbSendUser.setBounds(10, 80, d.width, d.height);
		bgSendToDevice.add(jrbSendUser);
		jrbSendUser.setActionCommand("User");
		jrbSendUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
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
		@Override
		public void mouseClicked(final MouseEvent e)
		{
			e.getID(); // fool compiler;
			final int selectedIndex = getSelectedIndex();
			if (selectedIndex >= 0 && selectedIndex < panels.length)
			{
				setComponentAt(selectedIndex, panels[selectedIndex]);
			}
		}
	}

	// ////////////////////////////////////////////////////

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();
		if (source == applyLnFButton)
		{
			applyLnF();
		}
		else if (source == boxBaseLevel)
		{
			applyBaseLevel();
		}
		else if (source == boxMISLevel)
		{
			applyMISLevel();
		}
		else if (source == boxJMFLevel)
		{
			applyJMFLevel();
		}
		else if (source == boxReadOnly)
		{
			currReadOnly = boxReadOnly.isSelected();
		}
		else if (source == boxNormalizeOpen)
		{
			normalizeOpen = boxNormalizeOpen.isSelected();
		}
		else if (source == boxValOpen)
		{
			currValidate = boxValOpen.isSelected();
		}
		else if (source == cboxIndentSave)
		{
			currIndentSave = cboxIndentSave.isSelected();
		}
		else if (source == boxCheckURL)
		{
			checkURL = boxCheckURL.isSelected();
		}
		else if (source == boxDispDefault)
		{
			currDispDefault = boxDispDefault.isSelected();
		}
		else if (source == boxRemDefault)
		{
			currRemoveDefault = boxRemDefault.isSelected();
		}
		else if (source == boxRemWhite)
		{
			currRemoveWhite = boxRemWhite.isSelected();
		}
		else if (source == boxLongID)
		{
			longID = boxLongID.isSelected();
		}
		else if (source == boxUpdateJobID)
		{
			updateJobID = boxUpdateJobID.isSelected();
		}
		else if (source == boxIgnoreDefaults)
		{
			ignoreDefaults = boxIgnoreDefaults.isSelected();
		}
		else if (source == boxGenerateFull)
		{
			generateFull = boxGenerateFull.isSelected();
		}
		else if (source == boxSchema)
		{
			useSchema = boxSchema.isSelected();
		}
		else if (source == schemaBrowse)
		{
			final EditorFileChooser files = new EditorFileChooser(schemaFile, "xsd");
			final int option = files.showOpenDialog(this);

			if (option == JFileChooser.APPROVE_OPTION)
			{
				schemaFile = files.getSelectedFile();
				schemaPath.setText(schemaFile.getAbsolutePath());
			}
			else if (option == JFileChooser.ERROR_OPTION)
			{
				JOptionPane.showMessageDialog(this, "File is not accepted", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (source == fieldGenericStrings)
		{
			genericStrings = fieldGenericStrings.getText();
		}
		if (source == chooseValidLevel)
		{
			validationLevel = EnumValidationLevel.getEnum((String) chooseValidLevel.getSelectedItem());
		}
		if (source == boxExportValidation)
		{
			exportValidation = boxExportValidation.isSelected();
		}
		else if (source == chooseVersion)
		{
			validationVersion = EnumVersion.getEnum((String) chooseVersion.getSelectedItem());
		}
		else if (source == fieldMISURL)
		{
			misURL = fieldMISURL.getText();
		}
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

	public void writeToIni()
	{
		validTab.writeToIni();
		settingService.setBoolean(SettingKey.GENERAL_USE_SCHEMA, useSchema);

        if(getSchemaURL() != null) {
            settingService.setString(SettingKey.VALIDATION_SCHEMA_URL, getSchemaURL().getAbsolutePath());
        }

		settingService.setInteger(SettingKey.GOLDENTICKET_BASELEVEL, getBaseLevel());
		settingService.setInteger(SettingKey.GOLDENTICKET_MISLEVEL, getMISLevel());
		settingService.setInteger(SettingKey.GOLDENTICKET_JMFLEVEL, getJMFLevel());

		settingService.setString(SettingKey.GENERAL_LANGUAGE, getLanguage());
		settingService.setBoolean(SettingKey.GENERAL_READ_ONLY, getReadOnly());
		settingService.setBoolean(SettingKey.GENERAL_AUTO_VALIDATE, getAutoVal());
		settingService.setString(SettingKey.SEND_METHOD, getMethodSendToDevice());
		settingService.setString(SettingKey.GENERAL_LOOK, getLNF());
		settingService.setBoolean(SettingKey.GENERAL_REMOVE_DEFAULT, currRemoveDefault);
		settingService.setBoolean(SettingKey.GENERAL_DISPLAY_DEFAULT, currDispDefault);
		settingService.setBoolean(SettingKey.GENERAL_REMOVE_WHITE, currRemoveWhite);
		settingService.setBoolean(SettingKey.GENERAL_INDENT, currIndentSave);
        settingService.setBoolean(SettingKey.VALIDATION_CHECK_URL, checkURL);
		settingService.setBoolean(SettingKey.GENERAL_LONG_ID, longID);
		settingService.setBoolean(SettingKey.GENERAL_UPDATE_JOBID, updateJobID);
        settingService.setBoolean(SettingKey.VALIDATION_GENERATE_FULL, generateFull);
		settingService.setBoolean(SettingKey.GENERAL_NORMALIZE, normalizeOpen);
        settingService.setBoolean(SettingKey.VALIDATION_IGNORE_DEFAULT, ignoreDefaults);
        settingService.setString(SettingKey.VALIDATION_LEVEL, validationLevel.getName());
        settingService.setString(SettingKey.VALIDATION_VERSION, validationVersion.getName());
        settingService.setBoolean(SettingKey.VALIDATION_EXPORT, exportValidation);
		misURL = fieldMISURL.getText();

		settingService.setString(SettingKey.GOLDENTICKET_MISURL, misURL);

		genericStrings = fieldGenericStrings.getText();
		final VString genericAttributes = new VString(genericStrings, null);
		genericAttributes.unify();

        String s = StringUtil.setvString(genericAttributes, " ", null, null);
        settingService.setString(SettingKey.VALIDATION_GENERIC_ATTR, s);
	}

	/**
	 * @return the newly set schema file, if it is readable
	 */
	private File getSchemaURL()
	{
		final String s = schemaPath.getText();
		if (s != null && s.length() != 0)
		{
			final File f = new File(s);
			if (f.canRead())
			{
				schemaFile = f;
			}
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

	public void setIgnoreDefaults(final boolean _ignoreDefaults)
	{
		this.ignoreDefaults = _ignoreDefaults;
	}
}
