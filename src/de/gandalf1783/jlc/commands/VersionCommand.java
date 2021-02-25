package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.threads.CLIUtils;

public class VersionCommand implements Command {


    @Override
    public int exec(String[] args) {
        CLIUtils.println(Main.getVersion() + " - Java Light Control by Gandalf1893 (c) 2021");
        return 0;
    }
}
