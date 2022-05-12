package domain.market;

public class PaymentServiceImp implements PaymentService{
    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public boolean processPayment(String fullName, String id, String creditCard, String expiredDate, double total) {
        return true;
    }
}
