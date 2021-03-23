package com.seidlserver.controller;

import com.seidlserver.wol.WakeOnLan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Created by: Jonas Seidl
    Date: 19.03.2021
    Time: 17:51
*/
@RestController
@CrossOrigin
public class WOLController {
    @PostMapping("/wake")
    public String wake(){
        try{
            WakeOnLan.wake();
            return "Magic Packet sent";
        }catch(Exception ex){
            return ex.toString();
        }
    }
}
