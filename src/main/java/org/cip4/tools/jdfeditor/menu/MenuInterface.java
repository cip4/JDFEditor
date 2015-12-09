package org.cip4.tools.jdfeditor.menu;


import javax.swing.JMenu;

public interface MenuInterface
{
	JMenu createMenu();
	void setEnableClose();
	void setEnableOpen(final boolean mode);
}
