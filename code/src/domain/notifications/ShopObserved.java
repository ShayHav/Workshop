package domain.notifications;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import domain.notifications.Observer;


public class ShopObserved extends Observable {
    private int shopID;
    private List<Observer> observerList;

    public ShopObserved(int shopID){
        this.shopID=shopID;
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
