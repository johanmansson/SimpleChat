package Client;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by johanmansson on 15-05-07.
 */
public class NewChatWindow extends ChatWindow {
    private Socket socket;
    private String sendFrom;

    public NewChatWindow(int x, int y, String sendTo, String sendFrom, Socket socket) {
        super(x, y, sendTo);
        this.socket = socket;
        this.sendFrom = sendFrom;

    }

    public void messageEntered(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("M:" + getTitle() + ":" + sendFrom + ":" + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSendFrom() {
        return sendFrom;
    }


}
