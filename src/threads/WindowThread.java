package threads;

import main.Main;
import main.Utils;

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
	Object[] JFrameObjects = new Object[300];

	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			System.out.println("[ERROR] An unknown Error occured. \n"+e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("[ERROR] An unknown Error occured. \n"+e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("[ERROR] An unknown Error occured. \n"+e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println("[ERROR] An unknown Error occured. \n"+e.getMessage());
		}
		System.out.println("[Window] Thread started.");
		jframe = new JFrame("[JLC] Java Light Control - "+ Main.getVersion());
		jframe.setSize(700,500);
		menuBar = new JMenuBar();
		menu1 = new JMenu("File");
		openProject = new JMenuItem("Open Project");
		saveProject = new JMenuItem("Save Project");
		saveProjectAs = new JMenuItem("Save Project As...");
		openProject.addActionListener(this);
		saveProject.addActionListener(this);
		saveProjectAs.addActionListener(this);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Main.saveProject();
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

	public static void setShouldStop(Boolean shouldStop) {
		WindowThread.shouldStop = shouldStop;
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
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