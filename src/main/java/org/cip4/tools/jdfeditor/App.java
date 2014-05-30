package org.cip4.tools.jdfeditor;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;

import java.io.File;

/**
 * Initialize and start JDFEditor Application.
 */
public class App {

    public static final String APP_NAME = "CIP4 JDF Editor -- Copyright (c) 2001-2014 CIP4";

    public static final String APP_VERSION = "Estimated Build Date After May 14 2014";

    public static final String APP_RELEASE_DATE = "";

    public static final String JDFLIBJ_VERSION = JDFAudit.software();

    public static final String JDFLIBJ_RELEASE_DATE = "";

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


