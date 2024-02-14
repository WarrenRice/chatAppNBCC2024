import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ChatClientApp extends JFrame {

	private static final long serialVersionUID = 1L;		// Serial version UID for serialization compatibility


	private JList<String> chatBox; 							// JList for displaying messages
	private JScrollPane chatListScrollPane;					// Scroll pane for the chat list
	private JTextField messageInputField; 					// Text field for typing messages
	private JButton sendButton; 							// Button to send messages
	private JTextField serverIPField; 						// Text field for entering server IP address
	private JTextField portField; 							// Text field for entering server port number
	private JButton connectButton; 							// Button to connect to the server
	private String username;								// User's username
	private JLabel usernameLabel;							// Label for displaying username					

	private String serverIP;								// Server IP address
	private String portStr;									// String representation of server port number
	private int port;										// Server port number

	private JPanel inputPanel;								// Panel for user input components
	private JPanel serverPanel;								// Panel for server connection components
	private JPanel mainPanel;								// Main panel containing all components

	private MessageManager mManager;						// Manager for handling messages

	private Timer updateTimer;								// Timer for periodic updates

	private boolean initialUpdate = false;					// Flag indicating whether initial update is done

	public ChatClientApp() {
		setTitle("Chat Client");							// Set title of the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// Set default close operation
		setSize(600, 800);									// Set size of the frame

		mManager = new MessageManager();					// Initialize message manager

		// Initialize components
		chatBox = new JList<>(mManager.getMessages());
		chatBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chatListScrollPane = new JScrollPane(chatBox); 		// Create a JScrollPane and add the JList to it
		// Enable vertical scroll bar policy
		chatListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Initialize username label, message input field, and send button
		usernameLabel = new JLabel();
		messageInputField = new JTextField(20);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();					// Call sendMessage method
				scrollToBottom();				// Call scrollToBottom method
			}
		});

		// Panel for text input and send button
		inputPanel = new JPanel();				// Initialize input panel
		inputPanel.add(usernameLabel);			// Add username label to input panel
		inputPanel.add(messageInputField);		// Add message input field to input panel
		inputPanel.add(sendButton);				// Add send button to input panel

		// Initialize server IP field, port field, and connect button
		serverIPField = new JTextField(15);
		serverIPField.setText("25.42.224.13");
		//serverIPField.setText("localhost");
		portField = new JTextField(5); 					// Text field for port input
		portField.setText("6066");						// Set default port value in port field
		connectButton = new JButton("Connect");			// Add action listener to connect button
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectToServer();						// Call connectToServer method
			}
		});

		serverPanel = new JPanel();						// Initialize server panel
		serverPanel.add(new JLabel("Server IP: "));		// Add label for Server IP to server panel
		serverPanel.add(serverIPField);					// Add server IP field to server panel
		serverPanel.add(new JLabel("Port: "));			// Add label for Port to server panel
		serverPanel.add(portField);						// Add port field to server panel
		serverPanel.add(connectButton);					// Add connect button to server panel
		
		// Layout
		mainPanel = new JPanel(new BorderLayout());				// Initialize main panel with BorderLayout
		mainPanel.add(chatListScrollPane, BorderLayout.CENTER); // Adding chat box to the center of the main panel
		mainPanel.add(inputPanel, BorderLayout.SOUTH); 			// Adding input panel to the bottom of the main panel
		mainPanel.add(serverPanel, BorderLayout.NORTH); 		// Adding server panel to the top of the main panel

		getContentPane().add(mainPanel); 						// Adding main panel to the frame's content pane
		// Initialize emoji list renderer
		EmojiListRenderer emojiListRenderer = new EmojiListRenderer();
		chatBox.setCellRenderer(emojiListRenderer);				// Set emoji list renderer to the chat box

		// Add key listener to message input field
		messageInputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();								// Call sendMessage method when Enter key is pressed
				}
			}
		});

		inputPanel.setVisible(false); 	// Hide input panel

		setVisible(true); 				// Make the frame visible
	}

	private void startUpdateTimer() {
		updateTimer = new Timer();				// Initialize update timer
		updateTimer.schedule(new TimerTask() {	// Schedule timer task to run periodically
			@Override
			public void run() {
				sendUpdateMessage();			// Call sendUpdateMessage method
				if (!initialUpdate) {			// Check if initial update is done
					scrollToBottom();			// Scroll chat to bottom
					initialUpdate = true;		// Set initialUpdate flag to true
				}
			}
		}, 100, 1000); 							// Delay 0ms, repeat every 1000ms (1 second)
	}

	private void sendUpdateMessage() {
		try {
			// Attempt to establish a connection to the server
			Socket clientS = new Socket(serverIP, port);

			// Output data
			OutputStream outputToServer = clientS.getOutputStream();
			DataOutputStream outputData = new DataOutputStream(outputToServer);
			outputData.writeUTF("UPDATE"); // Send a command to the server

			// Close the socket after sending the message
			clientS.close(); // Close the socket after sending the message
		} catch (Exception err) {
			// Handle exceptions related to socket communication
			System.out.println("Error sending UPDATE message to server: " + err.getMessage());
		}

		updateMessage();		// Call updateMessage method
	}


	private void sendMessage() {
		// Get text message from input field and trim leading/trailing whitespace
		String textMessage = messageInputField.getText().trim();
		// Check if message is not empty
		if (!textMessage.isEmpty()) {
			
			// Prepare message to send with username and delimiter
			String textToSend = username + MessageManager.MESSAGE_PROPERTY_DELIMETER + textMessage;

			try {
				// Attempt to establish a connection to the server
				Socket clientS = new Socket(serverIP, port);

				// Output data
				OutputStream outputToServer = clientS.getOutputStream();
				DataOutputStream outputData = new DataOutputStream(outputToServer);
				outputData.writeUTF(textToSend); // Send a command to the server

				System.out.println("Sent");

				clientS.close();
			} catch (Exception err) {
				// Handle exceptions related to socket communication
				System.out.println("Error in sendMessage " + err.getMessage());
			}
			
			// Call updateMessage method
			updateMessage();

			// Clear the message input field
			messageInputField.setText(""); // Clear the message input field

		} else {
			// Show error message if input field is empty
			JOptionPane.showMessageDialog(this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public class EmojiListRenderer extends DefaultListCellRenderer {
		// Serial version UID for serialization compatibility
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			// Call super method to get default list cell renderer
			JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			// Cast value to String
			String word = (String) value;
			// Get emoji keys from EmojiHashMap
			String[] emojiKeys = EmojiHashMap.getKeys();
			// Iterate through emoji keys
			for (int i = 0; i < emojiKeys.length; i++) {
				// Check if word contains current emoji key
				if (word.contains(emojiKeys[i])) {
					// Generate HTML with image tag for emoji
					String html = "<img src=\"" + getClass().getResource(EmojiHashMap.imagePath(emojiKeys[i]))
							+ "\" width=24 height=24 />";
					// Replace emoji key with HTML representatio
					word = word.replace(emojiKeys[i], html);
				}
			}
			
			// Set text of renderer with HTML formatting (wrap string)
			renderer.setText("<html><body>" + word + "</body></html>"); // Clear text
			// Return modified renderer
			return renderer;
		}
	}

	private void connectToServer() {
		// Get server IP from text field and trim leading/trailing whitespace
		serverIP = serverIPField.getText().trim();
		// Get port string from text field and trim leading/trailing whitespace
		portStr = portField.getText().trim();
		// Initialize port variable
		port = 0;

		try {
			port = Integer.parseInt(portStr); 	// Parse the port number as an integer
		} catch (NumberFormatException e) {
			// Show error message if port number is invalid
			JOptionPane.showMessageDialog(this, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
			return; // Exit method if port number is invalid
		}

		if (!serverIP.isEmpty()) {				// Check if server IP is not empty
			
			// Show connecting info message
			JOptionPane.showMessageDialog(this, "Connecting to server at IP: " + serverIP + ", Port: " + port, "Info",
					JOptionPane.INFORMATION_MESSAGE);
			// Prompt for username
			username = JOptionPane.showInputDialog(this, "Enter Username:");
			// Set usernameLabel
			usernameLabel.setText(username);

			// Check username
			/*
			 * if (!validateCredentials(username)) { JOptionPane.showMessageDialog(this,
			 * "Invalid username. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
			 * System.exit(0); // Terminate the application }
			 */

			try {
				// Attempt to establish a connection to the server
				Socket clientS = new Socket(serverIP, port);

				// Prepare message to send with username and "CONNECT" command
				String inputText = username + MessageManager.MESSAGE_PROPERTY_DELIMETER + "CONNECT";
				// Output data
				OutputStream outputToServer = clientS.getOutputStream();
				DataOutputStream outputData = new DataOutputStream(outputToServer);
				outputData.writeUTF(inputText); // Send a command to the server

				//System.out.println("Connected");
				inputPanel.setVisible(true);	// Make input panel visible
				startUpdateTimer();				// Start update timer

				clientS.close();				// Close the socket after connection is established
			} catch (Exception err) {
				// Handle exceptions related to socket communication
				System.out.println("Error in Connection");
			}

		} else {
			// Show error message if server IP is empty
			JOptionPane.showMessageDialog(this, "Please enter the server IP address", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		connectButton.setVisible(false);		// Hide connect button after connection attempt
	}

	private void updateMessage() {
		try {
			// Clear messages in message manager
			mManager.clearMessages();
			// Attempt to establish a connection to the server
			Socket clientS = new Socket(serverIP, port);

			// Output data
			OutputStream outputToServer = clientS.getOutputStream();
			DataOutputStream outputData = new DataOutputStream(outputToServer);
			outputData.writeUTF("UPDATE"); // Send a command to the server

			// INPUT DATA
			InputStream inputFromServer = clientS.getInputStream();
			DataInputStream serverData = new DataInputStream(inputFromServer);

			// Read the drawing information from the server
			String serverString = serverData.readUTF();

			// Split serverString and update messages in message manager
			mManager.splitMessage(serverString);
			
			// Update chat box with new messages
			chatBox.setListData(mManager.getMessages());

			clientS.close();				// Close the socket after updating messages
		} catch (Exception err) {
			// Handle exceptions related to socket communication
			System.out.println("Error in sendMessage");
		}
	}

	// Method to validate user credentials
	/*
	private boolean validateCredentials(String username) {
		// Check if username is valid
		return username.equals("Renzo") || username.equals("Dhuvid") || username.equals("Warin");
	}
	*/

	/*
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new
	 * Runnable() { public void run() { new ChatClientApp(); } }); } }
	 */
	
	// Method to scroll the chat box to the bottom
	public void scrollToBottom() {
		// Get the index of the last message in the chat box
		int lastIndex = chatBox.getModel().getSize() - 1;
		// Check if there are messages in the chat box
		if (lastIndex >= 0) {
			// Ensure the last message is visible in the chat box
			chatBox.ensureIndexIsVisible(lastIndex);
		}

	}
	
	public static void main(String[] args) {
		// Start the chat client application on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> new ChatClientApp());
	}


}
