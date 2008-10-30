/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2008 The International Cooperation for the Integration of 
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
package org.cip4.jdfeditor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.mail.Multipart;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.cip4.jdfeditor.Editor;
import org.cip4.jdfeditor.EditorDocument;
import org.cip4.jdfeditor.EditorUtils;
import org.cip4.jdfeditor.INIReader;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.elementwalker.AttributeReplacer;
import org.cip4.jdflib.jmf.JDFCommand;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFQueueSubmissionParams;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.MimeUtil;

/*
 * Created on 12.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author MRE (Institute for Print and Media Technology)
 * History:
 * 20040903 MRE send MIME multipart/related
 */
public class SendToDevice extends JPanel implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4676135228882149268L;

	//  private File file;
	private JTextField urlPath;
	private JTextField urlReturn;
	private JRadioButton rbJMF;
	private JRadioButton rbMIME;

	//  private JButton browse;

	/**
	 * 
	 */
	public SendToDevice()
	{
		super();
		setLayout(new GridLayout(4, 1, 0, 5));
		init();
		setVisible(true);
	}

	/**
	 * Creates the input field for the URL of the device.
	 * The behaviour of the of the input window depends on the
	 * settings in the Editor.ini
	 *  -if method is set to "JMF" - the message will be send
	 *   as a SubmitQueueEntry with the location of the JDF
	 *   as an URL
	 * - if method is set to "MIME" a multipart/related message 
	 *   will be send
	 * - if method is set to "User" the user will be able to
	 *   choose the method to be send by means of radio buttons 
	 */
	private void init()
	{
		final ResourceBundle littleBundle = Editor.getBundle();
		final JLabel urlText = new JLabel(littleBundle.getString("setURL"));
		urlText.setVerticalAlignment(SwingConstants.BOTTOM);
		add(urlText);

		final INIReader iniFile = Editor.getIniFile();

		//RadioButtons to choose sending JDF with QueueSubmissionParams
		//and URL or as a multipart/related MIME message
		rbJMF = new JRadioButton(littleBundle.getString("sendMethodJMF"));
		rbMIME = new JRadioButton(littleBundle.getString("sendMethodMIME"));
		if (iniFile.getMethodSendToDevice().equals("MIME"))
		{
			rbMIME.setSelected(true);
		}
		else
		{
			rbJMF.setSelected(true);
		}
		rbMIME.addActionListener(this);
		rbJMF.addActionListener(this);
		final ButtonGroup sendMethodGroup = new ButtonGroup();
		sendMethodGroup.add(rbJMF);
		sendMethodGroup.add(rbMIME);

		final JLabel rbLabel = new JLabel(littleBundle.getString("sendMethod"));
		final Box SendMethodBox = Box.createHorizontalBox();

		SendMethodBox.add(rbLabel);
		SendMethodBox.add(rbJMF);
		SendMethodBox.add(rbMIME);
		add(SendMethodBox);
		urlPath = initURL(littleBundle.getString("pathToURL"), iniFile.getURLSendToDevice());
		urlReturn = initURL(littleBundle.getString("returnToURL"), iniFile.getURLReturnToDevice());
	}

	/**
	 * @param url the url label
	 * @param preset
	 * @return the set textfield
	 */
	private JTextField initURL(final String url, String preset)
	{
		final Box urlBox = Box.createHorizontalBox();
		final JLabel urlLabel = new JLabel(url);
		urlBox.add(urlLabel);
		JTextField tf = new JTextField(50);
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
	public void actionPerformed(ActionEvent e)
	{
		INIReader ini = Editor.getIniFile();
		final Object eSrc = e.getSource();
		if (eSrc == rbJMF)
		{
			ini.setMethodSendToDevice(rbJMF.isSelected() ? "JMF" : "MIME");
		}
		else
		{
			ini.setMethodSendToDevice(rbMIME.isSelected() ? "MIME" : "JMF");
		}
	}

	/**
	 * sendJMFJDFmime
	 * sends the actual open JDF as a MIME multipart/related message 
	 * to the given URL
	 * 
	 * @param url
	 * @param bMime 
	 * @param returnURL 
	 * @return true if success
	 */
	public boolean sendJDF(URL url, boolean bMime, URL returnURL)
	{

		if (url == null)
		{
			EditorUtils.errorBox("ErrorSendDevice", "URL =null");
		}
		// create a JMF message
		final EditorDocument editorDoc = Editor.getEditorDoc();
		JDFDoc theDoc = editorDoc.getJDFDoc();
		JDFJMF theJMF = theDoc.getJMFRoot();
		if (theJMF != null)
			return sendJMF(url);

		return submitJDFToDevice(url, bMime, returnURL);
	}

	/**
	 * @param url
	 * @param bMime
	 * @param returnURL 
	 * @return true iff success
	 */
	private boolean submitJDFToDevice(URL url, boolean bMime, URL returnURL)
	{
		boolean bSendTrue;
		JDFDoc jmfDoc = new JDFDoc("JMF");
		JDFCommand command = jmfDoc.getJMFRoot().appendCommand(EnumType.SubmitQueueEntry);
		JDFQueueSubmissionParams qsp = command.appendQueueSubmissionParams();
		if (returnURL != null)
		{
			qsp.setReturnJMF(returnURL);
		}
		int rc = -2;
		String message = "Snafu";
		final EditorDocument editorDoc = Editor.getEditorDoc();
		if (editorDoc == null)
			return false;
		HttpURLConnection uc = null;
		try
		{
			JDFDoc theDoc = (JDFDoc) editorDoc.getJDFDoc().clone();

			JDFNode root = theDoc.getJDFRoot();
			if (root == null)
			{
				return false;
			}
			updateJobID(root);
			if (bMime)
			{
				qsp.setURL("dummy");
				Multipart mp = MimeUtil.buildMimePackage(jmfDoc, theDoc, true);
				uc = MimeUtil.writeToURL(mp, url.toExternalForm());
			}
			else
			{
				//TODO set url, but which???
				uc = jmfDoc.write2HTTPURL(url, null);
			}
			if (uc != null)
			{
				rc = uc.getResponseCode();
				message = uc.getResponseMessage();
			}
			bSendTrue = rc == 200;
		}
		catch (Exception x)
		{
			bSendTrue = false;
		}

		if (!bSendTrue)
			EditorUtils.errorBox("ErrorSendDevice", "Bad reguest; rc= " + rc + " : " + message);

		return bSendTrue;
	}

	/**
	 * @param root
	 */
	private void updateJobID(JDFNode root)
	{
		INIReader ir = Editor.getIniFile();
		int inc = ir.getJobIncrement();
		inc = ++inc % 10000;
		ir.setJobIncrement(inc);
		String jobID = root.getJobID(true) + "." + inc;
		AttributeReplacer ar = new AttributeReplacer("JobID", jobID, null);
		ar.replace(root);
	}

	/**
	 * @param url
	 * @return true if ok
	 */
	private boolean sendJMF(URL url)
	{
		boolean bSendTrue = true;
		final EditorDocument editorDoc = Editor.getEditorDoc();
		JDFDoc theDoc = editorDoc == null ? null : editorDoc.getJDFDoc();
		if (theDoc == null)
			return false;

		HttpURLConnection con = theDoc.write2HTTPURL(url, null);

		try
		{
			bSendTrue = con != null && con.getResponseCode() == 200;
		}
		catch (IOException x)
		{
			bSendTrue = false;
		}
		return bSendTrue;
	}

	/**
	 * send
	 * 
	 * performs the sending process
	 * set the MIME type application/vnd.cip4-jdf+xml 
	 * 
	 * @param url (URL of the device)
	 * @param sMessage (message to be sent)
	 * @return true if successful
	 */
	static boolean send(URL url, String sMessage)
	{
		try
		{
			HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
			httpURLconnection.setRequestMethod("POST");
			//httpURLconnection.setRequestProperty("Connection","close");
			//          httpURLconnection.setRequestProperty("Content-type", "application/vnd.cip4-jmf+xml");
			//          httpURLconnection.setRequestProperty("User-Agent", "Queue (JMF protocol)");
			//          httpURLconnection.setRequestProperty("Accept", "application/vnd.cip4-jmf+xml");
			//          httpURLconnection.setUseCaches(false);
			httpURLconnection.setDoOutput(true);
			httpURLconnection.setDoInput(true);

			final OutputStream out = httpURLconnection.getOutputStream();

			out.write(sMessage.getBytes());
			//          String s=new String(sMessage.getBytes());
			//          TODO fix sending            
			out.flush();
			out.close();

			httpURLconnection.disconnect();
		}
		catch (Exception e)
		{
			EditorUtils.errorBox("ErrorSendDevice", null);
			return false;
		}
		return true;
	}

	/**
	 * getURL
	 * 
	 * returns the URL of the device given by the user
	 * @param bReturn if true, the url is the returnurl, else it is the device url
	 * 
	 * @return URL the url
	 */

	public URL getURL(boolean bReturn)
	{
		// returns the URL given by the user
		URL url = null;
		JTextField tf = bReturn ? urlReturn : urlPath;
		final String urlText = tf.getText();

		if (bReturn && KElement.isWildCard(urlText))
			return null;

		try
		{
			url = new URL(urlText);
			INIReader ini = Editor.getIniFile();
			if (bReturn)
				ini.setURLReturnToDevice(urlText);
			else
				ini.setURLSendToDevice(urlText);
		}
		catch (MalformedURLException e)
		{
			EditorUtils.errorBox("InvalidURL", ":" + urlText);
			return null;
		}
		return url;
	}

	/**
	 * 
	 */
	//TODO display response
	public void trySend()
	{
		if (Editor.getEditorDoc() == null)
			return;
		ResourceBundle m_littleBundle = Editor.getBundle();
		//get the URL to send to and call the CommunicationController
		boolean bSendTrue = false;
		final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };

		final int option = JOptionPane.showOptionDialog(Editor.getFrame(), this, m_littleBundle.getString("JDFSendToDevice"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		URL url = null;
		if (option == JOptionPane.OK_OPTION)
		{
			//the send method depends on the settings in the Editor.ini file
			url = getURL(false);
			bSendTrue = sendJDF(url, isMime(), getURL(true));
		}

		//show success in a popup window
		String sLabel = (bSendTrue) ? m_littleBundle.getString("JDFSent") : m_littleBundle.getString("JDFNotSent");
		if (bSendTrue)
			sLabel += "\n" + url.toExternalForm();
		JOptionPane.showMessageDialog(Editor.getFrame(), sLabel);
	}

	/**
	 * @return true if mime is selected
	 */
	private boolean isMime()
	{
		return rbMIME.isSelected();
	}

}
