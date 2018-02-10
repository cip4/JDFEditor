/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2018 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor.view.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.JDFTreeNode;
import org.cip4.tools.jdfeditor.service.RuntimeProperties;

/**
 * Abstract tree renderer class which renders jdf nodes and attributes to tree elements.
 */
public abstract class AbstractTreeCellRenderer extends DefaultTreeCellRenderer
{
	private final Log LOGGER = getLogger();

	private static final String ICON_ROOT_PATH = "/org/cip4/tools/jdfeditor/icons/treeview/";
	private static final Color COLOR_SELECTED = new Color(110, 200, 240);
	private static final Color COLOR_DEFAULT = new Color(255, 255, 255);

	/**
	 * All icons available for the tree view.
	 */
	protected enum TreeIcon
	{

		NODE_JDF("node_jdf.png"), NODE_XJDF("node_xjdf.png"), NODE_REF("node_link.png"), NODE_REF_IN("node_link.png"), NODE_REF_OUT("node_link.png"), NODE_WARN(
				"WarnElemIcon.gif"), NODE_ERR("ErrorElemIcon.gif"), NODE_DEFAULT("node.png"), ATTR_PARTKEYS("leaf_keys.png"), ATTR_INHERITED(
						"leaf_inherit.png"), ATTR_REF("leaf_ref.png"), ATTR_WARN("WarnAttIcon.gif"), ATTR_ERR("leaf_error.png"), ATTR_DEFAULT("leaf.png");

		private final String fileName;

		TreeIcon(final String fileName)
		{
			this.fileName = fileName;
		}

		private String getFileName()
		{
			return this.fileName;
		}
	}

	/**
	 * Default constructor
	 */
	public AbstractTreeCellRenderer()
	{
	}

	/**
	 * Create a logger object for logging.
	 * @return A new Logger object.
	 */
	protected abstract Log getLogger();

	@Override
	public final Component getTreeCellRendererComponent(final JTree jTree, final Object value, final boolean isSelected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus)
	{
		final JDFTreeNode node = (JDFTreeNode) value;
		final JDFTreeModel model = (JDFTreeModel) jTree.getModel();

		// basic appearance
		setBasicAppearance(node, isSelected, hasFocus);

		// set text and tooltip
		setNodeText(node);

		// set text color
		setTextColor(node, model);

		// set icon
		setNodeIcon(node, model);

		return this;
	}

	/**
	 * Configures the default appearance of the JTree Element.
	 *
	 * @param isSelected Indicator whether or not the element is selected.
	 */
	protected void setBasicAppearance(final JDFTreeNode node, final boolean isSelected, final boolean hasFocus)
	{
		// basic appearance
		setOpaque(true);
		setBackground(isSelected ? COLOR_SELECTED : COLOR_DEFAULT);

		final Font cellFont = new Font(null, Font.PLAIN, RuntimeProperties.enlargedTextFontSize);
		setFont(cellFont);
	}

	/**
	 * Configures the elements text and tooltip.
	 *
	 * @param node The JDFTreeNode object for the element.
	 */
	protected void setNodeText(final JDFTreeNode node)
	{
		final String text = node.toDisplayString();
		String toolTip = text;

		if (node.isElement())
		{
			final KElement e = node.getElement();
			toolTip = e.getText();

			final String descName = e.getAttribute(AttributeName.DESCRIPTIVENAME, null, null);
			if (descName != null && toolTip != null)
			{
				toolTip += "\n " + descName;
			}
			else if (toolTip == null)
			{
				toolTip = descName;
			}
		}

		setText(text);
		setToolTipText(toolTip);
	}

	/**
	 * Defines the text color for a tree element.
	 *
	 * @param node  The JDFTreeNode object for the element.
	 * @param model The model for the element.
	 */
	protected void setTextColor(final JDFTreeNode node, final JDFTreeModel model)
	{

		// define colors
		final Color colorFont = new Color(0, 0, 0);
		final Color colorFontNamespace = new Color(0, 120, 255);
		final Color colorFontError = new Color(255, 0, 0);
		final Color colorFontNew = new Color(255, 0, 255);

		// set node text colors
		if (!model.isValid(node))
		{
			setForeground(colorFontError);

		}
		else if ("new value".equals(node.getValue()))
		{
			setForeground(colorFontNew);

		}
		else if (node.hasForeignNS())
		{
			setForeground(colorFontNamespace);

		}
		else
		{
			setForeground(colorFont);
		}
	}

	/**
	 * Defines the icon for a element in tree.
	 *
	 * @param node  The JDFTreeNode object for the element.
	 * @param model The model for the element.
	 */
	protected void setNodeIcon(final JDFTreeNode node, final JDFTreeModel model)
	{

		final KElement elem = node.getElement();

		final String errType = model.getErrorType(node);

		if (errType == null)
		{

			if (node.isElement())
			{ // element icons

				if (elem instanceof JDFNode)
				{
					setIcon(loadImageIcon(TreeIcon.NODE_JDF));

				}
				else if (elem.getNodeName().equals("XJDF"))
				{
					setIcon(loadImageIcon(TreeIcon.NODE_XJDF));

				}
				else if (elem.hasAttribute("Usage", null, false))
				{

					if (elem.getAttribute("Usage", null, "").equals("Input"))
					{
						setIcon(loadImageIcon(TreeIcon.NODE_REF_IN));

					}
					else if (elem.getAttribute("Usage", null, "").equals("Output"))
					{
						setIcon(loadImageIcon(TreeIcon.NODE_REF_OUT));

					}
					else
					{
						setIcon(loadImageIcon(TreeIcon.NODE_REF));
					}
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.NODE_DEFAULT));
				}

			}
			else
			{ // attribute icons

				final String attName = node.getName();

				if (node.isInherited())
				{

					if (attName.equals("PartIDKeys"))
					{
						setIcon(loadImageIcon(TreeIcon.ATTR_PARTKEYS));

					}
					else
					{
						setIcon(loadImageIcon(TreeIcon.ATTR_INHERITED));
					}

				}
				else
				{

					switch (attName)
					{
					case "PartIDKeys":
						setIcon(loadImageIcon(TreeIcon.ATTR_PARTKEYS));
						break;

					case "rRef":
						setIcon(loadImageIcon(TreeIcon.ATTR_REF));
						break;

					default:
						setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
						break;

					}
				}
			}

		}
		else if ("UnlinkedResource".equalsIgnoreCase(errType))
		{ // warnings

			if (node.isElement())
			{
				setIcon(loadImageIcon(TreeIcon.NODE_WARN));

			}
			else
			{
				setIcon(loadImageIcon(TreeIcon.ATTR_WARN));
			}

		}
		else
		{ // not valid (error)

			if (node.isElement())
			{
				setIcon(loadImageIcon(TreeIcon.NODE_ERR));

			}
			else
			{
				setIcon(loadImageIcon(TreeIcon.ATTR_ERR));
			}
		}
	}

	/**
	 * Load and returns an image icon object.
	 *
	 * @param treeIcon Name of the icon to be loaded.
	 * @return The icon as ImageIcon object.
	 */
	protected ImageIcon loadImageIcon(final TreeIcon treeIcon)
	{

		final String resPath = ICON_ROOT_PATH + treeIcon.getFileName();
		byte[] bytes = null;

		try
		{
			final InputStream is = JDFTreeCellRenderer.class.getResourceAsStream(resPath);
			bytes = IOUtils.toByteArray(is);
			is.close();
		}
		catch (final IOException e)
		{
			LOGGER.error(e);
		}

		return new ImageIcon(bytes);
	}
}
