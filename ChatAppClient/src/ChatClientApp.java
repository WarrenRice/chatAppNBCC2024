import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
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


//Add ENTER KEY listener

public class ChatClientApp extends JFrame {
    private JList<String> chatBox;  // JList for displaying messages
    private DefaultListModel<String> messageListModel;  // DefaultListModel to store messages
    private JTextField messageInputField;  // Text field for typing messages
    private JButton sendButton;  // Button to send messages
    private JTextField serverIPField;  // Text field for entering server IP address
    private JTextField portField; // Text field for entering server port number
    private JButton connectButton;  // Button to connect to the server
    private String username;
    private JLabel usernameLabel;
    
    private String serverIP;
    private String portStr;
    private int port;
    
    private JPanel inputPanel;
    private JPanel serverPanel;
    private JPanel mainPanel;
    
    private MessageManager mManager;
    
    //private static final String MESSAGE_PROPERTY_DELIMETER = "▐";
    //private static final String MESSAGE_SET_DELIMETER = "†";
    
    public ChatClientApp() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);

        mManager = new MessageManager();
        
        // Initialize components
        messageListModel = new DefaultListModel<>();
        chatBox = new JList<>(messageListModel);
        chatBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userListScrollPane = new JScrollPane(chatBox);

        usernameLabel = new JLabel();
        messageInputField = new JTextField(20);
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Panel for text input and send button
        inputPanel = new JPanel();
        inputPanel.add(usernameLabel);
        inputPanel.add(messageInputField);
        inputPanel.add(sendButton);
        

        // Panel for server IP input, port input, and connect button
        serverIPField = new JTextField(15);
        //serverIPField.setText("25.42.224.13");
        serverIPField.setText("localhost");
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
        mainPanel.add(userListScrollPane, BorderLayout.CENTER);  // Adding chat box to the main panel
        mainPanel.add(inputPanel, BorderLayout.SOUTH);  // Adding input panel to the bottom of the main panel
        mainPanel.add(serverPanel, BorderLayout.NORTH);  // Adding server panel to the top of the main panel

        getContentPane().add(mainPanel);  // Adding main panel to the frame

        // Add key listener to message input field
        messageInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        
        inputPanel.setVisible(false);	//hide
        
        setVisible(true);  // Making the frame visible
    }

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

                
            } catch (Exception err) {
                // Handle exceptions related to socket communication
                System.out.println("Error in sendMessage");
            }
        	
        	updateMessage();

        	//get from server and add
        	
        	//messageListModel.addElement(textMessage);  // Add message to the list model
            messageInputField.setText("");  // Clear the message input field
        
        
        
        
        
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
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
            
        	JOptionPane.showMessageDialog(this, "Connecting to server at IP: " + serverIP + ", Port: " + port, "Info", JOptionPane.INFORMATION_MESSAGE);
            // Prompt for username
            username = JOptionPane.showInputDialog(this, "Enter Username:");
            // Send username to server (To be implemented)
            // Set usernameLabel
            usernameLabel.setText(username);
            
            // Check username
            /*
            if (!validateCredentials(username)) {
                JOptionPane.showMessageDialog(this, "Invalid username. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0); // Terminate the application
            }
            */
            
            try {
                // Attempt to establish a connection to the server
                Socket clientS = new Socket(serverIP, port);

                //String inputText = username + MESSAGE_SET_DELIMETER + messageInputField.getText();
                String inputText = username + MessageManager.MESSAGE_PROPERTY_DELIMETER + "CONNECT";
                // Output data
                OutputStream outputToServer = clientS.getOutputStream();
                DataOutputStream outputData = new DataOutputStream(outputToServer);
                outputData.writeUTF(inputText); // Send a command to the server

                System.out.println("Connected");
                inputPanel.setVisible(true);
                
            } catch (Exception err) {
                // Handle exceptions related to socket communication
                System.out.println("Error in Connection");
            }
            

            
        } else {
            JOptionPane.showMessageDialog(this, "Please enter the server IP address", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void updateMessage() {
        try {
        	messageListModel.clear();
        	mManager.clearMessages();
            // Attempt to establish a connection to the server
            Socket clientS = new Socket(serverIP, port);

            // Output data
            OutputStream outputToServer = clientS.getOutputStream();
            DataOutputStream outputData = new DataOutputStream(outputToServer);
            outputData.writeUTF("UPDATE"); // Send a command to the server
            
			//INPUT DATA
			InputStream inputFromServer = clientS.getInputStream();
			DataInputStream serverData = new DataInputStream(inputFromServer);
			
			// Read the drawing information from the server
			String serverString = serverData.readUTF();
            
			System.out.println(serverString);
			
			mManager.splitMessage(serverString);
			
			System.out.println(mManager.messages.size());
			
			for (String s : mManager.messages) {
				System.out.println(s);
				messageListModel.addElement(s);
			}

            
        } catch (Exception err) {
            // Handle exceptions related to socket communication
            System.out.println("Error in sendMessage");
        }
    }
    
    private boolean validateCredentials(String username) {
        return username.equals("Renzo") || username.equals("Dhuvid") || username.equals("Warin");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClientApp();
            }
        });
    }
}
