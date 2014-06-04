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
package org.cip4.tools.jdfeditor;

import org.cip4.tools.jdfeditor.util.ResourceUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ThunellE AnderssonA
 * Choose format of new file
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NewFileChooser extends JPanel implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 457494844472759130L;

	private String fileType;
	private JRadioButton radioJDFButton;
	private JRadioButton radioJMFButton;
	private JRadioButton radioGTButton;
	private ButtonGroup fileTypeGroup;

	/**
	 * Method NewFileChooser.
	 */
	public NewFileChooser()
	{
		super();
		setLayout(new GridLayout(4, 1));
		this.fileType = "JDF";
		init();
		setVisible(true);
	}

	/**
	 * Method init.
	 * Create the new file chooser
	 */
	private void init()
	{
		/*
		* Need to add this getString "Golden Ticket" b/c it states right now Do you want to create a new JDF or JMF file?
		*/
		final JLabel label = new JLabel(ResourceUtil.getMessage("ChooseNewFileKey"));
		add(label);

		fileTypeGroup = new ButtonGroup();

		radioJDFButton = new JRadioButton(ResourceUtil.getMessage("NewJDFKey"));
		radioJDFButton.setActionCommand(ResourceUtil.getMessage("NewJDFKey"));
		radioJDFButton.addActionListener(this);
		radioJDFButton.setSelected(true);
		add(radioJDFButton);
		fileTypeGroup.add(radioJDFButton);

		radioJMFButton = new JRadioButton(ResourceUtil.getMessage("NewJMFKey"));
		radioJMFButton.setActionCommand(ResourceUtil.getMessage("NewJMFKey"));
		radioJMFButton.addActionListener(this);
		add(radioJMFButton);
		fileTypeGroup.add(radioJMFButton);

		radioGTButton = new JRadioButton(ResourceUtil.getMessage("NewGoldenTicket"));
		radioGTButton.setActionCommand(ResourceUtil.getMessage("NewGoldenTicket"));
		radioGTButton.addActionListener(this);
		add(radioGTButton);
		fileTypeGroup.add(radioGTButton);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		final Object src = e.getSource();
		if (src == radioJDFButton)
		{
			this.fileType = "JDF";
		}
		else if (src == radioJMFButton)
		{
			this.fileType = "JMF";
		}
		else
		{
			this.fileType = "GoldenTicket";
		}
	}

	/**
	 * Method getSelection.
	 * get the format of the file to open
	 * @return String
	 */
	public String getSelection()
	{
		return fileType;
	}

}
