package de.gandalf1783.jlc.preferences;

import java.io.Serializable;

public class UniverseOut implements Serializable {

	/**
	 * 
	 */
	private final long serialVersionUID = -1326425548448853224L;

	private String[] ip = new String[10];

	public UniverseOut() {}


	public String[] getIP() {
		return ip;
	}

	public String getIP(int i) {
		return ip[i];
	}

	public void setIP(String[] ip) {
		this.ip = ip;
	}

	public void setIP(int i, String ip) {
		this.ip[i] = ip;
	}
}
