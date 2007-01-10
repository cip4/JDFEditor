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
/**
 *
 * Copyright (c) 2001 Heidelberger Druckmaschinen AG, All Rights Reserved.
 *
 * JDFDoc.java
 *
 * -------------------------------------------------------------------------------------------------
 */
package org.cip4.jdfeditor;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlUtil;

public class EditorDocument
{
    /**
     * this class collects all pointers to documents, trees, models etc and consolidates multi document functionality
     *
     */
    private JDFDoc m_jdfDoc=null;
    private JTree m_JDFTree=null;
    // The model of the JDF
    private JDFTreeModel m_model=null;
    protected EditorSelectionListener m_SelectionListener=null;
    protected Vector m_PathHistory=null; // history of selection path
    int m_HistoryPos=-1;
    private String m_MimePackage=null;
    private String m_cid=null;
    private double zoom = 1.0;
    private int topTab=0;
  
    
    
    /**
     * @return the zoom
     */
    public double getZoom()
    {
        return zoom;
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(double _zoom)
    {
        zoom = _zoom;
    }

    public EditorDocument(JDFDoc jdfDoc, String packageName)
    {
        m_jdfDoc=jdfDoc;
        m_PathHistory=new Vector();
        m_MimePackage=packageName;
        zoom=1.0;
    }
    
    public JDFDoc getJDFDoc()
    {
        return m_jdfDoc;
    }
    
    public String getMimePackage()
    {
        return m_MimePackage;
    }
    /**
     * check whether the vector contains a document corresponding to this document
     * @param doc
     * @param vjdfDocument
     * @return
     */
    public static int indexOfJDF(JDFDoc doc, Vector vjdfDocument)
    {
        if (vjdfDocument==null)
            return -1;
        for(int i=0;i<vjdfDocument.size();i++)
        {
            EditorDocument ed=(EditorDocument)vjdfDocument.elementAt(i);
            if(ed.getJDFDoc().equals(doc))
                return i;
        }
        return -1;
    }
    
    /////////////////////////////////////////////////////////////////
    /**
     * returns the index of the EditorDocument in vjdfDocument, -1 if not found
     * @param file the file that corresponds to the document
     * @param vjdfDocument the vector to search in
     * @return int the index of the document in vjdfDocument 
     */
    static public int indexOfFile(File file, Vector vjdfDocument)
    {
        
        if(file==null)
            return -1;
        String filePath=file.getAbsolutePath();    
        for(int i=0; i<vjdfDocument.size();i++)
        {
            EditorDocument d=(EditorDocument) vjdfDocument.elementAt(i);
            if(d!=null)
            {
                if(filePath.equals(d.getOriginalFileName()))
                    return i;
            }
        }
        return -1;
    }
    /////////////////////////////////////////////////////////////////
    
    public String toString()
    {
        if(m_jdfDoc==null)
            return "EditorDocument: #null";
        return "EditorDocument: "+m_jdfDoc.toString();
    }

////////////////////////////////////////////////////////////////////////////
    
    public String getOriginalFileName()
    {
        if(m_jdfDoc==null)
            return null;
        return m_jdfDoc.getOriginalFileName();
    }

    public void setModel(JDFTreeModel model)
    {
        m_model = model;
    }


    public JDFTreeModel getModel()
    {
        return m_model;
    }

    // sets the selection path for this document
    public void setSelectionPath(TreePath path, boolean trackHistory)
    {
        if(m_JDFTree==null)
            return;
        
        if(trackHistory==false)
            m_JDFTree.removeTreeSelectionListener(m_SelectionListener);
        
        if(path!=null)
        {
            m_JDFTree.setSelectionPath(path);
            m_JDFTree.scrollPathToVisible(path);
        }
        
        if(trackHistory==false)
            m_JDFTree.addTreeSelectionListener(m_SelectionListener);
    }

    public TreePath getSelectionPath()
    {
        if(getJDFTree()==null)
            return null;
        return getJDFTree().getSelectionPath();
    }
    
    public TreePath[] getSelectionPaths()
    {
        if(getJDFTree()==null)
            return null;
        return getJDFTree().getSelectionPaths();
    }

    public void setJDFTree(JTree jdfTree)
    {
        this.m_JDFTree = jdfTree;
        m_SelectionListener = new EditorSelectionListener();
        m_JDFTree.addTreeSelectionListener(m_SelectionListener);
    }

    public JTree getJDFTree()
    {
        return m_JDFTree;
    }
    
    public JDFTreeNode getRootNode()
    {
        JDFTreeModel mod = getModel();
        return mod==null ? null : mod.getRootNode();
    }

    public void setLastSelection()
    {
        JDFTreeNode selNode=null;
        if(m_HistoryPos==-1)
            m_HistoryPos=m_PathHistory.size()-1;
        if(m_HistoryPos>0)
        {
            m_HistoryPos--;
            selNode = (JDFTreeNode) m_PathHistory.elementAt(m_HistoryPos);
            setSelectionNode(selNode,false);
        }
        enableNextLast();
    }
    
    /**
     * @param selNode
     * @param b
     */
    private void setSelectionNode(JDFTreeNode selNode, boolean trackHistory)
    {
        TreePath path = getPathFromNode(selNode);
        if(path!=null)
            setSelectionPath(path, trackHistory);
    }

    /**
     * @param selNode
     * @return
     */
    private TreePath getPathFromNode(JDFTreeNode selNode)
    {
        final JDFTreeModel model = Editor.getModel();
        if(model==null)
            return null;
        final JDFTreeNode theRoot = (JDFTreeNode) model.getRootNode().getFirstChild();
        final Enumeration e = theRoot.depthFirstEnumeration();
        TreePath path=null;
        if(theRoot.equals(selNode))
        {
            path =new TreePath(selNode.getPath());
        }
        else
        {
            while (e.hasMoreElements())
            {
                final JDFTreeNode node = (JDFTreeNode) e.nextElement();
                if(node.equals(selNode))
                {
                    path =new TreePath(selNode.getPath());
                }
            }
        }
        return path;
    }

    public TreePath getLastSelection()
    {
        final JDFTreeNode node = getLastTreeNode();
        return getPathFromNode(node);
    }

    public JDFTreeNode getLastTreeNode()
    {
        JDFTreeNode selNode=null;
        if(m_HistoryPos==-1)
            m_HistoryPos=m_PathHistory.size()-1;
        if(m_HistoryPos>0)
        {
            selNode = (JDFTreeNode) m_PathHistory.elementAt(m_HistoryPos);
        }
        return selNode;
    }

/////////////////////////////////////////////////////////////////

    public void setNextSelection()
    {
        if(m_HistoryPos>=0)
        {
            m_HistoryPos++;
            if(m_HistoryPos<m_PathHistory.size())
            {
                setSelectionNode((JDFTreeNode) m_PathHistory.elementAt(m_HistoryPos),false);
            }
            else
            {
                m_HistoryPos=-1; // we are at the head
            }
        }
        enableNextLast();
    } 
/////////////////////////////////////////////////////////////////
 
    protected void enableNextLast()
    {
        final EditorButtonBar editorButtonBar = Editor.getFrame().m_buttonBar;
        editorButtonBar.m_LastButton.setEnabled(m_HistoryPos!=0  && m_PathHistory.size()>1);
        editorButtonBar.m_NextButton.setEnabled(m_HistoryPos!=m_PathHistory.size()-1 && m_PathHistory.size()>1);
    } 
    /**
     * Method createModel.
     * create the treeModel
     * @param doc
     * @return TreeModel
     */
    public JDFTreeModel createModel(JDFTreeNode root)
    {
        
        m_model=new JDFTreeModel(root,false);
        final JDFDoc doc=getJDFDoc();
        if(doc==null)
            return null;
        
        root.add(new JDFTreeNode(doc.getRoot()));
        m_model.buildModel((JDFTreeNode) root.getFirstChild());
        m_model.addTreeModelListener(new MyTreeModelListener());
        if(Editor.getIniFile().getAutoVal())
            m_model.validate();
        return m_model;
    }
    
    
/////////////////////////////////////////////////////////////////
    class EditorSelectionListener implements TreeSelectionListener 
    {
        public void valueChanged(TreeSelectionEvent e) 
        {
            final TreePath p = e.getPath();
            JDFTreeNode tn=(JDFTreeNode)p.getLastPathComponent();
            m_HistoryPos=-1;
            m_PathHistory.add(tn);
            if(m_PathHistory.size()>100)
                m_PathHistory.remove(0);
            enableNextLast();
            
        }
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    class MyTreeModelListener implements TreeModelListener
    {
        public void treeNodesChanged(TreeModelEvent event)
        {
            JDFTreeNode node = (JDFTreeNode) (event.getTreePath().getLastPathComponent());
            try
            {
                final int index = event.getChildIndices()[0];
                node = (JDFTreeNode) (node.getChildAt(index));
            }
            catch (NullPointerException e)
            {
                //
            }
        }
        public void treeNodesInserted(TreeModelEvent event)
        {
            event.getClass();
            //TODO implement
        }
        public void treeNodesRemoved(TreeModelEvent event)
        {
            event.getClass();
            //TODO implement
        }
        public void treeStructureChanged(TreeModelEvent event)
        {
            event.getClass();
            //TODO implement
        }
    }     
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @param file
     */
    public void saveFile(File file)
    {
        
        if(m_jdfDoc==null)
            return;
        
        if(file==null)
            file=new File(getSaveFileName());
        
        INIReader ini=Editor.getIniFile();
       
        KElement e=m_jdfDoc.getRoot();
        if(ini.getRemoveDefault())
            e.eraseDefaultAttributes(true);
        if(ini.getRemoveWhite())
            e.eraseEmptyNodes(true);
        String extension=UrlUtil.extension(file.getAbsolutePath().toLowerCase());
        
        if(!"mjm".equals(extension))
        {                
            m_jdfDoc.write2File(file.getAbsolutePath(), 2, false);
            m_jdfDoc.setOriginalFileName(file.getAbsolutePath());
        }
        else
        {
            Multipart mp=null;
            if(m_MimePackage==null)
            {
                mp=MimeUtil.buildMimePackage(null, getJDFDoc());
            }
            else
            {
                mp=MimeUtil.getMultiPart(m_MimePackage);
                BodyPart bp=MimeUtil.updateXMLMultipart(mp, m_jdfDoc, m_cid);
                if(bp==null)
                    mp=null; // flag that we shouldnt write
            }
            if(mp!=null)
            {
                MimeUtil.writeToFile(mp, file.getAbsolutePath());
                m_MimePackage=file.getAbsolutePath();
            }
        }
        m_jdfDoc.clearDirtyIDs();
    }

    /**
     * * get the name of the file that this document was originally loaded from.
     * the mime package name, if  ti was a mime package, otherwise the jdf file name
     * @return
     */
    public String getSaveFileName()
    {
        String fileName=getMimePackage();
        if(fileName==null)
            fileName=getOriginalFileName();
        return fileName;
    }

    /**
     * @param cid
     */
    public void setCID(String cid)
    {
        m_cid=cid;
        
    }

    /**
     * @return the topTab
     */
    public int getTopTab()
    {
        return topTab;
    }

    /**
     * @param topTab the topTab to set
     */
    public void setTopTab(int topTab)
    {
        this.topTab = topTab;
    }
   
}
