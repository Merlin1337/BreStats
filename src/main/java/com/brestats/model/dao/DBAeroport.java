package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Aeroport;
import com.brestats.model.data.Departement;

/**
 * Implement the connection to the database for the table "aeroport"
 * @see com.brestats.model.dao.DBObject
 * @see com.brestats.model.data.Aeroport
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DBAeroport extends DBObject<Aeroport> {
    private DBDepartement dbDep;

    /**
     * Initiate the connection to the database for the table "aeroport". This class require the connection to the table "departement", while it has references to this object.
     * @param dbDep An instance of the connection to the table "departement"
     */
    public DBAeroport(DBDepartement dbDep) {
        super();
        this.dbDep = dbDep;
    }

    /**
     * Return a constructed {@link com.brestats.model.data.Aeroport Aeroport} object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object.
     * <ul>
     *  <li> {@link String <span style="color: blue">String</span>} nom </li>
     *  <li> {@link String <span style="color: blue">String</span>} adresse </li>
     *  <li> {@link com.brestats.model.data.Departement <span style="color: blue">Departement</span>} dep </li>
     * </ul>
     * @return The initialised {@link com.brestats.model.data.Aeroport Aeroport} object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     * @see com.brestats.model.data.Aeroport#Aeroport(String, String, Departement)
     */
    protected Aeroport constructor(String[] args) throws IncorectConstructorArguments {       
        Aeroport ret = null;

        if(args.length == 3) {
            try {
                String name = args[0];
                String address = args[1];
                Departement dep = this.dbDep.getItem(args[2]);

                ret = new Aeroport(name, address, dep);
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
    public void insertQuery(Aeroport obj) {
        String query;
        
        if(this.getItem(obj.getId()) == null) {
            query = "INSERT INTO annee VALUES (" + obj.getNom() + "," + obj.getAdresse() + "," + obj.getDep().getIdDep() +";";
        } else {
            query = "UPDATE annee SET adresse = " + obj.getAdresse() + ", leDepartement = " + obj.getDep().getIdDep() + "WHERE nom = " + obj.getNom() + ";";
        }

        this.executeQuery(query);
    }

    /**
     * Return the query to select an item from its id in the table
     * @return The select query
     */
    @Override
    protected String getWhereClause(String id) {
        return "WHERE nom = " + id;
    }
}
