package domain.shop;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductHistory implements Product{

    @Id
    private final int serialNumber;
    private final String name;
    private final String description;
    private final double price;
    private final int numSold;
    private final String category;

    public ProductHistory(Product p, double price, int amount){
        this.name = p.getName();
        this.description = p.getDescription();
        this.category = p.getCategory();
        this.price = price;
        this.numSold = amount;
        this.serialNumber = p.getId();
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

    public int getId() {
        return serialNumber;
    }

    public int getNumSold() {
        return numSold;
    }
}
