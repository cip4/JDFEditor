/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2008 The International Cooperation for the Integration of 
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
package org.cip4.jdfeditor;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.ArrayUtils;
import org.cip4.jdfeditor.extensions.Caps;
import org.cip4.jdfeditor.extensions.XJDF20;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.VJDFAttributeMap;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.devicecapability.JDFDeviceCap;
import org.cip4.jdflib.util.ContainerUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.jdflib.validate.JDFValidator;
import org.w3c.dom.Attr;

/**
 * 
 * @author rainer prosi
 * This is a new dump for some of the JDFFrame classes
 * anything related to the abstract datamodel in the jdf tree belongs here
 * TODO move some of the routines from JDFTreeArea to here, where they belong and reduce the dependencies with JDFFrame
 */
public class JDFTreeModel extends DefaultTreeModel
{

	/**
	 * 
	 */
	public static final String TEXT = "#text"; // string for element content text labels
	private XMLDoc validationResult; // the checkJDF XML output result
	/**
	 * 
	 */
	private static final long serialVersionUID = -5922527273385407946L;
	private boolean m_ignoreAttributes = false;

	JDFTreeModel(JDFTreeNode _root, boolean ignoreAttributes)
	{
		super(_root);
		m_ignoreAttributes = ignoreAttributes;
		validationResult = null;

	}

	public boolean validate()
	{
		Runtime.getRuntime().gc(); // clean up before validating
		//this may be what needs to be done (i.e. getFrame()) for the variables in other methods that are being moved from JDFFrame to here.
		JDFFrame m_frame = Editor.getFrame();
		final JDFDoc theDoc = m_frame.getJDFDoc();
		if (theDoc == null)
			return false;

		final INIReader iniFile = Editor.getIniFile();
		JDFValidator checkJDF = new JDFValidator();
		checkJDF.setPrint(false);
		checkJDF.bQuiet = true;
		checkJDF.level = iniFile.getValidationLevel();
		checkJDF.bMultiID = true;
		checkJDF.setWarning(iniFile.getWarnCheck());
		XMLDoc schemaValidationResult = null;

		checkJDF.setIgnorePrivate(!iniFile.getHighlight());
		checkJDF.bWarnDanglingURL = iniFile.getCheckURL();

		final String fn = theDoc.getOriginalFileName();
		if (iniFile.getExportValidation())
		{
			checkJDF.xmlOutputName = StringUtil.newExtension(fn, ".validate.xml");
		}
		else
		{
			checkJDF.xmlOutputName = null;
		}
		if (iniFile.getUseSchema())
		{

			File f = theDoc.getSchemaLocationFile(JDFElement.getSchemaURL());
			if (!UrlUtil.isFileOK(f)) // snafu, try the internal default 
				f = iniFile.getSchemaURL();

			if (UrlUtil.isFileOK(f))
			{
				checkJDF.setJDFSchemaLocation(f);

				try
				{
					File tmpFile = File.createTempFile("tmp", ".jdf");
					theDoc.write2File(tmpFile.getAbsolutePath(), 0, false);
					theDoc.setOriginalFileName(fn);
					FileInputStream inStream = new FileInputStream(tmpFile);
					JDFDoc tmpDoc = EditorUtils.parseInStream(inStream, true);
					if (tmpDoc == null)
					{
						inStream.close();
						inStream = new FileInputStream(tmpFile);
						tmpDoc = EditorUtils.parseInStream(inStream, false);
					}

					tmpFile.delete();
					if (tmpDoc != null)
						schemaValidationResult = tmpDoc.getValidationResult();
				}
				catch (Exception e)
				{
					// nop
				}

			}
			else
			{
				iniFile.setSchemaURL(null);
			}
		}
		// TODO addFile
		validationResult = checkJDF.processSingleDocument(theDoc);

		m_frame.m_errorTabbedPane.m_validErrScroll.drawCheckJDFOutputTree(validationResult);
		m_frame.m_errorTabbedPane.m_SchemaErrScroll.drawSchemaOutputTree(schemaValidationResult);
		Editor.getEditorDoc().getJDFTree().repaint();
		m_frame.m_treeArea.goToPath(m_frame.m_treeArea.getSelectionPath());
		return validationResult.getRoot().getFirstChildElement().getBoolAttribute("IsValid", null, true);
	}

