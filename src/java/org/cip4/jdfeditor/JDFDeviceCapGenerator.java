package org.cip4.jdfeditor;

/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2010 The International Cooperation for the Integration of 
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
import java.util.zip.DataFormatException;

import org.cip4.jdflib.auto.JDFAutoBasicPreflightTest.EnumListType;
import org.cip4.jdflib.auto.JDFAutoDevCaps.EnumContext;
import org.cip4.jdflib.auto.JDFAutoDeviceCap.EnumCombinedMethod;
import org.cip4.jdflib.auto.JDFAutoDeviceInfo.EnumDeviceStatus;
import org.cip4.jdflib.auto.JDFAutoValue.EnumValueUsage;
import org.cip4.jdflib.core.AttributeInfo.EnumAttributeType;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.JDFDateTimeRange;
import org.cip4.jdflib.datatypes.JDFDateTimeRangeList;
import org.cip4.jdflib.datatypes.JDFDurationRange;
import org.cip4.jdflib.datatypes.JDFDurationRangeList;
import org.cip4.jdflib.datatypes.JDFNumberRangeList;
import org.cip4.jdflib.jmf.JDFDeviceInfo;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFResponse;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFDevice;
import org.cip4.jdflib.resource.JDFDeviceList;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.JDFValue;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;
import org.cip4.jdflib.resource.devicecapability.JDFAction;
import org.cip4.jdflib.resource.devicecapability.JDFActionPool;
import org.cip4.jdflib.resource.devicecapability.JDFBooleanState;
import org.cip4.jdflib.resource.devicecapability.JDFDateTimeState;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFDevCapPool;
import org.cip4.jdflib.resource.devicecapability.JDFDevCaps;
import org.cip4.jdflib.resource.devicecapability.JDFDeviceCap;
import org.cip4.jdflib.resource.devicecapability.JDFDurationState;
import org.cip4.jdflib.resource.devicecapability.JDFIntegerState;
import org.cip4.jdflib.resource.devicecapability.JDFNumberState;
import org.cip4.jdflib.resource.devicecapability.JDFShapeState;
import org.cip4.jdflib.resource.devicecapability.JDFStringState;
import org.cip4.jdflib.resource.devicecapability.JDFTest;
import org.cip4.jdflib.resource.devicecapability.JDFTestPool;
import org.cip4.jdflib.span.JDFSpanBase;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.JDFDuration;

/**
 * JDFDeviceCapGenerator.java
 * 
 * @author Elena Skobchenko
 * 
 */
public class JDFDeviceCapGenerator
{
	private final JDFNode jdfRoot;
	private int dcID = 1000;
	private int stID = 3000;
	private final VString genericAttributes;
	private JDFDeviceCap deviceCap;
	private JDFDevCapPool devCapPool;
	private JDFActionPool actionPool;
	private JDFTestPool testPool;
	private JDFDoc docDevCap;

	public JDFDeviceCapGenerator(final JDFNode jdfNode, final VString genAttr)
	{
		this.jdfRoot = jdfNode;
		this.genericAttributes = genAttr;

		generateDeviceCapabilities();
	}

