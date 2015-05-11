package Client;


import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 * Created by johanmansson on 15-04-13.
 */
public class ClientHandler {
    private Socket socket;
    private Boolean isConnected;
    private ArrayList<String> people;
    private ArrayList<NewChatWindow> chatWindows;


    public ClientHandler() {

        isConnected = false;
        people = new ArrayList<String>();
        chatWindows = new ArrayList<NewChatWindow>();

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
            GetInputThread writer = new GetInputThread(socket);
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

    public void newChatWindow(String name) {
        Boolean test = false;

        for(int i = 0; i < chatWindows.size(); i++) {
            if(chatWindows.get(i).getTitle() == name) {
                test = true;
                chatWindows.get(i).show();
            }
        }

        if(test == false) {
            NewChatWindow temp = new NewChatWindow(100,100,name, socket);
            temp.show();
            chatWindows.add(temp);

        }

        System.out.println("Total number of chatwindows: " + chatWindows.size());
        System.out.println("Status: " + test);

    }


    public ArrayList<String> getPeople() {
        return people;
    }

    public void flushPeople(){
        people = new ArrayList<String>();
    }


    public class GetInputThread extends Thread {
        private Socket socket;

        public GetInputThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            String input;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while ((input = in.readLine()) != null ) {

                    if (input.startsWith("P:")) {
                        people.add(input.substring(3));
                    }
                    if (input.equals("flush_SOME_CODE_TO_NOT_ACCIDENTALLY_WRITE_COMMAND")) {
                        flushPeople();
                    }
                    if (input.startsWith("M:")) {
                        //System.out.println(input.substring(3));
                        for(NewChatWindow nCW: chatWindows) {
                            nCW.add(input.substring(3));
                        }
                    }


                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}