	/**
	 * inserts element before selected node
	 */
	public void insertElementBefore(final JDFTreeNode parentNode, final JDFTreeNode beforeNode)
	{
		if (parentNode == null)
			return;

		final KElement parentElement = parentNode.getElement();
		final String insertElementName = EditorUtils.chooseElementName(parentElement);

		JDFTreeNode newNode = null;
		if (insertElementName != null && !insertElementName.equals(JDFConstants.EMPTYSTRING))
		{

			String elemNS = getNSURI(parentElement, insertElementName);
			final KElement siblingElement = beforeNode == null ? null : beforeNode.getElement();
			KElement newElement = parentElement.insertBefore(insertElementName, siblingElement, elemNS);
			newNode = createNewNode(newElement);

			addRequiredAttributes(newNode);
			addRequiredElements(newNode);
			insertInto(newNode, parentNode, parentNode.getIndex(beforeNode));

			Editor.getFrame().updateViews(new TreePath(newNode.getPath()));
			if (Editor.getIniFile().getAutoVal())
				validate();
			final InsertElementEdit edit = new InsertElementEdit(beforeNode, newNode, "Insert Element");
			Editor.getFrame().undoSupport.postEdit(edit);
		}
	}

	/**
	 * Method getSiblingElement.
	 * gets the sibling KElement for this treepath
	 * @param path
	 * @return KElement
	 */
	public KElement getSiblingElement(final TreePath path)
	{
		final JDFTreeNode sibling = (JDFTreeNode) path.getLastPathComponent();
		return sibling.getElement();
	}

	/**
	 * Method getParentElement.
	 * gets the parent element for this treepath.
	 * If this treepath is a path of a root element, returns root.
	 * @param path
	 * @return KElement
	 */
	public KElement getParentElement(TreePath path)
	{
		JDFTreeNode parentNode = (JDFTreeNode) path.getLastPathComponent();

		if (!((JDFTreeNode) ((JDFTreeNode) root).getFirstChild()).equals(parentNode))
		{
			parentNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
		}
		return parentNode.getElement();
	}

	/**
	 * Method buildModel.
	 * Creates the JDFTreeNodes
	 * @param node
	 * @param m_root
	 */
	public void buildModel(JDFTreeNode my_Root)
	{
		KElement element = my_Root.getElement();
		addNodeAttributes(my_Root);
		KElement e = element.getFirstChildElement();
		while (e != null)
		{
			JDFTreeNode tn = new JDFTreeNode(e);
			my_Root.add(tn);
			buildModel(tn);
			e = e.getNextSiblingElement();
		}
	}

	///////////////////////////////////////////////////////////////////
	/**
	 * get the models root node as a JDFTreeNode
	 * @return the root Node
	 */
	JDFTreeNode getRootNode()
	{
		return (JDFTreeNode) getRoot();
	}

	///////////////////////////////////////////////////////////////
	/**
	 * Method insertAttribute.
	 * inserts a new AttributeNode into the m_jdfTree
	 * @param path
	 * @param attribute
	 */
	public JDFTreeNode setAttribute(JDFTreeNode parentNode, String attName, String attValue, String attNS, boolean bInherit)
	{
		if (parentNode == null || !parentNode.isElement())
		{
			throw new JDFException("setAttribute: not an element");
		}
		if (TEXT.equals(attName))
			return setText(parentNode, attValue);
		JDFTreeNode nOld = parentNode.getNodeWithName(attName);

		final KElement e = parentNode.getElement();
		// we have an existing node, simply rename tha value and ciao!
		if (nOld != null)
		{
			final Attr oldattr = nOld.getAttr();
			if (oldattr.getOwnerElement() == e) // this is not an inherited tree node
			{
				setNodeValue(oldattr, attValue);
				nOld.setUserObject(oldattr);

			}
			else
			// this is an inherited node; must create a new attribute in the DOM tree and link it
			{
				e.setAttribute(attName, attValue, attNS);
				Attr newAttr = e.getDOMAttr(attName, attNS, false);
				nOld.isInherited = false;
				nOld.setUserObject(newAttr);
				e.setDirty(true);
			}
			return nOld;
		}

		// create a new node whether the attribute exists in the dom or not
		int index = parentNode.getInsertIndexForName(attName, true);
		Attr attr = e.getDOMAttr(attName, attNS, bInherit);
		// no attribute in DOM, create it
		if (attr == null)
		{
			e.setAttribute(attName, attValue, attNS);
			attr = e.getDOMAttr(attName, attNS, bInherit);
			String attNSReal = attr.getNamespaceURI();
			final String xmlnsPrefix = KElement.xmlnsPrefix(attName);
			if (attNSReal == null && xmlnsPrefix != null)
			{
				e.addNameSpace(xmlnsPrefix, "http://www." + xmlnsPrefix + ".com");
				Attr attrNS = e.getDOMAttr("xmlns:" + xmlnsPrefix, null, true);
				JDFTreeNode nodeNS = new JDFTreeNode(attrNS, attrNS.getParentNode() != e);
				nodeNS.setAllowsChildren(false);
				insertInto(nodeNS, parentNode, index);
			}
		}
		else if (attr.getOwnerElement() == e) // this is not an inherited tree node
		{
			setNodeValue(attr, attValue);
		}
		// create the new tree node
		nOld = new JDFTreeNode(attr, bInherit);
		nOld.setAllowsChildren(false);
		insertInto(nOld, parentNode, index);
		return nOld;
	}

