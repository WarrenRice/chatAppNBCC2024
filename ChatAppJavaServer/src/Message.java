import java.util.Date;

public class Message {
	// Private fields to store message properties
	private String text;	// Content of the message
	private String sender;	// Sender of the message
	private Date sent;		// Timestamp indicating when the message was sent
	
	// Constructor to initialize the message with sender and message text
	public Message(String sender, String messageText) {
		this.sender = sender;
		this.text = messageText;
		this.sent = new Date();	// Set the sent timestamp to the current date and time
	}
	
	// Getter method for retrieving the message text
	public String getText() {
		return text;
	}
	
	// Setter method for setting the message text
	public void setText(String text) {
		this.text = text;
	}
	
	// Getter method for retrieving the sender of the message
	public String getSender() {
		return sender;
	}
	
	// Setter method for setting the sender of the message
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	// Getter method for retrieving the timestamp when the message was sent
	public Date getSent() {
		return sent;
	}
	
	// Setter method for setting the timestamp when the message was sent
	public void setSent(Date sent) {
		this.sent = sent;
	}
	
	// Override toString method to represent the message as a string
	public String toString() {
		// Format the message as sender|text|sentTimestamp
		return sender + MessageManager.MESSAGE_PROPERTY_DELIMETER +
				text + MessageManager.MESSAGE_PROPERTY_DELIMETER +
				sent;
	}
	
}
