
import java.util.HashMap;
import java.util.Map;
public class EmojiHashMap {
    private static final String IMAGES_FOLDER_PATH = "emojis/";

    private static final Map<String, String> emojiMap = new HashMap<>();

    static {
        // Define emoji image paths
        emojiMap.put(":smile:", "smily.png");
        emojiMap.put(":think:", "think.png");
        emojiMap.put(":laugh:", "laugh.png");
        emojiMap.put(":cry:", "cry.png");
        emojiMap.put(":angry:", "angry.png");
    }
  

    public static String imagePath(String key) {
        String fileName = emojiMap.get(key);
        if (fileName != null) {
            return IMAGES_FOLDER_PATH + fileName;
        }
        return null;
    }
    public static void main(String[] args) {
        String emojiCode = ":smile:";
    
        String imagePath = EmojiHashMap.imagePath(emojiCode);
        if (imagePath != null) {
            System.out.println("Emoji found! Image path: " + imagePath);
        } else {
            System.out.println("Emoji code not found: " + emojiCode);
        }
    }
    
    public static String[] getKeys() {
    	return emojiMap.keySet().toArray(new String[emojiMap.size()]);
    }

}
