/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2006 The International Cooperation for the Integration of 
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.jmf.JDFCommand;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFQueueSubmissionParams;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;



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
public class SendToDevice extends JPanel 
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4676135228882149268L;  
    
//    private File file;
    private JTextField urlPath;
    private JRadioButton rbJMF;
    private JRadioButton rbMIME;
//  private JButton browse;
    
    
    public SendToDevice()
    {
        super();
        setLayout(new GridLayout(3, 1, 0, 5));
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
        
        final ResourceBundle littleBundle=Editor.getBundle();
        final JLabel urlText = new JLabel(littleBundle.getString("setURL"));
        urlText.setVerticalAlignment(SwingConstants.BOTTOM);
        add(urlText);
        
        final JLabel urlLabel = new JLabel(littleBundle.getString("pathToURL"));
        
        final INIReader iniFile=Editor.getIniFile();
        urlPath = new JTextField(50);
        urlPath.setText(iniFile.getURLSendToDevice());
        
        final Box URLBox = Box.createHorizontalBox();
        
        URLBox.add(urlLabel);
        URLBox.add(urlPath);
        URLBox.add(Box.createHorizontalStrut(5));
        
        //RadioButtons to choose sending JDF with QueueSubmissionParams
        //and URL or as a multipart/related MIME message
        rbJMF = new JRadioButton( littleBundle.getString("sendMethodJMF") );
        rbMIME = new JRadioButton( littleBundle.getString("sendMethodMIME") );
        if(iniFile.getMethodSendToDevice().equals("MIME")){
            rbMIME.setSelected( true );
        }
        else
        {
            rbJMF.setSelected(true);
        }
        final ButtonGroup sendMethodGroup = new ButtonGroup();
        sendMethodGroup.add( rbJMF ); 
        sendMethodGroup.add( rbMIME );
        
        final JLabel rbLabel = new JLabel(littleBundle.getString("sendMethod"));
        final Box SendMethodBox = Box.createHorizontalBox();
        
        SendMethodBox.add(rbLabel);
        SendMethodBox.add(rbJMF);
        SendMethodBox.add(rbMIME);
        add(SendMethodBox);
        //add boxes to the window
        add(URLBox);
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) 
    { 
        e.getClass(); // nop to fool compiler
    }
    
    /**
     * sendJMFJDFmime
     * sends the actual open JDF as a MIME multipart/related message 
     * to the given URL
     * 
     * @param url
     * @return
     */
    public boolean sendJMFJDFmime(URL url){
        
        boolean bSendTrue = false;
        if (url != null){
            try{
                // create a JMF message
                final JDFDoc jmfDoc=new JDFDoc("JMF");
                final JDFJMF jaj = jmfDoc.getJMFRoot();
                
                
                // create a command message
                JDFCommand command=(JDFCommand)jaj.appendMessageElement(EnumFamily.Command,JDFMessage.EnumType.SubmitQueueEntry);
                JDFQueueSubmissionParams eQsp = command.appendQueueSubmissionParams();
                
                //create an unique CID
                final String sJDFCID = "MIME"+JDFElement.uniqueID(0)+"@cip4.org";
                eQsp.setURL("cid+"+sJDFCID);
                
                 //             Create empty properties
                final Properties props = new Properties();
                
                //             Get session
                final Session session = Session.getDefaultInstance(props, null);
                final Message mMessage = new MimeMessage(session);
                
                //body part for the JMF
                final MimeBodyPart mMessageBodyPartJMF = new MimeBodyPart();
                
                //write JMF to body part
                // the MIME type will be replaced by the send method
                mMessageBodyPartJMF.setContent(jmfDoc.toXML(), "text/xml; charset=UTF-8");                
                mMessageBodyPartJMF.setHeader("Content-Transfer-Encoding","binary");

                //             Create a related multi-part to combine the parts
                final MimeMultipart mpMultipart = new MimeMultipart("related");
                mpMultipart.addBodyPart(mMessageBodyPartJMF);
                
                
                //            create part for the JDF
                EditorDocument eDoc=Editor.getEditorDoc();               
                final JDFDoc jdfDoc = eDoc.getJDFDoc();
                final File fil=new File(eDoc.getOriginalFileName());

                final MimeBodyPart mMessageBodyPartJDF = new MimeBodyPart();
                mMessageBodyPartJDF.setHeader("Content-Transfer-Encoding","binary");                
                mMessageBodyPartJDF.setContentID("<"+sJDFCID+">");                
                mMessageBodyPartJDF.setFileName(fil.getName());
                mMessageBodyPartJDF.setContent(jdfDoc.toXML(), "text/xml; charset=UTF-8");
                mMessageBodyPartJDF.setHeader("Content-Transfer-Encoding","binary");
                
                //            add JDF part to multi-part
                mpMultipart.addBodyPart(mMessageBodyPartJDF);
                //            associate multi-part with message
                mMessage.setContent(mpMultipart);
                bSendTrue =  send(url, mMessage);
            }
            catch (Exception e){
                EditorUtils.errorBox("ErrorSendDevice",null);
                return false;
            }
        }
        return bSendTrue;
    }
    /**
     * sendJMFJDF
     * sends the actual open JDF as a JMF command 
     * to the given URL
     * 
     * @param url
     * @return
     */
    
    public boolean sendJMFJDF(URL url)
    {
        boolean bSendTrue = false;        
        if (url != null){
            try{
                final JDFDoc jmfDoc=new JDFDoc("JMF");
                final JDFJMF jaj = jmfDoc.getJMFRoot();
                
                
                // create a command message
                JDFCommand command=(JDFCommand)jaj.appendMessageElement(EnumFamily.Command,JDFMessage.EnumType.SubmitQueueEntry);
                JDFQueueSubmissionParams eQsp = command.appendQueueSubmissionParams();
                eQsp.setURL(Editor.getEditorDoc().getOriginalFileName());
                bSendTrue =  send(url, jaj.toXML());
            }
            catch (Exception e)
            {
                EditorUtils.errorBox("ErrorSendDevice",null);
                return false;
            }
        }
        return bSendTrue;
    }
    
    /**
     * send
     * 
     * set the MIME type application/vnd.cip4-jdf+xml 
     * 
     * @param URL url (URL of the device)
     * @param Message mMessage (message to be sent)
     * @return
     */
    static boolean send(URL url, Message mMessage)
    {
        try    
        {
            final OutputStream sOut = new ByteArrayOutputStream();
            mMessage.writeTo(sOut);
            String sMessage = sOut.toString();
            
            // replace the MIME type by the CIP4 MIME type
            sMessage = sMessage.replaceAll(JDFConstants.MIME_TEXTXML,JDFConstants.MIME_JMF);
            // send changed message
            send(url, sMessage);
        }
        catch (Exception e){
            EditorUtils.errorBox("ErrorSendDevice",null);
            return false;
        }
        return true;
    }
    
    /**
     * send
     * 
     * performes the sending process
     * set the MIME type application/vnd.cip4-jdf+xml 
     * 
     * @param URL url (URL of the device)
     * @param Message mMessage (message to be sent)
     * @return
     */
    static boolean send(URL url, String sMessage)
    {
        try    {
            HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
            httpURLconnection.setRequestMethod("POST");
            //httpURLconnection.setRequestProperty("Connection","close");
//            httpURLconnection.setRequestProperty("Content-type", "application/vnd.cip4-jmf+xml");
//            httpURLconnection.setRequestProperty("User-Agent", "Queue (JMF protocol)");
//            httpURLconnection.setRequestProperty("Accept", "application/vnd.cip4-jmf+xml");
//            httpURLconnection.setUseCaches(false);
            httpURLconnection.setDoOutput(true);
            httpURLconnection.setDoInput(true);
            
            final OutputStream out    = httpURLconnection.getOutputStream();
                        
            out.write(sMessage.getBytes());
//            String s=new String(sMessage.getBytes());
//TODO fix sending            
            out.flush();
            out.close();
            
            httpURLconnection.disconnect();
        }
        catch (Exception e){
            EditorUtils.errorBox("ErrorSendDevice",null);
            return false;
        }
        return true;
    }
    
    
    /**
     * getURL
     * 
     * returns the URL of the device given by the user
     * 
     * @return URL url
     */
    
    public URL getURL() {
        // returns the URL given by the user
        URL url = null;
        final String urlText = urlPath.getText();
        try {
            url = new URL(urlText);
            INIReader ini=Editor.getIniFile();
            ini.setURLSendToDevice(urlText);
        } 
        catch (MalformedURLException e) 
        {
            //e.printStackTrace();
            EditorUtils.errorBox("InvalidURL",":"+urlText);
            return null;
        }
        return url;
    }    
    
    /**
     * getActiveRadioButton()
     * 
     * returns the radio button set for the send method
     * 
     * @return String activeRadioButton, one of MIME or JMF
     */
    public String getActiveRadioButton() {
        // returns the URL given by the user
        INIReader  ini=Editor.getIniFile();
        String activeRadioButton = new String();
        ResourceBundle littleBundle=Editor.getBundle();
        if(rbJMF.isSelected())
        {
            activeRadioButton = littleBundle.getString("sendMethodJMF");
            ini.setMethodSendToDevice("JMF");
        }
        else
        {
            activeRadioButton = littleBundle.getString("sendMethodMIME");
            ini.setMethodSendToDevice("MIME");
        }
        return activeRadioButton;
    }        
    
}
