package domain.shop;

public class ShopInfo {
    private String name;
    private int rank;

    public ShopInfo(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }
}
