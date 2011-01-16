/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2011 The International Cooperation for the Integration of 
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
package org.cip4.jdfeditor.swtui;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFAuditPool;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JDFTreeContentProvider implements ITreeContentProvider
{

	public Object[] getChildren(Object parent)
	{
		System.out.println("getChildren parent: " + parent);
		return getElements(parent);
	}

	public Object getParent(Object object)
	{
		System.out.println("getParent object: " + object);
		return null;
	}

	public boolean hasChildren(Object element)
	{
		System.out.println("hasChildren element: " + element);
		if (element instanceof KElement)
		{
			KElement e = (KElement) element;
//			System.out.println("hasChildren e.getNodeName(): " + e.getNodeName() + ", xsi:type: " + e.getXSIType());
			boolean r = e.hasChildNodes();
			System.out.println("hasChildren r: " + r);
			return r;
		} else
		{
			System.out.println("hasChildren else element: " + element);
		}
		System.out.println("hasChildren return false");
		return false;
	}

	public Object[] getElements(Object o)
	{
		System.out.println("getElements o: " + o);
		
//		KElement e = (KElement) o;
//		VElement elements = e.getChildElementVector(null, null);
//		elements.toArray();

		NodeList nl = null;
		Object[] resultArray = null;
		if (o instanceof JDFDoc) {
		    JDFDoc jdfDoc = (JDFDoc) o;
		    nl = jdfDoc.getChildNodes();
		    System.out.println("getElements nl.getLength(): " + nl.getLength());
		    resultArray = new Object[nl.getLength()];
		} else if (o instanceof JDFNode) {
		    JDFNode jdfNode = (JDFNode) o;
		    nl = jdfNode.getChildNodes();
		    resultArray = new Object[nl.getLength()];
		} else if (o instanceof JDFAuditPool) {
		    JDFAuditPool jdfAudit = (JDFAuditPool) o;
            nl = jdfAudit.getChildNodes();
            resultArray = new Object[nl.getLength()];
		}
		
		for (int i = 0; i < nl.getLength(); i++)
		{
			if (nl.item(i) instanceof JDFNode) {
				JDFNode n = (JDFNode) nl.item(i);
				resultArray[i] = n;
			} else if (nl.item(i) instanceof JDFJMF) {
				JDFJMF n = (JDFJMF) nl.item(i);
			} if (nl.item(i) instanceof JDFAuditPool) {
			    JDFAuditPool n = (JDFAuditPool) nl.item(i);
			    resultArray[i] = n;
			} else {
				Node n = nl.item(i);
				if (n.getNodeType() == Node.COMMENT_NODE)
				{
					System.out.println("getElements comment node: " + n/*.getNodeName()*/);
				}
			}
		}
		
		return resultArray;
	}

	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// TODO Auto-generated method stub
		
	}

}
