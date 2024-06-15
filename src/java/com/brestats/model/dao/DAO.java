package com.brestats.model.dao;

/**
 * Static class containing an instance of all {@link DBObject} classes
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DAO {
    /** Instance of connection to departement table */
    public static final DBDepartement DB_DEP = new DBDepartement();
    /** Instance of connection to annee table */
    public static final DBAnnee DB_ANNEE = new DBAnnee();
    /** Instance of connection to commune table */
    public static final DBCommune DB_COM = new DBCommune(DB_DEP);
    /** Instance of connection to gare table */
    public static final DBGare DB_GARE = new DBGare(DB_COM);
    /** Instance of connection to aeroport table */
    public static final DBAeroport DB_AEROPORT = new DBAeroport(DB_DEP);
    /** Instance of connection to donneesannuelles table */
    public static final DBValeursCommuneAnnee DB_VAL = new DBValeursCommuneAnnee(DB_COM, DB_ANNEE);
}
