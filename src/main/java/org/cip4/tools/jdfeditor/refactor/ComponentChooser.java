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
package org.cip4.tools.jdfeditor.refactor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * ComponentChooser.java
 * @author SvenoniusI
 */
public class ComponentChooser extends JPanel implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8989231145248120133L;

	private String component;
	private JRadioButton processButton;
	private JRadioButton inOutButton;
	private JRadioButton jdfTreeButton;
	private ButtonGroup rbg;

	/**
	 *    
	 * 
	 */
	public ComponentChooser()
	{
		super();
		this.setLayout(new GridLayout(4, 1));
		this.component = Editor.getString("ProcessViewKey");
		init();
		setVisible(true);
	}

	/**
	 * 
	 *  
	 */
	private void init()
	{
		final JLabel label = new JLabel("Choose Component to Print:");
		add(label);

		rbg = new ButtonGroup();

		processButton = new JRadioButton(Editor.getString("ProcessViewKey"));
		processButton.setActionCommand(Editor.getString("ProcessViewKey"));
		rbg.add(processButton);
		final JDFFrame frame = Editor.getFrame();
		if (frame.m_topTabs.processAreaIsNull())
			processButton.setEnabled(false);
		else
		{
			processButton.setEnabled(true);
			processButton.setSelected(true);
		}
		processButton.addActionListener(this);
		add(processButton);

		inOutButton = new JRadioButton(Editor.getString("NextNeighbourKey"));
		inOutButton.setActionCommand(Editor.getString("NextNeighbourKey"));
		rbg.add(inOutButton);
		if (frame.m_topTabs.inOutIsNull())
			inOutButton.setEnabled(false);
		else
		{
			inOutButton.setEnabled(true);
			if (frame.m_topTabs.processAreaIsNull())
				inOutButton.setSelected(true);
		}
		inOutButton.addActionListener(this);
		add(inOutButton);

		jdfTreeButton = new JRadioButton(Editor.getString("TreeViewKey"));
		jdfTreeButton.setActionCommand(Editor.getString("TreeViewKey"));
		rbg.add(jdfTreeButton);
		if (frame.m_treeArea.jdfTreeIsNull())
			jdfTreeButton.setEnabled(false);
		else
		{
			jdfTreeButton.setEnabled(true);
			if (frame.m_topTabs.processAreaIsNull() && frame.m_topTabs.inOutIsNull())
				jdfTreeButton.setSelected(true);
		}
		jdfTreeButton.addActionListener(this);
		add(jdfTreeButton);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getComponent()
	{
		return this.component;
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.component = rbg.getSelection().getActionCommand();
	}
}
