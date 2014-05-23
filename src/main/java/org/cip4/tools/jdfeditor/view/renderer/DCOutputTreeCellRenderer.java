package org.cip4.tools.jdfeditor.view.renderer;
import org.apache.log4j.Logger;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.validate.JDFValidator;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.INIReader;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.JDFTreeNode;

/**
 * @author Elena Skobchenko
 */

public class DCOutputTreeCellRenderer extends AbstractTreeCellRenderer
{
    private static final Logger LOGGER = Logger.getLogger(DCOutputTreeCellRenderer.class);

    private static final long serialVersionUID = 626128726824503000L;

    /**
     * Default constructor.
     */
    public DCOutputTreeCellRenderer()
    {
        super();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void setNodeIcon(JDFTreeNode node, JDFTreeModel model) {

        final INIReader iniFile= Editor.getIniFile();

        final String nodeName=node.getName();
        KElement elem = node.getElement();
        
        if(node.isElement()){
            if(elem.getAttribute("Value",null,"").equals("true")){
                setIcon(loadImageIcon(TreeIcon.NODE_DEFAULT));
            }
            else if(elem.getAttribute("Value",null,"").equals("false")){
                setIcon(loadImageIcon(TreeIcon.NODE_ERR));
            }
            else if(elem.getAttribute("Severity",null,"").equals("Error")){
                setIcon(loadImageIcon(TreeIcon.NODE_ERR));
            }
            String tts=JDFValidator.toMessageString(elem);
            if(tts!=null)
                setToolTipText(tts);
        }
        else
        {
            setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
            
        }
        if (nodeName.equals("RejectedNode")||nodeName.equals("RejectedChildNode"))
        {

            setIcon(loadImageIcon(TreeIcon.NODE_ERR));
        }
        else if (nodeName.equals("InvalidAttribute")||nodeName.equals("InvalidSpan")||nodeName.equals("InvalidComment"))
        {
            setIcon(loadImageIcon(TreeIcon.ATTR_ERR));
        }
        else if (nodeName.equals("InvalidSubelement")||nodeName.equals("InvalidResource")||nodeName.equals("InvalidPartitionLeaf"))
        {
            setIcon(loadImageIcon(TreeIcon.NODE_ERR));
        }
        else if (nodeName.equals("UnknownSubelement"))
        {
            setIcon(loadImageIcon(TreeIcon.NODE_ERR));
        }
        else if (nodeName.equals("MissingAttribute")||nodeName.equals("MissingSpan"))
        {
            setIcon(loadImageIcon(TreeIcon.ATTR_ERR));
        }
        else if (nodeName.equals("UnknownAttribute")||nodeName.equals("UnknownSpan")||nodeName.equals("SyntaxWarning"))
        {
            setIcon(loadImageIcon(TreeIcon.ATTR_ERR));
        }
        else if (nodeName.equals("MissingSubelement")||nodeName.equals("MissingElement")||nodeName.equals("MissingResourceLink")
                ||nodeName.equals("MissingCustomerInfo")||nodeName.equals("MissingNodeInfo"))
        {
            setIcon(loadImageIcon(TreeIcon.NODE_ERR));
        }
        else if (nodeName.equals("MissingResources")|| nodeName.equals("MissingElements")|| nodeName.equals("InvalidResources")
                ||nodeName.equals("UnknownResources"))
        {
            setIcon(loadImageIcon(TreeIcon.NODE_ERR));
        }
        else if (nodeName.endsWith("Report"))
        {
            if(elem.hasChildElement("ExecutableNodes",null)||"true".equals(elem.getAttribute("IsValid")))
            {
                setIcon(loadImageIcon(TreeIcon.NODE_DEFAULT));
                                
            }
            else
            {
                setIcon(loadImageIcon(TreeIcon.NODE_ERR));
            }
        }
        else if (nodeName.equals("ExecutableNodes"))
        {
            setIcon(loadImageIcon(TreeIcon.NODE_DEFAULT));
        }        
        else if (elem instanceof JDFNode)
        {
            setIcon(loadImageIcon(TreeIcon.NODE_JDF));
        }        
    }
}

