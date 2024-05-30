package com.brestats.model.dao;

import com.brestats.model.data.Departement;

public class DBDepartement extends DBObject<Departement> {
    
    public DBDepartement() {
        super();
    }

    protected Departement constructor(String[] args) {
        Departement ret = null;
        if(args.length == 3) {
            try {
                int id = Integer.parseInt(args[0]);
                String name = args[1];
                double inves = Double.parseDouble(args[2]);

                ret = new Departement(id, name, inves);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("Bad argument type");
            }
        } else {
            throw new IllegalArgumentException("Bad amount of arguments");
        }
        return ret;
    }

    
}
