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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.elementwalker.AttributeReplacer;
import org.cip4.jdflib.extensions.XJDFHelper;
import org.cip4.jdflib.extensions.XJDFZipWriter;
import org.cip4.jdflib.extensions.XJMFHelper;
import org.cip4.jdflib.extensions.xjdfwalker.jdftoxjdf.JDFToXJDF;
import org.cip4.jdflib.jmf.JDFCommand;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;
import org.cip4.jdflib.jmf.JDFQueueSubmissionParams;
import org.cip4.jdflib.jmf.JDFReturnQueueEntryParams;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlPart;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.jdflib.util.mime.BodyPartHelper;
import org.cip4.jdflib.util.mime.MimeWriter;
import org.cip4.jdflib.util.mime.MimeWriter.eMimeSubType;
import org.cip4.lib.jdf.jsonutil.JSONObjHelper;
import org.cip4.lib.jdf.jsonutil.JSONWriter;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.EditorUtils;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;
import org.json.simple.JSONObject;

import jakarta.mail.Multipart;

/**
 * @author MRE (Institute for Print and Media Technology) History: 20040903 MRE send MIME multipart/related
 */
public class SendToDevice extends JPanel implements ActionListener
{
	private static final Log LOG = LogFactory.getLog(SendToDevice.class);

	SettingService settingService = SettingService.getSettingService();

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4676135228882149268L;

	private JComboBox<String> urlPath;
	private JComboBox<String> urlReturn;
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
	 * Creates the input field for the URL of the device. The behaviour of the of the input window depends on the settings in the Editor.ini -if method is set to "JMF" - the
	 * message will be send as a SubmitQueueEntry with the location of the JDF as an URL - if method is set to "MIME" a multipart/related message will be send - if method is set to
	 * "User" the user will be able to choose the method to be send by means of radio buttons
	 */
	private void init()
	{
		final JDFDoc d = MainView.getJDFDoc();
		final JDFJMF jmf = d == null ? null : d.getJMFRoot();
		final JLabel urlText = new JLabel(ResourceUtil.getMessage("setURL"));
		urlText.setVerticalAlignment(SwingConstants.BOTTOM);
		add(urlText);

		if (jmf == null)
		{
			// RadioButtons to choose sending JDF with QueueSubmissionParams
			// and URL or as a multipart/related MIME message
			rbJMF = new JRadioButton(ResourceUtil.getMessage("sendMethodJMF"));
			rbMIME = new JRadioButton(ResourceUtil.getMessage("sendMethodMIME"));
			rbRawXML = new JRadioButton(ResourceUtil.getMessage("sendMethodRaw"));
			rbPackageAll = new JRadioButton(ResourceUtil.getMessage("PackageAll"));
			cbReturn = new JCheckBox(ResourceUtil.getMessage("returnJMF"));
			if ("MIME".equals(settingService.getSetting(SettingKey.SEND_METHOD, String.class)))
			{
				rbMIME.setSelected(true);
			}
			else if ("JMF".equals(settingService.getSetting(SettingKey.SEND_METHOD, String.class)))
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

			final JLabel rbLabel = new JLabel(ResourceUtil.getMessage("sendMethod"));
			final Box SendMethodBox = Box.createHorizontalBox();

			SendMethodBox.add(rbLabel);
			SendMethodBox.add(rbJMF);
			SendMethodBox.add(rbMIME);
			SendMethodBox.add(rbRawXML);
			SendMethodBox.add(rbPackageAll);
			add(SendMethodBox);
			add(cbReturn);
			final String s = settingService.getSetting(SettingKey.SEND_URL_RETURN, String.class);
			urlReturn = initURL(ResourceUtil.getMessage("returnToURL"), s);
		}
		final String s = settingService.getSetting(SettingKey.SEND_URL_SEND, String.class);
		urlPath = initURL(ResourceUtil.getMessage("pathToURL"), s);
	}

