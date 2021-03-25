package com.seidlserver.controller;

import com.seidlserver.db.GameserverManager;
import com.seidlserver.pojos.Gameserver;
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
    public ResponseEntity<List<Gameserver>> list(){
        System.out.println("HEREEEEEEEEEEEEEE");
        List<Gameserver> servers = gm.getGameservers();
        return ResponseEntity.ok(servers);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestParam(name = "servername", defaultValue = "") String servername,
            @RequestParam(name = "script", defaultValue = "") String script,
            @RequestParam(name = "type", defaultValue = "") String type
    ){
        if(!(servername.equals("") || script.equals("") || type.equals(""))){
            gm.addGameserver(servername, script, type);
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
