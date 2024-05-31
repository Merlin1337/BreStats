package com.brestats.model.data;

import com.brestats.model.Model;

/**
 * The {@code Gare} class represents a train station with specific attributes such as its code, name, type (freight or passenger), and associated municipality.
 * It implements the {@code Model} interface and provides methods to access and manipulate its data.
 * 
 * <p>This class includes methods to get and set the station's code, name, type (freight or passenger), and associated municipality, as well as to return a string representation of the station.</p>
 * 
 * @see Model
 */
public class Gare implements Model {

    private int codeGare;
    private String nomGare;
    private boolean estFret;
    private boolean estVoyageur;
    private Commune laCom;

    /**
     * Constructs a new {@code Gare} with the specified code, name, associated municipality, and types (freight and passenger).
     *
     * @param code the code of the station
     * @param nom the name of the station
     * @param com the municipality associated with the station
     * @param estFret indicates if the station is for freight
     * @param estVoyageur indicates if the station is for passengers
     * @throws NullPointerException if any of the {@code nom} or {@code com} parameters are null
     */
    public Gare(int code, String nom, Commune com, boolean estFret, boolean estVoyageur) {
        if(nom == null || com == null) {
            throw new NullPointerException("null value");
        }

        this.codeGare = code;
        this.nomGare = nom;
        this.laCom = com;
        this.estFret = estFret;
        this.estVoyageur = estVoyageur;
    }

    /**
     * Returns the code of the station.
     *
     * @return the code of the station
     */
    public int getCodeGare() {
        return this.codeGare;
    }

    /**
     * Returns the code of the station as a String.
     *
     * @return the code of the station as a String
     */
    public String getId() {
        return String.valueOf(this.codeGare);
    }

    /**
     * Sets the code of the station.
     *
     * @param codeGare the new code of the station
     */
    public void setCodeGare(int codeGare) {
        this.codeGare = codeGare;
    }

    /**
     * Returns the name of the station.
     *
     * @return the name of the station
     */
    public String getNomGare() {
        return this.nomGare;
    }

    /**
     * Sets the name of the station.
     *
     * @param nomGare the new name of the station
     */
    public void setNomGare(String nomGare) {
        this.nomGare = nomGare;
    }

    /**
     * Returns whether the station is for freight.
     *
     * @return {@code true} if the station is for freight, {@code false} otherwise
     */
    public boolean getEstFret() {
        return this.estFret;
    }

    /**
     * Sets whether the station is for freight.
     *
     * @param estFret {@code true} if the station is for freight, {@code false} otherwise
     */
    public void setEstFret(boolean estFret) {
        this.estFret = estFret;
    }

    /**
     * Returns whether the station is for passengers.
     *
     * @return {@code true} if the station is for passengers, {@code false} otherwise
     */
    public boolean getEstVoyageur() {
        return this.estVoyageur;
    }

    /**
     * Sets whether the station is for passengers.
     *
     * @param estVoyageur {@code true} if the station is for passengers, {@code false} otherwise
     */
    public void setEstVoyageur(boolean estVoyageur) {
        this.estVoyageur = estVoyageur;
    }

    /**
     * Returns the municipality associated with the station.
     *
     * @return the municipality associated with the station
     */
    public Commune getCom() {
        return this.laCom;
    }

    /**
     * Sets the municipality associated with the station.
     *
     * @param com the new municipality associated with the station
     * @throws NullPointerException if the {@code com} parameter is null
     */
    public void setCom(Commune com) {
        if(com == null) {
            throw new NullPointerException("com is null");
        }

        this.laCom = com;
    }

    /**
     * Returns a string representation of the station, including its code and name.
     *
     * @return a string representation of the station
     */
    public String toString() {
        return this.codeGare + " - " + this.nomGare;
    }
}
