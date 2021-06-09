package com.seidlserver.pojos.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    Created by: Jonas Seidl
    Date: 13.04.2021
    Time: 10:52
*/

/***
 * Data Class for a Memory Statistic Entry
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemStat {
    private LocalDateTime timestamp;
    private double memFree;
    private double memTotal;
}
