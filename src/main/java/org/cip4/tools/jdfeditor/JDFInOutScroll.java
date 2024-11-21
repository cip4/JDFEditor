/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2016 The International Cooperation for the Integration of 
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.tools.jdfeditor.service.RuntimeProperties;
import org.cip4.tools.jdfeditor.util.EditorUtils;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;
import org.cip4.tools.jdfeditor.view.renderer.JDFResourceTreeCellRenderer;
import org.cip4.tools.jdfeditor.view.renderer.JDFTreeNode;

/**
 * 
 * scroll area for the input / output view
 * @author rainer prosi
 * @date pre Oct 8, 2011
 */
public class JDFInOutScroll extends JScrollPane
{
	private static final long serialVersionUID = 8635330186484361532L;

	private static final String TITLE_DEFAULT = "";
	private static final String TITLE_JDF_ELEMENT = ResourceUtil.getMessage("JDFElementKey");
	private static final String TITLE_RESOURCE = ResourceUtil.getMessage("ResourceKey");
	private static final String TITLE_INPUT_RESOURCE = ResourceUtil.getMessage("InputResourceKey");
	private static final String TITLE_OUTPUT_RESOURCE = ResourceUtil.getMessage("OutputResourceKey");
	private static final String TITLE_JDF_PRODUCER = ResourceUtil.getMessage("JDFProducerKey");
	private static final String TITLE_JDF_CONSUMER = ResourceUtil.getMessage("JDFConsumerKey");

	JPanel m_inOutArea;
	private final JPanel m_inOutAreaLeft;
	private final JPanel m_inOutAreaMiddle;
	private final JPanel m_inOutAreaRight;
	private JDFTreeNode m_searchInOutNode;
	private int m_inTreePos = 0;
	private int m_outTreePos = 0;

	private JLabel leftLabel;
	private JLabel middleLabel;
	private JLabel rightLabel;

	public JDFInOutScroll()
	{
		m_inOutArea = new JPanel();
		m_inOutArea.setLayout(null);
		m_inOutArea.setBackground(Color.white);

		m_inOutAreaLeft = new JPanel();
		m_inOutAreaLeft.setLayout(null);
		m_inOutAreaLeft.setBackground(Color.white);
		m_inOutArea.add(m_inOutAreaLeft);

		m_inOutAreaMiddle = new JPanel();
		m_inOutAreaMiddle.setLayout(null);
		m_inOutAreaMiddle.setBackground(Color.white);
		m_inOutArea.add(m_inOutAreaMiddle);

		m_inOutAreaRight = new JPanel();
		m_inOutAreaRight.setLayout(null);
		m_inOutAreaRight.setBackground(Color.white);
		m_inOutArea.add(m_inOutAreaRight);

		m_inOutArea.add(Box.createHorizontalGlue());
		getViewport().add(m_inOutArea, null);
		getVerticalScrollBar().setUnitIncrement(20);
		getHorizontalScrollBar().setUnitIncrement(20);
	}

	/**
	 * 
	 * Draws the In & Output View Area with its components, except for the Input/JDF Producer and Output/JDF Consumer Trees. *
	 * @param element The element you wish to draw in the In & Output View
	 * @param lStr The Left Title
	 * @param mStr The Middle Title
	 * @param rStr The Right Title
	 */
	private void drawInOutView(final KElement element, final String lStr, final String mStr, final String rStr)
	{
		final Dimension d = getSize();
		final int w = d.width / 3;

		leftLabel = getTitleLabel(lStr, w);
		middleLabel = getTitleLabel(mStr, w);
		rightLabel = getTitleLabel(rStr, w);

		m_inOutAreaLeft.add(leftLabel);
		m_inOutAreaMiddle.add(middleLabel);
		m_inOutAreaRight.add(rightLabel);

		final JTree mTree = getInOutNodes(element);
		ToolTipManager.sharedInstance().registerComponent(mTree);
		mTree.setShowsRootHandles(false);
		m_inOutAreaMiddle.add(mTree);
		final int mHeight = mTree.getPreferredSize().height;
		mTree.setBounds(5, 50, w - 10, mHeight);

		m_inOutAreaLeft.setBounds(0, 0, w, m_inTreePos);
		m_inOutAreaMiddle.setBounds(w, 0, w, mHeight + 50);
		m_inOutAreaRight.setBounds(2 * w, 0, w, m_outTreePos);

		final int h = m_inTreePos < m_outTreePos ? m_outTreePos : m_inTreePos;
		final Dimension dim = new Dimension(d.width - 20, h < mHeight + 50 ? mHeight + 50 : h);
		m_inOutArea.setPreferredSize(dim);

		m_inOutArea.repaint();
		getViewport().add(m_inOutArea, null);

		final JDFFrame m_frame = MainView.getFrame();
		final EditorTabbedPaneA editorTabbedPaneA = m_frame.m_topTabs;
		editorTabbedPaneA.setComponentAt(editorTabbedPaneA.m_IO_INDEX, this);
		editorTabbedPaneA.setSelectedIndex(editorTabbedPaneA.m_IO_INDEX);
	}

