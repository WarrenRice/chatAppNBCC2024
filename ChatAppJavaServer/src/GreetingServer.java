// File Name GreetingServer.java
// Reference : http://www.tutorialspoint.com/java/java_networking.htm




import java.net.*;
import java.io.*;

public class GreetingServer extends Thread									// Server is inherited from the Thread object
{
   private ServerSocket serverSocket;										// Declaration of the socket that the server will
   																			// run on
   public GreetingServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);								// Instantiates the server socket
      serverSocket.setSoTimeout(0);										// Sets the timeout for the socket. A timeout of 0 establishes
   }																		// an infinite timeout

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +				// Output informational message that the server is waiting
            serverSocket.getLocalPort() + "...");							// for a client on the specified port
            Socket server = serverSocket.accept();							// Wait for a connection from the client
            System.out.println("Just connected to "							// Output informational message that the server is connected
                  + server.getRemoteSocketAddress());						// to the client at the specified IP address
            DataInputStream in =											// Get the data from the client
                  new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());								// Display the data from the client
            
            
            DataOutputStream out =											// Prepare the object for returning data to the client
                 new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to "						// Send the data back to the client
              + server.getLocalSocketAddress() + "\nGoodbye!");
            server.close();													// Shut down the server
         
         
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   {
      //int port = Integer.parseInt(args[0]);          						// Used to permit command prompt invocation
	   
      int port = 6066;														// Establish port 6066 as a hard-coded port override
	   
      try
      {
         Thread t = new GreetingServer(port);								// Instantiates a server on a separate thread
         t.start();															// Executes the server
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
