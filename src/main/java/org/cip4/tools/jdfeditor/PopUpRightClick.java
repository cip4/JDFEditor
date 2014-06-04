/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2012 The International Cooperation for the Integration of 
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
package org.cip4.tools.jdfeditor;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.extensions.XJDF20;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;
import org.cip4.jdflib.jmf.JDFMessageService;
import org.cip4.jdflib.jmf.JMFBuilder;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.devicecapability.JDFDeviceCap;
import org.cip4.tools.jdfeditor.dialog.SaveAsJDFDialog;
import org.cip4.tools.jdfeditor.dialog.SaveAsXJDFDialog;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class to implement all the menu bar and menu related stuff moved here from JDFFrame
 * @author prosirai
 * 
 */
public class PopUpRightClick extends JPopupMenu implements ActionListener
{
	private SettingService settingService = new SettingService();

	private static final long serialVersionUID = -8488973695389593826L;

	private final JMenuItem m_copyPopupItem;
	private final JMenuItem m_renamePopupItem;
	private final JMenuItem m_modifyAttrValuePopupItem;
	private final JMenuItem m_requiredAttrPopupItem;
	private final JMenuItem m_requiredElemPopupItem;
	private final JMenuItem m_xpandPopupItem;
	private final JMenuItem m_collapsePopupItem;
	private final JMenuItem m_copyToClipBoardPopupItem;
	private final JMenuItem m_insertElemBeforePopupItem;
	private final JMenuItem m_insertElemAfterPopupItem;
	private final JMenuItem m_insertElemIntoPopupItem;
	private final JMenuItem m_insertTextPopupItem;
	private final JMenuItem m_insertInResLinkPopupItem;
	private final JMenuItem m_insertOutResLinkPopupItem;
	private final JMenuItem m_insertInResPopupItem;
	private final JMenuItem m_insertOutResPopupItem;
	private final JMenuItem m_insertResPopupItem;
	private final JMenuItem m_insertAttrPopupItem;
	private final JMenuItem m_cutPopupItem;
	private final JMenuItem m_pastePopupItem;
	private final JMenuItem m_pasteRawPopupItem;
	private final JMenuItem m_deletePopupItem;
	private final JMenuItem m_targetItem;
	private JMenuItem m_saveXJDF = null;
	private JMenuItem m_saveJDF = null;
	private JMenuItem m_nodeFromCaps = null;
	private JMenuItem m_normalize = null;
	private JMenuItem m_saveXJDFCaps = null;
	private JMenuItem m_sendMessage = null;
	private JMenuItem m_spawn = null;
	private JMenuItem m_unspawn = null;

	private class MessageSender
	{

		JDFMessageService mService;

		/**
		 * @param ms
		 */
		public MessageSender(final JDFMessageService ms)
		{
			mService = ms;
		}

		/**
		 * 
		 */
		public void sendJMF()
		{
			final boolean b = generateDoc();
			if (b)
			{
				final SendToDevice sendTo = new SendToDevice();
				sendTo.trySend();
			}

		}

		/**
		 * @return
		 */
		private boolean generateDoc()
		{
			final Vector<EnumFamily> vf = mService.getFamilies();
			if (vf == null || vf.size() == 0)
			{
				return false;
			}
			EnumFamily f = vf.get(0);
			if (vf.contains(EnumFamily.Command))
			{
				f = EnumFamily.Command;
			}


            JMFBuilder b = new JMFBuilder();
            b.setSenderID("JDFEditor");

			final JDFJMF jmf = b.newJMF(f, mService.getType());
			JDFDoc doc = jmf.getOwnerDocument_JDFElement();
			doc.setOriginalFileName("Auto" + mService.getType() + ".jmf");
			final JDFMessage m = jmf.getMessageElement(f, EnumType.getEnum(mService.getType()), 0);
			extendMessage(m);
			MainView.getFrame().setJDFDoc(doc, null);
			MainView.getTreeArea().drawTreeView(MainView.getEditorDoc());

			return true;
		}

