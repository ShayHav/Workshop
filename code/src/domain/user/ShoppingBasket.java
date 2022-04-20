package domain.user;

import domain.Tuple;
import domain.shop.Product;
import domain.shop.Shop;

import java.util.LinkedList;
import java.util.List;

public class ShoppingBasket {
    private Shop shop;
    List<Tuple<Integer,Integer>> productAmountList = new LinkedList<>();

    public ShoppingBasket(Shop shop,int product,int amount){
        this.shop=shop;
        productAmountList.add(new Tuple<>(product,amount));
    }

    public boolean updateAmount(int product, int amount) {
        for (Tuple<Integer,Integer> run : productAmountList){
            if(run.x == product){
                run.y = amount;
                return true;
            }
        }
        return false;
    }
}
