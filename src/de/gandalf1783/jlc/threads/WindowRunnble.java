package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.gfx.Assets;
import de.gandalf1783.jlc.gfx.Display;
import de.gandalf1783.jlc.gfx.MouseManager;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.UniverseOut;
import de.gandalf1783.jlc.uiItems.Button;
import de.gandalf1783.jlc.uiItems.FaderPage;
import de.gandalf1783.jlc.uiItems.ToggleButton;
import de.gandalf1783.jlc.uiItems.UiItem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.net.NetworkInterface;
import java.util.ArrayList;

public class WindowRunnble implements Runnable {

	private static Boolean shouldStop = false;

	private Display display;
	private BufferStrategy bs;
	private Graphics g;
	ArrayList<ArrayList<UiItem>> items = new ArrayList<>(4);
	private MouseManager mouseManager;
	private int currentPage = 0;
	private String status = "Booting...";

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(final Boolean shouldStop) {
		WindowRunnble.shouldStop = shouldStop;

	}

	@Override
	public void run() {
		init();

		int fps = 24;
		double timePerTick;
		timePerTick = (double) 1000000000 / fps;
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

		CLIUtils.println("[Window] Thread stopped.");
	}

	private void init() {
		Assets.init();
		display = new Display("JLC", 1000, 500);
		display.getFrame().setIconImage(Assets.icon);

		mouseManager = new MouseManager();
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		ArrayList<UiItem> page1 = new ArrayList<>();
		ArrayList<UiItem> page_settings = new ArrayList<>();
		ArrayList<UiItem> page_effects = new ArrayList<>();
		ArrayList<UiItem> page_faders = new ArrayList<>();
		ArrayList<UiItem> page_session = new ArrayList<>();

		Button project_open = new Button(0, 0, 40, 110, "Open Project", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.loadProjectGUI();
			}
		};
		Button project_save = new Button(0, 40, 40, 110, "Save Project", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.saveProject(Main.getJLCSettings().getProject_path());
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
				ArtNetRunnable.toggleBlackout();
			}
		};
		Button effects = new Button(0, 160, 40, 110, "Effects", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.getWindowRunnable().setCurrentPage(2);
			}
		};
		Button faders = new Button(0, 200, 40, 110, "Faders", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.getWindowRunnable().setCurrentPage(3);
			}
		};
		Button settings = new Button(0, 432, 40, 110, "Settings", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.getWindowRunnable().setCurrentPage(1);
			}
		};
		Button home = new Button(0, 0, 40, 110, "Home", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.getWindowRunnable().setCurrentPage(0);
			}
		};
		Button backToSettings = new Button(0, 0, 40, 110, "<- Settings", g) {
			@Override
			public void onClick(MouseEvent e) {

				Main.getWindowRunnable().setCurrentPage(1);
			}
		};
		Button session = new Button(0, 40, 40, 110, "Sessions", g) {
			@Override
			public void onClick(MouseEvent e) {
				Main.getWindowRunnable().setCurrentPage(4);
			}
		};

		FaderPage fader = new FaderPage(110, 40, g);

		page1.add(project_open);
		page1.add(project_save);
		page1.add(project_save_as);
		page1.add(settings);
		page1.add(blackout);
		page1.add(effects);
		page1.add(faders);

		page_faders.add(fader);
		page_settings.add(home);
		page_settings.add(session);
		page_effects.add(home);

		page_faders.add(home);
		page_session.add(backToSettings);

		items.add(page1);
		items.add(page_settings);
		items.add(page_effects);
		items.add(page_faders);
		items.add(page_session);
	}

	public void onMouseMove(MouseEvent e) {
		if (!(currentPage <= items.size()))
			return;
		if (items.get(currentPage) != null) {
			for (UiItem item : items.get(currentPage)) {
				item.onMouseMove(e);
			}
		}
	}

	public void onMouseRelease(MouseEvent e) {
		for (UiItem item : items.get(currentPage)) {
			item.onMouseRelease(e);
		}
	}

	public void onMouseClicked(MouseEvent e) {
		for (UiItem item : items.get(currentPage)) {
			item.onMouseClicked(e);
		}
	}

	public void onMouseDragged(MouseEvent e) {
		for (UiItem item : items.get(currentPage)) {

			item.onMouseDrag(e);
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
		g.setColor(Color.cyan);
		g.drawString(status, 120, 455);
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

	public void setStatus(String s) {
		this.status = s;
	}

	public void getNetworkInterfaces() {

	}

	public void setNetworkInterface() {

	}

	public void setUniverseOut(NetworkInterface ni, Boolean enabled) {
		if (enabled) {
			UniverseOut[] uniOut = Main.getProject().getUniverseOut();
		}
	}

}