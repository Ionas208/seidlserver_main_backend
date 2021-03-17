package com.seidlserver.db;

import java.sql.*;

/*
    Created by: Jonas Seidl
    Date: 17.03.2021
    Time: 18:49
*/
public class DB {
    private String db_url = "jdbc:postgresql://localhost:5432/seidlserverdb";
    private String db_driver = "org.postgresql.Driver";
    private String db_username = "postgres";
    private String db_password = "postgres";
    private Connection connection;

    public DB() throws SQLException {
        this.connect();
    }

    public void connect() throws SQLException {
        if(connection != null){
            connection.close();
        }
        connection = DriverManager.getConnection(db_url, db_username, db_password);
    }

    public void disconnect() throws SQLException {
        if(connection != null){
            connection.close();
        }
    }

    public Statement getStatement() throws SQLException {
        return connection == null ? null : connection.createStatement();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connection == null ? null : connection.prepareStatement(sql);
    }
}
