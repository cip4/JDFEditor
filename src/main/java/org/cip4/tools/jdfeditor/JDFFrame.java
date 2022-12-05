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
package org.cip4.tools.jdfeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.core.XMLDocUserData.EnumDirtyPolicy;
import org.cip4.jdflib.elementwalker.FixVersion;
import org.cip4.jdflib.elementwalker.RemoveEmpty;
import org.cip4.jdflib.goldenticket.BaseGoldenTicket;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.EnumUtil;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.file.UserDir;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.DocumentService;
import org.cip4.tools.jdfeditor.util.RecentFileUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * @author AnderssA ThunellE SvenoniusI Elena Skobchenko
 *
 *         This is the junk dump all in class with gazillions of private members
 *
 *         TODO clean up and move routines to the model
 */

public class JDFFrame extends JFrame implements ActionListener, DropTargetListener, DragSourceListener, DragGestureListener, ClipboardOwner
{
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_TITLE = "CIP4 JDFEditor";
	private static final String UNTITLED = FilenameUtils.concat(new UserDir("JDFEditor").getToolPath(), "Untitled");

	private static final Log LOGGER = LogFactory.getLog(JDFFrame.class);

	private MainController mainController;

	private final DocumentService documentService = new DocumentService();

	private JDFTreeArea m_treeArea;

	public EditorTabbedPaneA m_topTabs;

	private EditorTabbedPaneB m_bottomTabs;

	JTree m_searchTree;

	// handles all copying and pasting
	public JDFTreeCopyNode m_copyNode;

	// quick & dirty hack for multi doc support
	Vector<EditorDocument> m_VjdfDocument = new Vector<EditorDocument>();
	int m_DocPos = -1; // document position

	public EditorMenuBar m_menuBar = null;
	public EditorButtonBar m_buttonBar = null;

	/**
	 * undo and redo support
	 */
	final MyUndoManager undomanager = new MyUndoManager();
	final UndoableEditSupport undoSupport = new UndoableEditSupport();
	public UndoAction undoAction = new UndoAction();
	public RedoAction redoAction = new RedoAction();

