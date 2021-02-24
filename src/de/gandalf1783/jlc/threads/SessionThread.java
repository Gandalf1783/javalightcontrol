package de.gandalf1783.jlc.threads;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.sessions.Packet;
import de.gandalf1783.jlc.sessions.ServerListener;

import java.io.IOException;

public class SessionThread implements Runnable {

	private static Boolean shouldStop = false;
	private static Server server;
	private static Boolean isServer = false;
	private void init() {
		System.out.println("[Session] Thread started.");
	}
	
	@Override
	public void run() {
		init();
		//while (!shouldStop) {
			// SESSION IS TO BE ADDED



			//NOT IMPLEMENTED YET!
		//}
		System.out.println("[Session] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(final Boolean shouldStop) {
		SessionThread.shouldStop = shouldStop;
	}

	public static void createSession() {
		isServer = true;
		server = new Server();
		server.start();
		try {
			server.bind(54555, 54777);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		server.addListener(new ServerListener());
		System.out.println("Please make sure that TCP 54555 and UDP 54777 are reachable.");
		Main.notify("Session has been started.");
	}

	public static void destroySession() {
		if (isServer) {
			final Packet packet = new Packet();
			packet.command = "SESSION-DESTROY";
			// packet.systemUUID = Main.getJLCSettings().getSystemUUID();
			final Packet packet1 = new Packet();
			packet1.command = "NOTIFY#Session_closed.";
			// packet1.systemUUID = Main.getJLCSettings().getSystemUUID();
			for (final Connection c : server.getConnections()) {
				c.sendTCP(packet);
				c.sendTCP(packet1);
			}
			server.stop();
			Main.notify("Session is destroyed.");
		} else {
			final Packet packet = new Packet();
			packet.command = "LEAVE";
			// packet.systemUUID = Main.getJLCSettings().getSystemUUID();
			Main.notify("Left Session.");
		}

	}

	public static void joinSession() {

	}

}