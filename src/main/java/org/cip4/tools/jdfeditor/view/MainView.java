package org.cip4.tools.jdfeditor.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.core.JDFAudit;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.tools.jdfeditor.Editor;
import org.cip4.tools.jdfeditor.EditorDocument;
import org.cip4.tools.jdfeditor.JDFFrame;
import org.cip4.tools.jdfeditor.JDFTreeArea;
import org.cip4.tools.jdfeditor.JDFTreeModel;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;

/**
 * The MainView of the CIP4 JDFEditor.
 */
public class MainView
{
	private static final Log LOGGER = LogFactory.getLog(MainView.class);

	private MainController mainController;

	public static JDFFrame my_Frame;

	/**
	 * Default constructor.
	 */
	public MainView()
	{
		my_Frame = new JDFFrame();
	}

	/**
	 * Register a MainController for this view (MVC Pattern)
	 * @param mainController The MainController for this view.
	 */
	public void registerController(final MainController mainController)
	{
		this.mainController = mainController;
		my_Frame.registerController(mainController);
	}

	/**
	 * Show a Message Dialog in MainView.
	 * @param message The message of the dialog.
	 * @param title The title of the dialog.
	 */
	public void showMessageDialog(String message, String title)
	{
		JOptionPane.showMessageDialog(my_Frame, message, title, JOptionPane.INFORMATION_MESSAGE);

		// final ImageIcon imgCIP = MainView.getImageIcon(MainView.ICONS_PATH + "CIP4.gif");
		// JOptionPane.showMessageDialog(this, "see http://cip4.org/jdfeditor", "CIP4 JDF Editor", JOptionPane.INFORMATION_MESSAGE, imgCIP);

	}

	/**
	 * Sets the cursor to wait or ready
	 * @param iWait 0 = ready; 1 = wait; 2 = hand
	 * @param parentComponent the parent frame to set the cursor in, if null use the main frame
	 */
	public static void setCursor(final int iWait, java.awt.Component parentComponent)
	{
		if (parentComponent == null)
		{
			parentComponent = my_Frame;
		}

		if (iWait == 0)
		{
			parentComponent.setCursor(Cursor.getDefaultCursor());
		}
		else if (iWait == 1)
		{
			parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else if (iWait == 2)
		{
			final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			parentComponent.setCursor(handCursor);
		}
	}

	/**
	 * Method instantiate the editor window
	 * @param file the file to open initially
	 */
	public void display(final File file)
	{
		try
		{
			String currentLookAndFeel = mainController.getSetting(SettingKey.GENERAL_LOOK, String.class);
			UIManager.setLookAndFeel(currentLookAndFeel);
		}
		catch (final Exception e)
		{
			LOGGER.error("Error during setting 'Look and Feel' of JDFEditor.", e);
		}

		setCursor(0, null);

		// read the initialization stuff
		JDFAudit.setStaticAgentName(Editor.getEditor().getEditorName());
		JDFAudit.setStaticAgentVersion(getEditorVersion());
		JDFElement.setDefaultJDFVersion(JDFElement.EnumVersion.getEnum(mainController.getSetting(SettingKey.VALIDATION_VERSION, String.class)));
		JDFParser.m_searchStream = true;

		my_Frame.drawWindow();
		my_Frame.setBackground(Color.white);
		my_Frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		final WindowListener winLis = new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent e)
			{
				if (my_Frame.closeFile(999) != JOptionPane.CANCEL_OPTION)
				{
					System.exit(0);
					e.getID(); // make compile happy
				}
			}
		};
		my_Frame.addWindowListener(winLis);
		my_Frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainView.class.getResource("/org/cip4/tools/jdfeditor/jdfeditor_128.png")));

		// this is only for the PC version
		if (file != null)
		{
			final boolean b = my_Frame.readFile(file);
			if (b)
			{
				my_Frame.m_menuBar.updateRecentFilesMenu(file.toString());
			}
		}
	}

	/**
	 * Method getFrame.
	 * @return JDFFrame
	 */
	public static JDFFrame getFrame()
	{
		return my_Frame;
	}

	/**
	 * get the JDFDoc of the currently displayed JDF
	 * @return the JDFDoc that is currently being displayed
	 */
	public static EditorDocument getEditorDoc()
	{
		return my_Frame.getEditorDoc();
	}

	/**
	 * Returns the Version of the JDFLibJ.
	 * @return The version number of JDFLibJ.
	 */
	public String getEditorVersion()
	{
		return "Build version " + JDFAudit.software();
	}

	/**
	 * Get the model associated with the currently displayed document.
	 * @return The data model
	 */
	public static JDFTreeModel getModel()
	{
		final EditorDocument ed = getEditorDoc();
		return ed == null ? null : ed.getModel();
	}

	/**
	 * get the JDFDoc of the currently displayed JDF
	 * @return the JDFDoc that is currently being displayed BMI Created 07-08-31
	 */
	public static JDFDoc getJDFDoc()
	{
		return my_Frame.getJDFDoc();
	}

	/**
	 * Method getTreeArea.
	 * @return ResourceBundle the static resource bundle BMI Created 07-08-31
	 */
	public static JDFTreeArea getTreeArea()
	{
		return my_Frame.m_treeArea;
	}
}
