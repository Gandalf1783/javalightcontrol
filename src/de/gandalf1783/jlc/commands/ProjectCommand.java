package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.threads.CLIUtils;

public class ProjectCommand implements Command {
    @Override
    public int exec(String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("name")) {
                CLIUtils.println("The current name of the Project is \"" + Main.getProject().getProjectName() + "\".");
            } else if (args[0].equalsIgnoreCase("path")) {
                if (Main.getJLCSettings().getProject_path() != null) {
                    if (Main.getJLCSettings().getProject_path().equals("")) {
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

        return 0;
    }
}
