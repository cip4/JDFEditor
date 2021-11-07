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
package org.cip4.tools.jdfeditor.transport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.extensions.MessageHelper;
import org.cip4.jdflib.extensions.XJMFHelper;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.util.ByteArrayIOStream;
import org.cip4.jdflib.util.ContainerUtil;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.jdflib.util.file.RollingBackupDirectory;
import org.cip4.jdflib.util.mime.MimeReader;
import org.cip4.tools.jdfeditor.EditorUtils;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.pane.MessageBean;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.view.MainView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * simple dumping servlet
 *
 * @author rainer prosi
 *
 */
public class JMFServlet extends HttpServlet
{
	private static final Log LOGGER = LogFactory.getLog(JMFServlet.class);
	private static final String UTF_8 = "UTF-8";
	private static final long serialVersionUID = 1L;
	private static final String HEADER_CONTENT_ID = "Content-ID";
	private static final String CONTENT_JMF = "application/vnd.cip4-jmf+xml";
	private static final String CONTENT_JDF = "application/vnd.cip4-jdf+xml";
	private static final String CONTENT_PDF = "application/pdf";

	private String lastDump;
	private RollingBackupDirectory dumpDir;
	private final JDFFrame jdfFrame = MainView.getFrame();

	/**
	 *
	 */
	public JMFServlet()
	{
	}

	private RollingBackupDirectory getDump()
	{
		final SettingService settingService = SettingService.getSettingService();
		final String dump = settingService.getSetting(SettingKey.HTTP_STORE_PATH, String.class);
		if (!ContainerUtil.equals(dump, lastDump))
		{
			dumpDir = new RollingBackupDirectory(new File(dump), 420, "http_received");
			lastDump = dump;
		}
		return dumpDir;
	}

