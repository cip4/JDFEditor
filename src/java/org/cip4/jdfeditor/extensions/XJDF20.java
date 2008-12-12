//Titel:        JDF TestApplication
//Version:
//Copyright:    Copyright (c) 1999
//Autor:       Sabine Jonas, sjonas@topmail.de
//Firma:      BU/GH Wuppertal
//Beschreibung:  first Applications using the JDFLibrary
//package testApps;

package org.cip4.jdflib.extensions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFCustomerInfo;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFException;
import org.cip4.jdflib.core.JDFNodeInfo;
import org.cip4.jdflib.core.JDFPartAmount;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
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

	private static boolean bInit = false;
	public static String rootName = "XJDF";
	private static final String m_spawnInfo = "SpawnInfo";
	private static VString resAttribs;

	/**
	 * @param node
	 * @return
	 */
	public static KElement makeNewJDF(JDFNode node, JDFNode rootIn)
	{
		init();
		JDFDoc newDoc = new JDFDoc(rootName);
		KElement newRoot = newDoc.getRoot();
		setRootAttributes(node, newRoot);
		setProduct(node, rootIn);
		setResources(newRoot, node, null, rootIn);
		setElements(node, newRoot);
		newRoot.eraseEmptyNodes(true);
		newRoot.removeAttribute(AttributeName.ACTIVATION);

		return newRoot;
	}

	/**
	 * 
	 */
	private static void init()
	{
		if (bInit)
			return;
		bInit = true;
		JDFResourcePool dummyResPool = (JDFResourcePool) new JDFDoc("ResourcePool").getRoot();
		JDFResource intRes = dummyResPool.appendResource("intent", EnumResourceClass.Intent, null);
		JDFResource physRes = dummyResPool.appendResource("physical", EnumResourceClass.Consumable, null);
		JDFResource paramRes = dummyResPool.appendResource("param", EnumResourceClass.Parameter, null);
		resAttribs = paramRes.knownAttributes();
		resAttribs.appendUnique(physRes.knownAttributes());
		resAttribs.appendUnique(intRes.knownAttributes());
	}

	/**
	 * @param node
	 * @return
	 */
	public static KElement makeNewJDF(JDFNode node, VJDFAttributeMap vMap)
	{

		JDFNode root = node.getRoot();
		final JDFNode spawnedNode;
		if (root == node)
		{
			spawnedNode = node;
		}
		else
		{
			JDFSpawn spawn = new JDFSpawn(node);
			spawn.bCopyComments = false;
			spawn.bCopyCustomerInfo = false;
			spawn.bCopyNodeInfo = false;
			spawn.bSpawnROPartsOnly = true;
			spawn.vRWResources_in = null;
			spawn.vSpawnParts = vMap;
			spawnedNode = spawn.spawnInformative();
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
		setAudits(newRoot, node);
		VElement v = node.getChildElementVector(null, null, null, true, 0, false);
		for (int i = 0; i < v.size(); i++)
		{
			KElement e = v.item(i);
			if (e instanceof JDFResourceLinkPool)
				continue;
			if (e instanceof JDFResourcePool)
				continue;
			if (e instanceof JDFAncestorPool)
				continue;
			if (e instanceof JDFAuditPool)
				continue;
			if (e instanceof JDFNode)
				continue;
			if (e.getLocalName().equals("ProductList"))
			{
				newRoot.moveElement(e, null);
				continue;
			}
			newRoot.copyElement(e, null);
		}
	}

	/**
	 * @param newRoot
	 * @param node
	 */
	private static void setAudits(KElement newRoot, JDFNode node)
	{
		JDFAuditPool ap = node.getAuditPool();
		if (ap == null)
			return;
		VElement audits = ap.getAudits(null, null, null);
		KElement newPool = newRoot.appendElement("AuditPool");
		int n = 0;
		for (int i = 0; i < audits.size(); i++)
		{
			JDFAudit audit = (JDFAudit) audits.elementAt(i);
			if (audit instanceof JDFSpawned)
				continue;
			if (audit instanceof JDFMerged)
				continue;
			newPool.copyElement(audit, null);
			n++;
		}
		if (n == 0)
			newPool.deleteNode();
	}

	/**
	 * @param node
	 * @param rootIn
	 */
	private static String setProduct(JDFNode node, JDFNode rootIn)
	{
		if (rootIn == null)
			return null;
		if (!rootIn.getType().equals("Product"))
			return null;
		KElement list = node.getCreateElement("ProductList");
		KElement product = list.appendElement("Product");
		product.setAttributes(rootIn);
		setProductResources(product, rootIn);
		VElement subProducts = rootIn.getvJDFNode("Product", null, true);
		for (int i = 0; i < subProducts.size(); i++)
		{
			String childID = setProduct(node, (JDFNode) subProducts.elementAt(i));
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
		VElement prodLinks = rootIn.getResourceLinks(null);
		HashMap componentMap = new HashMap();
		for (int i = prodLinks.size() - 1; i >= 0; i--)
		{
			JDFResourceLink rl = (JDFResourceLink) prodLinks.elementAt(i);
			final JDFResource linkRoot = rl.getLinkRoot();
			if (linkRoot instanceof JDFNodeInfo)
			{
				prodLinks.remove(i);
			}
			if (linkRoot instanceof JDFCustomerInfo)
			{
				prodLinks.remove(i);
			}
			if (linkRoot instanceof JDFComponent)
			{
				prodLinks.remove(i);
				if (EnumUsage.Output.equals(rl.getUsage()))
				{
					linkRoot.setAttribute("tmp_id", linkRoot.getID());
					componentMap.put(linkRoot.getID(), rootIn.getID());
				}
			}
		}
		setResources(product, rootIn, prodLinks, null);
		VElement vDropItems = product.getChildrenByTagName(ElementName.DROPITEMINTENT, null, null, false, true, 0);
		for (int i = 0; i < vDropItems.size(); i++)
		{
			final JDFDropItemIntent dropItemIntent = (JDFDropItemIntent) vDropItems.item(i);
			JDFComponent c = dropItemIntent.getComponent();
			if (c != null)
			{
				String id = (String) componentMap.get(c.getAttribute("tmp_id", null, ""));
				if (id != null)
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
		VElement vResLinks = resLinks == null ? nodeIn.getResourceLinks(null) : resLinks;
		if (vResLinks == null)
			return;
		boolean bProduct = EnumType.Product.equals(nodeIn.getEnumType());

		for (int i = 0; i < vResLinks.size(); i++)
		{
			JDFResourceLink rl = (JDFResourceLink) vResLinks.elementAt(i);
			final JDFResource linkTarget = rl.getLinkRoot();
			if (bProduct && linkTarget instanceof JDFComponent)
				continue;
			linkTarget.expand(false);
			setResource(newRoot, rl, linkTarget, rootIn);
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
		String className = getClassName(linkTarget);
		if (className == null)
			return;

		KElement resourceSet = newRoot.appendElement(className + "Set");

		setLinkAttributes(resourceSet, rl, linkTarget, rootIn);

		VElement vRes = rl.getTargetVector(0);
		int dot = 0;
		String resID = linkTarget.getID();
		for (int j = 0; j < vRes.size(); j++)
		{
			JDFResource r = (JDFResource) vRes.elementAt(j);
			VElement vLeaves = r.getLeaves(false);
			for (int k = 0; k < vLeaves.size(); k++)
			{
				JDFResource leaf = (JDFResource) vLeaves.elementAt(k);
				KElement newLeaf = resourceSet.appendElement(className);
				//TODO this is just a quick hack - generating true id, idref pairs would be better
				leaf.inlineRefElements(null, null, false);
				//                VElement vRefs=leaf.getRefElements();
				//                int refSize = vRefs==null ? 0 : vRefs.size();
				//                for(int kk=0;kk<refSize;kk++)
				//                {
				//                    JDFRefElement ref=(JDFRefElement) vRefs.elementAt(kk);
				//                    JDFResource refRes=ref.getTarget();
				//                    if(!refRes.hasAttribute_KElement("ID", null, false))
				//                    {
				//                        String newID=refRes.getID()+"."+StringUtil.formatInteger(dot++);
				//                        refRes.setAttribute("xjdf:partID", newID, "xjdf");
				//                        leaf.appendAttribute(ref.getLocalName(), newID, null, " ", false);
				//                    }
				//                    
				//                }
				newLeaf.setAttribute("ID", resID + "." + StringUtil.formatInteger(dot++));
				setLeafAttributes(leaf, rl, newLeaf);
			}
		}
	}

	/**
	 * @param leaf
	 * @param newLeaf
	 */
	private static void setLeafAttributes(JDFResource leaf, JDFResourceLink rl, KElement newLeaf)
	{
		JDFAttributeMap partMap = leaf.getPartMap();
		//                   JDFAttributeMap attMap=leaf.getAttributeMap();
		//                   attMap.remove("ID");
		setAmountPool(rl, newLeaf, partMap);
		if (partMap != null && partMap.size() > 0)
		{
			newLeaf.appendElement("Part").setAttributes(partMap);
			//                     attMap.removeKeys(partMap.keySet());
		}

		KElement newResLeaf = newLeaf.copyElement(leaf, null);
		newResLeaf.removeAttributes(leaf.getPartIDKeys());
		newResLeaf.removeAttribute(AttributeName.ID);
		newResLeaf.removeAttribute(AttributeName.CLASS);
		newResLeaf.removeAttribute(AttributeName.PARTUSAGE);
		newResLeaf.removeAttribute(AttributeName.LOCKED);

		for (int i = 0; i < resAttribs.size(); i++)
		{
			if (newResLeaf.hasAttribute(resAttribs.stringAt(i)))
				newLeaf.moveAttribute(resAttribs.stringAt(i), newResLeaf, null, null, null);
		}

		VElement allNewKids = newResLeaf.getChildrenByTagName(null, null, null, false, true, 0);
		for (int j = 0; j < allNewKids.size(); j++)
		{
			KElement kj = allNewKids.item(j);
			if (kj instanceof JDFResource)
			{
				for (int i = 0; i < resAttribs.size(); i++)
				{
					kj.removeAttribute(resAttribs.stringAt(i));
				}
			}
		}

		// retain spawn informatiom
		if (leaf.hasAttribute(AttributeName.SPAWNIDS))
		{
			KElement spawnInfo = newLeaf.getDocRoot().getCreateElement(m_spawnInfo, null, 0);
			KElement spawnID = spawnInfo.appendElement("SpawnID");
			spawnID.moveAttribute(AttributeName.SPAWNIDS, newLeaf, null, null, null);
			spawnID.moveAttribute(AttributeName.SPAWNSTATUS, newLeaf, null, null, null);
			spawnID.copyAttribute(AttributeName.RESOURCEID, newLeaf, AttributeName.ID, null, null);
		}
	}

	private static void setAmountPool(JDFResourceLink rl, KElement newLeaf, JDFAttributeMap partMap)
	{
		JDFAmountPool ap = rl.getAmountPool();
		if (ap != null)
		{
			VElement vPartAmounts = ap.getMatchingPartAmountVector(partMap);
			if (vPartAmounts != null)
			{
				for (int i = 0; i < vPartAmounts.size(); i++)
				{
					JDFPartAmount pa = (JDFPartAmount) vPartAmounts.item(i);
					JDFAttributeMap map = pa.getPartMap();
					map.removeKeys(partMap.keySet());
					if (map.isEmpty()) // no further subdevision - simply blast into leaf
					{
						newLeaf.setAttributes(pa);
					}
					else if (map.size() == 1 && map.containsKey(AttributeName.CONDITION))
					{
						JDFAttributeMap attMap = pa.getAttributeMap();
						Iterator it = attMap.getKeyIterator();
						String condition = map.get(AttributeName.CONDITION);
						while (it.hasNext())
						{
							String key = (String) it.next();
							//                            if(key.indexOf(AttributeName.AMOUNT)>0)
							//                            {
							newLeaf.setAttribute(key + condition, attMap.get(key));
							//                            }
						}
					}
					else
					// retain ap
					{
						KElement amountPool = newLeaf.getCreateElement("AmountPool");
						pa = (JDFPartAmount) amountPool.copyElement(pa, null);
						pa.setPartMap(map);
					}
					// TODO special handling for condition
				}
			}
		}
	}

	/**
	 * @param r
	 */
	private static String getClassName(JDFResource r)
	{
		if (r == null)
			return null;
		final EnumResourceClass resourceClass = r.getResourceClass();
		if (resourceClass == null)
			return null;
		String className = "Resource";
		if (resourceClass.equals(EnumResourceClass.Parameter) || resourceClass.equals(EnumResourceClass.Intent))
			className = resourceClass.getName();
		if (resourceClass.equals(EnumResourceClass.PlaceHolder))
			return null;
		return className;
	}

	/**
	 * @param newRoot
	 * @param rl
	 */
	private static void setLinkAttributes(KElement resourceSet, KElement rl, JDFResource linkRoot, JDFNode rootIn)
	{
		resourceSet.setAttribute("Name", linkRoot.getNodeName());
		resourceSet.copyAttribute("ID", linkRoot, null, null, null);
		resourceSet.setAttributes(rl);
		resourceSet.removeAttribute(AttributeName.RREF);
		resourceSet.removeAttribute(AttributeName.RSUBREF);
		if (rl instanceof JDFResourceLink)
		{
			JDFResourceLink resLink = (JDFResourceLink) rl;

			JDFResource resInRoot = rootIn == null ? linkRoot : (JDFResource) rootIn.getChildWithAttribute(null, "ID", null, resLink.getrRef(), 0, false);
			if (resInRoot != null)
			{
				VElement vCreators = resInRoot.getCreator(EnumUsage.Input.equals(resLink.getUsage()));
				final int size = vCreators == null ? 0 : vCreators.size();
				for (int i = 0; i < size; i++)
				{
					JDFNode depNode = (JDFNode) vCreators.elementAt(i);
					KElement dependent = resourceSet.appendElement("Dependent");
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
		newRoot.appendXMLComment("Very preliminary experimental prototype trial version: using: "
				+ JDFAudit.getStaticAgentName() + " " + JDFAudit.getStaticAgentVersion(), null);
		newRoot.setAttributes(node);
		if (!newRoot.hasAttribute(AttributeName.TYPES))
			newRoot.renameAttribute("Type", "Types", null, null);
		if (newRoot.hasAttribute(AttributeName.SPAWNID))
		{
			KElement spawnInfo = newRoot.appendElement(m_spawnInfo, "www.cip4.org/SpawnInfo");
			spawnInfo.moveAttribute(AttributeName.SPAWNID, newRoot, null, null, null);
			final JDFAncestorPool ancestorPool = node.getAncestorPool();
			if (ancestorPool != null)
			{
				VJDFAttributeMap vParts = ancestorPool.getPartMapVector();
				int size = vParts == null ? 0 : vParts.size();
				for (int i = 0; i < size; i++)
				{
					spawnInfo.appendElement(ElementName.PART).setAttributes(vParts.elementAt(i));
				}
			}
		}
	}

	/**
	 * calculate a file extension name based of rootName
	 * @return String
	 */
	public static String getExtension()
	{
		return rootName.toLowerCase();
	}

	public static void saveZip(String fileName, JDFNode rootNode, boolean replace)
	{
		File file = new File(fileName);
		if (file.canRead())
		{
			if (replace)
			{
				file.delete();
			}
			else
				throw new JDFException("output file exists: " + file.getPath());
		}
		//file.createNewFile(fileName);

		try
		{
			VElement v = rootNode.getvJDFNode(null, null, false);
			FileOutputStream fos = new FileOutputStream(fileName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (int i = 0; i < v.size(); i++)
			{
				JDFNode n = (JDFNode) v.elementAt(i);
				String nam = n.getJobPartID(false);
				if (nam == "")
					nam = "Node" + i;
				try
				{
					nam += "." + rootName;
					ZipEntry ze = new ZipEntry(nam);
					zos.putNextEntry(ze);
					KElement newRoot = makeNewJDF(n, n.getRoot());
					newRoot.getOwnerDocument_KElement().write2Stream(zos, 2, true);
					zos.closeEntry();

				}
				catch (ZipException x)
				{
					// TODO Auto-generated catch block
					x.printStackTrace();
				}
				catch (IOException x)
				{
					// TODO Auto-generated catch block
					x.printStackTrace();
				}
			}
			zos.close();
		}
		catch (IOException x)
		{
			// TODO Auto-generated catch block
			x.printStackTrace();
		}
	}
}
