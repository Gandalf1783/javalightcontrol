package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleThread implements Runnable {

	private static Boolean shouldStop = false;

	private void init() {
		System.out.println("[CONSOLE THREAD] Welcome!");
		System.out.println("JavaLightControl "+Main.getVersion() +" by Gandalf1783 (c) Copyright 2020");
		System.out.println("Consult \"help\" for more");
	}

	@Override
	public void run() {
		init();
		while (!shouldStop) {
			// Enter data using BufferReader
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			// Reading data using readLine
			String cmd = "";

			try {
				cmd = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (cmd.equalsIgnoreCase("shutdown")) {
				System.out.println("[CONSOLE] Initiated Shutdown");
				Main.shutdown();
			}

			if (cmd.startsWith("save")) {
				String[] args = cmd.split("\\s+");
				if (args.length == 1) {
					Main.saveProject(Main.getSettings().getProjectName());
				} else if (args.length > 1) {
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						sb.append(args[i]);
					}
					Main.saveProject(sb.toString());
				}
			}

			if (cmd.startsWith("load")) {
				String[] args = cmd.split("\\s+");
				if (args.length == 1) {
					System.out.println(
							"No Filename provided, trying to load \"last-project.project\" from current folder.");
					try {
						Main.loadSettingsFromFile("last-project");
					} catch (IOException e) {
						System.out.println("File not found. Cant load any.");
					}
				}

				if(args.length == 2) {
					try {
						Main.loadSettingsFromFile(args[1]);
					} catch (IOException e) {
						System.out.println("File not found. Cant load any.");
					}
				}
			}

			if (cmd.startsWith("project")) {
				String[] args = cmd.split("\\s+");
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("name")) {
						System.out.println("Project is named \"" + Main.getSettings().getProjectName() + "\"");
					}
				} else if (args.length >= 3) {
					if (args[1].equalsIgnoreCase("name")) {
						StringBuilder sb = new StringBuilder();
						for (int i = 2; i < args.length; i++) {
							sb.append(args[i]);
						}
						String name = sb.toString();
						System.out.println("Project will be named \"" + name + "\"");
						Main.getSettings().setProjectName(name);
					}
				}
			}

			if (cmd.startsWith("universe")) {
				System.out.println("NOT WORKING!");
				return;

			}

			if(cmd.startsWith("dmx")) {
				String[] args = cmd.split("\\s+");
				if(args.length == 4) {
					int universe = Integer.parseInt(args[1]);
					int address = Integer.parseInt(args[2]);
					int value = Integer.parseInt(args[3]);
					Main.setDmxByte((byte) value, universe, address);

				}
			}
			
		}
		System.out.println("[Console] Thread stopped.");
	}

}