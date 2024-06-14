package com.brestats.model.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.Model;


/**
 * This class uses the MySQL connector package to communicate with the database. <br>
 * It is the root of all subclasses, which handle each a table of the database (for example {@link com.brestats.model.dao.DBDepartement DBDepartement} can query the departement table). <br>
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public abstract class DBObject<T extends Model>  {
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://45.147.98.227:3306/brestats_db";
    private final String username = "brestats_db";
    private final String password = "Xb390cc!2";
    
    private Connection con;
    /** Contains the constructed objects from table's data */
    protected ArrayList<T> list;

    /**
     * Initiate the connection with the database
     */
    public DBObject(){
        try {
            this.con = this.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }

        this.list = new ArrayList<T>();
    }

    /**
     * Return an item from the list with its id in DB. If it already has been fetch from the database, the method look in the array, else query the database.
     * @param id the id in the DB
     * @return the ith element in the list
     * @see Model#getId()
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
            String query = null;
            try {
                query = "SELECT * FROM " + this.getTable() + " " + this.getWhereClause(id) + ";";
                ArrayList<T> res = this.selectQuery(query);
                if(res.size() > 0) {
                    item = res.get(res.size()-1);
                } else {
                    throw new NullPointerException("No results with query : " + query);
                }
            } catch(SQLException e) {
                System.out.println("Unexpected exception with query : " + query);
                e.printStackTrace();
            } catch(IllegalCallerException e) {
                System.out.println("Unexpected exception with the method getSelectItemQuery(String)");
                e.printStackTrace();
            }
        }

        return item;
    }

    /**
     * Send a select query to the database and return an arrayList of objects representing results. Query must return all necessary data to contruct the corresponding object. See the implementation of the {@link #constructor(String[])} in each subclass.
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
                // System.out.println(args[i-1]);
            }

            try {
                T object = this.constructor(args);
                resultList.add(object);
                if(!this.list.contains(object)) {
                    this.list.add(object);
                }
            } catch(IncorectConstructorArguments e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    /**
     * Send the prepared query in the {@link #insertQuery(Model)} method
     * @param query the insert, update, or delete query
     */
    protected void executeQuery(String query) {
        try {
            Statement statement = this.con.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException e) {
            System.out.println("Unexpected exception");
            e.printStackTrace();
        }
    }

    /**
     * Send a delete query to the database from the {@code obj} argument's data.
     * @param obj the obj used to send the query
     */
    public void deleteQuery(T obj) {
        String query = "DELETE FROM " + this.getTable() + " " + this.getWhereClause(obj.getId()) + ";";
        try {
            Statement statement = this.con.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Unexpected exception with query : " + query);
            e.printStackTrace();
        } 
    }

    /**
     * Insert or update an element in the database from the obj param
     * @param obj The object which will be converted and inserted in the database
     */
    public abstract void insertQuery(T obj);

    /**
     * Return the name of the table
     * @return the table as String
     */
    public abstract String getTable();

    /**
     * Return a constructed object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object
     * @return The initialised object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     */
    protected abstract T constructor(String[] args) throws IncorectConstructorArguments;

    /**
     * Return the query to select an item from its id in the table
     * @param id the unique id of the wanted item
     * @return The select query
     */
    protected abstract String getWhereClause(String id);

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