package Presentation.Model;

import domain.shop.Shop;

import java.util.ArrayList;
import java.util.List;

public class PresentationShop {

    public String id;
    public String name;
    public String description;
    public List<PresentationProduct> products;
    public PresentationUser founder;
    public boolean isOpen;

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
        founder = new PresentationUser(shop.getShopFounder());
        isOpen = shop.isOpen();
    }

    public boolean isFounder(PresentationUser user){
        return user.getUsername().equals(founder.getUsername());
    }

    public boolean isOpen(){
        return isOpen;
    }
}
