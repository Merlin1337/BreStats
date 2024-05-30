package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Departement;

public class DBDepartement extends DBRawObject<Departement> {
    
    public DBDepartement() {
        super();
    }

    protected Departement constructor(String[] args) throws IncorectConstructorArguments {
        Departement ret = null;
        if(args.length == 3) {
            try {
                int id = Integer.parseInt(args[0]);
                String name = args[1];
                double inves = Double.parseDouble(args[2]);

                ret = new Departement(id, name, inves);
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }
        return ret;
    }

    
}
