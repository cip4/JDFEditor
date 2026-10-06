/*
 * The CIP4 Software License, Version 1.0
 */
package org.cip4.tools.jdfeditor.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JMFBuilder;
import org.cip4.jdflib.util.JDFDate;
import org.junit.Test;

public class PlayerBackendTest
{

	@Test
	public void testSetsCurrentTimestamp()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		final JDFMessage message = getFirstMessage(jmf);
		message.setTime(new JDFDate(1000000L));

		new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, true, false, false, "");

		final long deltaMillis = Math.abs(System.currentTimeMillis() - message.getTime().getTimeInMillis());
		assertTrue("Message timestamp was not set close to now.", deltaMillis <= 60000L);
	}

	@Test
	public void testDoesNotChangeTimestampWhenDisabled()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		final JDFMessage message = getFirstMessage(jmf);
		message.setTime(new JDFDate(1234567000L));
		final long originalTimeMillis = message.getTime().getTimeInMillis();

		new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, false, false, false, "");

		assertEquals("Message timestamp changed unexpectedly.", originalTimeMillis, message.getTime().getTimeInMillis());
	}

	@Test
	public void testRegeneratesUniqueIds()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		final String originalRootId = jmf.getID();
		final VElement messages = jmf.getMessageVector(null, null);
		assertNotNull("Expected at least one JMF message.", messages);
		assertFalse("Expected at least one JMF message.", messages.isEmpty());
		final JDFMessage firstMessage = (JDFMessage) messages.get(0);
		final String originalMessageId = firstMessage.getID();
		assertNotNull("Expected message to have an existing ID.", originalMessageId);
		assertFalse("Expected message to have a non-empty ID.", originalMessageId.isEmpty());

		new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, false, true, false, "");

		final String newMessageId = firstMessage.getID();
		assertNotNull("Message ID should be regenerated.", newMessageId);
		assertFalse("Message ID should be non-empty after regeneration.", newMessageId.isEmpty());
		assertNotEquals("Message ID should change when unique IDs are regenerated.", originalMessageId, newMessageId);

		final String newRootId = jmf.getID();
		assertNotNull("JMF root ID should be present after regeneration.", newRootId);
		assertFalse("JMF root ID should be non-empty after regeneration.", newRootId.isEmpty());
		assertNotEquals("JMF root ID should change when unique IDs are regenerated.", originalRootId, newRootId);

		if (messages.size() > 1)
		{
			final Set<String> ids = new HashSet<>();
			for (final Object element : messages)
			{
				final JDFMessage message = (JDFMessage) element;
				final String id = message.getID();
				assertNotNull("Regenerated message ID should not be null.", id);
				assertFalse("Regenerated message ID should not be empty.", id.isEmpty());
				assertTrue("Regenerated message IDs should be distinct.", ids.add(id));
			}
		}
	}

	@Test
	public void testDoesNotChangeIdsWhenDisabled()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		final JDFMessage message = getFirstMessage(jmf);
		final String originalMessageId = message.getID();

		new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, false, false, false, "");

		assertEquals("Message ID changed unexpectedly.", originalMessageId, message.getID());
	}

	@Test
	public void testNoOpWhenBothDisabled()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		final JDFMessage message = getFirstMessage(jmf);
		message.setTime(new JDFDate(2233445566L));
		final String originalMessageId = message.getID();
		final long originalTimeMillis = message.getTime().getTimeInMillis();

		final JDFDoc returnedDoc = new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, false, false, false, "");

		assertSame("Expected same doc instance when both flags are disabled.", doc, returnedDoc);
		assertEquals("Message ID changed unexpectedly.", originalMessageId, message.getID());
		assertEquals("Message timestamp changed unexpectedly.", originalTimeMillis, message.getTime().getTimeInMillis());
	}

	@Test
	public void testSetsDeviceIdAsSenderId()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();

		new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, false, false, true, "DEV-123");

		assertEquals("Device ID should be set as the JMF SenderID.", "DEV-123", jmf.getSenderID());
	}

	@Test
	public void testDoesNotChangeSenderIdWhenUpdateDisabled()
	{
		final JDFJMF jmf = new JMFBuilder().buildKnownMessagesQuery();
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		final String originalSenderId = jmf.getSenderID();

		new PlayerBackend.PlaybackItem("test", doc).applyPlaybackTransformations(doc, false, false, false, "DEV-999");

		assertEquals("SenderID should be unchanged when update device id is disabled.", originalSenderId, jmf.getSenderID());
	}

	private JDFMessage getFirstMessage(final JDFJMF jmf)
	{
		final VElement messages = jmf.getMessageVector(null, null);
		assertNotNull("Expected at least one JMF message.", messages);
		assertFalse("Expected at least one JMF message.", messages.isEmpty());
		return (JDFMessage) messages.get(0);
	}
}