	/**
	* @param parentNode
	* @param attValue
	* @return
	*/
	private JDFTreeNode setText(JDFTreeNode parentNode, String attValue)
	{
		JDFTreeNode nOld = parentNode.getNodeWithName(TEXT);
		// we have an existing node, simply rename tha value and ciao!
		if (nOld != null)
		{
			final String s = nOld.getText();
			if (!ContainerUtil.equals(s, attValue))
			{
				nOld.setText(attValue);
			}
		}
		else
		{
			// create the new tree node
			nOld = new JDFTreeNode();
			nOld.setAllowsChildren(false);
			nOld.setText(attValue);
			int index = parentNode.getInsertIndexForName(TEXT, true);
			insertInto(nOld, parentNode, index);
		}
		return nOld;
	}

	/**
	 * sets an attribute value if it differs
	 * @param attValue
	 * @param oldattr
	 */
	private void setNodeValue(final Attr oldattr, String attValue)
	{
		if (!oldattr.getValue().equals(attValue))
		{
			oldattr.setNodeValue(attValue);
			((KElement) oldattr.getOwnerElement()).setDirty(true);
		}
	}

	/**
	 * Method addRequiredAttributes.
	 * adds the required attributes to an element
	 * @param newNode
	 */

	public Vector addRequiredAttributes(JDFTreeNode newNode)
	{
		final KElement kElement = newNode.getElement();

		Vector addedAttributeNodesVector = new Vector();
		VString requiredAttributes = kElement.getMissingAttributes(9999999);
		if (kElement instanceof JDFNode)
		{
			if (!kElement.hasAttribute(AttributeName.JOBPARTID))
				requiredAttributes.add(AttributeName.JOBPARTID);
		}

		for (int i = 0; i < requiredAttributes.size(); i++)
		{
			final String attValue = JDFElement.getValueForNewAttribute(kElement, requiredAttributes.stringAt(i));
			final JDFTreeNode attrNode = setAttribute(newNode, requiredAttributes.stringAt(i), attValue, null, false);
			addedAttributeNodesVector.add(attrNode);
		}
		return addedAttributeNodesVector;
	}

	///////////////////////////////////////////////////////

	public JDFTreeNode appendNode(final String newNodeName, final JDFTreeNode parentNode)
	{
		final KElement element = parentNode.getElement();
		JDFTreeNode newNode = null;
		if (newNodeName != null && element != null)
		{
			KElement newElement = element.appendElement(newNodeName);
			newNode = createNewNode(newElement);
			insertInto(newNode, parentNode, -1);
		}
		return newNode;
	}

	/////////////////////////////////////////////////////////////////

	public void insertInto(final JDFTreeNode newNode, final JDFTreeNode parentNode, int pos)
	{
		if (parentNode.isElement())
		{
			int childCount = parentNode.getChildCount();
			if (pos == -1 || (pos >= 0 && pos > childCount))
			{
				pos = childCount;
			}
			insertNodeInto(newNode, parentNode, pos);
		}
	}

