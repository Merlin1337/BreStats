package com.brestats.model.data;

import com.brestats.model.Model;

/**
 * The {@code Annee} class represents a year with specific attributes such as the year itself and the inflation rate.
 * It implements the {@code Model} interface and provides methods to access and manipulate its data.
 * 
 * <p>This class includes methods to get and set the year and inflation rate, determine if the year is a leap year, and return a string representation of the year.</p>
 * 
 * @see Model
 */
public class Annee implements Model {

    private int annee;
    private double tauxInflation;

    /**
     * Constructs a new {@code Annee} with the specified year and inflation rate.
     *
     * @param annee the year
     * @param taux the inflation rate for the year
     */
    public Annee(int annee, double taux) {
        this.annee = annee;
        this.tauxInflation = taux;
    }

    /**
     * Returns the year.
     *
     * @return the year
     */
    public int getAnnee() {
        return this.annee;
    }

    /**
     * Returns the year as a String, which serves as the ID of the {@code Annee}.
     *
     * @return the year as a String
     */
    public String getId() {
        return String.valueOf(this.annee);
    }

    /**
     * Sets the year.
     *
     * @param annee the new year
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * Returns the inflation rate for the year.
     *
     * @return the inflation rate for the year
     */
    public double getTauxInflation() {
        return this.tauxInflation;
    }

    /**
     * Sets the inflation rate for the year.
     *
     * @param tauxInflation the new inflation rate for the year
     */
    public void setTauxInflation(double tauxInflation) {
        this.tauxInflation = tauxInflation;
    }

    /**
     * Determines if the year is a leap year.
     *
     * @return {@code true} if the year is a leap year, {@code false} otherwise
     */
    public boolean estBissextile() {
        return this.annee % 4 == 0;
    }

    /**
     * Returns a string representation of the year and the inflation rate.
     *
     * @return a string representation of the year and the inflation rate
     */
    
    public String toString() {
        return this.annee + " - " + this.tauxInflation + "%";
    }
}
