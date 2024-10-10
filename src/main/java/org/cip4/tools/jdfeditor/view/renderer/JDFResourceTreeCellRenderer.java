package org.cip4.tools.jdfeditor.view.renderer;

import java.awt.Color;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tree renderer for JDFResource elements.
 */
public class JDFResourceTreeCellRenderer extends AbstractTreeCellRenderer
{

	private static final Log LOGGER = LogFactory.getLog(JDFResourceTreeCellRenderer.class);

	/**
	 * Default constructor.
	 */
	public JDFResourceTreeCellRenderer()
	{
	}

	@Override
	protected Log getLogger()
	{
		return LOGGER;
	}

	@Override
	protected void setBasicAppearance(JDFTreeNode node, boolean isSelected, boolean hasFocus)
	{

		// default behavior
		super.setBasicAppearance(node, isSelected, hasFocus);

		// additional behavior
		Color colorFocused = new Color(110, 200, 240);

		if (hasFocus)
		{
			this.setBackground(colorFocused);
		}
	}
}