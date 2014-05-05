import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private Socket clientSocket = null;
    private PrintWriter clientOut = null;
    private BufferedReader clientIn = null;
    private String clientAddrss;
    private static String serverAddrss;

    public ServerThread(Socket client, String servAddress) {
        super("ServerThread");
        serverAddrss = servAddress;
        this.clientSocket = client;
        this.clientAddrss = this.clientSocket.getInetAddress().getHostAddress();
        try {
            clientOut =
                new PrintWriter(clientSocket.getOutputStream(), true);
            clientIn = new BufferedReader
                (new InputStreamReader (clientSocket.getInputStream()));
        }
        catch (Exception e) {
        }
    }

    public void run () {
        try {
            processPhases();
        }
        catch (Exception e) {
            System.out.println("The client disconnected.");
            killResources();
            e.printStackTrace();
        }
        System.out.println("Done with thread's run method.");
    }


    private String handleCommand (final String msg, final String command, final String errorMSG) throws IOException {
        String fromClient;
        System.out.println("Handle command called");
        this.clientOut.println(msg);
        fromClient = validRequest(this.clientIn.readLine(), command, errorMSG);
        System.out.println("Client data read");
        System.out.println(fromClient);
        return fromClient;
    }

    private void handleMessage(final String line) throws IOException{
        if (line.equals(".")) { return;}
        System.out.println(line);
        handleMessage(this.clientIn.readLine());
    }

    private void processPhases () throws IOException {

        handleCommand("200 " + this.serverAddrss , "HELO", "503 5.5.2 Send hello first");
        handleCommand("250 " + serverAddrss + " Hello " + this.clientAddrss,
                       "MAIL FROM:", "503 5.5.2 Need mail command");
        handleCommand("250 2.1.0 Sender OK", "RCPT TO:", "503 5.5.2 Need rcpt command");
        handleCommand("250 2.1.5 Recipient OK", "DATA", "503 5.5.2 Need data command");

        this.clientOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
        handleMessage(this.clientIn.readLine());
        this.clientOut.println("250 Message received and to be delivered");

        if (this.clientIn.readLine().toUpperCase().equals("QUIT")) {
            killResources();
            return;
        }
        processPhases();
    }

    private String validRequest (final String userResponse,
                                 final String expected,
                                 final String errorMSG) throws IOException{
        System.out.println("Valid request called with user response" + userResponse);
        if (userResponse.toUpperCase().equals("QUIT")) { // Allow QUIT from anywhere
            killResources();
            return "";
        }

        if (userResponse.toUpperCase().contains(expected.toUpperCase())) {
            return userResponse;
        }

        clientOut.println(errorMSG);
        return validRequest(clientIn.readLine(), expected, errorMSG);
    }

    private void killResources() {
        try {
            System.out.println("Killing all resources.");
            this.clientOut.println("221 " + serverAddrss + " closing connection.");
            this.clientOut.close();
            this.clientIn.close();
            this.clientSocket.close();
        }

        catch (IOException e) {
        }
    }
}
