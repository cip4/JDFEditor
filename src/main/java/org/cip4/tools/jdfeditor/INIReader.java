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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.cip4.jdflib.core.*;
import org.cip4.jdflib.core.JDFElement.EnumValidationLevel;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.dialog.SearchComboBoxModel;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * INI_Reader.java
 * @author SvenoniusI
 * 
 * History
 * 20040906 MRE methods for send to device added
 * 
 * File includes path for display icons in the JDFEditor.
 */

/**
 * @author Dr. Rainer Prosi, Heidelberger Druckmaschinen AG
 * 
 * 13.02.2009
 */
public class INIReader
{
	private final String language = "General/@language";
	private final String lookAndFeel = "General/@lookAndFeel";
	private final String methodSendToDevice = "SendToDevice/@Method";
	private final String packageAll = "SendToDevice/@PackageAll";

	private final String incSendToDevice = "SendToDevice/@JobIncrement";
	private final String urlSendToDevice = "SendToDevice/@URLSendTo";
	private final String urlReturnToDevice = "SendToDevice/@URLReturnTo";
	private final String longID = "General/@longID";
	private final String normalizeOpen = "General/@NormalizeOpen";

	private final String enableExtensions = "Extension/@enableExtensions";
	private final String structuredCaps = "Extension/@StructuredCaps";
	private final String xjdfSingleNode = "Extension/XJDF/@SingleNode";
	private final String xjdfConvertStripping = "Extension/XJDF/@ConvertStripping";
	private final String xjdfConvertTilde = "Extension/XJDF/@ConvertTilde";
	private final String xjdfSpanAsAttribute = "Extension/XJDF/@SpanAsAttribute";
	private final String xjdfMergeRunList = "Extension/XJDF/@MergeRunList";
	private final String xjdfConvertLayoutPrep = "Extension/XJDF/@ConvertLayoutPrep";
	private final String xjdfTypesafeJMF = "Extension/XJDF/@TypesafeJMF";
	private final String fromXjdfRetainProduct = "Extension/XJDF/@FromXjdfRetainProduct";
	private final String fromXJDFHeuristicLink = "Extension/XJDF/@FromXJDFHeuristicLink";

	//	HTTP server settings
	private final String httpStorePath = "HTTPserver/@StorePath";

	//	Find dialog
	private final String findPattern = "Find/@Pattern-";
	private final String findCaseSensitive = "Find/@CaseSensitive";
	private final String findWrap = "Find/@Wrap";

	private final String[] recentFiles = new String[5];
	private final String recentDevCap = "RecentFiles/@recentDevCap";

	private final String autoValidate = "ValidEdit/@autoValidate";
	private final String exportValidate = "ValidEdit/@exportValidate";
	private final String highlightFN = "ValidEdit/@highlightFN";
	private final String readOnly = "ValidEdit/@readOnly";
	private final String schemaURL = "ValidEdit/@schemaURL";
	private final String useSchema = "ValidEdit/@useSchema";
	private final String validVersion = "ValidEdit/@version";
	private final String validLevel = "ValidEdit/@level";
	private final String removeDefault = "ValidEdit/@removeDefault";
	private final String displayDefault = "ValidEdit/@displayDefault";
	private final String ignoreDefault = "ValidEdit/@ignoreDefault";
	private final String removeWhite = "ValidEdit/@removeWhite";
	private final String indentSave = "ValidEdit/@indentSave";
	private final String checkURL = "ValidEdit/@checkURL";
	private final String fixICSVersion = "ValidEdit/@fixICSVersion";
	private final String convertLPP = "ValidEdit/@convertLPP";

	private final String attribute = "TreeView/@attribute";
	private final String inheritedAttr = "TreeView/@inheritedAttr";

	private final String generateFull = "Validate/@GenerateFull";
	private final String genericAtts = "Validate/@genericAtts";

	private final String misURL = "GoldenTicket/@misURL";
	private final String BaseLevel = "GoldenTicket/@BaseLevel";
	private final String MISLevel = "GoldenTicket/@MISLevel";
	private final String JMFLevel = "GoldenTicket/@JMFLevel";

	private XMLDoc xDoc; // The XMLDocument that represents the ini file

