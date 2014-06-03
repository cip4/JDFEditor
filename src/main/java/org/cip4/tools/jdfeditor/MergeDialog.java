package org.cip4.tools.jdfeditor;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.util.JDFMerge;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.util.ResourceBundleUtil;
import org.cip4.tools.jdfeditor.view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * MergeDialog.java
 * @author Elena Skobchenko
 */

public class MergeDialog extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -267165456151780040L;

	private JTextField idPath;
	private JButton browse;
	private File file;
	private File idFile;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private final JDFFrame parFrame;

	/**
	 * Constructor of MergeDialog
	 * creates MergeDialog and executes mergeJDF()
	 * result saves in File 'file'
	 * All thrown and not catched in MergeDialog exeptions will be catched in JDFFrame.merge()
	 * @param jdfRoot - JDFNode to merge into 
	 */
	public MergeDialog(final JDFNode jdfRoot)
	{
		super();
		this.parFrame = MainView.getFrame();
		final XMLDoc originalDoc = jdfRoot.getOwnerDocument_KElement();
		this.file = new File(originalDoc.getOriginalFileName());

		init();

		final String[] options = { ResourceBundleUtil.getMessage("OkKey"), ResourceBundleUtil.getMessage("CancelKey") };

		final int option = JOptionPane.showOptionDialog(parFrame, this, "Merge", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			File tmpFile = new File(idPath.getText());

			if (UrlUtil.isFileOK(tmpFile))
			{
				idFile = tmpFile;
				final JDFParser parser = new JDFParser();
				final JDFDoc spawnedIDDoc = parser.parseFile(idFile.getAbsolutePath());
				final JDFNode spawnedIDRoot = (JDFNode) spawnedIDDoc.getRoot();

				boolean successful = true;
				try
				{
					new JDFMerge(jdfRoot).mergeJDF(spawnedIDRoot, idFile.toURI().toString(), JDFNode.EnumCleanUpMerge.None, JDFResource.EnumAmountMerge.None);
				}
				catch (JDFException ex)
				{
					successful = false;
				}

				if (successful)
				{
					jdfRoot.eraseEmptyNodes(true);
					successful = originalDoc.write2File((String) null, 0, true);

					if (successful)
					{
						final JPanel panel = new JPanel();
						panel.setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("MergingCompletedKey")));
						final JLabel label = new JLabel(ResourceBundleUtil.getMessage("MessageOpenMergedFileKey"));
						panel.add(label);

						JOptionPane.showMessageDialog(this, label, ResourceBundleUtil.getMessage("MergingCompletedKey"), JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(parFrame, ResourceBundleUtil.getMessage("MergingFailedKey"), "Error", JOptionPane.ERROR_MESSAGE);

						file = null;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(parFrame, ResourceBundleUtil.getMessage("MergingFailedKey"), "Error", JOptionPane.ERROR_MESSAGE);
					file = null;
				}

			}
			else
			{
				JOptionPane.showMessageDialog(parFrame, ResourceBundleUtil.getMessage("MergingAcceptFileFailedKey"), "Error", JOptionPane.ERROR_MESSAGE);
				file = null;
			}

		}

	}

	/**
	 * Creates the fields and view for the Merge Dialog.
	 */
	private void init()
	{
		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3, 5, 3, 5);
		setBorder(BorderFactory.createTitledBorder(ResourceBundleUtil.getMessage("SpawnedInputKey")));

		final JLabel mergeLabel = new JLabel(EditorUtils.displayPathName(file, ResourceBundleUtil.getMessage("SpawnedInputKey").length()));
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(mergeLabel, constraints);
		add(mergeLabel);

		final JLabel idLabel = new JLabel(ResourceBundleUtil.getMessage("SpawnedJDFKey"));
		constraints.insets = new Insets(10, 5, 3, 5);
		layout.setConstraints(idLabel, constraints);
		add(idLabel);

		final Box idBox = Box.createHorizontalBox();
		idPath = new JTextField(35);
		idBox.add(idPath);
		idBox.add(Box.createHorizontalStrut(10));

		browse = new JButton(ResourceBundleUtil.getMessage("BrowseKey"));
		browse.setPreferredSize(new Dimension(85, 22));
		browse.addActionListener(this);
		idBox.add(browse);

		constraints.insets = new Insets(0, 5, 8, 5);
		layout.setConstraints(idBox, constraints);
		add(idBox);

		setVisible(true);

	}

	/**
	 * return merged reuslt file
	 * @return result file after merging
	 */
	public File getFileToSave()
	{
		return file;
	}

	/**
	 * actionPerformed method for realization of "Browse" button 
	 * method opens new Dialog window "Browse" and 
	 * allows user to choose a spawned file. must be either .jdf file or .xml file
	 * sets field idPath to the selected file path.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == browse)
		{
			final EditorFileChooser files = new EditorFileChooser(file, "xml jdf");
			final int option = files.showOpenDialog(parFrame);

			if (option == JFileChooser.APPROVE_OPTION)
			{
				idPath.setText(files.getSelectedFile().getAbsolutePath());
			}
			else if (option == JFileChooser.ERROR_OPTION)
			{
				JOptionPane.showMessageDialog(parFrame, ResourceBundleUtil.getMessage("MergingAcceptFileFailedKey"), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
