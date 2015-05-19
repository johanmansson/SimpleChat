package Client;

/**
 * Created by Emil on 19/05/15.
 */

import java.net.*;
import java.io.*;
import java.lang.*;
import java.io.ByteArrayOutputStream;
import javax.swing.*;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JOptionPane;
import java.awt.FileDialog;



public class ReceiveFileThread extends Thread{

    private int port;

    public ReceiveFileThread(int port){
        this.port = port;
    }

    public void run(){
        System.out.println("test - R");


        try{
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();

            String inputString = new String();
            char inChar;
            String command = new String();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();


            //Somewhat ugly stuff to deal with first input character??
            inChar = (char) inputStream.read();
            while(inChar != '\n') {
                inputString = inputString + inChar;
                inChar = (char) inputStream.read(); //Somehow int -> char, bytes n stuff
            }
            if(inputString.equals("abort")){
                JFrame aFrame = new JFrame();
                JOptionPane.showMessageDialog(aFrame, "Sender canceled the process");

            }





            if(!inputString.equals("abort")) {
                String directory = choosePlace();
                if (!directory.equals("abort")) {
                    String path = fixName(directory, inputString);
                    if (!path.equals("CODE_FOR_ABORT")) {
                        byte[] aByte = new byte[1]; //???? storlek?
                        //String path = "/Users/Emil/Documents/networkprog/proj/test2.jpg";

                        int bytesRead;
                        //FILE-STUFF
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        FileOutputStream fos = null;
                        BufferedOutputStream bos = null;
                        try {
                            fos = new FileOutputStream(path);
                            bos = new BufferedOutputStream(fos);
                            bytesRead = inputStream.read(aByte, 0, aByte.length);

                            do {
                                //write TO baos
                                //TEST TEST TEST
                                baos.write(aByte);
                                bytesRead = inputStream.read(aByte);
                            } while (bytesRead != -1);
                            bos.write(baos.toByteArray());
                            bos.flush();
                            bos.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }


            System.out.println("Done!");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static String choosePlace(){
        JFrame dFrame = new JFrame("Directory");
        dFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dFrame.getContentPane().add(new JLabel(), BorderLayout.CENTER);
        dFrame.pack();
        dFrame.setVisible(true);
        JPanel panel = new JPanel();
        dFrame.add(panel);

        File file = null;

        FileDialog fd = new FileDialog(dFrame, "Choose a file, the received file will be placed in the same directory (bug :( )", FileDialog.LOAD);
        //fd.setDirectory("C:\\");
        fd.setFile("*.xml");
        fd.setVisible(true);
        String filename = fd.getFile();
        String dirName = fd.getDirectory();
        if (filename == null){
            System.out.println("You cancelled the choice");
            dFrame.dispose();
            return "abort";
        }else{
            System.out.println("You chose " + dirName + filename);
        }
        dFrame.dispose();
        return dirName;







    }

    private static String fixName(String dir, String oldName){
        //Gör detta i annan metod? Returna file.
        String name = null;

        do {
            name = JOptionPane.showInputDialog("Name the file: (without extension)");
        }while(name.equals(""));

        if(name.equals(null)){
            return "CODE_FOR_ABORT";
        }

        String extension = getExtension(oldName);
        String s =  dir + name + "." + extension;

        return s;


    }

    private static String getExtension(String name){
        int lastDotIndex = name.lastIndexOf('.');
        if((lastDotIndex != -1) && (lastDotIndex!= 0)){ //There is a dot, and it is not the first character
            return name.substring(lastDotIndex + 1);
        }else{
            return "";
        }
    }
}
