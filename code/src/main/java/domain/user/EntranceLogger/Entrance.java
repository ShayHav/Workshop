package domain.user.EntranceLogger;

import domain.user.User;

import java.time.LocalDate;

public class Entrance {

    private User enteredUser;
    private LocalDate dateOfEntrance;

    public Entrance(User user, LocalDate date){
        enteredUser = user;
        dateOfEntrance = date;
    }

    public LocalDate getDateOfEntrance() {
        return dateOfEntrance;
    }

    public User getEnteredUser() {
        return enteredUser;
    }
}
