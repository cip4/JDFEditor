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
package org.cip4.jdfeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
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

import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.core.XMLDocUserData.EnumDirtyPolicy;
import org.cip4.jdflib.goldenticket.BaseGoldenTicket;
import org.cip4.jdflib.goldenticket.IDPGoldenTicket;
import org.cip4.jdflib.goldenticket.MISCPGoldenTicket;
import org.cip4.jdflib.goldenticket.MISPreGoldenTicket;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.StringUtil;

/**
 * @author AnderssA ThunellE SvenoniusI Elena Skobchenko
 * 
 * This is the junk dump all in class with gazillions of private members
 * 
 * TODO clean up and move routines to the model
 */

public class JDFFrame extends JFrame implements ActionListener, DropTargetListener, DragSourceListener, DragGestureListener, ClipboardOwner
{
	/**
	 * Comment for <code>serialVersionU
	 * ID</code>
	 */
	private static final long serialVersionUID = 626128726824503039L;

	// TODO remove messy globals
	/**
	 * 
	 */
	public JDFTreeArea m_treeArea;

	EditorTabbedPaneA m_topTabs;
	EditorTabbedPaneB m_errorTabbedPane;
	JTree m_searchTree;

	/**
	 * handles all copying and pasting
	 * 
	 */
	public JDFTreeCopyNode m_copyNode;

	SearchDialog m_dialog = null;

	/**
	 * all settings are stored here
	 */
	public ResourceBundle m_littleBundle;

	// q&d hack for multi doc support
	Vector<EditorDocument> m_VjdfDocument = new Vector<EditorDocument>();
	int m_DocPos = -1; // document position

	// dragndrop support
	private final DragSource m_source;

	/**
	 * munues and buttons
	 */
	public EditorMenuBar m_menuBar = null;
	/**
	 * munues and buttons
	 */
	public EditorButtonBar m_buttonBar = null;

	/**
	 * undo and redo support
	 */
	final MyUndoManager undomanager = new MyUndoManager();
	final UndoableEditSupport undoSupport = new UndoableEditSupport();
	UndoAction undoAction = new UndoAction();
	RedoAction redoAction = new RedoAction();

	/**
	 * constructor of the frame
	 * 
	 */
	public JDFFrame()
	{
		super("CIP4 JDF Editor");
		// dirty hack to avoid npe
		Editor.my_Frame = this;
		m_source = DragSource.getDefaultDragSource();
		m_source.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, d.width, d.height - 30);

		final INIReader iniFile = Editor.getIniFile();
		final String language = iniFile.getLanguage();
		final String currentLookAndFeel = iniFile.getLookAndFeel();

		final Locale currentLocale = new Locale(language, language.toUpperCase());

		m_littleBundle = ResourceBundle.getBundle("org.cip4.jdfeditor.messages.JDFEditor", currentLocale);
		Locale.setDefault(currentLocale);

		m_menuBar = new EditorMenuBar();
		try
		{
			UIManager.setLookAndFeel(currentLookAndFeel);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * Method drawWindow.
	 */
	public void drawWindow()
	{
		Editor.setCursor(1, null);
		this.setJMenuBar(m_menuBar.drawMenu());
		this.getContentPane().add(drawBoxContent());
		this.setEnableClose();
		this.setVisible(true);
		Editor.setCursor(0, null);
	}

	/**
	 * Method drawBoxContent.
	 * @return Box
	 */
	private Box drawBoxContent()
	{
		final Box box_splitPane = Box.createHorizontalBox();
		box_splitPane.add(drawSplitPane());

		final Box box_content = Box.createVerticalBox();
		m_buttonBar = new EditorButtonBar(m_littleBundle, this);
		m_buttonBar.drawButtonBar();
		box_content.add(m_buttonBar);
		box_content.add(box_splitPane);

		return box_content;
	}

	/**
	 * Draws the splitpanes in which the views are to be displayed
	 * @return JSplitPane
	 */
	private JSplitPane drawSplitPane()
	{
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		m_topTabs = new EditorTabbedPaneA();
		m_errorTabbedPane = new EditorTabbedPaneB(this);

		m_treeArea = new JDFTreeArea(m_littleBundle, this);
		new DropTarget(m_treeArea, this);
		m_treeArea.setToolTipText(m_littleBundle.getString("TreeViewKey"));

		undomanager.setLimit(100);
		m_treeArea.getDocument().addUndoableEditListener(undomanager);
		undoSupport.addUndoableEditListener(undomanager);

		final JSplitPane attAndErrorPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_topTabs, m_errorTabbedPane);
		attAndErrorPane.setDividerLocation(d.height / 2);
		attAndErrorPane.setResizeWeight(1);

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_treeArea.getScrollPane(), attAndErrorPane);
		splitPane.setDividerLocation(d.width / 4);

