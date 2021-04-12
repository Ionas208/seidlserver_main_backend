package com.seidlserver.controller;

import com.seidlserver.db.GameserverManager;
import com.seidlserver.db.UserManager;
import com.seidlserver.model.GameserverModel;
import com.seidlserver.pojos.gameserver.Gameserver;
import com.seidlserver.pojos.user.User;
import org.hibernate.HibernateException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private GameserverManager gm = GameserverManager.getInstance();

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Gameserver>> list(){
        User u = getUser();
        try{
            List<Gameserver> servers = gm.getGameserversForUser(u.getId());
            return ResponseEntity.ok(servers);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestBody GameserverModel gs
    ){
        User u = getUser();
        try{
            gm.addGameserver(gs.getServername(), gs.getScript(), gs.getType(), u.getId());
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestParam(name = "id", defaultValue = "-1") Integer id
            ){
        User u = getUser();
        try{
            gm.removeGameserverFromUser(id, u.getId());
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/share", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity share(
            @RequestParam(name = "serverid") Integer serverid,
            @RequestParam(name = "email") String email
    ){
        User u = getUser();
        UserManager um = UserManager.getInstance();
        try{
            User recipient = um.getUserByEmail(email);
            gm.shareGameserver(serverid, u.getId(), recipient.getId());
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserManager um = UserManager.getInstance();
        return um.getUserByEmail((String)auth.getPrincipal());
    }
}
