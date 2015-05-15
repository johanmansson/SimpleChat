package Client;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by johanmansson on 15-05-07.
 */
public class NewChatWindow extends ChatWindow {
    private Socket socket;

    public NewChatWindow(int x, int y, String title, Socket socket) {
        super(x, y, title);
        this.socket = socket;

    }

    public void messageEntered(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("M: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
