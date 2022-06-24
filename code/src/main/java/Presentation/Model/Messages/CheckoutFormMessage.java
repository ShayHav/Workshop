package Presentation.Model.Messages;

public class CheckoutFormMessage {
    private String fullName;
    private String address;
    private String city;
    private String country;
    private String zip;
    private String phoneNumber;
    private String cardNumber;
    private String ccv;
    private String month;
    private String year;

    public CheckoutFormMessage(){

    }


    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
