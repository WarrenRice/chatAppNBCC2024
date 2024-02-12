import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	public static final String MESSAGE_PROPERTY_DELIMETER = "▐";
	public static final String MESSAGE_SET_DELIMETER = "†";
	public List<String> messages;
	
	public MessageManager() {
		messages = new ArrayList<>();
	}
	
	public void clearMessages() {
		messages.clear();
	}
	
	public void splitMessage(String message) {
		String[] splitMessages = message.split(MESSAGE_SET_DELIMETER);
		
		for (String s : splitMessages) {
			if (!s.contains("▐UPDATE▐")) {
				// Replace the first "▐ " with ":"
		        s = s.replaceFirst("▐", ":   ");
		        // Replace the second "▐ " with "<-"
		        s = s.replaceFirst("▐", "   <-");
		        //System.out.println(s);
				messages.add(s);
			}
		}
	}
}