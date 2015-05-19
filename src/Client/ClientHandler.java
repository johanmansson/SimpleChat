package Client;


import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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

    public String getUserName() { return userName; }

    public Socket getSocket() {return socket; }

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
                        sb.append(inputParts[3]);

                        if(inputParts.length > 3) {
                            for(int i = 4; i < inputParts.length; i++) {
                                sb.append(":" + inputParts[i]);
                            }
                        }

                        String message = sb.toString();



                        SimpleDateFormat timeStamp = new SimpleDateFormat("HH.mm");
                        String time = timeStamp.format(Calendar.getInstance().getTime());

                        for(NewChatWindow nCW: chatWindows) {
                            if(nCW.getTitle().equals(inputParts[1])) {
                                nCW.add(time + ": " + nCW.getSendFrom() + ": " + message);
                            }
                            if(nCW.getTitle().equals(inputParts[2])) {
                                nCW.add(time + ": " + nCW.getTitle() + ": " + message);
                            }

                        }


                    }
                    if (input.startsWith("R:")){
                        //Filöverföring

                        String parts[] = input.split(":");
                        int port = Integer.parseInt(parts[1]);
                        String address = parts[2];
                        String senderString = parts[3];

                        JFrame frame = new JFrame("Attention!");
                        //Option to not receive
                        //JOptionPane.showMessageDialog(frame, "User " + senderString + " is sending you a file");
                        int selectedOption = JOptionPane.showConfirmDialog(null, "User " + senderString + " wants to send you a file. Accept?", "Accept?", JOptionPane.YES_NO_OPTION);
                        if(selectedOption == JOptionPane.YES_OPTION) {


                            ReceiveFileThread receiveFileThread = new ReceiveFileThread(port);
                            receiveFileThread.start();

                            //Send message to server to confirm readyness
                            OutputStream os = null;
                            try {
                                os = socket.getOutputStream();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                            PrintWriter out = new PrintWriter(os, true);
                            out.println("F2:" + port + ":" + address + ":" + senderString);
                        }else{
                            OutputStream os = null;
                            try {
                                os = socket.getOutputStream();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                            PrintWriter out = new PrintWriter(os, true);
                            out.println("F3:"+ senderString);
                        }
                    }

                    if(input.startsWith("S:")){
                        System.out.println("Test - S innan tråd");

                        String parts[] = input.split(":");
                        int port = Integer.parseInt(parts[1]);
                        String addressString = parts[2].substring(parts[2].indexOf('/') + 1);
                        System.out.println(addressString);
                        InetAddress address = InetAddress.getByName(addressString);
                        System.out.println("test efter getByName");
                        SendFileThread sendFileThread = new SendFileThread(address,port);
                        sendFileThread.start();
                    }

                    if(input.startsWith("S2:")){
                        JFrame frame = new JFrame("Attention!");
                        JOptionPane.showMessageDialog(frame, "The person you want to send files to does not want them.");
                    }


                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}
