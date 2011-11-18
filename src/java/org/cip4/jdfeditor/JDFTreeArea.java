/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2011 The International Cooperation for the Integration of 
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFRefElement;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFCreated;
import org.cip4.jdflib.resource.JDFDeleted;
import org.cip4.jdflib.resource.JDFModified;
import org.cip4.jdflib.resource.JDFPart;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFDevCaps;
import org.cip4.jdflib.util.StringUtil;

/**
 * This is a new dump for some of the JDFFrame classes that relate to the actual tree view
 * TODO move some of the routines here into the model
 * where they belong and reduce the dependencies with JDFFrame
 * 
 * @author prosirai
 * 
 */
public class JDFTreeArea extends JTextArea
{
	TreeSelectionListener m_treeSelectionListener;
	private static final long serialVersionUID = 2036935468347224324L;
	private final JScrollPane m_treeScroll;
	JViewport m_treeView;
	private JDFTreeRenderer m_renderer;
	JDFFrame m_frame;
	private final ResourceBundle m_littleBundle;
	private static String lastPath = "/JDF";

	/**
	 * 
	 * @param bundle
	 * @param frame
	 */
	public JDFTreeArea(final ResourceBundle bundle, final JDFFrame frame)
	{
		super();
		m_littleBundle = bundle;
		m_frame = frame;
		setEditable(false);
		m_treeScroll = new JScrollPane();
		m_treeScroll.getVerticalScrollBar().setUnitIncrement(20);
		m_treeScroll.getHorizontalScrollBar().setUnitIncrement(20);

		final JLabel treeLabel = new JLabel(" " + bundle.getString("TreeViewKey"));
		treeLabel.setPreferredSize(new Dimension(treeLabel.getPreferredSize().width, 23));
		treeLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		treeLabel.setToolTipText(bundle.getString("TreeViewKey"));
		treeLabel.setBorder(BorderFactory.createLineBorder(Color.black));

		m_treeView = new JViewport();

		m_treeView.setView(this);
		m_treeScroll.setViewport(m_treeView);
		m_treeScroll.setColumnHeaderView(treeLabel);
	}

	/**
	 * Expands the TreePath and all of its subelements.
	 * @param p - The TreePath to expand
	 */
	@SuppressWarnings("unchecked")
	public void xpand(TreePath p)
	{
		if (p == null)
		{
			p = getSelectionPath();
		}
		final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
		getJDFTree().expandPath(p);
		final Enumeration<JDFTreeNode> e = node.preorderEnumeration();

		while (e.hasMoreElements())
		{
			final JDFTreeNode treeNode = e.nextElement();

			if (treeNode.isElement())
			{
				getJDFTree().expandPath(new TreePath(treeNode.getPath()));
			}
		}
	}

	/**
	 * Collapses the TreePath and all of its subelements.
	 * @param p - The TreePath to collapse
	 */
	public void collapse(TreePath p)
	{
		if (p == null)
		{
			p = getSelectionPath();
		}

		final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
		final Enumeration<JDFTreeNode> e = node.postorderEnumeration();

		while (e.hasMoreElements())
		{
			final JDFTreeNode treeNode = e.nextElement();

			if (treeNode.isElement())
			{
				getJDFTree().collapsePath(new TreePath(treeNode.getPath()));
			}
		}
		getJDFTree().collapsePath(p);
	}