	/**
	 * Method addRequiredElements.
	 * adds the required elements to an element
	 * @param newNode
	 */
	public Vector addRequiredElements(JDFTreeNode node)
	{
		final KElement kElement = node.getElement();

		Vector addedElementNodesVector = new Vector();
		VString requiredElements = kElement.getMissingElements(9999999);
		final String[] abstractElems = { ElementName.RESOURCELINK, "ResourceRef" };
		for (int i = 0; i < requiredElements.size(); i++)
		{
			final String elName = requiredElements.stringAt(i);
			if (ArrayUtils.contains(abstractElems, elName))
				continue;
			final KElement newElement = kElement.appendElement(elName);
			final JDFTreeNode newNode = createNewNode(newElement);
			insertInto(newNode, node, -1);
			addedElementNodesVector.add(newNode);
		}
		autoValidate();
		return addedElementNodesVector;
	}

	/**
	 * Method createNewNode.
	 * creates a new JDFTreeNode for the KElement without putting it into the tree
	 * @param elem the KElement to create a treenode from
	 * @return JDFTreeNode the newly created tree node
	 */
	public JDFTreeNode createNewNode(KElement elem)
	{
		JDFTreeNode newNode = new JDFTreeNode(elem);
		addNodeAttributes(newNode);
		return newNode;
	}

	/**
	 * Adds the attributes to the JDFTreeNode and checks if they are invalid
	 * and inherited or not.
	 * @param node    - The JDFTreeNode you want to add the attributes to     * @param m_invalidNode  - The InvalidNode
	 */
	public void addNodeAttributes(final JDFTreeNode node)
	{
		KElement elem = node.getElement();
		if (!elem.hasAttributes())
			return; // nothing to do
		final INIReader iniFile = Editor.getIniFile();
		if (iniFile.getAttr() == false)
			return;

		boolean showDefaultAtts = iniFile.getDisplayDefault();
		// get of 'elem' all not inherited attribute names
		VString vAttNames = elem.getAttributeVector_KElement();

		vAttNames = processDefaultAttributes(vAttNames, elem, showDefaultAtts);
		int attSize = vAttNames.size();
		for (int i = 0; i < attSize; i++)
		{
			final String attName = vAttNames.stringAt(i);
			if (!m_ignoreAttributes || attName.equals(AttributeName.TYPE) || attName.equals(AttributeName.TYPES)
					|| attName.equals(AttributeName.DESCRIPTIVENAME) || attName.equals(AttributeName.ID))
			{
				String attVal = elem.getAttribute(attName);
				setAttribute(node, attName, attVal, null, false);
			}
		}
		// Show inherited attributes in the In&Output View and in the Tree View if that feature is selected
		if (!m_ignoreAttributes && (elem instanceof JDFResource && iniFile.getInhAttr()))
		{
			JDFResource res = (JDFResource) elem;

			// Create a vector with all the attribute names including the inherited attributes
			VString vInheritedAttNames = res.getAttributeVector_JDFResource();
			vInheritedAttNames = processDefaultAttributes(vInheritedAttNames, elem, showDefaultAtts);

			for (int i = 0; i < vInheritedAttNames.size(); i++)
			{
				String attNameStr = vInheritedAttNames.stringAt(i);

				// Add the attribute to the TreeNode, but only if it is an inherited attribute
				if (!vAttNames.contains(attNameStr))
				{
					final String attName = vInheritedAttNames.stringAt(i);
					String attVal = elem.getAttribute(attName);
					setAttribute(node, attName, attVal, null, true);
				}
			}
		}
		String text = elem.getText();
		if (text != null)
		{
			text = text.trim();
			if (text.length() > 0)
			{
				setAttribute(node, TEXT, text, null, false);
			}
		}
	}

	/**
	 * @param elem
	 * @param showDefaultAtts
	 * @return
	 */
	private VString processDefaultAttributes(VString vAttNames, KElement elem, boolean showDefaultAtts)
	{
		int attSize = vAttNames.size();

		// remove defaults if any
		if (!showDefaultAtts)
		{
			Map defMap = elem.getDefaultAttributeMap();
			if (defMap != null)
			{
				for (int d = attSize - 1; d >= 0; d--)
				{
					final String key = vAttNames.stringAt(d);
					String defValue = (String) defMap.get(key);
					if (defValue != null && elem.getAttribute(key).equals(defValue))
						vAttNames.remove(d);
				}
			}
		}
		return vAttNames;
	}

