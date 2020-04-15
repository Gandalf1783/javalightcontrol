package threads;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WindowThread implements Runnable {

	private static Boolean shouldStop = false;

	private void init() {
		System.out.println("[Window] Thread started.");
		
		
		
	}
	
	@Override
	public void run() {
		init();
		//while (!shouldStop) {
			// UI NEEDS TO BE ADDED
		//}
		System.out.println("[Window] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(Boolean shouldStop) {
		WindowThread.shouldStop = shouldStop;
		
	}


}