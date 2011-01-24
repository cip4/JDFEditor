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
package org.cip4.jdfeditor.swtui.tabs;

import java.util.ResourceBundle;
import java.util.Vector;

import org.cip4.jdfeditor.ProcessPart;
import org.cip4.jdfeditor.swtui.EditorSwtMain;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.node.JDFNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ProcessViewTab extends Canvas implements ISelectionChangedListener
{
	private static ResourceBundle bundle = EditorSwtMain.bundle;
	private TreeViewer treeViewer;
	private static int x = 10;
	private static int y = 10;
	private JDFNode jdfNode;

	public ProcessViewTab(Composite parent, int style)
	{
		super(parent, style);
	}
	
	public void setTreeViewer(TreeViewer treeViewer)
	{
		this.treeViewer = treeViewer;
		treeViewer.addSelectionChangedListener(this);
		
		addDisposeListener(new DisposeListener() {
	         public void widgetDisposed(DisposeEvent e) {
//	        	 ProcessViewTab.this.widgetDisposed(e);
	         }
		});
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e)
			{
				ProcessViewTab.this.paintControl(e);
			}
		});
	}
	
	public void paintControl(PaintEvent e)
	{
//		System.out.println("paintControl e: " + e);
		e.gc.drawRectangle(x++, y++, 40, 40);
		
		/*JDFNode rootJDF = jdfNode;
		VElement vJDFNodes = new VElement();
        vJDFNodes.add(rootJDF);
        
        vJDFNodes.addAll(rootJDF.getChildElementVector("JDF", null, null, false, -1,false));
        
        final Vector _vParts = new Vector();*/
	}
	
	public void selectionChanged(SelectionChangedEvent e)
	{
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		Object o = ts.getFirstElement();
		if (o instanceof JDFNode)
		{
			jdfNode = (JDFNode) o;
//			System.out.println("ProcessViewTab2 is: " + is);
			redraw();
		}
	}

}
