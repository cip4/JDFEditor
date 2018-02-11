/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2018 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFVersion;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.BuildPropsUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * The utterly useless HCNF spring dummy requested Main Controller class.
 */
public class MainController implements ActionListener
{

	private static final Log LOGGER = LogFactory.getLog(MainController.class);

	public static final String ACTION_ONLINE_HELP = "actionOnlineHelp";

	public static final String ACTION_INFO = "actionInfo";

	private final SettingService settingService = SettingService.getSettingService();

	private final MainView mainView;

	/**
	 * Default constructor.
	 */
	public MainController()
	{
		mainView = new MainView();
		init();
	}

	/**
	 * Initializes the MainController class. This method is called by the Spring Framework after construction.
	 */
	public void init()
	{
		mainView.registerController(this);
	}

	/**
	 * Display the main form.
	 * @param file The file to be shown after application start.
	 */
	public void displayForm(final File file)
	{

		// display main view
		mainView.display(file);
	}

	/**
	 * Get a typed setting value by key.
	 * @param key The key of the setting value.
	 * @param clazz The type of the setting value.
	 * @return The typed setting value.
	 */
	public <T> T getSetting(final SettingKey key, final Class<T> clazz)
	{
		return settingService.getSetting(key, clazz);
	}

	/**
	 * Set a setting value by a key.
	 * @param key   The configuration key.
	 * @param value The configuration setting value as String.
	 */
	public void setSetting(final SettingKey key, final Object value)
	{
		settingService.setSetting(key, value);
	}

	/**
	 * ActionListner for defined actions.
	 * @param e The ActionEvent generated by the calling object.
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{

		// analyze action command
		switch (e.getActionCommand())
		{

		case ACTION_ONLINE_HELP:
			openOnlineHelp();
			break;

		case ACTION_INFO:
			showInfo();
			break;

		default:
			LOGGER.warn(String.format("ActionCommand '%s' is unknown.", e.getActionCommand()));
			break;
		}
	}

	/**
	 * Open the JDF Editor online help or if not possible show a message box with the URL.
	 */
	private void openOnlineHelp()
	{

		final String url = "https://confluence.cip4.org/display/PUB/JDFEditor+Online+Help";
		// open in browser (if possible)
		if (Desktop.isDesktopSupported())
		{
			try
			{
				Desktop.getDesktop().browse(new URI(url));
				LOGGER.info("Opening Online Help at:" + url);
			}
			catch (final Exception ex)
			{
				LOGGER.error("Error opening Online Help at:" + url, ex);
			}
		}
		else
		{
			final String msg = "see " + url;
			final String title = "Online Help";
			LOGGER.info("No Desktop - Cannot Open Online Help at:" + url);

			mainView.showMessageDialog(msg, title);
		}
	}

	/**
	 * Show JDF Editor Info Dialog.
	 */
	private void showInfo()
	{

		LOGGER.info("Show Info Window.");

		final Editor editor = Editor.getEditor();

		final String msg = String.format("%s\n" + "%s (%s)\n\n" + "based on\n" + "%s\n"
				+ "%s (%s)", BuildPropsUtil.getAppName(), BuildPropsUtil.getAppVersion(), BuildPropsUtil.getBuildDate(), JDFVersion.LIB_NAME, JDFVersion.LIB_VERSION, JDFVersion.LIB_RELEASE_DATE);

		final String title = "Version";

		mainView.showMessageDialog(msg, title);
	}
}
