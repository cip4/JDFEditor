package org.cip4.tools.jdfeditor.controller;

import org.cip4.tools.jdfeditor.view.MainForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by stefanmeissner on 18.05.14.
 */
@Controller
public class MainController {

    private final MainForm mainForm;

    /**
     * Custom constructor. Accepting several params for initializing.
     * @param mainForm The MainForm instance.
     */
    @Autowired
    public MainController(MainForm mainForm) {

        // apple properties
        System.setProperty("apple.laf.useScreenMenuBar", "true"); // use menu

        // init view
        this.mainForm = mainForm; //new MainView(this);
        this.mainForm.registerController(this);

    }

    /**
     * Display the Main Form.
     */
    public void displayForm() {
        mainForm.display();
    }
}
