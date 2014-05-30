package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.JDFTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract tree renderer class which renders jdf nodes and attributes to tree elements.
 */
public abstract class AbstractTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final String ICON_ROOT_PATH = "/org/cip4/tools/jdfeditor/icons/treeview/";

    private final Logger LOGGER = getLogger();

    /**
     * All icons available for the tree view.
     */
    protected enum TreeIcon {

        NODE_JDF("node_jdf.png"),
        NODE_XJDF("node_xjdf.png"),
        NODE_REF("node_link.png"),
        NODE_REF_IN("node_link.png"),
        NODE_REF_OUT("node_link.png"),
        NODE_WARN("WarnElemIcon.gif"),
        NODE_ERR("ErrorElemIcon.gif"),
        NODE_DEFAULT("node.png"),
        ATTR_PARTKEYS("leaf_keys.png"),
        ATTR_INHERITED("leaf_inherit.png"),
        ATTR_REF("leaf_ref.png"),
        ATTR_WARN("WarnAttIcon.gif"),
        ATTR_ERR("leaf_error.png"),
        ATTR_DEFAULT("leaf.png");

        private final String fileName;

        TreeIcon(String fileName) {
            this.fileName = fileName;
        }

        private String getFileName() {
            return this.fileName;
        }
    }


    /**
     * Default constructor
     */
    public AbstractTreeCellRenderer() {
    }

    /**
     * Create a logger object for logging.
     * @return A new Logger object.
     */
    protected abstract Logger getLogger();

    @Override
    public final Component getTreeCellRendererComponent(JTree jTree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        JDFTreeNode node = (JDFTreeNode) value;
        JDFTreeModel model = (JDFTreeModel) jTree.getModel();

        // basic appearance
        setBasicAppearance(node, isSelected, hasFocus);

        // set text and tooltip
        setNodeText(node);

        // set text color
        setTextColor(node, model);

        // set icon
        this.setNodeIcon(node, model);

        // return component
        return this;
    }

    /**
     * Configures the default appearance of the JTree Element.
     *
     * @param isSelected Indicator whether or not the element is selected.
     */
    protected void setBasicAppearance(JDFTreeNode node, boolean isSelected, boolean hasFocus) {

        // define colors
        final Color colorSelection = new Color(110, 200, 240);
        final Color colorDefault = new Color(255, 255, 255);

        // basic appearance
        this.setOpaque(true);
        this.setBackground(isSelected ? colorSelection : colorDefault);
        this.setFont(new Font(null, Font.PLAIN, 10));
    }

    /**
     * Configures the elements text and tooltip.
     *
     * @param node The JDFTreeNode object for the element.
     */
    protected void setNodeText(JDFTreeNode node) {
        String text = node.toDisplayString();
        String toolTip = text;

        if (node.isElement()) {
            KElement e = node.getElement();
            toolTip = e.getText();

            String descName = e.getAttribute(AttributeName.DESCRIPTIVENAME, null, null);
            if (descName != null && toolTip != null) {
                toolTip += "\n " + descName;
            }
        }

        this.setText(text);
        this.setToolTipText(toolTip);
    }

    /**
     * Defines the text color for a tree element.
     *
     * @param node  The JDFTreeNode object for the element.
     * @param model The model for the element.
     */
    protected void setTextColor(JDFTreeNode node, JDFTreeModel model) {

        // define colors
        final Color colorFont = new Color(0, 0, 0);
        final Color colorFontNamespace = new Color(0, 120, 255);
        final Color colorFontError = new Color(255, 0, 0);
        final Color colorFontNew = new Color(255, 0, 255);

        // set node text colors
        if (!model.isValid(node)) {
            this.setForeground(colorFontError);

        } else if ("new value".equals(node.getValue())) {
            this.setForeground(colorFontNew);

        } else if (node.hasForeignNS()) {
            this.setForeground(colorFontNamespace);

        } else {
            this.setForeground(colorFont);
        }
    }

    /**
     * Defines the icon for a element in tree.
     *
     * @param node  The JDFTreeNode object for the element.
     * @param model The model for the element.
     */
    protected void setNodeIcon(JDFTreeNode node, JDFTreeModel model) {

        KElement elem = node.getElement();

        String errType = model.getErrorType(node);

        if (errType == null) {

            if (node.isElement()) {  // element icons

                if (elem instanceof JDFNode) {
                    setIcon(loadImageIcon(TreeIcon.NODE_JDF));

                } else if (elem.getNodeName().equals("XJDF")) {
                    setIcon(loadImageIcon(TreeIcon.NODE_XJDF));

                } else if (elem.hasAttribute("Usage", null, false)) {

                    if (elem.getAttribute("Usage", null, "").equals("Input")) {
                        setIcon(loadImageIcon(TreeIcon.NODE_REF_IN));

                    } else if (elem.getAttribute("Usage", null, "").equals("Output")) {
                        setIcon(loadImageIcon(TreeIcon.NODE_REF_OUT));

                    } else {
                        setIcon(loadImageIcon(TreeIcon.NODE_REF));
                    }
                } else {
                    setIcon(loadImageIcon(TreeIcon.NODE_DEFAULT));
                }

            } else {  // attribute icons

                final String attName = node.getName();

                if (node.isInherited()) {

                    if (attName.equals("PartIDKeys")) {
                        setIcon(loadImageIcon(TreeIcon.ATTR_PARTKEYS));

                    } else {
                        setIcon(loadImageIcon(TreeIcon.ATTR_INHERITED));
                    }

                } else {

                    switch (attName) {
                        case "PartIDKeys":
                            setIcon(loadImageIcon(TreeIcon.ATTR_PARTKEYS));
                            break;

                        case "rRef":
                            setIcon(loadImageIcon(TreeIcon.ATTR_REF));
                            break;

                        default:
                            setIcon(loadImageIcon(TreeIcon.ATTR_DEFAULT));
                            break;

                    }
                }
            }

        } else if ("UnlinkedResource".equalsIgnoreCase(errType)) {  // warnings

            if (node.isElement()) {
                setIcon(loadImageIcon(TreeIcon.NODE_WARN));

            } else {
                setIcon(loadImageIcon(TreeIcon.ATTR_WARN));
            }

        } else {  // not valid (error)

            if (node.isElement()) {
                setIcon(loadImageIcon(TreeIcon.NODE_ERR));

            } else {
                setIcon(loadImageIcon(TreeIcon.ATTR_ERR));
            }
        }
    }

    /**
     * Load and returns an image icon object.
     *
     * @param treeIcon Name of the icon to be loaded.
     * @return The icon as ImageIcon object.
     */
    protected ImageIcon loadImageIcon(TreeIcon treeIcon) {

        String resPath = ICON_ROOT_PATH + treeIcon.getFileName();
        byte[] bytes = null;

        try {
            InputStream is = JDFTreeCellRenderer.class.getResourceAsStream(resPath);
            bytes = IOUtils.toByteArray(is);
            is.close();
        } catch (IOException e) {

            // log error
            LOGGER.error(e);
        }

        // return new icon
        return new ImageIcon(bytes);
    }
}
