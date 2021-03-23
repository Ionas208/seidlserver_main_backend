package com.seidlserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seidlserver.beans.State;
import com.seidlserver.beans.StateType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 09:54
*/
@RestController
@CrossOrigin
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
