package de.gandalf1783.jlc.preferences;

import java.io.Serializable;

public class UniverseOut implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6752786453398992804L;
	
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
