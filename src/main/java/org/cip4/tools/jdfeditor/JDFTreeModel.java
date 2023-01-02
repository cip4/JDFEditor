/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2022 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumNodeStatus;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.VJDFAttributeMap;
import org.cip4.jdflib.elementwalker.XPathWalker;
import org.cip4.jdflib.extensions.XJDF20;
import org.cip4.jdflib.extensions.XJDFConstants;
import org.cip4.jdflib.extensions.XJDFHelper;
import org.cip4.jdflib.extensions.xjdfwalker.XJDFToJDFConverter;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.devicecapability.JDFDeviceCap;
import org.cip4.jdflib.util.ByteArrayIOStream;
import org.cip4.jdflib.util.ContainerUtil;
import org.cip4.jdflib.util.JDFSpawn;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.jdflib.validate.JDFValidator;
import org.cip4.tools.jdfeditor.extension.Caps;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;
import org.w3c.dom.Attr;

/**
 * @author rainer prosi This is a new dump for some of the JDFFrame classes anything related to the abstract datamodel in the jdf tree belongs here TODO move some of the routines
 *         from JDFTreeArea to here, where they belong and reduce the dependencies with JDFFrame
 */
public class JDFTreeModel extends DefaultTreeModel
{

	private final SettingService settingService = SettingService.getSettingService();
	private final static Log log = LogFactory.getLog(JDFTreeModel.class);

