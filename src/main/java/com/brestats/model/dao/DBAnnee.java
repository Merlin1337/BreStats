package com.brestats.model.dao;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Annee;

public class DBAnnee extends DBRawObject<Annee> {
    public DBAnnee() {
        super();
    }

    protected Annee constructor(String[] args) throws IncorectConstructorArguments {
        Annee ret = null;

        if(args.length == 2) {
            try {
                int annee = Integer.parseInt(args[0]);
                double taux = Double.parseDouble(args[1]);

                ret = new Annee(annee, taux);
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }
}
