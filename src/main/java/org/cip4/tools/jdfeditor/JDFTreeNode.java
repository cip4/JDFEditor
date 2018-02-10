/*
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
 *    Alternately, this acknowledgment mrSubRefay appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior writtenrestartProcesses()
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
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIrSubRefAL DAMAGES (INCLUDING, BUT NOT
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
 * originally based on software restartProcesses()
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG
 * copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *
 */
package org.cip4.tools.jdfeditor;

import java.util.Collections;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.cip4.jdflib.auto.JDFAutoRefAnchor.EnumAnchor;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFComment;
import org.cip4.jdflib.core.JDFCustomerInfo;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumNodeStatus;
import org.cip4.jdflib.core.JDFNodeInfo;
import org.cip4.jdflib.core.JDFRefElement;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.extensions.ResourceHelper;
import org.cip4.jdflib.extensions.XJDF20;
import org.cip4.jdflib.extensions.XJDFConstants;
import org.cip4.jdflib.jmf.JDFDeviceInfo;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFJobPhase;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFMessageService;
import org.cip4.jdflib.jmf.JDFQueueEntry;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFDevice;
import org.cip4.jdflib.resource.JDFEvent;
import org.cip4.jdflib.resource.JDFMarkObject;
import org.cip4.jdflib.resource.JDFNotification;
import org.cip4.jdflib.resource.JDFPart;
import org.cip4.jdflib.resource.JDFPhaseTime;
import org.cip4.jdflib.resource.JDFRefAnchor;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFDevCaps;
import org.cip4.jdflib.resource.intent.JDFDropItemIntent;
import org.cip4.jdflib.resource.process.JDFBinderySignature;
import org.cip4.jdflib.resource.process.JDFColor;
import org.cip4.jdflib.resource.process.JDFComChannel;
import org.cip4.jdflib.resource.process.JDFCompany;
import org.cip4.jdflib.resource.process.JDFContact;
import org.cip4.jdflib.resource.process.JDFContentObject;
import org.cip4.jdflib.resource.process.JDFEmployee;
import org.cip4.jdflib.resource.process.JDFFileSpec;
import org.cip4.jdflib.resource.process.JDFGeneralID;
import org.cip4.jdflib.resource.process.JDFIdentical;
import org.cip4.jdflib.resource.process.JDFLayoutElement;
import org.cip4.jdflib.resource.process.JDFMedia;
import org.cip4.jdflib.resource.process.JDFPerson;
import org.cip4.jdflib.resource.process.JDFRuleLength;
import org.cip4.jdflib.resource.process.JDFSeparationSpec;
import org.cip4.jdflib.resource.process.JDFSourceResource;
import org.cip4.jdflib.resource.process.JDFStation;
import org.cip4.jdflib.span.JDFSpanBase;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.StringUtil;
import org.w3c.dom.Attr;

