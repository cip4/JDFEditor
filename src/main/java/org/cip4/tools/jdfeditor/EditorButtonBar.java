package org.cip4.tools.jdfeditor;

/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2013 The International Cooperation for the Integration of 
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * 
 *  
 * @author rainer prosi
 * @date Apr 11, 2013
 */
public class EditorButtonBar extends JToolBar implements ActionListener
{

	JButton m_newButton;
	JButton m_openButton;
	JButton m_saveButton;
	JButton m_undoButton;
	JButton m_redoButton;
	JButton m_cutButton;
	JButton m_copyButton;
	JButton m_pasteButton;
	JButton m_validateButton;
	JButton m_upOneLevelButton;
	JButton m_NextButton;
	JButton m_LastButton;
	JButton m_printButton;
	JButton m_zoomInButton;
	JButton m_zoomOutButton;
	JButton m_zoomOrigButton;
	JButton m_zoomBestButton;
	JButton m_refreshButton;
	JButton m_closeButton;
	HashSet<JButton> m_allButtons;

	private final JDFFrame m_frame;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2161156231007579898L;

	/**
	 * 
	 *  
	 * @param frame
	 */
	public EditorButtonBar(JDFFrame frame)
	{
		super(SwingConstants.HORIZONTAL);
		m_frame = frame;
		m_allButtons = new HashSet<JButton>();
	}

