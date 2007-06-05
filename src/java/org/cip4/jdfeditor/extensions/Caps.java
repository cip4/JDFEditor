
//Titel:        JDF TestApplication
//Version:
//Copyright:    Copyright (c) 1999
//Autor:       Sabine Jonas, sjonas@topmail.de
//Firma:      BU/GH Wuppertal
//Beschreibung:  first Applications using the JDFLibrary
//package testApps;

package org.cip4.jdfeditor.extensions;

import java.util.HashSet;

import org.cip4.jdfeditor.JDFDeviceCapGenerator;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.JDFBaseDataTypes.EnumFitsValue;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;
import org.cip4.jdflib.resource.devicecapability.JDFAction;
import org.cip4.jdflib.resource.devicecapability.JDFActionPool;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFTest;
import org.cip4.jdflib.resource.devicecapability.JDFTestPool;


/**
 * xjdf style lineart capabilities list
 * @author prosirai
 *
 */
public class Caps
{
    // experimental simple capabilities file
    public static KElement createCaps(KElement xjdf, VString genericAtts)
    {
        XMLDoc d=new JDFDoc("Cap");
        KElement rootCap=d.getRoot();
        JDFActionPool ap=(JDFActionPool) rootCap.appendElement("ActionPool");
        JDFTestPool tp=(JDFTestPool) rootCap.appendElement("TestPool");
        JDFAction a=ap.appendAction();
        JDFTest t=tp.appendTest();
        a.setTest(t);
            
        // grab all elements
        VElement vXJDFElements=xjdf.getChildrenByTagName(null, null, null, false, true, 0);
        vXJDFElements.add(xjdf);
        JDFDevCap dcGeneric=(JDFDevCap) rootCap.appendElement(ElementName.DEVCAP);
        JDFDevCap dcDummy=(JDFDevCap) rootCap.appendElement(ElementName.DEVCAP);
        dcGeneric.setAttribute(AttributeName.XPATH, "//*"); /// todo what is xpath for any

        JDFDeviceCapGenerator dcgen=new JDFDeviceCapGenerator(null,null);
        HashSet setDoneXPaths=new HashSet();
        for(int i=0;i<vXJDFElements.size();i++)
        {
            KElement e=vXJDFElements.item(i);
            
            String elmXPathFull=e.buildXPath(null,0);
            String elmXPath=elmXPathFull.substring(0, elmXPathFull.lastIndexOf("/"));
            JDFAttributeMap map=new JDFAttributeMap(AttributeName.XPATH, elmXPath);
            map.put("Name",e.getNodeName());
            JDFDevCap dc;
            if(setDoneXPaths.contains(elmXPathFull))
            {
                dc=(JDFDevCap) rootCap.getChildByTagName(null, null, 0, map, true, true);
            }
            else
            {
                dc=(JDFDevCap) rootCap.appendElement("DevCap");
                dc.setMinOccurs(0);
                dc.setMaxOccurs(Integer.MAX_VALUE);
                dc.setAttributes(map);
                setDoneXPaths.add(elmXPathFull);
            }
            // generate states 
            dcgen.setStatesForAttributes(e, dcDummy);
            VElement vStates=dcDummy.getChildElementVector(null, null, new JDFAttributeMap("Name",""), true, 0, false);
            for(int j=0;j<vStates.size();j++)
            {
                JDFAbstractState state = (JDFAbstractState) vStates.item(j);
                final String name = state.getAttribute("Name");
                final boolean isGeneric = genericAtts.contains(name);
                final String fullpath = isGeneric ? "@"+name :elmXPathFull+"/@"+name;
                KElement newRoot=isGeneric ? dcGeneric : dc;
                if(!setDoneXPaths.contains(fullpath))
                {
                    state=(JDFAbstractState) newRoot.moveElement(state, null);
                    state.setAttribute("XPath", "@"+name);
                    setDoneXPaths.add(fullpath);
                }
                else
                {
                    state.deleteNode();
                    state=(JDFAbstractState) newRoot.getChildWithAttribute(null, "XPath", null, "@"+name, 0, true);
                    state.addValue(e.getAttribute(name),EnumFitsValue.Present);
                }
            }
        }        
        dcDummy.deleteNode();
        return rootCap;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////
    
    public static VString getBadAttributes(XMLDoc capFile, KElement xjdfRoot)
    {
        if(xjdfRoot==null)
            return null;
        
        VString vBadPaths=new VString();
        VElement vXJDFElements=xjdfRoot.getChildrenByTagName(null, null, null, false, true, 0);
        vXJDFElements.add(xjdfRoot);
        KElement capRoot=capFile.getRoot();
        VElement devcaps =capRoot.getChildElementVector(null, null, new JDFAttributeMap("Name",""),true ,0,false);
        VElement states=new VElement();
        for (int i = 0; i < devcaps.size(); i++)
        {
            states.addAll(devcaps.item(i).getChildElementVector(null, null, new JDFAttributeMap("Name",""),true ,0,false));
        }
        for (int i = 0; i < vXJDFElements.size(); i++)
        {
            KElement e = vXJDFElements.item(i);
            VString keys = e.getAttributeVector();
            for(int j=0;j<keys.size();j++)
            {
                String attribute=keys.stringAt(j);
                boolean bFound=false;
                for(int k=0;k<states.size();k++)
                {
                    if(! (states.item(k) instanceof JDFAbstractState))
                            continue; // its a devcap - move on
                    JDFAbstractState state=(JDFAbstractState) states.item(k);
                    if(attribute.equals(state.getAttribute("Name")))
                    {
                        if(e.hasXPathNode(state.getAttribute("XPath")))
                        {
                            if(state.fitsValue(e.getAttribute(attribute), EnumFitsValue.Allowed))
                            {
                                bFound=true;
                                break;
                            }
                        }
                    }
                }
                if(!bFound)
                    vBadPaths.add(e.buildXPath(null, 1)+"/"+attribute);
            }
            
        }
        return vBadPaths.isEmpty() ? null : vBadPaths;
    }
}
