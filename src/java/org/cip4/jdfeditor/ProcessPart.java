package org.cip4.jdfeditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JComponent;

import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.JDFResource.EnumResStatus;

/*
 * FooProcessPart.java
 * @author SvenoniusI
 */

public class ProcessPart extends JComponent
{
    private static Color nodeColorExec = new Color(180, 230, 250);
    private static Color nodeColorNonExec = new Color(250, 230, 180);
    private static Color defColor = new Color(250, 230, 180);
    private static Color resColorAvailable = new Color(180, 200, 250);
    private static Color resColorUnAvailable = new Color(250, 200, 180);
    private static Color resColorDraft = new Color(250, 250, 180);

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5703455772001305819L;

    public int rawWidth;
    public int rawHeight;
    public static final int PARENT=0;
    public static final int NODE=1;
    public static final int RESOURCE=2;
    public static final int RES_EXTERNAL=3;
    
    
    public boolean isSelected=false; // if true, this element is selected and is connected by emphasized ResourceLink lines
    private KElement elem; // the element (node or resource) that is displayed
    public int style; // the style of this ProcessPart, i.e Node, Parent Resource or Res_External
    
    private Color gColor;
    private String[] gString;    
    private Vector vInRes = new Vector();
    private Vector vOutRes = new Vector();
    private int xPos;
    private int yPos;

    private boolean isPositioned=false;

    static private Font procFont = null;
    static private Font resourceFont = null;
    static private Font parentFont = null;
    
    public ProcessPart(KElement _elem, int _style, JDFResourceLink rl)
    {
        elem = _elem;
        style=_style;
        isSelected=_style==PARENT;
        setupFonts();
        
        switch (style)
        {
            case 0:
                setFont(parentFont);                
                break;
            case 1:
                setFont(procFont);                
                break;
            case 2:
                setFont(resourceFont);                
                break;
            case 3:
                setFont(resourceFont);                
                break;

            default:
                throw new IllegalArgumentException("bad style in constructor, mustt be in range 0-3: "+style);
        } 
        setStrings(rl);
    }

    /**
     * set up the initial fonts based on inireader
     */
    private void setupFonts()
    {
        if(procFont==null)
        {
            INIReader reader=Editor.getIniFile();
            final String fontName=reader.getFontName();
            final int fontSize=reader.getFontSize();
            procFont = new Font(fontName, Font.PLAIN, fontSize);
            resourceFont = new Font(fontName, Font.PLAIN, fontSize-1);
            parentFont = new Font(fontName, Font.BOLD, fontSize+2);
        }
    }
    
    public void setPos(int _xPos, int _yPos)
    {
        xPos = _xPos;
        yPos = _yPos;
        isPositioned=true;
    }

