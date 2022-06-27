package domain.shop.predicate;


import domain.shop.discount.Basket;

import java.time.*;
import java.util.function.Predicate;

public class PredicateManager {

    public static Predicate<Basket> createPricePredicate(double price){
        return new Predicate<Basket>() {
            @Override
            public boolean test(Basket basket) {
                return basket.calculateTotal() >= price;
            }

            @Override
            public String toString() {
                return String.format("if basket total before discounts exceeds %f", price);
            }
        };
    }

    public static Predicate<Basket> createMinimumProductsPredicate(int prodID, String productName,int amount){
        return new Predicate<Basket>() {
            @Override
            public boolean test(Basket basket) {
                Integer amountOfProductInPurchase;
                amountOfProductInPurchase = basket.findAmount(prodID);
                if(amountOfProductInPurchase < amount)
                    return false;
                return true;
            }

            @Override
            public String toString() {
                return String.format("if basket contains atleast %d product %s", amount, productName);
            }
        };
    }

    public static Predicate<Basket> createMaximumProductsPredicate(int prodID, String productName,int amount){
        return new Predicate<Basket>() {
            @Override
            public boolean test(Basket basket) {
                Integer amountOfProductInPurchase;
                amountOfProductInPurchase = basket.findAmount(prodID);
                if(amountOfProductInPurchase > amount)
                    return false;
                return true;
            }

            @Override
            public String toString() {
                return String.format("if basket contains atleast %d product %s", amount, productName);
            }
        };
    }

    public static Predicate<Basket> createTimePredicate(double from, double to){
        return new Predicate<Basket>() {
            @Override
            public boolean test(Basket basket) {
                double partial = ((LocalDateTime.now().getMinute()));
                partial = partial / 60;
                double currentTime = ((double) LocalDateTime.now().getHour()) + partial;
                if(to >= from)
                    return !(currentTime >= from && currentTime <= to);
                return !(currentTime >= from || currentTime <= to);
            }

            @Override
            public String toString() {
                return String.format("cannot be purchased between %f, %f", from, to);
            }
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
