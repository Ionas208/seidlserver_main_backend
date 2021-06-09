package com.seidlserver.pojos.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    Created by: Jonas Seidl
    Date: 06.04.2021
    Time: 11:26
*/

/***
 * Data Class for CPU load Statistic Entry
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpuStat {
    private LocalDateTime timestamp;
    private CpuLoad load;
}
