package org.cip4.tools.jdfeditor.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.tools.jdfeditor.service.SettingService;
import org.cip4.tools.jdfeditor.view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The Main Controller class.
 */
public class MainController implements ActionListener {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class.getName());

    private final SettingService settingService = new SettingService();

    private final MainView mainView;

    private File file;

    /**
     * Default constructor.
     */
    public MainController() {
        this(null);
    }

    /**
     * Custom constructor. Accepting a file object for initializing.
     * @param file The JDF File which should be shown after start.
     */
    public MainController(File file) {

        this.mainView = new MainView();
        this.mainView.registerController(this);
        this.file = file;

    }

    /**
     * Display the main form.
     */
    public void displayForm() {

        mainView.display(file);
    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
