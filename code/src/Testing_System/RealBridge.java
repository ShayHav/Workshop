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

    @Override
    public Result<Boolean, Boolean> Login(String username, String pw) {
        return sv.Login(username,pw);
    }

    @Override
    public Result<Boolean, Boolean> Register(String username, String pw) {
        return sv.Register(username,pw);
    }

    @Override
    public Result<Boolean,String> EnterMarket() {
        return sv.EnterMarket();
    } //??

    @Override
    public Result<Boolean, String> LeaveMarket() {
        return sv.LeaveMarket();
    }

    @Override
    public Result<Boolean, Boolean> Logout(String username) {
        return sv.Logout();
    }

    @Override
    public Result<Boolean, String> CreateShop(String username, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return null;
    }

    @Override
    public Result<Boolean, String> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products) {
        return null;
    }

    @Override
    public Result<Boolean, String> Payment(TransactionInfo ti) {
        return null;
    }

    @Override
    public Result<Boolean, String> StartMarket() {
        return null;    }

    @Override
    public Result<Boolean, String> AddSupplyService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveSupplyService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddPaymentService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemovePaymentService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> GetShopsInfo() {
        return null;
    }

    @Override
    public Result<Boolean, String> GetProductInfoInShop(String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> SearchProductByName(String pName) {
        return null;
    }

    @Override
    public Result<Boolean, String> SearchProductByCategory(String pName) {
        return null;
    }

    @Override
    public Result<Boolean, String> SearchProductByKeyword(String keyword) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddToShoppingCart(Product p, String shopname, int amount) {
        return null;
    }

    @Override
    public Result<Boolean, String> EditShoppingCart(Product p, String shopname, int amount) {
        return null;
    }

    @Override
    public Result<Boolean, String> Checkout() {
        return null;
    }

    @Override
    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> CheckIfProductAvailable(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, Integer> AddProductToShopInventory(Product p,String username, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveProductToShopInventory(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> ChangeProductDetail(Product p, String shopname, Map<String, String> newinfo) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveBuyingShopPolicy(String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveDiscountShopPolicy(String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveBuyingProductPolicy(String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> AppointNewShopOwner(String username) {
        return null;
    }

    @Override
    public Result<Boolean, String> AppointNewShopManager(String username) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddShopMangerPermissions(String username, List<ShopManagersPermissions> permissions) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveShopManagerPermissions(String username, List<ShopManagersPermissions> permissions) {
        return null;
    }

    @Override
    public Result<Boolean, String> CloseShop(String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> RequestShopOfficialsInfo(String shopname, Filter f) {
        return null;
    }

    @Override
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveProductFromShopInventory(int id, String username, String shopname) {
        return null;
    }
}
