package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.gfx.Assets;
import de.gandalf1783.jlc.gfx.Display;
import de.gandalf1783.jlc.gfx.MouseManager;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.uiItems.Button;
import de.gandalf1783.jlc.uiItems.ToggleButton;
import de.gandalf1783.jlc.uiItems.UiItem;

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
	ArrayList<ArrayList<UiItem>> items = new ArrayList<ArrayList<UiItem>>(4);
	private MouseManager mouseManager;
	private int currentPage = 0;

	private void init() {
		display = new Display("JLC", 1000, 500);
		Assets.init();

		mouseManager = new MouseManager();
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		ArrayList<UiItem> page1 = new ArrayList<>();
		ArrayList<UiItem> page_settings = new ArrayList<>();
		Button project_open = new Button(0, 0, 40, 110, "Open Project", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.loadProjectGUI();
			}
		};
		Button project_save = new Button(0, 40, 40, 110, "Save Project", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.saveProject();
			}
		};
		Button project_save_as = new Button(0, 80, 40, 110, "Save Project As", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.saveProjectHandlerGUI();
			}
		};
		ToggleButton blackout = new ToggleButton(0, 120, 40, 110, "Blackout", g) {
			@Override
			public void onToggle(MouseEvent e) {
				ArtNetThread.toggleBlackout();
			}
		};
		Button settings = new Button(0, 432, 40, 110, "Settings", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.getWindowThread().setCurrentPage(1);
			}
		};

		Button home = new Button(0, 0, 40, 110, "Home", g) {
			@Override
			public void onClick(MouseEvent e) {

				Main.getWindowThread().setCurrentPage(0);
			}
		};

		page1.add(project_open);
		page1.add(project_save);
		page1.add(project_save_as);
		page1.add(settings);
		page1.add(blackout);
		page_settings.add(home);
		items.add(page1);
		items.add(page_settings);
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
		for (ArrayList<UiItem> arrayList : items) {
			for (UiItem u : arrayList) {
				u.onMouseMove(e);
			}
		}
	}

	public void onMouseRelease(MouseEvent e) {
		for (ArrayList<UiItem> arrayList : items) {
			for (UiItem u : arrayList) {
				u.onMouseRelease(e);
			}
		}
	}

	public void onMouseClicked(MouseEvent e) {
		for (ArrayList<UiItem> arrayList : items) {
			for (UiItem u : arrayList) {
				u.onMouseClicked(e);
			}
		}

	}

	public void onMouseDragged(MouseEvent e) {
		for (ArrayList<UiItem> arrayList : items) {
			for (UiItem u : arrayList) {
				u.onDrag(e);
			}
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


		for (UiItem item : items.get(currentPage)) {
			item.tick();
			item.render(g);
		}

		//End Drawing!
		bs.show();
		g.dispose();
	}

	public Boolean isOnCurrentPage(UiItem u) {
		return items.get(currentPage).contains(u);
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public void nextPage() {
		currentPage++;
	}

	public void previousPage() {
		currentPage--;
	}

	public void setCurrentPage(int page) {
		currentPage = page;
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