	/**
	 * 
	 * @return
	 */
	public boolean jdfTreeIsNull()
	{
		if (getJDFTree() == null || m_treeView.getComponent(0).getClass().equals(JTextArea.class))
		{
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @return
	 */
	public JScrollPane getScrollPane()
	{
		return m_treeScroll;
	}

	/**
	 * Method drawTreeView.
	 * @param eDoc
	 */
	public void drawTreeView(final EditorDocument eDoc)
	{
		// TODO create a root that is not a null element!
		final JDFTreeNode root = new JDFTreeNode();
		eDoc.createModel(root);
		final JTree jdfTree = new JTree();
		eDoc.setJDFTree(jdfTree);
		jdfTree.setModel(eDoc.getModel());

		final PopupListener popupListener = new PopupListener();
		jdfTree.addMouseListener(popupListener);

		jdfTree.setRootVisible(false);
		jdfTree.setEditable(false);
		jdfTree.setExpandsSelectedPaths(true);
		jdfTree.putClientProperty("JTree.lineStyle", "Angled");
		ToolTipManager.sharedInstance().registerComponent(jdfTree);
		m_renderer = new JDFTreeRenderer();
		jdfTree.setCellRenderer(m_renderer);
		jdfTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		m_treeSelectionListener = m_frame.getTreeSelectionListener();
		jdfTree.addTreeSelectionListener(m_treeSelectionListener);
		jdfTree.setRowHeight(18);

		// jdfTree.expandPath(new TreePath(((JDFTreeNode) root.getFirstChild()).getPath()));
		// jdfTree.expandPath(eDoc.getLastSelection());
		new DropTarget(jdfTree, m_frame);
		m_treeView.setView(jdfTree);
		jdfTree.setBackground(Color.white);
		jdfTree.setShowsRootHandles(true);
		findNode(eDoc.getLastTreeNode());

		// goToPath(eDoc.getLastSelection());
	}

	/**
	 * Sets focus on the correct node in the Tree View from a selected node in the In & Output View. Called from method findNode().
	 * @param p - The path which you want to show
	 */
	public void goToPath(final TreePath p)
	{
		if (p == null)
		{
			return;
		}
		final JTree jdfTree = getJDFTree();
		jdfTree.makeVisible(p);
		jdfTree.removeTreeSelectionListener(m_treeSelectionListener);
		final int row = jdfTree.getRowForPath(p);
		jdfTree.setSelectionRow(row);
		jdfTree.scrollRowToVisible(row);
		final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
		m_frame.m_errorTabbedPane.selectNodeWithXPath(new TreePath(node.getPath()));
		jdfTree.addTreeSelectionListener(m_treeSelectionListener);
		// m_frame.m_errorTabbedPane.goToPath(p);
	}

	class PopupListener extends MouseAdapter
	{
		@Override
		public void mousePressed(final MouseEvent e)
		{
			final EditorDocument ed = m_frame.getEditorDoc();
			if (ed == null)
			{
				return;
			}

			final JTree jdfTree = ed.getJDFTree();
			final TreePath path = jdfTree.getPathForLocation(e.getX(), e.getY());

			if ((SwingUtilities.isRightMouseButton(e) || e.isAltDown()) && path != null && !Editor.getIniFile().getReadOnly())
			{
				jdfTree.removeTreeSelectionListener(m_treeSelectionListener);
				ed.setSelectionPath(path, true);

				final JPopupMenu rightMenu = new PopUpRightClick(path);
				final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

				final Point pTree = e.getComponent().getLocationOnScreen();
				final Dimension d2 = rightMenu.getPreferredSize();

				final int x = (int) pTree.getX() + e.getX();
				final int y = (int) pTree.getY() + e.getY();
				final int x2 = d2.width;
				final int y2 = d2.height;
				int xStart = e.getX();
				int yStart = e.getY();

				if ((x + x2) > d.getWidth())
				{
					xStart = xStart - x2;
				}

				if ((y + y2) > d.getHeight())
				{
					yStart = yStart - y2;
				}

				rightMenu.show(e.getComponent(), xStart, yStart);
				jdfTree.addTreeSelectionListener(m_treeSelectionListener);
			}
		}

		@Override
		public void mouseClicked(final MouseEvent event)
		{
			final EditorDocument ed = m_frame.getEditorDoc();
			if (ed == null)
			{
				return;
			}

			final TreePath path = ed.getSelectionPath();
			if (path == null)
			{
				return;
			}
			if (event.isControlDown() || event.isShiftDown())
			{
				return;
			}

			final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
			final int clickCount = event.getClickCount();
			final KElement kElement = (clickCount == 2) ? getPathTarget() : node.getElement();
			if (kElement == null)
			{
				if (!node.isElement())
				{
					modifyAttribute();
				}
				goToPath(path);
			}
		}
	}

	protected void modifyAttribute()
	{
		final TreePath path = Editor.getEditorDoc().getSelectionPath();
		if (path != null)
		{
			final JDFTreeNode attrNode = (JDFTreeNode) path.getLastPathComponent();
			final String oldVal = attrNode.getValue();
			final String attributeName = attrNode.getName();
			if (!attrNode.isElement())
			{
				final KElement element = attrNode.getElement();

				final VString vValues = (element instanceof JDFElement) ? ((JDFElement) element).getNamesVector(attributeName) : null;
				String selectedValue = null;

				if (vValues != null && !vValues.isEmpty())
				{
					vValues.sort();
					vValues.add("Other..");
					String defVal;
					if (!vValues.contains(oldVal))
					{
						defVal = vValues.stringAt(0);
					}
					else
					{
						defVal = oldVal;
					}

					selectedValue = (String) JOptionPane.showInputDialog(this, m_littleBundle.getString("ChooseAttValueKey"), m_littleBundle.getString("ModifyAttValueKey"), JOptionPane.QUESTION_MESSAGE, null, vValues.toArray(), defVal);
				}
				else
				{
					selectedValue = JOptionPane.showInputDialog(this, m_littleBundle.getString("InsertNewAttValueKey"), oldVal);
				}
				if (selectedValue != null && selectedValue.equals("Other.."))
				{
					selectedValue = JOptionPane.showInputDialog(this, m_littleBundle.getString("InsertNewAttValueKey"), oldVal);
				}
				if ((selectedValue != null) && !(selectedValue.equals(oldVal)))
				{
					Editor.getModel().setAttribute((JDFTreeNode) attrNode.getParent(), attributeName, selectedValue, null, false);
					Editor.getModel().nodeChanged(attrNode);
				}
			}

			final ModifyAttrEdit edit = new ModifyAttrEdit(path, attrNode, oldVal);
			m_frame.undoSupport.postEdit(edit);
		}
	}

	/**
	 * inserts element before selected node
	 * @param iPos -1: before, 0: into, 1: after
	 */
	public void insertElementAtSelectedNode(final int iPos)
	{
		final TreePath path = m_frame.getEditorDoc().getSelectionPath();
		if (path != null)
		{
			JDFTreeNode beforeNode = null;
			JDFTreeNode parentNode = null;

			if (iPos == -1)
			{
				beforeNode = (JDFTreeNode) path.getLastPathComponent();
				parentNode = (JDFTreeNode) beforeNode.getParent();
			}
			else if (iPos == 0)
			{
				parentNode = (JDFTreeNode) path.getLastPathComponent();
			}
			else if (iPos == 1)
			{
				beforeNode = (JDFTreeNode) path.getLastPathComponent();
				parentNode = (JDFTreeNode) beforeNode.getParent();
				beforeNode = (JDFTreeNode) beforeNode.getNextSibling();
			}

			final JDFTreeModel model = Editor.getModel();
			model.insertElementBefore(parentNode, beforeNode);
		}
	}

	/**
	 * Find the node in the m_jdfTree after clicking in the validation list
	 * @param path - path to the node you want to find
	 */
	public void findInNode(final String path)
	{
		if (path != null && !path.equals(JDFConstants.EMPTYSTRING))
		{
			final JDFTreeNode theRoot = (JDFTreeNode) Editor.getModel().getRootNode().getFirstChild();

			if (path.equals(theRoot.getXPath()))
			{
				goToPath(new TreePath(theRoot.getPath()));
				return;
			}
			final Enumeration<JDFTreeNode> e = theRoot.depthFirstEnumeration();
			int i = 0;
			while (e.hasMoreElements())
			{
				i++;
				final JDFTreeNode node = e.nextElement();
				if (node.matchesPath(path))
				{
					goToPath(new TreePath(node.getPath()));
					return;
				}
			}
		}

		// nothing found or null, clear it
		getJDFTree().clearSelection();
	}

	/**
	 * Possibility for user to choose ResourceName he wants to insert into the parentNode All resource names are the allowed resources for this parentNode + user
	 * defined "Other.." choice
	 * 
	 * @param parentNode - parentNode we want to insert the resource into
	 * @return String - resourceName
	 */
	public String chooseResourceName(final JDFNode parentNode)
	{

		final VString vValid = parentNode.linkNames();
		//TODO evaluate cardinality
		//		final VString vInfo = parentNode.linkInfo();
		if (vValid == null)
		{
			return null;
		}
		vValid.unify();
		final int size = vValid.size();
		final String validValues[] = new String[size + 1];

		for (int i = 0; i < size; i++)
		{
			validValues[i] = vValid.stringAt(i);
		}
		validValues[size] = "zzzzzz";
		Arrays.sort(validValues);
		validValues[size] = "Other..";

		String selectedResource = (String) JOptionPane.showInputDialog(this, "Choose a resource to insert", "Insert new resource", JOptionPane.PLAIN_MESSAGE, null, validValues, validValues[0]);

		if (selectedResource != null && (selectedResource.equals("Other..") || selectedResource.equals("*")))
		{
			selectedResource = JOptionPane.showInputDialog(this, "Insert new resource", "");
		}
		return selectedResource;
	}

	/**
	 * choose Resource to link
	 * @param jdfNode where ResourceLink must be added
	 * @param usage input, output or any
	 * @return - selected ResourceName
	 */
	public JDFResource chooseResourceToLink(final JDFNode jdfNode, final EnumUsage usage)
	{
		JDFResource resource = null;
		final VElement rVector = EditorUtils.getResourcesAllowedToLink(jdfNode, usage);
		final String[] resourceName_Id = new String[rVector.size()];
		for (int i = 0; i < rVector.size(); i++)
		{
			final JDFResource res = (JDFResource) rVector.item(i);
			resourceName_Id[i] = res.getNodeName() + ", " + res.getAttribute("ID");
		}

		// we may have removed all...
		String selectedResource = null;
		if (resourceName_Id.length > 0)
		{
			selectedResource = (String) JOptionPane.showInputDialog(this, "Choose the resource you want to link", "Insert new resource link", JOptionPane.PLAIN_MESSAGE, null, resourceName_Id, resourceName_Id[0]);
		}

		if (selectedResource != null && !selectedResource.equals(JDFConstants.EMPTYSTRING))
		{
			for (int i = 0; i < resourceName_Id.length && resource == null; i++)
			{
				if (resourceName_Id[i].equals(selectedResource))
				{
					resource = (JDFResource) rVector.item(i); // __Lena__ falsch if sort resourceName_Id !!!
				}
			}
		}
		return resource;
	}

	/**
	 * 
	 * get the tree view
	 * @return
	 */
	public JViewport getTreeView()
	{
		return m_treeView;
	}

	/**
	 * Finds and sets focus on a node in the Tree View when a node is selected in the In & Output View
	 * @param searchNode - The Node from the In & Output View which you want to find in the Tree View
	 */
	void findNode(final JDFTreeNode searchNode)
	{
		if (searchNode == null)
		{
			return;
		}

		final JDFTreeNode theRoot = (JDFTreeNode) Editor.getModel().getRootNode().getFirstChild();

		boolean bRoot = false;
		final Enumeration e = theRoot.depthFirstEnumeration();
		JDFTreeNode nodeFound = null;
		if (theRoot.equals(searchNode))
		{
			nodeFound = searchNode;
			bRoot = true;
		}

		while (nodeFound == null && e.hasMoreElements())
		{
			final JDFTreeNode node = (JDFTreeNode) e.nextElement();
			if (node.equals(searchNode))
			{
				nodeFound = node;
			}
			else if (!node.isElement() && !searchNode.isElement() && node.getParent() != null && searchNode.getParent() != null)
			{
				final KElement nElem = ((JDFTreeNode) node.getParent()).getElement();
				final KElement sElem = ((JDFTreeNode) searchNode.getParent()).getElement();
				if (nElem.equals(sElem))
				{
					if (searchNode.isInherited())
					{
						nodeFound = Editor.getModel().getInhNode(node, searchNode);
					}
				}
			}
		}

		if (nodeFound != null)
		{
			goToPath(new TreePath(nodeFound.getPath()));
			if (m_frame.m_topTabs.getSelectedIndex() == m_frame.m_topTabs.m_PROC_INDEX)
			{
				m_frame.m_buttonBar.m_upOneLevelButton.setEnabled(!bRoot);
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates the m_dialog for Input of XPath. Selects in the TreeView the node with defined XPath. If XPath does not exist - displays Error message
	 */
	synchronized public void findXPathElem()
	{
		final EditorDocument ed = Editor.getEditorDoc();
		if (ed == null)
		{
			return;
		}

		lastPath = JOptionPane.showInputDialog(this, "Input XPath", lastPath);
		if (lastPath == null || lastPath.equals(JDFConstants.EMPTYSTRING))
		{
			JOptionPane.showMessageDialog(this, "XPath was not chosen", "Error", JOptionPane.ERROR_MESSAGE);
			lastPath = "/JDF";
		}
		else
		{
			final KElement r = ((JDFTreeNode) ed.getRootNode().getFirstChild()).getElement();
			final int atPos = lastPath.indexOf(JDFConstants.AET);
			String message = null;
			// String findPath=lastPath;
			if (atPos > 0 && lastPath.charAt(atPos - 1) != '[') // attribute and not element search qualifier e.g. e[@a="b"]
			{
				try
				{
					final String attr = r.getXPathAttribute(lastPath, null);
					if (attr == null)
					{
						message = "No attribute with XPath found: " + lastPath;
					}
				}
				catch (final JDFException exc)
				{
					message = exc.getMessage();
				}

			}
			else
			// element
			{
				try
				{
					final KElement el = r.getXPathElement(lastPath);
					if (el == null)
					{
						message = "No element with XPath found: " + lastPath;
					}
					else
					{
						// findPath=
						el.buildXPath(null, 1);
					}
				}
				catch (final JDFException exc)
				{
					message = exc.getMessage();
				}
				catch (final IllegalArgumentException exc)
				{
					message = exc.getMessage();
				}
			}
			if (message != null)
			{
				ed.getJDFTree().clearSelection();
				JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				findInNode(lastPath);
			}
		}
	}

	/**
	 * 
	 * get thr currently selected path
	 * @return
	 */
	public TreePath getSelectionPath()
	{
		final EditorDocument ed = m_frame.getEditorDoc();
		return ed == null ? null : ed.getSelectionPath();
	}

	// //////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new link, that links to an already existing resource in the m_jdfTree
	 * @param usage - resource link usage
	 */
	public void insertResourceLink(final EnumUsage usage)
	{
		final TreePath path = getJDFTree().getSelectionPath();
		if (path != null)
		{
			JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
			KElement parent = node.getElement();

			while (parent != null && !(parent instanceof JDFNode))
			{
				parent = parent.getParentNode_KElement();
				node = (JDFTreeNode) node.getParent();
			}

			if (parent != null && parent instanceof JDFNode)
			{
				final JDFNode jdfNode = (JDFNode) parent;
				final boolean hasResourceLinkPool = jdfNode.hasChildElement("ResourceLinkPool", null);

				final JDFResource resource = chooseResourceToLink(jdfNode, usage);
				if (resource != null)
				{
					// linkResource also moves Resource to the closest Ancestor!!! Lena
					final JDFResourceLink resLink = jdfNode.linkResource(resource, usage, null);
					if (resLink != null)
					{
						final JDFTreeNode resLinkNode = Editor.getModel().insertNewResourceLinkNode(jdfNode, node, hasResourceLinkPool, resLink);
						if (resLinkNode != null)
						{
							final InsertResourceLinkEdit edit = new InsertResourceLinkEdit(m_frame, jdfNode, node, hasResourceLinkPool, resLink, resLinkNode);
							m_frame.undoSupport.postEdit(edit);
						}
						else
						{
							JOptionPane.showMessageDialog(this, "Insert ResourceLink operation was not completed." + "\nInternal error occured", "Insert ResourceLink", JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Insert ResourceLink operation was aborted", "Insert ResourceLink", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	/**
	 * Method insertResourceWithLink
	 * @param withLink - if true insert Resource + Link, false - insert only Resource
	 * @param input - resource link usage, true - input, false - output
	 */
	public void insertResourceWithLink(final boolean withLink, final boolean input)
	{
		final TreePath path = m_frame.getEditorDoc().getSelectionPath();
		if (path != null)
		{
			JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
			KElement parent = node.getElement();

			while (parent != null && !(parent instanceof JDFNode))
			{
				parent = parent.getParentNode_KElement();
				node = (JDFTreeNode) node.getParent();
			}

			JDFTreeNode resNode = null;
			JDFTreeNode resLinkNode = null;
			if (parent != null && parent instanceof JDFNode)
			{
				final JDFNode jdfNode = (JDFNode) parent;
				final boolean hasResourcePool = jdfNode.hasChildElement(ElementName.RESOURCEPOOL, null);
				final boolean hasResourceLinkPool = jdfNode.hasChildElement(ElementName.RESOURCELINKPOOL, null);

				final String selectedResource = chooseResourceName(jdfNode);

				if (selectedResource != null && !selectedResource.equals(JDFConstants.EMPTYSTRING))
				{
					EnumUsage usage = null;
					if (withLink)
					{
						usage = input ? EnumUsage.Input : EnumUsage.Output;
					}
					resNode = Editor.getModel().insertNewResourceNode(jdfNode, node, selectedResource, hasResourcePool, usage);
					if (withLink)
					{
						final VElement v = jdfNode.getResourceLinkPool().getPoolChildren(selectedResource + "Link", null, "");
						if (v.size() > 0)
						{
							final JDFResourceLink resLink = (JDFResourceLink) v.get(v.size() - 1);
							resLinkNode = Editor.getModel().insertNewResourceLinkNode(jdfNode, node, hasResourceLinkPool, resLink);
						}
					}
					if (resNode != null) // resLinkNode may be == null in case when withLink == false
					{
						final InsertResourceAndLinkEdit edit = new InsertResourceAndLinkEdit(m_frame, node, hasResourcePool, hasResourceLinkPool, resNode, resLinkNode);
						m_frame.undoSupport.postEdit(edit);
					}
					else
					{
						JOptionPane.showMessageDialog(this, "Insert Resource operation was not completed." + "\nInternal error occured", "Insert Resource", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	/**
	 * inserts Attribute into node, implements undo, redo for this action
	 */
	public void insertAttrItem()
	{
		final TreePath path = Editor.getEditorDoc().getSelectionPath();
		if (path != null)
		{
			final JDFTreeNode intoNode = (JDFTreeNode) path.getLastPathComponent();
			final JDFTreeNode attrNode = Editor.getModel().insertAttributeIntoDoc(intoNode);
			if (attrNode != null)
			{
				final InsertAttrEdit edit = new InsertAttrEdit(path, attrNode);
				m_frame.undoSupport.postEdit(edit);
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////

	private JTree getJDFTree()
	{
		final EditorDocument ed = Editor.getEditorDoc();
		return ed == null ? null : ed.getJDFTree();
	}

	/**
	 * 
	 * @return
	 */
	public KElement getPathTarget()
	{
		final EditorDocument ed = m_frame.getEditorDoc();
		if (ed == null)
		{
			return null;
		}

		final TreePath path = ed.getSelectionPath();
		if (path == null)
		{
			return null;
		}

		final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
		final KElement kElement = node.getElement();
		KElement kElementTarget = null;
		if (node.isElement())
		{

			if ((kElement instanceof JDFResourceLink) || (kElement instanceof JDFRefElement) || (kElement instanceof JDFPart) || (kElement instanceof JDFCreated)
					|| (kElement instanceof JDFModified))
			{
				kElementTarget = getLinkTarget(kElement);
			}

			else if (kElement instanceof JDFDevCaps && kElement.hasAttribute(AttributeName.DEVCAPREF))
			{
				kElementTarget = ((JDFElement) kElement).getTarget_JDFElement(kElement.getAttribute(AttributeName.DEVCAPREF), AttributeName.ID);
			}
			else if (kElement instanceof JDFDevCap && kElement.hasAttribute(AttributeName.DEVCAPREFS))
			{
				kElementTarget = kElement.getTarget_KElement(kElement.getAttribute(AttributeName.DEVCAPREFS), AttributeName.ID);
			}
		}
		else
		// attribute
		{
			final String name = node.getName();
			String lowerCase = name.toLowerCase();
			if (lowerCase.endsWith("ref") || lowerCase.endsWith("refs"))
			{
				String value = node.getValue();
				value = StringUtil.token(value, 0, " ");
				kElementTarget = kElement.getTarget_KElement(value, AttributeName.ID);

			}
		}
		if (kElementTarget != null)
		{
			findNode(new JDFTreeNode(kElementTarget));
		}

		return kElementTarget;
	}

	private KElement getLinkTarget(final KElement kElement)
	{
		KElement target = null;
		if (kElement instanceof JDFResourceLink)
		{
			target = ((JDFResourceLink) kElement).getTarget();
			if (target == null)
			{
				target = ((JDFResourceLink) kElement).getLinkRoot();
			}
		}
		else if (kElement instanceof JDFRefElement)
		{
			target = ((JDFRefElement) kElement).getTarget();
			if (target == null)
			{
				target = ((JDFRefElement) kElement).getTargetRoot();
			}
		}
		else if ((kElement instanceof JDFCreated) || (kElement instanceof JDFModified) || (kElement instanceof JDFDeleted))
		{
			final String rRef = kElement.getAttribute("XPath");
			if (rRef != null)
			{
				final JDFElement j = (JDFElement) kElement;
				target = j.getParentJDF().getXPathElement(rRef);
			}
		}
		else if (kElement instanceof JDFPart)
		{
			final KElement parentTarget = getLinkTarget(kElement.getParentNode_KElement());
			if (parentTarget instanceof JDFResource)
			{
				final JDFResource r = (JDFResource) parentTarget;
				final JDFPart part = (JDFPart) kElement;
				target = r.getPartition(part.getPartMap(), null);
				if (target == null)
				{
					target = r;
				}
			}
		}
		return target;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////
}
