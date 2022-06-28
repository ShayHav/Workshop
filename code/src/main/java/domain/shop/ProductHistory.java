package domain.shop;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Embeddable
public class ProductHistory implements Product{

    private int serialNumber;
    private String name;
    private String description;
    private double price;
    private int numSold;
    private String category;

    public ProductHistory(Product p, double price, int amount){
        this.name = p.getName();
        this.description = p.getDescription();
        this.category = p.getCategory();
        this.price = price;
        this.numSold = amount;
        this.serialNumber = p.getId();
    }

    public ProductHistory() {

    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setNumSold(int numSold) {
        this.numSold = numSold;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    //TODO: remove when adding a DB
    @Override
    public int getRank() {
        return 0;
    }

    @Override
    public void setShopRank(int shopRank) {

    }

    @Override
    public int getShopRank() {
        return 0;
    }

    @Override
    public int getAmount() {
        return numSold;
    }

    @Override
    public Product merge(Product pi) {
        setName(pi.getName());
        setDescription(pi.getDescription());
        setPrice(pi.getPrice());
        setNumSold(pi.getAmount());
        setCategory(pi.getCategory());
        return this;
    }

    public int getId() {
        return serialNumber;
    }

    public int getNumSold() {
        return numSold;
    }
}
