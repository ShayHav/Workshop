package domain.market;

public interface PaymentService {
    boolean connect();
    boolean processPayment(String fullName,String id, String creditCard, String expiredDate, double total);
}
