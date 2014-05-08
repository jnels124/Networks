import java.io.*;
import java.net.*;
import java.util.*;

/**
08. *
09. * @author Administrator
10. */
public class SMTPClient1
{

    /**
14.     * @param args the command line arguments
15.     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        // Establish a TCP connection with the mail server.
                  System.out.println("Enter the mail server you wish to connect to (example:  edge.nunet.nova.edu):\n");
                  String hostName = new String();
                  BufferedReader emailScanner = new BufferedReader(new  InputStreamReader (System.in));
                  hostName = emailScanner.readLine();
                  System.out.println(hostName);
                  Socket emailSocket = new Socket(hostName, 4567);
                  System.out.println("The client socket was created.");

                  // Create a BufferedReader to read a line at a time.
                  InputStream is = emailSocket.getInputStream();
                  BufferedReader br = new BufferedReader( new InputStreamReader (is));
                  PrintWriter  isr = new PrintWriter(emailSocket.getOutputStream(), true);
                  //System.out.println()

                  // Read greeting from the server.
                  String response = br.readLine();
                  System.out.println(response);

                  if (!response.startsWith("200")) {
                  throw new Exception("220 reply not received from server.\n");
                  }

                  // Get a reference to the socket's output stream.
                  //OutputStream os = emailSocket.getOutputStream();

                  // Send HELO command and get server response.
                  String command = "HELO pbrooks";
                  isr.println(command);
                  System.out.println("I printed the hello command to server");
                  //System.out.print(command);
                  //os.write(command.getBytes("US-ASCII"));
                  response = br.readLine();
                  System.out.println("After the  second readline");
                  System.out.println(response);
                  if (!response.startsWith("250")) {
                  throw new Exception("250 reply not received from server.\n");
                  }
                  // Send MAIL FROM command.
                  System.out.println("Please enter your (source) e-mail address (example: me@myexample.com:\n");
                  String sourceAddress = emailScanner.readLine();
                  String mailFromCommand = "MAIL FROM:  <" + sourceAddress + ">";
                  System.out.println(mailFromCommand);
                  isr.println(command);
                  response = br.readLine();
                  System.out.println(response);


                  if (!response.startsWith("250"))
                      throw new Exception("250 reply not received from server.\n");

                  // Send RCPT TO command.
                  System.out.println("Please type the destination e-mail address (example:  example@nova.edu):\n");
                  String destEmailAddress = new String();
                  destEmailAddress = emailScanner.readLine();
                  String fullAddress = new String();
                  fullAddress = "RCPT TO:  <" + destEmailAddress + ">";
                  System.out.println(fullAddress);
                  isr.println(fullAddress);
                  response = br.readLine();
                  System.out.println(response);
                  if(!response.startsWith("250"))
                  {
                      System.out.println("Nope");

                      throw new Exception("250 reply not received from server.\n");
                  }
                    // Send DATA command.
                  String dataString = new String();
                  dataString = "DATA";
                  System.out.println(dataString);
                  isr.println(dataString);
                  response = br.readLine();
                  if(!response.startsWith("354"))
                      throw new Exception("354 reply not received from server.\n");
                  System.out.println(response);
                  // Send message data.
                  System.out.println("Enter your message, enter '.' on a separate line to end message data entry:\n");
                  String input = new String();
                  while(input.charAt(0) != '.')
                  {
                      input = emailScanner.readLine();
                      isr.println(input);
                  }
                      //End with line with a single period.
                  isr.println(input);
                  response = br.readLine();
                  System.out.println(response);
                  if(!response.startsWith("250"))
                  throw new Exception("250 reply not received from server\n");

                  // Send QUIT command.
                  String quitCommand = new String();
                  quitCommand = "QUIT";
                  isr.println(command);

                    }
                  }
