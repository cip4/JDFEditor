package org.cip4.tools.jdfeditor.streamloader;

import java.io.File;
import java.io.IOException;

import org.cip4.jdflib.core.JDFDoc;

/**
 * 
 *  
 * @author rainer schielke
 * @date Sep 19, 2013
 */
public interface IStreamLoader
{
	/**
	 * Reads a JDFDoc from given file.
	 * 
	 * @param fileJDF
	 * @param fileSchema
	 * 
	 * @return JDFDoc - The JDFDoc or <code>null</code> if file format is not supported
	 *                  by this plugin.
	 * 
	 * @throws IOException
	 */

	public JDFDoc read(File fileJDF, File fileSchema) throws IOException;
}
