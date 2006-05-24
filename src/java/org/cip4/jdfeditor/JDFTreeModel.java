package org.cip4.jdfeditor;
/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2006 The International Cooperation for the Integration of 
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
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.CheckJDF;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.core.KElement.EnumValidationLevel;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;
import org.w3c.dom.Attr;


/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2006 The International Cooperation for the Integration of
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
 * DISCLAIMED.  IN NO EVENT SHALL THE INTERN
 }ATIONAL COOPERATION FOR
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
 }
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
/**
 * 
 * @author rainer prosi
 * This is a new dump for some of the JDFFrame classes
 * anything related to the abstract datamodel in the jdf tree belongs here
 * TODO move some of the routines from JDFTreeArea to here, where they belong and reduce the dependencies with JDFFrame
 */
public class JDFTreeModel extends DefaultTreeModel
{
    
    private JDFFrame m_frame; //TODO remove any references here
    private XMLDoc validationResult; // the checkJDF XML output result
    /**
     * 
     */
    private static final long serialVersionUID = -5922527273385407946L;
    private boolean m_ignoreAttributes = false;
    
    JDFTreeModel(JDFFrame frame,JDFTreeNode _root, boolean ignoreAttributes)
    {
        super(_root);
        m_ignoreAttributes=ignoreAttributes;
        m_frame=frame;
        validationResult=null;
        
    }
    
