/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2014 The International Cooperation for the Integration of 
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpringLayout;
import javax.swing.table.TableRowSorter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.transport.HttpReceiver;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

/**
 * Class that implements a "HTTP server" tab/panel.
 *
 */
public class HttpServerPane implements ActionListener
{
	private static final Log LOGGER = LogFactory.getLog(HttpServerPane.class);

	private final SettingService settingService = SettingService.getSettingService();

	private JComboBox<String> ipComboBox;
	private JTextField portValueLabel;
	private JLabel statusValueLabel;
	private JTextField gatewayValueLabel;
	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonClear;
	private JButton buttonSelectPath;
	private JTextField labelStorePath;

	private final MessageTableModel tableModel = new MessageTableModel();

	/**
	 * 
	 */
	public HttpServerPane()
	{
		super();
	}

	public JPanel createPane()
	{
		JPanel httpPanel = new JPanel(new BorderLayout());

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel(ResourceUtil.getMessage("HTTPserver") + ":"), BorderLayout.NORTH);

		JPanel settingsPanel = new JPanel();
		SpringLayout settingsLayout = new SpringLayout();
		settingsPanel.setLayout(settingsLayout);

		JLabel ipLabel = new JLabel(ResourceUtil.getMessage("IPAddress") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, ipLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipLabel, 10, SpringLayout.NORTH, settingsPanel);

		ipComboBox = new JComboBox<String>();
		fillWithIPAddresses();
		settingsLayout.putConstraint(SpringLayout.WEST, ipComboBox, 5, SpringLayout.EAST, ipLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipComboBox, 10, SpringLayout.NORTH, settingsPanel);

		JLabel portLabel = new JLabel(ResourceUtil.getMessage("Port") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, portLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, portLabel, 10, SpringLayout.SOUTH, ipLabel);

		portValueLabel = new JTextField("8280", 5);
		settingsLayout.putConstraint(SpringLayout.WEST, portValueLabel, 0, SpringLayout.WEST, ipComboBox);
		settingsLayout.putConstraint(SpringLayout.NORTH, portValueLabel, 10, SpringLayout.SOUTH, ipLabel);

		JLabel statusLabel = new JLabel(ResourceUtil.getMessage("Status") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, statusLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusLabel, 10, SpringLayout.SOUTH, portLabel);

		statusValueLabel = new JLabel(ResourceUtil.getMessage("Stopped"));
		settingsLayout.putConstraint(SpringLayout.WEST, statusValueLabel, 0, SpringLayout.WEST, portValueLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusValueLabel, 10, SpringLayout.SOUTH, portLabel);

		createGatewaylabel();
		settingsLayout.putConstraint(SpringLayout.WEST, gatewayValueLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, gatewayValueLabel, 10, SpringLayout.SOUTH, statusLabel);

		settingsPanel.add(ipLabel);
		settingsPanel.add(ipComboBox);
		settingsPanel.add(portLabel);
		settingsPanel.add(portValueLabel);
		settingsPanel.add(statusLabel);
		settingsPanel.add(statusValueLabel);

		settingsPanel.add(gatewayValueLabel);

