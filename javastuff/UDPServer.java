/*
 * Server App upon UDP
 * Weiying Zhu
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {
    public static void main(String[] args) throws IOException {

	     DatagramSocket udpServerSocket = null;
        BufferedReader in = null;
		  DatagramPacket udpPacket = null, udpPacket2 = null;
		  String fromClient = null, toClient = null;
        boolean morePackets = true;

		  byte[] buf = new byte[256];
                  System.out.println("the length is " + buf.length);

		  udpServerSocket = new DatagramSocket(5678);

        while (morePackets) {
            try {

                // receive UDP packet from client
                udpPacket = new DatagramPacket(buf, buf.length);
                udpServerSocket.receive(udpPacket);

					 fromClient = new String(
					 		udpPacket.getData(), 0, udpPacket.getLength());

					 // get the response
					 toClient = fromClient.toUpperCase();

					 // send the response to the client at "address" and "port"
                InetAddress address = udpPacket.getAddress();
                int port = udpPacket.getPort();
					 byte[] buf2 = toClient.getBytes();
                udpPacket2 = new DatagramPacket(buf2, buf2.length, address, port);
                udpServerSocket.send(udpPacket2);

            } catch (IOException e) {
                e.printStackTrace();
					 morePackets = false;
            }
        }

        udpServerSocket.close();

    }
}
