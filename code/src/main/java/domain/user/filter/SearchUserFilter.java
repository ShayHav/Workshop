package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.Order;
import domain.user.User;
import domain.user.UserState2;
import jdk.jshell.spi.ExecutionControl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchUserFilter implements Filter<User> {
    private boolean isMember;
    private boolean isGuest;
    private String Name;


    public SearchUserFilter() {
        isMember = false;
        isGuest = false;
    }
    public SearchUserFilter(String b) {
        switch (b){
            case "isMember":
                isMember=true;
            case "isGuest":
                isGuest=true;
            default: Name =b;
        }
    }

    public void setName(String name) {
        Name = name;
    }

    public List<User> applyFilter(List<User> orders) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO,"Operate filter for shops");
        if( Name != null || !Name.equals(""))
            orders = orders.stream().filter(user -> user.getUserName().equals(Name)).collect(Collectors.toList());
        if(isMember)
            orders = orders.stream().filter(user -> user.getUs()== UserState2.member).collect(Collectors.toList());
        if(isGuest)
            orders = orders.stream().filter(user -> user.getUs()== UserState2.guest).collect(Collectors.toList());
        return orders;
    }
}
