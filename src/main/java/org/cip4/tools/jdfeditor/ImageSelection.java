package org.cip4.tools.jdfeditor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;

/*
 * Created on 07.09.2004
 *
* @author MRE 
*
* @description Image Selection methods for copy images
* to the systems clipboard 
*/

public class ImageSelection implements Transferable,ClipboardOwner {
    private BufferedImage image;
//    private int iImageHeight, iImageWidth;

    public ImageSelection(BufferedImage _image) {
        if (_image == null)
            throw new NullPointerException();
        this.image = _image;
    }
    
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DataFlavor.imageFlavor};
    }
    
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }
    
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);
        return image;
    }
    
    public void lostOwnership(Clipboard c, Transferable t) 
    { 
        c.getClass(); // nop to make compile happy
        t.getClass();
    }
}
