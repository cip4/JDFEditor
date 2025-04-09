/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2025 The International Cooperation for the Integration of 
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
package org.cip4.tools.jdfeditor.pane;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *  
 *
 */
public class MessageTableModel extends AbstractTableModel
{

	private long received = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log LOGGER = LogFactory.getLog(MessageTableModel.class);

	private final String[] columnNames = { "Sender ID", "Message Type", "Time Received", "Message Time", "Size" };

	private final List<MessageBean> data = new ArrayList<MessageBean>();

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return data.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		if (columnIndex == 0)
		{
			return data.get(rowIndex).getSenderId();
		}
		else if (columnIndex == 1)
		{
			return data.get(rowIndex).getMessageType();
		}
		else if (columnIndex == 2)
		{
			return data.get(rowIndex).getTimeReceived();
		}
		else if (columnIndex == 3)
		{
			return data.get(rowIndex).getMessageDate();
		}
		else if (columnIndex == 4)
		{
			return data.get(rowIndex).getSize();
		}
		else
		{
			LOGGER.error("No column available: " + columnIndex);
			return "";
		}
	}

	@Override
	public String getColumnName(final int col)
	{
		return columnNames[col];
	}

	public synchronized void addMessage(final MessageBean msg)
	{
		data.add(msg);
		received++;
		while (data.size() > 666)
		{
			data.remove(0);
		}

		fireTableDataChanged();
	}

	public synchronized void clearAll()
	{
		data.clear();
		received = 0;
		fireTableDataChanged();
	}

	public MessageBean getItem(final int i)
	{
		return data.get(i);
	}

	@Override
	public String toString()
	{
		return "MessageTableModel [data=" + data + "]";
	}

	public long getReceived()
	{
		return received;
	}

}
