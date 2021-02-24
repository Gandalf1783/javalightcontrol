package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.threads.CLIUtils;

public class HelpCommand implements Command {


    @Override
    public int exec(String[] args) {
        CLIUtils.println("## HELP ##\nList of all Commands:\n");
        CLIUtils.println("version - Displays current deployed version");
        CLIUtils.println("load - Loads a Project (Opens the GUI)");
        CLIUtils.println("project - Sets a project name, or displays the current path");
        CLIUtils.println("saveas - Saves the project as a new file (Opens the GUI)");
        CLIUtils.println("save - Saves the current project at its default location (Opens the GUI if it was not saved before)");
        CLIUtils.println("universe - Configures the output universes and their IP-Address");
        CLIUtils.println("exit - Exits the application. (Saves, opens Save GUI if not saved yet, and exits)");
        return 0;
    }


}
