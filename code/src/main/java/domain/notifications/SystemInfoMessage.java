package domain.notifications;
import Presentation.Model.PresentationUser;
import domain.notifications.Message;

import java.util.Map;

public class SystemInfoMessage{


    private int currentActiveUsers;
    private int currentActiveMembers;
    private int currentActiveGuests;
    private int totalRegisteredMembers;
    private Map<Integer,PresentationUser> usersInMarket;


    public SystemInfoMessage(int currentActiveUsers, int currentActiveMembers, int currentActiveGuests,
                             int totalRegisteredMembers, Map<Integer,PresentationUser> usersInMarket){
        this.currentActiveUsers = currentActiveUsers;
        this.currentActiveMembers = currentActiveMembers;
        this.currentActiveGuests = currentActiveGuests;
        this.totalRegisteredMembers = totalRegisteredMembers;
        this.usersInMarket = usersInMarket;
    }

    public int getCurrentActiveGuests() {
        return currentActiveGuests;
    }

    public int getCurrentActiveMembers() {
        return currentActiveMembers;
    }

    public int getCurrentActiveUsers() {
        return currentActiveUsers;
    }

    public int getTotalRegisteredMembers() {
        return totalRegisteredMembers;
    }

    public Map<Integer,PresentationUser> getUsersInMarket() {
        return usersInMarket;
    }
}
