package Presentation.Model;

import domain.shop.ServiceProduct;

public class PresentationProduct {
    public int serialNumber;
    public String name;
    public String description;
    public String category;
    public double price;
    public int amount;

    public PresentationProduct(int serialNumber, String name, String description, String category, double price, int amount){
        this.serialNumber = serialNumber;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.amount = amount;
    }

    public PresentationProduct(ServiceProduct serviceProduct){
        this.serialNumber = serviceProduct.getId();
        this.name = serviceProduct.getName();
        this.description = serviceProduct.getDescription();
        this.category = serviceProduct.getCategory();
        this.price = serviceProduct.getPrice();
        this.amount = serviceProduct.getAmount();
    }

}
