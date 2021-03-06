package Presentation.Model;

import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.Shop;
import domain.shop.discount.DiscountPolicy;

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
    public List<PresentationUser> owners;
    public PurchasePolicy purchasePolicy;
    public DiscountPolicy discountPolicy;
    public List<PresentationAppointment> appointments;

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
        discountPolicy = shop.getDiscountPolicy();
        owners = shop.getShopOwners().stream().map(PresentationUser::new).collect(Collectors.toList());
        appointments = shop.getAppointment().stream().map(ownerAppointment -> new PresentationAppointment(ownerAppointment, shop.getShopID())).collect(Collectors.toList());
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

    public List<PresentationAppointment> getAppointments() {
        return appointments;
    }
}
