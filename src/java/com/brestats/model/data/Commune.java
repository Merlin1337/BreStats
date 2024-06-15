package com.brestats.model.data;

import java.util.ArrayList;
import java.util.Iterator;

import com.brestats.model.Model;

/**
 * The {@code Commune} class represents a city with specific attributes such as
 * its ID, name, associated department, and neighboring cities.
 * It implements the {@code Model} interface and provides methods to access and
 * manipulate its data.
 * 
 * <p>
 * This class includes methods to get and set the city's ID, name, and
 * department, manage its neighboring cities, and determine if it borders
 * another department.
 * </p>
 * 
 * @see Model
 */
public class Commune implements Model {

    /** List of neighbours */
    private ArrayList<Commune> voisins;
    /** The INSEE id of the city */
    private int idCommune;
    /** the name of the city */
    private String nomCommune;
    /** the Département in which is located the city */
    private Departement LeDep;
    /** Latitude of the city */
    private double latitude;
    /** Longitude of the city */
    private double longitude;

    /**
     * Constructs a new {@code Commune} with the specified ID, name, department,
     * neighboring cities, latitude and longitude.
     *
     * @param id      the ID of the city
     * @param nom     the name of the city
     * @param dep     the department associated with the city
     * @param voisins the list of neighboring cities
     * @param lat     city's latitude
     * @param lgn     city's longitude
     * @throws NullPointerException if any of the {@code nom}, {@code dep}, or
     *                              {@code voisins} parameters are null
     */
    public Commune(int id, String nom, Departement dep, ArrayList<Commune> voisins, double lat, double lgn) {
        if (nom == null || dep == null || voisins == null) {
            throw new NullPointerException("null values");
        }

        this.idCommune = id;
        this.nomCommune = nom;
        this.LeDep = dep;
        this.voisins = new ArrayList<>(voisins);
        this.latitude = lat;
        this.longitude = lgn;
    }

    /**
     * Constructs a new {@code Commune} with the specified ID, name, department, and
     * neighboring cities. Sets the latitude and the longitude to 0
     *
     * @param id      the ID of the city
     * @param nom     the name of the city
     * @param dep     the department associated with the city
     * @param voisins the list of neighboring cities
     * @throws NullPointerException if any of the {@code nom}, {@code dep}, or
     *                              {@code voisins} parameters are null
     */
    public Commune(int id, String nom, Departement dep, ArrayList<Commune> voisins) {
        new Commune(id, nom, dep, voisins);
    }

    /**
     * Returns the ID of the city.
     *
     * @return the ID of the city
     */
    public int getIdCommune() {
        return this.idCommune;
    }

    /**
     * Returns the ID of the city as a String.
     *
     * @return the ID of the city as a String
     */
    public String getId() {
        return String.valueOf(this.idCommune);
    }

    /**
     * Sets the ID of the city.
     *
     * @param idCommune the new ID of the city
     */
    public void setIdCommune(int idCommune) {
        this.idCommune = idCommune;
    }

    /**
     * Returns the name of the city.
     *
     * @return the name of the city
     */
    public String getNomCommune() {
        return this.nomCommune;
    }

    /**
     * Sets the name of the city.
     *
     * @param nomCommune the new name of the city
     */
    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    /**
     * Returns the department associated with the city.
     *
     * @return the department associated with the city
     */
    public Departement getDep() {
        return this.LeDep;
    }

    /**
     * Sets the department associated with the city.
     *
     * @param dep the new department associated with the city
     * @throws NullPointerException if the {@code dep} parameter is null
     */
    public void setDep(Departement dep) {
        if (dep == null) {
            throw new NullPointerException("dep is null");
        }

        this.LeDep = dep;
    }

    /**
     * Returns a list of neighboring cities.
     *
     * @return a list of neighboring cities
     */
    public ArrayList<Commune> getVoisins() {
        return new ArrayList<>(this.voisins);
    }

    /**
     * Return the city's latitude
     * 
     * @return latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Return the city's longitude
     * 
     * @return latitude
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Sets the city's latitude
     * 
     * @param lat latitude
     */
    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    /**
     * Sets the city's longitude
     * 
     * @param lgn latitude
     */
    public void setLongitude(double lgn) {
        this.longitude = lgn;
    }

    /**
     * Adds a neighboring city to the list of neighbors.
     *
     * @param c the neighboring city to add
     * @throws NullPointerException if the {@code c} parameter is null
     */
    public void ajouterVoisin(Commune c) {
        if (c == null) {
            throw new NullPointerException("commune is null");
        }

        if (!this.voisins.contains(c)) {
            this.voisins.add(c);
            c.ajouterVoisin(this);
        }
    }

    /**
     * Determines if the city borders another department.
     *
     * @return {@code true} if the city borders another department, {@code false}
     *         otherwise
     */
    public boolean estBordureDep() {
        Iterator<Commune> it = this.voisins.iterator();
        boolean found = false;

        while (it.hasNext() && !found) {
            Commune com = it.next();
            if (com.getDep().getIdDep() != this.getDep().getIdDep()) {
                found = true;
            }
        }

        return found;
    }

    /**
     * Determines if the city is a neighbor with another specified city.
     *
     * @param autreCom the other city to check
     * @return {@code true} if the city is a neighbor with the specified city,
     *         {@code false} otherwise
     * @throws NullPointerException if the {@code autreCom} parameter is null
     */
    public boolean estVoisineAvec(Commune autreCom) {
        if (autreCom == null) {
            throw new NullPointerException("autreCom is null");
        }

        return this.voisins.contains(autreCom);
    }

    /**
     * Returns a string representation of the city, including its ID, name, and
     * associated department ID.
     *
     * @return a string representation of the city
     */
    public String toString() {
        return this.idCommune + " - " + this.nomCommune + "(" + this.LeDep.getIdDep() + ")";
    }
}
