/*
*
* The CIP4 Software License, Version 1.0
*
*
* Copyright (c) 2001-2021 The International Cooperation for the Integration of
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

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;

/**
 * generic file chooser for the editor
 * 
 * @author prosirai
 *
 */
public class EditorFileChooser extends JFileChooser
{
	private static final long serialVersionUID = 1835778964946902960L;
	final static String allFilesOpen = "xml jdf jmf mim mjm mjd xjdf ptk jsn json zip";
	final static String allFilesSave = "xml jdf jmf mim mjm mjd xjdf ptk zip";
	final static String allFilesJSON = "jsn json rtf";

	/**
	 *
	 * @param arg0
	 * @param fileTypes
	 */
	public EditorFileChooser(final File arg0, final String fileTypes)
	{
		super(arg0);
		init(arg0, fileTypes);
	}

	private void init(final File file, final String fileTypes)
	{
		setApproveButtonText(ResourceUtil.getMessage("OkKey"));
		setApproveButtonMnemonic('O');
		setDialogTitle(ResourceUtil.getMessage("BrowseKey"));
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setMultiSelectionEnabled(false);
		setFilters(fileTypes);
		rescanCurrentDirectory();
		if (file != null)
		{
			setCurrentDirectory(file);
			if (!file.isDirectory())
				setSelectedFile(file);
		}

	}

	private void setFilters(final String fileTypes)
	{
		if (fileTypes == null)
			return;
		final VString v = StringUtil.tokenize(fileTypes, " ", false);
		final JDFFileFilter xmlFilterAll = new JDFFileFilter();
		xmlFilterAll.setDescription("All accepted files");
		for (int i = 0; i < v.size(); i++)
		{
			final String s = v.get(i);
			xmlFilterAll.addExtension(s);
		}
		addChoosableFileFilter(xmlFilterAll);

		for (int i = 0; i < v.size(); i++)
		{
			final String s = v.get(i);
			final JDFFileFilter xmlFilter = new JDFFileFilter();
			xmlFilter.addExtension(s);
			xmlFilter.setDescription(s.toUpperCase() + " files");
			addChoosableFileFilter(xmlFilter);
		}
		setFileFilter(xmlFilterAll);
	}

	/**
	 * @param schemaPath
	 * @return the newly set schema file, if it is readable
	 */
	public static File getSchemaURL(final JTextField schemaPath)
	{
		final String s = schemaPath.getText();
		if (!StringUtil.isEmpty(s))
		{
			final File f = new File(s);
			if (f.canRead())
			{
				return f;
			}
		}
		return null;
	}

}
