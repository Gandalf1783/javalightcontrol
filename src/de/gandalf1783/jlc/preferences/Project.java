package de.gandalf1783.jlc.preferences;

import java.io.Serializable;

public class Project implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3551438906802569304L;

	private final int universeLimit = 5;
	private final int subNet = 0;
	private final int effectLimit = 8192;
	private final int unicastLimit = 20;
	private String projectName = "unnamed-project";
	private UniverseOut[] universeOut = new UniverseOut[unicastLimit];
	private byte[][] dmxData = null;

	public Project() {
	}

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
