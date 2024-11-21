/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2024 The International Cooperation for the Integration of 
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

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.extensions.XJDFSchemaPrune;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.EditorDocument;
import org.cip4.tools.jdfeditor.EditorFileChooser;
import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.ExportDialog;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.EditorUtils;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class MenuValidate implements ActionListener, MenuInterface
{
	private final MainController mainController;

	private JMenu menu;

	private JMenuItem m_QuickValidateItem;
	public JMenuItem m_copyValidationListItem;
	private JMenuItem m_exportItem;
	private JMenuItem m_pruneXJDFItem;
	private JMenuItem m_pruneSchemaItem;
	private JMenuItem m_pruneValidateItem;

	public JMenuItem m_devCapItem;

	public MenuValidate(final MainController mainController)
	{
		this.mainController = mainController;
	}

	/**
	 * Export to Device Capabilities File
	 */
	void exportToDevCap()
	{
		final JDFFrame frame = MainView.getFrame();
		final JDFDoc doc = EditorDocument.getEditorDoc().getJDFDoc();
		final JDFNode root = doc.getJDFRoot();
		if (root == null)
		{
			EditorUtils.errorBox("RootNotAJDFKey", doc.getRoot().getNodeName());
			return;
		}
		try
		{
			frame.cleanupSelected(); // remove all defaults etc. so that the generated file remains reasonable
			final ExportDialog exportDialog = new ExportDialog(root);

			final File fileToOpen = exportDialog.getFileToOpen();
			if (fileToOpen != null)
			{
				final VString vs = StringUtil.tokenize(exportDialog.generAttrString, " ", false);
				vs.unify();

				final String s = StringUtil.setvString(vs, " ", null, null);
				mainController.setSetting(SettingKey.VALIDATION_GENERIC_ATTR, s);
				frame.clearViews();
				frame.readFile(fileToOpen);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame,
					ResourceUtil.getMessage("DevcapExportErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		menu = new JMenu(ResourceUtil.getMessage("main.menu.tools.validate"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);

		m_QuickValidateItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.validate"));
		m_QuickValidateItem.addActionListener(this);
		m_QuickValidateItem.setEnabled(false);
		m_QuickValidateItem.setAccelerator(KeyStroke.getKeyStroke('A', menuKeyMask));
		menu.add(m_QuickValidateItem);

		menu.add(new JSeparator());

		m_copyValidationListItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.copy"));
		m_copyValidationListItem.addActionListener(frame);
		m_copyValidationListItem.setEnabled(false);
		menu.add(m_copyValidationListItem);

		m_exportItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.export"));
		m_exportItem.addActionListener(this);
		m_exportItem.setAccelerator(KeyStroke.getKeyStroke('E', menuKeyMask));
		menu.add(m_exportItem);

		m_pruneXJDFItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.prune"));
		m_pruneXJDFItem.addActionListener(this);
		menu.add(m_pruneXJDFItem);

		m_pruneSchemaItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.pruneschema"));
		m_pruneSchemaItem.addActionListener(this);
		menu.add(m_pruneSchemaItem);

		m_pruneValidateItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.prunevalidate"));
		m_pruneValidateItem.addActionListener(this);
		menu.add(m_pruneValidateItem);

		m_devCapItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.validate.test"));
		m_devCapItem.addActionListener(frame);
		m_devCapItem.setAccelerator(KeyStroke.getKeyStroke('D', menuKeyMask));
		m_devCapItem.setEnabled(false);
		menu.add(m_devCapItem);

		return menu;
	}

	@Override
	public void setEnableClose()
	{
		m_devCapItem.setEnabled(false);
		m_exportItem.setEnabled(false);
		m_QuickValidateItem.setEnabled(false);
		m_pruneXJDFItem.setEnabled(false);
		m_pruneValidateItem.setEnabled(false);
	}

	@Override
	public void setEnableOpen(final boolean mode)
	{
		m_devCapItem.setEnabled(true);
		m_exportItem.setEnabled(true);
		m_QuickValidateItem.setEnabled(MainView.getModel() != null);
		final boolean ok = EditorDocument.getEditorDoc().isXJDF() && !EditorDocument.getEditorDoc().isJson();
		m_pruneXJDFItem.setEnabled(ok);
		m_pruneValidateItem.setEnabled(ok && getPrunedSchema() != null);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);
		final Object eSrc = e.getSource();

		if (eSrc == m_QuickValidateItem && MainView.getModel() != null)
		{
			MainView.getModel().validate();
		}
		else if (eSrc == m_exportItem)
		{
			exportToDevCap();
		}
		else if (eSrc == m_pruneXJDFItem)
		{
			pruneSchema();
		}
		else if (eSrc == m_pruneSchemaItem)
		{
			openPrunedSchema();
		}
		else if (eSrc == m_pruneValidateItem)
		{
			MainView.getModel().validateXJDF(getPrunedSchema());
		}
		MainView.setCursor(0, null);
	}

	void pruneSchema()
	{
		final EditorDocument editorDoc = EditorDocument.getEditorDoc();
		final XMLDoc schema = XMLDoc.parseURL(EditorDocument.getXJDFSchemaUrl(), null);
		final JDFDoc doc = editorDoc.getJDFDoc();
		final XJDFSchemaPrune prune = new XJDFSchemaPrune(schema);
		prune.setCheckAttributes(true);
		final KElement pruned = prune.prune(doc.getRoot());
		final String fileName = editorDoc.getOriginalFileName() + ".xsd";
		JDFTreeModel.saveElement(pruned, fileName, false);
	}

	/**
	 * Choose which file to open.
	 */
	public void openPrunedSchema()
	{
		final String prunedSchema = getPrunedSchema();
		File fileToOpen = UrlUtil.urlToFile(prunedSchema);
		final EditorFileChooser chooser = new EditorFileChooser(fileToOpen, "xsd");

		final int answer = chooser.showOpenDialog(JDFFrame.getFrame());

		if (answer == JFileChooser.APPROVE_OPTION)
		{
			fileToOpen = chooser.getSelectedFile();
			SettingService.getSettingService().set(SettingKey.VALIDATION_PRUNE_SCHEMA_URL, UrlUtil.fileToUrl(fileToOpen, false));
		}
	}

	String getPrunedSchema()
	{
		return SettingService.getSettingService().getString(SettingKey.VALIDATION_PRUNE_SCHEMA_URL);
	}

}
