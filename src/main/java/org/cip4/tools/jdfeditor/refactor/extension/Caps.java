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
package org.cip4.tools.jdfeditor.refactor.extension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.cip4.jdflib.core.ElementName;
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
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.refactor.JDFDeviceCapGenerator;

/**
 * xjdf style linear capabilities list
 * @author prosirai
 * 
 */
public class Caps
{
	private final KElement xjdf;
	private final JDFDeviceCapGenerator dcgen;

	/**
	 * @param e
	 */
	public Caps(KElement e)
	{
		xjdf = e;
		dcgen = new JDFDeviceCapGenerator(null, null);
	}

	/**
	 * @param bStateInDevCap
	 * 
	 * @return the modified root
	 * 
	 */
	public KElement createCaps(boolean bStateInDevCap)
	{
		return new CapsCreator().createCaps(bStateInDevCap);
	}

	// experimental simple capabilities file
	class CapsCreator
	{
		final XMLDoc docCaps;
		final KElement rootCap;
		final HashSet<String> setDoneXPaths;
		final HashMap<String, JDFAbstractState> mapDoneAttNames;

		/**
		 * 
		 */
		public CapsCreator()
		{
			super();
			docCaps = new JDFDoc("DeviceCap");
			rootCap = docCaps.getRoot();
			rootCap.setXMLComment("Very Preliminaty Draft JDF 2.0 Capabilities example\n" + "Warning!!! This is a preliminary prototype proof of concept only\n"
					+ "Names and values are subject to change without notice\n " + "Generated by JDFEditor version: " + JDFAudit.software());
			setDoneXPaths = new HashSet<String>();
			mapDoneAttNames = new HashMap<String, JDFAbstractState>();
		}

		/**
		 * @param bStateInDevCap 
		 * @param xjdf
		 * @param genericAtts
		 * @return
		 * 
		 */
		public KElement createCaps(boolean bStateInDevCap)
		{
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

			for (KElement e : vXJDFElements)
			{
				processElement(e);
			}

			cleanStates(rootCap);
			if (bStateInDevCap)
				moveStates(rootCap);
			return rootCap;
		}

		protected void processElement(KElement e)
		{
			final String elmXPathFull = e.buildXPath(null, 0);
			String elmXPath = elmXPathFull.substring(0, elmXPathFull.lastIndexOf("/"));
			final JDFAttributeMap map = new JDFAttributeMap();
			if (elmXPath.length() == 0)
			{
				map.put("XPath", elmXPathFull);
			}
			else
			{
				map.put("XPath", e.getNodeName());
				map.put("XPathRoot", elmXPath);
			}
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
				processAttribute(e, elmXPathFull, state);
			}
		}

		protected void processAttribute(KElement e, final String elmXPathFull, JDFAbstractState state)
		{
			final String name = state.getAttribute("Name");
			JDFAbstractState sameName = mapDoneAttNames.get(name);
			if (sameName != null)
			{
				String xpathRootSame = sameName.getAttribute("XPathRoot");
				if (elmXPathFull.equals(xpathRootSame))
				{
					sameName = null;
				}
				else
				{
					setDoneXPaths.remove(sameName.getAttribute("XPathRoot") + "/@" + name);
					setDoneXPaths.add("//*/@" + name);
				}
			}
			mapDoneAttNames.put(name, state);
			//final boolean isGeneric = new VString("ID", null).contains(name);
			final boolean isGeneric = sameName != null;
			final String context = isGeneric ? "//*" : elmXPathFull;
			final String fullpath = context + "/@" + name;
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

		/**
		 * Tmove states to elementstates and rename elementstates to devcap
		 * @param rootCap
		 */
		private void moveStates(KElement rootCap)
		{
			VElement vElementStates = rootCap.getChildElementVector("ElementState", null);
			KElement devCapPool = rootCap.getCreateElement(ElementName.DEVCAPPOOL);
			KElement defaultCap = devCapPool.appendElement(ElementName.DEVCAP);
			defaultCap.setAttribute("XPath", "//*");
			Map<String, KElement> pathMap = new HashMap<String, KElement>();
			pathMap.put("//*", defaultCap);
			for (KElement elmState : vElementStates)
			{
				elmState.renameElement("DevCap", null);
				devCapPool.moveElement(elmState, null);
				String newPath = elmState.getAttribute("XPathRoot");
				if (StringUtil.getNonEmpty(newPath) != null)
					newPath += "/";
				newPath += elmState.getAttribute("XPath");
				pathMap.put(newPath, elmState);
			}

			vElementStates = rootCap.getChildElementVector(null, null);
			for (KElement elmState : vElementStates)
			{
				if (elmState.getNodeName().endsWith("State"))
				{
					String root = elmState.getAttribute("XPathRoot");
					KElement e = pathMap.get(root);
					if (e == null)
						e = rootCap;
					e.moveElement(elmState, null);
					elmState.removeAttribute("XPathRoot");
				}
			}
		}
	}

	/**
	 * @param rootCap 
	 * 
	 */
	private void cleanStates(KElement rootCap)
	{
		VElement v = rootCap.getChildElementVector(null, null);
		Iterator<KElement> it = v.iterator();
		while (it.hasNext())
		{
			KElement e = it.next();
			if (e.getLocalName().endsWith("State"))
				e.removeAttribute("Name");
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param capFile
	 * @param xjdfRoot
	 * @return
	 * 
	 */
	public VString getBadAttributes(final XMLDoc capFile, final KElement xjdfRoot)
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
