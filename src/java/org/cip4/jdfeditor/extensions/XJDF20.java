
//Titel:        JDF TestApplication
//Version:
//Copyright:    Copyright (c) 1999
//Autor:       Sabine Jonas, sjonas@topmail.de
//Firma:      BU/GH Wuppertal
//Beschreibung:  first Applications using the JDFLibrary
//package testApps;

package org.cip4.jdfeditor.extensions;

import java.util.HashMap;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFCustomerInfo;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFNodeInfo;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.datatypes.VJDFAttributeMap;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.node.JDFSpawned;
import org.cip4.jdflib.node.JDFNode.EnumType;
import org.cip4.jdflib.pool.JDFAmountPool;
import org.cip4.jdflib.pool.JDFAncestorPool;
import org.cip4.jdflib.pool.JDFAuditPool;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFMerged;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.resource.JDFResource.EnumResourceClass;
import org.cip4.jdflib.resource.intent.JDFDropItemIntent;
import org.cip4.jdflib.resource.process.JDFComponent;
import org.cip4.jdflib.util.JDFSpawn;
import org.cip4.jdflib.util.StringUtil;

public class XJDF20
{


    private static String rootName = "XJDF";

    /**
     * @param node
     * @return
     */
    public static KElement makeNewJDF(JDFNode node, JDFNode rootIn)
    {
        JDFDoc newDoc=new JDFDoc(rootName);
        KElement newRoot=newDoc.getRoot();
        setRootAttributes(node, newRoot);
        setProduct(node,rootIn);
        setResources(newRoot, node,null,rootIn);
        setElements(node, newRoot);
        newRoot.eraseEmptyNodes(true);
        return newRoot;
    }
    /**
     * @param node
     * @return
     */
    public static KElement makeNewJDF(JDFNode node, VJDFAttributeMap vMap)
    {

        JDFNode root=node.getRoot();
        final JDFNode spawnedNode;
        if(root==node)
        {
            spawnedNode=node;
        }
        else
        {
            JDFSpawn spawn=new JDFSpawn(node);
            spawn.bCopyComments=false;
            spawn.bCopyCustomerInfo=false;
            spawn.bCopyNodeInfo=false;
            spawn.bSpawnROPartsOnly=true;
            spawn.vRWResources_in=null;
            spawn.vSpawnParts=vMap;
            spawnedNode=spawn.spawnInformative();
        }
        spawnedNode.fixVersion(EnumVersion.Version_1_3);
        return makeNewJDF(spawnedNode, root);
    }

    /**
     * @param node
     * @param newRoot
     */
    private static void setElements(JDFNode node, KElement newRoot)
    {
        setAudits(newRoot,node);
        VElement v=node.getChildElementVector(null, null, null, true, 0,false);
        for(int i=0;i<v.size();i++)
        {
            KElement e=v.item(i);
            if(e instanceof JDFResourceLinkPool)
                continue;
            if(e instanceof JDFResourcePool)
                continue;
            if(e instanceof JDFAncestorPool)
                continue;
            if(e instanceof JDFAuditPool)
                continue;
            if(e instanceof JDFNode)
                continue;
            newRoot.copyElement(e, null);
        }
    }

    /**
     * @param newRoot
     * @param node
     */
    private static void setAudits(KElement newRoot, JDFNode node)
    {
        JDFAuditPool ap=node.getAuditPool();
        if(ap==null)
            return;
        VElement audits=ap.getAudits(null, null,null);
        KElement newPool=newRoot.appendElement("AuditPool");
        int n=0;
        for(int i=0;i<audits.size();i++)
        {
            JDFAudit audit=(JDFAudit) audits.elementAt(i);
            if(audit instanceof JDFSpawned)
                continue;
            if(audit instanceof JDFMerged)
                continue;
            newPool.copyElement(audit, null);
            n++;
        }
        if(n==0)
            newPool.deleteNode(); 
    }

    /**
     * @param node
     * @param rootIn
     */
    private static String setProduct(JDFNode node, JDFNode rootIn)
    {
        if(rootIn==null)
            return null;
        if(!rootIn.getType().equals("Product"))
            return null;
        KElement list=node.getCreateElement("ProductList");
        KElement product=list.appendElement("Product");
        product.setAttributes(rootIn);
        setProductResources(product,rootIn);
        VElement subProducts=rootIn.getvJDFNode("Product", null, true);
        for(int i=0;i<subProducts.size();i++)
        {
            String childID=setProduct(node, (JDFNode)subProducts.elementAt(i));
            product.appendAttribute("Children", childID, null, " ", true);
        }
        return product.getAttribute("ID");
    }

