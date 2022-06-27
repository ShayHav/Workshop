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
    private LocalDate dateOfEntrance;

    public Entrance(User user, LocalDate date){
        enteredUser = user;
        dateOfEntrance = date;
        username = enteredUser.getUserName();
    }

    public LocalDate getDateOfEntrance() {
        return dateOfEntrance;
    }

    public User getEnteredUser() {
        return enteredUser;
    }
}
