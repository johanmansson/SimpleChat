package Server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.Random;

/**
 * Created by johanmansson on 15-04-12.
 */
public class ClientThread extends Thread {

    private Socket clientSocket;
    private Mailbox mailBox;
    private String name;
    private SimpleDateFormat timeStamp;
    private Random rng;
    public Vector<ClientThread> clients;


    public ClientThread(Socket clientSocket, Mailbox mailBox, Vector<ClientThread> clients) {

        this.clientSocket = clientSocket;
        this.mailBox = mailBox;
        this.clients = clients;
        name = "Anonymous";
        timeStamp = new SimpleDateFormat("HH.mm");
        rng = new Random();



    }


    public void run() {

        System.out.println("New connection from: " + clientSocket.getInetAddress().getHostName() + ": " + clientSocket.getPort());

        try {
            String message = null;
            Boolean isRunning = true;

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (isRunning && (message = in.readLine()) != null) {

                if (message.startsWith("Q:")) {
                    isRunning = false;

                }
                if (message.startsWith("N:")) {
                    name = message.substring(3);


                }
                if (message.startsWith("M:")) {
                    mailBox.setMessage(message);
                    //mailBox.setMessage("M:" + timeStamp.format(Calendar.getInstance().getTime()) + " " + name + ": " + message.substring(3));
                }

                if( message.startsWith("F:")){
                    String[] parts = message.split(":");
                    String senderString = parts[1];
                    String receiverString = parts[2];
                    ClientThread receiver = findClient(receiverString, clients);
                    Socket receiveSocket = receiver.getSocket();
                    InetAddress address = receiveSocket.getInetAddress();
                    int port = 30001 + rng.nextInt(10000);

                    try {
                        receiver.writeToClient("R:" + port + ":" + address.toString() + ":" + senderString);
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }

                }
                if(message.startsWith("F2:")){
                    System.out.println("Test - ServerF2");
                    String[] parts = message.split(":");
                    int port = Integer.parseInt(parts[1]);
                    String address = parts[2];
                    String senderString = parts[3];
                    ClientThread sender = findClient(senderString,clients);
                    try {
                        sender.writeToClient("S:" + port + ":" + address.toString());
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }

                }
                if(message.startsWith("F3:")){
                    String[] parts = message.split(":");
                    ClientThread sender = findClient(parts[1],clients);
                    try {
                        sender.writeToClient("S2:");
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                }

            }


            System.out.println("Connection closed from: " + clientSocket.getInetAddress().getHostName() + ": " + clientSocket.getPort() + ": Name: " + name);
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendPeopleList() {

        for (ClientThread ch : clients) {
            String name = ch.getUserName();
            if (name != "Anonymous") {
                writeToClient("P: " + name);
            }
        }
    }

    public void sendFlushCommand() {
        writeToClient("flush_SOME_CODE_TO_NOT_ACCIDENTALLY_WRITE_COMMAND");
    }


    public void writeToClient(String message) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
            out.flush();


        } catch (IOException e) {
            System.out.println(e);
        }


    }

    public String getUserName() {
        return name;
    }

    public Socket getSocket() {
        return clientSocket;
    }

    private ClientThread findClient(String name, Vector<ClientThread> clients){
        for(ClientThread ct : clients){
            String clientName = ct.getUserName();
            if (clientName.equals(name)){
                return ct;
            }
        }
        return null; //should never happen
    }


}
