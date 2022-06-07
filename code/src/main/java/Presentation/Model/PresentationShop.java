package Presentation.Model;

import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PresentationShop {

    public String id;
    public String name;
    public String description;
    public List<PresentationProduct> products;
    public PresentationUser founder;
    public boolean isOpen;
    public List<PresentationUser> managers;
    public PurchasePolicy purchasePolicy;

    public PresentationShop(String id, String name, String description, List<PresentationProduct> products, PurchasePolicy policy){
        this.id = id;
        this.name = name;
        this.description = description;
        this.products = products;
        purchasePolicy = policy;
    }

    public PresentationShop(Shop shop){
        id = Integer.toString(shop.getShopID());
        name = shop.getName();
        products = new ArrayList<>();
        description = shop.getDescription();
        founder = new PresentationUser(shop.getShopFounder());
        isOpen = shop.isOpen();
        managers = shop.getShopsManagers().stream().map(PresentationUser::new).collect(Collectors.toList());
        purchasePolicy = shop.getPurchasePolicy();
    }

    public boolean isFounder(PresentationUser user){
        return user.getUsername().equals(founder.getUsername());
    }

    public boolean isOpen(){
        return isOpen;
    }

    public boolean isManager(PresentationUser user){
        return managers.stream().anyMatch(manager -> manager.getUsername().equals(user.getUsername()));
    }

    public int getID(){
        return Integer.parseInt(id);
    }

    public List<PurchaseRule> getAllRules(){
        return purchasePolicy.getAllDistinctPurchaseRules();
    }
}
