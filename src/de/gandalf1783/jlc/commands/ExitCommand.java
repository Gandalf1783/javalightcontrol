package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.main.Main;

public class ExitCommand implements Command {
    @Override
    public int exec(String[] args) {
        Main.shutdown();
        return 0;
    }
}
