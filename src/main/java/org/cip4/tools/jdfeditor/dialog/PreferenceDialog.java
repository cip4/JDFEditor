/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2021 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
import javax.swing.UIManager.LookAndFeelInfo;

import org.cip4.jdflib.core.VString;
import org.cip4.tools.jdfeditor.EditorFileChooser;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * @author Dr. Rainer Prosi, Heidelberger Druckmaschinen AG
 *
 *         Jun 8, 2009
 */
public class PreferenceDialog extends JTabbedPane implements ActionListener
{
	private final SettingService settingService = SettingService.getSettingService();

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1350654061722234773L;

	private final ImageIcon sweFlag = ResourceUtil.getImageIcon("SwedishFlag.gif");
	private final ImageIcon engFlag = ResourceUtil.getImageIcon("GreatBrittainFlag.gif");
	private final ImageIcon freFlag = ResourceUtil.getImageIcon("FrenchFlag.gif");
	private final ImageIcon gerFlag = ResourceUtil.getImageIcon("GermanFlag.gif");
	private final ImageIcon spaFlag = ResourceUtil.getImageIcon("SpanishFlag.gif");
	private final ImageIcon japFlag = ResourceUtil.getImageIcon("JapanFlag.gif");

	public boolean useSchema = false;
	private JCheckBox boxSchema = null;
	public File schemaFile = null;
	private JButton schemaBrowse;
	private JTextField schemaPath;

	// private JButton defaultIconButton;
	// private JButton changeIconButton;
	private JButton applyLnFButton;
	private JTextField enlargeTextField;

	private JCheckBox boxReadOnly;
	private JCheckBox boxNormalizeOpen;
	private JCheckBox boxRemDefault;
	private JCheckBox boxDispDefault;
	private JCheckBox boxLongID;
	private JCheckBox boxUpdateJobID;
	private JCheckBox boxRemWhite;
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
	// private JLabel iconPreview;
	private final ButtonGroup langGroup = new ButtonGroup();
	private String currLang;
	// private String currIcon;
	private String currLNF;
	private boolean currRemoveDefault;
	private boolean currDispDefault;
	private boolean currRemoveWhite;
	private boolean currIndentSave;
	private boolean currValidate;
	private boolean currReadOnly;
	private boolean normalizeOpen;
	private boolean longID;
	private boolean updateJobID;
	private JTextField fieldMISURL;
	private String misURL;

	private final UIManager.LookAndFeelInfo aLnF[] = UIManager.getInstalledLookAndFeels();

	private final ValidationTab validTab;
	private final SaveAsXJDFDialog xjdfPanel;
	private final SaveAsJDFDialog jdfPanel;
	private final SaveAsJSONDialog jsonPanel;

	/**
	 *
	 */
	public PreferenceDialog()
	{
		validTab = new ValidationTab();
		xjdfPanel = new SaveAsXJDFDialog();
		jdfPanel = new SaveAsJDFDialog();
		jsonPanel = new SaveAsJSONDialog();

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
		settingService.set(SettingKey.SEND_METHOD, methodSendToDevice);
	}

	/**
	 * @return
	 */
	public String getMethodSendToDevice()
	{
		return settingService.getString(SettingKey.SEND_METHOD);
	}

