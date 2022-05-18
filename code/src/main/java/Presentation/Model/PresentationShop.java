package Presentation.Model;

import domain.shop.Shop;

import java.util.ArrayList;
import java.util.List;

public class PresentationShop {

    public String id;
    public String name;
    public String description;
    public List<PresentationProduct> products;

    public PresentationShop(String id, String name, String description, List<PresentationProduct> products){
        this.id = id;
        this.name = name;
        this.description = description;
        this.products = products;
    }

    public PresentationShop(Shop shop){
        id = Integer.toString(shop.getShopID());
        name = shop.getName();
        products = new ArrayList<>();
        description = shop.getDescription();
        // TODO: decide what to do with the product of shop
    }
}