		/**
		 * @param m
		 */
		private void extendMessage(final JDFMessage m)
		{
			final EnumType t = m == null ? null : EnumType.getEnum(m.getType());
			if (t == null || m == null)
			{
				return;
			}
			if (EnumType.AbortQueueEntry.equals(t))
			{
				m.appendQueueEntryDef();
			}
			else if (EnumType.HoldQueueEntry.equals(t))
			{
				m.appendQueueEntryDef();
			}
			else if (EnumType.RemoveQueueEntry.equals(t))
			{
				m.appendQueueEntryDef();
			}

		}

	}

	/**
	 * Creates the popupmenu after a right mouse click on node in the Tree View.
	 * @param path - The path to the clicked node
	 */
	public PopUpRightClick(final TreePath path)
	{
		super();
		final JSeparator separator = new JSeparator();

		final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();
		final KElement elem = (node.isElement()) ? node.getElement() : null;

		final JMenu insertPopupMenu = new JMenu(ResourceUtil.getMessage("InsertElKey"));
		insertPopupMenu.setEnabled(elem != null);

		final JMenuItem xpath = new JMenuItem(node.getXPath());
		xpath.setBackground(Color.YELLOW);
		add(xpath);
		final JMenuItem size = new JMenuItem("Size: " + (elem == null ? node.toDisplayString().length() : elem.toString().length()));
		add(size);
		add(separator);

		m_insertElemBeforePopupItem = new JMenuItem(ResourceUtil.getMessage("BeforeKey"));
		m_insertElemBeforePopupItem.addActionListener(this);
		insertPopupMenu.add(m_insertElemBeforePopupItem);

		m_insertElemIntoPopupItem = new JMenuItem(ResourceUtil.getMessage("IntoKey"));
		m_insertElemIntoPopupItem.addActionListener(this);
		insertPopupMenu.add(m_insertElemIntoPopupItem);

		m_insertElemAfterPopupItem = new JMenuItem(ResourceUtil.getMessage("AfterKey"));
		m_insertElemAfterPopupItem.addActionListener(this);
		insertPopupMenu.add(m_insertElemAfterPopupItem);

		add(insertPopupMenu);

		final JMenu resMenu = new JMenu(ResourceUtil.getMessage("InsertResKey"));
		resMenu.setEnabled((elem instanceof JDFNode) || (elem instanceof JDFResourcePool));

		m_insertInResPopupItem = new JMenuItem(ResourceUtil.getMessage("InputResourceKey"));
		m_insertInResPopupItem.addActionListener(this);
		resMenu.add(m_insertInResPopupItem);

		m_insertOutResPopupItem = new JMenuItem(ResourceUtil.getMessage("OutputResourceKey"));
		m_insertOutResPopupItem.addActionListener(this);
		resMenu.add(m_insertOutResPopupItem);

		resMenu.add(separator);

		m_insertResPopupItem = new JMenuItem(ResourceUtil.getMessage("ResourceKey"));
		m_insertResPopupItem.addActionListener(this);
		resMenu.add(m_insertResPopupItem);

		add(resMenu);

		final JMenu resLinkMenu = new JMenu(ResourceUtil.getMessage("InsertResLinkKey"));
		resLinkMenu.setEnabled((elem instanceof JDFNode) || (elem instanceof JDFResourceLinkPool));

		m_insertInResLinkPopupItem = new JMenuItem(ResourceUtil.getMessage("ResourceInLinkKey"));
		m_insertInResLinkPopupItem.addActionListener(this);
		resLinkMenu.add(m_insertInResLinkPopupItem);

		m_insertOutResLinkPopupItem = new JMenuItem(ResourceUtil.getMessage("ResourceOutLinkKey"));
		m_insertOutResLinkPopupItem.addActionListener(this);
		resLinkMenu.add(m_insertOutResLinkPopupItem);

		add(resLinkMenu);

		m_insertAttrPopupItem = new JMenuItem(ResourceUtil.getMessage("InsertAttKey"));
		m_insertAttrPopupItem.addActionListener(this);
		add(m_insertAttrPopupItem);

		m_insertTextPopupItem = new JMenuItem(ResourceUtil.getMessage("InsertTextKey"));
		m_insertTextPopupItem.addActionListener(this);
		add(m_insertTextPopupItem);

		add(separator);

		m_cutPopupItem = addMenuItem("CutKey");
		// m_cutPopupItem.setAccelerator(KeyStroke.getKeyStroke('X', menuKeyMask));

		m_copyPopupItem = addMenuItem("CopyKey");
		// m_copyPopupItem.setAccelerator(KeyStroke.getKeyStroke('C', menuKeyMask));

		m_pastePopupItem = addMenuItem("PasteKey");
		m_pasteRawPopupItem = addMenuItem("PasteRawKey");
		// m_pastePopupItem.setAccelerator(KeyStroke.getKeyStroke('P', menuKeyMask));
		m_deletePopupItem = addMenuItem("DeleteKey");

		add(separator);

		m_renamePopupItem = addMenuItem("RenameKey");
		m_modifyAttrValuePopupItem = addMenuItem("ModifyAttValueKey");
		m_targetItem = addMenuItem("GotoTargetKey");
		add(separator);

		m_requiredAttrPopupItem = addMenuItem("AddRequiredAttKey");
		m_requiredElemPopupItem = addMenuItem("AddRequiredElKey");
		add(separator);

		// TODO add spawn
		if (elem instanceof JDFNode)
		{
			m_saveXJDF = addMenuItem("SaveXJDFKey");
			m_normalize = addMenuItem("NormalizeKey");
			m_spawn = addMenuItem("SpawnKey");
			m_unspawn = addMenuItem("UnspawnKey");

			add(separator);
		}
		else if (elem instanceof JDFDeviceCap)
		{
			m_nodeFromCaps = addMenuItem("NodeFromCapsKey");
			add(separator);
		}
		else if (elem instanceof JDFMessageService)
		{
			m_sendMessage = addMenuItem("SendJMF");
			add(separator);
		}
		else if (elem instanceof JDFJMF)
		{
			m_sendMessage = addMenuItem("SendJMF");
			m_saveXJDF = addMenuItem("SaveXJDFKey");
			add(separator);
		}
		else if (elem != null && XJDF20.rootName.equals(elem.getLocalName()))
		{
			m_saveXJDFCaps = addMenuItem("ExportToDevCapKey");
			m_saveJDF = addMenuItem("SaveJDFKey");
		}

		m_xpandPopupItem = addMenuItem("ExpandKey");
		m_collapsePopupItem = addMenuItem("CollapseKey");

		m_copyToClipBoardPopupItem = addMenuItem("CopyNode");
		setEnabledInMouseMenu(node, elem);
	}

