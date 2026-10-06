/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2026 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.player.PlayerBackend;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class MenuPlayer implements MenuInterface, ActionListener
{
	private final MainController mainController;

	private JMenu menu;
	private JMenuItem selectFileItem;
	private JMenuItem settingsItem;
	private JMenuItem startPlayerItem;
	private JMenuItem pausePlayerItem;
	private JMenuItem stopPlayerItem;

	public MenuPlayer(final MainController mainController)
	{
		this.mainController = mainController;
	}

	@Override
	public JMenu createMenu()
	{
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();

		menu = new JMenu(ResourceUtil.getMessage("main.menu.player"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);

		selectFileItem = new JMenuItem(ResourceUtil.getMessage("main.menu.player.selectfile"));
		selectFileItem.addActionListener(this);
		menu.add(selectFileItem);

		settingsItem = new JMenuItem(ResourceUtil.getMessage("main.menu.player.settings"));
		settingsItem.addActionListener(this);
		menu.add(settingsItem);

		menu.addSeparator();

		startPlayerItem = new JMenuItem(ResourceUtil.getMessage("main.menu.player.start"));
		startPlayerItem.setIcon(ResourceUtil.getImageIcon("toolbar/play.png"));
		startPlayerItem.addActionListener(this);
		menu.add(startPlayerItem);

		pausePlayerItem = new JMenuItem(ResourceUtil.getMessage("main.menu.player.pause"));
		pausePlayerItem.setIcon(ResourceUtil.getImageIcon("toolbar/pause.png"));
		pausePlayerItem.addActionListener(this);
		pausePlayerItem.setEnabled(false);
		menu.add(pausePlayerItem);

		stopPlayerItem = new JMenuItem(ResourceUtil.getMessage("main.menu.player.stop"));
		stopPlayerItem.setIcon(ResourceUtil.getImageIcon("toolbar/stop.png"));
		stopPlayerItem.addActionListener(this);
		stopPlayerItem.setEnabled(false);
		menu.add(stopPlayerItem);

		return menu;
	}

	@Override
	public void setEnableClose()
	{
	}

	@Override
	public void setEnableOpen(final boolean mode)
	{
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();
		if (source == selectFileItem)
		{
			selectFile();
		}
		else if (source == settingsItem)
		{
			showSettings();
		}
		else if (source == startPlayerItem)
		{
			startPlayer();
		}
		else if (source == pausePlayerItem)
		{
			pausePlayer();
		}
		else if (source == stopPlayerItem)
		{
			stopPlayer();
		}
	}

	private void selectFile()
	{
		final Map<String, String> fileChooserEnglishKeys = new LinkedHashMap<>();
		fileChooserEnglishKeys.put("FileChooser.lookInLabelText", "Look In:");
		fileChooserEnglishKeys.put("FileChooser.saveInLabelText", "Save In:");
		fileChooserEnglishKeys.put("FileChooser.fileNameLabelText", "File Name:");
		fileChooserEnglishKeys.put("FileChooser.filesOfTypeLabelText", "Files of Type:");
		fileChooserEnglishKeys.put("FileChooser.upFolderToolTipText", "Up One Level");
		fileChooserEnglishKeys.put("FileChooser.homeFolderToolTipText", "Desktop");
		fileChooserEnglishKeys.put("FileChooser.newFolderToolTipText", "Create New Folder");
		fileChooserEnglishKeys.put("FileChooser.listViewButtonToolTipText", "List");
		fileChooserEnglishKeys.put("FileChooser.detailsViewButtonToolTipText", "Details");
		fileChooserEnglishKeys.put("FileChooser.fileNameHeaderText", "Name");
		fileChooserEnglishKeys.put("FileChooser.fileSizeHeaderText", "Size");
		fileChooserEnglishKeys.put("FileChooser.fileTypeHeaderText", "Type");
		fileChooserEnglishKeys.put("FileChooser.fileDateHeaderText", "Date Modified");
		fileChooserEnglishKeys.put("FileChooser.fileAttrHeaderText", "Attributes");
		fileChooserEnglishKeys.put("FileChooser.openButtonText", "Open");
		fileChooserEnglishKeys.put("FileChooser.openDialogTitleText", "Open");
		fileChooserEnglishKeys.put("FileChooser.directoryOpenButtonText", "Open");
		fileChooserEnglishKeys.put("FileChooser.cancelButtonText", "Cancel");
		fileChooserEnglishKeys.put("FileChooser.openButtonToolTipText", "Open selected file");
		fileChooserEnglishKeys.put("FileChooser.cancelButtonToolTipText", "Abort file chooser dialog");
		fileChooserEnglishKeys.put("FileChooser.saveButtonText", "Save");
		fileChooserEnglishKeys.put("FileChooser.saveDialogTitleText", "Save");
		fileChooserEnglishKeys.put("FileChooser.updateButtonText", "Update");
		fileChooserEnglishKeys.put("FileChooser.helpButtonText", "Help");

		final Map<String, Object> previousUiValues = new LinkedHashMap<>();
		try
		{
			// Override FileChooser keys directly so chooser chrome stays English regardless of OS locale.
			for (final Map.Entry<String, String> entry : fileChooserEnglishKeys.entrySet())
			{
				previousUiValues.put(entry.getKey(), UIManager.get(entry.getKey()));
				UIManager.put(entry.getKey(), entry.getValue());
			}

			final JFileChooser chooser = new JFileChooser();
			final String configuredPath = mainController.getSetting(SettingKey.PLAYER_FILE_PATH, String.class);
			if (configuredPath != null && !configuredPath.trim().isEmpty())
			{
				final File configuredFile = new File(configuredPath.trim());
				final File parent = configuredFile.getParentFile();
				if (parent != null)
				{
					chooser.setCurrentDirectory(parent);
				}
				chooser.setSelectedFile(configuredFile);
			}
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileNameExtensionFilter(ResourceUtil.getMessage("PlayerZipFilterKey"), "zip"));
			chooser.setLocale(Locale.ENGLISH);
			chooser.updateUI();

			if (chooser.showOpenDialog(MainView.getFrame()) == JFileChooser.APPROVE_OPTION)
			{
				mainController.setSetting(SettingKey.PLAYER_FILE_PATH, chooser.getSelectedFile().getAbsolutePath());
				MainView.getFrame().getBottomTabs().getPlayerPanel().refreshConfiguredValues();
			}
		}
		finally
		{
			for (final Map.Entry<String, Object> entry : previousUiValues.entrySet())
			{
				UIManager.put(entry.getKey(), entry.getValue());
			}
		}
	}

	private void showSettings()
	{
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		final List<String> history = getUrlHistory(mainController.getSetting(SettingKey.PLAYER_URL, String.class));
		final JComboBox<String> urlCombo = new JComboBox<>(history.toArray(new String[0]));
		urlCombo.setEditable(true);
		if (!history.isEmpty())
		{
			urlCombo.setSelectedItem(history.get(0));
		}

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		panel.add(new JLabel(ResourceUtil.getMessage("main.menu.player.settings.url")), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		panel.add(urlCombo, gbc);

		final boolean repeat = mainController.getSetting(SettingKey.PLAYER_REPEAT, Boolean.class);
		final JRadioButton playOnceRadio = new JRadioButton(ResourceUtil.getMessage("main.menu.player.settings.playonce"));
		final JRadioButton repeatRadio = new JRadioButton(ResourceUtil.getMessage("main.menu.player.settings.repeat"));
		final ButtonGroup playbackGroup = new ButtonGroup();
		playbackGroup.add(playOnceRadio);
		playbackGroup.add(repeatRadio);
		if (repeat)
		{
			repeatRadio.setSelected(true);
		}
		else
		{
			playOnceRadio.setSelected(true);
		}

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		panel.add(playOnceRadio, gbc);

		gbc.gridy = 2;
		panel.add(repeatRadio, gbc);

		final boolean constantDelay = mainController.getSetting(SettingKey.PLAYER_CONSTANT_DELAY, Boolean.class);
		final JRadioButton constantDelayRadio = new JRadioButton(ResourceUtil.getMessage("main.menu.player.settings.constantdelay"));
		final JRadioButton timestampDelayRadio = new JRadioButton(ResourceUtil.getMessage("main.menu.player.settings.timestampdelay"));
		final ButtonGroup delayGroup = new ButtonGroup();
		delayGroup.add(constantDelayRadio);
		delayGroup.add(timestampDelayRadio);
		if (constantDelay)
		{
			constantDelayRadio.setSelected(true);
		}
		else
		{
			timestampDelayRadio.setSelected(true);
		}

		gbc.gridy = 3;
		panel.add(constantDelayRadio, gbc);

		gbc.gridy = 4;
		panel.add(timestampDelayRadio, gbc);

		final JTextField delaySecondsField = new JTextField(String.valueOf(mainController.getSetting(SettingKey.PLAYER_DELAY_SECONDS, Integer.class)));
		final JTextField timelapseField = new JTextField(String.valueOf(mainController.getSetting(SettingKey.PLAYER_TIMELAPSE_FACTOR, Integer.class)));
		delaySecondsField.setEnabled(constantDelayRadio.isSelected());
		timelapseField.setEnabled(timestampDelayRadio.isSelected());
		final ActionListener delayModeListener = event -> {
			delaySecondsField.setEnabled(constantDelayRadio.isSelected());
			timelapseField.setEnabled(timestampDelayRadio.isSelected());
		};
		constantDelayRadio.addActionListener(delayModeListener);
		timestampDelayRadio.addActionListener(delayModeListener);

		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.weightx = 0;
		panel.add(new JLabel(ResourceUtil.getMessage("main.menu.player.settings.delayseconds")), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		panel.add(delaySecondsField, gbc);

		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.weightx = 0;
		panel.add(new JLabel(ResourceUtil.getMessage("main.menu.player.settings.timelapsefactor")), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		panel.add(timelapseField, gbc);

		final boolean setCurrentTimestamp = mainController.getSetting(SettingKey.PLAYER_SET_CURRENT_TIMESTAMP, Boolean.class);
		final boolean generateUniqueIds = mainController.getSetting(SettingKey.PLAYER_GENERATE_UNIQUE_IDS, Boolean.class);
		final JCheckBox setCurrentTimestampCheck = new JCheckBox(ResourceUtil.getMessage("main.menu.player.settings.setcurrenttimestamp"), setCurrentTimestamp);
		final JCheckBox generateUniqueIdsCheck = new JCheckBox(ResourceUtil.getMessage("main.menu.player.settings.generateuniqueids"), generateUniqueIds);

		gbc.gridy = 7;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		panel.add(setCurrentTimestampCheck, gbc);

		gbc.gridy = 8;
		panel.add(generateUniqueIdsCheck, gbc);

		final String deviceId = mainController.getSetting(SettingKey.PLAYER_DEVICE_ID, String.class);
		final JTextField deviceIdField = new JTextField(deviceId == null ? "" : deviceId);

		gbc.gridy = 9;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.weightx = 0;
		panel.add(new JLabel(ResourceUtil.getMessage("main.menu.player.settings.deviceid")), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		panel.add(deviceIdField, gbc);

		final boolean updateDeviceId = mainController.getSetting(SettingKey.PLAYER_UPDATE_DEVICE_ID, Boolean.class);
		final JCheckBox updateDeviceIdCheck = new JCheckBox(ResourceUtil.getMessage("main.menu.player.settings.updatedeviceid"), updateDeviceId);

		gbc.gridy = 10;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		panel.add(updateDeviceIdCheck, gbc);

		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };
		while (true)
		{
			final int option = JOptionPane.showOptionDialog(MainView.getFrame(), panel, ResourceUtil.getMessage("main.menu.player.settings"),
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option != JOptionPane.OK_OPTION)
			{
				return;
			}

			Object urlValue = urlCombo.getEditor().getItem();
			if (urlValue == null)
			{
				urlValue = urlCombo.getSelectedItem();
			}
			final String urlText = urlValue == null ? "" : urlValue.toString().trim();

			if (!urlText.isEmpty() && !isValidHttpUrl(urlText))
			{
				JOptionPane.showMessageDialog(MainView.getFrame(), ResourceUtil.getMessage("InvalidURL"), ResourceUtil.getMessage("ErrorMessKey"),
						JOptionPane.ERROR_MESSAGE);
				continue;
			}

			if (!urlText.isEmpty())
			{
				final List<String> updatedHistory = new ArrayList<>(history);
				updatedHistory.remove(urlText);
				updatedHistory.add(0, urlText);
				while (updatedHistory.size() > 13)
				{
					updatedHistory.remove(updatedHistory.size() - 1);
				}
				mainController.setSetting(SettingKey.PLAYER_URL, String.join(";", updatedHistory));
			}

			mainController.setSetting(SettingKey.PLAYER_REPEAT, repeatRadio.isSelected());
			mainController.setSetting(SettingKey.PLAYER_CONSTANT_DELAY, constantDelayRadio.isSelected());
			mainController.setSetting(SettingKey.PLAYER_SET_CURRENT_TIMESTAMP, setCurrentTimestampCheck.isSelected());
			mainController.setSetting(SettingKey.PLAYER_GENERATE_UNIQUE_IDS, generateUniqueIdsCheck.isSelected());
			mainController.setSetting(SettingKey.PLAYER_DEVICE_ID, deviceIdField.getText().trim());
			mainController.setSetting(SettingKey.PLAYER_UPDATE_DEVICE_ID, updateDeviceIdCheck.isSelected());

			try
			{
				final int delaySeconds = Integer.parseInt(delaySecondsField.getText().trim());
				if (delaySeconds >= 0)
				{
					mainController.setSetting(SettingKey.PLAYER_DELAY_SECONDS, Integer.valueOf(delaySeconds));
				}
			}
			catch (final NumberFormatException ignored)
			{
				// Keep existing setting when parsing fails.
			}

			try
			{
				final int timelapseFactor = Integer.parseInt(timelapseField.getText().trim());
				if (timelapseFactor > 0)
				{
					mainController.setSetting(SettingKey.PLAYER_TIMELAPSE_FACTOR, Integer.valueOf(timelapseFactor));
				}
			}
			catch (final NumberFormatException ignored)
			{
				// Keep existing setting when parsing fails.
			}

			MainView.getFrame().getBottomTabs().getPlayerPanel().refreshConfiguredValues();
			return;
		}
	}

	private List<String> getUrlHistory(final String configuredUrlHistory)
	{
		final List<String> history = new ArrayList<>();
		if (configuredUrlHistory == null || configuredUrlHistory.isEmpty())
		{
			return history;
		}
		for (final String value : configuredUrlHistory.split(";"))
		{
			if (value != null)
			{
				final String trimmedValue = value.trim();
				if (!trimmedValue.isEmpty())
				{
					history.add(trimmedValue);
				}
			}
		}
		return history;
	}

	private boolean isValidHttpUrl(final String urlText)
	{
		try
		{
			final URI uri = new URI(urlText);
			final String scheme = uri.getScheme();
			final String host = uri.getHost();
			return scheme != null && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) && host != null && !host.isEmpty();
		}
		catch (final URISyntaxException e)
		{
			return false;
		}
	}

	private void startPlayer()
	{
		final PlayerBackend backend = PlayerBackend.getInstance();
		if (backend.isPaused())
		{
			backend.resume();
		}
		else
		{
			backend.start();
		}
		startPlayerItem.setEnabled(false);
		pausePlayerItem.setEnabled(true);
		stopPlayerItem.setEnabled(true);
	}

	private void pausePlayer()
	{
		PlayerBackend.getInstance().pause();
		startPlayerItem.setEnabled(true);
		pausePlayerItem.setEnabled(false);
		stopPlayerItem.setEnabled(true);
	}

	private void stopPlayer()
	{
		PlayerBackend.getInstance().stop();
		startPlayerItem.setEnabled(true);
		pausePlayerItem.setEnabled(false);
		stopPlayerItem.setEnabled(false);
	}
}