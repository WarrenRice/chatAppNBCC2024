import java.util.Date;

public class Message {
	private String text;
	private String sender;
	private Date sent;
	
	public Message(String sender, String messageText) {
		this.sender = sender;
		this.text = messageText;
		this.sent = new Date();
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getSent() {
		return sent;
	}
	
	public void setSent(Date sent) {
		this.sent = sent;
	}
	
	public String toString() {
		return sender + MessageManager.MESSAGE_PROPERTY_DELIMETER +
				text + MessageManager.MESSAGE_PROPERTY_DELIMETER +
				sent;
	}
	
}
