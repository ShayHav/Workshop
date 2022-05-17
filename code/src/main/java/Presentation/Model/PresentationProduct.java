package Presentation.Model;

import domain.shop.Product;
import domain.shop.ServiceProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PresentationProduct {
    public int serialNumber;
    public String name;
    public String description;
    public String category;
    public double price;
    public int amount;
    public int shop;

    public PresentationProduct(int serialNumber, String name, String description, String category, double price, int amount, int shopID){
        this.serialNumber = serialNumber;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.amount = amount;
        this.shop = shopID;
    }

    public PresentationProduct(Product serviceProduct, int shopID){
        this.serialNumber = serviceProduct.getId();
        this.name = serviceProduct.getName();
        this.description = serviceProduct.getDescription();
        this.category = serviceProduct.getCategory();
        this.price = serviceProduct.getPrice();
        this.amount = serviceProduct.getAmount();
        this.shop = shopID;
    }

    public static List<PresentationProduct> convertProduct(List<Product> products, int shopID){
       return  products.stream().map(p-> new PresentationProduct(p, shopID)).collect(Collectors.toList());
    }
}
