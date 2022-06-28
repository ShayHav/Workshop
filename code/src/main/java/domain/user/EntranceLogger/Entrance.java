package domain.user.EntranceLogger;

import domain.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;

@Entity
public class Entrance {

    @Transient
    private User enteredUser;
    @Id
    private String username;
    @Id
    private LocalDate dateOfEntrance;

    public Entrance(User user, LocalDate date){
        enteredUser = user;
        dateOfEntrance = date;
        username = user.getUserName();
    }

    public void setEnteredUser(User enteredUser) {
        this.enteredUser = enteredUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDateOfEntrance(LocalDate dateOfEntrance) {
        this.dateOfEntrance = dateOfEntrance;
    }

    public Entrance() {

    }
//    public Entrance merge(Entrance e)
//    {
//
//    }


    public LocalDate getDateOfEntrance() {
        return dateOfEntrance;
    }

    public User getEnteredUser() {
        return enteredUser;
    }
}
