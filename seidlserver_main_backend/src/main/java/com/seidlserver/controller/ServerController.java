package com.seidlserver.controller;

import com.seidlserver.network.RequestHandler;
import com.seidlserver.network.StateHandler;
import com.seidlserver.pojos.state.State;
import com.seidlserver.wol.WakeOnLan;
import org.apache.coyote.Request;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 09:54
*/
@RestController
@RequestMapping("server")
public class ServerController {

    @GetMapping(path = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<State> state(){
        return ResponseEntity.ok(StateHandler.getState());
    }

    @PostMapping("/start")
    public ResponseEntity start(){
        try{
            WakeOnLan.wake();
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity stop(){
        try {
            RequestHandler.sendRequest("server/stop", "POST", false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping(path = "/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity restart(){
        try {
            RequestHandler.sendRequest("server/restart", "POST", false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
