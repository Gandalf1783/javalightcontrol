package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.main.Main;

public class SaveCommand implements Command {
    @Override
    public int exec(String[] args) {

        Main.saveProject(Main.getJLCSettings().getProject_path());
        return 0;
    }
}
