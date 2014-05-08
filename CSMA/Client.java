import java.io.*;
import java.net.*;

public class Client {
    private final static int DESTINATION_PORT = 5678;
    private final static String SIMSTART_MSG = "SIMSTART";
    private static BufferedReader sysIn =
        new BufferedReader(new InputStreamReader(System.in));
    private InetAddress serverAddress;
    private int initialDelay;
    private int timeToTransmit;
    private DatagramSocket dgSocket;

    public Client (InetAddress serverAddress, int initialDelay,
                   int timeToTransmit )  throws IOException{
        System.out.println("Created Client 2");
        this.serverAddress = serverAddress;
        this.initialDelay = initialDelay;
        this.timeToTransmit = timeToTransmit;
        this.dgSocket = new DatagramSocket(DESTINATION_PORT, this.serverAddress);
        handleClient();
    }

    final private void handleClient () throws IOException{
        System.out.println("Handle client called");
        int totalCollisions = 0;
        String response;
        response = sendAndWait(SIMSTART_MSG);
        if (!"YESSS".equals(response)) {
            System.out.println("There was an error. Expected YESSS and got " + response);
            System.exit(1);
        }

        System.out.println("Print response and current time here");


    }

    final private String sendAndWait(String message) throws IOException{
        byte outBuff [] = message.getBytes();
        DatagramPacket outgoing =
            new DatagramPacket(outBuff, outBuff.length, this.serverAddress, DESTINATION_PORT);
        this.dgSocket.send(outgoing);

        byte inBuff [] = new byte[1024];
        DatagramPacket incoming =
            new DatagramPacket(inBuff, inBuff.length);
        this.dgSocket.receive(incoming);
        return new String (incoming.getData(), 0, incoming.getLength());
    }

    final public static String messageResponse(final String message) throws IOException{
        System.out.println(message);
        return sysIn.readLine();
    }

    public static void main (String [] args) {
        try {
            final int frameReady =
                Integer.parseInt(messageResponse("How many seconds until the frame is ready to be sent out?"));
            final int frameTransTime =
                Integer.parseInt(messageResponse("How long does it take the host to transmit the frame entirely?"));
            final String address = messageResponse ("Please enter the server address.");
            new Client (InetAddress.getByName(address), frameReady, frameTransTime);
            System.out.println("Created the Client");
        }
        catch (Exception e) {
            System.out.println("There was an exception on the client\n"  + e.getMessage());
        }
    }
}
