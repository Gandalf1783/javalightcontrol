package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowThread implements Runnable, ActionListener {

	private static Boolean shouldStop = false;

	JFrame jframe;

	JMenuBar menuBar;
	JMenu menu1;
	JMenuItem openProject, saveProject, saveProjectAs;
	JComboBox<String> sessionComboBox;

	public JFrame getJFrame() {
		return jframe;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final ClassNotFoundException e) {
			System.out.println("[ERROR] An unknown Error occured. \n" + e.getMessage());
		} catch (final InstantiationException e) {
			System.out.println("[ERROR] An unknown Error occured. \n" + e.getMessage());
		} catch (final IllegalAccessException e) {
			System.out.println("[ERROR] An unknown Error occured. \n" + e.getMessage());
		} catch (final UnsupportedLookAndFeelException e) {
			System.out.println("[ERROR] An unknown Error occured. \n" + e.getMessage());
		}
		System.out.println("[Window] Thread started.");
		jframe = new JFrame("[JLC] Java Light Control - " + Main.getVersion());
		jframe.setSize(700, 500);
		menuBar = new JMenuBar();
		menu1 = new JMenu("File");
		openProject = new JMenuItem("Open Project");
		saveProject = new JMenuItem("Save Project");
		saveProjectAs = new JMenuItem("Save Project As...");
		openProject.addActionListener(this);
		saveProject.addActionListener(this);
		saveProjectAs.addActionListener(this);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				Main.shutdown();
			}
		});
		menu1.add(openProject);
		menu1.add(saveProject);
		menu1.add(saveProjectAs);
		menuBar.add(menu1);
		jframe.setJMenuBar(menuBar);
		jframe.setVisible(true);
		jframe.setResizable(true);
	}

	@Override
	public void run() {
		init();
		while (!shouldStop) {

		}
		System.out.println("[Window] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(final Boolean shouldStop) {
		WindowThread.shouldStop = shouldStop;

	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final String s = e.getActionCommand();
		if(s.equalsIgnoreCase("Open Project")) {
			Main.loadProjectGUI();
		}
		if(s.equalsIgnoreCase("Save Project")) {
			Main.saveProject();
		}
		if(s.equalsIgnoreCase("Save Project As...")) {
			Main.saveProjectHandlerGUI();
		}

	}
}