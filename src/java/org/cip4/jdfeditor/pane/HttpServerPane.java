package org.cip4.jdfeditor.pane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;


public class HttpServerPane {
	
	public HttpServerPane() {
	}
	
	public JPanel createPane() {
		JPanel httpPanel = new JPanel(new BorderLayout());
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel("HTTP server:"), BorderLayout.NORTH);
		
		JPanel settingsPanel = new JPanel();
		SpringLayout settingsLayout = new SpringLayout();
		settingsPanel.setLayout(settingsLayout);
		
		JLabel ipLabel = new JLabel("IP-Address:");
		settingsLayout.putConstraint(SpringLayout.WEST, ipLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipLabel, 10, SpringLayout.NORTH, settingsPanel);
		
		JLabel ipValueLabel = new JLabel("192.168.0.1");
		settingsLayout.putConstraint(SpringLayout.WEST, ipValueLabel, 5, SpringLayout.EAST, ipLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, ipValueLabel, 10, SpringLayout.NORTH, settingsPanel);
		
		JLabel portLabel = new JLabel("Port:");
		settingsLayout.putConstraint(SpringLayout.WEST, portLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, portLabel, 10, SpringLayout.SOUTH, ipLabel);
		
		JTextField portValueLabel = new JTextField("80", 5);
		settingsLayout.putConstraint(SpringLayout.WEST, portValueLabel, 0, SpringLayout.WEST, ipValueLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, portValueLabel, 10, SpringLayout.SOUTH, ipLabel);
		
		JLabel statusLabel = new JLabel("Status:");
		settingsLayout.putConstraint(SpringLayout.WEST, statusLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusLabel, 10, SpringLayout.SOUTH, portLabel);
		
		JLabel statusValueLabel = new JLabel("---");
		settingsLayout.putConstraint(SpringLayout.WEST, statusValueLabel, 0, SpringLayout.WEST, portValueLabel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusValueLabel, 10, SpringLayout.SOUTH, portLabel);
		
		settingsPanel.add(ipLabel);
		settingsPanel.add(ipValueLabel);
		settingsPanel.add(portLabel);
		settingsPanel.add(portValueLabel);
		settingsPanel.add(statusLabel);
		settingsPanel.add(statusValueLabel);
		
		leftPanel.add(settingsPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(new JButton("Start"));
		buttonsPanel.add(new JButton("Stop"));
		
		leftPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		JPanel rightTopPanel = new JPanel(new BorderLayout());
		rightTopPanel.add(new JLabel("Received messages:"), BorderLayout.NORTH);
		
		String[] columnNames = {"Sender ID", "Message Type", "Time Received", "Size"};
		Object[][] data = {
				{"a1", "a2", "a3", "a4"},
				{"b1", "b2", "b3", "b4"}
		};
		JTable table = new JTable(data, columnNames);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		rightTopPanel.add(scrollPane, BorderLayout.CENTER);

		
		rightTopPanel.add(new JButton("Clear all"), BorderLayout.SOUTH);
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		JPanel rightBottomPanel = new JPanel(new BorderLayout());
		rightBottomPanel.add(new JLabel("Path message store:"), BorderLayout.NORTH);
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		pathPanel.add(new JButton("..."));
		pathPanel.add(new JLabel("/var/tmp/JDFEditor/ReceivedMessages/"));
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

}
