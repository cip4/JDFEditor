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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;
import org.cip4.tools.jdfeditor.view.renderer.JDFTreeNode;

/**
 * 
 * @author SvenoniusI
 */
public class ProcessPanel extends JPanel
{
	private static final Log LOGGER = LogFactory.getLog(ProcessPanel.class);

	private class PartListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				if (e.getClickCount() == 2)
				{
					getProcessSearchNode(e.getSource());
					final ProcessPart processPart = ((ProcessPart) e.getSource());
					final KElement element = processPart.getElem();
					drawNewRoot(element);
				}
				else if (e.getClickCount() == 1)
				{
					setSelPart(e);
				}
			}
		}
	}

	private static final long serialVersionUID = 5217687675396163701L;
	// various variables needed for drawing
	private int x = 0;
	private int y = 0;
	private int pPartStart = 10;

//	The Empty Space between Nodes
	private final int esX = 50;
	private final int esY = 30;

	protected Vector<ProcessPart> vParts = new Vector<ProcessPart>();
	private ProcessPart parentPart = null;

	public ProcessPanel()
	{
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		final EditorDocument editorDoc = MainView.getEditorDoc();
		if (editorDoc == null)
			return;
		double zoom = editorDoc.getZoom();
		((Graphics2D) g).scale(zoom, zoom);

		paintParent(g);
		paintPoints(g);
		paintParts(g);
	}

	/**
	 * @param g
	 */
	private void paintParent(Graphics g)
	{
		if (parentPart != null)
		{
			g.setFont(parentPart.getFont());
			g.setColor(parentPart.getgColor());
			g.fillRoundRect(parentPart.getxPos(), parentPart.getyPos(), parentPart.rawWidth, parentPart.rawHeight, 25, 25);
			g.setColor(Color.black);
			g.drawRoundRect(parentPart.getxPos(), parentPart.getyPos(), parentPart.rawWidth, parentPart.rawHeight, 25, 25);

			final String[] s = parentPart.getgString();
			final int xMarg = 15;
			int yMarg = 2;

			for (int i = 0; i < s.length; i++)
			{
				yMarg += 15;
				g.drawString(s[i], parentPart.getxPos() + xMarg, parentPart.getyPos() + yMarg);
			}

			double zoom = MainView.getEditorDoc().getZoom();
			parentPart.setBounds((int) (parentPart.getxPos() * zoom), (int) (parentPart.getyPos() * zoom), (int) (parentPart.rawWidth * zoom), (int) (parentPart.rawHeight * zoom));
			add(parentPart, -1);
		}
	}

	/**
	 * @param g
	 */
	private void paintPoints(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		BasicStroke stroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);

		final int partSize = vParts.size();
		for (int selTop = 0; selTop < 2; selTop++)
		{
			g2.setColor(selTop == 0 ? Color.black : Color.red);

			for (int i = 0; i < partSize; i++)
			{
				final ProcessPart part = vParts.get(i);
				for (int k = 0; k < 2; k++)
				{
					final Vector v = k == 0 ? part.getvInRes() : part.getvOutRes();
					if (v != null && !v.isEmpty())
					{
						if (part.style != ProcessPart.NODE && part.style != ProcessPart.PARENT)
							throw new JDFException("oops");

						final Point p1 = k == 0 ? part.getLeftPoint() : part.getRightPoint();
						final int size = v.size();
						for (int j = 0; j < size; j++)
						{
							ProcessPart inPart = (ProcessPart) v.elementAt(j);
							if (part.isSelected || inPart.isSelected)
							{
								if (selTop == 0)
									continue;
							}
							else
							{
								if (selTop == 1)
									continue;
							}
							final Point p2 = k == 0 ? inPart.getRightPoint() : inPart.getLeftPoint();
							paintArrowLine(g2, k == 0 ? p2 : p1, k == 0 ? p1 : p2);
						}
					}
				}
			}
		}
	}

	/**
	 * @param g2
	 * @param p1
	 * @param p2
	 */
	private void paintArrowLine(Graphics2D g2, final Point p1, final Point p2)
	{
		final int arrowLength = 8;
		final int arrowWidth = 6;
		final int arcRadius = 6;
		final int x1 = (int) p1.getX();
		final int y1 = (int) p1.getY();
		final int x2 = (int) p2.getX();
		final int y2 = (int) p2.getY();
		final int xm = x1 < x2 ? x2 - 20 : x2 + 20;

		// line
		g2.drawLine(x1, y1, xm, y1);
		g2.drawLine(xm, y1, xm, y2);
		g2.drawLine(xm, y2, x2, y2);

		//arc
		final int angle = x1 < x2 ? 270 : 90;
		g2.fillArc(x1 - arcRadius, y1 - arcRadius, 2 * arcRadius, 2 * arcRadius, angle, 180);

		//arrow
		final int xa = x1 < x2 ? x2 - arrowLength : x2 + arrowLength;
		final int arrowX[] = { xa, x2, xa };
		final int arrowY[] = { y2 - arrowWidth, y2, y2 + arrowWidth };
		g2.fillPolygon(arrowX, arrowY, 3);
	}

	/**
	 * @param g
	 */
	private void paintParts(Graphics g)
	{
		for (int i = 0; i < vParts.size(); i++)
		{
			final ProcessPart part = vParts.get(i);
			if (part == parentPart)
				continue;

			g.setFont(part.getFont());
			g.setColor(part.getgColor());

			if (part.getElem() instanceof JDFNode)
			{
				g.fillRoundRect(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight, 10, 10);
				g.setColor(Color.black);
				g.drawRoundRect(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight, 10, 10);
			}
			else
			{
				g.fillRect(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight);
				g.setColor(Color.black);
				Graphics2D g2 = (Graphics2D) g;
				if (part.isSelected)
				{
					g2.setStroke(new BasicStroke(3));
				}
				else
				{
					g2.setStroke(new BasicStroke(1));
				}
				g2.drawRect(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight);
			}
			final String[] s = part.getgString();
			final int xMarg = 15;
			int yMarg = 2;

			for (int j = 0; j < s.length; j++)
			{
				yMarg += 15;
				g.drawString(s[j], part.getxPos() + xMarg, part.getyPos() + yMarg);
			}

			double zoom = MainView.getEditorDoc().getZoom();
			part.setBounds((int) (part.getxPos() * zoom), (int) (part.getyPos() * zoom), (int) (part.rawWidth * zoom), (int) (part.rawHeight * zoom));
			add(part, 0);
		}
	}

	/**
	 * Sorts the panels for the process view so that those who share resources are next to another
	 * @param panelVector
	 * @return Vector
	 */
	private Vector sortPanels(Vector _v)
	{
		Vector v = (Vector) _v.clone();
		Vector vRet = new Vector();
		Vector vUnlinked = new Vector();
		while (v.size() > 0)
		{
			ProcessPart p = (ProcessPart) v.elementAt(v.size() - 1);
			JDFNode n = (JDFNode) p.getElem();
			Vector vTmp = new Vector();
			vTmp.add(p);
			v.remove(p);
			Vector vPred = n.getPredecessors(true, false);
			Vector vPost = n.getPredecessors(false, false);
			for (int j = v.size() - 1; j >= 0; j--)
			{
				ProcessPart p2 = (ProcessPart) v.elementAt(j);
				JDFNode n2 = (JDFNode) p2.getElem();
				if (vPred != null && vPred.contains(n2))
				{
					vTmp.add(0, p2);
					v.remove(p2);
				}
				else if (vPost != null && vPost.contains(n2))
				{
					vTmp.add(p2);
					v.remove(p2);
				}
			}
			if (vTmp.size() > 1 || n.getResourceLinks(null) != null)
				vRet.add(vTmp);
			else
				vUnlinked.add(p);
		}
		if (!vUnlinked.isEmpty())
			vRet.add(vUnlinked);
		return vRet;
	}

	/**
	 * return true if part is already in the list  
	 * @param part the ProcessPart to check
	 * @return
	 */
	private boolean hasPart(ProcessPart part)
	{
		return vParts.indexOf(part) >= 0;
	}

	/**
	 * if part is already in the list return the existing, else add and return
	 * @param part the ProcessPart to add
	 * @return
	 */
	private ProcessPart addPart(ProcessPart part)
	{
		final int partPos = vParts.indexOf(part);
		if (partPos >= 0)
			return vParts.elementAt(partPos);
		part.addMouseListener(new PartListener());
		vParts.add(part);
		return part;
	}

	/**
	 * Set zoom.
	 * @param c - possible values are:
	 * <ol>
	 * <li>"+" to increase 10%,
	 * <li>"-" to decrease 10%,
	 * <li>"o" for original,
	 * <li>"b" for "best", aka "fit frame".
	 * </ol>
	 */
	public void setZoom(final char c)
	{
		final EditorDocument editorDoc = MainView.getEditorDoc();
		if (editorDoc == null)
			return;
		double zoom = editorDoc.getZoom();
		Dimension d = calcSize();

		if (c == '+')
		{
			if (zoom >= EditorButtonBar.MAX_ALLOWED_ZOOM)
				return;
			zoom += 0.1;
		}
		else if (c == '-')
		{
			if (zoom <= EditorButtonBar.MIN_ALLOWED_ZOOM)
				return;
			zoom -= 0.1;
		}
		else if (c == 'o')
			zoom = EditorButtonBar.DEFAULT_ZOOM;
		else if (c == 'b')
		{
			final Dimension screen = getParent().getSize();
			final double wFactor = (screen.getWidth() - 15) / d.width;
			final double hFactor = (screen.getHeight() - 40) / d.height;

			zoom = wFactor < hFactor ? wFactor : hFactor;
		}
		editorDoc.setZoom(zoom);
		MainView.getFrame().m_buttonBar.updateZoomButtons(zoom);

		revalidate();
		d.width *= zoom;
		d.height *= zoom;
		setPreferredSize(d);
		repaint();
	}

	public void clear()
	{
		vParts.clear();
		parentPart = null;
		setLayout(null);
		removeAll();
		setBackground(Color.white);
	}

	/**
	 * Method goUpOneLevelInProcessView.
	 * takes the selected node in the m_jdfTree an goes up one level in the
	 * Process View
	 */
	public void goUpOneLevelInProcessView()
	{
		if (parentPart == null)
			return;

		KElement kElement = JDFElement.getParentJDF(parentPart.getElem());
		drawNewRoot(kElement);
	}

	/**
	 * 
	 */
	private Dimension calcSize()
	{
		int _x = 0;
		int _y = 0;
		for (int i = 0; i < vParts.size(); i++)
		{
			ProcessPart p = vParts.elementAt(i);
			_x = Math.max(_x, p.getxPos() + p.rawWidth);
			_y = Math.max(_y, p.getyPos() + p.rawHeight);
		}
		return new Dimension(_x + 20, _y + 20);
	}

	/**
	 * @param element
	 */
	protected void drawNewRoot(final KElement element)
	{
		if (element == null)
			return;
		JDFFrame m_frame = MainView.getFrame();
		if (element instanceof JDFNode)
		{
			removeAll();
			drawProcessView((JDFNode) element);
			JDFTreeNode node = new JDFTreeNode(element);
			m_frame.getJDFTreeArea().findNode(node);
		}
		final JDFTreeNode rootNode = m_frame.getRootNode();
		m_frame.m_buttonBar.m_upOneLevelButton.setEnabled(!element.equals(rootNode == null ? null : ((JDFTreeNode) rootNode.getFirstChild()).getElement()));
	}

	/**
	 * Initiate the Process View.
	 */
	void initProcessView()
	{
		clear();
		JDFFrame m_frame = MainView.getFrame();
		EditorDocument ed = MainView.getEditorDoc();
		if (ed == null)
			return;

		EditorDocument eDoc = MainView.getEditorDoc();

		KElement kElement;

		JTree m_jdfTree = ed.getJDFTree();
		JDFTreeNode node = null;
		if (m_jdfTree != null && ed.getJDFDoc() != null)
		{
			if (m_jdfTree.getSelectionPath() == null)
			{
				node = (JDFTreeNode) m_frame.getRootNode().getFirstChild();
				kElement = ((JDFTreeNode) m_frame.getRootNode().getFirstChild()).getElement();
			}
			else
			{
				node = (JDFTreeNode) m_jdfTree.getSelectionPath().getLastPathComponent();
				kElement = ((JDFTreeNode) m_jdfTree.getSelectionPath().getLastPathComponent()).getElement();
			}

			eDoc.setSelectionPath(new TreePath(node.getPath()), true);

			if (!(kElement instanceof JDFNode))
			{
				kElement = JDFElement.getParentJDF(kElement);
			}
			drawNewRoot(kElement);
		}
	}

	/**
	 * @param rootJDF
	 */
	private void drawInputLinks(ProcessPart rootPart)
	{
		JDFNode rootJDF = (JDFNode) rootPart.getElem();
		final VElement vInputLinks = rootJDF.getResourceLinks(null, new JDFAttributeMap(AttributeName.USAGE, EnumUsage.Input), null);
		int w = 0;
		if (vInputLinks != null && !vInputLinks.isEmpty())
		{
			int size = vInputLinks.size();
			Vector vTmp = new Vector();

			for (int i = 0; i < size; i++)
			{
				final JDFResourceLink link = (JDFResourceLink) vInputLinks.item(i);
				final JDFResource r = link.getLinkRoot();
				if (r != null) // simply ignore unlinked resourcelinks
				{
					final ProcessPart inputPart = addPart(new ProcessPart(r, ProcessPart.RES_EXTERNAL, link));
					rootPart.addTovInRes(inputPart);
					inputPart.setPos(x, y);
					y += inputPart.rawHeight + esY;
					w = Math.max(w, inputPart.rawWidth);
					vTmp.add(inputPart);
				}
			}
			rescale(w, vTmp);
		}
		x += w + esX;
		pPartStart = x - 10;
	}

	/**
	 * @param rootJDF
	 */
	private void drawLeafNode(JDFNode rootJDF)
	{
		y = 20;
		final ProcessPart procPart = addPart(new ProcessPart(rootJDF, ProcessPart.NODE, null));
		procPart.setPos(x, y);
		x += procPart.rawWidth + esX;
	}

	/**
	 * Get the node to search for in the Process View.
	 * @param source - The location in the Process View that's been selected
	 * @return the JDFTreeNode that is to be searched for.
	 */
	protected void getProcessSearchNode(Object src)
	{
		final ProcessPart pp = (ProcessPart) src;
		JDFTreeNode node = null;
		try
		{
			final KElement kElement = pp.getElem();
			node = new JDFTreeNode(kElement);
		}
		catch (Exception s)
		{
			JOptionPane.showMessageDialog(this, ResourceUtil.getMessage("FindErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
			s.printStackTrace();
		}
		if (node != null)
			MainView.getFrame().getJDFTreeArea().findNode(node);
	}

	/**
	 * @param rootJDF
	 * @param parentPart
	 */
	private void drawOutputLinks(ProcessPart partRoot)
	{
		int outResStart = parentPart.rawWidth + parentPart.getxPos();

		JDFNode rootJDF = (JDFNode) partRoot.getElem();
		final VElement vOutputLinks = rootJDF.getResourceLinks(null, new JDFAttributeMap(AttributeName.USAGE, EnumUsage.Output), null);
		if (vOutputLinks != null && !vOutputLinks.isEmpty())
		{
			int w = 0;
			y = 20;
			Vector vTmp = new Vector();

			for (int i = 0; i < vOutputLinks.size(); i++)
			{
				final JDFResourceLink outputLink = (JDFResourceLink) vOutputLinks.get(i);
				if (outputLink == null)
					continue;
				JDFResource r = outputLink.getLinkRoot();

				if (r != null)
				{
					ProcessPart outputPart = new ProcessPart(r, ProcessPart.RES_EXTERNAL, outputLink);

					if (!hasPart(outputPart))
					{
						vTmp.add(outputPart);
					}
					if (rootJDF.hasChildElement("JDF", null))
						x = outResStart + 40;
					outputPart = addPart(outputPart);
					outputPart.setPos(x, y);
					partRoot.addTovOutRes(outputPart);
					w = Math.max(w, outputPart.rawWidth);
					y += outputPart.rawHeight + esY;
				}
			}
			rescale(w, vTmp);
			x += w + esX;
		}
	}

	/**
	 * Method drawProcessoView.
	 * draws the process view for the selected node in the m_jdfTree
	 * @param rootJDF
	 */
	public void drawProcessView(final JDFNode rootJDF)
	{
		if (rootJDF == null)
			return;
		MainView.setCursor(1, null);
		clear();

		preparePopUp();
		setupVariables();
		final VElement vJDFNodes = new VElement();
		vJDFNodes.add(rootJDF);
		vJDFNodes.addAll(rootJDF.getChildElementVector("JDF", null, null, false, -1, false));
		final Vector _vParts = new Vector();
		parentPart = addPart(new ProcessPart(rootJDF, ProcessPart.PARENT, null));

		drawInputLinks(parentPart);
		drawSubResLinks(vJDFNodes, _vParts);

		if (rootJDF.hasChildElement("JDF", null))
		{
			drawParts(_vParts);
		}
		else
		{
			drawLeafNode(rootJDF);
		}

		drawOutputLinks(parentPart);

		setPreferredSize(calcSize());
		repaint();
		MainView.setCursor(0, null);
	}

	/**
	 * @param _vParts
	 */
	private void drawParts(final Vector _vParts)
	{
		int pWidth = 0;
		int pHeight = 0;
		final Vector vSorted = sortPanels(_vParts);

		int pLasty = y = 90;
		int pLastx = x;
		// Loop over all the Sorted JDF Nodes
		for (int i = 0; i < vSorted.size(); i++)
		{
			Vector vChain = (Vector) vSorted.get(i);
			x = pLastx;
			y = 90 + pHeight;
			pLasty = y;
			for (int ic = 0; ic < vChain.size(); ic++)
			{
				final ProcessPart node = (ProcessPart) vChain.get(ic);

				int wRes = drawInResParts(node);
				final int yParent = 10;

				pWidth = Math.max(pWidth, x + wRes - esX);
				pHeight = Math.max(pHeight, y);
				if (wRes > 0)
					x += wRes + esX;

				y = pLasty;
				node.setPos(x, y);
				pHeight = Math.max(pHeight, y + node.rawHeight);
				pWidth = Math.max(pWidth, x + node.rawWidth - esX);

				x += node.rawWidth + esX;
				addPart(node);

				wRes = drawOutResParts(node);

				pWidth = Math.max(pWidth, x + wRes);
				pHeight = Math.max(pHeight, y - esY);
				pPartStart = pPartStart == 0 ? yParent : pPartStart;

				parentPart.setPos(pPartStart, yParent);
				parentPart.rawWidth = Math.max(pWidth - pPartStart + yParent, parentPart.rawWidth);
				parentPart.rawHeight = pHeight;
			}
		}
	}

	/**
	 * @param node
	 * @return
	 */
	private int drawInResParts(final ProcessPart node)
	{
		int wRes = 0;
		final Vector vInRes = node.getvInRes();
		for (int j = 0; j < vInRes.size(); j++)
		{
			final ProcessPart inputPart = (ProcessPart) vInRes.get(j);
			if (!inputPart.hasPosition())
			{
				inputPart.setPos(x, y);
				y += inputPart.rawHeight + esY;
				wRes = Math.max(inputPart.rawWidth, wRes);
			}
		}
		return wRes;
	}

	/**
	 * @param node
	 * @return
	 */
	private int drawOutResParts(final ProcessPart node)
	{
		int wRes;
		final Vector vOutRes = node.getvOutRes();
		wRes = 0;
		for (int j = 0; j < vOutRes.size(); j++)
		{
			final ProcessPart outputPart = (ProcessPart) vOutRes.get(j);
			if (!outputPart.hasPosition())
			{

				outputPart.setPos(x, y);
				addPart(outputPart);

				y += outputPart.rawHeight + esY;
				wRes = Math.max(outputPart.rawWidth, wRes);
			}
		}
		return wRes;
	}

	/**
	 * @param vJDFNodes
	 * @param _vParts the vector of JDFNode parts that will be filled
	 */
	private void drawSubResLinks(final VElement vJDFNodes, final Vector _vParts)
	{
		if (vJDFNodes != null)
		{
			final int sizeJDF = vJDFNodes.size();
			for (int i = 1; i < sizeJDF; i++)
			{
				final JDFNode nodeElem = (JDFNode) vJDFNodes.get(i);
				final ProcessPart nodePart = addPart(new ProcessPart(nodeElem, ProcessPart.NODE, null));

				final JDFResourceLinkPool resLinkPool = nodeElem.getResourceLinkPool();
				if (resLinkPool != null)
				{
					final VElement links = resLinkPool.getInOutLinks(null, true, null, null);
					if (links != null)
					{
						final int size = links.size();
						for (int j = 0; j < size; j++)
						{
							prepareResLink(nodePart, (JDFResourceLink) links.elementAt(j));
						}
					}
				}

				_vParts.add(nodePart);
			}
		}
	}

	/**
	 * 
	 */
	private void preparePopUp()
	{
		//create a popup menu for copy to clipboard
		final JPopupMenu jpmPopup = new JPopupMenu();

		//create a "copy top clipboard" item for the popup menus
		final JMenuItem jmiCopyToClipboard = new JMenuItem(ResourceUtil.getMessage("copyToClipboard"));
		jmiCopyToClipboard.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//get the system clipboard
				final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				final Dimension size = getSize();

				final BufferedImage myImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
				final Graphics2D g2 = myImage.createGraphics();
				paint(g2);
				g2.dispose();

				final ImageSelection icontents = new ImageSelection(myImage);

				cb.setContents(icontents, icontents);
			}
		});
		//add the item to the popup menu
		jpmPopup.add(jmiCopyToClipboard);

		//create a mouse event listener for the process area, because
		//the copy action is allowd only for this area
		//listen for both MousePressed and MouseReleased events to
		//ensure functionality on all OSes
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
					jpmPopup.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
					jpmPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	/**
	 * @param nodePart
	 * @param links
	 * @param j
	 */
	private void prepareResLink(final ProcessPart nodePart, JDFResourceLink rL)
	{
		JDFResource resource = rL.getTarget();
		if (resource != null)
		{
			resource = resource.getResourceRoot();
			ProcessPart res = addPart(new ProcessPart(resource, ProcessPart.RESOURCE, rL));
			final EnumUsage usage = rL.getUsage();
			if (EnumUsage.Input.equals(usage))
			{
				nodePart.addTovInRes(res);
			}
			else if (EnumUsage.Output.equals(usage))
			{
				nodePart.addTovOutRes(res);
			}
		}
	}

	/**
	 * 
	 */
	private void setupVariables()
	{
		x = 20;
		y = 20;
		pPartStart = 10;
	}

	/**
	 * @param _width
	 * @param vTmp
	 */
	private void rescale(int _width, Vector vTmp)
	{
		final int size = vTmp.size();
		for (int i = 0; i < size; i++)
		{
			final ProcessPart inputPart = (ProcessPart) vTmp.get(i);
			inputPart.rawWidth = _width;
		}
	}

	/**
	 * @param e
	 */
	protected void setSelPart(MouseEvent e)
	{
		ProcessPart pSel = (ProcessPart) e.getSource();
		final int size = vParts == null ? 0 : vParts.size();
		for (int i = 0; i < size; i++)
		{
			ProcessPart p = vParts.elementAt(i);
			p.isSelected = p.equals(pSel);
		}
		repaint();
	}
}
