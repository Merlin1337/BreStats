package com.brestats.model.dao;

import com.brestats.model.data.Aeroport;
import com.brestats.model.data.Departement;

public class DBAeroport extends DBObject<Aeroport> {
    public DBAeroport() {
        super();
    }

    protected Aeroport constructor(String[] args) {
        Aeroport ret = null;

        if(args.length == 3) {
            try {
                DBDepartement dbDep = new DBDepartement();

                String name = args[0];
                String address = args[1];
                Departement dep = dbDep.getItem(args[2]);

                ret = new Aeroport(name, address, dep);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("Bad argument type");
            }
        } else {
            throw new IllegalArgumentException("Bad amount of arguments");
        }

        return ret;
    }
}
