package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Annee;

/**
 * Implement the connection to the database for the table "annee"
 * @see com.brestats.model.dao.DBObject
 * @see com.brestats.model.data.Annee
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DBAnnee extends DBObject<Annee> {
    /**
     * Initiate the connection to the database for the table "annee"
     */
    public DBAnnee() {
        super();
    }

    /**
     * Return a constructed {@link com.brestats.model.data.Annee Annee} object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object.
     * <ul>
     *  <li> <span style="color: blue">int</span> annee </li>
     *  <li> <span style="color: blue">double</span> tauxInflation </li>
     * </ul>
     * @return The initialised {@link com.brestats.model.data.Annee Annee} object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     * @see com.brestats.model.data.Annee#Annee(int, double)
     */
    protected Annee constructor(String[] args) throws IncorectConstructorArguments {
        Annee ret = null;

        if(args.length == 2) {
            try {
                int annee = Integer.parseInt(args[0]);
                double taux = Double.parseDouble(args[1]);

                ret = new Annee(annee, taux);
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }

    /**
     * Insert or update an element in the database from the obj param
     * @param obj The object which will be converted and inserted in the database
     */
    public void insertQuery(Annee obj) {
        String query;
        
        if(this.getItem(obj.getId()) == null) {
            query = "INSERT INTO annee(annee, tauxInflation) VALUES (" + obj.getAnnee() + "," + obj.getTauxInflation() + ");";
        } else {
            query = "UPDATE annee SET tauxInflation = " + obj.getTauxInflation() + " WHERE annee = " + obj.getAnnee() + ";";
        }

        this.executeQuery(query);
    }

    /**
     * Return the table name (= annee)
     * @return "annee" 
     */
    public String getTable() {
        return "annee";
    }

    /**
     * Return the query to select an item from its id in the table
     * @return The select query
     */
    protected String getWhereClause(String id) {
        return "WHERE annee = " + id;
    }
}
