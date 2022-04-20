package domain.shop;

public class ProductImp implements Product{

    private final int id;
    private final String name;
    private final String description;
    private final String category;

    public ProductImp(int id, String name, String description, String category){
        this.id = id;
        this.name = name;
        this.description = description;
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
    public int getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return category;
    }
}
