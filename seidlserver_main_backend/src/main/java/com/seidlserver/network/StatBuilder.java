package com.seidlserver.network;

import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/*
    Created by: Jonas Seidl
    Date: 20.05.2021
    Time: 12:21
*/

/***
 * Thread class to asynchronously send requests to the App in order to build the statistics
 */
public class StatBuilder implements Runnable{

    private static final int DELAY = 60_000*10;

    @Override
    public void run() {
        try {
            Thread.sleep(25000);
            while(true){
                //Cpu
                URL url = new URL("http://localhost;8080/stats/cpu");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getInputStream();

                //Mem
                url = new URL("http://localhost;8080/stats/mem");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getInputStream();

                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
