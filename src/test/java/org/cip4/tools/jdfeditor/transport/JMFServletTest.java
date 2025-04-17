/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2024 The International Cooperation for the Integration of
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
 *    Alternately, this acknowledgment mrSubRefay appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior writtenrestartProcesses()
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
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIrSubRefAL DAMAGES (INCLUDING, BUT NOT
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
 * originally based on software restartProcesses()
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG
 * copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *
 */
package org.cip4.tools.jdfeditor.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.cip4.jdflib.util.JDFDate;
import org.cip4.tools.jdfeditor.EditorTabbedPaneB;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.pane.HttpServerPane;
import org.cip4.tools.jdfeditor.pane.MessageBean;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class JMFServletTest
{
	private static final String CONTENT_MULTIPART_RELATED = "multipart/related";
	private static final String CONTENT_PLAIN_JMF = "application/vnd.cip4-jmf+xml";

	private static final String EXPECTED_BEGIN_OF_COMPUTER_ERA = new JDFDate(0).getFormattedDateTime(JDFDate.DATETIMEREADABLE);

	@Mock
	private HttpServletRequest httpServletRequest;

	@Mock
	private HttpServletResponse httpServletResponse;

	@Mock
	private JDFFrame jdfFrame;

	@Mock
	private EditorTabbedPaneB editorTabbedPaneB;

	@Mock
	private HttpServerPane httpServerPane;

	private StubServletInputStream stubServletInputStream;

	@InjectMocks
	private final JMFServlet jmfServlet = new JMFServlet();

	@Test
	public void shouldReturnErrorForHttpGetRequest() throws IOException
	{
		jmfServlet.doGet(httpServletRequest, httpServletResponse);
		verify(httpServletResponse, times(1)).sendError(eq(HttpServletResponse.SC_NOT_IMPLEMENTED), any(String.class));
	}

	@Test
	public void shouldAddOneItemToHttpServerPaneSmallFile() throws IOException
	{
		stubServletInputStream = new StubServletInputStream("samples/mime-multipart-related/one-jmf-jdf.mjm");

		when(jdfFrame.getBottomTabs()).thenReturn(editorTabbedPaneB);
		when(editorTabbedPaneB.getHttpPanel()).thenReturn(httpServerPane);

		when(httpServletRequest.getHeader("Content-type")).thenReturn(CONTENT_MULTIPART_RELATED);
		when(httpServletRequest.getInputStream()).thenReturn(stubServletInputStream);

		jmfServlet.doPost(httpServletRequest, httpServletResponse);

		verify(httpServerPane, times(1)).addMessage(any(MessageBean.class));

		final ArgumentCaptor<MessageBean> argument = ArgumentCaptor.forClass(MessageBean.class);
		verify(httpServerPane).addMessage(argument.capture());

		assertEquals("---", argument.getValue().getSenderId());
		assertEquals("MJM (JMF,JDF)", argument.getValue().getMessageType());
		assertNotNull(argument.getValue().getTimeReceived());
		assertEquals(EXPECTED_BEGIN_OF_COMPUTER_ERA, argument.getValue().getMessageDate());
		assertEquals("7 KB", argument.getValue().getSize());
		assertNotNull(argument.getValue().getFilePathName());
	}

	@Test
	public void shouldAddOneItemToHttpServerPaneBigFile() throws IOException
	{
		stubServletInputStream = new StubServletInputStream("samples/mime-multipart-jmf-jdf-pdf/elk-approval.mjm");

		when(jdfFrame.getBottomTabs()).thenReturn(editorTabbedPaneB);
		when(editorTabbedPaneB.getHttpPanel()).thenReturn(httpServerPane);

		when(httpServletRequest.getHeader("Content-type")).thenReturn(CONTENT_MULTIPART_RELATED);
		when(httpServletRequest.getInputStream()).thenReturn(stubServletInputStream);

		jmfServlet.doPost(httpServletRequest, httpServletResponse);

		verify(httpServerPane, times(1)).addMessage(any(MessageBean.class));

		final ArgumentCaptor<MessageBean> argument = ArgumentCaptor.forClass(MessageBean.class);
		verify(httpServerPane).addMessage(argument.capture());

		assertEquals("---", argument.getValue().getSenderId());
		assertEquals("MJM (JMF,JDF,PDF)", argument.getValue().getMessageType());
		assertNotNull(argument.getValue().getTimeReceived());
		assertEquals(EXPECTED_BEGIN_OF_COMPUTER_ERA, argument.getValue().getMessageDate());
		assertEquals("27 KB", argument.getValue().getSize());
		assertNotNull(argument.getValue().getFilePathName());
	}

	@Test
	public void shouldAddOneItemToHttpServerPaneForTwoJmf() throws IOException
	{
		stubServletInputStream = new StubServletInputStream("samples/mime-multipart-related/two-jmf-jdf.mjm");

		when(jdfFrame.getBottomTabs()).thenReturn(editorTabbedPaneB);
		when(editorTabbedPaneB.getHttpPanel()).thenReturn(httpServerPane);

		when(httpServletRequest.getHeader("Content-type")).thenReturn(CONTENT_MULTIPART_RELATED);
		when(httpServletRequest.getInputStream()).thenReturn(stubServletInputStream);

		jmfServlet.doPost(httpServletRequest, httpServletResponse);

		verify(httpServerPane, times(1)).addMessage(any(MessageBean.class));
	}

	@Test
	public void shouldAddItemsForPlainPost() throws IOException
	{
		stubServletInputStream = new StubServletInputStream("samples/plain-jmf/QueryKnownMessages.jmf");

		when(jdfFrame.getBottomTabs()).thenReturn(editorTabbedPaneB);
		when(editorTabbedPaneB.getHttpPanel()).thenReturn(httpServerPane);

		when(httpServletRequest.getHeader("Content-type")).thenReturn(CONTENT_PLAIN_JMF);
		when(httpServletRequest.getInputStream()).thenReturn(stubServletInputStream);

		jmfServlet.doPost(httpServletRequest, httpServletResponse);

		verify(httpServerPane, times(1)).addMessage(any(MessageBean.class));
	}

}