	/**
	 * Method drawButtonBar.
	 * Editor.java contains ICONS_PATH. This package can be found under JDFEditor/src/Java/org.cip4.jdfeditor.icons
	 * You can add icons to this packagae by putting your .gif files into the following folder. This folder comes from whereever you
	 * downloaded yoru code from SubVersion. My place (BIskey) is the reference point. You can make it whatever you like. 
	 *  C:\Subversion_Root\My_Root\JDFEditor\src\java\org\cip4\jdfeditor\icons
	 *  After adding the icons to this folder, you need to come here and refresh the package w/in your Java editing software.
	 */
	public void drawButtonBar()
	{

		final ImageIcon imgNew = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "NewButton.gif");
		final ImageIcon imgOpen = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "OpenButton.gif");
		final ImageIcon imgSave = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "SaveButton.gif");
		final ImageIcon imgCut = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "CutButton.gif");
		final ImageIcon imgCopy = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "CopyButton.gif");
		final ImageIcon imgPaste = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "PasteButton.gif");
		final ImageIcon imgUndo = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "UndoButton.gif");
		final ImageIcon imgRedo = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "RedoButton.gif");
		final ImageIcon imgReval = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "RevalidateButton.gif");
		final ImageIcon imgUp = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "UpButton.gif");
		final ImageIcon imgLast = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "LastButton.gif");
		final ImageIcon imgNext = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "NextButton.gif");
		final ImageIcon imgPrint = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "PrintButton.gif");
		final ImageIcon imgZoomIn = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ZoomInButton.gif");
		final ImageIcon imgZoomOut = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ZoomOutButton.gif");
		final ImageIcon imgZoomOrig = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ZoomOrigButton.gif");
		final ImageIcon imgZoomBest = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ZoomBestButton.gif");
		final ImageIcon imgRefresh = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "RefreshButton.gif");

		final Dimension d = new Dimension(10, 30);
		setFloatable(false);

		m_newButton = createDefaultButton(imgNew, Editor.getString("NewKey"), true, '|');
		m_openButton = createDefaultButton(imgOpen, Editor.getString("OpenKey"), true, '|');
		m_saveButton = createDefaultButton(imgSave, Editor.getString("SaveKey"), false, '|');

		addSeparator(d);

		m_printButton = createDefaultButton(imgPrint, Editor.getString("PrintKey"), false, '|');
		m_refreshButton = createDefaultButton(imgRefresh, Editor.getString("RefreshKey"), false, '|');

		addSeparator(d);

		m_cutButton = createDefaultButton(imgCut, Editor.getString("CutKey"), false, '|');
		m_copyButton = createDefaultButton(imgCopy, Editor.getString("CopyKey"), false, '|');
		m_pasteButton = createDefaultButton(imgPaste, Editor.getString("PasteKey"), false, '|');

		addSeparator(d);

		m_undoButton = createDefaultButton(imgUndo, Editor.getString("UndoKey"), false, '|');
		m_undoButton.addActionListener(m_frame.undoAction);
		m_redoButton = createDefaultButton(imgRedo, Editor.getString("RedoKey"), false, '|');
		m_redoButton.addActionListener(m_frame.redoAction);

		addSeparator(d);

		m_validateButton = createDefaultButton(imgReval, Editor.getString("ValidateToolTipKey"), false, 'A');
		m_upOneLevelButton = createDefaultButton(imgUp, Editor.getString("GoUpInProcessViewKey"), false, '|');
		m_LastButton = createDefaultButton(imgLast, Editor.getString("LastButtonKey"), false, '|');
		m_NextButton = createDefaultButton(imgNext, Editor.getString("NextButtonKey"), false, '|');

		addSeparator(d);

		m_zoomInButton = createDefaultButton(imgZoomIn, Editor.getString("ZoomInKey"), false, '|');
		m_zoomOutButton = createDefaultButton(imgZoomOut, Editor.getString("ZoomOutKey"), false, '|');
		m_zoomOrigButton = createDefaultButton(imgZoomOrig, Editor.getString("ZoomOrigKey"), false, '|');
		m_zoomBestButton = createDefaultButton(imgZoomBest, Editor.getString("ZoomFitKey"), false, '|');

		addSeparator(d);
		final ImageIcon imgClose = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "CloseFile.GIF");
		m_closeButton = createDefaultButton(imgClose, Editor.getString("CloseKey"), true, '|');
		addSeparator(d);

		add(Box.createHorizontalGlue());
		SwingUtilities.updateComponentTreeUI(this);
	}

	/**
	 * Creates a JButton with the default settings.
	 * @param iconStr - The Path to the image file
	 * @param tip     - The tool tip
	 * @param enabled - If the button is enabled or disabled initially
	 * @return The default JButton.
	 */
	private JButton createDefaultButton(ImageIcon icon, String tip, boolean enabled, char mnemonic)
	{
		final JButton button = new JButton(icon);

		if (Editor.getIniFile().getLookAndFeel().equalsIgnoreCase("com.sun.java.swing.plaf.motif.MotifLookAndFeel"))
			button.setPreferredSize(new Dimension(45, 45));
		else
			button.setPreferredSize(new Dimension(30, 30));

		button.addActionListener(this);
		button.addMouseListener(new Button_MouseListener());
		button.setBorderPainted(false);
		button.setEnabled(enabled);
		button.setToolTipText(tip);
		if (mnemonic != '|')
		{
			button.setMnemonic(mnemonic);
		}
		m_allButtons.add(button);
		add(button);
		return button;
	}

	/**
	 * enable or disable zoom buttons depending on the current zoom factor
	 * @param zoom the current zoom factor
	 */
	public void setEnableZoom(double zoom)
	{
		if (Editor.getEditorDoc() == null)
		{
			m_zoomOutButton.setEnabled(false);
			m_zoomInButton.setEnabled(false);
			m_zoomOrigButton.setEnabled(false);
			m_zoomBestButton.setEnabled(false);
			m_upOneLevelButton.setEnabled(false);

		}
		else
		{
			m_zoomOutButton.setEnabled(zoom > 0.2);
			m_zoomInButton.setEnabled(zoom < 2.5);
			m_zoomOrigButton.setEnabled(zoom != 1.0);
			m_zoomBestButton.setEnabled(true);
		}
	}

	/**
	 * 
	 */
	public void setEnableClose()
	{
		m_saveButton.setEnabled(false);
		m_cutButton.setEnabled(false);
		m_copyButton.setEnabled(false);
		m_pasteButton.setEnabled(false);
		m_undoButton.setEnabled(false);
		m_redoButton.setEnabled(false);
		m_upOneLevelButton.setEnabled(false);
		m_LastButton.setEnabled(false);
		m_NextButton.setEnabled(false);
		m_zoomInButton.setEnabled(false);
		m_zoomOutButton.setEnabled(false);
		m_zoomOrigButton.setEnabled(false);
		m_zoomBestButton.setEnabled(false);
	}

	public void setEnableOpen(boolean mode)
	{
		m_newButton.setEnabled(mode);
		m_closeButton.setEnabled(mode);
		m_saveButton.setEnabled(mode);
		m_cutButton.setEnabled(mode);
		m_copyButton.setEnabled(mode);

		m_redoButton.setEnabled(false);
		m_undoButton.setEnabled(false);
		m_LastButton.setEnabled(true);
		m_NextButton.setEnabled(true);

		m_validateButton.setEnabled(true);
		m_printButton.setEnabled(true);
		EditorDocument eDoc = Editor.getEditorDoc();
		m_refreshButton.setEnabled(eDoc == null ? true : eDoc.getMimePackage() == null);
	}

	///////////////////////////////////////////////////////////////
	/**
	 * Mother of all action dispatchers
	 * @param e the event that gets checked
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{

		final Object eSrc = e.getSource();
		Editor.setCursor(1, null);
		if (eSrc == m_newButton)
		{
			m_frame.newFile();
		}
		else if (eSrc == m_openButton)
		{
			m_frame.openFile();
		}
		else if (eSrc == m_saveButton)
		{
			if (m_frame.getTitle().equalsIgnoreCase("Untitled.jdf") || m_frame.getTitle().equalsIgnoreCase("Untitled.jmf"))
			{
				m_frame.saveAs();
			}
			else
			{
				m_frame.getJDFDoc().write2File((String) null, 2, false);
				m_frame.getJDFDoc().clearDirtyIDs();
			}
		}
		else if (eSrc == m_validateButton && m_frame.getModel() != null)
		{
			m_frame.getModel().validate();
		}
		else if (eSrc == m_upOneLevelButton)
		{
			m_frame.m_topTabs.m_pArea.goUpOneLevelInProcessView();
		}

		else if (eSrc == m_NextButton)
		{
			Editor.getEditorDoc().setNextSelection();
		}
		else if (eSrc == m_LastButton)
		{
			Editor.getEditorDoc().setLastSelection();
		}
		else if (eSrc == m_printButton)
		{
			m_frame.printWhat();
		}
		else if (eSrc == m_zoomInButton)
		{
			m_frame.m_topTabs.m_pArea.zoom('+');
		}
		else if (eSrc == m_zoomOutButton)
		{
			m_frame.m_topTabs.m_pArea.zoom('-');
		}
		else if (eSrc == m_zoomOrigButton)
		{
			m_frame.m_topTabs.m_pArea.zoom('o');
		}
		else if (eSrc == m_zoomBestButton)
		{
			m_frame.m_topTabs.m_pArea.zoom('b');
		}
		else if (eSrc == m_closeButton)
		{
			m_frame.closeFile(1);
		}
		else if (!Editor.getIniFile().getReadOnly())
		{
			if (eSrc == m_cutButton)
			{
				m_frame.cutSelectedNode();
			}
			else if (eSrc == m_copyButton)
			{
				m_frame.copySelectedNode();
			}
			else if (eSrc == m_pasteButton)
			{
				m_frame.pasteCopiedNode();
			}
			else if (eSrc == m_refreshButton)
			{
				m_frame.refresh();
			}
		}
		// always clean up!
		Editor.setCursor(0, null);
	}

	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////

	class Button_MouseListener extends MouseAdapter
	{
		@Override
		public void mouseEntered(MouseEvent e)
		{
			final Object source = e.getSource();
			if (m_allButtons.contains(source))
			{
				JButton b = (JButton) source;
				if (b.isEnabled())
					b.setBorderPainted(true);
			}
		}

		///////////////////////////////////////////////////////////////
		@Override
		public void mouseExited(MouseEvent e)
		{
			final Object source = e.getSource();
			if (m_allButtons.contains(source))
			{
				JButton b = (JButton) source;
				if (b.isEnabled())
					b.setBorderPainted(false);
			}
		}
	}
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////

}
