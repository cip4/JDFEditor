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
package org.cip4.tools.jdfeditor.view;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.jdflib.core.*;
import org.cip4.tools.jdfeditor.*;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The MainView of the CIP4 JDFEditor.
 */
@Component
public class MainView
{
    private static final Logger LOGGER = LogManager.getLogger(MainView.class);

    private MainController mainController;


	public static JDFFrame my_Frame;

	public static final String ICONS_PATH = "/org/cip4/tools/jdfeditor/icons/";

    /**
     * Default constructor.
     */
    public MainView() {
        my_Frame = new JDFFrame(this);
    }

    /**
     * Register a MainController for this view (MVC Pattern)
     * @param mainController The MainController for this view.
     */
    public void registerController(final MainController mainController) {

        this.mainController = mainController;


        my_Frame.registerController(mainController);
    }

    /**
     * Show a Message Dialog in MainView.
     * @param message The message of the dialog.
     * @param title The title of the dialog.
     */
    public void showMessageDialog(String message, String title) {
        JOptionPane.showMessageDialog(my_Frame, message, title, JOptionPane.INFORMATION_MESSAGE);

        // final ImageIcon imgCIP = MainView.getImageIcon(MainView.ICONS_PATH + "CIP4.gif");
        // JOptionPane.showMessageDialog(this, "see http://cip4.org/jdfeditor", "CIP4 JDF Editor", JOptionPane.INFORMATION_MESSAGE, imgCIP);

    }

    /**
     * Returns a resource as ImageIcon object.
     * @param res Resource String of Icon.
     * @return The ImageIcon object.
     */
	public static ImageIcon getImageIcon(String res)
	{
        // load icon
        ImageIcon imageIcon = null;
        InputStream is = MainView.class.getResourceAsStream(res);

        try {
            byte[] bytes = IOUtils.toByteArray(is);
            imageIcon = new ImageIcon(bytes);

        } catch (IOException e) {
            LOGGER.error("Error during loading ImageIcon", e);
        }

        // return icon
		return imageIcon;
	}

	/**
	 * Sets the cursor to wait or ready
	 * @param iWait 0 = ready; 1 = wait; 2 = hand
	 * @param parentComponent the parent frame to set the cursor in, if null use the main frame
	 */
	public static void setCursor(final int iWait, java.awt.Component parentComponent)
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

	/**
	 * Method instantiate the editor window
	 * @param file the file to open initially
	 */
	public void display(final File file)
	{
		try	{
            String currentLookAndFeel = mainController.getSetting(SettingKey.GENERAL_LOOK, String.class);
			UIManager.setLookAndFeel(currentLookAndFeel);
		} catch (final Exception e)	{
            LOGGER.error("Error during setting 'Look and Feel' of JDFEditor.", e);
		}

		setCursor(0, null);

		// read the initialization stuff
		JDFAudit.setStaticAgentName(App.APP_NAME);
		JDFAudit.setStaticAgentVersion(getEditorVersion());
		KElement.setLongID(mainController.getSetting(SettingKey.GENERAL_LONG_ID, Boolean.class));
		JDFElement.setDefaultJDFVersion(JDFElement.EnumVersion.getEnum(mainController.getSetting(SettingKey.VALIDATION_VERSION, String.class)));
		JDFParser.m_searchStream = true;

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
        my_Frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainView.class.getResource("/org/cip4/tools/jdfeditor/jdfeditor_128.png")));

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

	/**
	 * Method getFrame.
	 * @return JDFFrame
	 */
	public static JDFFrame getFrame()
	{
		return my_Frame;
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
	 * Returns the Version of the JDFLibJ.
	 * @return The version number of JDFLibJ.
	 */
	public String getEditorVersion()
	{
		return "Build version " + JDFAudit.software();
	}

	/**
	 * Get the model associated with the currently displayed document.
	 * @return The data model
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
}
