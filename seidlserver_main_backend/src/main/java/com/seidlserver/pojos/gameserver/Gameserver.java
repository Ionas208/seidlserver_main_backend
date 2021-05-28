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

    @Column(name="script")
    public String script;

    @Column(name="servername")
    public String servername;

    @ManyToOne
    @JoinColumn(name = "type")
    private GameserverType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class)
    @JoinTable(name="USER_GAMESERVER", joinColumns = { @JoinColumn(name = "gameserverid") }, inverseJoinColumns = { @JoinColumn(name = "userid") })
    private List<User> sharedUsers;

    public Gameserver(String script, String servername, GameserverType type, User user) {
        this.script = script;
        this.servername = servername;
        this.type = type;
        this.user = user;
    }
}