	/**
	 * Creates a JLabel for the titles in the In & Output View
	 * @param title - The text on the JLabel
	 * @param width - The width of the JLabel
	 * @return A JLabel with the title text
	 */
	private JLabel getTitleLabel(final String title, final int width)
	{
		final JLabel label = new JLabel(title);

		Font labelFont = new Font(null, Font.PLAIN, RuntimeProperties.enlargedTextFontSize);
		label.setFont(labelFont);

		label.setBackground(Color.white);
		label.setForeground(Color.black);
		label.setBounds(5, 0, width, 50);

		return label;
	}

	/**
	 * Adds the Input/JDF Producer and Output/JDF Consumer Trees to the In & Output View. Called numerous times to draw all the different trees
	 * @param elem - The element you want to add as a Tree to the In & Output View
	 * @param isJDFElem - Is the element a JDF element?
	 */
	private void addResourceTree(final KElement elem, final boolean isJDFElem)
	{
		final Dimension d = getSize();
		final int w = d.width / 3;
		final String usage = elem.getAttribute("Usage", null, "");
		final String rRef = elem.getAttribute("rRef", null, "");

		KElement res = null;
		if (isJDFElem)
		{
			res = elem.getTarget_KElement(rRef, "ID");
		}
		else
		{
			res = (KElement) elem.getParentNode().getParentNode();
		}

		if (usage.equals("Input") == isJDFElem)
		{
			final JTree inTree = getInOutNodes(res);
			m_inOutAreaLeft.add(inTree);
			ToolTipManager.sharedInstance().registerComponent(inTree);
			inTree.setBounds(5, m_inTreePos, w - 10, inTree.getPreferredSize().height);
			m_inTreePos += inTree.getPreferredSize().height + 10;
		}
		if (usage.equals("Output") == isJDFElem)
		{
			final JTree outTree = getInOutNodes(res);
			m_inOutAreaRight.add(outTree);
			ToolTipManager.sharedInstance().registerComponent(outTree);
			outTree.setBounds(5, m_outTreePos, w - 10, outTree.getPreferredSize().height);
			m_outTreePos += outTree.getPreferredSize().height + 10;
		}
	}

