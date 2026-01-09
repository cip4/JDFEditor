/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2025 The International Cooperation for the Integration of Processes in Prepress, Press and Postpress (CIP4). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must include the following acknowledgment: "This product includes software developed by the The International Cooperation for
 * the Integration of Processes in Prepress, Press and Postpress (www.cip4.org)" Alternately, this acknowledgment mrSubRefay appear in the software itself, if and wherever such third-party
 * acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of Processes in Prepress, Press and Postpress" must not be used to endorse or promote products derived from this software
 * without prior written permission. For written permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4", nor may "CIP4" appear in their name, without prior writtenrestartProcesses() permission of the CIP4 organization
 *
 * Usage of this software in commercial products is subject to restrictions. For details please consult info@cip4.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIrSubRefAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. ====================================================================
 *
 * This software consists of voluntary contributions made by many individuals on behalf of the The International Cooperation for the Integration of Processes in Prepress, Press and Postpress and was
 * originally based on software restartProcesses() copyright (c) 1999-2001, Heidelberger Druckmaschinen AG copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the Integration of Processes in Prepress, Press and Postpress , please see <http://www.cip4.org/>.
 *
 */
package org.cip4.tools.jdfeditor.view.renderer;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.xerces.dom.AttrNSImpl;
import org.cip4.jdflib.auto.JDFAutoRefAnchor.EAnchor;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFComment;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFCustomerInfo;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumNodeStatus;
import org.cip4.jdflib.core.JDFNodeInfo;
import org.cip4.jdflib.core.JDFPartAmount;
import org.cip4.jdflib.core.JDFRefElement;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.JDFSeparationList;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.StringArray;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.JDFMatrix;
import org.cip4.jdflib.extensions.MessageHelper;
import org.cip4.jdflib.extensions.ResourceHelper;
import org.cip4.jdflib.extensions.XJDF20;
import org.cip4.jdflib.extensions.XJDFConstants;
import org.cip4.jdflib.extensions.XJMFHelper;
import org.cip4.jdflib.jmf.JDFDeviceInfo;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFJobPhase;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFMessageService;
import org.cip4.jdflib.jmf.JDFQueueEntry;
import org.cip4.jdflib.jmf.JDFResourceInfo;
import org.cip4.jdflib.node.JDFAncestor;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFDevice;
import org.cip4.jdflib.resource.JDFEvent;
import org.cip4.jdflib.resource.JDFMarkObject;
import org.cip4.jdflib.resource.JDFNotification;
import org.cip4.jdflib.resource.JDFPart;
import org.cip4.jdflib.resource.JDFPatch;
import org.cip4.jdflib.resource.JDFPhaseTime;
import org.cip4.jdflib.resource.JDFRefAnchor;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFDevCaps;
import org.cip4.jdflib.resource.intent.JDFDropItemIntent;
import org.cip4.jdflib.resource.process.JDFAssembly;
import org.cip4.jdflib.resource.process.JDFAssemblySection;
import org.cip4.jdflib.resource.process.JDFBinderySignature;
import org.cip4.jdflib.resource.process.JDFColor;
import org.cip4.jdflib.resource.process.JDFColorantAlias;
import org.cip4.jdflib.resource.process.JDFColorantControl;
import org.cip4.jdflib.resource.process.JDFComChannel;
import org.cip4.jdflib.resource.process.JDFCompany;
import org.cip4.jdflib.resource.process.JDFContact;
import org.cip4.jdflib.resource.process.JDFContentObject;
import org.cip4.jdflib.resource.process.JDFConventionalPrintingParams;
import org.cip4.jdflib.resource.process.JDFDeliveryParams;
import org.cip4.jdflib.resource.process.JDFDropItem;
import org.cip4.jdflib.resource.process.JDFEmployee;
import org.cip4.jdflib.resource.process.JDFFileSpec;
import org.cip4.jdflib.resource.process.JDFGeneralID;
import org.cip4.jdflib.resource.process.JDFIdentical;
import org.cip4.jdflib.resource.process.JDFLayoutElement;
import org.cip4.jdflib.resource.process.JDFMedia;
import org.cip4.jdflib.resource.process.JDFPerson;
import org.cip4.jdflib.resource.process.JDFPreview;
import org.cip4.jdflib.resource.process.JDFRuleLength;
import org.cip4.jdflib.resource.process.JDFSeparationSpec;
import org.cip4.jdflib.resource.process.JDFSourceResource;
import org.cip4.jdflib.resource.process.JDFStation;
import org.cip4.jdflib.resource.process.postpress.JDFCutMark;
import org.cip4.jdflib.span.JDFSpanBase;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.JDFTreeNodeEnumeration;
import org.w3c.dom.Attr;

