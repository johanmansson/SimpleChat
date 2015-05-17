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
    private String userName;





    public ClientHandler() {

        isConnected = false;
        people = new ArrayList<String>();
        chatWindows = new ArrayList<NewChatWindow>();




    }

    public Boolean isConnected() {
        return isConnected;
    }

    public String startConnection(String address, String inPort, String name) {
        socket = null;
        String message = "Connected to server!";
        userName = name;

        try {
            int port = Integer.parseInt(inPort);
            socket = new Socket(address, port);
            System.out.println("Connected to: " + socket.getInetAddress().getHostName());
            GetInputThread writer = new GetInputThread(socket);
            writer.start();

            isConnected = true;

            while(true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String testName : people) {
                    if (testName.equals(name)) {
                        message = "Username is already taken!";
                        isConnected = false;
                        writer.killThread();
                        writer.join();



                    }
                }
                break;
            }

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            if(isConnected()) {
                out.println("N: " + name);

            }



        } catch(IOException e) {
            System.out.println(e);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }


        return message;
       

    }


    public void newChatWindow(String name) {
        Boolean test = false;

        for(int i = 0; i < chatWindows.size(); i++) {
            if(chatWindows.get(i).getTitle().startsWith(name)) {
                test = true;
                chatWindows.get(i).show();

            }
        }

        if(test == false) {
            NewChatWindow temp = new NewChatWindow(100,100,name, userName, socket);
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
        private Boolean killThread;

        public GetInputThread(Socket socket) {
            this.socket = socket;
            killThread = false;

        }

        public void killThread() {
            killThread = true;
        }

        public void run() {
            String input;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while ((input = in.readLine()) != null && killThread == false) {

                    if (input.startsWith("P:")) {
                        people.add(input.substring(3));
                    }
                    if (input.equals("flush_SOME_CODE_TO_NOT_ACCIDENTALLY_WRITE_COMMAND")) {
                        flushPeople();
                    }
                    if (input.startsWith("M:")) {
                        System.out.println(input);

                        String[] inputParts = input.split(":");

                        if(inputParts[1].equals(userName)) {
                            newChatWindow(inputParts[2]);

                        }
                        StringBuilder sb = new StringBuilder();
                        for(int i = 3; i < inputParts.length; i++) {
                            sb.append(inputParts[i]);
                        }
                        String message = sb.toString();


                        for(NewChatWindow nCW: chatWindows) {
                            if(nCW.getTitle().equals(inputParts[1])) {
                                nCW.add(nCW.getSendFrom() + ": " + message);
                            }
                            if(nCW.getTitle().equals(inputParts[2])) {
                                nCW.add(nCW.getTitle() + ": " + message);
                            }

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
