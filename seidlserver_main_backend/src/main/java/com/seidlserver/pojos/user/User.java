package com.seidlserver.pojos.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seidlserver.pojos.gameserver.Gameserver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 24.03.2021
    Time: 11:12
*/

/***
 * Data class for the User
 * Mapped to USER Table in DB
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="USERS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails{
    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @JsonIgnore
    @Column(name="first_name")
    private String first_name;

    @JsonIgnore
    @Column(name="last_name")
    private String last_name;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @JsonIgnore
    @ManyToMany(targetEntity = Gameserver.class)
    @JoinTable(name="USER_GAMESERVER", joinColumns = { @JoinColumn(name = "userid") }, inverseJoinColumns = { @JoinColumn(name = "gameserverid") })
    private List<Gameserver> sharedGameservers;

    public User(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
