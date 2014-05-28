package org.cip4.tools.jdfeditor.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Util class for defining generic locations on a file system.
 */
public class DirectoryUtil {

    /**
     * Returns the location of the JDFEditor folder in the CIP4Tool folder.
     * @return Location of the JDFEditor folder in the CIP4Tool folder.
     */
    public static String getDirCIP4Tools() {

        String pathDir = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), "CIP4Tools");
        pathDir = FilenameUtils.concat(pathDir, "JDFEditor");

        return pathDir;
    }
}
