/*
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
 *    Alternately, this acknowledgment mrSubRefay appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior writtenrestartProcesses()
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
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIrSubRefAL DAMAGES (INCLUDING, BUT NOT
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
 * originally based on software restartProcesses()
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG
 * copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *
 */
package org.cip4.tools.jdfeditor;

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * spawnDialog.java
 * @author Elena Skobchenko
 */

public class ExportDialog extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -267165456151780440L;

    private SettingService settingService = new SettingService();

	private JTextField idPath;
	private JTextField generAttrField;
	String generAttrString;
	private JButton browse;
	private final File originalFile;
	private File newDCFile;
	private File fileToOpen;
	private GridBagLayout layout;
	private GridBagConstraints constraints;

	/**
	 * 
	 * @param jdfRoot
	 */
	public ExportDialog(final JDFNode jdfRoot)
	{
		super();
		this.originalFile = new File(jdfRoot.getOwnerDocument_KElement().getOriginalFileName());
		generAttrString = settingService.getSetting(SettingKey.VALIDATION_GENERIC_ATTR, String.class);
		JDFFrame frame = MainView.getFrame();
		init();
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };

		final int option = JOptionPane.showOptionDialog(frame, this, ResourceUtil.getMessage("ExportToDevCapKey"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			String path = idPath.getText();
			if (path == null || path.equals(JDFConstants.EMPTYSTRING))
			{
				path = "Gen_DevCaps_" + jdfRoot.getID() + ".jdf";
			}

			newDCFile = new File(path);

			if (newDCFile == null)
			{
				JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage("ExportFailedKey"), "Error creating " + path, JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				try
				{
					generAttrString = generAttrField.getText();
					final VString genericAttributes = new VString(generAttrString, null);
					genericAttributes.unify();

					final JDFDeviceCapGenerator devCapGenerator = new JDFDeviceCapGenerator(jdfRoot, genericAttributes);
					final JDFDoc devCapDoc = devCapGenerator.getDevCapDoc();

					boolean success = devCapDoc.write2File(newDCFile.getAbsolutePath(), 2, true);
					//                    boolean success = devCapDoc.write2File(newDCFile.getAbsolutePath(), 2, false);                   
					if (!success)
					{
						JOptionPane.showMessageDialog(frame, ResourceUtil.getMessage("ExportFailedKey"), "Error", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						setOpenFileDialog();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "An internal error occured: \n" + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * Creates the fields and view for the Spawn Dialog and also the default
	 * file names for the jdfFile and partFile.
	 */
	private void init()
	{
		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3, 5, 3, 5);
		setBorder(BorderFactory.createTitledBorder(ResourceUtil.getMessage("DevCapChooseKey")));

		final JLabel mergeLabel = new JLabel(EditorUtils.displayPathName(originalFile, ResourceUtil.getMessage("DevCapChooseKey").length()));
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(mergeLabel, constraints);
		add(mergeLabel);

		final JLabel idLabel = new JLabel(ResourceUtil.getMessage("DevCapOutputFileKey"));
		constraints.insets = new Insets(10, 5, 3, 5);
		layout.setConstraints(idLabel, constraints);
		add(idLabel);

		final Box idBox = Box.createHorizontalBox();

		newDCFile = new File(createFileName("Generated_DevCaps_"));
		int col = newDCFile.getName().length() < 35 ? newDCFile.getName().length() : 35;
		idPath = new JTextField(newDCFile.getAbsolutePath(), col);

		idBox.add(idPath);
		idBox.add(Box.createHorizontalStrut(10));

		browse = new JButton(ResourceUtil.getMessage("BrowseKey"));
		browse.setPreferredSize(new Dimension(85, 22));
		browse.addActionListener(this);
		idBox.add(browse);

		constraints.insets = new Insets(0, 5, 8, 5);
		layout.setConstraints(idBox, constraints);
		add(idBox);

		final JLabel rLabel = new JLabel(ResourceUtil.getMessage("DevCapGenericAttrKey"));
		constraints.insets = new Insets(10, 5, 3, 5);
		layout.setConstraints(rLabel, constraints);
		add(rLabel);

		generAttrField = new JTextField(generAttrString, col + 15);
		generAttrField.setEditable(true);
		constraints.insets = new Insets(0, 5, 8, 5);
		layout.setConstraints(generAttrField, constraints);
		add(generAttrField);

		setVisible(true);

	}

	private void setOpenFileDialog()
	{
		final JLabel label = new JLabel(ResourceUtil.getMessage("DCOpenAfterGenerationKey"));
		final String[] options = { ResourceUtil.getMessage("YesKey"), ResourceUtil.getMessage("NoKey") };

		final int option = JOptionPane.showOptionDialog(MainView.getFrame(), label, ResourceUtil.getMessage("DCHappyMessageKey"), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			fileToOpen = newDCFile;
		}
	}

	/**
	 * 
	 *  
	 * @return
	 */
	public File getFileToOpen()
	{
		return fileToOpen;
	}

	/**
	 * Create the default file name including its absolute path. The String addOn
	 * is added just ahead of the file's extension.
	 * @return The file name with the addon.
	 */
	private String createFileName(String addBefore)
	{
		int index = originalFile.getAbsolutePath().lastIndexOf('\\');
		final String path = originalFile.getAbsolutePath().substring(0, index + 1);

		index = originalFile.getName().lastIndexOf('.');
		final String name = originalFile.getName().substring(0, index);
		final String extension = originalFile.getName().substring(index, originalFile.getName().length());

		return path + addBefore + name + extension;
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == browse)
		{
			final EditorFileChooser files = new EditorFileChooser(newDCFile, "xml jdf jmf");
			final int option = files.showOpenDialog(MainView.getFrame());

			if (option == JFileChooser.APPROVE_OPTION)
			{
				newDCFile = files.getSelectedFile();
				idPath.setText(files.getSelectedFile().getAbsolutePath());
			}
			else if (option == JFileChooser.ERROR_OPTION)
			{
				EditorUtils.errorBox("ExportFailedKey", null);
			}
		}
	}
}
