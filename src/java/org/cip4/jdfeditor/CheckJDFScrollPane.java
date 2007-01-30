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
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;

/**
 * this class encapsulates the output of the dev caps test
 * @author prosirai
 *
 */
public class CheckJDFScrollPane extends ValidationScrollPane
{
    private CheckJDFOutputWrapper checkJDFRoot = null;
    
    /**
     * 
     */
    private static final long serialVersionUID = 2367868076065696719L;
    
     
    public CheckJDFScrollPane(JDFFrame frame)
    {
        super(frame);
    }
    
    
    public void drawCheckJDFOutputTree (XMLDoc bugReport) 
    {
        if (bugReport==null)
            return;
        KElement repRoot=bugReport.getRoot().getChildByTagName("CheckJDFOutput",null,0,null,false,true);
        if(repRoot==null)
            return;
        checkJDFRoot =new CheckJDFOutputWrapper(repRoot);
        m_reportTree = new JTree(checkJDFRoot);
        m_reportTree.setModel(new JDFTreeModel(checkJDFRoot,false));
        m_reportTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_reportTree.setExpandsSelectedPaths(true);
        m_reportTree.setEditable(false);
        ToolTipManager.sharedInstance().registerComponent(m_reportTree);
        
        //DCOutputWrapper bugReportRoot = null;

        setCheckJDFOutputTree(checkJDFRoot);            
//        m_reportTree.expandPath(new TreePath(bugReportRoot.getPath()));
        m_reportTree.expandPath(new TreePath(checkJDFRoot.getPath()));

        m_SelectionListener = new ValidationSelectionListener();
        m_reportTree.addTreeSelectionListener(m_SelectionListener);
        
        final ValidationPopupListener popupListener = new ValidationPopupListener();
        m_reportTree.addMouseListener(popupListener);
        
        final CheckJDFOutputRenderer dcRenderer = new CheckJDFOutputRenderer();
        m_reportTree.setCellRenderer(dcRenderer);
        getViewport().setView(m_reportTree);
    }
    
    
    
    
    
    private void setCheckJDFOutputTree(CheckJDFOutputWrapper bugReport)
    {        
        KElement repElem=bugReport.getElement();
        // now add the individual attributes
        VString vAtts=repElem.getAttributeVector();
        for(int i=0;i<vAtts.size();i++){
            final String stringAt = vAtts.stringAt(i);
            if(stringAt.equals("ID"))
                continue;
            if(stringAt.equals("rRef"))
                continue;
            if(stringAt.equals("ErrorType"))
                continue;
            if(stringAt.equals("NodeName"))
                continue;
            if(stringAt.equals("Value"))
                continue;
            if(stringAt.equals("IsValid"))
                continue;
            if(stringAt.equals("Message"))
                continue;
            if(stringAt.equals("XPath"))
                continue;
            if(stringAt.endsWith("Elements"))
                continue;
            if(stringAt.endsWith("Attributes"))
                continue;
            if(stringAt.equals("Separation"))
                continue;
            CheckJDFOutputWrapper next = new CheckJDFOutputWrapper(repElem.getAttributeNode(stringAt));
            bugReport.add(next);
        }
        // recurse through children
        Vector childVector = repElem.getChildElementVector(null, null, null, true, 0,false);
        for (int i=0; i< childVector.size(); i++)
        {
            KElement kEl = (KElement) childVector.elementAt(i);
            CheckJDFOutputWrapper next = new CheckJDFOutputWrapper(kEl);
            setCheckJDFOutputTree(next);
            bugReport.add(next);
        }
    }    
    
}
