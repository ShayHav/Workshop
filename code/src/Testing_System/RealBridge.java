package Testing_System;

import Service.Services;
import domain.shop.Product;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.ShopManagersPermissions;
import domain.shop.discount.Discount;
import domain.user.Filter;
import domain.user.TransactionInfo;

import java.util.List;
import java.util.Map;

public class RealBridge implements  Bridge{


    private Services sv;
    public RealBridge()
    {
        sv = new Services();
    }

}