	/**
	 * Find the inherited node in the Tree View when selected in
	 * the In & Output View.
	 * @param nNode - The current JDFTreeNode in the enumeration
	 * @param sNode - The Node you want to find
	 * @return The inherited attribute for the JDFTreeNode.
	 */
	public JDFTreeNode getInhNode(JDFTreeNode nNode, JDFTreeNode sNode)
	{
		nNode = (JDFTreeNode) nNode.getParent();
		final String attName = sNode.getName();
		final String attValue = sNode.getValue();
		final String sNodeString = sNode.toString();

		while (((JDFTreeNode) nNode.getParent()).getUserObject() != null)
		{
			nNode = (JDFTreeNode) nNode.getParent();
			final KElement elem = nNode.getElement();
			if (attValue.equals(elem.getAttribute(attName, null, null)))
			{
				JDFTreeNode n = null;
				for (int i = 0; i < nNode.getChildCount(); i++)
				{
					n = (JDFTreeNode) nNode.getChildAt(i);
					if (n.toString().equals(sNodeString))
						return n;
				}
			}
		}
		return null;
	}

	/**
	 * Inserts new Resource JDFTreeNode into the m_jdfTree.
	 * If node had no ResourcePool - creates it.
	 * @param parentNode - JDFNode to add resource to
	 * @param node - JDFTreeNode representation of parentNode
	 * @param selectedResource - name of resource to insert
	 * @param hasResourcePool - Has parentNode had a resourcePool before action started? 
	 * Importent for representation of m_jdfTree. ResourcePool is automatically added to parentNode
	 * but we need to insert it into m_model.
	 * @param withLink - insert Resource + ResourceLink if true and only Resource if false
	 * @param input - resource link usage. true - input, false - output 
	 * 
	 * @returns created newResourceNode. null if operation was not completed successful
	 */
	public JDFTreeNode insertNewResourceNode(final JDFNode parentNode, final JDFTreeNode node, final String selectedResource, boolean hasResourcePool, EnumUsage usage)
	{
		JDFTreeNode newResourceNode = null;

		// if parentNode has no ResourcePool method addResource creates it.
		// Class=Unknown because we don't know now what class this res belongs to
		// but in a virtual method init() that is used in addResource 
		// attribute Class with a correct value will be added automatically
		JDFResource newResource = parentNode.addResource(selectedResource, null, usage, null, null, null, null);

		if (newResource != null)
		{
			final JDFResourcePool rPoolElm = parentNode.getCreateResourcePool();
			String xpath = rPoolElm.buildXPath(null, 1);
			JDFTreeNode rPool = null;
			boolean bFound = false;

			if (!hasResourcePool)
			{
				rPool = new JDFTreeNode(rPoolElm);
				insertInto(rPool, node, -1);
				bFound = true;
			}
			else
			{
				final Enumeration e = node.breadthFirstEnumeration();
				Object currNode;

				while (e.hasMoreElements() && !bFound)
				{
					currNode = e.nextElement();
					final JDFTreeNode treeNode = (JDFTreeNode) currNode;
					if (xpath.equals(treeNode.getXPath()))
					{
						rPool = treeNode;
						bFound = true;
					}
				}
			}
			if (bFound)
			{
				newResourceNode = createNewNode(newResource);
				insertInto(newResourceNode, rPool, -1);
				autoValidate();
			}
			else
			{
				EditorUtils.errorBox("ErrInsertResLink", null);
			}
		}
		else
		{
			EditorUtils.errorBox("ErrInsertRes", null);
		}

		return newResourceNode;
	}

