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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.tools.jdfeditor.EditorDocument;
import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.JDFTreeArea;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class MenuView implements MenuInterface, ActionListener
{
	private MainController mainController;

	private JMenu menu;

	private JRadioButtonMenuItem highlightFNRadioItem;
	private JRadioButtonMenuItem showAttrRadioItem;
	private JRadioButtonMenuItem showInhAttrRadioItem;
	private JRadioButtonMenuItem dispDefAttrRadioItem;


	public MenuView(final MainController mainController)
	{
		this.mainController = mainController;
	}

	@Override
	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		menu = new JMenu(ResourceUtil.getMessage("main.menu.view"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);
		
		highlightFNRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.view.highlight"), mainController.getSetting(SettingKey.VALIDATION_HIGHTLIGHT_FN, Boolean.class));
		highlightFNRadioItem.addActionListener(this);
		menu.add(highlightFNRadioItem);
		menu.add(new JSeparator());
		
		showAttrRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.view.display.attributes"), mainController.getSetting(SettingKey.TREEVIEW_ATTRIBUTE, Boolean.class));
		showAttrRadioItem.addActionListener(this);
		menu.add(showAttrRadioItem);

		showInhAttrRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.view.display.attributes.inherited"), mainController.getSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, Boolean.class));
		showInhAttrRadioItem.addActionListener(this);
		menu.add(showInhAttrRadioItem);

		dispDefAttrRadioItem = new JRadioButtonMenuItem(ResourceUtil.getMessage("main.menu.view.display.attributes.default"), mainController.getSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, Boolean.class));
		dispDefAttrRadioItem.addActionListener(this);
		menu.add(dispDefAttrRadioItem);
		
		return menu;
	}

	@Override
	public void setEnableClose()
	{
	}

	@Override
	public void setEnableOpen(boolean mode)
	{
	}
	
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();
		
		if (source == highlightFNRadioItem)
		{
			mainController.setSetting(SettingKey.VALIDATION_HIGHTLIGHT_FN, highlightFNRadioItem.isSelected());
		} else if (source == showAttrRadioItem)
		{
			toggleAttributes();
		} else if (source == showInhAttrRadioItem)
		{
			mainController.setSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, showInhAttrRadioItem.isSelected());
			if (getJDFDoc() != null)
			{
				MainView.getFrame().getJDFTreeArea().drawTreeView(getEditorDoc());
			}
		} else if (source == dispDefAttrRadioItem)
		{
			mainController.setSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, dispDefAttrRadioItem.isSelected());
			if (getJDFDoc() != null)
			{
				MainView.getFrame().getJDFTreeArea().drawTreeView(getEditorDoc());
			}
		}
	}
	
	private void toggleAttributes()
	{
		if (!showAttrRadioItem.isSelected())
		{
			showInhAttrRadioItem.setSelected(false);
			showInhAttrRadioItem.setEnabled(false);
			dispDefAttrRadioItem.setSelected(false);
			dispDefAttrRadioItem.setEnabled(false);
		}
		else
		{
			showInhAttrRadioItem.setEnabled(true);
			dispDefAttrRadioItem.setEnabled(true);
		}

		mainController.setSetting(SettingKey.TREEVIEW_ATTRIBUTE, showAttrRadioItem.isSelected());
		mainController.setSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, dispDefAttrRadioItem.isSelected());
		mainController.setSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, showInhAttrRadioItem.isSelected());
		if (getJDFDoc() != null)
		{
			MainView.getFrame().getJDFTreeArea().drawTreeView(getEditorDoc());
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

}
