/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2016 The International Cooperation for the Integration of 
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.util.SharedByteArrayInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.util.ByteArrayIOStream;
import org.cip4.jdflib.util.ByteArrayIOStream.ByteArrayIOInputStream;
import org.cip4.jdflib.util.ContainerUtil;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.file.RollingBackupDirectory;
import org.cip4.jdflib.util.mime.MimeReader;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.pane.MessageBean;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * 
 * @author rainer prosi
 *
 */
public class JMFServlet extends HttpServlet
{
	private static final Log LOGGER = LogFactory.getLog(JMFServlet.class);
	private static final String UTF_8 = "UTF-8";
	private static final int INDENT = 2;
	private static final long serialVersionUID = 1L;
	private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd_hh-mm-ss-SSS";

	private static final String CONTENT_PLAIN_JMF = "application/vnd.cip4-jmf+xml";

	private String lastDump;
	private RollingBackupDirectory dumpDir;
	private JDFFrame jdfFrame = MainView.getFrame();

	public JMFServlet()
	{
	}

	private RollingBackupDirectory getDump()
	{
		final SettingService settingService = SettingService.getSettingService();
		String dump = settingService.getSetting(SettingKey.HTTP_STORE_PATH, String.class);
		if (!ContainerUtil.equals(dump, lastDump))
		{
			dumpDir = new RollingBackupDirectory(new File(dump), 200, "http_received.jmf");
			lastDump = dump;
		}
		return dumpDir;
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		LOGGER.debug("received doPost: " + this);
		try
		{
			processMessage(req, res);
		}
		catch (Exception e)
		{
			String err = "The request body could not be processed. Maybe it did not contain JMF or JDF?";
			LOGGER.error(err, e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, err + e);
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		String msg = "Received HTTP GET request from " + req.getHeader("User-Agent") + " @ " + req.getRemoteHost() + " (" + req.getRemoteAddr() + "). Request ignored.";
		LOGGER.warn(msg);
		res.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "HTTP GET not implemented.");
	}

	private void processMessage(final HttpServletRequest req, final HttpServletResponse res) throws IOException
	{
		String logMessage = "Receiving message from " + req.getHeader("User-Agent") + " @ " + req.getRemoteHost() + " (" + req.getRemoteAddr() + ")...";
		LOGGER.info(logMessage);
//		Build incoming Message
		String headerContentType = req.getHeader("Content-type");
		LOGGER.info("header Content-type: " + headerContentType);
		
		final boolean isMultipart = MimeUtil.isMimeMultiPart(headerContentType);
		LOGGER.info("isMultipart: " + isMultipart);
		
		InputStream inputStreamTemp = req.getInputStream();
		String requestString = IOUtils.toString(inputStreamTemp, UTF_8);
		LOGGER.info("requestString: " + requestString);
		InputStream inputStream = IOUtils.toInputStream(requestString, UTF_8);

		if (getDump() == null)
		{
			LOGGER.error("no http dump defined");
			return;
		}

		if (isMultipart) {
			processMultipartPostMessage2(inputStream);
		} else {
			processPlainPostMessage(inputStream);
		}
	}

	private void processPlainPostMessage(final InputStream inputStream) throws IOException
	{
		ByteArrayIOInputStream inputStream2 = ByteArrayIOStream.getBufferedInputStream(inputStream);

		JDFDoc doc = JDFDoc.parseStream(inputStream2);
		if (doc == null)
		{
			LOGGER.error("error parsing jmf");
			return;
		}
		JDFJMF jmf = doc.getJMFRoot();
		if (jmf == null)
		{
			LOGGER.error("no root jmf");
			return;
		}
		processJmfMessage(jmf, inputStream2);
	}

	private void processMultipartPostMessage2(final InputStream inputStream) throws IOException
	{
		String type = "MJM";
		File dumpFile = dumpDir.getNewFileWithExt(type);
		LOGGER.debug("dumpFile path: " + dumpFile.getAbsolutePath());
		FileUtil.streamToFile(ByteArrayIOStream.getBufferedInputStream(inputStream), dumpFile);

		final MessageBean msg = new MessageBean("---", new JDFDate(0), type, dumpFile);
		jdfFrame.getBottomTabs().getHttpPanel().addMessage(msg);
	}

	private void processMultipartPostMessage(final InputStream inputStream) throws IOException
	{
		final MimeReader mr = new MimeReader(inputStream);
		final BodyPart[] bp = mr.getBodyParts();

		LOGGER.info("Received total parts: " + bp.length);

		for (int bodyPartNumber = 0; bodyPartNumber < bp.length; bodyPartNumber++)
		{
			BodyPart part = bp[bodyPartNumber];
			try
			{
				if (part.isMimeType(CONTENT_PLAIN_JMF))
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
	}

	private void processJmfMessage(final JDFJMF jmf, final InputStream inputStream)
	{
		final VElement jmfMessagesVector = jmf.getMessageVector(null, null);

		for (int i = 0; i < jmfMessagesVector.size(); i++)
		{
			JDFMessage jmfMessage = (JDFMessage) jmfMessagesVector.get(i); // jmf.getMessageElement(null, null, i);
			String type = jmfMessage.getType();
			LOGGER.debug("jmfMessage id: " + jmfMessage.getID() + ", type: " + type);
			File dumpFile = dumpDir.getNewFileWithExt(type);
			FileUtil.streamToFile(ByteArrayIOStream.getBufferedInputStream(inputStream), dumpFile);
			final MessageBean msg = new MessageBean(jmfMessage, dumpFile);
			jdfFrame.getBottomTabs().getHttpPanel().addMessage(msg);
		}
	}
}
