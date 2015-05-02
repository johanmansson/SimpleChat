package Client;


import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 * Created by johanmansson on 15-04-13.
 */
public class ChatClient extends ChatWindow {
    private Socket socket;
    private Boolean isConnected;
    private ArrayList<String> people;


    public ChatClient(int x, int y, String title) {
        super(x, y, title);
        isConnected = false;
        people = new ArrayList<String>();

    }

    public Boolean isConnected() {
        return isConnected;
    }

    public void startConnection(String address, String inPort) {
        socket = null;
        try {
            int port = Integer.parseInt(inPort);
            socket = new Socket(address, port);
            System.out.println("Connected to: " + socket.getInetAddress().getHostName());
            GetMessageThread writer = new GetMessageThread(socket);
            writer.start();
            isConnected = true;



        } catch(IOException e) {
            System.out.println(e);

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

    public ArrayList<String> getPeople() {
        return people;
    }

    public void flushPeople(){
        people = new ArrayList<String>();
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
                    if(getMessage.startsWith("P:")) {
                        people.add(getMessage.substring(3));
                    } else if(getMessage.equals("flush_SOME_CODE_TO_NOT_ACCIDENTALLY_WRITE_COMMAND")){
                        flushPeople();
                    }else{
                        System.out.println(getMessage);
                        add(getMessage);
                    }

                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }




}
