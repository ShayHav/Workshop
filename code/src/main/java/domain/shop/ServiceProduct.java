package domain.shop;

public class ServiceProduct implements Product{

    private int id;
    private String name;
    private String description;
    private String category;
    private double price;
    private int amount;
    public ServiceProduct(int id, String name, String description, String category, double price, int amount)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.amount = amount;

    }

    public ServiceProduct(ProductImp serviceProduct){
        this.id = serviceProduct.getId();
        this.name = serviceProduct.getName();
        this.description = serviceProduct.getDescription();
        this.category = serviceProduct.getCategory();
        this.price = serviceProduct.getPrice();
        this.amount = serviceProduct.getAmount();
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
        return id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

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

    public int getAmount() {
        return amount;
    }

    @Override
    public Product merge(Product pi) {
        return null;
    }
}
