package com.brestats.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.brestats.exceptions.IncorectConstructorArguments;
import com.brestats.model.data.Annee;
import com.brestats.model.data.Commune;
import com.brestats.model.data.DonneesAnnuelles;

/**
 * Implement the connection to the database for the table "donneesannuelles"
 * @see com.brestats.model.dao.DBObject
 * @see com.brestats.model.data.DonneesAnnuelles
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class DBValeursCommuneAnnee extends DBObject<DonneesAnnuelles> {
    private DBCommune dbCom;
    private DBAnnee dbAnnee;

     /**
     * Initiate the connection to the database for the table "donnnesannuelles". This class require the connection to the tables "commune" and "annee", while it has references to these objects.
     * @param dbCom An instance of the connection to the table "commune"
     * @param dbAnnee An instance of the connection to the table "annee"
     */
    public DBValeursCommuneAnnee(DBCommune dbCom, DBAnnee dbAnnee) {
        super();
        this.dbCom = dbCom;
        this.dbAnnee = dbAnnee;
    }

    /**
     * Return a constructed {@link com.brestats.model.data.DonneesAnnuelles DonneesAnnuelles} object from the query's result through a {@link String String[]} argument
     * @param args The {@link String String[]} argument which contains all the required data to initiate the object.
     * <ul>
     *  <li> {@link com.brestats.model.data.Commune <span style="color: blue">Commune</span>} com </li>
     *  <li> {@link com.brestats.model.data.Annee <span style="color: blue">Commune</span>} annee </li>
     *  <li> <span style="color: blue">int</span> nbMaisons </li>
     *  <li> <span style="color: blue">int</span> nbAppart </li>
     *  <li> <span style="color: blue">double</span> prixMoyen </li>
     *  <li> <span style="color: blue">double</span> prixM2Moyen </li>
     *  <li> <span style="color: blue">double</span> surfaceMoy </li>
     *  <li> <span style="color: blue">double</span> depCulturellesTotales </li>
     *  <li> <span style="color: blue">double</span> budgetTotal </li>
     *  <li> <span style="color: blue">double</span> pop </li>
     * </ul>
     * @return The initialised {@link com.brestats.model.data.DonneesAnnuelles DonneesAnnuelles} object
     * @throws IncorectConstructorArguments if args does not contains the right arguments
     * @see com.brestats.model.data.DonneesAnnuelles#DonneesAnnuelles(Commune, Annee, int, int, double, double, double, double, double, double)
     */
    protected DonneesAnnuelles constructor(String[] args) throws IncorectConstructorArguments {
        DonneesAnnuelles ret = null;

        if(args.length == 10) {
            try {
                Annee annee = this.dbAnnee.getItem(args[0]);
                Commune com = this.dbCom.getItem(args[1]);
                int nbMaison = Integer.parseInt(args[2]);
                int nbAppart = Integer.parseInt(args[3]);
                double prixMoyen = Double.parseDouble(args[4]);
                double prixM2Moyen = Double.parseDouble(args[5]);
                double surfaceMoy = Double.parseDouble(args[6]);
                double depCulturellesTotales = Double.parseDouble(args[7]);
                double budgetTotal = Double.parseDouble(args[8]);
                double pop = Double.parseDouble(args[9]);

                ret = new DonneesAnnuelles(com, annee, nbMaison, nbAppart, prixMoyen, prixM2Moyen, surfaceMoy, depCulturellesTotales, budgetTotal, pop);
                
            } catch(NumberFormatException e) {
                throw new IncorectConstructorArguments("Bad argument type");
            }
        } else {
            throw new IncorectConstructorArguments("Bad amount of arguments");
        }

        return ret;
    }

    /**
     * Insert or update an element in the database from the obj param
     * @param obj The object which will be converted and inserted in the database
     */
    public void insertQuery(DonneesAnnuelles obj) {
        String query;
        
        if(this.getItem(obj.getId()) == null) {
            query = "INSERT INTO annee VALUES (" + obj.getLAnnee().getAnnee() + "," + obj.getLaCom().getIdCommune() + "," + obj.getNbMaison() + "," + obj.getNbAppart() + "," + obj.getPrixMoyen() + "," + obj.getPrixM2Moyen() + "," + obj.getSurfaceMoyenne() + "," + obj.getDepCulturelTotales() + "," + obj.getBudgetTotal() + "," + obj.getPopulation() + ");";
        } else {
            query = "UPDATE annee SET nbMaison = " + obj.getNbMaison() + ", nbAppart = " + obj.getNbAppart() + ", prixMoyen = " + obj.getPrixMoyen() + ", prixM2Moyen = " + obj.getPrixM2Moyen() + ", surfaceMoy = " + obj.getSurfaceMoyenne() + ", depensesCulturellesTotales = " + obj.getDepCulturelTotales() + ", budgetTotal = " + obj.getBudgetTotal() + ", population = " + obj.getPopulation() + " WHERE lAnnee = " + obj.getLAnnee().getAnnee() + " AND laCommune = " + obj.getLaCom().getIdCommune() + ";";
        }

        this.executeQuery(query);
    }

    /**
     * Return the table name (= donneesannuelles)
     * @return "donneesannuelles" 
     */
    public String getTable() {
        return "donneesannuelles";
    }

    /**
     * Caculate and return a {@code DonneesAnnuelles} object with average of all available years for given city. This object has an {@link Annee} object with 0 in its both fields.
     * @param city the wanted city to get its data
     * @return A {@code DonneesAnnuelles} object with average of data
     */
    public DonneesAnnuelles getAverageItemByCity(Commune city) {
        DonneesAnnuelles data = new DonneesAnnuelles(city, new Annee(0,0));
        try {
            ArrayList<DonneesAnnuelles> res = this.selectQuery("SELECT * FROM donneesannuelles WHERE laCommune = " + city.getId() + ";");

            int nbMaison = 0;
            int nbAppart = 0;
            double prixMoyen = 0;
            double prixM2Moyen = 0;
            double surfaceMoyenne = 0;
            double depCulturelTotales = 0;
            double budgetTotal = 0;
            double population = 0;

            for(DonneesAnnuelles item : res) {
                nbMaison += item.getNbMaison();
                nbAppart += item.getNbAppart();
                prixMoyen += item.getPrixMoyen();
                prixM2Moyen += item.getPrixM2Moyen();
                surfaceMoyenne += item.getSurfaceMoyenne();
                depCulturelTotales += item.getDepCulturelTotales();
                budgetTotal += item.getBudgetTotal();
                population += item.getPopulation();
            }

            data.setNbMaison(nbMaison/res.size());
            data.setNbAppart(nbAppart/res.size());
            data.setPrixMoyen(prixMoyen/res.size());
            data.setPrixM2Moyen(prixM2Moyen/res.size());
            data.setSurfaceMoyenne(surfaceMoyenne/res.size());
            data.setDepCulturelTotales(depCulturelTotales/res.size());
            data.setBudgetTotal(budgetTotal/res.size());
            data.setPopulation(population/res.size());
        } catch(SQLException e) {
            System.out.println("Unexpected exception with query : SELECT * FROM donneesannuelles WHERE laCommune = " + city.getId() + "; \nReturn null.");
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Return the query to select an item from its id in the table
     * @return The select query
     */
    protected String getWhereClause(String id) {
        String[] param = id.split("-");
        return "WHERE laCommune = " + param[0] + " and lAnnee = " + param[1];
    }
}
