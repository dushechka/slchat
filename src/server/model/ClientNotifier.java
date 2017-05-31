package server.model;

import static server.model.ServerConstants.*;

import java.io.IOException;
import java.net.*;

/**
 * Recieves multicast packets from client and then sends
 * datagram packet back in order for client to know server's address;
 */
public class ClientNotifier extends Thread {
        // Determines whether this thread is alive;
        private boolean IS_RUNNING;
        // Socket which, recieves clients multicast packet;
        private MulticastSocket mSocket = null;
        // Multicast group in which client sends packets;
        private InetAddress group = null;
        private DatagramPacket packet = null;
        private byte[] packetData = null;

    ClientNotifier() {
        super("ClientNotifier");
        try {
            mSocket = new MulticastSocket(SERVER_MULTI_PORT);
            group = InetAddress.getByName(GROUP_ADDRESS);
            mSocket.joinGroup(group);
            packetData = new byte[8];
            packet = new DatagramPacket(packetData, packetData.length);
        } catch (IOException exc) {
            System.out.println("Exception thrown while initializing resources in constructor.");
            System.out.println("Thread " + getName());
            exc.printStackTrace();
        }
    }

    public void run() {
        IS_RUNNING = true;
        System.out.println("ClientNotifier started.");
        try {
            while(IS_RUNNING) {
                mSocket.receive(packet);
                // Recieving multicast packet from clients;
                if (byteToString(packetData).equals(SERVER_STRING)) {
                    System.out.println("Multicast packet recieved from client.");
                    System.out.println("Clients address: " + packet.getAddress());
                    packet = new DatagramPacket(packetData, packetData.length, packet.getAddress(), CLIENT_PORT);
                    mSocket.send(packet);
                    System.out.println("Back datagram packet sent to client;");
                } else {
                    System.out.println("Unknown packet intercepted.");
                    System.out.println("Sender's address: " + packet.getAddress());
                    System.out.println("Packet data: " + byteToString(packetData));
                }
                sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception thrown in run() method.");
            System.out.println("Thread " + getName());
            e.printStackTrace();
        } finally {
            close();
        }
    }

    // Killing this thread;
    void die() {
        IS_RUNNING = false;
    }

    private void close() {
        if ((mSocket != null) && (!mSocket.isClosed())) {
            this.mSocket.close();
        }
        System.out.println("ClientNotifier stoped.");
    }
}
