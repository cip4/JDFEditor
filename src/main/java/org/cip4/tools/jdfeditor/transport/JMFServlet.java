/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2015 The International Cooperation for the Integration of 
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

import java.awt.GraphicsEnvironment;
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
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.file.RollingBackupDirectory;
import org.cip4.jdflib.util.mime.MimeReader;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
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
	private static String TIMESTAMP_PATTERN = "yyyy-MM-dd_hh-mm-ss-SSS";

	private String lastDump;
	private RollingBackupDirectory dumpDir;
	private MainView mainView;

	public JMFServlet()
	{
		super();
		if (GraphicsEnvironment.isHeadless())
		{
//			TODO: finish with headless mode
			System.err.println("Temporary workaround for headless mode used in Bamboo CI");
		} else
		{
			mainView = new MainView();
		}
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

	/**
	 * 
	 */


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

	private void processMessage(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		String msg = "Receiving message from " + req.getHeader("User-Agent") + " @ " + req.getRemoteHost() + " (" + req.getRemoteAddr() + ")...";
		LOGGER.info(msg);
//		Build incoming Message
		String contentType = req.getHeader("Content-type");
		LOGGER.info("contentType: " + contentType);
		
		final boolean isMultipart = MimeUtil.isMimeMultiPart(contentType);
		LOGGER.info("isMultipart: " + isMultipart);
		
		InputStream inputStreamTemp = req.getInputStream();
		String requestString = IOUtils.toString(inputStreamTemp, UTF_8);
		LOGGER.info("requestString: " + requestString);
		InputStream inputStream = IOUtils.toInputStream(requestString, UTF_8);
		
		if (isMultipart) {
			processMultipartMessage(inputStream);
		} else {
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
			VElement e = jmf.getMessageVector(null, null);
			LOGGER.debug("e.size: " + e.size());
			if (getDump() == null)
			{
				LOGGER.error("no http dump defined");
				return;
			}
			for (int i = 0; i < e.size(); i++)
			{
				JDFMessage currMessage = jmf.getMessageElement(null, null, i);
				String type = currMessage.getType();
				LOGGER.debug("currMessage type: " + type);
				File f = dumpDir.getNewFileWithExt(type);
				FileUtil.streamToFile(ByteArrayIOStream.getBufferedInputStream(inputStream2), f);
				mainView.getCurrentFrame().getBottomTabs().getHttpPanel().addMessage(jmf, f);
			}
		} // else
	}

	private void processMultipartMessage(InputStream inputStream) throws IOException
	{
		final MimeReader mr = new MimeReader(inputStream);
		final BodyPart[] bp = mr.getBodyParts();

		LOGGER.info("Received total parts: " + bp.length);

		for (int bodyPartNumber = 0; bodyPartNumber < bp.length; bodyPartNumber++)
		{
			BodyPart part = bp[bodyPartNumber];
			try
			{
				if (part.isMimeType("application/vnd.cip4-jmf+xml"))
				{
					LOGGER.info("Processing bodyPartNumber: " + bodyPartNumber);
					SharedByteArrayInputStream is = (SharedByteArrayInputStream) part.getContent();
					String jmfRequestString = IOUtils.toString(is, UTF_8);
					LOGGER.info("jmfRequestString: " + jmfRequestString);

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

					VElement e = jmf.getMessageVector(null, null);
					LOGGER.info("e.size: " + e.size());
					if (getDump() == null)
					{
						LOGGER.error("no http dump defined");
						return;
					}
					for (int i = 0; i < e.size(); i++)
					{
						JDFMessage currMessage = jmf.getMessageElement(null, null, i);
						String type = currMessage.getType();
						LOGGER.debug("currMessage type: " + type);
						File f = dumpDir.getNewFileWithExt(type);
						FileUtil.streamToFile(ByteArrayIOStream.getBufferedInputStream(inputStreamJmf), f);
						mainView.getCurrentFrame().getBottomTabs().getHttpPanel().addMessage(jmf, f);
					}
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
}
