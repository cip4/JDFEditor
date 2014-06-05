package org.cip4.tools.jdfeditor.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.tools.jdfeditor.EditorDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Vector;

/**
 * This services handles all document actions.
 */
@Service
public class DocumentService {

    private static final Logger LOGGER = LogManager.getLogger(DocumentService.class);

    @Autowired
    private SettingService settingService;

    /**
     * Default constructor.
     */
    public DocumentService() {
    }


    /**
     * Checks whether the vector contains a document corresponding to this document.
     * @param doc The JDF Document looking for.
     * @param vjdfDocument The JDFDocument Vector to search in.
     * @return Returns the position of the Document in the vector. In case the document isn't part of the vector the return value is -1.
     */
    public int indexOfJDF(final JDFDoc doc, final Vector<EditorDocument> vjdfDocument)
    {
        if (vjdfDocument == null)
        {
            return -1;
        }
        for (int i = 0; i < vjdfDocument.size(); i++)
        {
            final EditorDocument ed = vjdfDocument.elementAt(i);
            if (ed.getJDFDoc().equals(doc))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the EditorDocument in vjdfDocument, -1 if not found
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