	/**
	 * Inserts new ResourceLink JDFTreeNode into the m_jdfTree.
	 * If node had no ResourceLinkPool - creates it.
	 * @param parentNode - JDFNode to add resource link to
	 * @param node - JDFTreeNode representation of parentNode
	 * @param hasResourceLinkPool - Has parentNode had a resourceLinkPool before action started? 
	 * Importent for representation of m_jdfTree. ResourceLinkPool is automatically added to parentNode
	 * but we need to insert it into m_model.
	 * @param resLink - ResourceLink to insert
	 * 
	 * @returns created newLinkNode. null if operation was not completed successful
	 */
	public JDFTreeNode insertNewResourceLinkNode(final JDFNode parentNode, final JDFTreeNode node, boolean hasLinkPool, final JDFResourceLink resLink)
	{
		JDFTreeNode newLinkNode = null;
		final JDFResourceLinkPool rLinkPoolElm = parentNode.getResourceLinkPool();

		JDFTreeNode rLinkPool = null;
		boolean bFound = false;
		if (rLinkPoolElm == null)
		{
			rLinkPool = appendNode(ElementName.RESOURCELINKPOOL, node);
			bFound = true;
		}
		else if (!hasLinkPool) // the link pool was created by a low level call - must create a treenode for the link pool
		{
			rLinkPool = new JDFTreeNode(rLinkPoolElm);
			insertInto(rLinkPool, node, -1);
			bFound = true;
		}
		else
		{
			String xpath = rLinkPoolElm.buildXPath(null, 1);
			final Enumeration e = node.breadthFirstEnumeration();
			Object currNode;

			while (e.hasMoreElements() && !bFound)
			{
				currNode = e.nextElement();
				final JDFTreeNode treeNode = (JDFTreeNode) currNode;
				if (xpath.equals(treeNode.getXPath()))
				{
					rLinkPool = treeNode;
					bFound = true;
				}
			}
		}
		if (bFound)
		{
			newLinkNode = createNewNode(resLink);

			insertInto(newLinkNode, rLinkPool, -1);
			autoValidate();
		}
		else
		{
			EditorUtils.errorBox("ErrInsertResLink", null);
		}
		return newLinkNode;
	}

	/**
	 * Method renameElementsAndAttributes.
	 * renames the selected node in the m_jdfTree and updates the jdfDoc
	 * @param path
	 */
	public String renameElementsAndAttributes(final TreePath path)
	{
		final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
		final KElement originalElement = node.getElement();
		String selectedName = null;
		JDFFrame m_frame = Editor.getFrame();
		if (node.isElement())
		{
			final KElement parentElement = originalElement.getParentNode_KElement();
			if (parentElement != null)
			{
				try
				{
					if (originalElement instanceof JDFResource && parentElement instanceof JDFResourcePool)
					{
						final JDFNode parentResourceNode = ((JDFNode) originalElement).getJDFRoot();
						selectedName = m_frame.m_treeArea.chooseResourceName(parentResourceNode);
					}
					else
					{
						selectedName = EditorUtils.chooseElementName(parentElement);
					}
				}
				catch (Exception e)
				{
					return null;
				}
			}
		}
		else
		// attribute case
		{
			final KElement parent = node.getElement();
			final String[] possibleValues = EditorUtils.getAttributeOptions(parent);

			final ResourceBundle resourceBundle = Editor.getBundle();
			selectedName = (String) JOptionPane.showInputDialog(m_frame, resourceBundle.getString("ChooseNewAttTypeKey"), resourceBundle.getString("RenameKey"), JOptionPane.PLAIN_MESSAGE, null, possibleValues, possibleValues[0]);

			if (selectedName != null && selectedName.equals("Other.."))
			{
				selectedName = JOptionPane.showInputDialog(m_frame, resourceBundle.getString("InsertNewAttTypeKey"), resourceBundle.getString("InsertNewAttTypeKey"), JOptionPane.PLAIN_MESSAGE);
			}
		}

		return selectedName;
	}

	/**
	 * rename the attribute described by node to selectedValue
	 * 
	 * @param node the JDFTreeNode that represents the attribute
	 * @param selectedValue the new attribute name
	 * @return JDFTreeNode the treeNode that represents the renamed attribute
	 */
	public JDFTreeNode renameAttribute(final JDFTreeNode node, String selectedValue)
	{
		if (selectedValue == null || selectedValue.equals(JDFConstants.EMPTYSTRING))
			return null;
		KElement originalElement = node.getElement();
		final String oldAttribute = node.getName();
		final String attrValue = originalElement.getAttribute(oldAttribute);
		final JDFTreeNode parentNode = (JDFTreeNode) node.getParent();
		removeNodeFromParent(node);
		originalElement.removeAttribute(oldAttribute, null);
		return setAttribute(parentNode, selectedValue, attrValue, null, false);
	}

	/**
	 * deletes selected Node
	 */
	public void deleteSelectedNodes()
	{
		final TreePath[] paths = Editor.getEditorDoc().getSelectionPaths();
		if (paths != null)
		{
			JDFFrame m_frame = Editor.getFrame();
			for (int i = paths.length - 1; i >= 0; i--)
			{
				final DeleteItemEdit edit = new DeleteItemEdit(paths[i]);
				m_frame.undoSupport.postEdit(edit);
			}
		}
	}

