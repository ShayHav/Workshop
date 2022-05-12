package domain.shop;

public interface Product {
    String getName();
    String getDescription();
    int getId();
    String getCategory();
    int getPrice();
    void setShopName(String s);
    void setShopRank(int s);
}
