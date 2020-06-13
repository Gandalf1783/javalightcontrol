package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.gfx.Assets;
import de.gandalf1783.jlc.gfx.Display;
import de.gandalf1783.jlc.gfx.MouseManager;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.uiItems.Button;
import de.gandalf1783.jlc.uiItems.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class WindowThread implements Runnable, ActionListener {

	private static Boolean shouldStop = false;

	private Display display;
	private BufferStrategy bs;
	private Graphics g;
	private ArrayList<UiItem> items;
	private MouseManager mouseManager;

	private void init() {
		display = new Display("JLC", 1000, 500);
		Assets.init();
		items = new ArrayList<>();

		mouseManager = new MouseManager();
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);

		HorizontalScrollBar bar = new HorizontalScrollBar(40, 0, g);
		VerticalScrollBar bar2 = new VerticalScrollBar(0, 40, g);
		Button b = new Button(50, 50, 40, 90, "Button", g);
		ToggleButton tb = new ToggleButton(100, 100, 50, 50, "Test", g);
		items.add(bar2);
		items.add(bar);
		items.add(b);
		items.add(tb);
	}

	@Override
	public void run() {
		init();

		int fps = 24;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (!shouldStop) {

			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				render();
				ticks++;
				delta--;
			}

			if (timer >= 1000000000) {
				ticks = 0;
				timer = 0;
			}

		}

		System.out.println("[Window] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(final Boolean shouldStop) {
		WindowThread.shouldStop = shouldStop;

	}


	public void onMouseMove(MouseEvent e) {
		for (UiItem u : items) {
			u.onMouseMove(e);
		}

	}

	public void onMouseRelease(MouseEvent e) {
		for (UiItem u : items) {
			u.onMouseRelease(e);
		}
	}

	public void onMouseClicked(MouseEvent e) {
		for (UiItem u : items) {
			u.onMouseClicked(e);
		}
	}

	public void onMouseDragged(MouseEvent e) {
		for (UiItem u : items) {
			u.onDrag(e);
		}
	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screen
		g.clearRect(0, 0, 1000, 500);
		//Draw Here!


		for (UiItem item : items) {
			item.tick();
			item.render(g);
		}

		//End Drawing!
		bs.show();
		g.dispose();
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	@Override
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