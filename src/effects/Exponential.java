package effects;

import java.util.UUID;

public class Exponential {

	public static final UUID uuid = UUID.randomUUID();
	public static double valueNow = 1;
	public static double valueTarget = 255;
	public static int frames = 240;
	private static Boolean finished = false;
	public static int universe = 0;
	public static int address = 1;
	public static double faktor = 0;
	
	public static double getValueNow() {
		return valueNow;
	}

	public static void setValueNow(int valueNow) {
		Exponential.valueNow = valueNow;
	}

	public static double  getValueTarget() {
		return valueTarget;
	}

	public static void setValueTarget(int valueTarget) {
		Exponential.valueTarget = valueTarget;
	}

	public static int getFrames() {
		return frames;
	}

	public static void setFrames(int frames) {
		Exponential.frames = frames;
	}

	public static int getUniverse() {
		return universe;
	}

	public static void setUniverse(int universe) {
		Exponential.universe = universe;
	}

	public static int getAddress() {
		return address;
	}

	public static void setAddress(int address) {
		Exponential.address = address;
	}

	public void calculate() {
		System.out.println("Calculate: \n Target: "+valueTarget +" Now: "+valueNow +" Left frames: "+frames);
		faktor = ((valueTarget/valueNow)/frames);
		frames--;
		valueNow = (valueNow*faktor);
		System.out.println("Calculation done. Now: "+valueNow);
	}

	public boolean isFinished() {
		System.out.println("Checking if finished");
		if (frames == 0) {
			System.out.println("Has finished.");
			return true;
		}
		System.out.println("Isnt finished. ");
		return false;
	}

	public static Boolean getFinished() {
		return finished;
	}

	public static void setFinished(Boolean finished) {
		Exponential.finished = finished;
	}

}
