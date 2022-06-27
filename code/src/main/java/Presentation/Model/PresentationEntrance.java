package Presentation.Model;

import domain.user.EntranceLogger.Entrance;

import java.time.LocalDate;

public class PresentationEntrance {

    private PresentationUser enteredUser;
    private LocalDate date;

    public PresentationEntrance(Entrance e){
        enteredUser = new PresentationUser(e.getEnteredUser());
        date = e.getDateOfEntrance();
    }

    public LocalDate getDate() {
        return date;
    }

    public PresentationUser getEnteredUser() {
        return enteredUser;
    }
}
