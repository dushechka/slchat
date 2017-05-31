package client.model;

import java.io.IOException;
import java.net.*;

import static java.lang.Thread.*;
import static server.model.ServerConstants.*;

/**
 * Chat's client handling class;
 */
public class Client {
    // Multicast packets sender;
        MulticastInterviewer mi = null;
    // This socket used to get a datagram packet
    // packet from server, which contains it's IP;
        private DatagramSocket dSocket = null;
        private DatagramPacket packet = null;
        private byte[] packetData = null;

    Client () {
        establishConnection();
    }

    private void establishConnection() {
        try {
            mi = new MulticastInterviewer();
            mi.start();
            dSocket = new DatagramSocket(CLIENT_PORT);
            packetData = new byte[8];
            packet = new DatagramPacket(packetData, packetData.length);
            dSocket.receive(packet);
            System.out.println("Server's back packet recieved.");
            System.out.println("Server's address: " + packet.getAddress());
            mi.die();
        } catch (IOException ie) {
            System.out.println("Exception thrown while client tried to establish connection with server;");
            ie.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            mi.join();
        } catch (InterruptedException e) {
            System.out.println("Exception thrown while client tied to release resources.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
