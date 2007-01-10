package org.cip4.jdfeditor;
/*
*
* The CIP4 Software License, Version 1.0
*
*
* Copyright (c) 2001-2006 The International Cooperation for the Integration of 
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFComment;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;

/**
 * @author AnderssA ThunellE SvenoniusI
 * The renderer used in the main JDFTree
 */
public class JDFTreeRenderer extends DefaultTreeCellRenderer
{
    /**
     * 
     */
    private static final long serialVersionUID = 1526856515806803255L;
    protected Color colorSel;
    protected boolean highlightFN;
    protected Font standardFont=null;

    public JDFTreeRenderer()
    {
        setOpaque(true);
        colorSel = new Color(110, 200, 240);

        //TODO actually do something with hilite...
        final INIReader iniFile=Editor.getIniFile();
        
        highlightFN = iniFile.getHighlight();
        standardFont=new Font(iniFile.getFontName(), Font.PLAIN, iniFile.getFontSize());
    }
    
    public Component getTreeCellRendererComponent(
            JTree jdfTree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean _hasFocus)
    {
        if((expanded!=_hasFocus)||(leaf==expanded)||(row==0)) // fool compiler
        {
            jdfTree.getClass();
        }
        
        setBackground(sel ? colorSel : Color.white);
        setForeground(Color.black);
        setFont(standardFont);
        
        String s=null;
        String toolString=null;
        JDFTreeNode treeNode=null;
        if(value instanceof JDFTreeNode)
        {
            treeNode=(JDFTreeNode)value;
            s=treeNode.toDisplayString();
            toolString=s;
            if (treeNode.isElement()){
                KElement e=treeNode.getElement();
                if (e instanceof JDFComment)
                {
                    JDFComment c = (JDFComment) e;
                    toolString=c.getText();
                }
                String descName=e.getAttribute(AttributeName.DESCRIPTIVENAME,null,null);
                if(descName!=null && toolString!=null)
                    toolString+="\n"+descName;
                
            }
            
            JDFTreeModel mod=(JDFTreeModel) jdfTree.getModel();
            if (mod.isValid(treeNode))
            {
                setForeground(Color.black);
                if (!treeNode.isElement())
                {
                    final String attVal = treeNode.getValue();
                    
                    if (attVal.equals("new value"))
                        setForeground(Color.magenta);
                }
            }
            else
            {
                setForeground(Color.red);
                if (treeNode.isElement())
                {
                    toolString="Invalid Element";
                }
                else
                {
                    toolString="Invalid Attribute";
                }
            }
        }        
        else // value no treenode
        {
            s=value.toString();
            toolString=s;
        }
        setText(s);        
        setToolTipText(toolString);
        setNodeIcon(jdfTree,sel,treeNode);
        return this;
    }
    
    protected void setNodeIcon(JTree jdfTree,boolean sel, JDFTreeNode treeNode){
        KElement elem = treeNode.getElement();
        
        JDFTreeModel mod=(JDFTreeModel) jdfTree.getModel();
        final INIReader iniFile=Editor.getIniFile();
        if (mod.isValid(treeNode))
        {
            if (treeNode.isElement())
            {
                if (elem instanceof JDFNode)
                {
                    if (sel)
                        setIcon(iniFile.jdfElemIconS);
                    else
                        setIcon(iniFile.jdfElemIcon);
                }
                else if (elem != null && elem.hasAttribute("rRef", null, false))
                {
                    if (elem.getAttribute("Usage", null, "").equals("Input"))
                    {
                        if (sel)
                            setIcon(iniFile.rRefInElemIconS);
                        else
                            setIcon(iniFile.rRefInElemIcon);
                    }
                    else if (elem.getAttribute("Usage", null, "").equals("Output"))
                    {
                        if (sel)
                            setIcon(iniFile.rRefOutElemIconS);
                        else
                            setIcon(iniFile.rRefOutElemIcon);
                    }
                    else
                    {
                        if (sel)
                            setIcon(iniFile.rRefElemIconS);
                        else
                            setIcon(iniFile.rRefElemIcon);
                    }
                }
                else
                {
                    if (sel)
                        setIcon(iniFile.elemIconS);
                    else
                        setIcon(iniFile.elemIcon);
                }
            }
            else
            {
                final String attName = treeNode.getName();
                if (treeNode.isInherited())
                {
                    if (attName.equals("PartIDKeys"))
                    {
                        if (sel)
                            setIcon(iniFile.iPAttIconS);
                        else
                            setIcon(iniFile.iPAttIcon);
                    }
                    else
                    {
                        if (sel)
                            setIcon(iniFile.iAttIconS);
                        else
                            setIcon(iniFile.iAttIcon);
                    }
                }
                else
                {
                    if (attName.equals("PartIDKeys"))
                    {
                        if (sel)
                            setIcon(iniFile.pAttIconS);
                        else
                            setIcon(iniFile.pAttIcon);
                    }
                    else if (attName.equals("rRef"))
                    {
                        if (sel)
                            setIcon(iniFile.refAttIconS);
                        else
                            setIcon(iniFile.refAttIcon);
                    }
                    else
                    {
                        if (sel)
                            setIcon(iniFile.attIconS);
                        else
                            setIcon(iniFile.attIcon);
                    }
                }
            }
        }
        else
        {
            if (treeNode.isElement())
            {
                if (sel)
                    setIcon(iniFile.errElemIconS);
                else
                    setIcon(iniFile.errElemIcon);
            }
            else
            {
                if (sel)
                    setIcon(iniFile.errAttIconS);
                else
                    setIcon(iniFile.errAttIcon);
            }
        }
    }
    
}