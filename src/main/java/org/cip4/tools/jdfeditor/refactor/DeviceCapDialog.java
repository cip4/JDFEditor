package org.cip4.tools.jdfeditor.refactor;

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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement.EnumValidationLevel;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.datatypes.JDFBaseDataTypes.EnumFitsValue;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFResponse;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFDevice;
import org.cip4.jdflib.resource.devicecapability.JDFDeviceCap;
import org.cip4.jdflib.util.EnumUtil;
import org.cip4.jdflib.util.UrlUtil;

/**
 * DeviceCapsDialog.java
 * @author Elena Skobchenko
 */

public class DeviceCapDialog extends JPanel implements ActionListener
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
	private VElement executableJDF = null;
	private final boolean ignoreDefaults;
	private final boolean ignoreExtensions;
	//    private boolean breport;
	private XMLDoc bugReport = null;

	private JComboBox chooseValidLevel;
	EnumFitsValue testlists = EnumFitsValue.Allowed;
	private EnumValidationLevel validationLevel = EnumValidationLevel.RecursiveComplete;

	/**
	 * 
	 * @param doc
	 */
	public DeviceCapDialog(final JDFDoc doc)
	{
		super();
		final INIReader iniFile = Editor.getIniFile();
		JDFFrame parent = Editor.getFrame();
		KElement docRoot = doc.getRoot();

		idFile = iniFile.getRecentDevCap();
		ignoreDefaults = iniFile.getIgnoreDefault();
		ignoreExtensions = !Editor.getIniFile().getHighlight();

		init();
		final String[] options = { Editor.getString("OkKey"), Editor.getString("CancelKey") };

		final int option = JOptionPane.showOptionDialog(parent, this, "Test against DeviceCapabilities file", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			File tmpFile = new File(idPath.getText());
			if (UrlUtil.isFileOK(tmpFile))
			{
				idFile = tmpFile;
				final JDFParser parser = new JDFParser();
				iniFile.setRecentDevCap(idFile);

				try
				{
					final JDFDoc devCapDoc = parser.parseFile(idFile.getAbsolutePath());
					JDFJMF jmfRoot = null;
					if (devCapDoc != null)
					{
						jmfRoot = devCapDoc.getJMFRoot();
						if (jmfRoot == null)
						{
							JOptionPane.showMessageDialog(parent, "Chosen file is not a JMF file", "Error", JOptionPane.ERROR_MESSAGE);
						}
						else if (jmfRoot.getChildByTagName(ElementName.DEVICECAP, null, 0, null, false, true) == null
								&& jmfRoot.getChildByTagName(ElementName.MESSAGESERVICE, null, 0, null, false, true) == null)
						{
							JOptionPane.showMessageDialog(parent, "File does not contain Device Capability description", "Error", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							Editor.setCursor(1, null);
							if (docRoot instanceof JDFNode)
							{
								final JDFDevice device = (JDFDevice) jmfRoot.getXPathElement("Response/DeviceList/DeviceInfo/Device");
								if (device == null)
								{
									JOptionPane.showMessageDialog(parent, "DevCap File does not contain JDF Node Device Capability description", "Error", JOptionPane.ERROR_MESSAGE);
								}
								else
								{
									VElement vDC = device.getChildElementVector(ElementName.DEVICECAP, null, null, true, -1, false);
									for (int i = 0; i < vDC.size(); i++)
									{
										JDFDeviceCap deviceCap = (JDFDeviceCap) vDC.elementAt(i);
										deviceCap.setIgnoreExtensions(ignoreExtensions);
										deviceCap.setIgnoreDefaults(ignoreDefaults);
									}
									executableJDF = device.getExecutableJDF((JDFNode) docRoot, testlists, validationLevel);
									bugReport = device.getBadJDFInfo((JDFNode) docRoot, testlists, validationLevel);
								}

								if (bugReport != (null))
								{
									//There is a bug report to write
									//Output BugReport to file. Location is the location of the DevCap file.
									String dcReport = null;
									final String dcr = doc.getOriginalFileName();
									dcReport = UrlUtil.newExtension(dcr, ".JDFDevCapReport.xml");
									bugReport.write2File(dcReport, 2, true);
								}
							}
							else if (docRoot instanceof JDFJMF)
							{
								KElement ms = jmfRoot.getChildWithAttribute(ElementName.MESSAGESERVICE, AttributeName.TYPE, null, null, 0, false);
								if (ms == null)
								{
									JOptionPane.showMessageDialog(parent, "DevCap File does not contain JMF Device Capability description", "Error", JOptionPane.ERROR_MESSAGE);
								}
								else
								{
									final JDFResponse respKnownMessages = (JDFResponse) ms.getParentNode_KElement();
									executableJDF = null;
									bugReport = JDFDeviceCap.getJMFInfo((JDFJMF) docRoot, respKnownMessages, testlists, validationLevel, !Editor.getIniFile().getHighlight());

								}
								if (bugReport != (null))
								{
									//There is a bug report to write
									//Output BugReport to file. Location is the location of the DevCap file.
									String dcReport = null;
									final String dcr = doc.getOriginalFileName();
									dcReport = UrlUtil.newExtension(dcr, ".JMFDevCapReport.xml");
									bugReport.write2File(dcReport, 2, true);
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(parent, "An internal error occured: \n" + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(parent, "File is not accepted", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		Editor.setCursor(0, null);
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
		panel.setLayout(inLayout);

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3, 5, 3, 5);
		panel.setBorder(BorderFactory.createTitledBorder(Editor.getString("DCInputKey")));

		final JLabel dLabel = new JLabel(EditorUtils.displayPathName(idFile, Editor.getString("DCInputKey").length()));
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		inLayout.setConstraints(dLabel, constraints);
		panel.add(dLabel);

		final JLabel idLabel = new JLabel(Editor.getString("DCFileKey"));
		constraints.insets = new Insets(10, 5, 3, 5);
		inLayout.setConstraints(idLabel, constraints);
		panel.add(idLabel);

		final Box horizontalBox = Box.createHorizontalBox();
		idPath = new JTextField(35);
		if (idFile != null)
			idPath.setText(idFile.getAbsolutePath());

		horizontalBox.add(idPath);
		horizontalBox.add(Box.createHorizontalStrut(10));

		browse = new JButton(Editor.getString("BrowseKey"));
		browse.setPreferredSize(new Dimension(85, 22));
		browse.addActionListener(this);
		horizontalBox.add(browse);

		constraints.insets = new Insets(0, 5, 8, 5);
		inLayout.setConstraints(horizontalBox, constraints);
		panel.add(horizontalBox);

		add(panel);
		final JPanel downPanel = new JPanel();
		outConstraints.gridwidth = GridBagConstraints.REMAINDER;
		downPanel.setLayout(outLayout);

		JPanel testLists = new JPanel();
		testLists.setLayout(inLayout);

		testLists.setBorder(BorderFactory.createTitledBorder(Editor.getString("DCTestListsKey")));

		//        final Box verticalBox = Box.createVerticalBox();
		final ButtonGroup group = new ButtonGroup();

		JRadioButton allowedButton;
		JRadioButton presentButton;
		group.add(allowedButton = new JRadioButton("Allowed"));
		allowedButton.setSelected(true);
		allowedButton.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
					testlists = EnumFitsValue.Allowed;
			}
		});
		//        verticalBox.add(allowedButton);
		testLists.add(allowedButton);

		group.add(presentButton = new JRadioButton("Present"));
		presentButton.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
					testlists = EnumFitsValue.Present;
			}
		});
		//        verticalBox.add(presentButton);
		testLists.add(presentButton);

		//        testLists.add(verticalBox);
		outLayout.setConstraints(testLists, outConstraints);
		downPanel.add(testLists);

		final JPanel validationPanel = new JPanel();
		validationPanel.setBorder(BorderFactory.createTitledBorder(Editor.getString("ValidationLevelKey")));

		final VString allowedValues = EnumUtil.getNamesVector(EnumValidationLevel.class);
		allowedValues.removeElementAt(0);
		chooseValidLevel = new JComboBox(allowedValues);
		chooseValidLevel.setSelectedItem(EnumValidationLevel.RecursiveComplete.getName());
		chooseValidLevel.addActionListener(this);
		validationPanel.add(chooseValidLevel);
		outLayout.setConstraints(validationPanel, outConstraints);

		downPanel.add(validationPanel);

		add(downPanel);
		setVisible(true);
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
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
		else if (source == chooseValidLevel)
		{
			validationLevel = EnumValidationLevel.getEnum((String) chooseValidLevel.getSelectedItem());
		}
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public VElement getExecutable()
	{
		return executableJDF;
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public XMLDoc getBugReport()
	{
		return bugReport;
	}
}
