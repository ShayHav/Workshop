package domain.shop;

public class ProductHistory implements Product{

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
    public int getId() {
        return 0;
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

    public int getNumSold() {
        return numSold;
    }
}
