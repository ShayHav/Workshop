package domain.user.filter;

import domain.EventLoggerSingleton;
import domain.shop.Product;
import domain.shop.ProductInfo;
import domain.user.filter.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchProductFilter implements Filter<Product> {

    private Double minPrice;
    private Double maxPrice;
    private Integer productRank;
    private Integer shopRank;
    private String category;

    public SearchProductFilter(){
        minPrice = 0d;
        maxPrice = Double.MAX_VALUE;
        productRank = null;
        shopRank = null;
        category = null;
    }

    public SearchProductFilter(Double minPrice, Double maxPrice, Integer productRank, Integer shopRank, String category) {
        this.minPrice = Objects.requireNonNullElse(minPrice, 0d);
        this.maxPrice = Objects.requireNonNullElse(maxPrice, Double.MAX_VALUE);
        this.productRank = productRank;
        this.shopRank = shopRank;
        this.category = category;
    }

    public List<Product> applyFilter(List<Product> products) {
        EventLoggerSingleton.getInstance().logMsg(Level.INFO,"Operate filer for products");
        List<Product> result = new ArrayList<>();

        if(minPrice < -1 || maxPrice > Double.MAX_VALUE || minPrice > maxPrice)
            return result;

        result = products.stream().filter(p -> (p.getPrice() >= minPrice & p.getPrice() <= maxPrice)).collect(Collectors.toList());

        if (productRank != null) {
            result = result.stream().filter(p -> p.getRank() == productRank).collect(Collectors.toList());
        }
        if (shopRank != null) {
            result = result.stream().filter(p -> p.getShopRank() == shopRank).collect(Collectors.toList());
        }
        if(category != null){
            result = result.stream().filter(p -> p.getCategory().equals(category)).collect(Collectors.toList());
        }


        return result;
    }


}
