package org.cip4.tools.jdfeditor.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * Util class for defining generic locations on a file system.
 * 
 * @deprecated use jdflib userdir
 */
@Deprecated
public class DirectoryUtil
{
	private static final String CIP4_FOLDER = "CIP4Tools/JDFEditor";
	private static final String LINUX_FOLDER = "." + CIP4_FOLDER;
	private static final String OSX_FOLDER = "Library/Preferences/" + CIP4_FOLDER;
	private static final String WINDOWS_FOLDER = "AppData/Local/" + CIP4_FOLDER;

	/**
	 * Returns the location of the JDFEditor folder in the CIP4Tool folder.
	 * 
	 * @return Location of the JDFEditor folder in the CIP4Tool folder.
	 */
	public static String getDirCIP4Tools()
	{
		String pathDir = "";
		if (isLinux())
		{
			pathDir = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), LINUX_FOLDER);
		}
		else if (isOsX())
		{
			pathDir = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), OSX_FOLDER);
		}
		else if (isWindows())
		{
			pathDir = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), WINDOWS_FOLDER);
		}

		return pathDir;
	}

	private static boolean isLinux()
	{
		return SystemUtils.IS_OS_LINUX;
	}

	private static boolean isOsX()
	{
		return SystemUtils.IS_OS_MAC_OSX;
	}

	private static boolean isWindows()
	{
		return SystemUtils.IS_OS_WINDOWS;
	}
}
