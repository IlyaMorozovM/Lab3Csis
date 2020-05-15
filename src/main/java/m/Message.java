package m;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Message implements Comparable<Message>, Serializable {
    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String msg;
    private String hash;
    private String author;
    private int AuthorId;
    private Date dateOne;
    private InetAddress inetAddress;
    private int port;
    private int code;
    private boolean isMyself = false;

    /* Codes:
    1 - default message
    2 - connecting message
    3 - disconnect message
    4 - request history
    5 - response of "4"
     */

    public Message(String msg, String author, InetAddress inetAddress, int port, int code) {
        this.msg = msg;
        this.author = author;
        Calendar c1 = Calendar.getInstance();
        this.dateOne = c1.getTime();
        messageDigest.update(this.author.getBytes());
        this.hash = new String(messageDigest.digest());
        this.inetAddress = inetAddress;
        this.port = port;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (((Message) o).getCode() == 2 || ((Message) o).getCode() == 3) {
            Message message = (Message) o;
            return code == message.code &&
                    Objects.equals(msg, message.msg) &&
                    author.equals(message.author) &&
                    dateOne.equals(message.dateOne);
        }
        Message message = (Message) o;
        return code == message.code &&
                Objects.equals(msg, message.msg) &&
                author.equals(message.author);

    }

    @Override
    public int hashCode() {
        if (code == 2 || code == 3) {
            return Objects.hash(msg, author, code, dateOne);
        }
        return Objects.hash(msg, author, code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(int authorId) {
        AuthorId = authorId;
    }

    public Date getDateOne() {
        return dateOne;
    }

    public void setDateOne(Date dateOne) {
        this.dateOne = dateOne;
    }

    @Override
    public int compareTo(Message o) {
        return this.getDateOne().compareTo(o.getDateOne());
    }
}
