/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2013 The International Cooperation for the Integration of 
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

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * class to handle copy / paste actions from the frame
 * @author prosirai
 *
 */
public class JDFTreeCopyNode
{
	SettingService settingService = SettingService.getSettingService();

	private final JDFTreeNode treeNode;
	private boolean m_isPastedBefore;

	/**
	 * @param _treeNode
	 * @param bExists
	 */
	public JDFTreeCopyNode(JDFTreeNode _treeNode, boolean bExists)
	{
		treeNode = _treeNode;
		m_isPastedBefore = bExists; // TODO fix id handling for doc to doc copy
		// TODO ensure that no nirvana links are created when copying resource links
	}

	/**
	 * Method getChildrenForCopiedNode.
	 * checks if the copied node has any children and inserts them into the
	 * m_jdfTree
	 * @param newNode
	 */
	private void getChildrenForCopiedNode(JDFTreeNode newNode)
	{
		KElement newChild = newNode.getElement();
		final int pos = newNode.getChildCount();

		final VElement children = newChild.getChildElementVector(null, null, null, true, 0, false);

		final JDFTreeModel model = MainView.getModel();
		for (int i = 0; i < children.size(); i++)
		{
			final KElement childElm = children.item(i);
			if (childElm.hasAttribute("ID") && m_isPastedBefore)
				childElm.setAttribute("ID", "E" + JDFElement.uniqueID(0));

			final JDFTreeNode childN = model.createNewNode(childElm);
			model.insertNodeInto(childN, newNode, pos + i);

			getChildrenForCopiedNode(childN);
			if (settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class))
				model.validate();
		}
	}

	/**
	 * Method pasteNode.
	 * inserts a copied or cutted node into the m_jdfTree and jdfDoc
	 * @param path
	 * @return
	 */
	public JDFTreeNode pasteNode(TreePath path)
	{
		JDFTreeNode parentNode = (JDFTreeNode) path.getLastPathComponent();
		JDFTreeNode newNode = null;
		KElement parentElement = parentNode.getElement();
		final JDFFrame m_frame = MainView.getFrame();
		final JDFTreeModel model = m_frame.getModel();
		if (treeNode.isElement())
		{
			final KElement newChild = treeNode.getElement();
			try
			{
				final KElement copiedElement = parentElement.copyElement(newChild, null);

				if (copiedElement.hasAttribute("ID") && m_isPastedBefore)
					copiedElement.setAttribute("ID", "E" + JDFElement.uniqueID(0));
				else if (!m_isPastedBefore)
					m_isPastedBefore = true;

				newNode = model.createNewNode(copiedElement);
				model.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

				getChildrenForCopiedNode(newNode);
				if (settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class))
					model.validate();
			}
			catch (Exception s)
			{
				s.printStackTrace();
				JOptionPane.showMessageDialog(m_frame, ResourceUtil.getMessage("NodeInsertErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			final String attributeName = treeNode.getName();
			final String attributeValue = treeNode.getValue();
			newNode = model.setAttribute(parentNode, attributeName, attributeValue, null, false);
		}
		return newNode;
	}

	/**
	 *  
	 * @param path
	 * @return
	 */
	public JDFTreeNode pasteRawNode(TreePath path)
	{
		JDFTreeNode parentNode = (JDFTreeNode) path.getLastPathComponent();
		JDFTreeNode newNode = null;
		KElement parentElement = parentNode.getElement();
		final JDFFrame m_frame = MainView.getFrame();
		final JDFTreeModel model = m_frame.getModel();
		if (treeNode.isElement())
		{
			final KElement newChild = treeNode.getElement();
			try
			{
				final KElement copiedElement = parentElement.copyElement(newChild, null);

				newNode = model.createNewNode(copiedElement);
				model.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

				getChildrenForCopiedNode(newNode);
				if (settingService.getSetting(SettingKey.GENERAL_AUTO_VALIDATE, Boolean.class))
					model.validate();
			}
			catch (Exception s)
			{
				s.printStackTrace();
				JOptionPane.showMessageDialog(m_frame, ResourceUtil.getMessage("NodeInsertErrorKey"), ResourceUtil.getMessage("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			final String attributeName = treeNode.getName();
			final String attributeValue = treeNode.getValue();
			newNode = model.setAttribute(parentNode, attributeName, attributeValue, null, false);
		}
		return newNode;
	}
}
