package domain.shop;

public class ProductImp implements Product{

    private final int id;
    private String name;
    private String description;
    private String category;
    private double basePrice;
    private int rank;
    private int shopRank;
    private int amount;

    public ProductImp(int id, String name, String description, String category, double basePrice, int amount){
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.basePrice = basePrice;
        this.amount = amount;
        //in the next versions, there will be a feedback from user that will change the rank initial value
        rank = -1;
    }

    public ProductImp(Product p) {
        this.id = p.getId();
        this.description = p.getDescription();
        this.name = p.getName();
        this.category = p.getCategory();
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
        return this.amount;
    }
}
