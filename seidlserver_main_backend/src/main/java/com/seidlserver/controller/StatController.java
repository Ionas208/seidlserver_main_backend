package com.seidlserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seidlserver.pojos.stat.CpuLoad;
import com.seidlserver.pojos.stat.CpuStat;
import com.seidlserver.pojos.stat.Host;
import com.seidlserver.pojos.stat.MemStat;
import com.seidlserver.network.RequestHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
    Created by: Jonas Seidl
    Date: 06.04.2021
    Time: 09:55
*/

/***
 * Controller for statistics related requests
 * Reachable under /stats/
 */
@RestController
@RequestMapping("stats")
public class StatController {
    private List<CpuStat> cpuStats = new ArrayList<>();
    private List<MemStat> memStats = new ArrayList<>();

    /***
     * Entrypoint for getting the current load of the CPU
     * @return ResponseEntity with Code
     *         200 OK: When the fetching of the load was successful
     *                 Load included in response body
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @GetMapping(path = "/cpu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CpuStat>> cpu(){
        ObjectMapper om = new ObjectMapper();
        try {
            String cpu = RequestHandler.sendRequest("server/cpu", "GET", true);
            JsonNode node = om.readTree(cpu);
            CpuLoad load =  om.readValue(node.get("sysstat").get("hosts").toString(), Host[].class)[0].getStatistics().get(0).getLoad().get(0);
            cpuStats.add(new CpuStat(LocalDateTime.now(), load));
            return ResponseEntity.ok(cpuStats);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Entrypoint for getting the current memory usage
     * @return ResponseEntity with Code
     *         200 OK: When the fetching of the memory was successful
     *                 Free memory and Total memory included in response body
     *         500 INTERNAL SERVER ERROR: When there is some other error
     *                                    Error message is included in the response body
     */
    @GetMapping(path = "/mem", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MemStat>> mem(){
        try {
            double memFree = Double.parseDouble(RequestHandler.sendRequest("server/memFree", "GET", true));
            double memTotal = Double.parseDouble(RequestHandler.sendRequest("server/memTotal", "GET", true));
            MemStat memStat = new MemStat(LocalDateTime.now(), memFree, memTotal);
            memStats.add(memStat);
            return ResponseEntity.ok(memStats);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