	/**
	 * Searching after a string in the next neighbour view, starting from the selected node.
	 * @param inString - The String to search for
	 * @param forwardDirection - Search forward or backward?
	 * @param bIgnoreCase
	 */
	public void findStringInNeighbourTree(final String inString, final boolean forwardDirection, final boolean bIgnoreCase)
	{
		MainView.setCursor(1, null);
		final JDFFrame m_frame = MainView.getFrame();
		if (m_frame.m_searchTree != null && m_searchInOutNode != null && inString != null && !inString.equals(JDFConstants.EMPTYSTRING))
		{
			boolean found = false;
			String searchString = inString;
			if (bIgnoreCase)
			{
				searchString = searchString.toUpperCase();
			}
			final JPanel[] areaArray = { m_inOutAreaLeft, m_inOutAreaMiddle, m_inOutAreaRight };
			JPanel areaPanel;
			boolean finishedFirstSearch = false;
			JTree lastSelectedTree = m_frame.m_searchTree;

			if (forwardDirection)
			{
				for (int j = 0; j < areaArray.length; j++)
				{
					areaPanel = areaArray[j];
					final int count = areaPanel.getComponentCount() - 1;

					for (int i = 0; i < count; i++)
					{
						final Component component2 = areaPanel.getComponent(i);
						if (!(component2 instanceof JTree))
						{
							continue;
						}

						final JTree tmpTree = (JTree) component2;

						if (finishedFirstSearch)
						{
							m_frame.m_searchTree = tmpTree;
							m_searchInOutNode = (JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent();
						}
						if (tmpTree.equals(m_frame.m_searchTree))
						{
							if (((JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent()).equals(m_searchInOutNode))
							{
								final Enumeration<JDFTreeNode> e = m_searchInOutNode.preorderJdfEnumeration();

								if (!finishedFirstSearch)
								{
									e.nextElement();
								}

								while (e.hasMoreElements())
								{
									final JDFTreeNode checkNode = e.nextElement();
									String tmpString = checkNode.toString();
									if (bIgnoreCase)
									{
										tmpString = tmpString.toUpperCase();
									}

									if (tmpString.indexOf(searchString) != -1)
									{
										lastSelectedTree.removeSelectionPath(lastSelectedTree.getSelectionPath());
										m_searchInOutNode = checkNode;
										m_frame.m_searchTree = tmpTree;
										final TreePath path = new TreePath(checkNode.getPath());
										lastSelectedTree = tmpTree;
										tmpTree.makeVisible(path);
										tmpTree.setSelectionPath(path);
										tmpTree.scrollPathToVisible(path);
										found = true;
										break;
									}
								}
							}
							else
							{
								JDFTreeNode checkNode = m_searchInOutNode;

								while (checkNode.getNextSibling() != null)
								{
									checkNode = (JDFTreeNode) checkNode.getNextSibling();
									String tmpString = checkNode.toString();
									if (bIgnoreCase)
									{
										tmpString = tmpString.toUpperCase();
									}
									if (tmpString.indexOf(searchString) != -1)
									{
										lastSelectedTree.removeSelectionPath(lastSelectedTree.getSelectionPath());
										m_searchInOutNode = checkNode;
										m_frame.m_searchTree = tmpTree;
										final TreePath path = new TreePath(checkNode.getPath());
										lastSelectedTree = tmpTree;
										tmpTree.makeVisible(path);
										tmpTree.setSelectionPath(path);
										tmpTree.scrollPathToVisible(path);
										found = true;
										break;
									}
								}
							}
							if (found)
							{
								break;
							}

							finishedFirstSearch = true;
						}
					}
					if (found)
					{
						break;
					}
				}
			}
			else
			{
				boolean lastWasRoot = false;
				for (int j = areaArray.length - 1; j >= 0; j--)
				{
					areaPanel = areaArray[j];
					final int nr = areaPanel.getComponentCount() - 1;
					final int count = 0;

					for (int i = nr; i >= count; i--)
					{
						final Component component2 = areaPanel.getComponent(i);
						if (!(component2 instanceof JTree))
						{
							continue;
						}

						final JTree tmpTree = (JTree) component2;

						if (finishedFirstSearch)
						{
							m_frame.m_searchTree = tmpTree;
							final JDFTreeNode tmpNode = (JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent();
							if (tmpNode.getChildCount() != 0)
							{
								m_searchInOutNode = (JDFTreeNode) tmpNode.getLastChild();
							}
							else
							{
								m_searchInOutNode = tmpNode;
							}
						}
						if (tmpTree.equals(m_frame.m_searchTree))
						{
							if (!((JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent()).equals(m_searchInOutNode))
							{
								final Enumeration<JDFTreeNode> e = ((JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent()).preorderJdfEnumeration();
								final Stack<JDFTreeNode> tmpStack = new Stack<JDFTreeNode>();

								while (e.hasMoreElements())
								{
									tmpStack.push(e.nextElement());
								}
								JDFTreeNode checkNode;
								while (!tmpStack.isEmpty())
								{
									checkNode = m_searchInOutNode;

									if (!lastWasRoot)
									{
										checkNode = tmpStack.pop();
									}

									if (checkNode.equals(m_searchInOutNode))
									{
										while (!tmpStack.isEmpty())
										{
											checkNode = tmpStack.pop();
											String tmpString = checkNode.toString();
											if (bIgnoreCase)
											{
												tmpString = tmpString.toUpperCase();
											}

											if (tmpString.indexOf(searchString) != -1)
											{
												lastSelectedTree.removeSelectionPath(lastSelectedTree.getSelectionPath());
												m_searchInOutNode = checkNode;
												//												((JLabel) ((Box) ((Box) m_frame.m_dialog.getContentPane().getComponent(1)).getComponent(7)).getComponent(1)).setText(" ");
												final TreePath path = new TreePath(checkNode.getPath());
												lastSelectedTree = tmpTree;
												tmpTree.makeVisible(path);
												tmpTree.setSelectionPath(path);
												tmpTree.scrollPathToVisible(path);
												found = true;
												break;
											}
										}
										if (found)
										{
											break;
										}
									}
								}
								lastWasRoot = false;
							}
							else
							{
								lastWasRoot = true;
							}

							finishedFirstSearch = true;
						}
						if (found)
						{
							break;
						}
					}
					if (found)
					{
						break;
					}
				}
			}
			if (!found)
			{
				EditorUtils.errorBox("StringNotFoundKey", inString);
			}
		}
		MainView.setCursor(0, null);
	}

	/**
	 * 
	 * @return
	 */
	public JTree findIt()
	{
		boolean found = false;
		JTree searchTree = null;
		if (!found)
		{
			for (int i = 0; i < m_inOutAreaLeft.getComponentCount() && !found; i++)
			{
				final Component comp = m_inOutAreaLeft.getComponent(i);
				if (comp instanceof JTree && comp.isFocusOwner())
				{
					searchTree = (JTree) comp;
					m_searchInOutNode = (JDFTreeNode) ((JTree) comp).getSelectionPath().getLastPathComponent();
					found = true;
				}
			}
		}
		if (!found)
		{
			if (m_inOutAreaMiddle.getComponentCount() > 1)
			{
				final Component comp = m_inOutAreaMiddle.getComponent(1);
				if (comp instanceof JTree && comp.isFocusOwner())
				{
					searchTree = (JTree) comp;
					m_searchInOutNode = (JDFTreeNode) ((JTree) comp).getSelectionPath().getLastPathComponent();
					found = true;
				}
			}
		}
		if (!found)
		{
			for (int i = 0; i < m_inOutAreaRight.getComponentCount() && !found; i++)
			{
				final Component comp = m_inOutAreaRight.getComponent(i);
				if (comp instanceof JTree && comp.isFocusOwner())
				{
					searchTree = (JTree) comp;
					m_searchInOutNode = (JDFTreeNode) ((JTree) comp).getSelectionPath().getLastPathComponent();
					found = true;
				}
			}
		}
		return searchTree;
	}

	/**
	 * clear the input ouput view
	 * 
	 */
	public void clearInOutView()
	{
		m_inOutAreaLeft.removeAll();
		m_inOutAreaRight.removeAll();
		m_inOutAreaMiddle.removeAll();
		m_inOutArea.validate();
		m_inOutArea.repaint();
	}

	/**
	 * Creates the In & Output View.
	 */
	public void initInOutView(EditorDocument eDoc)
	{
		if (eDoc == null)
		{
			eDoc = MainView.getEditorDoc();
		}

		if (eDoc == null)
		{
			return;
		}

		MainView.setCursor(1, null);
		final TreePath path = eDoc.getSelectionPath();

		JDFTreeNode node = null;
		final JDFFrame m_frame = MainView.getFrame();

		if (path != null)
		{
			node = (JDFTreeNode) path.getLastPathComponent();
		}
		else if (m_frame.getJDFTreeArea() != null)
		{
			final JDFTreeNode rootNode = eDoc.getRootNode();
			node = rootNode == null ? null : (JDFTreeNode) rootNode.getFirstChild();
		}
		if (node == null)
		{
			MainView.setCursor(0, null);
			return;
		}

		eDoc.setSelectionPath(new TreePath(node.getPath()), true);

		final KElement root = eDoc.getJDFDoc().getRoot(); // check whether JMF,
		if (node.isElement() && (root instanceof JDFNode))
		{
			final KElement kElement = node.getElement();
			m_inTreePos = m_outTreePos = 50;
			SwingUtilities.updateComponentTreeUI(this);

			if (kElement != null)
			{
				boolean isJDFNode = false;

				if (kElement instanceof JDFNode)
				{
					isJDFNode = true;
					final JDFNode n = (JDFNode) kElement;

					if (kElement.hasChildElement(ElementName.RESOURCELINKPOOL, null))
					{
						final JDFResourceLinkPool resourceLinkPool = n.getResourceLinkPool();
						if (resourceLinkPool != null && resourceLinkPool.hasChildNodes())
						{
							final VElement resourceLinks = resourceLinkPool.getPoolChildren(null, null, null);

							for (int i = 0; i < resourceLinks.size(); i++)
							{
								final JDFResourceLink link = (JDFResourceLink) resourceLinks.item(i);
								if (link.getLinkRoot() != null)
								{
									addResourceTree(link, isJDFNode);
								}
							}
						}
					}
					drawInOutView(kElement, TITLE_INPUT_RESOURCE, TITLE_JDF_ELEMENT, TITLE_OUTPUT_RESOURCE);
				}
				else if (kElement instanceof JDFResource && (kElement.hasChildElements() || kElement.hasAttributes()))
				{
					final JDFResource r = (JDFResource) kElement;

					if (root instanceof JDFNode) // not in JMF
					{
						String id = r.getID();
						VElement vProcs = ((JDFNode) root).getvJDFNode(null, null, false);

						for (int i = 0; i < vProcs.size(); i++)
						{
							final JDFNode jdfNode = (JDFNode) vProcs.elementAt(i);
							final VElement resourceLinks = jdfNode.getResourceLinks(new JDFAttributeMap(AttributeName.RREF, id));
							final int size = resourceLinks == null ? 0 : resourceLinks.size();
							for (int j = 0; j < size; j++)
							{
								final JDFResourceLink link = (JDFResourceLink) resourceLinks.elementAt(j);
								addResourceTree(link, isJDFNode);
							}
						}
					}
					drawInOutView(kElement, TITLE_JDF_PRODUCER, TITLE_RESOURCE, TITLE_JDF_CONSUMER);
				}
				else
				{
					String mTitle = kElement.getLocalName();
					drawInOutView(kElement, TITLE_DEFAULT, mTitle, TITLE_DEFAULT);
				}
			}
		}
		MainView.setCursor(0, null);
	}

	/**
	 * Creates the Trees from the Nodes in the In & Output View
	 * @param rootElement - The element you want to create a Tree from
	 * @return The JTree
	 */
	private JTree getInOutNodes(final KElement rootElement)
	{
		final JDFTreeNode mRoot = new JDFTreeNode(rootElement);
		final JTree resTree = new JTree(mRoot);
		final JDFTreeModel treeModel = new JDFTreeModel(mRoot, true);
		treeModel.addNodeAttributes(mRoot);
		resTree.setModel(treeModel);

		final MouseAdapter mouseListener = new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent e)
			{
				final JTree tree = (JTree) e.getSource();

				if (SwingUtilities.isLeftMouseButton(e) && !e.isControlDown())
				{
					final JDFFrame m_frame = MainView.getFrame();

					final TreePath path = tree.getSelectionPath();
					if (path != null)
					{
						m_frame.getJDFTreeArea().findNode((JDFTreeNode) path.getLastPathComponent());
					}
				}
				else if (SwingUtilities.isRightMouseButton(e) || e.isControlDown())
				{
					final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if (path != null)
					{
						tree.setSelectionRow(tree.getRowForPath(path));
						final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();

						if (node.isElement())
						{
							final JDFFrame m_frame = MainView.getFrame();

							m_frame.getJDFTreeArea().findNode(node);
							clearInOutView();
							initInOutView(null);
						}
					}
				}
			}
		};
		resTree.addMouseListener(mouseListener);
		ToolTipManager.sharedInstance().registerComponent(resTree);
		resTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		final JDFResourceTreeCellRenderer resourceRenderer = new JDFResourceTreeCellRenderer();
		resTree.setCellRenderer(resourceRenderer);
		resTree.setRowHeight(0);
		resTree.setBackground(Color.white);

		return resTree;
	}

}
