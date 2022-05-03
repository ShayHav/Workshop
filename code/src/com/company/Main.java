package com.company;

import Service.Services;
import domain.market.MarketSystem;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;

public class Main {

    public static void main(String[] args) {
        MarketSystem marketSystem = MarketSystem.getInstance();
        marketSystem.start(new PaymentServiceImp(),new SupplyServiceImp(),"Admin","Admin123");
    }
}
