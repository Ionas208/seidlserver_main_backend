package com.seidlserver.db;

import com.seidlserver.beans.Gameserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
    Created by: Jonas Seidl
    Date: 17.03.2021
    Time: 18:44
*/
public class DBAccess {

    private DB db;
    private static DBAccess instance;

    public static DBAccess getInstance() throws SQLException {
        if(instance==null){
            instance = new DBAccess();
        }
        return instance;
    }

    private DBAccess() throws SQLException {
        db = new DB();
    }

    public void register(
            String first_name,
            String last_name,
            String email,
            String password
    ) throws SQLException {

        String sql = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?);";

        PreparedStatement ps = db.getPreparedStatement(sql);

        ps.setString(1, first_name);
        ps.setString(2, last_name);
        ps.setString(3, email);
        ps.setString(4, password);

        ps.execute();
    }

    public boolean login(String email, String password) throws SQLException {
        String sql = "SELECT password FROM users WHERE email = ?;";
        PreparedStatement ps = db.getPreparedStatement(sql);

        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();
        rs.next();
        String password_from_db = rs.getString("password");

        if(password.equals(password_from_db)){
            return true;
        }else{
            return false;
        }
    }

    public List<Gameserver> getGameservers() throws SQLException {
        String sql = "SELECT * FROM gameserver g INNER JOIN gameservertype gt ON g.type = gt.name;";
        Statement s = db.getStatement();
        ResultSet rs = s.executeQuery(sql);

        List<Gameserver> gameservers = new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String url = rs.getString("url");
            Gameserver g = new Gameserver(id, name, url);
            gameservers.add(g);
        }
        return gameservers;
    }
}
