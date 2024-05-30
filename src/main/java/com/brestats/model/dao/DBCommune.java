package com.brestats.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.brestats.model.data.Commune;
import com.brestats.model.data.Departement;

public class DBCommune extends DBObject<Commune> {
    
    public DBCommune() {
        super();
    }

    protected Commune constructor(String[] args) {
        Commune ret = null;
        if(args.length == 4) {
            try {
                DBDepartement dbDep = new DBDepartement();

                int id = Integer.parseInt(args[0]);
                String name = args[1];
                Departement dep = dbDep.getItem(args[2]);
                
                ret = new Commune(id, name, dep, new ArrayList<Commune>());
                fullfillNeighbours(ret);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("Bad argument type");
            }
        } else {
            throw new IllegalArgumentException("Bad amount of arguments");
        }

        return ret;
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
