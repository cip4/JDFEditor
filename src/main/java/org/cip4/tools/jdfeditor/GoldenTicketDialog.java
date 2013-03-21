package org.cip4.tools.jdfeditor;

/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2008 The International Cooperation for the Integration of 
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * DeviceCapsDialog.java
 * @author Elena Skobchenko
 */

public class GoldenTicketDialog extends JPanel implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -267165456151780040L;

	private JTextField idPath;
	private JButton browse;
	private File idFile;
	private final GridBagLayout outLayout = new GridBagLayout();
	private final GridBagConstraints outConstraints = new GridBagConstraints();

	private JComboBox goldenTicketLabel;
	private JComboBox misICSLevel1;
	private JComboBox jmfICSLevel1;
	private JComboBox gtICSLevel;

	private String gtSelected = "MISCP";
	private String misSelected = "1";
	private String jmfSelected = "1";
	private String gtLevelSelected = "1";

	private int misSelectLevel = 1;
	private int jmfSelectLevel = 1;
	private int gtSelectLevel = 1;
	private HashMap<String, String> labelMap;

	public String[] l1 = { "1", "2" };
	public String[] l2 = { "1", "2", "3" };

	public GoldenTicketDialog()
	{
		super();
		JDFFrame parent = Editor.getFrame();
		final ResourceBundle littleBundle = Editor.getBundle();

		init();
		final String[] options = { littleBundle.getString("OkKey"), littleBundle.getString("CancelKey") };

		final int option = JOptionPane.showOptionDialog(parent, this, "Golden Ticket File Creation", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)//will create the Golden Ticket
		{
			/*String tmpGTfile = gtSelected;*/
			String tmpMISfile = misSelected;
			String tmpJMFfile = jmfSelected;
			String tmpGTlevel = gtLevelSelected;

			misSelectLevel = Integer.parseInt(tmpMISfile);
			jmfSelectLevel = Integer.parseInt(tmpJMFfile);
			gtSelectLevel = Integer.parseInt(tmpGTlevel);
		}
		else
		{
			//             	
		}

	}

	/**
	 * Creates the fields and view for the Merge Dialog.
	 */
	private void init()
	{
		final JPanel panel = new JPanel();
		final ResourceBundle littleBundle = Editor.getBundle();
		outConstraints.fill = GridBagConstraints.BOTH;
		outConstraints.insets = new Insets(0, 0, 10, 0);
		outLayout.setConstraints(panel, outConstraints);
		setLayout(outLayout);
		final GridBagLayout inLayout = new GridBagLayout();
		panel.setLayout(new GridLayout(4, 1));
		panel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GTInputKey")));

		//Golden Ticket Chooser
		JPanel gtChooser = new JPanel();
		gtChooser.setLayout(inLayout);
		gtChooser.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GTFileKey")));
		outLayout.setConstraints(gtChooser, outConstraints);

		initGTLabels(panel, gtChooser);

		//MIS Level
		JPanel MISLevel = new JPanel();
		MISLevel.setLayout(inLayout);
		MISLevel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("MISLevelKey")));
		outLayout.setConstraints(MISLevel, outConstraints);

		misICSLevel1 = new JComboBox(l2);
		misICSLevel1.addActionListener(this);

		MISLevel.add(misICSLevel1);
		panel.add(MISLevel);

		//JMF Level
		JPanel JMFLevel = new JPanel();
		JMFLevel.setLayout(inLayout);
		JMFLevel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("JMFLevelKey")));
		outLayout.setConstraints(JMFLevel, outConstraints);

		jmfICSLevel1 = new JComboBox(l1);
		jmfICSLevel1.addActionListener(this);

		JMFLevel.add(jmfICSLevel1);
		panel.add(JMFLevel);

		//GT Level
		JPanel GTLevel = new JPanel();
		GTLevel.setLayout(inLayout);
		GTLevel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GTLevelKey")));
		outLayout.setConstraints(GTLevel, outConstraints);

		gtICSLevel = new JComboBox(l1);
		gtICSLevel.addActionListener(this);

		GTLevel.add(gtICSLevel);
		panel.add(GTLevel);

		add(panel);
		setVisible(true);
	}

	private void initGTLabels(final JPanel panel, JPanel gtChooser)
	{
		final ResourceBundle littleBundle = Editor.getBundle();
		//Add Golden Tickets as they become available.
		final String[] gt = { "MISCP", "MISPre", "IDP" };
		String[] gtDisplay = new String[gt.length];
		labelMap = new HashMap<String, String>();
		for (int i = 0; i < gt.length; i++)
		{
			gtDisplay[i] = littleBundle.getString(gt[i]);
			labelMap.put(gtDisplay[i], gt[i]);
		}
		goldenTicketLabel = new JComboBox(gtDisplay);
		goldenTicketLabel.addActionListener(this);

		gtChooser.add(goldenTicketLabel);
		panel.add(gtChooser);
	}

	public void actionPerformed(ActionEvent e)
	{
		final Object source = e.getSource();
		JDFFrame parent = Editor.getFrame();
		if (source == browse)
		{
			final EditorFileChooser files = new EditorFileChooser(idFile, "xml jdf");
			final int option = files.showOpenDialog(parent);

			if (option == JFileChooser.APPROVE_OPTION)
			{
				idPath.setText(files.getSelectedFile().getAbsolutePath());
			}
			else if (option == JFileChooser.ERROR_OPTION)
			{
				JOptionPane.showMessageDialog(parent, "File is not accepted", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (source == goldenTicketLabel)
		{
			gtSelected = getGTLabel();
		}
		else if (source == misICSLevel1)
		{
			misSelected = (String) misICSLevel1.getSelectedItem();
		}
		else if (source == jmfICSLevel1)
		{
			jmfSelected = (String) jmfICSLevel1.getSelectedItem();
		}
		else if (source == gtICSLevel)
		{
			getGTLevelSelected();
		}
	}

	private String getGTLabel()
	{
		String s = (String) goldenTicketLabel.getSelectedItem();
		return labelMap.get(s);
	}

	private void getGTLevelSelected()
	{
		gtLevelSelected = (String) gtICSLevel.getSelectedItem();
	}

	/*
	 * Methods to return the Levels and which golden ticket to create.
	 */
	public String getGoldenTicket()
	{
		return gtSelected;
	}

	public int getMISLevel()
	{
		return misSelectLevel;
	}

	public int getJMFLevel()
	{
		return jmfSelectLevel;
	}

	public int getGTLevel()
	{
		return gtSelectLevel;
	}
}
