package de.gandalf1783.jlc.main;

import de.gandalf1783.jlc.preferences.JLCSettings;
import de.gandalf1783.jlc.preferences.Project;
import de.gandalf1783.jlc.preferences.UniverseOut;
import de.gandalf1783.jlc.threads.*;
import org.jline.reader.LineReader;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.terminal.Terminal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.sql.Timestamp;

public class Main {

	private static byte[][] dmxData = null;

	public static final String VERSION = "BETA-1.2.3";
	public static final String NET_VERSION = "DEV-1.1";
	// Setting up Threads

	private static final ArtNetThread artNetThread = new ArtNetThread();
	private static final WindowThread windowThread = new WindowThread();
	private static final ConsoleThread consoleThread = new ConsoleThread();
	private static final CalculateThread calculateThread = new CalculateThread();
	private static final SessionThread sessionThread = new SessionThread();

	// Creating Threads from Runnables


	/*
	 * Classes & Vars for Command Line Interface
	 */
	private static Terminal terminal;
	private static ConsoleThread conRunnable;
	private static Thread conThread;
	private static LineReader lineReader;
	private static AggregateCompleter aggregateCompleter;
	private static ArgumentCompleter argumentCompleter;
	private static String consolePrompt;

	private static final Thread artnetRunnable = new Thread(artNetThread);
	private static final Thread windowRunnable = new Thread(windowThread);
	private static Thread consoleRunnable;
	private static final Thread calculateRunnable = new Thread(calculateThread);
	private static final Thread sessionRunnable = new Thread(sessionThread);

	private static Project project;
	private static JLCSettings jlcSettings;
	private static Boolean sessionMode = false;

	public static void main(String[] args) {
		CLIUtils.println("[JLC] Starting JLC...");
		CLIUtils.println("[JLC] Loading Settings");

		prepareWorkflow();

		CLIUtils.println("[JLC] Initialising & Starting Threads...");

		conRunnable = new ConsoleThread();

		consoleRunnable = new Thread(conRunnable);

		consoleRunnable.start();
		artnetRunnable.start();
		sessionRunnable.start();
		calculateRunnable.start();
		windowRunnable.start();
		CLIUtils.println("[JLC] System started.");
	}

	private static void prepareWorkflow() {
		Main.getWindowThread().setStatus("Loading JLC Settings...");
		// Preparing SETTINGS Object, if none is found.
		project = new Project();


		// Setting Outputs for ArtNet
		UniverseOut uni0 = new UniverseOut();
		uni0.addAddress("192.168.178.255");
		UniverseOut[] uniArray = {uni0};

		// Setting the Universes into settings
		project.setUniverseOut(uniArray);
		// applying settings
		setProject(project);


		// Preparing JLCSETTINGS Object, if none is found.
		jlcSettings = new JLCSettings();

		jlcSettings.setProject_path("");
		jlcSettings.setVersion(Main.getVersion());
		loadJLCSettings();

		if (!jlcSettings.getVersion().equalsIgnoreCase(Main.getVersion())) {
			CLIUtils.println("");
			Utils.displayPopup("Warning", "You seem to updated JLC. Please backup everything before continuing.", 500, 100);
			Utils.displayPopup("WARNING", "THIS VERSION OF JLC IS INCOMPATIBLE WITH OLDER PROJECT FILES! DO NOT USE THEM!", 500, 100);
			jlcSettings.setVersion(Main.getVersion());
		}


		saveJLCSettings();

		// creating empty dmxData (with max-universes supported and 512 channels per
		// universe)
		byte[][] temp_dmxData = new byte[project.getUniverseLimit()][512];
		for (int i = 0; i < project.getUniverseLimit(); i++) {
			for (int j = 0; j < 512; j++) {
				temp_dmxData[i][j] = (byte) 0;
			}
		}
		setDmxData(temp_dmxData);

		CLIUtils.println("[JLC] Settings loaded.");
		CLIUtils.println("Searching for Project-Files");

		if (!Main.getJLCSettings().getProject_path().equals("")) {
			try {
				loadProjectFromFile(Main.getJLCSettings().getProject_path());
			} catch (IOException e) {
				CLIUtils.println("[IO EXCEPTION]");
			}
		} else {
			Main.getWindowThread().setStatus("No recent project was found.");
			CLIUtils.println("[JLC] No recent project found.");
		}
	}

