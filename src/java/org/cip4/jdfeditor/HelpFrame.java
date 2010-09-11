package org.cip4.jdfeditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * @author ThunellE AnderssonA
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class HelpFrame extends JFrame
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -7797849179421833321L;
    private static HelpFrame theHelp=null;
    ResourceBundle littleBundle;
    private JLabel help1;
    private JLabel help2;
    private JLabel help3;
    private JLabel help4;
    private JLabel help5;
    JLabel [] helpLabels = { help1, help2, help3, help4, help5 };
    private JSplitPane helpPane;
    private JScrollPane rightScroll;
    private JLabel rightLabel;
    private String[] titles = {
            "Element",
            "JDF Element",
            "RefElement",
            "Element with Error",
            "Attribute",
            "Inherited Attribute",
            "PartIDKey Attribute",
            "Inherited PartIDKey Attribute",
    "Attriubte with Error" };

    ImageIcon imgUp  = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "UpButton.gif");
    ImageIcon imgVal = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "RevalidateButton.gif");

    private ImageIcon[] icons =
    {
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ElemIcon.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "JDFElemIcon.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefElemIcon.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ErrorElemIcon.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "AttIconSelected.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "InhAttIconSelected.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "PartIDKeysAttIconSelected.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "InhPartIDKeysAttIconSelected.gif"),
            Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ErrorAttIconSelected.gif")
    };

    String startTags = "<html><font size=2 color=black face=verdana> ";
    String startTagsLarge = "<html><font size=3 color=black face=verdana><b> ";
    String startTagsSmall = "<html><font size=2 color=black face=verdana><b> ";
    String startTagsSmallSel = "<html><font size=2 color=#6EC8F0 face=verdana><b> <u>";
    String endTags = "</b></font></html>";
    String endTag = "</font></html>";

    /**
     * Constructor for HelpFrame.
     * @deprecated Replaced by JavaHelp.
     */
    public HelpFrame()
    {
        super();
        this.littleBundle = Editor.getBundle();
        this.setTitle(littleBundle.getString("HelpCIP4Key"));
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(d.width / 3, 0, d.width * 2 / 3, d.height - 30);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final WindowListener winLis = new WindowAdapter()
        {
            @Override
			public void windowClosing(WindowEvent e)
            {
                dispose();
                e.getID(); // make compile happy
            }
        };
        this.addWindowListener(winLis);
        this.toFront();
        init();
        theHelp=this;
    }
    /**
     * Method init.
     * 
     */
    private void init()
    {
        final Dimension d = new Dimension(this.getSize());

        final JPanel leftPanel = drawLeftPanel();
        leftPanel.setBackground(Color.white);
        final JPanel rightPanel = drawRightPanel();
        rightPanel.setBackground(Color.white);

        final JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBackground(Color.white);
        leftScroll.getVerticalScrollBar().setUnitIncrement(20);
        leftScroll.getHorizontalScrollBar().setUnitIncrement(20);

        final JLabel leftLabel = new JLabel(startTagsLarge + littleBundle.getString("ContentsKey") + endTags);
        leftLabel.setBackground(Color.white);
        leftLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        leftScroll.setColumnHeaderView(leftLabel);

        rightScroll = new JScrollPane(rightPanel);
        rightScroll.setBackground(Color.white);
        rightScroll.getVerticalScrollBar().setUnitIncrement(20);
        rightScroll.getHorizontalScrollBar().setUnitIncrement(20);

        rightLabel = new JLabel(startTagsLarge + endTags);
        rightLabel.setBackground(Color.white);
        rightLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        rightScroll.setColumnHeaderView(rightLabel);

        helpPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
        helpPane.setDividerLocation(d.width / 3);
        getContentPane().add(helpPane);
    }
    /**
     * Method drawLeftPanel.
     * @return JPanel
     */
    private JPanel drawLeftPanel()
    {
        final int x = 10;
        int y = 10;
        int w =  0;
        final String[] labelStrings = {
                littleBundle.getString("GettingStartedKey"),
                littleBundle.getString("ViewKey"),
                littleBundle.getString("EditKey"),
                littleBundle.getString("ValidateCertKey"),
                littleBundle.getString("DevelopmentNotesKey")};

        final JPanel leftPanel = new JPanel(null);
        final Dimension d = new Dimension(this.getSize());
        leftPanel.setBounds(0, 0, d.width / 3, d.height);
        leftPanel.setBackground(Color.white);

        for (int i = 0; i < helpLabels.length; i++)
        {
            helpLabels[i] = createDefaultLabel(startTagsSmall + labelStrings[i] + endTags);
            helpLabels[i].setBorder(BorderFactory.createEmptyBorder());
            helpLabels[i].setToolTipText(labelStrings[i]);
            helpLabels[i].addMouseListener(new LabelMouseListener());

            final Dimension dim = helpLabels[i].getPreferredSize();

            helpLabels[i].setBounds(x, y, dim.width, dim.height);
            leftPanel.add(helpLabels[i]);

            y += dim.height + 5;
            w = w < dim.width ? dim.width : w;
        }
        leftPanel.setPreferredSize(new Dimension(w + 2 * x, y));

        return leftPanel;
    }
    /**
     * Method drawRightPanel.
     * @return JPanel
     */
    private JPanel drawRightPanel()
    {
        final JPanel rp = new JPanel(null);

        final JLabel textLabel = createDefaultLabel(startTags + littleBundle.getString("RightView") + endTags);

        final Dimension d = textLabel.getPreferredSize();

        textLabel.setBounds(20, 20, d.width, d.height);
        rp.add(textLabel);
        rp.setPreferredSize(new Dimension(d.width + 40, d.height + 40));

        return rp;
    }

    private JLabel createDefaultLabel(String text)
    {
        final JLabel label = new JLabel(text, SwingConstants.LEFT);

        label.setVerticalTextPosition(SwingConstants.TOP);
        label.setBackground(Color.white);
        label.setOpaque(true);

        return label;
    }
    /**
     * Method helpGettingStarted.
     */
    void helpGettingStarted()
    {
        final JPanel rp = new JPanel(null);

        final JLabel startLabel = createDefaultLabel(startTags + littleBundle.getString("GettingStarted") + endTags);

        final Dimension d = startLabel.getPreferredSize();

        startLabel.setBounds(20, 20, d.width, d.height);
        rp.add(startLabel);
        rp.setBackground(Color.white);
        rp.setPreferredSize(new Dimension(d.width + 40, d.height + 40));
        rightScroll.getViewport().setView(rp);

        rightLabel.setText(startTagsLarge + littleBundle.getString("GettingStartedKey") + endTags);
    }
    /**
     * Method helpViews.
     */
    void helpViews()
    {
        final int x = 20;
        int y = 20;
        int w =  0;

        final JPanel rp = new JPanel(null);

        final JLabel treeLabel = createDefaultLabel(startTags + littleBundle.getString("TreeView") + endTags);

        Dimension d = treeLabel.getPreferredSize();

        treeLabel.setBounds(x, y, d.width, d.height);
        rp.add(treeLabel);
        w = w < d.width ? d.width : w;

        y += d.height;

        for(int i = 0; i < titles.length; i++)
        {
            final JLabel label = drawIconLabel(titles[i], icons[i]);        
            d = label.getPreferredSize();

            label.setBounds(50, y, d.width, d.height);
            rp.add(label);
            w = w < d.width ? d.width : w;

            y += d.height + 5;
        }

        final JLabel errorLabel = createDefaultLabel(startTags + littleBundle.getString("ErrorView") + endTags);

        d = errorLabel.getPreferredSize();

        errorLabel.setBounds(x, y, d.width, d.height);
        rp.add(errorLabel);
        w = w < d.width ? d.width : w;

        y += d.height;

        final JLabel inOutLabel = createDefaultLabel(startTags + littleBundle.getString("InOutView") + endTags);

        d = inOutLabel.getPreferredSize();

        inOutLabel.setBounds(x, y, d.width, d.height);
        rp.add(inOutLabel);
        w = w < d.width ? d.width : w;

        y += d.height;

        final JLabel procLabel = createDefaultLabel(startTags + littleBundle.getString("ProcessView") + endTags);

        d = procLabel.getPreferredSize();

        procLabel.setBounds(x, y, d.width, d.height);
        rp.add(procLabel);
        w = w < d.width ? d.width : w;

        y += d.height;

        final JLabel procIcon = new JLabel(startTagsSmall + littleBundle.getString("GoUpInProcessViewKey") + endTags,
                imgUp, SwingConstants.LEFT);
        procIcon.setHorizontalTextPosition(SwingConstants.LEFT);
        procIcon.setBackground(Color.white);
        procIcon.setOpaque(true);

        d = procIcon.getPreferredSize();

        procIcon.setBounds(x, y, d.width, d.height);
        rp.add(procIcon);
        w = w < d.width ? d.width : w;

        y += d.height;

        final JLabel comLabel = createDefaultLabel(startTags + littleBundle.getString("CommentView") + endTags);

        d = comLabel.getPreferredSize();

        comLabel.setBounds(x, y, d.width, d.height);
        rp.add(comLabel);
        w = w < d.width ? d.width : w;

        y += d.height;

        rp.setBackground(Color.white);
        rp.setPreferredSize(new Dimension(w + 2 * x, y + 10));

        rightScroll.getViewport().setView(rp);

        rightLabel.setText(startTagsLarge + littleBundle.getString("ViewKey") + endTags);
    }
    /**
     * Method drawIconLabel
     * @param title
     * @param icon
     * @return
     */
    private JLabel drawIconLabel(String title, ImageIcon icon)
    {
        final String s = startTags + title + endTag;
        final JLabel iLabel = new JLabel(s, icon, SwingConstants.LEFT);
        iLabel.setIconTextGap(10);
        iLabel.setBackground(Color.white);
        iLabel.setOpaque(true);

        return iLabel;
    }
    /**
     * Method helpEditing.
     */
    void helpEditing()
    {
        final JPanel rp = new JPanel(null);

        final JLabel editLabel = createDefaultLabel(startTags + littleBundle.getString("EditView") + endTags);

        final Dimension d = editLabel.getPreferredSize();

        editLabel.setBounds(20, 20, d.width, d.height);
        rp.add(editLabel);
        rp.setBackground(Color.white);
        rp.setPreferredSize(new Dimension(d.width + 40, d.height + 40));
        rightScroll.getViewport().setView(rp);

        rightLabel.setText(startTagsLarge + littleBundle.getString("EditKey") + endTags);
    }
    /**
     * Method helpValidation. Also where you talk about certification.
     */
    void helpValidation()
    {
        final int x = 20;
        int y = 20;
        int w =  0;

        final JPanel rp = new JPanel(null);

        final JLabel validLabel = createDefaultLabel(startTags + littleBundle.getString("ValidationView") + endTags);

        Dimension d = validLabel.getPreferredSize();

        validLabel.setBounds(x, y, d.width, d.height);
        rp.add(validLabel);
        w = w < d.width ? d.width : w;

        y += d.height;

        final JLabel validIcon = new JLabel(startTagsSmall + littleBundle.getString("ValidateToolTipKey") + endTags,
                imgVal, SwingConstants.LEFT);
        validIcon.setHorizontalTextPosition(SwingConstants.LEFT);
        validIcon.setBackground(Color.white);
        validIcon.setOpaque(true);

        d = validIcon.getPreferredSize();

        validIcon.setBounds(x, y, d.width, d.height);
        rp.add(validIcon);
        w = w < d.width ? d.width : w;

        y += d.height;

        rp.setBackground(Color.white);
        rp.setPreferredSize(new Dimension(w + 2 * x, y + 10));
        rightScroll.getViewport().setView(rp);

        
        //Certification help

        final JLabel certLabel = createDefaultLabel(startTags + littleBundle.getString("CertificationView") + endTags);
        
        Dimension c = certLabel.getPreferredSize();

        certLabel.setBounds(x, y, c.width, c.height);
        rp.add(certLabel);
        w = w < c.width ? c.width : w;

        y += c.height;

        rightLabel.setText(startTagsLarge + littleBundle.getString("ValidateCertKey") + endTags);
    }
    /**
     * Method helpFurtherDevelopment.
     */
    void helpFurtherDevelopment()
    {
        final JPanel rp = new JPanel(null);

        final JLabel devLabel = createDefaultLabel(startTags + littleBundle.getString("FurtherDevView") + endTags);

        final Dimension d = devLabel.getPreferredSize();

        devLabel.setBounds(20, 20, d.width, d.height);
        rp.add(devLabel);
        rp.setBackground(Color.white);
        rp.setPreferredSize(new Dimension(d.width + 40, d.height + 40));
        rightScroll.getViewport().setView(rp);

        rightLabel.setText(startTagsLarge + littleBundle.getString("DevelopmentNotesKey") + endTags);
    }
    /**
     * @author ThunellE
     *
     * To change this generated comment edit the template variable "typecomment":
     * Window>Preferences>Java>Templates.
     * To enable and disable the creation of type comments go to
     * Window>Preferences>Java>Code Generation.
     */
    class LabelMouseListener extends MouseAdapter
    {
        @Override
		public void mouseClicked(MouseEvent e)
        {
            if (e.getSource() == helpLabels[0])
            {
                helpLabels[0].setText(startTagsSmall + littleBundle.getString("GettingStartedKey") + endTags);
                helpGettingStarted();
            }
            else if (e.getSource() == helpLabels[1])
            {
                helpLabels[1].setText(startTagsSmall + littleBundle.getString("ViewKey") + endTags);
                helpViews();
            }
            else if (e.getSource() == helpLabels[2])
            {
                helpLabels[2].setText(startTagsSmall + littleBundle.getString("EditKey") + endTags);
                helpEditing();
            }
            else if (e.getSource() == helpLabels[3])
            {
            	//make "ValidateKey" = Validate and Certification
                helpLabels[3].setText(startTagsSmall + littleBundle.getString("ValidateCertKey") + endTags);
                helpValidation();
            }
            else if (e.getSource() == helpLabels[4])
            {
                helpLabels[4].setText(startTagsSmall + littleBundle.getString("DevelopmentNotesKey") + endTags);
                helpFurtherDevelopment();
            }
        }
        @Override
		public void mouseEntered(MouseEvent e)
        {
            Editor.setCursor(2,(Component)e.getSource());
            if (e.getSource() == helpLabels[0])
            {
                helpLabels[0].setText(startTagsSmallSel + littleBundle.getString("GettingStartedKey") + endTags);
            }
            else if (e.getSource() == helpLabels[1])
            {
                helpLabels[1].setText(startTagsSmallSel + littleBundle.getString("ViewKey") + endTags);
            }
            else if (e.getSource() == helpLabels[2])
            {
                helpLabels[2].setText(startTagsSmallSel + littleBundle.getString("EditKey") + endTags);
            }
            else if (e.getSource() == helpLabels[3])
            {
                helpLabels[3].setText(startTagsSmallSel + littleBundle.getString("ValidateCertKey") + endTags);
            }
            else if (e.getSource() == helpLabels[4])
            {
                helpLabels[4].setText(startTagsSmallSel + littleBundle.getString("DevelopmentNotesKey") + endTags);
            }
        }
        @Override
		public void mouseExited(MouseEvent e)
        {
            Editor.setCursor(0,(Component)e.getSource());

            if (e.getSource() == helpLabels[0])
            {
                helpLabels[0].setText(startTagsSmall + littleBundle.getString("GettingStartedKey") + endTags);
            }
            else if (e.getSource() == helpLabels[1])
            {
                helpLabels[1].setText(startTagsSmall + littleBundle.getString("ViewKey") + endTags);
            }
            else if (e.getSource() == helpLabels[2])
            {
                helpLabels[2].setText(startTagsSmall + littleBundle.getString("EditKey") + endTags);
            }
            else if (e.getSource() == helpLabels[3])
            {
                helpLabels[3].setText(startTagsSmall + littleBundle.getString("ValidateCertKey") + endTags);
            }
            else if (e.getSource() == helpLabels[4])
            {
                helpLabels[4].setText(startTagsSmall + littleBundle.getString("DevelopmentNotesKey") + endTags);
            }
        }
    }
    /**
     * @return
     * @deprecated Replaced by JavaHelp.
     */
    public static HelpFrame getHelp()
    {
       if(theHelp==null)
           theHelp=new HelpFrame();
       return theHelp;
    }
}
