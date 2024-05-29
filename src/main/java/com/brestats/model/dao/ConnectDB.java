package com.brestats.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;

import com.brestats.model.Model;

public abstract class ConnectDB<T extends Model>  {
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://45.147.98.227:3306/brestats_db";
    private final String username = "brestats_db";
    private final String password = "Xb390cc!2";

    private Connection con;
    protected ArrayList<T> list;

    public ConnectDB(){
        try {
            this.con = this.getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM departement");
            ResultSet set = statement.executeQuery();

            while(set.next()) {
                System.out.println(set.getString("nomDep"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace ();
        }

        this.list = new ArrayList<T>();

    }

    protected void selectQuery(String query) throws SQLException {
        Statement statement = this.con.prepareStatement(query);
        ResultSet result = statement.executeQuery(query);

        while(result.next()) {
            System.out.println(result.getFetchSize());
            // this.constructor(result.getString(0));
        }

    }

    protected abstract T constructor(String... args) throws IllegalArgumentException;


    private Connection getConnection () throws SQLException {
    // Charger la classe du pilote
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace ();
            return null;
        }
    // Obtenir la connection
        return DriverManager.getConnection(this.url , this.username , this.password);
    }
    
}