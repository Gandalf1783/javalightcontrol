package de.gandalf1783.jlc.commands;

public interface Command {

    /**
     * Exec executes upon call by the ConsoleThread.
     *
     * @param args Arguments AFTER the initial command
     * @return Wether the execution succeeded or not.
     */
    int exec(String[] args);

}
