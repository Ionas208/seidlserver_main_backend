package com.seidlserver.controller;

import com.seidlserver.db.GameserverManager;
import com.seidlserver.db.UserManager;
import com.seidlserver.model.GameserverModel;
import com.seidlserver.model.GameserverResponseModel;
import com.seidlserver.network.RequestHandler;
import com.seidlserver.pojos.gameserver.Gameserver;
import com.seidlserver.pojos.gameserver.GameserverType;
import com.seidlserver.pojos.user.User;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
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
    public ResponseEntity<List<GameserverResponseModel>> list(){
        User u = getUser();
        try{
            List<Gameserver> servers = gm.getGameserversForUser(u.getId());
            List<GameserverResponseModel> response = new ArrayList<>();
            for (Gameserver g: servers) {
                response.add(new GameserverResponseModel(g.getId(), g.getScript(), g.getServername(), g.getType(), g.getOwner().getEmail()));
            }
            return ResponseEntity.ok(response);
        }catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameserverType>> types(){
        try{
            List<GameserverType> types = gm.getGameserverTypes();
            return ResponseEntity.ok(types);
        }catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestBody GameserverModel gs
    ){
        User u = getUser();
        try{
            gm.addGameserver(gs.getScript(), gs.getServername(), gs.getType(), u.getId());
            return ResponseEntity.ok().build();
        } catch(HibernateException ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        } catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity remove(
            @RequestParam(name = "id", defaultValue = "-1") Integer id
            ){
        User u = getUser();
        try{
            gm.removeGameserverFromUser(id, u.getId());
            return ResponseEntity.ok().build();
        } catch(HibernateException ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
        } catch(HibernateException ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/start")
    public ResponseEntity start(@RequestParam(name = "id", defaultValue = "-1") Integer id){
        User u = getUser();
        Gameserver g = gm.getGameserversForId(id);
        if(g == null){
            return new ResponseEntity("Gameserver with ID "+id+" does not exist", HttpStatus.BAD_REQUEST);
        }
        if(!(g.getOwner().getId() == u.getId())){
            return new ResponseEntity("Gameserver with ID "+id+" is not accessible to user " + getUser().getEmail(), HttpStatus.UNAUTHORIZED);
        }
        try {
            RequestHandler.sendRequest("gameserver/start?script="+g.getScript(), "POST", true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/stop")
    public ResponseEntity stop(@RequestParam(name = "id", defaultValue = "-1") Integer id){
        User u = getUser();
        Gameserver g = gm.getGameserversForId(id);
        if(g == null){
            return new ResponseEntity("Gameserver with ID "+id+" does not exist", HttpStatus.BAD_REQUEST);
        }
        if(!(g.getOwner().getId() == u.getId())){
            return new ResponseEntity("Gameserver with ID "+id+" is not accessible to user " + getUser().getEmail(), HttpStatus.UNAUTHORIZED);
        }
        try {
            RequestHandler.sendRequest("gameserver/stop?script="+g.getScript(), "POST", true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/state")
    public ResponseEntity<String> state(@RequestParam(name = "id", defaultValue = "-1") Integer id){
        try {
            User u = getUser();
            Gameserver g = gm.getGameserversForId(id);
            if(g == null){
                return new ResponseEntity("Gameserver with ID "+id+" does not exist", HttpStatus.BAD_REQUEST);
            }
            if(!(g.getOwner().getId() == u.getId())){
                return new ResponseEntity("Gameserver with ID "+id+" is not accessible to user " + getUser().getEmail(), HttpStatus.UNAUTHORIZED);
            }
            String state = RequestHandler.sendRequest("gameserver/state?script="+g.getScript(), "GET", true);
            return ResponseEntity.ok(state);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserManager um = UserManager.getInstance();
        return um.getUserByEmail((String)auth.getPrincipal());
    }
}