	private void applyLnF()
	{
		final JDFFrame f = MainView.getFrame();
		settingService.setSetting(SettingKey.GENERAL_LOOK, currLNF);
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
		this.currLang = settingService.getSetting(SettingKey.GENERAL_LANGUAGE, String.class);
		this.currLNF = settingService.getSetting(SettingKey.GENERAL_LOOK, String.class);
		this.currValidate = settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class);
		this.currReadOnly = settingService.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class);
		this.longID = settingService.getSetting(SettingKey.GENERAL_LONG_ID, Boolean.class);
		this.updateJobID = settingService.getSetting(SettingKey.GENERAL_UPDATE_JOBID, Boolean.class);
		this.useSchema = settingService.getSetting(SettingKey.GENERAL_USE_SCHEMA, Boolean.class);

		if (settingService.getSetting(SettingKey.VALIDATION_SCHEMA_URL, String.class) != null)
		{
			this.schemaFile = new File(settingService.getSetting(SettingKey.VALIDATION_SCHEMA_URL, String.class));
		}

		this.currRemoveDefault = settingService.getSetting(SettingKey.GENERAL_REMOVE_DEFAULT, Boolean.class);
		this.currRemoveWhite = settingService.getSetting(SettingKey.GENERAL_REMOVE_WHITE, Boolean.class);
		this.currIndentSave = settingService.getSetting(SettingKey.GENERAL_INDENT, Boolean.class);
		this.currDispDefault = settingService.getSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, Boolean.class);

		this.normalizeOpen = settingService.getSetting(SettingKey.GENERAL_NORMALIZE, Boolean.class);

		this.misURL = settingService.getSetting(SettingKey.GOLDENTICKET_MISURL, String.class);

		/*
		 * BaseLevel=iniFile.getBaseLevel(); MISLevel=iniFile.getMISLevel(); JMFLevel=iniFile.getJMFLevel();
		 */

		enlargeTextField = new JTextField(3);
		enlargeTextField.setText(settingService.getString(SettingKey.FONT_SIZE_ENLARGED));

		setPreferredSize(new Dimension(420, 420));
		drawTabs();
		setVisible(true);
	}

	private void drawTabs()
	{
		MainView.setCursor(1, this);

		final JPanel generalPanel = createGeneralPref();
		addTab(ResourceUtil.getMessage("GeneralKey"), generalPanel);

		final JPanel sendPanel = new CommunicationTab().createSendToDevicePref();
		addTab(ResourceUtil.getMessage("SendToDeviceKey"), sendPanel);

		addTab(ResourceUtil.getMessage("ValidateKey"), validTab);

		final JPanel xjdfPanel1 = createXJDFPref();
		addTab(ResourceUtil.getMessage("SaveAsXJDFKey"), xjdfPanel1);

		final JPanel jdfPanel1 = createJDFPref();
		addTab(ResourceUtil.getMessage("SaveJDFKey"), jdfPanel1);

		final JPanel jsonPanel1 = createJSONPref();
		addTab(ResourceUtil.getMessage("SaveAsJSONKey"), jsonPanel1);

		final JPanel goldenTicketPanel = createGoldenTicketPref();
		addTab(ResourceUtil.getMessage("GoldenTicketKey"), goldenTicketPanel);

		final JPanel lnfPanel = createLnFPref();
		addTab(ResourceUtil.getMessage("LookAndFeelKey"), lnfPanel);

		final JPanel langPanel = createLanguagePref();
		addTab(ResourceUtil.getMessage("LanguageKey"), langPanel);

		final JPanel dirPanel = createDirPref();
		addTab(ResourceUtil.getMessage("DirectoriesKey"), dirPanel);

		MainView.setCursor(0, this);
	}

	private JPanel createGeneralPref()
	{
		final JPanel main = getMainPanel();

		final JPanel genPanel = new JPanel(null);
		genPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GeneralOptionsKey")));

		int y = 30;
		boxReadOnly = new JCheckBox(ResourceUtil.getMessage("OpenReadOnlyKey"), currReadOnly);
		Dimension d = boxReadOnly.getPreferredSize();
		boxReadOnly.setBounds(10, y, d.width, d.height);
		boxReadOnly.addActionListener(this);
		genPanel.add(boxReadOnly);

		y += d.height + 3;
		boxNormalizeOpen = new JCheckBox(ResourceUtil.getMessage("NormalizeOpenKey"), normalizeOpen);
		d = boxNormalizeOpen.getPreferredSize();
		boxNormalizeOpen.setBounds(10, y, d.width, d.height);
		boxNormalizeOpen.addActionListener(this);
		genPanel.add(boxNormalizeOpen);

		y += d.height + 3;
		boxValOpen = new JCheckBox(ResourceUtil.getMessage("OpenAutoValKey"), currValidate);
		d = boxValOpen.getPreferredSize();
		boxValOpen.setBounds(10, y, d.width, d.height);
		boxValOpen.addActionListener(this);
		genPanel.add(boxValOpen);

		y += d.height + 3;
		boxDispDefault = new JCheckBox(ResourceUtil.getMessage("DisplayDefaultsKey"), currDispDefault);
		d = boxDispDefault.getPreferredSize();
		boxDispDefault.setBounds(10, y, d.width, d.height);
		boxDispDefault.addActionListener(this);
		genPanel.add(boxDispDefault);

		y += d.height + 3;
		boxRemDefault = new JCheckBox(ResourceUtil.getMessage("SaveRemoveDefaultsKey"), currRemoveDefault);
		d = boxRemDefault.getPreferredSize();
		boxRemDefault.setBounds(10, y, d.width, d.height);
		boxRemDefault.addActionListener(this);
		genPanel.add(boxRemDefault);

		y += d.height + 3;
		boxRemWhite = new JCheckBox(ResourceUtil.getMessage("SaveRemoveWhiteKey"), currRemoveWhite);
		d = boxRemWhite.getPreferredSize();
		boxRemWhite.setBounds(10, y, d.width, d.height);
		boxRemWhite.addActionListener(this);
		genPanel.add(boxRemWhite);

		y += d.height + 3;
		cboxIndentSave = new JCheckBox(ResourceUtil.getMessage("IndentSave"), currIndentSave);
		d = cboxIndentSave.getPreferredSize();
		cboxIndentSave.setBounds(10, y, d.width, d.height);
		cboxIndentSave.addActionListener(this);
		genPanel.add(cboxIndentSave);

		y += d.height + 3;
		boxLongID = new JCheckBox(ResourceUtil.getMessage("LongIDKey"), longID);
		d = boxLongID.getPreferredSize();
		boxLongID.setBounds(10, y, d.width, d.height);
		boxLongID.addActionListener(this);
		genPanel.add(boxLongID);

		y += d.height + 3;
		boxUpdateJobID = new JCheckBox(ResourceUtil.getMessage("UpdateJobIDKey"), updateJobID);
		d = boxUpdateJobID.getPreferredSize();
		boxUpdateJobID.setBounds(10, y, d.width, d.height);
		boxUpdateJobID.addActionListener(this);
		genPanel.add(boxUpdateJobID);

		y += d.height + 3;
		boxSchema = new JCheckBox(ResourceUtil.getMessage("UseSchemaKey"), useSchema);
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

		schemaBrowse = new JButton(ResourceUtil.getMessage("BrowseKey"));
		schemaBrowse.setPreferredSize(new Dimension(85, 22));
		schemaBrowse.addActionListener(this);
		d = schemaBrowse.getPreferredSize();
		y += d.height + 9;
		schemaBrowse.setBounds(10, y, d.width, d.height);
		genPanel.add(schemaBrowse);

		main.add(genPanel, BorderLayout.CENTER);

		return main;
	}

	/**
	 * draw the flags etc. for the language preferences
	 * 
	 * @return
	 */
	private JPanel createLanguagePref()
	{
		final JPanel main = new JPanel(new BorderLayout());

		final Box northBox = Box.createHorizontalBox();
		northBox.add(Box.createHorizontalStrut(10));
		final String txt = "<html><br>" + ResourceUtil.getMessage("LanguageTitleKey") + "<br></html>";
		final JLabel text = new JLabel(txt, SwingConstants.LEFT);
		northBox.add(text);

		main.add(northBox, BorderLayout.NORTH);
		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

		final JPanel langPanel = new JPanel(null);
		langPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("LangSelectKey")));

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

	private JPanel createLnFPref()
	{
		final ButtonGroup buttonGroup = new ButtonGroup();

		final JPanel main = new JPanel(new BorderLayout());

		final Box northBox = Box.createHorizontalBox();
		northBox.add(Box.createHorizontalStrut(10));
		final String txt = "<html><br>" + ResourceUtil.getMessage("LnFTitleKey") + "<br></html>";
		final JLabel text = new JLabel(txt, SwingConstants.LEFT);
		northBox.add(text);

		main.add(northBox, BorderLayout.NORTH);
		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);

		final JPanel lnfPanel = new JPanel();
		lnfPanel.setLayout(null);
		lnfPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("LnFSelectKey")));
		int y = 30;

		for (final LookAndFeelInfo element : aLnF)
		{
			final String lnfStr = element.getClassName();
			final boolean sel = lnfStr.equals(currLNF) ? true : false;
			final JRadioButton jrb = new JRadioButton(element.getName(), sel);
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
		applyLnFButton = new JButton(ResourceUtil.getMessage("ApplyKey"));
		final Dimension d = applyLnFButton.getPreferredSize();
		applyLnFButton.setBounds(20, y + 10, d.width, d.height);
		applyLnFButton.addActionListener(this);
		lnfPanel.add(applyLnFButton);
		main.add(lnfPanel, BorderLayout.CENTER);

		final JPanel enlargeTextSizePanel = new JPanel();
		enlargeTextSizePanel.add(new JLabel("Enlarge text to"));
		enlargeTextSizePanel.add(enlargeTextField);
		enlargeTextSizePanel.add(new JLabel("%"));

		main.add(enlargeTextSizePanel, BorderLayout.SOUTH);

		return main;
	}

	private JPanel createDirPref()
	{
		final JPanel main = getMainPanel();

		final JPanel dirPanel = new JPanel(null);
		dirPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("DefaultDirsKey")));

		main.add(dirPanel, BorderLayout.CENTER);

		return main;
	}

	private JPanel createXJDFPref()
	{
		final JPanel main = getMainPanel();

		xjdfPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("SaveAsXJDFKey")));

		main.add(xjdfPanel, BorderLayout.WEST);
		return main;
	}

	private JPanel createJDFPref()
	{
		final JPanel main = getMainPanel();

		jdfPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("SaveJDFKey")));

		main.add(jdfPanel, BorderLayout.WEST);
		return main;
	}

	private JPanel createJSONPref()
	{
		final JPanel main = getMainPanel();

		jsonPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("SaveAsJSONKey")));

		main.add(jsonPanel, BorderLayout.WEST);
		return main;
	}

	private JPanel getMainPanel()
	{
		final JPanel main = new JPanel(new BorderLayout());

		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		return main;
	}

	private JPanel createGoldenTicketPref()
	{
		final JPanel main = getMainPanel();

		final JPanel panel = new JPanel(null);
		panel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GoldenTicketKey")));

		// Adds the MISURL to the GoldenTicket Tab
		int y = 30;
		final JLabel label = new JLabel(ResourceUtil.getMessage("MISURLKey"));
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
		bl.setText(ResourceUtil.getMessage("BaseLevelKey"));
		d = bl.getPreferredSize();
		bl.setBounds(10, y, d.width, d.height);
		panel.add(bl);

		BaseLevel = settingService.getSetting(SettingKey.GOLDENTICKET_BASELEVEL, Integer.class);
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
		ml.setText(ResourceUtil.getMessage("MISLevelKey"));
		d = ml.getPreferredSize();
		ml.setBounds(10, y, d.width, d.height);
		panel.add(ml);

		MISLevel = settingService.getSetting(SettingKey.GOLDENTICKET_MISLEVEL, Integer.class);
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
		jl.setText(ResourceUtil.getMessage("JMFLevelKey"));
		d = jl.getPreferredSize();
		jl.setBounds(10, y, d.width, d.height);
		panel.add(jl);

		JMFLevel = settingService.getSetting(SettingKey.GOLDENTICKET_JMFLEVEL, Integer.class);
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
			langStr = ResourceUtil.getMessage("SwedishKey");
		}
		else if (language.equalsIgnoreCase("en"))
		{
			langStr = ResourceUtil.getMessage("EnglishKey");
		}
		else if (language.equalsIgnoreCase("de"))
		{
			langStr = ResourceUtil.getMessage("GermanKey");
		}
		else if (language.equalsIgnoreCase("es"))
		{
			langStr = ResourceUtil.getMessage("SpanishKey");
		}
		else if (language.equalsIgnoreCase("fr"))
		{
			langStr = ResourceUtil.getMessage("FrenchKey");
		}
		else if (language.equalsIgnoreCase("jp"))
		{
			langStr = ResourceUtil.getMessage("JapaneseKey");
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

	private class CommunicationTab implements ActionListener
	{
		public CommunicationTab()
		{
			super();
		}

		JPanel createSendToDevicePref()
		{
			final JPanel main = new JPanel(new BorderLayout());

			final ButtonGroup bgSendToDevice = new ButtonGroup();

			main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
			main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
			main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
			main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

			final JPanel sendPanel = new JPanel(null);
			sendPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("DefaultSendToDeviceKey")));

			final String methodSendToDevice = getMethodSendToDevice();
			final JRadioButton jrbSendJMF = new JRadioButton(ResourceUtil.getMessage("sendMethodJMF"), "JMF".equals(methodSendToDevice));
			Dimension d = jrbSendJMF.getPreferredSize();
			jrbSendJMF.setBounds(10, 40, d.width, d.height);
			bgSendToDevice.add(jrbSendJMF);
			jrbSendJMF.setActionCommand("JMF");
			jrbSendJMF.addActionListener(this);

			final JRadioButton jrbSendMIME = new JRadioButton(ResourceUtil.getMessage("sendMethodMIME"), "MIME".equals(methodSendToDevice));
			d = jrbSendMIME.getPreferredSize();
			jrbSendMIME.setBounds(10, 60, d.width, d.height);
			jrbSendMIME.addActionListener(this);
			jrbSendMIME.setActionCommand("MIME");
			bgSendToDevice.add(jrbSendMIME);

			final JRadioButton jrbSendUser = new JRadioButton(ResourceUtil.getMessage("sendMethodUser"), "USER".equals(methodSendToDevice));
			d = jrbSendUser.getPreferredSize();
			jrbSendUser.setBounds(10, 80, d.width, d.height);
			jrbSendUser.addActionListener(this);
			jrbSendUser.setActionCommand("USER");
			bgSendToDevice.add(jrbSendUser);

			sendPanel.add(jrbSendJMF);
			sendPanel.add(jrbSendMIME);
			sendPanel.add(jrbSendUser);

			main.add(sendPanel, BorderLayout.CENTER);

			return main;
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			final String method = e.getActionCommand();
			setMethodSendToDevice(method);
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
		else if (source == fieldMISURL)
		{
			misURL = fieldMISURL.getText();
		}
		else
		{
			validTab.actionPerformed(e);
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
		settingService.setSetting(SettingKey.GENERAL_USE_SCHEMA, useSchema);

		final File schemaURL = EditorFileChooser.getSchemaURL(schemaPath);
		if (schemaURL != null)
		{
			settingService.setSetting(SettingKey.VALIDATION_SCHEMA_URL, schemaURL.getAbsolutePath());
		}

		settingService.setSetting(SettingKey.GOLDENTICKET_BASELEVEL, getBaseLevel());
		settingService.setSetting(SettingKey.GOLDENTICKET_MISLEVEL, getMISLevel());
		settingService.setSetting(SettingKey.GOLDENTICKET_JMFLEVEL, getJMFLevel());

		settingService.setSetting(SettingKey.GENERAL_LANGUAGE, getLanguage());
		settingService.setSetting(SettingKey.GENERAL_READ_ONLY, getReadOnly());
		settingService.setSetting(SettingKey.GENERAL_AUTO_VALIDATE, getAutoVal());
		settingService.setSetting(SettingKey.SEND_METHOD, getMethodSendToDevice());
		settingService.setSetting(SettingKey.GENERAL_LOOK, getLNF());
		settingService.setSetting(SettingKey.GENERAL_REMOVE_DEFAULT, currRemoveDefault);
		settingService.setSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, currDispDefault);
		settingService.setSetting(SettingKey.GENERAL_REMOVE_WHITE, currRemoveWhite);
		settingService.setSetting(SettingKey.GENERAL_INDENT, currIndentSave);
		settingService.setSetting(SettingKey.GENERAL_LONG_ID, longID);
		settingService.setSetting(SettingKey.GENERAL_UPDATE_JOBID, updateJobID);
		settingService.setSetting(SettingKey.GENERAL_NORMALIZE, normalizeOpen);
		misURL = fieldMISURL.getText();

		settingService.setSetting(SettingKey.GOLDENTICKET_MISURL, misURL);

		settingService.setSetting(SettingKey.FONT_SIZE_ENLARGED, enlargeTextField.getText());

		xjdfPanel.write2Ini();
		jsonPanel.write2Ini();
		jdfPanel.write2Ini();
	}

}
