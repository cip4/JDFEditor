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
package org.cip4.tools.jdfeditor.player;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.StringArray;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.XMLDoc;
import org.cip4.jdflib.datatypes.JDFAttributeMap;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.UrlPart;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.jdflib.util.net.HTTPDetails;
import org.cip4.jdflib.util.zip.ZipReader;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;

/**
 * Stub backend for player lifecycle handling.
 */
public class PlayerBackend implements Runnable
{

	private static final Log LOG = LogFactory.getLog(PlayerBackend.class);
	private static PlayerBackend instance;

	public static synchronized PlayerBackend getInstance()
	{
		if (instance == null)
		{
			instance = new PlayerBackend();
		}
		return instance;
	}

	private final Object lock = new Object();

	private Thread thread;

	private volatile boolean running;

	private volatile boolean paused;

	private volatile boolean stepRequested;

	private volatile String currentSource = "";

	private volatile String currentTargetUrl = "";

	private volatile String currentItemName = "";

	private volatile int successCount;

	private volatile int noConnectionFailureCount;

	private volatile int invalidResponseFailureCount;

	private volatile int lastResponseCode;

	private volatile long successLastLatencyMillis;

	private volatile long successLatencySumMillis;

	private volatile long successLastEventMillis;

	private volatile long invalidLastLatencyMillis;

	private volatile long invalidLatencySumMillis;

	private volatile long invalidLastEventMillis;

	private volatile long noConnectionLastLatencyMillis;

	private volatile long noConnectionLatencySumMillis;

	private volatile long noConnectionLastEventMillis;

	private final List<PlayerStatusListener> listeners = new CopyOnWriteArrayList<>();

	private PlayerBackend()
	{
	}

	public synchronized void start()
	{
		if (thread != null && thread.isAlive())
		{
			return;
		}

		running = true;
		paused = false;
		thread = new Thread(this, "PlayerBackend");
		thread.start();
		LOG.info("Player backend started.");
		fireStatusChanged(PlayerStatus.RUNNING, "started");
	}

	public void pause()
	{
		paused = true;
		LOG.info("Player backend paused.");
		fireStatusChanged(PlayerStatus.PAUSED, "paused");
	}

	public void resume()
	{
		paused = false;
		synchronized (lock)
		{
			stepRequested = false;
			lock.notifyAll();
		}
		LOG.info("Player backend resumed.");
		fireStatusChanged(PlayerStatus.RUNNING, "resumed");
	}

	public void stepOnce()
	{
		synchronized (lock)
		{
			if (running && paused)
			{
				stepRequested = true;
				lock.notifyAll();
			}
		}
	}

	public void addPlayerStatusListener(final PlayerStatusListener l)
	{
		if (l != null)
		{
			listeners.add(l);
		}
	}

	public void removePlayerStatusListener(final PlayerStatusListener l)
	{
		if (l != null)
		{
			listeners.remove(l);
		}
	}

	private void fireStatusChanged(final PlayerStatus status, final String message)
	{
		for (final PlayerStatusListener listener : listeners)
		{
			try
			{
				listener.playerStatusChanged(status, message);
			}
			catch (final RuntimeException e)
			{
				LOG.warn("Player status listener failed.", e);
			}
		}
	}