	public void generateDeviceCapabilities()
	{
		docDevCap = new JDFDoc(ElementName.JMF);

		final JDFJMF jmfRoot = docDevCap.getJMFRoot();

		if (jdfRoot != null && jmfRoot != null)
		{
			jmfRoot.init();
			jmfRoot.setSenderID("MessageSender");

			final JDFResponse responseKnownDevices = (JDFResponse) jmfRoot.appendMessageElement(JDFMessage.EnumFamily.Response, JDFResponse.EnumType.KnownDevices);

			final JDFDeviceList deviceList = responseKnownDevices.appendDeviceList();
			final JDFDeviceInfo deviceInfo = deviceList.appendDeviceInfo();
			deviceInfo.setDeviceStatus(EnumDeviceStatus.Idle);
			final JDFDevice device = deviceInfo.appendDevice();
			device.setDeviceID("DeviceID1");

			deviceCap = device.appendDeviceCap();

			final String nodeType = jdfRoot.getType();
			final VString nodeTypes = jdfRoot.getTypes();
			VString devCapTypes = new VString(nodeType, null); // nodeType is 1 entry
			EnumCombinedMethod combinedMethod = EnumCombinedMethod.None;
			if (nodeType.equals(JDFConstants.PROCESSGROUP))
			{
				combinedMethod = EnumCombinedMethod.ProcessGroup;
				devCapTypes = nodeTypes; // nodeTypes must be in this case non null
			}
			else if (nodeType.equals(JDFConstants.COMBINED))
			{
				combinedMethod = EnumCombinedMethod.Combined;
				devCapTypes = nodeTypes; // nodeTypes must be in this case non null
			}
			deviceCap.setCombinedMethod(combinedMethod);
			deviceCap.setGenericAttributes(genericAttributes);
			deviceCap.setTypes(devCapTypes);

			devCapPool = deviceCap.appendDevCapPool();

			final VElement vNodes = jdfRoot.getvJDFNode(null, null, false);
			final int size = vNodes.size();
			for (int i = size - 1; i >= 0; i--)
			{
				final JDFNode node = (JDFNode) vNodes.elementAt(i);

				setResourceLinksDescription(node); // first build all DevCaps + appropriate DevCap elements
				final VElement velem = node.getChildElementVector(null, null, null, false, 9999, false);
				for (int ii = 0; ii < velem.size(); ii++)
				{
					final KElement e = velem.item(ii);
					if (!((e instanceof JDFResourcePool) || (e instanceof JDFResourceLinkPool) || (e instanceof JDFNode)))
					{
						setElementDescription(e);
					}
				}
				setElementDescription(node);
			}

			// do this after all links of all nodes have been followed
			for (int i = size - 1; i >= 0; i--)
			{
				final JDFNode node = (JDFNode) vNodes.elementAt(i);
				setResourceDescription(node); // build the rest of DevCap
			}

			// set up one set of dummy tests and actions
			actionPool = deviceCap.appendActionPool();
			final JDFAction act = actionPool.appendAction();

			testPool = deviceCap.appendTestPool();
			final JDFTest test = testPool.appendTest();
			// link action to test
			act.setTestRef(test.getID());
		}
	}

	/**
	 * @param deviceCap
	 * @param node
	 */
	private void setResourceLinksDescription(final JDFNode node)
	{
		final JDFResourceLinkPool resLinkPool = node.getResourceLinkPool();

		VElement vResLinks = new VElement();
		if (resLinkPool != null)
		{// get all links
			vResLinks = resLinkPool.getPoolChildren(null, new JDFAttributeMap(), null);
		}

		final int size = vResLinks.size();
		for (int rl = 0; rl < size; rl++)
		{
			final JDFResourceLink resLink = (JDFResourceLink) vResLinks.elementAt(rl);
			final EnumUsage linkUsage = resLink.getUsage();
			final String processUsage = resLink.getProcessUsage();

			final JDFResource res = resLink.getTarget();
			if (res == null)
			{
				continue; // if smth is wrong with a resLink,
			} // continue to build DevCap from other links

			final JDFAttributeMap attrMap = new JDFAttributeMap();
			final String resName = res.getLocalName();
			final String resID = res.getID();

			attrMap.put(AttributeName.NAME, resName);
			if (!resID.equals(JDFConstants.EMPTYSTRING))
			{
				attrMap.put(AttributeName.ID, "DCs_" + resID);
			}

			final VElement vDevCaps = deviceCap.getChildElementVector(ElementName.DEVCAPS, null, attrMap, true, 99999, false);
			JDFDevCaps devCaps;
			boolean bFound = false;
			// TODO Processusage
			for (int i = 0; i < vDevCaps.size(); i++)
			{
				devCaps = (JDFDevCaps) vDevCaps.elementAt(i);
				if (!devCaps.hasAttribute("LinkUsage") || (devCaps.hasAttribute("LinkUsage") && devCaps.getLinkUsage().equals(linkUsage))
						&& (devCaps.getProcessUsage().equals(processUsage)))
				{
					bFound = true;
				}
			}

			if (!bFound)
			{
				devCaps = deviceCap.appendDevCaps();
				devCaps.setName(resName);
				devCaps.setDevNS(res.getNamespaceURI());
				final String id = "DCs_" + resID; // Hier resID must exist, cause DevCaps describes resources in ResPool
				devCaps.setID(id);
				devCaps.setContext(EnumContext.Resource); // default
				if (linkUsage != null)
				{
					devCaps.setLinkUsage(linkUsage);
				}
				if (processUsage.length() > 0)
				{
					devCaps.setProcessUsage(processUsage);
				}

				// create for every DevCap Name a corresponding DevCap in DevCapPool,
				// set refDevCap to reference DevCap from DevCaps
				final JDFDevCap dc = devCapPool.appendDevCap();
				dc.setName(resName);
				dc.setID("DC_" + resID);
				final String ref = "DC_" + resID;
				// final JDFDevCap dc = devCapPool.getCreateDevCapByName(resName,ref);
				// ref=dc.getID();
				devCaps.setDevCapRef(ref);
			}
		}
	}

