package DB;

import domain.shop.Inventory;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InventoryTable {

    @Id
    private int shopID;
    @Id
    private int productID;
    private int amountInStock;

    public InventoryTable merge (InventoryTable it)
    {
        setShopID(it.getShopID());
        setProductID(it.getProductID());
        setAmountInStock(it.getAmountInStock());
        return this;
    }
    public InventoryTable(){}
    public InventoryTable(int shopId, int pID, int amount)
    {
        this.shopID = shopId;
        this.productID = pID;
        this.amountInStock = amount;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }
}