	public boolean isRunning()
	{
		return running;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public int getSuccessCount()
	{
		return successCount;
	}

	public int getNoConnectionFailureCount()
	{
		return noConnectionFailureCount;
	}

	public int getInvalidResponseFailureCount()
	{
		return invalidResponseFailureCount;
	}

	public int getLastResponseCode()
	{
		return lastResponseCode;
	}

	public long getSuccessLastLatencyMillis()
	{
		return successLastLatencyMillis;
	}

	public long getSuccessAverageLatencyMillis()
	{
		return successCount > 0 ? successLatencySumMillis / successCount : 0L;
	}

	public long getSuccessLastEventMillis()
	{
		return successLastEventMillis;
	}

	public long getInvalidLastLatencyMillis()
	{
		return invalidLastLatencyMillis;
	}

	public long getInvalidAverageLatencyMillis()
	{
		return invalidResponseFailureCount > 0 ? invalidLatencySumMillis / invalidResponseFailureCount : 0L;
	}

	public long getInvalidLastEventMillis()
	{
		return invalidLastEventMillis;
	}

	public long getNoConnectionLastLatencyMillis()
	{
		return noConnectionLastLatencyMillis;
	}

	public long getNoConnectionAverageLatencyMillis()
	{
		return noConnectionFailureCount > 0 ? noConnectionLatencySumMillis / noConnectionFailureCount : 0L;
	}

	public long getNoConnectionLastEventMillis()
	{
		return noConnectionLastEventMillis;
	}

	public String getCurrentSource()
	{
		if (currentSource != null && !currentSource.isEmpty())
		{
			return currentSource;
		}

		final String configuredPath = SettingService.getSettingService().getSetting(SettingKey.PLAYER_FILE_PATH, String.class);
		if (configuredPath != null && !configuredPath.trim().isEmpty())
		{
			return configuredPath.trim();
		}

		return "";
	}

	public String getCurrentTargetUrl()
	{
		if (currentTargetUrl != null && !currentTargetUrl.isEmpty())
		{
			return currentTargetUrl;
		}

		final String history = SettingService.getSettingService().getSetting(SettingKey.PLAYER_URL, String.class);
		final String resolved = resolveTargetUrl(history);
		return resolved != null ? resolved : "";
	}

	public String getCurrentItemName()
	{
		return currentItemName;
	}

	public void stop()
	{
		final Thread currentThread;
		synchronized (this)
		{
			running = false;
			paused = false;
			currentThread = thread;
			thread = null;
		}

		if (currentThread != null)
		{
			currentThread.interrupt();
		}

		synchronized (lock)
		{
			lock.notifyAll();
		}

		if (currentThread != null)
		{
			try
			{
				currentThread.join(200L);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}

		LOG.info("Player backend stopped.");
		fireStatusChanged(PlayerStatus.STOPPED, "stopped");
	}

	@Override
	public void run()
	{
		final SettingService settingService = SettingService.getSettingService();
		String finishedMessage = "finished";
		boolean repeat;
		successCount = 0;
		noConnectionFailureCount = 0;
		invalidResponseFailureCount = 0;
		lastResponseCode = -1;
		successLastLatencyMillis = 0L;
		successLatencySumMillis = 0L;
		successLastEventMillis = 0L;
		invalidLastLatencyMillis = 0L;
		invalidLatencySumMillis = 0L;
		invalidLastEventMillis = 0L;
		noConnectionLastLatencyMillis = 0L;
		noConnectionLatencySumMillis = 0L;
		noConnectionLastEventMillis = 0L;
		stepRequested = false;
		currentSource = "";
		currentTargetUrl = "";
		currentItemName = "";

		do
		{
			repeat = false;
			final String sourcePath = settingService.getSetting(SettingKey.PLAYER_FILE_PATH, String.class);
			if (sourcePath == null || sourcePath.trim().isEmpty())
			{
				LOG.warn("Player backend: no source configured.");
				finishedMessage = "no source configured";
				break;
			}

			currentSource = sourcePath.trim();
			final List<PlaybackItem> playbackItems = loadPlaybackItems(sourcePath.trim());
			if (playbackItems.isEmpty())
			{
				LOG.warn("Player backend: no playable documents loaded from source: " + sourcePath);
				finishedMessage = "no documents loaded";
				break;
			}

			final String urlHistory = settingService.getSetting(SettingKey.PLAYER_URL, String.class);
			final String targetUrl = resolveTargetUrl(urlHistory);
			if (targetUrl == null)
			{
				LOG.warn("Player backend: no target URL configured.");
				finishedMessage = "no target URL configured";
				break;
			}

			final URL url = UrlUtil.stringToURL(targetUrl);
			if (url == null)
			{
				LOG.warn("Player backend: invalid target URL configured: " + targetUrl);
				finishedMessage = "invalid target URL configured";
				break;
			}
			currentTargetUrl = targetUrl;

			final boolean constantDelay = settingService.getBool(SettingKey.PLAYER_CONSTANT_DELAY);
			final int delaySeconds = settingService.getInt(SettingKey.PLAYER_DELAY_SECONDS);
			final int timelapseFactor = settingService.getInt(SettingKey.PLAYER_TIMELAPSE_FACTOR);

			long prevMillis = -1L;
			for (final PlaybackItem playbackItem : playbackItems)
			{
				awaitResumeOrStop();
				if (!running)
				{
					finishedMessage = "stopped";
					break;
				}

				currentItemName = playbackItem.name;

				final HTTPDetails details = new HTTPDetails();
				final long sendStartMillis = System.currentTimeMillis();
				final UrlPart p = playbackItem.doc.write2HttpURL(url, details);
				final long latencyMillis = System.currentTimeMillis() - sendStartMillis;
				final long eventMillis = System.currentTimeMillis();
				final int rc = UrlPart.getReturnCode(p);
				final boolean ok = UrlPart.isReturnCodeOK(p);
				lastResponseCode = rc;
				if (ok)
				{
					successCount++;
					successLastLatencyMillis = latencyMillis;
					successLatencySumMillis += latencyMillis;
					successLastEventMillis = eventMillis;
				}
				else if (rc == -1)
				{
					noConnectionFailureCount++;
					noConnectionLastLatencyMillis = latencyMillis;
					noConnectionLatencySumMillis += latencyMillis;
					noConnectionLastEventMillis = eventMillis;
				}
				else
				{
					invalidResponseFailureCount++;
					invalidLastLatencyMillis = latencyMillis;
					invalidLatencySumMillis += latencyMillis;
					invalidLastEventMillis = eventMillis;
				}

				JDFJMF responseJmf = null;
				if (p != null)
				{
					p.buffer();
					final String s = p.getResponseString(10);
					if (s.length() == 10)
					{
						final XMLDoc respXml = p.getXMLDoc();
						if (respXml != null)
						{
							responseJmf = new JDFDoc(respXml).getJMFRoot();
						}
					}
				}

				if (LOG.isInfoEnabled())
				{
					LOG.info("Player backend sent '" + playbackItem.name + "' -> " + targetUrl + " HTTP=" + rc + " result=" + (ok ? "ok" : "fail")
							+ " responseJMF=" + (responseJmf != null));
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Player backend response JMF type for '" + playbackItem.name + "': "
							+ (responseJmf == null ? "none" : responseJmf.getClass().getSimpleName()));
				}
				fireStatusChanged(PlayerStatus.RUNNING, "sent " + playbackItem.name + " -> HTTP " + rc + " (" + (ok ? "ok" : "fail") + "), successes="
						+ successCount + ", noConnection=" + noConnectionFailureCount + ", invalidResponse=" + invalidResponseFailureCount);

				if (!running)
				{
					finishedMessage = "stopped";
					break;
				}

				long waitMillis = delaySeconds * 1000L;
				if (!constantDelay)
				{
					final long currentMillis = getMessageTimeMillis(playbackItem.doc);
					if (prevMillis >= 0L && currentMillis >= 0L)
					{
						waitMillis = Math.max(0L, (currentMillis - prevMillis) / timelapseFactor);
					}
					if (currentMillis >= 0L)
					{
						prevMillis = currentMillis;
					}
				}

				if (!sleepCooperatively(waitMillis))
				{
					finishedMessage = "stopped";
					break;
				}
			}

			if (!running)
			{
				break;
			}

			final Boolean configuredRepeat = settingService.getSetting(SettingKey.PLAYER_REPEAT, Boolean.class);
			repeat = configuredRepeat != null && configuredRepeat.booleanValue();
			if (!repeat)
			{
				break;
			}

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Player backend repeating playback cycle.");
			}

		}
		while (running && repeat);

		running = false;
		currentItemName = "";
		synchronized (this)
		{
			if (thread == Thread.currentThread())
			{
				thread = null;
			}
		}

		if (!"stopped".equals(finishedMessage))
		{
			fireStatusChanged(PlayerStatus.STOPPED, finishedMessage);
		}

		LOG.info("Player backend thread ended (" + finishedMessage + ").");
	}

