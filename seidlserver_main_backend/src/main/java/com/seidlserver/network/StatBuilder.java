package com.seidlserver.network;

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
public class StatBuilder implements Runnable{

    private static final int DELAY = 60_000*10;

    @Override
    public void run() {
        try {
            Thread.sleep(25000);
            while(true){
                //Cpu
                URL url = new URL("http://localhost/stats/cpu");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.getInputStream();

                //Mem
                url = new URL("http://localhost/stats/mem");
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
