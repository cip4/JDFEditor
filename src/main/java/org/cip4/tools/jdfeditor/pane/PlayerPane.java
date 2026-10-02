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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.cip4.tools.jdfeditor.player.PlayerBackend;
import org.cip4.tools.jdfeditor.player.PlayerStatus;
import org.cip4.tools.jdfeditor.player.PlayerStatusListener;
import org.cip4.tools.jdfeditor.util.ResourceUtil;

/**
 * Class that implements a "Player" tab/panel.
 */
public class PlayerPane implements PlayerStatusListener, ActionListener
{
	private static final Color PASTEL_GREEN = new Color(198, 239, 206);
	private static final Color PASTEL_YELLOW = new Color(255, 235, 156);
	private static final Color PASTEL_RED = new Color(255, 199, 206);

	private final PlayerBackend playerBackend = PlayerBackend.getInstance();

	private JLabel statusValueLabel;
	private JLabel activityValueLabel;
	private JLabel sourceValueLabel;
	private JLabel currentItemValueLabel;
	private JLabel targetUrlValueLabel;
	private JLabel responseCodeValueLabel;

	private JTable statsTable;
	private DefaultTableModel statsTableModel;
	private volatile int highlightRow = -1;
	private final SimpleDateFormat statsTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private JButton startButton;
	private JButton pauseButton;
	private JButton stopButton;
	private JButton resumeButton;
	private JButton sendNextButton;

	private volatile PlayerStatus currentStatus;
	private volatile String latestActivityMessage;

	public PlayerPane()
	{
		super();
		currentStatus = determineCurrentStatus();
		latestActivityMessage = "";
		playerBackend.addPlayerStatusListener(this);
	}

	public JPanel createPane()
	{
		final JPanel playerPanel = new JPanel(new BorderLayout());

		final JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel(ResourceUtil.getMessage("PlayerKey") + ":"), BorderLayout.NORTH);

		final JPanel settingsPanel = new JPanel();
		final SpringLayout settingsLayout = new SpringLayout();
		settingsPanel.setLayout(settingsLayout);

		final JLabel statusLabel = new JLabel(ResourceUtil.getMessage("Status") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, statusLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, statusLabel, 10, SpringLayout.NORTH, settingsPanel);

		statusValueLabel = new JLabel(localizeStatus(currentStatus));
		settingsLayout.putConstraint(SpringLayout.NORTH, statusValueLabel, 0, SpringLayout.NORTH, statusLabel);

		final JLabel activityLabel = new JLabel(ResourceUtil.getMessage("PlayerMessageKey") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, activityLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, activityLabel, 10, SpringLayout.SOUTH, statusLabel);

		activityValueLabel = new JLabel(latestActivityMessage);
		settingsLayout.putConstraint(SpringLayout.NORTH, activityValueLabel, 0, SpringLayout.NORTH, activityLabel);

		final JLabel sourceLabel = new JLabel(ResourceUtil.getMessage("PlayerSourceKey") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, sourceLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, sourceLabel, 10, SpringLayout.SOUTH, activityLabel);

		sourceValueLabel = new JLabel(playerBackend.getCurrentSource());
		settingsLayout.putConstraint(SpringLayout.NORTH, sourceValueLabel, 0, SpringLayout.NORTH, sourceLabel);

		final JLabel currentItemLabel = new JLabel(ResourceUtil.getMessage("PlayerCurrentMessageKey") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, currentItemLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, currentItemLabel, 10, SpringLayout.SOUTH, sourceLabel);

		currentItemValueLabel = new JLabel(playerBackend.getCurrentItemName());
		settingsLayout.putConstraint(SpringLayout.NORTH, currentItemValueLabel, 0, SpringLayout.NORTH, currentItemLabel);

		final JLabel targetUrlLabel = new JLabel(ResourceUtil.getMessage("PlayerTargetUrlKey") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, targetUrlLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, targetUrlLabel, 10, SpringLayout.SOUTH, currentItemLabel);

		targetUrlValueLabel = new JLabel(playerBackend.getCurrentTargetUrl());
		settingsLayout.putConstraint(SpringLayout.NORTH, targetUrlValueLabel, 0, SpringLayout.NORTH, targetUrlLabel);

		final JLabel responseCodeLabel = new JLabel(ResourceUtil.getMessage("PlayerResponseCodeKey") + ":");
		settingsLayout.putConstraint(SpringLayout.WEST, responseCodeLabel, 5, SpringLayout.WEST, settingsPanel);
		settingsLayout.putConstraint(SpringLayout.NORTH, responseCodeLabel, 10, SpringLayout.SOUTH, targetUrlLabel);

		responseCodeValueLabel = new JLabel(String.valueOf(playerBackend.getLastResponseCode()));
		settingsLayout.putConstraint(SpringLayout.NORTH, responseCodeValueLabel, 0, SpringLayout.NORTH, responseCodeLabel);

