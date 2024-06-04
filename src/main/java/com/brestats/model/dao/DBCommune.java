package com.brestats.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Commune;
import com.brestats.model.data.Departement;

/**
 * Implement the connection to the database for the table "commune"
 * @see com.brestats.model.dao.DBObject
 * @see com.brestats.model.data.Commune
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DBCommune extends DBObject<Commune> {
    private DBDepartement dbDep;
    
    /**
     * Initiate the connection to the database for the table "commune". This class require the connection to the table "departement", while it has references to this object.
     * @param dbDep An instance of the connection to the table "departement"
     */
    public DBCommune(DBDepartement dbDep) {
        super();
        this.dbDep = dbDep;
    }

    /**
     * Return a constructed {@link com.brestats.model.data.Commune Commune} object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object.
     * <ul>
     *  <li> <span style="color: blue">int</span> id </li>
     *  <li> {@link String <span style="color: blue">String</span>} nom </li>
     *  <li> {@link com.brestats.model.data.Departement <span style="color: blue">Departement</span>} dep </li>
     *  <li> <span style="color: blue">{@link java.util.ArrayList ArrayList}&lt;{@link com.brestats.model.data.Commune Commune}&gt;</span> voisins </li>
     * </ul>
     * @return The initialised {@link com.brestats.model.data.Commune Commune} object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     * @see com.brestats.model.data.Commune#Commune(int, String, Departement, ArrayList)
     */
    protected Commune constructor(String[] args) throws IncorectConstructorArguments {
        
        Commune ret = null;
        
        if(args.length == 4) {
            try {
                int id = Integer.parseInt(args[0]);
                String name = args[1];
                Departement dep = dbDep.getItem(args[2]);
                
                ret = new Commune(id, name, dep, new ArrayList<Commune>());
                fullfillNeighbours(ret);
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
    public void insertQuery(Commune obj) {
        String query;
        
        if(this.getItem(obj.getId()) == null) {
            query = "INSERT INTO annee VALUES (" + obj.getIdCommune() + "," + obj.getNomCommune() + "," + obj.getDep().getIdDep() +";";
        } else {
            query = "UPDATE annee SET nomCommune = " + obj.getNomCommune() + ", leDepartement = " + obj.getDep().getIdDep() + "WHERE idCommune = " + obj.getIdCommune() + ";";
        }

        this.executeQuery(query);
    }

    /**
     * Return the query to select an item from its id in the table
     * @return The select query
     */
    protected String getWhereClause(String id) {
        return "WHERE idCommune = " + id;
    }



    private void fullfillNeighbours(Commune com) {
        try {
            ArrayList<Commune> neighbourList = this.selectQuery("SELECT c2.* FROM commune c1 JOIN voisinage ON idCommune = commune JOIN commune ON communeVoisine = idCommune WHERE c1.idCommune = " + com.getId());

            for (Commune voisin : neighbourList) {
                com.ajouterVoisin(voisin);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
    }    
}
