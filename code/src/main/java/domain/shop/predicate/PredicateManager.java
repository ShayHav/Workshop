package domain.shop.predicate;


import domain.shop.discount.Basket;

import java.time.*;
import java.util.function.Predicate;

public class PredicateManager {

    public static Predicate<Basket> createPricePredicate(double price){
        return (i)-> i.calculateTotal() >= price;
    }

    public static Predicate<Basket> createMinimumProductsPredicate(int prodID, int amount){
        return (i)-> {
            Integer amountOfProductInPurchase;
            amountOfProductInPurchase = i.findAmount(prodID);
            if(amountOfProductInPurchase < amount)
                return false;
            return true;
        };
    }

    public static Predicate<Basket> createMaximumProductsPredicate(int prodID, int amount){
        return (i)-> {
            Integer amountOfProductInPurchase;
            amountOfProductInPurchase = i.findAmount(prodID);
            if(amountOfProductInPurchase > amount)
                return false;
            return true;
        };
    }

    public static Predicate<Basket> createTimePredicate(double from, double to){
        return (i)-> {
            double currentTime = LocalDateTime.now().getHour() + (LocalDateTime.now().getMinute()/60);
            if(to >= from)
                return !(currentTime >= from && currentTime <= to);
            return !(currentTime >= from || currentTime <= to);
        };
    }

    public static Predicate<Basket> orPredicate(Predicate<Basket> pred1, Predicate<Basket> pred2){
        return (i)->{
            return pred1.test(i) || pred2.test(i);
        };
    }

    public static Predicate<Basket> xorPredicate(Predicate<Basket> pred1, Predicate<Basket> pred2){
        return (i)->{
            boolean bool1 = pred1.test(i);
            boolean bool2 = pred2.test(i);
            boolean toRet;
            if((bool1 && bool2) || (!bool1 && !bool2))
                toRet = false;
            else
                toRet = true;
            return toRet;
        };
    }

    public static Predicate<Basket> andPredicate(Predicate<Basket> pred1, Predicate<Basket> pred2){
        return (i)->{
            return pred1.test(i) && pred2.test(i);
        };
    }

}
