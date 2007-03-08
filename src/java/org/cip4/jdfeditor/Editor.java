package org.cip4.jdfeditor;
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFElement;

/**
 * @author AnderssA ThunellE
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Editor
{
    private static Editor my_Editor;
    protected static JDFFrame my_Frame;
    private static INIReader m_iniFile;

    public static final String ICONS_PATH = "/org/cip4/jdfeditor/icons/";

    public static ImageIcon getImageIcon(Class myClass, String resString)
    {
        URL url=myClass.getResource(resString);
        ImageIcon imIc=null;
        if(url!=null)
        {
            imIc=new ImageIcon(url);
        }
        else
        {
            imIc=new ImageIcon("."+resString);            
        }
        return imIc;            
    }

    //////////////////////////////////////////////////////////////////

    public static void main(String[] args)
    {
        File file=null;
        // mac may have 2nd argument
        for(int i=args.length - 1; i>=0; i--) 
        {
            if(!args[i].startsWith("-"))
            {
                File f=new File(args[i]);
                if(f.canRead())
                {
                    file=f;
                    break;
                }           
            }
        }
        my_Editor=new Editor();
        my_Editor.init(file);
    }

    //////////////////////////////////////////////////////////////////
    /**
     * set the cursor to wait or ready
     * @param iWait
     * 0=ready
     * 1=wait
     * 2=hand
     * @param parentComponent the parent frame to set the cursor in, if null use the main frame
     */
    static void setCursor(int iWait, Component parentComponent)
    {
        if(parentComponent==null)
            parentComponent=my_Frame;
        
        if(iWait==0)
        {
            parentComponent.setCursor(Cursor.getDefaultCursor());
        }
        else if(iWait==1)
        {
            parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));            
        }
        else if(iWait==2)
        {
            final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR); 
            parentComponent.setCursor(handCursor);            
        }
    }

    public Editor()
    {
        // nothing to do here (yet)
    }

    /**
     * Method init.
     * instanciate the editor window
     */
    public void init(File file)
    {
        // read the initialization stuff
        m_iniFile = new INIReader();       

        my_Frame = new JDFFrame();
        setCursor(0, null);
        JDFAudit.setStaticAgentName(getEditorName());
        JDFAudit.setStaticAgentVersion(getEditorVersion());
        JDFElement.setLongID(m_iniFile.getLongID());
        JDFElement.setFixVersionIDFix(true);
        
        try
        {
            my_Frame.drawWindow();
            my_Frame.setBackground(Color.white);
            my_Frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            final WindowListener winLis = new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    if(my_Frame.closeFile(999) != JOptionPane.CANCEL_OPTION)
                    {
                        getIniFile().writeINIFile();
                        System.exit(0);
                        e.getID(); // make compile happy
                    }
                }
            };
            my_Frame.addWindowListener(winLis);

            //this is only for the PC version
            if(file != null )
            {
                boolean b=my_Frame.readFile(file);
                if(b)
                {
                    my_Frame.m_menuBar.updateRecentFilesMenu(file.toString());
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method getEditor.
     * @return Editor
     */
    public static Editor getEditor()
    {
        return my_Editor;
    }
    /**
     * Method getFrame.
     * @return JDFFrame
     */
    public static JDFFrame getFrame()
    {
        return my_Frame;
    }

    /**
     * Method getBundle.
     * @return ResourceBundle the static resource bundle
     */
    public static ResourceBundle getBundle()
    {
        return my_Frame.m_littleBundle;
    }
    /**
     * get the JDFDoc of the currently displayed JDF
     * @return the JDFDoc that is currently being displayed
     */
    public static EditorDocument getEditorDoc()
    {
        return my_Frame.getEditorDoc();
    }

    /**
     * Method getFrame.
     * @return JDFFrame
     */
    public static INIReader getIniFile()
    {
        return m_iniFile;
    }

    /**
     * 
     * @return
     */
    public static String getEditorName()
    {
        return "CIP4 JDF Editor -- Copyright © 2001-2007 CIP4\n";
    }

    /**
     * 
     * @return the editor build date
     */
    public static String getEditorBuildDate()
    {
        return "Build Date - 16 March 2007";
    }

    /**
     * 
     * @return the editor version
     */
    public static String getEditorVersion()
    {
        return "Build version " + JDFAudit.software();
    }

    /**
     * get the model associated with the currently displayed document
     * @return
     */
    public static JDFTreeModel getModel()
    {
        EditorDocument ed =getEditorDoc();
        return ed==null ? null : ed.getModel();
    }
    ///////////////////////////////////////////////////////////////

}
