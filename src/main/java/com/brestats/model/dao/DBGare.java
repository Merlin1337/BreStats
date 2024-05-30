package com.brestats.model.dao;

import com.brestats.model.data.Commune;
import com.brestats.model.data.Gare;

public class DBGare extends DBObject<Gare> {
    
    public DBGare() {
        super();
    }

    protected Gare constructor(String[] args) {
        Gare ret = null;
        if(args.length == 5) {
            try {
                DBCommune dbCom = new DBCommune();

                int code = Integer.parseInt(args[0]);
                String name = args[1];
                boolean isTraveller = Boolean.parseBoolean(args[2]);
                boolean isFreight = Boolean.parseBoolean(args[3]);
                Commune com = dbCom.getItem(args[4]);

                ret = new Gare(code, name, com, isFreight, isTraveller);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("Bad argument type");
            }
        } else {
            throw new IllegalArgumentException("Bad amount of arguments");
        }

        return ret;
    }
 
}
