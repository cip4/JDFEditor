package org.cip4.jdfeditor;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

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
    private Hashtable filters = null;
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
    public boolean accept(File file)
    {
        if (file != null)
        {
            if (file.isDirectory())
            {
                return true;
            }
            final String extension = getExtension(file);
            if (extension != null && filters.get(getExtension(file)) != null)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method getExtension.
     * @param file
     * @return String
     */
    public String getExtension(File file)
    {
        if (file != null)
        {
            final String filename = file.getName();
            final int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1)
            {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
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
    public String getDescription()
    {
        if (fullDescription == null)
        {
            if (description == null || isExtensionListInDescription())
            {
                fullDescription = description == null ? "(" : description + " (";
                // build the description from the extension list
                final Enumeration extensions = filters.keys();
                if (extensions != null)
                {
                    fullDescription += "." + (String) extensions.nextElement();
                    while (extensions.hasMoreElements())
                    {
                        fullDescription += ", ."+ (String) extensions.nextElement();
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
            filters = new Hashtable(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
    }

}
