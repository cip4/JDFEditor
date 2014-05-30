package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.tools.jdfeditor.JDFTreeNode;

import java.awt.*;

/**
 * Tree renderer for JDFResource elements.
 */
public class JDFResourceTreeCellRenderer extends AbstractTreeCellRenderer {

    private static final Logger LOGGER = LogManager.getLogger(JDFResourceTreeCellRenderer.class);

    /**
     * Default constructor.
     */
    public JDFResourceTreeCellRenderer() {
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void setBasicAppearance(JDFTreeNode node, boolean isSelected, boolean hasFocus) {

        // default behavior
        super.setBasicAppearance(node, isSelected, hasFocus);

        // additional behavior
        Color colorFocused = new Color(110, 200, 240);

        if (hasFocus) {
            this.setBackground(colorFocused);
        }
    }
}