package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.Order;
import domain.user.User;
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
    }
    public SearchUserFilter(String b) {
        switch (b){
            case "isMember":
                isMember=true;
            case "isGuest":
                isGuest=true;
        }
    }

    public void setName(String name) {
        Name = name;
    }

    public List<User> applyFilter(List<User> orders) {
        throw new UnsupportedOperationException();
    }
}
