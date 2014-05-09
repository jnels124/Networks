import java.util.*;
import java.net.*;

public class SensingTask extends TimerTask {
    DatagramPacket packetToSend;
    DatagramSocket sendingSocket;

    public SensingTask (DatagramSocket skt, DatagramPacket packet) {
        this.packetToSend = packet;
        this.sendingSocket = skt;
    }
    public void run () {
        try {
            this.sendingSocket.send(this.packetToSend);
        } catch (Exception e) {
            System.out.println("Exception while performing sensing task " + e.getMessage());
        }
    }
}
