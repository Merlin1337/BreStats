package com.brestats.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

import com.brestats.model.Model;

public abstract class DBObject<T extends Model>  {
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://45.147.98.227:3306/brestats_db";
    private final String username = "brestats_db";
    private final String password = "Xb390cc!2";
    
    private Connection con;
    protected ArrayList<T> list;

    public DBObject(){
        try {
            this.con = this.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }

        this.list = new ArrayList<T>();
    }

    /**
     * Return an item from the list with its id in DB
     * @param i the id in the DB
     * @return the ith element in the list
     */
    public T getItem(String id) {
        T item = null;
        boolean isInList = false;
        int i = 0;
        while(i < this.list.size() && !isInList) {
            if(this.list.get(i).getId().equals(id)) {
                isInList = true;
                item = this.list.get(i);
            }
            i++;
        }

        if(!isInList) {
            String query = "SELECT * FROM " + item.getClass().toString().toUpperCase() + " WHERE ";
            try {
                this.selectQuery(query);
                item = this.list.get(this.list.size()-1);
            } catch(SQLException e) {
                System.out.println("Unexpected exception with query : " + query);
                e.printStackTrace();
            }
        }

        return item;
    }

    /**
     * Send a select query to the database and return an arrayList of objects representing results. Query must return all necessary data to contruct the corresponding object
     * @param query The sql query
     * @return An array of constructed objects
     * @throws SQLException If a problem occur in the sql query
     */
    public ArrayList<T> selectQuery(String query) throws SQLException {
        PreparedStatement statement = this.con.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        ArrayList<T> resultList = new ArrayList<T>();

        while(result.next()) {
            String[] args = new String[result.getMetaData().getColumnCount()];
            for(int i = 1 ; i <= args.length ; i++) {
                args[i-1] = result.getString(i);
            }

            T object = this.constructor(args);
            resultList.add(object);
            if(!this.list.contains(object)) {
                this.list.add(object);
            }
        }

        return resultList;
    }

    protected abstract T constructor(String[] args) throws IllegalArgumentException;


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