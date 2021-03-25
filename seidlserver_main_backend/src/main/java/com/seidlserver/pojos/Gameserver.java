package com.seidlserver.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 10:48
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gameserver {
    public int id;
    public String name;
    public String url;
    public String script;
}
