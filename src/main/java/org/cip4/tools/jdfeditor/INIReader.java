
package org.cip4.tools.jdfeditor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.cip4.jdflib.core.*;
import org.cip4.jdflib.core.JDFElement.EnumValidationLevel;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.dialog.SearchComboBoxModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ...born 2 die... (refactored by SettingService)
 */
public class INIReader
{

	//	Find dialog
	private final String findPattern = "Find/@Pattern-";

	private final String[] recentFiles = new String[5];
	private final String recentDevCap = "RecentFiles/@recentDevCap";

	private final String exportValidate = "ValidEdit/@exportValidate";
	private final String highlightFN = "ValidEdit/@highlightFN";
	private final String schemaURL = "ValidEdit/@schemaURL";
	private final String validVersion = "ValidEdit/@version";
	private final String validLevel = "ValidEdit/@level";
	private final String ignoreDefault = "ValidEdit/@ignoreDefault";
	private final String checkURL = "ValidEdit/@checkURL";
	private final String fixICSVersion = "ValidEdit/@fixICSVersion";
	private final String convertLPP = "ValidEdit/@convertLPP";

	private final String attribute = "TreeView/@attribute";
	private final String inheritedAttr = "TreeView/@inheritedAttr";

	private final String generateFull = "Validate/@GenerateFull";
	private final String genericAtts = "Validate/@genericAtts";


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
			writeINIFile();
			readINIFile();
		}
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
	public boolean getExportValidation()
	{
		return getAttribute(exportValidate, "").equals("on");
	}

	public void setExportValidation(final boolean bVal)
	{
		setAttribute(exportValidate, bVal ? "on" : "off");
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

	public boolean getStructuredCaps()
	{
		return true;
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

	public boolean getWarnCheck()
	{
		final EnumValidationLevel level = getValidationLevel();
		return !EnumValidationLevel.isNoWarn(level);
	}


	@Override
	public String toString()
	{
		return xDoc == null ? "null ini file " : "INUReader: " + xDoc.toString();
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



}
