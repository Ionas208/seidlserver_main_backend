package com.seidlserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Gameserver(String script, String servername, GameserverType type) {
        this.script = script;
        this.servername = servername;
        this.type = type;
    }
}