    /**
     * @param product
     * @param rootIn
     */
    private static void setProductResources(KElement product, JDFNode rootIn)
    {
        VElement prodLinks=rootIn.getResourceLinks(null);
        HashMap componentMap=new HashMap();
        for(int i=prodLinks.size()-1;i>=0;i--)
        {
            JDFResourceLink rl=(JDFResourceLink) prodLinks.elementAt(i);
            final JDFResource linkRoot = rl.getLinkRoot();
            if(linkRoot instanceof JDFNodeInfo)
            {
                prodLinks.remove(i);
            }
            if(linkRoot instanceof JDFCustomerInfo)
            {
                prodLinks.remove(i);
            }
            if(linkRoot instanceof JDFComponent)
            {
                prodLinks.remove(i);
                if(EnumUsage.Output.equals(rl.getUsage()))
                {
                    linkRoot.setAttribute("tmp_id",linkRoot.getID());
                    componentMap.put(linkRoot.getID(), rootIn.getID());
                }
            }
        }
        setResources(product,rootIn, prodLinks,null);
        VElement vDropItems=product.getChildrenByTagName(ElementName.DROPITEMINTENT, null, null, false, true, 0);
        for(int i=0;i<vDropItems.size();i++)
        {
            final JDFDropItemIntent dropItemIntent = (JDFDropItemIntent) vDropItems.item(i);
            JDFComponent c=dropItemIntent.getComponent();
            if(c!=null)
            {
                String id=(String) componentMap.get(c.getAttribute("tmp_id", null, ""));
                if(id!=null)
                {
                    dropItemIntent.setAttribute("ProductRef", id);
                    c.deleteNode();
                }
            }
        }
    }

    /**
     * @param product
     * @param nodeIn
     * @return
     */
    private static void setResources(KElement newRoot, JDFNode nodeIn, VElement resLinks, JDFNode rootIn)
    {
        VElement vResLinks=resLinks==null ? nodeIn.getResourceLinks(null) : resLinks;
        if(vResLinks==null)
            return;
        boolean bProduct=EnumType.Product.equals(nodeIn.getEnumType());

        for(int i=0;i<vResLinks.size();i++)
        {
            JDFResourceLink rl=(JDFResourceLink) vResLinks.elementAt(i);
            final JDFResource linkTarget = rl.getLinkRoot();
            if(bProduct&&linkTarget instanceof JDFComponent)
                continue;

            setResource(newRoot, rl, linkTarget,rootIn);
        }
        return;
    }

    /**
     * @param newRoot
     * @param rl
     * @param linkTarget
     */
    private static void setResource(KElement newRoot, JDFResourceLink rl, final JDFResource linkTarget, JDFNode rootIn)
    {
        String className=getClassName(linkTarget);
        if(className==null)
            return;
        
        KElement resourceSet=newRoot.appendElement(className+"Set");

        setLinkAttributes(resourceSet, rl, linkTarget, rootIn);

        VElement vRes=rl.getTargetVector(0);
        int dot=0;
        String resID=linkTarget.getID();
        for(int j=0;j<vRes.size();j++)
        {
            JDFResource r=(JDFResource)vRes.elementAt(j);
            VElement vLeaves=r.getLeaves(false);
            for(int k=0;k<vLeaves.size();k++)
            {
                JDFResource leaf=(JDFResource)vLeaves.elementAt(k);
                //TODO this is just a quick hack - generating true id, idref pairs would be better
                leaf.inlineRefElements(null, null, false);
                KElement newLeaf=resourceSet.appendElement(className);
                setLeafAttributes(leaf, rl, newLeaf);
                newLeaf.setAttribute("ID", resID+"."+StringUtil.formatInteger(dot++));
            }
        }
    }

