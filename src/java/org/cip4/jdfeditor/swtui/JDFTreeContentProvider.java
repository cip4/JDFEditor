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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFAuditPool;
import org.cip4.jdflib.resource.JDFCreated;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.w3c.dom.NamedNodeMap;
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
		if (element instanceof JDFCreated)
		{
		    JDFCreated e = (JDFCreated) element;
//		    KElement e = (KElement) element;
//		    e.getAttributeMap();
		    return e.getAttributes().getLength() > 0 ? true : false;
		} else if (element instanceof KElement)
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

		NodeList nlChildNodes = null;
		Object[] resultArray = new Object[0];
		if (o instanceof JDFDoc) {
		    JDFDoc jdfDoc = (JDFDoc) o;
		    nlChildNodes = jdfDoc.getChildNodes();
		    
//		    NamedNodeMap nnmAttr = jdfDoc.getAttributes();
//		    System.out.println("nnmAttr: " + nnmAttr);
		    
//		    System.out.println("getElements nlChildNodes.getLength(): " + nlChildNodes.getLength());
//		    resultArray = new Object[nlChildNodes.getLength()];
		} else if (o instanceof JDFJMF) {
		    JDFJMF jdfJmf = (JDFJMF) o;
            nlChildNodes = jdfJmf.getChildNodes();
		} else if (o instanceof JDFNode) {
			List<Object> resultList = new ArrayList<Object>();
			
			JDFNode jdfNode = (JDFNode) o;
			nlChildNodes = jdfNode.getChildNodes();
		    
			NamedNodeMap nnmAttr = jdfNode.getAttributes();
//			System.out.println("nnm: " + nnm);
			for (int i = 0; i < nnmAttr.getLength(); i++)
			{
				Node n = nnmAttr.item(i);
				System.out.println("attribute: " + n.getNodeName() +
						", value: " + n.getNodeValue());
				resultList.add(n);
			}
		    
		    resultArray = resultList.toArray();
		} else if (o instanceof JDFAuditPool) {
		    JDFAuditPool jdfAudit = (JDFAuditPool) o;
            nlChildNodes = jdfAudit.getChildNodes();
//            int l = nlChildNodes.getLength();
//            System.out.println("l: " + l);
//            resultArray = new Object[l];
            
            List<Object> resultList = new ArrayList<Object>();
            resultArray = resultList.toArray();
		} else if (o instanceof JDFCreated) {
		    List<Object> resultList = new ArrayList<Object>();
		    JDFCreated jdfCreated = (JDFCreated) o;
		    nlChildNodes = jdfCreated.getChildNodes();
		    
		    NamedNodeMap nnmAttr = jdfCreated.getAttributes();
		    for (int i = 0; i < nnmAttr.getLength(); i++)
            {
		        Node n = nnmAttr.item(i);
		        resultList.add(n);
            }
		    resultArray = resultList.toArray();
		}
		
		for (int i = 0; i < nlChildNodes.getLength(); i++)
		{
			if (nlChildNodes.item(i) instanceof JDFNode) {
				JDFNode n = (JDFNode) nlChildNodes.item(i);
				
				List<Object> l = Arrays.asList(resultArray);
				List<Object> l1 = new ArrayList<Object>(l);
				l1.add(n);
				
				resultArray = l1.toArray();
			} else if (nlChildNodes.item(i) instanceof JDFJMF) {
			    JDFJMF n = (JDFJMF) nlChildNodes.item(i);
                
                List<Object> l = Arrays.asList(resultArray);
                List<Object> l1 = new ArrayList<Object>(l);
                l1.add(n);
                
                resultArray = l1.toArray();
			} else if (nlChildNodes.item(i) instanceof JDFAuditPool) {
			    JDFAuditPool n = (JDFAuditPool) nlChildNodes.item(i);
			    
			    List<Object> l = Arrays.asList(resultArray);
				List<Object> l1 = new ArrayList<Object>(l);
				l1.add(n);
				
				resultArray = l1.toArray();
			} else if (nlChildNodes.item(i) instanceof JDFCreated) {
				JDFCreated n = (JDFCreated) nlChildNodes.item(i);
			    
			    List<Object> l = Arrays.asList(resultArray);
				List<Object> l1 = new ArrayList<Object>(l);
				l1.add(n);
				
				resultArray = l1.toArray();
			} else {
				Node n = nlChildNodes.item(i);
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
