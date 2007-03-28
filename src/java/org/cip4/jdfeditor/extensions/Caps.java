
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
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.JDFBaseDataTypes.EnumFitsValue;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;


/**
 * xjdf style lineart capabilities list
 * @author prosirai
 *
 */
public class Caps
{
    public static KElement createCaps(KElement xjdf, VString genericAtts)
    {
        XMLDoc d=new JDFDoc("Cap");
        KElement rootCap=d.getRoot();
        // grab all elements
        VElement vXJDFElements=xjdf.getChildrenByTagName(null, null, null, false, true, 0);
        vXJDFElements.add(xjdf);

        JDFDeviceCapGenerator dcgen=new JDFDeviceCapGenerator(null,null);
        JDFDoc dummy=new JDFDoc("DevCaps");
        KElement dummyRoot=dummy.getRoot();
        HashSet setDoneXPaths=new HashSet();
        for(int i=0;i<vXJDFElements.size();i++)
        {
            KElement e=vXJDFElements.item(i);
            String elmXPath=e.buildXPath(null,false);
            KElement dc=dummyRoot.appendElement("DevCap");
            // generate states as if this was a resource element
            dcgen.setStatesForAttributes(e, dc);
            VElement vStates=dc.getChildElementVector(null, null, new JDFAttributeMap("Name",""), true, 0, false);
            for(int j=0;j<vStates.size();j++)
            {
                KElement state = vStates.item(j);
                final String name = state.getAttribute("Name");
                final String xpath = genericAtts.contains(name) ? "@"+name :elmXPath+"/@"+name;
                if(!setDoneXPaths.contains(xpath))
                {
                    state=rootCap.moveElement(state, null);
                    state.setAttribute("XPath", xpath);
                    setDoneXPaths.add(xpath);
                }
            }
            dc.deleteNode();
        }
        
        return rootCap;
    }
    
    public static VString getBadAttributes(XMLDoc capFile, KElement xjdfRoot)
    {
        if(xjdfRoot==null)
            return null;
        
        VString vBadPaths=new VString();
        VElement vXJDFElements=xjdfRoot.getChildrenByTagName(null, null, null, false, true, 0);
        vXJDFElements.add(xjdfRoot);
        KElement capRoot=capFile.getRoot();
        VElement states =capRoot.getChildElementVector(null, null, new JDFAttributeMap("Name",""),true ,0,false);
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
                    vBadPaths.add(e.buildXPath(null, true)+"/"+attribute);
            }
            
        }
        return vBadPaths.isEmpty() ? null : vBadPaths;
    }
}