	/**
	 * create devcaps + sub elements for direct children of a JDF
	 * @param elem the element to expand to a devcap
	 */
	private void setElementDescription(final KElement elem)
	{
		final String elemName = elem.getLocalName();
		JDFDevCaps devCaps = (JDFDevCaps) deviceCap.getChildWithAttribute(ElementName.DEVCAPS, AttributeName.NAME, null, elemName, 0, true);
		String elemID = elem.getAttribute("ID", null, null);

		if (devCaps == null)
		{
			devCaps = deviceCap.appendDevCaps();

			if (elemID == null)
			{
				elemID = Integer.toString(dcID);
				dcID++;
			}
			devCaps.setName(elemName);
			devCaps.setID("DCs_" + elemID);
			devCaps.setContext(EnumContext.Element);

			final JDFDevCap dc = devCapPool.appendDevCap();
			dc.setName(elemName);
			dc.setID("DC_" + elemID);
			devCaps.setDevCapRef("DC_" + elemID);
		}

		setDevCapForChildElements(elem, !elemName.equals(ElementName.JDF));
	}

	/**
	 * @param node
	 */
	private void setResourceDescription(final JDFNode node)
	{
		final JDFResourcePool resPool = node.getResourcePool();
		if (resPool != null)
		{// get all resources of ResourcePool
			final VElement vRes = resPool.getPoolChildren(null, null, null);// resolve the target
			for (int r = 0; r < vRes.size(); r++)
			{
				setDevCapForChildElements(vRes.elementAt(r), true);
			}
		}
	}