	/**
	 * constructor of the frame
	 *
	 */
	public JDFFrame()
	{
		super("CIP4 JDF Editor");
		enableOSXFullscreen(this);

		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, d.width, d.height - 30);
		m_menuBar = new EditorMenuBar();
		m_bottomTabs = new EditorTabbedPaneB();
		setExtendedState(MAXIMIZED_BOTH);
	}

	/**
	 * Register a MainController for this view (MVC Pattern)
	 * 
	 * @param mainController The MainController for this view.
	 */
	public void registerController(final MainController mainController)
	{
		this.mainController = mainController;
		m_menuBar.registerController(mainController);
	}

	/**
	 * Enables JDFEditor to run in full screen mode on a MacOSX System.
	 * 
	 * @param window This window
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void enableOSXFullscreen(final Window window)
	{
		try
		{
			final Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
			final Class params[] = new Class[] { Window.class, Boolean.TYPE };
			final Method method = util.getMethod("setWindowCanFullScreen", params);
			method.invoke(util, window, true);
		}
		catch (final ClassNotFoundException e)
		{
			LOGGER.debug("Exception: ", e);
		}
		catch (final Exception e)
		{
			LOGGER.debug("Exception: ", e);
		}
	}

	/**
	 * Method drawWindow.
	 */
	public void drawWindow()
	{
		MainView.setCursor(1, null);
		setJMenuBar(m_menuBar.drawMenu());
		this.getContentPane().add(drawBoxContent());
		this.setEnableClose();
		this.setVisible(true);
		MainView.setCursor(0, null);
	}

	/**
	 * Method drawBoxContent.
	 * 
	 * @return Box
	 */
	private Box drawBoxContent()
	{
		final Box box_splitPane = Box.createHorizontalBox();
		box_splitPane.add(drawSplitPane());

		final Box box_content = Box.createVerticalBox();
		m_buttonBar = new EditorButtonBar(this);
		m_buttonBar.drawButtonBar();
		box_content.add(m_buttonBar);
		box_content.add(box_splitPane);

		return box_content;
	}

	/**
	 * Draws the splitpanes in which the views are to be displayed
	 * 
	 * @return JSplitPane
	 */
	private JSplitPane drawSplitPane()
	{
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		m_topTabs = new EditorTabbedPaneA();
		m_bottomTabs = new EditorTabbedPaneB();

		m_treeArea = new JDFTreeArea(this);
		new DropTarget(m_treeArea, this);
		m_treeArea.setToolTipText(ResourceUtil.getMessage("TreeViewKey"));

		undomanager.setLimit(100);
		m_treeArea.getDocument().addUndoableEditListener(undomanager);
		undoSupport.addUndoableEditListener(undomanager);

		final JSplitPane attAndErrorPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_topTabs, getBottomTabs());
		attAndErrorPane.setDividerLocation(d.height / 2);
		attAndErrorPane.setResizeWeight(1);

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_treeArea.getScrollPane(), attAndErrorPane);
		splitPane.setDividerLocation(d.width / 4);

		return splitPane;
	}

	/**
	 * Choose which file to open.
	 */
	public void openFile()
	{
		File fileToSave = null; // Do this for GoldenTicket (?)
		if (getJDFDoc() != null)
		{
			final String originalFileName = getJDFDoc().getOriginalFileName();
			if (originalFileName != null)
			{
				fileToSave = new File(originalFileName);
			}
		}
		else
		{
			final String recentFile = RecentFileUtil.getRecentFiles()[0];
			if (recentFile != null)
			{
				fileToSave = new File(recentFile);
			}
		}

		final EditorFileChooser chooser = new EditorFileChooser(fileToSave, EditorFileChooser.allFilesOpen);
		final int answer = chooser.showOpenDialog(this);

		if (answer == JFileChooser.APPROVE_OPTION)
		{
			fileToSave = chooser.getSelectedFile(); // Do this GoldenTicket (?)
			readFile(fileToSave); // Do this as well for GoldenTicket (?)
		}
	}

	public void applyLookAndFeel(final PreferenceDialog pd)
	{
		try
		{
			UIManager.setLookAndFeel(mainController.getSetting(SettingKey.GENERAL_LOOK, String.class));
			m_buttonBar.removeAll();
			m_buttonBar.drawButtonBar();
			SwingUtilities.updateComponentTreeUI(JDFFrame.this);

			if (pd != null)
			{
				SwingUtilities.updateComponentTreeUI(pd);
			}
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("LookAndFeelErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		catch (final InstantiationException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("LookAndFeelErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		catch (final IllegalAccessException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("LookAndFeelErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		catch (final UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("LookAndFeelErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void printWhat()
	{
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };

		final ComponentChooser cc = new ComponentChooser();
		final int option = JOptionPane.showOptionDialog(this, cc, ResourceUtil.getMessage("PrintMessKey"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
				options[0]);

		final EditorDocument ed = getEditorDoc();
		if (option == JOptionPane.OK_OPTION)
		{
			Component comp = null;

			if (cc.getComponent().equals(ResourceUtil.getMessage("ProcessViewKey")))
			{
				comp = m_topTabs.m_pArea;
			}
			else if (cc.getComponent().equals(ResourceUtil.getMessage("NextNeighbourKey")))
			{
				comp = m_topTabs.m_inOutScrollPane.m_inOutArea;
			}
			else if (cc.getComponent().equals(ResourceUtil.getMessage("TreeViewKey")))
			{
				comp = ed.getJDFTree();
			}

			PrintDialog.printIt(comp);
		}
	}

	/**
	 * Export to Device Capabilities File
	 */
	void exportToDevCap()
	{
		final JDFNode root = getJDFDoc().getJDFRoot();
		if (root == null)
		{
			EditorUtils.errorBox("RootNotAJDFKey", getJDFDoc().getRoot().getNodeName());
			return;
		}
		try
		{
			cleanupSelected(); // remove all defaults etc. so that the generated file remains reasonable
			final ExportDialog exportDialog = new ExportDialog(root);

			final File fileToOpen = exportDialog.getFileToOpen();
			if (fileToOpen != null)
			{
				final VString vs = StringUtil.tokenize(exportDialog.generAttrString, " ", false);
				vs.unify();

				final String s = StringUtil.setvString(vs, " ", null, null);
				mainController.setSetting(SettingKey.VALIDATION_GENERIC_ATTR, s);
				clearViews();
				readFile(fileToOpen);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					ResourceUtil.getMessage("DevcapExportErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openDeviceCapFile()
	{
		try
		{
			final JDFDoc nodeDoc = getJDFDoc();
			if (nodeDoc != null)
			{
				final DeviceCapDialog testResult = new DeviceCapDialog(nodeDoc);
				final XMLDoc bugReport = testResult.getBugReport();
				final VElement executNodes = testResult.getExecutable();

				getBottomTabs().m_devCapErrScroll.drawDevCapOutputTree(bugReport, executNodes);

				getBottomTabs().setSelectedIndex(getBottomTabs().m_DC_ERRORS_INDEX);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					ResourceUtil.getMessage("DevcapOpenErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method readFile.
	 * 
	 * @param fileToRead the file to read
	 * @return true if at least one file was read or selected
	 */
	public boolean readFile(final File fileToRead)
	{
		EditorDocument eDocs[] = null;

		if (fileToRead != null)
		{
			final int docIndex = documentService.indexOfFile(fileToRead, m_VjdfDocument);
			if (docIndex >= 0)
			{
				nextFile(docIndex);
				return true;
			}
			else if (fileToRead.exists())
			{
				eDocs = EditorUtils.getEditorDocuments(fileToRead);
				if (eDocs == null)
				{
					EditorUtils.errorBox("FileNotOpenKey", ": " + fileToRead.getName() + "!");
					MainView.setCursor(0, MainView.getFrame());
				}
				else
				{
					m_menuBar.updateRecentFilesMenu(fileToRead.getAbsolutePath());
					importDocs(eDocs);
				}
			}
		}
		return eDocs != null;
	}

	void importDocs(EditorDocument[] eDocs)
	{
		for (final EditorDocument eDoc : eDocs)
		{
			refreshView(eDoc, null);
			final JDFDoc doc = eDoc.getJDFDoc();
			if (doc != null)
			{
				doc.clearDirtyIDs();
			}
		}
	}

	public void refreshView(EditorDocument eDoc, TreePath path)
	{
		if (eDoc == null)
		{
			eDoc = getEditorDoc();
		}
		if (eDoc == null)
		{
			return;
		}
		if (path == null)
		{
			path = eDoc.getLastSelection();
		}

		MainView.setCursor(1, null);
		try
		{
			setEnableOpen(!mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class));

			m_treeArea.drawTreeView(eDoc);
			m_topTabs.refreshView(eDoc);
			setTitle(buildWindowTitleString());
			m_treeArea.setHeaderLabel(eDoc.isJson());
			getBottomTabs().refreshView(path);
			getBottomTabs().refreshXmlEditor(eDoc.getString(), eDoc.isJson());
			if (path != null)
			{
				m_treeArea.goToPath(path);
				updateViews(path);
			}
			this.toFront();
		}
		catch (final Exception e)
		{
			setJDFDoc(null, null);
			LOGGER.error("Error during refreshing View.", e);
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("FileNotOpenKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			MainView.setCursor(0, null);
		}
	}

	public void refreshTitle()
	{
		setTitle(buildWindowTitleString());
	}

	/**
	 * Build application title string for currently displayed file.
	 */
	private String buildWindowTitleString()
	{
		final EditorDocument editorDoc = getEditorDoc();
		if (editorDoc == null)
		{
			return DEFAULT_TITLE;
		}
		String title = DEFAULT_TITLE + ": ";
		if (editorDoc.isDirtyFlag())
		{
			title += "* ";
		}
		title += editorDoc.getSaveFileName();

		final String packageName = editorDoc.getPackageName();
		if (packageName != null)
		{
			title += " (" + editorDoc.getOriginalFileName() + ")";
		}
		return title;
	}

	/**
	 * Reload the currently opened file.
	 */
	public void refresh()
	{
		final EditorDocument editorDoc = getEditorDoc();
		if (editorDoc == null)
		{
			return;
		}
		if (editorDoc.getPackageName() != null)
		{
			return;
		}

		if (editorDoc.getJDFDoc() != null)
		{
			final String originalFileName = getJDFDoc().getOriginalFileName();
			setJDFDoc(null, null);
			readFile(new File(originalFileName));
		}
	}

	/**
	 * Save As-chooser.
	 */
	public void saveAs()
	{
		final EditorDocument editorDoc = getEditorDoc();
		if (editorDoc == null)
		{
			return;
		}
		final String fileName = editorDoc.getSaveFileName();

		final File fileToSave = new File(fileName);
		final EditorFileChooser saveChooser = new EditorFileChooser(fileToSave, editorDoc.getSaveExtensions());
		final int answer = saveChooser.showSaveDialog(null);

		if (answer == JFileChooser.APPROVE_OPTION)
		{
			final File file = saveChooser.getSelectedFile();
			boolean canSave = !file.exists() || file.equals(fileToSave);
			canSave = canSave || editorDoc.checkSave(file);
			if (canSave)
			{
				if ("rtf".equalsIgnoreCase(FileUtil.getExtension(file)))
				{
					editorDoc.writeToRTF(file);
				}
				else
				{
					editorDoc.saveFile(file);
					editorDoc.resetDirtyFlag();
					setTitle(buildWindowTitleString());
					m_menuBar.updateRecentFilesMenu(fileToSave.toString());
				}
			}
		}
	}

	/**
	 * Get the node to search for in the Process View.
	 * 
	 * @param src - The location in the Process View that's been selected
	 */
	void getProcessSearchNode(final Object src)
	{
		final ProcessPart pp = (ProcessPart) src;
		JDFTreeNode node = null;
		try
		{
			final KElement kElement = pp.getElem();
			node = new JDFTreeNode(kElement);
		}
		catch (final Exception s)
		{
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("FindErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
			s.printStackTrace();
		}
		if (node != null)
		{
			m_treeArea.findNode(node);
		}
	}

	/**
	 * Method clearViews. clear all views before opening a new file
	 */
	void clearViews()
	{
		m_topTabs.clearViews();
		m_bottomTabs.clearViews();
	}

	/**
	 * Ask user if he wants to save an unsaved file before closing.
	 */
	private int saveFileQuestion()
	{
		LOGGER.info("saveFileQuestion asking...");
		int save = JOptionPane.YES_OPTION;
		final EditorDocument doc = getEditorDoc();
		if (doc != null)
		{
			// if (!mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class) || !isDirty())
			// {
			String originalFileName = doc.getOriginalFileName();
			if (originalFileName == null)
			{
				originalFileName = UNTITLED;
			}

			final String question = ResourceUtil.getMessage("SaveQuestionKey") + "\n" + '"' + originalFileName + '"';
			save = JOptionPane.showConfirmDialog(this, question, "", JOptionPane.YES_NO_CANCEL_OPTION);
			// }
		}
		return save;
	}

	/**
	 * Creates a new JDF document.
	 */
	void newJDF()
	{
		clearViews();
		final JDFDoc jdfDoc = new JDFDoc("JDF");
		final JDFNode jdfRoot = jdfDoc.getJDFRoot();
		jdfRoot.setType("Product", true);
		final int position = setJDFDoc(jdfDoc, null);
		LOGGER.info("New doc created with position: " + position);

		final EditorDocument document = getEditorDoc();
		document.setDirtyFlag();

		jdfDoc.setOriginalFileName(EditorUtils.getNewPath(UNTITLED + ".jdf"));
	}

	/**
	 * Method newJMF. creates a new JMF file
	 * 
	 * @param f
	 * @param type
	 */
	private void newJMF(final EnumFamily f, String type)
	{
		clearViews();
		try
		{
			final JDFJMF jmf = Editor.getEditor().getJMFBuilder().newJMF(f, type);
			final VString requiredAttributes = jmf.getMissingAttributes(9999999);

			for (int i = 0; i < requiredAttributes.size(); i++)
			{
				final String s = JDFElement.getValueForNewAttribute(jmf, requiredAttributes.get(i));
				if (!jmf.hasAttribute(requiredAttributes.get(i)))
				{
					jmf.setAttribute(requiredAttributes.get(i), s);
				}
			}
			if (type == null)
			{
				type = UNTITLED;
			}

			final JDFDoc jmfDoc = jmf.getOwnerDocument_JDFElement();
			setJDFDoc(jmfDoc, null);
			jmfDoc.setOriginalFileName(EditorUtils.getNewPath(type + ".jmf"));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("FileNotOpenKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method newGoldenTicket. creates a new JDF file from an existing Golden Ticket.java file.
	 */
	private void newGoldenTicket(final EnumVersion jdfVersion)
	{
		final String gtselect = "";

		try
		{
			final GoldenTicketDialog gtDialog = new GoldenTicketDialog();

			final BaseGoldenTicket theGT = gtDialog.getGoldenTicket(jdfVersion);

			if (theGT != null)
			{
				final JDFNode root = theGT.getNode();
				final JDFDoc doc = root.getOwnerDocument_JDFElement();
				setJDFDoc(doc, null);
				doc.setOriginalFileName(EditorUtils.getNewPath(gtselect + "_GoldenTicket.jdf"));
			}
		}
		catch (final Exception e)
		{
			LOGGER.error("snafu converting", e);
			JOptionPane.showMessageDialog(this,
					ResourceUtil.getMessage("DevcapOpenErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Close the current file.
	 */
	public int closeFile(final int nMax)
	{
		LOGGER.info("Closing file with position: " + nMax);
		int save = JOptionPane.YES_OPTION;
		int n = 0;
		EditorDocument doc = getEditorDoc();
		while (doc != null)
		{
			if (n++ >= nMax)
			{
				break;
			}

			if (doc.isDirtyFlag() && !mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class))
			{
				save = saveFileQuestion();
			}
			else
			{
				save = JOptionPane.NO_OPTION;
			}

			if (save != JOptionPane.CANCEL_OPTION)
			{
				final String originalFileName = doc.getOriginalFileName();
				if (originalFileName != null && !originalFileName.startsWith(UNTITLED))
				{
					m_menuBar.updateRecentFilesMenu(originalFileName);
				}

				if (save == JOptionPane.YES_OPTION)
				{
					if (doc.isDirtyFlag() && !mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class) && !(new File(originalFileName).exists()))
					{
						saveAs();
					}
					else
					{
						doc.saveFile(null);
					}
					setJDFDoc(null, null); // close current document
				}
				else
				{
					setJDFDoc(null, null); // no button clicked - just close
				}
			}
			else
			{
				break; // cancel button
			}

			doc = getEditorDoc();
			if (doc != null)
			{
				refreshView(doc, null);
			}
			else
			{
				final JTextArea textArea = new JTextArea();
				textArea.setEditable(false);
				new DropTarget(textArea, this);

				if (!mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class))
				{
					setEnableClose();
				}

				m_treeArea.m_treeView.setView(textArea);
				clearViews();
				m_topTabs.setSelectedIndex(m_topTabs.m_IO_INDEX);
			}
		}
		setTitle(buildWindowTitleString());
		return save;
	}

	/**
	 * Asks if the user wants to create a new JDF or JMF file. BMI: Also where can select Golden Ticket
	 *
	 * Coming from EditorMenuBar, second stop in the chain. called from frame.NewFile()
	 */
	public void newFile()
	{
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };

		final NewFileChooser newFileChooser = new NewFileChooser();

		final int option = JOptionPane.showOptionDialog(this, newFileChooser, ResourceUtil.getMessage("ChooseNewFileKey"), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			final String selection = newFileChooser.getSelection();
			final EnumVersion v = newFileChooser.getVersionSelected();
			if (selection.equals("JDF"))
			{
				newJDF();
			}
			else if (selection.equals("JMF"))
			{
				newJMF(EnumFamily.Query, "KnownMessages");
			}
			else
			{
				newGoldenTicket(v);
			}
			if (EnumUtil.aLessEqualsThanB(EnumVersion.Version_2_0, v))
			{
				MainView.getEditorDoc().createModel();
				MainView.getModel().saveAsXJDF(null, false);
			}
			else
			{
				new FixVersion(v).convert(getEditorDoc().getJDFDoc().getRoot());
			}
			MainView.getFrame().refreshView(MainView.getFrame().getEditorDoc(), null);
			setEnableOpen(!mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class));
		}
	}

	/**
	 * fixes the version of a JDF by calling fixVersion for the selected JDF node ore the closest JDF parent.
	 *
	 */
	public void fixVersion()
	{
		final JDFDoc doc = getJDFDoc();
		if (doc == null)
		{
			return;
		}

		final FixVersionDialog dialog = new FixVersionDialog();
		final TreePath path = m_treeArea.getSelectionPath();
		dialog.fixIt(path);
	}

	/**
	 * fixes the version of a JDF by calling fixVersion for the selected JDF node ore the closest JDF parent.
	 *
	 */
	public void cleanupSelected()
	{
		final JDFDoc doc = getJDFDoc();
		if (doc == null)
		{
			return;
		}

		try
		{

			// find the closest selected JDF or JMF element and fix it
			final TreePath path = m_treeArea.getSelectionPath();
			final KElement element = EditorUtils.getElement(path);
			if (element != null)
			{
				final JDFNode n1 = (JDFNode) element.getDeepParent(ElementName.JDF, 0);

				final RemoveEmpty rem = new RemoveEmpty();
				rem.setZappElements(true);
				if (n1 != null)
				{
					n1.eraseUnlinkedResources();
					n1.eraseDefaultAttributes(true);
					rem.removEmpty(n1);
				}
				else
				{
					if (element instanceof JDFElement)
						((JDFElement) element).eraseDefaultAttributes(true);
					rem.walkTreeKidsFirst(element);
				}
			}
			refreshView(getEditorDoc(), path);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					ResourceUtil.getMessage("FixVersionErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * fixes the version of a JDF by calling fixVersion for the selected JDF node ore the closest JDF parent.
	 *
	 */
	public void removeExtensionsfromSelected()
	{
		final JDFDoc doc = getJDFDoc();
		if (doc == null)
		{
			return;
		}

		try
		{

			// find the closest selectd JDF or JMF element and fix it
			final TreePath path = m_treeArea.getSelectionPath();
			final KElement element = EditorUtils.getElement(path);
			if (element instanceof JDFElement)
			{
				final JDFElement n1 = (JDFElement) element;
				n1.removeExtensions();
			}
			refreshView(getEditorDoc(), path);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					ResourceUtil.getMessage("FixVersionErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Creates the SearchDialog.
	 * 
	 * @param searchComponent - Where to perform the search?
	 */
	private void findWhatDialog(final String searchComponent)
	{
		new SearchDialog(searchComponent);
	}

	/**
	 * Mother of all action dispatchers TODO remove and distribute over the relevant classes
	 * 
	 * @param e the event that gets checked
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);

		final Object eSrc = e.getSource();
		if (eSrc == m_menuBar.getMenuValidate().m_exportItem)
		{
			exportToDevCap();
		}
		else if (eSrc == m_menuBar.getMenuFile().m_quitItem)
		{
			if (closeFile(9999) != JOptionPane.CANCEL_OPTION)
			{
				System.exit(0);
			}
		}
		else if (eSrc == m_menuBar.m_infoItem)
		{

		}
		else if (eSrc == m_menuBar.getMenuEdit().m_findItem)
		{
			findIt();
		}
		else if (eSrc == m_buttonBar.m_validateButton)
		{
			getModel().validate();
		}
		else if (eSrc == m_menuBar.getMenuTools().m_fixVersionItem)
		{
			fixVersion();
		}
		else if (eSrc == m_buttonBar.m_upOneLevelButton)
		{
			m_topTabs.m_pArea.goUpOneLevelInProcessView();
		}

		else if (eSrc == m_buttonBar.m_printButton)
		{
			printWhat();
		}
		else if (eSrc == m_menuBar.getMenuValidate().m_devCapItem)
		{
			openDeviceCapFile();
		}
		// copy the results of the validation to the system clip board
		else if (eSrc == m_menuBar.getMenuValidate().m_copyValidationListItem)
		{
			// TODO m_errorTabbedPane.copyValidationListToClipBoard();
		}
		else if (!mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class))
		{
			if (eSrc == m_buttonBar.m_cutButton || eSrc == m_menuBar.getMenuEdit().m_cutItem)
			{
				cutSelectedNode();
			}
			else if (eSrc == m_buttonBar.m_copyButton || eSrc == m_menuBar.getMenuEdit().m_copyItem)
			{
				copySelectedNode();
			}
			else if (eSrc == m_buttonBar.m_pasteButton || eSrc == m_menuBar.m_pastePopupItem || eSrc == m_menuBar.getMenuEdit().m_pasteItem)
			{
				pasteCopiedNode();
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertElemBeforeItem)
			{
				m_treeArea.insertElementAtSelectedNode(-1);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertElemAfterItem)
			{
				m_treeArea.insertElementAtSelectedNode(1);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertElemIntoItem)
			{
				m_treeArea.insertElementAtSelectedNode(0);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertInResItem)
			{
				m_treeArea.insertResourceWithLink(true, true);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertOutResItem)
			{
				m_treeArea.insertResourceWithLink(true, false);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertResItem)
			{
				m_treeArea.insertResourceWithLink(false, false);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertInResLinkItem)
			{
				m_treeArea.insertResourceLink(EnumUsage.Input);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertOutResLinkItem)
			{
				m_treeArea.insertResourceLink(EnumUsage.Output);
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_insertAttrItem)
			{
				m_treeArea.insertAttrItem();
			}
			else if (eSrc == m_menuBar.getMenuEdit().m_renameItem)
			{
				renameSelectedNode();
			}
			else if (eSrc == m_menuBar.getMenuEdit().m_modifyAttrValueItem)
			{
				m_treeArea.modifyAttribute();
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_requiredAttrItem)
			{
				addRequiredAttrsToSelectedNode();
			}
			else if (eSrc == m_menuBar.getMenuInsert().m_requiredElemItem)
			{
				addRequiredElemsToSelectedNode();
			}
			else if (eSrc == m_buttonBar.m_refreshButton)
			{
				refresh();
			}
		}
		MainView.setCursor(0, null);
	}

	public void save()
	{
		File fileToSave = null;

		final EditorDocument doc = getEditorDoc();
		if (doc != null)
		{
			final String originalFileName = doc.getSaveFileName();
			if (originalFileName != null)
			{
				fileToSave = new File(originalFileName);
			}
		}
		if (fileToSave != null)
		{
			if (doc.isDirtyFlag() && !mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class))
			{
				saveAs();
				m_menuBar.updateRecentFilesMenu(fileToSave.toString());
			}
			else
			{
				doc.saveFile(fileToSave);
			}
		}
		else
		{
			EditorUtils.errorBox("FileNotFoundKey", null);
		}
	}

	/**
	 *
	 *
	 * @param pos
	 * @return
	 */
	public EditorDocument nextFile(int pos)
	{
		if (m_VjdfDocument.isEmpty())
		{
			return null;
		}
		if (pos == -1)
		{
			pos = m_DocPos + 1;
		}

		if (pos >= m_VjdfDocument.size())
		{
			pos = 0;
		}

		EditorDocument ed = m_VjdfDocument.elementAt(m_DocPos);
		if (pos == m_DocPos)
		{
			return ed; // nop
		}

		m_menuBar.setWindowMenuItemColor(pos);
		m_DocPos = pos;
		ed = m_VjdfDocument.elementAt(m_DocPos);

		refreshView(ed, null);
		return ed;
	}

	/**
	 * @param path
	 */
	public void updateViews(final TreePath path)
	{
		MainView.setCursor(1, null);
		m_treeArea.repaint();
		final EditorDocument ed = getEditorDoc();
		if (path != null && ed != null)
		{
			final JTree tree = ed.getJDFTree();
			tree.getPreferredSize();
			tree.scrollPathToVisible(path);
			tree.setSelectionPath(path);
		}
		MainView.setCursor(0, null);
	}

	class CutItemEdit extends DeleteItemEdit
	{
		private static final long serialVersionUID = -2778264565816330000L;

		public CutItemEdit(final TreePath treePath)
		{
			super(treePath);
		}

		@Override
		public String getPresentationName()
		{
			return "Cut";
		}
	}

	/**
	 * renames selected node
	 */
	public void renameSelectedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null)
		{
			final JDFTreeNode lastPathComponent = (JDFTreeNode) path.getLastPathComponent();
			final String previousNodeName = lastPathComponent.getName();
			String previousNodeValue = null;
			final String newName = getModel().renameElementsAndAttributes(path);
			if (newName != null)
			{
				if (!lastPathComponent.isElement() && !newName.equals(previousNodeName))
				{
					final KElement e = lastPathComponent.getElement();
					if (e.hasAttribute(newName))
					{
						previousNodeValue = e.getAttribute(newName);
					}
				}
				final RenameNodeEdit edit = new RenameNodeEdit(path, lastPathComponent, newName, previousNodeValue);
				edit.redo();
				undoSupport.postEdit(edit);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Rename operation was not completed." + "\nNo name selected or an error occured", "Rename", JOptionPane.INFORMATION_MESSAGE);
			}
			updateViews(path);
		}
	}

	/**
	 * cuts selected node
	 */
	public void cutSelectedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null)
		{
			MainView.getFrame().getEditorDoc().setDirtyFlag();
			m_copyNode = new JDFTreeCopyNode((JDFTreeNode) path.getLastPathComponent(), false);

			final CutItemEdit edit = new CutItemEdit(path);
			undoSupport.postEdit(edit);
		}
	}

	/**
	 * adds required elements to selected Node
	 */
	public void addRequiredElemsToSelectedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null)
		{
			JDFTreeNode intoNode;
			try
			{
				intoNode = (JDFTreeNode) path.getLastPathComponent();
			}
			catch (final Exception s)
			{
				intoNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
			}

			final Vector<JDFTreeNode> addedVector = getModel().addRequiredElements(intoNode);

			if (addedVector.size() > 0)
			{
				final AddRequiredElemEdit edit = new AddRequiredElemEdit(path, intoNode, addedVector);
				undoSupport.postEdit(edit);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "No missing required elements found", "Add Required Elements", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * adds required attributes to selected Node
	 */
	public void addRequiredAttrsToSelectedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null)
		{
			JDFTreeNode intoNode;
			try
			{
				intoNode = (JDFTreeNode) path.getLastPathComponent();
			}
			catch (final Exception s)
			{
				intoNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
			}

			final Vector<JDFTreeNode> addedVector = getModel().addRequiredAttributes(intoNode);

			if (addedVector.size() > 0)
			{
				final AddRequiredAttrEdit edit = new AddRequiredAttrEdit(path, intoNode, addedVector);
				undoSupport.postEdit(edit);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "No missing required attributes found", "Add Required Attributes", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * copies selected node
	 */
	public void copySelectedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null)
		{
			m_copyNode = new JDFTreeCopyNode((JDFTreeNode) path.getLastPathComponent(), true);
		}
	}

	/**
	 * pastes copied node
	 */
	public void pasteCopiedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null && m_copyNode != null)
		{
			final JDFTreeNode intoNode = (JDFTreeNode) path.getLastPathComponent();
			final JDFTreeNode pasteNode = m_copyNode.pasteNode(path);
			if (pasteNode != null)
			{
				MainView.getFrame().getEditorDoc().setDirtyFlag();
				final PasteItemEdit edit = new PasteItemEdit(path, intoNode, pasteNode);
				undoSupport.postEdit(edit);
			}
		}
	}

	/**
	 * Pastes raw copied node
	 */
	public void pasteRawCopiedNode()
	{
		final TreePath path = m_treeArea.getSelectionPath();
		if (path != null && m_copyNode != null)
		{
			final JDFTreeNode intoNode = (JDFTreeNode) path.getLastPathComponent();
			final JDFTreeNode pasteNode = m_copyNode.pasteRawNode(path);
			if (pasteNode != null)
			{
				final PasteItemEdit edit = new PasteItemEdit(path, intoNode, pasteNode);
				undoSupport.postEdit(edit);
			}
		}
	}

	/**
	 * Determine where the search is to be done
	 */
	private void findIt()
	{
		String findString = "JDFTree";
		final EditorDocument ed = getEditorDoc();
		if (!ed.getJDFTree().isFocusOwner())
		{
			m_searchTree = m_topTabs.m_inOutScrollPane.findIt();
			final boolean found = m_searchTree != null;

			if (found)
			{
				findString = "NeighbourTree";
			}
		}
		// findstring is always set at least to JDFTree
		findWhatDialog(findString);
	}

	/**
	 * returns true if the currently selected document has been modified
	 * 
	 * @deprecated
	 * @return
	 */
	@Deprecated
	boolean isDirty()
	{
		final JDFDoc doc = getJDFDoc();
		if (doc != null)
		{
			final KElement e = doc.getRoot();
			return e.isDirty();
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////
	private class MyTreeSelectionListener implements TreeSelectionListener
	{
		@Override
		public void valueChanged(final TreeSelectionEvent e)
		{
			if (m_treeArea.getSelectionPath() != null)
			{
				final JDFTreeNode node = (JDFTreeNode) m_treeArea.getSelectionPath().getLastPathComponent();
				if (node.isElement())
				{
					final KElement elem = node.getElement();

					if (elem instanceof JDFNode)
					{
						m_menuBar.getMenuTools().setSpawnMergeEnabled(true);
					}
					else
					{
						m_menuBar.getMenuTools().setSpawnMergeEnabled(false);
					}
					m_menuBar.getMenuEdit().m_pasteItem.setEnabled(m_copyNode != null);
					m_buttonBar.m_pasteButton.setEnabled(m_copyNode != null);

				}
				else
				{
					m_menuBar.getMenuTools().setSpawnMergeEnabled(false);
					m_menuBar.getMenuEdit().m_pasteItem.setEnabled(false);
					m_buttonBar.m_pasteButton.setEnabled(false);
				}

				final int selIndex = m_topTabs.getSelectedIndex();

				if (!mainController.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class))
				{
					m_menuBar.setEnabledInMenu(m_treeArea.getSelectionPath());
				}

				if (selIndex == m_topTabs.m_IO_INDEX)
				{
					if (!m_topTabs.isEnabledAt(m_topTabs.m_COM_INDEX))
					{
						m_topTabs.m_inOutScrollPane.clearInOutView();
						m_topTabs.m_inOutScrollPane.initInOutView(null);
					}
				}
				else if (selIndex == m_topTabs.m_COM_INDEX)
				{
					m_topTabs.showComment();
				}
				else if (selIndex == m_topTabs.m_PROC_INDEX)
				{
					m_topTabs.m_pArea.initProcessView();
				}
			}
		}
	}

	private void setEnableClose()
	{
		m_menuBar.setEnableClose();
		m_buttonBar.setEnableClose();
	}

	public void setEnableOpen(final boolean mode)
	{
		m_menuBar.setEnableOpen(mode);
		m_buttonBar.setEnableOpen(mode);
	}

	class MyUndoManager extends UndoManager
	{
		private static final long serialVersionUID = 626128726824504001L;

		@Override
		public void undoableEditHappened(final UndoableEditEvent e)
		{
			addEdit(e.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}

	class UndoAction extends AbstractAction
	{
		private static final long serialVersionUID = 626128726824504002L;

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			try
			{
				if (undomanager.canUndo())
				{
					undomanager.undo();
				}
			}
			catch (final CannotUndoException ex)
			{
				EditorUtils.errorBox("UnableUndo", ex.getMessage());
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		public void updateUndoState()
		{
			// refresh undo
			m_buttonBar.m_undoButton.setText(undomanager.getUndoPresentationName());
			m_buttonBar.m_undoButton.setEnabled(undomanager.canUndo());

			m_menuBar.getMenuEdit().m_undoItem.setText(undomanager.getUndoPresentationName());
			m_menuBar.getMenuEdit().m_undoItem.setEnabled(undomanager.canUndo());
		}
	}

	class RedoAction extends AbstractAction
	{
		private static final long serialVersionUID = 626128726824504003L;

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			try
			{
				if (undomanager.canRedo())
				{
					undomanager.redo();
				}
			}
			catch (final CannotUndoException ex)
			{
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		public void updateRedoState()
		{
			// refresh redo
			m_buttonBar.m_redoButton.setText(undomanager.getRedoPresentationName());
			m_buttonBar.m_redoButton.setEnabled(undomanager.canRedo());

			m_menuBar.getMenuEdit().m_redoItem.setText(undomanager.getRedoPresentationName());
			m_menuBar.getMenuEdit().m_redoItem.setEnabled(undomanager.canRedo());
		}
	}

	class EditChangeListener implements ChangeListener
	{
		@Override
		public void stateChanged(final ChangeEvent e)
		{
			if (m_treeArea != null)
			{
				m_treeArea.repaint();
			}

			if (getJDFDoc() == null)
			{
				m_buttonBar.m_validateButton.setEnabled(false);
			}
		}
	}

	/**
	 *
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	@Override
	public void dragGestureRecognized(final DragGestureEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	@Override
	public void dragDropEnd(final DragSourceDropEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	@Override
	public void dragEnter(final DragSourceDragEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragExit(final DragSourceEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	@Override
	public void dragOver(final DragSourceDragEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	@Override
	public void dropActionChanged(final DragSourceDragEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragEnter(final DropTargetDragEvent e)
	{
		e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/**
	 *
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(final DropTargetEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(final DropTargetDragEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public synchronized void drop(final DropTargetDropEvent e)
	{
		try
		{
			final Transferable flavor = e.getTransferable();

			if (flavor.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				@SuppressWarnings("unchecked")
				final java.util.List<File> fileList = (java.util.List<File>) flavor.getTransferData(DataFlavor.javaFileListFlavor);
				final Iterator<File> files = fileList.iterator();

				while (files.hasNext())
				{
					readFile(files.next());
				}
				e.getDropTargetContext().dropComplete(true);
			}
			else
			{
				e.rejectDrop();
			}
		}
		catch (final IOException ioe)
		{
			LOGGER.warn("data no longer available in the requested flavor");
			e.rejectDrop();
		}
		catch (final UnsupportedFlavorException ufe)
		{
			LOGGER.error("data flavor not supported", ufe);
			e.rejectDrop();
		}
	}

	/**
	 *
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(final DropTargetDragEvent e)
	{
	}

	/**
	 *
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	@Override
	public void lostOwnership(final Clipboard arg0, final Transferable arg1)
	{
	}

	/**
	 *
	 *
	 * @return
	 */
	public TreeSelectionListener getTreeSelectionListener()
	{
		return new MyTreeSelectionListener();
	}

	/**
	 *
	 * @return
	 */
	public JDFTreeNode getRootNode()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getRootNode();
	}

	/**
	 * set the currently displayed doc to doc
	 * 
	 * @param doc
	 * @param mimePackage
	 * @return the index in the list of docs that doc is stored in
	 */
	public int setJDFDoc(final JDFDoc doc, final String mimePackage)
	{
		int i = m_DocPos;
		if (doc != null)
		{
			i = documentService.indexOfJDF(doc, m_VjdfDocument);

			if (i >= 0)
			{
				m_DocPos = i;
			}
			else
			{
				m_VjdfDocument.add(new EditorDocument(doc, mimePackage));
				m_DocPos = m_VjdfDocument.size() - 1;
				// make sure that we have a global dirty policy in force
				doc.getCreateXMLDocUserData().setDirtyPolicy(EnumDirtyPolicy.Doc);
			}
		} // doc==null --> remove this entry
		else if (m_DocPos >= 0 && m_DocPos < m_VjdfDocument.size())
		{
			m_VjdfDocument.remove(m_DocPos);
			m_DocPos--;
			// roll over to the end; also ok if size=0, since -1 is the flag for all closed
			if (m_DocPos == -1)
			{
				m_DocPos = m_VjdfDocument.size() - 1;
			}
		}
		if (m_DocPos >= 0)
		{
			setTitle(getEditorDoc().getOriginalFileName());
		}
		else
		{
			getBottomTabs().refreshXmlEditor("", false);
		}
		m_menuBar.updateWindowsMenu();

		return m_DocPos;
	}

	/**
	 * set the currently displayed doc to doc
	 * 
	 * @param doc
	 * @param mimePackage
	 * @return the index in the list of docs that doc is stored in
	 */
	public int setEditorDoc(final EditorDocument doc)
	{
		int i = m_DocPos;
		if (doc != null)
		{
			i = m_VjdfDocument.indexOf(doc);

			if (i >= 0)
			{
				m_DocPos = i;
			}
			else
			{
				m_VjdfDocument.add(doc);
				m_DocPos = m_VjdfDocument.size() - 1;
				// make sure that we have a global dirty policy in force
				doc.getJDFDoc().getCreateXMLDocUserData().setDirtyPolicy(EnumDirtyPolicy.Doc);
			}
		}
		if (m_DocPos >= 0)
		{
			setTitle(doc.getOriginalFileName());
		}
		m_menuBar.updateWindowsMenu();

		return m_DocPos;
	}

	/**
	 * get the JDFDoc of the currently displayed JDF
	 * 
	 * @return the JDFDoc that is currently being displayed
	 */
	public EditorDocument getEditorDoc()
	{
		if (m_DocPos < 0)
		{
			return null;
		}
		return m_VjdfDocument.elementAt(m_DocPos);
	}

	/**
	 * get the JDFDoc of the currently displayed JDF
	 * 
	 * @return the JDFDoc that is currently being displayed
	 */
	public JDFDoc getJDFDoc()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getJDFDoc();
	}

	/**
	 *
	 *
	 * @return
	 */
	public JDFTreeModel getModel()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getModel();
	}

	public JDFTreeArea getJDFTreeArea()
	{
		return m_treeArea;
	}

	public EditorTabbedPaneA getTopTabs()
	{
		return m_topTabs;
	}

	public EditorTabbedPaneB getBottomTabs()
	{
		return m_bottomTabs;
	}
}
