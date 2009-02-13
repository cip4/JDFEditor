package org.cip4.jdfeditor;
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
    
    private JComboBox goldenTicketLevel;
    private JComboBox MISICSLevel1;
    private JComboBox JMFICSLevel1;
    private JComboBox gtICSLevel1;
    
    private String gtSelected = "MIS to Conventional Printing ICS";
    private String misSelected = "1";
    private String jmfSelected = "1";
    private String gtLevelSelected = "1";
    
	private int MISSelectLevel = 1;
	private int JMFSelectLevel = 1;
	private int GTSelectLevel = 1;

    
	public String[] l1 = { "1", "2" };
	public String[] l2 = { "1", "2", "3" };
    

	public GoldenTicketDialog()
    {
        super();
        JDFFrame parent=Editor.getFrame();
        final ResourceBundle littleBundle=Editor.getBundle();

        
        init();
        final String[] options = { littleBundle.getString("OkKey"), littleBundle.getString("CancelKey") };
        
        final int option = JOptionPane.showOptionDialog(parent, this, "Golden Ticket File Creation",
            JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            
        if (option == JOptionPane.OK_OPTION)//will create the Golden Ticket
        {
        	/*String tmpGTfile = gtSelected;*/
        	String tmpMISfile = misSelected;
        	String tmpJMFfile = jmfSelected;
        	String tmpGTlevel = gtLevelSelected;
        	
        	MISSelectLevel = Integer.parseInt(tmpMISfile);
        	JMFSelectLevel = Integer.parseInt(tmpJMFfile);
        	GTSelectLevel = Integer.parseInt(tmpGTlevel);
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
        final ResourceBundle littleBundle=Editor.getBundle();
        outConstraints.fill = GridBagConstraints.BOTH;
        outConstraints.insets = new Insets(0,0,10,0);
        outLayout.setConstraints(panel, outConstraints);
        setLayout(outLayout);
        final GridBagLayout inLayout = new GridBagLayout();    	   	
        panel.setLayout(new GridLayout(4,1));
        panel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GTInputKey")));
        
        //Golden Ticket Chooser
        JPanel gtChooser = new JPanel();
        gtChooser.setLayout(inLayout);
        gtChooser.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GTFileKey")));
        outLayout.setConstraints(gtChooser, outConstraints);
        
        //Add Golden Tickets as they become available.
        final String[] gt = { "MIS to Conventional Printing ICS", "MIS to Prepress ICS" };
        goldenTicketLevel = new JComboBox(gt);
        goldenTicketLevel.addActionListener(this);
        
        gtChooser.add(goldenTicketLevel);
        panel.add(gtChooser);
        
        //MIS Level
        JPanel MISLevel = new JPanel();
        MISLevel.setLayout(inLayout);
        MISLevel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("MISLevelKey")));
        outLayout.setConstraints(MISLevel, outConstraints);
        
        MISICSLevel1 = new JComboBox(l2);
        MISICSLevel1.addActionListener(this);
        
        MISLevel.add(MISICSLevel1);
        panel.add(MISLevel);
      
      //JMF Level
        JPanel JMFLevel = new JPanel();
        JMFLevel.setLayout(inLayout);
        JMFLevel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("JMFLevelKey")));
        outLayout.setConstraints(JMFLevel, outConstraints);
        
        JMFICSLevel1 = new JComboBox(l1);
        JMFICSLevel1.addActionListener(this);
        
        JMFLevel.add(JMFICSLevel1);
        panel.add(JMFLevel);
      
      //GT Level
        JPanel GTLevel = new JPanel();
        GTLevel.setLayout(inLayout);
        GTLevel.setBorder(BorderFactory.createTitledBorder(littleBundle.getString("GTLevelKey")));
        outLayout.setConstraints(GTLevel, outConstraints);
        
        gtICSLevel1 = new JComboBox(l1);
        gtICSLevel1.addActionListener(this);
        
        GTLevel.add(gtICSLevel1);
        panel.add(GTLevel);
        
        add(panel);
        setVisible(true);
    }
    
         
    public void actionPerformed(ActionEvent e)
    {
        final Object source = e.getSource();
        JDFFrame parent=Editor.getFrame();
        if (source == browse)
        {
            final EditorFileChooser files = new EditorFileChooser(idFile,"xml jdf");
            final int option = files.showOpenDialog(parent);
            
            if (option == JFileChooser.APPROVE_OPTION)
            {
                idPath.setText(files.getSelectedFile().getAbsolutePath());
            }
            else if (option == JFileChooser.ERROR_OPTION) 
            {
                JOptionPane.showMessageDialog(parent, "File is not accepted", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (source == goldenTicketLevel)
        {
        	gtSelected = (String) goldenTicketLevel.getSelectedItem();
        }
        else if (source == MISICSLevel1)
        {
        	misSelected = (String) MISICSLevel1.getSelectedItem();
        }
        else if (source == JMFICSLevel1)
        {
        	jmfSelected = (String) JMFICSLevel1.getSelectedItem();
        }
        else if (source == gtICSLevel1)
        {
        	gtLevelSelected = (String) gtICSLevel1.getSelectedItem();
        }
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
        return MISSelectLevel;
    }
    
    public int getJMFLevel() 
    {
        return JMFSelectLevel;
    }
    
    public int getGTLevel() 
    {
        return GTSelectLevel;
    }
}
