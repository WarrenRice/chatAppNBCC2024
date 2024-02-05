import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClientApp extends JFrame {
    private JList<String> chatBox;  // JList for displaying messages
    private DefaultListModel<String> messageListModel;  // DefaultListModel to store messages
    private JTextField messageInputField;  // Text field for typing messages
    private JButton sendButton;  // Button to send messages
    private JTextField serverIPField;  // Text field for entering server IP address
    private JButton connectButton;  // Button to connect to the server

    public ChatClientApp() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Initialize components
        messageListModel = new DefaultListModel<>();
        chatBox = new JList<>(messageListModel);
        chatBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userListScrollPane = new JScrollPane(chatBox);

        messageInputField = new JTextField(20);
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Panel for text input and send button
        JPanel inputPanel = new JPanel();
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

        setVisible(true);  // Making the frame visible
    }

    private void sendMessage() {
        String task = messageInputField.getText().trim();
        if (!task.isEmpty()) {
            messageListModel.addElement(task);  // Add message to the list model
            messageInputField.setText("");  // Clear the message input field
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void connectToServer() {
        String serverIP = serverIPField.getText().trim();
        if (!serverIP.isEmpty()) {
            // Add code here to establish connection to the server using the provided IP address
            JOptionPane.showMessageDialog(this, "Connecting to server at IP: " + serverIP, "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter the server IP address", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClientApp();
            }
        });
    }
}
