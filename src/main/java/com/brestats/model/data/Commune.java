package com.brestats.model.data;

import java.util.ArrayList;
import java.util.Iterator;

import com.brestats.model.Model;

/**
 * The {@code Commune} class represents a municipality with specific attributes such as its ID, name, associated department, and neighboring municipalities.
 * It implements the {@code Model} interface and provides methods to access and manipulate its data.
 * 
 * <p>This class includes methods to get and set the municipality's ID, name, and department, manage its neighboring municipalities, and determine if it borders another department.</p>
 * 
 * @see Model
 */
public class Commune implements Model {

    private ArrayList<Commune> voisins;
    private int idCommune;
    private String nomCommune;
    private Departement LeDep;

    /**
     * Constructs a new {@code Commune} with the specified ID, name, department, and neighboring municipalities.
     *
     * @param id the ID of the municipality
     * @param nom the name of the municipality
     * @param dep the department associated with the municipality
     * @param voisins the list of neighboring municipalities
     * @throws NullPointerException if any of the {@code nom}, {@code dep}, or {@code voisins} parameters are null
     */
    public Commune(int id, String nom, Departement dep, ArrayList<Commune> voisins) {
        if(nom == null || dep == null || voisins == null) {
            throw new NullPointerException("null values");
        }

        this.idCommune = id;
        this.nomCommune = nom;
        this.LeDep = dep;
        this.voisins = new ArrayList<>(voisins);
    }

    /**
     * Returns the ID of the municipality.
     *
     * @return the ID of the municipality
     */
    public int getIdCommune() {
        return this.idCommune;
    }

    /**
     * Returns the ID of the municipality as a String.
     *
     * @return the ID of the municipality as a String
     */
    public String getId() {
        return String.valueOf(this.idCommune);
    }

    /**
     * Sets the ID of the municipality.
     *
     * @param idCommune the new ID of the municipality
     */
    public void setIdCommune(int idCommune) {
        this.idCommune = idCommune;
    }

    /**
     * Returns the name of the municipality.
     *
     * @return the name of the municipality
     */
    public String getNomCommune() {
        return this.nomCommune;
    }

    /**
     * Sets the name of the municipality.
     *
     * @param nomCommune the new name of the municipality
     */
    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    /**
     * Returns the department associated with the municipality.
     *
     * @return the department associated with the municipality
     */
    public Departement getDep() {
        return this.LeDep;
    }

    /**
     * Sets the department associated with the municipality.
     *
     * @param dep the new department associated with the municipality
     * @throws NullPointerException if the {@code dep} parameter is null
     */
    public void setDep(Departement dep) {
        if(dep == null) {
            throw new NullPointerException("dep is null");
        }

        this.LeDep = dep;
    }

    /**
     * Returns a list of neighboring municipalities.
     *
     * @return a list of neighboring municipalities
     */
    public ArrayList<Commune> getVoisins() {
        return new ArrayList<>(this.voisins);
    }

    /**
     * Adds a neighboring municipality to the list of neighbors.
     *
     * @param c the neighboring municipality to add
     * @throws NullPointerException if the {@code c} parameter is null
     */
    public void ajouterVoisin(Commune c) {
        if(c == null) {
            throw new NullPointerException("commune is null");
        }

        if(!this.voisins.contains(c)) {
            this.voisins.add(c);
            c.ajouterVoisin(this);
        }
    }

    /**
     * Determines if the municipality borders another department.
     *
     * @return {@code true} if the municipality borders another department, {@code false} otherwise
     */
    public boolean estBordureDep() {
        Iterator<Commune> it = this.voisins.iterator();
        boolean found = false;

        while(it.hasNext() && !found) {
            Commune com = it.next();
            if(com.getDep().getIdDep() != this.getDep().getIdDep()) {
                found = true;
            }
        }

        return found;
    }

    /**
     * Determines if the municipality is a neighbor with another specified municipality.
     *
     * @param autreCom the other municipality to check
     * @return {@code true} if the municipality is a neighbor with the specified municipality, {@code false} otherwise
     * @throws NullPointerException if the {@code autreCom} parameter is null
     */
    public boolean estVoisineAvec(Commune autreCom) {
        if(autreCom == null) {
            throw new NullPointerException("autreCom is null");
        }

        return this.voisins.contains(autreCom);
    }

    /**
     * Returns a string representation of the municipality, including its ID, name, and associated department ID.
     *
     * @return a string representation of the municipality
     */
    public String toString() {
        return this.idCommune + " - " + this.nomCommune + "(" + this.LeDep.getIdDep() + ")";
    }
}
