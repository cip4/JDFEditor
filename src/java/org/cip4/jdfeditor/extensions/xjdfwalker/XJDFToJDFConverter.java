/**
 * 
 */
package org.cip4.jdflib.extensions.xjdfwalker;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.elementwalker.BaseElementWalker;
import org.cip4.jdflib.elementwalker.BaseWalker;
import org.cip4.jdflib.elementwalker.BaseWalkerFactory;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFAuditPool;
import org.cip4.jdflib.resource.JDFPart;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.util.StringUtil;

/**
 * @author Rainer Prosi, Heidelberger Druckmaschinen
 *
 */
public class XJDFToJDFConverter extends BaseElementWalker
{
	JDFDoc jdfDoc;
	JDFNode theNode;

	/**
	 * @param template the jdfdoc to fill this into 
	 * 
	 */
	public XJDFToJDFConverter(JDFDoc template)
	{
		super(new BaseWalkerFactory());
		jdfDoc = template == null ? null : (JDFDoc) template.clone();
	}

	/**
	 * @param xjdf
	 * @return the converted jdf
	 */
	public JDFDoc convert(KElement xjdf)
	{
		if (jdfDoc == null)
			jdfDoc = new JDFDoc("JDF");
		theNode = findNode(xjdf);
		if (theNode == null)
			return null;
		walkTree(xjdf, null);
		return jdfDoc;
	}

	/**
	 * find and optionally create the appropriate node
	 * @param xjdf
	 * @return the node
	 */
	private JDFNode findNode(KElement xjdf)
	{
		JDFNode root = jdfDoc.getJDFRoot();
		JDFNode n = root.getJobPart(xjdf.getAttribute(AttributeName.JOBPARTID), null);
		if (n == null)
		{
			VElement nodes = root.getvJDFNode(null, null, false);
			VString xTypes = StringUtil.tokenize(xjdf.getAttribute(AttributeName.TYPES), null, false);
			for (int i = 0; i < nodes.size(); i++)
			{
				JDFNode n2 = (JDFNode) nodes.get(i);
				VString vtypes = n2.getAllTypes();
				if (vtypes.containsAll(xTypes))
					return n2;

			}
		}
		return n;
	}

	/**
	 * 
	 * @author Rainer Prosi, Heidelberger Druckmaschinen
	 *
	 */
	protected class WalkXElement extends BaseWalker
	{

		/**
		 * @param factory
		 */
		public WalkXElement()
		{
			super(getFactory());
		}

		/**
		 * @param e
		 * @return true if must continue
		 */
		@Override
		public KElement walk(KElement e, KElement trackElem)
		{
			trackElem.copyElement(e, null);
			return null;
		}

	}

	/**
	 * @author Rainer Prosi, Heidelberger Druckmaschinen
	 * walker for the xjdf root
	 */
	public class WalkXJDF extends WalkXElement
	{
		/////////////////////////////////////////////////////////////////////////////////
		/**
		 * @param e
		 * @return true if must continue
		 */
		@Override
		public KElement walk(KElement e, KElement trackElem)
		{
			theNode.setAttributes(e);
			return theNode;
		}

		/**
		 * @see org.cip4.jdflib.elementwalker.BaseWalker#matches(org.cip4.jdflib.core.KElement)
		 * @param toCheck
		 * @return true if it matches
		 */
		@Override
		public boolean matches(KElement toCheck)
		{
			return super.matches(toCheck) && "XJDF".equals(toCheck.getLocalName());
		}
	}

	/**
	 * @author Rainer Prosi, Heidelberger Druckmaschinen
	 * walker for the various resource sets
	 */
	public class WalkSet extends WalkXElement
	{
		/**
		 * @param e
		 * @return thr created resource
		 */
		@Override
		public KElement walk(KElement e, KElement trackElem)
		{
			EnumUsage inOut = EnumUsage.getEnum(e.getAttribute(AttributeName.USAGE));
			JDFResource r = theNode.addResource(e.getAttribute("Name"), inOut);
			JDFResourceLink rl = theNode.getLink(r, inOut);
			r.setAttributes(e);
			rl.setrRef(e.getAttribute(AttributeName.ID));
			r.removeAttribute(AttributeName.USAGE);
			rl.moveAttribute(AttributeName.PROCESSUSAGE, r);
			rl.moveAttribute(AttributeName.AMOUNT, r);
			rl.moveAttribute(AttributeName.ACTUALAMOUNT, r);
			rl.moveAttribute(AttributeName.MAXAMOUNT, r);
			rl.moveAttribute(AttributeName.MINAMOUNT, r);
			return r;
		}

