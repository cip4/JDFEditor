/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2014 The International Cooperation for the Integration of 
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.goldenticket.BaseGoldenTicket;
import org.cip4.jdflib.goldenticket.IDPGoldenTicket;
import org.cip4.jdflib.goldenticket.MISCPGoldenTicket;
import org.cip4.jdflib.goldenticket.MISFinGoldenTicket;
import org.cip4.jdflib.goldenticket.MISPreGoldenTicket;
import org.cip4.jdflib.goldenticket.ODPGoldenTicket;
import org.cip4.jdflib.goldenticket.WideFormatGoldenTicket;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

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

	private JComboBox<String> goldenTicketLabel;
	private JComboBox<String> misICSLevel1;
	private JComboBox<String> jmfICSLevel1;
	private JComboBox<String> gtICSLevel;
	private JComboBox<String> gtType;
	private JComboBox<String> gtVersion;

	private String gtSelected = "MISCP";
	private String misSelected = "1";
	private String jmfSelected = "1";
	private String gtLevelSelected = "1";
	private EnumVersion gtVersionSelected = JDFElement.getDefaultJDFVersion();
	private int gtTypeSelected = 0;

	public int getGtTypeSelected()
	{
		return gtTypeSelected;
	}

	private int misSelectLevel = 1;
	private int jmfSelectLevel = 1;
	private int gtSelectLevel = 1;
	private HashMap<String, String> labelMap;

	String[] l1 = { "1", "2" };
	String[] l2 = { "1", "2", "3" };

	/**
	 * 
	 */
	public GoldenTicketDialog()
	{
		super();
		JDFFrame parent = MainView.getFrame();

		init();
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };

		final int option = JOptionPane.showOptionDialog(parent, this, "Golden Ticket File Creation", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)//will create the Golden Ticket
		{
			misSelectLevel = StringUtil.parseInt(misSelected, 1);
			jmfSelectLevel = StringUtil.parseInt(jmfSelected, 1);
			gtSelectLevel = StringUtil.parseInt(gtLevelSelected, 1);
		}
		else
		{
			gtSelected = null;
		}

	}

	/**
	 * 
	 * @param gtselect
	 * @param jdfVersion
	 * @return
	 */
	public BaseGoldenTicket getGoldenTicket()
	{
		String gtselect = getGoldenTicketName();
		if (gtselect == null)
		{
			return null; // cancel...
		}
		EnumVersion jdfVersion = getGtVersionSelected();

		int mis = getMISLevel();
		int jmf = getJMFLevel();
		int gt1 = getGTLevel();
		BaseGoldenTicket theGT = null;
		if ("MISCP".equals(gtselect))
		{

			theGT = new MISCPGoldenTicket(gt1, jdfVersion, jmf, mis, true, BaseGoldenTicket.createSheetMap(1));
			theGT.nCols = new int[] { 4, 4 };
		}
		else if (gtselect.startsWith("MISPRE"))
		{
			theGT = new MISPreGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(1));
			theGT.nCols = new int[] { 4, 4 };
			((MISPreGoldenTicket) theGT).setCategory(gtselect);
		}
		else if (gtselect.startsWith(MISFinGoldenTicket.MISFIN))
		{
			if (gtselect == MISFinGoldenTicket.MISFIN_STITCHFIN)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(2));
			}
			else if (gtselect == MISFinGoldenTicket.MISFIN_SHEETFIN)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(1));
			}
			else if (gtselect == MISFinGoldenTicket.MISFIN_SHEETFIN)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(1));
			}
			else if (gtselect == MISFinGoldenTicket.MISFIN_BOXMAKING)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(1));
			}
			else if (gtselect == MISFinGoldenTicket.MISFIN_HARDCOVERFIN)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(5));
			}
			else if (gtselect == MISFinGoldenTicket.MISFIN_SOFTCOVERFIN)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(3));
			}
			else if (gtselect == MISFinGoldenTicket.MISFIN_INSERTFIN)
			{
				theGT = new MISFinGoldenTicket(gt1, jdfVersion, jmf, mis, BaseGoldenTicket.createSheetMap(1));
			}
			((MISFinGoldenTicket) theGT).setCategory(gtselect);
		}
		else if ("IDP".equals(gtselect))
		{
			theGT = new IDPGoldenTicket(gt1, jdfVersion);
		}
		else if ("ODP".equals(gtselect))
		{
			theGT = new ODPGoldenTicket(gt1, jdfVersion);
		}
		else if ("DPW".equals(gtselect))
		{
			theGT = new WideFormatGoldenTicket(gt1, jdfVersion);
		}
		assignGT(theGT);
		return theGT;
	}

	protected void assignGT(BaseGoldenTicket theGT)
	{
		if (theGT != null)
		{
			theGT.assign(null);
			int iType = getGtTypeSelected();
			if (iType >= 1)
				theGT.makeReadyAll();
			if (iType >= 2)
				theGT.executeAll(null);
		}
	}

	/**
	 * Creates the fields and view for the Merge Dialog.
	 */
	private void init()
	{
		final JPanel panel = new JPanel();
		outConstraints.fill = GridBagConstraints.BOTH;
		outConstraints.insets = new Insets(0, 0, 10, 0);
		outLayout.setConstraints(panel, outConstraints);
		setLayout(outLayout);
		final GridBagLayout inLayout = new GridBagLayout();
		panel.setLayout(new GridLayout(6, 1));
		panel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GTInputKey")));

		//Golden Ticket Chooser
		JPanel gtChooser = new JPanel();
		gtChooser.setLayout(inLayout);
		gtChooser.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GTFileKey")));
		outLayout.setConstraints(gtChooser, outConstraints);

		initGTLabels(panel, gtChooser);
		initGTType(panel);

		//MIS Level
		JPanel MISLevel = new JPanel();
		MISLevel.setLayout(inLayout);
		MISLevel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("MISLevelKey")));
		outLayout.setConstraints(MISLevel, outConstraints);

		misICSLevel1 = new JComboBox<String>(l2);
		misICSLevel1.addActionListener(this);

		MISLevel.add(misICSLevel1);
		panel.add(MISLevel);

		//JMF Level
		JPanel JMFLevel = new JPanel();
		JMFLevel.setLayout(inLayout);
		JMFLevel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("JMFLevelKey")));
		outLayout.setConstraints(JMFLevel, outConstraints);

		jmfICSLevel1 = new JComboBox<String>(l1);
		jmfICSLevel1.addActionListener(this);

		JMFLevel.add(jmfICSLevel1);
		panel.add(JMFLevel);

		//GT Level
		JPanel GTLevel = new JPanel();
		GTLevel.setLayout(inLayout);
		GTLevel.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GTLevelKey")));
		outLayout.setConstraints(GTLevel, outConstraints);

		gtICSLevel = new JComboBox<String>(l2);
		gtICSLevel.addActionListener(this);

		GTLevel.add(gtICSLevel);
		panel.add(GTLevel);

		//GT Level
		JPanel GTVersion = new JPanel();
		GTVersion.setLayout(inLayout);
		GTVersion.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GTVersionKey")));
		outLayout.setConstraints(GTVersion, outConstraints);

		String[] vs = new String[4];
		vs[0] = EnumVersion.Version_1_3.getName();
		vs[1] = EnumVersion.Version_1_4.getName();
		vs[2] = EnumVersion.Version_1_5.getName();
		vs[3] = EnumVersion.Version_2_0.getName();
		gtVersion = new JComboBox<String>(vs);
		gtVersion.addActionListener(this);
		gtVersion.setSelectedIndex(2);

		GTVersion.add(gtVersion);
		panel.add(GTVersion);

		add(panel);
		setVisible(true);
	}

	private void initGTType(JPanel panel)
	{
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("GTTypeKey")));
		gtType = new JComboBox<String>();

		String[] vs = new String[3];
		vs[0] = ResourceUtil.getMessage("GTTypeMIS");
		vs[1] = ResourceUtil.getMessage("GTTypeWorkflow");
		vs[2] = ResourceUtil.getMessage("GTTypeDevice");
		gtType = new JComboBox<String>(vs);
		gtType.addActionListener(this);
		gtType.setSelectedIndex(0);
		p.add(gtType);
		panel.add(p);
	}

	private void initGTLabels(final JPanel panel, JPanel gtChooser)
	{
		//Add Golden Tickets as they become available.
		final String[] gt = { "MISCP", MISPreGoldenTicket.MISPRE_CONTENTCREATION, MISPreGoldenTicket.MISPRE_IMPOSITIONPREPARATION, MISPreGoldenTicket.MISPRE_IMPOSITIONRIPING,
				MISPreGoldenTicket.MISPRE_PLATEMAKING, MISPreGoldenTicket.MISPRE_PLATESETTING, MISPreGoldenTicket.MISPRE_PREPRESSPREPARATION, "IDP", "ODP", "DPW",
				MISFinGoldenTicket.MISFIN_SHEETFIN, MISFinGoldenTicket.MISFIN_STITCHFIN, MISFinGoldenTicket.MISFIN_BOXMAKING, MISFinGoldenTicket.MISFIN_SOFTCOVERFIN,
				MISFinGoldenTicket.MISFIN_HARDCOVERFIN };
		String[] gtDisplay = new String[gt.length];
		labelMap = new HashMap<String, String>();
		for (int i = 0; i < gt.length; i++)
		{
			gtDisplay[i] = ResourceUtil.getMessage(gt[i]);
			labelMap.put(gtDisplay[i], gt[i]);
		}
		goldenTicketLabel = new JComboBox(gtDisplay);
		goldenTicketLabel.addActionListener(this);

		gtChooser.add(goldenTicketLabel);
		panel.add(gtChooser);
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		final Object source = e.getSource();
		JDFFrame parent = MainView.getFrame();
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
		else if (source == gtVersion)
		{
			gtVersionSelected = EnumVersion.getEnum((String) gtVersion.getSelectedItem());
		}
		else if (source == gtICSLevel)
		{
			getGTLevelSelected();
		}
		else if (source == gtType)
		{
			getGTTypeSelected();
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

	private void getGTTypeSelected()
	{
		gtTypeSelected = gtType.getSelectedIndex();
	}

	/**
	 * Methods to return the Levels and which golden ticket to create.
	 * @return 
	 */
	private String getGoldenTicketName()
	{
		return gtSelected;
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public int getMISLevel()
	{
		return misSelectLevel;
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public int getJMFLevel()
	{
		return jmfSelectLevel;
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public int getGTLevel()
	{
		return gtSelectLevel;
	}

	/**
	 * 
	 * @return
	 */
	public EnumVersion getGtVersionSelected()
	{
		return gtVersionSelected;
	}

}
