import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private final static int DESTINATION_PORT = 4567;
    private final static String SIMSTART_MSG = "SIMSTART";
    private static BufferedReader sysIn =
        new BufferedReader(new InputStreamReader(System.in));
    private InetAddress serverAddress;
    private int initialDelay;
    private int timeToTransmit;
    private DatagramSocket dgSocket;
    private Timer timer;

    public Client (InetAddress serverAddress, int initialDelay,
                   int timeToTransmit )  throws IOException{
        System.out.println("Created Client 2");
        this.serverAddress = serverAddress;
        this.initialDelay = initialDelay;
        this.timeToTransmit = timeToTransmit;
        System.out.println("server Address " + serverAddress);
        this.dgSocket = new DatagramSocket();
        this.timer = new Timer();
        handleClient();
    }

    final private void handleClient () throws IOException{
        System.out.println("Handle client called");
        int totalCollisions = 0;
        String response;
        long currTime = 0;
        response = sendAndWait(SIMSTART_MSG, true);
        if (!"YESSS".equals(response.toUpperCase())) {
            System.out.println("There was an error. Expected YESSS and got " + response);
            System.exit(1);
        }
        response = scheduler("IDLE", 1000*this.initialDelay, true);
        if (response.toUpperCase().equals("NO")) {
            currTime = System.nanoTime();
            System.out.println("\nThe channel is busy right now. The current time is " + currTime);
            scheduler("IDLE", 1000, false);
        } else {
            int leftoverTransmissionDuration = this.timeToTransmit;
            response = sendAndWait("START", true);
            System.out.println("\nNIC starts transmitting a frame. Current time is " + System.nanoTime() + "\nThe leftover transmission time is " + leftoverTransmissionDuration);
            response = scheduler("COLIDE", 1000, true);
            System.out.println("\nNIC detects collision on channel. Current time is " + System.nanoTime());
            if ("NO".equals(response.toUpperCase())) {
                if (leftoverTransmissionDuration <= 0) {
                    System.out.println("\nDone with transmitting this frame!. The current time is " + System.nanoTime());
                    System.exit(0);
                } else {
                    leftoverTransmissionDuration = leftoverTransmissionDuration - (leftoverTransmissionDuration > 1 ? 1 : leftoverTransmissionDuration);
                    System.out.println("The NIC is still transmitting. The leftover transmission time is " + leftoverTransmissionDuration);
                    response = scheduler ("COLIDE", 1000 * leftoverTransmissionDuration, false);
                }
            } else {
                int backoff = getBackoff(++totalCollisions);
                System.out.println("NIC detects a collision and aborts transmitting the frame and will sense the channel for re-transmission " + backoff + " later. Local time is " + System.nanoTime());
                sendAndWait("ABORT", false);
                scheduler("IDLE", backoff * 1000, false );
            }

        }

        System.out.println("Print response and current time here \nThe response is " + response);
    }

    final private int getBackoff(int high) {
        Random rand = new Random();
        ArrayList<Integer> possibleK = new ArrayList<Integer>();
        high = (int) Math.pow(2, high - 1);
        for (int i = 0; i <= high; i++)  {
            possibleK.add(new Integer(i));
        }
        return possibleK.get(rand.nextInt(possibleK.size())) * this.timeToTransmit;
    }

    final private String sendAndWait(String message, boolean waitForResponse) throws IOException{
        byte outBuff [] = message.getBytes();
        DatagramPacket outgoing =
            new DatagramPacket(outBuff, outBuff.length, this.serverAddress, DESTINATION_PORT);
        this.dgSocket.send(outgoing);
        if (waitForResponse) {
            byte inBuff [] = new byte[1024];
            DatagramPacket incoming =
                new DatagramPacket(inBuff, inBuff.length);
            this.dgSocket.receive(incoming);
            return new String (incoming.getData(), 0, incoming.getLength());
        }
        return "";
    }



final private String scheduler (String msg, long delay, boolean waitForResponse) throws IOException{
        byte [] currBytes = msg.getBytes();
        DatagramPacket outpkt =  new DatagramPacket(currBytes, currBytes.length,
                                                    this.serverAddress, DESTINATION_PORT);
        TimerTask newTask =
            msg.toUpperCase().equals("IDLE") ? new SensingTask(this.dgSocket, outpkt) : new CollisionTask(this.dgSocket, outpkt);
            this.timer.schedule(newTask, delay);

        byte [] inBuff = new byte[1024];
        DatagramPacket inPkt = new DatagramPacket(inBuff, inBuff.length);
        if (waitForResponse) {
            this.dgSocket.receive(inPkt);
        return new String(inPkt.getData(), 0, inPkt.getData().length);
        }
        return "";
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
