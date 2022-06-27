package domain.shop;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
public class ProductImp implements Product{

    @Id
    private final int id;
    private String name;
    private String description;
    private String category;
    private double basePrice;
    private int rank;
    private int shopRank;
    private int quantity;

    public Product merge(ProductImp pi)
    {
        setName(pi.getName());
        setDescription(pi.getDescription());
        setCategory(pi.getCategory());
        setBasePrice(pi.getBasePrice());
        setShopRank(pi.getShopRank());
        setRank(pi.getRank());
        setQuantity(pi.getAmount());
        return this;
    }
    public void setRank(int rank)
    {
        this.rank = rank;
    }
    public ProductImp(int id, String name, String description, String category, double basePrice, int quantity){
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.basePrice = basePrice;
        this.quantity = quantity;
        //in the next versions, there will be a feedback from user that will change the rank initial value
        rank = -1;
    }

    public ProductImp(Product p) {
        this.id = p.getId();
        this.description = p.getDescription();
        this.name = p.getName();
        this.category = p.getCategory();
        this.basePrice = p.getPrice();
    }

    public ProductImp() {
        id = -1;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public void setShopRank(int shopRank) {
        this.shopRank = rank;
    }

    @Override
    public int getShopRank() {
        return shopRank;
    }

    public double getBasePrice() {return basePrice;}
    public void setBasePrice(double basePrice) {this.basePrice = basePrice;}
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public double getPrice() {
        return basePrice;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount(){
        return this.quantity;
    }

    @Override
    public Product merge(Product pi) {
        setName(pi.getName());
        setDescription(pi.getDescription());
        setCategory(pi.getCategory());
        if (pi instanceof  ProductImp)
            setBasePrice(((ProductImp)pi).getBasePrice());
        setShopRank(pi.getShopRank());
        setRank(pi.getRank());
        setQuantity(pi.getAmount());
        return this;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}
