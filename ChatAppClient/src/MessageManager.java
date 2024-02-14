import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	// Delimiters used for message properties and message sets
	public static final String MESSAGE_PROPERTY_DELIMETER = "▐";
	public static final String MESSAGE_SET_DELIMETER = "†";
	// List to store messages
	public List<String> messages;
	
	// Constructor initializes the message list
	public MessageManager() {
		messages = new ArrayList<>();
	}
	
	// Method to clear all messages
	public void clearMessages() {
		messages.clear();
	}
	
	// Method to split a received message and add its parts to the messages list
	public void splitMessage(String message) {
		// Split the message using the message set delimiter
		String[] splitMessages = message.split(MESSAGE_SET_DELIMETER);
		
		// Iterate through each split message
		for (String s : splitMessages) {
			// Check if the message does not contain the UPDATE command
			if (!s.contains("▐UPDATE▐")) {
				// Replace the first "▐ " with ":"
		        s = s.replaceFirst("▐", ":   ");
		        // Replace the second "▐ " with "<-"
		        s = s.replaceFirst("▐", "   <-");
		        // Add the formatted message to the messages list
				messages.add(s);
			}
		}
	}
	
	// Method to retrieve all messages as an array
	public String[] getMessages() {
		// Convert the list of messages to an array and return
		String[] arr = new String[messages.size()];
		return messages.toArray(arr);
	}
}