	/**
	 * the ini file reader for the editor
	 */
	public INIReader()
	{
		init();
	}

	private void init()
	{
		// Read the Editor.ini file and store the data in it
		try
		{
			final JDFParser p = new JDFParser();
			xDoc = p.parseFile(getIniPath());
			readINIFile();
		}
		// If the Editor.ini file is not found, create a new default file and read it
		catch (final Exception ex)
		{
			setLanguage("en"); // dummy to set up a minimal xml document
			writeINIFile();
			readINIFile();
		}
	}

	/**
	 * @return
	 */
	public String getLanguage()
	{
		return getAttribute(language, "en");
	}

	private String getAttribute(final String path, final String def)
	{
		if (xDoc == null)
		{
			return def;
		}
		return xDoc.getRoot().getXPathAttribute(path, def);
	}

	/**
	 * @return
	 */
	public String getLookAndFeel()
	{

		return getAttribute(lookAndFeel, UIManager.getSystemLookAndFeelClassName());
	}

	/**
	 * @return
	 */
//	public int getFontSize()
//	{
//		final String s = getAttribute(fontSize, "10");
//		return Integer.parseInt(s);
//	}

	/**
	 * @return
	 */
//	public String getFontName()
//	{
//		return getAttribute(fontName, null);
//	}

	/**
	 * @return
	 */
	public String getGenericAtts()
	{
		final String defaultGenerics = "ID Type JobID JobPartID ProductID CustomerID SpawnIDs" + " Class Status PartIDKeys xmlns xmlns:xsi xsi:Type"
				+ " SettingsPolicy BestEffortExceptions" + " OperatorInterventionExceptions" + " MustHonorExceptions" + " DocIndex Locked DescriptiveName Brand";

		final String s = getAttribute(genericAtts, defaultGenerics);
		return s;
	}

	/**
	 * @param s
	 */
	public void setGenericAtts(final VString s)
	{
		setAttribute(genericAtts, StringUtil.setvString(s, " ", null, null));
	}

	/**
	 * 
	 * @return
	 */
	public boolean getAutoVal()
	{
		return getAttribute(autoValidate, "").equals("on");
	}

	/**
	 * 
	 *  
	 * @param bVal
	 */
	public void setAutoVal(final boolean bVal)
	{
		setAttribute(autoValidate, bVal ? "on" : "off");
	}

	/**
	 * 
	 * @return
	 */
	public boolean getExportValidation()
	{
		return getAttribute(exportValidate, "").equals("on");
	}

	public void setExportValidation(final boolean bVal)
	{
		setAttribute(exportValidate, bVal ? "on" : "off");
	}

	public void setReadOnly(final boolean bRO)
	{
		setAttribute(readOnly, bRO ? "on" : "off");
	}

	public String[] getRecentFiles()
	{
		return this.recentFiles;
	}

	public void setRecentDevCap(final File devCapFile)
	{
		if (devCapFile == null)
		{
			setAttribute(recentDevCap, null);
		}
		else
		{
			setAttribute(recentDevCap, devCapFile.getAbsolutePath());
		}
	}

	public File getRecentDevCap()
	{
		final String s = getAttribute(recentDevCap, null);
		if (s == null)
		{
			return null;
		}
		return new File(s);
	}

	public void setSchemaURL(final File schema)
	{
		if (schema == null)
		{
			setAttribute(schemaURL, null);
		}
		else
		{
			setAttribute(schemaURL, schema.getAbsolutePath());
		}
	}

	public File getSchemaURL()
	{
		final String s = getAttribute(schemaURL, null);
		if (s == null)
		{
			return null;
		}
		return new File(s);
	}

