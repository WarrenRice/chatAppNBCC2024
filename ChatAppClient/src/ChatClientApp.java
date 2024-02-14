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

	private static final long serialVersionUID = 1L;

//Add ENTER KEY listener

	private JList<String> chatBox; // JList for displaying messages
	private JScrollPane chatListScrollPane;
	private JTextField messageInputField; // Text field for typing messages
	private JButton sendButton; // Button to send messages
	private JTextField serverIPField; // Text field for entering server IP address
	private JTextField portField; // Text field for entering server port number
	private JButton connectButton; // Button to connect to the server
	private String username;
	private JLabel usernameLabel;

	private String serverIP;
	private String portStr;
	private int port;

	private JPanel inputPanel;
	private JPanel serverPanel;
	private JPanel mainPanel;

	private MessageManager mManager;

	private Timer updateTimer;

	private boolean initialUpdate = false;

	// private static final String MESSAGE_PROPERTY_DELIMETER = "▐";
	// private static final String MESSAGE_SET_DELIMETER = "†";

	public ChatClientApp() {
		setTitle("Chat Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 300);

		mManager = new MessageManager();

		// Initialize components
		chatBox = new JList<>(mManager.getMessages());
		chatBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chatListScrollPane = new JScrollPane(chatBox); // Create a JScrollPane and add the JList to it
		// Enable vertical scroll bar policy
		chatListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		usernameLabel = new JLabel();
		messageInputField = new JTextField(20);
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
				scrollToBottom();
			}
		});

		// Panel for text input and send button
		inputPanel = new JPanel();
		inputPanel.add(usernameLabel);
		inputPanel.add(messageInputField);
		inputPanel.add(sendButton);

		// Panel for server IP input, port input, and connect button
		serverIPField = new JTextField(15);
		serverIPField.setText("25.42.224.13");
//		serverIPField.setText("localhost");
		portField = new JTextField(5); // Text field for port input
		portField.setText("6066	");
		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectToServer();
			}
		});

		serverPanel = new JPanel();
		serverPanel.add(new JLabel("Server IP: "));
		serverPanel.add(serverIPField);
		serverPanel.add(new JLabel("Port: "));
		serverPanel.add(portField); // Add port text field
		serverPanel.add(connectButton);

		// Layout
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(chatListScrollPane, BorderLayout.CENTER); // Adding chat box to the main panel
		mainPanel.add(inputPanel, BorderLayout.SOUTH); // Adding input panel to the bottom of the main panel
		mainPanel.add(serverPanel, BorderLayout.NORTH); // Adding server panel to the top of the main panel

		getContentPane().add(mainPanel); // Adding main panel to the frame
		EmojiListRenderer emojiListRenderer = new EmojiListRenderer();
		chatBox.setCellRenderer(emojiListRenderer);

		// Add key listener to message input field
		messageInputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		inputPanel.setVisible(false); // hide

		setVisible(true); // Making the frame visible

	}

	private void startUpdateTimer() {
		updateTimer = new Timer();
		updateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sendUpdateMessage();
				if (!initialUpdate) {
					scrollToBottom();
					initialUpdate = true;
				}
			}
		}, 100, 1000); // Delay 0ms, repeat every 1000ms (1 second)
	}

	private void sendUpdateMessage() {
		try {
			// Attempt to establish a connection to the server
			Socket clientS = new Socket(serverIP, port);

			// Output data
			OutputStream outputToServer = clientS.getOutputStream();
			DataOutputStream outputData = new DataOutputStream(outputToServer);
			outputData.writeUTF("UPDATE"); // Send a command to the server

			// System.out.println("Sent UPDATE message to server");
			clientS.close(); // Close the socket after sending the message
		} catch (Exception err) {
			// Handle exceptions related to socket communication
			System.out.println("Error sending UPDATE message to server: " + err.getMessage());
		}

		updateMessage();
	}

	// In ChatClientApp.java, inside the sendMessage method or another appropriate
	// place

	private void sendMessage() {
		String textMessage = messageInputField.getText().trim();
		if (!textMessage.isEmpty()) {

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

			updateMessage();

			// get from server and add

			messageInputField.setText(""); // Clear the message input field

		} else {
			JOptionPane.showMessageDialog(this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public class EmojiListRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			String word = (String) value;
			String[] emojiKeys = EmojiHashMap.getKeys();
			for (int i = 0; i < emojiKeys.length; i++) {
				if (word.contains(emojiKeys[i])) {
					String html = "<img src=\"" + getClass().getResource(EmojiHashMap.imagePath(emojiKeys[i]))
							+ "\" width=24 height=24 />";
					word = word.replace(emojiKeys[i], html);
				}
			}

			renderer.setText("<html><body>" + word + "</body></html>"); // Clear text

			return renderer;
		}
	}

	private void connectToServer() {
		serverIP = serverIPField.getText().trim();
		portStr = portField.getText().trim(); // Retrieve port number from the text field
		port = 0;

		try {
			port = Integer.parseInt(portStr); // Parse the port number as an integer
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
			return; // Exit method if port number is invalid
		}

		if (!serverIP.isEmpty()) {

			JOptionPane.showMessageDialog(this, "Connecting to server at IP: " + serverIP + ", Port: " + port, "Info",
					JOptionPane.INFORMATION_MESSAGE);
			// Prompt for username
			username = JOptionPane.showInputDialog(this, "Enter Username:");
			// Send username to server (To be implemented)
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

				// String inputText = username + MESSAGE_SET_DELIMETER +
				// messageInputField.getText();
				String inputText = username + MessageManager.MESSAGE_PROPERTY_DELIMETER + "CONNECT";
				// Output data
				OutputStream outputToServer = clientS.getOutputStream();
				DataOutputStream outputData = new DataOutputStream(outputToServer);
				outputData.writeUTF(inputText); // Send a command to the server

				System.out.println("Connected");
				inputPanel.setVisible(true);
				startUpdateTimer();

				clientS.close();
			} catch (Exception err) {
				// Handle exceptions related to socket communication
				System.out.println("Error in Connection");
			}

		} else {
			JOptionPane.showMessageDialog(this, "Please enter the server IP address", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		connectButton.setVisible(false);
	}

	private void updateMessage() {
		try {
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

			// System.out.println(serverString);

			mManager.splitMessage(serverString);

			// System.out.println(mManager.messages.size());

			chatBox.setListData(mManager.getMessages());

			clientS.close();
		} catch (Exception err) {
			// Handle exceptions related to socket communication
			System.out.println("Error in sendMessage");
		}
	}

	private boolean validateCredentials(String username) {
		return username.equals("Renzo") || username.equals("Dhuvid") || username.equals("Warin");
	}

	/*
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new
	 * Runnable() { public void run() { new ChatClientApp(); } }); } }
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ChatClientApp());
	}

	public void scrollToBottom() {
		int lastIndex = chatBox.getModel().getSize() - 1;
		if (lastIndex >= 0) {
			chatBox.ensureIndexIsVisible(lastIndex);
		}

	}
}
