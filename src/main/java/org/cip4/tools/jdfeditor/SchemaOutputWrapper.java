package org.cip4.tools.jdfeditor;

import org.cip4.jdflib.core.KElement;
import org.w3c.dom.Attr;

class SchemaOutputWrapper extends JDFTreeNode
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
		String s = super.toDisplayString();
		if (isElement())
		{
			String path = getElement().getNonEmpty("Path");
			if ("$".equals(path))
				path = "JSON root";
			else if (path == null)
			{
				path = "Message";
			}
			else if (path.startsWith("$."))
			{
				path = path.substring(2);
			}
			final String s2 = getElement().getAttribute("Message", null, null);
			if (s2 != null)
				s += ": " + path + ": " + s2;
		}
		return s;

	}

}
