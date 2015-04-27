package Server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by johanmansson on 15-04-12.
 */
public class ClientThread extends Thread {

    private Socket clientSocket;
    private Mailbox mailBox;
    private String name;
    private SimpleDateFormat timeStamp;


    public ClientThread(Socket clientSocket, Mailbox mailBox) {

        this.clientSocket = clientSocket;
        this.mailBox = mailBox;
        name = "Anonymous";
        timeStamp = new SimpleDateFormat("HH.mm");



    }

    public void run() {

        System.out.println("New connection from: " + clientSocket.getInetAddress().getHostName() +": " + clientSocket.getPort());

        try {
            String message = null;
            Boolean isRunning = true;

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(isRunning && (message = in.readLine()) != null) {

                if (message.startsWith("Q:")) {
                    isRunning = false;

                }
                if (message.startsWith("N:")) {
                    name = message.substring(3);



                }
                if (message.startsWith("M:")) {

                    mailBox.setMessage(timeStamp.format(Calendar.getInstance().getTime()) + " " + name + ": " + message.substring(3));
                }

            }


            System.out.println("Connection closed from: " + clientSocket.getInetAddress().getHostName() + ": " + clientSocket.getPort() + ": Name: " + name);
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



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




}