package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.main.Main;

public class LoadCommand implements Command {
    @Override
    public int exec(String[] args) {

        Main.loadProjectGUI();

        return 0;
    }
}
