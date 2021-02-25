package de.gandalf1783.jlc.commands;

import de.gandalf1783.jlc.main.Main;

public class SaveAsCommand implements Command {
    @Override
    public int exec(String[] args) {

        Main.saveProjectHandlerGUI();

        return 0;
    }
}
