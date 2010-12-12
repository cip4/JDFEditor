/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2010 The International Cooperation for the Integration of 
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
package org.cip4.jdfeditor.transport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.cip4.jdfeditor.Editor;
import org.cip4.jdfeditor.INIReader;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;


public class JMFServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(JMFServlet.class);
	private static INIReader conf = Editor.getIniFile();
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		log.debug("received doPost: " + this);
		try {
			processMessage(req, res);
		} catch (Exception e) {
			String err = "The request body could not be processed. Maybe it did not contain JMF or JDF?";
			log.error(err, e);
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, err + e);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String msg = "Received HTTP GET request from "
				+ req.getHeader("User-Agent") + " @ " + req.getRemoteHost()
				+ " (" + req.getRemoteAddr() + "). Request ignored.";
		log.warn(msg);
		res.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "HTTP GET not implemented.");
	}
	
	public void processMessage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String msg = "Receiving message from " + req.getHeader("User-Agent")
				+ " @ " + req.getRemoteHost() + " (" + req.getRemoteAddr()
				+ ")...";
		log.debug(msg);
		// Build incoming Message
		final String messageBody = toString(req.getInputStream());
		log.debug("Incoming message body: " + messageBody);
		String contentType = req.getHeader("Content-type");
		log.debug("contentType: " + contentType);
		
		JDFJMF jmf = new JDFParser().parseString(messageBody).getJMFRoot();
		JDFMessage m = jmf.getMessageElement(null/*EnumFamily.Query*/, null, 0);
//		JDFMessage m = jmf.getMessageElement(EnumFamily.Command, null, 0);
//		String senderId = m.getSenderID();
		
		String type = m.getType();
		log.debug("type: " + type);
		
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-SSS");
		String fileName = formatter.format(today);
		if (type == null || type.equals("")) {
			fileName += "-UnknownType.jmf";
		} else {
			fileName += "-" + type + ".jmf";
		}
		log.info("Save message to file: " + fileName);
		
		String fullPathFile = conf.getHttpStorePath() + File.separator + fileName;
		File f = new File(fullPathFile);
		
		FileUtils.writeStringToFile(f, messageBody);
		log.info("Message saved as: " + fullPathFile);
	}
	
	public static String toString(InputStream input) throws IOException {
        StringWriter writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader(input);
        copy(reader, writer);
        return writer.toString();
    }
	
	public static int copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1024 * 4];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