	/**
	 * Spawn informative TODO correctly dump into multiple file
	 * 
	 * @param bSpawnInformative
	 */
	public void spawn(final boolean bSpawnInformative)
	{
		final EditorDocument ed = MainView.getEditorDoc();
		if (ed == null)
		{
			return;
		}
		final JDFFrame frame = MainView.getFrame();
		try
		{
			final JDFTreeNode node = (JDFTreeNode) ed.getSelectionPath().getLastPathComponent();
			final JDFNode selectedNode = (JDFNode) node.getElement();
			final SpawnDialog spawnDialog = new SpawnDialog(selectedNode, bSpawnInformative);

			if (spawnDialog.bOK)
			{
				frame.clearViews();

				if (!bSpawnInformative)
				{
					frame.readFile(spawnDialog.newRootFile);
				}

				frame.readFile(spawnDialog.newPartFile);
			}
		}
		catch (final Exception e)
		{
			JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage("SpawnErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * undo spawn
	 */
	void unspawn()
	{
		final EditorDocument ed = MainView.getEditorDoc();
		if (ed == null)
		{
			return;
		}
		final JDFFrame frame = MainView.getFrame();
		try
		{
			final JDFTreeNode node = (JDFTreeNode) ed.getSelectionPath().getLastPathComponent();
			final JDFNode selectedNode = (JDFNode) node.getElement();

			while (true)
			{
				selectedNode.setPartStatus((JDFAttributeMap) null, EnumNodeStatus.Waiting, null);
				final JDFNode unspawned = new JDFSpawn(selectedNode).unSpawn(null);
				if (unspawned == null)
					break;
			}

			frame.refreshView(ed, null);

		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage("SpawnErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Merge
	 */
	public void merge()
	{
		final JDFFrame frame = MainView.getFrame();
		try
		{
			final MergeDialog mergeResult = new MergeDialog(MainView.getJDFDoc().getJDFRoot());

			final File f = mergeResult.getFileToSave();
			if (f != null)
			{
				frame.refreshView(MainView.getEditorDoc(), null);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage("MergeErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""),
					ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}

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
	private boolean json;

	public JDFTreeModel(final JDFTreeNode _root, final boolean ignoreAttributes, boolean json)
	{
		super(_root);
		m_ignoreAttributes = ignoreAttributes;
		validationResult = null;
		this.json = json;
	}

	public JDFTreeModel(final JDFTreeNode _root, final boolean ignoreAttributes)
	{
		this(_root, ignoreAttributes, false);
	}

	/**
	 *
	 * @return
	 */
	public boolean validate()
	{
		// this may be what needs to be done (i.e. getFrame()) for the variables in other methods that are being moved from JDFFrame to here.
		final JDFFrame m_frame = MainView.getFrame();
		final JDFDoc theDoc = m_frame.getJDFDoc();
		if (theDoc == null)
		{
			return false;
		}

		final JDFValidator checkJDF = new JDFValidator();
		checkJDF.setPrint(false);
		checkJDF.bQuiet = true;
		checkJDF.level = JDFElement.EnumValidationLevel.getEnum(settingService.getSetting(SettingKey.VALIDATION_LEVEL, String.class));
		checkJDF.bMultiID = true;
		checkJDF.setWarning(!JDFElement.EnumValidationLevel.isNoWarn(checkJDF.level));
		XMLDoc schemaValidationResult = null;

		checkJDF.setIgnorePrivate(settingService.getSetting(SettingKey.IGNORE_PRIVATE_VALIDATION, Boolean.class));
		checkJDF.bWarnDanglingURL = settingService.getSetting(SettingKey.VALIDATION_CHECK_URL, Boolean.class);

		final String fn = theDoc.getOriginalFileName();
		if (settingService.getSetting(SettingKey.VALIDATION_EXPORT, Boolean.class))
		{
			checkJDF.xmlOutputName = UrlUtil.newExtension(fn, ".validate.xml");
		}
		else
		{
			checkJDF.xmlOutputName = null;
		}
		if (settingService.getSetting(SettingKey.GENERAL_USE_SCHEMA, Boolean.class))
		{

			File f = theDoc.getSchemaLocationFile(JDFElement.getSchemaURL());
			final String validationSchemaUrl = settingService.getSetting(SettingKey.VALIDATION_SCHEMA_URL, String.class);

			if (!UrlUtil.isFileOK(f) && validationSchemaUrl != null)
			{
				f = new File(validationSchemaUrl);
			}

			if (UrlUtil.isFileOK(f))
			{
				checkJDF.setJDFSchemaLocation(f);

				try
				{
					final ByteArrayIOStream outStream = new ByteArrayIOStream();
					theDoc.write2Stream(outStream, 0, false);
					theDoc.setOriginalFileName(fn);
					JDFDoc tmpDoc = EditorUtils.parseInStream(outStream.getInputStream(), EditorUtils.getSchemaLoc());
					if (tmpDoc == null)
					{
						tmpDoc = EditorUtils.parseInStream(outStream.getInputStream(), null);
					}

					if (tmpDoc != null)
					{
						schemaValidationResult = tmpDoc.getValidationResult();
					}
				}
				catch (final Exception e)
				{
					// nop
				}

			}
			else
			{
				settingService.setSetting(SettingKey.VALIDATION_SCHEMA_URL, null);
			}
		}
		// TODO addFile
		validationResult = checkJDF.processSingleDocument(theDoc);

		m_frame.getBottomTabs().m_validErrScroll.drawCheckJDFOutputTree(validationResult);
		m_frame.getBottomTabs().m_SchemaErrScroll.drawSchemaOutputTree(schemaValidationResult);
		if (MainView.getEditorDoc().getJDFTree() != null)
		{
			MainView.getEditorDoc().getJDFTree().repaint();
			m_frame.getJDFTreeArea().goToPath(m_frame.getJDFTreeArea().getSelectionPath()); // TODO: what this code actually do ?
		}
		return validationResult.getRoot().getFirstChildElement().getBoolAttribute("IsValid", null, true);
	}

	/**
	 * inserts element before selected node
	 * 
	 * @param parentNode
	 * @param beforeNode
	 */
	public void insertElementBefore(final JDFTreeNode parentNode, final JDFTreeNode beforeNode)
	{
		if (parentNode == null)
		{
			return;
		}

		final KElement parentElement = parentNode.getElement();
		final String insertElementName = EditorUtils.chooseElementName(parentElement);

		JDFTreeNode newNode = null;
		if (insertElementName != null && !insertElementName.equals(JDFConstants.EMPTYSTRING))
		{
			final String elemNS = getNSURI(parentElement, insertElementName);
			final KElement siblingElement = beforeNode == null ? null : beforeNode.getElement();
			final KElement newElement = parentElement.insertBefore(insertElementName, siblingElement, elemNS);
			newNode = createNewNode(newElement);

			addRequiredAttributes(newNode);
			addRequiredElements(newNode);
			insertInto(newNode, parentNode, parentNode.getIndex(beforeNode));

			MainView.getFrame().updateViews(new TreePath(newNode.getPath()));
			if (settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class))
			{
				validate();
			}
			MainView.getFrame().getEditorDoc().setDirtyFlag();
			final InsertElementEdit edit = new InsertElementEdit(beforeNode, newNode, "Insert Element");
			MainView.getFrame().undoSupport.postEdit(edit);
		}
	}

	/**
	 * Method getSiblingElement. gets the sibling KElement for this treepath
	 * 
	 * @param path
	 * @return KElement
	 */
	public KElement getSiblingElement(final TreePath path)
	{
		final JDFTreeNode sibling = (JDFTreeNode) path.getLastPathComponent();
		return sibling.getElement();
	}

	/**
	 * Method getParentElement. gets the parent element for this treepath. If this treepath is a path of a root element, returns root.
	 * 
	 * @param path
	 * @return KElement
	 */
	public KElement getParentElement(final TreePath path)
	{
		JDFTreeNode parentNode = (JDFTreeNode) path.getLastPathComponent();

		if (!((JDFTreeNode) ((JDFTreeNode) root).getFirstChild()).equals(parentNode))
		{
			parentNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
		}
		return parentNode.getElement();
	}

	/**
	 * Method buildModel. Creates the JDFTreeNodes
	 * 
	 * @param my_Root
	 */
	public void buildModel(final JDFTreeNode my_Root)
	{
		final KElement element = my_Root.getElement();
		addNodeAttributes(my_Root);
		KElement e = element.getFirstChildElement();
		while (e != null)
		{
			final JDFTreeNode tn = new JDFTreeNode(e);
			my_Root.add(tn);
			buildModel(tn);
			e = e.getNextSiblingElement();
		}
	}

	// /////////////////////////////////////////////////////////////////
	/**
	 * get the models root node as a JDFTreeNode
	 * 
	 * @return the root Node
	 */
	JDFTreeNode getRootNode()
	{
		return (JDFTreeNode) getRoot();
	}

	// /////////////////////////////////////////////////////////////
	/**
	 * Method insertAttribute. inserts a new AttributeNode into the m_jdfTree
	 * 
	 * @param parentNode
	 * @param attName
	 * @param attValue
	 * @param attNS
	 * @param bInherit
	 * @return
	 */
	public JDFTreeNode setAttribute(final JDFTreeNode parentNode, final String attName, final String attValue, final String attNS, final boolean bInherit)
	{
		if (parentNode == null || !parentNode.isElement())
		{
			throw new JDFException("setAttribute: not an element");
		}
		if (TEXT.equals(attName))
		{
			return setText(parentNode, attValue);
		}
		else if (json && "xmlns".equals(StringUtil.token(attName, 0, ":")))
		{
			return null;
		}

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
				final Attr newAttr = e.getDOMAttr(attName, attNS, false);
				nOld.isInherited = false;
				nOld.setUserObject(newAttr);

				e.setDirty(true);
			}
			return nOld;
		}

		// create a new node whether the attribute exists in the dom or not
		final int index = parentNode.getInsertIndexForName(attName, true);
		Attr attr = e.getDOMAttr(attName, attNS, bInherit);
		// no attribute in DOM, create it
		if (attr == null)
		{
			e.setAttribute(attName, attValue, attNS);
			attr = e.getDOMAttr(attName, attNS, bInherit);
			final String attNSReal = attr.getNamespaceURI();
			final String xmlnsPrefix = KElement.xmlnsPrefix(attName);
			if (attNSReal == null && xmlnsPrefix != null)
			{
				e.addNameSpace(xmlnsPrefix, "http://www." + xmlnsPrefix + ".com");
				final Attr attrNS = e.getDOMAttr("xmlns:" + xmlnsPrefix, null, true);
				final JDFTreeNode nodeNS = new JDFTreeNode(attrNS, attrNS.getParentNode() != e);
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
	private JDFTreeNode setText(final JDFTreeNode parentNode, final String attValue)
	{
		JDFTreeNode nOld = parentNode.getNodeWithName(TEXT);
		// we have an existing node, simply rename the value and ciao!
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
			final int index = parentNode.getInsertIndexForName(TEXT, true);
			insertInto(nOld, parentNode, index);
		}
		return nOld;
	}

	/**
	 * sets an attribute value if it differs
	 * 
	 * @param attValue
	 * @param oldattr
	 */
	private void setNodeValue(final Attr oldattr, final String attValue)
	{
		if (!oldattr.getValue().equals(attValue))
		{
			oldattr.setNodeValue(attValue);
			((KElement) oldattr.getOwnerElement()).setDirty(true);
		}
	}

	/**
	 * Method addRequiredAttributes. adds the required attributes to an element
	 * 
	 * @param newNode
	 * @return
	 */
	public Vector<JDFTreeNode> addRequiredAttributes(final JDFTreeNode newNode)
	{
		final KElement kElement = newNode.getElement();

		final Vector<JDFTreeNode> addedAttributeNodesVector = new Vector<JDFTreeNode>();
		if (kElement instanceof JDFElement)
		{
			final VString requiredAttributes = ((JDFElement) kElement).getMissingAttributes(9999999);
			if (kElement instanceof JDFNode)
			{
				if (!kElement.hasAttribute(AttributeName.JOBPARTID))
				{
					requiredAttributes.add(AttributeName.JOBPARTID);
				}
			}

			for (int i = 0; i < requiredAttributes.size(); i++)
			{
				final String attValue = JDFElement.getValueForNewAttribute(kElement, requiredAttributes.get(i));
				final JDFTreeNode attrNode = setAttribute(newNode, requiredAttributes.get(i), attValue, null, false);
				addedAttributeNodesVector.add(attrNode);
			}
		}
		return addedAttributeNodesVector;
	}

	/**
	 * append a new node into an existing parent
	 * 
	 * @param newNodeName
	 * @param parentNode
	 * @return
	 */
	public JDFTreeNode appendNode(final String newNodeName, final JDFTreeNode parentNode)
	{
		final KElement element = parentNode.getElement();
		JDFTreeNode newNode = null;
		if (newNodeName != null && element != null)
		{
			final KElement newElement = element.appendElement(newNodeName);
			newNode = createNewNode(newElement);
			insertInto(newNode, parentNode, -1);
		}
		return newNode;
	}

	/**
	 * @param newNode
	 * @param parentNode
	 * @param pos
	 *
	 */
	public void insertInto(final JDFTreeNode newNode, final JDFTreeNode parentNode, int pos)
	{
		if (parentNode.isElement())
		{
			final int childCount = parentNode.getChildCount();
			if (pos == -1 || (pos >= 0 && pos > childCount))
			{
				pos = childCount;
			}
			insertNodeInto(newNode, parentNode, pos);
		}
	}

	/**
	 * Method addRequiredElements. adds the required elements to an element
	 * 
	 * @param node
	 * @return
	 */
	public Vector<JDFTreeNode> addRequiredElements(final JDFTreeNode node)
	{
		final KElement kElement = node.getElement();
		final Vector<JDFTreeNode> addedElementNodesVector = new Vector<JDFTreeNode>();
		if (kElement instanceof JDFElement)
		{
			final VString requiredElements = ((JDFElement) kElement).getMissingElements(9999999);
			final String[] abstractElems = { ElementName.RESOURCELINK, "ResourceRef" };
			for (int i = 0; i < requiredElements.size(); i++)
			{
				final String elName = requiredElements.get(i);
				if (ArrayUtils.contains(abstractElems, elName))
				{
					continue;
				}
				final KElement newElement = kElement.appendElement(elName);
				final JDFTreeNode newNode = createNewNode(newElement);
				insertInto(newNode, node, -1);
				addedElementNodesVector.add(newNode);
			}
		}
		autoValidate();
		return addedElementNodesVector;
	}

	/**
	 * Method createNewNode. creates a new JDFTreeNode for the KElement without putting it into the tree
	 * 
	 * @param elem the KElement to create a treenode from
	 * @return JDFTreeNode the newly created tree node
	 */
	public JDFTreeNode createNewNode(final KElement elem)
	{
		final JDFTreeNode newNode = new JDFTreeNode(elem);
		addNodeAttributes(newNode);
		return newNode;
	}

	/**
	 * Adds the attributes to the JDFTreeNode and checks if they are invalid and inherited or not.
	 * 
	 * @param node - The JDFTreeNode you want to add the attributes to * @param m_invalidNode - The InvalidNode
	 * @param json
	 */
	public void addNodeAttributes(final JDFTreeNode node)
	{
		final KElement elem = node.getElement();
		if (settingService.getSetting(SettingKey.TREEVIEW_ATTRIBUTE, Boolean.class) == false)
		{
			return;
		}

		final boolean showDefaultAtts = settingService.getSetting(SettingKey.GENERAL_DISPLAY_DEFAULT, Boolean.class);
		// get of 'elem' all not inherited attribute names
		VString vAttNames = elem.getAttributeVector_KElement();

		vAttNames = processDefaultAttributes(vAttNames, elem, showDefaultAtts);
		for (final String attName : vAttNames)
		{
			if (!m_ignoreAttributes || attName.equals(AttributeName.TYPE) || attName.equals(AttributeName.TYPES) || attName.equals(AttributeName.DESCRIPTIVENAME)
					|| attName.equals(AttributeName.ID))
			{
				final String attVal = elem.getAttribute(attName);
				setAttribute(node, attName, attVal, null, false);
			}
		}
		// Show inherited attributes in the In&Output View and in the Tree View if that feature is selected
		if (elem instanceof JDFResource)
		{
			final JDFResource res = (JDFResource) elem;
			if (node.getParent() == null)
			{
				final String id = StringUtil.getNonEmpty(res.getID());
				if (id != null)
					setAttribute(node, AttributeName.ID, id, null, true);

			}
			if (!m_ignoreAttributes && (elem instanceof JDFResource && settingService.getSetting(SettingKey.TREEVIEW_ATTRIBUTE_INHERITED, Boolean.class)))
			{

				// Create a vector with all the attribute names including the inherited attributes
				VString vInheritedAttNames = res.getAttributeVector_JDFResource();
				vInheritedAttNames = processDefaultAttributes(vInheritedAttNames, elem, showDefaultAtts);

				for (int i = 0; i < vInheritedAttNames.size(); i++)
				{
					final String attNameStr = vInheritedAttNames.get(i);

					// Add the attribute to the TreeNode, but only if it is an inherited attribute
					if (!vAttNames.contains(attNameStr))
					{
						final String attName = vInheritedAttNames.get(i);
						final String attVal = elem.getAttribute(attName);
						setAttribute(node, attName, attVal, null, true);
					}
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
	private VString processDefaultAttributes(final VString vAttNames, final KElement elem, final boolean showDefaultAtts)
	{
		final int attSize = vAttNames.size();

		// remove defaults if any
		if (!showDefaultAtts && (elem instanceof JDFElement))
		{
			final JDFAttributeMap defMap = ((JDFElement) elem).getDefaultAttributeMap();
			if (defMap != null)
			{
				for (int d = attSize - 1; d >= 0; d--)
				{
					final String key = vAttNames.get(d);
					final String defValue = defMap.get(key);
					if (defValue != null && elem.getAttribute(key).equals(defValue))
					{
						vAttNames.remove(d);
					}
				}
			}
		}
		return vAttNames;
	}

	/**
	 * Find the inherited node in the Tree View when selected in the In & Output View.
	 * 
	 * @param nNode - The current JDFTreeNode in the enumeration
	 * @param sNode - The Node you want to find
	 * @return The inherited attribute for the JDFTreeNode.
	 */
	public JDFTreeNode getInhNode(JDFTreeNode nNode, final JDFTreeNode sNode)
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
				for (int i = 0; i < nNode.getChildCount(); i++)
				{
					final JDFTreeNode n = (JDFTreeNode) nNode.getChildAt(i);
					if (n.toString().equals(sNodeString))
					{
						return n;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Inserts new Resource JDFTreeNode into the m_jdfTree. If node had no ResourcePool - creates it.
	 * 
	 * @param parentNode - JDFNode to add resource to
	 * @param node - JDFTreeNode representation of parentNode
	 * @param selectedResource - name of resource to insert
	 * @param hasResourcePool - Has parentNode had a resourcePool before action started? Importent for representation of m_jdfTree. ResourcePool is automatically added to
	 *        parentNode but we need to insert it into m_model.
	 * @param usage - resource link usage. true - input, false - output
	 * @return JDFTreeNode created newResourceNode. null if operation was not completed successful
	 */
	public JDFTreeNode insertNewResourceNode(final JDFNode parentNode, final JDFTreeNode node, final String selectedResource, final boolean hasResourcePool, final EnumUsage usage)
	{
		JDFTreeNode newResourceNode = null;

		// if parentNode has no ResourcePool method addResource creates it.
		// Class=Unknown because we don't know now what class this res belongs to
		// but in a virtual method init() that is used in addResource
		// attribute Class with a correct value will be added automatically
		final JDFResource newResource = parentNode.addResource(selectedResource, null, usage, null, null, null, null);

		final JDFResourcePool rPoolElm = parentNode.getCreateResourcePool();
		final String xpath = rPoolElm.buildXPath(null, 1);
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
			final Enumeration<JDFTreeNode> e = node.breadthFirstJdfEnumeration();

			while (e.hasMoreElements() && !bFound)
			{
				final JDFTreeNode treeNode = e.nextElement();
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

		return newResourceNode;
	}

	/**
	 * Inserts new ResourceLink JDFTreeNode into the m_jdfTree. If node had no ResourceLinkPool - creates it.
	 * 
	 * @param parentNode - JDFNode to add resource link to
	 * @param node - JDFTreeNode representation of parentNode automatically added to parentNode but we need to insert it into m_model.
	 * @param resLink - ResourceLink to insert
	 * @returns created newLinkNode. null if operation was not completed successful
	 */
	public JDFTreeNode insertNewResourceLinkNode(final JDFNode parentNode, final JDFTreeNode node, final boolean hasLinkPool, final JDFResourceLink resLink)
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
			final String xpath = rLinkPoolElm.buildXPath(null, 1);
			final Enumeration<JDFTreeNode> e = node.breadthFirstJdfEnumeration();

			while (e.hasMoreElements() && !bFound)
			{
				final JDFTreeNode treeNode = e.nextElement();
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
	 * Method renameElementsAndAttributes. renames the selected node in the m_jdfTree and updates the jdfDoc
	 * 
	 * @param path
	 */
	public String renameElementsAndAttributes(final TreePath path)
	{
		final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
		final KElement originalElement = node.getElement();
		String selectedName = null;
		final JDFFrame m_frame = MainView.getFrame();
		if (node.isElement())
		{
			final KElement parentElement = originalElement.getParentNode_KElement();
			if (parentElement != null)
			{
				try
				{
					if (originalElement instanceof JDFResource && parentElement instanceof JDFResourcePool)
					{
						final JDFNode parentResourceNode = ((JDFResource) originalElement).getJDFRoot();
						selectedName = m_frame.getJDFTreeArea().chooseResourceName(parentResourceNode);
					}
					else
					{
						selectedName = EditorUtils.chooseElementName(parentElement);
					}
				}
				catch (final Exception e)
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

			selectedName = (String) JOptionPane.showInputDialog(m_frame, ResourceUtil.getMessage("ChooseNewAttTypeKey"), ResourceUtil.getMessage("RenameKey"),
					JOptionPane.PLAIN_MESSAGE, null, possibleValues, possibleValues[0]);

			if (selectedName != null && selectedName.equals("Other.."))
			{
				selectedName = JOptionPane.showInputDialog(m_frame, ResourceUtil.getMessage("InsertNewAttTypeKey"), ResourceUtil.getMessage("InsertNewAttTypeKey"),
						JOptionPane.PLAIN_MESSAGE);
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
	public JDFTreeNode renameAttribute(final JDFTreeNode node, final String selectedValue)
	{
		if (selectedValue == null || selectedValue.equals(JDFConstants.EMPTYSTRING))
		{
			return null;
		}
		final KElement originalElement = node.getElement();
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
		final TreePath[] paths = MainView.getEditorDoc().getSelectionPaths();
		if (paths != null)
		{
			MainView.getFrame().getEditorDoc().setDirtyFlag();
			final JDFFrame m_frame = MainView.getFrame();
			for (int i = paths.length - 1; i >= 0; i--)
			{
				final DeleteItemEdit edit = new DeleteItemEdit(paths[i]);
				m_frame.undoSupport.postEdit(edit);
			}
		}
	}

	/**
	 * Method deleteItem. deletes attributes or elements deletes the selected node in the m_jdfTree an removes it from the jdfDoc as well
	 * 
	 * @param treePath
	 */
	public boolean deleteItem(final TreePath treePath)
	{
		if (treePath == null)
		{
			return false;
		}
		final JDFTreeNode node = (JDFTreeNode) treePath.getLastPathComponent();
		return deleteNode(node, treePath);
	}

	/**
	 * Method deleteNode. deletes the selected node in the m_jdfTree an removes it from the jdfDoc as well, note that it must reside in a valid tree to correctly work
	 */
	public boolean deleteNode(final JDFTreeNode node, TreePath path)
	{
		if (node == null)
		{
			return false;
		}
		JDFTreeNode parentNode = (JDFTreeNode) node.getParent();
		if (parentNode == null && path != null)
		{
			path = path.getParentPath();
			if (path != null)
			{
				parentNode = (JDFTreeNode) path.getLastPathComponent();
			}
		}

		if (!node.isElement() && node.isInherited)
		{
			// cannot delete an inherited node!

			return false;
		}
		if (parentNode == null)
		{
			return false;
		}

		try
		{
			removeNodeFromParent(node);
		}
		catch (final IllegalArgumentException ex)
		{
			ex.printStackTrace();
			EditorUtils.errorBox("DeleteErrorKey", " Node=" + node + " ParentNode=" + parentNode + " ParentNode.getIndex(Node)=" + parentNode.getIndex(node));
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

		final JDFFrame m_frame = MainView.getFrame();
		final EditorDocument ed = m_frame.getEditorDoc();
		final JTree jtree = ed.getJDFTree();
		m_frame.m_topTabs.m_inOutScrollPane.clearInOutView();
		jtree.repaint();

		final TreePath treePath = new TreePath(parentNode.getPath());
		jtree.scrollPathToVisible(treePath);
		ed.setSelectionPath(treePath, true);
		return true;
	}

	private void autoValidate()
	{
		if (settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class))
		{
			validate();
		}
	}

	/**
	 * return the errortype associated with a given tree node
	 * 
	 * @param treeNode
	 * @return
	 */
	public String getErrorType(final JDFTreeNode treeNode)
	{
		if (validationResult == null)
		{
			return null;
		}

		if (treeNode instanceof DCOutputWrapper)
		{
			return null;
		}

		final String xPath = treeNode.getXPath();
		final JDFAttributeMap map = new JDFAttributeMap("XPath", xPath);
		map.put("ErrorType", (String) null);
		final KElement e = validationResult.getRoot().getChildByTagName(null, null, 0, map, false, true);

		return e == null ? null : e.getAttribute("ErrorType");
	}

	/**
	 *
	 * @param treeNode
	 * @return
	 */
	public boolean isValid(final JDFTreeNode treeNode)
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
		{
			return null;
		}
		return validationResult.getRoot();
	}

	/**
	 * Method insertAttributeIntoDoc. creates a new attribute and adds it to the jdfDoc
	 * 
	 * @param node
	 * @return
	 */
	public JDFTreeNode insertAttributeIntoDoc(JDFTreeNode node)
	{
		JDFTreeNode newAttrNode = null;
		if (!node.isElement())
		{
			node = (JDFTreeNode) node.getParent();
		}
		final JDFFrame m_frame = MainView.getFrame();

		final KElement element = node.getElement();
		final String[] possibleValues = EditorUtils.getAttributeOptions(element);
		final String attName = ResourceUtil.getMessage("ChooseNewAttTypeKey");
		String selectedValue = (String) JOptionPane.showInputDialog(m_frame, attName, attName, JOptionPane.PLAIN_MESSAGE, null, possibleValues, possibleValues[0]);

		if (selectedValue != null && selectedValue.equals("Other.."))
		{
			selectedValue = JOptionPane.showInputDialog(m_frame, attName, "");
		}
		if (selectedValue != null && !selectedValue.equals(JDFConstants.EMPTYSTRING))
		{
			final String ns = getNSURI(element, selectedValue);

			newAttrNode = setAttribute(node, selectedValue, getDefaultAttributeValue(element, selectedValue), ns, false);
		}

		return newAttrNode;
	}

	/**
	 * @param element
	 * @param selectedValue
	 * @return
	 */
	private String getDefaultAttributeValue(final KElement element, final String selectedValue)
	{
		String s = JDFElement.getValueForNewAttribute(element, selectedValue);
		if (AttributeName.SENDERID.equals(selectedValue))
		{
			s = "CIP4JDFEditor";
		}

		return s;
	}

	/**
	 * get the ns uri for a given element or attribute name
	 * 
	 * @param selectedValue
	 * @return
	 */
	private String getNSURI(final KElement parentElement, final String selectedValue)
	{
		String ns = KElement.xmlnsPrefix(selectedValue);
		if (ns != null)
		{
			ns = parentElement.getNamespaceURIFromPrefix(ns);
			if (ns == null)
			{
				ns = JOptionPane.showInputDialog(MainView.getFrame(), ResourceUtil.getMessage("ChoosePrefixKey"), "");
			}
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
	public void saveAsXJDF(final TreePath selectionPath, final boolean reallysave)
	{
		final JDFTreeNode node = selectionPath == null ? (JDFTreeNode) getRootNode() : (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
		{
			log.info("not converting to XJDF ");
			return;
		}
		final EditorDocument eDoc = MainView.getEditorDoc();
		final boolean xjdf = eDoc.isJSONEnabled();
		if (xjdf)
		{
			log.info("converting JSON to XJDF");
			eDoc.setJson(false, true);
		}
		else
		{
			saveJDFasXJDF(reallysave, node);
		}
	}

	void saveJDFasXJDF(final boolean reallysave, final JDFTreeNode node)
	{
		final EditorDocument eDoc = MainView.getEditorDoc();
		final KElement e = node.getElement();
		String ext = null;
		if (e instanceof JDFNode)
		{
			ext = XJDFConstants.XJDF.toLowerCase();
		}
		else if (e instanceof JDFJMF)
		{
			ext = XJDFConstants.XJMF.toLowerCase();
		}
		log.info("converting JDF/JMF to " + ext);
		String fn = eDoc.getOriginalFileName();
		String fnNew = UrlUtil.newExtension(fn, ext);
		final File fileToRead = new File(fnNew);
		if (!reallysave || eDoc.checkSave(fileToRead))
		{
			final KElement xJDF = extractXJDF(e, fn, ext);
			if (xJDF != null)
			{
				final XMLDoc d = xJDF.getOwnerDocument_KElement();
				if (reallysave)
				{
					log.info("writing XJDF " + fileToRead.getAbsolutePath());
					d.write2File(fileToRead, 2, false);
					MainView.getFrame().readFile(fileToRead);
				}
				else
				{
					final JDFDoc doc = new JDFDoc(d);
					fnNew = FilenameUtils.getName(fnNew);

					doc.setOriginalFileName(EditorUtils.getNewPath(fnNew));
					MainView.getFrame().setJDFDoc(doc, null);

				}
			}
			else
			{
				log.warn("problems converting JDF to " + ext);
			}
		}
		else
		{
			log.info("skipping conversion of " + fileToRead + " to " + ext + " reallysave=" + reallysave);
		}
	}

	protected KElement extractXJDF(final KElement e, final String fn, final String ext)
	{
		KElement xJDF = null;
		final XJDF20 xjdf20 = EditorUtils.getXJDFConverter();
		if (e instanceof JDFNode)
		{
			log.info("converting JDF node to " + ext);
			xJDF = convertJDF(e, fn, xjdf20);
		}
		else if (e instanceof JDFJMF)
		{
			log.info("converting JMF to " + ext);
			xJDF = xjdf20.makeNewJMF((JDFJMF) e);
		}
		return xJDF;
	}

	/**
	 * @param selectionPath
	 * @experimental
	 */
	public void saveAsJSON(final TreePath selectionPath)
	{
		final JDFTreeNode node = selectionPath == null ? (JDFTreeNode) getRootNode() : (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null || !EditorUtils.isJSONEnabled(node.getElement().getLocalName()))
		{
			return;
		}
		final EditorDocument eDoc = MainView.getEditorDoc();
		List<EditorDocument> eDocs = eDoc.splitJSON();
		if (eDocs != null)
		{
			final JDFFrame frame = MainView.getFrame();
			for (EditorDocument ed : eDocs)
			{
				ed.setJson(true, true);
				frame.setEditorDoc(ed);
			}
		}
		else
		{
			eDoc.setJson(true, true);
		}
		log.info("converting XJDF to JSON ");
	}

	KElement convertJDF(final KElement e, final String fn, final XJDF20 xjdf20)
	{
		KElement xJDF = null;
		final String procMethod = settingService.getString(SettingKey.XJDF_CONVERT_SINGLENODE);
		final JDFNode currentNode = (JDFNode) e;
		log.info("converting to XJDF - " + procMethod);

		if ("zip".equals(procMethod))
		{
			final String fnNew = UrlUtil.newExtension(fn, "xjdf.zip");
			xjdf20.saveZip(fnNew, currentNode, true);
		}
		else if (xjdf20.isSingleNode())
		{
			final XJDFHelper combined = xjdf20.getCombined(currentNode);
			if (combined != null)
			{
				xJDF = combined.getRoot();
			}
		}
		if (xJDF == null)
		{
			xJDF = xjdf20.makeNewJDF(currentNode, (VJDFAttributeMap) null);
		}
		return xJDF;
	}

	/**
	 * @param selectionPath
	 * @experimental
	 */
	public void saveAsCSV(final TreePath selectionPath)
	{
		final JDFTreeNode node;
		if (selectionPath == null)
			node = getRootNode();
		else
			node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
		{
			return;
		}
		final EditorDocument eDoc = MainView.getEditorDoc();
		String fn = eDoc.getOriginalFileName();
		fn = UrlUtil.newExtension(fn, "csv");

		XPathWalker walker;
		try
		{
			walker = new XPathWalker(new File(fn));
		}
		catch (final FileNotFoundException x)
		{
			return;
		}
		walker.setMethod(0);
		walker.setAttribute(true);
		walker.setAttributeValue(true);
		walker.setSeparator(",");
		walker.setDatatype(true);
		final KElement e = node.getElement();
		walker.walkAll(e);
	}

	/**
	 * @param selectionPath
	 * @experimental
	 */
	public void saveAsJDF(final TreePath selectionPath, final XJDFToJDFConverter c)
	{
		final JDFTreeNode node = selectionPath == null ? (JDFTreeNode) getRootNode() : (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null || node.getElement() instanceof JDFNode)
		{
			log.warn("not converting to JDF ");
			return;
		}
		final KElement e = node.getElement();
		final EditorDocument eDoc = MainView.getEditorDoc();
		final String fn = eDoc.getOriginalFileName();
		final String ext = XJDFConstants.XJMF.equalsIgnoreCase(e.getLocalName()) ? "jmf" : "jdf";
		final String fnNew = UrlUtil.newExtension(fn, ext);
		if (eDoc.checkSave(UrlUtil.urlToFile(fnNew)))
		{
			log.info("converting to JDF/JMF " + fnNew);
			final JDFDoc d = c.convert(e);
			if (d != null)
			{
				d.write2File(fnNew, 2, false);
				MainView.getFrame().readFile(new File(fnNew));
				log.info("converted to JDF " + fnNew);
			}
			else
			{
				EditorUtils.errorBox("FixVersionErrorKey", "could not convert xjdf to jdf");
			}
		}
	}

	/**
	 * @param selectionPath the selection path to convert
	 * @param structuredCaps if true, create a slightly more structured version of caps
	 * @experimental
	 */
	public void saveAsXJDFCaps(final TreePath selectionPath, final boolean structuredCaps)
	{
		final JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
		{
			return;
		}
		final KElement e = node.getElement();
		final EditorDocument eDoc = MainView.getEditorDoc();
		final String fn = eDoc.getOriginalFileName();
		final KElement caps = new Caps(e).createCaps(structuredCaps);
		final XMLDoc d = caps.getOwnerDocument_KElement();
		final String fnNew = UrlUtil.newExtension(fn, "cap");
		d.write2File(fnNew, 2, false);
		// VString badCaps=Caps.getBadAttributes(d, e);
		MainView.getFrame().readFile(new File(fnNew));
	}

	/**
	 * @param selectionPath
	 */
	public void createNodeFromCaps(final TreePath selectionPath)
	{
		final JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
		{
			return;
		}
		final JDFDeviceCap dc = (JDFDeviceCap) node.getElement();
		final EditorDocument eDoc = MainView.getEditorDoc();
		final String fn = eDoc.getOriginalFileName();

		final JDFDoc d = new JDFDoc("JDF");
		final JDFNode n = d.getJDFRoot();
		dc.setDefaultsFromCaps(n, true, settingService.getSetting(SettingKey.VALIDATION_GENERATE_FULL, Boolean.class));
		final String fnNew = UrlUtil.newExtension(fn, ".generated.JDF");
		d.write2File(fnNew, 2, false);
		MainView.getFrame().readFile(new File(fnNew));
	}

	/**
	 * @param selectionPath
	 */
	public void normalize(final TreePath selectionPath)
	{
		final JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
		{
			return;
		}
		final KElement e = node.getElement();
		if (e != null)
		{
			e.sortChildren();
		}
		MainView.getFrame().refreshView(null, selectionPath);

	}

	/**
	 * Methods moved from JDFFrame.java
	 */

	/**
	 * Finds a JDFTreeNode in the JDFTree.
	 * 
	 * @param row - The row in the JTree
	 * @param jTree - The JTree
	 * @return The JDFTreeNode. Moved 07-08-17
	 */
	public JDFTreeNode getTreeNode(final int row, final JTree jTree)
	{
		JDFTreeNode node;
		final TreePath path = jTree.getPathForRow(row);

		if (path != null)
		{
			node = (JDFTreeNode) path.getLastPathComponent();
		}
		else
		{
			node = getRootNode();
		}

		return node;
	}

	@Override
	public String toString()
	{
		return "JDFTreeModel [json=" + json + ", validationResult=" + validationResult + ", m_ignoreAttributes=" + m_ignoreAttributes + "]";
	}
}
