/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2010 The International Cooperation for the Integration of 
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MainController mainController;

	private JMenu m_insertElementMenu;
	private JMenu m_resourceMenu;
	private JMenu m_resourceLinkMenu;

	protected JMenu m_insertMenu;
	
	private EditorMenuBarFile editorMenuBarFile;
	private JMenu m_fileMenu;
	
	private EditorMenuBarTools editorMenuBarTools;
	private JMenu m_toolsMenu;
	
	protected JMenu m_editMenu;
	protected JMenu m_validateMenu;
	protected JMenu m_windowMenu;
	protected JMenu m_helpMenu;

	private JMenuItem m_nextItem;
	JMenuItem m_exportItem;
	JMenuItem m_copyItem;
	JMenuItem m_cutItem;
	JMenuItem m_undoItem;
	JMenuItem m_redoItem;
	private JMenuItem m_onlineItem;
	private JMenuItem m_QuickValidateItem;

	JMenuItem m_copyValidationListItem;
	JMenuItem m_findItem;
	private JMenuItem m_findXPathItem;
	JMenuItem m_infoItem;
	JMenuItem m_renameItem;
	JMenuItem m_modifyAttrValueItem;
	JMenuItem m_requiredAttrItem;
	JMenuItem m_requiredElemItem;

	private JMenuItem m_Windows[] = null;

	JMenuItem m_pasteItem;
	private JMenuItem m_deleteItem;

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

	private JRadioButtonMenuItem m_highlightFNRadioItem;
	private JRadioButtonMenuItem m_showAttrRadioItem;
	private JRadioButtonMenuItem m_showInhAttrRadioItem;
	private JRadioButtonMenuItem m_DispDefAttrRadioItem;


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

	public EditorMenuBarFile getMenuFile()
	{
		return editorMenuBarFile;
	}

	public EditorMenuBarTools getMenuTools()
	{
		return editorMenuBarTools;
	}

	private JMenu drawFileMenu()
	{
		editorMenuBarFile = new EditorMenuBarFile(mainController);
		m_fileMenu = editorMenuBarFile.createMenu();
		
		return m_fileMenu;
	}
	
	private JMenu drawToolsMenu()
	{
		editorMenuBarTools = new EditorMenuBarTools(mainController);
		m_toolsMenu = editorMenuBarTools.createMenu();
		
		return m_toolsMenu;
	}

	/**
	 * Creates the Edit menu.
	 * @return The Edit menu with the menu items.
	 */
	private JMenu drawEditMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		m_editMenu = new JMenu(ResourceUtil.getMessage("main.menu.edit"));
		m_editMenu.setBorderPainted(false);
		m_editMenu.addMouseListener(menuListener);

		m_undoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.undo"));
		m_undoItem.addActionListener(frame.undoAction);
		m_undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', menuKeyMask));
		m_undoItem.setEnabled(false);
		m_editMenu.add(m_undoItem);

		m_redoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.redo"));
		m_redoItem.addActionListener(frame.redoAction);
		m_redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', menuKeyMask));
		m_redoItem.setEnabled(false);
		m_editMenu.add(m_redoItem);

		m_editMenu.add(new JSeparator());

		m_cutItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.cut"));
		m_cutItem.addActionListener(frame);
		m_cutItem.setAccelerator(KeyStroke.getKeyStroke('X', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_cutItem.setEnabled(false);
		m_editMenu.add(m_cutItem);

		m_copyItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.copy"));
		m_copyItem.addActionListener(frame);
		m_copyItem.setAccelerator(KeyStroke.getKeyStroke('C', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_copyItem.setEnabled(false);
		m_editMenu.add(m_copyItem);

		m_pasteItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.paste"));
		m_pasteItem.addActionListener(frame);
		m_pasteItem.setAccelerator(KeyStroke.getKeyStroke('V', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_pasteItem.setEnabled(false);
		m_editMenu.add(m_pasteItem);

		m_deleteItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.delete"));
		m_deleteItem.addActionListener(this);
		m_deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		m_deleteItem.setEnabled(false);
		m_editMenu.add(m_deleteItem);

		m_editMenu.add(new JSeparator());

		m_renameItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.rename"));
		m_renameItem.addActionListener(frame);
		m_editMenu.add(m_renameItem);

		m_modifyAttrValueItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.modify"));
		m_modifyAttrValueItem.addActionListener(frame);
		m_editMenu.add(m_modifyAttrValueItem);

		m_editMenu.add(new JSeparator());

		m_findItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.find"));
		m_findItem.addActionListener(frame);
		m_findItem.setAccelerator(KeyStroke.getKeyStroke('F', menuKeyMask));
		m_editMenu.add(m_findItem);

		m_findXPathItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.find.xpath"));
		m_findXPathItem.addActionListener(this);
		m_editMenu.add(m_findXPathItem);

		return m_editMenu;
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

		final JMenu fileM = drawFileMenu();
		fileM.setMnemonic('F');
		fileM.setBackground(menuColor);
		add(fileM);

		final JMenu editM = drawEditMenu();
		editM.setMnemonic('E');
		editM.setBackground(menuColor);
		add(editM);

		final JMenu insertM = drawInsertMenu();
		insertM.setMnemonic('I');
		insertM.setBackground(menuColor);
		add(insertM);

		final JMenu toolsM = drawToolsMenu();
		toolsM.setMnemonic('T');
		toolsM.setBackground(menuColor);
		add(toolsM);

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

		m_highlightFNRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.highlight"), mainController.getSetting(SettingKey.VALIDATION_HIGHTLIGHT_FN, Boolean.class));
		m_highlightFNRadioItem.addActionListener(this);
		m_validateMenu.add(m_highlightFNRadioItem);

		m_validateMenu.add(new JSeparator());

		m_showAttrRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.display.attributes"), mainController.getSetting(SettingKey.TREEVIEW_ATTRIBUTE, Boolean.class));
		m_showAttrRadioItem.addActionListener(this);
		m_validateMenu.add(m_showAttrRadioItem);

		m_showInhAttrRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.display.attributes.inherited"), mainController.getSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, Boolean.class));
		m_showInhAttrRadioItem.addActionListener(this);
		m_validateMenu.add(m_showInhAttrRadioItem);

		m_DispDefAttrRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.display.attributes.default"), mainController.getSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, Boolean.class));
		m_DispDefAttrRadioItem.addActionListener(this);
		m_validateMenu.add(m_DispDefAttrRadioItem);

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
			m_modifyAttrValueItem.setEnabled(false);

			KElement kElement = EditorUtils.getElement(path);
			if (kElement == null)
			{
				kElement = node.getElement();
			}

			m_renameItem.setEnabled(parent != null);

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
			m_renameItem.setEnabled(parent != null);
			m_modifyAttrValueItem.setEnabled(true);
		}
	}

	/**
	 *  
	 */
	public void setEnableClose()
	{
		editorMenuBarFile.setEnableClose();
		editorMenuBarTools.setEnableClose();
		
		m_cutItem.setEnabled(false);
		m_copyItem.setEnabled(false);
		m_pasteItem.setEnabled(false);
		m_deleteItem.setEnabled(false);
		m_insertElementMenu.setEnabled(false);
		m_resourceMenu.setEnabled(false);
		m_resourceLinkMenu.setEnabled(false);
		m_insertAttrItem.setEnabled(false);
		m_requiredAttrItem.setEnabled(false);
		m_requiredElemItem.setEnabled(false);
		m_modifyAttrValueItem.setEnabled(false);
		m_renameItem.setEnabled(false);
		m_findItem.setEnabled(false);
		m_findXPathItem.setEnabled(false);
		m_devCapItem.setEnabled(false);
		m_exportItem.setEnabled(false);
		m_QuickValidateItem.setEnabled(false);
		m_undoItem.setEnabled(false);
		m_redoItem.setEnabled(false);
	}

	/**
	 * @param mode
	 */
	public void setEnableOpen(final boolean mode)
	{
		editorMenuBarTools.setEnableOpen(mode);
		
		m_cutItem.setEnabled(mode);
		m_copyItem.setEnabled(mode);
		m_deleteItem.setEnabled(mode);
		m_insertElementMenu.setEnabled(mode);
		m_resourceMenu.setEnabled(mode);
		m_resourceLinkMenu.setEnabled(mode);
		m_insertAttrItem.setEnabled(mode);
		m_requiredAttrItem.setEnabled(mode);
		m_requiredElemItem.setEnabled(mode);
		m_findItem.setEnabled(true);
		m_findXPathItem.setEnabled(true);
		m_devCapItem.setEnabled(true);
		m_exportItem.setEnabled(true);
		m_QuickValidateItem.setEnabled(true);
	}

	// //////////////////////////////////////////////////////////////////////////////

	class Menu_MouseListener extends MouseAdapter
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
		
		if (eSrc == m_showInhAttrRadioItem)
		{
			mainController.setSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, m_showInhAttrRadioItem.isSelected());
			if (getJDFDoc() != null)
			{
				ta.drawTreeView(frame.getEditorDoc());
			}

		}
		if (eSrc == m_DispDefAttrRadioItem)
		{
			mainController.setSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, m_DispDefAttrRadioItem.isSelected());
			if (getJDFDoc() != null)
			{
				ta.drawTreeView(frame.getEditorDoc());
			}
		}
		else if (eSrc == m_showAttrRadioItem)
		{
			toggleAttributes();
		}
		else if (eSrc == m_highlightFNRadioItem)
		{
			mainController.setSetting(SettingKey.VALIDATION_HIGHTLIGHT_FN, m_highlightFNRadioItem.isSelected());
		}
		else if (eSrc == m_nextItem)
		{
			frame.nextFile(-1);
		}
		/*
		 * Action that is performed when select File->New
		 */
		else if (eSrc == m_QuickValidateItem && MainView.getModel() != null)
		{
			MainView.getModel().validate();
		}
		else if (eSrc == m_deleteItem)
		{
			MainView.getModel().deleteSelectedNodes();
		}
		else if (eSrc == m_findXPathItem)
		{
			MainView.getFrame().m_treeArea.findXPathElem();
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
	 * toggle attributes on and off
	 */
	private void toggleAttributes()
	{
		final JDFFrame frame = MainView.getFrame();
		final JDFTreeArea ta = frame.m_treeArea;

		if (!m_showAttrRadioItem.isSelected())
		{
			m_showInhAttrRadioItem.setSelected(false);
			m_showInhAttrRadioItem.setEnabled(false);
			m_DispDefAttrRadioItem.setSelected(false);
			m_DispDefAttrRadioItem.setEnabled(false);
		}
		else
		{
			m_showInhAttrRadioItem.setEnabled(true);
			m_DispDefAttrRadioItem.setEnabled(true);
		}

		mainController.setSetting(SettingKey.TREEVIEW_ATTRIBUTE, m_showAttrRadioItem.isSelected());
		mainController.setSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, m_DispDefAttrRadioItem.isSelected());
		mainController.setSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, m_showInhAttrRadioItem.isSelected());
		if (getJDFDoc() != null)
		{
			ta.drawTreeView(frame.getEditorDoc());
		}
	}

	private JDFDoc getJDFDoc()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getJDFDoc();
	}

	private EditorDocument getEditorDoc()
	{
		final EditorDocument ed = MainView.getFrame().getEditorDoc();
		return ed;
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
