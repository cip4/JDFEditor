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
package org.cip4.tools.jdfeditor.dialog;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
 * @author rainer prosi
 * @date before  Nov 28, 2012
 */
public class SearchComboBoxModel extends AbstractListModel implements ComboBoxModel
{
	private SettingService settingService = new SettingService();

    /**
	 * 	Max number of strings in combobox
	 */
	public static int MAX_ELEMENTS = 5;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SearchComboBoxModel()
	{
		super();
	}

	private static final Logger log = Logger.getLogger(SearchComboBoxModel.class);
	private List<String> elements = new ArrayList<String>();
	private String selectedItem;

	/**
	 * 
	 *  
	 * @param item
	 */
	public void addItem(String item)
	{
		if (!ArrayUtils.contains(elements.toArray(new String[0]), item))
		{
			elements.add(item);
		}
		if (elements.size() > MAX_ELEMENTS)
		{
			String[] newElements = (String[]) ArrayUtils.subarray(elements.toArray(new String[0]), elements.size() - MAX_ELEMENTS, elements.size());
			elements = Arrays.asList(newElements);
			log.debug("elements: " + elements);
		}
		fireContentsChanged(this, 0, elements.size());

        String findPattern = null;

        for(int i = 0; i < elements.size() && i < 5 ; i ++) {

            if(i == 0) {
                findPattern = elements.get(i);

            } else {
                findPattern += ";" + elements.get(i);

            }
        }

        settingService.setString(SettingKey.FIND_PATTERN, findPattern);

	}

	/**
	 * 
	 *  
	 * @param elements
	 */
	public void addAll(List<String> elements)
	{
		for (String item : elements)
		{
			addItem(item);
		}
	}

	/**
	 * 
	 * @see javax.swing.ComboBoxModel#getSelectedItem()
	 */
	public Object getSelectedItem()
	{
		return selectedItem;
	}

	/**
	 * 
	 * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
	 */
	public void setSelectedItem(Object anItem)
	{
		selectedItem = (String) anItem;
	}

	/**
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index)
	{
		int size = elements.size();
		return elements.get(size - index - 1);
	}

	/**
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize()
	{
		return elements.size();
	}

}
