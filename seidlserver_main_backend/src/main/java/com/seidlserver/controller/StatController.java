package com.seidlserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seidlserver.pojos.stat.CpuLoad;
import com.seidlserver.pojos.stat.CpuStat;
import com.seidlserver.pojos.stat.Host;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 06.04.2021
    Time: 09:55
*/
@RestController
@RequestMapping("stats")
public class StatController {

    private String testJson = "{\"sysstat\": {\"hosts\": [{\"nodename\": \"Lenovo\",\"sysname\": \"Linux\",\"release\": \"4.4.0-19041-Microsoft\",\"machine\": \"x86_64\",\"number-of-cpus\": 12,\"date\": \"04/06/21\",\"statistics\": [ {\"timestamp\": \"10:05:36\",\"cpu-load\": [{\"cpu\": \"all\", \"usr\": 7.25, \"nice\": 0.00, \"sys\": 3.93, \"iowait\": 0.00, \"irq\": 0.12, \"soft\": 0.00, \"steal\": 0.00, \"guest\": 0.00, \"gnice\": 0.00, \"idle\": 88.70}]}] }    ]}}";

    private List<CpuStat> cpuStats = new ArrayList<>();

    @GetMapping(path = "/cpu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CpuStat>> cpu(){
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode node = om.readTree(testJson);
            CpuLoad load =  om.readValue(node.get("sysstat").get("hosts").toString(), Host[].class)[0].getStatistics().get(0).getLoad().get(0);
            cpuStats.add(new CpuStat(LocalDateTime.now(), load));
            return ResponseEntity.ok(cpuStats);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public static void main(String[] args) {
        String testJson = "{\"sysstat\": {\"hosts\": [{\"nodename\": \"Lenovo\",\"sysname\": \"Linux\",\"release\": \"4.4.0-19041-Microsoft\",\"machine\": \"x86_64\",\"number-of-cpus\": 12,\"date\": \"04/06/21\",\"statistics\": [ {\"timestamp\": \"10:05:36\",\"cpu-load\": [{\"cpu\": \"all\", \"usr\": 7.25, \"nice\": 0.00, \"sys\": 3.93, \"iowait\": 0.00, \"irq\": 0.12, \"soft\": 0.00, \"steal\": 0.00, \"guest\": 0.00, \"gnice\": 0.00, \"idle\": 88.70}]}] }    ]}}";

        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode node = om.readTree(testJson);
            CpuLoad load =  om.readValue(node.get("sysstat").get("hosts").toString(), Host[].class)[0].getStatistics().get(0).getLoad().get(0);
            System.out.println(load);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