	@Override
	public void doPost(final HttpServletRequest req, final HttpServletResponse res) throws IOException
	{
		LOGGER.debug("received doPost: " + this);
		try
		{
			processMessage(req, res);
		}
		catch (final Exception e)
		{
			final String err = "The request body could not be processed. Maybe it did not contain JMF or JDF?";
			LOGGER.error(err, e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, err + e);
		}
	}

	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException
	{
		final String msg = "Received HTTP GET request from " + req.getHeader("User-Agent") + " @ " + req.getRemoteHost() + " (" + req.getRemoteAddr() + "). Request ignored.";
		LOGGER.warn(msg);
		res.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "HTTP GET not implemented.");
	}

	private void processMessage(final HttpServletRequest req, final HttpServletResponse res) throws IOException
	{
		final String logMessage = "Receiving message from " + req.getHeader("User-Agent") + " @ " + req.getRemoteHost() + " (" + req.getRemoteAddr() + ")...";
		LOGGER.info(logMessage);
		//		Build incoming Message
		final String headerContentType = req.getHeader("Content-type");
		LOGGER.info("header Content-type: " + headerContentType);

		final boolean isMultipart = MimeUtil.isMimeMultiPart(headerContentType);
		LOGGER.info("isMultipart: " + isMultipart);

		final InputStream inputStream = req.getInputStream();

		if (getDump() == null)
		{
			LOGGER.error("no http dump defined");
			return;
		}

		if (isMultipart)
		{
			processMultipartPostMessage2(inputStream, headerContentType);
		}
		else
		{
			processPlainPostMessage(inputStream, headerContentType);
		}
	}

	private void processPlainPostMessage(final InputStream inputStream, final String headerContentType) throws IOException
	{

		final ByteArrayIOStream byteArrayIOStream = new ByteArrayIOStream(inputStream);
		final JDFDoc doc = EditorUtils.parseInStream(byteArrayIOStream, null);

		String extension = UrlUtil.getExtensionFromMimeType(headerContentType);
		if (extension == null)
			extension = "log";
		String type = "unknown";
		String device = "unknown";
		JDFDate timestamp = null;
		if (doc != null)
		{
			final JDFJMF jmf = doc.getJMFRoot();
			if (jmf == null)
			{
				final XJMFHelper h = XJMFHelper.getHelper(doc);
				if (h != null)
				{
					final MessageHelper message = h.getMessageHelper(0);
					type = message == null ? "xjmf" : message.getType();
					if (message != null)
					{
						device = message.getDeviceID();
						timestamp = JDFDate.createDate(message.getHeader().getAttribute(AttributeName.TIME));
					}
				}
			}
			else
			{
				final JDFMessage message = jmf.getMessageElement(null, null, 0);
				type = message == null ? "jmf" : message.getType();
				if (message != null)
				{
					timestamp = message.getTime();
					device = message.getSenderID();
				}
			}
		}
		final File dumpFile = dumpDir.getNewFileWithExt(type + "." + extension);
		FileUtil.streamToFile(byteArrayIOStream.getInputStream(), dumpFile);
		if (timestamp == null)
			timestamp = new JDFDate();
		final MessageBean msg = new MessageBean(device, timestamp, type + "." + extension, dumpFile);
		jdfFrame.getBottomTabs().getHttpPanel().addMessage(msg);

	}

	private void processMultipartPostMessage2(final InputStream inputStream, final String headerContentType) throws IOException
	{
		final String type = "MJM";
		final File dumpFile = dumpDir.getNewFileWithExt(type);
		LOGGER.debug("dumpFile path: " + dumpFile.getAbsolutePath());
		FileUtil.streamToFile(ByteArrayIOStream.getBufferedInputStream(inputStream), dumpFile);

		final FileInputStream fileInputStream = FileUtils.openInputStream(dumpFile);

		final MimeReader mr = new MimeReader(fileInputStream);
		final BodyPart[] bodyPartArray = mr.getBodyParts();
		LOGGER.info("Received total parts: " + bodyPartArray.length);

		boolean hasJmfInside = false;
		boolean hasJdfInside = false;
		boolean hasPdfInside = false;
		for (int bodyPartNumber = 0; bodyPartNumber < bodyPartArray.length; bodyPartNumber++)
		{
			final BodyPart part = bodyPartArray[bodyPartNumber];
			try
			{
				if (part.isMimeType(CONTENT_JMF))
				{
					hasJmfInside = true;
					final InputStream is = (InputStream) part.getContent();
					final String jmfRequestString = IOUtils.toString(is, UTF_8);
					validateUrlResources(jmfRequestString, bodyPartNumber, bodyPartArray);
				}
				else if (part.isMimeType(CONTENT_JDF))
				{
					hasJdfInside = true;
					final InputStream is = (InputStream) part.getContent();
					final String jdfRequestString = IOUtils.toString(is, UTF_8);
					validateUrlResources(jdfRequestString, bodyPartNumber, bodyPartArray);
				}
				else if (part.isMimeType(CONTENT_PDF))
				{
					hasPdfInside = true;
				}
			}
			catch (final MessagingException e)
			{
				LOGGER.error("Error: " + e.getMessage() + ", while processing bodyPartNumber: " + bodyPartNumber, e);
			}
		}

		String messageTypeFull = type;
		if (hasJmfInside || hasJdfInside || hasPdfInside)
		{
			final String partJmf = (hasJmfInside ? "JMF," : "");
			final String partJdf = (hasJdfInside ? "JDF," : "");
			final String partPdf = (hasPdfInside ? "PDF," : "");

			messageTypeFull = type + " (" + partJmf + partJdf + partPdf;
			messageTypeFull = messageTypeFull.substring(0, messageTypeFull.length() - 1);
			messageTypeFull += ")";
		}

		fileInputStream.close();
		final MessageBean msg = new MessageBean("---", new JDFDate(0), messageTypeFull, dumpFile);
		jdfFrame.getBottomTabs().getHttpPanel().addMessage(msg);
	}

	private void validateUrlResources(final String jmfRequestString, final int bodyPartNumber, final BodyPart[] bodyPartArray)
	{
		final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try
		{
			final DocumentBuilder builder = builderFactory.newDocumentBuilder();
			final Document document = builder.parse(new ByteArrayInputStream(jmfRequestString.getBytes()));
			final XPath xPath = XPathFactory.newInstance().newXPath();
			final String prefix = "cid:";
			final String expressionUrl = "//@URL";
			final NodeList urlNodeList = (NodeList) xPath.compile(expressionUrl).evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < urlNodeList.getLength(); i++)
			{
				final Node node = urlNodeList.item(i);
				if (node.getNodeType() == Node.ATTRIBUTE_NODE)
				{
					final String url = node.getNodeValue();
					LOGGER.debug("URL found: " + url + " at part number: " + bodyPartNumber);

					if (url.startsWith(prefix))
					{
						final String exactCid = "<" + url.substring(prefix.length(), url.length()) + ">";
						if (!isContentIdExistInParts(exactCid, bodyPartArray))
						{
							LOGGER.fatal("Validation error: Expected Content-ID does not exist");
						}
					}
				}
			}
		}
		catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
		{
			LOGGER.error("Exception occured: " + e.getMessage(), e);
			return;
		}
	}

	private boolean isContentIdExistInParts(final String resource, final BodyPart[] partsArray)
	{
		for (int partNumber = 0; partNumber < partsArray.length; partNumber++)
		{
			final BodyPart part = partsArray[partNumber];
			try
			{
				if (part.getHeader(HEADER_CONTENT_ID) != null)
				{
					final String contentId = part.getHeader(HEADER_CONTENT_ID)[0];
					if (contentId.equalsIgnoreCase(resource))
					{
						LOGGER.debug("Confirmed resource at bodyPartNumber: " + partNumber);
						return true;
					}
				}
				else
				{
					LOGGER.fatal("Potential validation error: No Content-ID exist in part of MIME, partNumber: " + partNumber);
				}
			}
			catch (final MessagingException e)
			{
				LOGGER.error("Error: " + e.getMessage(), e);
			}
		}
		return false;
	}

	/*private void processMultipartPostMessage(final InputStream inputStream) throws IOException
	{
		final MimeReader mr = new MimeReader(inputStream);
		final BodyPart[] bp = mr.getBodyParts();

		LOGGER.info("Received total parts: " + bp.length);

		for (int bodyPartNumber = 0; bodyPartNumber < bp.length; bodyPartNumber++)
		{
			BodyPart part = bp[bodyPartNumber];
			try
			{
				if (part.isMimeType(CONTENT_JMF))
				{
					LOGGER.info("Processing bodyPartNumber: " + bodyPartNumber);
					SharedByteArrayInputStream is = (SharedByteArrayInputStream) part.getContent();
					String jmfRequestString = IOUtils.toString(is, UTF_8);
	//					LOGGER.info("jmfRequestString: " + jmfRequestString);

	//					next 4 lines are quite tricky, idea is - to get JDFJMF initialized from String
					JDFDoc jdfDocTemp = new JDFDoc();
					XMLDoc xmlDoc = jdfDocTemp.parseString(jmfRequestString);
					JDFDoc jdfDoc = new JDFDoc(xmlDoc);
					JDFJMF jmf = jdfDoc.getJMFRoot();

					if ((jmf == null) || (jmf.isJDFNode()))
					{
						LOGGER.info("Skip bodyPartNumber: " + bodyPartNumber + ", jmf: " + jmf);
						continue;
					}

					String originalJmf = jmf.toDisplayXML(INDENT);
					LOGGER.info("bodyPartNumber: " + bodyPartNumber + ", originalJmf: \n" + originalJmf);
					InputStream inputStreamJmf = IOUtils.toInputStream(originalJmf, UTF_8);

					processJmfMessage(jmf, inputStreamJmf);
				} else
				{
					LOGGER.info("Skip bodyPartNumber: " + bodyPartNumber);
				}
			}
			catch (MessagingException e)
			{
				LOGGER.error("Error: " + e.getMessage() + ", while processing bodyPartNumber: " + bodyPartNumber, e);
			}
		}
	}*/
}