	/**
	 * Creates DevCap elements for all subelemens, sets refDevCaps of this (references to children), creates all State elements for Attributes and Spans, sets
	 * other attributes of DevCap for 'dcElem' (MinOccurs, MaxOccurs)
	 * @param dcElem - JDF subelement to work with
	 * @return DevCap/@ID of the correponding to dcrElem' DevCap element
	 */
	private String setDevCapForChildElements(final KElement dcElem, final boolean bRecurse)
	{
		final JDFAttributeMap attrMap = new JDFAttributeMap();
		final String parElemName = dcElem.getLocalName();
		attrMap.put(AttributeName.NAME, parElemName);

		String elemID = dcElem.getAttribute(AttributeName.ID, null, "");
		if (dcElem instanceof JDFResource)
		{
			final JDFResource r = (JDFResource) dcElem;
			elemID = r.getID(); // will get the correct id from the resourceroot and thus reuse the devcap
		}

		if (!elemID.equals(JDFConstants.EMPTYSTRING))
		{
			attrMap.put(AttributeName.ID, "DC_" + elemID);
		}

		JDFDevCap dc = (JDFDevCap) devCapPool.getChildByTagName(ElementName.DEVCAP, null, 0, attrMap, true, true);

		if (dc == null) // none there, create a new one
		{
			dc = devCapPool.appendDevCap();
			dc.setName(parElemName);

			if (elemID.equals(JDFConstants.EMPTYSTRING))
			{
				elemID = Integer.toString(dcID);
				dcID++;
			}
			dc.setID("DC_" + elemID);
		}
		elemID = dc.getID();

		// get all children of parElem we want to reference
		VElement vEl = null;
		if (dcElem instanceof JDFElement)
		{
			vEl = ((JDFElement) dcElem).getChildElementVector_JDFElement(null, null, null, true, 99999, true); // resolve the target
		}
		else
		{
			vEl = dcElem.getChildElementVector(null, null, null, true, 99999, true); // resolve the target
		}

		final VElement spanVector = new VElement();
		final VString devCapRefs = new VString();
		for (int i = 0; i < vEl.size(); i++)
		{
			if (!(vEl.elementAt(i) instanceof JDFElement))
			{
				continue; // safety for external namespace elements
			}

			final JDFElement element = (JDFElement) vEl.elementAt(i);

			if (element instanceof JDFSpanBase)
			{
				spanVector.addElement(element);
			}
			else if (bRecurse)
			{
				final String refStr = setDevCapForChildElements(element, bRecurse); // recursion
				if (!refStr.equals(elemID)) // can`t reference itself - case for Partition
				{
					devCapRefs.appendUnique(refStr);
				}
			}
		}
		if (!devCapRefs.isEmpty())
		{
			dc.setDevCapRefs(devCapRefs);
		}

		setStatesForSpans(spanVector, dc);
		setStatesForAttributes(dcElem, dc);
		setMinMaxOccurs(dcElem, dc);

		return elemID;
	}

	/**
	 * @param dcElem the element to create a dc for
	 * @param dc the devCap to generate
	 */
	private void setMinMaxOccurs(final KElement dcElem, final JDFDevCap dc)
	{

		if ((dcElem instanceof JDFResource) && (!((JDFResource) dcElem).isRootElement()))
		{
			return;
		}
		final KElement parentElem = dcElem.getParentNode_KElement();
		if (!(parentElem instanceof JDFElement))
		{
			return;
		}
		JDFElement jP = (JDFElement) parentElem;
		final String elementName = dcElem.getLocalName();

		if (jP.requiredElements().contains(elementName))
		{
			dc.setMinOccurs(1);
		}
		else
		// optional
		{
			dc.setMinOccurs(0);
		}

		if (jP.uniqueElements().contains(elementName))
		{
			dc.setMaxOccurs(1);
		}
		else
		// many are allowed
		{
			dc.setMaxOccurs(Integer.MAX_VALUE);
		}
	}

