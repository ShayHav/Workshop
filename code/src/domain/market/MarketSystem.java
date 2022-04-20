package domain.market;

import domain.shop.ShopController;
import domain.user.TransactionInfo;
import domain.user.UserController;

import java.util.logging.Logger;

public class MarketSystem {
    private UserController uc; // singleton?
    private ShopController sc;
    private static final Logger logger = Logger.getLogger(MarketSystem.class.getName()); // array of loggers?

    public MarketSystem()
    {
        logger.info("System start");
        this.start();
    }


    /***
     * init the Market system:
     * Connect to payment service
     * Connect to supply service
     * Ensures there is at least 1 System manager
     */
    private void start()
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param ti - contains amount, method of payment etc
     * @return true if approve, false if otherwise
     */
    public boolean pay(TransactionInfo ti)
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param ti - should be address and maybe also date
     * @return - true if supply is approved, false otherwise
     */
    public boolean supply(TransactionInfo ti)
    {
        throw new UnsupportedOperationException();
    }


}
