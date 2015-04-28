package Server;


import java.util.Vector;

/**
 * Created by johanmansson on 15-04-12.
 */
public class MailboxReader extends Thread {
    private Mailbox mailBox;
    private Vector<ClientThread> clients;

    public MailboxReader(Mailbox mailBox, Vector<ClientThread> clients) {

        this.mailBox = mailBox;
        this.clients = clients;
    }

    public void run() {
        while (true) {

            String message = mailBox.getMessage();
            for (ClientThread ch : clients) {
                ch.writeToClient(message);
            }
            try {
                sleep(new Double(Math.random()).longValue());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }









}
