package org.cip4.tools.jdfeditor.util;

import org.apache.commons.lang3.StringUtils;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;

/**
 * Util class for RecentFile handling.
 */
public class RecentFileUtil
{

	public static final int MAX_RECENT = 10;

	private final static String[] recentFiles = initRecentFiles();

	private final static String SEPARATOR = ";";

	/**
	 * Private constructor. This class cannot be instantiated.
	 */
	private RecentFileUtil()
	{
	}

	/**
	 * Initialize the recentFiles attribute.
	 * 
	 * @return The recentFiles String array.
	 */
	private static String[] initRecentFiles()
	{

		final String[] result = new String[MAX_RECENT];

		// load recent files
		final String strRecentFiles = SettingService.getSettingService().getSetting(SettingKey.RECENT_FILES, String.class);

		// create result
		if (strRecentFiles != null)
		{
			final String[] items = strRecentFiles.split(SEPARATOR);

			for (int i = 0; i < result.length && i < items.length; i++)
			{
				result[i] = items[i];
			}
		}

		return result;
	}

	/**
	 * Write recentFiles array to config file.
	 */
	private static void saveRecentFiles()
	{

		// create config string
		String strRecentFiles = null;

		for (int i = 0; i < recentFiles.length; i++)
		{

			if (!StringUtils.isEmpty(recentFiles[i]))
			{

				if (i == 0)
				{
					strRecentFiles = recentFiles[i];
				}
				else
				{
					strRecentFiles += SEPARATOR + recentFiles[i];
				}
			}
		}

		// write to config
		SettingService.getSettingService().setSetting(SettingKey.RECENT_FILES, strRecentFiles);
	}

	/**
	 * Returns the string array of the recent files.
	 * 
	 * @return The string array of the recent files.
	 */
	public static String[] getRecentFiles()
	{
		return recentFiles;
	}

	/**
	 * Checks how many file paths the recentFiles String contains.
	 * 
	 * @return The number of previously opened files.
	 */
	public static int nrOfRecentFiles()
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
	 * 
	 * @param s - The path to the file
	 * @return true if the path already is in the recent files menu; false otherwise.
	 */
	public static boolean pathNameExists(final String s)
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
	 * 
	 * @param s     - The path to the file
	 * @param exist - Do the path already exists?
	 */
	public static void updateOrder(final String s, final boolean exist)
	{
		final String[] tmpFiles = new String[MAX_RECENT];

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
				for (int j = 1; j <= nrOfRecentFiles() && j < MAX_RECENT; j++)
				{
					tmpFiles[j] = recentFiles[j - 1];
				}
			}
		}
		recentFiles[0] = s;
		int n = 1;
		while (n < MAX_RECENT && tmpFiles[n] != null)
		{
			recentFiles[n] = tmpFiles[n];
			n++;
		}

		// save
		saveRecentFiles();
	}

	/**
	 * The position which the filepath has in the recent files menu.
	 * 
	 * @param s - The path to the file
	 * @return The position in the m_recentFiles String[] as an integer.
	 */
	private static int pathNamePosition(final String s)
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
}
