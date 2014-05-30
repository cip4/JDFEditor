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
package org.cip4.tools.jdfeditor.pane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.transport.HttpReceiver;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Class that implements a "HTTP server" tab/panel.
 *
 */
public class HttpServerPane implements FileAlterationListener, ActionListener
{
	private static final Logger LOGGER = LogManager.getLogger(HttpServerPane.class);

    private SettingService settingService = new SettingService();

	private final JDFFrame frame;

	private JComboBox ipComboBox;
	private JTextField portValueLabel;
	private JLabel statusValueLabel;
	private JLabel gatewayValueLabel;
	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonClear;
	private JButton buttonSelectPath;
	private JLabel labelStorePath;

	private final MessageTableModel tableModel = new MessageTableModel();

	public HttpServerPane(JDFFrame frame)
	{
		this.frame = frame;

		File directory = new File(settingService.getString(SettingKey.HTTP_STORE_PATH));
		FileAlterationObserver observer = new FileAlterationObserver(directory);
		observer.addListener(this);

		FileAlterationMonitor monitor = new FileAlterationMonitor(3000);
		monitor.addObserver(observer);
		try
		{
			monitor.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public JPanel createPane()
	{
		JPanel httpPanel = new JPanel(new BorderLayout());

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel(Editor.getString("HTTPserver") + ":"), BorderLayout.NORTH);

		JPanel settingsPanel = new JPanel();
		SpringLayout settingsLayout = new SpringLayout();
		settingsPanel.setLayout(settingsLayout);

		JLabel ipLabel = new JLabel(Editor.getString("IPAddress") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, ipLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipLabel, 10, SpringLayout.NORTH, settingsPanel);

		ipComboBox = new JComboBox();
		fillWithIPAddresses(ipComboBox);
		settingsLayout.putConstraint(SpringLayout.WEST, ipComboBox, 5, SpringLayout.EAST, ipLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipComboBox, 10, SpringLayout.NORTH, settingsPanel);

		JLabel portLabel = new JLabel(Editor.getString("Port") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, portLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, portLabel, 10, SpringLayout.SOUTH, ipLabel);

		portValueLabel = new JTextField("8280", 5);
		settingsLayout.putConstraint(SpringLayout.WEST, portValueLabel, 0, SpringLayout.WEST, ipComboBox);
		settingsLayout.putConstraint(SpringLayout.NORTH, portValueLabel, 10, SpringLayout.SOUTH, ipLabel);

		JLabel statusLabel = new JLabel(Editor.getString("Status") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, statusLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusLabel, 10, SpringLayout.SOUTH, portLabel);

		statusValueLabel = new JLabel(Editor.getString("Stopped"));
		settingsLayout.putConstraint(SpringLayout.WEST, statusValueLabel, 0, SpringLayout.WEST, portValueLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusValueLabel, 10, SpringLayout.SOUTH, portLabel);

		gatewayValueLabel = new JLabel(HttpReceiver.DEF_PROTOCOL + "://localhost:" + portValueLabel.getText() + HttpReceiver.DEF_PATH);
		gatewayValueLabel.setEnabled(false);
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
		buttonStart = new JButton(Editor.getString("Start"));
		buttonStart.addActionListener(this);
		buttonStop = new JButton(Editor.getString("Stop"));
		buttonStop.addActionListener(this);
		buttonStop.setEnabled(false);
		buttonsPanel.add(buttonStart);
		buttonsPanel.add(buttonStop);

		leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JPanel rightTopPanel = new JPanel(new BorderLayout());
		rightTopPanel.add(new JLabel(Editor.getString("ReceivedMessages") + ":"), BorderLayout.NORTH);

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
					frame.readFile(f);
				}
			}
		});
		TableRowSorter<MessageTableModel> sorter = new TableRowSorter<MessageTableModel>((MessageTableModel) table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);

		rightTopPanel.add(scrollPane, BorderLayout.CENTER);

		buttonClear = new JButton(Editor.getString("ClearAll"));
		buttonClear.addActionListener(this);
		rightTopPanel.add(buttonClear, BorderLayout.SOUTH);

		JPanel rightPanel = new JPanel(new BorderLayout());

		JPanel rightBottomPanel = new JPanel(new BorderLayout());
		rightBottomPanel.add(new JLabel(Editor.getString("PathMessages") + ":"), BorderLayout.NORTH);
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonSelectPath = new JButton("...");
		buttonSelectPath.addActionListener(this);
		pathPanel.add(buttonSelectPath);
		labelStorePath = new JLabel(settingService.getString(SettingKey.HTTP_STORE_PATH));
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

	private void fillWithIPAddresses(JComboBox ipComboBox)
	{
		ipComboBox.removeAllItems();
		ipComboBox.setEditable(false);
		try
		{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements())
			{
				NetworkInterface ni = interfaces.nextElement();
				Enumeration<InetAddress> inetAddress = ni.getInetAddresses();
				while (inetAddress.hasMoreElements())
				{
					InetAddress address = inetAddress.nextElement();
					//                    if (address.isLoopbackAddress()) continue;
					LOGGER.debug("host address: " + address.getHostAddress());
					ipComboBox.addItem(address.getHostAddress());
				}
				LOGGER.debug("------- next interface");
			}
		}
		catch (SocketException e)
		{
			LOGGER.error("Snafu filling addresses", e);
		}
	}

	private void updateControls(boolean enabled)
	{
		buttonStart.setEnabled(enabled);
		buttonStop.setEnabled(!enabled);
		ipComboBox.setEnabled(enabled);
		portValueLabel.setEnabled(enabled);
		gatewayValueLabel.setEnabled(!enabled);
	}

	@Override
	public void onDirectoryChange(File f)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onDirectoryCreate(File f)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onDirectoryDelete(File f)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onFileChange(File f)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onFileCreate(File f)
	{
		LOGGER.debug("file created: " + f.getAbsolutePath());

		String senderId = "none";
		String type = "---";
		try
		{
			String messageBody = FileUtils.readFileToString(f);
			JDFJMF jmf = new JDFParser().parseString(messageBody).getJMFRoot();
			senderId = jmf.getSenderID();

			JDFMessage m = jmf.getMessageElement(null /*EnumFamily.Query*/, null, 0);
			type = m.getType();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		MessageBean msg = new MessageBean();
		msg.setFilePathName(f.getAbsolutePath());
		msg.setMessageType(type);
		msg.setSenderId(senderId);
		msg.setSize(FileUtils.byteCountToDisplaySize(f.length()));

		Date d = new Date(f.lastModified());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timeReceived = formatter.format(d);

		msg.setTimeReceived(timeReceived);

		tableModel.addMessage(msg);
	}

	@Override
	public void onFileDelete(File f)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onStart(FileAlterationObserver fao)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onStop(FileAlterationObserver fao)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == buttonStart)
		{
			try
			{
				HttpReceiver.getInstance().startServer((String) ipComboBox.getSelectedItem(), Integer.parseInt(portValueLabel.getText()));
				statusValueLabel.setText(Editor.getString("Started"));
				gatewayValueLabel.setText(HttpReceiver.DEF_PROTOCOL + "://" + (String) ipComboBox.getSelectedItem() + ":" + portValueLabel.getText() + HttpReceiver.DEF_PATH);
				updateControls(false);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Could not start server", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource() == buttonStop)
		{
			HttpReceiver.getInstance().stopServer();
			statusValueLabel.setText(Editor.getString("Stopped"));
			updateControls(true);
		}
		else if (e.getSource() == buttonClear)
		{
			tableModel.clearAll();
		}
		else if (e.getSource() == buttonSelectPath)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(settingService.getString(SettingKey.HTTP_STORE_PATH)));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
				LOGGER.debug("getSelectedFile(): " + chooser.getSelectedFile());
                settingService.setString(SettingKey.HTTP_STORE_PATH, chooser.getSelectedFile().getAbsolutePath());
				labelStorePath.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

}
