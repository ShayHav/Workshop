package domain.shop;

public interface Product {
    String getName();
    String getDescription();
    int getId();
    String getCategory();
    double getPrice();
    int getRank();
    void setShopRank(int shopRank);
    int getShopRank();
    int getAmount();
}
