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

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.cip4.jdfeditor.INIReader;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Class with main method to run JDFEditor in SWT UI.
 *
 */
public class EditorSwtMain {
	private static final Logger log = Logger.getLogger(EditorSwtMain.class);
	
	private static ResourceBundle bundle;
	
	private static final String ICONS_PATH = "icons-nuvola/org/cip4/jdfeditor/icons-nuvola/";

	public EditorSwtMain() {
	}
	
	public void createMenuBar(Shell shell) {
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
	
	public void createToolBar(Shell shell) {
		Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Image iconNew = new Image(shell.getDisplay(), ICONS_PATH + "filenew.png");
		Image iconOpen = new Image(shell.getDisplay(), ICONS_PATH + "fileopen.png");
		
		CoolBar coolBar = new CoolBar(c, SWT.HORIZONTAL);
		
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				log.debug("event: " + event);
				ToolItem item = (ToolItem) event.widget;
				String string = item.getToolTipText();
				log.debug("string: " + string);
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
		
		Point p = tb.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		tb.setSize(p);
		Point p2 = item.computeSize(p.x, p.y);
		item.setControl(tb);
		item.setSize(p2);
	}
	
	public void createMainUI(Composite c) {
		SashForm sashForm = new SashForm(c, SWT.HORIZONTAL);
		sashForm.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		sashForm.setLayoutData(gd);
		
		Composite child1 = new Composite(sashForm, SWT.NONE);
		child1.setLayout(new GridLayout(1, false));
		child1.setLayoutData(gd);
		new Label(child1, SWT.NONE).setText("Label in pane 1");

		Composite child2 = new Composite(sashForm, SWT.NONE);
		child2.setLayout(new FillLayout());
		new Button(child2, SWT.PUSH).setText("Button in pane2");
		
		sashForm.setWeights(new int[] {20, 80});
	}
	
	public void createUI() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setMaximized(true);
		shell.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		shell.setLayoutData(gd);
		shell.setText(bundle.getString("TitleKey"));
		shell.open();
		
		createMenuBar(shell);
		createToolBar(shell);
		
		Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new GridLayout());
		c.setLayoutData(gd);
		
		createMainUI(c);
		
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		INIReader iniFile = new INIReader();
		final String language = iniFile.getLanguage();
		final Locale currentLocale = new Locale(language, language.toUpperCase());
		bundle = ResourceBundle.getBundle("org.cip4.jdfeditor.messages.JDFEditor", currentLocale);
		
		EditorSwtMain esm = new EditorSwtMain();
		esm.createUI();
	}

}
