package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Commune;
import com.brestats.model.data.Gare;

/**
 * Implement the connection to the database for the table "gare"
 * @see com.brestats.model.dao.DBObject
 * @see com.brestats.model.data.Gare
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DBGare extends DBObject<Gare> {
    private DBCommune dbCom;
    
    /**
     * Initiate the connection to the database for the table "donnnesannuelles". This class require the connection to the table "commune", while it has references to this object.
     * @param dbCom An instance of the connection to the table "commune"
     */
    public DBGare(DBCommune dbCom) {
        super();
        this.dbCom = dbCom;
    }

    /**
     * Return a constructed {@link com.brestats.model.data.Gare Gare} object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object.
     * <ul>
     *  <li> <span style="color: blue">int</span> code </li>
     *  <li> {@link String <span style="color: blue">String</span>} nom </li>
     *  <li> {@link com.brestats.model.data.Commune <span style="color: blue">Commune</span>} com </li>
     *  <li> <span style="color: blue">boolean</span> estFret </li>
     *  <li> <span style="color: blue">boolean</span> estVoyageur </li>
     * </ul>
     * @return The initialised {@link com.brestats.model.data.Gare Gare} object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     * @see com.brestats.model.data.Gare#Gare(int, String, Commune, boolean, boolean)
     */
    protected Gare constructor(String[] args) throws IncorectConstructorArguments {
        
        Gare ret = null;

        if(args.length == 5) {
            try {
                int code = Integer.parseInt(args[0]);
                String name = args[1];
                boolean isTraveller = Boolean.parseBoolean(args[2]);
                boolean isFreight = Boolean.parseBoolean(args[3]);
                Commune com = this.dbCom.getItem(args[4]);

                ret = new Gare(code, name, com, isFreight, isTraveller);
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
    public void insertQuery(Gare obj) {
        String query;
        
        if(this.getItem(obj.getId()) == null) {
            query = "INSERT INTO annee VALUES (" + obj.getCodeGare() + "," + obj.getNomGare() + "," + obj.getEstFret() + "," + obj.getEstVoyageur() + "," + obj.getCom().getIdCommune() +";";
        } else {
            query = "UPDATE annee SET nomgare = " + obj.getNomGare() + ", estFret = " + obj.getEstFret() + ", estVoyageur = " + obj.getEstVoyageur() + ", laCommune = " + obj.getCom().getIdCommune() + "WHERE code = " + obj.getCodeGare() + ";";
        }

        this.executeQuery(query);
    }

    /**
     * Return the table name (= gare)
     * @return "gare" 
     */
    public String getTable() {
        return "gare";
    }
 
    /**
     * Return the query to select an item from its id in the table
     * @return The select query
     */
    protected String getWhereClause(String id) {
        return "WHERE code = " + id;
    }
}
