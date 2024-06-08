package com.brestats.model.data;

import com.brestats.model.Model;

/**
 * The {@code Aeroport} class represents an airport with specific attributes such as its name, address, and associated department.
 * It implements the {@code Model} interface and provides methods to access and manipulate its data.
 * 
 * <p>This class includes methods to get and set the airport's name, address, and department, as well as to return a string representation of the airport.</p>
 * 
 * @see Model
 */
public class Aeroport implements Model {

    private Departement LeDep;
    private String nom;
    private String adresse;

    /**
     * Constructs a new {@code Aeroport} with the specified name, address, and department.
     *
     * @param nom the name of the airport
     * @param adresse the address of the airport
     * @param dep the department associated with the airport
     * @throws NullPointerException if any of the {@code nom}, {@code adresse}, or {@code dep} parameters are null
     */
    public Aeroport(String nom, String adresse, Departement dep) {
        if(nom == null || adresse == null || dep == null) {
            throw new NullPointerException("A value is null");
        }

        this.nom = nom;
        this.adresse = adresse;
        this.LeDep = dep;
    }

    /**
     * Returns the ID of the airport, which is its name.
     *
     * @return the ID of the airport
     */
    public String getId() {
        return this.nom;
    }

    /**
     * Returns the name of the airport.
     *
     * @return the name of the airport
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Sets the name of the airport.
     *
     * @param nom the new name of the airport
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Returns the address of the airport.
     *
     * @return the address of the airport
     */
    public String getAdresse() {
        return this.adresse;
    }

    /**
     * Sets the address of the airport.
     *
     * @param adresse the new address of the airport
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Returns the department associated with the airport.
     *
     * @return the department associated with the airport
     */
    public Departement getDep() {
        return this.LeDep;
    }

    /**
     * Sets the department associated with the airport.
     *
     * @param dep the new department associated with the airport
     * @throws NullPointerException if the {@code dep} parameter is null
     */
    public void setDep(Departement dep) {
        if(dep == null) {
            throw new NullPointerException("dep is null");
        }

        this.LeDep = dep;
    }

    /**
     * Returns a string representation of the airport, including its name, address, and associated department ID.
     *
     * @return a string representation of the airport
     */
    public String toString() {
        return "Aeroport " + this.nom + " ; " + this.adresse + " - " + this.LeDep.getIdDep();
    }
}
