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
package org.cip4.jdfeditor;

import java.io.File;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.KElement.EnumValidationLevel;
import org.cip4.jdflib.util.StringUtil;

/*
 * INI_Reader.java
 * @author SvenoniusI
 * 
 * History
 * 20040906 MRE methods for send to device added
 * 
 * File includes path for display icons in the JDFEditor.
 */

public class INIReader
{

	final public ImageIcon defaultErrAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ErrorAttIcon.gif");
    final public ImageIcon defaultErrAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ErrorAttIconSelected.gif");
    final public ImageIcon defaultErrElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ErrorElemIcon.gif");
    final public ImageIcon defaultErrElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ErrorElemIconSelected.gif");
    
    final public ImageIcon defaultWarnAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "WarnAttIcon.gif");
    final public ImageIcon defaultWarnAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "WarnAttIconSelected.gif");
    final public ImageIcon defaultWarnElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "WarnElemIcon.gif");
    final public ImageIcon defaultWarnElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "WarnElemIconSelected.gif");

    final public ImageIcon defaultAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "AttIcon.gif");
    final public ImageIcon defaultAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "AttIconSelected.gif");
    final public ImageIcon defaultIAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "InhAttIcon.gif");
    final public ImageIcon defaultIAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "InhAttIconSelected.gif");
    
    final public ImageIcon defaultPAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "PartIDKeysAttIcon.gif");
    final public ImageIcon defaultPAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "PartIDKeysAttIconSelected.gif");
    
    final public ImageIcon defaultIPAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "InhPartIDKeysAttIcon.gif");
    final public ImageIcon defaultIPAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "InhPartIDKeysAttIconSelected.gif");
    
    final public ImageIcon defaultRefAttIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "RefAttIcon.gif");
    final public ImageIcon defaultRefAttIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "RefAttIconSelected.gif");
    
    final public ImageIcon defaultElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ElemIcon.gif");
    final public ImageIcon defaultElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "ElemIconSelected.gif");
    
    final public ImageIcon defaultJDFElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "JDFElemIcon.gif");
    final public ImageIcon defaultJDFElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "JDFElemIconSelected.gif");
    
    final public ImageIcon defaultRefElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefElemIcon.gif");
    final public ImageIcon defaultRefElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefElemIconSelected.gif");
    final public ImageIcon defaultRefInElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefInElemIcon.gif");
    final public ImageIcon defaultRefInElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefInElemIconSelected.gif");
    final public ImageIcon defaultRefOutElemIcon = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefOutElemIcon.gif");
    final public ImageIcon defaultRefOutElemIconS = Editor.getImageIcon(getClass(), Editor.ICONS_PATH + "rRefOutElemIconSelected.gif");
    
    public ImageIcon warnAttIcon=defaultWarnAttIcon;
    public ImageIcon warnAttIconS=defaultWarnAttIconS;
    public ImageIcon warnElemIcon=defaultWarnElemIcon;
    public ImageIcon warnElemIconS=defaultWarnElemIconS;
    public ImageIcon errAttIcon;
    public ImageIcon errAttIconS;
    public ImageIcon errElemIcon;
    public ImageIcon errElemIconS;
    public ImageIcon attIcon;
    public ImageIcon attIconS;
    public ImageIcon iAttIcon;
    public ImageIcon iAttIconS;
    public ImageIcon pAttIcon;
    public ImageIcon pAttIconS;
    public ImageIcon iPAttIcon;
    public ImageIcon iPAttIconS;
    public ImageIcon refAttIcon;
    public ImageIcon refAttIconS;
    public ImageIcon elemIcon;
    public ImageIcon elemIconS;
    public ImageIcon jdfElemIcon;
    public ImageIcon jdfElemIconS;
    public ImageIcon rRefInElemIcon;
    public ImageIcon rRefInElemIconS;
    public ImageIcon rRefOutElemIcon;
    public ImageIcon rRefOutElemIconS;
    public ImageIcon rRefElemIcon;
    public ImageIcon rRefElemIconS;