	private void awaitResumeOrStop()
	{
		if (paused)
		{
			synchronized (lock)
			{
				while (paused && running && !stepRequested)
				{
					try
					{
						lock.wait();
					}
					catch (final InterruptedException e)
					{
						Thread.currentThread().interrupt();
						if (!running)
						{
							break;
						}
					}
				}
				stepRequested = false;
			}
		}
	}

	private boolean sleepCooperatively(final long waitMillis)
	{
		if (paused)
		{
			return running;
		}

		if (waitMillis <= 0L)
		{
			return running;
		}

		try
		{
			Thread.sleep(waitMillis);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			if (!running)
			{
				return false;
			}
		}
		return running;
	}

	private List<PlaybackItem> loadPlaybackItems(final String sourcePath)
	{
		final List<PlaybackItem> playbackItems = new ArrayList<>();
		final File source = new File(sourcePath);

		if (source.isDirectory())
		{
			final File[] files = source.listFiles(File::isFile);
			if (files != null)
			{
				Arrays.sort(files, Comparator.comparing(File::getName));
				for (final File file : files)
				{
					final JDFDoc doc = JDFDoc.parseFile(file);
					if (doc != null)
					{
						playbackItems.add(new PlaybackItem(file.getName(), doc));
					}
				}
			}
			return playbackItems;
		}

		final ZipReader zipReader = new ZipReader(source);
		try
		{
			zipReader.buffer();
			final Vector<ZipEntry> entries = zipReader.getEntries();
			final StringArray names = new StringArray();
			for (final ZipEntry entry : entries)
			{
				names.add(entry.getName());
			}
			names.sort(null);

			for (final String name : names)
			{
				final ZipEntry entry = zipReader.getEntry(name);
				if (entry == null || entry.isDirectory())
				{
					continue;
				}
				final JDFDoc doc = zipReader.getJDFDoc();
				if (doc != null)
				{
					playbackItems.add(new PlaybackItem(name, doc));
				}
			}
		}
		catch (final RuntimeException e)
		{
			LOG.warn("Player backend failed loading zip source: " + sourcePath, e);
		}
		finally
		{
			zipReader.close();
		}

		return playbackItems;
	}

