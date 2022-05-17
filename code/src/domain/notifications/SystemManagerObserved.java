package domain.notifications;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;


public class SystemManagerObserved extends Observable {
    private List<Observer> observerList;

    public SystemManagerObserved(){
        this.observerList = new LinkedList<>();
    }

    public void subscribe(Observer observer){
        observerList.add(observer);
    }
    public void unsubscribe(Observer observer){
        observerList.remove(observer);
    }
    public void notifyAllObservers(String msg){
        for (Observer observer : observerList) {
            observer.update(msg);
        }
    }
}
