package v;

import c.Main;
import m.User;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class UI extends JFrame {
    private JPanel myPanel;
    private JTextField textField1;
    private JButton SendButton;
    private JTextPane ChatPane;
    private JList list1;
    private JScrollPane scroll;

    private class SubmitButton implements ActionListener, KeyListener {

        JTextField nameInput;


        public SubmitButton(JTextField textfield){
            nameInput = textfield;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (textField1.getText().length() > 0) {
                Main.getHolder().sendToAll(textField1.getText());

                //Main.getHolder().closeEveryone();
//                    Main.appendToChat(textField1.getText());

                textField1.setText("");
            } else {
                JOptionPane.showMessageDialog(myPanel, "nothing to send!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            scrollDown();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_ENTER){
                System.out.println("Hello");
            }
            Component frame = new JFrame();
            JOptionPane.showMessageDialog(frame , "You've Submitted the name " + nameInput.getText());

        }

        @Override
        public void keyReleased(KeyEvent arg0) {

        }

        @Override
        public void keyTyped(KeyEvent arg0) {

        }
    }

    public void scrollDown() {

        textField1.setCaretPosition(textField1.getDocument().getLength());
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    public UI(String title) throws HeadlessException {
        super(title);

        this.setSize(800, 600);
        list1.setModel(new UserListModel());
        this.add(myPanel);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                Main.finalization();
            }
        });

        SubmitButton listener = new SubmitButton(textField1);
        textField1.addActionListener(listener);
        SendButton.addActionListener(listener);

        DefaultCaret caret = (DefaultCaret) ChatPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.setVisible(true);
        Main.isUILoaded = true;
//        SendButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (textField1.getText().length() > 0) {
//                    Main.getHolder().sendToAll(textField1.getText());
//
//                    //Main.getHolder().closeEveryone();
////                    Main.appendToChat(textField1.getText());
//
//                    textField1.setText("");
//                } else {
//                    JOptionPane.showMessageDialog(myPanel, "nothing to send!", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//                DefaultCaret caret = (DefaultCaret) ChatPane.getCaret();
//                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
//            }
//        });
    }

    public void update() {
        // list1.clearSelection();
        // list1.revalidate();
        // list1.repaint();
        list1.updateUI();
        //list1.clearSelection();
        //list1.revalidate();
        //list1.repaint();
    }

    public void setChatContent(String chatContent, User user) {
        StyledDocument doc = ChatPane.getStyledDocument();

//  Define a keyword attribute

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, user.getColor1());
        StyleConstants.setBackground(keyWord, user.getColor2());
        StyleConstants.setBold(keyWord, true);

//  Add some text

        try {
            doc.insertString(doc.getLength(), "\n" + user.getName() + " ", keyWord);
            doc.insertString(doc.getLength(), chatContent + "\n", null);
        } catch (Exception e) {
            System.out.println(e);
        }
        scrollDown();
    }

    public void setChatContent(String chatContent, String uname) {
        StyledDocument doc = ChatPane.getStyledDocument();

//  Define a keyword attribute

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        StyleConstants.setBackground(keyWord, Color.YELLOW);
        StyleConstants.setBold(keyWord, true);

        try {
            doc.insertString(doc.getLength(), "\n" + uname + " ", keyWord);
            doc.insertString(doc.getLength(), chatContent + "\n", null);
        } catch (Exception e) {
            System.out.println(e);
        }
        scrollDown();
    }

    public void setChatContent(boolean isConnected, User user, Date date) {
        StyledDocument doc = ChatPane.getStyledDocument();

//  Define a keyword attribute

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, user.getColor1());
        StyleConstants.setBackground(keyWord, user.getColor2());
        StyleConstants.setBold(keyWord, true);

        SimpleAttributeSet keyWord2 = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord2, Color.BLACK);
        StyleConstants.setBackground(keyWord2, Color.YELLOW);
        StyleConstants.setBold(keyWord2, true);

        try {
            doc.insertString(doc.getLength(), "\n" + user.getName() + " ", keyWord);
            doc.insertString(doc.getLength(), (isConnected ? "Connected" : "Disconnected at " + date) + "\n", keyWord2);
        } catch (Exception e) {
            System.out.println(e);
        }
        scrollDown();
    }

    public void clearChatContent() {
        StyledDocument doc = ChatPane.getStyledDocument();
        try {
            doc.insertString(0, "", null);
        } catch (Exception e) {
            System.out.println(e);
        }
        scrollDown();
    }
}

