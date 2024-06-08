package com.brestats.model.dao;

public class DAO {
    public static final DBDepartement DB_DEP = new DBDepartement();
    public static final DBAnnee DB_ANNEE = new DBAnnee();
    public static final DBCommune DB_COM = new DBCommune(DB_DEP);
    public static final DBGare DB_GARE = new DBGare(DB_COM);
    public static final DBAeroport DB_AEROPORT = new DBAeroport(DB_DEP);
    public static final DBValeursCommuneAnnee DB_VAL = new DBValeursCommuneAnnee(DB_COM, DB_ANNEE);
}
