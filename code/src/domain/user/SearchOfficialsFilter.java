package domain.user;

import java.util.List;

public class SearchOfficialsFilter implements Filter<User>{
    private List<Role> roleList;


    public SearchOfficialsFilter(List<Role> roleList){
        this.roleList = roleList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    @Override
    public List<User> applyFilter(List<User> userList) {
        return null;
    }
}
