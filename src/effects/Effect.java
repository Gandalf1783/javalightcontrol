package effects;

import java.util.UUID;

public class Effect {

	public final UUID uuid = UUID.randomUUID();
	public double valueNow = 1;
	public double valueBefore = valueNow;
	public double valueTarget = 255;
	public int frames = 1;
	private Boolean finished = false;
	public int universe = 0;
	public int address = 0;
	public double faktor = 1;

	public void calculate() {
		valueNow = valueNow + ((valueTarget / valueBefore) / frames);
		frames--;
	}

	public boolean isFinished() {
		if (valueNow >= valueTarget) {
			valueNow = valueTarget;
			return true;
		}
		return false;
	}

	public double getValueNow() {
		return valueNow;
	}

	public void setValueNow(double valueNow) {
		this.valueNow = valueNow;
	}

	public double getValueBefore() {
		return valueBefore;
	}

	public void setValueBefore(double valueBefore) {
		this.valueBefore = valueBefore;
	}

	public double getValueTarget() {
		return valueTarget;
	}

	public void setValueTarget(double valueTarget) {
		this.valueTarget = valueTarget;
	}

	public int getFrames() {
		return frames;
	}

	public void setFrames(int frames) {
		this.frames = frames;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public int getUniverse() {
		return universe;
	}

	public void setUniverse(int universe) {
		this.universe = universe;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public double getFaktor() {
		return faktor;
	}

	public void setFaktor(double faktor) {
		this.faktor = faktor;
	}

	public UUID getUuid() {
		return uuid;
	}

	
	
}
