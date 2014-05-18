package org.cip4.tools.jdfeditor.refactor;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

import org.cip4.jdflib.util.FileUtil;

/**
 * @author AnderssA ThunellE
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JDFFileFilter extends FileFilter
{
	private Hashtable<String, JDFFileFilter> filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	/**
	 * @see java.lang.Object#Object()
	 */
	public JDFFileFilter()
	{
		super();
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file)
	{
		if (file != null)
		{
			if (file.isDirectory())
			{
				return true;
			}
			final String extension = FileUtil.getExtension(file);
			if (extension != null && filters.get(extension) != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public void setDescription(String _description)
	{
		this.description = _description;
		fullDescription = null;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription()
	{
		if (fullDescription == null)
		{
			if (description == null || isExtensionListInDescription())
			{
				fullDescription = description == null ? "(" : description + " (";
				// build the description from the extension list
				final Enumeration<String> extensions = filters.keys();
				if (extensions != null)
				{
					fullDescription += "." + extensions.nextElement();
					while (extensions.hasMoreElements())
					{
						fullDescription += ", ." + extensions.nextElement();
					}
				}
				fullDescription += ")";
			}
			else
			{
				fullDescription = description;
			}
		}
		return fullDescription;

	}

	/**
	 * Method isExtensionListInDescription.
	 * @return boolean
	 */
	public boolean isExtensionListInDescription()
	{
		return useExtensionsInDescription;
	}

	/**
	 * Method setExtensionListInDescription.
	 * @param b
	 */
	public void setExtensionListInDescription(boolean b)
	{
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	/**
	 * Method addExtension.
	 * @param extension
	 */
	public void addExtension(String extension)
	{
		if (filters == null)
		{
			filters = new Hashtable<String, JDFFileFilter>(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

}
