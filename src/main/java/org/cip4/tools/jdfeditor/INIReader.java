
package org.cip4.tools.jdfeditor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.XMLDoc;
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

//	private final String exportValidate = "ValidEdit/@exportValidate";
//	private final String highlightFN = "ValidEdit/@highlightFN";
//	private final String schemaURL = "ValidEdit/@schemaURL";
//	private final String validVersion = "ValidEdit/@version";
//	private final String validLevel = "ValidEdit/@level";
//	private final String ignoreDefault = "ValidEdit/@ignoreDefault";
//	private final String checkURL = "ValidEdit/@checkURL";
//	private final String fixICSVersion = "ValidEdit/@fixICSVersion";
//	private final String convertLPP = "ValidEdit/@convertLPP";
//
//	private final String attribute = "TreeView/@attribute";
//	private final String inheritedAttr = "TreeView/@inheritedAttr";
//
//  private final String generateFull = "Validate/@GenerateFull";
//	private final String genericAtts = "Validate/@genericAtts";


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



	static private String defaultVersion = "1.5";


	// public void setDefaultVersion(final EnumVersion v)
//	{
//		setAttribute(validVersion, v == null ? defaultVersion : v.getName());
//		if (v != null)
//		{
//			JDFElement.setDefaultJDFVersion(v);
//		}
//
//	}

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



//	public boolean getWarnCheck()
//	{
//		final EnumValidationLevel level = getValidationLevel();
//		return !EnumValidationLevel.isNoWarn(level);
//	}




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
