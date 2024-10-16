package org.cip4.tools.jdfeditor.service;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.EditorDocument;

/**
 * This services handles all document actions.
 */
public class DocumentService
{

	private static final Log LOGGER = LogFactory.getLog(DocumentService.class);

	private SettingService settingService;

	/**
	 * Default constructor.
	 */
	public DocumentService()
	{
	}

	/**
	 * Checks whether the vector contains a document corresponding to this document.
	 * 
	 * @param doc The JDF Document looking for.
	 * @param vjdfDocument The JDFDocument Vector to search in.
	 * @return Returns the position of the Document in the vector. In case the document isn't part of the vector the return value is -1.
	 */
	public int indexOfJDF(final JDFDoc doc, final List<EditorDocument> vjdfDocument)
	{
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
	 * @param file the file that corresponds to the document
	 * @param vjdfDocument the vector to search in
	 * @return int the index of the document in vjdfDocument
	 */
	public int indexOfFile(final File file, final Vector<EditorDocument> vjdfDocument)
	{

		if (file == null)
		{
			return -1;
		}
		final String filePath = file.getAbsolutePath();
		for (int i = 0; i < vjdfDocument.size(); i++)
		{
			final EditorDocument d = vjdfDocument.elementAt(i);
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
