package com.company;

import domain.Exceptions.IncorrectIdentification;
import domain.user.UserController;

public class Main {
    static final int MAX_T = 2;
    public static void main(String[] args) throws InterruptedException, IncorrectIdentification {
        if(UserController.getInstance().getUser("nitay")!=null)
            System.out.println("WORKIIING");
        else System.out.println("NotGood");

    }
}