		/**
		 * @see org.cip4.jdflib.elementwalker.BaseWalker#matches(org.cip4.jdflib.core.KElement)
		 * @param toCheck
		 * @return true if it matches
		 */
		@Override
		public boolean matches(KElement toCheck)
		{
			KElement parent = toCheck.getParentNode_KElement();
			boolean bL1 = parent != null && parent.getLocalName().equals("XJDF");
			return bL1 && super.matches(toCheck) && toCheck.getLocalName().endsWith("Set")
					&& toCheck.hasAttribute(AttributeName.NAME);
		}

	}

	/**
	 * @author Rainer Prosi, Heidelberger Druckmaschinen
	 * walker for the various resource sets
	 */
	public class WalkResource extends WalkXElement
	{
		/**
		 * @param e
		 * @return thr created resource
		 */
		@Override
		public KElement walk(KElement e, KElement trackElem)
		{
			trackElem.setAttributes(e);
			return trackElem;
		}

		/**
		 * @see org.cip4.jdflib.elementwalker.BaseWalker#matches(org.cip4.jdflib.core.KElement)
		 * @param toCheck
		 * @return true if it matches
		 */
		@Override
		public boolean matches(KElement toCheck)
		{
			//test on grandparent
			KElement parent = toCheck.getParentNode_KElement();
			parent = parent == null ? null : parent.getParentNode_KElement();
			boolean bL1 = parent != null && parent.getLocalName().endsWith("Set");
			return bL1 && super.matches(toCheck) && toCheck.getLocalName().equals(parent.getAttribute("Name"));
		}
	}

	/**
	 * @author Rainer Prosi, Heidelberger Druckmaschinen
	 * walker for the various resource sets
	 */
	public class WalkReplace extends WalkXElement
	{
		/**
		 * @param e
		 * @return the created resource
		 */
		@Override
		public KElement walk(KElement e, KElement trackElem)
		{
			trackElem.removeChildren(e.getNodeName(), null, null);
			trackElem.copyElement(e, null);
			return null;
		}

		/**
		 * @see org.cip4.jdflib.elementwalker.BaseWalker#matches(org.cip4.jdflib.core.KElement)
		 * @param toCheck
		 * @return true if it matches
		 */
		@Override
		public boolean matches(KElement toCheck)
		{
			return super.matches(toCheck) && (toCheck instanceof JDFAuditPool);
		}

	}

	/**
	 * @author Rainer Prosi, Heidelberger Druckmaschinen
	 * walker for the various resource sets
	 */
	public class WalkXJDFResource extends WalkXElement
	{
		/**
		 * @param e
		 * @return thr created resource
		 */
		@Override
		public KElement walk(KElement e, KElement trackElem)
		{
			JDFResource p;
			JDFPart part = (JDFPart) e.getElement(ElementName.PART);

			if (part != null)
			{
				p = createPartition(e, trackElem, part);
			}
			else
			{
				p = (JDFResource) trackElem;
			}
			JDFAttributeMap map = e.getAttributeMap();
			map.remove(AttributeName.ID);
			p.setAttributes(map);
			return p;
		}

		/**
		 * @param e
		 * @param trackElem
		 * @return
		 */
		private JDFResource createPartition(KElement e, KElement trackElem, JDFPart part)
		{
			JDFResource r = (JDFResource) trackElem;
			JDFResource rPart = r.getCreatePartition(part.getPartMap(), part.guessPartIDKeys());
			JDFResourceLink rl = theNode.getLink(r, null);
			if (rl != null)
				rl.moveElement(part, null);
			return rPart;
		}

		/**
		 * @see org.cip4.jdflib.elementwalker.BaseWalker#matches(org.cip4.jdflib.core.KElement)
		 * @param toCheck
		 * @return true if it matches
		 */
		@Override
		public boolean matches(KElement toCheck)
		{
			KElement parent = toCheck.getParentNode_KElement();
			boolean bL1 = parent != null && parent.getLocalName().endsWith("Set");
			return bL1 && super.matches(toCheck) && parent.hasAttribute(AttributeName.NAME);
		}

	}

}