/**
 * @author AnderssA ThunellE The tree node in the JTree To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class JDFTreeNode extends DefaultMutableTreeNode
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2778264565816334126L;

	/**
	 *
	 */
	public boolean isInherited;

	/**
	 * constructor for an element node
	 * @param elem the element
	 */
	public JDFTreeNode(final KElement elem)
	{
		super(elem);
	}

	/**
	 * constructor for an attribute node
	 * @param atr the attribute
	 * @param _isInherited
	 */
	public JDFTreeNode(final Attr atr, final boolean _isInherited)
	{
		super(atr);
		this.isInherited = _isInherited;
	}

	/**
	 *
	 */
	public JDFTreeNode()
	{
		super();
		isInherited = false;
	}

	/**
	 * true, if either the element or attribute are identical
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (super.equals(o))
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (!(o instanceof JDFTreeNode))
		{
			return false;
		}
		final JDFTreeNode to = (JDFTreeNode) o;
		if (userObject == null)
		{
			return to.getUserObject() == null;
		}

		return userObject.equals(to.getUserObject());
	}

	/**
	 * return the KElement related to this node. In case of attribute or text nodes, it is the parent KElement
	 * @return the related element
	 */
	public KElement getElement()
	{
		final Object o = this.getUserObject();
		if (o instanceof KElement)
		{
			return (KElement) this.getUserObject();
		}

		// this is an attribute - try thises parent
		final JDFTreeNode nParent = (JDFTreeNode) getParent();
		if (nParent == null)
		{
			return null;
		}

		return nParent.getElement();
	}

	/**
	 * return the text string related to this node. In case of attribute or element nodes, it is null
	 * @return the related element
	 */
	public String getText()
	{
		final Object o = this.getUserObject();
		if (o instanceof String)
		{
			return (String) o;
		}

		return null;
	}

	/**
	 * set the text string related to this node. In case of attribute or text nodes, it is null
	 * @param text
	 */
	public void setText(final String text)
	{
		setUserObject(text);
	}

	/**
	 * return the Attr related to this node. In case of attribute or text nodes, it is null
	 * @return the related element
	 */
	public Attr getAttr()
	{
		final Object o = this.getUserObject();
		if (o instanceof Attr)
		{
			return (Attr) o;
		}

		return null;
	}

	// /////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public boolean hasForeignNS()
	{
		final KElement e = getElement();
		return (e != null) && !(e instanceof JDFElement);
	}

	// /////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public boolean isElement()
	{
		return (userObject instanceof KElement);
	}

	// /////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public boolean isInherited()
	{
		return this.isInherited;
	}

	// /////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public String getXPath()
	{
		final KElement element = getElement();
		if (element == null)
		{
			return null;
		}
		if (this.isElement())
		{
			return element.buildXPath(null, 2);
		}

		return element.buildXPath(null, 2) + "/@" + getName();
	}

	/**
	 * get the child with name name
	 * @param name the name of the child node
	 * @return JDFTreeNode the child with name=name
	 */
	public JDFTreeNode getNodeWithName(final String name)
	{
		if (getChildCount() == 0)
		{
			return null;
		}
		JDFTreeNode n = (JDFTreeNode) getFirstChild();
		while (n != null)
		{
			if (n.getName().equals(name))
			{
				return n;
			}
			n = (JDFTreeNode) n.getNextSibling();
		}
		return n;
	}

	/**
	 * get the insert index for a child with name name always place attributes in front of elements
	 * @param name the name of the child node
	 * @param bAttribute if true, the placed element is an attribute, else it is an element
	 * @return the index for insertinto
	 */
	public int getInsertIndexForName(final String name, final boolean bAttribute)
	{
		if (getChildCount() == 0)
		{
			return -1;
		}
		JDFTreeNode n = (JDFTreeNode) getFirstChild();
		int index = 0;
		while (n != null)
		{
			if (bAttribute && n.isElement())
			{
				return index; // elements are always last
			}

			if (n.getName().compareTo(name) >= 0)
			{
				if (bAttribute && !n.isElement() || !bAttribute && n.isElement())
				{
					return index;
				}
			}
			n = (JDFTreeNode) n.getNextSibling();
			index++;
		}
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public String getName()
	{
		if (isElement())
		{
			return getElement().getNodeName();
		}
		if (userObject == null)
		{
			return "";
		}
		if (userObject instanceof String)
		{
			return JDFTreeModel.TEXT;
		}
		return ((Attr) userObject).getNodeName();
	}

	// /////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public String getValue()
	{
		if (isElement())
		{
			return "";
		}
		if (userObject == null)
		{
			return "";
		}
		if (userObject instanceof String)
		{
			return (String) userObject;
		}
		return ((Attr) userObject).getNodeValue();
	}

	/**
	 * this is the display of the object
	 */
	@Override
	public String toString()
	{
		return toDisplayString();
	}

	/**
	 * this is the display of the object in the tree
	 * @return the string to be displayed in the tree view
	 */
	public String toDisplayString()
	{
		final Object o = this.getUserObject();
		String s;
		if (o == null)
		{
			return null;
		}

		if (o instanceof Attr)
		{
			final Attr a = (Attr) o;
			s = a.getNodeName() + "=\"" + a.getNodeValue() + "\"";
		}
		else if (o instanceof String)
		{
			s = "#text=\"" + (String) o + "\"";
		}
		else
		// element
		{
			s = elementDisplay(o);
		}

		return s;
	}

	/**
	 *
	 * @param o
	 * @return
	 */
	protected String elementDisplay(final Object o)
	{
		final KElement e = (KElement) o;
		String s = e.getNodeName();
		final String nodeName = e.getLocalName();
		s = displaySpecial(e, s, nodeName);

		// always add id
		final String id = e.getAttribute_KElement(AttributeName.ID, null, null);
		if (id != null)
		{
			s += ", " + id;
		}

		// add any partidkeys in resources
		if (e instanceof JDFResource)
		{
			s = displayResource(e, s);
		}
		return s;
	}

	protected String displaySpecial(final KElement e, String s, final String nodeName)
	{
		if (e instanceof JDFAudit)
		{
			s = displayAudit(e, s);
		}
		else if (e instanceof JDFComment)
		{
			s = displayComment(e, s);
		}
		else if (e instanceof JDFRefElement)
		{
			final String ref = e.getAttribute("rRef", null, null);
			if (ref != null)
			{
				s += ": " + ref;
			}
		}
		else if (JDFResourceLink.isResourceLink(e))
		{
			s = displayResourceLink(e, s);
		}
		else if ((e instanceof JDFDevCap) || (e instanceof JDFDevCaps) || (e instanceof JDFAbstractState) || (e instanceof JDFSeparationSpec) || (e instanceof JDFColor))
		{
			final String nam = e.getAttribute(AttributeName.NAME, null, null);
			if (nam != null)
			{
				s += ": " + nam;
			}
		}
		else if (e instanceof JDFMessage)
		{
			s = displayMessage(e, s);
		}
		else if (e instanceof JDFJMF)
		{
			final String senderID = StringUtil.getNonEmpty(((JDFJMF) e).getSenderID());
			if (senderID != null)
			{
				s += " SenderID: " + senderID;
			}
		}
		else if (e instanceof JDFNode || XJDF20.rootName.equals(nodeName))
		{
			s = displayJDF(e, s);
		}
		else if (e instanceof JDFMessageService)
		{
			s = displayMessageService(e, s);
		}
		else if (e instanceof JDFSpanBase)
		{
			s = displaySpanBase(e, s);
		}
		else if ((e instanceof JDFContact))
		{
			final String contactTypes = e.getAttribute(AttributeName.CONTACTTYPES, null, null);
			if (contactTypes != null)
			{
				s += " - " + contactTypes;
			}
		}
		else if ((e instanceof JDFContentObject) || (e instanceof JDFMarkObject) || XJDFConstants.PlacedObject.equals(nodeName))
		{
			s = displayPlacedObject(e, s);
		}
		else if (e instanceof JDFDevice)
		{
			final String att = e.getAttribute(AttributeName.DEVICEID, null, null);
			if (att != null)
			{
				s += " DeviceID=" + att;
			}
		}
		else if (e instanceof JDFDropItemIntent)
		{
			final String att = e.getAttribute(AttributeName.AMOUNT, null, null);
			if (att != null)
			{
				s += " Amount=" + att;
			}
		}
		else if (e instanceof JDFLayoutElement)
		{
			final String att = e.getXPathAttribute("FileSpec/@URL", null);
			if (att != null)
			{
				s += " URL=" + att;
			}
		}
		else if (e instanceof JDFFileSpec)
		{
			final String att = e.getAttribute(AttributeName.URL, null, null);
			if (att != null)
			{
				s += " URL=" + att;
			}
		}
		else if (e instanceof JDFEmployee)
		{
			final String att = e.getAttribute(AttributeName.PERSONALID, null, null);
			if (att != null)
			{
				s += " PersonalID=" + att;
			}
		}
		else if (e instanceof JDFQueueEntry)
		{
			String att = e.getAttribute(AttributeName.QUEUEENTRYID, null, null);
			if (att != null)
			{
				s += " QEID=" + att;
			}
			att = e.getAttribute(AttributeName.STATUS, null, null);
			if (att != null)
			{
				s += " Status=" + att;
			}
		}
		else if (e instanceof JDFPhaseTime || e instanceof JDFJobPhase)
		{
			s = displayPhase(e, s);
		}
		else if (e instanceof JDFDeviceInfo)
		{
			s = displayDeviceInfo(e, s);
		}
		else if (e instanceof JDFNotification)
		{
			s = displayNotification(e, s);
		}
		else if (e instanceof JDFNodeInfo)
		{
			s = displayNodeInfo(e, s);
		}
		else if (e instanceof JDFEvent)
		{
			final JDFEvent di = (JDFEvent) e;
			final String att = di.getEventID();
			if (!KElement.isWildCard(att))
			{
				s += " EventID=" + att;
			}
		}
		else if (e instanceof JDFPart)
		{
			final JDFPart p = (JDFPart) e;
			final JDFAttributeMap map = p.getPartMap();
			s = addPartMap(s, map);
		}
		else if (e instanceof JDFIdentical)
		{
			final JDFIdentical p = (JDFIdentical) e;
			final JDFAttributeMap map = p.getPartMap();
			s = addPartMap(s, map);
		}
		else if (e instanceof JDFSourceResource)
		{
			final JDFSourceResource p = (JDFSourceResource) e;
			final JDFRefElement re = p.getRefElement();
			if (re != null)
			{
				s += " rRef: " + re.getrRef();
				s = addPartMap(s, re.getPartMap());
			}
		}
		else if (e instanceof JDFGeneralID)
		{
			final JDFGeneralID p = (JDFGeneralID) e;
			s += " " + p.getIDUsage() + " = " + p.getIDValue();
		}
		else if (e instanceof JDFPerson)
		{
			final JDFPerson p = (JDFPerson) e;
			s += " " + p.getDescriptiveName();
		}
		else if (e instanceof JDFCompany)
		{
			final JDFCompany c = (JDFCompany) e;
			s += " " + c.getOrganizationName();
		}
		else if (e instanceof JDFBinderySignature)
		{
			final JDFBinderySignature bs = (JDFBinderySignature) e;
			final String foldCatalog = bs.getFoldCatalog();
			if (!"".equals(foldCatalog))
				s += " " + foldCatalog;
		}
		else if (e instanceof JDFRuleLength)
		{
			final JDFRuleLength rl = (JDFRuleLength) e;
			s += " " + rl.getDDESCutType();
			final double l = rl.getLengthJDF();
			if (l > 0)
				s += " Len=" + l;
		}
		else if (e instanceof JDFStation)
		{
			final JDFStation rl = (JDFStation) e;
			s += " " + rl.getStationName();
			final int l = rl.getStationAmount();
			if (l > 0)
				s += " Amount=" + l;
		}
		else if (e instanceof JDFComChannel)
		{
			final JDFComChannel p = (JDFComChannel) e;
			s += " " + p.getLocator();
		}
		else if (e instanceof JDFCustomerInfo)
		{
			final JDFCustomerInfo p = (JDFCustomerInfo) e;
			s += " " + p.getCustomerID();
		}
		else if (e instanceof JDFRefAnchor)
		{
			final JDFRefAnchor p = (JDFRefAnchor) e;
			final EnumAnchor anchor = p.getAnchor();
			if (anchor != null)
			{
				s += " " + anchor.getName();
			}
		}
		else if (nodeName.endsWith("Set") || nodeName.equals("Intent"))
		{
			s = displaySet(e, s);
		}
		else if (XJDFConstants.Product.equals(nodeName))
		{
			s = displayProduct(e, s);
		}
		else if (ResourceHelper.isAsset(e))
		{
			s = displayXRes(e, s);
		}
		else if (XJDFConstants.Header.equals(nodeName))
		{
			s = displayHeader(e, s);
		}
		else if (nodeName.equals("Price"))
		{
			s = displayPrice(e, s);
		}
		return s;
	}

	protected String displayMessageService(final KElement e, String s)
	{
		String typ = e.getAttribute(AttributeName.TYPE, null, null);
		if (typ != null)
		{
			s += ": " + typ;
		}
		typ = e.getAttribute("JMFRole", null, null);
		if (typ != null)
		{
			s += " - " + typ;
		}
		return s;
	}

	protected String displayMessage(final KElement e, String s)
	{
		final String typ = e.getAttribute(AttributeName.TYPE, null, null);
		if (typ != null)
		{
			s += ": " + typ;
		}
		final String senderID = StringUtil.getNonEmpty(((JDFMessage) e).getSenderID());
		if (senderID != null)
		{
			s += " SenderID: " + senderID;
		}
		return s;
	}

	protected String displayAudit(final KElement e, String s)
	{
		final JDFAudit a = (JDFAudit) e;
		final JDFDate d = a.getTimeStamp();
		if (d != null)
		{
			s += d.getFormattedDateTime(" MMM dd yyyy - HH:mm");
		}
		return s;
	}

	protected String displayResourceLink(final KElement e, String s)
	{
		final JDFResourceLink rl = (JDFResourceLink) e;
		final String ref = rl.getrRef();
		final EnumUsage u = rl.getUsage();
		boolean bUsage = false;
		if (EnumUsage.Input.equals(u) || EnumUsage.Output.equals(u))
		{
			s += "(" + u.getName();
			bUsage = true;
		}
		final String pu = rl.getProcessUsage();
		if (!pu.equals(""))
		{
			s += " [" + pu + "]";
		}
		if (bUsage)
		{
			s += ") : " + ref;
		}
		return s;
	}

	protected String displayPlacedObject(final KElement e, String s)
	{
		final String ord = e.getAttribute("Ord", null, null);
		if (ord != null)
		{
			s += " Ord=" + ord;
		}
		final String ctm = e.getAttribute("CTM", null, null);
		if (ord != null)
		{
			s += " CTM=" + ctm;
		}
		return s;
	}

	protected String displayJDF(final KElement e, String s)
	{
		String typ = e.getNonEmpty(AttributeName.TYPE);
		if (typ != null)
		{
			s += ": " + typ;
		}
		else
		{
			typ = e.getNonEmpty(AttributeName.TYPES);
			if (typ != null)
			{
				s += ": " + typ;
			}
		}
		typ = e.getAttribute(AttributeName.CATEGORY, null, null);
		if (typ != null)
		{
			s += "-" + typ;
		}
		typ = e.getAttribute(AttributeName.JOBID, null, null);
		if (typ != null)
		{
			s += " JobID=" + typ;
		}
		typ = e.getAttribute(AttributeName.JOBPARTID, null, null);
		if (typ != null)
		{
			s += " JobPartID=" + typ;
		}
		final String stat = e.getAttribute(AttributeName.STATUS, null, null);
		if (stat != null)
		{
			s += " Status=" + stat;
		}
		return s;
	}

	protected String displaySpanBase(final KElement e, String s)
	{
		final String act = e.getAttribute("Actual", null, null);
		if (act != null)
		{
			s += " actual: " + act;
		}
		else
		{
			final String pref = e.getAttribute(AttributeName.PREFERRED, null, null);
			if (pref != null)
			{
				s += " preferred: " + pref;
			}
		}
		return s;
	}

	protected String displayComment(final KElement e, String s)
	{
		final String nam = e.getAttribute("Name", null, null);
		if (nam != null)
		{
			s += ": " + nam;
		}
		else
		{
			final String nam2 = e.getAttribute("Type", null, null);
			if (nam2 != null)
			{
				s += ": " + nam2;
			}
		}
		final String txt = e.getText();
		if (txt != null)
		{
			s += " " + StringUtil.leftStr(txt, 42);
			if (txt.length() > 42)
			{
				s += "...";
			}
		}
		return s;
	}

	protected String displayPhase(final KElement e, String s)
	{
		String att = e.getAttribute(AttributeName.STATUS, null, null);
		if (att != null)
		{
			s += " " + att;
		}
		att = e.getAttribute(AttributeName.STATUSDETAILS, null, null);
		if (att != null)
		{
			s += "/" + att;
		}
		if (e instanceof JDFJobPhase)
		{
			att = e.getAttribute(AttributeName.JOBID, null, null);
			if (att != null)
			{
				s += " JobID=" + att;
			}
		}
		return s;
	}

	protected String displayDeviceInfo(final KElement e, String s)
	{
		final JDFDeviceInfo di = (JDFDeviceInfo) e;
		String att = di.getDeviceID();
		if (!KElement.isWildCard(att))
		{
			s += " " + att;
		}
		att = e.getAttribute(AttributeName.DEVICESTATUS, null, null);
		if (att != null)
		{
			s += " " + att;
		}
		att = e.getAttribute(AttributeName.STATUSDETAILS, null, null);
		if (att != null)
		{
			s += "/" + att;
		}
		return s;
	}

	protected String displayNotification(final KElement e, String s)
	{
		final JDFNotification no = (JDFNotification) e;
		final String att = no.getType();
		if (!KElement.isWildCard(att))
		{
			s += " " + att;
		}
		final String att2 = e.getAttribute(AttributeName.CLASS, null, null);
		if (att2 != null)
		{
			s += " - " + att2;
		}
		return s;
	}

	protected String displayNodeInfo(final KElement e, String s)
	{
		final JDFNodeInfo no = (JDFNodeInfo) e;
		final JDFDate start = no.getStart();
		if (start != null)
		{
			s += " start:" + start.getFormattedDateTime(JDFDate.DATETIMEREADABLE);
		}
		final JDFDate end = no.getEnd();
		if (end != null)
		{
			s += " end:" + end.getFormattedDateTime(JDFDate.DATETIMEREADABLE);
		}
		return s;
	}

	private String displayHeader(final KElement e, String s)
	{
		final String extID = e.getNonEmpty(AttributeName.DEVICEID);
		if (extID != null)
		{
			s += " " + extID;
		}
		final String an = e.getNonEmpty(AttributeName.AGENTNAME);
		if (an != null)
		{
			s += " " + an;
		}
		return s;
	}

	protected String displayResource(final KElement e, String s)
	{
		if (e instanceof JDFMedia && e.hasAttribute(AttributeName.MEDIATYPE))
		{
			s += "/" + e.getAttribute(AttributeName.MEDIATYPE);
		}
		else if (e instanceof JDFNodeInfo)
		{
			final EnumNodeStatus nodeStatus = ((JDFNodeInfo) e).getNodeStatus();
			if (nodeStatus != null)
				s += " " + nodeStatus.getName();
		}
		final JDFResource r = (JDFResource) e;
		final String partKey = r.getLocalPartitionKey();
		if (partKey != null)
		{
			s += " [@" + partKey + "=" + r.getAttribute(partKey) + "]";
		}
		return s;
	}

	protected String displayPrice(final KElement e, String s)
	{
		final String name = e.getAttribute(AttributeName.DESCRIPTIVENAME, null, null);
		if (name != null)
		{
			s += " " + name;
		}
		final String price = e.getAttribute(AttributeName.PRICE, null, null);
		if (price != null)
		{
			s += " " + price;
		}
		return s;
	}

	protected String displayProduct(final KElement e, String s)
	{
		final String r = e.getNonEmpty(XJDFConstants.IsRoot);
		if (r != null)
		{
			s += " root=" + r;
		}
		final int a = e.getIntAttribute(AttributeName.AMOUNT, null, 0);
		if (a > 1)
		{
			s += " " + a;
		}
		final String name = e.getNonEmpty(AttributeName.PRODUCTTYPE);
		if (name != null)
		{
			s += " " + name;
		}
		final String det = e.getNonEmpty(AttributeName.PRODUCTTYPEDETAILS);
		if (det != null)
		{
			s += " " + det;
		}
		return s;
	}

	protected String displaySet(final KElement e, String s)
	{
		final String name = e.getAttribute("Name", null, null);
		final String usage = e.getAttribute(AttributeName.USAGE, null, null);
		final String procUsage = e.getAttribute(AttributeName.PROCESSUSAGE, null, null);
		String prefix = null;
		if (usage != null)
		{
			prefix = usage;
		}
		if (procUsage != null)
		{
			prefix += "/" + procUsage;
		}
		if (prefix != null)
		{
			s = prefix + " " + s;
		}
		if (name != null)
		{
			s += " " + name;
		}
		return s;
	}

	protected String displayXRes(final KElement e, String s)
	{
		final String extID = e.getNonEmpty(XJDFConstants.ExternalID);
		if (extID != null)
		{
			s += " " + extID;
		}
		final String desc = e.getNonEmpty(AttributeName.DESCRIPTIVENAME);
		if (desc != null)
		{
			s += ": " + desc;
		}
		return s;
	}

	/**
	 * @param s
	 * @param map
	 * @return
	 */
	private String addPartMap(String s, final JDFAttributeMap map)
	{
		final VString keys = map == null ? null : map.getKeys();
		if (keys != null)
		{
			Collections.sort(keys);
			for (int i = 0; i < keys.size(); i++)
			{
				s += " " + keys.elementAt(i) + "=" + map.get(keys.elementAt(i));
			}
		}
		return s;
	}

	/**
	 * @return the attribute value
	 */
	public String getXPathAttr()
	{
		final KElement element = getElement();
		if (element instanceof JDFNode)
		{
			final JDFNode n = (JDFNode) element;
			return n.buildXPath(null, 1);
		}

		String x = element.getAttribute("XPath", null, null);
		if (x != null)
		{
			return x;
		}

		x = element.getAttribute("Name", null, null);
		if (x == null)
		{
			return null;
		}

		final String parentLocal = element.getInheritedAttribute("XPath", null, null);
		return parentLocal != null ? parentLocal + "/@" + x : null;

	}

	/**
	 *
	 * @param attName
	 * @param prefix
	 * @param postFix
	 * @return
	 */
	protected String getDCString(final String attName, String prefix, final String postFix)
	{
		String strValue = "";
		if (getElement().hasAttribute(attName))
		{
			if (prefix == null)
			{
				prefix = " " + attName + "=";
			}
			strValue = prefix + getElement().getAttribute(attName);
			if (postFix != null)
			{
				strValue += postFix;
			}
		}
		return strValue;
	}

	/**
	 * get the index of a TreeNode, -1 if null
	 * @see javax.swing.tree.DefaultMutableTreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	@Override
	public int getIndex(final TreeNode arg0)
	{
		if (arg0 == null)
		{
			return -1;
		}
		return super.getIndex(arg0);
	}

	/**
	 * @param path
	 * @return
	 */
	public boolean matchesPath(final String path)
	{
		if (path == null)
		{
			return false;
		}

		int lastAt = path.lastIndexOf("@");
		final int lastAt2 = path.lastIndexOf("[@");
		if (lastAt2 + 1 == lastAt)
		{
			lastAt = -1;
		}

		final String attribute = lastAt > 0 ? StringUtil.token(path, -1, "@") : null;
		final String elementString = lastAt > 0 ? path.substring(0, lastAt) : path;
		final boolean element = isElement();
		if (element && attribute != null)
		{
			return false;
		}
		if (!element)
		{
			if (attribute == null || !attribute.equals(getName()))
			{
				return false;
			}
			return getElement().matchesPath(elementString, true);
		}
		return getElement().matchesPath(path, true);
	}
}
