package com.brestats.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DAO  {
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://45.147.98.227:3306/brestats_db";
    private final String username = "brestats_db";
    private final String password = "Xb390cc!2";

    public DAO(){
        try (Connection con = this.getConnection();) {

            System.out.println("ok");

            Statement st = con.createStatement ();
            
            ResultSet rs = st.executeQuery("SELECT * FROM Departement");
            while (rs.next()) {
                int id = rs.getInt("idDep");
                String nom = rs.getString("nomDep");
                System.out.println(id+ " " + nom + " ");
                //users.add(new User(nom , pwd));
            }
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }

    }
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