package org.cip4.tools.jdfeditor.view.renderer;

import org.cip4.jdflib.core.KElement;
import org.w3c.dom.Attr;

public class SchemaOutputWrapper extends JDFTreeNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2575958231112905133L;

	public SchemaOutputWrapper(final KElement element)
	{
		super(element);
	}

	public SchemaOutputWrapper(final Attr atr)
	{
		super(atr, false);
	}

	/**
	 * generates the string to be displayed in the tree
	 */
	@Override
	public String toDisplayString()
	{
		final String s = super.toDisplayString();
		if (isElement())
		{
		}
		return s;

	}

}
