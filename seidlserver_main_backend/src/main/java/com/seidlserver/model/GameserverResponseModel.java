package com.seidlserver.model;

import com.seidlserver.pojos.gameserver.GameserverType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
    Created by: Jonas Seidl
    Date: 04.06.2021
    Time: 12:35
*/

/***
 * JSON Model Class to be used while returning a list of gameserver
 * Needed because all the information of a User (i.e. password) is not be sent with the response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameserverResponseModel {
    public int id;

    public String linuxuser;

    public String servername;

    private GameserverType type;

    private String owner;
}
