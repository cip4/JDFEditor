package org.cip4.tools.jdfeditor.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cip4.jdflib.core.JDFElement.EnumValidationLevel;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.util.EnumUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;

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
class ValidationTab extends JPanel implements ActionListener
{
	public ValidationTab()
	{
		super(new BorderLayout());
		final SettingService settingService = SettingService.getSettingService();
		this.genericStrings = settingService.getSetting(SettingKey.VALIDATION_GENERIC_ATTR, String.class);
		this.generateFull = settingService.getSetting(SettingKey.VALIDATION_GENERATE_FULL, Boolean.class);
		this.checkURL = settingService.getSetting(SettingKey.VALIDATION_CHECK_URL, Boolean.class);
		this.ignoreDefaults = settingService.getSetting(SettingKey.VALIDATION_IGNORE_DEFAULT, Boolean.class);
		this.ignorePrivateValidation = settingService.getSetting(SettingKey.IGNORE_PRIVATE_VALIDATION, Boolean.class);
		this.exportValidation = settingService.getSetting(SettingKey.VALIDATION_EXPORT, Boolean.class);
		this.validationVersion = EnumVersion.getEnum(settingService.getSetting(SettingKey.VALIDATION_VERSION, String.class));
		this.validationLevel = EnumValidationLevel.getEnum(settingService.getString(SettingKey.VALIDATION_LEVEL));
		if (validationLevel == null)
		{
			validationLevel = EnumValidationLevel.Complete;
		}
		createValidatePref();
	}

	private JTextField fieldGenericStrings;
	private String genericStrings;
	private JCheckBox boxGenerateFull;
	private boolean generateFull;
	private JCheckBox boxCheckURL;
	private JComboBox chooseValidLevel;
	private JComboBox chooseVersion;
	private boolean checkURL;
	private JCheckBox boxIgnoreDefaults;
	private JCheckBox boxIgnorePrivateValidation;
	private boolean ignoreDefaults;
	private boolean ignorePrivateValidation;
	private JCheckBox boxExportValidation;
	private boolean exportValidation;
	private EnumValidationLevel validationLevel = null;
	private EnumVersion validationVersion = null;

	public void writeToIni()
	{
		final SettingService settingService = SettingService.getSettingService();

		settingService.setSetting(SettingKey.VALIDATION_CHECK_URL, checkURL);
		settingService.setSetting(SettingKey.VALIDATION_GENERATE_FULL, generateFull);
		settingService.setSetting(SettingKey.VALIDATION_IGNORE_DEFAULT, ignoreDefaults);
		settingService.setSetting(SettingKey.VALIDATION_LEVEL, validationLevel.getName());
		settingService.setSetting(SettingKey.VALIDATION_VERSION, validationVersion.getName());
		settingService.setSetting(SettingKey.VALIDATION_EXPORT, exportValidation);
		settingService.setSetting(SettingKey.IGNORE_PRIVATE_VALIDATION, ignorePrivateValidation);

		genericStrings = fieldGenericStrings.getText();
		final VString genericAttributes = new VString(genericStrings, null);
		genericAttributes.unify();

		final String s = StringUtil.setvString(genericAttributes, " ", null, null);
		settingService.setSetting(SettingKey.VALIDATION_GENERIC_ATTR, s);

	}

	JPanel createValidatePref()
	{
		final JPanel main = this;

		main.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		main.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		main.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		main.add(Box.createVerticalStrut(10), BorderLayout.NORTH);

		final JPanel panel = new JPanel(null);
		panel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("ValidateKey")));

		int y = 30;
		final JLabel label = new JLabel(ResourceUtil.getMessage("DevCapGenericAttrKey"));
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

		boxGenerateFull = new JCheckBox(ResourceUtil.getMessage("GenerateFullKey"), generateFull);
		d = boxGenerateFull.getPreferredSize();
		boxGenerateFull.setBounds(10, y, d.width, d.height);
		boxGenerateFull.addActionListener(this);
		panel.add(boxGenerateFull);

		y += d.height + 3;
		boxCheckURL = new JCheckBox(ResourceUtil.getMessage("CheckURLKey"), checkURL);
		d = boxCheckURL.getPreferredSize();
		boxCheckURL.setBounds(10, y, d.width, d.height);
		boxCheckURL.addActionListener(this);
		panel.add(boxCheckURL);

		y += d.height + 3;
		boxIgnoreDefaults = new JCheckBox(ResourceUtil.getMessage("IgnoreDefaultsKey"), ignoreDefaults);
		d = boxIgnoreDefaults.getPreferredSize();
		boxIgnoreDefaults.setBounds(10, y, d.width, d.height);
		boxIgnoreDefaults.addActionListener(this);
		panel.add(boxIgnoreDefaults);

		y += d.height + 3;
		boxExportValidation = new JCheckBox(ResourceUtil.getMessage("ExportValidationKey"), exportValidation);
		d = boxExportValidation.getPreferredSize();
		boxExportValidation.setBounds(10, y, d.width, d.height);
		boxExportValidation.addActionListener(this);
		panel.add(boxExportValidation);

		y += d.height + 3;
		boxIgnorePrivateValidation = new JCheckBox(ResourceUtil.getMessage("IgnorePrivateValidation"), ignorePrivateValidation);
		d = boxIgnorePrivateValidation.getPreferredSize();
		boxIgnorePrivateValidation.setBounds(10, y, d.width, d.height);
		boxIgnorePrivateValidation.addActionListener(this);
		panel.add(boxIgnorePrivateValidation);

		y += d.height + 3;
		final VString allowedValues = getValidationLevels();
		chooseValidLevel = new JComboBox(allowedValues);
		chooseValidLevel.setSelectedItem(validationLevel.getName());
		chooseValidLevel.addActionListener(this);
		d = chooseValidLevel.getPreferredSize();

		final JPanel validLevelPanel = new JPanel();
		validLevelPanel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("ValidationLevelKey")));
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
		allValues.addElement(EnumVersion.Version_1_5.getName());
		allValues.addElement(EnumVersion.Version_1_6.getName());
		allValues.addElement(EnumVersion.Version_1_7.getName());
		allValues.addElement(EnumVersion.Version_1_8.getName());
		// allValues.addElement("2.0");
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

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();
		if (source == boxCheckURL)
		{
			checkURL = boxCheckURL.isSelected();
		}
		else if (source == boxIgnoreDefaults)
		{
			ignoreDefaults = boxIgnoreDefaults.isSelected();
		}
		else if (source == boxGenerateFull)
		{
			generateFull = boxGenerateFull.isSelected();
		}
		else if (source == boxIgnorePrivateValidation)
		{
			ignorePrivateValidation = boxIgnorePrivateValidation.isSelected();
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
	}

	private VString getValidationLevels()
	{
		final VString allowedValues = EnumUtil.getNamesVector(EnumValidationLevel.class);
		allowedValues.remove(EnumValidationLevel.RecursiveComplete.getName());
		allowedValues.remove(EnumValidationLevel.RecursiveIncomplete.getName());
		return allowedValues;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ValidationTab [generateFull=" + generateFull + ", checkURL=" + checkURL + ", ignoreDefaults=" + ignoreDefaults + ", "
				+ (validationLevel != null ? "validationLevel=" + validationLevel + ", " : "") + (validationVersion != null ? "validationVersion=" + validationVersion : "") + "]";
	}
}