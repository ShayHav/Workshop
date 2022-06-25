package domain.ExternalConnectors;

public class PaymentServiceImp implements PaymentService{
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public int processPayment(String fullName, String id, String creditCard, String expiredDate, String ccv, double total) {
        return 15000;
    }

    @Override
    public boolean cancelPayment(int transactionID) {
        return true;
    }
}
