package org.cip4.tools.jdfeditor.view;

import org.cip4.tools.jdfeditor.controller.MainController;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * Created by stefanmeissner on 18.05.14.
 */
@Component
public class MainForm {

    private JTabbedPane tabbedPane1;

    private JTree treeView;

    private JToolBar toolBar;

    private JPanel panel;

    private JFrame frame;

    private MainController mainController;

    /**
     * Default constructor.
     */
    public MainForm() {
    }

    /**
     * Register a MainController for this view (MVC Pattern)
     * @param mainController
     */
    public void registerController(final MainController mainController) {

        this.mainController = mainController;

    }

    /**
     * Display this form.
     */
    public void display() {
        if (frame == null) {
            frame = new JFrame("MainView");
            frame.setContentPane(this.panel);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
    }
}
