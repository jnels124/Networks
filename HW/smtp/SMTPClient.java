import java.net.*;
import java.io.*;

public class SMTPClient {
     private static final int port = 4567;
        private final BufferedReader sysIn =
            new BufferedReader(new InputStreamReader(System.in));

        private Socket tcpSocket = null;
        private PrintWriter socketOut = null;
        private BufferedReader socketIn = null;
        private String hostname = null;
        SMTPClient () {
            //
            try {
                createConnection();
            } catch (IOException e) {}
        }

        private void createConnection() throws IOException {
            this.hostname = messageResponse("Please enter the host name.");
            this.tcpSocket = new Socket(this.hostname, port); //Server Will send 200 response
            handleConnection(new PrintWriter(tcpSocket.getOutputStream(), true),
                             new BufferedReader
                             (new InputStreamReader(tcpSocket.getInputStream())));
        }
        private void handleCommand (PrintWriter outstrm,
                                    BufferedReader instrm,
                                    String msg) throws IOException {
            double startTime = System.nanoTime();
            outstrm.println(msg);
            System.out.println(instrm.readLine());
            System.out.println("RTT = " + ((System.nanoTime() - startTime) / 1000000.0));
        }

        private void handleConnection (final PrintWriter outstrm,
                                       final BufferedReader instrm) throws IOException {
            while (true) {
                System.out.println(instrm.readLine()); // display 220 response
                String message[] = createMessages(outstrm, instrm);

                if (!("YES".equals(
                    messageResponse ("Would you like to continue? yes or no?").toUpperCase()))) {
                    break;
                }
                outstrm.println("continue"); // notify server continuing.
            }
            outstrm.println("QUIT");
            System.out.println(instrm.readLine());
            outstrm.close();
            instrm.close();
            this.sysIn.close();
            this.tcpSocket.close();
        }

        private String [] createMessages (final PrintWriter outstrm,
                                         final BufferedReader instrm) throws IOException {
            handleCommand(outstrm, instrm, "HELO " + this.tcpSocket.getInetAddress());
            final String sendersEmail = messageResponse("Enter the senders email address.");
            handleCommand(outstrm, instrm, "MAIL FROM: <" + sendersEmail + ">");
            final String recieversEmail = messageResponse("Enter the recipients email address.");
            handleCommand(outstrm, instrm, "RCPT TO: <" + recieversEmail + ">");
            handleCommand(outstrm, instrm, "DATA");
            final String subject = messageResponse("Enter the subject.");
            final String email =
                createBody(messageResponse("Enter the body and end with '.' ."), "");
            handleCommand(outstrm, instrm,
                          "To: " + recieversEmail + "\n" + "From: " + sendersEmail + "\n" +
                          "Subject: " + subject + "\n" + email);
            return new String [] {sendersEmail, recieversEmail, subject, email};
        }

        private String createBody(final String line, final String body) throws IOException{
            if (line.equals(".")) return body + "\n" + line;
            return createBody(sysIn.readLine(), body + "\n" + line);
        }

        private String messageResponse (final String msg) throws IOException {
            System.out.println(msg);
            return sysIn.readLine();
        }

    //final BufferedReader sysin = new BufferedReader (new InputStreamReader (System.in));
    public static void main (String [] args) throws Exception {
        new SMTPClient();
    }

}
