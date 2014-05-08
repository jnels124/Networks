import java.io.*;
import java.net.*;
import java.util.*;

public class Channel {
    private static final int PORT = 5678;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private double propDelay;
    private ArrayList<Host> connectedHosts;
    private int numHosts;
    private DatagramSocket dgSocket;

    public Channel(double propDelay, int numHosts) {
        this.propDelay;
        this.numHosts = numHosts;
        this.connectedClients = new ArrayList<Host>();
        this.dgSocket = new DatagramSocket(PORT);
        handleConnections();
        eventLoop();
    }

    private void eventLoop () {
        final long t_now = System.getNanoTime();
        byte [] buffer = new byte [DEFAULT_BUFFER_SIZE];
        DatagramPacket currentPacket = new DatagramPacket(buffer, buffer.length);
        this.dgSocket.recieve(currentPacket);
        String msg = new String (currentPacket.getData(), 0, currentPacket.getLength()).toUpperCase();
        if(msg == null) return;
        switch(msg) {
        case "IDLE" :
            for (Host host : this.connectedHosts) {
                final long t_start = host.getT_Start();
                final long t_stop = host.getT_Stop();
                boolean yesOrNo =  (t_start<= 0);
                yesOrNo =
                    yesOrNo || (((t_start + 1000 * this.propDelay) >  t_now) &&
                                ((t_stop < 0) || ((t_stop + 1000 * this.propDelay) <= t_now)));
                yesOrNo =
                    yesOrNo || ((t_start <= t_stop) && ((t_stop + 1000 * this.propDelay) <= t_now));

                if (yesOrNo) {
                    buffer = "YES".getBytes();
                    this.DatagramSocket.send(new DatagramPacket(buffer, buffer.length,
                                                                host.getHostIP(), host.getPort()));
                } else {
                    buffer = "NO".getBytes();
                    this.DatagramSocket.send(new DatagramPacket(buffer, buffer.length,
                                                                host.getHostIP(), host.getPort()));
                }
            }
            break;

        case "COLIDE" :
            for (Host host : this.connectedHosts) {
                if (currentPacket.getAddress().equals(host.getHostIP())) {
                    long t_start = host.getT_Start();
                    long t_stop = host.getT_Stop();
                    boolean yesOrNo =  (t_start<= 0);
                    yesOrNo =
                        yesOrNo || (((t_start + 1000 * this.propDelay) >  t_now) &&
                                ((t_stop < 0) || ((t_stop + 1000 * this.propDelay) <= t_now)));
                    yesOrNo =
                        yesOrNo || ((t_start <= t_stop) && ((t_stop + 1000 * this.propDelay) <= t_now));

                    if (yesOrNo) {
                        buffer = "NO".getBytes();
                        this.DatagramSocket.send(new DatagramPacket(buffer, buffer.length,
                                                                host.getHostIP(), host.getPort()));
                    } else {
                        buffer = "YES".getBytes();
                        this.DatagramSocket.send(new DatagramPacket(buffer, buffer.length,
                                                                host.getHostIP(), host.getPort()));
                    }
                }
            }
            break;

        case "START" :
            Host temp;
            for (Host host : this.connectedHosts) {
                if (host.getHostIP().equals(currentPacket.getAddress())) {
                    temp = host;
                    break;
                }
            }
            if (temp == null) {
                this.connectedHosts.add(new Host(currentPacket.getAddress(),
                                                 currentPacket.getPort(),
                                                 t_now,
                                                 -1));
            }
            break;
            //Just fall through to abort
        case "DONE" :

        case "ABORT":
            Host temp;
            for (Host host : this.connectedHosts) {
                if (host.getHostIP().equals(currentPacket.getAddress())) {
                    temp = host;
                    break;
                }
            }
            if (temp == null) {
                this.connectedHosts.add(new Host(currentPacket.getAddress(),
                                                 currentPacket.getPort(),
                                                 -1,
                                                 t_now));
            }
            break;

        }
    }

    private void handleConnections () {
        int simStartsSent = 0;
        DatagramPacket currentPacket;
        byte [] buffer = new byte [DEFAULT_BUFFER_SIZE];
        while (true) {
            if (simStartsSent == this.numHosts) break;
            try {
                currentPacket = new DatagramPacket(buffer, buffer.length);
                this.dgSocket.recieve(currentPacket);
                this.connectedHosts.add(new Host (currentPacket.getAddress(),
                                                  currentPacket.getPort(),
                                                  -1.0,
                                                  -1.0));
                simStartsSent++;
            }
        }
        byte [] toClient = "YESS".getBytes();
        for (Host host : this.connectedHosts) {
            currentPacket = new DatagramPacket(toClient, toClient.length, host.getHostIP(), host.getPort());
            this.dgSocket.send(currentPacket);
        }
    }

    public int getPropDelay () {
        return this.propDelay;
    }

    public static void main (String [] args) {
        double delay =
            Double.parseDouble(Client.messageResponse("What is the end to end propagation delay? (seconds)"));
        new Channel(delay);

    }

}
