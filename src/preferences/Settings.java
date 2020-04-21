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

	private String projectName = "unnamed-project";

	private final int unicastLimit = 20;

	private UniverseOut[] universeOut = new UniverseOut[unicastLimit];
	private byte[][] dmxData = null;

	public Settings() {}

	public int getSubNet() {
		return subNet;
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

	public int getUniverseLimit() {
		return universeLimit;
	}
	public int getEffectLimit() {
		return effectLimit;
	}
	public int getUnicastLimit() {
		return unicastLimit;
	}
	public byte[][] getDmxData() {
		return dmxData;
	}
	public void setDmxData(byte[][] dmxData) {
		this.dmxData = dmxData;
	}
	
	public long getSerialVersionUID() {
		return serialVersionUID;
	}
}
