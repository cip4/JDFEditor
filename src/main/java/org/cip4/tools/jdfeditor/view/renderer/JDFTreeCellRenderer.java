package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
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
 * @author s.meissner (refactoring)
 *         Render JDF Elements in tree view.
 */
public class JDFTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1526856515806803255L;

    private static final Logger LOGGER = Logger.getLogger(JDFTreeCellRenderer.class);

    private static final Color COLOR_BG_SELECTION = new Color(110, 200, 240);

    private static final Color COLOR_BG_DEFAULT = new Color(255, 255, 255);

    private static final Color COLOR_FONT = new Color(0, 0, 0);

    private static final Color COLOR_FONT_ERROR = new Color(255, 0, 0);

    private static final String ICON_ROOT_PATH = "/org/cip4/tools/jdfeditor/icons/treeview/";

    protected static final String ICON_NODE_JDF = "node_jdf.png";

    protected static final String ICON_NODE_XJDF = "node_xjdf.png";

    protected static final String ICON_NODE_REF = "node_link.png";

    protected static final String ICON_NODE_REF_IN = "node_link.png";

    protected static final String ICON_NODE_REF_OUT = "node_link.png";

    protected static final String ICON_NODE_WARN = "WarnElemIcon.gif";

    protected static final String ICON_NODE_ERR = "ErrorElemIcon.gif";

    protected static final String ICON_NODE_DEFAULT = "node.png";

    protected static final String ICON_ATTR_PARTKEYS = "leaf_keys.png";

    protected static final String ICON_ATTR_INHERITED = "leaf_inherit.png";

    protected static final String ICON_ATTR_REF = "leaf_ref.png";

    protected static final String ICON_ATTR_WARN = "WarnAttIcon.gif";

    protected static final String ICON_ATTR_ERR = "leaf_error.png";

    protected static final String ICON_ATTR_DEFAULT = "leaf.png";

    /**
     * Default constructor
     */
    public JDFTreeCellRenderer() {
        this.setOpaque(true);
    }


    @Override
    public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean _hasFocus) {

        // basic apperance
        this.setBackground(sel ? COLOR_BG_SELECTION : COLOR_BG_DEFAULT);
        this.setForeground(COLOR_FONT);
        this.setFont(new Font(null, Font.PLAIN, 10));

        // set text and tooltip
        JDFTreeNode node = (JDFTreeNode) value;
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

        // if necessary, change font color
        JDFTreeModel mod = (JDFTreeModel) jTree.getModel();

        if (mod.isValid(node)) {
            this.setForeground(COLOR_FONT);

            if (!node.isElement()) {
                final String attVal = node.getValue();
                if (attVal.equals("new value")) {
                    setForeground(Color.magenta);
                }
            }
        } else {
            this.setForeground(COLOR_FONT_ERROR);

            if (node.isElement()) {
                toolTip = "Invalid Element";
            } else {
                toolTip = "Invalid Attribute";
            }
        }

        // set
        this.setText(text);
        this.setToolTipText(toolTip);
        this.setNodeIcon(jTree, node);

        // return component
        return this;
    }

    /**
     * Define the icon for a tree view element.
     *
     * @param jTree    The according JTree object for an element.
     * @param treeNode The tree node element.
     */
    protected void setNodeIcon(JTree jTree, JDFTreeNode treeNode) {

        KElement elem = treeNode.getElement();

        JDFTreeModel mod = (JDFTreeModel) jTree.getModel();
        String errType = mod.getErrorType(treeNode);

        if (errType == null) {

            if (treeNode.isElement()) {  // element icons

                if (elem instanceof JDFNode) {
                    setIcon(loadImageIcon(ICON_NODE_JDF));

                } else if (elem.getNodeName().equals("XJDF")) {
                    setIcon(loadImageIcon(ICON_NODE_XJDF));

                } else if (elem.hasAttribute("Usage", null, false)) {

                    if (elem.getAttribute("Usage", null, "").equals("Input")) {
                        setIcon(loadImageIcon(ICON_NODE_REF_IN));

                    } else if (elem.getAttribute("Usage", null, "").equals("Output")) {
                        setIcon(loadImageIcon(ICON_NODE_REF_OUT));

                    } else {
                        setIcon(loadImageIcon(ICON_NODE_REF));
                    }
                } else {
                    setIcon(loadImageIcon(ICON_NODE_DEFAULT));
                }

            } else {  // attribute icons

                final String attName = treeNode.getName();

                if (treeNode.isInherited()) {

                    if (attName.equals("PartIDKeys")) {
                        setIcon(loadImageIcon(ICON_ATTR_PARTKEYS));

                    } else {
                        setIcon(loadImageIcon(ICON_ATTR_INHERITED));
                    }

                } else {

                    switch (attName) {
                        case "PartIDKeys":
                            setIcon(loadImageIcon(ICON_ATTR_PARTKEYS));
                            break;

                        case "rRef":
                            setIcon(loadImageIcon(ICON_ATTR_REF));
                            break;

                        default:
                            setIcon(loadImageIcon(ICON_ATTR_DEFAULT));
                            break;

                    }
                }
            }

        } else if ("UnlinkedResource".equalsIgnoreCase(errType)) {  // warnings

            if (treeNode.isElement()) {
                setIcon(loadImageIcon(ICON_NODE_WARN));

            } else {
                setIcon(loadImageIcon(ICON_ATTR_WARN));
            }

        } else {  // not valid (error)

            if (treeNode.isElement()) {
                setIcon(loadImageIcon(ICON_NODE_ERR));

            } else {
                setIcon(loadImageIcon(ICON_ATTR_ERR));
            }
        }
    }

    /**
     * Load and returns an image icon object.
     *
     * @param iconName Name of the icon to be loaded.
     * @return The icon as ImageIcon object.
     */
    protected ImageIcon loadImageIcon(String iconName) {

        String resPath = ICON_ROOT_PATH + iconName;
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