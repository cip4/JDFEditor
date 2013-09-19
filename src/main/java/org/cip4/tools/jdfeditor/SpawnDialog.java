package org.cip4.tools.jdfeditor;

/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2006 The International Cooperation for the Integration of
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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.util.JDFSpawn;

/**
 * spawnDialog.java
 * @author Elena Skobchenko
 */

public class SpawnDialog extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -267165456151780440L;

	private JTextField idPath, rootPath;
	private JButton browse1, browse2;
	File originalFile;

	File newPartFile;

	File newRootFile;

	private final JDFNode selectedJDF, originalJDF;

	private JDFNode spawnedPartJDF;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private final JDFFrame parFrame;
	boolean bOK = false;

	/**
	 * 
	 * @param selectJDF
	 * @param spawnInformative
	 */
	public SpawnDialog(final JDFNode selectJDF, boolean spawnInformative)
	{
		super();
		this.selectedJDF = selectJDF;
		this.originalFile = new File(selectJDF.getOwnerDocument_KElement().getOriginalFileName());
		this.parFrame = Editor.getFrame();

		final XMLDoc originalDoc = selectJDF.getOwnerDocument_KElement();
		this.originalJDF = (JDFNode) originalDoc.getRoot();

		init();

		final String[] options = { Editor.getString("OkKey"), Editor.getString("CancelKey") };

		final int option = JOptionPane.showOptionDialog(parFrame, this, "Spawn", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			bOK = true;
			String path = idPath.getText();
			if (path == null || path.equals(JDFConstants.EMPTYSTRING))
				path = "SpawnedJDF.jdf";

			String rPath = rootPath.getText();
			if (rPath == null || rPath.equals(JDFConstants.EMPTYSTRING))
				rPath = "MainJDF.jdf";

			newPartFile = new File(path);
			newRootFile = new File(rPath);

			if (newPartFile == null || newRootFile == null)
			{
				JOptionPane.showMessageDialog(parFrame, Editor.getString("SpawningFailedKey"), "Error", JOptionPane.ERROR_MESSAGE);
				bOK = false;
			}
			else
			{
				try
				{
					if (spawnInformative)
					{
						final JDFSpawn _spawn = new JDFSpawn(selectedJDF);
						spawnedPartJDF = _spawn.spawnInformative(originalFile.toURI().toString(), newPartFile.toURI().toString(), null, false, true, true, true);
					}
					else
					{
						final JDFSpawn spawn = new JDFSpawn(selectedJDF);
						spawnedPartJDF = spawn.spawn(originalFile.toURI().toString(), newPartFile.toURI().toString(), null, null, false, true, true, true);
					}
					spawnedPartJDF.eraseEmptyNodes(true);
					final XMLDoc outDoc_part1 = spawnedPartJDF.getOwnerDocument_KElement();
					outDoc_part1.write2File(newPartFile.getAbsolutePath(), 0, true);

					originalJDF.eraseEmptyNodes(true);
					originalDoc.write2File(newRootFile.getAbsolutePath(), 0, true);

				}
				catch (Exception e)
				{
					bOK = false;
					e.printStackTrace();
					JOptionPane.showMessageDialog(parFrame, "An internal error occured: \n" + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), "Error", JOptionPane.ERROR_MESSAGE);
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
		setBorder(BorderFactory.createTitledBorder(Editor.getString("SpawnedOutputKey")));

		final JLabel mergeLabel = new JLabel(EditorUtils.displayPathName(originalFile, Editor.getString("SpawnedOutputKey").length()));
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(mergeLabel, constraints);
		add(mergeLabel);

		final JLabel idLabel = new JLabel(Editor.getString("SpawnedJDFKey"));
		constraints.insets = new Insets(10, 5, 3, 5);
		layout.setConstraints(idLabel, constraints);
		add(idLabel);

		final Box idBox = Box.createHorizontalBox();

		newPartFile = new File(createFileName(".ID_" + selectedJDF.getID()));
		final String absolutePath = newPartFile.getAbsolutePath();
		int col = absolutePath.length() < 35 ? absolutePath.length() : 35;
		idPath = new JTextField(absolutePath, col);

		idBox.add(idPath);
		idBox.add(Box.createHorizontalStrut(10));

		browse1 = new JButton(Editor.getString("BrowseKey"));
		browse1.setPreferredSize(new Dimension(85, 22));
		browse1.addActionListener(this);
		idBox.add(browse1);

		constraints.insets = new Insets(0, 5, 8, 5);
		layout.setConstraints(idBox, constraints);
		add(idBox);

		final JLabel rLabel = new JLabel(Editor.getString("MainJDFKey"));
		constraints.insets = new Insets(10, 5, 3, 5);
		layout.setConstraints(rLabel, constraints);
		add(rLabel);

		final Box idBox2 = Box.createHorizontalBox();

		newRootFile = new File(createFileName(".ID_" + originalJDF.getID()));
		final String newRootAbsolutePath = newRootFile.getAbsolutePath();
		int col2 = newRootAbsolutePath.length() < 35 ? newRootAbsolutePath.length() : 35;
		rootPath = new JTextField(newRootAbsolutePath, col2);

		idBox2.add(rootPath);
		idBox2.add(Box.createHorizontalStrut(10));

		browse2 = new JButton(Editor.getString("BrowseKey"));
		browse2.setPreferredSize(new Dimension(85, 22));
		browse2.addActionListener(this);
		idBox2.add(browse2);

		constraints.insets = new Insets(0, 5, 8, 5);
		layout.setConstraints(idBox2, constraints);
		add(idBox2);

		setVisible(true);

	}

	/**
	 * Create the default file name including its absolute path. The String addOn
	 * is added just ahead of the file's extension.
	 * @param addOn - The String to add to the original file name.
	 * @return The file name with the addon.
	 */
	private String createFileName(String addOn)
	{
		int index = originalFile.getAbsolutePath().lastIndexOf('\\');
		String path = originalFile.getAbsolutePath().substring(0, index + 1);

		index = originalFile.getName().lastIndexOf('.');
		String name = originalFile.getName().substring(0, index);
		String extension = originalFile.getName().substring(index, originalFile.getName().length());

		return path + name + addOn + extension;
	}

	/**
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == browse1 || e.getSource() == browse2)
		{
			File file = (e.getSource() == browse1) ? newPartFile : newRootFile;

			final EditorFileChooser files = new EditorFileChooser(file, "xml jdf");
			final int option = files.showOpenDialog(parFrame);

			if (option == JFileChooser.APPROVE_OPTION)
			{
				if (e.getSource() == browse1)
				{
					newPartFile = files.getSelectedFile();
					idPath.setText(files.getSelectedFile().getAbsolutePath());
				}
				else if (e.getSource() == browse2)
				{
					newRootFile = files.getSelectedFile();
					rootPath.setText(files.getSelectedFile().getAbsolutePath());
				}
			}
			else if (option == JFileChooser.ERROR_OPTION)
			{
				JOptionPane.showMessageDialog(parFrame, "Spawn failed", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
