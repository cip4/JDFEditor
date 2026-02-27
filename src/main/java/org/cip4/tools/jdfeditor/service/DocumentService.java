/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2026 The International Cooperation for the Integration of
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
package org.cip4.tools.jdfeditor.service;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.XMLDocUserData.EnumDirtyPolicy;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.EditorDocument;

/**
 * This services handles all document actions.
 */
public class DocumentService
{

	private static final Log LOGGER = LogFactory.getLog(DocumentService.class);

	/**
	 * Default constructor.
	 */
	public DocumentService()
	{
	}

	public int setJDFDoc(final JDFDoc doc, final String mimePackage)
	{
		int docPos = EditorDocument.getDocPos();
		final List<EditorDocument> editorDocs = EditorDocument.getEditorDocs();
		if (doc != null)
		{
			docPos = indexOfJDF(doc);
			final EditorDocument ed = new EditorDocument(doc, mimePackage);

			if (docPos >= 0)
			{
				EditorDocument.setDocPos(docPos);
				editorDocs.set(docPos, ed);
			}
			else
			{
				editorDocs.add(ed);
				EditorDocument.setDocPos(editorDocs.size() - 1);
				// make sure that we have a global dirty policy in force
				doc.getCreateXMLDocUserData().setDirtyPolicy(EnumDirtyPolicy.Doc);
			}
		} // doc==null --> remove this entry
		else if (docPos >= 0 && docPos < editorDocs.size())
		{
			editorDocs.remove(docPos);
			docPos--;
			// roll over to the end; also ok if size=0, since -1 is the flag for all closed
			if (docPos == -1)
			{
				docPos = editorDocs.size() - 1;
			}
		}
		return docPos;
	}

	/**
	 * set the currently displayed doc to doc
	 *
	 * @param doc
	 * @param mimePackage
	 * @return the index in the list of docs that doc is stored in
	 */
	public int setEditorDoc(final EditorDocument doc)
	{
		int i = EditorDocument.getDocPos();
		if (doc != null)
		{
			final List<EditorDocument> editorDocs = EditorDocument.getEditorDocs();
			i = editorDocs.indexOf(doc);

			if (i >= 0)
			{
				EditorDocument.setDocPos(i);
			}
			else
			{
				editorDocs.add(doc);
				i = editorDocs.size() - 1;
				EditorDocument.setDocPos(i);
				// make sure that we have a global dirty policy in force
				doc.getJDFDoc().getCreateXMLDocUserData().setDirtyPolicy(EnumDirtyPolicy.Doc);
			}
		}
		return i;
	}

	/**
	 * Checks whether the vector contains a document corresponding to this document.
	 *
	 * @param doc          The JDF Document looking for.
	 * @param vjdfDocument The JDFDocument Vector to search in.
	 * @return Returns the position of the Document in the vector. In case the document isn't part of the vector the return value is -1.
	 */
	public int indexOfJDF(final JDFDoc doc)
	{
		final List<EditorDocument> vjdfDocument = EditorDocument.getEditorDocs();
		for (int i = 0; i < vjdfDocument.size(); i++)
		{
			final EditorDocument ed = vjdfDocument.get(i);
			if (ed.getJDFDoc().equals(doc) || StringUtil.equals(doc.getOriginalFileName(), ed.getOriginalFileName()))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the EditorDocument in vjdfDocument, -1 if not found
	 *
	 * @param file         the file that corresponds to the document
	 * @param vjdfDocument the vector to search in
	 * @return int the index of the document in vjdfDocument
	 */
	public int indexOfFile(final File file, final List<EditorDocument> vjdfDocument)
	{

		if (file == null)
		{
			return -1;
		}
		final String filePath = file.getAbsolutePath();
		for (int i = 0; i < vjdfDocument.size(); i++)
		{
			final EditorDocument d = vjdfDocument.get(i);
			if (d != null)
			{
				if (filePath.equals(d.getOriginalFileName()))
				{
					return i;
				}
			}
		}
		return -1;
	}
}
