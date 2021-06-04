package com.seidlserver.controller;

import com.seidlserver.wol.WakeOnLan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Created by: Jonas Seidl
    Date: 19.03.2021
    Time: 17:51
*/
@RestController
public class WOLController {
    @PostMapping("/wake")
    public ResponseEntity wake(){
        try{
            WakeOnLan.wake();
            return ResponseEntity.ok("Magic Packet sent");
        }catch(Exception ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
