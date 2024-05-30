package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.Model;
import com.brestats.model.data.Commune;
import com.brestats.model.data.Gare;

public class DBGare extends DBObject<Gare> {
    
    public DBGare() {
        super();
    }

    protected Gare constructor(String[] args, DBObject<? extends Model>[] db) throws IncorectConstructorArguments {
        if(db.getClass().toString().equals("DBCommune[]") || db.length != 1) {
            throw new IncorectConstructorArguments("db[0] must be of DBCommune type");
        }
        
        Gare ret = null;
        DBCommune dbCom = (DBCommune) db[0];

        if(args.length == 5) {
            try {
                int code = Integer.parseInt(args[0]);
                String name = args[1];
                boolean isTraveller = Boolean.parseBoolean(args[2]);
                boolean isFreight = Boolean.parseBoolean(args[3]);
                Commune com = dbCom.getItem(args[4], db);

                ret = new Gare(code, name, com, isFreight, isTraveller);
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }
 
}
