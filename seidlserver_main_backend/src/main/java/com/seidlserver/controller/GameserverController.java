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

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 10:46
*/
/***
 * Controller for gameserver related requests
 * Reachable under /gameserver/
 */
@RestController
@RequestMapping("gameserver")
public class GameserverController {

    private GameserverManager gm = GameserverManager.getInstance();

    /***
     * Entrypoint for getting an array for all the avaible gameservers of the user
     * @return ResponseEntity with Code
     *         200 OK: When the list was fetched successfully
     *                 JSON Array of Gameservers is included in the response body
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     *
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameserverResponseModel>> list(){
        User u = getUser();
        try{
            List<Gameserver> servers = gm.getGameserversForUser(u.getId());
            List<GameserverResponseModel> response = new ArrayList<>();
            for (Gameserver g: servers) {
                response.add(new GameserverResponseModel(g.getId(), g.getLinuxuser(), g.getServername(), g.getType(), g.getOwner().getEmail()));
            }
            return ResponseEntity.ok(response);
        }catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for getting an array of all the available Gameservertypes (i.e. ARK, TS3, ...)
     * @return ResponseEntity with Code
     *         200 OK: When the list was successfully fetched
     *                 JSON array of types is included in the response body
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
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

    /***
     * Entrypoint for adding an Gameserver to the current user
     * @param gs JSON Model of the Gameserver
     * @return ResponseEntity with Code
     *         200 OK: When adding was successful
     *         409 CONFLICT: When the script is not unique
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(
            @RequestBody GameserverModel gs
    ){
        User u = getUser();
        try{
            gm.addGameserver(gs.getLinuxuser(), gs.getServername(), gs.getType(), u.getId());
            return ResponseEntity.ok().build();
        } catch(HibernateException ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        } catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for removing a gameserver identified via its id
     * @param id The id of the gameserver
     * @return ResponseEntity with Code
     *         200 OK: When the removal was successful
     *         401 UNAUTHORIZED: When the gameserver to remove does not belong to the current user
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
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

    /***
     * Entrypoint for sharing a gameserver to another user
     * @param serverid The ID of the server to share
     * @param email The email of the user to be shared with
     * @return ResponseEntity with Code
     *         200 OK: When the sharing was successful
     *         400 BAD REQUEST: When the gameserver is already shared with the user
     *         401 UNAUTHORIZED: When the gameserver to share does not belong to the current user
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
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
        catch(PersistenceException ex){
            ex.printStackTrace();
            return new ResponseEntity("Server is already shared with user.", HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for unsharing a gameserver from the current user
     * @param serverid The ID of the server to share
     * @return ResponseEntity with Code
     *         200 OK: When the unsharing was successful
     *         401 UNAUTHORIZED: When the gameserver to unshare is not shared with the user
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping(value = "/unshare", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity unshare(
            @RequestParam(name = "serverid") Integer serverid
    ){
        User u = getUser();
        try{
            gm.unshareGameserver(serverid, u.getId());
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

    /***
     * Entrypoint for starting a gameserver
     * @param id The ID of the gameserver to be started
     * @return ResponseEntity with Code
     *         200 OK: When the starting was successful
     *         400 BAD REQUEST: When the gameserver does not exist
     *         401 UNAUTHORIZED: When the gameserver to start is not accessible to the current user
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping("/start")
    public ResponseEntity start(@RequestParam(name = "id", defaultValue = "-1") Integer id){
        User u = getUser();
        Gameserver g = gm.getGameserverForId(id);
        if(g == null){
            return new ResponseEntity("Gameserver with ID "+id+" does not exist", HttpStatus.BAD_REQUEST);
        }
        if(!gm.isAccessibleTo(id, u.getId())){
            return new ResponseEntity("Gameserver with ID "+id+" is not accessible to user " + getUser().getEmail(), HttpStatus.UNAUTHORIZED);
        }
        try {
            RequestHandler.sendRequest("gameserver/start?linuxuser="+g.getLinuxuser()+"&script="+g.getType().getScript(), "POST", true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for stopping a gameserver
     * @param id The ID of the gameserver to be stopped
     * @return ResponseEntity with Code
     *         200 OK: When the stopping was successful
     *         400 BAD REQUEST: When the gameserver does not exist
     *         401 UNAUTHORIZED: When the gameserver to stop is not accessible to the current user
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping("/stop")
    public ResponseEntity stop(@RequestParam(name = "id", defaultValue = "-1") Integer id){
        User u = getUser();
        Gameserver g = gm.getGameserverForId(id);
        if(g == null){
            return new ResponseEntity("Gameserver with ID "+id+" does not exist", HttpStatus.BAD_REQUEST);
        }
        if(!gm.isAccessibleTo(id, u.getId())){
            return new ResponseEntity("Gameserver with ID "+id+" is not accessible to user " + getUser().getEmail(), HttpStatus.UNAUTHORIZED);
        }
        try {
            RequestHandler.sendRequest("gameserver/stop?linuxuser="+g.getLinuxuser()+"&script="+g.getType().getScript(), "POST", true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for getting the current state of a gameserver (ONLINE or OFFLINE)
     * @param id The ID of the gameserver to know the state of
     * @return ResponseEntity with Code
     *         200 OK: When the fetching of the state was successful
     *                 State included in the response body
     *         400 BAD REQUEST: When the gameserver does not exist
     *         401 UNAUTHORIZED: When the gameserver to fetch the state of is not accessible to the current user
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @GetMapping("/state")
    public ResponseEntity<String> state(@RequestParam(name = "id", defaultValue = "-1") Integer id){
        try {
            User u = getUser();
            Gameserver g = gm.getGameserverForId(id);
            if(g == null){
                return new ResponseEntity("Gameserver with ID "+id+" does not exist", HttpStatus.BAD_REQUEST);
            }
            if(!gm.isAccessibleTo(id, u.getId())){
                return new ResponseEntity("Gameserver with ID "+id+" is not accessible to user " + getUser().getEmail(), HttpStatus.UNAUTHORIZED);
            }
            String state = RequestHandler.sendRequest("gameserver/state?linuxuser="+g.getLinuxuser()+"&script="+g.getType().getScript(), "GET", true);
            return ResponseEntity.ok(state);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * This method fetches the user from the current Security Context
     * (i.e. from the JWT token sent with the request)
     * @return current User
     */
    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserManager um = UserManager.getInstance();
        return um.getUserByEmail((String)auth.getPrincipal());
    }
}
