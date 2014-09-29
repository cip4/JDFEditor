package org.cip4.tools.jdfeditor.streamloader;

/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2011 The International Cooperation for the Integration of 
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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class PluginLoader
 *
 */
public class PluginLoader<I>
{
	private static final Log LOGGER = LogFactory.getLog(PluginLoader.class);

	private static final String PLUGIN_FILE_EXT = ".jar";

	private static final String CLASS_EXT = ".class";

	private final Class<I> m_interf;
	private final File m_filePluginDir;

	private ArrayList<I> m_alPlugins = null;

	/**
	 * Constructor PluginLoader
	 * 
	 * @param interf
	 * @param filePluginDir
	 */
	public PluginLoader(Class<I> interf, File filePluginDir)
	{
		m_interf = interf;
		m_filePluginDir = filePluginDir;
	}

	/**
	 * Method getPlugins
	 * 
	 * @return
	 */

	public List<I> getPlugins()
	{
		if (m_alPlugins == null)
		{
			final ArrayList<I> alPlugins = new ArrayList<I>();

			final File[] fileList = m_filePluginDir.listFiles(new PlugInFilter());

			if ((fileList != null) && (fileList.length > 0))
			{
				////////////////////////////////////////////////////////
				// Create URLs for all found files.
				//

				final URL[] urls = new URL[fileList.length];

				for (int i = 0; i < fileList.length; i++)
				{
					try
					{
						urls[i] = fileList[i].toURI().toURL();
					}

					catch (MalformedURLException ex)
					{
						urls[i] = null;
					}
				}

				////////////////////////////////////////////////////////
				// Create class loader which loads all found files.
				//

				final URLClassLoader classLoader = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());

				for (File file : fileList)
				{
					try
					{
						addImplementingClasses(file, alPlugins, classLoader);
					}

					catch (IOException ex)
					{
						LOGGER.error("failed loading plugin " + file.getAbsolutePath(), ex);
					}
				}
			}

			alPlugins.add((I) new ZipStreamLoader());
			m_alPlugins = alPlugins;
		}

		return (m_alPlugins);
	}

	/**
	 * Class PlugInFilter
	 *
	 */

	private static class PlugInFilter implements FileFilter
	{
		@Override
		public boolean accept(File file)
		{
			return (file.getName().toLowerCase().endsWith(PLUGIN_FILE_EXT));
		}
	}

	/**
	 * Method addImplementingClasses
	 * 
	 * @param file
	 * @param alPlugins
	 * @param classLoader
	 * 
	 * @throws IOException
	 */

	private void addImplementingClasses(File file, ArrayList<I> alPlugins, ClassLoader classLoader) throws IOException
	{
		final JarFile jarFile = new JarFile(file, true);

		final Enumeration<JarEntry> jarEntries = jarFile.entries();

		if (jarEntries != null)
		{
			while (jarEntries.hasMoreElements())
			{
				final JarEntry jarEntry = jarEntries.nextElement();

				final String strFullQualifiedClassName = getFullQualifiedClassName(jarEntry);

				if (strFullQualifiedClassName != null)
				{
					try
					{
						final Class<?> clazz = Class.forName(strFullQualifiedClassName, true, classLoader);
						final List<Class<?>> lstInterfaces = Arrays.asList(clazz.getInterfaces());

						if (lstInterfaces.contains(m_interf))
						{
							alPlugins.add((I) clazz.newInstance());
						}
					}

					catch (Exception ex)
					{
						LOGGER.error("failed reading plugin " + file.getAbsolutePath(), ex);
					}
				}
			}
		}
	}

	/**
	 * Method getFullQualifiedClassName
	 * 
	 * @param jarEntry
	 * 
	 * @return
	 */

	private String getFullQualifiedClassName(final JarEntry jarEntry)
	{
		String strFullQualifiedClassName = null;

		final String strJarEntryName = jarEntry.getName();

		if (strJarEntryName.toLowerCase().endsWith(CLASS_EXT))
		{
			final String strJarEntryNameWithoutClass = strJarEntryName.substring(0, strJarEntryName.length() - CLASS_EXT.length());

			strFullQualifiedClassName = strJarEntryNameWithoutClass.replaceAll("/", ".");
		}

		return (strFullQualifiedClassName);
	}
}
