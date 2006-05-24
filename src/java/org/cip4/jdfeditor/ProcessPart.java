package org.cip4.jdfeditor;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JComponent;

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;

/*
 * FooProcessPart.java
 * @author SvenoniusI
 */

public class ProcessPart extends JComponent
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5703455772001305819L;

    public int width;
    public int height;
    
    private String name;
    private KElement elem;
    private KElement link;
    private FontMetrics fm;
    private Color gColor;
    private String[] gString;    
    private Vector vInRes = new Vector();
    private Vector vOutRes = new Vector();
    private Vector vAllRes = new Vector();
    private int xPos;
    private int yPos;
    private int yResIn;
    private int yResOut;
    private boolean sharedInput;
    
    public ProcessPart(KElement _elem, String _name)
    {
        this.elem = _elem;
        this.name = _name;
    }
    
    public void setGfxParams(FontMetrics _fm, int _xPos, int _yPos)
    {
        this.xPos = _xPos;
        this.yPos = _yPos;
        this.fm = _fm;
        
        if(elem instanceof JDFNode)
        {
            height = 75;
            
            if (name.equals("ParentPart"))
                gColor = new Color(215, 245, 255);
            else
                gColor = new Color(180, 230, 250);
            

            JDFNode node = (JDFNode) elem;
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
                String[] tmp = { elem.getNodeName() + " " + elem.getAttribute("Type"),
                        elem.getAttribute("ID"),
                        elem.getAttribute("Status"),
                        elem.getAttribute("DescriptiveName")
                        };
                gString = tmp;
            }
           
            width = setPartWidth(gString);
        }
        else if (name.equals("ResPart"))
        {
            height = 45;
            
            gColor = new Color(200, 250, 200);
            String[] tmp = { elem.getNodeName(),
                elem.getAttribute("ID") };
            gString = tmp;
            
            width = setPartWidth(gString);
        }
        else //if (JDFElement.isResourceStatic(elem))
        {
            height = 60;
            
            gColor = new Color(200, 250, 200);
            String[] tmp = { elem.getNodeName(),
                elem.getAttribute("ID"),
                elem.getAttribute("Status", "", "") };
            gString = tmp;
            
            width = setPartWidth(gString);
        }
        width += 30;
    }
    
    private int setPartWidth(String[] s)
    {
        int w = 0;
        
        for (int i = 0; i < s.length; i++)
        {
            w = w < fm.stringWidth(s[i]) ? fm.stringWidth(s[i]) : w;
        }
        
        return w;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public KElement getElem()
    {
        return this.elem;
    }
    
    public KElement getLink()
    {
        return this.link;
    }
    
    public void setLink(KElement _link)
    {
        this.link = _link;
    }
    
    public Point getInPoint()
    {
        final Point p = new Point(xPos + this.width, yPos + this.height / 2);
        
        return p;
    }
    
    public Point getOutPoint()
    {
        final Point p = new Point(xPos, yPos + this.height / 2);
        
        return p;
    }
    
    public int getyResIn()
    {
        return this.yResIn;
    }
    
    public void setyResIn(int _yResIn)
    {
        this.yResIn = _yResIn;
    }
    
    public int getyResOut()
    {
        return this.yResOut;
    }
    
    public void setyResOut(int _yResOut)
    {
        this.yResOut = _yResOut;
    }
    
    /*public int getXPosition()
    {
        return this.xPosition;
    }*/
    
    public Vector getvAllRes()
    {
        /*Vector vAllRes = new Vector();
        
        for (int i = 0; i < vInRes.size(); i ++)
        {
            vAllRes.add(vInRes.get(i));
        }
        for (int i = 0; i < vOutRes.size(); i ++)
        {
            vAllRes.add(vOutRes.get(i));
        }
        
        return vAllRes;*/
        
        return this.vAllRes;
    }
    
    public void addTovAllRes(ProcessPart pp)
    {
        this.vAllRes.add(pp);
    }
    
    public boolean hasRes(ProcessPart pp)
    {
        return this.vInRes.contains(pp) || this.vOutRes.contains(pp);
    }
    
    public Vector getvInRes()
    {
        return this.vInRes;
    }
    
    /*public boolean hasInRes(ProcessPart pp)
    {
        return this.vInRes.contains(pp);
    }*/
    
    public void addTovInRes(ProcessPart pp)
    {
        this.vInRes.add(pp);
    }
    
    public Vector getvOutRes()
    {
        return this.vOutRes;
    }
    
    /*public boolean hasOutRes(ProcessPart pp)
    {
        return this.vOutRes.contains(pp);
    }*/
    
    public void addTovOutRes(ProcessPart pp)
    {
        this.vOutRes.add(pp);
    }
    
    public void setSharedInput(boolean _sharedInput)
    {
        this.sharedInput = _sharedInput;
    }
    
    public boolean hasSharedInput()
    {
        return this.sharedInput;
    }
    
    public Color getgColor()
    {
        return this.gColor;
    }
    
    public String[] getgString()
    {
        return this.gString;
    }
    
    public int getxPos()
    {
        return this.xPos;
    }
    
    public int getyPos()
    {
        return this.yPos;
    }
}