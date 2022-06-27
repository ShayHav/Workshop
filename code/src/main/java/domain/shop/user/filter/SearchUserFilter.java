package domain.shop.user.filter;

import domain.shop.user.User;

import java.util.List;

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