    /**
     * @param leaf
     * @param newLeaf
     */
    private static void setLeafAttributes(JDFResource leaf, JDFResourceLink rl, KElement newLeaf)
    {
        JDFAttributeMap partMap=leaf.getPartMap();
        //                   JDFAttributeMap attMap=leaf.getAttributeMap();
        //                   attMap.remove("ID");
        JDFAmountPool ap=rl.getAmountPool();
        if(ap!=null)
        {
            VElement vPartAmounts=ap.getMatchingPartAmountVector(partMap); 
            if(vPartAmounts!=null)
            {
                KElement amountPool=newLeaf.appendElement("AmountPool");
                for(int i=0;i<vPartAmounts.size();i++)
                    amountPool.copyElement(vPartAmounts.item(i), null);
            }
        }
        if(partMap!=null &&partMap.size()>0)    
        {
            newLeaf.appendElement("Part").setAttributes(partMap);
            //                     attMap.removeKeys(partMap.keySet());
        }

        KElement newResLeaf=newLeaf.copyElement(leaf, null);
        newResLeaf.removeAttribute(AttributeName.ID);
        newResLeaf.removeAttribute(AttributeName.CLASS);
        newResLeaf.removeAttribute(AttributeName.SPAWNID);
        newResLeaf.removeAttribute(AttributeName.SPAWNIDS);
        newResLeaf.removeAttribute(AttributeName.SPAWNSTATUS);
        newResLeaf.removeAttribute(AttributeName.PARTUSAGE);
        newResLeaf.removeAttribute(AttributeName.LOCKED);

        //TODO complete list
        newLeaf.moveAttribute(AttributeName.DESCRIPTIVENAME, newResLeaf, null, null, null);
        newLeaf.moveAttribute(AttributeName.AGENTNAME, newResLeaf, null, null, null);
        newLeaf.moveAttribute(AttributeName.AGENTVERSION, newResLeaf, null, null, null);
        newLeaf.moveAttribute(AttributeName.STATUS, newResLeaf, null, null, null);
        newLeaf.moveAttribute(AttributeName.AGENTVERSION, newResLeaf, null, null, null);
    }

    /**
     * @param r
     */
    private static String getClassName(JDFResource r)
    {
        if(r==null)
            return null;
        final EnumResourceClass resourceClass = r.getResourceClass();
        if(resourceClass==null)
            return null;
        String className="Resource";
        if(resourceClass.equals(EnumResourceClass.Parameter)||resourceClass.equals(EnumResourceClass.Intent))
            className=resourceClass.getName();
        if(resourceClass.equals(EnumResourceClass.PlaceHolder))
            return null;
        return className;
    }

    /**
     * @param newRoot
     * @param rl
     */
    private static void setLinkAttributes(KElement resourceSet, KElement rl, JDFResource linkRoot, JDFNode rootIn)
    {
        resourceSet.setAttribute("Name",linkRoot.getNodeName());
        resourceSet.copyAttribute("ID", linkRoot, null, null, null);
        resourceSet.setAttributes(rl);
        resourceSet.removeAttribute(AttributeName.RREF);
        resourceSet.removeAttribute(AttributeName.RSUBREF);
        if(rl instanceof JDFResourceLink)
        {
            JDFResourceLink resLink=(JDFResourceLink)rl;

            JDFResource resInRoot=rootIn==null ? linkRoot: (JDFResource)rootIn.getChildWithAttribute(null, "ID", null, resLink.getrRef(), 0, false);
            if(resInRoot!=null)
            {
                VElement vCreators=resInRoot.getCreator(EnumUsage.Input.equals(resLink.getUsage()));
                final int size = vCreators==null ? 0 : vCreators.size();
                for( int i=0;i<size;i++)
                {
                    JDFNode depNode=(JDFNode) vCreators.elementAt(i);
                    KElement dependent=resourceSet.appendElement("Dependent");
                    dependent.setAttribute(AttributeName.JOBID, depNode.getJobID(true));
                    dependent.copyAttribute(AttributeName.JMFURL, depNode, null, null, null);
                    dependent.copyAttribute(AttributeName.JOBPARTID, depNode, null, null, null);
                }
            }
        }
    }

    /**
     * @param node
     * @param newRoot
     */
    private static void setRootAttributes(JDFNode node, KElement newRoot)
    {
        //newRoot.appendXMLComment("Very preliminary experimental prototype trial version: using: "+JDFAudit.getStaticAgentName()+" "+JDFAudit.getStaticAgentVersion());
        newRoot.setAttributes(node);
    }

    /**
     * calculate a file extension name based of rootName
     * @return String
     */
    public static String getExtension()
    {
        return rootName.toLowerCase();
    }


}
