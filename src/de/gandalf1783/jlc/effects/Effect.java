package de.gandalf1783.jlc.effects;

import java.io.Serializable;

public class Effect implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 5782987786864602144L;
	
	public double valueNow = 1;
	public double valueBefore = valueNow;
	public double valueTarget = 255;

	public int frames = 1;

	public int universe = 0;
	public int address = 0;

	public Effect() {}

	public Double getValueNow() {
		return valueNow;
	}
	public void setValueNow(Double valueNow) {
		this.valueNow = valueNow;
	}
	public Double getValueBefore() {
		return valueBefore;
	}
	public void setValueBefore(Double valueBefore) {
		this.valueBefore = valueBefore;
	}
	public Double getValueTarget(){
		return valueTarget;
	}
	public void setValueTarget(Double valueTarget) {
		this.valueTarget = valueTarget;
	}
	public int getFrames() {
		return frames;
	}
	public void setFrames(int frames) {
		this.frames = frames;
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
}