	/**
	 * Method deleteItem. deletes attributes or elements
	 * deletes the selected node in the m_jdfTree an removes it from the
	 * jdfDoc as well
	 * @param treePath
	 */
	public boolean deleteItem(final TreePath treePath)
	{
		if (treePath == null)
			return false;
		final JDFTreeNode node = (JDFTreeNode) treePath.getLastPathComponent();
		return deleteNode(node, treePath);
	}

	/**
	 * Method deleteNode.
	 * deletes the selected node in the m_jdfTree an removes it from the
	 * jdfDoc as well, note that it must reside in a valid tree to correctly work
	 * @param treePath
	 */
	public boolean deleteNode(final JDFTreeNode node, TreePath path)
	{
		if (node == null)
			return false;
		JDFTreeNode parentNode = (JDFTreeNode) node.getParent();
		if (parentNode == null && path != null)
		{
			path = path.getParentPath();
			if (path != null)
				parentNode = (JDFTreeNode) path.getLastPathComponent();
		}

		if (!node.isElement() && node.isInherited)
		{
			// cannot delete an inherited node!

			return false;
		}
		if (parentNode == null)
			return false;

		try
		{
			removeNodeFromParent(node);
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
			EditorUtils.errorBox("DeleteErrorKey", " Node=" + node + " ParentNode=" + parentNode
					+ " ParentNode.getIndex(Node)=" + parentNode.getIndex(node));
			return false;
		}
		final KElement parentElement = parentNode.getElement();

		if (!node.isElement())
		{
			final String attributeName = node.getName();
			parentElement.removeAttribute(attributeName, null);
		}
		else
		{
			final KElement kElement = node.getElement();
			kElement.deleteNode();
		}
		autoValidate();

		JDFFrame m_frame = Editor.getFrame();
		EditorDocument ed = m_frame.getEditorDoc();
		JTree jtree = ed.getJDFTree();
		m_frame.m_topTabs.m_inOutScrollPane.clearInOutView();
		jtree.repaint();

		final TreePath treePath = new TreePath(parentNode.getPath());
		jtree.scrollPathToVisible(treePath);
		ed.setSelectionPath(treePath, true);
		return true;
	}

	///////////////////////////////////////////////////////////

	private void autoValidate()
	{
		if (Editor.getIniFile().getAutoVal())
		{
			validate();
		}
	}

	/**
	 * return the rrorotype associated with a given tree node
	 * @param treeNode
	 * @return
	 */
	public String getErrorType(JDFTreeNode treeNode)
	{
		if (validationResult == null)
			return null;

		if (treeNode instanceof DCOutputWrapper)
			return null;

		String xPath = treeNode.getXPath();
		JDFAttributeMap map = new JDFAttributeMap("XPath", xPath);
		map.put("ErrorType", null);
		KElement e = validationResult.getRoot().getChildByTagName(null, null, 0, map, false, true);

		return e == null ? null : e.getAttribute("ErrorType");
	}

	public boolean isValid(JDFTreeNode treeNode)
	{
		return getErrorType(treeNode) == null;
	}

	/**
	 * @return Returns the validationResult.
	 */
	public XMLDoc getValidationResult()
	{
		return validationResult;
	}

	public KElement getValidationRoot()
	{
		if (validationResult == null)
			return null;
		return validationResult.getRoot();
	}

	/**
	 * Method insertAttributeIntoDoc.
	 * creates a new attribute and adds it to the jdfDoc
	 * @param node
	 */
	public JDFTreeNode insertAttributeIntoDoc(JDFTreeNode node)
	{
		JDFTreeNode newAttrNode = null;
		if (!node.isElement())
		{
			node = (JDFTreeNode) node.getParent();
		}
		JDFFrame m_frame = Editor.getFrame();

		final KElement element = node.getElement();
		final String[] possibleValues = EditorUtils.getAttributeOptions(element);
		final ResourceBundle resourceBundle = Editor.getBundle();
		final String attName = resourceBundle.getString("ChooseNewAttTypeKey");
		String selectedValue = (String) JOptionPane.showInputDialog(m_frame, attName, attName, JOptionPane.PLAIN_MESSAGE, null, possibleValues, possibleValues[0]);

		if (selectedValue != null && selectedValue.equals("Other.."))
		{
			selectedValue = JOptionPane.showInputDialog(m_frame, attName, "");
		}
		if (selectedValue != null && !selectedValue.equals(JDFConstants.EMPTYSTRING))
		{
			String ns = getNSURI(element, selectedValue);

			newAttrNode = setAttribute(node, selectedValue, JDFElement.getValueForNewAttribute(element, selectedValue), ns, false);
		}

		return newAttrNode;
	}

