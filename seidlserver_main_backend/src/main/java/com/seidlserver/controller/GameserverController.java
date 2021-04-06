package com.seidlserver.controller;

import com.seidlserver.db.GameserverManager;
import com.seidlserver.pojos.gameserver.Gameserver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 10:46
*/
@RestController
@RequestMapping("gameserver")
public class GameserverController {

    private GameserverManager gm = new GameserverManager();

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Gameserver>> list(
            @RequestParam(name = "userid", defaultValue = "-1") Integer userid
    ){
        if(!userid.equals(-1)){
            List<Gameserver> servers = gm.getGameserversForUser(userid);
            return ResponseEntity.ok(servers);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestParam(name = "servername", defaultValue = "") String servername,
            @RequestParam(name = "script", defaultValue = "") String script,
            @RequestParam(name = "type", defaultValue = "") String type,
            @RequestParam(name = "userid", defaultValue = "-1") Integer userid
    ){
        if(!(servername.equals("") || script.equals("") || type.equals("") || userid.equals(-1))){
            gm.addGameserver(servername, script, type, userid);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestParam(name = "id", defaultValue = "-1") Integer id
            ){
        if(!id.equals(-1)){
            gm.removeGameserver(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
