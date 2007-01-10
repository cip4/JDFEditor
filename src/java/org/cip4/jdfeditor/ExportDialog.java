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

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.node.JDFNode;

/**
 * spawnDialog.java
 * @author Elena Skobchenko
 */

public class ExportDialog extends JPanel implements ActionListener
{
    private static final long serialVersionUID = -267165456151780440L;
    private JTextField idPath; 
    private JTextField generAttrField;
    public String generAttrString;
    private JButton browse; 
    private File originalFile;
    private File newDCFile;
    private File fileToOpen; 
    private GridBagLayout layout;
    private GridBagConstraints constraints;
        
    public ExportDialog(final JDFNode jdfRoot)
    {
        super();
        this.originalFile = new File(jdfRoot.getOwnerDocument_KElement().getOriginalFileName());
        generAttrString = Editor.getIniFile().getGenericAtts();      
        JDFFrame frame=Editor.getFrame();
        init();
        ResourceBundle littleBundle=Editor.getBundle();        
        final String[] options = { littleBundle.getString("OkKey"), littleBundle.getString("CancelKey") };
        
        final int option = JOptionPane.showOptionDialog(frame, this, littleBundle.getString("ExportToDevCapKey"),
            JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (option == JOptionPane.OK_OPTION)
        {
            String path = idPath.getText();
            if (path==null || path.equals(JDFConstants.EMPTYSTRING))
            {
                path = "Gen_DevCaps_" + jdfRoot.getID() + ".jdf";
            }
            
            newDCFile = new File(path);
            
            if (newDCFile == null) 
            {
                JOptionPane.showMessageDialog(frame, littleBundle.getString("ExportFailedKey"),
                                                "Error creating "+path, JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    generAttrString =generAttrField.getText();
                    final VString genericAttributes = new VString(generAttrString,null);
                    genericAttributes.unify();
                    
                    final JDFDeviceCapGenerator devCapGenerator = new JDFDeviceCapGenerator(jdfRoot,genericAttributes);
                    final JDFDoc devCapDoc = devCapGenerator.getDevCapDoc();
                    
                    boolean success = devCapDoc.write2File(newDCFile.getAbsolutePath(), 2, true);
//                    boolean success = devCapDoc.write2File(newDCFile.getAbsolutePath(), 2, false);                   
                    if (!success)
                    {
                        JOptionPane.showMessageDialog(frame, littleBundle.getString("ExportFailedKey"),
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else 
                    {
                        setOpenFileDialog();
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "An internal error occured: \n" + e.getClass() + " \n"
                            + (e.getMessage()!=null ? ("\"" + e.getMessage() + "\"") : ""), 
                            "Error", JOptionPane.ERROR_MESSAGE);
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
        ResourceBundle littleBundle=Editor.getBundle();        
        layout = new GridBagLayout(); 
        setLayout(layout);
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(3,5,3,5);
        setBorder(BorderFactory.createTitledBorder(littleBundle.getString("DevCapChooseKey")));
        
        final JLabel mergeLabel = new JLabel(createPathName(littleBundle.getString("DevCapChooseKey").length()));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(mergeLabel, constraints);
        add(mergeLabel);
       
        final JLabel idLabel = new JLabel(littleBundle.getString("DevCapOutputFileKey"));
        constraints.insets = new Insets(10,5,3,5);
        layout.setConstraints(idLabel, constraints);
        add(idLabel);
  
        final Box idBox = Box.createHorizontalBox();
        
        newDCFile = new File(createFileName("Generated_DevCaps_"));
        int col = newDCFile.getName().length() < 35 ? newDCFile.getName().length() : 35;
        idPath = new JTextField(newDCFile.getAbsolutePath(), col);
        
        idBox.add(idPath);
        idBox.add(Box.createHorizontalStrut(10));
        
        browse = new JButton(littleBundle.getString("BrowseKey"));
        browse.setPreferredSize(new Dimension(85,22));
        browse.addActionListener(this);
        idBox.add(browse);
        
        constraints.insets = new Insets(0,5,8,5);
        layout.setConstraints(idBox, constraints);
        add(idBox);
        
        
        final JLabel rLabel = new JLabel(littleBundle.getString("DevCapGenericAttrKey"));
        constraints.insets = new Insets(10,5,3,5);
        layout.setConstraints(rLabel, constraints);
        add(rLabel);
        
        generAttrField = new JTextField(generAttrString, col+ 15);
        generAttrField.setEditable(true);
        constraints.insets = new Insets(0,5,8,5);
        layout.setConstraints(generAttrField, constraints);
        add(generAttrField);
        
        setVisible(true);
 
    }
    
    
    private void setOpenFileDialog()
    {
        ResourceBundle littleBundle=Editor.getBundle();        

        final JLabel label = new JLabel(littleBundle.getString("DCOpenAfterGenerationKey"));

        final String[] options = { littleBundle.getString("YesKey"), littleBundle.getString("NoKey") };
        
        final int option = JOptionPane.showOptionDialog(Editor.getFrame(), label, littleBundle.getString("DCHappyMessageKey"),
            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (option == JOptionPane.OK_OPTION)
        {
            fileToOpen = newDCFile;
        }
        
    }
    
    public File getFileToOpen()
    {
        return fileToOpen;
    }
    
    /**
     * Create the default file name including its absolute path. The String addOn
     * is added just ahead of the file's extension.
     * @param addOn - The String to add to the original file name.
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
     * Creates the String which is to be displayed...
     * @param length - The length of the title...
     * @return The file name, may be a little bit altered.
     */
    private String createPathName(int length)
    {
        final String s = '"' + originalFile.getAbsolutePath() + '"';
        
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
    
    
  
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == browse)
        {
            final EditorFileChooser files = new EditorFileChooser(newDCFile,"xml jdf jmf");
            final int option = files.showOpenDialog(Editor.getFrame());
            
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
