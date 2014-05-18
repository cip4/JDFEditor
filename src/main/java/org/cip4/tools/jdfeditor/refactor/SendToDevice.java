/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2014 The International Cooperation for the Integration of 
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
package org.cip4.tools.jdfeditor.refactor;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.elementwalker.AttributeReplacer;
import org.cip4.jdflib.jmf.JDFCommand;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;
import org.cip4.jdflib.jmf.JDFQueueSubmissionParams;
import org.cip4.jdflib.jmf.JDFReturnQueueEntryParams;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlUtil;

import javax.mail.Multipart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author MRE (Institute for Print and Media Technology) History: 20040903 MRE send MIME multipart/related
 */
public class SendToDevice extends JPanel implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4676135228882149268L;

	private JTextField urlPath;
	private JTextField urlReturn;
	private JRadioButton rbRawXML;
	private JRadioButton rbJMF;
	private JRadioButton rbMIME;
	private JRadioButton rbPackageAll;
	private JCheckBox cbReturn;

	// private JButton browse;

	/**
	 * 
	 */
	public SendToDevice()
	{
		super();
		setLayout(new GridLayout(5, 1, 0, 5));
		init();
		setVisible(true);
	}

	/**
	 * Creates the input field for the URL of the device. The behaviour of the of the input window depends on the settings in the Editor.ini -if method is set
	 * to "JMF" - the message will be send as a SubmitQueueEntry with the location of the JDF as an URL - if method is set to "MIME" a multipart/related message
	 * will be send - if method is set to "User" the user will be able to choose the method to be send by means of radio buttons
	 */
	private void init()
	{
		final JDFDoc d = Editor.getJDFDoc();
		final JDFJMF jmf = d == null ? null : d.getJMFRoot();
		final JLabel urlText = new JLabel(Editor.getString("setURL"));
		urlText.setVerticalAlignment(SwingConstants.BOTTOM);
		add(urlText);

		final INIReader iniFile = Editor.getIniFile();

		if (jmf == null)
		{
			// RadioButtons to choose sending JDF with QueueSubmissionParams
			// and URL or as a multipart/related MIME message
			rbJMF = new JRadioButton(Editor.getString("sendMethodJMF"));
			rbMIME = new JRadioButton(Editor.getString("sendMethodMIME"));
			rbRawXML = new JRadioButton(Editor.getString("sendMethodRaw"));
			rbPackageAll = new JRadioButton(Editor.getString("PackageAll"));
			cbReturn = new JCheckBox(Editor.getString("returnJMF"));
			if (iniFile.getMethodSendToDevice().equals("MIME"))
			{
				rbMIME.setSelected(true);
			}
			else if (iniFile.getMethodSendToDevice().equals("JMF"))
			{
				rbJMF.setSelected(true);
			}
			else
			{
				rbRawXML.setSelected(true);
			}
			rbMIME.addActionListener(this);
			rbJMF.addActionListener(this);
			rbRawXML.addActionListener(this);
			rbPackageAll.addActionListener(this);
			final ButtonGroup sendMethodGroup = new ButtonGroup();
			sendMethodGroup.add(rbJMF);
			sendMethodGroup.add(rbMIME);
			sendMethodGroup.add(rbRawXML);
			sendMethodGroup.add(rbPackageAll);

			final JLabel rbLabel = new JLabel(Editor.getString("sendMethod"));
			final Box SendMethodBox = Box.createHorizontalBox();

			SendMethodBox.add(rbLabel);
			SendMethodBox.add(rbJMF);
			SendMethodBox.add(rbMIME);
			SendMethodBox.add(rbRawXML);
			SendMethodBox.add(rbPackageAll);
			add(SendMethodBox);
			add(cbReturn);
			urlReturn = initURL(Editor.getString("returnToURL"), iniFile.getURLReturnToDevice());
		}
		urlPath = initURL(Editor.getString("pathToURL"), iniFile.getURLSendToDevice());
	}

	/**
	 * @param url the url label
	 * @param preset
	 * @return the set textfield
	 */
	private JTextField initURL(final String url, final String preset)
	{
		final Box urlBox = Box.createHorizontalBox();
		final JLabel urlLabel = new JLabel(url);
		urlBox.add(urlLabel);
		final JTextField tf = new JTextField(50);
		tf.setText(preset);
		urlBox.add(tf);
		urlBox.add(Box.createHorizontalStrut(5));
		add(urlBox);
		return tf;
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final INIReader ini = Editor.getIniFile();
		final Object eSrc = e.getSource();
		if (eSrc == rbJMF || eSrc == rbMIME || eSrc == rbRawXML)
		{
			if (rbJMF.isSelected())
				ini.setMethodSendToDevice("JMF");
			else if (rbMIME.isSelected())
				ini.setMethodSendToDevice("MIME");
			else if (rbRawXML.isSelected())
				ini.setMethodSendToDevice("RAW");

		}
		else if (eSrc == rbPackageAll)
		{
			ini.setPackageAll(rbPackageAll.isSelected());
		}
	}

	/**
	 * sendJMFJDFmime sends the actual open JDF as a MIME multipart/related message to the given URL
	 * 
	 * @param url
	 * @param bMime
	 * @param returnURL
	 * @return true if success
	 */
	private boolean sendJDF()
	{
		URL url = getURL(false);
		if (url == null)
		{
			EditorUtils.errorBox("ErrorSendDevice", "URL =null");
		}
		// create a JMF message
		final EditorDocument editorDoc = Editor.getEditorDoc();
		final JDFDoc theDoc = editorDoc.getJDFDoc();
		final JDFJMF theJMF = theDoc.getJMFRoot();
		if (theJMF != null || isRaw())
		{
			return sendRaw(url);
		}

		if (cbReturn.isSelected())
		{
			return returnJDFFromDevice(url, isMime(), isPackageAll());
		}
		else
		{
			return submitJDFToDevice(url, isMime(), getURL(true), isPackageAll());
		}
	}

	/**
	 * @param url
	 * @param bMime
	 * @param returnURL
	 * @return true if success
	 */
	private boolean submitJDFToDevice(final URL url, final boolean bMime, final URL returnURL, final boolean packageAll)
	{
		boolean bSendTrue;
		final JDFDoc jmfDoc = new JDFDoc("JMF");
		final JDFCommand command = jmfDoc.getJMFRoot().appendCommand(EnumType.SubmitQueueEntry);
		final JDFQueueSubmissionParams qsp = command.appendQueueSubmissionParams();
		if (returnURL != null)
		{
			qsp.setReturnJMF(returnURL);
		}
		int rc = -2;
		String message = "Snafu";
		final EditorDocument editorDoc = Editor.getEditorDoc();
		if (editorDoc == null)
		{
			return false;
		}
		HttpURLConnection uc = null;
		try
		{
			final JDFDoc theDoc = editorDoc.getJDFDoc().clone();

			final KElement root = theDoc.getRoot();
			if (root == null)
			{
				return false;
			}
			updateJobID(root);
			if (bMime)
			{
				qsp.setURL("dummy");
				final Multipart mp = MimeUtil.buildMimePackage(jmfDoc, theDoc, packageAll);
				uc = MimeUtil.writeToURL(mp, url.toExternalForm());
			}
			else
			{
				uc = jmfDoc.write2HTTPURL(url, null);
			}
			if (uc != null)
			{
				rc = uc.getResponseCode();
				message = uc.getResponseMessage();
			}
			bSendTrue = rc == 200;
			if (bSendTrue)
				createResponse(uc);
		}
		catch (final Exception x)
		{
			bSendTrue = false;
		}

		if (!bSendTrue)
		{
			EditorUtils.errorBox("ErrorSendDevice", "Bad reguest; rc= " + rc + " : " + message);
		}

		return bSendTrue;
	}

	/**
	 * @param url
	 * @param bMime
	 * @param returnURL
	 * @return true if success
	 */
	private boolean returnJDFFromDevice(final URL url, final boolean bMime, final boolean packageAll)
	{
		boolean bSendTrue;
		final JDFDoc jmfDoc = new JDFDoc("JMF");
		final JDFCommand command = jmfDoc.getJMFRoot().appendCommand(EnumType.ReturnQueueEntry);
		final JDFReturnQueueEntryParams rqp = command.appendReturnQueueEntryParams();
		int rc = -2;
		String message = "Snafu";
		final EditorDocument editorDoc = Editor.getEditorDoc();
		if (editorDoc == null)
		{
			return false;
		}
		HttpURLConnection uc = null;
		try
		{
			final JDFDoc theDoc = editorDoc.getJDFDoc().clone();
			if (bMime)
			{
				rqp.setURL("dummy");
				final Multipart mp = MimeUtil.buildMimePackage(jmfDoc, theDoc, packageAll);
				uc = MimeUtil.writeToURL(mp, url.toExternalForm());
			}
			else
			{
				uc = jmfDoc.write2HTTPURL(url, null);
			}
			if (uc != null)
			{
				rc = uc.getResponseCode();
				message = uc.getResponseMessage();
				if (rc == 200)
					createResponse(uc);

			}
			bSendTrue = rc == 200;
		}
		catch (final Exception x)
		{
			bSendTrue = false;
		}

		if (!bSendTrue)
		{
			EditorUtils.errorBox("returnJDFFromDevice", "Bad reguest; rc= " + rc + " : " + message);
		}

		return bSendTrue;
	}

	/**
	 * @param root
	 */
	private void updateJobID(final KElement root)
	{
		final INIReader ir = Editor.getIniFile();
		int inc = ir.getJobIncrement();
		inc = ++inc % 10000;
		ir.setJobIncrement(inc);
		final String jobID = root.getAttribute(AttributeName.JOBID) + "." + inc;
		final AttributeReplacer ar = new AttributeReplacer("JobID", jobID, null);
		ar.replace(root);
	}

	/**
	 * @param url
	 * @return true if ok
	 */
	private boolean sendRaw(final URL url)
	{
		boolean bSendTrue = true;
		final EditorDocument editorDoc = Editor.getEditorDoc();
		final JDFDoc theDoc = editorDoc == null ? null : editorDoc.getJDFDoc();
		if (theDoc == null)
		{
			return false;
		}

		final HttpURLConnection con = theDoc.write2HTTPURL(url, null);

		try
		{
			bSendTrue = con != null && con.getResponseCode() == 200;
			if (bSendTrue)
				createResponse(con);
		}
		catch (final IOException x)
		{
			bSendTrue = false;
		}
		return bSendTrue;
	}

	/**
	 * @param con
	 * @throws IOException
	 */
	private void createResponse(final HttpURLConnection con) throws IOException
	{
		if (con != null)
		{
			final JDFDoc d2 = new JDFParser().parseStream(con.getInputStream());
			if (d2 != null)
			{
				String newFileName = Editor.getEditorDoc().getOriginalFileName();
				newFileName = UrlUtil.newExtension(newFileName, ".resp.jmf");
				d2.write2File(newFileName, 2, true);
				Editor.getFrame().readFile(new File(newFileName));
			}
			else
			{
				JOptionPane.showMessageDialog(Editor.getFrame(), "No Message Response recieved");
			}
		}
	}

	/**
	 * getURL
	 * 
	 * returns the URL of the device given by the user
	 * @param bReturn if true, the url is the returnurl, else it is the device url
	 * 
	 * @return URL the url
	 */
	public URL getURL(final boolean bReturn)
	{
		// returns the URL given by the user
		URL url = null;
		final JTextField tf = bReturn ? urlReturn : urlPath;
		final String urlText = tf == null ? null : tf.getText();

		if (bReturn && KElement.isWildCard(urlText))
		{
			return null;
		}

		try
		{
			url = new URL(urlText);
			final INIReader ini = Editor.getIniFile();
			if (bReturn)
			{
				ini.setURLReturnToDevice(urlText);
			}
			else
			{
				ini.setURLSendToDevice(urlText);
			}
		}
		catch (final MalformedURLException e)
		{
			EditorUtils.errorBox("InvalidURL", ":" + urlText);
			return null;
		}
		return url;
	}

	/**
	 * 
	 */
	public void trySend()
	{
		if (Editor.getEditorDoc() == null)
		{
			return;
		}
		// get the URL to send to and call the CommunicationController
		boolean bSendTrue = false;
		final String[] options = { Editor.getString("OkKey"), Editor.getString("CancelKey") };

		final int option = JOptionPane.showOptionDialog(Editor.getFrame(), this, Editor.getString("JDFSendToDevice"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			// the send method depends on the settings in the Editor.ini file
			bSendTrue = sendJDF();
		}

		// show success in a popup window
		String sLabel = (bSendTrue) ? Editor.getString("JDFSent") : Editor.getString("JDFNotSent");
		URL url = getURL(false);
		sLabel += "\n\nURL= " + url.toExternalForm();
		if (bSendTrue)
		{
			final INIReader iniFile = Editor.getIniFile();
			iniFile.writeINIFile();
			JOptionPane.showMessageDialog(Editor.getFrame(), sLabel, "", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(Editor.getFrame(), sLabel, "SNAFU", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean isPackageAll()
	{
		return rbPackageAll == null ? false : rbPackageAll.isSelected();
	}

	/**
	 * @return true if raw is selected
	 */
	private boolean isRaw()
	{
		return rbRawXML.isSelected();
	}

	/**
	 * @return true if mime is selected
	 */
	private boolean isMime()
	{
		boolean simpleMime = rbMIME == null ? false : rbMIME.isSelected();
		return simpleMime || isPackageAll();
	}

}
