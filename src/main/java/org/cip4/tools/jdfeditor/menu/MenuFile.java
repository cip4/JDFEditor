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
package org.cip4.tools.jdfeditor.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.EditorUtils;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.RecentFileUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class MenuFile implements ActionListener, MenuInterface
{
	private MainController mainController;
	final JDFFrame frame = MainView.getFrame();
	
	private JMenu menu;
	private JMenu m_recentFilesMenu;
	
	private JMenuItem m_newItem;
	private JMenuItem m_openItem;
	private JMenuItem m_closeItem;
	private JMenuItem m_closeAllItem;
	private JMenuItem m_saveItem;
	private JMenuItem m_saveAsItem;
	private JMenuItem[] m_subMenuItem = new JMenuItem[5];
	private JMenuItem m_devcapOpenMenu;
	private JMenuItem m_csvItem;
	public JMenuItem m_quitItem;

	public MenuFile(final MainController mainController)
	{
		this.mainController = mainController;
	}

	public JMenu createMenu()
	{
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		menu = new JMenu(ResourceUtil.getMessage("main.menu.file"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);

		m_newItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.new"));
		m_newItem.addActionListener(this);
		m_newItem.setAccelerator(KeyStroke.getKeyStroke('N', menuKeyMask));
		menu.add(m_newItem);

		m_openItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.open"));
		m_openItem.addActionListener(this);
		m_openItem.setAccelerator(KeyStroke.getKeyStroke('O', menuKeyMask));
		menu.add(m_openItem);

		m_closeItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.close"));
		m_closeItem.addActionListener(this);
		m_closeItem.setAccelerator(KeyStroke.getKeyStroke('W', menuKeyMask));
		menu.add(m_closeItem);

		m_closeAllItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.close.all"));
		m_closeAllItem.addActionListener(this);
		menu.add(m_closeAllItem);

		menu.add(new JSeparator());

		m_saveItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.save"));
		m_saveItem.addActionListener(this);
		m_saveItem.setAccelerator(KeyStroke.getKeyStroke('S', menuKeyMask));
		menu.add(m_saveItem);

		m_saveAsItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.save.as"));
		m_saveAsItem.addActionListener(this);
		m_saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		menu.add(m_saveAsItem);

		menu.add(new JSeparator());

		m_recentFilesMenu = new JMenu(ResourceUtil.getMessage("main.menu.file.open.recent.file"));
		final String[] vRecentFiles = recentFiles();
		m_recentFilesMenu.setEnabled(vRecentFiles.length > 0);
		m_recentFilesMenu.setMnemonic('R');
		for (int i = 0; i < vRecentFiles.length; i++)
		{
			m_subMenuItem[i] = new JMenuItem(vRecentFiles[i]);
			m_subMenuItem[i].addActionListener(this);
			m_subMenuItem[i].setAccelerator(KeyStroke.getKeyStroke('1' + i, menuKeyMask));
			m_recentFilesMenu.add(m_subMenuItem[i]);
		}
		menu.add(m_recentFilesMenu);

		menu.add(new JSeparator());

		m_devcapOpenMenu = new JMenuItem(ResourceUtil.getMessage("main.menu.file.open.recent.devcap"));
		menu.add(m_devcapOpenMenu);
		m_devcapOpenMenu.addActionListener(this);

		m_csvItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.export.csv"));
		m_csvItem.addActionListener(this);
		menu.add(m_csvItem);

		menu.add(new JSeparator());

		m_quitItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.quit"));
		m_quitItem.addActionListener(frame); // TODO: this ?
		m_quitItem.setAccelerator(KeyStroke.getKeyStroke('Q', menuKeyMask));
		menu.add(m_quitItem);

		return menu;
	}
	
	public void setEnableClose()
	{
		m_saveItem.setEnabled(false);
		m_saveAsItem.setEnabled(false);
		m_csvItem.setEnabled(false);
		m_closeItem.setEnabled(false);
		m_closeAllItem.setEnabled(false);
	}
	
	public void setEnableOpen(final boolean mode)
	{
		m_csvItem.setEnabled(true);
		m_newItem.setEnabled(mode);
		m_closeItem.setEnabled(true);
		m_closeAllItem.setEnabled(true);
		m_devcapOpenMenu.setEnabled(mainController.getSetting(SettingKey.RECENT_DEV_CAP, String.class) != null);
		m_saveAsItem.setEnabled(mode);
		m_saveItem.setEnabled(mode);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);
		final Object eSrc = e.getSource();

		if (eSrc == m_newItem)
		{
			frame.newFile();
		}
		else if (eSrc == m_openItem)
		{
			frame.openFile();
		}
		else if (eSrc == m_closeItem)
		{
			frame.closeFile(1);
		}
		else if (eSrc == m_closeAllItem)
		{
			frame.closeFile(99999);
		}
		else if (eSrc == m_saveAsItem)
		{
			frame.saveAs();
		}
		else if (eSrc == m_saveItem)
		{
			frame.save();
		}
		else if (eSrc == m_csvItem)
		{
			MainView.getModel().saveAsCSV(null);
		}
		else if (eSrc == m_devcapOpenMenu)
		{
			String s = mainController.getSetting(SettingKey.RECENT_DEV_CAP, String.class);
			File f = null;

			if (s != null)
			{
				f = new File(s);
			}

			if (f != null)
				openRecentFile(f);
			else
				EditorUtils.errorBox("OpenJDFErrorKey", "No Devcap File defined");
		}

		for (int i = 0; i < m_subMenuItem.length; i++)
		{
			if (eSrc == m_subMenuItem[i])
			{
				final String newFile = m_subMenuItem[i].getText();
				openRecentFile(new File(newFile));
			}
		}
		MainView.setCursor(0, null);
	}
	
	/**
	 * Checks if the String[] m_iniFile.recentFiles file has any content. Called to check if the the m_recentFilesMenu is to be created.
	 * @return true if any file has recently been opened; false otherwise.
	 */
	private String[] recentFiles()
	{
		int l = 0;
		final String[] v = RecentFileUtil.getRecentFiles();
		for (int i = 0; i < v.length; i++)
		{
			if (v[i] != null)
			{
				l++;
			}
		}
		// no nulls - just return v
		if (l == v.length)
		{
			return v;
		}

		// create new continuous list w
		final String w[] = new String[l];
		l = 0;
		for (int i = 0; i < v.length; i++)
		{
			if (v[i] != null)
			{
				w[l++] = v[i];
			}
		}
		return w;
	}

	/**
	 * Called if a file is opened from the recent files menu.
	 * @param fileToSave - Path to the file that is to be opened
	 */
	private void openRecentFile(final File fileToSave)
	{
		MainView.setCursor(1, null);
		final boolean b = frame.readFile(fileToSave);

		if (b)
		{
			final String s = fileToSave.getAbsolutePath();

			if (RecentFileUtil.nrOfRecentFiles() != 1)
			{
				RecentFileUtil.updateOrder(s, true);
			}
		}
		else
		{
			EditorUtils.errorBox("OpenJDFErrorKey", fileToSave.getPath().toString());
		}
		MainView.setCursor(0, null);
	}

	/**
	 * Updates the order in the Recent Files Menu. also updates all windows and the ini file - just in case
	 * @param pathName - The path to the file
	 */
	public void updateRecentFilesMenu(final String pathName)
	{
		final boolean exist = RecentFileUtil.pathNameExists(pathName);

		RecentFileUtil.updateOrder(pathName, exist);

		if (RecentFileUtil.nrOfRecentFiles() != 0)
		{
			m_recentFilesMenu.setEnabled(true);
			for (int i = 0; i < RecentFileUtil.nrOfRecentFiles(); i++)
			{
				if (m_subMenuItem[i] == null)
				{
					m_subMenuItem[i] = new JMenuItem(RecentFileUtil.getRecentFiles()[i]);
					m_subMenuItem[i].addActionListener(this);
					m_recentFilesMenu.add(m_subMenuItem[i]);
				}
				else
				{
					m_subMenuItem[i].setText(RecentFileUtil.getRecentFiles()[i]);
				}
			}
		}
		else
		{
			m_recentFilesMenu.setEnabled(false);
		}
	}
}
