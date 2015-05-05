package Server;

//import com.sun.deploy.util.SessionState;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Emil on 03/05/15.
 */
public class UpdateThread extends Thread{
    private Vector<ClientThread> clients;

    public UpdateThread(Vector<ClientThread> clients){
        this.clients = clients;
    }

    public void run(){
        while(true){
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                //Do something?
            }
            updateClients();

        }
    }


    private void updateClients(){
        //To avoid ConcurrentModificationException iterate over list instead of vector
        ArrayList<ClientThread> clientList = new ArrayList<ClientThread>(clients);
        for(ClientThread thread : clientList) {
            if (thread.getSocket().isClosed()) {
                clients.remove(thread); //might need this to be synchronized through a container
            }
        }

        ArrayList<ClientThread> clientList2 = new ArrayList<ClientThread>(clients);
        for(ClientThread thread: clientList2) {
            thread.sendFlushCommand();
            thread.sendPeopleList();
        }
        System.out.println(clients.size());

    }



}
