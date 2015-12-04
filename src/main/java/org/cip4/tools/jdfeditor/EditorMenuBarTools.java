package org.cip4.tools.jdfeditor;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.elementwalker.URLExtractor;
import org.cip4.jdflib.util.FileUtil;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.jdflib.util.UrlUtil;
import org.cip4.tools.jdfeditor.EditorMenuBar.Menu_MouseListener;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.util.ResourceUtil;
import org.cip4.tools.jdfeditor.view.MainView;

public class EditorMenuBarTools implements ActionListener
{
	private MainController mainController;
	
	private JMenu menu;
	
	private JMenuItem spawnItem;
	private JMenuItem spawnInformItem;
	private JMenuItem mergeItem;
	private JMenuItem unspawnItem;
	private JMenuItem preferenceItem;
	private JMenuItem sendToDeviceItem;
	public JMenuItem m_fixVersionItem;
	private JMenuItem m_fixCleanupItem;
	private JMenuItem m_removeExtenisionItem;
	private JMenuItem extractItem;
	
	
	public EditorMenuBarTools(final MainController mainController)
	{
		this.mainController = mainController;
	}
	
	public JMenu createMenu()
	{
		final JDFFrame frame = MainView.getFrame();
		final Menu_MouseListener menuListener = new EditorMenuBar().new Menu_MouseListener();
		final int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		menu = new JMenu(ResourceUtil.getMessage("main.menu.tools"));
		menu.setBorderPainted(false);
		menu.addMouseListener(menuListener);
		
		spawnItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.spawn"));
		spawnItem.addActionListener(this);
		spawnItem.setEnabled(false);
		spawnItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
		menu.add(spawnItem);

		spawnInformItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.spawn.info"));
		spawnInformItem.addActionListener(this);
		spawnInformItem.setAccelerator(KeyStroke.getKeyStroke('I', InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
		spawnInformItem.setEnabled(false);
		menu.add(spawnInformItem);

		mergeItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.merge"));
		mergeItem.addActionListener(this);
		mergeItem.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
		mergeItem.setEnabled(false);
		menu.add(mergeItem);

		unspawnItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.spawn.undo"));
		unspawnItem.addActionListener(this);
		unspawnItem.setEnabled(false);
		menu.add(unspawnItem);

		menu.add(new JSeparator());
		
		preferenceItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.preferences"));
		preferenceItem.addActionListener(this);
		menu.add(preferenceItem);
		
		menu.add(new JSeparator());

		sendToDeviceItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.send"));
		sendToDeviceItem.addActionListener(this);
		menu.add(sendToDeviceItem);

		menu.add(new JSeparator());
		
		m_fixVersionItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.fix"));
		m_fixVersionItem.addActionListener(frame);
		m_fixVersionItem.setEnabled(true);
		menu.add(m_fixVersionItem);

		m_fixCleanupItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.clean"));
		m_fixCleanupItem.addActionListener(this);
		m_fixCleanupItem.setEnabled(true);
		menu.add(m_fixCleanupItem);

		m_removeExtenisionItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.remove"));
		m_removeExtenisionItem.addActionListener(this);
		m_removeExtenisionItem.setEnabled(true);
		menu.add(m_removeExtenisionItem);

		extractItem = new JMenuItem(ResourceUtil.getMessage("main.menu.tools.extract"));
		extractItem.addActionListener(this);
		extractItem.setEnabled(true);
		menu.add(extractItem);
		
		return menu;
	}
	
	public void setEnableClose()
	{
		sendToDeviceItem.setEnabled(false);
		extractItem.setEnabled(false);
	}
	
	public void setEnableOpen(final boolean mode)
	{
		sendToDeviceItem.setEnabled(true);
		extractItem.setEnabled(true);
	}
	
	/**
	 * enable or disable spawn n merge in bulk
	 * @param enable
	 */
	public void setSpawnMergeEnabled(final boolean enable)
	{
		spawnItem.setEnabled(enable);
		spawnInformItem.setEnabled(enable);
		mergeItem.setEnabled(enable);
		unspawnItem.setEnabled(enable);
	}
	
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final Object eSrc = e.getSource();
		if (eSrc == spawnItem)
		{
			MainView.getModel().spawn(false);
		}
		else if (eSrc == spawnInformItem)
		{
			MainView.getModel().spawn(true);
		}
		else if (eSrc == mergeItem)
		{
			MainView.getModel().merge();
		}
		else if (eSrc == sendToDeviceItem)
		{
			new SendToDevice().trySend();
		}
		else if (eSrc == preferenceItem)
		{
			showPreferences();
		}
		else if (eSrc == extractItem)
		{
			extractAll();
		}
		else if (eSrc == m_fixCleanupItem)
		{
			MainView.getFrame().cleanupSelected();
		}
		else if (eSrc == m_removeExtenisionItem)
		{
			MainView.getFrame().removeExtensionsfromSelected();
		}
	}
	
	private void showPreferences()
	{
		final String[] options = { ResourceUtil.getMessage("OkKey"), ResourceUtil.getMessage("CancelKey") };
		final PreferenceDialog pd = new PreferenceDialog();

		final int option = JOptionPane.showOptionDialog(menu, pd, ResourceUtil.getMessage("PreferenceKey"), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (option == JOptionPane.OK_OPTION)
		{
			pd.writeToIni();
			final EditorDocument ed = MainView.getEditorDoc();
			if (ed != null && ed.getJDFTree() != null)
			{
				ed.getJDFTree().repaint();
			}
		}
	}
	
	private void extractAll()
	{
		EditorDocument d = getEditorDoc();

		String name = d == null ? null : StringUtil.getNonEmpty(d.getSaveFileName());
		if (name != null)
		{
			String dir = UrlUtil.newExtension(name, null);

			File dirFile = FileUtil.getFileInDirectory(new File(dir), new File("extracted"));
			if (dirFile.mkdirs() || dirFile.isDirectory())
			{
				new URLExtractor(dirFile, null, null).convert(getJDFDoc().getRoot());
			}
		}
	}
	
	private JDFDoc getJDFDoc()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getJDFDoc();
	}

	private EditorDocument getEditorDoc()
	{
		final EditorDocument ed = MainView.getFrame().getEditorDoc();
		return ed;
	}
}
