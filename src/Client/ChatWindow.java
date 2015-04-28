package Client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * Created by johanmansson on 15-04-27.
 */
public class ChatWindow {

    private Frame         theWindow;
    private TextArea      theMessages;
    private Panel         inputPanel;
    private TextField     inputField;
    private Button        submitButton;
    private SubmitHandler handler;
    private boolean       destroyed;
    private SubmitThread  submitter;


    /**
     * Constructs a ChatWindow at a given position on the screen and with
     * a given title which is displayed in the title bar of the window. Note
     * that the window is initially invisible. You must call the
     * <code>show</code> method in order to display the window.
     *
     *
     * @param x      the x coordinate of the position of the window
     * @param y      the y coordinate of the position of the window
     * @param title  a String containing the title to be displayed in
     *               the title bar of the window
     */
    public ChatWindow(int x,int y,String title) {


        theWindow = new Frame(title);
        theWindow.setLocation(x,y);

        //Font f = new Font("Dialog",Font.PLAIN,16);

        theMessages = new TextArea("",20,50);
        //theMessages.setFont(f);
        theMessages.setEditable(false);
        theMessages.setBackground(SystemColor.text);
        theWindow.add("Center",theMessages);

        inputPanel = new Panel();
        theWindow.add("South",inputPanel);

        inputField = new TextField("",40);
        //inputField.setFont(f);
        inputPanel.add(inputField);

        //f = new Font("Dialog",Font.BOLD,16);
        submitButton = new Button("Submit");
        //submitButton.setFont(f);
        inputPanel.add(submitButton);

        handler = new SubmitHandler();
        submitButton.addActionListener(handler);
        inputField.addActionListener(handler);

        theWindow.pack();

        submitter = null;

        theWindow.addWindowListener(new WindowHandler());
    }

    /**
     * Adds a new message to the message area in the upper part of the
     * ChatWindow. The new message should consist of a String containing
     * a single line of text. The new message is added to the bottom of
     * the message area.
     *
     * @param message  a String containing the message to add
     */
    public void add(String message) {
        if (!destroyed) {
            theMessages.append(message+'\n');
        }
    }

    /**
     * Clears the contents of the message area in the upper part of the
     * ChatWindow.
     */
    public void clear() {
        if (!destroyed) {
            theMessages.replaceRange("",0,theMessages.getText().length());
        }
    }

    /**
     * Makes the ChatWindow invisible. Call <code>show</code> to make the
     * window visible again.
     *
     * @see ChatWindow#show
     */
    public void hide() {
        if (!destroyed) {
            theWindow.setVisible(false);
        }
    }

    /**
     * Makes the ChatWindow visible. ChatWindows is initially invisible
     * when having been created. To actually make it visible you must call
     * the <code>show</code> method. Call <code>hide</code> to make the
     * window invisible again.
     *
     * @see ChatWindow#hide
     */
    public void show() {
        if (!destroyed) {
            theWindow.setVisible(true);
        }
    }

    /**
     * Destroys the ChatWindow and frees all resources associated with it.
     * The <code>destroy</code> method should be called when the ChatWindow
     * is not needed anymore. Note that it is <b>ILLEGAL</b> to call any
     * method of the ChatWindow object once <code>destroy</code> has
     * been called.
     */
    public void destroy() {
        destroyed = true;
        theWindow.dispose();
    }

    /**
     * The <code>messageEntered</code> method is called whenever the user
     * submits a new message and controls how to handle the new message.
     * The intention is that this method should be overloaded in a
     * subclass to ChatWindow. The default implementation found in the
     * ChatWindow class does nothing.
     *
     * @param message  a String containing the new message entered by
     *                 the user
     */
    public void messageEntered(String message) {
        // Default implementation: do nothing
        // Overload messageEntered in a subclass to ChatWindow.
    }

    private class SubmitHandler implements ActionListener {
        boolean confirmed;

        public void actionPerformed(ActionEvent e) {
            String m = inputField.getText();
            inputField.setText("");
            if (submitter==null || !submitter.isAlive()) {
                submitter = new SubmitThread(m);
                submitter.start();
            }
        }
    }

    private class SubmitThread extends Thread {
        private String mess;

        public SubmitThread(String mess) {
            this.mess = mess;
        }

        public void run() {
            messageEntered(mess);
        }
    }

    class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            hide();
        }
    }


}
