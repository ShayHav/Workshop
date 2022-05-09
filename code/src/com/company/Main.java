package com.company;

import Service.Services;
import domain.market.MarketSystem;
import domain.user.IncorrectIdentification;
import domain.user.UserController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static final int MAX_T = 2;
    public static void main(String[] args) throws InterruptedException, IncorrectIdentification {
        Services services1 = new Services();
        Services services2 = new Services();
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        pool.execute(services1);


        if(UserController.getInstance().getUser("nitay")!=null)
            System.out.println("WORKIIING");
        else System.out.println("NotGood");

    }
}
