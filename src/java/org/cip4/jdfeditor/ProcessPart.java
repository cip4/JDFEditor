/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2008 The International Cooperation for the Integration of
 * Processes in  Prepress, Press and Postpress (CIP4).  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        The International Cooperation for the Integration of
 *        Processes in  Prepress, Press and Postpress (www.cip4.org)"
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior written
 *    permission of the CIP4 organization
 *
 * Usage of this software in commercial products is subject to restrictions. For
 * details please consult info@cip4.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR
 * THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the The International Cooperation for the Integration
 * of Processes in Prepress, Press and Postpress and was
 * originally based on software
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG
 * copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *
 *
 */
package org.cip4.jdfeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JComponent;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.datatypes.VJDFAttributeMap;
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
    private final KElement elem; // the element (node or resource) that is displayed
    public int style; // the style of this ProcessPart, i.e Node, Parent Resource or Res_External

    private Color gColor;
    private String[] gString;    
    private final Vector<ProcessPart> vInRes = new Vector<ProcessPart>();
    private final Vector<ProcessPart> vOutRes = new Vector<ProcessPart>();
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
                VJDFAttributeMap nodeParts=node.getPartMapVector();
                boolean isExe=true;
                if(nodeParts==null)
                {
                    nodeParts=new VJDFAttributeMap();
                    nodeParts.add(null);
                }
                for(int i=0;i<nodeParts.size();i++)
                {
                    isExe=isExe && node.isExecutable(nodeParts.elementAt(i),false);
                }

                gColor = isExe ? nodeColorExec : nodeColorNonExec;
            }
            String[] tmp = { 
                    elem.getNodeName() + " " + elem.getAttribute("Type"),
                    elem.getAttribute("ID")+" - "+elem.getAttribute("Status"),
                    node.getJobID(true)+" - "+node.getJobPartID(false),
                    node.getDescriptiveName()
                    };
            gString = tmp;
            String types=elem.getAttribute(AttributeName.TYPES,null,null);
            if(types!=null)
                gString[0]+= " : " + elem.getAttribute(AttributeName.TYPES);
            String cat=elem.getAttribute(AttributeName.CATEGORY,null,null);
            if(cat!=null)
                gString[0]+= " - " + elem.getAttribute(AttributeName.CATEGORY);
            
            rawWidth = setPartWidth(gString);
            setToolTipText("JDFNode: "+elem.getAttribute("DescriptiveName"));
        }
        else if (style==RES_EXTERNAL)
        {
            rawHeight = 75 /*55*/;

            String[] tmp = { 
                    elem.getNodeName(),
                    elem.getAttribute("ID"),
                    elem.getAttribute("Status")
            };
            gString = tmp;

            rawWidth = setPartWidth(gString);
            setToolTipText(elem.getNodeName()+": "+elem.getAttribute("DescriptiveName"));
        }
        else 
        {
            rawHeight = 75 /*60*/;

            String[] tmp = { elem.getNodeName(),
                    elem.getAttribute("ID"),
                    elem.getAttribute("Status", null, "") };
            gString = tmp;

            rawWidth = setPartWidth(gString);
            setToolTipText(elem.getNodeName()+": "+elem.getAttribute("DescriptiveName"));
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
                    if (v != null)
                    {
						final int size = v.size();
						for (int i = 0; i < size; i++)
						{
							JDFResource rPart0 = (JDFResource) v.elementAt(i);
							VElement vv = rPart0.getLeaves(false);
							for (int j = 0; j < vv.size(); j++)
							{
								JDFResource rPart = (JDFResource) vv.elementAt(j);

								final EnumResStatus resStatus = rPart.getResStatus(false);
								int col = resStatus == null ? 0 : resStatus.getValue();
								if (col > colMax)
									colMax = col;
							}
						}
					}
                    
                    final EnumResStatus enumStatus = EnumResStatus.getEnum(colMax);
                    gString[2] = enumStatus==null ? "unknown" : enumStatus.getName();
                }
                else
                {
                    colMax=r.getStatusFromLeaves(false).getValue();
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
    @Override
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

    @Override
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
            final int indexOf = parts.indexOf(new ProcessPart(v.elementAt(i),0,null));
            if(indexOf<0)
            {
                v.remove(i);
            }
        }
        return v;
    }

}