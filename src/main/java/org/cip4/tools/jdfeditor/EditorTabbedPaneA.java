/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2013 The International Cooperation for the Integration of 
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

import org.cip4.jdflib.core.JDFComment;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * this handles anything located in the top right tabbed panes
 * it has been misused as a dump from JDFFrame
 */
public class EditorTabbedPaneA extends JTabbedPane
{
	private static final long serialVersionUID = 136801779114140915L;

	protected JDFInOutScroll m_inOutScrollPane;

	// one index per tab
	final public int m_IO_INDEX = 0;
	final public int m_PROC_INDEX = 1;
	final public int m_DC_INDEX = 2;
	final public int m_COM_INDEX = 3;

	private final JTextArea m_commentArea;
	private final JTextArea m_processArea;
	private final JScrollPane m_commentScrollPane;
	private final JScrollPane m_processScrollPane;
	private final JDFDevCapScrollPane m_devCapScrollPane;

	ProcessPanel m_pArea = new ProcessPanel();

	/**
	 * sets up the top right frame
	 *  
	 */
	public EditorTabbedPaneA()
	{
		setBorder(BorderFactory.createLineBorder(Color.black));

		m_inOutScrollPane = new JDFInOutScroll();

		addTab(ResourceUtil.getMessage("NextNeighbourKey"), null, m_inOutScrollPane, ResourceUtil.getMessage("NextNeighbourKey"));
		setComponentAt(m_IO_INDEX, m_inOutScrollPane);
		setSelectedIndex(m_IO_INDEX);

		m_processArea = new JTextArea();
		m_processArea.setEditable(false);

		m_processScrollPane = new JScrollPane();
		m_processScrollPane.getViewport().add(m_processArea, null);
		m_processScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		m_processScrollPane.getHorizontalScrollBar().setUnitIncrement(20);

		addTab(ResourceUtil.getMessage("ProcessViewKey"), null, m_processScrollPane, ResourceUtil.getMessage("ProcessViewKey"));
		setComponentAt(m_PROC_INDEX, m_processScrollPane);
		m_processScrollPane.getViewport().add(m_pArea, null);
		SwingUtilities.updateComponentTreeUI(m_processScrollPane);

		m_devCapScrollPane = new JDFDevCapScrollPane();

		addTab(ResourceUtil.getMessage("DevCapViewKey"), null, m_devCapScrollPane, ResourceUtil.getMessage("DevCapViewKey"));
		setComponentAt(m_DC_INDEX, m_devCapScrollPane);

		m_commentArea = new JTextArea();
		m_commentArea.setEditable(true);
		m_commentArea.setBackground(Color.white);

		m_commentScrollPane = new JScrollPane();
		m_commentScrollPane.getViewport().add(m_commentArea, null);
		m_commentScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		m_commentScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
		addTab(ResourceUtil.getMessage("CommentViewKey"), null, m_commentScrollPane, ResourceUtil.getMessage("CommentViewKey"));
		setComponentAt(m_COM_INDEX, m_commentScrollPane);
		setEnabledAt(m_COM_INDEX, false);

		final MouseAdapter tabbedPaneListener = new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				onSelect();
				EditorDocument ed = MainView.getEditorDoc();
				if (ed != null)
					ed.setTopTab(getSelectedIndex());
			}
		};
		addMouseListener(tabbedPaneListener);
	}

	public JDFInOutScroll getInOutScrollPane()
	{
		return m_inOutScrollPane;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean processAreaIsNull()
	{
		if (m_pArea == null || getSelectedIndex() != m_PROC_INDEX || MainView.getFrame().getJDFTreeArea().getTreeView().getComponent(0).getClass().equals(JTextArea.class))
			return true;

		return false;
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public boolean inOutIsNull()
	{
		if (m_inOutScrollPane.m_inOutArea == null || getSelectedIndex() != m_IO_INDEX
				|| MainView.getFrame().getJDFTreeArea().getTreeView().getComponent(0).getClass().equals(JTextArea.class))
			return true;

		return false;
	}

	/**
	 * Shows the text in the Comment View, if there is any text to show.
	 */
	void showComment()
	{
		JDFFrame m_frame = MainView.getFrame();

		EditorDocument ed = m_frame.getEditorDoc();
		if (ed.getSelectionPath() != null)
		{
			final TreePath path = ed.getSelectionPath();
			final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();

			if (node.isElement())
			{
				final KElement jdfElem = node.getElement();
				if (jdfElem instanceof JDFComment)
				{
					final String txt = jdfElem.getTextContent().trim();
					m_commentArea.setText(txt.equals("") ? ResourceUtil.getMessage("EmptyCommentKey") : txt);
					setEnabledAt(m_COM_INDEX, true);
					setSelectedIndex(m_COM_INDEX);
				}
				else
					setEnabledAt(m_COM_INDEX, false);
			}
			else
				setEnabledAt(m_COM_INDEX, false);

			m_commentScrollPane.getViewport().add(m_commentArea);
			setComponentAt(m_COM_INDEX, m_commentScrollPane);

			SwingUtilities.updateComponentTreeUI(m_commentScrollPane);
		}
	}

	/**
	 * 
	 *  
	 * @param eDoc
	 */
	public void refreshView(EditorDocument eDoc)
	{

		m_inOutScrollPane.clearInOutView();
		if (eDoc == null)
			return;
		if (eDoc.getTopTab() != getSelectedIndex())
		{
			setSelectedIndex(eDoc.getTopTab());
			onSelect();
		}
		JDFDoc jdfDoc = eDoc.getJDFDoc();
		final KElement rootElement = jdfDoc.getRoot();
		final int selectedIndex = getSelectedIndex();
		if (rootElement instanceof JDFNode)
		{
			if (selectedIndex == m_PROC_INDEX)
				m_pArea.drawProcessView((JDFNode) rootElement);
		}
		else
		{
			m_pArea.clear(); // clear processView area
		}

		if (selectedIndex == m_IO_INDEX)
			m_inOutScrollPane.initInOutView(eDoc);
	}

	/**
	 * Method clearViews.
	 * clear all views before opening a new file
	 */
	void clearViews()
	{
		m_inOutScrollPane.clearInOutView();
		m_pArea.removeAll();
		m_pArea.repaint();
		m_commentArea.setText("");
		m_commentArea.repaint();
		setEnabledAt(m_COM_INDEX, false);
	}

	/**
	 * 
	 */
	public void onSelect()
	{
		final int selectedIndex = getSelectedIndex();
		final boolean bProcSel = selectedIndex == m_PROC_INDEX;
		JDFFrame m_frame = MainView.getFrame();
		m_frame.m_buttonBar.setEnableZoom(1.1);
		if (bProcSel)
		{
			m_pArea.initProcessView();
		}
		else if (selectedIndex == m_IO_INDEX)
		{
			m_inOutScrollPane.clearInOutView();
			m_inOutScrollPane.initInOutView(null);
		}
		else if (selectedIndex == m_COM_INDEX)
		{
			showComment();
		}
		else if (selectedIndex == m_DC_INDEX)
		{
			// __Lena__ TBD - display DeviceCap Editor
		}
	}
}