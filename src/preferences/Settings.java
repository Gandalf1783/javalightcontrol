package preferences;

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

	private final int unicastLimit = 20;

	private UniverseOut[] universeOut = new UniverseOut[unicastLimit];

	public Settings() {

	}

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

	
	
}
