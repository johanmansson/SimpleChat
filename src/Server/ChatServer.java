package Server;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by johanmansson on 15-04-12.
 */
public class ChatServer {
    private ServerSocket serverSocket;
    private Mailbox mailBox;
    private Vector<ClientThread> clients;



    public static void main(String[] args) {
        if(args.length!=1) {
            System.out.println("Usage: java ChatServer <port number>");
            System.exit(1);
        }
        int portNbr = Integer.parseInt(args[0]);
        int maxNumberOfClients = 10;
        new ChatServer(portNbr, maxNumberOfClients);

    }

    public ChatServer(int portNbr, int maxNumberOfClients) {
        super();
        mailBox = new Mailbox();
        clients = new Vector<ClientThread>();
        MailboxReader reader = new MailboxReader(mailBox, clients);

        ExecutorService pool = Executors.newFixedThreadPool(maxNumberOfClients);

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(portNbr));
            System.out.println("Server running...");
            reader.start();


            while(true) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket, mailBox);
                clients.add(clientThread);
                pool.submit(clientThread);


            }




        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


    }







}
