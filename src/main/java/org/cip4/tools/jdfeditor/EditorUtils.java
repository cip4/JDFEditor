/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2013 The International Cooperation for the Integration of
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
 *    Alternately, this acknowledgment mrSubRefay appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior writtenrestartProcesses()
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
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIrSubRefAL DAMAGES (INCLUDING, BUT NOT
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
 * originally based on software restartProcesses()
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG
 * copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *
 */
package org.cip4.tools.jdfeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumValidationLevel;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.streamloader.IStreamLoader;
import org.cip4.tools.jdfeditor.streamloader.PluginLoader;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * static utilities for the editor
 * @author prosirai
 * 
 */
public class EditorUtils
{
	private static final Log LOGGER = LogFactory.getLog(EditorUtils.class);

	private static PluginLoader<IStreamLoader> pluginLoader = null;

	private final SettingService settingService = new SettingService();

	/**
	 * Check if a KElement is valid or not.
	 * @param elem - The KElement you want to check.
	 * @return True if the KElement is valid, false otherwise.
	 */
	public static boolean elemIsValid(final KElement elem)
	{
		if (!(elem instanceof JDFElement) || (!JDFElement.isInJDFNameSpaceStatic(elem)))
		{
			return true;
		}
		return ((JDFElement) elem).isValid(EnumValidationLevel.Complete);
	}

	/**
	 * gets all resources from this node and from all its ancestors.
	 * @param jdfNode 
	 * @param usage 
	 * @return - vector of all resources allowed to be linked from the level of jdfNode
	 */
	public static VElement getResourcesAllowedToLink(final JDFNode jdfNode, final EnumUsage usage)
	{
		final VElement resourcesInTree = new VElement();
		JDFNode nodeTmp = jdfNode;
		while (nodeTmp != null)
		{
			final JDFResourcePool resPool = nodeTmp.getResourcePool();
			if (resPool != null)
			{
				final VElement vRes = resPool.getPoolChildren(null, null, null);
				resourcesInTree.addAll(vRes);
			}
			nodeTmp = nodeTmp.getParentJDF();
		}
		final VElement vResLinks = jdfNode.getResourceLinks(null, null, null);

		final VString vValidLinks = jdfNode.linkNames();
		if (vValidLinks == null)
		{
			return null;
		}
		vValidLinks.unify();
		final VString vValidInfo = jdfNode.linkInfo();
		final int iAny = vValidLinks.index("*");

		for (int i = resourcesInTree.size() - 1; i >= 0; i--)
		{
			final JDFResource res = (JDFResource) resourcesInTree.item(i);
			final String resName = res.getNodeName();

			if (vResLinks != null)
			{
				final int vRLSize = vResLinks.size();
				final String id = res.getID();
				if (id != null)
				{
					for (int j = 0; j < vRLSize; j++)
					{
						if (id.equals(vResLinks.item(j).getAttribute(AttributeName.RREF)))
						{
							resourcesInTree.remove(res);
							continue;
						}
					}
				}
			}

			int n = vValidLinks.index(resName);
			if (n < 0)
			{
				n = iAny;
			}

			if (n < 0)
			{
				resourcesInTree.remove(res);
			}
			else if (usage != null)
			{
				final String io = usage.equals(EnumUsage.Input) ? "i" : "o";
				if (vValidInfo.stringAt(n).indexOf(io) < 0)
				{
					resourcesInTree.remove(res);
				}
			}

		}
		return resourcesInTree.size() == 0 ? null : resourcesInTree;
	}

	/**
	 * Method getAttributeOptions
	 * @param parentElement
	 * @return
	 */
	public static String[] getElementOptions(final KElement parentElement)
	{
		VString validElementsVector = new VString();
		if (parentElement instanceof JDFElement)
		{
			JDFElement jParent = (JDFElement) parentElement;
			validElementsVector = jParent.knownElements();
			final VString uniqueElementsVector = jParent.uniqueElements();

			final VString existingElementsVector = parentElement.getElementNameVector();
			for (int i = 0; i < existingElementsVector.size(); i++)
			{
				final String existingElementName = existingElementsVector.stringAt(i);
				if (uniqueElementsVector.contains(existingElementName))
				{
					// if element is unique and already in a parentElement - remove it from a valid list
					validElementsVector.remove(existingElementName);
				}
			}
		}
		final int size = validElementsVector.size();
		final String validValues[] = new String[size + 1];
		for (int i = 0; i < size; i++)
		{
			validValues[i] = validElementsVector.get(i).toString();
		}
		validValues[size] = "zzzzzz";
		Arrays.sort(validValues);
		validValues[size] = "Other..";
		return validValues;
	}

	/**
	 * Method chooseElementName. gets the valid element to insert into parentElement
	 * @param parentElement -
	 * @return the name of the selected element to insert
	 */
	public static String chooseElementName(final KElement parentElement)
	{
		final String validValues[] = EditorUtils.getElementOptions(parentElement);
		String selectedElementName = (String) JOptionPane.showInputDialog(MainView.getFrame(), "Choose an element to insert", "Insert new element", JOptionPane.PLAIN_MESSAGE, null, validValues, validValues[0]);

		if (selectedElementName != null && selectedElementName.equals("Other.."))
		{
			selectedElementName = JOptionPane.showInputDialog(MainView.getFrame(), "Choose element name", "");
		}
		return selectedElementName;
	}

	/**
	 * Method getAttributeOptions
	 * @param element 
	 * @return
	 */
	public static String[] getAttributeOptions(final KElement element)
	{
		final VString validAttributesVector = (element instanceof JDFElement) ? ((JDFElement) element).knownAttributes() : new VString();

		VString existingAttributes = element.getAttributeVector();
		if (element instanceof JDFResource)
		{
			final JDFResource re = (JDFResource) element;
			existingAttributes = re.getAttributeVector();
		}
		for (int i = 0; i < existingAttributes.size(); i++)
		{
			final String att = existingAttributes.get(i).toString();
			for (int j = 0; j < validAttributesVector.size(); j++)
			{
				if (att.equals(validAttributesVector.get(j).toString()))
				{
					validAttributesVector.remove(j);
				}
			}
		}
		final int size = validAttributesVector.size();
		final String possibleValues[] = new String[size + 1];
		for (int i = 0; i < size; i++)
		{
			possibleValues[i] = validAttributesVector.stringAt(i);
		}
		possibleValues[size] = "zzzzzzz";
		Arrays.sort(possibleValues);
		possibleValues[size] = "Other..";

		return possibleValues;
	}

	/**
	 * Method getElement. gets the KElement for this treepath or the parent in case this is an attribute selection
	 * @param path
	 * @return KElement
	 */
	public static KElement getElement(final TreePath path)
	{
		JDFTreeNode node;
		if (path == null)
		{
			node = (JDFTreeNode) MainView.getModel().getRootNode().getFirstChild();
		}
		else
		{
			node = (JDFTreeNode) path.getLastPathComponent();
		}

		if (!node.isElement()) // one level is always ok, since attributes live in elements
		{
			node = (JDFTreeNode) node.getParent();
		}

		return node.getElement();
	}

	/**
	 * Creates the String which is to be displayed...
	 * @param file the File object to display
	 * @param length - The length of the title...
	 * @return The file name, may be a little bit altered.
	 */
	public static String displayPathName(final File file, final int length)
	{
		if (file == null)
		{
			return "";
		}

		final String s = '"' + file.getAbsolutePath() + '"';

		if (s.length() <= 1.5 * length)
		{
			return s;
		}

		final int i = s.indexOf('\\');
		final int j = s.lastIndexOf('\\');

		if (i == j)
		{
			return s.substring(0, length - 4) + "..." + '"';
		}

		final String start = s.substring(0, i + 1);
		final String end = s.substring(j, s.length());

		return start + "..." + end;
	}

	// ////////////////////////////////////////////////////////////////////////////
	/**
	 * creates the standard error box
	 * @param errorKey the key in the resources that will be used to display the message
	 * @param addedString 
	 */
	public static void errorBox(final String errorKey, String addedString)
	{
		final JDFFrame frame = MainView.getFrame();
		if (addedString == null)
		{
			addedString = "";
		}
		else
		{
			addedString = " " + addedString;
		}

		JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage(errorKey) + addedString, ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
	}

	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * @param fts the file to get inputstreams from
	 * @return InputStream[] the array of inputstreams
	 */
	public static EditorDocument[] getEditorDocuments(final File fts)
	{
		if (fts == null)
		{
			return null;
		}

		EditorDocument ediDocs[] = null;
		for (int i = 0; i < 2; i++) // try the correct mime / non mime parser first
		{
			try
			{
				// if i>0, the initial parse failed and we will try the other type
				if (UrlUtil.isMIME(fts) ^ i == 1)
				{
					final String packageName = fts.getCanonicalPath();
					ediDocs = unpackMIME(fts, packageName);
				}
				else
				// standard xml parse
				{
					ediDocs = new EditorDocument[1];

					JDFDoc jdfDoc = parseInStream(fts, getSchemaLoc());
					if (jdfDoc == null)
					{
						jdfDoc = parseInStream(fts, null);
					}

					EditorDocument edidoc = null;

					if (jdfDoc != null)
					{
						edidoc = makeEditorDocument(jdfDoc, null);
					}
					if (edidoc != null)
					{
						edidoc.getJDFDoc().setOriginalFileName(fts.getPath());
						ediDocs[0] = edidoc;
					}
					else
					{
						if (i == 1)
						{
							EditorUtils.errorBox("FileNotOpenKey", ": " + fts.getName());
						}
						ediDocs = null;
					}
				}
			}
			catch (final IOException x)
			{
				if (i > 0)
				{
					return null;
				}
			}
			catch (final MessagingException x)
			{
				if (i > 0)
				{
					return null;
				}
			}
			if (ediDocs != null)
			{
				break;
			}
		}
		return ediDocs;

	}

	/**
	 * unpack the mime packeage specified in fts
	 */
	private static EditorDocument[] unpackMIME(final File fts, final String packageName) throws FileNotFoundException, MessagingException, IOException
	{
		EditorDocument[] ediDocs;
		InputStream fileStream = FileUtil.getBufferedInputStream(fts);

		// in case of spurious email header lines, skipem
		final byte b[] = new byte[1000];
		fileStream.read(b);
		final String s = new String(b);
		int posMime = s.indexOf("MIME-Version:");
		if (posMime < 0)
		{
			posMime = s.toLowerCase().indexOf("mime-version:");
		}
		fileStream.close();
		fileStream = new FileInputStream(fts);
		if (posMime > 0)
		{
			fileStream.skip(posMime);
		}
		final Multipart mp = MimeUtil.getMultiPart(fileStream);
		if (mp == null)
		{
			return null;
		}
		final int count = mp.getCount();
		int n = 0;
		// count jdfs and jmfs
		for (int i = 0; i < count; i++)
		{
			final BodyPart bp = mp.getBodyPart(i);
			if (MimeUtil.isJDFMimeType(bp.getContentType()))
			{
				n++;
			}
		}
		if (n == 0)
		{
			return null;
		}

		ediDocs = new EditorDocument[n];

		n = 0;
		for (int i = 0; i < count; i++)
		{
			final BodyPart bp = mp.getBodyPart(i);
			if (MimeUtil.isJDFMimeType(bp.getContentType()))
			{
				InputStream is = bp.getInputStream();

				JDFDoc jdfDoc = parseInStream(is, getSchemaLoc());
				if (jdfDoc == null)
				{
					is.close();
					is = bp.getInputStream();
					parseInStream(is, null);
				}
				EditorDocument edidoc = null;

				if (jdfDoc != null)
				{
					edidoc = makeEditorDocument(jdfDoc, packageName);
				}
				if (edidoc != null)
				{
					String fileName = bp.getFileName();
					if (fileName == null)
					{
						fileName = packageName + "_" + i + ".jdf";
					}
					fileName = StringUtil.unEscape(fileName, "%", 16, 2);
					fileName = StringUtil.getUTF8String(fileName.getBytes());
					jdfDoc.setOriginalFileName(fileName);
					jdfDoc.setBodyPart(bp);
					edidoc.setCID(MimeUtil.getContentID(bp));
					ediDocs[n++] = edidoc;
				}
			}
		}
		return ediDocs;
	}

	static File getSchemaLoc()
	{
		File schemaloc = null;

		SettingService s = new SettingService();
		boolean generalUseSchema = s.getSetting(SettingKey.GENERAL_USE_SCHEMA, Boolean.class);
		String validationSchemaUrl = s.getSetting(SettingKey.VALIDATION_SCHEMA_URL, String.class);

		if (generalUseSchema && validationSchemaUrl != null)
		{
			schemaloc = new File(validationSchemaUrl);
		}

		return (schemaloc);
	}

	private static EditorDocument makeEditorDocument(JDFDoc jdfDoc, String packageName)
	{
		EditorDocument edidoc = null;

		if (jdfDoc != null)
		{
			jdfDoc.clearDirtyIDs();

			if (new SettingService().getSetting(SettingKey.GENERAL_NORMALIZE, Boolean.class))
			{
				jdfDoc.getRoot().sortChildren();
			}

			final JDFFrame frame = MainView.getFrame();
			frame.setJDFDoc(jdfDoc, packageName);
			edidoc = frame.getEditorDoc();
		}

		return (edidoc);
	}

	private static JDFDoc parseInStream(InputStream inStream, File fileSchema) throws IOException
	{
		final JDFParser p = new JDFParser();

		if (fileSchema != null)
		{
			p.setJDFSchemaLocation(fileSchema);
		}

		return (p.parseStream(inStream));
	}

	/**
	 * 
	 * 
	 * @param fileJDF
	 * @param fileSchema
	 * @return
	 * @throws IOException
	 */
	static JDFDoc parseInStream(File fileJDF, File fileSchema) throws IOException
	{
		JDFDoc jdfDoc = null;

		if (pluginLoader == null)
		{
			File fileAppDir = new File(".");
			String strAppPath = EditorUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			strAppPath = URLDecoder.decode(strAppPath, StringUtil.UTF8);
			LOGGER.info("application path: " + strAppPath);
			final File fileApp = new File(strAppPath);
			if (fileApp.exists() && fileApp.isFile())
			{
				fileAppDir = fileApp.getParentFile();
			}

			final File filePluginDir = new File(fileAppDir, "plugins");
			LOGGER.info("found plugin directory: " + filePluginDir.getAbsolutePath());
			pluginLoader = new PluginLoader<IStreamLoader>(IStreamLoader.class, filePluginDir);
		}

		final List<IStreamLoader> lstStreamLoaders = pluginLoader.getPlugins();

		for (IStreamLoader loader : lstStreamLoaders)
		{
			jdfDoc = loader.read(fileJDF, fileSchema);

			if (jdfDoc != null)
			{
				break;
			}
		}

		if (jdfDoc == null)
		{
			final JDFParser p = new JDFParser();

			if (fileSchema != null)
			{
				p.setJDFSchemaLocation(fileSchema);
			}

			final FileInputStream inStream = new FileInputStream(fileJDF);
			jdfDoc = p.parseStream(inStream);
			inStream.close();
		}

		return jdfDoc;
	}
}
