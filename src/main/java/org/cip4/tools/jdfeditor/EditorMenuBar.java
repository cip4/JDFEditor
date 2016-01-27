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
package org.cip4.tools.jdfeditor;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.menu.MenuTools;
import org.cip4.tools.jdfeditor.menu.MenuFile;
import org.cip4.tools.jdfeditor.menu.MenuInsert;
import org.cip4.tools.jdfeditor.menu.MenuValidate;
import org.cip4.tools.jdfeditor.menu.MenuView;
import org.cip4.tools.jdfeditor.menu.MenuEdit;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * Class to implement all the menu bar and menu related stuff moved here from JDFFrame
 * @author prosirai
 * 
 * Code for the menu on the top of the JDFEditor as well as the pop up menus. You can modify the hot key settings here for every menu choice.
 * 
 */
public class EditorMenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private MainController mainController;

	protected JMenu m_insertMenu;

	private MenuFile menuFile;
	private MenuEdit menuEdit;
	private MenuView menuView;
	private MenuInsert menuInsert;
	private MenuTools menuTools;
	private MenuValidate menuValidate;

	protected JMenu m_windowMenu;
	protected JMenu m_helpMenu;

	private JMenuItem m_nextItem;
	private JMenuItem m_onlineItem;

	JMenuItem m_infoItem;

	private JMenuItem m_Windows[] = null;

	// popup menues
	JMenuItem m_pastePopupItem;


	/**
	 * Default constructor.
	 */
	public EditorMenuBar()
	{
	}

	/**
	 * Register a MainController for this view (MVC Pattern)
	 * @param mainController The MainController for this view.
	 */
	public void registerController(final MainController mainController)
	{
		this.mainController = mainController;
	}

	public MenuFile getMenuFile()
	{
		return menuFile;
	}

	public MenuEdit getMenuEdit()
	{
		return menuEdit;
	}

	public MenuInsert getMenuInsert()
	{
		return menuInsert;
	}

	public MenuTools getMenuTools()
	{
		return menuTools;
	}

	public MenuValidate getMenuValidate()
	{
		return menuValidate;
	}

	/**
	 * Creates the Help menu.
	 * @return The Help menu with the menu items.
	 */
	private JMenu drawHelpMenu()
	{
		final Menu_MouseListener menuListener = new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		m_helpMenu = new JMenu(ResourceUtil.getMessage("main.menu.help"));
		m_helpMenu.setBorderPainted(false);
		m_helpMenu.addMouseListener(menuListener);

		m_onlineItem = new JMenuItem(ResourceUtil.getMessage("main.menu.help.online"));
		m_onlineItem.addActionListener(this.mainController);
		m_onlineItem.setActionCommand(MainController.ACTION_ONLINE_HELP);
		m_onlineItem.setAccelerator(KeyStroke.getKeyStroke('H', menuKeyMask));
		m_helpMenu.add(m_onlineItem);

		m_helpMenu.add(new JSeparator());

		m_infoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.help.info"));
		m_infoItem.addActionListener(this.mainController);
		m_infoItem.setActionCommand(MainController.ACTION_INFO);
		m_helpMenu.add(m_infoItem);

		return m_helpMenu;
	}

	/**
	 * Creates the Window menu.
	 * @return The Window menu with the menu items.
	 */
	private JMenu drawWindowMenu()
	{
		final Menu_MouseListener menuListener = new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		m_windowMenu = new JMenu(ResourceUtil.getMessage("main.menu.tools.window"));
		m_windowMenu.setBorderPainted(false);
		m_windowMenu.addMouseListener(menuListener);

		m_nextItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.window.next"));
		m_nextItem.addActionListener(this);
		m_nextItem.setAccelerator(KeyStroke.getKeyStroke('T', menuKeyMask));
		m_windowMenu.add(m_nextItem);

		return m_windowMenu;
	}

	/**
	 * Create the different menues and adds them to the menubar.
	 * @return The Menu bar with all its items.
	 */
	public JMenuBar drawMenu()
	{
		setBackground(Color.lightGray);

		menuFile = new MenuFile(mainController);
		final JMenu m_fileMenu = menuFile.createMenu();
		m_fileMenu.setMnemonic('F');
		add(m_fileMenu);

		menuEdit = new MenuEdit(mainController);
		final JMenu m_editMenu = menuEdit.createMenu();
		m_editMenu.setMnemonic('E');
		add(m_editMenu);

		menuView = new MenuView(mainController);
		final JMenu m_viewMenu = menuView.createMenu();
		m_viewMenu.setMnemonic('V');
		add(m_viewMenu);

		menuInsert = new MenuInsert(mainController);
		final JMenu insertMenu = menuInsert.createMenu();
		insertMenu.setMnemonic('I');
		add(insertMenu);

		menuTools = new MenuTools(mainController);
		final JMenu m_toolsMenu = menuTools.createMenu();
		m_toolsMenu.setMnemonic('T');
		add(m_toolsMenu);

		menuValidate = new MenuValidate(mainController);
		final JMenu m_validateMenu = menuValidate.createMenu();
		m_validateMenu.setMnemonic('V');
		add(m_validateMenu);

		final JMenu windowM = drawWindowMenu();
		windowM.setMnemonic('W');
		add(windowM);

		final JMenu helpM = drawHelpMenu();
		helpM.setMnemonic('H');
		add(helpM);

		return this;
	}

	/**
	 * Creates the Help menu.
	 * @return The Help menu with the menu items.
	 */

	/**
	 * Updates the order in the Recent Files Menu. also updates all windows and the ini file - just in case
	 * @param pathName - The path to the file
	 */
	public void updateRecentFilesMenu(final String pathName)
	{
		menuFile.updateRecentFilesMenu(pathName);
		updateWindowsMenu();
	}

	/**
	 * Updates the order in the Recent Files Menu.
	 */
	public void updateWindowsMenu()
	{
		final JDFFrame m_frame = MainView.getFrame();
		if (m_frame.m_DocPos >= 0)
		{
			JMenuItem mwindows[] = null;
			if ((m_Windows == null) || (m_Windows.length != m_frame.m_VjdfDocument.size()))
			{
				mwindows = new JMenuItem[m_frame.m_VjdfDocument.size()];
				if (m_Windows != null)
				{
					for (int i = 0; i < m_Windows.length; i++)
					{
						if (i < mwindows.length)
						{
							mwindows[i] = m_Windows[i];
						}
						else
						{
							m_windowMenu.remove(m_Windows[i]);
						}
					}
				}
				for (int i = m_Windows == null ? 0 : m_Windows.length; i < mwindows.length; i++)
				{
					mwindows[i] = new JMenuItem("foo");
					mwindows[i].addActionListener(this);
					m_windowMenu.add(mwindows[i]);
				}
				m_Windows = mwindows;
			}
			for (int i = 0; i < m_Windows.length; i++)
			{
				final JDFDoc d = m_frame.m_VjdfDocument.elementAt(i).getJDFDoc();
				m_Windows[i].setText(d.getOriginalFileName());
			}
			setWindowMenuItemColor(m_frame.m_DocPos);
		}
		else if (m_Windows != null)
		{
			for (int i = 0; i < m_Windows.length; i++)
			{
				m_windowMenu.remove(m_Windows[i]);
			}
			m_Windows = null;
		}
	}

	protected void setEnabledInMenu(final TreePath path)
	{
		final JDFFrame m_frame = MainView.getFrame();
		final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
		final JDFTreeNode parNode = (JDFTreeNode) node.getParent();
		Object parent = null;
		if (parNode != null)
		{
			parent = parNode.getUserObject();
		}
		if (node.isElement())
		{
			menuInsert.setEnabled4(true);
			menuEdit.setTwoProperties(parent != null, false);

			KElement kElement = EditorUtils.getElement(path);
			if (kElement == null)
			{
				kElement = node.getElement();
			}


			if (((JDFTreeNode) m_frame.getRootNode().getFirstChild()).equals(node))
			{
				menuInsert.setEnabled2(false);
			}
			else
			{
				menuInsert.setEnabled2(true);
			}

			if (!(kElement instanceof JDFNode) && !kElement.getNodeName().equals("ResourcePool"))
			{
				menuInsert.m_resourceMenu.setEnabled(false);
			}
			if (!(kElement instanceof JDFNode) && !kElement.getNodeName().equals("ResourceLinkPool"))
			{
				menuInsert.m_resourceLinkMenu.setEnabled(false);
			}

			if (kElement instanceof JDFResourcePool)
			{
				menuInsert.setEnabledJDFResourcePool(false);
			}
			else if ((kElement instanceof JDFResourceLinkPool) && EditorUtils.getResourcesAllowedToLink(((JDFResourceLinkPool) kElement).getParentJDF(), null) != null)
			{
				menuInsert.setEnabledJDFResourcePool(true);
			}
			else if (kElement instanceof JDFNode)
			{
				menuInsert.m_insertInResItem.setEnabled(true);
				menuInsert.m_insertOutResItem.setEnabled(true);
				menuInsert.m_insertResItem.setEnabled(true);

				final boolean bSwitch = EditorUtils.getResourcesAllowedToLink((JDFNode) kElement, null) != null;
				menuInsert.m_insertInResLinkItem.setEnabled(bSwitch);
				menuInsert.m_insertOutResLinkItem.setEnabled(bSwitch);
			}
		}
		else
		{
			menuInsert.setEnabled3(false);
			menuEdit.setTwoProperties(parent != null, true);
		}
	}

	/**
	 *  
	 */
	public void setEnableClose()
	{
		menuFile.setEnableClose();
		menuEdit.setEnableClose();
		menuTools.setEnableClose();
		menuInsert.setEnableClose();
		menuValidate.setEnableClose();
	}

	/**
	 * @param mode
	 */
	public void setEnableOpen(final boolean mode)
	{
		menuEdit.setEnableOpen(mode);
		menuTools.setEnableOpen(mode);
		menuInsert.setEnableOpen(mode);
		menuValidate.setEnableOpen(mode);
	}

	// //////////////////////////////////////////////////////////////////////////////

	public class Menu_MouseListener extends MouseAdapter
	{
		@Override
		public void mouseEntered(final MouseEvent e)
		{
			final Object source = e.getSource();
			if (source instanceof JMenu)
				((JMenu) source).setBorderPainted(true);
		}

		@Override
		public void mouseExited(final MouseEvent e)
		{
			final Object source = e.getSource();
			if (source instanceof JMenu)
				((JMenu) source).setBorderPainted(true);
		}
	}

	// ///////////////////////////////////////////////////////////////////
	/**
	 * WHERE the process begins when you select File->New from the JDFEditor menu.
	 * @param e
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);
		final Object eSrc = e.getSource();
		final JDFFrame frame = MainView.getFrame();
		final JDFTreeArea ta = frame.getJDFTreeArea();
		
		if (eSrc == m_nextItem)
		{
			frame.nextFile(-1);
		}

		if (m_Windows != null)
		{
			for (int i = 0; i < m_Windows.length; i++)
			{
				if (eSrc == m_Windows[i])
				{
					MainView.getFrame().nextFile(i);
				}
			}
		}

		MainView.setCursor(0, null);
	}

	/**
	 * set the windows menuiten background color of pos
	 * @param pos the position in the document list
	 */
	public void setWindowMenuItemColor(final int pos)
	{
		final Color menuColor = m_nextItem.getBackground();
		for (int i = 0; i < m_Windows.length; i++)
		{
			if (i == pos)
			{
				m_Windows[i].setBackground(Color.orange);
			}
			else
			{
				m_Windows[i].setBackground(menuColor);
			}
		}
	}

}
