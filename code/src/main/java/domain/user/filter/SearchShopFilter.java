package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.ShopInfo;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchShopFilter implements Filter<ShopInfo> {
    String name;
    Integer rank;

    public SearchShopFilter(String name, Integer rank){
        this.name = name;
        this.rank = rank;
    }

    @Override
    public List<ShopInfo> applyFilter(List<ShopInfo> shops) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO,"Operate filer for shops");
        if(name != null){
            shops = shops.stream().filter(shop -> shop.getName().equals(name)).collect(Collectors.toList());
        }
        if(rank!= null){
            shops = shops.stream().filter(shop -> shop.getRank() == rank).collect(Collectors.toList());
        }
        return shops;
    }
}
