/**
 * The CIP4 Software License, Version 1.0
 *
 * Copyright (c) 2001-2021 The International Cooperation for the Integration of
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

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.lib.jdf.jsonutil.JSONWriter;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * The Document model.
 */
public class EditorDocument
{
	private final SettingService settingService = SettingService.getSettingService();

	private final JDFDoc jdfDoc;

	private JTree jTree;

	private JDFTreeModel jdfTreeModel;

	private EditorSelectionListener editorSelectionListener;

	private final Vector<JDFTreeNode> jdfTreeNodesHistory;

	private int historyPos;

	private String packageName;

	private String cid;

	private double zoom;

	private int topTab;

	private boolean dirtyFlag;

	private boolean json;

	/**
	 * @return the json
	 */
	public boolean isJson()
	{
		return json;
	}

	/**
	 * @return the json
	 */
	public boolean isXJDF()
	{
		return jdfDoc != null && EditorUtils.isXJDF(jdfDoc.getRoot().getLocalName());
	}

	/**
	 * @param json the json to set
	 */
	public void setJson(final boolean json)
	{
		this.json = json;
		if (jdfDoc != null)
		{

			final String extension = json ? "json" : jdfDoc.getRoot().getLocalName().toLowerCase();
			jdfDoc.setOriginalFileName(UrlUtil.newExtension(getOriginalFileName(), extension));
			MainView.getFrame().refreshTitle();
			MainView.getFrame().setEnableOpen(true);
		}
	}

	/**
	 * Custom constructor. Accepting several attributes for initializing.
	 * @param jdfDoc The JDFDoc object.
	 * @param mimeContent The package name as String
	 */
	public EditorDocument(final JDFDoc jdfDoc, final String mimeContent)
	{
		this.historyPos = -1;
		this.jdfDoc = jdfDoc;
		this.jdfTreeNodesHistory = new Vector<JDFTreeNode>();
		this.packageName = mimeContent;
		this.zoom = EditorButtonBar.DEFAULT_ZOOM;
		json = false;
	}

	public boolean isDirtyFlag()
	{
		return dirtyFlag;
	}

	public void setDirtyFlag()
	{
		this.dirtyFlag = true;
	}

	public void resetDirtyFlag()
	{
		this.dirtyFlag = false;
	}

	public double getZoom()
	{
		return zoom;
	}

	public void setZoom(final double zoom)
	{
		this.zoom = zoom;
	}

	/**
	 * Getter for the jdfDoc attribute value.
	 * @return The JDFDoc attribute value.
	 */
	public JDFDoc getJDFDoc()
	{
		return jdfDoc;
	}

	/**
	 * Getter for the PackageName attribute value.
	 * @return The PackageName value as String.
	 */
	public String getPackageName()
	{
		return packageName;
	}

	@Override
	public String toString()
	{
		if (jdfDoc == null)
		{
			return "EditorDocument: #null";
		}
		return "EditorDocument: " + getSaveFileName() + " " + jdfDoc + " json=" + json;
	}

	/**
	 * Returns the original file name of a JDF Document.
	 * @return The original file name of a JDF Document as String.
	 */
	public String getOriginalFileName()
	{
		if (jdfDoc == null)
		{
			return null;
		}

		return jdfDoc.getOriginalFileName();
	}

	/**
	 *
	 * @param model
	 */
	public void setModel(final JDFTreeModel model)
	{
		jdfTreeModel = model;
	}

	/**
	 *
	 * @return
	 */
	public JDFTreeModel getModel()
	{
		return jdfTreeModel;
	}

	/**
	 *  sets the selection path for this document
	 * @param path
	 * @param trackHistory
	 */
	public void setSelectionPath(final TreePath path, final boolean trackHistory)
	{
		if (jTree == null)
		{
			return;
		}

		if (trackHistory == false)
		{
			jTree.removeTreeSelectionListener(editorSelectionListener);
		}

		if (path != null)
		{
			jTree.setSelectionPath(path);
			jTree.scrollPathToVisible(path);
		}

		if (trackHistory == false)
		{
			jTree.addTreeSelectionListener(editorSelectionListener);
		}
	}

	/**
	 *
	 * @return
	 */
	public TreePath getSelectionPath()
	{
		if (getJDFTree() == null)
		{
			return null;
		}
		return getJDFTree().getSelectionPath();
	}

	/**
	 *
	 * @return
	 */
	public TreePath[] getSelectionPaths()
	{
		if (getJDFTree() == null)
		{
			return null;
		}
		return getJDFTree().getSelectionPaths();
	}

