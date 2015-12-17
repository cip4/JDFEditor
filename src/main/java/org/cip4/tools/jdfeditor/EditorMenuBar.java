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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.elementwalker.URLExtractor;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.menu.MenuFile;
import org.cip4.tools.jdfeditor.menu.EditorMenuBarView;
import org.cip4.tools.jdfeditor.menu.MenuEdit;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.RecentFileUtil;
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

	private JMenu m_insertElementMenu;
	private JMenu m_resourceMenu;
	private JMenu m_resourceLinkMenu;

	protected JMenu m_insertMenu;
	
	private MenuFile editorMenuBarFile;
	private MenuEdit editorMenuBarEdit;
	private EditorMenuBarView editorMenuBarView;
	private EditorMenuBarTools editorMenuBarTools;
	
	protected JMenu m_editMenu;
	protected JMenu m_validateMenu;
	protected JMenu m_windowMenu;
	protected JMenu m_helpMenu;

	private JMenuItem m_nextItem;
	JMenuItem m_exportItem;
	private JMenuItem m_onlineItem;
	private JMenuItem m_QuickValidateItem;

	JMenuItem m_copyValidationListItem;
	JMenuItem m_infoItem;
	JMenuItem m_requiredAttrItem;
	JMenuItem m_requiredElemItem;

	private JMenuItem m_Windows[] = null;

	JMenuItem m_devCapItem;
	// popup menues
	JMenuItem m_insertElemBeforeItem;
	JMenuItem m_insertElemAfterItem;
	JMenuItem m_insertElemIntoItem;
	JMenuItem m_insertInResLinkItem;
	JMenuItem m_insertOutResLinkItem;
	JMenuItem m_insertInResItem;
	JMenuItem m_insertOutResItem;
	JMenuItem m_insertResItem;
	JMenuItem m_insertAttrItem;
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
		return editorMenuBarFile;
	}

	public MenuEdit getMenuEdit()
	{
		return editorMenuBarEdit;
	}

	public EditorMenuBarTools getMenuTools()
	{
		return editorMenuBarTools;
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
		final Color menuColor = getBackground();

		editorMenuBarFile = new MenuFile(mainController);
		final JMenu m_fileMenu = editorMenuBarFile.createMenu();
		m_fileMenu.setMnemonic('F');
		m_fileMenu.setBackground(menuColor);
		add(m_fileMenu);

		editorMenuBarEdit = new MenuEdit(mainController);
		final JMenu m_editMenu = editorMenuBarEdit.createMenu();
		m_editMenu.setMnemonic('E');
		m_editMenu.setBackground(menuColor);
		add(m_editMenu);

		editorMenuBarView = new EditorMenuBarView(mainController);
		final JMenu m_viewMenu = editorMenuBarView.createMenu();
		m_viewMenu.setMnemonic('V');
		m_viewMenu.setBackground(menuColor);
		add(m_viewMenu);

		final JMenu insertM = drawInsertMenu();
		insertM.setMnemonic('I');
		insertM.setBackground(menuColor);
		add(insertM);

		editorMenuBarTools = new EditorMenuBarTools(mainController);
		final JMenu m_toolsMenu = editorMenuBarTools.createMenu();
		m_toolsMenu.setMnemonic('T');
		m_toolsMenu.setBackground(menuColor);
		add(m_toolsMenu);

		final JMenu validateM = drawValidateMenu();
		validateM.setMnemonic('V');
		validateM.setBackground(menuColor);
		add(validateM);

		final JMenu windowM = drawWindowMenu();
		windowM.setMnemonic('W');
		windowM.setBackground(menuColor);
		add(windowM);

		final JMenu helpM = drawHelpMenu();
		helpM.setMnemonic('H');
		helpM.setBackground(menuColor);
		add(helpM);

		return this;
	}

	/**
	 * Creates the Insert menu.
	 * @return The Insert menu with the menu items.
	 */
	private JMenu drawInsertMenu()
	{
		final Menu_MouseListener menuListener = new Menu_MouseListener();

		final JDFFrame m_frame = MainView.getFrame();
		m_insertMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert"));
		m_insertMenu.setBorderPainted(false);
		m_insertMenu.addMouseListener(menuListener);

		m_insertElementMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert.element"));

		m_insertElemBeforeItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.element.before"));
		m_insertElemBeforeItem.addActionListener(m_frame);
		m_insertElementMenu.add(m_insertElemBeforeItem);

		m_insertElemIntoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.element.into"));
		m_insertElemIntoItem.addActionListener(m_frame);
		m_insertElementMenu.add(m_insertElemIntoItem);

		m_insertElemAfterItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.element.after"));
		m_insertElemAfterItem.addActionListener(m_frame);
		m_insertElementMenu.add(m_insertElemAfterItem);

		m_insertMenu.add(m_insertElementMenu);

		m_resourceMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert.resource"));

		m_insertInResItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.resource.input"));
		m_insertInResItem.addActionListener(m_frame);
		m_resourceMenu.add(m_insertInResItem);

		m_insertOutResItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.resource.output"));
		m_insertOutResItem.addActionListener(m_frame);
		m_resourceMenu.add(m_insertOutResItem);

		m_resourceMenu.add(new JSeparator());

		m_insertResItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.resource.resource"));
		m_insertResItem.addActionListener(m_frame);
		m_resourceMenu.add(m_insertResItem);

		m_insertMenu.add(m_resourceMenu);

		m_resourceLinkMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert.reslink"));

		m_insertInResLinkItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.reslink.input"));
		m_insertInResLinkItem.addActionListener(m_frame);
		m_resourceLinkMenu.add(m_insertInResLinkItem);

		m_insertOutResLinkItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.reslink.output"));
		m_insertOutResLinkItem.addActionListener(m_frame);
		m_resourceLinkMenu.add(m_insertOutResLinkItem);

		m_insertMenu.add(m_resourceLinkMenu);

		m_insertAttrItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.attribute"));
		m_insertAttrItem.addActionListener(m_frame);
		m_insertMenu.add(m_insertAttrItem);

		m_insertMenu.add(new JSeparator());

		m_requiredAttrItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.required.attributes"));
		m_requiredAttrItem.addActionListener(m_frame);
		m_insertMenu.add(m_requiredAttrItem);

		m_requiredElemItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.required.elements"));
		m_requiredElemItem.addActionListener(m_frame);
		m_insertMenu.add(m_requiredElemItem);

		return m_insertMenu;
	}

	/**
	 * Creates the Validate menu.
	 * @return The Validate menu with the menu items.
	 */
	private JMenu drawValidateMenu()
	{
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		final JDFFrame m_frame = MainView.getFrame();

		m_validateMenu = new JMenu(ResourceUtil.getMessage("main.menu.tools.validate"));
		m_validateMenu.setBorderPainted(false);
		m_validateMenu.addMouseListener(new Menu_MouseListener());

		m_QuickValidateItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.validate"));
		m_QuickValidateItem.addActionListener(this);
		m_QuickValidateItem.setEnabled(false);
		m_QuickValidateItem.setAccelerator(KeyStroke.getKeyStroke('A', menuKeyMask));
		m_validateMenu.add(m_QuickValidateItem);

		m_validateMenu.add(new JSeparator());

		m_copyValidationListItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.copy"));
		m_copyValidationListItem.addActionListener(m_frame);
		m_copyValidationListItem.setEnabled(false);
		m_validateMenu.add(m_copyValidationListItem);

		m_exportItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.export"));
		m_exportItem.addActionListener(m_frame);
		m_exportItem.setAccelerator(KeyStroke.getKeyStroke('E', menuKeyMask));
		m_validateMenu.add(m_exportItem);

		m_devCapItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.test"));
		m_devCapItem.addActionListener(m_frame);
		m_devCapItem.setAccelerator(KeyStroke.getKeyStroke('D', menuKeyMask));
		m_devCapItem.setEnabled(false);
		m_validateMenu.add(m_devCapItem);

		return m_validateMenu;
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
		editorMenuBarFile.updateRecentFilesMenu(pathName);
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
			m_insertElementMenu.setEnabled(true);
			m_resourceMenu.setEnabled(true);
			m_resourceLinkMenu.setEnabled(true);
			m_requiredElemItem.setEnabled(true);
			editorMenuBarEdit.setTwoProperties(parent != null, false);

			KElement kElement = EditorUtils.getElement(path);
			if (kElement == null)
			{
				kElement = node.getElement();
			}


			if (((JDFTreeNode) m_frame.getRootNode().getFirstChild()).equals(node))
			{
				m_insertElemAfterItem.setEnabled(false);
				m_insertElemBeforeItem.setEnabled(false);
			}
			else
			{
				m_insertElemAfterItem.setEnabled(true);
				m_insertElemBeforeItem.setEnabled(true);
			}

			if (!(kElement instanceof JDFNode) && !kElement.getNodeName().equals("ResourcePool"))
			{
				m_resourceMenu.setEnabled(false);
			}
			if (!(kElement instanceof JDFNode) && !kElement.getNodeName().equals("ResourceLinkPool"))
			{
				m_resourceLinkMenu.setEnabled(false);
			}

			if (kElement instanceof JDFResourcePool)
			{
				m_insertInResLinkItem.setEnabled(false);
				m_insertOutResLinkItem.setEnabled(false);
				m_insertInResItem.setEnabled(true);
				m_insertOutResItem.setEnabled(true);
				m_insertResItem.setEnabled(true);
			}
			else if ((kElement instanceof JDFResourceLinkPool) && EditorUtils.getResourcesAllowedToLink(((JDFResourceLinkPool) kElement).getParentJDF(), null) != null)
			{
				m_insertInResItem.setEnabled(false);
				m_insertOutResItem.setEnabled(false);
				m_insertResItem.setEnabled(false);
				m_insertInResLinkItem.setEnabled(true);
				m_insertOutResLinkItem.setEnabled(true);
			}
			else if (kElement instanceof JDFNode)
			{
				m_insertInResItem.setEnabled(true);
				m_insertOutResItem.setEnabled(true);
				m_insertResItem.setEnabled(true);

				final boolean bSwitch = EditorUtils.getResourcesAllowedToLink((JDFNode) kElement, null) != null;
				m_insertInResLinkItem.setEnabled(bSwitch);
				m_insertOutResLinkItem.setEnabled(bSwitch);
			}
		}
		else
		{
			m_insertElementMenu.setEnabled(false);
			m_resourceMenu.setEnabled(false);
			m_requiredElemItem.setEnabled(false);
			editorMenuBarEdit.setTwoProperties(parent != null, true);
		}
	}

	/**
	 *  
	 */
	public void setEnableClose()
	{
		editorMenuBarFile.setEnableClose();
		editorMenuBarEdit.setEnableClose();
		editorMenuBarTools.setEnableClose();

		m_insertElementMenu.setEnabled(false);
		m_resourceMenu.setEnabled(false);
		m_resourceLinkMenu.setEnabled(false);
		m_insertAttrItem.setEnabled(false);
		m_requiredAttrItem.setEnabled(false);
		m_requiredElemItem.setEnabled(false);

		m_devCapItem.setEnabled(false);
		m_exportItem.setEnabled(false);
		m_QuickValidateItem.setEnabled(false);
	}

	/**
	 * @param mode
	 */
	public void setEnableOpen(final boolean mode)
	{
		editorMenuBarEdit.setEnableOpen(mode);
		editorMenuBarTools.setEnableOpen(mode);

		m_insertElementMenu.setEnabled(mode);
		m_resourceMenu.setEnabled(mode);
		m_resourceLinkMenu.setEnabled(mode);
		m_insertAttrItem.setEnabled(mode);
		m_requiredAttrItem.setEnabled(mode);
		m_requiredElemItem.setEnabled(mode);
		m_devCapItem.setEnabled(true);
		m_exportItem.setEnabled(true);
		m_QuickValidateItem.setEnabled(true);
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
		final JDFTreeArea ta = frame.m_treeArea;
		
		if (eSrc == m_nextItem)
		{
			frame.nextFile(-1);
		}
		else if (eSrc == m_QuickValidateItem && MainView.getModel() != null)
		{
			MainView.getModel().validate();
		}

		// select
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
