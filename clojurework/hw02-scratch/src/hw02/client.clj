(ns hw02.client
  (import java.io.BufferedReader
          java.net.InetAddress
          java.net.DatagramPacket
          java.net.DatagramSocket))

(def ids #{"00001" "00002" "00003" "00004" "00005" "00006"})

(def table
  (str "Item ID  Item Description\n"
       "00001    New Inspiron 15\n"
       "00002    New Inspiron 17\n"
       "00003    New Inspiron 15R\n"
       "00004    New Inspiron 15z Ultrabook\n"
       "00005    XPS 14 Ultrabook\n"
       "00006    New XPS 12 UltrabookXPS\n\n"))

(defn -main
  [& args]
  (println "Enter the DNS or IP address to the server.")
  (let [server-addr (read-line)
        server-port 5000]
    (println (str table "Enter an item id."))
    (let [item-id (atom (read-line))]
      (while (complement (some #{@item-id} ids))
        (do
          (println "The id you have entered isn't valid. Please choose a valid id from the table."
                   "\n\n"
                   table)
          (swap! item-id (read-line))))
      (let [byte-response (byte-array (.getBytes @item-id))
            udp-packet (DatagramPacket. byte-response (count byte-response) server-addr server-port)
            socket (DatagramSocket. )]
        (.send udp-packet)
        (let [response-buffer (byte-array 256)
              response-packet (DatagramPacket. response-buffer (count response-buffer))]
          (.receive response-packet)
          (println (String. (.getData response-packet) 0 (.getLength response-packet))))))))


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
        String fromUse
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