	/**
	 *
	 * @param jdfTree
	 */
	public void setJDFTree(final JTree jdfTree)
	{
		this.jTree = jdfTree;
		editorSelectionListener = new EditorSelectionListener();
		jTree.addTreeSelectionListener(editorSelectionListener);
	}

	/**
	 *
	 * @return
	 */
	public JTree getJDFTree()
	{
		return jTree;
	}

	/**
	 *
	 * @return
	 */
	public JDFTreeNode getRootNode()
	{
		final JDFTreeModel mod = getModel();
		return mod == null ? null : mod.getRootNode();
	}

	/**
	 *
	 */
	public void setLastSelection()
	{
		JDFTreeNode selNode = null;
		if (historyPos == -1)
		{
			historyPos = jdfTreeNodesHistory.size() - 1;
		}
		if (historyPos > 0)
		{
			historyPos--;
			selNode = jdfTreeNodesHistory.elementAt(historyPos);
			setSelectionNode(selNode, false);
		}
		enableNextLast();
	}

	/**
	 * @param selNode
	 * @param trackHistory
	 *
	 */
	private void setSelectionNode(final JDFTreeNode selNode, final boolean trackHistory)
	{
		final TreePath path = getPathFromNode(selNode);
		if (path != null)
		{
			setSelectionPath(path, trackHistory);
		}
	}

	/**
	 * @param selNode
	 * @return
	 */
	private TreePath getPathFromNode(final JDFTreeNode selNode)
	{
		final JDFTreeModel model = MainView.getModel();
		if (model == null)
		{
			return null;
		}
		final JDFTreeNode theRoot = model.getRootNode();
		TreePath path = null;
		if (theRoot.equals(selNode))
		{
			path = new TreePath(selNode.getPath());
		}
		else
		{
			final Enumeration<JDFTreeNode> e = theRoot.depthFirstJdfEnumeration();
			while (e.hasMoreElements())
			{
				final JDFTreeNode node = e.nextElement();
				if (node.equals(selNode))
				{
					path = new TreePath(selNode.getPath());
					break;
				}
			}
		}
		return path;
	}

	/**
	 *
	 * @return
	 */
	public TreePath getLastSelection()
	{
		final JDFTreeNode node = getLastTreeNode();
		return getPathFromNode(node);
	}

	/**
	 *
	 * @return
	 */
	public JDFTreeNode getLastTreeNode()
	{
		JDFTreeNode selNode = null;
		if (historyPos == -1)
		{
			historyPos = jdfTreeNodesHistory.size() - 1;
		}
		if (historyPos > 0)
		{
			selNode = jdfTreeNodesHistory.elementAt(historyPos);
		}
		return selNode;
	}

	// ///////////////////////////////////////////////////////////////

	/**
	 *
	 */
	public void setNextSelection()
	{
		if (historyPos >= 0)
		{
			historyPos++;
			if (historyPos < jdfTreeNodesHistory.size())
			{
				setSelectionNode(jdfTreeNodesHistory.elementAt(historyPos), false);
			}
			else
			{
				historyPos = -1; // we are at the head
			}
		}
		enableNextLast();
	}

	// ///////////////////////////////////////////////////////////////

	protected void enableNextLast()
	{
		final EditorButtonBar editorButtonBar = MainView.getFrame().m_buttonBar;
		editorButtonBar.m_LastButton.setEnabled(historyPos != 0 && jdfTreeNodesHistory.size() > 1);
		editorButtonBar.m_NextButton.setEnabled(historyPos != jdfTreeNodesHistory.size() - 1 && jdfTreeNodesHistory.size() > 1);
	}