	/**
	 * @param url    the url label
	 * @param preset
	 * @return the set textfield
	 */
	private JComboBox<String> initURL(final String url, final String preset)
	{
		final Box urlBox = Box.createHorizontalBox();
		final JLabel urlLabel = new JLabel(url);
		urlBox.add(urlLabel);
		final JComboBox<String> tf = new JComboBox<>();
		tf.setEditable(true);

		int items = 0;
		final StringTokenizer st = new StringTokenizer(preset, ";");
		while (st.hasMoreTokens())
		{
			items++;
			if (items > 5)
			{
				break; // support only 5 items, don't show others even if they are in conf file
			}
			final String s = st.nextToken();
			tf.addItem(s);
			LOG.debug("added item: " + s);
		}

		urlBox.add(tf);
		urlBox.add(Box.createHorizontalStrut(5));
		add(urlBox);
		return tf;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object eSrc = e.getSource();
		if (eSrc == rbJMF || eSrc == rbMIME || eSrc == rbRawXML)
		{
			if (rbJMF.isSelected())
			{
				settingService.setSetting(SettingKey.SEND_METHOD, "JMF");
			}
			else if (rbMIME.isSelected())
			{
				settingService.setSetting(SettingKey.SEND_METHOD, "MIME");
			}
			else if (rbRawXML.isSelected())
			{
				settingService.setSetting(SettingKey.SEND_METHOD, "RAW");
			}

		}
		else if (eSrc == rbPackageAll)
		{
			settingService.setSetting(SettingKey.SEND_PACKAGE, rbPackageAll.isSelected());
		}
	}

	/**
	 * sendJMFJDFmime sends the actual open JDF as a MIME multipart/related message to the given URL
	 *
	 * @return true if success
	 */
	private boolean sendJDF()
	{
		final URL url = getURL(false, true);
		if (url == null)
		{
			EditorUtils.errorBox("ErrorSendDevice", "URL = null");
		}
		// create a JMF message
		final EditorDocument editorDoc = MainView.getEditorDoc();
		final JDFDoc theDoc = editorDoc.getJDFDoc();
		final JDFJMF theJMF = theDoc.getJMFRoot();
		final XJMFHelper xjmf = XJMFHelper.getHelper(theDoc);
		if (theJMF != null || xjmf != null || isRaw())
		{
			getURL(true, false); // ensure any return url gets saved
			return sendRaw(url);
		}

		if (cbReturn.isSelected())
		{
			return returnJDFFromDevice(url, isMime(), isPackageAll());
		}
		else
		{
			return submitJDFToDevice(url, isMime(), getURL(true, true), isPackageAll());
		}
	}

	static class EditorZipWriter extends XJDFZipWriter
	{

		private final String singlePath;

		public EditorZipWriter(final String singlePath)
		{
			super();
			this.singlePath = singlePath;
		}

