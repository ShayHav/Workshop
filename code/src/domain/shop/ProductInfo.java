package domain.shop;

public class ProductInfo {
    String shopName;
    int shopRank;
    String productName;
    String description;
    String category;
    double price;
    int productRank;

    public ProductInfo(String name, String description, String category, double price, int rank, String shopName, int shopRank) {
        this.productName = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.productRank = rank;
        this.shopName = shopName;
        this.shopRank = shopRank;
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
}
