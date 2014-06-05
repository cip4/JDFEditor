package org.cip4.tools.jdfeditor;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.view.MainView;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The Document model.
 */
public class EditorDocument
{
	private SettingService settingService = new SettingService();

    private JDFDoc jdfDoc;

	private JTree jTree;

	private JDFTreeModel jdfTreeModel;

	private EditorSelectionListener editorSelectionListener;

	private Vector<JDFTreeNode> jdfTreeNodesHistory;

	private int historyPos;

	private String packageName;

	private String cid;

	private double zoom;

	private int topTab;

    /**
     * Custom constructor. Accepting several attributes for initializing.
     * @param jdfDoc The JDFDoc object.
     * @param packageName The package name as String
     */
    public EditorDocument(JDFDoc jdfDoc, String packageName)
    {
        this.historyPos = -1;
        this.jdfDoc = jdfDoc;
        this.jdfTreeNodesHistory = new Vector<JDFTreeNode>();
        this.packageName = packageName;
        this.zoom = 1.0;
    }

	/**
	 * Getter for the zoom attribute value.
     * @return The zoom value as double.
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * Setter for the zoom attribute value.
     * @param zoom The zoom attribute value as double.
	 */
	public void setZoom(double zoom) {
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
		return "EditorDocument: " + jdfDoc.toString();
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
	@SuppressWarnings("unchecked")
	private TreePath getPathFromNode(final JDFTreeNode selNode)
	{
		final JDFTreeModel model = MainView.getModel();
		if (model == null)
		{
			return null;
		}
		final JDFTreeNode theRoot = (JDFTreeNode) model.getRootNode().getFirstChild();
		TreePath path = null;
		if (theRoot.equals(selNode))
		{
			path = new TreePath(selNode.getPath());
		}
		else
		{
			final Enumeration<JDFTreeNode> e = theRoot.depthFirstEnumeration();
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
	public JDFTreeModel createModel(final JDFTreeNode root)
	{

		jdfTreeModel = new JDFTreeModel(root, false);
		final JDFDoc doc = getJDFDoc();
		if (doc == null)
		{
			return null;
		}

		root.add(new JDFTreeNode(doc.getRoot()));
		jdfTreeModel.buildModel((JDFTreeNode) root.getFirstChild());
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

		public void treeNodesInserted(final TreeModelEvent event)
		{
			event.getClass();
			// TODO implement
		}

		public void treeNodesRemoved(final TreeModelEvent event)
		{
			event.getClass();
			// TODO implement
		}

		public void treeStructureChanged(final TreeModelEvent event)
		{
			event.getClass();
			// TODO implement
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

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
	}

	/**
	 * 
	 * 
	 * @param file
	 */
	private void writeToFile(File file)
	{
		int indent = settingService.getSetting(SettingKey.GENERAL_INDENT, Boolean.class) ? 2 : 0;
		jdfDoc.write2File(file.getAbsolutePath(), indent, !settingService.getSetting(SettingKey.GENERAL_INDENT, Boolean.class));
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
