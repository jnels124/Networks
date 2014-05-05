import java.io.*;
import java.net.*;

public class Client {
    private final static int DESTINATION_PORT = 5678;
    private final static String SIMSTART_MSG = "SIMSTART";
    private BufferedReader sysIn;
    private InetAdress serverAdress;
    private int intialDelay;
    private int timeToTransmit;
    private DatagramSocket dgSocket;

    public Client (InetAdress serverAdress, int initialDelay,
                   int timeToTransmit ) {
        this.sysIn = new BufferedReader(new InputStreamReader(System.in));
        this.serverAdress = serverAdress;
        this.initialDelay = initialDelay;
        this.timeToTransmit = timeToTransmit;
        handleClient();
    }

    final private void handleClient () {
        int totalCollisions = 0;
        String response;
        response = sendAndWait(SIMSTART_MSG);
        if (!"YESSS".equals(response)) {
            System.out.println("There was an error. Expected YESSS and got " + response);
            System.exit(1);
        }

        System.our.println("Print response and current time here");


    }

    final private String sendAndWait(String message) {
        byte outBuff [] = message.getBytes();
        DatagramPacket outgoing =
            new DatagramPacket(outBuff, outBuff.length, this.serverAdress, DESTINATION_PORT);
        this.dgSocket.send(outgoing);

        byte inBuff [] = new byte[1024];
        DatagramPacket incoming =
            new DatagramPacket(inBuff, inBuff.length);
        this.dgSocket.recieve(incoming);
        return new String (incoming.getData(), 0, incoming.getLength())
    }

    final public static String messageResponse(final String message) {
        System.out.println(message);
        return sysIn.readLine();
    }

    public static void main (String [] args) {
        final int frameReady =
            Integer.parseInt(messageResponse("How many seconds until the frame is ready to be sent out?"));
        final int frameTransTime =
            Integer.parseInt(messageResponse("How long does it take the host to transmit the frame entirely?"));
        final String address = messageResponse ("Please enter the server address.");
        new Client (InetAdress.getByName(address), frameReady, frameTransTime, null);

    }

}
