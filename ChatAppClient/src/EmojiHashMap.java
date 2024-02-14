
import java.util.HashMap;
import java.util.Map;
public class EmojiHashMap {
	// Folder path where emoji images are stored
    private static final String IMAGES_FOLDER_PATH = "emojis/";
    
    // HashMap to store mappings between emoji codes and image file names
    private static final Map<String, String> emojiMap = new HashMap<>();

    static {
    	// Define emoji image paths and add them to the map
        emojiMap.put(":smile:", "smily.png");
        emojiMap.put(":think:", "think.png");
        emojiMap.put(":laugh:", "laugh.png");
        emojiMap.put(":cry:", "cry.png");
        emojiMap.put(":angry:", "angry.png");
    }
  
    // Method to retrieve the image path for a given emoji code
    public static String imagePath(String key) {
        String fileName = emojiMap.get(key);
        if (fileName != null) {
            return IMAGES_FOLDER_PATH + fileName;
        }
        return null;	// Return null if emoji code is not found
    }
    
    // Main method for testing
    /*
    public static void main(String[] args) {
        String emojiCode = ":smile:";
    
        String imagePath = EmojiHashMap.imagePath(emojiCode);
        if (imagePath != null) {
            System.out.println("Emoji found! Image path: " + imagePath);
        } else {
            System.out.println("Emoji code not found: " + emojiCode);
        }
    }
    */
    
    // Method to retrieve all keys (emoji codes) from the emoji map
    public static String[] getKeys() {
    	// Convert emoji keys to an array and return
    	return emojiMap.keySet().toArray(new String[emojiMap.size()]);
    }

}
