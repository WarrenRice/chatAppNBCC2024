import javax.swing.ImageIcon;

public class ChatMessage {
    public enum Type {
        TEXT, IMAGE
    }

    private Type type;
    private String text;
    private ImageIcon image;

    // Constructor for text messages
    public ChatMessage(String text) {
        this.type = Type.TEXT;
        this.text = text;
        this.image = null;
    }

    // Constructor for image messages
    public ChatMessage(ImageIcon image) {
        this.type = Type.IMAGE;
        this.text = null;
        this.image = image;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public ImageIcon getImage() {
        return image;
    }
    

   
}