	/**
	 * @param schema
	 */
	public void setUseSchema(final boolean schema)
	{
		setAttribute(useSchema, schema ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getUseSchema()
	{
		return getAttribute(useSchema, "false").equals("true");
	}

	/**
	 * @param url
	 */
	public void setCheckURL(final boolean url)
	{
		setAttribute(checkURL, url ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getFixICSVersion()
	{
		return getAttribute(fixICSVersion, "false").equals("true");
	}

	/**
	 * @param ics
	 */
	public void setFixICSVersion(final boolean ics)
	{
		setAttribute(fixICSVersion, ics ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getConvertLPP()
	{
		return getAttribute(convertLPP, "false").equals("true");
	}

	/**
	 * @param ics
	 */
	public void setConvertLPP(final boolean ics)
	{
		setAttribute(convertLPP, ics ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getCheckURL()
	{
		return getAttribute(checkURL, "false").equals("true");
	}

	/**
	 * @param rem
	 */
	public void setRemoveWhite(final boolean rem)
	{
		setAttribute(removeWhite, rem ? "true" : "false");
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean getRemoveWhite()
	{
		return getAttribute(removeWhite, "true").equals("true");
	}

	/**
	 * 
	 *  
	 * @param rem
	 */
	public void setRemoveDefault(final boolean rem)
	{
		setAttribute(removeDefault, rem ? "true" : "false");
	}

	public boolean getRemoveDefault()
	{
		return getAttribute(removeDefault, "true").equals("true");
	}

	public void setDisplayDefault(final boolean rem)
	{
		setAttribute(displayDefault, rem ? "true" : "false");
	}

	public boolean getDisplayDefault()
	{
		return getAttribute(displayDefault, "true").equals("true");
	}

	public void setIgnoreDefault(final boolean rem)
	{
		setAttribute(ignoreDefault, rem ? "true" : "false");
	}

	public boolean getIgnoreDefault()
	{
		return getAttribute(ignoreDefault, "true").equals("true");
	}

	public boolean getHighlight()
	{
		return getAttribute(highlightFN, "").equalsIgnoreCase("on") ? true : false;
	}

	public void setHighlight(final boolean b)
	{
		setAttribute(highlightFN, b ? "on" : "off");
	}

	public boolean getEnableExtensions()
	{
		return true;
	}

	public void setEnableExtensions(final boolean b)
	{
		setAttribute(enableExtensions, b ? "true" : "false");
	}

	public boolean getStructuredCaps()
	{
		return true;
	}

	public void setStructuredCaps(final boolean b)
	{
		setAttribute(structuredCaps, b ? "true" : "false");
	}

	public boolean getReadOnly()
	{
		return getAttribute(readOnly, "").equalsIgnoreCase("on") ? true : false;
	}

	public boolean getAttr()
	{
		return getAttribute(attribute, "on").equalsIgnoreCase("on") ? true : false;
	}

	public void setAttr(final boolean b)
	{
		setAttribute(attribute, b ? "on" : "off");
	}

	public boolean getInhAttr()
	{
		return getAttribute(inheritedAttr, "on").equalsIgnoreCase("on") ? true : false;
	}

	public void setInhAttr(final boolean b)
	{
		setAttribute(inheritedAttr, b ? "on" : "off");
	}

	public String getMethodSendToDevice()
	{
		return getAttribute(methodSendToDevice, "MIME");
	}

	public void setMethodSendToDevice(final String method)
	{
		setAttribute(methodSendToDevice, method);
	}

	public String getURLSendToDevice()
	{
		return getAttribute(urlSendToDevice, "http://");
	}

	public void setURLSendToDevice(final String url)
	{
		setAttribute(urlSendToDevice, url);
	}

	public String getURLReturnToDevice()
	{
		return getAttribute(urlReturnToDevice, "http://");
	}

	public void setURLReturnToDevice(final String url)
	{
		setAttribute(urlReturnToDevice, url);
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public EnumValidationLevel getValidationLevel()
	{
		final String s = getAttribute(validLevel, JDFConstants.VALIDATIONLEVEL_RECURSIVECOMPLETE);
		return EnumValidationLevel.getEnum(s);
	}

	/**
	 * 
	 *  
	 * @param level
	 */
	public void setValidationLevel(final EnumValidationLevel level)
	{
		setAttribute(validLevel, level.getName());
	}

	static private String defaultVersion = "1.5";

	/**
	 * 
	 * @return the default version for new files, initialization, fixing to etc
	 */
	public EnumVersion getDefaultVersion()
	{
		final String s = getAttribute(validVersion, defaultVersion);
		return EnumVersion.getEnum(s);
	}

	/**
	 * 
	 * @param v the default version for new files, initialization, fixing to etc
	 */
	public void setDefaultVersion(final EnumVersion v)
	{
		setAttribute(validVersion, v == null ? defaultVersion : v.getName());
		if (v != null)
		{
			JDFElement.setDefaultJDFVersion(v);
		}

	}

	private void readINIFile()
	{
		for (int i = 0; i < 5; i++)
		{
			final String s = getAttribute("RecentFiles/File[" + String.valueOf(i + 1) + "]/@Path", null);
			if (s == null)
			{
				break;
			}
			recentFiles[i] = s;
		}

		for (int i = 0; true; i++)
		{
			final String name = getAttribute("Icons/Icon[" + String.valueOf(i + 1) + "]/@Name", null);
			final String path = getAttribute("Icons/Icon[" + String.valueOf(i + 1) + "]/@Path", null);
			if (name == null || path == null)
			{
				break;
			}
		}

	}

	public void writeINIFile()
	{
		if (recentFiles != null)
		{
			for (int i = 0; i < recentFiles.length; i++)
			{
				if (recentFiles[i] != null && recentFiles[i].length() > 0)
				{
					setAttribute("RecentFiles/File[" + String.valueOf(i + 1) + "]/@Path", recentFiles[i]);
				}
			}
		}
		final String iniPath = getIniPath();
		xDoc.write2File(iniPath, 2, true);
	}

	private String getIniPath()
	{
		// ini file
		String pathDir = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), "CIP4Tools");
		pathDir = FilenameUtils.concat(pathDir, "JDFEditor");
		new File(pathDir).mkdirs();

		String pathFile = FilenameUtils.concat(pathDir, "JDFEditor.ini");
		return pathFile;

		//		this.settingsFile = new File(pathFile);
		//		
		//		
		//		final String path = System.getProperty("user.home");
		//		final File iniDir = new File(path + File.separator + "CIP4Editor");
		//		if (!iniDir.exists())
		//		{
		//			iniDir.mkdir();
		//		}
		//		final String iniPath = iniDir.getPath() + File.separator + "Editor.ini";
		//		return iniPath;
	}

	/**
	 * Checks how many file paths the recentFiles String contains.
	 * @return The number of previously opened files.
	 */
	public int nrOfRecentFiles()
	{
		int nr = 0;
		if (recentFiles == null)
		{
			return nr;
		}

		while (nr < recentFiles.length && recentFiles[nr] != null)
		{
			nr++;
		}

		return nr;
	}

	/**
	 * Checks if the file that is to be opened already exists in the recent files menu.
	 * @param s - The path to the file
	 * @return true if the path already is in the recent files menu; false otherwise.
	 */
	public boolean pathNameExists(final String s)
	{
		final int nrOfRecentFiles = nrOfRecentFiles();
		for (int i = 0; i < nrOfRecentFiles; i++)
		{
			if (s.equals(recentFiles[i]))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the order in the recent files menu.
	 * @param s - The path to the file
	 * @param exist - Do the path already exists?
	 */
	void updateOrder(final String s, final boolean exist)
	{
		final String[] tmpFiles = new String[5];

		if (exist)
		{
			final int pos = pathNamePosition(s);
			if (pos > 0)
			{
				for (int i = 1; i < pos + 1; i++)
				{
					tmpFiles[i] = recentFiles[i - 1];
				}
			}
		}
		else
		{
			if (nrOfRecentFiles() > 0)
			{
				for (int j = 1; j <= nrOfRecentFiles() && j < 5; j++)
				{
					tmpFiles[j] = recentFiles[j - 1];
				}
			}
		}
		recentFiles[0] = s;
		int n = 1;
		while (n < 5 && tmpFiles[n] != null)
		{
			recentFiles[n] = tmpFiles[n];
			n++;
		}
	}

	/**
	 * The position which the filepath has in the recent files menu.
	 * @param s - The path to the file
	 * @return The position in the m_recentFiles String[] as an integer.
	 */
	public int pathNamePosition(final String s)
	{
		for (int i = 0; i < nrOfRecentFiles(); i++)
		{
			if (s.equals(recentFiles[i]))
			{
				return i;
			}
		}
		return -1;
	}

	public void setLanguage(final String _language)
	{
		setAttribute(language, _language);
	}

	private void setAttribute(final String xPath, final String attrib)
	{
		if (xDoc == null)
		{
			xDoc = new XMLDoc("EditorIni", null);
		}
		final KElement root = xDoc.getRoot();
		if (attrib == null)
		{
			root.removeXPathAttribute(xPath);
		}
		else
		{
			root.setXPathAttribute(xPath, attrib);
		}
	}

	public void setLookAndFeel(final String lnf)
	{
		setAttribute(lookAndFeel, lnf);
	}

	/**
	 * @return
	 */
	public boolean getLongID()
	{
		return getAttribute(longID, "true").equalsIgnoreCase("true") ? true : false;
	}

	/**
	 * @param b
	 */
	public void setLongID(final boolean b)
	{
		setAttribute(longID, b ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getGenerateFull()
	{
		return getAttribute(generateFull, "true").equalsIgnoreCase("true") ? true : false;
	}

	public void setGenerateFull(final boolean b)
	{
		setAttribute(generateFull, b ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getNormalizeOpen()
	{
		return getAttribute(normalizeOpen, "true").equalsIgnoreCase("true") ? true : false;
	}

	public void setNormalizeOpen(final boolean bNormalizeOpen)
	{
		setAttribute(normalizeOpen, bNormalizeOpen ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getWarnCheck()
	{
		final EnumValidationLevel level = getValidationLevel();
		return !EnumValidationLevel.isNoWarn(level);
	}

	public void setMISURL(final String _misURL)
	{
		setAttribute(misURL, _misURL);
	}

	/**
	 * @return
	 */
	public String getMISURL()
	{
		return getAttribute(misURL, null);
	}

	public void setBaseLevel(final int _baselevel)
	{
		setAttribute(BaseLevel, String.valueOf(_baselevel));
	}

	/**
	 * @return
	 */
	public int getBaseLevel()
	{
		final String s = getAttribute(BaseLevel, null);
		return StringUtil.parseInt(s, 1);

	}

	public void setMISLevel(final int _mislevel)
	{
		setAttribute(MISLevel, String.valueOf(_mislevel));
	}

	/**
	 * @return
	 */
	public int getMISLevel()
	{
		final String s = getAttribute(MISLevel, null);
		return StringUtil.parseInt(s, 1);
	}

	public void setJobIncrement(final int inc)
	{
		setAttribute(incSendToDevice, String.valueOf(inc));
	}

	/**
	 * @return
	 */
	public int getJobIncrement()
	{
		final String s = getAttribute(incSendToDevice, null);
		return StringUtil.parseInt(s, 1);
	}

	public void setJMFLevel(final int _jmflevel)
	{
		setAttribute(JMFLevel, String.valueOf(_jmflevel));
	}

	/**
	 * @return
	 */
	public int getJMFLevel()
	{
		final String s = getAttribute(JMFLevel, null);
		return StringUtil.parseInt(s, 1);
	}

	@Override
	public String toString()
	{
		return xDoc == null ? "null ini file " : "INUReader: " + xDoc.toString();
	}

	/**
	 * @param b
	 */
	public void setPackageAll(final boolean b)
	{
		setAttribute(packageAll, b ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean setPackageAll()
	{
		return getAttribute(packageAll, "true").equalsIgnoreCase("true") ? true : false;
	}

	public boolean getXjdfSingleNode()
	{
		return getAttribute(xjdfSingleNode, "false").equalsIgnoreCase("true") ? true : false;
	}

	public void setXjdfSingleNode(final boolean b)
	{
		setAttribute(xjdfSingleNode, b ? "true" : "false");
	}

	public boolean getXjdfConvertStripping()
	{
		return getAttribute(xjdfConvertStripping, "false").equalsIgnoreCase("true") ? true : false;
	}

	public void setXjdfConvertStripping(final boolean b)
	{
		setAttribute(xjdfConvertStripping, b ? "true" : "false");
	}

	public boolean getXjdfSpanAsAttribute()
	{
		return getAttribute(xjdfSpanAsAttribute, "false").equalsIgnoreCase("true") ? true : false;
	}

	public void setXjdfSpanAsAttribute(final boolean b)
	{
		setAttribute(xjdfSpanAsAttribute, b ? "true" : "false");
	}

	public boolean getXjdfMergeRunList()
	{
		return getAttribute(xjdfMergeRunList, "false").equalsIgnoreCase("true") ? true : false;
	}

	public void setXjdfMergeRunList(final boolean b)
	{
		setAttribute(xjdfMergeRunList, b ? "true" : "false");
	}

	/**
	 * @return
	 */
	public boolean getXjdfConvertLOPrep()
	{
		return getAttribute(xjdfConvertLayoutPrep, "false").equalsIgnoreCase("true") ? true : false;
	}

	/**
	 * @return
	 */
	public void setXjdfConvertLOPrep(final boolean b)
	{
		setAttribute(xjdfConvertLayoutPrep, b ? "true" : "false");
	}

	/**
	 * if true, tildes are converte to two instances
	 * @return
	 */
	public boolean getXjdfConvertTilde()
	{
		return getAttribute(xjdfConvertTilde, "false").equalsIgnoreCase("true") ? true : false;
	}

	/**
	 *  
	 * @param selected
	 */
	public void setFromXjdfRetainProduct(boolean selected)
	{
		setAttribute(fromXjdfRetainProduct, selected ? "true" : "false");
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public boolean getFromXjdfRetainProduct()
	{
		return getAttribute(fromXjdfRetainProduct, "false").equalsIgnoreCase("true") ? true : false;
	}

	/**
	 *  
	 * @param selected
	 */
	public void setFromXJDFHeuristicLink(boolean selected)
	{
		setAttribute(fromXJDFHeuristicLink, selected ? "true" : "false");
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public boolean getFromXJDFHeuristicLink()
	{
		return getAttribute(fromXJDFHeuristicLink, "false").equalsIgnoreCase("true") ? true : false;
	}

	/**
	 * 
	 *  
	 * @param b
	 */
	public void setXjdfConvertTilde(final boolean b)
	{
		setAttribute(xjdfConvertTilde, b ? "true" : "false");
	}

	public String getHttpStorePath()
	{
		return getAttribute(httpStorePath, "/var/tmp/JDFEditor/ReceivedMessages/");
	}

	public void setHttpStorePath(final String p)
	{
		setAttribute(httpStorePath, p);
	}

	public boolean getFindCaseSensitive()
	{
		return getAttribute(findCaseSensitive, "true").equalsIgnoreCase("true") ? true : false;
	}

	public void setFindCaseSensitive(final boolean p)
	{
		setAttribute(findCaseSensitive, p ? "true" : "false");
	}

	public boolean getFindWrap()
	{
		return getAttribute(findWrap, "true").equalsIgnoreCase("true") ? true : false;
	}

	public void setFindWrap(final boolean p)
	{
		setAttribute(findWrap, p ? "true" : "false");
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public List<String> getFindPattern()
	{
		List<String> res = new ArrayList<String>();
		for (int i = 0; i < SearchComboBoxModel.MAX_ELEMENTS; i++)
		{
			String pattern = getAttribute(findPattern + i, "");
			if (StringUtils.isNotBlank(pattern))
			{
				res.add(pattern);
			}
		}
		return res;
	}

	/**
	 * 
	 *  
	 * @param patternList
	 */
	public void setFindPattern(List<String> patternList)
	{
		for (int i = 0; i < patternList.size(); i++)
		{
			setAttribute(findPattern + i, patternList.get(i));
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean getIndentSave()
	{
		return getAttribute(indentSave, "true").equals("true");
	}

	/**
	 * 
	 *  
	 * @param indent
	 */
	public void setIndentSave(final boolean indent)
	{
		setAttribute(indentSave, indent ? "true" : "false");
	}

	/**
	 * 
	 * @return
	 */
	public boolean getTypesafeJMF()
	{
		return getAttribute(xjdfTypesafeJMF, "true").equals("true");
	}

	/**
	 * 
	 *  
	 * @param indent
	 */
	public void setTypesafeJMF(final boolean indent)
	{
		setAttribute(xjdfTypesafeJMF, indent ? "true" : "false");
	}

}
