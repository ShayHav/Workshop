package domain.ExternalConnectors;

public interface PaymentService {
    boolean connect();
    int processPayment(String fullName,String id, String creditCard, String expiredDate, String ccv, double total);
    boolean cancelPayment(int transactionID);
}
