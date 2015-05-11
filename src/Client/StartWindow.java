package Client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;


/**
 * Created by johanmansson on 15-04-27.
 */
public class StartWindow {
    private JTabbedPane tabbedPane;
    protected JTextArea resultArea;
    protected ClientHandler chatClient;


    public StartWindow(ClientHandler chatClient) {
        this.chatClient = chatClient;


        JFrame frame = new JFrame("SimpleChat");
        tabbedPane = new JTabbedPane();

        ConnectPane connectPane = new ConnectPane(chatClient);
        tabbedPane.addTab("Connect", null, connectPane, "Connect");

        PeoplePane peoplePane = new PeoplePane(chatClient);
        tabbedPane.addTab("People", null, peoplePane, "People");

        tabbedPane.setSelectedIndex(0);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        frame.setSize(300, 600);
        frame.setVisible(true);

        tabbedPane.addChangeListener(new ChangeHandler());
        frame.addWindowListener(new WindowHandler());

    }

    class ChangeHandler implements ChangeListener{

        public void stateChanged(ChangeEvent e){
            BasicPane selectedPane = (BasicPane) tabbedPane.getSelectedComponent();
            selectedPane.entryActions();
        }

   }

    class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }



}
