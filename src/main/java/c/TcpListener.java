package c;

import m.Message;
import m.User;

import javax.swing.*;
import java.util.ArrayList;

public class TcpListener implements Runnable {
    private User user;

    public TcpListener(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        System.out.println("started tcpListener");
        while (Main.holderContains(user)) {
            try {
                if (user.getIs().available() > 0) {
                    System.out.println("has something to receive. appending");
                    Message receiveMessage = user.receiveSmartMessage();
                    Main.addMessage(receiveMessage);
                    System.out.println(receiveMessage.getCode());
                    switch (receiveMessage.getCode()) {
                        case 1:
                            Main.appendToChat(user, receiveMessage.getMsg() + "<" + receiveMessage.getDateOne() + ">");
                            break;
                        case 2:
                            Main.appendToChat(true, user, receiveMessage.getDateOne());
                            break;
                        case 3:
                            Main.appendToChat(false, user, receiveMessage.getDateOne());
                            Main.getHolder().getVisibleUsersList().remove(user);
                            Main.updateUI();
                            break;
                        case 4:
                            ArrayList<Message> messages = new ArrayList<Message>();
                            for (Message m : Main.getMessages()) {
                                if (receiveMessage.compareTo(m) == 1) {
                                    messages.add(m);
                                }
                            }
                            user.sendSmartMessages(messages);
                            break;
                        case 5:

                            break;
                        default:
                            break;
                    }
                    System.out.println("append");

                }
                else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "error!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
