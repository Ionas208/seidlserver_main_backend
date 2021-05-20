package com.seidlserver.network;

import com.seidlserver.pojos.state.State;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
    Created by: Jonas Seidl
    Date: 20.05.2021
    Time: 14:23
*/
public class StateHandler {

    private static String IP = "10.0.0.1";

    public static State getState(){
        try {
            InetAddress api = InetAddress.getByName(IP);
            if(api.isReachable(30)){
                return State.UP;
            }else{
                return State.DOWN;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return State.DOWN;
    }
}
