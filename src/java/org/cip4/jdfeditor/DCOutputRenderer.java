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

        String n=treeNode.getName();
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
        if (n.equals("RejectedNode")||n.equals("RejectedChildNode"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (n.equals("InvalidAttribute")||n.equals("InvalidSpan")||n.equals("InvalidComment"))
        {
            if (sel)
                setIcon(iniFile.errAttIconS);
            else
                setIcon(iniFile.errAttIcon);
        }
        else if (n.equals("InvalidSubelement")||n.equals("InvalidResource")||n.equals("InvalidPartitionLeaf"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (n.equals("UnknownSubelement"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (n.equals("MissingAttribute")||n.equals("MissingSpan"))
        {
            if (sel)
                setIcon(iniFile.errAttIconS);
            else
                setIcon(iniFile.errAttIcon);
        }
        else if (n.equals("UnknownAttribute")||n.equals("UnknownSpan")||n.equals("SyntaxWarning"))
        {
            if (sel)
                setIcon(iniFile.errAttIconS);
            else
                setIcon(iniFile.errAttIcon);
        }
        else if (n.equals("MissingSubelement")||n.equals("MissingResourceLink")
                ||n.equals("MissingCustomerInfo")||n.equals("MissingNodeInfo"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (n.equals("MissingResources")||n.equals("InvalidResources")
                ||n.equals("UnknownResources"))
        {
            if (sel)
                setIcon(iniFile.errElemIconS);
            else
                setIcon(iniFile.errElemIcon);
        }
        else if (n.endsWith("Report"))
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
        else if (n.equals("ExecutableNodes"))
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

