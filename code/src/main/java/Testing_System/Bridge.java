package Testing_System;

import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Exceptions.ShopNotFoundException;
import domain.Response;
import domain.ResponseList;
import domain.ResponseMap;
import domain.ResponseT;
import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.notifications.UserObserver;
import domain.shop.*;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchOfficialsFilter;
import domain.user.filter.SearchOrderFilter;
import domain.user.filter.SearchUserFilter;

import java.util.List;
import java.util.Map;

public interface Bridge {

    //General Guest-Visitor
    ResponseT<User> Login(String username, String pw,UserObserver uo); //done

    Response Register(String username, String pw); //done

    ResponseT<User> EnterMarket();

    Response LeaveMarket(String username);

    //General Member-Visitor
    ResponseT<User> Logout(String username); //done

    ResponseT<Shop> CreateShop(String dis, String username, String shopname); //done


    //System
    Result<Boolean, String> RealTimeNotification(List<String> users, String msg);

    ResponseT<Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products);

    ResponseT<Boolean> Payment(TransactionInfo ti);

    Response StartMarket(PaymentService payment, SupplyService supply, String userID, String password); //done

    Result<Boolean, String> AddSupplyService(String path);

    Result<Boolean, String> RemoveSupplyService(String path);

    Result<Boolean, String> AddPaymentService(String path);

    Result<Boolean, String> RemovePaymentService(String path);

    //Guest-Visitor Shop option
    ResponseList<Shop> GetShopsInfo(String userName, Filter<Shop> filter);

    ResponseList<Product> GetProductInfoInShop(String userName, int shopID, Filter<Product> f);

    ResponseMap<Integer,List<Product>> SearchProductByName(String userName, String pName, Filter<Product> f);

    ResponseMap<Integer,List<Product>> SearchProductByKeyword(String userName, String keyword, Filter<Product> f);

    Response AddToShoppingCart(String userID, int shopID, int productId, int amount);

    Response EditShoppingCart(String userId, int shopId, int productId, int amount);

    Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate);

    Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname);

    Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname); //

    Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname);

    ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID);

    ResponseT<Product> AddProductToShopInventory(int serialNumber, String pName, String pDis, String pCat, double price, int amount, String username, int shopID);

    ResponseT<Product> ChangeProduct(String username, Product p, int shopID);
    /*
    Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingShopPolicy(String shopname);

    Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount);

    Result<Boolean, String> RemoveDiscountShopPolicy(String shopname);

    Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingProductPolicy(String shopname);

     */

    Response RemoveProductFromShopInventory(int productId, String username, int shopname); //done

    Response AppointNewShopOwner(int key, String targetUser, String userId); //done

    Response AppointNewShopManager(int key, String targetUser, String userId);//done

    Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID);//done

    Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID); //done

    Response CloseShop(int shopID, String username);//done

    Response OpenShop(int shopId, String userName);

    ResponseMap<Integer,List<Product>> SearchProductByCategory(String userName, String category, Filter<Product> f);

    ResponseList<User> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName);

    Result<Boolean, String> DeleteUserTest(String[] usernames);

    ResponseList<Order> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f, String userName);

    Response DismissalUserBySystemManager(String usernames,String targetUser);

    Response DismissalOwnerByOwner(String usernames,String targetUser,int shop);

    ResponseList<User> RequestUserInfo(SearchUserFilter f, String userName);

    Response CreateSystemManager(String systemManager,String username, String pw);

    }