		// align all value labels in one column, 5px east of the widest caption (currentItemLabel)
		settingsLayout.putConstraint(SpringLayout.WEST, statusValueLabel, 5, SpringLayout.EAST, currentItemLabel);
		settingsLayout.putConstraint(SpringLayout.WEST, activityValueLabel, 5, SpringLayout.EAST, currentItemLabel);
		settingsLayout.putConstraint(SpringLayout.WEST, sourceValueLabel, 5, SpringLayout.EAST, currentItemLabel);
		settingsLayout.putConstraint(SpringLayout.WEST, currentItemValueLabel, 5, SpringLayout.EAST, currentItemLabel);
		settingsLayout.putConstraint(SpringLayout.WEST, targetUrlValueLabel, 5, SpringLayout.EAST, currentItemLabel);
		settingsLayout.putConstraint(SpringLayout.WEST, responseCodeValueLabel, 5, SpringLayout.EAST, currentItemLabel);

		settingsLayout.putConstraint(SpringLayout.SOUTH, settingsPanel, 10, SpringLayout.SOUTH, responseCodeValueLabel);

		settingsPanel.add(statusLabel);
		settingsPanel.add(statusValueLabel);
		settingsPanel.add(activityLabel);
		settingsPanel.add(activityValueLabel);
		settingsPanel.add(sourceLabel);
		settingsPanel.add(sourceValueLabel);
		settingsPanel.add(currentItemLabel);
		settingsPanel.add(currentItemValueLabel);
		settingsPanel.add(targetUrlLabel);
		settingsPanel.add(targetUrlValueLabel);
		settingsPanel.add(responseCodeLabel);
		settingsPanel.add(responseCodeValueLabel);

