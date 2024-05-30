package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.Model;
import com.brestats.model.data.Aeroport;
import com.brestats.model.data.Departement;

public class DBAeroport extends DBObject<Aeroport> {
    public DBAeroport() {
        super();
    }

    protected Aeroport constructor(String[] args, DBObject<? extends Model>[] db) throws IncorectConstructorArguments {
        if(db.getClass().toString().equals("DBDepartement")) {
            throw new IncorectConstructorArguments("db[0] must be of DBDepartement type");
        }
        
        Aeroport ret = null;
        DBDepartement dbDep = (DBDepartement) db[0];

        if(args.length == 3) {
            try {
                String name = args[0];
                String address = args[1];
                Departement dep = dbDep.getItem(args[2]);

                ret = new Aeroport(name, address, dep);
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }
}
