package de.gandalf1783.jlc.gfx;

import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.threads.CLIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Display {

	private final String title;
	private final int width;
	private final int height;
	private JFrame frame;
	private Canvas canvas;

	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;

		createDisplay();
	}

	public static Color hex2Rgb(String colorStr) {
		return new Color(
				Integer.valueOf(colorStr.substring(1, 3), 16),
				Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}

	private void createDisplay() {
		CLIUtils.println("Creating Display");
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setBackground(hex2Rgb("#484848"));
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		canvas.setBackground(hex2Rgb("#484848"));
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				Main.shutdown();
			}
		});


		frame.add(canvas);
		//frame.pack();
		CLIUtils.println("Created.");
	}

}
