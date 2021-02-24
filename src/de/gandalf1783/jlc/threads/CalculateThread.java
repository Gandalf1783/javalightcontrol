package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.effects.Effect;
import de.gandalf1783.jlc.main.Main;

public class CalculateThread implements Runnable {

	private static final Boolean shouldStop = false;

	public static Effect[] calculatingEffects;

	public final static int TARGET_FPS = 30;

	public static void addEffect(Effect e) {
		if (e != null) {
			Effect[] temp_effects = new Effect[calculatingEffects.length + 1];
			int i = 0;
			for (Effect temp : calculatingEffects) {
				temp_effects[i] = temp;
				i++;
			}
			temp_effects[i] = e;
			calculatingEffects = temp_effects;
			CLIUtils.println("LENGTH OF NEW ARRAY IS " + temp_effects.length);
		}
	}

	public static Effect[] getCalculatingEffects() {
		return calculatingEffects;
	}

	public boolean isFinished(Effect e) {
		if (e.getValueNow() >= e.getValueTarget()) {
			e.setValueNow(e.getValueTarget());
			return true;
		}
		return false;
	}

	public void calculate(Effect e) {
		e.setValueNow(e.getValueNow() + ((e.getValueTarget() / e.getValueBefore()) / e.getFrames()));
		int frames = e.getFrames();
		frames--;
		e.setFrames(frames);
	}

	private void init() {
		calculatingEffects = new Effect[0];
		CLIUtils.println("[Calculation] Thread started.");
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
						calculate(e1);
						Main.setDmxByte((byte) e1.valueNow, e1.universe, e1.address);
						if (isFinished(e1)) {
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
					CLIUtils.println("CalculateNet Thread: Sleep couldn't be executed due exiting the app");
				}
			}

		}
		CLIUtils.println("[Calculation] Thread stopped.");
	}

}