	private JMenuItem addMenuItem(final String key)
	{
		final JMenuItem item = new JMenuItem(ResourceUtil.getMessage(key));
		item.addActionListener(this);
		add(item);
		return item;
	}

	/**
	 * Disables the MenuItems in the MouseMenu that isn't selectable for the selected JDFTreeNode.
	 * @param node - The selected node
	 * @param elem - The KElement for the selected node, can be null
	 */
	private void setEnabledInMouseMenu(final JDFTreeNode node, final KElement elem)
	{
		final JDFFrame m_frame = MainView.getFrame();
		final boolean isElement = node.isElement();
		if (isElement && elem != null)
		{
			if (!((JDFTreeNode) m_frame.getRootNode().getFirstChild()).equals(node))
			{
				m_insertElemBeforePopupItem.setEnabled(true);
				m_insertElemAfterPopupItem.setEnabled(true);

				if (elem.getTagName().equals("Comment"))
				{
					m_insertElemIntoPopupItem.setEnabled(false);
					// TODO insert text
					m_insertTextPopupItem.setEnabled(false);
				}
				else
				{
					m_insertElemIntoPopupItem.setEnabled(true);
					m_insertTextPopupItem.setEnabled(false);
				}
			}
			else
			{
				m_insertElemBeforePopupItem.setEnabled(false);
				m_insertElemAfterPopupItem.setEnabled(false);
				m_insertElemIntoPopupItem.setEnabled(true);
			}

			final boolean bMayContainResources = elem instanceof JDFNode || elem.getNodeName().equals("ResourcePool");

			m_insertInResPopupItem.setEnabled(bMayContainResources);
			m_insertOutResPopupItem.setEnabled(bMayContainResources);
			m_insertResPopupItem.setEnabled(bMayContainResources);

			final boolean bNodeOK = (elem instanceof JDFNode && EditorUtils.getResourcesAllowedToLink((JDFNode) elem, null) != null);

			boolean bResLinkPoolOK = (elem instanceof JDFResourceLinkPool);
			if (bResLinkPoolOK)
			{
				bResLinkPoolOK = EditorUtils.getResourcesAllowedToLink(((JDFResourceLinkPool) elem).getParentJDF(), null).size() != 0;
			}

			m_insertInResLinkPopupItem.setEnabled(bNodeOK || bResLinkPoolOK);
			m_insertOutResLinkPopupItem.setEnabled(bNodeOK || bResLinkPoolOK);

			final boolean bHasChildNodes = elem.hasChildElements() || elem.hasAttributes();

			m_xpandPopupItem.setEnabled(bHasChildNodes);
			m_collapsePopupItem.setEnabled(bHasChildNodes);

			m_requiredAttrPopupItem.setEnabled(true);
			m_requiredElemPopupItem.setEnabled(true);
			m_modifyAttrValuePopupItem.setEnabled(false);
		}
		else
		{
			m_requiredAttrPopupItem.setEnabled(false);
			m_requiredElemPopupItem.setEnabled(false);
			m_modifyAttrValuePopupItem.setEnabled(true);
			m_xpandPopupItem.setEnabled(false);
			m_collapsePopupItem.setEnabled(false);
			if (!isElement)
			{
				// attribute
				m_targetItem.setEnabled(node.getName().toLowerCase().endsWith("ref"));
			}
		}

		m_pastePopupItem.setEnabled(m_frame.m_copyNode != null && elem != null);
		final Object parent = ((JDFTreeNode) node.getParent()).getUserObject();
		m_renamePopupItem.setEnabled(parent != null);

	}

