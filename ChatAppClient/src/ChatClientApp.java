import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClientApp extends JFrame {
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JTextField messageField;
    private JButton sendButton;

    public ChatClientApp() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Initialize components
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userListScrollPane = new JScrollPane(userList);

        messageField = new JTextField(20);
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(userListScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Sample users
        userListModel.addElement("User 1");
        userListModel.addElement("User 2");
        userListModel.addElement("User 3");

        setVisible(true);
    }

    private void sendMessage() {
        String selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                // In a real application, you would send this message to the selected user
                System.out.println("Sending message to " + selectedUser + ": " + message);
                messageField.setText(""); // Clear the message field after sending
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a message", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
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
