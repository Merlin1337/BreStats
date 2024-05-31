package com.brestats.model.data;

import com.brestats.model.Model;

/**
 * The {@code Departement} class represents a department with specific attributes such as its ID, name, and cultural investment for the year 2019.
 * It implements the {@code Model} interface and provides methods to access and manipulate its data.
 * 
 * <p>This class includes methods to get and set the department's ID, name, and cultural investment, as well as to return a string representation of the department.</p>
 * 
 * @see Model
 */
public class Departement implements Model {

    private int idDep;
    private String nomDep;
    private double invesCulturel2019;

    /**
     * Constructs a new {@code Departement} with the specified ID, name, and cultural investment.
     *
     * @param id the ID of the department
     * @param nom the name of the department
     * @param inves the cultural investment of the department for the year 2019
     * @throws NullPointerException if the {@code nom} parameter is null
     */
    public Departement(int id, String nom, double inves) {
        if(nom == null) {
            throw new NullPointerException("null value");
        }

        this.idDep = id;
        this.nomDep = nom;
        this.invesCulturel2019 = inves;
    }

    /**
     * Returns the name of the department.
     *
     * @return the name of the department
     */
    public String getNomDep() {
        return this.nomDep;
    }

    /**
     * Sets the name of the department.
     *
     * @param nomDep the new name of the department
     */
    public void setNomDep(String nomDep) {
        this.nomDep = nomDep;
    }

    /**
     * Returns the ID of the department.
     *
     * @return the ID of the department
     */
    public int getIdDep() {
        return this.idDep;
    }

    /**
     * Returns the ID of the department as a String.
     *
     * @return the ID of the department as a String
     */
    public String getId() {
        return String.valueOf(this.idDep);
    }

    /**
     * Sets the ID of the department.
     *
     * @param idDep the new ID of the department
     */
    public void setIdDep(int idDep) {
        this.idDep = idDep;
    }

    /**
     * Returns the cultural investment of the department for the year 2019.
     *
     * @return the cultural investment of the department for the year 2019
     */
    public double getInvesCulturel2019() {
        return this.invesCulturel2019;
    }

    /**
     * Sets the cultural investment of the department for the year 2019.
     *
     * @param invesCulturel2019 the new cultural investment of the department for the year 2019
     */
    public void setInvesCulturel2019(double invesCulturel2019) {
        this.invesCulturel2019 = invesCulturel2019;
    }

    /**
     * Returns a string representation of the department.
     *
     * @return a string representation of the department
     */
    public String toString() {
        return this.idDep + " - " + this.nomDep;
    }
}
