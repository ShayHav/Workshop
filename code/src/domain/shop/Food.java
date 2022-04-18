package domain.shop;

public class Food implements Product {
    private String name;
    private String info;
    private int id;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getId() {
        return id;
    }
}