	/**
	 * get the ns uri for a given element or attribute name
	 * @param element
	 * @param selectedValue
	 * @return
	 */
	private String getNSURI(final KElement parentElement, String selectedValue)
	{
		String ns = KElement.xmlnsPrefix(selectedValue);
		if (ns != null)
		{
			ns = parentElement.getNamespaceURIFromPrefix(ns);
			if (ns == null)
				ns = JOptionPane.showInputDialog(Editor.getFrame(), Editor.getBundle().getString("ChoosePrefixKey"), "");
			if (ns == null || ns.equals(JDFConstants.EMPTYSTRING))
			{
				EditorUtils.errorBox("InvalidNamespaceKey", KElement.xmlnsPrefix(selectedValue));
			}
		}
		return ns;
	}

	/**
	 * @param selectionPath
	 * @experimental
	 */
	public void saveAsXJDF(TreePath selectionPath)
	{
		JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
			return;
		KElement e = node.getElement();
		boolean bZip = e.hasChildElement(ElementName.JDF, null);
		EditorDocument eDoc = Editor.getEditorDoc();
		String fn = eDoc.getOriginalFileName();
		if (bZip)
		{
			XJDF20.saveZip(StringUtil.newExtension(fn, "zip"), (JDFNode) e, true);
		}
		else
		{
			KElement xJDF = XJDF20.makeNewJDF((JDFNode) e, (VJDFAttributeMap) null);
			XMLDoc d = xJDF.getOwnerDocument_KElement();
			String fnNew = StringUtil.newExtension(fn, XJDF20.getExtension());
			d.write2File(fnNew, 2, false);
			Editor.getFrame().readFile(new File(fnNew));
		}
	}

	/**
	 * @param selectionPath
	 * @experimental
	 */
	public void saveAsXJDFCaps(TreePath selectionPath)
	{
		JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
			return;
		KElement e = node.getElement();
		EditorDocument eDoc = Editor.getEditorDoc();
		String fn = eDoc.getOriginalFileName();
		KElement caps = Caps.createCaps(e, new VString(Editor.getIniFile().getGenericAtts(), " "));
		XMLDoc d = caps.getOwnerDocument_KElement();
		String fnNew = StringUtil.newExtension(fn, "cap");
		d.write2File(fnNew, 2, false);
		//        VString badCaps=Caps.getBadAttributes(d, e);
		Editor.getFrame().readFile(new File(fnNew));
	}

	/**
	 * @param selectionPath
	 */
	public void createNodeFromCaps(TreePath selectionPath)
	{
		JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
			return;
		JDFDeviceCap dc = (JDFDeviceCap) node.getElement();
		EditorDocument eDoc = Editor.getEditorDoc();
		String fn = eDoc.getOriginalFileName();

		JDFDoc d = new JDFDoc("JDF");
		JDFNode n = d.getJDFRoot();
		dc.setDefaultsFromCaps(n, true, Editor.getIniFile().getGenerateFull());
		String fnNew = StringUtil.newExtension(fn, ".generated.JDF");
		d.write2File(fnNew, 2, false);
		Editor.getFrame().readFile(new File(fnNew));
	}

	/**
	 * @param selectionPath
	 */
	public void normalize(TreePath selectionPath)
	{
		JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
			return;
		KElement e = node.getElement();
		if (e != null)
			e.sortChildren();
		Editor.getFrame().refreshView(null, selectionPath);

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Methods moved from JDFFrame.java
	 */

	/**
	 * Finds a JDFTreeNode in the JDFTree.
	 * @param row     - The row in the JTree
	 * @param jTree - The JTree
	 * @return The JDFTreeNode.
	 * Moved 07-08-17
	 */
	public JDFTreeNode getTreeNode(int row, JTree jTree)
	{
		JDFTreeNode node;
		final TreePath path = jTree.getPathForRow(row);

		if (path != null)
			node = (JDFTreeNode) path.getLastPathComponent();
		else
			node = getRootNode();

		return node;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
}
