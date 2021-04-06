package com.seidlserver.pojos.stat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 06.04.2021
    Time: 10:16
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statistic {
    private String timestamp;
    @JsonProperty("cpu-load")
    private List<CpuLoad>  load;
}
