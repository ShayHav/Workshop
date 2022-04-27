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

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
