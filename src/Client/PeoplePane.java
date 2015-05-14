package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by johanmansson on 15-04-27.
 */
public class PeoplePane extends BasicPane {
    private static final long serialVersionUID = 1;
    private DefaultListModel<String> peopleListModel;
    private JList<String> peopleList;


    public PeoplePane(ClientHandler clientHandler) {
        super(clientHandler);

    }
    public JComponent createMiddlePanel() {
        peopleListModel = new DefaultListModel<String>();

        peopleList = new JList<String>(peopleListModel);
        peopleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        peopleList.setPrototypeCellValue("123456789012");

        JScrollPane p1 = new JScrollPane(peopleList);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.add(p1);

        return p;
    }

    public JComponent createBottomPanel() {
        JButton[] buttons = new JButton[2];
        buttons[0] = new JButton("Start Chat");
        buttons[1] = new JButton("Send file");
        ActionHandler actHand = new ActionHandler();
        return new ButtonPanel(buttons, actHand);


    }

    public void fillPeopleList() {
        peopleListModel.removeAllElements();
        ArrayList<String> people = clientHandler.getPeople();

        for(String name : people) {
            peopleListModel.addElement(name);

        }
    }


    public void entryActions() {

        clearMessage();

        //Updates peopleList automatic
        UpdateList update = new UpdateList();
        update.start();

    }


    class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                String name = null;
                name = peopleList.getSelectedValue();
                if (name.equals(ConnectPane.getUserName())){
                   JFrame frame = new JFrame("Attention!");
                    JOptionPane.showMessageDialog(frame, "Choose a different user than yourself!");
           }
                    else if(name != null) {
                    clientHandler.newChatWindow(name);
                }



        }

    }

    public class ButtonPanel extends JPanel {
        private static final long serialVersionUID = 1;

        public ButtonPanel(JButton[] buttons,
                           ActionListener actHand) {
            setLayout(new GridLayout(1, 1));

            JPanel buttonPanel = new JPanel();
            for (int i = 0; i < buttons.length; i++) {
                buttonPanel.add(buttons[i]);
            }
            add(buttonPanel);



            for (int i = 0; i < buttons.length; i++) {
                buttons[i].addActionListener(actHand);
            }
        }
    }

    public class UpdateList extends Thread {

        public void run() {
            while(true) {
                fillPeopleList();
                try {
                    Thread.sleep(6000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }



            }
        }


    }


}
