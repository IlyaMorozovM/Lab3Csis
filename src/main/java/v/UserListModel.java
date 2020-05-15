package v;

import c.Main;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class UserListModel implements ListModel {
    @Override
    public int getSize() {
        return Main.getUsersCount();
    }

    @Override
    public Object getElementAt(int index) {
        return Main.getUserAt(index).getName() + ":" + Main.getUserAt(index).getSocket().getInetAddress();
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
