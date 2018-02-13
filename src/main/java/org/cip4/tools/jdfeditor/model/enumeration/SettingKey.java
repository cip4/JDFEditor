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
package org.cip4.tools.jdfeditor.model.enumeration;

import javax.swing.UIManager;

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;

/**
 * Enum of all setting keys.
 */
public enum SettingKey
{

	FIND_PATTERN("find.pattern", null), FIND_CASE_SENSITIVE("find.case.sensitive", true), FIND_WRAP("find.warp", true),

	GENERAL_LANGUAGE("general.language", "en"), GENERAL_LOOK("general.look", UIManager.getSystemLookAndFeelClassName()), GENERAL_READ_ONLY("general.readonly",
			false), GENERAL_NORMALIZE("general.normalize", true), GENERAL_AUTO_VALIDATE("general.auto.validate", false), GENERAL_DISPLAY_DEFAULT("general.display.default",
					true), GENERAL_REMOVE_DEFAULT("general.remove.default", true), GENERAL_REMOVE_WHITE("general.remove.white", true), GENERAL_INDENT("general.indent",
							true), GENERAL_LONG_ID("general.long.id", false), GENERAL_UPDATE_JOBID("general.update.jobid", false), GENERAL_USE_SCHEMA("general.use.schema", false),

	GOLDENTICKET_MISURL("goldenticket.misurl", null), GOLDENTICKET_BASELEVEL("goldenticket.baselevel", 1), GOLDENTICKET_MISLEVEL("goldenticket.mislevel",
			1), GOLDENTICKET_JMFLEVEL("goldenticket.jmflevel", 1),

	HTTP_STORE_PATH("http.store.path", DirectoryUtil.getReceivedMessagesDir()), HTTP_PRESELECTED_ADDRESS("http.preselected.address", "127.0.0.1"),

	FONT_SIZE_ENLARGED("font.size.enlarged", "100"),

	LOGGING_LEVEL("logging.level", "INFO"), LOGGING_ENABLED("logging.enabled", true),

	RECENT_DEV_CAP("recent.dev.cap", null), RECENT_FILES("recent.files", null),

	SEND_METHOD("send.method", "MIME"), SEND_PACKAGE("send.package", true), SEND_JOB_INCREMENT("send.job.increment", 1), SEND_URL_SEND("send.url.send",
			"http://"), SEND_URL_RETURN("send.url.return", "http://"),

	TREEVIEW_ATTRIBUTE("treeview.attribute", true), TREEVIEW_ATTRIBUTE_INHERITED("treeview.attribute.inherited", true),

	VALIDATION_EXPORT("validation.export", false), VALIDATION_HIGHTLIGHT_FN("validation.highlight.fn", true), VALIDATION_SCHEMA_URL("validation.schema.url",
			null), VALIDATION_VERSION("validation.version", "1.5"), VALIDATION_LEVEL("validation.level",
					JDFConstants.VALIDATIONLEVEL_RECURSIVECOMPLETE), VALIDATION_IGNORE_DEFAULT("validation.ignore.default", true), VALIDATION_CHECK_URL("validation.check.url",
							false), VALIDATION_FIX_ICS_VERSION("validation.fix.ics.version", false), VALIDATION_CONVERT_LPP("validation.convert.lpp",
									false), VALIDATION_GENERATE_FULL("validation.generate.full", true), VALIDATION_GENERIC_ATTR("validation.generic.attr", initGenericAttr()),

	XJDF_CONVERT_SINGLENODE("xjdf.convert.singlenode", "zip"), XJDF_CONVERT_STRIPPING("xjdf.convert.stripping", true), XJDF_CONVERT_SPAN("xjdf.convert.span",
			true), XJDF_CONVERT_RUNLIST("xjdf.convert.runlist", true), XJDF_CONVERT_LAYOUTPREP("xjdf.convert.layoutprep", true), XJDF_CONVERT_TILDE("xjdf.convert.tilde",
					true), XJDF_TYPESAFE_JMF("xjdf.typesave_jmf", true), XJDF_FROM_RETAIN_PRODUCT("xjdf.from.retain.product",
							false), XJDF_FROM_HEURISTIC_LINK("xjdf.from.heuristic.link", false), XJDF_SPLIT_PARAMETER("xjdf.split.parameter", false);

	private final String key;

	private final String defaultValue;

	private final Class<?> clazz;

	/**
	 * Custom constructor. Accepting a key an a default value for initializing.
	 *
	 * @param key          The Setting key as String.
	 * @param defaultValue The default value as String.
	 */
	SettingKey(final String key, final String defaultValue)
	{
		this.key = key;
		this.defaultValue = defaultValue;
		this.clazz = String.class;
	}

	/**
	 * Custom constructor. Accepting a key an a default value for initializing.
	 *
	 * @param key          The Setting key as String.
	 * @param defaultValue The default value as Boolean.
	 */
	SettingKey(final String key, final boolean defaultValue)
	{
		this.key = key;
		this.defaultValue = Boolean.toString(defaultValue);
		this.clazz = Boolean.class;
	}

	/**
	 * Custom constructor. Accepting a key an a default value for initializing.
	 *
	 * @param key          The Setting key as String.
	 * @param defaultValue The default value as Integer.
	 */
	SettingKey(final String key, final int defaultValue)
	{
		this.key = key;
		this.defaultValue = Integer.toString(defaultValue);
		this.clazz = Integer.class;
	}

	/**
	 * Creates and returns the list of generic attributes.
	 *
	 * @return The list of generic attributes.
	 */
	private static String initGenericAttr()
	{

		String result = "";

		final String[] generics = new String[] { "ID", "Type", "JobID", "JobPartID", "ProductID", "CustomerID", "SpawnIDs", "Class", "Status", "PartIDKeys", "xmlns", "xmlns:xsi",
				"xsi:Type", "SettingsPolicy", "BestEffortExceptions", "OperatorInterventionExceptions", "MustHonorExceptions", "DocIndex", "Locked", "DescriptiveName", "Brand" };

		// build string
		for (final String generic : generics)
		{
			result += generic + " ";
		}

		result = result.trim();

		// return result
		return result;
	}

	/**
	 * Returns the setting key as String.
	 *
	 * @return The setting key as String.
	 */
	public String getKey()
	{
		return this.key;
	}

	/**
	 * Returns the default value as String.
	 *
	 * @return The default value as String.
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * Returns the class type as Class object.
	 *
	 * @return The class type as Class object.
	 */
	public Class<?> getClazz()
	{
		return clazz;
	}
}