		@Override
		protected String getXJDFPath(final int i)
		{
			return singlePath;
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
		JDFDoc jmfDoc = new JDFDoc(ElementName.JMF);
		final JDFJMF jmfRoot = jmfDoc.getJMFRoot();
		final JDFCommand command = jmfRoot.appendCommand(EnumType.SubmitQueueEntry);
		final JDFQueueSubmissionParams qsp = command.appendQueueSubmissionParams();
		if (returnURL != null)
		{
			qsp.setReturnJMF(returnURL);
		}
		int rc = -2;
		final String message = "Snafu";
		final EditorDocument editorDoc = MainView.getEditorDoc();
		if (editorDoc == null)
		{
			return false;
		}
		UrlPart up = null;
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
				final String fileName = UrlUtil.urlToFileName(editorDoc.getOriginalFileName());
				qsp.setURL(theDoc.getOriginalFileName());

				final MimeWriter mw = new MimeWriter(editorDoc.isJson() ? eMimeSubType.formdata : eMimeSubType.related);

				if (editorDoc.isXJDF())
				{
					if (editorDoc.isJson())
					{
						qsp.setURL(fileName);
					}
					else
					{
						qsp.setURL("xjdf/" + fileName);
					}
					final JDFToXJDF conv = EditorUtils.getXJDFConverter();
					final KElement xjmf = conv.convert(jmfRoot);
					jmfDoc = new JDFDoc(xjmf.getOwnerDocument());
					if (editorDoc.isJson())
					{
						final BodyPartHelper bph = new BodyPartHelper();
						final JSONWriter jw = Editor.getEditor().getJSonWriter();
						final JSONObject o = jw.convert(jmfDoc.getRoot());
						bph.setContent(new JSONObjHelper(o).getInputStream(), MimeUtil.VND_XJMF_J);
						bph.setFileName("submit.xjmf");
						mw.addBodyPart(bph);

						final BodyPartHelper bph2 = new BodyPartHelper();
						final JSONObject o2 = jw.convert(theDoc.getRoot());
						bph2.setContent(new JSONObjHelper(o2).getInputStream(), MimeUtil.VND_XJDF_J);
						bph2.setFileName(fileName);
						mw.addBodyPart(bph2);
					}
					else
					{
						final XJDFZipWriter zw = new EditorZipWriter("xjdf/" + fileName);
						zw.setXjmf(XJMFHelper.getHelper(xjmf));
						zw.addXJDF(XJDFHelper.getHelper(theDoc.getRoot()));
						up = UrlUtil.writerToURL(url.toExternalForm(), zw, UrlUtil.POST, UrlUtil.APPLICATION_ZIP, null);
					}

				}
				else
				{
					mw.buildMimePackage(jmfDoc, theDoc, packageAll);
				}
				if (up == null)
				{
					up = mw.writeToURL(url.toExternalForm());
				}
			}
			else
			{
				up = jmfDoc.write2HttpURL(url, null);
			}
			if (up != null)
			{
				rc = up.getResponseCode();
			}
			bSendTrue = rc == 200;
			if (bSendTrue && !UrlUtil.isFile(url.toExternalForm()))
			{
				createResponse(up, "jmf");
			}
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

	private void createResponse(final UrlPart up, final String ext)
	{
		String newFileName = MainView.getEditorDoc().getOriginalFileName();
		newFileName = UrlUtil.newExtension(newFileName, ".resp." + ext);
		final File f = FileUtil.streamToFile(up.getResponseStream(), newFileName);
		if (f != null)
		{
			MainView.getFrame().readFile(f);
		}
		else
		{
			JOptionPane.showMessageDialog(MainView.getFrame(), "No Message Response received");
		}
	}

