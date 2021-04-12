package com.seidlserver.controller;

import com.seidlserver.pojos.state.State;
import com.seidlserver.pojos.state.StateType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 09:54
*/
@RestController
@RequestMapping("server")
public class ServerController {

    public State state = new State(StateType.DOWN);

    @GetMapping(path = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<State> state(){
        return ResponseEntity.ok(state);
    }

    @PostMapping(path = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<State> start(){
        state.setState(StateType.UP);
        return ResponseEntity.ok(state);
    }

    @PostMapping(path = "/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<State> stop(){
        state.setState(StateType.DOWN);
        return ResponseEntity.ok(state);
    }

    @PostMapping(path = "/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<State> restart(){
        state.setState(StateType.UP);
        return ResponseEntity.ok(state);
    }
}
