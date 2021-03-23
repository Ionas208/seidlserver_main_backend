package com.seidlserver.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 09:45
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    public String server_url;
    public int port;

    public static Config getConfig(ConfigType type){
        return null;
    }
}