	/**
	 * @param url
	 * @param bMime
	 * @return true if success
	 */
	private boolean returnJDFFromDevice(final URL url, final boolean bMime, final boolean packageAll)
	{
		boolean bSendTrue;
		final JDFDoc jmfDoc = new JDFDoc("JMF");
		final JDFCommand command = jmfDoc.getJMFRoot().appendCommand(EnumType.ReturnQueueEntry);
		final JDFReturnQueueEntryParams rqp = command.appendReturnQueueEntryParams();
		int rc = -2;
		final String message = "Snafu";
		final EditorDocument editorDoc = MainView.getEditorDoc();
		if (editorDoc == null)
		{
			return false;
		}
		UrlPart up = null;
		try
		{
			final JDFDoc theDoc = editorDoc.getJDFDoc().clone();
			if (bMime)
			{
				rqp.setURL("dummy");
				final Multipart mp = MimeUtil.buildMimePackage(jmfDoc, theDoc, packageAll);
				up = new MimeWriter(mp).writeToURL(url.toExternalForm());
			}
			else
			{
				up = jmfDoc.write2HttpURL(url, null);
			}
			if (up != null)
			{
				rc = up.getResponseCode();
				if (rc == 200 && !UrlUtil.isFile(url.toExternalForm()))
				{
					createResponse(up, "jmf");
				}

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
		if (settingService.getSetting(SettingKey.GENERAL_UPDATE_JOBID, Boolean.class))
		{
			int inc = settingService.getSetting(SettingKey.SEND_JOB_INCREMENT, Integer.class);
			inc = ++inc % 10000;
			settingService.setSetting(SettingKey.SEND_JOB_INCREMENT, inc);
			final String jobID = root.getAttribute(AttributeName.JOBID) + "." + inc;
			final AttributeReplacer ar = new AttributeReplacer("JobID", jobID, null);
			ar.replace(root);
		}
	}

	/**
	 * @param url
	 * @return true if ok
	 */
	private boolean sendRaw(final URL url)
	{
		final EditorDocument editorDoc = MainView.getEditorDoc();
		final JDFDoc theDoc = editorDoc == null ? null : editorDoc.getJDFDoc();
		if (theDoc == null)
		{
			return false;
		}
		final KElement root = theDoc.getRoot();
		updateJobID(root);
		final String extension = EditorUtils.getExtension(root, editorDoc.isJson());
		final UrlPart up;
		if (editorDoc.isJson())
		{
			final JSONWriter jw = Editor.getEditor().getJSonWriter();
			jw.convert(theDoc.getRoot());
			up = UrlUtil.writerToURL(url.toExternalForm(), jw, UrlUtil.POST, UrlUtil.APPLICATION_JSON, null);

		}
		else
		{
			up = theDoc.write2HttpURL(url, null);
		}
		final boolean bSendTrue = up != null && up.getResponseCode() == 200;
		if (bSendTrue && !UrlUtil.isFile(url.toExternalForm()))
		{
			String newFileName = MainView.getEditorDoc().getOriginalFileName();
			newFileName = UrlUtil.newExtension(newFileName, "resp." + extension);
			final File f = FileUtil.streamToFile(up.getResponseStream(), newFileName);
			if (f != null)
			{
				MainView.getFrame().readFile(f);
			}
			else
			{
				JOptionPane.showMessageDialog(MainView.getFrame(), "No Message Response received");
			}
		}

		return bSendTrue;
	}

	/**
	 * getURL
	 * returns the URL of the device given by the user
	 *
	 * @param bReturn if true, the url is the returnurl, else it is the device url
	 * @return URL the url
	 */
	URL getURL(final boolean bReturn, boolean notify)
	{
		// returns the URL given by the user
		URL url = null;
		final JComboBox<String> tf = bReturn ? urlReturn : urlPath;
		final String urlText = (String) tf.getEditor().getItem();

		if (bReturn && KElement.isWildCard(urlText))
		{
			return null;
		}

		try
		{
			url = new URL(urlText);
			settingService.setSetting(bReturn ? SettingKey.SEND_URL_RETURN : SettingKey.SEND_URL_SEND, url.toExternalForm());
		}
		catch (final MalformedURLException e)
		{
			if (notify)
			{
				EditorUtils.errorBox("InvalidURL", ":" + urlText);
			}
			return null;
		}
		return url;
	}

	/**
	 *
	 */
	public void trySend()
	{
		if (MainView.getEditorDoc() == null)
		{
			return;
		}
		// get the URL to send to and call the CommunicationController
		boolean bSendTrue = false;
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };

		final int option = JOptionPane.showOptionDialog(MainView.getFrame(), this, ResourceUtil.getMessage("JDFSendToDevice"), JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			// the send method depends on the settings in the Editor.ini file
			bSendTrue = sendJDF();
		}

		// show success in a popup window
		String sLabel = (bSendTrue) ? ResourceUtil.getMessage("JDFSent") : ResourceUtil.getMessage("JDFNotSent");
		final URL url = getURL(false, true);
		sLabel += "\n\nURL= " + url.toExternalForm();
		if (bSendTrue)
		{
			JOptionPane.showMessageDialog(MainView.getFrame(), sLabel, "", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(MainView.getFrame(), sLabel, "SNAFU", JOptionPane.ERROR_MESSAGE);
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
		final boolean simpleMime = rbMIME == null ? false : rbMIME.isSelected();
		return simpleMime || isPackageAll();
	}

}