	public static void loadProjectFromFile(String path) throws IOException {
		Main.getWindowThread().setStatus("Loading Project...");
		CLIUtils.println("Loading from Path: " + path);
		FileInputStream fis = new FileInputStream(path);
		XMLDecoder xml = new XMLDecoder(fis);
		Project temp_project;
		temp_project = (Project) xml.readObject();
		xml.close();
		Main.setProject(temp_project);
		if (Main.getProject().getDmxData() != null) {
			Main.dmxData = Main.getProject().getDmxData();
		}
		CLIUtils.println("Loaded.");
		Main.getWindowThread().setStatus("Project \"" + Main.getProject().getProjectName() + "\" loaded.");
	}

	public static void loadProjectGUI() {
		JFrame parentFrame = new JFrame();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(parentFrame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				loadProjectFromFile(selectedFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Main.getWindowThread().setStatus("Could not load the Project.");
			Utils.displayPopup("Opening Project", "Could not load the Project.");
			CLIUtils.println("[ERROR] No file specified.");
		}

	}

	public static void shutdown() {
		Main.getWindowThread().setStatus("Shutting down...");
		SessionThread.destroySession();
		CLIUtils.println("[JLC] Terminating Threads...");
		calculateRunnable.interrupt();
		sessionRunnable.interrupt();
		saveProject();
		saveJLCSettings();
		byte[][] temp_dmxData = new byte[15][512];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 512; j++) {
				temp_dmxData[i][j] = (byte) 0;
			}
		}

