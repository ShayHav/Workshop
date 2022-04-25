package Testing_System;

import domain.market.MarketSystem;
import domain.shop.Product;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.ShopManagersPermissions;
import domain.shop.TransactionInfo;
import domain.shop.discount.Discount;
import domain.user.Filter;

import java.util.List;
import java.util.Map;

public class RealBridge implements  Bridge{
    @Override
    public Result<Boolean, String> Login(String username, String pw) {
        return null;
    }

    @Override
    public Result<Boolean, String> Register(String username, String pw) {
        return null;
    }

    @Override
    public Result<Boolean, String> EnterMarket() {
        return null;
    }

    @Override
    public Result<Boolean, String> LeaveMarket() {
        return null;
    }

    @Override
    public Result<Boolean, String> Logout(String username) {
        return null;
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
    public Result<Boolean, String> PurchaseDelivery(TransactionInfo ti) {
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
    public Result<Boolean, String> AddExternalService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveExternalService(String path) {
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
    public Result<Boolean, String> AddProductToShopInventory(Product p, String shopname) {
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
}
