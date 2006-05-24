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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.tree.TreePath;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFAuditPool;
import org.cip4.jdflib.resource.JDFModified;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.XMLstrm;

/**
 * @author AnderssA ThunellE SvenoniusI Elena Skobchenko
 * This is the junk dump all in class with gazillions of private members
 * TODO clean up and move routines to the model
 */

public class JDFFrame extends JFrame implements ActionListener,
DropTargetListener,
DragSourceListener,
DragGestureListener,
ClipboardOwner
{
    /**
     * Comment for <code>serialVersionU
     * ID</code>
     */
    private static final long serialVersionUID = 626128726824503039L;    
    
    // TODO remove messy globals   
//    boolean m_callFromSearch = false;
    
    public JDFTreeArea m_treeArea; 
    
    EditorTabbedPaneA m_topTabs;   
    EditorTabbedPaneB m_errorTabbedPane;
    
    JTree m_searchTree;
    
    // handles all copying and pasting
    public JDFTreeCopyNode m_copyNode;
    
    SearchDialog m_dialog = null;
    private HelpFrame m_helpFrame = null;
    
    // all settings are stored here
    public ResourceBundle m_littleBundle;
    
    
    // q&d hack for multi doc support
    Vector m_VjdfDocument=new Vector();
    int m_DocPos=-1; // document position
    
    // the cursors in the frame
    static protected Cursor m_readyCursor = Cursor.getDefaultCursor();
    static protected Cursor m_waitCursor  = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    // dragndrop support
    private DragSource m_source;
    
    // munus and buttons
    public EditorMenuBar m_menuBar = null;
    public EditorButtonBar m_buttonBar = null;
    
    // undo and redo support
    final MyUndoManager undomanager = new MyUndoManager();
    final UndoableEditSupport undoSupport = new UndoableEditSupport();
    public UndoAction undoAction = new UndoAction();
    public RedoAction redoAction = new RedoAction();
    
    /**
     * constructor of the frame
     *
     */
    public JDFFrame() 
    {
        super("CIP4 JDF Editor");
        
        m_source = DragSource.getDefaultDragSource();
        m_source.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, d.width, d.height - 30);
        this.setCursor(m_readyCursor);
        
        INIReader iniFile=Editor.getIniFile();
        String language = iniFile.getLanguage();
        String currentLookAndFeel = iniFile.getLookAndFeel();

        final Locale currentLocale = new Locale(language, language.toUpperCase());

        
        m_littleBundle = ResourceBundle.getBundle("org.cip4.jdfeditor.messages.JDFEditor", currentLocale);
        Locale.setDefault(currentLocale);
        
        
        m_menuBar = new EditorMenuBar(m_littleBundle,this);        
        try
        {
            UIManager.setLookAndFeel(currentLookAndFeel);
        }
        catch (Exception e)
        { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, m_littleBundle.getString("LookAndFeelErrorKey"), 
                    m_littleBundle.getString("ErrorMessKey"),
                    JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    /**
     * Method drawWindow.
     */
    public void drawWindow()
    {
        this.setCursor(m_waitCursor);
        this.setJMenuBar(m_menuBar.drawMenu());
        this.getContentPane().add(drawBoxContent());
        this.setEnableClose(false);
        this.setVisible(true);
        this.setCursor(m_readyCursor);
    }
    /**
     * Method drawBoxContent.
     * @return Box
     */
    private Box drawBoxContent()
    {
        final Box box_splitPane = Box.createHorizontalBox();
        box_splitPane.add(drawSplitPane());
        
        final Box box_content = Box.createVerticalBox();            
        m_buttonBar = new EditorButtonBar(m_littleBundle,this);
        m_buttonBar.drawButtonBar();
        box_content.add(m_buttonBar);
        box_content.add(box_splitPane);
        
        return box_content;
    }
    /**
     * Draws the splitpanes in which the views are to be displayed
     * @return JSplitPane
     */
    private JSplitPane drawSplitPane()
    {
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        m_topTabs = new EditorTabbedPaneA(this);        
        m_errorTabbedPane = new EditorTabbedPaneB(this);
        
        m_treeArea = new JDFTreeArea(m_littleBundle,this);
        new DropTarget(m_treeArea, this);
        m_treeArea.setToolTipText(m_littleBundle.getString("TreeViewKey"));
        
        undomanager.setLimit( 100 );
        m_treeArea.getDocument().addUndoableEditListener(undomanager);
        undoSupport.addUndoableEditListener(undomanager);
        
        final JSplitPane attAndErrorPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_topTabs, m_errorTabbedPane);
        attAndErrorPane.setDividerLocation(d.height / 2);
        attAndErrorPane.setResizeWeight(1);
        
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_treeArea.getScrollPane(), attAndErrorPane);
        splitPane.setDividerLocation(d.width / 4);
        
        return splitPane;
    }
    
    
    /**
     * Method createModel.
     * create the treeModel
     * @param doc
     * @return TreeModel
     */
    public JDFTreeModel createModel(JDFDoc doc, JDFTreeNode root)
    {
        setJDFDoc(doc);
        
        setModel(new JDFTreeModel(this, root,false));
        root.add(new JDFTreeNode(doc.getRoot()));
        if (getJDFDoc() != null)
        {
            getModel().buildModel((JDFTreeNode) root.getFirstChild());
        }
        getModel().addTreeModelListener(new MyTreeModelListener());
        if(Editor.getIniFile().getAutoVal())
            getModel().validate();
        return getModel();
    }
    
    /**
     * Finds a JDFTreeNode in the JDFTree.
     * @param row     - The row in the JTree
     * @param jTree - The JTree
     * @return The JDFTreeNode.
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
    
    
    /**
     * Choose which file to open.
     */
    void openFile()
    {
        File fileToSave=null;
        if(getJDFDoc()!=null)
        {
            fileToSave=new File(getJDFDoc().getOriginalFileName());
        }
        else
        {
            final String recentFile=Editor.getIniFile().getRecentFiles()[0];
            if(recentFile!=null)
                fileToSave=new File(recentFile);
        }
        
        final EditorFileChooser chooser = new EditorFileChooser(fileToSave,"xml jdf jmf",m_littleBundle);
        final int answer = chooser.showOpenDialog(this);
        
        if (answer == JFileChooser.APPROVE_OPTION)
        {
            fileToSave = chooser.getSelectedFile();           
            readFile(fileToSave);
        }
    }
    
    
    /**
     * Called if a file is opened from the recent files menu.
     * @param filePathToSave - Path to the file that is to be opened
     */
    private void openRecentFile(String filePathToSave)
    {
        getContentPane().setCursor(m_waitCursor);
        File fileToSave = new File(filePathToSave);
        EditorDocument d=readFile(fileToSave);
        
        if (d!=null)
        {
            final String s = fileToSave.getAbsolutePath();
            
            final INIReader m_iniFile = Editor.getIniFile();
            if (m_iniFile.nrOfRecentFiles() != 1)
                m_iniFile.updateOrder(s, true);
        }
        getContentPane().setCursor(m_readyCursor);
    }    
    
    /**
     * Reload the currently opened file.
     */
    public void refresh()
    {
        if (getJDFDoc() != null)
        {
            final String originalFileName = getJDFDoc().getOriginalFileName();
            setJDFDoc(null);
            readFile(new File(originalFileName));
        }
    }

    public void applyLookAndFeel(PreferenceDialog pd)
    {
        try
        {
            final INIReader m_iniFile = Editor.getIniFile();
            UIManager.setLookAndFeel(m_iniFile.getLookAndFeel());
            m_buttonBar.removeAll();
            m_buttonBar.drawButtonBar();
            SwingUtilities.updateComponentTreeUI(JDFFrame.this);
            EditorDocument ed=getEditorDoc();
            if (ed != null && ed.getJDFTree() != null)
                ed.getJDFTree().setRowHeight(18);
            
            if (pd != null)
            {
                SwingUtilities.updateComponentTreeUI(pd);
            }
        }
        catch (ClassNotFoundException e)
        { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
        catch (InstantiationException e)
        { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalAccessException e)
        { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
        catch (UnsupportedLookAndFeelException e)
        { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("LookAndFeelErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void printWhat()
    {
        final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };
        
        final ComponentChooser cc = new ComponentChooser(m_littleBundle);
        
        final int option = JOptionPane.showOptionDialog(this, cc, m_littleBundle.getString("PrintMessKey"),
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        EditorDocument ed=getEditorDoc();
        if (option == JOptionPane.OK_OPTION)
        {
            Component comp = null;
            
            if (cc.getComponent().equals(m_littleBundle.getString("ProcessViewKey")))    
                comp = m_topTabs.m_pArea;
            
            else if (cc.getComponent().equals(m_littleBundle.getString("NextNeighbourKey")))
                comp = m_topTabs.m_inOutScrollPane.m_inOutArea;
            
            else if (cc.getComponent().equals(m_littleBundle.getString("TreeViewKey")))
                comp = ed.getJDFTree();
            
            PrintDialog.printIt(comp);
        }
    }
    
    /**
     * Spawn informative
     * TODO correctly dump into multiple file
     */
    void spawn(boolean bSpawnInformative)
    {
        EditorDocument ed=getEditorDoc();
        if(ed==null)
            return;
        try 
        {
            final JDFTreeNode node = (JDFTreeNode) ed.getSelectionPath().getLastPathComponent();
            final JDFNode selectedNode = (JDFNode) node.getElement();
            final SpawnDialog spawnDialog = new SpawnDialog(this, m_littleBundle, selectedNode, bSpawnInformative);
            
            if (spawnDialog.bOK)
            {
                clearViews();
                
                if(!bSpawnInformative)
                    readFile(spawnDialog.newRootFile);
      
                readFile(spawnDialog.newPartFile);
            }                    
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("SpawnErrorKey") + e.getClass() + " \n"
                    + (e.getMessage()!=null ? ("\"" + e.getMessage() + "\"") : ""), 
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Merge
     */
    void merge()
    {
        try 
        {
            final MergeDialog mergeResult = 
                new MergeDialog(this, m_littleBundle, getJDFDoc().getJDFRoot());
            
            File f = mergeResult.getFileToSave(); 
            if (f != null)
            {
                refresh();
                clearViews();
            }   
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("MergeErrorKey") + e.getClass() + " \n"
                    + (e.getMessage()!=null ? ("\"" + e.getMessage() + "\"") : ""), 
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Export to Device Capabilities File
     */
    void exportToDevCap()
    {
        try 
        {
            final ExportDialog exportDialog = 
                new ExportDialog(this, m_littleBundle, getJDFDoc().getJDFRoot());
            
            File fileToOpen = exportDialog.getFileToOpen(); 
            if (fileToOpen != null) 
            {
                VString vs=StringUtil.tokenize(exportDialog.generAttrString," ",false);
                vs.unify();
                final INIReader m_iniFile = Editor.getIniFile();
                m_iniFile.setGenericAtts(vs);
                clearViews();                
                readFile(fileToOpen);
            }                     
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("DevcapExportErrorKey") + e.getClass() + " \n"
                    + (e.getMessage()!=null ? ("\"" + e.getMessage() + "\"") : ""), 
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openDeviceCapFile()
    {
        try 
        {
            final KElement nodeRoot = getJDFDoc().getRoot();
            if (nodeRoot !=null )
            {
                final String nodeName = nodeRoot.getLocalName();
                if (ElementName.JMF.equals(nodeName))
                { 
                    JOptionPane.showMessageDialog(this, 
                            "Testing of JMF against Device Capabilities is not implemented yet", 
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else 
                {
                    final DeviceCapDialog testResult = 
                        new DeviceCapDialog(this, m_littleBundle, (JDFNode) nodeRoot);
                    final XMLDoc bugReport = testResult.getBugReport();
                    final VElement executNodes = testResult.getExecutable();
                    
                    m_errorTabbedPane.m_devCapErrScroll.drawDevCapOutputTree(bugReport, executNodes);
                    
                    m_errorTabbedPane.setSelectedIndex(m_errorTabbedPane.m_DC_ERRORS_INDEX);
                }
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("DevcapOpenErrorKey") + e.getClass() + " \n"
                    +(e.getMessage()!=null ? ("\"" + e.getMessage() + "\""): ""), 
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showPreferences()
    {
        final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };
        
        final INIReader m_iniFile = Editor.getIniFile();
        PreferenceDialog pd = new PreferenceDialog(m_littleBundle);
        
        final int option = JOptionPane.showOptionDialog(this, pd, m_littleBundle.getString("PreferenceKey"),
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (option == JOptionPane.OK_OPTION)
        {
            pd.writeToIni();
            m_iniFile.writeINI(m_menuBar);
            EditorDocument ed=getEditorDoc();
            if (ed != null &&  ed.getJDFTree()!=null){
                ed.getJDFTree().repaint();
            }
        }
    }
    
    //20040712 MRE
    private void sendToDevice()
    {
        if(getJDFDoc()==null)
            return;
        
        //get the URL to send to and call the CommunicationController
        boolean bSendTrue = false;
        final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };
        final SendToDevice cc = new SendToDevice();
        
        final int option = JOptionPane.showOptionDialog(this, cc, m_littleBundle.getString("JDFSendToDevice"),
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (option == JOptionPane.OK_OPTION)
        {
            //the send method depends on the settings in the Editor.ini file
            if(cc.getActiveRadioButton().equals(m_littleBundle.getString("sendMethodJMF")))
                bSendTrue = cc.sendJMFJDF(cc.getURL());            
            else
                bSendTrue = cc.sendJMFJDFmime(cc.getURL()); 
        }
        
        //show success in a popup window
        final String sLabel = (bSendTrue) ?m_littleBundle.getString("JDFSent") : m_littleBundle.getString("JDFNotSent"); 
        JOptionPane.showMessageDialog(this, sLabel);
    }
    
    /**
     * Method readFile.
     * @param m_fileToSave
     */
    public EditorDocument readFile(File fts)
    {
        EditorDocument eDoc=null;
        JDFDoc jdfDoc=null;
        Runtime.getRuntime().gc(); // clean up before loading
        
        if (fts != null)
        {
            int docIndex=EditorDocument.indexOfFile(fts,m_VjdfDocument);
            if(docIndex>=0)
            {
                eDoc=nextFile(docIndex);                
            }
            else if(fts.exists())
            {
                try
                {
                    jdfDoc=EditorUtils.parseFile(fts);
                    if (jdfDoc!=null)
                    {
                        m_menuBar.updateRecentFilesMenu(fts.getAbsolutePath());
                        setJDFDoc(jdfDoc);
                        eDoc=getEditorDoc();
                    }
                    else
                    {
                        EditorUtils.errorBox("FileNotOpenKey",": "+fts.getName()+"!"); 
                    }
                    // refresh the v iew to the selected document
                    refreshView(eDoc,null);
                }
                catch (Exception e)
                {                    
                    e.printStackTrace();
                    EditorUtils.errorBox("FileNotOpenKey",": "+fts.getName()+"!\n"+e.getMessage()); 
                }
            }
            else
            {
                EditorUtils.errorBox("FileNotOpenKey",": "+fts.getName()+"!"); 
            }
        }
        return eDoc;
    }

    /////////////////////////////////////////////////////////////
    
    private void refreshView(EditorDocument eDoc, TreePath path)
    {
        if(eDoc==null)
            return;
        if(path==null)
            path=eDoc.getSelectionPath();
        
        JDFDoc jdfDoc=eDoc.getJDFDoc();
        try
        {            
            final INIReader m_iniFile = Editor.getIniFile();
            setEnableOpen(!m_iniFile.getReadOnly());
            
            this.setCursor(m_waitCursor);
            m_treeArea.drawTreeView(eDoc);
            m_topTabs.refreshView(jdfDoc);
            m_copyNode = null;
            this.setTitle(getJDFDoc().getOriginalFileName());
            
            m_errorTabbedPane.refreshView(path);
            eDoc.setSelectionPath(new TreePath(((JDFTreeNode) getRootNode().getFirstChild()).getPath()),false);
            if(path!=null){
                m_treeArea.goToPath(path);
                updateViews(path);
            }
            this.setCursor(m_readyCursor);
            this.toFront();
        }
        catch (Exception e)
        {
            setJDFDoc(null);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"), 
                    m_littleBundle.getString("ErrorMessKey"),
                    JOptionPane.ERROR_MESSAGE);
            this.setCursor(m_readyCursor);
        }
    }
    
    /**
     * Method saveFile.
     * Save As-chooser
     */
    public void saveFile()
    {
        final JDFDoc jdfDoc = getJDFDoc();
        if(jdfDoc==null)
            return;
        String fileName=jdfDoc.getOriginalFileName();
        File fileToSave=new File(fileName);
        final EditorFileChooser saveChooser = new EditorFileChooser(fileToSave,"xml jmf jdf",m_littleBundle);
        final int answer = saveChooser.showSaveDialog(null);
        
        if (answer == JFileChooser.APPROVE_OPTION)
        {
            File file = saveChooser.getSelectedFile();
            int newAnswer=JOptionPane.YES_OPTION;
            
            if (file.exists() && !file.equals(fileToSave)){
                final String[] options = 
                { m_littleBundle.getString("YesKey"), m_littleBundle.getString("NoKey"), m_littleBundle.getString("CancelKey")};
                newAnswer = JOptionPane.showOptionDialog(this, m_littleBundle.getString("FileExistsKey"), null,
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            }
            if(newAnswer==JOptionPane.YES_OPTION)
            {
                jdfDoc.write2File(file.getAbsolutePath(), 2, false);
                jdfDoc.setOriginalFileName(file.getAbsolutePath());
                jdfDoc.clearDirtyIDs();
                this.setTitle(file.getName());
                m_menuBar.updateRecentFilesMenu(fileToSave.toString());
            }
        }
    }
     
    /**
     * Get the node to search for in the Process View.
     * @param source - The location in the Process View that's been selected
     * @return the JDFTreeNode that is to be searched for.
     */
    void getProcessSearchNode(Object src)
    {
        final ProcessPart pp = (ProcessPart) src;
        JDFTreeNode node = null;
        try
        {
            final KElement kElement = pp.getElem();
            node = new JDFTreeNode(kElement);
        }
        catch (Exception s)
        {
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("FindErrorKey"), m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
            s.printStackTrace();
        }
        if (node != null)
            m_treeArea.findNode(node);
    }
    
    
    
    
    /**
     * Method clearViews.
     * clear all views before opening a new file
     */
    private void clearViews()
    {
        if(m_dialog!=null)
        {
            m_dialog.dispose();
            m_dialog=null;
        }
        m_topTabs.clearViews();
        m_errorTabbedPane.clearViews();
    }
    /**
     * Method saveFileQuestion.
     * ask if the user wants to save an unsaved file before closing
     * @return int
     */
    public int saveFileQuestion()
    {
        int save = JOptionPane.YES_OPTION;
        if (getJDFDoc() != null)
        {
            final INIReader m_iniFile = Editor.getIniFile();
            if (!m_iniFile.getReadOnly())
            {
                String originalFileName = getJDFDoc().getOriginalFileName();
                if(originalFileName==null)
                    originalFileName="Untitled";
                if (fileIsEdited())
                {
                    if (originalFileName != null)
                    {
                        final String question = m_littleBundle.getString("SaveQuestionKey")
                        + "\n" + '"' + originalFileName + '"';
                        save = JOptionPane.showConfirmDialog(this, question, "",
                                JOptionPane.YES_NO_CANCEL_OPTION);
                        
                        if (save == JOptionPane.YES_OPTION)
                        {
                            if (originalFileName.startsWith("Untitled"))
                                saveFile();
                            
                            else
                            {
                                XMLstrm.xmlIndent(2, true);
                                getJDFDoc().write2File(null, 2, false);
                                getJDFDoc().clearDirtyIDs();
                            }
                        }
                    }
                }
            }
        }
        return save;
    }
    
    /**
     * Method newJDF.
     * creates a new JDF file
     */
    private void newJDF()
    {
        clearViews();
        try
        {
            final JDFDoc jdfDoc = new JDFDoc("JDF");
            final JDFNode jdfRoot = jdfDoc.getJDFRoot();
            jdfRoot.init();
            jdfRoot.setType("Product",true);
            setJDFDoc(jdfDoc);
           
            
            m_treeArea.drawTreeView(getEditorDoc());
            setTitle("Untitled.jdf");
            jdfDoc.setOriginalFileName("Untitled.jdf");
             
        }
        catch (Exception s)
        {
            s.printStackTrace();
            JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"),
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
        
    }
    /**
     * Method newJMF.
     * creates a new JMF file
     */
    private void newJMF()
    {
        clearViews();
        try
        {
            final JDFDoc jdfDoc = new JDFDoc("JMF");
            final JDFJMF jmfRoot = jdfDoc.getJMFRoot();
            jmfRoot.init();
            final Vector requiredAttributes = jmfRoot.getMissingAttributes(9999999);
            
            for (int i = 0; i < requiredAttributes.size(); i++)
            {
                jmfRoot.setAttribute((String)requiredAttributes.elementAt(i), "New Value", null);
            }
            
            setJDFDoc(jdfDoc);
            m_treeArea.drawTreeView(getEditorDoc());
            setTitle("Untitled.jmf");
            jdfDoc.setOriginalFileName("Untitled.jmf");
 
        }
        catch (Exception s)
        {
            s.printStackTrace();
            JOptionPane.showMessageDialog(this, m_littleBundle.getString("FileNotOpenKey"),
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Close the current file.
     */
    private void closeFile()
    {
        int save = 0;
        if(getJDFDoc()==null)
            return;
        if (fileIsEdited())
            save = saveFileQuestion();
        
        if (save != JOptionPane.CANCEL_OPTION)
        {
            final INIReader m_iniFile=Editor.getIniFile();
            final String originalFileName = getJDFDoc().getOriginalFileName();
            if (!originalFileName.startsWith("Untitled"))
            {
                m_iniFile.writeINI(m_menuBar);
                m_menuBar.updateRecentFilesMenu(originalFileName);
            }
            int iDoc=setJDFDoc(null);
            if(iDoc>=0){
                refreshView(getEditorDoc(),null);
            }
            else
            {
                
                final JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                new DropTarget(textArea, this);
                
                if (!m_iniFile.getReadOnly())
                    setEnableClose(false);
                
                m_treeArea.m_treeView.setView(textArea);
                clearViews();
                m_topTabs.setSelectedIndex(m_topTabs.m_IO_INDEX);
                setTitle("CIP4 JDF Editor");
            }
        }
    }
    /**
     * Asks if the user wants to create a new JDF or JMF file.
     */
    public void newFile()
    {
        final String[] options = { m_littleBundle.getString("OkKey"), m_littleBundle.getString("CancelKey") };
        
        final NewFileChooser newFileChooser = new NewFileChooser(m_littleBundle);
        
        final int option = JOptionPane.showOptionDialog(this, newFileChooser, 
                m_littleBundle.getString("ChooseNewFileKey"),
                JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, options, options[0]);
        
        if (option == JOptionPane.OK_OPTION)
        {            
            if ((newFileChooser.getSelection()).equals("JDF"))
                newJDF();
            else
                newJMF();
            
            final INIReader m_iniFile=Editor.getIniFile();
            setEnableOpen(!m_iniFile.getReadOnly());
        }
    }
    
    /**
     * opens a Dialog for Settings of validation and
     *
     */
    public void validateFile()
    {
        try 
        {
            final INIReader m_iniFile=Editor.getIniFile();
            final ValidationDialog dialog = new ValidationDialog(this, m_littleBundle);
            if (dialog.getValidationKeyChosen())
            {
                m_iniFile.setValidationLevel(dialog.validationLevel);
                m_iniFile.setValidationVersion(dialog.version);
                m_iniFile.setUseSchema(dialog.useSchema);
                m_iniFile.setSchemaURL(dialog.schemaFile);
                getModel().validate();
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("ValidateErrorKey") + e.getClass() + " \n"
                    +(e.getMessage()!=null ? ("\"" + e.getMessage() + "\""): ""), 
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * fixes the version of a JDF by calling fixVersion for the selected JDF node ore
     * the closest JDF parent.
     *
     */
    public void fixVersion()
    {
        final JDFDoc doc=getJDFDoc();
        if(doc==null)
            return;
        
        try 
        {
            final FixVersionDialog dialog = new FixVersionDialog(this, m_littleBundle);
            
            // find the closest selectd JDF or JMF element and fix it
            final TreePath path = m_treeArea.getSelectionPath();
            final KElement element = EditorUtils.getElement(path);
            if(element!=null){
                KElement n1=element.getDeepParent(ElementName.JDF,0);
                if(n1==null)
                    n1=element.getDeepParent(ElementName.JMF,0);
                if(n1!=null && n1 instanceof JDFNode)
                {    
                    JDFNode theRoot=(JDFNode) n1;         		
                    JDFAuditPool ap=theRoot.getCreateAuditPool();
                    JDFModified modi=ap.addModified(Editor.getEditorName(),null);
                    modi.setDescriptiveName("update to version "+dialog.getVersion());
                }
            }
            
            // mark our work in the audit pool
            
            // the mother of all fixing routines
            if(element instanceof JDFElement)
            {
                ((JDFElement)element).fixVersion(dialog.getVersion());
                element.eraseEmptyNodes(true);
            }
            
            refreshView(getEditorDoc(),path);           
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    m_littleBundle.getString("FixVersionErrorKey") + e.getClass() + " \n"
                    +(e.getMessage()!=null ? ("\"" + e.getMessage() + "\""): ""), 
                    m_littleBundle.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Creates the SearchDialog.
     * @param searchComponent - Where to perform the search?
     */
    private void findWhatDialog(String searchComponent)
    {
        m_dialog = new SearchDialog(this);
        m_dialog.setSearchComponent(searchComponent);
    }
    /**
     * Method setHelpExit.
     */
    public void setHelpExit()
    {
        m_helpFrame = null;
    }
    
    /**
     * Mother of all action dispatchers
     * @param e the event that gets checked
     */
    public void actionPerformed(ActionEvent e)
    {
        final INIReader m_iniFile=Editor.getIniFile();
        File fileToSave=null;
        
        if(getJDFDoc()!=null)
            fileToSave=new File(getJDFDoc().getOriginalFileName());
        
        final Object eSrc = e.getSource();
        if (eSrc == m_buttonBar.m_saveButton || eSrc == m_menuBar.m_saveItem)
        {
            setCursor(m_waitCursor);
            if (getTitle().equalsIgnoreCase("Untitled.jdf") || getTitle().equalsIgnoreCase("Untitled.jmf"))
            {
                saveFile();
                m_menuBar.updateRecentFilesMenu(fileToSave.toString());
            }
            else
            {
                getJDFDoc().write2File(fileToSave.getAbsolutePath(), 2, false);
                getJDFDoc().clearDirtyIDs();
            }
        }
        else if (eSrc == m_menuBar.m_saveAsItem)
        {
            saveFile();
            m_menuBar.updateRecentFilesMenu(fileToSave.toString());
        }
        else if (eSrc == m_menuBar.m_exportItem)
        {
            exportToDevCap();
        }
        else if (eSrc == m_menuBar.m_quitItem)
        {
            m_iniFile.writeINI(m_menuBar);
            
            if (saveFileQuestion() != JOptionPane.CANCEL_OPTION)
                System.exit(0);
        }
        else if (eSrc == m_menuBar.m_aboutItem)
        {
            ImageIcon imgCIP = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "CIP4.gif");
            
            final String about = "Copyright © 2000-2006,\n"
                + "International Cooperation for Integration of Processes in Prepress, Press and Postpress,\n"
                + "hereinafter referred to as CIP4. All Rights Reserved\n\n"
                + "Authors: Anna Andersson, Evelina Thunell, Ingemar Svenonius, Elena Skobchenko, Rainer Prosi\n\n"
                + "PRELIMINARY PRE-RELEASE VERSION\n\n"
                + "The APPLICATION is provided 'as is', without warranty of any kind, express, implied, or\n"
                + "otherwise, including but not limited to the warranties of merchantability,fitness for a\n"
                + "particular purpose and noninfringement. In no event will CIP4 be liable, for any claim,\n"
                + "damages or other liability whether in an action of contract, tort or otherwise, arising\n"
                + "from, out of, or in connection with the APPLICATION or the use or other dealings in the\n"
                + "APPLICATION.";
            JOptionPane.showMessageDialog(
                    this, about, "CIP4 JDF Editor", JOptionPane.INFORMATION_MESSAGE, imgCIP);
        }
        else if (eSrc == m_menuBar.m_helpItem)
        {
            if (m_helpFrame == null)
            {
                m_helpFrame = new HelpFrame(m_littleBundle);
                m_helpFrame.setVisible(true);
            }
            else
                m_helpFrame.toFront();
        }
        else if (eSrc == m_menuBar.m_versionItem)
        {
            JOptionPane.showMessageDialog(this, 
                    Editor.getEditorName()+"\n"
                    + Editor.getEditorBuildDate()
                    + "\nJDF 1.3 compatible version\n" 
                    + "Schema JDF_1.3.xsd preRelease Candidate\n"
                    + "Build version " + Editor.getEditorVersion(),
                    "Version", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (eSrc == m_menuBar.m_findItem)
        {
            findIt();
        }
        else if (eSrc == m_menuBar.m_findXPathItem)
        {
            findXPathElem();
        }    
        else if (eSrc == m_buttonBar.m_validateButton)
        {
            if (fileToSave != null) 
            {
                setCursor(m_waitCursor);
                getModel().validate();
            }
        }
        else if (eSrc == m_menuBar.m_validateItem)
        {
            if (fileToSave != null) 
            {
                validateFile();
            }
        }
        else if (eSrc == m_menuBar.m_fixVersionItem)
        {
            if (fileToSave != null) 
            {
                fixVersion();
            }
        }
        else if (eSrc == m_buttonBar.m_upOneLevelButton)
        {
            final TreePath selectionPath = m_treeArea.getSelectionPath();
            if (selectionPath != null
                    && ((JDFTreeNode) selectionPath.getLastPathComponent()).isElement()
                    && !((JDFTreeNode) selectionPath.getLastPathComponent()).hasForeignNS()
                    && ((JDFTreeNode) selectionPath.getLastPathComponent())
                    != (JDFTreeNode) getRootNode().getFirstChild())
                m_topTabs.goUpOneLevelInProcessView();
        }
        
        else if (eSrc == m_buttonBar.m_printButton)
        {
            printWhat();
        }
        else if (eSrc == m_buttonBar.m_zoomInButton)
        {
            m_topTabs.zoom('+');
        }
        else if (eSrc == m_buttonBar.m_zoomOutButton)
        {
            m_topTabs.zoom('-');
        }
        else if (eSrc == m_buttonBar.m_zoomOrigButton)
        {
            m_topTabs.zoom('o');
        }
        else if (eSrc == m_buttonBar.m_zoomBestButton)
        {
            m_topTabs.zoom('b');
        }
        else if (eSrc == m_menuBar.m_closeItem)
        {
            closeFile();
        }
        // open the most recent file
        else if (eSrc == m_menuBar.m_subMenuItem[0])
        {
            final String newFile = m_menuBar.m_subMenuItem[0].getText();
            openRecentFile(newFile);
        }
        // open number two in recent files menu
        else if (eSrc == m_menuBar.m_subMenuItem[1])
        {
            final String newFile = m_menuBar.m_subMenuItem[1].getText();
            openRecentFile(newFile);
        }
        // open number three in recent files menu
        else if (eSrc == m_menuBar.m_subMenuItem[2])
        {
            final String newFile = m_menuBar.m_subMenuItem[2].getText();
            openRecentFile(newFile);
        }
        // open number four in recent files menu
        else if (eSrc == m_menuBar.m_subMenuItem[3])
        {
            final String newFile = m_menuBar.m_subMenuItem[3].getText();
            openRecentFile(newFile);
        }
        // open the last file in recent files menu
        else if (eSrc == m_menuBar.m_subMenuItem[4])
        {
            final String newFile = m_menuBar.m_subMenuItem[4].getText();
            openRecentFile(newFile);
        }
        
        else if (eSrc == m_menuBar.m_spawnItem)
        {
            spawn(false);
        }
        else if (eSrc == m_menuBar.m_spawnInformItem)
        {
            spawn(true);
        }
        else if (eSrc == m_menuBar.m_mergeItem)
        {
            merge();
        }
        else if (eSrc == m_menuBar.m_devCapItem) 
        {
            openDeviceCapFile();
        }    
        else if (eSrc == m_menuBar.m_preferenceItem)
        {
            showPreferences();
        }
        //20040712 MRE
        else if (eSrc == m_menuBar.m_sendToDeviceItem)
        {
            sendToDevice();
        } 
           // copy the results of the validation to the system clip board
        else if (eSrc == m_menuBar.m_copyValidationListItem)
        {    
            //TODO           m_errorTabbedPane.copyValidationListToClipBoard();
        }
        else if (!m_iniFile.getReadOnly())
        {
            if (eSrc == m_buttonBar.m_cutButton || eSrc == m_menuBar.m_cutPopupItem || eSrc == m_menuBar.m_cutItem)
            {
                cutSelectedNode();
            }
            else if (eSrc == m_buttonBar.m_copyButton || eSrc == m_menuBar.m_copyPopupItem || eSrc == m_menuBar.m_copyItem)
            {
                copySelectedNode();
            }
            else if (eSrc == m_buttonBar.m_pasteButton || eSrc == m_menuBar.m_pastePopupItem || eSrc == m_menuBar.m_pasteItem)
            {
                pasteCopiedNode();
            }
            else if (eSrc == m_menuBar.m_insertElemBeforeItem || eSrc == m_menuBar.m_insertElemBeforePopupItem)
            {
                m_treeArea.insertElementBeforeSelectedNode();    
            }
            else if (eSrc == m_menuBar.m_insertElemAfterItem || eSrc == m_menuBar.m_insertElemAfterPopupItem)
            {
                insertElementAfterSelectedNode();
            }
            else if (eSrc == m_menuBar.m_insertElemIntoItem || eSrc == m_menuBar.m_insertElemIntoPopupItem)
            {
                insertElementIntoSelectedNode();
            }
            else if (eSrc == m_menuBar.m_insertInResItem || eSrc == m_menuBar.m_insertInResPopupItem)
            {
                m_treeArea.insertResourceWithLink(true, true);
            }
            else if (eSrc == m_menuBar.m_insertOutResItem || eSrc == m_menuBar.m_insertOutResPopupItem)
            {
                m_treeArea.insertResourceWithLink(true, false);
            }
            else if (eSrc == m_menuBar.m_insertResPopupItem || eSrc == m_menuBar.m_insertResItem)
            {
                m_treeArea.insertResourceWithLink(false, false);
            }
            else if (eSrc == m_menuBar.m_insertInResLinkItem || eSrc == m_menuBar.m_insertInResLinkPopupItem)
            {
                m_treeArea.insertResourceLink(true);
            }
            else if (eSrc == m_menuBar.m_insertOutResLinkItem || eSrc == m_menuBar.m_insertOutResLinkPopupItem)
            {
                m_treeArea.insertResourceLink(false);
            }
            else if (eSrc == m_menuBar.m_insertAttrItem || eSrc == m_menuBar.m_insertAttrPopupItem)
            {
                m_treeArea.insertAttrItem();
            }
            else if (eSrc == m_menuBar.m_deleteItem || eSrc == m_menuBar.m_deletePopupItem)
            {
                m_treeArea.deleteSelectedNode();
            }
            else if (eSrc == m_menuBar.m_renamePopupItem || eSrc == m_menuBar.m_renameItem)
            {
                renameSelectedNode();
            }
            else if (eSrc == m_menuBar.m_modifyAttrValuePopupItem || eSrc == m_menuBar.m_modifyAttrValueItem)
            {
                m_treeArea.modifyAttribute();
            }
            else if (eSrc == m_menuBar.m_requiredAttrItem || eSrc == m_menuBar.m_requiredAttrPopupItem)
            {
                addRequiredAttrsToSelectedNode();
            }
            else if (eSrc == m_menuBar.m_requiredElemItem || eSrc == m_menuBar.m_requiredElemPopupItem)
            {
                addRequiredElemsToSelectedNode();
            }
            else if (eSrc == m_menuBar.m_xpandPopupItem)
            {
                m_treeArea.xpand(null);
            }
            else if (eSrc == m_menuBar.m_collapsePopupItem)
            {
                m_treeArea.collapse(null); 
            }
            else if (eSrc == m_menuBar.m_copyToClipBoardPopupItem)
            {
                copyToClipBoard(m_treeArea.getSelectionPath()); 
            }
            else if (eSrc == m_buttonBar.m_refreshButton)
            {
                refresh();
            }
        }
        // select
        if (m_menuBar.m_Windows!=null)
        {
            for(int i=0;i<m_menuBar.m_Windows.length;i++)
            {
                if(eSrc==m_menuBar.m_Windows[i])
                    nextFile(i);
            }
        }
        setCursor(m_readyCursor);
    }
    
    ///////////////////////////////////////////////////////////////////
    
    public EditorDocument nextFile(int pos)
    {
        if(m_VjdfDocument.isEmpty())
            return null;
        if(pos==-1)
            pos=m_DocPos+1;
        
        if(pos>=m_VjdfDocument.size())
            pos=0;
        
        EditorDocument ed=(EditorDocument) m_VjdfDocument.elementAt(m_DocPos);
        if(pos==m_DocPos)
            return ed; // nop

        
        m_DocPos=pos;
        ed=(EditorDocument) m_VjdfDocument.elementAt(m_DocPos);
        
        refreshView(ed,null);
        return ed;
    }
    
    public void updateViews(TreePath path)
    {
        m_treeArea.repaint();
        EditorDocument ed=getEditorDoc();
        if (path != null && ed!=null)
        {
            ed.getJDFTree().scrollPathToVisible(path);
            ed.getJDFTree().setSelectionPath(path);
        }
        m_topTabs.m_inOutScrollPane.clearInOutView();
        m_topTabs.m_inOutScrollPane.initInOutView();
        this.setCursor(JDFFrame.m_readyCursor);
    }
    
    class CutItemEdit extends DeleteItemEdit
    {
        private static final long serialVersionUID = -2778264565816330000L;
        
        public CutItemEdit(final JDFFrame parentFrame, final TreePath treePath)
        {
            super(parentFrame, treePath);
        }
        
        public String getPresentationName() 
        {
            return "Cut";
        }
    }
    
    /**
     * renames selected node
     */
    private void renameSelectedNode()
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null)
        {
            final String previousNodeName=((JDFTreeNode)path.getLastPathComponent()).getName();
            final JDFTreeNode newTreeNode = getModel().renameElementsAndAttributes(path);
            if (newTreeNode != null)
            {
                final RenameNodeEdit edit = 
                    new RenameNodeEdit(this, path, newTreeNode, previousNodeName);
                undoSupport.postEdit( edit );
            }
            else 
            {
                JOptionPane.showMessageDialog(this, 
                        "Rename operation was not completed." +
                        "\nNo name selected or an error occured", "Rename", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            updateViews(path);
        }
    }
    
    /**
     * cuts selected node
     */
    public void cutSelectedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null)
        {
            m_copyNode = new JDFTreeCopyNode((JDFTreeNode) path.getLastPathComponent(),this);
            
            final CutItemEdit edit = new CutItemEdit(this, path);
            undoSupport.postEdit( edit );
            
            m_menuBar.m_pasteItem.setEnabled(true);
            m_buttonBar.m_pasteButton.setEnabled(true);
        }
    }
    
    /**
     * adds required elements to selected Node
     */
    private void addRequiredElemsToSelectedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null) 
        {
            JDFTreeNode intoNode;
            try
            {
                intoNode = (JDFTreeNode) path.getLastPathComponent();
            }
            catch (Exception s)
            {
                intoNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
            }
            
            final Vector addedVector = getModel().addRequiredElements(intoNode);
            
            if (addedVector.size()>0)
            {
                final AddRequiredElemEdit edit = 
                    new AddRequiredElemEdit(this, path, intoNode, addedVector);
                undoSupport.postEdit( edit );
            }
            else
            {
                JOptionPane.showMessageDialog(this, "No missing required elements found", 
                        "Add Required Elements", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * adds required attributes to selected Node
     */
    private void addRequiredAttrsToSelectedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null) 
        {
            JDFTreeNode intoNode;
            try
            {
                intoNode = (JDFTreeNode) path.getLastPathComponent();
             }
            catch (Exception s)
            {
                intoNode = (JDFTreeNode) path.getParentPath().getLastPathComponent();
            }
            
            final Vector addedVector = getModel().addRequiredAttributes(intoNode);
            
            if (addedVector.size()>0)
            {
                final AddRequiredAttrEdit edit = 
                    new AddRequiredAttrEdit(this, path, intoNode, addedVector);
                undoSupport.postEdit( edit );
            }
            else
            {
                JOptionPane.showMessageDialog(this, "No missing required attributes found", 
                        "Add Required Attributes", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    
    /**
     * copies selected node
     */
    public void copySelectedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null)
        {
            m_copyNode = new JDFTreeCopyNode((JDFTreeNode) path.getLastPathComponent(),this);
            m_menuBar.m_pasteItem.setEnabled(true);
            m_buttonBar.m_pasteButton.setEnabled(true);
        }
    }
    
    /**
     * pastes copied node
     */
    public void pasteCopiedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null && m_copyNode != null)
        {
            JDFTreeNode intoNode = (JDFTreeNode) path.getLastPathComponent();
            KElement intoElement = intoNode.getElement();
            
            final JDFTreeNode pasteNode = m_copyNode.pasteNode(path);
            if (pasteNode != null)
            {
                final PasteItemEdit edit = new PasteItemEdit(this, path, intoNode, intoElement, pasteNode);
                undoSupport.postEdit( edit );
            }
        }
    }
    
    /**
     * inserts element into selected node
     */
    private void insertElementIntoSelectedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null)
        {
            final JDFTreeNode intoNode = (JDFTreeNode) path.getLastPathComponent();            
            JDFTreeNode newNode = getModel().insertElementInto(intoNode);         
            if (newNode != null)
            {
                final InsertElementEdit edit =
                    new InsertElementEdit(this, null, newNode,"Insert Into");
                undoSupport.postEdit( edit );
            }
        }
    }
    
    
    /**
     * inserts element after selected node
     */
    private void insertElementAfterSelectedNode() 
    {
        final TreePath path = m_treeArea.getSelectionPath();
        if (path != null)
        {
            final JDFTreeNode afterNode = (JDFTreeNode) path.getLastPathComponent();
            final JDFTreeNode nextSiblingNode = (JDFTreeNode) afterNode.getNextSibling();
            
            final JDFTreeNode newNode = getModel().insertElementAfter(path);
            if (newNode != null)
            {
                final InsertElementEdit edit = new InsertElementEdit(this, nextSiblingNode,  newNode,"Insert Element after");
                undoSupport.postEdit( edit );
            }
            updateViews(new TreePath(newNode.getPath()));
        }
    }
    
    /**
     * copies the content of the marked node to the system clip board 
     * @param p - The TreePath to collapse
     */
    private void copyToClipBoard(TreePath p)
    {
        final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
        final Enumeration e = node.postorderEnumeration();
        
        while (e.hasMoreElements())
        {
            final JDFTreeNode treeNode = (JDFTreeNode) e.nextElement();
            
            if(treeNode.getUserObject()!=null && treeNode.isElement())
            {    
                final KElement elem = treeNode.getElement();
                
                //Copy XML representation of the selected node to clip board
                final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                final StringSelection contents = new StringSelection(elem.toXML());
                cb.setContents(contents, null);
            }    
        }
    }
    
    /**
     * Creates the m_dialog for Input of XPath.
     * Selects in the TreeView the node with defined XPath.  
     * If XPath does not exist - displays Error message
     */
    private void findXPathElem() 
    {
        if (m_treeArea != null)
        {
            final String xPath = JOptionPane.showInputDialog(this, "Input XPath", "/JDF");
            if (xPath == null || xPath.equals(JDFConstants.EMPTYSTRING))
            {
                JOptionPane.showMessageDialog(this, "XPath was not chosen", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else 
            {
                final KElement r = ((JDFTreeNode) getRootNode().getFirstChild()).getElement();
                
                final int atPos = xPath.indexOf(JDFConstants.AET);
                boolean bFound = true;
                
                String message = JDFConstants.EMPTYSTRING;
                
                if (atPos > 0)
                {
                    String attr = JDFConstants.EMPTYSTRING;
                    try 
                    {
                        attr = r.getXPathAttribute(xPath,JDFConstants.EMPTYSTRING);
                    }
                    catch (JDFException exc)
                    {
                        message = exc.getMessage();
                    }
                    
                    if (attr.equals(JDFConstants.EMPTYSTRING))
                        bFound = false;
                }
                else 
                {
                    KElement el = null;
                    try 
                    {
                        el = r.getXPathElement(xPath);
                    }
                    catch (JDFException exc)
                    {
                        message = exc.getMessage();
                    } 
                    if (el == null)
                        bFound = false;
                    
                }
                if (!bFound || !message.equals(JDFConstants.EMPTYSTRING))
                {
                    EditorDocument ed=getEditorDoc();
                    ed.getJDFTree().clearSelection();
                    if (message.equals(JDFConstants.EMPTYSTRING))
                    {
                        message = "No element with such XPath found:\n\n" + xPath + '\n';
                    }
                    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
                else 
                {
                    m_treeArea.findInNode(xPath);
                }
            }
        }
    }
    
    
    /**
     * Determine where the search is to be done
     */
    private void findIt()
    {
        String findString="JDFTree";
        EditorDocument ed=getEditorDoc();
        if (!ed.getJDFTree().isFocusOwner())
        {
            m_searchTree = m_topTabs.m_inOutScrollPane.findIt();
            boolean found=m_searchTree!=null;
            
            if (found)
            {
                findString="NeighbourTree";
            }
        }
        // findstring is always set at least to JDFTree
        if (m_dialog == null)
            findWhatDialog(findString);
        else
        {
            m_dialog.setSearchComponent(findString);
            m_dialog.toFront();
        }
    }
    
    
    /**
     * returns true if the currently selected document has been modified
     * @return
     */
    boolean fileIsEdited()
    {
        if (getJDFDoc() != null)
        {
            VString v = getJDFDoc().getDirtyIDs();            
            if (v != null && !v.isEmpty())
                return true;
        }        
        return false;
    }
    
    
    
    /////////////////////////////////////////////////////////////
    class MyTreeSelectionListener implements TreeSelectionListener
    {
        public void valueChanged(TreeSelectionEvent e)
        {
            e.getClass(); // fool compiler
            if (m_treeArea.getSelectionPath() != null)
            {
                final JDFTreeNode node = (JDFTreeNode) m_treeArea.getSelectionPath().getLastPathComponent();
                KElement elem = null;
                
                if (node.isElement())
                {
                    elem = node.getElement();
                    
                    if (elem instanceof JDFNode)
                    {
                        m_menuBar.setSpawnMergeEnabled(true);
                    }
                    else
                    {
                        m_menuBar.setSpawnMergeEnabled(false);
                    }
                }
                else 
                {
                    m_menuBar.setSpawnMergeEnabled(false);
                }
                
                final int selIndex = m_topTabs.getSelectedIndex();
                
//              m_errorTabbedPane.createList(node);
                
                final INIReader m_iniFile=Editor.getIniFile();
                if (!m_iniFile.getReadOnly())
                {
                    m_menuBar.setEnabledInMenu(m_treeArea.getSelectionPath());
                }
                m_topTabs.setSelectedIndex(selIndex);
                m_topTabs.showComment();
                
                if (selIndex == m_topTabs.m_IO_INDEX || selIndex == m_topTabs.m_COM_INDEX)
                {
                    if (!m_topTabs.isEnabledAt(m_topTabs.m_COM_INDEX))
                    {
                        m_topTabs.m_inOutScrollPane.clearInOutView();
                        m_topTabs.m_inOutScrollPane.initInOutView();
                    }
                }
                else if (selIndex == m_topTabs.m_PROC_INDEX && elem != null)
                    m_topTabs.initProcessView();
            }
        }
    }
    
    
    
    private void setEnableClose(boolean mode)
    {
        m_menuBar.setEnableClose(mode);
        m_buttonBar.setEnableClose(mode);
    }
    
    private void setEnableOpen(boolean mode)
    {
        m_menuBar.setEnableOpen(mode);
        m_buttonBar.setEnableOpen(mode);
    }
    
    class MyUndoManager extends UndoManager
    {
        private static final long serialVersionUID = 626128726824504001L;
        
        public void undoableEditHappened( UndoableEditEvent e )
        {
            addEdit( e.getEdit() );
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    class MyTreeModelListener implements TreeModelListener
    {
        public void treeNodesChanged(TreeModelEvent event)
        {
            JDFTreeNode node;
            node = (JDFTreeNode) (event.getTreePath().getLastPathComponent());
            
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
    
    class UndoAction extends AbstractAction 
    {
        private static final long serialVersionUID = 626128726824504002L;
        public void actionPerformed(ActionEvent e) {
            e.getID();
            try 
            {
                if (undomanager.canUndo())
                    undomanager.undo();
            } 
            catch (CannotUndoException ex) 
            {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }
        
        public void updateUndoState() 
        {
            // refresh undo
            m_buttonBar.m_undoButton.setText( undomanager.getUndoPresentationName() );
            m_buttonBar.m_undoButton.setEnabled( undomanager.canUndo() );
            
            m_menuBar.m_undoItem.setText( undomanager.getUndoPresentationName() );
            m_menuBar.m_undoItem.setEnabled( undomanager.canUndo() );
        }
    }
    
    class RedoAction extends AbstractAction 
    {
        private static final long serialVersionUID = 626128726824504003L;
        
        public void actionPerformed(ActionEvent e) {
            e.getID(); // fool compiler
            try 
            {
                if (undomanager.canRedo())
                    undomanager.redo();
            } 
            catch (CannotUndoException ex) 
            {
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }
        
        public void updateRedoState() 
        {
            // refresh redo
            m_buttonBar.m_redoButton.setText( undomanager.getRedoPresentationName() );
            m_buttonBar.m_redoButton.setEnabled( undomanager.canRedo() );
            
            m_menuBar.m_redoItem.setText( undomanager.getRedoPresentationName() );
            m_menuBar.m_redoItem.setEnabled( undomanager.canRedo() );
        }
    }
    
    class EditChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            e.getClass(); // fool compiler
            if (m_treeArea != null)
                m_treeArea.repaint();
            
            if (getJDFDoc() == null)
                m_buttonBar.m_validateButton.setEnabled(false);
            
            final INIReader m_iniFile=Editor.getIniFile();
            m_iniFile.writeINI(m_menuBar);
        }
    }
    
    
    public void dragGestureRecognized(DragGestureEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dragDropEnd(DragSourceDropEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dragEnter(DragSourceDragEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dragExit(DragSourceEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dragOver(DragSourceDragEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dropActionChanged(DragSourceDragEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dragEnter(DropTargetDragEvent e)
    {
        e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
    }
    public void dragExit(DropTargetEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public void dragOver(DropTargetDragEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    public synchronized void drop(DropTargetDropEvent e)
    {
        try
        {
            final Transferable flavor = e.getTransferable();
            
            if (flavor.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            {
                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                final java.util.List fileList = (java.util.List) flavor.getTransferData(DataFlavor.javaFileListFlavor);
                final Iterator files = fileList.iterator();
                
                if (files.hasNext())
                {
                    readFile((File) files.next());
                }
                e.getDropTargetContext().dropComplete(true);
            }
            else
                e.rejectDrop();
        }
        catch (IOException ioe)
        {
            System.out.println("data no longer available in the requested flavor");
            e.rejectDrop();
        }
        catch (UnsupportedFlavorException ufe)
        {
            System.out.println("data flavor not supported");
            e.rejectDrop();
        }
    }
    
    public void dropActionChanged(DropTargetDragEvent e)
    {
        e.getClass(); // fool compiler
        //
    }
    
    /* (non-Javadoc)
     * @see java.awt.datatransfer.boardOwner#lostOwnership(
     *      java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
     */
    public void lostOwnership(Clipboard arg0, Transferable arg1) {
        // TODO Auto-generated method stub
        arg0.getClass();
        arg1.getClass();
    }
    
    public TreeSelectionListener getTreeSelectionListener()
    {
        return new MyTreeSelectionListener();
    }
    public JDFTreeNode getRootNode()
    {
        EditorDocument ed = getEditorDoc();
        JDFTreeModel mod = ed==null ? null : ed.getModel();
        return mod==null ? null : mod.getRootNode();
    }
    
    /**
     * set the currently displayed doc to doc
     * @param doc
     * @return the index in the list of docs that doc is stored in
     */
    public int setJDFDoc(JDFDoc doc)
    {       
        int i=m_DocPos;
        if(doc!=null){
            i=EditorDocument.indexOfJDF(doc,m_VjdfDocument);
            
            if(i>=0)
            {
                m_DocPos=i;
            }
            else
            {
                m_VjdfDocument.add(new EditorDocument(doc));
                m_DocPos=m_VjdfDocument.size()-1;
            }
        } // doc==null --> remove this entry
        else if (m_DocPos>=0 && m_DocPos<m_VjdfDocument.size())
        {
            m_VjdfDocument.remove(m_DocPos);
            m_DocPos--;
            // roll over to the end; also ok if size=0, since -1 is the flag for all closed
            if(m_DocPos==-1)
                m_DocPos=m_VjdfDocument.size()-1;
        }
        m_menuBar.updateWindowsMenu();
        
        return m_DocPos;
    }
    
    /**
     * get the JDFDoc of the currently displayed JDF
     * @return the JDFDoc that is currently being displayed
     */
    public EditorDocument getEditorDoc()
    {
        if(m_DocPos<0)
            return null;
        return (EditorDocument) m_VjdfDocument.elementAt(m_DocPos);
    }
    /**
     * get the JDFDoc of the currently displayed JDF
     * @return the JDFDoc that is currently being displayed
     */
    public JDFDoc getJDFDoc()
    {
        EditorDocument ed =getEditorDoc();
        return ed==null ? null : ed.getJDFDoc();
    }

    public void setModel(JDFTreeModel m_model)
    {
        EditorDocument ed =getEditorDoc();
        ed.setModel(m_model);
    }

    public JDFTreeModel getModel()
    {
        EditorDocument ed =getEditorDoc();
        return ed==null ? null : ed.getModel();
    }

    ////////////////////////////////////////////////////////////////
    
}