		return splitPane;
	}

	/**
	 * Choose which file to open.
	 */
	void openFile()
	{
		File fileToSave = null; // Do this for GT
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
			final String recentFile = Editor.getIniFile().getRecentFiles()[0];
			if (recentFile != null)
			{
				fileToSave = new File(recentFile);
			}
		}

		final EditorFileChooser chooser = new EditorFileChooser(fileToSave, EditorFileChooser.allFiles);
		final int answer = chooser.showOpenDialog(this);

		if (answer == JFileChooser.APPROVE_OPTION)
		{
			fileToSave = chooser.getSelectedFile(); // Do this GT
			readFile(fileToSave); // Do this as well for GT
		}
	}

	/**
	 * 
	 * TODO Please insert comment!
	 * @param pd
	 */
	public void applyLookAndFeel(final PreferenceDialog pd)
	{
		try
		{
			final INIReader m_iniFile = Editor.getIniFile();
			UIManager.setLookAndFeel(m_iniFile.getLookAndFeel());
			m_buttonBar.removeAll();
			m_buttonBar.drawButtonBar();
			SwingUtilities.updateComponentTreeUI(JDFFrame.this);
			final EditorDocument ed = getEditorDoc();
			if (ed != null && ed.getJDFTree() != null)
			{
				ed.getJDFTree().setRowHeight(18);
			}

			if (pd != null)
			{
				SwingUtilities.updateComponentTreeUI(pd);
			}
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		catch (final InstantiationException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		catch (final IllegalAccessException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		catch (final UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void printWhat()
	{
		final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };

		final ComponentChooser cc = new ComponentChooser(m_littleBundle);

		final int option = JOptionPane.showOptionDialog(this, cc, m_littleBundle.getString("PrintMessKey"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		final EditorDocument ed = getEditorDoc();
		if (option == JOptionPane.OK_OPTION)
		{
			Component comp = null;

			if (cc.getComponent().equals(m_littleBundle.getString("ProcessViewKey")))
			{
				comp = m_topTabs.m_pArea;
			}
			else if (cc.getComponent().equals(m_littleBundle.getString("NextNeighbourKey")))
			{
				comp = m_topTabs.m_inOutScrollPane.m_inOutArea;
			}
			else if (cc.getComponent().equals(m_littleBundle.getString("TreeViewKey")))
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
				final INIReader m_iniFile = Editor.getIniFile();
				m_iniFile.setGenericAtts(vs);
				clearViews();
				readFile(fileToOpen);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("DevcapExportErrorKey") + e.getClass() + " \n"
					+ (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
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

				m_errorTabbedPane.m_devCapErrScroll.drawDevCapOutputTree(bugReport, executNodes);

				m_errorTabbedPane.setSelectedIndex(m_errorTabbedPane.m_DC_ERRORS_INDEX);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("DevcapOpenErrorKey") + e.getClass() + " \n"
					+ (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method readFile.
	 * @param fts the file to read
	 * @return true if at least one file was read or selected
	 */
	public boolean readFile(final File fts)
	{
		EditorDocument eDoc[] = null;
		Runtime.getRuntime().gc(); // clean up before loading

		if (fts != null)
		{
			final int docIndex = EditorDocument.indexOfFile(fts, m_VjdfDocument);
			if (docIndex >= 0)
			{
				nextFile(docIndex);
				return true;
			}
			else if (fts.exists())
			{
				eDoc = EditorUtils.getEditorDocuments(fts);
				if (eDoc == null)
				{
					EditorUtils.errorBox("FileNotOpenKey", ": " + fts.getName() + "!");
					Editor.setCursor(0, Editor.getFrame());
				}
				else
				{
					m_menuBar.updateRecentFilesMenu(fts.getAbsolutePath());
					for (int i = 0; i < eDoc.length; i++)
					{
						refreshView(eDoc[i], null);
						final JDFDoc doc = eDoc[i].getJDFDoc();
						if (doc != null)
						{
							doc.clearDirtyIDs();
						}
					}
				}
			}
		}
		return eDoc != null;
	}

	// ///////////////////////////////////////////////////////////

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

		Editor.setCursor(1, null);
		try
		{
			final INIReader m_iniFile = Editor.getIniFile();
			setEnableOpen(!m_iniFile.getReadOnly());

			m_treeArea.drawTreeView(eDoc);
			m_topTabs.refreshView(eDoc);
			this.setTitle(getWindowTitle());

			m_errorTabbedPane.refreshView(path);
			m_errorTabbedPane.refreshXmlEditor(eDoc.getJDFDoc().toXML());
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
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			Editor.setCursor(0, null);
		}
	}

	/**
	 * retrieves the display name for the currently displayed file
	 * @return
	 */
	private String getWindowTitle()
	{
		final EditorDocument ediDoc = getEditorDoc();
		String title = "CIP4 JDFEditor";
		if (ediDoc == null)
		{
			return title;
		}
		title += ": " + ediDoc.getSaveFileName();
		final String packageName = ediDoc.getMimePackage();
		if (packageName != null)
		{
			title += " (" + ediDoc.getOriginalFileName() + ")";
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
		if (editorDoc.getMimePackage() != null)
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
	 * Method saveFile. Save As-chooser
	 */
	public void saveAs()
	{
		final EditorDocument ediDoc = getEditorDoc();
		if (ediDoc == null)
		{
			return;
		}
		final String fileName = ediDoc.getSaveFileName();

		final File fileToSave = new File(fileName);
		final EditorFileChooser saveChooser = new EditorFileChooser(fileToSave, EditorFileChooser.allFiles);
		final int answer = saveChooser.showSaveDialog(null);

		if (answer == JFileChooser.APPROVE_OPTION)
		{
			final File file = saveChooser.getSelectedFile();
			int newAnswer = JOptionPane.YES_OPTION;

			if (file.exists() && !file.equals(fileToSave))
			{
				final String[] options = { m_littleBundle.getString("YesKey"), m_littleBundle.getString("NoKey"), m_littleBundle.getString("CancelKey") };
				newAnswer = JOptionPane.showOptionDialog(this, m_littleBundle.getString("FileExistsKey"), null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			}
			if (newAnswer == JOptionPane.YES_OPTION)
			{
				ediDoc.saveFile(file);
				this.setTitle(getWindowTitle());
				m_menuBar.updateRecentFilesMenu(fileToSave.toString());
			}
		}
	}

	/**
	 * Get the node to search for in the Process View.
	 * @param source - The location in the Process View that's been selected
	 * @return the JDFTreeNode that is to be searched for.
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
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("FindErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
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
		if (m_dialog != null)
		{
			m_dialog.dispose();
			m_dialog = null;
		}
		m_topTabs.clearViews();
		m_errorTabbedPane.clearViews();
	}

	/**
	 * Method saveFileQuestion. ask if the user wants to save an unsaved file before closing
	 * @param nMax maximum number of saves
	 * 
	 * @return int
	 */
	public int saveFileQuestion()
	{
		int save = JOptionPane.YES_OPTION;
		final INIReader m_iniFile = Editor.getIniFile();
		final EditorDocument doc = getEditorDoc();
		if (doc != null)
		{
			if (!m_iniFile.getReadOnly() || !isDirty())
			{
				String originalFileName = doc.getOriginalFileName();
				if (originalFileName == null)
				{
					originalFileName = "Untitled";
				}

				final String question = m_littleBundle.getString("SaveQuestionKey") + "\n" + '"' + originalFileName + '"';
				save = JOptionPane.showConfirmDialog(this, question, "", JOptionPane.YES_NO_CANCEL_OPTION);
			}
		}
		return save;
	}

	/**
	 * Method newJDF. creates a new JDF file
	 */
	private void newJDF()
	{
		clearViews();
		try
		{
			final JDFDoc jdfDoc = new JDFDoc("JDF");
			final JDFNode jdfRoot = jdfDoc.getJDFRoot();
			jdfRoot.setType("Product", true);
			setJDFDoc(jdfDoc, null);

			m_treeArea.drawTreeView(getEditorDoc());
			jdfDoc.setOriginalFileName("Untitled.jdf");
			setTitle(getWindowTitle());

		}
		catch (final Exception s)
		{
			s.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Method newJMF. creates a new JMF file
	 * @param f 
	 * @param type 
	 */
	void newJMF(final EnumFamily f, String type)
	{
		clearViews();
		try
		{
			JDFJMF jmf = Editor.getEditor().getJMFBuilder().newJMF(f, type);
			final VString requiredAttributes = jmf.getMissingAttributes(9999999);

			for (int i = 0; i < requiredAttributes.size(); i++)
			{
				final String s = JDFElement.getValueForNewAttribute(jmf, requiredAttributes.stringAt(i));
				if (!jmf.hasAttribute(requiredAttributes.stringAt(i)))
				{
					jmf.setAttribute(requiredAttributes.stringAt(i), s);
				}
			}
			if (type == null)
			{
				type = "untitled";
			}

			JDFDoc jmfDoc = jmf.getOwnerDocument_JDFElement();
			setJDFDoc(jmfDoc, null);
			m_treeArea.drawTreeView(getEditorDoc());
			jmfDoc.setOriginalFileName(type + ".jmf");
			setTitle(getWindowTitle());

		}
		catch (final Exception s)
		{
			s.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method newGoldenTicket. creates a new JDF file from an existing Golden Ticket.java file.
	 */
	private void newGoldenTicket()
	{
		int mis = 0;
		int jmf = 0;
		int gt1 = 0;
		String gtselect = "";

		try
		{
			final GoldenTicketDialog gt = new GoldenTicketDialog();

			gtselect = gt.getGoldenTicket();
			mis = gt.getMISLevel();
			jmf = gt.getJMFLevel();
			gt1 = gt.getGTLevel();
			BaseGoldenTicket theGT = null;

			if (gtselect == "MISCP")
			{

				theGT = new MISCPGoldenTicket(gt1, null, jmf, mis, true, BaseGoldenTicket.createSheetMap(1));
				theGT.nCols = new int[] { 4, 4 };
			}
			else if (gtselect == "MISPre")
			{
				theGT = new MISPreGoldenTicket(gt1, null, jmf, mis, BaseGoldenTicket.createSheetMap(1));
				theGT.nCols = new int[] { 4, 4 };
			}
			else if (gtselect == "IDP")
			{
				theGT = new IDPGoldenTicket(gt1);
			}

			if (theGT != null)
			{
				try
				{

					theGT.assign(null);

					// assigns the newly created JDF node to jdfcproot
					final JDFNode root = theGT.getNode();
					final JDFDoc doc = root.getOwnerDocument_JDFElement();
					setJDFDoc(doc, null);

					// display the result.
					m_treeArea.drawTreeView(getEditorDoc());
					doc.setOriginalFileName(gtselect + "_GoldenTicket.jdf");
					setTitle(getWindowTitle());
				}
				catch (final Exception s)
				{
					s.printStackTrace();
					JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
				}

			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("DevcapOpenErrorKey") + e.getClass() + " \n"
					+ (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Close the current file.
	 */
	public int closeFile(final int nMax)
	{
		int save = 0;
		int n = 0;
		EditorDocument doc = getEditorDoc();
		while (doc != null)
		{
			if (n++ >= nMax)
			{
				break;
			}

			final INIReader m_iniFile = Editor.getIniFile();
			if (isDirty() && !m_iniFile.getReadOnly())
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
				if (originalFileName != null && !originalFileName.startsWith("Untitled"))
				{
					m_iniFile.writeINIFile();
					m_menuBar.updateRecentFilesMenu(originalFileName);
				}

				if (save == JOptionPane.YES_OPTION)
				{
					if (originalFileName.startsWith("Untitled"))
					{
						saveAs();
					}
					else
					{
						doc.saveFile(null);
					}
				}
				else
				// no button or clean - just close
				{
					setJDFDoc(null, null);
				}
			}
			else
			// cancel button
			{
				break;
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

				if (!m_iniFile.getReadOnly())
				{
					setEnableClose();
				}

				m_treeArea.m_treeView.setView(textArea);
				clearViews();
				m_topTabs.setSelectedIndex(m_topTabs.m_IO_INDEX);
			}
		}
		setTitle(getWindowTitle());
		return save;
	}

	/**
	 * Asks if the user wants to create a new JDF or JMF file. BMI: Also where can select Golden Ticket
	 * 
	 * Coming from EditorMenuBar, second stop in the chain. called from frame.NewFile()
	 */
	public void newFile()
	{
		final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };

		final NewFileChooser newFileChooser = new NewFileChooser();

		final int option = JOptionPane.showOptionDialog(this, newFileChooser, m_littleBundle.getString("ChooseNewFileKey"), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			if ((newFileChooser.getSelection()).equals("JDF"))
			{
				newJDF();
			}
			else if ((newFileChooser.getSelection()).equals("JMF"))
			{
				newJMF(EnumFamily.Query, "KnownMessages");
			}
			else
			{
				newGoldenTicket();
			}

			final INIReader m_iniFile = Editor.getIniFile();
			setEnableOpen(!m_iniFile.getReadOnly());
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

		final FixVersionDialog dialog = new FixVersionDialog(m_littleBundle);
		final TreePath path = m_treeArea.getSelectionPath();
		dialog.fixIt(path);
	}

	/**
	 * fixes the version of a JDF by calling fixVersion for the selected JDF node ore the closest JDF parent.
	 * 
	 */
	void cleanupSelected()
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

				if (n1 != null)
				{
					n1.eraseUnlinkedResources();
					n1.eraseDefaultAttributes(true);
					n1.eraseEmptyAttributes(true);
					n1.eraseEmptyNodes(true);
				}
				else
				{
					if (element instanceof JDFElement)
						((JDFElement) element).eraseDefaultAttributes(true);
					element.eraseEmptyAttributes(true);
					element.eraseEmptyNodes(true);
				}
			}
			refreshView(getEditorDoc(), path);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("FixVersionErrorKey") + e.getClass() + " \n"
					+ (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * fixes the version of a JDF by calling fixVersion for the selected JDF node ore the closest JDF parent.
	 * 
	 */
	void removeExtensionsfromSelected()
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
			JOptionPane.showMessageDialog(this, m_littleBundle.getString("FixVersionErrorKey") + e.getClass() + " \n"
					+ (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Creates the SearchDialog.
	 * @param searchComponent - Where to perform the search?
	 */
	private void findWhatDialog(final String searchComponent)
	{
		m_dialog = new SearchDialog(searchComponent);
	}

	/**
	 * Mother of all action dispatchers
	 * TODO remove and distribute over the relevant classes
	 * @param e the event that gets checked
	 */
	public void actionPerformed(final ActionEvent e)
	{
		Editor.setCursor(1, null);
		final INIReader m_iniFile = Editor.getIniFile();

		final Object eSrc = e.getSource();
		if (eSrc == m_menuBar.m_exportItem)
		{
			exportToDevCap();
		}
		else if (eSrc == m_menuBar.m_quitItem)
		{
			m_iniFile.writeINIFile();
			if (closeFile(9999) != JOptionPane.CANCEL_OPTION)
			{
				System.exit(0);
			}
		}
		else if (eSrc == m_menuBar.m_versionItem)
		{
			final Editor editor = Editor.getEditor();
			JOptionPane.showMessageDialog(this, editor.getEditorName() + "\n" + editor.getEditorBuildDate() + "\nJDF 1.4 compatible version\n" + "Schema JDF_1.4.xsd\n"
					+ editor.getEditorVersion(), "Version", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (eSrc == m_menuBar.m_findItem)
		{
			findIt();
		}
		else if (eSrc == m_buttonBar.m_validateButton)
		{
			getModel().validate();
		}
		else if (eSrc == m_menuBar.m_fixVersionItem)
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
		else if (eSrc == m_buttonBar.m_zoomInButton)
		{
			m_topTabs.m_pArea.zoom('+');
		}
		else if (eSrc == m_buttonBar.m_zoomOutButton)
		{
			m_topTabs.m_pArea.zoom('-');
		}
		else if (eSrc == m_buttonBar.m_zoomOrigButton)
		{
			m_topTabs.m_pArea.zoom('o');
		}
		else if (eSrc == m_buttonBar.m_zoomBestButton)
		{
			m_topTabs.m_pArea.zoom('b');
		}
		else if (eSrc == m_menuBar.m_devCapItem)
		{
			openDeviceCapFile();
		}
		// copy the results of the validation to the system clip board
		else if (eSrc == m_menuBar.m_copyValidationListItem)
		{
			// TODO m_errorTabbedPane.copyValidationListToClipBoard();
		}
		else if (!m_iniFile.getReadOnly())
		{
			if (eSrc == m_buttonBar.m_cutButton || eSrc == m_menuBar.m_cutItem)
			{
				cutSelectedNode();
			}
			else if (eSrc == m_buttonBar.m_copyButton || eSrc == m_menuBar.m_copyItem)
			{
				copySelectedNode();
			}
			else if (eSrc == m_buttonBar.m_pasteButton || eSrc == m_menuBar.m_pastePopupItem || eSrc == m_menuBar.m_pasteItem)
			{
				pasteCopiedNode();
			}
			else if (eSrc == m_menuBar.m_insertElemBeforeItem)
			{
				m_treeArea.insertElementAtSelectedNode(-1);
			}
			else if (eSrc == m_menuBar.m_insertElemAfterItem)
			{
				m_treeArea.insertElementAtSelectedNode(1);
			}
			else if (eSrc == m_menuBar.m_insertElemIntoItem)
			{
				m_treeArea.insertElementAtSelectedNode(0);
			}
			else if (eSrc == m_menuBar.m_insertInResItem)
			{
				m_treeArea.insertResourceWithLink(true, true);
			}
			else if (eSrc == m_menuBar.m_insertOutResItem)
			{
				m_treeArea.insertResourceWithLink(true, false);
			}
			else if (eSrc == m_menuBar.m_insertResItem)
			{
				m_treeArea.insertResourceWithLink(false, false);
			}
			else if (eSrc == m_menuBar.m_insertInResLinkItem)
			{
				m_treeArea.insertResourceLink(EnumUsage.Input);
			}
			else if (eSrc == m_menuBar.m_insertOutResLinkItem)
			{
				m_treeArea.insertResourceLink(EnumUsage.Output);
			}
			else if (eSrc == m_menuBar.m_insertAttrItem)
			{
				m_treeArea.insertAttrItem();
			}
			else if (eSrc == m_menuBar.m_renameItem)
			{
				renameSelectedNode();
			}
			else if (eSrc == m_menuBar.m_modifyAttrValueItem)
			{
				m_treeArea.modifyAttribute();
			}
			else if (eSrc == m_menuBar.m_requiredAttrItem)
			{
				addRequiredAttrsToSelectedNode();
			}
			else if (eSrc == m_menuBar.m_requiredElemItem)
			{
				addRequiredElemsToSelectedNode();
			}
			else if (eSrc == m_buttonBar.m_refreshButton)
			{
				refresh();
			}
		}
		Editor.setCursor(0, null);
	}

	/**
	 * 
	 */
	void save()
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
			if (getTitle().equalsIgnoreCase("Untitled.jdf") || getTitle().equalsIgnoreCase("Untitled.jmf"))
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

	// /////////////////////////////////////////////////////////////////

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
		Editor.setCursor(1, null);
		m_treeArea.repaint();
		final EditorDocument ed = getEditorDoc();
		if (path != null && ed != null)
		{
			final JTree tree = ed.getJDFTree();
			tree.getPreferredSize();
			tree.scrollPathToVisible(path);
			tree.setSelectionPath(path);
		}
		Editor.setCursor(0, null);
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
		if (m_dialog == null)
		{
			findWhatDialog(findString);
		}
		else
		{
			m_dialog.setSearchComponent(findString);
			m_dialog.toFront();
		}
	}

	/**
	 * returns true if the currently selected document has been modified
	 * @return
	 */
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
	class MyTreeSelectionListener implements TreeSelectionListener
	{
		public void valueChanged(final TreeSelectionEvent e)
		{
			e.getClass(); // fool compiler
			if (m_treeArea.getSelectionPath() != null)
			{
				final JDFTreeNode node = (JDFTreeNode) m_treeArea.getSelectionPath().getLastPathComponent();
				if (node.isElement())
				{
					final KElement elem = node.getElement();

					if (elem instanceof JDFNode)
					{
						m_menuBar.setSpawnMergeEnabled(true);
					}
					else
					{
						m_menuBar.setSpawnMergeEnabled(false);
					}
					m_menuBar.m_pasteItem.setEnabled(m_copyNode != null);
					m_buttonBar.m_pasteButton.setEnabled(m_copyNode != null);

				}
				else
				{
					m_menuBar.setSpawnMergeEnabled(false);
					m_menuBar.m_pasteItem.setEnabled(false);
					m_buttonBar.m_pasteButton.setEnabled(false);
				}

				final int selIndex = m_topTabs.getSelectedIndex();

				final INIReader m_iniFile = Editor.getIniFile();
				if (!m_iniFile.getReadOnly())
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

	private void setEnableOpen(final boolean mode)
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

		public void actionPerformed(final ActionEvent e)
		{
			e.getID();
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

			m_menuBar.m_undoItem.setText(undomanager.getUndoPresentationName());
			m_menuBar.m_undoItem.setEnabled(undomanager.canUndo());
		}
	}

	class RedoAction extends AbstractAction
	{
		private static final long serialVersionUID = 626128726824504003L;

		public void actionPerformed(final ActionEvent e)
		{
			e.getID(); // fool compiler
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

			m_menuBar.m_redoItem.setText(undomanager.getRedoPresentationName());
			m_menuBar.m_redoItem.setEnabled(undomanager.canRedo());
		}
	}

	class EditChangeListener implements ChangeListener
	{
		public void stateChanged(final ChangeEvent e)
		{
			e.getClass(); // fool compiler
			if (m_treeArea != null)
			{
				m_treeArea.repaint();
			}

			if (getJDFDoc() == null)
			{
				m_buttonBar.m_validateButton.setEnabled(false);
			}

			final INIReader m_iniFile = Editor.getIniFile();
			m_iniFile.writeINIFile();
		}
	}

	public void dragGestureRecognized(final DragGestureEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dragDropEnd(final DragSourceDropEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dragEnter(final DragSourceDragEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dragExit(final DragSourceEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dragOver(final DragSourceDragEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dropActionChanged(final DragSourceDragEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dragEnter(final DropTargetDragEvent e)
	{
		e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	public void dragExit(final DropTargetEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	public void dragOver(final DropTargetDragEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

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

				if (files.hasNext())
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
			System.out.println("data no longer available in the requested flavor");
			e.rejectDrop();
		}
		catch (final UnsupportedFlavorException ufe)
		{
			System.out.println("data flavor not supported");
			e.rejectDrop();
		}
	}

	public void dropActionChanged(final DropTargetDragEvent e)
	{
		e.getClass(); // fool compiler
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.boardOwner#lostOwnership( java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(final Clipboard arg0, final Transferable arg1)
	{
		// TODO Auto-generated method stub
		arg0.getClass();
		arg1.getClass();
	}

	public TreeSelectionListener getTreeSelectionListener()
	{
		return new MyTreeSelectionListener();
	}

	public JDFTreeNode getRootNode()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getRootNode();
	}

	/**
	 * set the currently displayed doc to doc
	 * @param doc
	 * @return the index in the list of docs that doc is stored in
	 */
	public int setJDFDoc(final JDFDoc doc, final String mimePackage)
	{
		int i = m_DocPos;
		if (doc != null)
		{
			i = EditorDocument.indexOfJDF(doc, m_VjdfDocument);

			if (i >= 0)
			{
				m_DocPos = i;
			}
			else
			{
				m_VjdfDocument.add(new EditorDocument(doc, mimePackage));
				m_DocPos = m_VjdfDocument.size() - 1;
				// make sur that we have a global dirty policy in force
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
		m_menuBar.updateWindowsMenu();

		return m_DocPos;
	}

	/**
	 * get the JDFDoc of the currently displayed JDF
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
	 * @return the JDFDoc that is currently being displayed
	 */
	public JDFDoc getJDFDoc()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getJDFDoc();
	}

	public void setModel(final JDFTreeModel m_model)
	{
		final EditorDocument ed = getEditorDoc();
		ed.setModel(m_model);
	}

	public JDFTreeModel getModel()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getModel();
	}

	// //////////////////////////////////////////////////////////////

	/*
	 * public int newMISCPLevel() { String cp1 = (String) JOptionPane.showInputDialog( this, m_littleBundle.getString("MISCPLevelKey2"),
	 * m_littleBundle.getString("MISCPLevelKey"), JOptionPane.QUESTION_MESSAGE, null, l1, "1");
	 * 
	 * int MISCPSelectLevel = 0; MISCPSelectLevel = Integer.parseInt(cp1); return MISCPSelectLevel; }
	 * 
	 * public int newJMFLevel() { String j1 = (String) JOptionPane.showInputDialog( this, m_littleBundle.getString("JMFLevelKey2"),
	 * m_littleBundle.getString("JMFLevelKey"), JOptionPane.QUESTION_MESSAGE, null, l1, "1");
	 * 
	 * int JMFSelectLevel = 0; JMFSelectLevel = Integer.parseInt(j1); return JMFSelectLevel; }
	 * 
	 * public int newMISLevel() { String m1 = (String) JOptionPane.showInputDialog( this, m_littleBundle.getString("MISLevelKey2"),
	 * m_littleBundle.getString("MISLevelKey"), JOptionPane.QUESTION_MESSAGE, null, l2, "1");
	 * 
	 * int MISSelectLevel = 0; MISSelectLevel = Integer.parseInt(m1); return MISSelectLevel; }
	 */
	// //////////////////////////////////////////////////////////////
}
