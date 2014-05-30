package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.validate.JDFValidator;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.JDFTreeNode;

public class SchemaOutputTreeCellRenderer extends AbstractTreeCellRenderer {

    private static final Logger LOGGER = LogManager.getLogger(SchemaOutputTreeCellRenderer.class);

    private static final long serialVersionUID = 6261287268245030123L;

    /**
     * Default constructor.
     */
    public SchemaOutputTreeCellRenderer() {
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void setNodeIcon(JDFTreeNode node, JDFTreeModel model) {

        String n=node.getName();
        if(node.isElement())
        {
            KElement elem = node.getElement();
            String tts=JDFValidator.toMessageString(elem);
            if(tts!=null)
               setToolTipText(tts);
            
            if (n.equals("Error"))
            {
                setIcon(loadImageIcon(TreeIcon.NODE_ERR));
            }
            else if (n.equals("SchemaValidationOutput"))
            {
                if(elem!=null && !elem.getAttribute("ValidationResult",null,"").equals("Valid"))
                {
                    setIcon(loadImageIcon(TreeIcon.NODE_ERR));
                }
                else
                {
                    setIcon(loadImageIcon(TreeIcon.NODE_JDF));
                }
                
                if(elem.getAttribute("Message")!=null)
                    setToolTipText(elem.getAttribute("Message"));
                
            }

        }
        else // attributes
        {
            setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
        }
    }       
    
}