		Main.setDmxData(temp_dmxData);
		artnetRunnable.interrupt();
		CLIUtils.println("[JLC] Program shutdown");
		System.exit(0);
	}

	public static void setDmxByte(byte value, int universe, int address) {
		if (universe < project.getUniverseLimit() && !(universe < 0)) {
			if (address <= 512 && !(address < 0)) {
				dmxData[universe][address] = value;
			} else {
				Main.getWindowThread().setStatus("{setDmxByte} : The address \"+" + address + "\" you try to set is too high/low!");
				CLIUtils.println("{setDmxByte} Address \"" + address + "\" too high/low!");
			}
		}
	}

	public static void saveProject() {
		if (!Main.getJLCSettings().getProject_path().equalsIgnoreCase("")) {
			saveProjectHandler();
		} else {
			saveProjectHandlerGUI();
		}
	}

	public static String saveProjectHandlerGUI() {
		JFrame parentFrame = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");

		int userSelection = fileChooser.showSaveDialog(parentFrame);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			CLIUtils.println("[PROJECT] Saving in progress....");
			File fileToSave = fileChooser.getSelectedFile();
			String filePath = fileToSave.getAbsolutePath();
			if (!fileToSave.getAbsolutePath().endsWith(".project")) {
				filePath = fileToSave.getAbsolutePath() + ".project";
			}
			CLIUtils.println("[PROJECT] " + filePath);

			try {
				Main.getProject().setDmxData(dmxData);
				Main.getJLCSettings().setProject_path(filePath);
				FileOutputStream fos = new FileOutputStream(filePath);
				XMLEncoder xml = new XMLEncoder(fos);
				xml.setExceptionListener(new ExceptionListener() {
					public void exceptionThrown(Exception e) {
						CLIUtils.println("Exception! :" + e.toString());
					}
				});
				xml.writeObject(getProject());
				xml.close();
				fos.close();
				jlcSettings.setLatest_save(new Timestamp(System.currentTimeMillis()));
				saveJLCSettings();
			} catch (IOException e) {
				Main.getWindowThread().setStatus("Project could not be saved");
				CLIUtils.println("Project could not be saved: " + e.getMessage());
				Main.getJLCSettings().setProject_path("");
				return null;
			}
		} else {
			Main.getWindowThread().setStatus("Project could not be saved");
			Utils.displayPopup("Saving Error", "Could not save the Project.");
			CLIUtils.println("[ERROR] Could not save!");
		}
		return null;
	}

	private static void saveProjectHandler() {
		Main.getWindowThread().setStatus("Saving Project...");
		CLIUtils.println("Saving Project...");
		String projectname = Main.getProject().getProjectName();
		if (projectname == null || projectname.equalsIgnoreCase("")) {
			projectname = "last-project";
		}
		if (projectname.contains("\\s+")) {
			projectname.replace("\\s+", "-");
		}
		Main.getProject().setDmxData(dmxData);

		try {
			String path = System.getProperty("user.dir");
			path = path + "\\" + projectname + ".project";
			if (!Main.getJLCSettings().getProject_path().equals("")) {
				path = Main.getJLCSettings().getProject_path();
			}
			CLIUtils.println("Saving to " + path);
			Main.getJLCSettings().setProject_path(path);
			FileOutputStream fos = new FileOutputStream(path);
			XMLEncoder xml = new XMLEncoder(fos);
			xml.setExceptionListener(new ExceptionListener() {
				public void exceptionThrown(Exception e) {
					CLIUtils.println("Exception! :" + e.toString());
				}
			});
			xml.writeObject(getProject());
			xml.close();
			fos.close();
			Main.getWindowThread().setStatus("Project has been saved.");
			jlcSettings.setLatest_save(new Timestamp(System.currentTimeMillis()));
			saveJLCSettings();
		} catch (IOException e) {
			Main.getWindowThread().setStatus("Error while saving Project");
			CLIUtils.println("Project could not be saved: " + e.getMessage());
			return;
		}
		CLIUtils.println("Project was saved.");
	}

	public static void saveJLCSettings() {
		try {
			String path = System.getProperty("user.dir");
			FileOutputStream fos = new FileOutputStream(path + "\\" + "settings" + ".properties");
			XMLEncoder xml = new XMLEncoder(fos);
			xml.writeObject(Main.getJLCSettings());
			xml.close();
			fos.close();
			CLIUtils.println("JLC Settings saved");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadJLCSettings() {
		try {
			String path = System.getProperty("user.dir");
			path = path + "\\settings.properties";
			FileInputStream fis = null;

			fis = new FileInputStream(path);

			XMLDecoder xml = new XMLDecoder(fis);
			CLIUtils.println("Loading...: " + path);
			JLCSettings temp_settings;
			temp_settings = (JLCSettings) xml.readObject();
			xml.close();
			fis.close();
			setJLCSettings(temp_settings);
			if (Main.getProject().getDmxData() != null) {
				Main.dmxData = Main.getProject().getDmxData();
			}
			CLIUtils.println("Loaded.");
		} catch (FileNotFoundException e) {
			CLIUtils.println("EXCEPTION: " + e.getMessage());
		} catch (IOException e) {
			CLIUtils.println("EXCEPTION: " + e.getMessage());
		}
	}

	public static JLCSettings getJLCSettings() {
		return jlcSettings;
	}

	public static void setJLCSettings(JLCSettings settings) {
		Main.jlcSettings = settings;
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

	public static Project getProject() {
		return project;
	}

	public static void setProject(Project project) {
		Main.project = project;
	}

	public static String getVersion() {
		return VERSION;
	}

	public static void print(String s) {
		CLIUtils.println("[JLC] " + s);
	}

	public static void notify(String s) {
		CLIUtils.println("[NOTIFY] - " + s);
	}

	public static void setSessionMode(Boolean state) {
		sessionMode = state;
	}

	public static Boolean getSessionMode() {
		return sessionMode;
	}

	public static Runnable getSessionRunnable() {
		return sessionRunnable;
	}

	public static WindowThread getWindowThread() {
		return windowThread;
	}

	public static SessionThread getSessionThread() {
		return sessionThread;
	}

	public static CalculateThread getCalculateThread() {
		return calculateThread;
	}

	public static ArtNetThread getArtNetThread() {
		return artNetThread;
	}

	public static BufferedImage loadImage(String path) {
		try {
			CLIUtils.println(path);
			return ImageIO.read(Main.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static Terminal getTerminal() {
		return terminal;
	}

	public static void setTerminal(Terminal terminal) {
		Main.terminal = terminal;
	}

	public static LineReader getLineReader() {
		return lineReader;
	}

	public static void setLineReader(LineReader lineReader) {
		Main.lineReader = lineReader;
	}

	public static AggregateCompleter getAggregateCompleter() {
		return aggregateCompleter;
	}

	public static void setAggregateCompleter(AggregateCompleter aggregateCompleter) {
		aggregateCompleter = aggregateCompleter;
	}

	public static ArgumentCompleter getArgumentCompleter() {
		return argumentCompleter;
	}

	public static void setArgumentCompleter(ArgumentCompleter argumentCompleter) {
		argumentCompleter = argumentCompleter;
	}

	public static String getConsolePrompt() {
		return consolePrompt;
	}

	public static void setConsolePrompt(String consolePrompt) {
		Main.consolePrompt = consolePrompt;
	}
}
