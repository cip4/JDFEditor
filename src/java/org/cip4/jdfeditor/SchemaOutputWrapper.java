package org.cip4.jdfeditor;
import org.cip4.jdflib.core.KElement;
import org.w3c.dom.Attr;



class SchemaOutputWrapper extends JDFTreeNode
{
    /**
     * 
     */
    private static final long serialVersionUID = 2575958231112905133L;

    public SchemaOutputWrapper(KElement element)
    {
        super(element);
    }
    public SchemaOutputWrapper(Attr atr)
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
        if(isElement())
        {
            String s2=getElement().getAttribute("Message",null,null);
            if(s2!=null)
                s+=": Message= "+s2;
        }
        return s;
         
   }    
    
}
