package domain.user;

import domain.shop.ProductInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Filter {

    private Double minPrice;
    private Double maxPrice;
    private Integer productRank;
    private Integer shopRank;
    private String category;

    public Filter(Double minPrice, Double maxPrice, Integer productRank, Integer shopRank, String category) {
        this.minPrice = Objects.requireNonNullElse(minPrice, 0d);
        this.maxPrice = Objects.requireNonNullElse(maxPrice, Double.MAX_VALUE);
        this.productRank = productRank;
        this.shopRank = shopRank;
        this.category = category;
    }

    public List<ProductInfo> applyFilter(List<ProductInfo> products) {
        List<ProductInfo> result = new ArrayList<>();
        result = products.stream().filter(p -> (p.getPrice() >= minPrice & p.getPrice() <= maxPrice)).collect(Collectors.toList());

        if (productRank != null) {
            result = result.stream().filter(p -> p.getProductRank() == productRank).collect(Collectors.toList());
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
