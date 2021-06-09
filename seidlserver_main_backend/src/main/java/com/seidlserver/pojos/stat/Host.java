package com.seidlserver.pojos.stat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 06.04.2021
    Time: 10:15
*/

/***
 * Data class for Host
 * Used for extracting data from JSON
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {
    private String nodename;
    private String sysname;
    private String release;
    private String machine;
    @JsonProperty("number-of-cpus")
    private int num_cpus;
    private String date;
    private List<Statistic> statistics;
}
