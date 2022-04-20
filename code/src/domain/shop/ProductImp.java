package domain.shop;

public class ProductImp implements Product{

    private final int id;
    private String name;
    private String description;
    private String category;

    public ProductImp(int id, String name, String description, String category){
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public ProductImp(Product p) {
        this.id = p.getId();
        this.description = p.getDescription();
        this.name = p.getName();
        this.category = p.getCategory();
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
