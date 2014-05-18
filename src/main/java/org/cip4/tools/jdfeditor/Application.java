package org.cip4.tools.jdfeditor;


import org.apache.log4j.Logger;
import org.cip4.tools.jdfeditor.refactor.Editor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

        LOGGER.info("Start Imposition Client...");

        ApplicationContext ctx = new ClassPathXmlApplicationContext(RES_BEANS_XML);

        // apple properties
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // start editor
        Editor editor = new Editor();
        editor.init(null);
    }

}
