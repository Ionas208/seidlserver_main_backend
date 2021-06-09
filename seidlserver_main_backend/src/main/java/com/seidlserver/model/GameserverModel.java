package com.seidlserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 12.04.2021
    Time: 17:11
*/

/***
 * JSON Model Class to be used while adding a new Gameserver
 * Needed because not all information of a Gameserver can be determined before adding it to the DB
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameserverModel {
    public String linuxuser;
    public String servername;
    public String type;
}
