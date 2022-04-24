package domain.user;

import java.time.LocalDate;

public class TransactionInfo {

    private int userID;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String cardNumber;
    private String expirationDate;
    private LocalDate transactionDate;
    private double totalAmount;


    public TransactionInfo(int userID, String fullName, String address,String phoneNumber,String cardNumber, String expirationDate, LocalDate transactionDate, double totalAmount){
        this.userID = userID;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getUserID() {
        return userID;
    }
}
