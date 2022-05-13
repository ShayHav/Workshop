package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.Shop;
import domain.shop.ShopInfo;
import domain.user.filter.Filter;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchShopFilter implements Filter<Shop> {
    String name;
    Integer rank;

    public SearchShopFilter(String name, Integer rank){
        this.name = name;
        this.rank = rank;
    }

    public SearchShopFilter(){
        name = null;
        rank = null;
    }

    @Override
    public List<Shop> applyFilter(List<Shop> shops) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO,"Operate filter for shops");
        if(name != null){
            shops = shops.stream().filter(shop -> shop.getName().equals(name)).collect(Collectors.toList());
        }
        if(rank!= null){
            shops = shops.stream().filter(shop -> shop.getRank() == rank).collect(Collectors.toList());
        }
        return shops;
    }
}
