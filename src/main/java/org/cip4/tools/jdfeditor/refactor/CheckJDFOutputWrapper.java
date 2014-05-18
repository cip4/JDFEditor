package org.cip4.tools.jdfeditor.refactor;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.w3c.dom.Attr;



class CheckJDFOutputWrapper extends JDFTreeNode
{
    /**
     * 
     */
    private static final long serialVersionUID = 2575958231112905133L;

    public CheckJDFOutputWrapper(KElement element)
    {
        super(element);
    }
    public CheckJDFOutputWrapper(Attr atr)
    {
        super(atr,false);
    }
    
    /**
     * generates the string to be displayed in the tree
     */
    @Override
	public String toDisplayString()
    {
        String s=super.toDisplayString();
        if(s==null)
            return null;
        Object o=this.getUserObject();
        if(o instanceof Attr)
        {
           Attr atr=(Attr)o;
           String nam=atr.getLocalName();
           if(nam.equals("XPath"))
           {
               s=" XPath in the Test file: "+atr.getNodeValue();
           }
           return s;
        }
        
        KElement e=(KElement)o;
        if (e instanceof JDFNode)
            return s; 
        String nam=getName();
        if(nam.equals("TestElement"))
        {
            s=getDCString("ErrorType",""," ")+getDCString("NodeName",""," ");
        }
        if(nam.equals("TestAttribute"))
        {
            s=getDCString("ErrorType",""," ")+getDCString("NodeName",""," ");
        }
        if(nam.equals("Warning"))
        {
            s=getDCString("ErrorType",""," ");
        }
        s+=getDCString("Value",null,"; ");
        s+=getDCString("ID",null,"; ");
        s+=getDCString("rRef",null,"; ");
        s+=getDCString("Separation","","; ");
        return s;
         
   }    
    
}
