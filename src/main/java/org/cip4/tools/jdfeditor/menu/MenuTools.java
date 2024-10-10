/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2022 The International Cooperation for the Integration of
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
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.elementwalker.RemoveCompare;
import org.cip4.jdflib.elementwalker.URLExtractor;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.EditorDocument;
import org.cip4.tools.jdfeditor.EditorMenuBar;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.PreferenceDialog;
import org.cip4.tools.jdfeditor.SendToDevice;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.util.FontUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;
import org.cip4.tools.jdfeditor.view.renderer.JDFTreeNode;

public class MenuTools implements ActionListener, MenuInterface
{
	private static final Log LOG = LogFactory.getLog(MenuTools.class);

	private JMenu menu;

	private JMenuItem spawnItem;
	private JMenuItem spawnInformItem;
	private JMenuItem mergeItem;
	private JMenuItem unspawnItem;
	private JMenuItem preferenceItem;
	private JMenuItem sendToDeviceItem;
	public JMenuItem m_fixVersionItem;
	private JMenuItem m_fixCleanupItem;
	private JMenuItem removeIDItem;
	private JMenuItem m_removeExtenisionItem;
	private JMenuItem extractItem;

	public MenuTools(final MainController mainController)
	{
	}

	@Override
	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		menu = new JMenu(ResourceUtil.getMessage("main.menu.tools"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);

		spawnItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.spawn"));
		spawnItem.addActionListener(this);
		spawnItem.setEnabled(false);
		spawnItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
		menu.add(spawnItem);

		spawnInformItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.spawn.info"));
		spawnInformItem.addActionListener(this);
		spawnInformItem.setAccelerator(KeyStroke.getKeyStroke('I', InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
		spawnInformItem.setEnabled(false);
		menu.add(spawnInformItem);

		mergeItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.merge"));
		mergeItem.addActionListener(this);
		mergeItem.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
		mergeItem.setEnabled(false);
		menu.add(mergeItem);

		unspawnItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.spawn.undo"));
		unspawnItem.addActionListener(this);
		unspawnItem.setEnabled(false);
		menu.add(unspawnItem);

		menu.add(new JSeparator());

		preferenceItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.preferences"));
		preferenceItem.addActionListener(this);
		menu.add(preferenceItem);

		menu.add(new JSeparator());

		sendToDeviceItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.send"));
		sendToDeviceItem.addActionListener(this);
		menu.add(sendToDeviceItem);

		menu.add(new JSeparator());

		m_fixVersionItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.fix"));
		m_fixVersionItem.addActionListener(frame);
		m_fixVersionItem.setEnabled(true);
		menu.add(m_fixVersionItem);

		m_fixCleanupItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.clean"));
		m_fixCleanupItem.addActionListener(this);
		m_fixCleanupItem.setEnabled(true);
		menu.add(m_fixCleanupItem);

		m_removeExtenisionItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.remove"));
		m_removeExtenisionItem.addActionListener(this);
		m_removeExtenisionItem.setEnabled(true);
		menu.add(m_removeExtenisionItem);

		extractItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.extract"));
		extractItem.addActionListener(this);
		extractItem.setEnabled(true);
		menu.add(extractItem);

		removeIDItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.removeid"));
		removeIDItem.addActionListener(this);
		removeIDItem.setEnabled(true);

		menu.add(removeIDItem);

		return menu;
	}

	@Override
	public void setEnableClose()
	{
		sendToDeviceItem.setEnabled(false);
		extractItem.setEnabled(false);
		removeIDItem.setEnabled(false);
	}

	@Override
	public void setEnableOpen(final boolean mode)
	{
		sendToDeviceItem.setEnabled(true);
		extractItem.setEnabled(true);
		removeIDItem.setEnabled(true);
	}

	/**
	 * enable or disable spawn n merge in bulk
	 * 
	 * @param enable
	 */
	public void setSpawnMergeEnabled(final boolean enable)
	{
		spawnItem.setEnabled(enable);
		spawnInformItem.setEnabled(enable);
		mergeItem.setEnabled(enable);
		unspawnItem.setEnabled(enable);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object eSrc = e.getSource();
		if (eSrc == spawnItem)
		{
			MainView.getModel().spawn(false);
		}
		else if (eSrc == spawnInformItem)
		{
			MainView.getModel().spawn(true);
		}
		else if (eSrc == mergeItem)
		{
			MainView.getModel().merge();
		}
		else if (eSrc == sendToDeviceItem)
		{
			new SendToDevice().trySend();
		}
		else if (eSrc == preferenceItem)
		{
			showPreferences();
		}
		else if (eSrc == extractItem)
		{
			extractAll();
		}
		else if (eSrc == removeIDItem)
		{
			removeIDs();
		}
		else if (eSrc == m_fixCleanupItem)
		{
			MainView.getFrame().cleanupSelected();
		}
		else if (eSrc == m_removeExtenisionItem)
		{
			MainView.getFrame().removeExtensionsfromSelected();
		}
	}

	void removeIDs()
	{
		final EditorDocument ed = MainView.getEditorDoc();
		if (ed == null)
		{
			return;
		}
		final JDFFrame frame = MainView.getFrame();
		try
		{
			final JDFTreeNode node = (JDFTreeNode) ed.getSelectionPath().getLastPathComponent();
			final KElement selectedNode = node.getElement();
			RemoveCompare removeCompare = new RemoveCompare();
			removeCompare.setStandard();
			removeCompare.cleanup(selectedNode);
			String fn = ed.getOriginalFileName();
			if (!"cleanid".equals(StringUtil.token(fn, -2, JDFConstants.DOT)) && !"cleanid".equals(UrlUtil.prefix(fn)))
			{
				fn = UrlUtil.newExtension(fn, "cleanid." + UrlUtil.extension(fn));
			}
			ed.saveFile(new File(fn));
		}
		catch (final Exception e)
		{
			JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage("SpawnErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}

	}

	void showPreferences()
	{
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };
		final PreferenceDialog pd = new PreferenceDialog();

		final int option = JOptionPane.showOptionDialog(menu, pd, ResourceUtil.getMessage("PreferenceKey"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
				options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			pd.writeToIni();

			FontUtil.calcFontSize();
			if (getEditorDoc() != null)
			{
				MainView.getFrame().getJDFTreeArea().updateCellRenderer();
				MainView.getFrame().getJDFTreeArea().drawTreeView(getEditorDoc());

				MainView.getFrame().getTopTabs().getInOutScrollPane().initInOutView(getEditorDoc());

				MainView.getFrame().getBottomTabs().updateCheckJDFFontSize();
				MainView.getFrame().getBottomTabs().updateSchemaFontSize();

				MainView.getFrame().getBottomTabs().updateXmlEditorFontSize();
			}
		}
	}

	private void extractAll()
	{
		final EditorDocument editorDocument = getEditorDoc();

		final String documentFileName = editorDocument == null ? null : StringUtil.getNonEmpty(editorDocument.getSaveFileName());
		if (documentFileName != null)
		{
			final String initialDirectoryToExtract = UrlUtil.newExtension(documentFileName, null); // convert file name to directory by removing extension

			final File targetDirectory = FileUtil.getFileInDirectory(new File(initialDirectoryToExtract), new File("extracted"));
			String canonicalPath = "";
			try
			{
				canonicalPath = targetDirectory.getCanonicalPath();
			}
			catch (final IOException e)
			{
				LOG.error("Error: " + e.getMessage(), e);
				return;
			}

			if (!targetDirectory.exists())
			{
				final boolean result = targetDirectory.mkdirs();
				if (!result)
				{
					final String errorMessage = String.format(ResourceUtil.getMessage("main.menu.tools.extract.cannot.create.output.dir"), canonicalPath);
					LOG.error(errorMessage);
					JOptionPane.showMessageDialog(MainView.getFrame(), errorMessage, ResourceUtil.getMessage("main.menu.tools.extract.error"), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			if (!Files.isWritable(targetDirectory.toPath()))
			{
				final String errorMessage = String.format(ResourceUtil.getMessage("main.menu.tools.extract.directory.read.only"), canonicalPath);
				LOG.error(errorMessage);
				JOptionPane.showMessageDialog(MainView.getFrame(), errorMessage, ResourceUtil.getMessage("main.menu.tools.extract.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			MainView.setCursor(1, null);
			final URLExtractor extractor = new URLExtractor(targetDirectory, null, null);
			extractor.setWantLog(true);
			extractor.convert(getJDFDoc().getRoot());
			MainView.setCursor(0, null);

			final StringBuilder sb = new StringBuilder();
			if (!extractor.getSaved().isEmpty())
			{
				sb.append(ResourceUtil.getMessage("main.menu.tools.extract.files.successfully.extracted"));
				for (final String fileName : extractor.getSaved())
				{
					sb.append(fileName + "\n");
				}
				sb.append("\n");
			}

			// if (!extractor.getNotSaved().isEmpty()) // FIXME: implement this method
			// {
			// sb.append(ResourceUtil.getMessage("main.menu.tools.extract.files.not.extracted"));
			// for (String fileName : extractor.getNotSaved())
			// {
			// sb.append(fileName + "\n");
			// }
			// }

			final String finalExtractedMessage = sb.toString();
			JOptionPane.showMessageDialog(MainView.getFrame(), finalExtractedMessage, ResourceUtil.getMessage("main.menu.tools.extract.results"), JOptionPane.INFORMATION_MESSAGE);
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
