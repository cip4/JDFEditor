/*
 * Copyright (c) 2004-2011 tcpmon authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * $Id$
 */
package com.codegoogle.tcpmon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.codegoogle.tcpmon.bookmark.Bookmark;
import com.codegoogle.tcpmon.bookmark.BookmarkManager;

/**
 * The main GUI class
 * 
 * @author Inderjeet Singh
 */
public final class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private final BookmarkManager bookmarkManager;

	// Main tcpmon configuration
	private final Configuration configuration;

	private JPanel adminPanel;
	private JButton bAddMonitor;
	private JLabel jLabel1;
	private JLabel lLocalPort;
	private JLabel lRemoteHost;
	private JLabel lRemotePort;
	private JLabel lSsl;
	private JPanel pConnection;
	private JPanel pConnectionInfo;
	private JTabbedPane tabbedPane;
	private JTextField tfLocalPort;
	private JTextField tfRemoteHost;
	private JTextField tfRemotePort;
	private JTextPane tpInfo;
	private JCheckBox cbSsl;

	/**
	 * Creates new form MainWindow
	 * 
	 * @param bookmarkManager
	 *            Bookmark manager.
	 * @param configuration
	 *            tcpmon configuration to populate the fields.
	 */
	public MainWindow(final BookmarkManager bookmarkManager,
			final Configuration configuration)
	{
		this.bookmarkManager = bookmarkManager;
		this.configuration = configuration;

		// Set debug level.
		Debug.level = configuration.getDebugLevel();

		initComponents();
	}

	/**
	 * @return Configuration - configuration for the tcpmon app.
	 */
	public Configuration getConfiguration()
	{
		return configuration;
	}

	private void initComponents()
	{
		java.awt.GridBagConstraints gridBagConstraints;

		tabbedPane = new JTabbedPane();
		tabbedPane.setMinimumSize(new Dimension(640, 480));
		tabbedPane.setPreferredSize(new Dimension(640, 480));

		adminPanel = new JPanel();
		adminPanel.setLayout(new BorderLayout());
		adminPanel.setMinimumSize(new Dimension(400, 400));
		adminPanel.setPreferredSize(new Dimension(400, 400));

		pConnection = new JPanel();
		pConnection.setLayout(new GridBagLayout());

		jLabel1 = new JLabel();
		pConnectionInfo = new JPanel();
		lLocalPort = new JLabel();
		tfLocalPort = new JTextField();
		lRemoteHost = new JLabel();
		tfRemoteHost = new JTextField();
		lRemotePort = new JLabel();
		tfRemotePort = new JTextField();
		lSsl = new JLabel();
		cbSsl = new JCheckBox();

		bAddMonitor = new JButton();

		getContentPane().setLayout(new BorderLayout(5, 5));


		jLabel1.setText("Create a New TCP Monitor Connection: ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(50, 20, 0, 0);
		pConnection.add(jLabel1, gridBagConstraints);

		pConnectionInfo.setLayout(new GridBagLayout());

		pConnectionInfo.setMinimumSize(new Dimension(150, 100));
		pConnectionInfo.setPreferredSize(new Dimension(150, 100));
		lLocalPort.setText("Local Port: ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 16;
		gridBagConstraints.ipady = 9;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		pConnectionInfo.add(lLocalPort, gridBagConstraints);

		tfLocalPort.setText(configuration.getLocalPort());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 119;
		gridBagConstraints.ipady = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(0, 10, 0, 0);
		pConnectionInfo.add(tfLocalPort, gridBagConstraints);

		lRemoteHost.setText("Server Name: ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.ipadx = 3;
		gridBagConstraints.ipady = 9;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(4, 0, 0, 0);
		pConnectionInfo.add(lRemoteHost, gridBagConstraints);

		tfRemoteHost.setText(configuration.getRemoteHost());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.ipadx = 119;
		gridBagConstraints.ipady = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(6, 10, 0, 0);
		pConnectionInfo.add(tfRemoteHost, gridBagConstraints);

		lRemotePort.setText("Server Port: ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.ipadx = 6;
		gridBagConstraints.ipady = 9;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(2, 0, 0, 0);
		pConnectionInfo.add(lRemotePort, gridBagConstraints);

		tfRemotePort.setText(configuration.getRemotePort());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.ipadx = 119;
		gridBagConstraints.ipady = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(6, 10, 16, 0);
		pConnectionInfo.add(tfRemotePort, gridBagConstraints);

		lSsl.setText("SSL Server: ");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.ipadx = 6;
		gridBagConstraints.ipady = 9;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(2, 0, 0, 0);
		pConnectionInfo.add(lSsl, gridBagConstraints);

		cbSsl.setSelected(false);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.ipadx = 119;
		gridBagConstraints.ipady = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(6, 10, 16, 0);
		pConnectionInfo.add(cbSsl, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.ipadx = 90;
		gridBagConstraints.ipady = 40;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(15, 30, 0, 0);
		pConnection.add(pConnectionInfo, gridBagConstraints);

		bAddMonitor.setText("Add Monitor");
		bAddMonitor.addActionListener(new ActionListener()
		{
			public void actionPerformed(final ActionEvent evt)
			{
				bAddMonitorActionPerformed(evt);
			}
		});

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(0, 90, 278, 0);
		pConnection.add(bAddMonitor, gridBagConstraints);

		adminPanel.add(pConnection, BorderLayout.WEST);

		tabbedPane.addTab("Admin", adminPanel);

		add(tabbedPane, BorderLayout.CENTER);

		pack();
	}

	private void bAddMonitorActionPerformed(final ActionEvent evt)
	{
		MonitorPanel monitorPanel = new MonitorPanel();
		TunnelConfig tunnelConfig = new TunnelConfig(tfRemoteHost.getText(),
				tfRemotePort.getText(), tfLocalPort.getText(), true,
				cbSsl.isSelected());
		monitorPanel.start(tunnelConfig);

		tabbedPane.addTab("Port " + tunnelConfig.localPort, monitorPanel);
		tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(monitorPanel));
	}

}
