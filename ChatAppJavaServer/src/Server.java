import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Define a class named Server which extends Thread, allowing it to run concurrently with other parts of the program.
public class Server extends Thread {
	
	// Declare instance variables for ServerSocket and MessageManager.
	private ServerSocket serverSocket;
	private MessageManager messageManager;
	
	// Constructor method that takes a port number and initializes the serverSocket and messageManager.
	public Server(int port) throws IOException {
		
		// Instantiate a ServerSocket object and bind it to the specified port.
		serverSocket = new ServerSocket(port);
		
		// Set the timeout for the serverSocket to 0, meaning it will wait indefinitely for connections.
		serverSocket.setSoTimeout(0);
		
		// Instantiate a MessageManager object.
		messageManager = new MessageManager();
	}
	
	// Infinite loop to continuously listen for incoming client connections.
	public void run() {
		// Infinite loop to continuously listen for incoming client connections.
		while (true) {
			try {
				// Print a message indicating that the server is waiting for clients on the specified port.
				System.out.println("Waiting for client on port " 
						+ serverSocket.getLocalPort());
				
				// waits until a connection has been accepted, then resumes the code
				Socket socket = serverSocket.accept();		
	            
				// Print a message indicating that a connection has been established with a client and display the client's address.
				System.out.println("Connection from client on address: "+ socket.getRemoteSocketAddress());
	            
				// Create a DataInputStream to read data sent by the client over the socket.
				DataInputStream in = new DataInputStream(socket.getInputStream());
	           
				// Read the input from the client as a UTF-encoded string.
				String clientInput = in.readUTF();
	            
				// Print the input received from the client.
				System.out.println(clientInput);
	            
				// Create a DataOutputStream to send data back to the client over the socket.
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	            
				// Process the client input and get the corresponding server output.
				String serverOutput = handleClientInput(clientInput);
	            
				// Print the server output.
				System.out.println(serverOutput);
	            
				// Print a separator line for clarity.
				System.out.println("=================================");
	            
				// Write the server output back to the client as a UTF-encoded string.
				out.writeUTF(serverOutput);
	            
				// Close the socket to end the connection with the client.
				socket.close();
			} catch (IOException e) {
	            // Print any IOException that occurs during communication with the client.
				e.printStackTrace();
			}
		}
	}
	
	// Method to handle client input and generate server output accordingly.
	private String handleClientInput(String clientInput) {
	    // Print a message indicating that the client input is being processed.
		System.out.println("clientInput");
		try {
			if (validConnect(clientInput)) {
	            // Split the client input to extract relevant information.
				String[] splitMessage = clientInput.split(MessageManager.MESSAGE_PROPERTY_DELIMETER);
	            // Return a success message indicating that the client has successfully connected to the server.
				return "Successfully connected to server, " + splitMessage[0];
			} else if (clientInput.equals("UPDATE")) {
				// If the client input is "UPDATE", return all messages stored in the message manager.
				return messageManager.getAllMessages();
			} else {
				// If the client input is not a connection request or "UPDATE", add the message to the message manager.
				messageManager.addMessage(clientInput);
			}
			// Return a success message indicating that the client input has been processed successfully.
			return "Success";
		} catch (Exception e) {
			// Method to check if the client input represents a valid connection request.
			return "ERROR:" + e.toString();
		}
	}
	
	// Method to check if the client input represents a valid connection request.
	private boolean validConnect(String clientInput) {
		// Split the client input to extract relevant information.
		String[] splitMessage = clientInput.split(MessageManager.MESSAGE_PROPERTY_DELIMETER);
		// Check if the split message contains two parts and the second part is "CONNECT".
		if (splitMessage.length == 2 && splitMessage[1].equals("CONNECT")) {
			return true;
		};
		// Return false if the client input does not represent a valid connection request.
		return false;
	}
}
