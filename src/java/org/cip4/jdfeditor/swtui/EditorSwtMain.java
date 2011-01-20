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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.cip4.jdfeditor.EditorDocument;
import org.cip4.jdfeditor.EditorUtils;
import org.cip4.jdfeditor.INIReader;
import org.cip4.jdflib.core.JDFDoc;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Class with main method to run JDFEditor in SWT UI.
 *
 */
public class EditorSwtMain
{
	private static final Logger log = Logger.getLogger(EditorSwtMain.class);
	
	private static ResourceBundle bundle;
	
	private static final String ICONS_PATH = "icons-nuvola/org/cip4/jdfeditor/icons-nuvola/";
	
//	Vector<EditorDocument> m_VjdfDocument = new Vector<EditorDocument>();
	Map<String, JDFDoc> documentsMap = new HashMap<String, JDFDoc>();
	
	private Tree jdfTree;
	private TreeViewer treeViewer;

	public EditorSwtMain()
	{
	}
	
	public void createMenuBar(Shell shell)
	{
		Menu menuBar = new Menu(shell, SWT.BAR);

		MenuItem menuItemFile = new MenuItem(menuBar, SWT.CASCADE);
		menuItemFile.setText(bundle.getString("FileKey"));
		
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		menuItemFile.setMenu(fileMenu);
		
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
		newItem.setText(bundle.getString("NewKey"));
		
		MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
		openItem.setText(bundle.getString("OpenKey"));
		
		
		MenuItem menuItemEdit = new MenuItem(menuBar, SWT.CASCADE);
		menuItemEdit.setText(bundle.getString("EditKey"));
		
		shell.setMenuBar(menuBar);
	}
	
	public void createToolBar(final Shell shell)
	{
		Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Image iconNew = new Image(shell.getDisplay(), ICONS_PATH + "filenew.png");
		Image iconOpen = new Image(shell.getDisplay(), ICONS_PATH + "fileopen.png");
		
		CoolBar coolBar = new CoolBar(c, SWT.HORIZONTAL);
		
		Listener listener = new Listener()
		{
			public void handleEvent(Event event)
			{
//				log.debug("event: " + event);
				ToolItem item = (ToolItem) event.widget;
				String string = item.getToolTipText();
				log.debug("string: " + string);
				
				if (string.equals(bundle.getString("NewKey")))
				{
//					TODO: show dialog
				} else if (string.equals(bundle.getString("OpenKey")))
				{
					FileDialog dialog = new FileDialog(shell, SWT.OPEN);
					dialog.setFilterNames(new String[] { "All accepted files (.xml, .jdf, .jmf, .mim, .mjm, .mjd)",
							"XML files (.xml)", "JDF files (.jdf)", "JMF files (.jmf)",
							"MIM files (.mim)", "MJM files (.mjm)", "MJD files (.mjd)",
							"All Files" });
					dialog.setFilterExtensions(new String[] { "*.xml;*.jdf;*.jmf;*.mim;*.mjm;*.mjd",
							"*.xml", "*.jdf", "*.jmf",
							"*.mim", "*.mjm", "*.mjd",
							"*.*" });
					String result = dialog.open();
					log.debug("selected file: " + result);
					if (result == null)
					{
						return;
					}
					
					if (documentsMap.containsKey(result))
					{
						log.debug("file already opened: " + result);
						JDFDoc jdfDoc = documentsMap.get(result);
						treeViewer.setInput(jdfDoc);
					} else
					{
						File fts = new File(result);
//						EditorDocument[] eDoc = EditorUtils.getEditorDocuments(fts);
//						System.out.println("eDoc: " + eDoc);
						
						try {
							FileInputStream inStream = new FileInputStream(fts);
							JDFDoc jdfDoc = EditorUtils.parseInStream(inStream, false);
							
							System.out.println("jdfDoc: " + jdfDoc);
//							jdfTree.setInput(jdfDoc);
							treeViewer.setInput(jdfDoc);
							
							documentsMap.put(result, jdfDoc);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					/*File fts = new File(result);
					int docIndex = EditorDocument.indexOfFile(fts, m_VjdfDocument);
					log.debug("docIndex: " + docIndex);
					
					if (docIndex >= 0) {
						
					} else if (fts.exists()) {
						EditorDocument[] eDoc = EditorUtils.getEditorDocuments(fts);
					}*/
				}
			}
		};
		
		CoolItem item = new CoolItem(coolBar, SWT.NONE);
		ToolBar tb = new ToolBar(coolBar, SWT.FLAT);
		ToolItem fileNewToolItem = new ToolItem(tb, SWT.NONE);
		fileNewToolItem.setImage(iconNew);
		fileNewToolItem.setToolTipText(bundle.getString("NewKey"));
		fileNewToolItem.addListener(SWT.Selection, listener);
		
		ToolItem fileOpenToolItem = new ToolItem(tb, SWT.NONE);
		fileOpenToolItem.setImage(iconOpen);
		fileOpenToolItem.setToolTipText(bundle.getString("OpenKey"));
		fileOpenToolItem.addListener(SWT.Selection, listener);
		
		Point p = tb.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		tb.setSize(p);
		Point p2 = item.computeSize(p.x, p.y);
		item.setControl(tb);
		item.setSize(p2);
	}
	
	public void createMainUI(Composite c)
	{
		SashForm sashForm = new SashForm(c, SWT.HORIZONTAL);
		sashForm.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		sashForm.setLayoutData(gd);
		
		Composite child1 = new Composite(sashForm, SWT.NONE);
		child1.setLayout(new GridLayout(1, false));
		child1.setLayoutData(gd);
//		new Label(child1, SWT.NONE).setText("Label in pane 1");
		
		treeViewer = new TreeViewer(child1, SWT.BORDER);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		treeViewer.setContentProvider(new JDFTreeContentProvider());
		treeViewer.setLabelProvider(new JDFTreeLabelProvider());

		Composite child2 = new Composite(sashForm, SWT.NONE);
		child2.setLayout(new FillLayout());
		new Button(child2, SWT.PUSH).setText("Button in pane2");
		
		sashForm.setWeights(new int[] {25, 75});
	}
	
	public void createUI()
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setMaximized(true);
		shell.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		shell.setLayoutData(gd);
		shell.setText(bundle.getString("TitleKey"));
		
		createMenuBar(shell);
		createToolBar(shell);
		
		Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new GridLayout());
		c.setLayoutData(gd);
		
		createMainUI(c);
		
		shell.open();
		
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		INIReader iniFile = new INIReader();
		final String language = iniFile.getLanguage();
		final Locale currentLocale = new Locale(language, language.toUpperCase());
		bundle = ResourceBundle.getBundle("org.cip4.jdfeditor.messages.JDFEditor", currentLocale);
		
		EditorSwtMain esm = new EditorSwtMain();
		esm.createUI();
	}

}