    public boolean validate()
    {
        Runtime.getRuntime().gc(); // clean up before validating
        final JDFDoc theDoc = m_frame.getJDFDoc();
        if(theDoc==null)
            return false;
        
        CheckJDF checkJDF=new CheckJDF();
        checkJDF.setPrint(false);
        checkJDF.bQuiet=true;
        checkJDF.level=EnumValidationLevel.Complete;
        XMLDoc schemaValidationResult=null;
        final INIReader iniFile = Editor.getIniFile();
        
        checkJDF.setIgnorePrivate(!iniFile.getHighlight());
        if(iniFile.getUseSchema())
        {
            final File f=iniFile.getSchemaURL();
            if(EditorUtils.fileOK(f))
            {
                checkJDF.setJDFSchemaLocation(f.getPath());
                
                try
                { 
                    File tmpFile = File.createTempFile("tmp",".jdf");
                    final String fn=theDoc.getOriginalFileName();
                    theDoc.write2File(tmpFile.getAbsolutePath(), 0, false);
                    theDoc.setOriginalFileName(fn);
                    JDFDoc tmpDoc=EditorUtils.parseFile(tmpFile);
                    tmpFile.delete();
                    if(tmpDoc!=null)
                        schemaValidationResult=tmpDoc.getValidationResult();
                }
                catch (Exception e)
                {
                    // nop
                }
                
            }else{
                iniFile.setSchemaURL(null);               
            }
        }
        validationResult=checkJDF.processSingleDocument(theDoc);
        
        m_frame.m_errorTabbedPane.m_validErrScroll.drawCheckJDFOutputTree(validationResult);
        m_frame.m_errorTabbedPane.m_SchemaErrScroll.drawSchemaOutputTree(schemaValidationResult);
        Editor.getEditorDoc().getJDFTree().repaint();
        m_frame.m_treeArea.goToPath(m_frame.m_treeArea.getSelectionPath());
        return validationResult.getRoot().getFirstChildElement().getBoolAttribute("IsValid",null,true);
    }
    /**
     * inserts newNode before the selected node in the m_jdfTree
     * @param node
     * @param newNode
     */
    public void insertBefore(final JDFTreeNode node, final JDFTreeNode newNode)
    {
        final JDFTreeNode parentNode = (JDFTreeNode) node.getParent();
        
        if (node.isElement())
        {
            insertNodeInto(newNode, parentNode, parentNode.getIndex(node));
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
        JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
        JDFTreeNode parentNode;
        
        if (!((JDFTreeNode) ((JDFTreeNode)root).getFirstChild()).equals(node))
            parentNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
        else
            parentNode = node;
        
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
        KElement element =my_Root.getElement();
        addNodeAttributes(my_Root);       
        KElement e=element.getFirstChildElement();        
        while(e!=null)
        {
            JDFTreeNode tn=new JDFTreeNode(e);
            my_Root.add(tn);
            buildModel(tn);
            e=e.getNextSiblingElement();
        }
    }
    
    ///////////////////////////////////////////////////////////////////
    /**
     * get the models root node as a JDFTreeNode
     * @return the root Node
     */
    JDFTreeNode getRootNode()
    {
        return (JDFTreeNode)getRoot();
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
        if (parentNode==null || !parentNode.isElement())
        {
            throw new JDFException("insertAttribute: not an element");
        }
        JDFTreeNode nOld=parentNode.getNodeWithName(attName);
        
        final KElement e=parentNode.getElement();
        // we have an existing node, simply rename tha value and ciao!
        if(nOld!=null)
        {
            final Attr oldattr = nOld.getAttr();
            if(oldattr.getOwnerElement()==e) // this is not an inherited tree node
            {
               setNodeValue(oldattr,attValue);
            }
            else // this is an inherited node; must create a new attribute in the DOM tree and link it
            {
                e.setAttribute(attName,attValue,attNS);
                Attr newAttr=e.getDOMAttr(attName,attNS,false);
                nOld.isInherited=false;
                nOld.setUserObject(newAttr);
                e.setDirty(true);
            }
            return nOld;
        }
        
        // create a new node whether the attribute exists in the dom or not
        int index=parentNode.getInsertIndexForName(attName,true); 
        Attr attr=e.getDOMAttr(attName,attNS,bInherit);
        // no attribute in DOM, create it
        if(attr==null)
        {
            e.setAttribute(attName,attValue,attNS);
            attr=e.getDOMAttr(attName,attNS,bInherit);
            String attNSReal=attr.getNamespaceURI();
            final String xmlnsPrefix = KElement.xmlnsPrefix(attName);
            if(attNSReal==null && xmlnsPrefix!=null)
            {
                e.addNameSpace(xmlnsPrefix,"http://www."+xmlnsPrefix+".com");
                Attr attrNS=e.getDOMAttr("xmlns:"+xmlnsPrefix,null,true);
                JDFTreeNode nodeNS = new JDFTreeNode(attrNS,attrNS.getParentNode()!=e);
                nodeNS.setAllowsChildren(false);
                insertInto(nodeNS, parentNode, index);           
            }
        }
        else if(attr.getOwnerElement()==e) // this is not an inherited tree node
        {
            setNodeValue(attr,attValue);            
        }
        // create the new tree node
        nOld = new JDFTreeNode(attr,bInherit);
        nOld.setAllowsChildren(false);
        insertInto(nOld, parentNode, index);
        return nOld;
    }

    /**
     * sets an attribute value if it differs
     * @param attValue
     * @param oldattr
     */
    private void setNodeValue(final Attr oldattr, String attValue)
    {
        if(!oldattr.getValue().equals(attValue))
        {
            oldattr.setNodeValue(attValue);
            ((KElement)oldattr.getOwnerElement()).setDirty(true);
        }
    }    
    
    /**
     * Method addRequiredAttributes.
     * adds the required attributes to an element
     * @param newNode
     */
    
    public Vector addRequiredAttributes(JDFTreeNode newNode)
    {
        final KElement kElement =  newNode.getElement();
        
        Vector addedAttributeNodesVector = new Vector();
        VString requiredAttributes = kElement.getMissingAttributes(9999999);
        
        for (int i = 0; i < requiredAttributes.size(); i++)
        {
            final String attValue=EditorUtils.setValueForNewAttribute(requiredAttributes.stringAt(i));
            final JDFTreeNode attrNode = setAttribute(newNode, requiredAttributes.stringAt(i), attValue, null, false);
            addedAttributeNodesVector.add(attrNode);
        }
        return addedAttributeNodesVector;
    }
    
    
    /**
     * @param newNode
     * @param node
     */
    public void insertAfter(final JDFTreeNode node, final JDFTreeNode newNode)
    {
        final JDFTreeNode parentNode = (JDFTreeNode) node.getParent();
        int position = parentNode.getIndex(node) + 1;
        if (position == 0)
        {
            position = -1;
        }
        insertInto(newNode, parentNode, position);
    }
    
///////////////////////////////////////////////////////
    
    public JDFTreeNode appendNode(final String newNodeName, final JDFTreeNode parentNode)
    {
        final KElement element = parentNode.getElement();
        JDFTreeNode newNode=null;
        if (newNodeName!=null && element != null)
        {
            KElement newElement = element.appendElement(newNodeName);
            newNode = createNewNode(newElement);
            insertInto(newNode,parentNode,-1);
        }
        return newNode;
    }
/////////////////////////////////////////////////////////////////
    
    public void insertInto(final JDFTreeNode newNode, final JDFTreeNode parentNode, int pos)
    {        
        if (parentNode.isElement())
        {
            int childCount = parentNode.getChildCount();
            if (pos == -1 || (pos>=0 && pos > childCount))
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
        
        for (int i = 0; i < requiredElements.size(); i++)
        {
            final String elName = requiredElements.stringAt(i);
            final KElement newElement =kElement.appendElement(elName);
            final JDFTreeNode newNode = createNewNode(newElement);
            insertInto(newNode,node,-1);
            addedElementNodesVector.add(newNode);
        }
        autoValidate();
        return addedElementNodesVector;
    }
    
    
    
    
    /**
     * Method insertElementAfter.
     * inserts a new element after this element in the jdfDoc 
     * @param path
     */
    public JDFTreeNode insertElementAfter(final TreePath path)
    {
        final JDFTreeNode sibling = (JDFTreeNode) path.getLastPathComponent();
        JDFTreeNode nextSibling = null;
        if (sibling != null)
        {
            nextSibling = (JDFTreeNode) sibling.getNextSibling();
        }
        final KElement parentElement = getParentElement(path);
        
        final String insertElementName = m_frame.m_treeArea.chooseElementName(parentElement);
        
        JDFTreeNode newNode = null;
        KElement newElement = null;
        if (insertElementName!=null && !insertElementName.equals(JDFConstants.EMPTYSTRING))                            
        {
            if (nextSibling != null)
            {
                final KElement nextSiblingElement = nextSibling.getElement();
                newElement = parentElement.insertBefore(insertElementName, nextSiblingElement, 
                        parentElement.getNamespaceURI());
            }
            else
            {
                newElement = parentElement.appendElement(insertElementName);
            }
            
            newNode = createNewNode(newElement);
            final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();          
            addRequiredAttributes(newNode);
            addRequiredElements(newNode);
            insertAfter(node, newNode);
            autoValidate();
        }
        return newNode;
    }
    /**
     * Method insertElementInto.
     * inserts a new element into the selected element in the jdfDoc
     * @param path
     * @param insertElementName
     */
    public JDFTreeNode insertElementInto(final JDFTreeNode node)
    {
        final KElement element = node.getElement();
        
        JDFTreeNode newNode = null;
        if (element != null)
        {
            final String insertElementName = m_frame.m_treeArea.chooseElementName(element);
            
            if ( insertElementName != null && 
                    !insertElementName.equals(JDFConstants.EMPTYSTRING))                            
            {
                newNode = appendNode(insertElementName,node);
                autoValidate();
            }
        }
        return newNode;
    }
    
    
    /**
     * Method createNewNode.
     * creates a new JDFTreeNode for the KElement withou putting it into the tree
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
        if(Editor.getIniFile().showAttr()==false)
            return;
        
        // get of 'elem' all not inherited attribute names
        VString vAttNames = elem.getAttributeVector_KElement();        
        for (int i = 0; i < vAttNames.size(); i++)
        {
            final String attName=vAttNames.stringAt(i);
            if(!m_ignoreAttributes ||
            attName.equals(AttributeName.TYPE) ||
            attName.equals(AttributeName.TYPES) ||
            attName.equals(AttributeName.DESCRIPTIVENAME) ||
            attName.equals(AttributeName.ID) )
            {
                String attVal=elem.getAttribute(attName);
                setAttribute(node,attName,attVal,null,false);
            }
        }
        // Show inherited attributes in the In&Output View and in the Tree View if that feature is selected
        if (!m_ignoreAttributes && (elem instanceof JDFResource && m_frame.m_menuBar.m_showInhAttrRadioItem.isSelected()))
        {
            JDFResource res = (JDFResource) elem;
            
            // Create a vector with all the attribute names including the inherited attributes
            VString vInheritedAttNames = res.getAttributeVector_JDFResource();
            
            for (int i = 0; i < vInheritedAttNames.size(); i++)
            {
                String attNameStr = vInheritedAttNames.stringAt(i);
                
                // Add the attribute to the TreeNode, but only if it is an inherited attribute
                if (!vAttNames.contains(attNameStr))
                {
                    final String attName=vInheritedAttNames.stringAt(i);
                    String attVal=elem.getAttribute(attName);
                    setAttribute(node,attName,attVal,null,true);
                }
            }
        }
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
            if ( attValue.equals(elem.getAttribute(attName, null, null)))
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
    public JDFTreeNode insertNewResourceNode(final JDFNode parentNode, final JDFTreeNode node, 
            final String selectedResource, boolean hasResourcePool,
            boolean withLink, boolean input)
    {
        JDFTreeNode newResourceNode = null;
        
        // if parentNode has no ResourcePool method addResource creates it.
        // Class=Unknown because we don't know now what class this res belongs to
        // but in a virtual method init() that is used in addResource 
        // attribute Class with a correct value will be added automatically
        JDFResource newResource = parentNode.addResource(selectedResource, null, 
                input, null, withLink, null);
        
        if (newResource != null)
        {
            final JDFResourcePool rPoolElm = parentNode.getResourcePool();
            String xpath = rPoolElm.buildXPath();
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
                JOptionPane.showMessageDialog(m_frame, "Error occured.\nInsert ResourceLink operation was not completed",
                        "Insert ResourceLink", JOptionPane.ERROR_MESSAGE);
            }
        }
        else 
        {
            JOptionPane.showMessageDialog(m_frame, "Error occured.\nInsert Resource operation was not completed",
                    "Insert Resource", JOptionPane.ERROR_MESSAGE);
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
    public JDFTreeNode insertNewResourceLinkNode(final JDFNode parentNode, final JDFTreeNode node,
           final JDFResourceLink resLink)
    {
        JDFTreeNode newLinkNode = null;
        final JDFResourceLinkPool rLinkPoolElm = parentNode.getResourceLinkPool();
        
        String xpath = rLinkPoolElm.buildXPath();
        JDFTreeNode rLinkPool = null;
        boolean bFound = false;
        if (rLinkPoolElm==null)
        {
            rLinkPool = appendNode(ElementName.RESOURCELINKPOOL, node);
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
            JOptionPane.showMessageDialog(m_frame, "Error occured.\nInsert ResourceLink operation was not completed",
                    "Insert ResourceLink", JOptionPane.ERROR_MESSAGE);
        }
        return newLinkNode;
    }
    
    /**
     * Method renameElementsAndAttributes.
     * renames the selected node in the m_jdfTree and updates the jdfDoc
     * @param path
     */
    public JDFTreeNode renameElementsAndAttributes(final TreePath path)
    {
        JDFTreeNode newTreeNode = null;
        final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
        final KElement originalElement = node.getElement();
        
        if (node.isElement())
        {         
            final KElement parentElement = originalElement.getParentNode_KElement();
            if ( originalElement!= null && parentElement != null)
            {
                try
                {
                    if (originalElement instanceof JDFResource && 
                            parentElement instanceof JDFResourcePool )
                    {
                        final JDFNode parentResourceNode = ((JDFNode)originalElement).getJDFRoot();
                        final String selectedResourceName = m_frame.m_treeArea.chooseResourceName(parentResourceNode);
                        if (selectedResourceName != null && !selectedResourceName.equals(JDFConstants.EMPTYSTRING))
                        {
                            originalElement.renameElement(selectedResourceName, null);
                        }
                    }
                    else
                    {
                        final String selectedElementName = m_frame.m_treeArea.chooseElementName(parentElement);
                        if (selectedElementName!=null && !selectedElementName.equals(JDFConstants.EMPTYSTRING))                            
                        {
                            originalElement.renameElement(selectedElementName,null);
                        }
                    }
                }
                catch (Exception e)
                {
                    return null;
                }
                newTreeNode=node;
            }            
        }
        else // attribute case
        {
            final KElement parent = node.getElement();
            final String[] possibleValues = EditorUtils.getAttributeOptions(parent);
            
            String selectedValue = (String) JOptionPane.showInputDialog(
                    m_frame, m_frame.m_littleBundle.getString("ChooseNewAttTypeKey"),
                    m_frame.m_littleBundle.getString("RenameKey"), JOptionPane.PLAIN_MESSAGE, 
                    null, possibleValues, possibleValues[0]);
            
            if (selectedValue != null && selectedValue.equals("Other.."))
            {
                selectedValue = JOptionPane.showInputDialog(m_frame,
                        m_frame.m_littleBundle.getString("InsertNewAttTypeKey"), 
                        m_frame.m_littleBundle.getString("InsertNewAttTypeKey"),
                        JOptionPane.PLAIN_MESSAGE);
            }
            newTreeNode = renameAttribute(node, selectedValue);
        }
       autoValidate();
        
        return newTreeNode;
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
        KElement originalElement=node.getElement();
        final String oldAttribute = node.getName();
        final String attrValue = originalElement.getAttribute(oldAttribute);
        final JDFTreeNode parentNode=(JDFTreeNode) node.getParent();
        removeNodeFromParent(node);
        originalElement.removeAttribute(oldAttribute, null);
        return  setAttribute(parentNode,selectedValue,attrValue,null,false);
    }
    
    /**
     * Method deleteItem. deletes attributes or elements
     * deletes the selected node in the m_jdfTree an removes it from the
     * jdfDoc as well
     * @param treePath
     */
    public boolean deleteItem(final TreePath treePath)
    {
        if(treePath==null)
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
        if(node==null)
            return false;
        JDFTreeNode parentNode = (JDFTreeNode) node.getParent();
        if(parentNode==null && path!=null)
        {
            path=path.getParentPath();
            if(path!=null)
                parentNode = (JDFTreeNode)path.getLastPathComponent();
        }  
        
        if(!node.isElement()&&node.isInherited)
        {
            // cannot delete an inherited node!
            
            return false;
        }
        if(parentNode==null)
            return false;
        
        try 
        {
            removeNodeFromParent(node);
        }
        catch (IllegalArgumentException ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(m_frame, 
                    m_frame.m_littleBundle.getString("DeleteErrorKey")+" Node=" + node + " ParentNode=" + parentNode + 
                    " ParentNode.getIndex(Node)=" + parentNode.getIndex(node), m_frame.m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
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
        
        EditorDocument ed=m_frame.getEditorDoc();
        JTree jtree=ed.getJDFTree();
        m_frame.m_topTabs.m_inOutScrollPane.clearInOutView();
        jtree.repaint();

        final TreePath treePath = new TreePath(parentNode.getPath());
        jtree.scrollPathToVisible(treePath);
        ed.setSelectionPath(treePath,true);        
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
     * Method getParentsToRoot.
     * creates a vector with all the nodes parents, 
     * the m_root will be the first element in the vector.
     * @param node
     * @return Vector
     */
    public Vector getParentsToRoot(JDFTreeNode node)
    {
        final Vector parentVector = new Vector();
        if (node.equals(getRootNode().getFirstChild()))
            parentVector.add(0, node);
        
        while (!node.equals(getRootNode()))
        {
            parentVector.add(0, node);
            node = (JDFTreeNode) node.getParent();
            
            if (node == null)
                break;
        }
        return parentVector;
    }
    
    public boolean isValid(JDFTreeNode treeNode)
    {
        if(validationResult==null)
            return true;
        
        if(treeNode instanceof DCOutputWrapper)
            return true;
        
        String xPath=treeNode.getXPath();
        JDFAttributeMap map=new JDFAttributeMap("XPath",xPath);
        map.put("ErrorType",null);
        KElement e=validationResult.getRoot().getChildByTagName(null,null,0,map,false,true);
        
        return e==null;        
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
        if(validationResult==null)
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
            node = (JDFTreeNode)node.getParent();            
        }
        final KElement element = node.getElement();
        final String[] possibleValues = EditorUtils.getAttributeOptions(element);
        String selectedValue = (String) JOptionPane.showInputDialog( m_frame,  
                m_frame.m_littleBundle.getString("ChooseNewAttTypeKey"),
                m_frame.m_littleBundle.getString("ChooseNewAttTypeKey"), 
                JOptionPane.PLAIN_MESSAGE, null, possibleValues, possibleValues[0]);
        
        if (selectedValue != null && selectedValue.equals("Other.."))
        {
            selectedValue = JOptionPane.showInputDialog(m_frame, "Choose attribute name", "");
        }
        if (selectedValue != null && !selectedValue.equals(JDFConstants.EMPTYSTRING))
        {
            newAttrNode =setAttribute(node,selectedValue,EditorUtils.setValueForNewAttribute(selectedValue),null,false);
        }
        
        return newAttrNode;
    }
    
}
