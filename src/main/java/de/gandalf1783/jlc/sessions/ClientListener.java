package de.gandalf1783.jlc.sessions;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.preferences.Project;

public class ClientListener extends Listener {

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Packet) {
            Packet packet = (Packet) object;
            Packet out = new Packet();
            //out.systemUUID = Main.getJLCSettings().getSystemUUID();
            System.out.println("[PACKET] "+packet.command+" FROM UUID "+packet.systemUUID);
            if(packet.command.equalsIgnoreCase("VER?")) {
                out.command = Main.NET_VERSION;
            }
            if(packet.command.equalsIgnoreCase("SAVE-PROJECT-LOAD-NET")) {
                Main.saveProject();
                Main.saveJLCSettings();
                out.command = "SAVED-REQUESTING-NET";
            }
            if(packet.command.equalsIgnoreCase("ABORT")) {
                System.out.println("[SESSION] - Session was closed.");
                connection.close();
            }
            if(packet.command.equalsIgnoreCase("APPROVED")) {

            }
            if(packet.command.startsWith("NOTIFY#")) {
                String notify = packet.command.replace("NOTIFY#", "");
                String[] args = notify.split("_");
                StringBuilder sb = new StringBuilder();
                for(String s : args) {
                    sb.append(s);
                }

                Main.notify("TEST");
            }


        } else if(object instanceof ObjectPacket) {
            ObjectPacket objectPacket = (ObjectPacket) object;
            Packet out = new Packet();
            if(objectPacket.command.equalsIgnoreCase("ENABLE-SESSIONMODE")) {
                Main.setSessionMode(true);
                Main.setProject((Project) objectPacket.object);
                out.command = "DONE";
            }
        }
    }
}
