package com.seidlserver;

import com.seidlserver.network.StatBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    Created by: Jonas Seidl
    Date: 17.03.2021
    Time: 18:21
*/
@SpringBootApplication
public class SeidlserverApplication {
    public static void main(String[] args) {
        Thread statBuilder = new Thread(new StatBuilder());
        statBuilder.start();
        SpringApplication.run(SeidlserverApplication.class, args);
    }

}