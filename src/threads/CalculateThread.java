package threads;

import effects.Effect;
import main.Main;

public class CalculateThread implements Runnable {

	private static Boolean shouldStop = false;

	public static Effect[] calculatingEffects;
	public final static int TARGET_FPS = 120;

	private void init() {
		calculatingEffects = new Effect[Main.getSettings().getEffectLimit()];
		System.out.println("[Calculation] Thread started.");
	}

	public static Effect[] getCalculatingEffects() {
		return calculatingEffects;
	}

	public static void setCalculatingEffects(Effect[] calculatingEffects) {
		CalculateThread.calculatingEffects = calculatingEffects;
	}

	@Override
	public void run() {
		init();

		while (!shouldStop) {
			long lastLoopTime = System.nanoTime();

			final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
			long lastFpsTime = 0;
			while (true) {

				long now = System.nanoTime();
				long updateLength = now - lastLoopTime;
				lastLoopTime = now;
				lastFpsTime += updateLength;
				if (lastFpsTime >= 1000000000) {
					lastFpsTime = 0;
				}

				for (Effect e1 : calculatingEffects) {
					if (e1 != null) {
						e1.calculate();
						Main.setDmxByte((byte) e1.valueNow, e1.universe, e1.address);
						if (e1.isFinished()) {
							stopEffect(getIndex(e1));
						}
					}
				}

				try {
					long timeout = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
					if (timeout <= 0) {
						timeout = 10;
					}
					Thread.sleep(timeout);
				} catch (InterruptedException e1) {
					System.out.println("CalculateNet Thread: Sleep couldn't be executed due exiting the app");
				}
			}

		}
		System.out.println("[Calculation] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(Boolean shouldStop) {
		CalculateThread.shouldStop = shouldStop;
	}

	public static void stopEffect(int index) {
		Effect[] temp_effect = new Effect[calculatingEffects.length];
		for (int i = 0; i < temp_effect.length; i++) {
			if (i != index) {
				temp_effect[i] = calculatingEffects[i];
			}
		}
		calculatingEffects = temp_effect;
	}

	public static int getIndex(Effect e) {
		for (int i = 0; i < calculatingEffects.length; i++) {
			if (calculatingEffects[i].equals(e)) {
				return i;
			}
		}
		return 0;
	}
	
	public static void addEffect(Effect e) {
		if(e != null) {
			Effect[] temp_effect = new Effect[calculatingEffects.length];
			for (int i = 0; i < temp_effect.length; i++) {
					temp_effect[i] = calculatingEffects[i];
			}
			temp_effect[calculatingEffects.length] = e;
			calculatingEffects = temp_effect;
		}
	}

	public static int getFPS() {
		return TARGET_FPS;
	}
}