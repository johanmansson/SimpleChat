package Client;

import java.net.InetAddress;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.FileDialog;

/**
 * Created by Emil on 19/05/15.
 */
public class SendFileThread extends  Thread{

    private InetAddress inetAddress;
    private int port;

    public SendFileThread(InetAddress address, int port){
        inetAddress = address;
        this.port = port;
    }

    public void run(){
        String message = "";
        JFrame frame = new JFrame("Choose File to send");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        //fd.setDirectory("C:\\");
        fd.setFile("*.xml");
        fd.setVisible(true);
        String filename = fd.getFile();
        String dirName = fd.getDirectory();
        File file = null;
        if (filename == null){
            System.out.println("You cancelled the choice");
            message = "abort";
        }else{
            message = filename;
            file = new File(dirName + filename);
        }



        frame.dispose();


        Socket socket = null;
        OutputStream socketOs = null;
        try {
            socket = new Socket(inetAddress, port);
            socketOs = socket.getOutputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        BufferedOutputStream out = null;
        try{
            out = new BufferedOutputStream(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }


        PrintWriter pw = new PrintWriter(socketOs);
        //System.out.print(file.getName());
        //pw.print(file.getName());
        pw.print(message);
        pw.print('\n');
        pw.flush();
        //pw.close();


        if(! message.equals("abort")){
            //if(out != null){


            byte[] mybytearray = new byte[(int) file.length()];
            FileInputStream fis = null;
            try{
                //!!!!!
                fis = new FileInputStream(file);
            }catch(FileNotFoundException ex){
                ex.printStackTrace();
            }
            BufferedInputStream bis = new BufferedInputStream(fis);



            try{
                //Reads info from bis into byteArray
                bis.read(mybytearray,0,mybytearray.length);
                //Write to stream

                out.write(mybytearray,0,mybytearray.length);
                out.flush();
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }else{
            System.out.println("out is null");
        }
    }

}
