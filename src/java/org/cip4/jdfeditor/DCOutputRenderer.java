package org.cip4.jdfeditor;
import javax.swing.JTree;

import org.cip4.jdflib.CheckJDF;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;

/**
 * @author Elena Skobchenko
 */

public class DCOutputRenderer extends JDFTreeRenderer 
{
    private static final long serialVersionUID = 626128726824503000L;
    
    
    public DCOutputRenderer()
    {
        super();
    }
    
    protected void setNodeIcon(JTree jdfTree,boolean sel, JDFTreeNode treeNode){
        final INIReader iniFile=Editor.getIniFile();

        final String nodeName=treeNode.getName();
        KElement elem = treeNode.getElement();
        
        if(treeNode.isElement()){
            if(elem.getAttribute("Value",null,"").equals("true")){
                if (sel)
                    setIcon(iniFile.elemIconS);
                else
                    setIcon(iniFile.elemIcon);           
            }
            else if(elem.getAttribute("Value",null,"").equals("false")){
                if (sel)
                    setIcon(iniFile.errElemIconS);
                else
                    setIcon(iniFile.errElemIcon);           
            }
            else if(elem.getAttribute("Severity",null,"").equals("Error")){
                if (sel)
                    setIcon(iniFile.errElemIconS);
                else
                    setIcon(iniFile.errElemIcon);           
            }
            String tts=CheckJDF.toMessageString(elem);
            if(tts!=null)
                setToolTipText(tts);
        }
        else
        {
            if (sel)
                setIcon(iniFile.attIconS);
            else
                setIcon(iniFile.attIcon);
            
        }
        if (nodeName.equals("RejectedNode")||nodeName.equals("RejectedChildNode"))
        {
            
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (nodeName.equals("InvalidAttribute")||nodeName.equals("InvalidSpan")||nodeName.equals("InvalidComment"))
        {
            if (sel)
                setIcon(iniFile.errAttIconS);
            else
                setIcon(iniFile.errAttIcon);
        }
        else if (nodeName.equals("InvalidSubelement")||nodeName.equals("InvalidResource")||nodeName.equals("InvalidPartitionLeaf"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (nodeName.equals("UnknownSubelement"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (nodeName.equals("MissingAttribute")||nodeName.equals("MissingSpan"))
        {
            if (sel)
                setIcon(iniFile.errAttIconS);
            else
                setIcon(iniFile.errAttIcon);
        }
        else if (nodeName.equals("UnknownAttribute")||nodeName.equals("UnknownSpan")||nodeName.equals("SyntaxWarning"))
        {
            if (sel)
                setIcon(iniFile.errAttIconS);
            else
                setIcon(iniFile.errAttIcon);
        }
        else if (nodeName.equals("MissingSubelement")||nodeName.equals("MissingElement")||nodeName.equals("MissingResourceLink")
                ||nodeName.equals("MissingCustomerInfo")||nodeName.equals("MissingNodeInfo"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (nodeName.equals("MissingResources")|| nodeName.equals("MissingElements")|| nodeName.equals("InvalidResources")
                ||nodeName.equals("UnknownResources"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (nodeName.endsWith("Report"))
        {
            if(elem.hasChildElement("ExecutableNodes",null)||"true".equals(elem.getAttribute("IsValid")))
            {
                if (sel)
                    setIcon(iniFile.elemIconS);
                else
                    setIcon(iniFile.elemIcon);
                                
            }
            else
            {
                if (sel)
                    setIcon(iniFile.errElemIconS);
                else
                    setIcon(iniFile.errElemIcon);
            }
        }
        else if (nodeName.equals("ExecutableNodes"))
        {
            if (sel)
                setIcon(iniFile.elemIconS);
            else
                setIcon(iniFile.elemIcon);            
        }        
        else if (elem instanceof JDFNode)
        {
            if (sel)
                setIcon(iniFile.jdfElemIcon);
            else
                setIcon(iniFile.jdfElemIcon);
        }        
    }           
}

