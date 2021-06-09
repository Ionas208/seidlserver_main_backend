package com.seidlserver.controller;

import com.seidlserver.network.RequestHandler;
import com.seidlserver.network.StateHandler;
import com.seidlserver.pojos.state.State;
import com.seidlserver.wol.WakeOnLan;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 09:54
*/

/***
 * Controller for server related requests
 * Reachable under /server/
 */
@RestController
@RequestMapping("server")
public class ServerController {

    /***
     * Entrypoint for getting the state of the server (UP, DOWN)
     * @return ResponseEntity with Code
     *         200 OK: When fetching of state was successful
     *                 State included in response body
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @GetMapping(path = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<State> state(){
        return ResponseEntity.ok(StateHandler.getState());
    }

    /***
     * Entrypoint for starting the server via WOL
     * @return ResponseEntity with Code
     *         200 OK: When sending the Magic Packet was successful
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping("/start")
    public ResponseEntity start(){
        try{
            WakeOnLan.wake();
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for stopping the server
     * @return ResponseEntity with Code
     *         200 OK: When the stopping was successful
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping(path = "/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity stop(){
        try {
            RequestHandler.sendRequest("server/stop", "POST", false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /***
     * Entrypoint for restarting the server
     * @return ResponseEntity with Code
     *         200 OK: When restarting was successful
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @PostMapping(path = "/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity restart(){
        try {
            RequestHandler.sendRequest("server/restart", "POST", false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
