package domain.shop;

public class ServiceProduct implements Product{

    private int id;
    private String name;
    private String dis;
    private String cat;
    private double price;
    private int amount;
    public ServiceProduct(int id, String name, String dis, String cat, double price, int amount)
    {

    }

    public ServiceProduct(Product serviceProduct){
        this.id = serviceProduct.getId();
        this.name = serviceProduct.getName();
        this.dis = serviceProduct.getDescription();
        this.cat = serviceProduct.getCategory();
        this.price = serviceProduct.getPrice();
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getCategory() {
        return null;
    }

    public int getPrice() {
        return (int) price;
    }

    @Override
    public void setShopName(String s) {

    }

    @Override
    public void setShopRank(int s) {

    }

    public int getAmount() {
        return amount;
    }
}
