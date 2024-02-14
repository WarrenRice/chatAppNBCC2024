import javax.swing.ImageIcon;

public class ChatMessage {
    // Enum to represent different types of messages (text or image)
    public enum Type {
        TEXT, 		// Message contains text
        IMAGE		// Message contains an image
    }

 // Private fields to store message type, text, and image
    private Type type;			// Type of the message
    private String text;		// Text content of the message
    private ImageIcon image;	// Image content of the messag

 // Constructor for text messages
    public ChatMessage(String text) {
        this.type = Type.TEXT;
        this.text = text;
        this.image = null;		// No image for text messages
    }

    // Constructor for image messages
    public ChatMessage(ImageIcon image) {
        this.type = Type.IMAGE;
        this.text = null;		// No text for image messages
        this.image = image;
    }

    // Getter method for retrieving the type of the message
    public Type getType() {
        return type;
    }

    // Getter method for retrieving the text content of the message
    public String getText() {
        return text;
    }

    // Getter method for retrieving the image content of the message
    public ImageIcon getImage() {
        return image;
    }

}
