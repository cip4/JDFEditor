package org.cip4.tools.jdfeditor;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.validate.JDFValidator;

import javax.swing.*;

/**
 * @author Elena Skobchenko
 *
 * TBD Refactoring (s.meissner)
 */

public class DCOutputCellRenderer extends JDFTreeCellRenderer
{
    private static final long serialVersionUID = 626128726824503000L;
    
    
    public DCOutputCellRenderer()
    {
        super();
    }
    
    @Override
	protected void setNodeIcon(JTree jdfTree,JDFTreeNode treeNode){
        final INIReader iniFile=Editor.getIniFile();

        final String nodeName=treeNode.getName();
        KElement elem = treeNode.getElement();
        
        if(treeNode.isElement()){
            if(elem.getAttribute("Value",null,"").equals("true")){
                setIcon(loadImageIcon(ICON_NODE_DEFAULT));
            }
            else if(elem.getAttribute("Value",null,"").equals("false")){
                setIcon(loadImageIcon(ICON_NODE_ERR));
            }
            else if(elem.getAttribute("Severity",null,"").equals("Error")){
                setIcon(loadImageIcon(ICON_NODE_ERR));
            }
            String tts=JDFValidator.toMessageString(elem);
            if(tts!=null)
                setToolTipText(tts);
        }
        else
        {
            setIcon(loadImageIcon(ICON_ATTR_DEFAULT));
            
        }
        if (nodeName.equals("RejectedNode")||nodeName.equals("RejectedChildNode"))
        {

            setIcon(loadImageIcon(ICON_NODE_ERR));
        }
        else if (nodeName.equals("InvalidAttribute")||nodeName.equals("InvalidSpan")||nodeName.equals("InvalidComment"))
        {
            setIcon(loadImageIcon(ICON_ATTR_ERR));
        }
        else if (nodeName.equals("InvalidSubelement")||nodeName.equals("InvalidResource")||nodeName.equals("InvalidPartitionLeaf"))
        {
            setIcon(loadImageIcon(ICON_NODE_ERR));
        }
        else if (nodeName.equals("UnknownSubelement"))
        {
            setIcon(loadImageIcon(ICON_NODE_ERR));
        }
        else if (nodeName.equals("MissingAttribute")||nodeName.equals("MissingSpan"))
        {
            setIcon(loadImageIcon(ICON_ATTR_ERR));
        }
        else if (nodeName.equals("UnknownAttribute")||nodeName.equals("UnknownSpan")||nodeName.equals("SyntaxWarning"))
        {
            setIcon(loadImageIcon(ICON_ATTR_ERR));
        }
        else if (nodeName.equals("MissingSubelement")||nodeName.equals("MissingElement")||nodeName.equals("MissingResourceLink")
                ||nodeName.equals("MissingCustomerInfo")||nodeName.equals("MissingNodeInfo"))
        {
            setIcon(loadImageIcon(ICON_NODE_ERR));
        }
        else if (nodeName.equals("MissingResources")|| nodeName.equals("MissingElements")|| nodeName.equals("InvalidResources")
                ||nodeName.equals("UnknownResources"))
        {
            setIcon(loadImageIcon(ICON_NODE_ERR));
        }
        else if (nodeName.endsWith("Report"))
        {
            if(elem.hasChildElement("ExecutableNodes",null)||"true".equals(elem.getAttribute("IsValid")))
            {
                setIcon(loadImageIcon(ICON_NODE_DEFAULT));
                                
            }
            else
            {
                setIcon(loadImageIcon(ICON_NODE_ERR));
            }
        }
        else if (nodeName.equals("ExecutableNodes"))
        {
            setIcon(loadImageIcon(ICON_NODE_DEFAULT));
        }        
        else if (elem instanceof JDFNode)
        {
            setIcon(loadImageIcon(ICON_NODE_JDF));
        }        
    }
}

