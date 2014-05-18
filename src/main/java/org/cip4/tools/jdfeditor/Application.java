package org.cip4.tools.jdfeditor;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.cip4.jdflib.util.logging.LogConfigurator;
import org.cip4.tools.jdfeditor.refactor.Editor;

import java.io.File;

/**
 * CIP4 JDFEditor.
 */
public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class);

    private final static String RES_BEANS_XML = "/org/cip4/tools/jdfeditor/beans.xml";

    /**
     * Main Entrance Point of application.
     * @param args
     */
    public static void main(String[] args) {

        // init logging
        String pathDir = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), "CIP4Tools");
        pathDir = FilenameUtils.concat(pathDir, "JDFEditor");
        pathDir = FilenameUtils.concat(pathDir, "logs");
        new File(pathDir).mkdirs();
        LogConfigurator.configureLog(pathDir, "JDFEditor.log");

        LOGGER.info("Start CIP4 JDFEditor...");

        // init spring framework
        //ApplicationContext ctx = new ClassPathXmlApplicationContext(RES_BEANS_XML);

        // apple properties
        System.setProperty("apple.laf.useScreenMenuBar", "true"); // use menu

        // start editor
        Editor editor = new Editor();
        editor.init(null);
    }

}
