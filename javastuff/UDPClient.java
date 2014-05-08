/*
 * Client App upon UDP
 * Weiying Zhu
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
             System.out.println("Usage: java UDPClient <hostname>");
             return;
        }

            // creat a UDP socket
        DatagramSocket udpSocket = new DatagramSocket();

        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        while ((fromUser = sysIn.readLine()) != null) {

				//display user input
          System.out.println("From Client: " + fromUser);

            // send request
          InetAddress address = InetAddress.getByName(args[0]);
			 byte[] buf = fromUser.getBytes();
          DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, address, 5678);
          udpSocket.send(udpPacket);

            // get response
		    byte[] buf2 = new byte[256];
          DatagramPacket udpPacket2 = new DatagramPacket(buf2, buf2.length);
          udpSocket.receive(udpPacket2);

  	        // display response
          fromServer = new String(udpPacket2.getData(), 0, udpPacket2.getLength());
          System.out.println("From Server: " + fromServer);

			 if (fromUser.equals("Bye."))
    			break;
	 	  }

        udpSocket.close();
    }
}
