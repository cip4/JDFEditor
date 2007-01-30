package org.cip4.jdfeditor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.util.JDFMerge;

/*
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
    private ResourceBundle littleBundle;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private JDFFrame parFrame;
    
    /**
     * Constructor of MergeDialog
     * creates MergeDialog and executes mergeJDF()
     * result saves in File 'file'
     * All throwen and not catched in MergeDialog exeptions will be catched in JDFFrame.merge()
     * @param parent - parent JDFFrame
     * @param bundle - internationalisation strings for dialog description
     * @param jdfRoot - JDFNode to merge into 
     */
    public MergeDialog(final JDFFrame parent, final ResourceBundle bundle, final JDFNode jdfRoot)
    {
        super();
        this.littleBundle = bundle;
        this.parFrame = parent;
        final XMLDoc originalDoc = jdfRoot.getOwnerDocument_KElement();
        this.file=new File(originalDoc.getOriginalFileName());
        
        init();
        
        final String[] options = { littleBundle.getString("OkKey"), littleBundle.getString("CancelKey") };
        
        final int option = JOptionPane.showOptionDialog(parent, this, "Merge",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (option == JOptionPane.OK_OPTION)
        {
            File tmpFile = new File(idPath.getText());
            
            if(EditorUtils.fileOK(tmpFile))
            {
                idFile=tmpFile;
                final JDFParser parser = new JDFParser();
                final JDFDoc spawnedIDDoc = parser.parseFile(idFile.getAbsolutePath());
                final JDFNode spawnedIDRoot = (JDFNode) spawnedIDDoc.getRoot();
                
                boolean successful=true;
                try
                {
                    new JDFMerge(jdfRoot).mergeJDF(spawnedIDRoot, idFile.toURI().toString(), JDFNode.EnumCleanUpMerge.None, JDFResource.EnumAmountMerge.None);
                }
                catch (JDFException ex)
                {
                    successful=false;
                }
                
                if (successful)
                {
                    jdfRoot.eraseEmptyNodes(true);
                    successful = originalDoc.write2File(null, 0, true);
                    
                    if (successful)
                    {
                        final JPanel panel = new JPanel();
                        panel.setBorder(BorderFactory.createTitledBorder(
                                littleBundle.getString("MergingCompletedKey")));
                        final JLabel label = new JLabel(
                                littleBundle.getString("MessageOpenMergedFileKey"));
                        panel.add(label);
                        
                        JOptionPane.showMessageDialog(this,
                                label, littleBundle.getString("MergingCompletedKey"), 
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else 
                    {
                        JOptionPane.showMessageDialog(parent, littleBundle.getString("MergingFailedKey"),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        
                        file=null;
                    }        
                }
                else 
                {
                    JOptionPane.showMessageDialog(parent, littleBundle.getString("MergingFailedKey"),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    file=null;
                }
                
            }
            else 
            {
                JOptionPane.showMessageDialog(parent, littleBundle.getString("MergingAcceptFileFailedKey"), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                file=null;
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
        constraints.insets = new Insets(3,5,3,5);
        setBorder(BorderFactory.createTitledBorder(littleBundle.getString("SpawnedInputKey")));
        
        final JLabel mergeLabel = new JLabel(createPathName(littleBundle.getString("SpawnedInputKey").length()));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(mergeLabel, constraints);
        add(mergeLabel);
        
        final JLabel idLabel = new JLabel(littleBundle.getString("SpawnedJDFKey"));
        constraints.insets = new Insets(10,5,3,5);
        layout.setConstraints(idLabel, constraints);
        add(idLabel);
        
        final Box idBox = Box.createHorizontalBox();
        idPath = new JTextField(35);
        idBox.add(idPath);
        idBox.add(Box.createHorizontalStrut(10));
        
        browse = new JButton(littleBundle.getString("BrowseKey"));
        browse.setPreferredSize(new Dimension(85,22));
        browse.addActionListener(this);
        idBox.add(browse);
        
        constraints.insets = new Insets(0,5,8,5);
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
     * Creates the String which is to be displayed...
     * @param length - The length of the title...
     * @return The file name, may be a little bit altered.
     */
    private String createPathName(int length)
    {
        final String s = '"' + file.getAbsolutePath() + '"';
        
        if (s.length() <= 1.5 * length)
            return s;
        
        final int i = s.indexOf('\\');
        final int j = s.lastIndexOf('\\');
        
        if (i == j)
            return s.substring(0, length - 4) + "..." + '"';
        
        final String start = s.substring(0, i + 1);
        final String end = s.substring(j, s.length());
        
        return start + "..." + end;
    }
    
     
    /**
     * actionPerformed method for realization of "Browse" button 
     * method opens new Dialog window "Browse" and 
     * allows user to choose a spawned file. must be either .jdf file or .xml file
     * sets field idPath to the selected file path.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == browse)
        {
            final EditorFileChooser files = new EditorFileChooser(file,"xml jdf");
            final int option = files.showOpenDialog(parFrame);
            
            if (option == JFileChooser.APPROVE_OPTION)
            {
                idPath.setText(files.getSelectedFile().getAbsolutePath());
            }
            else if (option == JFileChooser.ERROR_OPTION) 
            {
                JOptionPane.showMessageDialog(parFrame, littleBundle.getString("MergingAcceptFileFailedKey"), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
