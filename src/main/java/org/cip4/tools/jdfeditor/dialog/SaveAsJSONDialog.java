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
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cip4.jdflib.util.UrlUtil;
import org.cip4.lib.jdf.jsonutil.JSONWriter.eJSONCase;
import org.cip4.lib.jdf.jsonutil.JSONWriter.eJSONPrefix;
import org.cip4.lib.jdf.jsonutil.JSONWriter.eJSONRoot;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.EditorFileChooser;
import org.cip4.tools.jdfeditor.EditorSwingUtils;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;

/**
 * Class that implements a "Save as XJDF..." dialog.
 *
 */
public class SaveAsJSONDialog extends JPanel implements ActionListener
{

	final SettingService settingService;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static final int BUTTON_CANCEL = 0;
	static final int BUTTON_OK = 1;

	private final JComboBox<String> cbCase;
	private final JComboBox<String> cbPrefix;
	private final JComboBox<String> cbRoot;
	private final JCheckBox cbTypesafe;
	private final JCheckBox cbSplitXJMF;
	private final JButton schemaBrowse;
	private final JTextField schemaPath;

	/**
	 *
	 */
	public SaveAsJSONDialog()
	{
		// setLayout(new BorderLayout());

		settingService = SettingService.getSettingService();

		// setLayout(new GridLayout(1, 0));
		final JPanel checkboxesPanel = new JPanel();
		checkboxesPanel.setLayout(new BoxLayout(checkboxesPanel, BoxLayout.Y_AXIS));
		final GridLayout layout = new GridLayout(6, 4);
		// checkboxesPanel.setLayout(layout);

		cbCase = new JComboBox<>(new Vector<>(eJSONCase.getNames()));
		cbPrefix = new JComboBox<>(new Vector<>(eJSONPrefix.getNames()));
		cbRoot = new JComboBox<>(new Vector<>(eJSONRoot.getNames()));
		cbTypesafe = new JCheckBox(ResourceUtil.getMessage("convert.json.typesafe"));
		cbSplitXJMF = new JCheckBox(ResourceUtil.getMessage("convert.json.splitxjmf"));

		final JPanel pCase = new JPanel();
		pCase.setLayout(new GridBagLayout());
		pCase.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("convert.json.case")));
		pCase.add(cbCase, EditorSwingUtils.getOutConstraints());
		checkboxesPanel.add(pCase);

		checkboxesPanel.add(cbPrefix);
		final JPanel pPrefix = new JPanel();
		pPrefix.setLayout(new GridBagLayout());
		pPrefix.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("convert.json.prefix")));
		pPrefix.add(cbPrefix, EditorSwingUtils.getOutConstraints());
		checkboxesPanel.add(pPrefix);

		checkboxesPanel.add(cbRoot);
		final JPanel pRoot = new JPanel();
		pRoot.setLayout(new GridBagLayout());
		pRoot.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("convert.json.root")));
		pRoot.add(cbRoot, EditorSwingUtils.getOutConstraints());
		checkboxesPanel.add(pRoot);

		checkboxesPanel.add(cbTypesafe);
		checkboxesPanel.add(cbSplitXJMF);

		add(checkboxesPanel, BorderLayout.CENTER);

		final JPanel schemaPannel = new JPanel();
		schemaPath = new JTextField(42);
		schemaPannel.add(schemaPath);

		schemaBrowse = new JButton(ResourceUtil.getMessage("BrowseKey"));
		schemaBrowse.setPreferredSize(new Dimension(85, 22));
		schemaBrowse.addActionListener(this);
		schemaPannel.add(schemaBrowse);
		schemaPannel.setLayout(new GridLayout(2, 1));

		checkboxesPanel.add(schemaPannel);
		final Toolkit tk = Toolkit.getDefaultToolkit();
		final Dimension screenSize = tk.getScreenSize();
		final int screenHeight = screenSize.height;
		final int screenWidth = screenSize.width;

		setSize(screenWidth / 3, screenHeight / 3);
		// setLocation(screenWidth / 4, screenHeight / 4);

		cbCase.setSelectedItem(settingService.getString(SettingKey.JSON_CASE));
		cbPrefix.setSelectedItem(settingService.getString(SettingKey.JSON_PREFIX));
		cbRoot.setSelectedItem(settingService.getString(SettingKey.JSON_ROOT));
		cbTypesafe.setSelected(settingService.getBool(SettingKey.JSON_TYPESAFE));
		cbSplitXJMF.setSelected(settingService.getBool(SettingKey.JSON_XJMF_SPLIT));
		schemaPath.setText(settingService.getString(SettingKey.JSON_SCHEMA_URL));

		setVisible(true);
	}

	public void write2Ini()
	{
		settingService.set(SettingKey.JSON_CASE, (String) cbCase.getSelectedItem());
		settingService.set(SettingKey.JSON_PREFIX, (String) cbPrefix.getSelectedItem());
		settingService.set(SettingKey.JSON_ROOT, (String) cbRoot.getSelectedItem());
		settingService.set(SettingKey.JSON_TYPESAFE, cbTypesafe.isSelected());
		settingService.set(SettingKey.JSON_XJMF_SPLIT, cbSplitXJMF.isSelected());
		Editor.getEditor().resetJSON();
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();

		if (source == schemaBrowse)
		{
			perFormSchemaButton();
		}

	}

	private void perFormSchemaButton()
	{
		final EditorFileChooser files = new EditorFileChooser(UrlUtil.urlToFile(schemaPath.getText()), "json");
		final int option = files.showOpenDialog(this);

		if (option == JFileChooser.APPROVE_OPTION)
		{
			final File schemaFile = files.getSelectedFile();
			schemaPath.setText(schemaFile.getAbsolutePath());
			settingService.set(SettingKey.JSON_SCHEMA_URL, schemaFile.getAbsolutePath());
		}
		else if (option == JFileChooser.ERROR_OPTION)
		{
			JOptionPane.showMessageDialog(this, "File is not accepted", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
