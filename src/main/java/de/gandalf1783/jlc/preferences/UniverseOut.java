package de.gandalf1783.jlc.preferences;

import java.io.Serializable;
import java.util.ArrayList;

public class UniverseOut implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6752786453398992804L;

	private ArrayList<String> addresses = new ArrayList<>();

	public UniverseOut() {
	}

	public ArrayList<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<String> addresses) {
		this.addresses = addresses;
	}

	public void addAddress(String address) {
		addresses.add(address);
	}
}
