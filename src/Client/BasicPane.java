package Client;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

/**
 * Created by johanmansson on 15-04-27.
 */
public class BasicPane extends JPanel {
    private static final long serialVersionUID = 1;

    protected JLabel messageLabel;
    protected ClientHandler clientHandler;

    public BasicPane(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
        messageLabel = new JLabel("      ");

        setLayout(new BorderLayout());

        JComponent leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JComponent topPanel = createTopPanel();
        JComponent middlePanel = createMiddlePanel();
        JComponent bottomPanel = createBottomPanel();
        bottomPanel.setBorder
                (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
                        bottomPanel.getBorder()));
        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(middlePanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.CENTER);
    }


    public JComponent createLeftPanel() {
        return new JPanel();
    }
    public JComponent createTopPanel() {
        return new JPanel();
    }
    public JComponent createMiddlePanel() {
        return new JPanel();
    }
    public JComponent createBottomPanel() {
        return new JPanel();
    }
    public void entryActions() {}
    public void displayMessage(String msg) {
        messageLabel.setText(msg);
    }
    public void clearMessage() {
        messageLabel.setText(" ");
    }

}
