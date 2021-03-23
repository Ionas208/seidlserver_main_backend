package com.seidlserver.controller;

import com.seidlserver.beans.Gameserver;
import com.seidlserver.db.DBAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 10:46
*/
@RestController
@RequestMapping("server")
@CrossOrigin
public class GameserverController {
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Gameserver>> list(){
        try {
            DBAccess dba = DBAccess.getInstance();
            List<Gameserver> list = dba.getGameservers();
            return ResponseEntity.ok(list);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }
}