    /**
     * 
     */
    private void setStrings(JDFResourceLink rl)
    {
        gColor=null;
        if(elem instanceof JDFNode)
        {
            JDFNode node = (JDFNode) elem;
           rawHeight = 75;
            if (style==PARENT && node.hasChildElement(ElementName.JDF, null))
            {
                gColor = new Color(215, 245, 255);
            }
            if(gColor==null)
            {
                if(node.isExecutable(null, true))
                    gColor = nodeColorExec;
                else
                    gColor = nodeColorNonExec;
            }
            if (node.getType().equals(JDFConstants.COMBINED))
            {
                String[] tmp = { elem.getNodeName() + " " + elem.getAttribute("Type"),
                        elem.getAttribute("Types"),
                        elem.getAttribute("ID"),
                        elem.getAttribute("Status"),
                        node.getDescriptiveName()};
                gString = tmp;
            }
            else 
            {
                String[] tmp = { 
                        elem.getNodeName() + " " + elem.getAttribute("Type"),
                        elem.getAttribute("ID"),
                        elem.getAttribute("Status"),
                        elem.getAttribute("DescriptiveName")
                        };
                gString = tmp;
            }
           
            rawWidth = setPartWidth(gString);
            setToolTipText("JDFNode: "+elem.getAttribute("DescriptiveName"));
        }
        else if (style==RES_EXTERNAL)
        {
            rawHeight = 55;
            
            String[] tmp = { 
                    elem.getNodeName(),
                    elem.getAttribute("ID"),
                    elem.getAttribute("Status")
            };
            gString = tmp;
            
            rawWidth = setPartWidth(gString);
            setToolTipText("JDFResource: "+elem.getAttribute("DescriptiveName"));
        }
        else 
        {
            rawHeight = 60;
            
            String[] tmp = { elem.getNodeName(),
                elem.getAttribute("ID"),
                elem.getAttribute("Status", "", "") };
            gString = tmp;
            
            rawWidth = setPartWidth(gString);
            setToolTipText("JDFResource: "+elem.getAttribute("DescriptiveName"));
        }
        rawWidth += 30;
        if(gColor==null)
        {
            if(elem instanceof JDFResource)
            {
                JDFResource r=(JDFResource)elem;
                int colMax=-1;
                if(rl!=null)
                {
                    VElement v=rl.getTargetVector(-1);
                    if(v!=null)
                    {
                        for(int i=0;i<v.size();i++)
                        {
                            JDFResource rPart=(JDFResource) v.elementAt(i);
                            int col=rPart.getResStatus(false).getValue();
                            if(col>colMax)
                                colMax=col;

                        }
                    }
                    gString[2] = EnumResStatus.getEnum(colMax).getName();
                }
                else
                {
                    colMax=r.getResStatus(false).getValue();
                }
                if(colMax<EnumResStatus.Draft.getValue())
                    gColor= resColorUnAvailable;
                else if(colMax>=EnumResStatus.Available.getValue())
                    gColor= resColorAvailable;
                else
                    gColor= resColorDraft;
            }
            else
            {
                gColor=defColor;
            }
        }
    }
    
    private int setPartWidth(String[] s)
    {
        int w = 0;
        FontMetrics fm=getFM();
        for (int i = 0; i < s.length; i++)
        {
            w = w < fm.stringWidth(s[i]) ? fm.stringWidth(s[i]) : w;
        }        
        return w;
    }
        
    public KElement getElem()
    {
        return elem;
    }
        
    public Point getRightPoint()
    {
        final Point p = new Point(xPos + this.rawWidth, yPos + this.rawHeight / 2);        
        return p;
    }
    
    public Point getLeftPoint()
    {
        final Point p = new Point(xPos, yPos + this.rawHeight / 2);        
        return p;
    }
                       
    public Vector getvInRes()
    {
        return vInRes;
    }
        
    public void addTovInRes(ProcessPart pp)
    {
        vInRes.add(pp);
    }
    
    public Vector getvOutRes()
    {
        return vOutRes;
    }
        
    public void addTovOutRes(ProcessPart pp)
    {
        vOutRes.add(pp);
    }
        
    public Color getgColor()
    {
        return gColor;
    }
    
    public String[] getgString()
    {
        return this.gString;
    }
    
    public int getxPos()
    {
        return xPos;
    }
    
    public int getyPos()
    {
        return yPos;
    }
    public FontMetrics getFM()
    {
        return getFontMetrics(getFont());
    }

    /**
     * ProcessParts are equal if they contain the same element elem
     * also compares this.elem to a KElement 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        if (super.equals(arg0))
            return true;
        if(arg0 instanceof ProcessPart)
            return elem.equals(((ProcessPart)arg0).elem);
        else if (arg0 instanceof KElement)
            return elem.equals(arg0);
        return false;
    }
    
    public String toString()
    {
        String s="[ProcessPart: ";
        if(elem!=null)
        {
            s+=" Name="+elem.getNodeName();
            String id=elem.getAttribute("ID",null,null);
            if(id!=null)
                s+=" ID="+id;
        }
        s+="["+super.toString()+ "]";
        return s;
    }

    /**
     * @return
     */
    public boolean hasPosition()
    {
        return isPositioned;
    }

    /**
     * @param b
     * @return
     */
    public VElement getPredecessors(boolean b, Vector parts)
    {
        final JDFNode jdfNode=(JDFNode)getElem();
        VElement v=jdfNode.getPredecessors(b, false);
        if(parts==null)
            return v;
        for(int i=v.size()-1;i>=0;i--)            
        {
            final int indexOf = parts.indexOf(new ProcessPart((KElement)v.elementAt(i),0,null));
            if(indexOf<0)
            {
                v.remove(i);
            }
        }
        return v;
    }
    
}