	private String resolveTargetUrl(final String urlHistory)
	{
		if (urlHistory == null || urlHistory.isEmpty())
		{
			return null;
		}

		for (final String token : urlHistory.split(";"))
		{
			if (token == null)
			{
				continue;
			}
			final String trimmed = token.trim();
			if (!trimmed.isEmpty())
			{
				return trimmed;
			}
		}

		return null;
	}

	private long getMessageTimeMillis(final JDFDoc doc)
	{
		final JDFJMF jmf = doc == null ? null : doc.getJMFRoot();
		if (jmf == null)
		{
			return -1L;
		}

		JDFMessage message = jmf.getSignal(0, false);
		if (message == null)
		{
			final VElement messages = jmf.getMessageVector(null, null);
			message = messages != null && !messages.isEmpty() ? (JDFMessage) messages.get(0) : null;
		}

		if (message == null)
		{
			return -1L;
		}

		final JDFDate time = message.getTime();
		return time == null ? -1L : time.getTimeInMillis();
	}

	static final class PlaybackItem
	{
		private final String name;
		private final JDFDoc doc;

		PlaybackItem(final String name, final JDFDoc doc)
		{
			this.name = name;
			this.doc = doc;
		}

		private JDFDoc getDoc(final boolean setCurrentTimestamp, final boolean generateUniqueIds, final boolean updateDeviceId, final String deviceId)
		{
			return applyPlaybackTransformations(doc, setCurrentTimestamp, generateUniqueIds, updateDeviceId, deviceId);
		}

		JDFDoc applyPlaybackTransformations(final JDFDoc doc, final boolean setCurrentTimestamp, final boolean generateUniqueIds, final boolean updateDeviceId,
				final String deviceId)
		{
			final boolean applyDeviceId = updateDeviceId && deviceId != null && !deviceId.isEmpty();
			if (!setCurrentTimestamp && !generateUniqueIds && !applyDeviceId)
			{
				return doc;
			}

			final JDFJMF jmf = doc == null ? null : doc.getJMFRoot();
			if (jmf == null)
			{
				return doc;
			}

			if (applyDeviceId)
			{
				jmf.setSenderID(deviceId);
				final VElement vdev = jmf.getChildrenByTagName(null, null, new JDFAttributeMap(AttributeName.DEVICEID, (String) null), false, true, 0);
				for (final KElement e : vdev)
				{
					e.setAttribute(AttributeName.DEVICEID, deviceId);
				}
			}

			if (setCurrentTimestamp)
			{
				jmf.setTimeStamp(null);
			}
			if (generateUniqueIds)
			{
				regenerateElementId(jmf);
			}

			final VElement messages = jmf.getMessageVector(null, null);
			if (messages == null || messages.isEmpty())
			{
				return doc;
			}

			for (final KElement element : messages)
			{
				final JDFMessage message = (JDFMessage) element;
				if (setCurrentTimestamp)
				{
					message.setTime(null);
				}
				if (generateUniqueIds)
				{
					regenerateElementId(message);
				}
			}

			return doc;
		}

		void regenerateElementId(final KElement element)
		{
			if (element != null)
			{
				// appendAnchor keeps existing IDs, so clear ID first to force regeneration.
				element.removeAttribute(AttributeName.ID, null);
				element.appendAnchor(null);
			}
		}
	}
}
