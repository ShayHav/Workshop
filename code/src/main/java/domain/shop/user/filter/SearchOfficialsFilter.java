package domain.shop.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.user.Role;
import domain.shop.user.User;
import domain.shop.user.UserSearchInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchOfficialsFilter implements Filter<User> {
    private List<Role> roleList;


    public SearchOfficialsFilter(List<Role> roleList){
        this.roleList = roleList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }
    @Override
    public List<User> applyFilter(List<User> userList){return null;}

    public List<User> applyFilter(List<User> userList, int shopId) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO,"Operate filer for Shop: "+shopId);
        List<User> result = new ArrayList<>();
        List<UserSearchInfo> output = new LinkedList<>();
        result = userList.stream().collect(Collectors.toList());
        for(Role run : roleList){
            result = result.stream().filter(u -> u.getRoleList().get(shopId).contains(run)).collect(Collectors.toList());
        }
        return result;
    }
}
