package de.gandalf1783.jlc.threads;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.sessions.Packet;
import de.gandalf1783.jlc.sessions.ServerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class SessionThread implements Runnable {

	private static Boolean shouldStop = false;
	private static Server server;
	private static Client client;
	private static Kryo kryo;
	private static Boolean isServer = false;
	private static Boolean isAvaliable = false;
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

	public static void setShouldStop(Boolean shouldStop) {
		SessionThread.shouldStop = shouldStop;
	}

	public static void createSession() {
		isServer = true;
		server = new Server();
		server.start();
		try {
			server.bind(54555,54777);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.addListener(new ServerListener());
		System.out.println("Please make sure that TCP 54555 and UDP 54777 are reachable.");
		Main.notify("Session has been started.");
		isAvaliable = true;
	}

	public static void destroySession() {
		if(isServer) {
			Packet packet = new Packet();
			packet.command = "SESSION-DESTROY";
			//packet.systemUUID = Main.getJLCSettings().getSystemUUID();
			Packet packet1 = new Packet();
			packet1.command = "NOTIFY#Session_closed.";
			//packet1.systemUUID = Main.getJLCSettings().getSystemUUID();
			for(Connection c : server.getConnections()) {
				c.sendTCP(packet);
				c.sendTCP(packet1);
			}
			server.stop();
			Main.notify("Session is destroyed.");
			isAvaliable = true;
		} else {
			Packet packet = new Packet();
			packet.command = "LEAVE";
			//packet.systemUUID = Main.getJLCSettings().getSystemUUID();
			Main.notify("Left Session.");
		}

	}

	public static void joinSession() {

	}

	public static String[] discoverSessions() {
		Main.notify("Searching Session...");
		if(isServer) {
			return new String[] {"You are the server."};
		}
		if(client == null) {
			client = new Client();
			client.start();
		}

		List<InetAddress> addresses = client.discoverHosts(54777,5000);
		if(addresses.size() > 0) {
			System.out.println("[LAN-DISCOVERY] Found "+addresses.size()+ " server(s)!");
			Main.notify("Found "+addresses.size()+" server(s)");
			String[] addressArray = new String[addresses.size()];
			int i = 0;
			for(InetAddress a : addresses) {
				addressArray[i] = a+"";
				i++;
			}
			return addressArray;
		} else {
			System.out.println("[LAN-DISCOVERY] No Sessions found.");
			Main.notify("No servers.");
			return new String[] {"No Sessions."};
		}
	}
}