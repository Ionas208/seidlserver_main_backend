package com.seidlserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
    Created by: Jonas Seidl
    Date: 25.03.2021
    Time: 21:06
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="GAMESERVERTYPE")
public class GameserverType {
    @Id
    @Column(name="name")
    private String name;

    @Column(name="url")
    private String url;
}
