package org.cip4.tools.jdfeditor;
import javax.swing.*;
import java.awt.*;

/**
 * @author AnderssA ThunellE SvenoniusI
 * The renderer used for the JTrees in the In & Output View
 * only overwrite the difference to the default tree renderer
 * 
 */
public class JDFResourceCellRenderer extends JDFTreeCellRenderer
{
    /**
     * 
     */


    private boolean highlightFN;

    private static final long serialVersionUID = 1L;
    // TODO see what can be reused from JDFTreeRenderer
    private Color colorFoc = new Color(110, 200, 240);
    
    public JDFResourceCellRenderer()
    {
        super();
        // colorSel = new Color(200, 200, 200);

        final INIReader iniFile = Editor.getIniFile();

        highlightFN = iniFile.getHighlight();
    }
    
    @Override
	public Component getTreeCellRendererComponent(JTree jdfTree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean _hasFocus)
    {
        
        super.getTreeCellRendererComponent(jdfTree,value,sel,expanded,leaf,row,_hasFocus);
        JDFTreeNode treeNode=null;
        Color background = hasFocus ? colorFoc : Color.white;
        setBackground(background);
        if(value instanceof JDFTreeNode)
        {
            treeNode=(JDFTreeNode)value;
            if (treeNode.hasForeignNS() && highlightFN)
            {
                final JDFTreeModel mod=(JDFTreeModel) jdfTree.getModel();
                if(mod.isValid(treeNode))  
                    setForeground(Color.blue);
        }    }    
        
        return this;
    }
}