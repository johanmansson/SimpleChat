package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

/**
 * Created by johanmansson on 15-04-27.
 */
public class ConnectPane extends BasicPane {
    private static final long serialVersionUID = 1;
    private JTextField[] fields;
    public static String userName;
    private static final int NBR_FIELDS = 3;

    public ConnectPane(ClientHandler chatClient) {
        super(chatClient);
    }

    public JComponent createTopPanel() {
        String[] texts = new String[NBR_FIELDS];
        texts[0] = "IP-Address:";
        texts[1] = "Port:";
        texts[2] = "Name:";
        fields = new JTextField[NBR_FIELDS];
        fields[0] = new JTextField(20);
        fields[1] = new JTextField(20);
        fields[2] = new JTextField(20);

        return new InputPanel(texts, fields);
    }

    public JComponent createBottomPanel() {
        JButton[] buttons = new JButton[1];
        buttons[0] = new JButton("Connect");
        ActionHandler actHand = new ActionHandler();
        fields[0].addActionListener(actHand);
        fields[1].addActionListener(actHand);
        fields[2].addActionListener(actHand);
        return new ButtonAndMessagePanel(buttons, messageLabel, actHand);
    }

    public void entryActions() {
        clearMessage();
    }

    public static String getUserName() {
        return userName;
    }

    class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String ipAddress = fields[0].getText();
            String port = fields[1].getText();
            String name = fields[2].getText();

            ;
            if (clientHandler.isConnected()) {
                messageLabel.setText("Client already connected.");
            } else if (name.length() == 0 || port.length() == 0 || ipAddress.length() == 0) {
                messageLabel.setText("All fields must be filled in.");
                // } else if (kolla om unikt användarnamn) {
                // messageLabel.setText("Name already exists, choose a different one.");
            } else {
                try {

                    clientHandler.startConnection(ipAddress, port);
                    clientHandler.setName(name);
                    messageLabel.setText("Connected to server.");
                    userName = name;

                } catch (Exception exception) {
                    messageLabel.setText("Could not connect.");
                }


            }
        }


    }

    public class InputPanel extends JPanel {
        private static final long serialVersionUID = 1;

        public InputPanel(String[] texts, JTextField[] fields) {
            JPanel left = new JPanel();
            left.setLayout(new GridLayout(texts.length, 1));
            JPanel right = new JPanel();
            right.setLayout(new GridLayout(fields.length, 1));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            for (int i = 0; i < texts.length; i++) {
                JLabel label = new JLabel(texts[i] + "      ", JLabel.RIGHT);
                add(left.add(label));
                add(right.add(fields[i]));

            }


        }

    }

    public class ButtonAndMessagePanel extends JPanel {
        private static final long serialVersionUID = 1;

        public ButtonAndMessagePanel(JButton[] buttons, JLabel messageLine,
                                     ActionListener actHand) {
            setLayout(new GridLayout(2, 1));

            JPanel buttonPanel = new JPanel();
            for (int i = 0; i < buttons.length; i++) {
                buttonPanel.add(buttons[i]);
            }
            add(buttonPanel);

            add(messageLine);

            for (int i = 0; i < buttons.length; i++) {
                buttons[i].addActionListener(actHand);
            }//
        }
    }


}
