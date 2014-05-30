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
package org.cip4.tools.jdfeditor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.jdflib.core.*;
import org.cip4.jdflib.jmf.JMFBuilder;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 */
public class Editor
{
    private static final Logger LOGGER = LogManager.getLogger(Editor.class);

    private SettingService settingService = new SettingService();

	private static Editor my_Editor;

	protected static JDFFrame my_Frame;

	static final String ICONS_PATH = "/org/cip4/tools/jdfeditor/icons/";

	static ImageIcon getImageIcon(final Class<?> myClass, final String resString)
	{
		final URL url = myClass.getResource(resString);
		ImageIcon imIc = null;
		if (url != null)
		{
			imIc = new ImageIcon(url);
		}
		if (imIc == null || imIc.getIconHeight() <= 0)
		{
			imIc = new ImageIcon("." + resString);
		}
		return imIc;
	}

	// ////////////////////////////////////////////////////////////////
	/**
	 * set the cursor to wait or ready
	 * @param iWait 0=ready 1=wait 2=hand
	 * @param parentComponent the parent frame to set the cursor in, if null use the main frame
	 */
	static void setCursor(final int iWait, Component parentComponent)
	{
		if (parentComponent == null)
		{
			parentComponent = my_Frame;
		}

		if (iWait == 0)
		{
			parentComponent.setCursor(Cursor.getDefaultCursor());
		}
		else if (iWait == 1)
		{
			parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else if (iWait == 2)
		{
			final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			parentComponent.setCursor(handCursor);
		}
	}

	private ResourceBundle m_littleBundle;

	/**
	 * Default constructor.
	 */
	public Editor()
	{
        my_Editor = this;
	}

	/**
	 * Method instantiate the editor window
	 * @param file the file to open initially
	 */
	public void init(final File file)
	{
		final String language = settingService.getString(SettingKey.GENERAL_LANGUAGE);
		final Locale currentLocale = new Locale(language, language.toUpperCase());

		Locale.setDefault(currentLocale);
		m_littleBundle = ResourceBundle.getBundle("org.cip4.tools.jdfeditor.messages.JDFEditor", currentLocale);
		final String currentLookAndFeel = settingService.getString(SettingKey.GENERAL_LOOK);

		try
		{
			UIManager.setLookAndFeel(currentLookAndFeel);
		}
		catch (final Exception e)
		{
			//
		}

		my_Frame = new JDFFrame();
		setCursor(0, null);

		// read the initialization stuff
		JDFAudit.setStaticAgentName(getEditorName());
		JDFAudit.setStaticAgentVersion(getEditorVersion());
		KElement.setLongID(settingService.getBoolean(SettingKey.GENERAL_LONG_ID));
		JDFElement.setDefaultJDFVersion(JDFElement.EnumVersion.getEnum(settingService.getString(SettingKey.VALIDATION_VERSION)));
		JDFParser.m_searchStream = true;

		try
		{
			my_Frame.drawWindow();
			my_Frame.setBackground(Color.white);
			my_Frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			final WindowListener winLis = new WindowAdapter()
			{
				@Override
				public void windowClosing(final WindowEvent e)
				{
					if (my_Frame.closeFile(999) != JOptionPane.CANCEL_OPTION)
					{
						System.exit(0);
						e.getID(); // make compile happy
					}
				}
			};
			my_Frame.addWindowListener(winLis);
			my_Frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Editor.class.getResource("/org/cip4/tools/jdfeditor/jdfeditor_128.png")));

			// this is only for the PC version
			if (file != null)
			{
				final boolean b = my_Frame.readFile(file);
				if (b)
				{
					my_Frame.m_menuBar.updateRecentFilesMenu(file.toString());
				}
			}
		}
		catch (final Exception e)
		{
			LOGGER.error("Error initializing Editor", e);
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
	 * Method getString
	 * @param key
	 * @return ResourceBundle the static resource bundle
	 */
	public static String getString(String key)
	{
		return my_Editor.m_littleBundle.getString(key);
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
	 * @return the about text
	 */
	public String getAboutText()
	{
		final String about = getEditorName() + "\n" + getEditorVersion() + "\nInternational Cooperation for Integration of Processes in Prepress, Press and Postpress,\n"
				+ "hereinafter referred to as CIP4. All Rights Reserved\n\n"
				+ "Authors: Anna Andersson, Evelina Thunell, Ingemar Svenonius, Elena Skobchenko, Rainer Prosi, Alex Khilov, Stefan Meissner\n\n"
				+ "The APPLICATION is provided 'as is', without warranty of any kind, express, implied, or\n"
				+ "otherwise, including but not limited to the warranties of merchantability,fitness for a\n"
				+ "particular purpose and noninfringement. In no event will CIP4 be liable, for any claim,\n"
				+ "damages or other liability whether in an action of contract, tort or otherwise, arising\n"
				+ "from, out of, or in connection with the APPLICATION or the use or other dealings in the\n" + "APPLICATION.";
		return about;
	}

	/**
	 * 
	 * @return the name of the editor
	 */
	public String getEditorName()
	{
		return "CIP4 JDF Editor -- Copyright (c) 2001-2014 CIP4";
	}

	/**
	 * 
	 * @return the editor build date
	 */
	public String getEditorBuildDate()
	{
		return "Estimated Build Date After May 14 2014";
	}

	/**
	 * 
	 * @return the editor version
	 */
	public String getEditorVersion()
	{
		return "Build version " + JDFAudit.software();
	}

	JMFBuilder getJMFBuilder()
	{
		JMFBuilder b = new JMFBuilder();
		b.setSenderID("JDFEditor");

		return b;
	}

	/**
	 * get the model associated with the currently displayed document
	 * @return the data model
	 */
	public static JDFTreeModel getModel()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getModel();
	}

	/**
	 * @param m_model
	 */
	public static void getsetModel(final JDFTreeModel m_model)
	{
		final EditorDocument ed = getEditorDoc();
		ed.setModel(m_model);
	}

	// /////////////////////////////////////////////////////////////

	/**
	 * get the JDFDoc of the currently displayed JDF
	 * @return the JDFDoc that is currently being displayed BMI Created 07-08-31
	 */
	public static JDFDoc getJDFDoc()
	{
		return my_Frame.getJDFDoc();
	}

	/**
	 * Method getTreeArea.
	 * @return ResourceBundle the static resource bundle BMI Created 07-08-31
	 */
	public static JDFTreeArea getTreeArea()
	{
		return my_Frame.m_treeArea;
	}

	// /////////////////////////////////////////////////////////////
}
