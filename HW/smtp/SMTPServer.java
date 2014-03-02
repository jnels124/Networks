import java.net.ServerSocket;
import java.io.*;

public class SMTPServer {
    final private static int port = 4567;
    private static ServerSocket servSocket;

    public static void main (String [] args) {
        try {
            servSocket = new ServerSocket(port);
            while (!servSocket.isClosed()) {
                new ServerThread(servSocket.accept(),
                                 servSocket.getInetAddress().getCanonicalHostName()).start();
                System.out.println("I am executing");
            }
        }

        catch (IOException e) {
            System.err.println("Unable to make connection to port " + port);
            System.exit(-1);
        }

    }
}