	/**
	 * Method createModel. create the treeModel
	 * @param root
	 * @return TreeModel
	 */
	public JDFTreeModel createModel()
	{
		final JDFDoc doc = getJDFDoc();
		if (doc == null)
		{
			jdfTreeModel = new JDFTreeModel(new JDFTreeNode(), false);
			return null;
		}
		final JDFTreeNode root = new JDFTreeNode(doc.getRoot());
		jdfTreeModel = new JDFTreeModel(root, false);

		jdfTreeModel.buildModel(root);
		jdfTreeModel.addTreeModelListener(new MyTreeModelListener());
		if (settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class))
		{
			jdfTreeModel.validate();
		}
		return jdfTreeModel;
	}

	// ///////////////////////////////////////////////////////////////
	class EditorSelectionListener implements TreeSelectionListener
	{
		@Override
		public void valueChanged(final TreeSelectionEvent e)
		{
			final TreePath p = e.getPath();
			final JDFTreeNode tn = (JDFTreeNode) p.getLastPathComponent();
			historyPos = -1;
			jdfTreeNodesHistory.add(tn);
			if (jdfTreeNodesHistory.size() > 100)
			{
				jdfTreeNodesHistory.remove(0);
			}
			enableNextLast();

		}
	}

	// //////////////////////////////////////////////////////////////////////////////

	class MyTreeModelListener implements TreeModelListener
	{
		@Override
		public void treeNodesChanged(final TreeModelEvent event)
		{
			JDFTreeNode node = (JDFTreeNode) (event.getTreePath().getLastPathComponent());
			try
			{
				final int index = event.getChildIndices()[0];
				node = (JDFTreeNode) (node.getChildAt(index));
			}
			catch (final NullPointerException e)
			{
				//
			}
		}

		@Override
		public void treeNodesInserted(final TreeModelEvent event)
		{
			event.getClass();
			// TODO implement
		}

		@Override
		public void treeNodesRemoved(final TreeModelEvent event)
		{
			event.getClass();
			// TODO implement
		}

		@Override
		public void treeStructureChanged(final TreeModelEvent event)
		{
			event.getClass();
			// TODO implement
		}
	}

	/**
	 * @param file
	 */
	public void saveFile(File file)
	{
		if (jdfDoc == null)
		{
			return;
		}

		if (file == null)
		{
			file = new File(getSaveFileName());
		}

		final KElement e = jdfDoc.getRoot();
		if (settingService.getSetting(SettingKey.GENERAL_REMOVE_DEFAULT, Boolean.class) && (e instanceof JDFElement))
		{
			((JDFElement) e).eraseDefaultAttributes(true);
		}
		if (settingService.getSetting(SettingKey.GENERAL_REMOVE_WHITE, Boolean.class))
		{
			e.eraseEmptyNodes(true);
		}
		if (settingService.getSetting(SettingKey.GENERAL_NORMALIZE, Boolean.class))
		{
			e.sortChildren();
			// String extension=UrlUtil.extension(file.getAbsolutePath().toLowerCase());
		}

		if (!UrlUtil.isMIME(file))
		{
			writeToFile(file);
			jdfDoc.setOriginalFileName(file.getAbsolutePath());
		}
		else
		{
			Multipart mp = null;
			if (packageName == null)
			{
				mp = MimeUtil.buildMimePackage(null, getJDFDoc(), true);
			}
			else
			{
				mp = MimeUtil.getMultiPart(packageName);
				final BodyPart bp = MimeUtil.updateXMLMultipart(mp, jdfDoc, cid);
				if (bp == null)
				{
					mp = null; // flag that we shouldnt write
				}
			}
			if (mp != null)
			{
				MimeUtil.writeToFile(mp, file.getAbsolutePath(), null);
				packageName = file.getAbsolutePath();
			}
		}
		jdfDoc.clearDirtyIDs();
		resetDirtyFlag();
	}

	/**
	 *
	 *
	 * @param file
	 */
	private void writeToFile(final File file)
	{
		final int indent = settingService.getSetting(SettingKey.GENERAL_INDENT, Boolean.class) ? 2 : 0;
		if (json)
		{
			final JSONWriter jw = Editor.getEditor().getJSonWriter();
			jw.convert(jdfDoc.getRoot());
			FileUtil.writeFile(jw, file);
		}
		else
		{
			jdfDoc.write2File(file.getAbsolutePath(), indent, !settingService.getSetting(SettingKey.GENERAL_INDENT, Boolean.class));
		}
	}

	/**
	 * * get the name of the file that this document was originally loaded from. the mime package name, if ti was a mime package, otherwise the jdf file name
	 * @return
	 */
	public String getSaveFileName()
	{
		String fileName = getPackageName();
		if (fileName == null)
		{
			fileName = getOriginalFileName();
		}
		return fileName;
	}

	/**
	 * @param cid
	 */
	public void setCID(final String cid)
	{
		this.cid = cid;

	}

	// ////////////////////////////////////////////////////////

	/**
	 * @return the topTab
	 */
	public int getTopTab()
	{
		return topTab;
	}

	// ////////////////////////////////////////////////////////
	/**
	 * @param _topTab the topTab to set
	 */
	public void setTopTab(final int _topTab)
	{
		this.topTab = _topTab;
	}

	// ////////////////////////////////////////////////////////
	/**
	 * @return
	 *
	 */
	public boolean isDirty()
	{
		return jdfDoc == null ? false : jdfDoc.isDirty(null);
	}
	// ////////////////////////////////////////////////////////

}
