/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2010 The International Cooperation for the Integration of 
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
package org.cip4.jdfeditor.pane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.cip4.jdfeditor.Editor;
import org.cip4.jdfeditor.INIReader;
import org.cip4.jdfeditor.JDFFrame;
import org.cip4.jdfeditor.transport.HttpReceiver;

/**
 * Class that implements a "HTTP server" tab/panel.
 *
 */
public class HttpServerPane implements FileAlterationListener, ActionListener {
	private static final Logger log = Logger.getLogger(HttpServerPane.class);
	private static ResourceBundle bundle = Editor.getBundle();
	private static INIReader conf = Editor.getIniFile();
	
	private JDFFrame frame;
	
	private JTextField portValueLabel;
	private JLabel statusValueLabel;
	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonSelectPath;
	private JLabel labelStorePath;
	
	private MessageTableModel tableModel = new MessageTableModel();
	
	
	public HttpServerPane(JDFFrame frame) {
		this.frame = frame;
		
		File directory = new File(conf.getHttpStorePath());
		FileAlterationObserver observer = new FileAlterationObserver(directory);
		observer.addListener(this);
		
		FileAlterationMonitor monitor = new FileAlterationMonitor(3000);
		monitor.addObserver(observer);
		try {
			monitor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JPanel createPane() {
		JPanel httpPanel = new JPanel(new BorderLayout());
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel(bundle.getString("HTTPserver") + ":"), BorderLayout.NORTH);
		
		JPanel settingsPanel = new JPanel();
		SpringLayout settingsLayout = new SpringLayout();
		settingsPanel.setLayout(settingsLayout);
		
		JLabel ipLabel = new JLabel(bundle.getString("IPAddress") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, ipLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipLabel, 10, SpringLayout.NORTH, settingsPanel);
		
		JComboBox ipComboBox = new JComboBox();
		fillWithIPAddresses(ipComboBox);
		settingsLayout.putConstraint(SpringLayout.WEST, ipComboBox, 5, SpringLayout.EAST, ipLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipComboBox, 10, SpringLayout.NORTH, settingsPanel);
		
		JLabel portLabel = new JLabel(bundle.getString("Port") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, portLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, portLabel, 10, SpringLayout.SOUTH, ipLabel);
		
		portValueLabel = new JTextField("8080", 5);
		settingsLayout.putConstraint(SpringLayout.WEST, portValueLabel, 0, SpringLayout.WEST, ipComboBox);
		settingsLayout.putConstraint(SpringLayout.NORTH, portValueLabel, 10, SpringLayout.SOUTH, ipLabel);
		
		JLabel statusLabel = new JLabel("Status:");
		settingsLayout.putConstraint(SpringLayout.WEST, statusLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusLabel, 10, SpringLayout.SOUTH, portLabel);
		
		statusValueLabel = new JLabel(bundle.getString("Stopped"));
		settingsLayout.putConstraint(SpringLayout.WEST, statusValueLabel, 0, SpringLayout.WEST, portValueLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusValueLabel, 10, SpringLayout.SOUTH, portLabel);
		
		settingsPanel.add(ipLabel);
		settingsPanel.add(ipComboBox);
		settingsPanel.add(portLabel);
		settingsPanel.add(portValueLabel);
		settingsPanel.add(statusLabel);
		settingsPanel.add(statusValueLabel);
		
		leftPanel.add(settingsPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonStart = new JButton(bundle.getString("Start"));
		buttonStart.addActionListener(this);
		buttonStop = new JButton(bundle.getString("Stop"));
		buttonStop.addActionListener(this);
		buttonStop.setEnabled(false);
		buttonsPanel.add(buttonStart);
		buttonsPanel.add(buttonStop);
		
		leftPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		JPanel rightTopPanel = new JPanel(new BorderLayout());
		rightTopPanel.add(new JLabel(bundle.getString("ReceivedMessages") + ":"), BorderLayout.NORTH);
		
		JTable table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					log.debug("row: " + row);
					if (row == -1) return;
					int modelRow = target.convertRowIndexToModel(row);
					log.debug("modelRow: " + modelRow);
					MessageBean msg = tableModel.getItem(modelRow);
					log.debug("file to load: " + msg.getFilePathName());
					File f = new File(msg.getFilePathName());
					frame.readFile(f);
				}
			}
		});
		TableRowSorter<MessageTableModel> sorter = new TableRowSorter<MessageTableModel>((MessageTableModel)table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(2, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);

		
		rightTopPanel.add(scrollPane, BorderLayout.CENTER);

		
		rightTopPanel.add(new JButton(bundle.getString("ClearAll")), BorderLayout.SOUTH);
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		JPanel rightBottomPanel = new JPanel(new BorderLayout());
		rightBottomPanel.add(new JLabel(bundle.getString("PathMessages") + ":"), BorderLayout.NORTH);
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonSelectPath = new JButton("...");
		buttonSelectPath.addActionListener(this);
		pathPanel.add(buttonSelectPath);
		labelStorePath = new JLabel(conf.getHttpStorePath());
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
	
	private void fillWithIPAddresses(JComboBox ipComboBox) {
	    ipComboBox.removeAllItems();
        ipComboBox.setEditable(false);
	    try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> inetAddress = ni.getInetAddresses();
                while (inetAddress.hasMoreElements()) {
                    InetAddress address = inetAddress.nextElement();
                    if (address.isLoopbackAddress()) continue;
                    log.info("address: " + address.getHostAddress());
                    ipComboBox.addItem(address.getHostAddress());
                }
                log.info("------- next interface");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
	}

	public void onDirectoryChange(File f)
	{
		// TODO Auto-generated method stub
	}

	public void onDirectoryCreate(File f)
	{
		// TODO Auto-generated method stub
	}

	public void onDirectoryDelete(File f)
	{
		// TODO Auto-generated method stub
	}

	public void onFileChange(File f)
	{
		// TODO Auto-generated method stub
	}

	public void onFileCreate(File f)
	{
		log.debug("file created: " + f.getAbsolutePath());
		
		MessageBean msg = new MessageBean();
		msg.setFilePathName(f.getAbsolutePath());
		msg.setMessageType("---");
		msg.setSenderId("Sender-ID");
		msg.setSize(FileUtils.byteCountToDisplaySize(f.length()));
		
		Date d = new Date(f.lastModified());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timeReceived = formatter.format(d);
		
		msg.setTimeReceived(timeReceived);
		
		tableModel.addMessage(msg);
	}

	public void onFileDelete(File f)
	{
		// TODO Auto-generated method stub
	}

	public void onStart(FileAlterationObserver fao)
	{
		// TODO Auto-generated method stub
	}

	public void onStop(FileAlterationObserver fao)
	{
		// TODO Auto-generated method stub
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == buttonStart) {
			try {
				HttpReceiver.getInstance().startServer(Integer.parseInt(portValueLabel.getText()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			statusValueLabel.setText(bundle.getString("Started"));
			buttonStart.setEnabled(false);
			buttonStop.setEnabled(true);
		} else if (e.getSource() == buttonStop) {
			HttpReceiver.getInstance().stopServer();
			statusValueLabel.setText(bundle.getString("Stopped"));
			buttonStart.setEnabled(true);
			buttonStop.setEnabled(false);
		} else if (e.getSource() == buttonSelectPath) {
			JFileChooser chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new File(conf.getHttpStorePath()));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
				log.debug("getSelectedFile(): " +  chooser.getSelectedFile());
				conf.setHttpStorePath(chooser.getSelectedFile().getAbsolutePath());
				labelStorePath.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

}
