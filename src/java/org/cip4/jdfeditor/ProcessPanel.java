package org.cip4.jdfeditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JPanel;

import org.cip4.jdflib.node.JDFNode;

/*
 * FooProcessPanel.java
 * @author SvenoniusI
 */

public class ProcessPanel extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5217687675396163701L;

    public int width;
    public int height;
    
    private Vector vPoints = new Vector();
    private Vector vDirPoints = new Vector();
    private Vector vParts = new Vector();
    private Font procFont = new Font("Verdana", Font.PLAIN, 10);
    private Font resourceFont = new Font("Verdana", Font.PLAIN, 9);
    private Font parentFont = new Font("Verdana", Font.BOLD, 12);
    private double zoom;
    private ProcessPart parent = null;

    public ProcessPanel()
    {
        super();
        this.zoom = 1.0;
    }

    public void paintComponent(Graphics g)
    {
        this.width = 0;
        this.height = 0;
        
        super.paintComponent(g);
        ((Graphics2D) g).scale(zoom, zoom);
        
        if (parent != null)
        {            
            g.setFont(getFont(parent));
            g.setColor(parent.getgColor());
            g.fillRoundRect(parent.getxPos(), parent.getyPos(), parent.width, parent.height, 25, 25);
            g.setColor(Color.black);
            g.drawRoundRect(parent.getxPos(), parent.getyPos(), parent.width, parent.height, 25, 25);
            
            final String[] s = parent.getgString();
            final int xMarg = 15;
            int yMarg = 2;
            
            for (int i = 0; i < s.length; i++)
            {
                yMarg += 15;
                g.drawString(s[i], parent.getxPos() + xMarg, parent.getyPos() + yMarg);
            }

            parent.setBounds((int) (parent.getxPos() * zoom),
                (int) (parent.getyPos() * zoom),
                (int) (parent.width * zoom),
                (int) (parent.height * zoom));
            add(parent, -1);
            
            width = width < parent.width ? parent.width : width;
            height = height < parent.height ? parent.height : height;
        }

        for (int i = 0; i < vParts.size(); i++)
        {
            final ProcessPart part = (ProcessPart) vParts.get(i);
            
            g.setFont(getFont(part));
            g.setColor(part.getgColor());
            
            if (part.getElem() instanceof JDFNode)
            {
                g.fillRoundRect(part.getxPos(), part.getyPos(), part.width, part.height, 10, 10);
                g.setColor(Color.black);
                g.drawRoundRect(part.getxPos(), part.getyPos(), part.width, part.height, 10, 10);
            }
            else
            {
                g.fillRect(part.getxPos(), part.getyPos(), part.width, part.height);
                g.setColor(Color.black);
                g.drawRect(part.getxPos(), part.getyPos(), part.width, part.height);
            }
            final String[] s = part.getgString();
            final int xMarg = 15;
            int yMarg = 2;
            
            for (int j = 0; j < s.length; j++)
            {
                yMarg += 15;
                g.drawString(s[j], part.getxPos() + xMarg, part.getyPos() + yMarg);
            }
            
            part.setBounds((int) (part.getxPos() * zoom),
                (int) (part.getyPos() * zoom),
                (int) (part.width * zoom),
                (int) (part.height * zoom));
            add(part, 0);
            
            width = width < part.getxPos() + part.width + 20 ? part.getxPos() + part.width + 20 : width;
            height = height < part.getyPos() + part.height + 20 ? part.getyPos() + part.height + 20 : height;
        }
        
        for (int i = 0; i < vPoints.size(); i += 2)
        {
            final int x1 = (int) ((Point) vPoints.get(i)).getX();
            final int y1 = (int) ((Point) vPoints.get(i)).getY();
            final int x2 = (int) ((Point) vPoints.get(i + 1)).getX();
            final int y2 = (int) ((Point) vPoints.get(i + 1)).getY();
            final int xm = x1 < x2 ? x2 - 20 : x2 + 20;

            g.drawLine(x1, y1, xm, y1);
            g.drawLine(xm, y1, xm, y2);
            g.drawLine(xm, y2, x2, y2);
        }
        for (int i = 0; i < vDirPoints.size(); i += 2)
        {
            g.setColor(Color.blue);
            
            final int x1 = (int) ((Point) vDirPoints.get(i)).getX();
            final int y1 = (int) ((Point) vDirPoints.get(i)).getY() - 5;
            final int x2 = (int) ((Point) vDirPoints.get(i + 1)).getX();
            final int y2 = (int) ((Point) vDirPoints.get(i + 1)).getY() - 5;
            final int xm = x1 < x2 ? x2 - 20 : x2 + 20;
            
            g.drawLine(x1, y1, xm, y1);
            g.drawLine(xm, y1, xm, y2);
            g.drawLine(xm, y2, x2, y2);
            
            final int xa = x1 < x2 ? x2 - 6 : x2 + 6;
            final int angle = x1 < x2 ? 270 : 90;
            
            final int arrowX[] = {xa, x2, xa};
            final int arrowY[] = {y2 - 4, y2, y2 + 4};
                
            g.fillPolygon(arrowX, arrowY, 3);            
            g.fillArc(x1 - 4, y1 - 3, 8, 6, angle, 180);
        }
    }
    /**
     * Sorts the panels for the process view so that those who share resources are next to another
     * @param panelVector
     * @return Vector
     */
    public Vector sortPanels(Vector v)
    {
        final Vector vSorted   = new Vector();
        final Vector vUnsorted = new Vector();

        for (int i = 0; i < v.size(); i++)
        {
            final ProcessPart part = (ProcessPart) v.get(i);
            final Vector vRes = part.getvAllRes();
            
            if (vRes.isEmpty())
                vUnsorted.add(part);
            else
            {
                boolean connected = false;
                v.remove(i);
                
                check : for (int j = 0; j < v.size(); j++)
                {
                    final ProcessPart nextPart = (ProcessPart) v.get(j);
                    final Vector vnextRes = nextPart.getvAllRes();

                    for (int n = 0; n < vRes.size(); n++)
                    {
                        final ProcessPart resLink = (ProcessPart) vRes.get(n);
                        final String rRefRL = resLink.getElem().getAttribute("ID");
                        
                        if (!rRefRL.equals(""))
                        {
                            for (int m = 0; m < vnextRes.size(); m++)
                            {
                                final ProcessPart nextResLink = (ProcessPart) vnextRes.get(m);
                                final String rRefnRL = nextResLink.getElem().getAttribute("ID");

                                if (rRefRL.equals(rRefnRL))
                                {
                                    connected = true;
                                    break check;
                                }
                            }
                        }
                    }
                }
                v.add(i, part);
                
                if (connected)
                    vSorted.add(part);
                else
                    vUnsorted.add(0, part);
            }
        }
        for (int i = 0; i < vSorted.size() - 1; i ++)
        {
            ProcessPart partOne = (ProcessPart) vSorted.get(i);
            final Vector vResOne = partOne.getvInRes();
            
            for (int j = 1; j < vSorted.size(); j++)
            {
                boolean change = false;
                ProcessPart partTwo = (ProcessPart) vSorted.get(j);
                final Vector vResTwo = partTwo.getvOutRes();
                
                check: for (int n = 0; n < vResOne.size(); n++)
                {
                    final ProcessPart linkOne = (ProcessPart) vResOne.get(n);
                    final String rRefOne = linkOne.getElem().getAttribute("ID");
                    
                    if (!rRefOne.equals(""))
                    {
                        for (int m = 0; m < vResTwo.size(); m++)
                        {
                            final ProcessPart linkTwo = (ProcessPart) vResTwo.get(m);
                            final String rRefTwo = linkTwo.getElem().getAttribute("ID");

                            if (rRefOne.equals(rRefTwo))
                            {
                                if (linkTwo.getElem().getAttribute("Usage", "", "").equals("Output")
                                    && linkOne.getElem().getAttribute("Usage", "", "").equals("Input"))
                                {
                                    change = true;
                                    break check;
                                }
                            }
                        }
                    }
                }
                if (change)
                {
                    partOne = partTwo;
                    vSorted.add(i, partTwo);
                    
                    if (((ProcessPart) vSorted.get(j + 1)).getElem().getAttribute("ID").equals(partTwo.getElem().getAttribute("ID")))
                        vSorted.remove(j + 1);
                    else
                    {
                        for (int n = 0; n < vSorted.size(); n++)
                        {
                            if (partTwo.getElem().getAttribute("ID").equals(((ProcessPart) vSorted.get(n)).getElem().getAttribute("ID")))
                            {
                                if (n < i)
                                    vSorted.remove(i);
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < vUnsorted.size(); i++)
        {
            vSorted.add(0, vUnsorted.get(i));
        }
        return vSorted;
    }
    /**
     * Checks if the current node has any resources that previously have been added to the ProcessPanel
     * @param vPrevNodes
     * @param node
     * @return Vector
     */
    public Vector comparePrevious(Vector vPrevNodes, ProcessPart node)
    {
        final Vector vPrev = new Vector();
        //Vector newRes = node.getvAllRes();
        final Vector vNodeInRes = node.getvInRes();

        for (int i = 0; i < vPrevNodes.size(); i++)
        {
            boolean shareRes = false;
            boolean add      = false;
            final ProcessPart prevNode = (ProcessPart) vPrevNodes.get(i);
            final Vector vPrevInRes = prevNode.getvInRes();
            final Vector vPrevOutRes = prevNode.getvOutRes();
            
            check : for (int j = 0; j < vPrevInRes.size(); j++)
            {
                final ProcessPart prevInPart = (ProcessPart) vPrevInRes.get(j);
                final String rRefIn = prevInPart.getElem().getAttribute("ID");
                
                if (!rRefIn.equals(""))
                {
                    for (int n = 0; n < vNodeInRes.size(); n++)
                    {
                        final ProcessPart inRes = (ProcessPart) vNodeInRes.get(n);
                        
                        if (inRes.getElem().getAttribute("ID").equals(rRefIn))
                        {
                            shareRes = true;
                            prevNode.setSharedInput(true);
                            break check;
                        }
                    }
                }
            }
            check : for (int j = 0; j < vPrevOutRes.size(); j++)
            {
                if (shareRes) break check;
                final ProcessPart prevOutPart = (ProcessPart) vPrevOutRes.get(j);
                final String rRefOut = prevOutPart.getElem().getAttribute("ID");
                
                if (!rRefOut.equals(""))
                {
                    for (int n = 0; n < vNodeInRes.size(); n++)
                    {
                        final ProcessPart inRes = (ProcessPart) vNodeInRes.get(n);
                        
                        if (inRes.getElem().getAttribute("ID").equals(rRefOut))
                        {
                            shareRes = true;
                            prevNode.setSharedInput(true);
                            break check;
                        }
                    }
                }
            }
            if (shareRes)
            {
                if (vPrev.isEmpty())
                    vPrev.add(prevNode);
                else
                {
                    check : for (int j = 0; j < vPrev.size(); j++)
                    {
                        if (!vPrev.get(j).equals(prevNode))
                        {
                            add = true;
                            break check;
                        }
                    }
                    if (add)
                        vPrev.add(prevNode);
                }
            }
            
            /*ProcessPart prevNode = (ProcessPart) vPrevNodes.get(i);
            Vector vPrevRes = prevNode.getvAllRes();
            
            boolean shareRes = false, add = false;
            
            check : for (int j = 0; j < vPrevRes.size(); j++)
            {
                ProcessPart prevPart = (ProcessPart) vPrevRes.get(j);
                JDFElement prevLink = prevPart.getElem();
                String rRefPrev = prevLink.getID();
                
                if (!rRefPrev.equals(""))
                {
                    if (prevLink.getAttribute("Usage", "", "").equals("Output"))
                    {                        
                        search : for (int n = 0; n < newRes.size(); n++)
                        {
                            ProcessPart newPart = (ProcessPart) newRes.get(n);
                            
                            if (newPart.getElem().getAttribute("Usage", "", "").equals("Input"))
                            {
                                if (newPart.getElem().getID().equals(rRefPrev))
                                {
                                    shareRes = true;
                                    break search;
                                }
                            }
                        }
                    }
                    else if (prevLink.getAttribute("Usage", "", "").equals("Input"))
                    {
                        for (int n = 0; n < newRes.size(); n++)
                        {
                            ProcessPart newPart = (ProcessPart) newRes.get(n);
                            
                            if (newPart.getElem().getAttribute("Usage", "", "").equals("Input"))
                            {
                                if (newPart.getElem().getID().equals(rRefPrev))
                                {
                                    shareRes = true;
                                    prevNode.setSharedInput(true);
                                    break check;
                                }
                            }
                        }
                    }
                }
            }
            if (shareRes)
            {
                if (vPrev.isEmpty())
                    vPrev.add(prevNode);
                else
                {
                    check : for (int j = 0; j < vPrev.size(); j++)
                    {
                        if (!((ProcessPanel) vPrev.get(j)).equals(prevNode))
                        {
                            add = true;
                            break check;
                        }
                    }
                    if (add)
                        vPrev.add(prevNode);
                }
            }*/
        }
        return vPrev;
    }
    
    public void addPart(Object part)
    {
        this.vParts.add(part);
    }
    
    public Font getFont(ProcessPart fpp)
    {
        if (fpp.getName().equals("ResPart"))
            return resourceFont;
            
        else if (fpp.getName().equals("ParentPart"))
            return parentFont;
            
        else return procFont;
    }
    
    public FontMetrics getFM(ProcessPart fpp)
    {
        if (fpp.getName().equals("ResPart"))
            return getFontMetrics(resourceFont);
            
        else if (fpp.getName().equals("ParentPart"))
            return getFontMetrics(parentFont);
            
        else return getFontMetrics(procFont);
    }

    public void addPoint(Point p)
    {
        vPoints.add(p);
    }
    
    public void addDirPoint(Point p)
    {
        vDirPoints.add(p);
    }
    
    public void setParent(ProcessPart _parent)
    {
        this.parent = _parent;
    }

    public void setZoom(double _zoom)
    {
        this.zoom = _zoom;
    }
    public double getZoom()
    {
        return this.zoom;
    }
    public void zoom()
    {
        revalidate();
        repaint();
        width *= zoom;
        height *= zoom;
        setPreferredSize(new Dimension(width, height));
    }
    public void clear()
    {
        vPoints.clear();
        vDirPoints.clear();
        vParts.clear();
        parent = null;
        setZoom(1.0);
        setLayout(null);
        removeAll();
        setBackground(Color.white);
    }
}