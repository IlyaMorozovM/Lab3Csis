package c;

import m.Message;
import m.User;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer implements Runnable {
    int myPort;
    ServerSocket sSocket;
    private boolean isRunning = true;

    public TCPServer(int myPort) {
        this.myPort = myPort;
    }

    @Override
    public void run() {
        try {
            sSocket = new ServerSocket(myPort);
            System.out.println("TCPSERVER:opened tcp listeng server socket");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("TCPSERVER:started tcp server for old user's connections");
        System.out.println("TCPSERVER:accepting connections from olds at " + sSocket.getLocalPort());
        while (isRunning) {
            try {
                Socket client = sSocket.accept();
                System.out.print("TCPSERVER:old " + client.getInetAddress() + " accepted.");
                System.out.println("TCPSERVER:adding old");
                User user = new User(client);
                Main.getHolder().addUser(user);
                Main.updateUI();
                user.sendSmartMessage(new Message("connected", Main.username, user.getSocket().getLocalAddress(), user.getSocket().getLocalPort(), 2));
                user.sendSmartMessage(new Message("request", Main.username, user.getSocket().getLocalAddress(), user.getSocket().getLocalPort(), 4));
                ArrayList<Message> messages;
                messages = user.receiveSmartMessages();
                messages.forEach(Main::addOldMessage);
                Main.clearChatContent();
                for (Message m : Main.getMessages()) {
                    switch (m.getCode()) {
                        case 1:
                            Main.appendToChat(m.getAuthor(), m.getMsg() + "<" + m.getDateOne() + ">");
                            break;
                        case 2:
                            Main.appendToChat(m.getAuthor(), "connected" + "<" + m.getDateOne() + ">");
                            break;
                        case 3:
                            Main.appendToChat(m.getAuthor(), "disconnected" + "<" + m.getDateOne() + ">");
                            break;
                    }
                }
                new Thread(new TcpListener(user)).start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
