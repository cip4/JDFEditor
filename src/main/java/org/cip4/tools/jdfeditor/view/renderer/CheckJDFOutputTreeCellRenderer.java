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
package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.validate.JDFValidator;
import org.cip4.tools.jdfeditor.JDFTreeModel;

/**
 * @author Elena Skobchenko
 */
public class CheckJDFOutputTreeCellRenderer extends AbstractTreeCellRenderer
{
	private static final Log LOGGER = LogFactory.getLog(CheckJDFOutputTreeCellRenderer.class);

	/**
	 * Default constructor.
	 */
	public CheckJDFOutputTreeCellRenderer()
	{
	}

	@Override
	protected Log getLogger()
	{
		return LOGGER;
	}

	@Override
	protected void setNodeIcon(final JDFTreeNode node, final JDFTreeModel model)
	{
		final String n = node.getName();
		if (node.isElement())
		{
			final KElement elem = node.getElement();

			if (n.equals("TestAttribute"))
			{
				if (elem != null && !elem.getBoolAttribute("IsValid", null, false))
				{
					setIcon(loadImageIcon(TreeIcon.ATTR_ERR));
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
				}

				final String tts = JDFValidator.toMessageString(elem);
				if (tts != null)
					setToolTipText(tts);
			}
			else if (n.equals("TestElement"))
			{
				if (elem != null && !elem.getBoolAttribute("IsValid", null, false))
				{
					setIcon(loadImageIcon(TreeIcon.NODE_ERR));
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.NODE_JDF));
				}

				if (elem.getAttribute("Message") != null)
					setToolTipText(elem.getAttribute("Message"));

			}
			else if (n.equals("CheckJDFOutput"))
			{
				if (elem != null && !elem.getBoolAttribute("IsValid", null, true))
				{
					setIcon(loadImageIcon(TreeIcon.NODE_ERR));
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.NODE_JDF));
				}

				if (elem.getAttribute("Message") != null)
					setToolTipText(elem.getAttribute("Message"));

			}
			else if (n.equals("JSONSchema"))
			{
				if (elem != null && !elem.getAttribute("ValidationResult", null, "").equals("Valid"))
				{
					setIcon(loadImageIcon(TreeIcon.ERROR));
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.OK));
				}

				if (elem.getAttribute("Message") != null)
					setToolTipText(elem.getAttribute("Message"));

			}
			else if (n.equals("Part"))
			{
				setIcon(loadImageIcon(TreeIcon.NODE_JDF));
			}
			else if (n.equals("Error"))
			{
				setIcon(loadImageIcon(TreeIcon.ERROR));
			}
			else if (n.equals("Warning"))
			{
				setIcon(loadImageIcon(TreeIcon.WARN));
			}
			else
			{
				if ("false".equals(elem.getInheritedAttribute("IsValid", null, null)))
				{
					setIcon(loadImageIcon(TreeIcon.NODE_ERR));
				}
				else
				{
					setIcon(loadImageIcon(TreeIcon.NODE_JDF));
				}

				if (elem.getAttribute("Message") != null)
					setToolTipText(elem.getAttribute("Message"));
			}
		}
		else
		// real attributes
		{
			setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
		}
	}
}
