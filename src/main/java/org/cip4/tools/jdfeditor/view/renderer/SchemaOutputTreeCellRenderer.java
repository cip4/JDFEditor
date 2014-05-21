package org.cip4.tools.jdfeditor.view.renderer;

import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.validate.JDFValidator;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.INIReader;
import org.cip4.tools.jdfeditor.JDFTreeNode;

import javax.swing.*;

/**
 * @author Elena Skobchenko
 */

public class SchemaOutputTreeCellRenderer extends JDFTreeCellRenderer
{
    private static final long serialVersionUID = 6261287268245030123L;
    
    
    public SchemaOutputTreeCellRenderer()

    {
        super();
    }
    
    @Override
	protected void setNodeIcon(JTree jdfTree,JDFTreeNode treeNode){
        String n=treeNode.getName();
        INIReader iniFile= Editor.getIniFile();
        if(treeNode.isElement())
        {
            KElement elem = treeNode.getElement();
            String tts=JDFValidator.toMessageString(elem);
            if(tts!=null)
               setToolTipText(tts);
            
            if (n.equals("Error"))
            {
                setIcon(loadImageIcon(ICON_NODE_ERR));
            }
            else if (n.equals("SchemaValidationOutput"))
            {
                if(elem!=null && !elem.getAttribute("ValidationResult",null,"").equals("Valid"))
                {
                    setIcon(loadImageIcon(ICON_NODE_ERR));
                }
                else
                {
                    setIcon(loadImageIcon(ICON_NODE_JDF));
                }
                
                if(elem.getAttribute("Message")!=null)
                    setToolTipText(elem.getAttribute("Message"));
                
            }

        }
        else // attributes
        {
            setIcon(loadImageIcon(ICON_ATTR_DEFAULT));
        }
    }       
    
}

