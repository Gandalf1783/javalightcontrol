package preferences;

import effects.Effect;

import java.io.Serializable;

public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2929431155275389116L;
	private final int universeLimit = 15;
	private int subNet = 0;
	private final int effectLimit = 8192;

	private String projectName = "last-project";
	private Boolean restoreLastValues = true;

	private final int unicastLimit = 20;

	private UniverseOut[] universeOut = new UniverseOut[unicastLimit];
	private Effect[] effects = new Effect[effectLimit];
	private Effect[] runningEffects = new Effect[effectLimit];
	private byte[][] dmxData = null;

	public Settings() {}

	public int getSubNet() {
		return subNet;
	}

	public void setSubNet(int subNet) {
		this.subNet = subNet;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public UniverseOut[] getUniverseOut() {
		return universeOut;
	}

	public void setUniverseOut(UniverseOut[] universeOut) {
		this.universeOut = universeOut;
	}
	
	public void setUniverseOutElement(int index, UniverseOut uout) {
		try {
			this.universeOut[index] = uout;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	public UniverseOut getUniverseOutElemt(int index) {
		try {
			return this.universeOut[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getUniverseLimit() {
		return universeLimit;
	}

	public int getEffectLimit() {
		return effectLimit;
	}

	public int getUnicastLimit() {
		return unicastLimit;
	}

	public Effect[] getEffects() {
		return this.effects;
	}
	public Effect getEffects(int i) {
		return this.effects[i];
	}
	public void setEffects(Effect[] effects) {
		this.effects = effects;
	}
	public void setEffects(int i, Effect effect) {
		this.effects[i] = effect;
	}
	public Effect[] getRunningEffects() {
		return this.runningEffects;
	}
	public Effect getRunningEffects(int i) {
		return this.runningEffects[i];
	}
	public void setRunningEffects(int i, Effect effect) {
		this.runningEffects[i] = effect;
	}
	public void setRunningEffects(Effect[] effects) {
		this.runningEffects = effects;
	}
	public byte[][] getDmxData() {
		return dmxData;
	}
	public void setDmxData(byte[][] dmxData) {
		this.dmxData = dmxData;
	}
	public Boolean getRestoreLastValues() {
		return true;
	}
	public void setRestoreLastValues(Boolean restoreLastValues) {
		this.restoreLastValues = restoreLastValues;
	}
}
