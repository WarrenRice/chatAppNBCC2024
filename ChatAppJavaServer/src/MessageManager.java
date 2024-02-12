import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	public static final String MESSAGE_PROPERTY_DELIMETER = "▐";
	public static final String MESSAGE_SET_DELIMETER = "†";
	private List<Message> messages;
	
	public MessageManager() {
		messages = new ArrayList<>();
	}
	
	public void clearMessages() {
		messages.clear();
	}
	
	/**
	 * 
	 * @param message in the form of "John Doe▐Hello World"
	 */
	public void addMessage(String message) {
		String[] splitMessage = message.split(MESSAGE_PROPERTY_DELIMETER);
		messages.add(new Message(splitMessage[0], splitMessage[1]));
		System.out.println(messages.get(messages.size() - 1));
	}
	
	public String getAllMessages() {
		// use string builder since it's faster than string 
		StringBuilder messageSet = new StringBuilder();
		for (Message m : messages) {
			messageSet.append(m.toString())
				.append(MESSAGE_SET_DELIMETER);
		}
		
		return messageSet.toString();
	}
	
	public void test() {
		addMessage("John Doe▐Hello World");
		addMessage("John Doe▐This Is A message");
		addMessage("Test Renzo▐Hello World qf1  3f12 312 3123d qdfas das fasd f");
		
		System.out.println(getAllMessages());
	}
}