	/**
	 * @param dc
	 * @param spanVector
	 */
	private void setStatesForSpans(final VElement spanVector, final JDFDevCap dc)
	{
		// TODO set appropriate ListType, MinOccurs, MaxOccurs for lists
		for (int sp = 0; sp < spanVector.size(); sp++) // TBD without checking if already exist
		{
			final JDFSpanBase span = (JDFSpanBase) spanVector.elementAt(sp);
			final String spValue = span.hasAttribute("Preferred") ? span.getAttribute("Preferred") : span.getAttribute("Actual");
			final String dataType = span.getDataType().getName();
			final String stateType = dataType.substring(0, dataType.indexOf("Span")) + "State";

			boolean bAllowedList = true;
			JDFAbstractState st;
			if (dc.optionalElements().contains(stateType))
			{
				st = (JDFAbstractState) dc.appendElement(stateType);
			}
			else if (dataType.equals("EnumerationSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.ENUMERATIONSTATE);
			}
			else if (dataType.equals("IntegerSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.INTEGERSTATE);
			}
			else if (dataType.equals("NameSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.NAMESTATE);
			}
			else if (dataType.equals("NumberSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.NUMBERSTATE);
			}
			else if (dataType.equals("OptionSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.BOOLEANSTATE);
			}
			else if (dataType.equals("ShapeSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.SHAPESTATE);
			}
			else if (dataType.equals("StringSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.STRINGSTATE);
				bAllowedList = false;
				final JDFStringState ss = (JDFStringState) st;
				ss.setAllowedRegExp("*");
			}
			else if (dataType.equals("TimeSpan") || dataType.equals("DurationSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.DATETIMESTATE);
			}
			else if (dataType.equals("XYPairSpan"))
			{
				st = (JDFAbstractState) dc.appendElement(ElementName.XYPAIRSTATE);
			}
			else
			{
				st = (JDFAbstractState) dc.appendElement("StateBase");
			}
			st.setName(span.getNodeName());
			st.setDevNS(span.getNamespaceURI());

			final String sID = "S_" + Integer.toString(stID);
			st.setID(sID);
			stID++;
			st.setListType(EnumListType.Span);
			if (bAllowedList)
			{
				st.setAttribute("AllowedValueList", spValue);
			}
		}
	}

	/**
	 * 
	 * @param parElem the root element whose attributes arfe converted
	 * @param dc the root devcap element
	 */
	public VElement setStatesForAttributes(final KElement parElem, final KElement dc)
	{
		// TODO Complete list
		// TODO set appropriate ListType, MinOccurs, MaxOccurs for lists
		final VElement vStates = new VElement();
		final JDFAttributeMap attrmap = parElem.getAttributeMap();
		final VString vAttrMapKeys = attrmap.getKeys();
		for (int at = 0; at < vAttrMapKeys.size(); at++)
		{
			final String key = vAttrMapKeys.elementAt(at);
			final String value = attrmap.get(key);

			EnumAttributeType eAttrType = (parElem instanceof JDFElement) ? ((JDFElement) parElem).getAtrType(key) : null;
			if (eAttrType == null)
			{
				eAttrType = EnumAttributeType.string;
			}

			if (genericAttributes == null || !genericAttributes.contains(key))
			{
				if (key.startsWith("xmlns") || key.equals("xsi:type"))
				{
					continue;
				}

				JDFAbstractState state;
				final KElement stEl = dc.getChildWithAttribute(null, AttributeName.NAME, null, key, 0, true);

				// found an existing state and only expand it
				if (stEl != null && stEl instanceof JDFAbstractState)
				{
					state = (JDFAbstractState) stEl;
					if (eAttrType.equals(EnumAttributeType.string) || eAttrType.equals(EnumAttributeType.shortString) || eAttrType.equals(EnumAttributeType.Any)
							|| eAttrType.equals(EnumAttributeType.PDFPath) || eAttrType.equals(EnumAttributeType.matrix) || eAttrType.equals(EnumAttributeType.URI)
							|| eAttrType.equals(EnumAttributeType.URL))
					{
						JDFValue stateValue = (JDFValue) state.getChildWithAttribute(ElementName.VALUE, AttributeName.ALLOWEDVALUE, null, value, 0, true);
						if (stateValue == null)
						{
							stateValue = (JDFValue) state.appendElement(ElementName.VALUE);
							stateValue.setAllowedValue(value);
							stateValue.setValueUsage(EnumValueUsage.Present);
						}
					}
					else
					{
						state.appendAttribute("PresentValueList", value, null, null, true);
					}
				}
				else
				// create new state elements
				{

					if (eAttrType.equals(EnumAttributeType.boolean_)) // Boolean
					{
						final JDFBooleanState bState = (JDFBooleanState) dc.appendElement(ElementName.BOOLEANSTATE);
						bState.setAttribute(AttributeName.PRESENTVALUELIST, value);
						bState.setAttribute(AttributeName.ALLOWEDVALUELIST, "true false");
						state = bState;
					}
					else if (eAttrType.equals(EnumAttributeType.dateTime) || // DateTime
							eAttrType.equals(EnumAttributeType.DateTimeRange) || eAttrType.equals(EnumAttributeType.DateTimeRangeList))
					{
						final JDFDateTimeState dState = (JDFDateTimeState) dc.appendElement(ElementName.DATETIMESTATE);
						dState.setAttribute(AttributeName.PRESENTVALUELIST, value);
						final JDFDateTimeRange r = new JDFDateTimeRange(new JDFDate(0), new JDFDate(4000000000000l)); // roughly 2100
						final JDFDateTimeRangeList dtrl = new JDFDateTimeRangeList();
						dtrl.append(r);
						dState.setAllowedValueList(dtrl);
						state = dState;
					}
					else if (eAttrType.equals(EnumAttributeType.duration) || // Duration
							eAttrType.equals(EnumAttributeType.DurationRange) || eAttrType.equals(EnumAttributeType.DurationRangeList))
					{
						final JDFDurationState dState = (JDFDurationState) dc.appendElement(ElementName.DURATIONSTATE);
						dState.setAttribute(AttributeName.PRESENTVALUELIST, value);
						final JDFDurationRange r = new JDFDurationRange(new JDFDuration(0), new JDFDuration(Integer.MAX_VALUE - 123));
						final JDFDurationRangeList durl = new JDFDurationRangeList();
						durl.append(r);
						dState.setAllowedValueList(durl);
						state = dState;
					}
					else if (eAttrType.equals(EnumAttributeType.double_)
							|| // Number
							eAttrType.equals(EnumAttributeType.NumberList) || eAttrType.equals(EnumAttributeType.NumberRange)
							|| eAttrType.equals(EnumAttributeType.NumberRangeList))
					{
						final JDFNumberState nState = (JDFNumberState) dc.appendElement(ElementName.NUMBERSTATE);
						nState.setAttribute(AttributeName.PRESENTVALUELIST, value);
						nState.setAttribute(AttributeName.ALLOWEDVALUELIST, "-INF ~ INF");
						state = nState;
					}
					else if (eAttrType.equals(EnumAttributeType.integer)
							|| // Integer
							eAttrType.equals(EnumAttributeType.IntegerList) || eAttrType.equals(EnumAttributeType.IntegerRange)
							|| eAttrType.equals(EnumAttributeType.IntegerRangeList))
					{
						final JDFIntegerState iState = (JDFIntegerState) dc.appendElement(ElementName.INTEGERSTATE);
						iState.setAttribute(AttributeName.PRESENTVALUELIST, value);
						iState.setAttribute(AttributeName.ALLOWEDVALUELIST, "-INF ~ INF");
						state = iState;
					}
					else if (eAttrType.equals(EnumAttributeType.XYPair) || // XYPair
							eAttrType.equals(EnumAttributeType.XYPairRange) || eAttrType.equals(EnumAttributeType.XYPairRangeList))
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.XYPAIRSTATE);
						state.setAttribute(AttributeName.PRESENTVALUELIST, value);
						state.setAttribute(AttributeName.ALLOWEDVALUELIST, "-INF -INF ~ INF INF");
					}
					else if (eAttrType.equals(EnumAttributeType.shape) || // Shape
							eAttrType.equals(EnumAttributeType.ShapeRange) || eAttrType.equals(EnumAttributeType.ShapeRangeList))
					{
						final JDFShapeState sState = (JDFShapeState) dc.appendElement(ElementName.SHAPESTATE);
						sState.setAttribute(AttributeName.PRESENTVALUELIST, value);
						final JDFNumberRangeList nrl = new JDFNumberRangeList();
						nrl.append(0, Double.MAX_VALUE);
						sState.setAllowedX(nrl);
						sState.setAllowedY(nrl);
						sState.setAllowedZ(nrl);
						state = sState;
					}
					else if (eAttrType.equals(EnumAttributeType.rectangle) || // Rectangle
							eAttrType.equals(EnumAttributeType.RectangleRange) || eAttrType.equals(EnumAttributeType.RectangleRangeList))
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.RECTANGLESTATE);
						state.setAttribute(AttributeName.ALLOWEDVALUELIST, "0 0 0 0 ~ -99999 -99999 99999 99999");
						state.setAttribute(AttributeName.PRESENTVALUELIST, value);
						state.setAttribute(AttributeName.DEFAULTVALUE, value);
					}
					else if (eAttrType.equals(EnumAttributeType.enumeration) || // Enumeration
							eAttrType.equals(EnumAttributeType.enumerations) || // Enumeration
							eAttrType.equals(EnumAttributeType.JDFJMFVersion))
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.ENUMERATIONSTATE);
						state.setAttribute(AttributeName.ALLOWEDVALUELIST, value);
					}
					else if (eAttrType.equals(EnumAttributeType.NameRange) || eAttrType.equals(EnumAttributeType.NameRangeList) || eAttrType.equals(EnumAttributeType.NMTOKEN)
							|| eAttrType.equals(EnumAttributeType.NMTOKENS) || eAttrType.equals(EnumAttributeType.ID) || eAttrType.equals(EnumAttributeType.IDREF)
							|| eAttrType.equals(EnumAttributeType.IDREFS) || eAttrType.equals(EnumAttributeType.language) || eAttrType.equals(EnumAttributeType.languages)) // Name
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.NAMESTATE);
						state.setAttribute(AttributeName.PRESENTVALUELIST, value);
						state.setAttribute(AttributeName.ALLOWEDREGEXP, ".*");
					}
					else if (eAttrType.equals(EnumAttributeType.hexBinary)) // hexbin
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.NAMESTATE);
						state.setAttribute(AttributeName.ALLOWEDVALUELIST, value);
						state.setAttribute(AttributeName.ALLOWEDREGEXP, JDFConstants.REGEXP_HEXBINARY);
					}
					else if (eAttrType.equals(EnumAttributeType.URI) || eAttrType.equals(EnumAttributeType.URL)) // uri
					{
						// TODO: regexp for uri
						state = (JDFAbstractState) dc.appendElement(ElementName.STRINGSTATE);
						final JDFStringState ns = (JDFStringState) state;
						final JDFValue stringStateValue = (JDFValue) state.appendElement(ElementName.VALUE);
						stringStateValue.setAllowedValue(value);
						stringStateValue.setValueUsage(EnumValueUsage.Present);
						ns.setPresentRegExp("*");
					}
					else if (eAttrType.equals(EnumAttributeType.string) || eAttrType.equals(EnumAttributeType.shortString) || eAttrType.equals(EnumAttributeType.Any)) // String
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.STRINGSTATE);
						final JDFStringState stst = (JDFStringState) state;
						stst.setAllowedRegExp("*");
						final JDFValue stringStateValue = (JDFValue) state.appendElement(ElementName.VALUE);
						stringStateValue.setAllowedValue(value);
						stringStateValue.setValueUsage(EnumValueUsage.Present);
					}
					else if (eAttrType.equals(EnumAttributeType.matrix)) // Matrix
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.MATRIXSTATE);
						final JDFValue stringStateValue = (JDFValue) state.appendElement(ElementName.VALUE);
						stringStateValue.setAllowedValue(value);
						stringStateValue.setValueUsage(EnumValueUsage.Present);
					}
					else if (eAttrType.equals(EnumAttributeType.PDFPath)) // PDFPath
					{
						state = (JDFAbstractState) dc.appendElement(ElementName.PDFPATHSTATE);
						final JDFValue stringStateValue = (JDFValue) state.appendElement(ElementName.VALUE);
						stringStateValue.setAllowedValue(value);
						stringStateValue.setValueUsage(EnumValueUsage.Present);
					}
					else if (eAttrType.equals(EnumAttributeType.CMYKColor)) // PDFPath
					{
						state = (JDFNumberState) dc.appendElement(ElementName.NUMBERSTATE);
						final JDFNumberState ns = (JDFNumberState) state;
						try
						{
							ns.setAllowedValueList(new JDFNumberRangeList("0 ~ 1"));
						}
						catch (final DataFormatException e)
						{
							// nothing to do
						}

						state.setMinOccurs(4);
						state.setMaxOccurs(4);
						state.setListType(EnumListType.List);
					}
					else if (eAttrType.equals(EnumAttributeType.LabColor) || eAttrType.equals(EnumAttributeType.RGBColor))
					{
						state = (JDFNumberState) dc.appendElement(ElementName.NUMBERSTATE);
						final JDFNumberState ns = (JDFNumberState) state;
						try
						{
							if (eAttrType.equals(EnumAttributeType.LabColor))
							{
								ns.setAllowedValueList(new JDFNumberRangeList("0 ~ 100"));
							}
							else
							// RGB
							{
								ns.setAllowedValueList(new JDFNumberRangeList("0 ~ 1"));
							}
						}
						catch (final DataFormatException e)
						{
							// nothing to do
						}

						state.setMinOccurs(3);
						state.setMaxOccurs(3);
						state.setListType(EnumListType.List);
					}
					else
					{
						throw new JDFException("Invalid State Type: " + eAttrType.getName());
					}

					state.setName(key);
					state.setDevNS(parElem.getNamespaceURIFromPrefix(KElement.xmlnsPrefix(key)));

					state.setDescriptiveName("ToDo: Edit the various XXXValueList Attributes etc. to reflect the actual capabilities");
					final String sID = "S_" + Integer.toString(stID);
					state.setID(sID);
					stID++;
					vStates.add(state);
				}

				final boolean bRequired = (parElem instanceof JDFElement) ? ((JDFElement) parElem).requiredAttributes().contains(key) : false;
				state.setRequired(bRequired);

				EnumListType listType = EnumListType.SingleValue;

				// TODO better list type algorithms
				if (value.indexOf("~") > 0)
				{
					listType = EnumListType.RangeList;
				}
				else if (eAttrType.equals(EnumAttributeType.NMTOKENS) || eAttrType.equals(EnumAttributeType.NumberList) || eAttrType.equals(EnumAttributeType.IntegerList)
						|| eAttrType.equals(EnumAttributeType.enumerations))
				{
					listType = EnumListType.List;
				}
				else if (eAttrType.equals(EnumAttributeType.DateTimeRangeList) || eAttrType.equals(EnumAttributeType.DurationRangeList)
						|| eAttrType.equals(EnumAttributeType.IntegerRangeList) || eAttrType.equals(EnumAttributeType.NameRangeList)
						|| eAttrType.equals(EnumAttributeType.NumberRangeList))
				{
					listType = EnumListType.RangeList;
				}
				else if (eAttrType.equals(EnumAttributeType.IntegerRange) || eAttrType.equals(EnumAttributeType.NumberRange) || eAttrType.equals(EnumAttributeType.NameRange))
				{
					listType = EnumListType.Range;
				}
				else if (!eAttrType.equals(EnumAttributeType.string) && new VString(value, null).size() > 1)
				{
					listType = EnumListType.List;
				}

				state.setListType(listType);
				state.setAttribute("DefaultValue", value); // TBD correct later to set a single value and typeSafe!
			}
		}
		return vStates;
	}

	/**
	 * @return
	 */
	public JDFDoc getDevCapDoc()
	{
		return this.docDevCap;
	}

}
