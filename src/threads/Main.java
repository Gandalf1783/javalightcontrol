package threads;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import effects.Effect;
import preferences.Settings;
import preferences.UniverseOut;

public class Main {

	private static byte[][] dmxData = null;

	public static final String VERSION = "1.3-DEV";
	public static final String NET_VERSION = "0.0-DEV";
	//Setting up Threads
	private static ArtNetThread artNetThread = new ArtNetThread();
	private static WindowThread windowThread = new WindowThread();
	private static ConsoleThread consoleThread = new ConsoleThread();
	private static CalculateThread calculateThread = new CalculateThread();
	private static SessionThread sessionThread = new SessionThread();

	//Creating Threads from Runnables
	private static Thread artnetRunnable = new Thread(artNetThread);
	private static Thread windowRunnable = new Thread(windowThread);
	private static Thread consoleRunnable = new Thread(consoleThread);
	private static Thread calculateRunnable = new Thread(calculateThread);
	private static Thread sessionRunnable = new Thread(sessionThread);

	private static Settings settings;

	public static void main(String[] args) {

		System.out.println("[JLC] Starting programm...");
		System.out.println("[JLC] Loading Settings");
		loadSettings();
		System.out.println("[JLC] Initialising & Starting Threads...");
		consoleRunnable.start();
		artnetRunnable.start();
		windowRunnable.start();
		sessionRunnable.start();
		calculateRunnable.start();
		System.out.println("[JLC] System started.");
	}

	private static void loadSettings() {
		settings = new Settings();
		String[] ip = {"127.0.0.1"};

		// Setting Outputs for ArtNet
		UniverseOut uni0 = new UniverseOut();
		UniverseOut uni1 = new UniverseOut();


		uni0.setIP(ip);
		uni1.setIP(ip);
		UniverseOut[] uniArray = {uni0, uni1};

		// Setting the Universes into settings
		settings.setUniverseOut(uniArray);
		// applying settings
		setSettings(settings);

		// creating empty dmxData (with max-universes supported and 512 channels per
		// universe)
		byte[][] temp_dmxData = new byte[settings.getUniverseLimit()][512];
		for (int i = 0; i < settings.getUniverseLimit(); i++) {
			for (int j = 0; j < 512; j++) {
				temp_dmxData[i][j] = (byte) 255;
			}
		}
		setDmxData(temp_dmxData);
		System.out.println("[JLC] Settings loaded.");
		System.out.println("Searching for Project-Files");
		try {
			loadSettingsFromFile("last-project");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error while looking for project: " + e.getMessage());
		}

		System.out.println("Setting up Effects...");

	}

	public static void loadSettingsFromFile(String name) throws IOException {
		String path = System.getProperty("user.dir");
		FileInputStream fis = new FileInputStream(path + "\\" + name + ".project");
		XMLDecoder xml = new XMLDecoder(fis);

		System.out.println("PATH: " + path + "\\" + name + ".project");

		System.out.println("The Object has been read from the file");
		Settings temp_settings;
		temp_settings = (Settings) xml.readObject();
		xml.close();
		System.out.println("SETTINGS: Proj-Name: " + temp_settings.getProjectName());
		Main.setSettings(temp_settings);
		if(Main.getSettings().getDmxData() != null) {
			Main.dmxData = Main.getSettings().getDmxData();
		}
	}

	public static void shutdown() {
		System.out.println("[JLC] Terminating Threads...");
		windowRunnable.interrupt();
		calculateRunnable.interrupt();
		windowRunnable.interrupt();
		sessionRunnable.interrupt();

		saveProject(settings.getProjectName());

		byte[][] temp_dmxData = new byte[15][512];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 512; j++) {
				temp_dmxData[i][j] = (byte) 0;
			}
		}

		Main.setDmxData(temp_dmxData);

		artnetRunnable.interrupt();
		System.out.println("[JLC] Program shutdown");
		System.exit(0);
	}

	public static void setDmxByte(byte value, int universe, int address) {
		if (!(universe > settings.getUniverseLimit()) && !(universe < 0)) {
			if (address <= 512 && !(address < 0)) {
				byte bytes = value;
				dmxData[universe][address] = value;
			} else {
				System.out.println("{setDmxByte} Address \"" + address + "\" to high/low!");
			}
		} else {
			System.out.println("{setDmxByte} Universe too high/low!");
		}
	}

	public static void saveProject(String projectname) {
		System.out.println("Saving Project...");
		System.out.println("Passed Projectname: " + projectname + " Settings-Name: " + settings.getProjectName());
		if (projectname == null || projectname.equalsIgnoreCase("")) {
			System.out.println("Is null or doesnt contain anything");
			projectname = "last-project";
		}
		if (projectname.contains("\\s+")) {
			System.out.println("Replacing wrong characters");
			projectname.replace("\\s+", "-");
		}
		Effect[] effects = calculateThread.getCalculatingEffects();
		Main.getSettings().setRunningEffects(effects);
		Main.getSettings().setDmxData(dmxData);

		try {

			String path = System.getProperty("user.dir");
			FileOutputStream fos = new FileOutputStream(path + "\\" + projectname + ".project");
			XMLEncoder xml = new XMLEncoder(fos);
			xml.setExceptionListener(new ExceptionListener() {
				public void exceptionThrown(Exception e) {
					System.out.println("Exception! :" + e.toString());
				}
			});
			xml.writeObject(getSettings());
			xml.close();
			fos.close();
			fos = new FileOutputStream(path + "\\last-project.project");
			xml = new XMLEncoder(fos);
			xml.writeObject(getSettings());
			xml.close();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Project could not be saved: " + e.getMessage());
			return;
		}
		System.out.println("Project was saved.");
	}

	public static byte[][] getDmxData() {
		return dmxData;
	}

	public static byte[] getUniverseData(int universe) {
		if (universe > 15) {
			return null;
		}
		return dmxData[universe];
	}

	public static void setDmxData(byte[][] dmxData) {
		Main.dmxData = dmxData;
	}

	public static Settings getSettings() {
		return settings;
	}

	public static void setSettings(Settings settings) {
		Main.settings = settings;
	}

	public static String getVersion() {
		return VERSION;
	}

	public static void print(String s) {
		System.out.println("[JLC] " + s);
	}

	public static void notify(String s) {
		print(s);
		// BROADCAST TO SESSION HERE
	}

}
