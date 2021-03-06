package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchShopFilter implements Filter<Shop> {
    private String name;
    private Integer rank;

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
            shops = shops.stream().filter(shop -> shop.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        if(rank!= null){
            shops = shops.stream().filter(shop -> shop.getRank() == rank).collect(Collectors.toList());
        }
        return shops;
    }

    public Integer getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }
}
