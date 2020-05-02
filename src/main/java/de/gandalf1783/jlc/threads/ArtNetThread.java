package de.gandalf1783.jlc.threads;

import ch.bildspur.artnet.ArtNetClient;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.UniverseOut;

public class ArtNetThread implements Runnable {

	private static Boolean shouldStop = false;
	private static int fps = 30;

	public static ArtNetClient artnet;

	private void init() {
		System.out.println("[ArtNet] Thread started.");
		artnet = new ArtNetClient();
		artnet.start();
	}

	@SuppressWarnings({ "unused" })
	@Override
	public void run() {
		init();
		while (!shouldStop) {

			long lastLoopTime = System.nanoTime();
			final int TARGET_FPS = 30;
			final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
			long lastFpsTime = 0;
			while (true) {

				long now = System.nanoTime();
				long updateLength = now - lastLoopTime;
				lastLoopTime = now;
				double delta = updateLength / ((double) OPTIMAL_TIME);

				lastFpsTime += updateLength;
				fps++;

				if (lastFpsTime >= 1000000000) {
					lastFpsTime = 0;
					fps = 0;
				}

				if(!Main.getSessionMode()){
					// Get all aditional unicast ip's and send to them:
					for (int i = 0; i < Main.getSettings().getUniverseOut().length; i++) {
						if (Main.getSettings().getUniverseOut()[i] != null) {
							UniverseOut uout = Main.getSettings().getUniverseOut()[i];
							for (int j = 0; j < uout.getIP().length; j++) {
								if (uout.getIP(j) != null) {
									artnet.unicastDmx(uout.getIP(j), Main.getSettings().getSubNet(), i,
											Main.getUniverseData(i));
								}
							}
						}
					}
				}
				try {
					long timeout = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
					if (timeout <= 0) {
						timeout = 10;
					}
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("ArtNet Thread: Sleep couldn't be executed due exiting the app");
				}
			}

		}
		System.out.println("[ArtNet] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(Boolean shouldStop) {
		ArtNetThread.shouldStop = shouldStop;
	}

	public static void shouldStop() {
		shouldStop = true;
	}

	public static int getFps() {
		return fps;
	}

	public static void setFps(int fps) {
		ArtNetThread.fps = fps;
	}

}
