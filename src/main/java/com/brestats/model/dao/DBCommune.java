package com.brestats.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.Model;
import com.brestats.model.data.Commune;
import com.brestats.model.data.Departement;

public class DBCommune extends DBObject<Commune> {
    
    public DBCommune() {
        super();
    }

    protected Commune constructor(String[] args, DBObject<? extends Model>[] db) throws IncorectConstructorArguments {
        if(db.getClass().toString().equals("DBDepartement")) {
            throw new IncorectConstructorArguments("db[0] must be of DBDepartement type");
        }
        
        Commune ret = null;
        DBDepartement dbDep = (DBDepartement) db[0];
        
        if(args.length == 4) {
            try {
                int id = Integer.parseInt(args[0]);
                String name = args[1];
                Departement dep = dbDep.getItem(args[2]);
                
                ret = new Commune(id, name, dep, new ArrayList<Commune>());
                fullfillNeighbours(ret, (DBDepartement[]) db);
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }

    private void fullfillNeighbours(Commune com, DBDepartement[] dbDep) {
        try {
            ArrayList<Commune> neighbourList = this.selectQuery("SELECT c2.* FROM commune c1 JOIN voisinage ON idCommune = commune JOIN commune ON communeVoisine = idCommune WHERE c1.idCommune = " + com.getId(), dbDep);

            for (Commune voisin : neighbourList) {
                com.ajouterVoisin(voisin);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
    }
 
}
