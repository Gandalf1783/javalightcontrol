package de.gandalf1783.jlc.sessions;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.Settings;

public class ServerListener extends Listener {



    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Packet) {
            Packet packet = (Packet) object;
            Packet out = new Packet();
            //out.systemUUID = Main.getJLCSettings().getSystemUUID();
            System.out.println("[PACKET] "+packet.command +" FROM UUID "+packet.systemUUID);
            if(packet.command.equalsIgnoreCase("CONNECT")) {
                out.command = "VER?";
            }
            if(packet.command.startsWith("VER")) {
                String[] args = packet.command.split(".");
                String[] args2 = Main.getVersion().split(".");
                if(args.length == 2) {
                    if (args[0].equalsIgnoreCase(args2[0])) {
                        out.command.equalsIgnoreCase("SAVE-PROJECT-LOAD-NET");
                    } else {
                        out.command.equalsIgnoreCase("ABORT");
                    }
                }
            }
            if(packet.command.equalsIgnoreCase("SAVED-REQUESTING-NET")) {
                ObjectPacket objectPacket = new ObjectPacket();
                objectPacket.object = Main.getSettings();
                objectPacket.command = "ENABLE-SESSIONMODE";
            }
            if(packet.command.equalsIgnoreCase("DONE")) {
                out.command = "APPROVED";
                System.out.println("[SESSION] A user has joined the session.");
            }
        }
    }
}
