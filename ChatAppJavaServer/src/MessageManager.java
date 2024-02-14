import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	// Delimiters used for message properties and message sets
	public static final String MESSAGE_PROPERTY_DELIMETER = "▐";
	public static final String MESSAGE_SET_DELIMETER = "†";
	
	// List to store messages
	private List<Message> messages;
	
	// Constructor initializes the message list
	public MessageManager() {
		messages = new ArrayList<>();
	}
	
	// Method to clear all messages
	public void clearMessages() {
		messages.clear();
	}
	

	// Method to add a message to the message list
	public void addMessage(String message) {
		// Split the message into sender and text using the property delimiter
		String[] splitMessage = message.split(MESSAGE_PROPERTY_DELIMETER);
		// Create a new Message object and add it to the messages list
		messages.add(new Message(splitMessage[0], splitMessage[1]));
		// Print the newly added message
		System.out.println(messages.get(messages.size() - 1));
	}
	
	// Method to get all messages as a formatted string
	public String getAllMessages() {
		// Use StringBuilder for efficient string concatenation
		StringBuilder messageSet = new StringBuilder();
		// Iterate through each message and append it to the message set string
		for (Message m : messages) {
			messageSet.append(m.toString())
				.append(MESSAGE_SET_DELIMETER);
		}
		// Return the formatted message set string
		return messageSet.toString();
	}
}
