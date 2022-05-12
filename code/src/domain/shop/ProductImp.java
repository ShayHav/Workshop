package domain.shop;

public class ProductImp implements Product{

    private final int id;
    private String name;
    private String description;
    private String category;
    private int rank;

    public ProductImp(int id, String name, String description, String category){
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
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

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ProductInfo getProductInfo(){
        return new ProductInfo(name,description,category,rank);
    }
}
