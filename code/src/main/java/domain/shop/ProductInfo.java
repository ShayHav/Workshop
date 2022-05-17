package domain.shop;

public class ProductInfo {
    private String shopName;
    private int shopRank;
    private String productName;
    private String description;
    private String category;
    private double price;
    private int productRank;

    public ProductInfo(String name, String description, String category, int rank) {
        this.productName = name;
        this.description = description;
        this.category = category;
        this.productRank = rank;
    }

    public ProductInfo(){
        this.productName = "---";
        this.description = "this product does not exist";
        this.category = "---";
        this.productRank = -1000;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getProductRank() {
        return productRank;
    }

    public String getCategory() {
        return category;
    }

    public int getShopRank() {
        return shopRank;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopRank(int shopRank) {
        this.shopRank = shopRank;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
