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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.KElement;
import org.cip4.tools.jdfeditor.pane.HttpServerPane;
import org.cip4.tools.jdfeditor.service.RuntimeProperties;
import org.cip4.tools.jdfeditor.util.FontUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;


/**
 * 
 *  
 * @author rainer prosi
 * @date Apr 11, 2013
 */
public class EditorTabbedPaneB extends JTabbedPane implements Runnable
{
	private static final long serialVersionUID = -6813043793787501763L;
	final public int m_VAL_ERRORS_INDEX = 0;
	final public int m_SCHEMA_ERRORS_INDEX = 1;
	final public int m_DC_ERRORS_INDEX = 2;
	final public int m_XML_EDITOR_INDEX = 3;

	JDFDevCapErrScrollPane m_devCapErrScroll;
	public CheckJDFScrollPane m_validErrScroll;
	public SchemaScrollPane m_SchemaErrScroll;

	//    Pane containing XML editor
	private final RSyntaxTextArea xmlEditorTextArea;
	private final HttpServerPane httpPanel;

	public EditorTabbedPaneB()
	{
		setBorder(BorderFactory.createLineBorder(Color.black));

		m_validErrScroll = new CheckJDFScrollPane();
		addTab(ResourceUtil.getMessage("ValidationResultKey"), null, m_validErrScroll, ResourceUtil.getMessage("ValidationResultKey"));
		setComponentAt(m_VAL_ERRORS_INDEX, m_validErrScroll);

		m_SchemaErrScroll = new SchemaScrollPane();
		addTab(ResourceUtil.getMessage("SchemaOutputKey"), null, m_SchemaErrScroll, ResourceUtil.getMessage("SchemaOutputKey"));
		setComponentAt(m_SCHEMA_ERRORS_INDEX, m_SchemaErrScroll);

		m_devCapErrScroll = new JDFDevCapErrScrollPane();
		addTab(ResourceUtil.getMessage("DevCapOutputKey"), null, m_devCapErrScroll, ResourceUtil.getMessage("DevCapOutputKey"));
		setComponentAt(m_DC_ERRORS_INDEX, m_devCapErrScroll);

		//        XML Editor tab
		xmlEditorTextArea = createXMLPane();

		//		HTTP server tab
		httpPanel = new HttpServerPane();
		new Thread(this).start();
	}

	/**
	 * 
	 * @return
	 */
	private RSyntaxTextArea createXMLPane()
	{
		//TODO link xml editor with jdf editor dom model and synchronize updates
		JPanel xmlEditorPanel = new JPanel(new BorderLayout());
		RSyntaxTextArea xmlArea = new RSyntaxTextArea();
		xmlArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		xmlArea.setAutoIndentEnabled(true);

		if (RuntimeProperties.enlargedTextFontName.isEmpty())
		{
			Font originalFont = xmlArea.getFont();
			RuntimeProperties.originalTextFontSize = originalFont.getSize();
			RuntimeProperties.enlargedTextFontName = originalFont.getFontName();
			FontUtil.calcFontSize();
		}

		Font xmlAreaFont = new Font(RuntimeProperties.enlargedTextFontName, Font.PLAIN, RuntimeProperties.enlargedTextFontSize);
		xmlArea.setFont(xmlAreaFont);

		xmlArea.setEditable(false);
		RTextScrollPane sp = new RTextScrollPane(xmlArea);
		xmlEditorPanel.add(sp);
		addTab(ResourceUtil.getMessage("XmlEditor"), null, xmlEditorPanel, ResourceUtil.getMessage("XmlEditor"));
		return xmlArea;
	}

	/**
	 * 
	 *  
	 * @param path
	 */
	public void refreshView(TreePath path)
	{
		if (path == null)
		{
			clearViews();
		}
		else
		{
			selectNodeWithXPath(path);
		}
	}

	public void refreshXmlEditor(String s)
	{
		xmlEditorTextArea.setText(s);
		xmlEditorTextArea.setCaretPosition(0);
	}

	public void updateXmlEditorFontSize()
	{
		Font xmlAreaFont = new Font(RuntimeProperties.enlargedTextFontName, Font.PLAIN, RuntimeProperties.enlargedTextFontSize);
		xmlEditorTextArea.setFont(xmlAreaFont);
	}

	public void updateCheckJDFFontSize()
	{
		m_validErrScroll.updateCellRenderer();
	}

	public void updateSchemaFontSize()
	{
		m_SchemaErrScroll.updateCellRenderer();
	}

	public void selectNodeWithXPath(TreePath path)
	{
		JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
		JDFTreeNode errNode = findNodeWithXPath(node.getXPath());

		JTree errTree = null;
		TreeSelectionListener selLi = null;
		if (getSelectedIndex() == m_SCHEMA_ERRORS_INDEX)
		{
			errTree = m_SchemaErrScroll.m_reportTree;
			selLi = m_SchemaErrScroll.m_SelectionListener;
		}
		else if (getSelectedIndex() == m_VAL_ERRORS_INDEX)
		{
			errTree = m_validErrScroll.m_reportTree;
			selLi = m_validErrScroll.m_SelectionListener;
		}
		else
		{
			errTree = m_devCapErrScroll.m_reportTree;
			selLi = m_devCapErrScroll.m_SelectionListener;
		}
		if (errTree != null)
		{
			errTree.removeTreeSelectionListener(selLi);
			TreePath p = null;
			if (errNode == null)
			{
				JDFTreeNode theRoot = (JDFTreeNode) errTree.getModel().getRoot();
				p = new TreePath(theRoot.getPath());
				errTree.collapsePath(p);
			}
			else
			{
				p = new TreePath(errNode.getPath());
				errTree.expandPath(p);
			}
			errTree.makeVisible(p);
			final int row = errTree.getRowForPath(p);
			errTree.setSelectionRow(row);
			errTree.scrollRowToVisible(row);
			errTree.addTreeSelectionListener(selLi);
		}
	}

	/**
	 * 
	 * @param xpath
	 * @return
	 */
	private JDFTreeNode findNodeWithXPath(String xpath)
	{
		JTree errTree = null;
		if (this.getSelectedIndex() == m_VAL_ERRORS_INDEX)
		{
			//JDFTreeNode
			errTree = m_validErrScroll.m_reportTree;
		}
		else
		{
			errTree = m_devCapErrScroll.m_reportTree;
		}
		if (errTree == null)
			return null;

		JDFTreeNode theRoot = (JDFTreeNode) errTree.getModel().getRoot();
		if (xpath == null)
			return theRoot;

		if (theRoot == null)
			return null;

		final Enumeration<JDFTreeNode> e = theRoot.depthFirstJdfEnumeration();
		while (e.hasMoreElements())
		{
			JDFTreeNode tn = e.nextElement();
			if (tn.isElement())
			{
				KElement el = tn.getElement();
				if (xpath.equals(el.getAttribute("XPath")))
					return tn;
			}
		}
		return null;
	}

	/**
	 * Clear all views before opening a new file.
	 */
	public void clearViews()
	{
		m_devCapErrScroll.clearReport();
		m_validErrScroll.clearReport();
		m_SchemaErrScroll.clearReport();
	}

	public HttpServerPane getHttpPanel()
	{
		return httpPanel;
	}

	/** 
	 * take this guy offline
	 */
	@Override
	public void run()
	{
		addTab(ResourceUtil.getMessage("HTTPserver"), null, getHttpPanel().createPane(), ResourceUtil.getMessage("HTTPserver"));
	}

}
