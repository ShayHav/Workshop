package domain.user;

import domain.Exceptions.InvalidParamException;

import java.time.LocalDate;

public class TransactionInfo {

    private String userID;
    private String fullName;
    private String address;
    private String city;
    private String country;
    private String zip;
    private String phoneNumber;
    private String cardNumber;
    private String expirationDate;
    private String ccv;
    private LocalDate transactionDate;
    private double totalAmount;

    public TransactionInfo(String userID, String fullName, String address,String city, String country, String zip,String phoneNumber,String cardNumber,
                           String expirationDate, String ccv, LocalDate transactionDate, double totalAmount){
        this.userID = userID;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.ccv = ccv;
    }



    public TransactionInfo(String userID, String fullName, String address,String phoneNumber,String cardNumber, String expirationDate, LocalDate transactionDate){
        this.userID = userID;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.transactionDate = transactionDate;
    }


    public void setTotalAmount(double totalAmount) throws InvalidParamException {
        if(totalAmount <= 0)
            throw new InvalidParamException("total amount must be more than 0");
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

    public String getUserID() {
        return userID;
    }

    public String getCcv() {
        return ccv;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getZip() {
        return zip;
    }
}
