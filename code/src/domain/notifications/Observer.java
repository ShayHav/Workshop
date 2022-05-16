package domain.notifications;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Observer {
    private String userName;
    private List<String> messages;
    private boolean updated;
    private int index;

    public Observer(String userName) {
        this.userName = userName;
        messages = new LinkedList<>();
        updated = false;
        index = 0;
    }

    public void update(String msg) {
        messages.add(index,msg);
        index++;
        updated = true;
    }

    public String isUpDate(){
        String output = null;
        if(updated) {
            output = messages.get(index);
            messages.remove(index);
            index--;
            updated=false;
        }
        return output;
    }
}
