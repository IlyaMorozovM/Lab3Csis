package c;

import m.Message;
import m.User;
import m.UserHolder;
import v.UI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static boolean isUILoaded = false;
    public static String chatContent = "";
    public static String username = "";
    private static ArrayList<Message> messages = new ArrayList<Message>();
    private static UI ui;
    private static UserHolder holder = new UserHolder();

    public static ArrayList<Message> getMessages() {
        return messages;
    }

    public static void setMessages(ArrayList<Message> messages) {
        messages = messages;
    }

    public static void sortMsgs() {
        synchronized (messages) {
            messages.sort((o1, o2) -> o1.compareTo(o2));
        }
    }

    public static int getUsersCount() {
        synchronized (holder) {
            return holder.getVisibleUsersList().size();
        }
    }

    public static User getUserAt(int id) {
        synchronized (holder) {
            return holder.getVisibleUsersList().get(id);
        }
    }

    public static void addMessage(Message message) {
        synchronized (messages) {
            if (messages.contains(message)) {
                System.err.println("Already exists");
            } else {
                System.out.println("Added");
                messages.add(message);
            }
        }
    }

    public static void addOldMessage(Message message) {
        synchronized (messages) {
            if (messages.contains(message)) {
                System.err.println("Already exists");
            } else {
                System.out.println("Added");
                messages.add(message);
                sortMsgs();
//                clearChatContent();
            }
        }
    }

    public static void updateUI() {
        System.out.println("updating ui");
        System.out.println("users:" + getUsersCount());
        ui.update();
    }

    public static void displayTray(String s1, String s2) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage(s1, s2, TrayIcon.MessageType.INFO);
    }

    public static void finalization() {
        for (User user : getHolder().getVisibleUsersList()) {
            System.out.println("USERHOLDER:sending to " + user.getName());
            user.sendSmartMessage(new Message("disconnected", Main.username, user.getSocket().getLocalAddress(), user.getSocket().getLocalPort(), 3));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public static boolean holderContains(User user){
        synchronized (holder)
        {
            return holder.getVisibleUsersList().contains(user);
        }
    }

    public static UserHolder getHolder() {
        return holder;
    }

    public static void setHolder(UserHolder holder) {
        holder = holder;
    }

    public static void appendToChat(boolean b, User user, Date date) {
        ui.setChatContent(b, user, date);
        updateUI();
    }

    public static void appendToChat(String username, String s) {
        synchronized (chatContent) {
            ui.setChatContent(s, username);
            updateUI();
        }
    }

    public static void appendToChat(User user, String s) {
        synchronized (chatContent) {
            ui.setChatContent(s, user);
            updateUI();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        //Scanner scanner = new Scanner(System.in);

        username = JOptionPane.showInputDialog("Enter username");
        if (null == username) {
            try {
                InetAddress addr;
                addr = InetAddress.getLocalHost();
                username = addr.getHostName();
            } catch (UnknownHostException ex) {
                System.out.println("MAIN:Hostname can not be resolved");
            }
        }
        AuthentificationThread authentificationThread = new AuthentificationThread(username, 555);
        new Thread(authentificationThread).start();//sending udp to everyone and receiving ne udp's
        ui = new UI("My chat");
    }

    public static void clearChatContent() {
        ui.clearChatContent();
    }

}
