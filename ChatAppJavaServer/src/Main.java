import java.io.IOException;

public class Main {
	final static int PORT = 6066;
	public static void main(String[] args) {
		try {
			Thread t = new Server(PORT);
			t.start();										// fires up a run() method
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
