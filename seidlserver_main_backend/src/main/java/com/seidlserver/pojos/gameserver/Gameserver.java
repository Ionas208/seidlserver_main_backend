package com.seidlserver.pojos.gameserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seidlserver.pojos.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 10:48
*/

/***
 * Data class for the Gameserver
 * Mapped to GAMESERVER Table in DB
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="GAMESERVER")
public class Gameserver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int id;

    @Column(name="linuxuser")
    public String linuxuser;

    @Column(name="servername")
    public String servername;

    @ManyToOne
    @JoinColumn(name = "type")
    private GameserverType type;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User owner;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class)
    @JoinTable(name="USER_GAMESERVER", joinColumns = { @JoinColumn(name = "gameserverid") }, inverseJoinColumns = { @JoinColumn(name = "userid") })
    private List<User> sharedUsers;

    public Gameserver(String linuxuser, String servername, GameserverType type, User user) {
        this.linuxuser = linuxuser;
        this.servername = servername;
        this.type = type;
        this.owner = user;
    }
}
