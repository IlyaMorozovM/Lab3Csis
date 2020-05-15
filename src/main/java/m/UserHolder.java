package m;

import c.Main;

import java.util.ArrayList;

public class UserHolder {
    private ArrayList<User> visibleUsersList;

    public UserHolder() {
        this.visibleUsersList = new ArrayList<User>();
    }

    public ArrayList<User> getVisibleUsersList() {
        return visibleUsersList;
    }

    public void setVisibleUsersList(ArrayList<User> visibleUsersList) {
        this.visibleUsersList = visibleUsersList;
    }

    public void addUser(User user) {
        visibleUsersList.add(user);
    }

    public void sendToAll(String msg) {
        System.out.println("gna send to a11");
        Message mMsg = null;
        for (User user : visibleUsersList) {
            mMsg = new Message(msg, Main.username, user.getSocket().getLocalAddress(), user.getSocket().getLocalPort(), 1);
            System.out.println("USERHOLDER:sending to " + user.getName());
            user.sendSmartMessage(mMsg);
        }
        if (mMsg != null) {
            Main.appendToChat(Main.username, mMsg.getMsg() + "<" + mMsg.getDateOne() + ">");
            Main.addMessage(mMsg);
        } else {
            Main.appendToChat("me", "Error no receivers");
        }
    }

}
