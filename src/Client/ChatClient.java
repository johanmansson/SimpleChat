package Client;


import java.io.*;
import java.net.*;


/**
 * Created by johanmansson on 15-04-13.
 */
public class ChatClient extends ChatWindow {
    private Socket socket;


    public ChatClient(int x, int y, String title) {
        super(x, y, title);

    }

    public boolean startConnection(String address, String inPort) {
        socket = null;
        try {
            int port = Integer.parseInt(inPort);
            socket = new Socket(address, port);
            System.out.println("Connected to: " + socket.getInetAddress().getHostName());
            GetMessageThread writer = new GetMessageThread(socket);
            writer.start();
            return true;



        } catch(IOException e) {
            System.out.println(e);
            return false;
        }

    }

    public void setName(String name) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("N: " + name);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messageEntered(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("M: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class GetMessageThread extends Thread {
        private Socket socket;

        public GetMessageThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            String getMessage;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((getMessage = in.readLine()) != null ) {
                    System.out.println(getMessage);

                   add(getMessage);


                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }




}