//  public ImageIcon invElemIcon;
//  public ImageIcon invAttIcon;
//  public ImageIcon misElemIcon;
//  public ImageIcon misAttIcon;
//  public ImageIcon unkElemIcon;
//  public ImageIcon unkAttIcon;
//  public ImageIcon execJDFIcon;
//  public ImageIcon invElemIconS;
//  public ImageIcon invAttIconS;
//  public ImageIcon misElemIconS;
//  public ImageIcon misAttIconS;
//  public ImageIcon unkElemIconS;
//  public ImageIcon unkAttIconS;
    
    String iconStrings[] =
    {
            "Attribute with Error=default",
            "Attribute with Error (selected)=default",
            "Element with Error=default",
            "Element with Error (selected)=default",
            "Attribute=default",
            "Attribute (selected)=default",
            "Inherited Attribute=default",
            "Inherited Attribute (selected)=default",
            "PartID Key Attribute=default",
            "PartID Key Attribute (selected)=default",
            "Inherited PartID Key Attribute=default",
            "Inherited PartID Key Attribute (selected)=default",
            "rRef Attribute=default",
            "rRef Attriubte (selected)=default",
            "Element=default",
            "Element (selected)=default",
            "JDF Element=default",
            "JDF Element (selected)=default",
            "Input rRef Element=default",
            "Input rRef Element (selected)=default",
            "Output rRef Element=default",
            "Output rRef Element (selected)=default",
            "rRef Element=default",
            "rRef Element (selected)=default",
            "Attribute with Warning=default",
            "Attribute with Warning (selected)=default",
            "Element with Warning=default",
            "Element with Warning (selected)=default",
//          "JDF Folder=default"
    };
    private String language="General/@language"; 
    private String lookAndFeel = "General/@lookAndFeel";
    private String methodSendToDevice = "General/@methodSendToDevice";
    private String urlSendToDevice = "General/@URLSendToDevice";
    private String longID = "General/@longID";
    private String fontSize = "General/@fontSize";
    private String fontName = "General/@fontName";
    private String normalizeOpen = "General/@NormalizeOpen";

    private String enableExtensions = "Extension/@enableExtensions";
    
    private String[] recentFiles = new String[5];
    private final String recentDevCap ="RecentFiles/@recentDevCap";
    
    private final String autoValidate = "ValidEdit/@autoValidate";
    private final String exportValidate = "ValidEdit/@exportValidate";
    private final String highlightFN = "ValidEdit/@highlightFN";
    private final String readOnly = "ValidEdit/@readOnly";
    private final String schemaURL = "ValidEdit/@schemaURL";
    private final String useSchema = "ValidEdit/@useSchema";
    private final String validVersion = "ValidEdit/@version";
    private final String validLevel = "ValidEdit/@level";
    private final String removeDefault = "ValidEdit/@removeDefault";
    private final String displayDefault = "ValidEdit/@displayDefault";
    private final String ignoreDefault = "ValidEdit/@ignoreDefault";
    private final String removeWhite = "ValidEdit/@removeWhite";
    private final String checkURL = "ValidEdit/@checkURL";
         
    private final String attribute = "TreeView/@attribute";
    private final String inheritedAttr = "TreeView/@inheritedAttr";
    
    private final String generateFull = "Validate/@GenerateFull";
    private final String genericAtts = "Validate/@genericAtts";
    
    private final String misURL = "GoldenTicket/@misURL";
    private final String BaseLevel = "GoldenTicket/@BaseLevel";
    private final String MISLevel = "GoldenTicket/@MISLevel";
    private final String JMFLevel = "GoldenTicket/@JMFLevel";
    
    
    private XMLDoc xDoc; // The XMLDocument that represents the ini file
    
    public INIReader()
    {
        init();
    }
    
    private void init()
    {
        Arrays.sort(iconStrings);
        //Read the Editor.ini file and store the data in it
        try
        {
            JDFParser p=new JDFParser();
            xDoc = p.parseFile(getIniPath());
            readINIFile();
        }
        //If the Editor.ini file is not found, create a new default file and read it
        catch (Exception ex)
        {
            setLanguage("en"); // dummy to set up a minimal xml document
            writeINIFile();
            readINIFile();
        }
        setIcons();        
    }
    
    
    public String getLanguage()
    {
        return getAttribute(language,"en");
    }
    
    private String getAttribute(String path, String def)
    {
        if(xDoc==null)
            return def;
        return xDoc.getRoot().getXPathAttribute(path,def);
    }
    
    public String getLookAndFeel()
    {
        
        return getAttribute(lookAndFeel, UIManager.getSystemLookAndFeelClassName());        
    }
    
    public int getFontSize()
    {
        String s = getAttribute(fontSize,"10");
        return Integer.parseInt(s);
    }   
    public String getFontName()
    {
        return getAttribute(fontName,null);
    }   
    public void setFontSize(int fs)
    {
        setAttribute(fontSize,String.valueOf(fs));
    }
    public void setFontName(String _fontName)
    {
        setAttribute(fontName,_fontName);
    }
    public String getGenericAtts()
    {
        String defaultGenerics="ID Type JobID JobPartID ProductID CustomerID SpawnIDs" +
        " Class Status PartIDKeys xmlns xmlns:xsi xsi:Type" +
        " SettingsPolicy BestEffortExceptions" +
        " OperatorInterventionExceptions" +
        " MustHonorExceptions" +
        " DocIndex Locked DescriptiveName Brand";  
        
        String s = getAttribute(genericAtts,defaultGenerics);
        return s;
    }   
    public void setGenericAtts(VString s)
    {
        setAttribute(genericAtts,StringUtil.setvString(s," ",null,null));
    }
    
    public boolean getAutoVal()
    {
        return getAttribute(autoValidate,"").equals("on");
    }
    public void setAutoVal(boolean bVal)
    {
        setAttribute(autoValidate,bVal ? "on" : "off");
    }
    public boolean getExportValidation()
    {
        return getAttribute(exportValidate,"").equals("on");
    }
    public void setExportValidation(boolean bVal)
    {
        setAttribute(exportValidate,bVal ? "on" : "off");
    }
    public void setReadOnly(boolean bRO)
    {
        setAttribute(readOnly,bRO ? "on" : "off");
    }
    
    public String[] getRecentFiles()
    {
        return this.recentFiles;
    }
    
    public void setRecentDevCap(File devCapFile)
    {
        if(devCapFile==null){
            setAttribute(recentDevCap,null);           
        }
        else
        {
            setAttribute(recentDevCap,devCapFile.getAbsolutePath());
        }
    }
    
    public File getRecentDevCap()
    {
        String s=getAttribute(recentDevCap,null);
        if(s==null)
            return null;
        return new File(s);
    }
    
    public void setSchemaURL(File schema)
    {
        if(schema==null){
            setAttribute(schemaURL,null);           
        }
        else
        {
            setAttribute(schemaURL,schema.getAbsolutePath());
        }
    }
    
    public File getSchemaURL()
    {
        String s=getAttribute(schemaURL,null);
        if(s==null)
            return null;
        return new File(s);
    }
    
    public void setUseSchema(boolean schema)
    {
        setAttribute(useSchema,schema ? "true" : "false");
    }
    
    public boolean getUseSchema()
    {
        return getAttribute(useSchema,"false").equals("true");
    }
    
    public void setCheckURL(boolean url)
    {
        setAttribute(checkURL,url ? "true" : "false");
    }
    
    public boolean getCheckURL()
    {
        return getAttribute(checkURL,"false").equals("true");
    }

    public void setRemoveWhite(boolean rem)
    {
        setAttribute(removeWhite,rem ? "true" : "false");
    }
    
    public boolean getRemoveWhite()
    {
        return getAttribute(removeWhite,"true").equals("true");
    }
    
    public void setRemoveDefault(boolean rem)
    {
        setAttribute(removeDefault,rem ? "true" : "false");
    }
    
    public boolean getRemoveDefault()
    {
        return getAttribute(removeDefault,"true").equals("true");
    }

    public void setDisplayDefault(boolean rem)
    {
        setAttribute(displayDefault,rem ? "true" : "false");
    }
        
    public boolean getDisplayDefault()
    {
        return getAttribute(displayDefault,"true").equals("true");
    }
    
    public void setIgnoreDefault(boolean rem)
    {
        setAttribute(ignoreDefault,rem ? "true" : "false");
    }
        
    public boolean getIgnoreDefault()
    {
        return getAttribute(ignoreDefault,"true").equals("true");
    }
    
    public String[] getIconStrings()
    {
        return this.iconStrings;
    }
    
    public boolean getEnableExtensions()
    {
        return getAttribute(enableExtensions,"false").equalsIgnoreCase("true") ? true : false;
    }
    
    public boolean getHighlight()
    {
        return getAttribute(highlightFN,"").equalsIgnoreCase("on") ? true : false;
    }
    public void setHighlight(boolean b)
    {
        setAttribute(highlightFN,b?"on":"off");
    }
    public void setEnableExtensions(boolean b)
    {
        setAttribute(enableExtensions,b?"true":"false");
    }
   
    public boolean getReadOnly()
    {
        return getAttribute(readOnly,"").equalsIgnoreCase("on") ? true : false;
    }
    
    public boolean getAttr()
    {
        return getAttribute(attribute,"on").equalsIgnoreCase("on") ? true : false;
    }
    public void setAttr(boolean b)
    {
        setAttribute(attribute,b?"on":"off");
    }
   
    public boolean getInhAttr()
    {
        return getAttribute(inheritedAttr,"on").equalsIgnoreCase("on") ? true : false;
    }
    public void setInhAttr(boolean b)
    {
        setAttribute(inheritedAttr,b?"on":"off");
    }
    
    public String getMethodSendToDevice()
    {
        return getAttribute(methodSendToDevice,"MIME");
    }
    public void setMethodSendToDevice(String method)
    {
        setAttribute(methodSendToDevice,method);
    }
    public String getURLSendToDevice()
    {
        return getAttribute(urlSendToDevice,"http://");
    }
    public void setURLSendToDevice(String url)
    {
        setAttribute(urlSendToDevice,url);
    }
    
    public EnumValidationLevel getValidationLevel()
    {
        final String s = getAttribute(validLevel,JDFConstants.VALIDATIONLEVEL_RECURSIVECOMPLETE);
        return EnumValidationLevel.getEnum(s);
    }
    public void setValidationLevel(EnumValidationLevel level)
    {
        setAttribute(validLevel,level.getName());
    }
    public EnumVersion getValidationVersion()
    {
        final String s = getAttribute(validVersion,null);
        return EnumVersion.getEnum(s);
    }
    public void setValidationVersion(EnumVersion level)
    {
        setAttribute(validVersion,level==null ? null : level.getName());
    }
    
    private void readINIFile()
    {         
        for(int i=0;i<5;i++)
        {
            String s=getAttribute("RecentFiles/File["+String.valueOf(i+1)+"]/@Path",null);
            if(s==null)
                break;
            recentFiles[i]=s;
        }
        
        for(int i=0;true;i++)
        {
            String name=getAttribute("Icons/Icon["+String.valueOf(i+1)+"]/@Name",null);
            String path=getAttribute("Icons/Icon["+String.valueOf(i+1)+"]/@Path",null);
            if(name==null || path==null)
                break;
            checkIcon(name,path);
        }
        
    }
    
    
    public void writeINIFile()
    {
        if(recentFiles!=null)
        {
            for(int i=0;i<recentFiles.length;i++)
            {
                if(recentFiles[i]!=null && recentFiles[i].length()>0)
                {
                    setAttribute("RecentFiles/File["+String.valueOf(i+1)+"]/@Path",recentFiles[i]);
                }
            }
        }
        final String iniPath = getIniPath();
        xDoc.write2File(iniPath, 2, true);
    }

    private String getIniPath()
    {
        //TODO find correct property for application data
        String path=System.getProperty("user.home");
        File iniDir=new File(path+File.separator+"CIP4Editor");
        if(!iniDir.exists())
            iniDir.mkdir();
        final String iniPath = iniDir.getPath()+File.separator+"Editor.ini";
        return iniPath;
    }
    
    private void checkIcon(String iconName, String iconPath)
    {
        
        for (int i = 0; i < iconStrings.length; i++)
        {
            final String temp = iconStrings[i].substring(0, iconStrings[i].indexOf("="));
            
            if (iconName.equals(temp))
            {
                iconStrings[i] = iconName + "=" + iconPath;
                break;
            }
        }
    }
    
    public void setIcons()
    {
        for (int i = 0; i < iconStrings.length; i++)
        {
            final int index = iconStrings[i].indexOf("=") + 1;
            final String iconName = iconStrings[i].substring(0, index);
            final String iconPath = iconStrings[i].substring(index, iconStrings[i].length());
            setAttribute("Icons/Icon["+String.valueOf(i+1)+"]/@Name",iconName);
            setAttribute("Icons/Icon["+String.valueOf(i+1)+"]/@Path",iconPath);
            
            if (iconName.equals("Attribute with Error="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    errAttIcon = defaultErrAttIcon;
                else
                    errAttIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Attribute with Error (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    errAttIconS = defaultErrAttIconS;
                else
                    errAttIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Element with Error="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    errElemIcon = defaultErrElemIcon;
                else
                    errElemIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Element with Error (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    errElemIconS = defaultErrElemIconS;
                else
                    errElemIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Attribute="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    attIcon = defaultAttIcon;
                else
                    attIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Attribute (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    attIconS = defaultAttIconS;
                else
                    attIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Inherited Attribute="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    iAttIcon = defaultIAttIcon;
                else
                    iAttIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Inherited Attribute (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    iAttIconS = defaultIAttIconS;
                else
                    iAttIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("PartID Key Attribute="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    pAttIcon = defaultPAttIcon;
                else
                    pAttIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("PartID Key Attribute (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    pAttIconS = defaultPAttIconS;
                else
                    pAttIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Inherited PartID Key Attribute="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    iPAttIcon = defaultIPAttIcon;
                else
                    iPAttIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Inherited PartID Key Attribute (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    iPAttIconS = defaultIPAttIconS;
                else
                    iPAttIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("rRef Attribute="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    refAttIcon = defaultRefAttIcon;
                else
                    refAttIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("rRef Attriubte (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    refAttIconS = defaultRefAttIconS;
                else
                    refAttIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Element="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    elemIcon = defaultElemIcon;
                else
                    elemIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Element (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    elemIconS = defaultElemIconS;
                else
                    elemIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("JDF Element="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    jdfElemIcon = defaultJDFElemIcon;
                else
                    jdfElemIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("JDF Element (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    jdfElemIconS = defaultJDFElemIconS;
                else
                    jdfElemIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Input rRef Element="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    rRefInElemIcon = defaultRefInElemIcon;
                else
                    rRefInElemIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Input rRef Element (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    rRefInElemIconS = defaultRefInElemIconS;
                else
                    rRefInElemIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Output rRef Element="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    rRefOutElemIcon = defaultRefOutElemIcon;
                else
                    rRefOutElemIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("Output rRef Element (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    rRefOutElemIconS = defaultRefOutElemIconS;
                else
                    rRefOutElemIconS = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("rRef Element="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    rRefElemIcon = defaultRefElemIcon;
                else
                    rRefElemIcon = new ImageIcon(iconPath);
            }
            
            else if (iconName.equals("rRef Element (selected)="))
            {
                if (iconPath.equalsIgnoreCase("default"))
                    rRefElemIconS = defaultRefElemIconS;
                else
                    rRefElemIconS = new ImageIcon(iconPath);
            }
//          else if (iconName.equals("JDF Folder="))
//          {
//          if (iconPath.equalsIgnoreCase("default"))
//          execJDFIcon = defaultExecJDFIcon;
//          }
            
//          invElemIcon = defaultInvElemIcon;
//          invAttIcon = defaultInvAttIcon;
//          misElemIcon = defaultMisElemIcon;
//          misAttIcon = defaultMisAttIcon;
//          unkElemIcon = defaultUnkElemIcon;
//          unkAttIcon = defaultUnkAttIcon;
//          execJDFIcon = defaultExecJDFIcon;
//          invElemIconS = defaultInvElemIconS;
//          invAttIconS = defaultInvAttIconS;
//          misElemIconS = defaultMisElemIconS;
//          misAttIconS = defaultMisAttIconS;
//          unkElemIconS = defaultUnkElemIconS;
//          unkAttIconS = defaultUnkAttIconS;
        }
    }
    
    /**
     * Checks how many file paths the recentFiles String contains.
     * @return The number of previously opened files.
     */
    public int nrOfRecentFiles()
    {
        int nr = 0;
        if(recentFiles==null)
            return nr;
        
        while(nr < recentFiles.length && recentFiles[nr] != null)
            nr++;
        
        return nr;
    }
    /**
     * Checks if the file that is to be opened already exists in the recent files menu.
     * @param s - The path to the file
     * @return true if the path already is in the recent files menu; false otherwise.
     */
    public boolean pathNameExists(String s)
    {
        final int nrOfRecentFiles = nrOfRecentFiles();
        for (int i = 0; i < nrOfRecentFiles; i++)
        {
            if (s.equals(recentFiles[i]))
                return true;
        }
        return false;
    }
    
    /**
     * Updates the order in the recent files menu.
     * @param s     - The path to the file
     * @param exist - Do the path already exists?
     */
    void updateOrder(String s, boolean exist)
    {
        final String[] tmpFiles = new String[5];
        
        if (exist)
        {
            final int pos = pathNamePosition(s);
            if (pos > 0)
            {
                for (int i = 1; i < pos + 1; i++)
                {
                    tmpFiles[i] = recentFiles[i - 1];
                }
            }
        }
        else
        {
            if (nrOfRecentFiles() > 0)
            {
                for(int j = 1; j <= nrOfRecentFiles() && j < 5; j++)
                {
                    tmpFiles[j] = recentFiles[j - 1];
                }
            }
        }
        recentFiles[0] = s;
        int n = 1;
        while (n < 5 && tmpFiles[n] != null)
        {
            recentFiles[n] = tmpFiles[n];
            n++;
        }
    }
    
    /**
     * The position which the filepath has in the recent files menu.
     * @param s - The path to the file
     * @return The position in the m_recentFiles String[] as an integer.
     */
    public int pathNamePosition(String s)
    {
        for (int i = 0; i < nrOfRecentFiles(); i++)
        {
            if (s.equals(recentFiles[i]))
                return i;
        }
        return -1;
    }
    
    public void setLanguage(String _language)
    {
        setAttribute(language,_language);
    }
    
    private void setAttribute(String xPath, String attrib)
    {
        if(xDoc==null)
            xDoc=new XMLDoc("EditorIni",null);
        final KElement root=xDoc.getRoot();
        if(attrib==null)
        {
            root.removeXPathAttribute(xPath);
        }
        else
        {
            root.setXPathAttribute(xPath,attrib);    
        }
    }
    
    public void setLookAndFeel(String lnf)
    {
        setAttribute(lookAndFeel,lnf);
    }
    
    public void setIconStrings(String[] is)
    {
        iconStrings=is;
    }

    /**
     * @return
     */
    public boolean getLongID()
    {
        return getAttribute(longID,"true").equalsIgnoreCase("true") ? true : false;
    }
    public void setLongID(boolean b)
    {
        setAttribute(longID,b?"true":"false");
    }

    /**
     * @return
     */
    public boolean getGenerateFull()
    {
        return getAttribute(generateFull,"true").equalsIgnoreCase("true") ? true : false;
    }
    public void setGenerateFull(boolean b)
    {
        setAttribute(generateFull,b?"true":"false");
    }

    /**
     * @return
     */
    public boolean getNormalizeOpen()
    {
        return getAttribute(normalizeOpen,"true").equalsIgnoreCase("true") ? true : false;
    }

    /**
     * @param normalizeOpen2
     */
    public void setNormalizeOpen(boolean bNormalizeOpen)
    {
        setAttribute(normalizeOpen,bNormalizeOpen?"true":"false");        
    }

      /**
     * @return
     */
    public boolean getWarnCheck()
    {
        EnumValidationLevel level=getValidationLevel();
        return !EnumValidationLevel.isNoWarn(level);
    }

    /**
     * @param misURL
     */
    public void setMISURL(String _misURL)
    {
        setAttribute(misURL,_misURL);   
    }

    /**
     * @return
     */
    public String getMISURL()
    {
        return getAttribute(misURL,null);
    }

    /**
     * @param BaseLevel
     */
    public void setBaseLevel(int _baselevel)
    {
    	setAttribute(BaseLevel,String.valueOf(_baselevel));
    }

    /**
     * @return
     */
    public int getBaseLevel()
    {
    	String s = getAttribute(BaseLevel,"1");
    	return Integer.parseInt(s);
 
    }
    
    /**
     * @param MISLevel
     */
    public void setMISLevel(int _mislevel)
    {
    	setAttribute(MISLevel,String.valueOf(_mislevel));  
    }

    /**
     * @return
     */
    public int getMISLevel()
    {
    	String s = getAttribute(MISLevel,"1");
    	return Integer.parseInt(s);
    }
    
    /**
     * @param JMFLevel
     */
    public void setJMFLevel(int _jmflevel)
    {
    	setAttribute(JMFLevel,String.valueOf(_jmflevel));   
    }

    /**
     * @return
     */
    public int getJMFLevel()
    {
    	String s = getAttribute(JMFLevel,"1");
    	return Integer.parseInt(s);
    }
    
    @Override
    public String toString()
    {
        return xDoc==null ? "null ini file " : "INUReader: "+xDoc.toString();
    }

 
}