		final String[] statsColumns = { ResourceUtil.getMessage("PlayerStatCountKey"), ResourceUtil.getMessage("PlayerStatCategoryKey"), ResourceUtil.getMessage("PlayerStatLastKey"), ResourceUtil.getMessage("PlayerStatAvgKey"), ResourceUtil.getMessage("PlayerStatLastEventKey") };
		statsTableModel = new DefaultTableModel(statsColumns, 0)
		{
			@Override
			public boolean isCellEditable(final int row, final int column)
			{
				return false;
			}
		};
		statsTableModel.addRow(new Object[] { "0", ResourceUtil.getMessage("PlayerSuccessCountKey"), "-", "-", "-" });
		statsTableModel.addRow(new Object[] { "0", ResourceUtil.getMessage("PlayerInvalidResponseCountKey"), "-", "-", "-" });
		statsTableModel.addRow(new Object[] { "0", ResourceUtil.getMessage("PlayerNoConnectionCountKey"), "-", "-", "-" });
		statsTable = new JTable(statsTableModel);
		statsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
			{
				final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected)
				{
					final int modelRow = table.convertRowIndexToModel(row);
					Color background = table.getBackground();
					if (modelRow == highlightRow)
					{
						if (modelRow == 0)
						{
							background = PASTEL_GREEN;
						}
						else if (modelRow == 1)
						{
							background = PASTEL_YELLOW;
						}
						else if (modelRow == 2)
						{
							background = PASTEL_RED;
						}
					}
					component.setBackground(background);
				}
				return component;
			}
		});

		final JPanel buttonsPanel = new JPanel();
		startButton = new JButton(ResourceUtil.getMessage("Start"), ResourceUtil.getImageIcon("toolbar/play.png"));
		pauseButton = new JButton(ResourceUtil.getMessage("Pause"), ResourceUtil.getImageIcon("toolbar/pause.png"));
		stopButton = new JButton(ResourceUtil.getMessage("Stop"), ResourceUtil.getImageIcon("toolbar/stop.png"));
		resumeButton = new JButton(ResourceUtil.getMessage("Resume"), ResourceUtil.getImageIcon("toolbar/play.png"));
		sendNextButton = new JButton(ResourceUtil.getMessage("SendNext"));
		startButton.addActionListener(this);
		pauseButton.addActionListener(this);
		stopButton.addActionListener(this);
		resumeButton.addActionListener(this);
		sendNextButton.addActionListener(this);
		buttonsPanel.add(startButton);
		buttonsPanel.add(pauseButton);
		buttonsPanel.add(resumeButton);
		buttonsPanel.add(sendNextButton);
		buttonsPanel.add(stopButton);

		final JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(settingsPanel, BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(statsTable), BorderLayout.CENTER);
		leftPanel.add(centerPanel, BorderLayout.CENTER);
		leftPanel.add(buttonsPanel, BorderLayout.SOUTH);
		playerPanel.add(leftPanel, BorderLayout.CENTER);

		currentStatus = determineCurrentStatus();
		updateButtons();
		updateStatsTable();

		return playerPanel;
	}

	@Override
	public void playerStatusChanged(final PlayerStatus status, final String message)
	{
		currentStatus = status;
		latestActivityMessage = message == null ? "" : message;
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				updateLabels();
				updateStatsTable();
				updateButtons();
			}
		});
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object source = e.getSource();
		if (source == startButton)
		{
			playerBackend.start();
		}
		else if (source == resumeButton)
		{
			playerBackend.resume();
		}
		else if (source == pauseButton)
		{
			playerBackend.pause();
		}
		else if (source == stopButton)
		{
			playerBackend.stop();
		}
		else if (source == sendNextButton)
		{
			playerBackend.stepOnce();
		}
	}

	private void updateLabels()
	{
		if (statusValueLabel != null)
		{
			statusValueLabel.setText(localizeStatus(currentStatus));
		}
		if (activityValueLabel != null)
		{
			activityValueLabel.setText(latestActivityMessage);
		}
		if (sourceValueLabel != null)
		{
			sourceValueLabel.setText(playerBackend.getCurrentSource());
		}
		if (currentItemValueLabel != null)
		{
			currentItemValueLabel.setText(playerBackend.getCurrentItemName());
		}
		if (targetUrlValueLabel != null)
		{
			targetUrlValueLabel.setText(playerBackend.getCurrentTargetUrl());
		}
		if (responseCodeValueLabel != null)
		{
			responseCodeValueLabel.setText(String.valueOf(playerBackend.getLastResponseCode()));
		}
	}

	public void refreshConfiguredValues()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				updateLabels();
			}
		});
	}

	private void updateStatsTable()
	{
		if (statsTableModel == null)
		{
			return;
		}
		setStatsRow(0, playerBackend.getSuccessLastLatencyMillis(), playerBackend.getSuccessAverageLatencyMillis(), playerBackend.getSuccessLastEventMillis());
		setStatsRow(1, playerBackend.getInvalidLastLatencyMillis(), playerBackend.getInvalidAverageLatencyMillis(), playerBackend.getInvalidLastEventMillis());
		setStatsRow(2, playerBackend.getNoConnectionLastLatencyMillis(), playerBackend.getNoConnectionAverageLatencyMillis(), playerBackend.getNoConnectionLastEventMillis());

		statsTableModel.setValueAt(String.valueOf(playerBackend.getSuccessCount()), 0, 0);
		statsTableModel.setValueAt(String.valueOf(playerBackend.getInvalidResponseFailureCount()), 1, 0);
		statsTableModel.setValueAt(String.valueOf(playerBackend.getNoConnectionFailureCount()), 2, 0);

		final long s = playerBackend.getSuccessLastEventMillis();
		final long inv = playerBackend.getInvalidLastEventMillis();
		final long noc = playerBackend.getNoConnectionLastEventMillis();
		int hr = -1;
		long best = 0L;
		if (s > best)
		{
			hr = 0;
			best = s;
		}
		if (inv > best)
		{
			hr = 1;
			best = inv;
		}
		if (noc > best)
		{
			hr = 2;
			best = noc;
		}
		highlightRow = hr;
		if (statsTable != null)
		{
			statsTable.repaint();
		}
	}

	private void setStatsRow(final int row, final long lastMillis, final long avgMillis, final long lastEventMillis)
	{
		if (lastEventMillis <= 0L)
		{
			statsTableModel.setValueAt("-", row, 2);
			statsTableModel.setValueAt("-", row, 3);
			statsTableModel.setValueAt("-", row, 4);
			return;
		}
		statsTableModel.setValueAt(String.valueOf(lastMillis), row, 2);
		statsTableModel.setValueAt(String.valueOf(avgMillis), row, 3);
		statsTableModel.setValueAt(statsTimeFormat.format(new Date(lastEventMillis)), row, 4);
	}

	private void updateButtons()
	{
		if (startButton == null || pauseButton == null || stopButton == null || resumeButton == null || sendNextButton == null)
		{
			return;
		}

		if (currentStatus == PlayerStatus.RUNNING)
		{
			startButton.setEnabled(false);
			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);
			resumeButton.setEnabled(false);
			sendNextButton.setEnabled(false);
		}
		else if (currentStatus == PlayerStatus.PAUSED)
		{
			startButton.setEnabled(false);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(true);
			resumeButton.setEnabled(true);
			sendNextButton.setEnabled(true);
		}
		else
		{
			startButton.setEnabled(true);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(false);
			resumeButton.setEnabled(false);
			sendNextButton.setEnabled(false);
		}
	}

	private PlayerStatus determineCurrentStatus()
	{
		if (playerBackend.isPaused())
		{
			return PlayerStatus.PAUSED;
		}
		if (playerBackend.isRunning())
		{
			return PlayerStatus.RUNNING;
		}
		return PlayerStatus.STOPPED;
	}

	private String localizeStatus(final PlayerStatus status)
	{
		if (status == PlayerStatus.RUNNING)
		{
			return ResourceUtil.getMessage("Running");
		}
		if (status == PlayerStatus.PAUSED)
		{
			return ResourceUtil.getMessage("Paused");
		}
		return ResourceUtil.getMessage("Stopped");
	}
}