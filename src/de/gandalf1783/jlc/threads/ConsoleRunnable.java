package de.gandalf1783.jlc.threads;

import de.gandalf1783.jlc.commands.*;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.UniverseOut;
import org.fusesource.jansi.Ansi;
import org.jline.builtins.Completers;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import static org.jline.builtins.Completers.TreeCompleter.node;

public class ConsoleRunnable implements Runnable {

	private static final Boolean shouldStop = false;
	private static final HashMap<String, Command> cmdMap = new HashMap<>();

	//TODO: Transfer this Command Structure via the COMMAND Interface!
	public static void runCommand(String cmd) {
		if (cmd.equalsIgnoreCase("shutdown") || cmd.equalsIgnoreCase("exit")) {
			CLIUtils.println("[CONSOLE] Initiated Shutdown");
			Main.shutdown();
		}
		if (cmd.startsWith("help")) {
			CLIUtils.println(" -=- HELP -=- ");
			CLIUtils.println("save - saves this project | With GUI");
			CLIUtils.println("load - loads last project | With GUI");
			CLIUtils.println("project name - displays current project name | project name <newname> - sets project name to <newname>");
			CLIUtils.println("dmx <universe> <channel> <value> - sets the corresponding value");
			CLIUtils.println("universe - output manager for artnet");
		}

		if (cmd.startsWith("save")) {
			Main.saveProject(Main.getJLCSettings().getProject_path());
		}

		if (cmd.startsWith("load")) {
			Main.loadProjectGUI();
		}

		if (cmd.startsWith("project")) {
			String[] args = cmd.split("\\s+");
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("name")) {
					CLIUtils.println("Project is named \"" + Main.getProject().getProjectName() + "\"");
				}
				if(args[1].equalsIgnoreCase("path")) {
					if(Main.getJLCSettings().getProject_path() != null) {
						if(Main.getJLCSettings().getProject_path().equals("")) {
							CLIUtils.println("Project path is empty. No recent Project was found.");
						} else {
							CLIUtils.println("Project Path is " + Main.getJLCSettings().getProject_path() + "");
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
					CLIUtils.println("Project will be named \"" + name + "\"");
					Main.getProject().setProjectName(name);
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
						CLIUtils.println(IP + " | " + universe);
					} else if(args[2].equalsIgnoreCase("remove")) {
						int universe = Integer.parseInt(args[3]);
						String IP = args[4];
						CLIUtils.println(IP + " | " + universe);

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
			Main.getProject().setUniverseOut(uarray);
		}
	}

	public static void init() {
		try {
			AggregateCompleter completer = new AggregateCompleter(new Completers.TreeCompleter(
					node("help"),
					node("version"),
					node("load"),
					node("save"),
					node("saveas"),
					node("universe"),
					node("project",
							node("path"),
							node("name"),
							node("name", "<name>")
					)
			));

			Main.setTerminal(TerminalBuilder.builder().system(true).dumb(true).encoding(StandardCharsets.UTF_8).name("Terminal").jna(true).jansi(true).build());
			Main.setLineReader(LineReaderBuilder.builder().terminal(Main.getTerminal()).completer(completer).build());
			Main.setConsolePrompt(Ansi.ansi().eraseScreen().fg(Ansi.Color.BLUE).bold().a("JLC").fgBright(Ansi.Color.BLACK).bold().a(" » ").reset().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		cmdMap.put("help", new HelpCommand());
		cmdMap.put("version", new VersionCommand());
		cmdMap.put("load", new LoadCommand());
		cmdMap.put("project", new ProjectCommand());
		cmdMap.put("saveas", new SaveAsCommand());
		cmdMap.put("save", new SaveCommand());
		cmdMap.put("universe", new UniverseCommand());
		cmdMap.put("exit", new ExitCommand());

		CLIUtils.println("[CONSOLE THREAD] INIT DONE");
	}

	@Override
	public void run() {
		init();
		while (!shouldStop) {
			try {
				String command = Main.getLineReader().readLine(Main.getConsolePrompt());
				String[] data = command.split("\\s+");
				String[] args = Arrays.copyOfRange(data, 1, data.length);
				if (cmdMap.containsKey(data[0])) {
					cmdMap.get(command).exec(args);
				} else {
					CLIUtils.println("Command \"" + data[0] + "\" not found.");
				}
			} catch (NumberFormatException e) {
				CLIUtils.println("NumberFormatException was thrown in ConsoleThread.", Ansi.Color.RED);
			}
		}
	}

}