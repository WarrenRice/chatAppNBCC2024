import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private ServerSocket serverSocket;
	private MessageManager messageManager;
	public Server(int port) throws IOException {
		// instantiate server socket
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(0);
		// instantiate message manager
		messageManager = new MessageManager();
		messageManager.test();
	}
	
	public void run() {
		while (true) {
			try {
				
				System.out.println("Waiting for client on port " 
						+ serverSocket.getLocalPort());
				// waits until a connection has been accepted, then resumes the code
				Socket socket = serverSocket.accept();					
				System.out.println("Connection from client on address: " 
						+ socket.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(socket.getInputStream());
				// client input
				String clientInput = in.readUTF();
				System.out.println(clientInput);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				// server output
				String serverOutput = handleClientInput(clientInput);
				System.out.println(serverOutput);
				System.out.println("=================================");
				out.writeUTF(serverOutput);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String handleClientInput(String clientInput) {
		System.out.println("clientInput");
		try {
			if (validConnect(clientInput)) {
				String[] splitMessage = clientInput.split(MessageManager.MESSAGE_PROPERTY_DELIMETER);
				return "Successfully connected to server, " + splitMessage[0];
			} else if (clientInput.equals("UPDATE")) {
				return messageManager.getAllMessages();
			} else {
				messageManager.addMessage(clientInput);
			}
			return "Success";
		} catch (Exception e) {
			return "ERROR:" + e.toString();
		}
	}
	
	private boolean validConnect(String clientInput) {
		String[] splitMessage = clientInput.split(MessageManager.MESSAGE_PROPERTY_DELIMETER);
		if (splitMessage.length == 2 && splitMessage[1].equals("CONNECT")) {
			return true;
		};
		return false;
	}
}