	/**
	 * perform any actions that this relates to
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param e
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);

		final Object eSrc = e.getSource();
		final JDFFrame frame = MainView.getFrame();
		final JDFTreeArea treeArea = frame.m_treeArea;

		if (!settingService.getSetting(SettingKey.GENERAL_READ_ONLY, Boolean.class))
		{
			if (eSrc == m_cutPopupItem)
			{
				frame.cutSelectedNode();
			}
			else if (eSrc == m_copyPopupItem)
			{
				frame.copySelectedNode();
			}
			else if (eSrc == m_insertElemBeforePopupItem)
			{
				treeArea.insertElementAtSelectedNode(-1);
			}
			else if (eSrc == m_insertElemAfterPopupItem)
			{
				treeArea.insertElementAtSelectedNode(1);
			}
			else if (eSrc == m_insertElemIntoPopupItem)
			{
				treeArea.insertElementAtSelectedNode(0);
			}
			else if (eSrc == m_insertInResPopupItem)
			{
				treeArea.insertResourceWithLink(true, true);
			}
			else if (eSrc == m_insertOutResPopupItem)
			{
				treeArea.insertResourceWithLink(true, false);
			}
			else if (eSrc == m_insertResPopupItem)
			{
				treeArea.insertResourceWithLink(false, false);
			}
			else if (eSrc == m_insertInResLinkPopupItem)
			{
				treeArea.insertResourceLink(EnumUsage.Input);
			}
			else if (eSrc == m_insertOutResLinkPopupItem)
			{
				treeArea.insertResourceLink(EnumUsage.Output);
			}
			else if (eSrc == m_insertAttrPopupItem)
			{
				treeArea.insertAttrItem();
			}
			else if (eSrc == m_renamePopupItem)
			{
				frame.renameSelectedNode();
			}
			else if (eSrc == m_modifyAttrValuePopupItem)
			{
				treeArea.modifyAttribute();
			}
			else if (eSrc == m_insertTextPopupItem)
			{
				// ta.insertText();
			}
			else if (eSrc == m_requiredAttrPopupItem)
			{
				frame.addRequiredAttrsToSelectedNode();
			}
			else if (eSrc == m_requiredElemPopupItem)
			{
				frame.addRequiredElemsToSelectedNode();
			}
			else if (eSrc == m_pastePopupItem)
			{
				frame.pasteCopiedNode();
			}
			else if (eSrc == m_pasteRawPopupItem)
			{
				frame.pasteRawCopiedNode();
			}
			else if (eSrc == m_deletePopupItem)
			{
				MainView.getModel().deleteSelectedNodes();
			}
			MainView.getFrame().refreshView(null, treeArea.getSelectionPath());
		}
		if (eSrc == m_xpandPopupItem)
		{
			treeArea.xpand(null);
		}
		else if (eSrc == m_collapsePopupItem)
		{
			treeArea.collapse(null);
		}
		else if (eSrc == m_copyToClipBoardPopupItem)
		{
			copyToClipBoard(treeArea.getSelectionPath());
		}
		else if (eSrc == m_targetItem)
		{
			treeArea.getPathTarget();
		}
		else if (eSrc == m_saveXJDF)
		{
			SaveAsXJDFDialog d = new SaveAsXJDFDialog();
			if (d.isOK())
			{
				MainView.getModel().saveAsXJDF(treeArea.getSelectionPath(), d.getXJDFConverter());
			}
		}
		else if (eSrc == m_saveJDF)
		{
			SaveAsJDFDialog d = new SaveAsJDFDialog();
			if (d.isOK())
			{
				MainView.getModel().saveAsJDF(treeArea.getSelectionPath(), d.getConverter());
			}
		}
		else if (eSrc == m_saveXJDFCaps)
		{
			MainView.getModel().saveAsXJDFCaps(treeArea.getSelectionPath(), true);
		}
		else if (eSrc == m_spawn)
		{
			MainView.getModel().spawn(false);
			MainView.getFrame().refreshView(null, treeArea.getSelectionPath());
		}
		else if (eSrc == m_unspawn)
		{
			MainView.getModel().unspawn();
			MainView.getFrame().refreshView(null, treeArea.getSelectionPath());
		}
		else if (eSrc == m_nodeFromCaps)
		{
			MainView.getModel().createNodeFromCaps(treeArea.getSelectionPath());
		}
		else if (eSrc == m_normalize)
		{
			MainView.getModel().normalize(treeArea.getSelectionPath());
		}
		else if (eSrc == m_sendMessage)
		{
			sendJMF(treeArea.getSelectionPath());
		}
		MainView.setCursor(0, null);

	}

	/**
	 * @param selectionPath
	 */
	private void sendJMF(final TreePath selectionPath)
	{
		final JDFTreeNode node = (JDFTreeNode) selectionPath.getLastPathComponent();
		if (node == null)
		{
			return;
		}
		final KElement e = node.getElement();
		if (e instanceof JDFMessageService)
		{
			final JDFMessageService ms = (JDFMessageService) e;
			new MessageSender(ms).sendJMF();
		}
		if (e instanceof JDFJMF)
		{
			final SendToDevice sendTo = new SendToDevice();
			sendTo.trySend();
		}
		else
		{
			return;
		}

	}

	/**
	 * copies the content of the marked node to the system clip board
	 * @param p - The TreePath to collapse
	 */
	@SuppressWarnings("unchecked")
	private void copyToClipBoard(final TreePath p)
	{
		final JDFTreeNode node = (JDFTreeNode) p.getLastPathComponent();
		final Enumeration<JDFTreeNode> e = node.postorderEnumeration();

		while (e.hasMoreElements())
		{
			final JDFTreeNode treeNode = e.nextElement();

			if (treeNode.getUserObject() != null && treeNode.isElement())
			{
				final KElement elem = treeNode.getElement();

				// Copy XML representation of the selected node to clip board
				final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				final StringSelection contents = new StringSelection(elem.toXML());
				cb.setContents(contents, null);
			}
		}
	}
}
