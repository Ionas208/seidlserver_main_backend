package com.seidlserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 12.04.2021
    Time: 17:11
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameserverModel {
    public String script;
    public String servername;
    public String type;
}
