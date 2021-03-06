package de.gandalf1783.jlc.threads;

import ch.bildspur.artnet.ArtNetClient;
import ch.bildspur.artnet.ArtNetServer;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.UniverseOut;

public class ArtNetRunnable implements Runnable {

	private static Boolean shouldStop = false;
	public static ArtNetServer artNetServer;
	private static int fps = 40;
	private static Boolean blackout = false;
	public static ArtNetClient artnet;
	private static byte[][] dmxBlackout = null;

	public static void enableBlackout() {
		blackout = true;
	}

	public static void disableBlackout() {
		blackout = false;
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(Boolean shouldStop) {
		ArtNetRunnable.shouldStop = shouldStop;
	}

	public static void shouldStop() {
		shouldStop = true;
	}

	public static int getFps() {
		return fps;
	}

	public static void setFps(int fps) {
		ArtNetRunnable.fps = fps;
	}

	public static void toggleBlackout() {
		blackout = !blackout;
	}

	private void init() {
		CLIUtils.println("[ArtNet] Thread started.");
		artnet = new ArtNetClient();
		artnet.start();
		artNetServer = artnet.getArtNetServer();

		byte[][] temp_dmxData = new byte[Main.getProject().getUniverseLimit()][512];
		for (int i = 0; i < Main.getProject().getUniverseLimit(); i++) {
			for (int j = 0; j < 512; j++) {
				temp_dmxData[i][j] = (byte) 0;
			}
		}
		dmxBlackout = temp_dmxData;
		shouldStop = false;
	}

	@Override
	public void run() {
		init();
		while (!shouldStop) {

			long lastLoopTime = System.nanoTime();
			final int TARGET_FPS = 30;
			final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
			long lastFpsTime = 0;
			while (!shouldStop) {

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

				if(!Main.getSessionMode()) {
                    // Get all aditional unicast ip's and send to them:
                    for (int i = 0; i < Main.getProject().getUniverseOut().length; i++) {
                        if (Main.getProject().getUniverseOut()[i] != null) {
							UniverseOut uout = Main.getProject().getUniverseOut()[i];
							for (int j = 0; j < uout.getAddresses().size(); j++) {
								if (uout.getAddresses().get(j) != null) {
									if (blackout) {
										artnet.unicastDmx(uout.getAddresses().get(j), Main.getProject().getSubNet(), i,
												dmxBlackout[i]);
									} else {
										if (Main.getUniverseData(i) != null) {
											artnet.unicastDmx(uout.getAddresses().get(j), Main.getProject().getSubNet(), i,
													Main.getUniverseData(i));
										}
									}
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
					CLIUtils.println("ArtNet Thread: Sleep couldn't be executed due exiting the app");
				}
			}

		}
		CLIUtils.println("[ArtNet] Thread stopped.");
	}
}