/**
 * @author AnderssA ThunellE The tree node in the JTree To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to Window>Preferences>Java>Code Generation.
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
	 *
	 * @param elem the element
	 */
	public JDFTreeNode(final KElement elem)
	{
		super(elem);
	}

	/**
	 * constructor for an attribute node
	 *
	 * @param atr          the attribute
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
		if (!(o instanceof final JDFTreeNode to))
		{
			return false;
		}
		if (userObject == null)
		{
			return to.getUserObject() == null;
		}

		return userObject.equals(to.getUserObject());
	}

	/**
	 * return the KElement related to this node. In case of attribute or text nodes, it is the parent KElement
	 *
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
	 *
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
	 *
	 * @param text
	 */
	public void setText(final String text)
	{
		setUserObject(text);
	}

	/**
	 * return the Attr related to this node. In case of attribute or text nodes, it is null
	 *
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
		final KElement e = (userObject instanceof KElement) ? getElement() : null;
		if (e == null && (userObject instanceof AttrNSImpl))
		{
			final String namespaceURI = ((AttrNSImpl) userObject).getNamespaceURI();
			return !StringUtil.isEmpty(namespaceURI) && !JDFElement.isInAnyJDFNameSpaceStatic(namespaceURI);
		}
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
	 *
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
	 *
	 * @param name       the name of the child node
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

	/**
	 * @return
	 */
	public static String getName(final JDFTreeNode node)
	{
		return node == null ? "null" : node.getName();
	}

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
	 *
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

		if (o instanceof final Attr a)
		{
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
	 * @param o
	 * @return
	 */
	protected String elementDisplay(final Object o)
	{
		final KElement e = (KElement) o;
		String s = e.getNodeName();
		final String nodeName = e.getLocalName();
		final String nsUri = e.getNamespaceURI();
		if ("http://www.w3.org/2001/XMLSchema".equals(nsUri))
		{
			return displaySchema(e, s);
		}
		s = displaySpecial(e, s, nodeName);

		s += addAttribute(e, AttributeName.ID);
		s += addAttribute(e, AttributeName.PRODUCTID);
		s += addAttribute(e, XJDFConstants.ExternalID);

		// add any partidkeys in resources
		if (e instanceof JDFResource)
		{
			s = displayResource(e, s);
		}
		return s;
	}

	String displaySchema(final KElement e, String s)
	{

		final String name = addAttributeValue(e, "name");
		s += name;
		final String type = addAttributeValue(e, "type");
		if (!StringUtil.equals(name, type))
		{
			s += type;
		}
		s += addAttributeValue(e, "ref", " ref=", null);
		s += addAttributeValue(e, "use", " ", null);
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
		else if ((e instanceof JDFAssembly) || (e instanceof JDFAssemblySection))
		{
			s = displayAssembly(e, s);
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
		else if ((e instanceof JDFDevCap) || (e instanceof JDFDevCaps) || (e instanceof JDFAbstractState) || (e instanceof JDFSeparationSpec))
		{
			final String nam = e.getAttribute(AttributeName.NAME, null, null);
			if (nam != null)
			{
				s += ": " + nam;
			}
		}
		else if (e instanceof JDFResourceInfo)
		{
			final String nam = ((JDFResourceInfo) e).getResourceName();
			s += ": " + nam;
		}
		else if (e instanceof JDFMessage)
		{
			s = displayMessage(e, s);
		}
		else if (XJDFConstants.XJMF.equals(nodeName))
		{
			final XJMFHelper xh = XJMFHelper.getHelper(e);
			final List<MessageHelper> mhs = xh.getMessageHelpers();
			final StringArray a = new StringArray();
			for (final MessageHelper mh : mhs)
			{
				a.appendUnique(mh.getLocalName());
			}
			String msgs = a.getString();
			if (msgs.length() > 42)
			{
				msgs = StringUtil.leftStr(msgs, 40) + "...";
			}
			s += " " + msgs;
		}
		else if (e instanceof JDFJMF)
		{
			s += addAttribute(e, AttributeName.SENDERID);
		}
		else if (e instanceof JDFNode || XJDF20.rootName.equals(nodeName) || e instanceof JDFAncestor)
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
		else if (e instanceof JDFColorantControl)
		{
			s += " " + ((JDFColorantControl) e).getSeparations().getString();
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
		else if (e instanceof JDFCutMark)
		{
			s += addAttributeValue(e, AttributeName.MARKTYPE);
			s += addAttributeValue(e, AttributeName.POSITION);
		}
		else if (e instanceof JDFDevice)
		{
			s += addAttribute(e, AttributeName.DEVICEID);
		}
		else if (e instanceof JDFDeliveryParams)
		{
			s += addAttribute(e, AttributeName.REQUIRED);
		}
		else if (e instanceof JDFDropItemIntent)
		{
			s += addAttribute(e, AttributeName.AMOUNT);
			s += addAttribute(e, AttributeName.EARLIEST);
		}
		else if (e instanceof JDFDropItem)
		{
			s += addAttribute(e, AttributeName.AMOUNT);
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
			s += addAttribute(e, AttributeName.URL);
		}
		else if (e instanceof JDFPreview)
		{
			s += addAttributeValue(e, AttributeName.PREVIEWUSAGE);
			s += addAttribute(e, AttributeName.URL);
		}
		else if (e instanceof JDFEmployee)
		{
			s += addAttribute(e, AttributeName.PERSONALID);
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
		else if (e instanceof JDFSeparationList)
		{
			s = displaySepList(e, s);
		}
		else if (e instanceof JDFColorantAlias)
		{
			s = displayColorantAlias(e, s);
		}
		else if (e instanceof final JDFEvent di)
		{
			final String att = di.getEventID();
			if (!KElement.isWildCard(att))
			{
				s += " EventID=" + att;
			}
		}
		else if (e instanceof JDFPart)
		{
			s = displayPart(e, s);
		}
		else if (e instanceof JDFPartAmount)
		{
			for (final String a : new StringArray("Amount ActualAmount Waste"))
			{
				s += addAttribute(e, a);
			}
		}
		else if (e instanceof final JDFColor p)
		{
			final String acn = p.getActualColorName();
			if (!StringUtil.isEmpty(acn))
			{
				s += JDFConstants.BLANK + acn;
			}
		}
		else if (e instanceof JDFPatch)
		{
			s += addAttribute(e, AttributeName.PATCHUSAGE);
		}
		else if (e instanceof JDFConventionalPrintingParams)
		{
			s += addAttribute(e, AttributeName.WORKSTYLE);
		}
		else if (e instanceof final JDFIdentical p)
		{
			final JDFAttributeMap map = p.getPartMap();
			s = addPartMap(s, map);
		}
		else if (e instanceof final JDFSourceResource p)
		{
			final JDFRefElement re = p.getRefElement();
			if (re != null)
			{
				s += " rRef: " + re.getrRef();
				s = addPartMap(s, re.getPartMap());
			}
		}
		else if (e instanceof final JDFGeneralID p)
		{
			s += JDFConstants.BLANK + p.getIDUsage() + " = " + p.getIDValue();
		}
		else if (e instanceof final JDFPerson p)
		{
			s += JDFConstants.BLANK + p.getDescriptiveName();
		}
		else if (e instanceof final JDFCompany c)
		{
			s += JDFConstants.BLANK + c.getOrganizationName();
		}
		else if (e instanceof JDFBinderySignature)
		{
			s += addAttribute(e, AttributeName.BINDERYSIGNATURETYPE);
			s += addAttribute(e, AttributeName.SPREADTYPE);
			s += addAttribute(e, AttributeName.FOLDCATALOG);
		}
		else if (e instanceof final JDFRuleLength rl)
		{
			s += JDFConstants.BLANK + rl.getDDESCutType();
			final double l = rl.getLengthJDF();
			if (l > 0)
			{
				s += " Len=" + l;
			}
		}
		else if (e instanceof final JDFStation rl)
		{
			s += JDFConstants.BLANK + rl.getStationName();
			final int l = rl.getStationAmount();
			if (l > 0)
			{
				s += " Amount=" + l;
			}
		}
		else if (e instanceof final JDFComChannel p)
		{
			s += JDFConstants.BLANK + p.getLocator();
		}
		else if (e instanceof final JDFCustomerInfo p)
		{
			s += JDFConstants.BLANK + p.getCustomerID();
		}
		else if (e instanceof final JDFRefAnchor p)
		{
			final EAnchor anchor = p.getEAnchor();
			if (anchor != null)
			{
				s += JDFConstants.BLANK + anchor.name();
			}
		}
		else if (nodeName.endsWith("Set") || "Intent".equals(nodeName))
		{
			s = displaySet(e, s);
		}
		else if (e instanceof JDFResourceInfo)
		{
			final KElement ee = e.getElement(XJDFConstants.ResourceSet);
			if (ee != null)
			{
				s = displaySet(ee, s);
			}
		}
		else if (XJDFConstants.Product.equals(nodeName))
		{
			s = displayProduct(e, s);
		}
		else if (XJDFConstants.ProductList.equals(nodeName))
		{
			s += " [" + e.numChildElements(XJDFConstants.Product, null) + "]";
		}
		else if (XJDFConstants.SurfaceColor.equals(nodeName))
		{
			s += JDFConstants.BLANK + e.getAttribute(XJDFConstants.Surface) + " / " + e.getAttribute(ElementName.COLORSUSED);
		}
		else if (ResourceHelper.isAsset(e))
		{
			s = displayXRes(e, s);
		}
		else if (XJDFConstants.Header.equals(nodeName))
		{
			s = displayHeader(e, s);
		}
		else if ("Price".equals(nodeName))
		{
			s = displayPrice(e, s);
		}
		else if ("Credential".equals(nodeName))
		{
			s += " " + e.getAttribute("domain") + "=" + e.getXPathAttribute("Identity", "");
		}
		else if ("Request".equals(nodeName))
		{
			s += " BusinessID=" + e.getAttribute("BusinessID");
		}
		return s;
	}

	String displayAssembly(final KElement e, String s)
	{
		s += addAttributeValue(e, AttributeName.ORDER);
		return s;
	}

	String addAttributeValue(final KElement e, final String key)
	{
		return addAttributeValue(e, key, null, null);
	}

	String addAttribute(final KElement e, final String key)
	{
		return addAttributeValue(e, key, " " + key + "=", null);
	}

	String addAttributeValue(final KElement e, final String key, String prefix, final String suffix)
	{
		String val = e.getNonEmpty(key);
		if (val != null)
		{
			if (!StringUtil.isEmpty(suffix))
			{
				val += suffix;
			}
			if (StringUtil.isEmpty(prefix))
			{
				prefix = JDFConstants.BLANK;
			}
			return prefix + val;
		}
		return JDFConstants.EMPTYSTRING;
	}

	protected String displayPart(final KElement e, String s)
	{
		final JDFPart p = (JDFPart) e;
		final JDFAttributeMap map = p.getPartMap();
		s = addPartMap(s, map);
		return s;
	}

	/**
	 * @param e
	 * @param s
	 * @return
	 */
	String displayColorantAlias(final KElement e, String s)
	{
		final JDFColorantAlias ca = (JDFColorantAlias) e;
		final String rep = ca.getReplacementColorantName();
		ca.getSeparations();
		if (!StringUtil.isEmpty(rep))
		{
			s += JDFConstants.SPACE + rep;
			final VString aliases = ca.getSeparations();
			if (!VString.isEmpty(aliases))
			{
				s += " -> " + aliases.getString(JDFConstants.COMMA, null, null);
			}
		}
		return s;
	}

	/**
	 * @param e
	 * @param s
	 * @return
	 */
	String displaySepList(final KElement e, String s)
	{
		final JDFSeparationList ca = (JDFSeparationList) e;
		ca.getSeparations();
		final VString aliases = ca.getSeparations();
		if (!VString.isEmpty(aliases))
		{
			s += ": " + aliases.getString(JDFConstants.COMMA, null, null);
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
		s = StringUtil.addToken(s, JDFConstants.BLANK, a.getNonEmpty(AttributeName.STATUS));
		s = StringUtil.addToken(s, JDFConstants.BLANK, a.getNonEmpty(AttributeName.ENDSTATUS));
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
		if (!"".equals(pu))
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
			s += "[" + ord + "]";
		}
		final JDFMatrix ctm = JDFMatrix.createMatrix(e.getAttribute("CTM", null, null));
		if (ctm != null)
		{
			s += " CTM=" + ctm.getString(1);
		}
		return s;
	}

	String displayJDF(final KElement e, String s)
	{
		String typ = e.getNonEmpty(AttributeName.TYPE);
		s = StringUtil.addToken(s, JDFConstants.BLANK, typ);
		final String typs = e.getNonEmpty(AttributeName.TYPES);
		if (typs != null)
		{
			s += " / " + typs;
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
			s += " , " + stat;
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
			s += JDFConstants.BLANK + StringUtil.leftStr(txt, 42);
			if (txt.length() > 42)
			{
				s += "...";
			}
		}
		return s;
	}

	protected String displayPhase(final KElement e, String s)
	{
		s += addAttributeValue(e, AttributeName.STATUS, null, null);
		s += addAttributeValue(e, AttributeName.STATUSDETAILS, "/", null);
		if (e instanceof JDFJobPhase)
		{
			s += addAttributeValue(e, AttributeName.PERCENTCOMPLETED, null, "%");
			s += addAttributeValue(e, AttributeName.JOBID, " JobID=", null);
		}
		return s;
	}

	protected String displayDeviceInfo(final KElement e, String s)
	{
		final JDFDeviceInfo di = (JDFDeviceInfo) e;
		String att = di.getDeviceID();
		if (!KElement.isWildCard(att))
		{
			s += JDFConstants.BLANK + att;
		}
		att = e.getAttribute(AttributeName.DEVICESTATUS, null, null);
		if (att != null)
		{
			s += JDFConstants.BLANK + att;
		}
		else // XJDF
		{
			att = e.getAttribute(AttributeName.STATUS, null, null);
			if (att != null)
			{
				s += JDFConstants.BLANK + att;
			}
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
			s += JDFConstants.BLANK + att;
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
			s += JDFConstants.BLANK + extID;
		}
		final String an = e.getNonEmpty(AttributeName.AGENTNAME);
		if (an != null)
		{
			s += JDFConstants.BLANK + an;
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
			{
				s += JDFConstants.BLANK + nodeStatus.getName();
			}
		}
		final JDFResource r = (JDFResource) e;
		final String partKey = r.getLocalPartitionKey();
		final String status = e.getNonEmpty_KElement(AttributeName.STATUS);
		if (status != null)
		{
			s += " / " + status;
		}
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
			s += JDFConstants.BLANK + name;
		}
		final String price = e.getAttribute(AttributeName.PRICE, null, null);
		if (price != null)
		{
			s += JDFConstants.BLANK + price;
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
			s += JDFConstants.BLANK + a;
		}
		final String name = e.getNonEmpty(AttributeName.PRODUCTTYPE);
		if (name != null)
		{
			s += JDFConstants.BLANK + name;
		}
		final String det = e.getNonEmpty(AttributeName.PRODUCTTYPEDETAILS);
		if (det != null)
		{
			s += JDFConstants.BLANK + det;
		}
		return s;
	}

	protected String displaySet(final KElement e, String s)
	{
		final String name = e.getNonEmpty("Name");
		final String procUsage = e.getNonEmpty(AttributeName.PROCESSUSAGE);
		final String cpi = e.getNonEmpty(AttributeName.COMBINEDPROCESSINDEX);
		String prefix = e.getAttribute(AttributeName.USAGE);
		if (procUsage != null)
		{
			prefix += "(" + procUsage + ")";
		}
		if (cpi != null)
		{
			prefix += "(" + cpi + ")";
		}
		s = prefix + JDFConstants.BLANK + s;
		if (name != null)
		{
			s += JDFConstants.BLANK + name;
		}
		s += "[" + e.numChildElements(XJDFConstants.Resource, null) + "]";
		return s;
	}

	protected String displayXRes(final KElement e, String s)
	{
		final ResourceHelper rh = new ResourceHelper(e);
		final String name = rh.getName();
		if (!StringUtil.isEmpty(name))
		{
			s += " " + name;
		}
		final String extID = e.getNonEmpty(XJDFConstants.ExternalID);
		if (extID != null)
		{
			s += JDFConstants.BLANK + extID;
		}
		final String desc = e.getNonEmpty(AttributeName.DESCRIPTIVENAME);
		if (desc != null)
		{
			s += ": " + desc;
		}
		final JDFAttributeMap common = rh.getPartMapVector().getCommonMap();
		return addPartMap(s, common);
	}

	/**
	 * @param s
	 * @param map
	 * @return
	 */
	private String addPartMap(String s, final JDFAttributeMap map)
	{
		final StringArray keys = map == null ? null : map.getKeyList();
		if (keys != null)
		{
			Collections.sort(keys);
			for (final String key : keys)
			{
				s += JDFConstants.BLANK + key + "=" + map.get(key);
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
		if (element instanceof final JDFNode n)
		{
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
				prefix = JDFConstants.BLANK + attName + "=";
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
	 *
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

	public Enumeration<JDFTreeNode> depthFirstJdfEnumeration()
	{
		return new JDFTreeNodeEnumeration(super.depthFirstEnumeration());
	}

	public Enumeration<JDFTreeNode> breadthFirstJdfEnumeration()
	{
		return new JDFTreeNodeEnumeration(super.breadthFirstEnumeration());
	}

	public Enumeration<JDFTreeNode> preorderJdfEnumeration()
	{
		return new JDFTreeNodeEnumeration(super.preorderEnumeration());
	}

	public Enumeration<JDFTreeNode> postorderJdfEnumeration()
	{
		return new JDFTreeNodeEnumeration(super.postorderEnumeration());
	}
}
