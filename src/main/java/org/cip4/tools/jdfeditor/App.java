package org.cip4.tools.jdfeditor;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;

import java.io.File;

/**
 * Initialize and start JDFEditor Application.
 */
public class App {

    /**
     * Application main entrance point.
     * @param args
     */
    public static void main(final String[] args)
    {

        // apple menu compatibility
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // init logging
        String logFile = FilenameUtils.concat(DirectoryUtil.getDirCIP4Tools(), "logs");
        logFile = FilenameUtils.concat(logFile, "JDFEditor.log");

        System.setProperty("filename", logFile);
        LogManager.getLogger(App.class).info("--- Start CIP4 JDFEditor ------------------------------ ");

        // handle command line arguments
        File file = null;
        // mac may have 2nd argument
        for (int i = args.length - 1; i >= 0; i--)
        {
            if (!args[i].startsWith("-"))
            {
                file = new File(args[i]);
                if (file.canRead())
                {
                    break;
                }
                file = null;
            }
        }

        // start editor
        Editor editor = new Editor();
        editor.init(file);
    }
}


