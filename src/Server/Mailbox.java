package Server;

/**
 * Created by johanmansson on 15-04-12.
 */
public class Mailbox {

    private String message;

    public Mailbox() {
        message = "";
    }

    public synchronized String getMessage() {
        while(message.equals("")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String returnMessage = message;
        message = "";
        notifyAll();
        return returnMessage;

    }

    public synchronized void setMessage(String inMessage) {
        while(!message.equals("")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        message += inMessage;
        notifyAll();

    }





}
