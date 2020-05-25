package de.gandalf1783.jlc.main;

import java.awt.*;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

import de.gandalf1783.jlc.effects.Effect;
import de.gandalf1783.jlc.preferences.JLCSettings;
import de.gandalf1783.jlc.preferences.Settings;
import de.gandalf1783.jlc.preferences.UniverseOut;
import de.gandalf1783.jlc.threads.*;

import javax.swing.*;

public class Main {

	private static byte[][] dmxData = null;

	public static final String VERSION = "DEV-1.3";
	public static final String NET_VERSION = "DEV-1.1";
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
	private static JLCSettings jlcSettings;
	private static Boolean isProjectLoaded = false;
	private static Boolean sessionMode = false;

	public static void main(String[] args) {
		System.out.println("[JLC] Starting JLC...");
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
	    //Preparing SETTINGS Object, if none is found.
		settings = new Settings();
		String[] ip = {"127.0.0.1", "192.168.178.255"};

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

		//Preparing JLCSETTINGS Object, if none is found.
        jlcSettings = new JLCSettings();

        jlcSettings.setProject_path("");
        jlcSettings.setVersion(Main.getVersion());
		//jlcSettings.setSystemUUID(UUID.randomUUID());
		loadJLCSettings();
		saveJLCSettings();



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

		if(!Main.getJLCSettings().getProject_path().equals("")) {
			try {
				loadSettingsFromFile(Main.getJLCSettings().getProject_path());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("[JLC] No recent project found.");
		}

		System.out.println("Setting up Effects...");

	}

	public static void loadSettingsFromFile(String path) throws IOException {
		System.out.println("Loading from Path: "+path);
		FileInputStream fis = new FileInputStream(path);
		XMLDecoder xml = new XMLDecoder(fis);
		Settings temp_settings;
		temp_settings = (Settings) xml.readObject();
		xml.close();
		Main.setSettings(temp_settings);
		if(Main.getSettings().getDmxData() != null) {
			Main.dmxData = Main.getSettings().getDmxData();
		}
		Main.isProjectLoaded = true;
		System.out.println("Loaded.");
	}
	public static void loadProjectGUI() {
		JFrame parentFrame = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(parentFrame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				loadSettingsFromFile(selectedFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Utils.displayPopup("Opening Project", "Could not load the Project.");
			System.out.println("[ERROR] No file specified.");
		}

	}
	public static void shutdown() {
		sessionThread.destroySession();
		System.out.println("[JLC] Terminating Threads...");
		windowRunnable.interrupt();
		calculateRunnable.interrupt();
		windowRunnable.interrupt();
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

	public static void saveProject() {
        if(!Main.getJLCSettings().getProject_path().equalsIgnoreCase("")) {
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
			System.out.println("[PROJECT] Saving in progress....");
			File fileToSave = fileChooser.getSelectedFile();
			String filePath = fileToSave.getAbsolutePath();
			if(!fileToSave.getAbsolutePath().endsWith(".project")) {
				filePath = fileToSave.getAbsolutePath()+".project";
			}
			System.out.println("[PROJECT] " + filePath);

			try {
				Effect[] effects = calculateThread.getCalculatingEffects();
				Main.getSettings().setDmxData(dmxData);
				Main.getJLCSettings().setProject_path(filePath);
				FileOutputStream fos = new FileOutputStream(filePath);
				XMLEncoder xml = new XMLEncoder(fos);
				xml.setExceptionListener(new ExceptionListener() {
					public void exceptionThrown(Exception e) {
						System.out.println("Exception! :" + e.toString());
					}
				});
				xml.writeObject(getSettings());
				xml.close();
				fos.close();

				loadSettingsFromFile(filePath);
			} catch (IOException e) {
				System.out.println("Project could not be saved: " + e.getMessage());
				Main.getJLCSettings().setProject_path("");
				return null;
			}
		} else {
			Utils.displayPopup("Saving Error", "Could not save the Project.");
			System.out.println("[ERROR] Could not save!");
		}
		return null;
	}

	private static void saveProjectHandler() {
	        String projectname = Main.getSettings().getProjectName();
			System.out.println("Saving Project...");
			if (projectname == null || projectname.equalsIgnoreCase("")) {
				projectname = "last-project";
			}
			if (projectname.contains("\\s+")) {
				projectname.replace("\\s+", "-");
			}
			Effect[] effects = calculateThread.getCalculatingEffects();
			Main.getSettings().setDmxData(dmxData);


			try {
				String path = System.getProperty("user.dir");
				path = path+"\\"+projectname+".project";
				if(!Main.getJLCSettings().getProject_path().equals("")) {
					path = Main.getJLCSettings().getProject_path();
				}
				System.out.println("Saving to "+path);
				Main.getJLCSettings().setProject_path(path);
				FileOutputStream fos = new FileOutputStream(path);
				XMLEncoder xml = new XMLEncoder(fos);
				xml.setExceptionListener(new ExceptionListener() {
					public void exceptionThrown(Exception e) {
						System.out.println("Exception! :" + e.toString());
					}
				});
				xml.writeObject(getSettings());
				xml.close();
				fos.close();

			} catch (IOException e) {
				System.out.println("Project could not be saved: " + e.getMessage());
				return;
			}
			System.out.println("Project was saved.");
	}

    public static void saveJLCSettings() {
        try {
            String path = System.getProperty("user.dir");
            FileOutputStream fos = new FileOutputStream(path + "\\" + "settings" + ".properties");
            XMLEncoder xml = new XMLEncoder(fos);
            xml.writeObject(Main.getJLCSettings());
            xml.close();
            fos.close();
            System.out.println("JLC Settings saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadJLCSettings() {
        try {
        String path = System.getProperty("user.dir");
        path = path+"\\settings.properties";
        FileInputStream fis = null;

        fis = new FileInputStream(path);

        XMLDecoder xml = new XMLDecoder(fis);
        System.out.println("Loading...: " + path);
        JLCSettings temp_settings;
        temp_settings = (JLCSettings) xml.readObject();
        xml.close();
        fis.close();
        setJLCSettings(temp_settings);
        if(Main.getSettings().getDmxData() != null) {
            Main.dmxData = Main.getSettings().getDmxData();
        }
        System.out.println("Loaded.");
        Main.isProjectLoaded = true;
        } catch (FileNotFoundException e) {
            System.out.println("EXCEPTION: "+e.getMessage());
        } catch (IOException e) {
            System.out.println("EXCEPTION: "+e.getMessage());
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
	    System.out.println("[JLC] "+s);
        Thread t = new Thread(() -> {
            JDialog jDialog = new JDialog();
            jDialog.setSize(200, 200);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - jDialog.getWidth()) / 2;
            int y = (screenSize.height - jDialog.getHeight()) / 2;
            jDialog.setLocation(x, y);
            jDialog.setTitle("Notify");
            jDialog.setSize(200,200);
            jDialog.setModal(true);
            jDialog.add(new JLabel(s, SwingConstants.CENTER));
            jDialog.setVisible(true);
        });
        t.start();
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
	public static SessionThread getSessionThread() {
		return sessionThread;
	}
}
