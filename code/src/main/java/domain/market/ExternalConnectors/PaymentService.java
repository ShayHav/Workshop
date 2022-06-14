package domain.market.ExternalConnectors;

public interface PaymentService {
    boolean connect();
    int processPayment(String fullName,String id, String creditCard, String expiredDate, double total);
    boolean cancelPayment(int transactionID);
}
