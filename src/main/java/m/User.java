package m;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class User {
    private static Random random = new Random();
    PrintWriter outpw;
    Scanner scn;
    ObjectInputStream smartMsgIn;
    ObjectOutputStream smartMsgOut;
    private String Name = "Anonymous";
    private Socket socket;
    private int port;
    private Color color1;
    private Color color2;
    private InetAddress addr;
    private OutputStream os;
    private Boolean isOnline = true;
    private InputStream is;

    public User(String name, int port, InetAddress addr) {
        Name = name;
        this.port = port;
        this.addr = addr;
        try {
            socket = new Socket(addr, port);
            os = socket.getOutputStream();
            outpw = new PrintWriter(socket.getOutputStream(), true);
            smartMsgOut = new ObjectOutputStream(socket.getOutputStream());

            is = socket.getInputStream();
            scn = new Scanner(is);
            smartMsgIn = new ObjectInputStream(socket.getInputStream());
            System.out.println("USER-3pconstructor:stream get succesefully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.color1 = getRndColor();
        this.color2 = getRndColor();
    }


    public User(Socket socket) {
        System.out.println("USER-1pConstructor:creating user via short constructor");
        // System.out.println("USER-1pConstructor:copying socket");
        this.socket = socket;
        System.out.println("USER-1pConstructor:copied socket");

        try {
            os = socket.getOutputStream();
            outpw = new PrintWriter(socket.getOutputStream(), true);
            smartMsgOut = new ObjectOutputStream(socket.getOutputStream());
            is = socket.getInputStream();
            scn = new Scanner(is);
            smartMsgIn = new ObjectInputStream(socket.getInputStream());
            Name = receiveMessage();
            System.out.println("name is " + Name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.color1 = getRndColor();
        this.color2 = getRndColor();
    }

    private static Color getRndColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public void sendMessage(String msg) {
        System.out.println("writing " + msg + " to " + socket.getInetAddress() + ":" + socket.getPort() + " from " + socket.getLocalAddress() + ":" + socket.getLocalPort());
        outpw.println(msg);
    }

    public void sendSmartMessage(Message msg) {
        System.out.println("writing " + msg.getMsg() + " to " + socket.getInetAddress() + ":" + socket.getPort() + " from " + socket.getLocalAddress() + ":" + socket.getLocalPort());
        try {
            smartMsgOut.writeObject(msg);
            smartMsgOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSmartMessages(ArrayList<Message> messages) {
        System.out.println("writing " + "msglist" + " to " + socket.getInetAddress() + ":" + socket.getPort() + " from " + socket.getLocalAddress() + ":" + socket.getLocalPort());
        try {
            smartMsgOut.writeObject(messages);
            smartMsgOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public String receiveMessage() {
        try {
            while (is.available() == 0)
                Thread.sleep(100);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        String s = scn.nextLine();
        System.out.println("read " + s + " from" + socket.getInetAddress() + ":" + socket.getPort() + " to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
        return s;
    }

    public ArrayList<Message> receiveSmartMessages() {
        try {
            while (is.available() == 0)
                Thread.sleep(100);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        ArrayList<Message> s = null;
        try {
            s = (ArrayList<Message>) smartMsgIn.readObject();
            System.out.println("read " + "MsgList" + " from" + socket.getInetAddress() + ":" + socket.getPort() + " to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    public Message receiveSmartMessage() {
        try {
            while (is.available() == 0)
                Thread.sleep(100);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        Message s = null;
        try {
            s = (Message) smartMsgIn.readObject();
            System.out.println("read " + s.getMsg() + " from" + socket.getInetAddress() + ":" + socket.getPort() + " to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
