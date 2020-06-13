package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.UniverseOut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleThread implements Runnable {

	private static Boolean shouldStop = false;

	public static void runCommand(String cmd) {
		if (cmd.equalsIgnoreCase("shutdown") || cmd.equalsIgnoreCase("exit")) {
			System.out.println("[CONSOLE] Initiated Shutdown");
			Main.shutdown();
		}
		if(cmd.startsWith("help")) {
			System.out.println(" -=- HELP -=- ");
			System.out.println("save - saves this project | With GUI");
			System.out.println("load - loads last project | With GUI");
			System.out.println("project name - displays current project name | project name <newname> - sets project name to <newname>");
			System.out.println("dmx <universe> <channel> <value> - sets the corresponding value");
			System.out.println("universe - output manager for artnet");
		}

		if (cmd.startsWith("save")) {
			Main.saveProject();
		}

		if (cmd.startsWith("load")) {
			Main.loadProjectGUI();
		}

		if (cmd.startsWith("project")) {
			String[] args = cmd.split("\\s+");
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("name")) {
					System.out.println("Project is named \"" + Main.getSettings().getProjectName() + "\"");
				}
				if(args[1].equalsIgnoreCase("path")) {
					if(Main.getJLCSettings().getProject_path() != null) {
						if(Main.getJLCSettings().getProject_path().equals("")) {
							System.out.println("Project path is empty. No recent Project was found.");
						} else {
							System.out.println("Project Path is "+Main.getJLCSettings().getProject_path() +"");
						}
					}
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
			//universe out add 1 192.168.178.1
			//universe out remove 1 192.168.178.1
			//universe in add 1 192.168.178.1 -> Dont needed yet
			//universe in remove 1 192.168.178.1 -> Dont needed yet
			//universe -> just lists all the stuff
			String[] args = cmd.split("\\s+");
			if(args.length == 5) {
				if(args[1].equalsIgnoreCase("out")) {
					if(args[2].equalsIgnoreCase("add")) {
						int universe = Integer.parseInt(args[3]);
						String IP = args[4];
						System.out.println(IP + " | "+universe);
					} else if(args[2].equalsIgnoreCase("remove")) {
						int universe = Integer.parseInt(args[3]);
						String IP = args[4];
						System.out.println(IP + " | "+universe);

					}
				}
			}
		}

		if(cmd.startsWith("dmx")) {
			String[] args = cmd.split("\\s+");
			if (args.length == 4) {
				int universe = Integer.parseInt(args[1]);
				int address = Integer.parseInt(args[2]);
				int value = Integer.parseInt(args[3]);
				Main.setDmxByte((byte) value, universe, address);

			}
		}

		if (cmd.equalsIgnoreCase("test")) {
			UniverseOut uout = new UniverseOut();
			UniverseOut[] uarray = {uout, uout};
			Main.getSettings().setUniverseOut(uarray);
		}
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
				runCommand(cmd);
			} catch (IOException e) {
				System.out.println("[Console] EXCEPTION: " + e.getMessage());
			}

		}
		System.out.println("[Console] Thread stopped.");
	}

	private void init() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("[CONSOLE THREAD] Welcome!");
		System.out.println("JavaLightControl " + Main.getVersion() + " by Gandalf1783 (c) Copyright 2020");
		System.out.println("Consult \"help\" for more");
		System.out.println();
		System.out.println();
		System.out.println();
	}

}