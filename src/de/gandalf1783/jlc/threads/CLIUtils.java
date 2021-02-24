package de.gandalf1783.jlc.threads;

import org.fusesource.jansi.Ansi;

public class CLIUtils {

    /**
     * Print routine that handles the Log Output aswell as the Console
     *
     * @param text Text to print to Console & Log
     */
    public static void println(String text, Ansi.Color color) {

        StringBuilder sb = new StringBuilder();

        sb.append(text);
        printConsole(Ansi.ansi().fg(color).bold().a(sb.toString() + "\n").reset().toString());
    }

    public static void println(String text) {

        StringBuilder sb = new StringBuilder();

        sb.append(text);
        printConsole(Ansi.ansi().fg(Ansi.Color.CYAN).bold().a(sb.toString() + "\n").reset().toString());
    }

    private static void printConsole(String text) {
        System.out.print(text);
    }

}
