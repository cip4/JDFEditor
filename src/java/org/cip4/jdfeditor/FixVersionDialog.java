/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2009 The International Cooperation for the Integration of 
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.JDFElement.EnumVersion;
import org.cip4.jdflib.elementwalker.FixVersion;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFAuditPool;
import org.cip4.jdflib.resource.JDFModified;

/**
 * class to update the version of a jdf or jmf<br/>
 * also draws the ui dialof
 * 
 * @author Dr. Rainer Prosi, Heidelberger Druckmaschinen AG
 * 
 * before June 3, 2009
 */
public class FixVersionDialog extends JPanel implements ActionListener
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -276165456151780040L;

	private final ResourceBundle littleBundle;
	private boolean bVersionKeyChosen = false;
	private JComboBox chooseVersion;
	private EnumVersion version = EnumVersion.Version_1_3;

	/**
	 * @param bundle
	 */
	public FixVersionDialog(final ResourceBundle bundle)
	{
		super();
		this.littleBundle = bundle;

		init();

		final String[] options = { littleBundle.getString("FixVersionKey"), littleBundle.getString("CancelKey") };

		final int option = JOptionPane.showOptionDialog(Editor.getFrame(), this, "Fix Version in file", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			bVersionKeyChosen = true;
		}
		else if (option == JOptionPane.CANCEL_OPTION)
		{
			bVersionKeyChosen = false;
		}

	}

	/**
	 * Creates the fields and view for the Merge Dialog.
	 */
	private void init()
	{
		final GridBagLayout outLayout = new GridBagLayout();
		final GridBagConstraints outConstraints = new GridBagConstraints();
		outConstraints.fill = GridBagConstraints.BOTH;

		final JPanel panel = new JPanel();
		outLayout.setConstraints(panel, outConstraints);
		panel.setLayout(outLayout);
		final INIReader ir = Editor.getIniFile();
		final EnumVersion defVersion = ir.getDefaultVersion();

		final JPanel versionPanel = new JPanel();
		versionPanel.setBorder(BorderFactory.createTitledBorder("JDFVersion"));

		final Vector<String> allValues = new Vector<String>();
		allValues.addElement("Retain Version");
		allValues.addElement(EnumVersion.Version_1_0.getName());
		allValues.addElement(EnumVersion.Version_1_1.getName());
		allValues.addElement(EnumVersion.Version_1_2.getName());
		allValues.addElement(EnumVersion.Version_1_3.getName());
		allValues.addElement(EnumVersion.Version_1_4.getName());
		chooseVersion = new JComboBox(allValues);
		chooseVersion.setSelectedItem(defVersion.getName());
		chooseVersion.addActionListener(this);
		versionPanel.add(Box.createHorizontalGlue());
		versionPanel.add(chooseVersion);
		versionPanel.add(Box.createHorizontalGlue());
		outLayout.setConstraints(versionPanel, outConstraints);

		panel.add(versionPanel);

		add(panel);
		setVisible(true);

	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();
		if (source == chooseVersion)
		{

			final String selectedItem = (String) chooseVersion.getSelectedItem();
			if (!selectedItem.startsWith("Retain"))
			{
				version = EnumVersion.getEnum(selectedItem);
			}
			else
			{
				version = null;
			}
		}
	}

	/**
	 * @return
	 */
	public EnumVersion getVersion()
	{
		return version;
	}

	/**
	 * @return
	 */
	public boolean getValidationKeyChosen()
	{
		return bVersionKeyChosen;
	}

	/**
	 * fix the version for the element specified in Path
	 * @param path
	 */
	public void fixIt(final TreePath path)
	{
		try
		{
			final KElement element = EditorUtils.getElement(path);

			// find the closest selected JDF or JMF element and fix it
			if (element != null)
			{
				KElement n1 = element.getDeepParent(ElementName.JDF, 0);
				if (n1 == null)
				{
					n1 = element.getDeepParent(ElementName.JMF, 0);
				}
				if (n1 != null && n1 instanceof JDFNode)
				{
					final JDFNode theRoot = (JDFNode) n1;
					// mark our work in the audit pool
					final JDFAuditPool ap = theRoot.getCreateAuditPool();
					final JDFModified modi = ap.addModified(null, null);
					modi.setDescriptiveName("automatic update to version " + getVersion());
				}
			}

			// the mother of all fixing routines
			if (element instanceof JDFElement)
			{
				final FixVersion fv = new FixVersion(getVersion());
				fv.setFixVersionIDFix(true);
				fv.walkTree(element, null);
				element.eraseEmptyNodes(true);
			}

			Editor.getFrame().refreshView(Editor.getEditorDoc(), path);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, littleBundle.getString("FixVersionErrorKey") + e.getClass() + " \n" + (e.getMessage() != null ? ("\"" + e.getMessage() + "\"") : ""), littleBundle
					.getString("ErrorMessKey"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
