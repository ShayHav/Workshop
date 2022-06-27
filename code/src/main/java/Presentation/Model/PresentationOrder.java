package Presentation.Model;

import Service.Services;
import domain.Responses.ResponseT;
import domain.shop.Order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresentationOrder {
    public long id;
    public LocalDateTime buyingTime;
    public String userBought;
    public Map<PresentationProduct, Double> productsBoughtWithPrices;
    public double totalAmount;
    public int shopID;
    public String shopName;

    public PresentationOrder(int shopID, long id, LocalDateTime buyingTime, List<PresentationProduct> products, double totalAmount, String userBought) {
        this.id = id;
        this.buyingTime = buyingTime;
        this.userBought = userBought;
        productsBoughtWithPrices = new HashMap<>();
        products.stream().map(p -> productsBoughtWithPrices.put(p, (p.finalPrice * p.amount)));
        this.totalAmount = totalAmount;
        this.shopID = shopID;
    }

    public PresentationOrder(Order order) {
        id = order.getOrderId();
        buyingTime = order.getBuyingTime();
        userBought = order.getUserID();
        totalAmount = order.getTotalAmount();
        this.shopID = order.getShopID();
        List<PresentationProduct> products = order.getBroughtItem().stream().map(p -> new PresentationProduct(p, shopID)).collect(Collectors.toUnmodifiableList());
        updateFinalPriceOfProducts(products);
        productsBoughtWithPrices = new HashMap<>();
        products.forEach(p -> productsBoughtWithPrices.put(p, (p.finalPrice * p.amount)));
        this.shopName = order.getShopName();

    }

    public String getPurchaseDate() {
        return buyingTime.toLocalDate().toString();
    }

    private void updateFinalPriceOfProducts(List<PresentationProduct> products) {
        ResponseT<Shop> r = Services.getInstance().GetShop(shopID);
        Shop s = r.getValue();
        Map<Integer, Integer> serialToAmount = new HashMap<>();
        products.forEach(product -> serialToAmount.put(product.serialNumber, product.amount));
        for (PresentationProduct p : products) {
            p.setFinalPrice(s.productPriceAfterDiscounts(p.serialNumber, serialToAmount));
        }
    }

}
