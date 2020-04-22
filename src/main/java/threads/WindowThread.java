package threads;

import main.Main;
import main.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WindowThread implements Runnable, ActionListener {

	private static Boolean shouldStop = false;

	JFrame jframe;

	JMenuBar menuBar;
	JMenu menu1;
	JMenuItem openProject, saveProject, saveProjectAs;
	Object[] JFrameObjects = new Object[300];
	
	private JTabbedPane tabbedPane;
	private JPanel effectPanel;
	private JPanel universePanel;
	private JPanel dmxPanel;
	private JPanel sessionPanel;
	private JButton button;
	private JButton button_1;
	private JButton button_2;

	/**
	 * @wbp.parser.entryPoint
	 */
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
		
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		effectPanel = new JPanel();
		universePanel = new JPanel();
		dmxPanel = new JPanel();
		sessionPanel = new JPanel();
		tabbedPane.addTab("Effects", effectPanel);
		GroupLayout gl_effectPanel = new GroupLayout(effectPanel);
		gl_effectPanel.setHorizontalGroup(
		    gl_effectPanel.createParallelGroup(Alignment.LEADING)
		        .addGap(0, 679, Short.MAX_VALUE)
		);
		gl_effectPanel.setVerticalGroup(
		    gl_effectPanel.createParallelGroup(Alignment.LEADING)
		        .addGap(0, 411, Short.MAX_VALUE)
		);
		effectPanel.setLayout(gl_effectPanel);
		tabbedPane.addTab("Universes", universePanel);
		GroupLayout gl_universePanel = new GroupLayout(universePanel);
		gl_universePanel.setHorizontalGroup(
		    gl_universePanel.createParallelGroup(Alignment.LEADING)
		        .addGap(0, 679, Short.MAX_VALUE)
		);
		gl_universePanel.setVerticalGroup(
		    gl_universePanel.createParallelGroup(Alignment.LEADING)
		        .addGap(0, 411, Short.MAX_VALUE)
		);
		universePanel.setLayout(gl_universePanel);
		tabbedPane.addTab("DMX", dmxPanel);
		GroupLayout gl_dmxPanel = new GroupLayout(dmxPanel);
		gl_dmxPanel.setHorizontalGroup(
		    gl_dmxPanel.createParallelGroup(Alignment.LEADING)
		        .addGap(0, 679, Short.MAX_VALUE)
		);
		gl_dmxPanel.setVerticalGroup(
		    gl_dmxPanel.createParallelGroup(Alignment.LEADING)
		        .addGap(0, 411, Short.MAX_VALUE)
		);
		dmxPanel.setLayout(gl_dmxPanel);
		tabbedPane.addTab("Session", sessionPanel);
		
		button = new JButton("Create session");
		button.addActionListener(e -> {
		    //TODO: implement createSession
		});
		
		button_1 = new JButton("Join session");
		button_1.addActionListener(e -> {
		    //TODO: implement joinSession
		});
		
		button_2 = new JButton("Leave session");
		button_2.addActionListener(e -> {
		    //TODO: implement leaveSession
		});
		GroupLayout gl_sessionPanel = new GroupLayout(sessionPanel);
		gl_sessionPanel.setHorizontalGroup(
		    gl_sessionPanel.createParallelGroup(Alignment.LEADING)
		        .addGroup(gl_sessionPanel.createSequentialGroup()
		            .addContainerGap()
		            .addComponent(button, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
		            .addGap(6)
		            .addComponent(button_1)
		            .addGap(6)
		            .addComponent(button_2, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
		            .addContainerGap(366, Short.MAX_VALUE))
		);
		gl_sessionPanel.setVerticalGroup(
		    gl_sessionPanel.createParallelGroup(Alignment.LEADING)
		        .addGroup(gl_sessionPanel.createSequentialGroup()
		            .addContainerGap()
		            .addGroup(gl_sessionPanel.createParallelGroup(Alignment.LEADING)
		                .addComponent(button)
		                .addComponent(button_1)
		                .addComponent(button_2))
		            .addContainerGap(377, Short.MAX_VALUE))
		);
		sessionPanel.setLayout(gl_sessionPanel);
		GroupLayout groupLayout = new GroupLayout(jframe.getContentPane());
		groupLayout.setHorizontalGroup(
		    groupLayout.createParallelGroup(Alignment.LEADING)
		        .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
		    groupLayout.createParallelGroup(Alignment.LEADING)
		        .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
		);
		jframe.getContentPane().setLayout(groupLayout);
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