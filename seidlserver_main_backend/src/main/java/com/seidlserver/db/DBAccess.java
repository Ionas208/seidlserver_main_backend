package com.seidlserver.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            String username,
            String first_name,
            String last_name,
            String email,
            String password
    ) throws SQLException {

        String sql = "INSERT INTO users(username, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?);";

        PreparedStatement ps = db.getPreparedStatement(sql);

        ps.setString(1, username);
        ps.setString(2, first_name);
        ps.setString(3, last_name);
        ps.setString(4, email);
        ps.setString(5, password);

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
}
