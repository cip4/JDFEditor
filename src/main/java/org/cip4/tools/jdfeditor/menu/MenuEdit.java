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
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class MenuEdit implements ActionListener, MenuInterface
{
	private MainController mainController;

	private JMenu menu;

	public JMenuItem m_undoItem;
	public JMenuItem m_redoItem;

	public JMenuItem m_cutItem;
	public JMenuItem m_copyItem;
	public JMenuItem m_pasteItem;

	private JMenuItem m_deleteItem;

	public JMenuItem m_renameItem;
	public JMenuItem m_modifyAttrValueItem;
	public JMenuItem m_findItem;
	private JMenuItem m_findXPathItem;


	public MenuEdit(final MainController mainController)
	{
		this.mainController = mainController;
	}

	@Override
	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		menu = new JMenu(ResourceUtil.getMessage("main.menu.edit"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);

		m_undoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.undo"));
		m_undoItem.addActionListener(frame.undoAction);
		m_undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', menuKeyMask));
		m_undoItem.setEnabled(false);
		menu.add(m_undoItem);

		m_redoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.redo"));
		m_redoItem.addActionListener(frame.redoAction);
		m_redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', menuKeyMask));
		m_redoItem.setEnabled(false);
		menu.add(m_redoItem);

		menu.add(new JSeparator());

		m_cutItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.cut"));
		m_cutItem.addActionListener(frame);
		m_cutItem.setAccelerator(KeyStroke.getKeyStroke('X', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_cutItem.setEnabled(false);
		menu.add(m_cutItem);

		m_copyItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.copy"));
		m_copyItem.addActionListener(frame);
		m_copyItem.setAccelerator(KeyStroke.getKeyStroke('C', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_copyItem.setEnabled(false);
		menu.add(m_copyItem);

		m_pasteItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.paste"));
		m_pasteItem.addActionListener(frame);
		m_pasteItem.setAccelerator(KeyStroke.getKeyStroke('V', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_pasteItem.setEnabled(false);
		menu.add(m_pasteItem);

		m_deleteItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.delete"));
		m_deleteItem.addActionListener(this);
		m_deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		m_deleteItem.setEnabled(false);
		menu.add(m_deleteItem);

		menu.add(new JSeparator());

		m_renameItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.rename"));
		m_renameItem.addActionListener(frame);
		menu.add(m_renameItem);

		m_modifyAttrValueItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.modify"));
		m_modifyAttrValueItem.addActionListener(frame);
		menu.add(m_modifyAttrValueItem);

		menu.add(new JSeparator());

		m_findItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.find"));
		m_findItem.addActionListener(frame);
		m_findItem.setAccelerator(KeyStroke.getKeyStroke('F', menuKeyMask));
		menu.add(m_findItem);

		m_findXPathItem = new JMenuItem(ResourceUtil.getMessage("main.menu.edit.find.xpath"));
		m_findXPathItem.addActionListener(this);
		menu.add(m_findXPathItem);

		return menu;
	}

	@Override
	public void setEnableClose()
	{
		m_undoItem.setEnabled(false);
		m_redoItem.setEnabled(false);
		m_cutItem.setEnabled(false);
		m_copyItem.setEnabled(false);
		m_pasteItem.setEnabled(false);
		m_deleteItem.setEnabled(false);
		m_modifyAttrValueItem.setEnabled(false);
		m_renameItem.setEnabled(false);
		m_findItem.setEnabled(false);
		m_findXPathItem.setEnabled(false);
	}

	@Override
	public void setEnableOpen(final boolean mode)
	{
		m_cutItem.setEnabled(mode);
		m_copyItem.setEnabled(mode);
		m_deleteItem.setEnabled(mode);
		m_findItem.setEnabled(true);
		m_findXPathItem.setEnabled(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);
		final Object eSrc = e.getSource();

		if (eSrc == m_deleteItem)
		{
			MainView.getModel().deleteSelectedNodes();
		}
		else if (eSrc == m_findXPathItem)
		{
			MainView.getFrame().getJDFTreeArea().findXPathElem();
		}

		MainView.setCursor(0, null);
	}

	public void setTwoProperties(final boolean enableRename, final boolean enableModify)
	{
		m_renameItem.setEnabled(enableRename);
		m_modifyAttrValueItem.setEnabled(enableModify);
	}

}
