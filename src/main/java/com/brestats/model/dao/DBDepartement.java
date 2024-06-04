package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Departement;

/**
 * Implement the connection to the database for the table "departement"
 * @see com.brestats.model.dao.DBObject
 * @see com.brestats.model.data.Departement
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DBDepartement extends DBObject<Departement> {
    /**
     * Initiate the connection to the database for the table "departement"
     */
    public DBDepartement() {
        super();
    }

    /**
     * Return a constructed {@link com.brestats.model.data.Departement Departement} object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object.
     * <ul>
     *  <li> <span style="color: blue">int</span> id </li>
     *  <li> {@link String <span style="color: blue">String</span>} nom </li>
     *  <li> <span style="color: blue">double</span> inves </li>
     * </ul>
     * @return The initialised {@link com.brestats.model.data.Departement Departement} object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     * @see com.brestats.model.data.Departement#Departement(int, String, double)
     */
    protected Departement constructor(String[] args) throws IncorectConstructorArguments {
        Departement ret = null;
        if(args.length == 3) {
            try {
                int id = Integer.parseInt(args[0]);
                String name = args[1];
                double inves = Double.parseDouble(args[2]);

                ret = new Departement(id, name, inves);
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
    public void insertQuery(Departement obj) {
        String query;
        
        if(this.getItem(obj.getId()) == null) {
            query = "INSERT INTO annee VALUES (" + obj.getIdDep() + "," + obj.getNomDep() + "," + obj.getInvesCulturel2019() + ");";
        } else {
            query = "UPDATE annee SET nomDep = " + obj.getNomDep() + ", investissementCulturel2019 = " + obj.getInvesCulturel2019() + " WHERE idDep = " + obj.getIdDep() + ";";
        }

        this.executeQuery(query);
    }

    /**
     * Return the query to select an item from its id in the table
     * @return The select query
     */
    protected String getWhereClause(String id) {
        return "WHERE idDep = " + id;
    }
}
