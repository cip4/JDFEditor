/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2022 The International Cooperation for the Integration of
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

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.StringArray;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.jmf.JMFBuilder;
import org.cip4.jdflib.jmf.JMFBuilderFactory;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.util.MyArgs;
import org.cip4.jdflib.util.file.UserDir;
import org.cip4.jdfutility.logging.LogConfigurator;
import org.cip4.lib.jdf.jsonutil.JSONPrepWalker;
import org.cip4.lib.jdf.jsonutil.JSONWriter;
import org.cip4.lib.jdf.jsonutil.JSONWriter.eJSONCase;
import org.cip4.tools.jdfeditor.commandline.EditorCommandLine;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.BuildPropsUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;

/**
 * @author AnderssA ThunellE
 */
public class Editor
{
	private static Editor my_Editor;

	private static Log log = null;
	private JSONWriter jsonWriter;

	/*
	 * This package is found under JDFEditor in the src/java section. It contains all of the icons associated with the JDFEditor. For your icons
	 * to appear, remember to refresh the package. To change the icons in the Menu bar, go to EditorButton.java. To change the icons in the tree
	 * mode, error icons, go to INIReader.java. If you would like to change the appearance of how the menu items appear, go to JDFEditor_(Language
	 * want, i.e. en)_.properties located under org.cip4.jdfeditor.messages package.
	 */

	/**
	 * @param args
	 */
	public static void main(final String[] args)
	{
		preInit();
		if (log == null)
			log = LogFactory.getLog(Editor.class);
		log.info("Starting editor");

		File file = null;
		// mac may have 2nd argument
		for (int i = args.length - 1; i >= 0; i--)
		{
			if (!args[i].startsWith("-"))
			{
				file = new File(args[i]);
				if (file.canRead())
				{
					break;
				}
				file = null;
			}
		}

		log.info("Main arguments: " + Arrays.toString(args) + " file=" + file);
		final MyArgs ma = new MyArgs(args, "C?", "", "");
		if (ma.boolParameter("C") || ma.boolParameter("?"))
		{
			final EditorCommandLine checker = new EditorCommandLine();
			checker.validate(args, null);
		}
		else
		{
			// apple menu compatibility
			initEditor(file);
			my_Editor.init(file);
		}
	}

	static void initEditor(final File file)
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		my_Editor = new Editor();
	}

	/**
	 * preliminary initialization done here
	 */
	protected static void preInit()
	{
		// dummy call to initialize logging early
		// log file location
		final String pathDir = new UserDir("JDFEditor").getLogPath();
		LogConfigurator.configureLog(pathDir, "JDFEditor.log");
		// nothing to do here (yet)
		SettingService.getSettingService();
		JDFResource.setAutoSubElementClass(false);
	}

	/**
	 *
	 */
	public Editor()
	{
		super();
	}

	/**
	 * Method instantiate the editor window
	 *
	 * @param file the file to open initially
	 */
	public void init(final File file)
	{

		final MainController mainController = new MainController();
		mainController.displayForm(file);

		// read the initialization stuff
		JDFAudit.setStaticAgentName(BuildPropsUtil.getAppName());
		JDFAudit.setStaticAgentVersion(BuildPropsUtil.getAppVersion());
		JMFBuilderFactory.setSenderID(getClass(), "JDFEditor");

		KElement.setLongID(mainController.getSetting(SettingKey.GENERAL_LONG_ID, Boolean.class));
		JDFElement.setDefaultJDFVersion(JDFElement.EnumVersion.getEnum(mainController.getSetting(SettingKey.VALIDATION_VERSION, String.class)));
		JDFParser.m_searchStream = true;
	}

	/**
	 * Method getEditor.
	 *
	 * @return Editor
	 */
	public static Editor getEditor()
	{
		return my_Editor;
	}

	/**
	 * @return
	 */
	JMFBuilder getJMFBuilder()
	{
		return JMFBuilderFactory.getJMFBuilder(getClass());
	}

	private static final String RES_SCHEMA = "/org/cip4/tools/jdfeditor/schema/xjdf.xsd";

	public void resetJSON()
	{
		jsonWriter = null;
	}

	/**
	 * @return
	 */
	public JSONWriter getJSonWriter()
	{
		final SettingService settingService = SettingService.getSettingService();
		if (jsonWriter == null)
		{
			final JSONWriter w = new JSONWriter();
			w.setXJDF();
			w.setTypeSafe(settingService.getBool(SettingKey.JSON_TYPESAFE));
			w.setKeyCase(eJSONCase.valueOf(settingService.getString(SettingKey.JSON_CASE)));
			w.setWantArray(false);
			final InputStream is = ResourceUtil.class.getResourceAsStream(RES_SCHEMA);
			final KElement schema = KElement.parseStream(is);
			w.fillTypesFromSchema(schema);
			removeMessages(w);
			w.setPrepWalker(new JSONPrepWalker());
			jsonWriter = w;
		}
		return jsonWriter;
	}

	void removeMessages(final JSONWriter w)
	{
		final StringArray a = new StringArray(w.getArrayNames());
		final VString fams = EnumFamily.getFamilies();
		for (int i = 0; i < fams.size(); i++)
			fams.set(i, fams.get(i).toLowerCase());
		for (final String msg : a)
		{
			for (final String fam : fams)
			{
				if (msg.startsWith(fam))
					w.removeArray(msg);
			}
		}

	}

}
