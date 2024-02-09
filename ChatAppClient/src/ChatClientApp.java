import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ChatClientApp extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private JList<String> chatBox;  // JList for displaying messages
    private DefaultListModel<String> messageListModel;  // DefaultListModel to store messages
    private JTextField messageInputField;  // Text field for typing messages
    private JButton sendButton;  // Button to send messages
    private JTextField serverIPField;  // Text field for entering server IP address
    private JButton connectButton;  // Button to connect to the server
    private String username;
    private JLabel usernameLabel;
    public ChatClientApp() {

    	
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

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
        JPanel inputPanel = new JPanel();
        inputPanel.add(usernameLabel);
        inputPanel.add(messageInputField);
        inputPanel.add(sendButton);

        // Panel for server IP input and connect button
        serverIPField = new JTextField(15);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        JPanel serverPanel = new JPanel();
        serverPanel.add(new JLabel("Server IP: "));
        serverPanel.add(serverIPField);
        serverPanel.add(connectButton);

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(userListScrollPane, BorderLayout.CENTER);  // Adding chat box to the main panel
        mainPanel.add(inputPanel, BorderLayout.SOUTH);  // Adding input panel to the bottom of the main panel
        mainPanel.add(serverPanel, BorderLayout.NORTH);  // Adding server panel to the top of the main panel

        getContentPane().add(mainPanel);  // Adding main panel to the frame
        EmojiListRenderer emojiListRenderer = new EmojiListRenderer();
        chatBox.setCellRenderer(emojiListRenderer);
        setVisible(true);  // Making the frame visible
    }

 // In ChatClientApp.java, inside the sendMessage method or another appropriate place

    private void sendMessage() {
        String message = messageInputField.getText().trim();
        if (!message.isEmpty()) {
            if (message.startsWith("image:")) {
                // If the message starts with "image:", it's an image message
                String imagePath = message.substring(6); // Remove "image:" prefix
                messageListModel.addElement(imagePath); // Add the image message to the list model
               
            } else {
                // Otherwise, it's a text message
                messageListModel.addElement(message); // Add the text message to the list model
            }
            messageInputField.setText(""); // Clear the message input field
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


	public class EmojiListRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            String word = (String) value;
            String imagePath = EmojiHashMap.imagePath(word);
            if (imagePath != null) {
                ImageIcon icon = new ImageIcon(imagePath);
                icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                renderer.setIcon(icon);
                renderer.setText(""); // Clear text
            }

            return renderer;
        }
    }

 private void connectToServer() {
        String serverIP = serverIPField.getText().trim();
        if (!serverIP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Connecting to server at IP: " + serverIP, "Info", JOptionPane.INFORMATION_MESSAGE);
            
            // Prompt for username
            username = JOptionPane.showInputDialog(this, "Enter Username:");
            
            //send username to server
            
            //set usernameLabel
            usernameLabel.setText(username);
            
            // Check username
            if (!validateCredentials(username)) {
                JOptionPane.showMessageDialog(this, "Invalid username. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0); // Terminate the application
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Please enter the server IP address", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateCredentials(String username) {
    	
        return username.equals("renzo") || username.equals("dhruvit") || username.equals("warren");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClientApp();
            }
        });
    }
}