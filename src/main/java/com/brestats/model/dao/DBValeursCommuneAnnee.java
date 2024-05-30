package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.Model;
import com.brestats.model.data.Commune;
import com.brestats.model.data.ValeursCommuneAnnee;

public class DBValeursCommuneAnnee extends DBObject<ValeursCommuneAnnee> {
    public DBValeursCommuneAnnee() {
        super();
    }

    protected ValeursCommuneAnnee constructor(String[] args, DBObject<? extends Model>[] db) throws IncorectConstructorArguments {
        if(db.length != 2 || db[0].getClass().toString().equals("DBCommune") || db[1].getClass().toString().equals("DBAnnee")) {
            throw new IncorectConstructorArguments("db[0] must be of DBCommune and db[1] must be of type DBAnnee");
        }

        DBCommune dbCommune = (DBCommune) db[0];
        DBAnnee dbAnnee = (DBAnnee) db[1];
        ValeursCommuneAnnee ret = null;

        if(args.length == 10) {
            try {
                Commune com = dbCommune.getItem(args[0], db);

                
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }
}