		leftPanel.add(settingsPanel, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		buttonStart = new JButton(ResourceUtil.getMessage("Start"));
		buttonStart.addActionListener(this);
		buttonStop = new JButton(ResourceUtil.getMessage("Stop"));
		buttonStop.addActionListener(this);
		buttonStop.setEnabled(false);
		buttonsPanel.add(buttonStart);
		buttonsPanel.add(buttonStop);

		leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JPanel rightTopPanel = new JPanel(new BorderLayout());
		rightTopPanel.add(new JLabel(ResourceUtil.getMessage("ReceivedMessages") + ":"), BorderLayout.NORTH);

		JTable table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					LOGGER.debug("row: " + row);
					if (row == -1)
						return;
					int modelRow = target.convertRowIndexToModel(row);
					LOGGER.debug("modelRow: " + modelRow);
					MessageBean msg = tableModel.getItem(modelRow);
					LOGGER.debug("file to load: " + msg.getFilePathName());
					File f = new File(msg.getFilePathName());
					MainView.getFrame().readFile(f);
				}
			}
		});
		TableRowSorter<MessageTableModel> sorter = new TableRowSorter<MessageTableModel>((MessageTableModel) table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);

		rightTopPanel.add(scrollPane, BorderLayout.CENTER);

		buttonClear = new JButton(ResourceUtil.getMessage("ClearAll"));
		buttonClear.addActionListener(this);
		rightTopPanel.add(buttonClear, BorderLayout.SOUTH);

		JPanel rightPanel = new JPanel(new BorderLayout());

		JPanel rightBottomPanel = new JPanel(new BorderLayout());
		rightBottomPanel.add(new JLabel(ResourceUtil.getMessage("PathMessages") + ":"), BorderLayout.NORTH);
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonSelectPath = new JButton("...");
		buttonSelectPath.addActionListener(this);
		pathPanel.add(buttonSelectPath);
		labelStorePath = new JTextField(settingService.getSetting(SettingKey.HTTP_STORE_PATH, String.class));
		labelStorePath.setEditable(false);
		pathPanel.add(labelStorePath);

		rightBottomPanel.add(pathPanel, BorderLayout.SOUTH);

		rightPanel.add(rightTopPanel, BorderLayout.CENTER);
		rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

		httpPanel.add(leftPanel, BorderLayout.WEST);
		httpPanel.add(rightPanel, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(220);
		httpPanel.add(splitPane);

		return httpPanel;
	}

	private void createGatewaylabel()
	{
	    String interfaceAddress = (String) ipComboBox.getSelectedItem();
	    
		gatewayValueLabel = new JTextField(HttpReceiver.DEF_PROTOCOL + "://" + interfaceAddress + ":" + portValueLabel.getText() + HttpReceiver.DEF_PATH);
		gatewayValueLabel.setEnabled(true);
		gatewayValueLabel.setEditable(false);
	}

	private void fillWithIPAddresses()
	{
		ipComboBox.removeAllItems();
		ipComboBox.setEditable(false);
		VString ipReal = new VString();
		VString ipLoopback = new VString();
		try
		{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements())
			{
				NetworkInterface ni = interfaces.nextElement();
				LOGGER.debug("network interface: " + ni +
				        ", isLoopback: " + ni.isLoopback() + ", isVirtual: " + ni.isVirtual());
				
				Enumeration<InetAddress> inetAddress = ni.getInetAddresses();
				while (inetAddress.hasMoreElements())
				{
					InetAddress address = inetAddress.nextElement();
					if (address instanceof Inet6Address)
                        continue; // skip IPv6 addresses
					LOGGER.debug("host name: " + address.getHostName() + ", host address: " + address.getHostAddress());
					
					if (address.isLoopbackAddress()) {
					    ipLoopback.appendUnique(address.getHostAddress());
					} else {
					    ipReal.appendUnique(address.getHostAddress());
					    ipReal.appendUnique(address.getHostName());
					}
				}
				LOGGER.debug("------- next interface");
			}
		}
		catch (SocketException e)
		{
			LOGGER.error("Snafu filling addresses", e);
		}
		
		sortIPFillComboBox(ipReal);
		sortIPFillComboBox(ipLoopback);
		
		String preselectAddress = settingService.getSetting(SettingKey.HTTP_PRESELECTED_ADDRESS, String.class);
		ipComboBox.setSelectedItem(preselectAddress);
	}
	
	private void sortIPFillComboBox(final VString list) {
	    list.sort();
        for (String s : list)
        {
            ipComboBox.addItem(s);
        }
	}

	private void updateControls(boolean enabled)
	{
		buttonStart.setEnabled(enabled);
		buttonStop.setEnabled(!enabled);
		ipComboBox.setEnabled(enabled);
		portValueLabel.setEnabled(enabled);
	}

	public void addMessage(final MessageBean msg)
	{
		tableModel.addMessage(msg);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == buttonStart)
		{
			try
			{
			    String interfaceAddress = (String) ipComboBox.getSelectedItem();
				HttpReceiver.getInstance().startServer(interfaceAddress, Integer.parseInt(portValueLabel.getText()));
				statusValueLabel.setText(ResourceUtil.getMessage("Started"));
				gatewayValueLabel.setText(HttpReceiver.DEF_PROTOCOL + "://" + (String) ipComboBox.getSelectedItem() + ":" + portValueLabel.getText() + HttpReceiver.DEF_PATH);
				updateControls(false);
				
				settingService.setSetting(SettingKey.HTTP_PRESELECTED_ADDRESS, interfaceAddress);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(MainView.getFrame(), "Could not start server", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource() == buttonStop)
		{
			HttpReceiver.getInstance().stopServer();
			statusValueLabel.setText(ResourceUtil.getMessage("Stopped"));
			updateControls(true);
		}
		else if (e.getSource() == buttonClear)
		{
			tableModel.clearAll();
		}
		else if (e.getSource() == buttonSelectPath)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(settingService.getSetting(SettingKey.HTTP_STORE_PATH, String.class)));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(MainView.getFrame()) == JFileChooser.APPROVE_OPTION)
			{
				LOGGER.debug("getSelectedFile(): " + chooser.getSelectedFile());
				settingService.setSetting(SettingKey.HTTP_STORE_PATH, chooser.getSelectedFile().getAbsolutePath());
				labelStorePath.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

}
