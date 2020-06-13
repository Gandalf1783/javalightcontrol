package de.gandalf1783.jlc.gfx;

import de.gandalf1783.jlc.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Display implements ActionListener {

	private JFrame frame;
	private Canvas canvas;

	private String title;
	private int width, height;

	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;

		createDisplay();
	}

	private void createDisplay() {
		System.out.println("Creating Display");
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				Main.shutdown();
			}
		});


		frame.add(canvas);
		//frame.pack();
		System.out.println("Created.");
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void actionPerformed(final ActionEvent e) {
		final String s = e.getActionCommand();
		if (s.equalsIgnoreCase("Open Project")) {
			Main.loadProjectGUI();
		}
		if (s.equalsIgnoreCase("Save Project")) {
			Main.saveProject();
		}
		if (s.equalsIgnoreCase("Save Project As...")) {
			Main.saveProjectHandlerGUI();
		}

	}

}
