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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class MenuInsert implements MenuInterface, ActionListener
{
	private MainController mainController;

	private JMenu menu;

	private JMenu m_insertElementMenu;
	public JMenu m_resourceMenu;
	public JMenu m_resourceLinkMenu;

	public JMenuItem m_insertElemBeforeItem;
	public JMenuItem m_insertElemIntoItem;
	public JMenuItem m_insertElemAfterItem;

	public JMenuItem m_insertInResItem;
	public JMenuItem m_insertOutResItem;

	public JMenuItem m_insertResItem;
	public JMenuItem m_insertInResLinkItem;
	public JMenuItem m_insertOutResLinkItem;

	public JMenuItem m_insertAttrItem;
	public JMenuItem m_requiredAttrItem;
	public JMenuItem m_requiredElemItem;


	public MenuInsert(final MainController mainController)
	{
		this.mainController = mainController;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		menu = new JMenu(ResourceUtil.getMessage("main.menu.insert"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);
		
		m_insertElementMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert.element"));

		m_insertElemBeforeItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.element.before"));
		m_insertElemBeforeItem.addActionListener(frame);
		m_insertElementMenu.add(m_insertElemBeforeItem);

		m_insertElemIntoItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.element.into"));
		m_insertElemIntoItem.addActionListener(frame);
		m_insertElementMenu.add(m_insertElemIntoItem);

		m_insertElemAfterItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.element.after"));
		m_insertElemAfterItem.addActionListener(frame);
		m_insertElementMenu.add(m_insertElemAfterItem);

		menu.add(m_insertElementMenu);
		
		m_resourceMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert.resource"));

		m_insertInResItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.resource.input"));
		m_insertInResItem.addActionListener(frame);
		m_resourceMenu.add(m_insertInResItem);

		m_insertOutResItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.resource.output"));
		m_insertOutResItem.addActionListener(frame);
		m_resourceMenu.add(m_insertOutResItem);

		m_resourceMenu.add(new JSeparator());

		m_insertResItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.resource.resource"));
		m_insertResItem.addActionListener(frame);
		m_resourceMenu.add(m_insertResItem);

		menu.add(m_resourceMenu);
		
		m_resourceLinkMenu = new JMenu(ResourceUtil.getMessage("main.menu.insert.reslink"));

		m_insertInResLinkItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.reslink.input"));
		m_insertInResLinkItem.addActionListener(frame);
		m_resourceLinkMenu.add(m_insertInResLinkItem);

		m_insertOutResLinkItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.reslink.output"));
		m_insertOutResLinkItem.addActionListener(frame);
		m_resourceLinkMenu.add(m_insertOutResLinkItem);

		menu.add(m_resourceLinkMenu);

		m_insertAttrItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.attribute"));
		m_insertAttrItem.addActionListener(frame);
		
		menu.add(m_insertAttrItem);

		menu.add(new JSeparator());

		m_requiredAttrItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.required.attributes"));
		m_requiredAttrItem.addActionListener(frame);
		
		menu.add(m_requiredAttrItem);

		m_requiredElemItem = new JMenuItem(ResourceUtil.getMessage("main.menu.insert.required.elements"));
		m_requiredElemItem.addActionListener(frame);
		menu.add(m_requiredElemItem);
		
		return menu;
	}

	@Override
	public void setEnableClose()
	{
		m_insertElementMenu.setEnabled(false);
		m_resourceMenu.setEnabled(false);
		m_resourceLinkMenu.setEnabled(false);
		m_insertAttrItem.setEnabled(false);
		m_requiredAttrItem.setEnabled(false);
		m_requiredElemItem.setEnabled(false);
	}

	@Override
	public void setEnableOpen(final boolean mode)
	{
		m_insertElementMenu.setEnabled(mode);
		m_resourceMenu.setEnabled(mode);
		m_resourceLinkMenu.setEnabled(mode);
		m_insertAttrItem.setEnabled(mode);
		m_requiredAttrItem.setEnabled(mode);
		m_requiredElemItem.setEnabled(mode);
	}

	public void setEnabledJDFResourcePool(final boolean mode)
	{
		m_insertInResLinkItem.setEnabled(mode);
		m_insertOutResLinkItem.setEnabled(mode);
		
		m_insertInResItem.setEnabled(!mode);
		m_insertOutResItem.setEnabled(!mode);
		m_insertResItem.setEnabled(!mode);
	}

	public void setEnabled2(final boolean mode)
	{
		m_insertElemAfterItem.setEnabled(mode);
		m_insertElemBeforeItem.setEnabled(mode);
	}

	public void setEnabled3(final boolean mode)
	{
		m_insertElementMenu.setEnabled(mode);
		m_resourceMenu.setEnabled(mode);
		m_requiredElemItem.setEnabled(mode);
	}

	public void setEnabled4(final boolean mode)
	{
		m_insertElementMenu.setEnabled(mode);
		m_resourceMenu.setEnabled(mode);
		m_resourceLinkMenu.setEnabled(mode);
		m_requiredElemItem.setEnabled(mode);
	}

}
