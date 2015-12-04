package org.cip4.tools.jdfeditor;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.RecentFileUtil;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class EditorMenuBarFile implements ActionListener
{
	private MainController mainController;
	
	private JMenu m_fileMenu;
	private JMenu m_recentFilesMenu;
	
	private JMenuItem m_newItem;
	private JMenuItem m_openItem;
	private JMenuItem m_closeItem;
	private JMenuItem m_closeAllItem;
	private JMenuItem m_saveItem;
	private JMenuItem m_saveAsItem;
	private JMenuItem[] m_subMenuItem = new JMenuItem[5];
	private JMenuItem m_devcapOpenMenu;
	private JMenuItem m_csvItem;
	private JMenuItem m_quitItem;
	
	public EditorMenuBarFile(final MainController mainController)
	{
		this.mainController = mainController;
	}

	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		m_fileMenu = new JMenu(ResourceUtil.getMessage("main.menu.file"));
		m_fileMenu.setBorderPainted(false);
		m_fileMenu.addMouseListener(menuListener);

		m_newItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.new"));
		m_newItem.addActionListener(this);
		m_newItem.setAccelerator(KeyStroke.getKeyStroke('N', menuKeyMask));
		m_fileMenu.add(m_newItem);

		m_openItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.open"));
		m_openItem.addActionListener(this);
		m_openItem.setAccelerator(KeyStroke.getKeyStroke('O', menuKeyMask));
		m_fileMenu.add(m_openItem);

		m_closeItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.close"));
		m_closeItem.addActionListener(this);
		m_closeItem.setAccelerator(KeyStroke.getKeyStroke('W', menuKeyMask));
		m_fileMenu.add(m_closeItem);

		m_closeAllItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.close.all"));
		m_closeAllItem.addActionListener(this);
		m_fileMenu.add(m_closeAllItem);

		m_fileMenu.add(new JSeparator());
		
		m_saveItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.save"));
		m_saveItem.addActionListener(this);
		m_saveItem.setAccelerator(KeyStroke.getKeyStroke('S', menuKeyMask));
		m_fileMenu.add(m_saveItem);

		m_saveAsItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.save.as"));
		m_saveAsItem.addActionListener(this);
		m_saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK + java.awt.event.InputEvent.SHIFT_MASK));
		m_fileMenu.add(m_saveAsItem);
		
		m_fileMenu.add(new JSeparator());
		
		m_recentFilesMenu = new JMenu(ResourceUtil.getMessage("main.menu.file.open.recent.file"));
		final String[] vRecentFiles = recentFiles();
		m_recentFilesMenu.setEnabled(vRecentFiles.length > 0);
		m_recentFilesMenu.setMnemonic('R');
		for (int i = 0; i < vRecentFiles.length; i++)
		{
			m_subMenuItem[i] = new JMenuItem(vRecentFiles[i]);
			m_subMenuItem[i].addActionListener(this);
			m_subMenuItem[i].setAccelerator(KeyStroke.getKeyStroke('1' + i, menuKeyMask));
			m_recentFilesMenu.add(m_subMenuItem[i]);
		}
		m_fileMenu.add(m_recentFilesMenu);
		
		m_fileMenu.add(new JSeparator());
		
		m_devcapOpenMenu = new JMenuItem(ResourceUtil.getMessage("main.menu.file.open.recent.devcap"));
		m_fileMenu.add(m_devcapOpenMenu);
		m_devcapOpenMenu.addActionListener(this);

		m_csvItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.export.csv"));
		m_csvItem.addActionListener(this);
		m_fileMenu.add(m_csvItem);

		m_fileMenu.add(new JSeparator());

		m_quitItem = new JMenuItem(ResourceUtil.getMessage("main.menu.file.quit"));
		m_quitItem.addActionListener(frame); // TODO: this ?
		m_quitItem.setAccelerator(KeyStroke.getKeyStroke('Q', menuKeyMask));
		m_fileMenu.add(m_quitItem);
		
		return m_fileMenu;
	}
	
	public void setEnableClose()
	{
		m_saveAsItem.setEnabled(false);
		m_saveItem.setEnabled(false);
		m_csvItem.setEnabled(false);
		m_closeItem.setEnabled(false);
		m_closeAllItem.setEnabled(false);
	}
	
	public void setEnableOpen(final boolean mode)
	{
		m_csvItem.setEnabled(true);
		m_newItem.setEnabled(mode);
		m_closeItem.setEnabled(true);
		m_closeAllItem.setEnabled(true);
		m_devcapOpenMenu.setEnabled(mainController.getSetting(SettingKey.RECENT_DEV_CAP, String.class) != null);
		m_saveAsItem.setEnabled(mode);
		m_saveItem.setEnabled(mode);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		MainView.setCursor(1, null);
		final Object eSrc = e.getSource();
		final JDFFrame frame = MainView.getFrame();
		
		if (eSrc == m_newItem)
		{
			frame.newFile();
		}
		else if (eSrc == m_openItem)
		{
			frame.openFile();
		}
		else if (eSrc == m_closeItem)
		{
			MainView.getFrame().closeFile(1);
		}
		else if (eSrc == m_closeAllItem)
		{
			MainView.getFrame().closeFile(99999);
		}
		else if (eSrc == m_saveAsItem)
		{
			MainView.getFrame().saveAs();
		}
		else if (eSrc == m_saveItem)
		{
			MainView.getFrame().save();
		}
		else if (eSrc == m_csvItem)
		{
			MainView.getModel().saveAsCSV(null);
		}
		else if (eSrc == m_devcapOpenMenu)
		{
			String s = mainController.getSetting(SettingKey.RECENT_DEV_CAP, String.class);
			File f = null;

			if (s != null)
			{
				f = new File(s);
			}

			if (f != null)
				openRecentFile(f);
			else
				EditorUtils.errorBox("OpenJDFErrorKey", "No Devcap File defined");
		}

		for (int i = 0; i < m_subMenuItem.length; i++)
		{
			if (eSrc == m_subMenuItem[i])
			{
				final String newFile = m_subMenuItem[i].getText();
				openRecentFile(new File(newFile));
			}
		}
		MainView.setCursor(0, null);
	}
	
	/**
	 * Checks if the String[] m_iniFile.recentFiles file has any content. Called to check if the the m_recentFilesMenu is to be created.
	 * @return true if any file has recently been opened; false otherwise.
	 */
	private String[] recentFiles()
	{
		int l = 0;
		final String[] v = RecentFileUtil.getRecentFiles();
		for (int i = 0; i < v.length; i++)
		{
			if (v[i] != null)
			{
				l++;
			}
		}
		// no nulls - just return v
		if (l == v.length)
		{
			return v;
		}

		// create new continuous list w
		final String w[] = new String[l];
		l = 0;
		for (int i = 0; i < v.length; i++)
		{
			if (v[i] != null)
			{
				w[l++] = v[i];
			}
		}
		return w;
	}

	/**
	 * Called if a file is opened from the recent files menu.
	 * @param fileToSave - Path to the file that is to be opened
	 */
	private void openRecentFile(final File fileToSave)
	{
		MainView.setCursor(1, null);
		final boolean b = MainView.getFrame().readFile(fileToSave);

		if (b)
		{
			final String s = fileToSave.getAbsolutePath();

			if (RecentFileUtil.nrOfRecentFiles() != 1)
			{
				RecentFileUtil.updateOrder(s, true);
			}
		}
		else
		{
			EditorUtils.errorBox("OpenJDFErrorKey", fileToSave.getPath().toString());
		}
		MainView.setCursor(0, null);
	}

	/**
	 * Updates the order in the Recent Files Menu. also updates all windows and the ini file - just in case
	 * @param pathName - The path to the file
	 */
	public void updateRecentFilesMenu(final String pathName)
	{
		final boolean exist = RecentFileUtil.pathNameExists(pathName);

		RecentFileUtil.updateOrder(pathName, exist);

		if (RecentFileUtil.nrOfRecentFiles() != 0)
		{
			m_recentFilesMenu.setEnabled(true);
			for (int i = 0; i < RecentFileUtil.nrOfRecentFiles(); i++)
			{
				if (m_subMenuItem[i] == null)
				{
					m_subMenuItem[i] = new JMenuItem(RecentFileUtil.getRecentFiles()[i]);
					m_subMenuItem[i].addActionListener(this);
					m_recentFilesMenu.add(m_subMenuItem[i]);
				}
				else
				{
					m_subMenuItem[i].setText(RecentFileUtil.getRecentFiles()[i]);
				}
			}
		}
		else
		{
			m_recentFilesMenu.setEnabled(false);
		}
	}
}
