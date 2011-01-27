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
package org.cip4.jdfeditor.swtui.tabs;

import java.util.ResourceBundle;
import java.util.Vector;

import org.cip4.jdfeditor.Editor;
import org.cip4.jdfeditor.ProcessPart;
import org.cip4.jdfeditor.swtui.EditorSwtMain;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.resource.JDFResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

public class ProcessViewTab extends Canvas implements ISelectionChangedListener
{
	private static ResourceBundle bundle = EditorSwtMain.bundle;
	private TreeViewer treeViewer;
	private static int x = 10;
	private static int y = 10;
	private JDFNode jdfNode;
	
	protected Vector<ProcessPartWidget> vParts = new Vector<ProcessPartWidget>();
	private ProcessPartWidget parentPart = null;
	
	private int pPartStart = 10;
	private final int esX = 50;
	private final int esY = 30; // The Empty Space between Nodes
	
	private Color red;

	public ProcessViewTab(Composite parent, int style)
	{
		super(parent, style | SWT.V_SCROLL | SWT.H_SCROLL);
		final Point origin = new Point(0, 0);
		final ScrollBar vBar = getVerticalBar();
		vBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e)
			{
				int vSelection = vBar.getSelection();
				int destY = -vSelection - origin.y;
				Rectangle rect = getBounds();
				scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y = -vSelection;
			}
		});
	}
	
	public void setTreeViewer(TreeViewer treeViewer)
	{
		this.treeViewer = treeViewer;
		treeViewer.addSelectionChangedListener(this);
		red = new Color(null, 255, 0, 0);
		
		addDisposeListener(new DisposeListener() {
	         public void widgetDisposed(DisposeEvent e) {
//	        	 ProcessViewTab.this.widgetDisposed(e);
	        	 red.dispose();
	         }
		});
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e)
			{
				ProcessViewTab.this.paintControl(e);
			}
		});
	}
	
	public void paintControl(PaintEvent e)
	{
//		System.out.println("paintControl e: " + e);
		e.gc.drawRectangle(x++, y++, 40, 40);
		
//		ProcessPanel.drawProcessView()
		drawProcessView();
//		ProcessPanel.paintComponent()
		paintParent(e.gc);
//		TODO: paintPoints(e.gc);
		paintParts(e.gc);
	}
	
	public void drawProcessView()
	{
		JDFNode rootJDF = jdfNode;
		
		setupVariables();
		
		VElement vJDFNodes = new VElement();
		vJDFNodes.add(rootJDF);

		vJDFNodes.addAll(rootJDF.getChildElementVector("JDF", null, null, false, -1, false));

		final Vector _vParts = new Vector();
		parentPart = addPart(new ProcessPartWidget(new Composite(this, SWT.BORDER), SWT.BORDER, rootJDF, ProcessPartWidget.PARENT, null));

		drawInputLinks(parentPart);
		drawSubResLinks(vJDFNodes, _vParts);
		
		if (rootJDF.hasChildElement("JDF", null))
		{
			drawParts(_vParts);
		}
	}
	
	private void drawParts(final Vector _vParts)
	{
		int pWidth = 0;
		int pHeight = 0;
		final Vector vSorted = sortPanels(_vParts);

		int pLasty = y = 90;
		int pLastx = x;
//		Loop over all the Sorted JDF Nodes
		for (int i = 0; i < vSorted.size(); i++)
		{
			Vector vChain = (Vector) vSorted.get(i);
			x = pLastx;
			y = 90 + pHeight;
			pLasty = y;
			for (int ic = 0; ic < vChain.size(); ic++)
			{
				final ProcessPartWidget node = (ProcessPartWidget) vChain.get(ic);

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
	private int drawInResParts(final ProcessPartWidget node)
	{
		int wRes = 0;
		final Vector vInRes = node.getvInRes();
		for (int j = 0; j < vInRes.size(); j++)
		{
			final ProcessPartWidget inputPart = (ProcessPartWidget) vInRes.get(j);
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
	private int drawOutResParts(final ProcessPartWidget node)
	{
		int wRes;
		final Vector vOutRes = node.getvOutRes();
		wRes = 0;
		for (int j = 0; j < vOutRes.size(); j++)
		{
			final ProcessPartWidget outputPart = (ProcessPartWidget) vOutRes.get(j);
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
	 * Sorts the panels for the process view so that those who share resources
	 * are next to another
	 * 
	 * @param panelVector
	 * @return Vector
	 */
	private Vector sortPanels(Vector _v)
	{
		Vector v = (Vector) _v.clone();
		Vector vRet = new Vector();
		Vector<ProcessPartWidget> vUnlinked = new Vector<ProcessPartWidget>();
		while (v.size() > 0)
		{
			ProcessPartWidget p = (ProcessPartWidget) v.elementAt(v.size() - 1);
			JDFNode n = (JDFNode) p.getElem();
			Vector vTmp = new Vector();
			vTmp.add(p);
			v.remove(p);
			Vector vPred = n.getPredecessors(true, false);
			Vector vPost = n.getPredecessors(false, false);
			for (int j = v.size() - 1; j >= 0; j--)
			{
				ProcessPartWidget p2 = (ProcessPartWidget) v.elementAt(j);
				JDFNode n2 = (JDFNode) p2.getElem();
				if (vPred != null && vPred.contains(n2))
				{
					vTmp.add(0, p2);
					v.remove(p2);
				} else if (vPost != null && vPost.contains(n2))
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
	 * @param g
	 */
	private void paintParent(GC gc)
	{
		if (parentPart != null)
		{
//			TODO: g.setFont(parentPart.getFont());
			gc.setForeground(parentPart.getgColor());
			gc.fillRoundRectangle(parentPart.getxPos(), parentPart.getyPos(), parentPart.rawWidth, parentPart.rawHeight, 25, 25);
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			gc.drawRoundRectangle(parentPart.getxPos(), parentPart.getyPos(), parentPart.rawWidth, parentPart.rawHeight, 25, 25);

			final String[] s = parentPart.getgString();
			final int xMarg = 15;
			int yMarg = 2;

			for (int i = 0; i < s.length; i++)
			{
				yMarg += 15;
				gc.drawString(s[i], parentPart.getxPos() + xMarg, parentPart.getyPos() + yMarg);
			}

			double zoom = 1; // TODO: Editor.getEditorDoc().getZoom();
			parentPart.setBounds((int) (parentPart.getxPos() * zoom),
					(int) (parentPart.getyPos() * zoom),
					(int) (parentPart.rawWidth * zoom),
					(int) (parentPart.rawHeight * zoom));
//			TODO: add(parentPart, -1);
		}
	}

	private void paintParts(GC gc)
	{
		for (int i = 0; i < vParts.size(); i++)
		{
			final ProcessPartWidget part = (ProcessPartWidget) vParts.get(i);
			if (part == parentPart)
				continue;

//			TODO: g.setFont(part.getFont());
			gc.setBackground(part.getgColor());

			if (part.getElem() instanceof JDFNode)
			{
//            	LineAttributes la = new LineAttributes();
            	
//            	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
//            	gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
            	
            	FontMetrics fm = gc.getFontMetrics();
            	fm.getAverageCharWidth();
            	Point p = gc.stringExtent("Test string");
            	
//            	gc.setBackground(new Color(null, 215, 245, 255));
            	System.out.println("x: " + part.getxPos() +
            			", y: " + part.getyPos() +
            			", w: " + part.rawWidth +
            			", h: " + part.rawHeight);
            	gc.fillRoundRectangle(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight, 10, 10);
//            	gc.fillRoundRectangle(10, 20, 30, 40, 10, 10);
//            	gc.drawRectangle(50, 50, 100, 100);
            	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
            	gc.drawRoundRectangle(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight, 10, 10);
			}
			else
			{
				gc.fillRectangle(part.getxPos(), part.getyPos(), part.rawWidth, part.rawHeight);
				gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			}
        }
	}
	
	private ProcessPartWidget addPart(ProcessPartWidget part)
	{
		final int partPos = vParts.indexOf(part);
		if (partPos >= 0)
			return (ProcessPartWidget) vParts.elementAt(partPos);
//		TODO: part.addMouseListener(new PartListener());
		vParts.add(part);
		return part;
	}
	
	private void drawInputLinks(ProcessPartWidget rootPart)
	{
		JDFNode rootJDF = (JDFNode) rootPart.getElem();
		final VElement vInputLinks = rootJDF.getResourceLinks(null, new JDFAttributeMap(AttributeName.USAGE, EnumUsage.Input), null);
		int w = 0;
		if (vInputLinks != null && !vInputLinks.isEmpty()) {
			int size = vInputLinks.size();
			Vector<ProcessPartWidget> vTmp = new Vector<ProcessPartWidget>();

			for (int i = 0; i < size; i++)
			{
				final JDFResourceLink link = (JDFResourceLink) vInputLinks.item(i);
				final JDFResource r = link.getLinkRoot();
				if (r != null) // simply ignore unlinked resourcelinks
				{
					final ProcessPartWidget inputPart = addPart(new ProcessPartWidget(new Composite(this, SWT.BORDER), SWT.BORDER, r, ProcessPartWidget.RES_EXTERNAL, link));
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
				final ProcessPartWidget nodePart = addPart(new ProcessPartWidget(new Composite(this, SWT.BORDER), SWT.BORDER, nodeElem, ProcessPart.NODE, null));

				final JDFResourceLinkPool resLinkPool = nodeElem
						.getResourceLinkPool();
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
	 * @param nodePart
	 * @param links
	 * @param j
	 */
	private void prepareResLink(final ProcessPartWidget nodePart, JDFResourceLink rL)
	{
		JDFResource resource = rL.getTarget();
		if (resource != null)
		{
			resource = resource.getResourceRoot();
			ProcessPartWidget res = addPart(new ProcessPartWidget(new Composite(this, SWT.BORDER), SWT.BORDER, resource, ProcessPart.RESOURCE, rL));
			final EnumUsage usage = rL.getUsage();
			if (EnumUsage.Input.equals(usage))
			{
				nodePart.addTovInRes(res);
			} else if (EnumUsage.Output.equals(usage))
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

	private void rescale(int _width, Vector<ProcessPartWidget> vTmp)
	{
		final int size = vTmp.size();
		for (int i = 0; i < size; i++)
		{
			final ProcessPartWidget inputPart = (ProcessPartWidget) vTmp.get(i);
			inputPart.rawWidth = _width;
		}
	}
	
	public void selectionChanged(SelectionChangedEvent e)
	{
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		Object o = ts.getFirstElement();
		if (o instanceof JDFNode)
		{
			jdfNode = (JDFNode) o;
//			System.out.println("ProcessViewTab2 is: " + is);
			redraw();
		}
	}

}
