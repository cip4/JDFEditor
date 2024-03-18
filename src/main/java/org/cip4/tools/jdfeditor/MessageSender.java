/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2015 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor;

import java.util.Vector;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.extensions.XJMFHelper;
import org.cip4.jdflib.jmf.JDFJMF;
import org.cip4.jdflib.jmf.JDFMessage;
import org.cip4.jdflib.jmf.JDFMessage.EnumFamily;
import org.cip4.jdflib.jmf.JDFMessage.EnumType;
import org.cip4.jdflib.jmf.JDFMessageService;
import org.cip4.jdflib.jmf.JMFBuilder;
import org.cip4.tools.jdfeditor.view.MainView;

class MessageSender
{

	JDFMessageService mService;

	/**
	 * @param ms
	 */
	public MessageSender(final JDFMessageService ms)
	{
		mService = ms;
	}

	/**
	 *
	 */
	public void sendJMF()
	{
		final boolean b = generateDoc();
		if (b)
		{
			final SendToDevice sendTo = new SendToDevice();
			sendTo.trySend();
		}

	}

	/**
	 * @return
	 */
	boolean generateDoc()
	{
		final EditorDocument ed = MainView.getEditorDoc();
		final JDFDoc doc;
		final boolean json = ed.isJson();
		if (ed.isXJDF())
		{
			doc = generateXJMFDoc();
		}
		else
		{
			doc = generateJMFDoc();

		}
		if (doc != null)
		{
			MainView.getFrame().setJDFDoc(doc, null);
			final EditorDocument edNew = MainView.getEditorDoc();
			edNew.setJson(json, json);
			MainView.getFrame().getJDFTreeArea().drawTreeView(edNew);
		}
		return doc != null;
	}

	JDFDoc generateJMFDoc()
	{
		final Vector<EnumFamily> vf = mService.getFamilies();
		if (vf == null || vf.size() == 0)
		{
			return null;
		}
		EnumFamily f = vf.get(0);
		if (vf.contains(EnumFamily.Command))
		{
			f = EnumFamily.Command;
		}

		final JMFBuilder b = new JMFBuilder();
		b.setSenderID("JDFEditor");

		final JDFJMF jmf = b.newJMF(f, mService.getType());
		final JDFDoc doc = jmf.getOwnerDocument_JDFElement();
		doc.setOriginalFileName(EditorUtils.getNewPath("Auto" + mService.getType() + ".jmf"));
		final JDFMessage m = jmf.getMessageElement(f, EnumType.getEnum(mService.getType()), 0);
		extendMessage(m);

		return doc;
	}

	JDFDoc generateXJMFDoc()
	{
		final String type = mService.getType();

		final XJMFHelper h = new XJMFHelper();
		h.appendMessage(type);
		final KElement header = h.getHeader();
		header.setAttribute(AttributeName.AGENTNAME, JDFAudit.getStaticAgentName());
		header.setAttribute(AttributeName.DESCRIPTIVENAME, "XJMF From MessageService for " + type);

		final JDFDoc xjmfDoc = new JDFDoc(h.getRoot().getOwnerDocument_KElement());
		xjmfDoc.setOriginalFileName(EditorUtils.getNewPath("Auto" + type + ".xjmf"));

		return xjmfDoc;
	}

	/**
	 * @param m
	 */
	private void extendMessage(final JDFMessage m)
	{
		final EnumType t = m == null ? null : EnumType.getEnum(m.getType());
		if (t == null || m == null)
		{
			return;
		}
		if (EnumType.AbortQueueEntry.equals(t))
		{
			m.appendQueueEntryDef();
		}
		else if (EnumType.HoldQueueEntry.equals(t))
		{
			m.appendQueueEntryDef();
		}
		else if (EnumType.RemoveQueueEntry.equals(t))
		{
			m.appendQueueEntryDef();
		}

	}

}