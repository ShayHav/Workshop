package domain.user;

import java.time.LocalDate;

enum status {
    active,
    completed,
    failed_payment,
    failed_supply,
    failed_due_to_error
}

public class Transaction {

    private status status;
    private User user;
    private Cart cart;
    TransactionInfo billingInfo;


    public boolean checkout(String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        LocalDate transaction_date = LocalDate.now();
        double totalAmount = cart.getTotalAmount();
        billingInfo = new TransactionInfo(user.getId(),fullName, address, phoneNumber, cardNumber, expirationDate, transaction_date, totalAmount);
        return cart.checkout(billingInfo);

    }

    public void setStatus(status status) {
        this.status = status;
    }
}
