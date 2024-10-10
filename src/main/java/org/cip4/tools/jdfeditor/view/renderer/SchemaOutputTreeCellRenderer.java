package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.validate.JDFValidator;
import org.cip4.tools.jdfeditor.JDFTreeModel;

public class SchemaOutputTreeCellRenderer extends AbstractTreeCellRenderer
{

	private static final Log LOGGER = LogFactory.getLog(SchemaOutputTreeCellRenderer.class);

	private static final long serialVersionUID = 6261287268245030123L;

	/**
	 * Default constructor.
	 */
	public SchemaOutputTreeCellRenderer()
	{
	}

	@Override
	protected Log getLogger()
	{
		return LOGGER;
	}

	@Override
	protected void setNodeIcon(final JDFTreeNode node, final JDFTreeModel model)
	{

		final String n = node.getName();
		if (node.isElement())
		{
			final KElement elem = node.getElement();
			final String tts = JDFValidator.toMessageString(elem);
			if (tts != null)
				setToolTipText(tts);

			if (n.equals("Error"))
			{
				setIcon(loadImageIcon(TreeIcon.NODE_ERR));
			}
			else if (n.equals("SchemaValidationOutput"))
			{
				if (elem != null && !elem.getAttribute("ValidationResult", null, "").equals("Valid"))
				{
					setIcon(loadImageIcon(TreeIcon.NODE_ERR));
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.OK));
				}

				if (elem.getAttribute("Message") != null)
					setToolTipText(elem.getAttribute("Message"));

			}

		}
		else
		// attributes
		{
			setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
		}
	}

}
