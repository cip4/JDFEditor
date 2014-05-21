/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2013 The International Cooperation for the Integration of 
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
package org.cip4.tools.jdfeditor.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.cip4.jdflib.extensions.XJDF20;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.INIReader;

/**
 * Class that implements a "Save as XJDF..." dialog.
 *
 */
public class SaveAsXJDFDialog extends JDialog implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int BUTTON_CANCEL = 0;
	static final int BUTTON_OK = 1;

	private final JButton bOK;
	private final JButton bCancel;

	private final JCheckBox cbExt1;
	private final JCheckBox cbExt2;
	private final JCheckBox cbExt3;
	private final JCheckBox cbExt4;
	private final JCheckBox cbLoPrep;
	private final JCheckBox cbTilde;
	private final JCheckBox cbTypesafeJMF;
	private int choosedButton = BUTTON_CANCEL;

	private static INIReader conf = Editor.getIniFile();

	/**
	 * 
	 */
	public SaveAsXJDFDialog()
	{
		setTitle(Editor.getString("SaveAsXJDFKey"));
		setModal(true);
		setLayout(new BorderLayout());

		JPanel checkboxesPanel = new JPanel();
		checkboxesPanel.setLayout(new BoxLayout(checkboxesPanel, BoxLayout.Y_AXIS));

		cbExt1 = new JCheckBox(Editor.getString("SingleNodeKey"));
		cbExt2 = new JCheckBox(Editor.getString("ConvertStrippingKey"));
		cbExt3 = new JCheckBox(Editor.getString("SpanAsAttributeKey"));
		cbExt4 = new JCheckBox(Editor.getString("MergeRunListKey"));
		cbLoPrep = new JCheckBox(Editor.getString("ConvertLayoutPrepKey"));
		cbTilde = new JCheckBox(Editor.getString("RemoveTildeFromRange"));
		cbTypesafeJMF = new JCheckBox(Editor.getString("TypesafeJMF"));

		checkboxesPanel.add(cbExt1);
		checkboxesPanel.add(cbExt2);
		checkboxesPanel.add(cbExt3);
		checkboxesPanel.add(cbExt4);
		checkboxesPanel.add(cbLoPrep);
		checkboxesPanel.add(cbTilde);
		checkboxesPanel.add(cbTypesafeJMF);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.add(Box.createHorizontalGlue());

		bOK = new JButton("OK");
		bOK.addActionListener(this);
		bCancel = new JButton(Editor.getString("CancelKey"));
		bCancel.addActionListener(this);

		buttonsPanel.add(bOK);
		buttonsPanel.add(bCancel);

		getContentPane().add(checkboxesPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		setSize(screenWidth / 4, screenHeight / 4);
		setLocation(screenWidth / 4, screenHeight / 4);

		cbExt1.setSelected(conf.getXjdfSingleNode());
		cbExt2.setSelected(conf.getXjdfConvertStripping());
		cbExt3.setSelected(conf.getXjdfSpanAsAttribute());
		cbExt4.setSelected(conf.getXjdfMergeRunList());
		cbLoPrep.setSelected(conf.getXjdfConvertLOPrep());
		cbTilde.setSelected(conf.getXjdfConvertTilde());
		cbTypesafeJMF.setSelected(conf.getTypesafeJMF());

		setVisible(true);
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public int getChoosedButton()
	{
		return choosedButton;
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bOK)
		{
			conf.setXjdfSingleNode(cbExt1.isSelected());
			conf.setXjdfConvertStripping(cbExt2.isSelected());
			conf.setXjdfSpanAsAttribute(cbExt3.isSelected());
			conf.setXjdfMergeRunList(cbExt4.isSelected());
			conf.setXjdfConvertLOPrep(cbLoPrep.isSelected());
			conf.setXjdfConvertTilde(cbTilde.isSelected());
			conf.setTypesafeJMF(cbTypesafeJMF.isSelected());

			choosedButton = BUTTON_OK;
			dispose();
		}
		else if (e.getSource() == bCancel)
		{
			choosedButton = BUTTON_CANCEL;
			dispose();
		}
	}

	/**
	 * 
	 * get the converter with the options set in this dialog
	 * @return the converter
	 */
	public XJDF20 getXJDFConverter()
	{
		XJDF20 xjdf20 = new XJDF20();
		xjdf20.setSingleNode(cbExt1.isSelected());
		xjdf20.setMergeLayout(cbExt2.isSelected());
		xjdf20.setSpanAsAttribute(cbExt3.isSelected());
		xjdf20.setMergeRunList(cbExt4.isSelected());
		xjdf20.setMergeLayoutPrep(cbLoPrep.isSelected());
		xjdf20.setConvertTilde(cbTilde.isSelected());
		return xjdf20;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOK()
	{
		return getChoosedButton() == SaveAsXJDFDialog.BUTTON_OK;
	}

}