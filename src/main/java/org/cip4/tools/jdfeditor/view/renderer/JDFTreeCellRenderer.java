package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.log4j.Logger;

/**
 * Render JDF Elements in tree view.
 */
public class JDFTreeCellRenderer extends AbstractTreeCellRenderer {

    private static final Logger LOGGER = Logger.getLogger(JDFTreeCellRenderer.class);

    private static final long serialVersionUID = 1526856515806803255L;

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }


}