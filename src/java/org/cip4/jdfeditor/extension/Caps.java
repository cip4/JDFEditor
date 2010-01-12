/*
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
//Titel:        JDF TestApplication
//Version:
//Copyright:    Copyright (c) 1999
//Autor:       Sabine Jonas, sjonas@topmail.de
//Firma:      BU/GH Wuppertal
//Beschreibung:  first Applications using the JDFLibrary
//package testApps;
package org.cip4.jdfeditor.extension;

import java.util.HashSet;

import org.cip4.jdfeditor.JDFDeviceCapGenerator;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.JDFBaseDataTypes.EnumFitsValue;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;
import org.cip4.jdflib.resource.devicecapability.JDFAction;
import org.cip4.jdflib.resource.devicecapability.JDFActionPool;
import org.cip4.jdflib.resource.devicecapability.JDFTest;
import org.cip4.jdflib.resource.devicecapability.JDFTestPool;

/**
 * xjdf style linear capabilities list
 * @author prosirai
 * 
 */
public class Caps
{
	// experimental simple capabilities file
	/**
	 * @param xjdf
	 * @param genericAtts
	 * @return
	 * 
	 */
	public static KElement createCaps(final KElement xjdf, final VString genericAtts)
	{
		final XMLDoc d = new JDFDoc("DeviceCap");
		final KElement rootCap = d.getRoot();
		rootCap.setXMLComment("Very Preliminaty Draft JDF 2.0 Capabilities example\n" + "Warning!!! This is a preliminary prototype proof of concept only\n"
				+ "Names and values are subject to change without notice\n " + "Generated by JDFEditor version: " + JDFAudit.software());
		final JDFActionPool ap = (JDFActionPool) rootCap.appendElement("ActionPool");
		final JDFTestPool tp = (JDFTestPool) rootCap.appendElement("TestPool");
		final JDFAction a = ap.appendAction();
		final JDFTest t = tp.appendTest();
		a.setTest(t);

		// grab all elements
		final VElement vXJDFElements = xjdf.getChildrenByTagName(null, null, null, false, true, 0);
		vXJDFElements.add(xjdf);
		// final JDFDevCap dcGeneric = (JDFDevCap) rootCap.appendElement(ElementName.DEVCAP);
		// final JDFDevCap dcDummy = (JDFDevCap) rootCap.appendElement(ElementName.DEVCAP);
		// dcGeneric.setAttribute(AttributeName.XPATH, "//*"); // / todo what is xpath for any

		final JDFDeviceCapGenerator dcgen = new JDFDeviceCapGenerator(null, null);
		final HashSet<String> setDoneXPaths = new HashSet<String>();
		for (int i = 0; i < vXJDFElements.size(); i++)
		{
			final KElement e = vXJDFElements.item(i);

			final String elmXPathFull = e.buildXPath(null, 0);
			String elmXPath = elmXPathFull.substring(0, elmXPathFull.lastIndexOf("/"));
			if (elmXPath.length() == 0)
			{
				elmXPath = "/";
			}
			final JDFAttributeMap map = new JDFAttributeMap(AttributeName.XPATH, elmXPath);
			map.put("XPath", e.getNodeName());
			map.put("XPathRoot", elmXPath);
			KElement elementState;
			if (setDoneXPaths.contains(elmXPathFull))
			{
				elementState = rootCap.getChildByTagName(null, null, 0, map, true, true);
				elementState.setAttribute("MaxOccurs", Integer.MAX_VALUE, null);
			}
			else
			{
				elementState = rootCap.appendElement("ElementState");
				elementState.setAttribute("MinOccurs", 0, null);
				elementState.setAttribute("MaxOccurs", 1, null);
				elementState.setAttributes(map);
				setDoneXPaths.add(elmXPathFull);
			}
			// generate states
			final VElement vStates = dcgen.setStatesForAttributes(e, rootCap);
			for (int j = 0; j < vStates.size(); j++)
			{
				JDFAbstractState state = (JDFAbstractState) vStates.item(j);
				final String name = state.getAttribute("Name");
				final boolean isGeneric = genericAtts.contains(name);
				final String fullpath = isGeneric ? "@" + name : elmXPathFull + "/@" + name;
				final String context = isGeneric ? "//*" : elmXPathFull;
				state.setAttribute("XPathRoot", context);
				state.setAttribute("XPath", "@" + name);
				// final KElement newRoot = isGeneric ? dcGeneric : dc;
				final KElement newRoot = rootCap;
				if (!setDoneXPaths.contains(fullpath))
				{
					state = (JDFAbstractState) newRoot.moveElement(state, null);
					setDoneXPaths.add(fullpath);
				}
				else
				{
					// state.deleteNode();
					state = (JDFAbstractState) newRoot.getChildWithAttribute(null, "Name", null, name, 0, true);
					state.addValue(e.getAttribute(name), EnumFitsValue.Present);
				}
			}
		}
		// dcDummy.deleteNode();
		return rootCap;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param capFile
	 * @param xjdfRoot
	 * @return
	 * 
	 */
	public static VString getBadAttributes(final XMLDoc capFile, final KElement xjdfRoot)
	{
		if (xjdfRoot == null)
		{
			return null;
		}

		final VString vBadPaths = new VString();
		final VElement vXJDFElements = xjdfRoot.getChildrenByTagName(null, null, null, false, true, 0);
		vXJDFElements.add(xjdfRoot);
		final KElement capRoot = capFile.getRoot();
		final VElement devcaps = capRoot.getChildElementVector(null, null, new JDFAttributeMap("Name", ""), true, 0, false);
		final VElement states = new VElement();
		for (int i = 0; i < devcaps.size(); i++)
		{
			states.addAll(devcaps.item(i).getChildElementVector(null, null, new JDFAttributeMap("Name", ""), true, 0, false));
		}
		for (int i = 0; i < vXJDFElements.size(); i++)
		{
			final KElement e = vXJDFElements.item(i);
			final VString keys = e.getAttributeVector();
			for (int j = 0; j < keys.size(); j++)
			{
				final String attribute = keys.stringAt(j);
				boolean bFound = false;
				for (int k = 0; k < states.size(); k++)
				{
					if (!(states.item(k) instanceof JDFAbstractState))
					{
						continue; // its a devcap - move on
					}
					final JDFAbstractState state = (JDFAbstractState) states.item(k);
					if (attribute.equals(state.getAttribute("Name")))
					{
						if (e.hasXPathNode(state.getParentNode_KElement().getAttribute("XPath")))
						{
							if (state.fitsValue(e.getAttribute(attribute), EnumFitsValue.Allowed))
							{
								bFound = true;
								break;
							}
						}
					}
				}
				if (!bFound)
				{
					vBadPaths.add(e.buildXPath(null, 1) + "/" + attribute);
				}
			}

		}
		return vBadPaths.isEmpty() ? null : vBadPaths;
	}
}
