package org.cip4.jdfeditor;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.RepaintManager;

/*
 * PrintUtilities.java
 * @author SvenoniusI
 */

public class PrintDialog implements Printable
{
    private Component comp;

    public static void printIt(Component c)
    {
        new PrintDialog(c).print();
    }

    public PrintDialog(Component _comp)
    {
        this.comp = _comp;
    }

    private void print()
    {        
        final PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(this);
        final PrintService[] ps = PrinterJob.lookupPrintServices();

        final PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        if (ps.length > 0)
        {
            try
            {
                pj.setPrintService(ps[0]);
                if(pj.printDialog(pras))
                    pj.print(pras);
            }
            catch (PrinterException pe)
            {
                System.err.println(pe);
            }
        }
    }
    
    public int print(Graphics g, PageFormat pf, int pI)
    {
        int result = NO_SUCH_PAGE;
        
        if (pI <= 0)
        {
            final Graphics2D gfx = (Graphics2D) g;
            final double f = getScalingFactor(pf);
            gfx.translate(pf.getImageableX(), pf.getImageableY());
            gfx.scale(f, f);
            disableDoubleBuffering(comp);
            comp.paint(gfx);
            enableDoubleBuffering(comp);

            result = PAGE_EXISTS;
        }
        
        return result;
    }
    /**
     * Get the scaling factor used in the paint method to get the correct
     * size of the component that is going to be printed.
     * @param p - The page format
     * @return The scaling factor as a double.
     */
    private double getScalingFactor(PageFormat p)
    {
        final double hFac = p.getImageableHeight() / comp.getSize().getHeight();
        final double wFac = p.getImageableWidth() / comp.getSize().getWidth();
                
        return hFac < wFac ? hFac : wFac;
    }
    /**
     *  Double buffering is turned off to enhance quality and speed.
     */
    private static void disableDoubleBuffering(Component c)
    {
        final RepaintManager manager = RepaintManager.currentManager(c);
        manager.setDoubleBufferingEnabled(false);
    }

    /**
     * Turns double buffering on.
     * */
    private static void enableDoubleBuffering(Component c)
    {
        final RepaintManager manager = RepaintManager.currentManager(c);
        manager.setDoubleBufferingEnabled(true);
    }
}
