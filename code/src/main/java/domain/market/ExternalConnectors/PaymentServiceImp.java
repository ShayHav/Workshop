package domain.market.ExternalConnectors;

public class PaymentServiceImp implements PaymentService{
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public int processPayment(String fullName, String id, String creditCard, String expiredDate, double total) {
        return 1;
    }

    @Override
    public boolean cancelPayment(int transactionID) {
        return true;
    }
}
