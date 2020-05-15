package c;

import m.User;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class AuthentificationThread implements Runnable {
    private String sendableName;
    private int port = 777;
    private boolean isRunning = true;
    private TCPServer tcpServer;

    public AuthentificationThread(String sendableName, int port) {
        this.sendableName = sendableName;
        this.port = port;
    }

    @Override
    public void run() {
        while (!Main.isUILoaded) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        byte[] buf = sendableName.getBytes();
        DatagramSocket initialUDPSocket;
        DatagramPacket initialUDPPacket;
        try {
            initialUDPSocket = new DatagramSocket();
            initialUDPPacket = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), this.port);
            this.tcpServer = new TCPServer(initialUDPSocket.getLocalPort() + 1);
            initialUDPSocket.send(initialUDPPacket);
            initialUDPSocket.close();
            new Thread(this.tcpServer).start();
            System.out.println("AuthThread:succesefully sent name(" + sendableName + ")over udp!");
            //sent udp message to everyone

            initialUDPSocket = new DatagramSocket(this.port);
            System.out.println("AuthThread:started loop in auth thread");
            while (isRunning) {
                buf = new byte[1024 * 63];
                initialUDPPacket = new DatagramPacket(buf, buf.length);
                System.out.println("AuthThread:receiving auth message from new user!");
                initialUDPSocket.receive(initialUDPPacket);
                System.out.println("AuthThread:received from " + initialUDPPacket.getAddress());
                System.out.println("AuthThread:creating new user!");
                User neW = new User(new String(buf).substring(0, initialUDPPacket.getLength()), initialUDPPacket.getPort() + 1, initialUDPPacket.getAddress());
                System.out.println("AuthThread:created new user!");
                System.out.println("AuthThread:sending name message to new user via tcp!" + neW.getName());
                neW.sendMessage(sendableName);
                System.out.println("AuthThread:sent name message to new user via tcp!" + neW.getName());
                Main.getHolder().addUser(neW);

                System.out.println("AuthThread:added new user!");

                System.out.println("AuthThread:starting tcplistener for new user");
                new Thread(new TcpListener(neW)).start();
                System.out.println("AuthThread:started tcplistener for new user");
                Main.updateUI();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "error!", JOptionPane.ERROR_MESSAGE);
        }


    }
}
