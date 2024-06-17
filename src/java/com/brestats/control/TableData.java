package com.brestats.control;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A nested class which is used for the table and average chart data
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class TableData {
    /** 
     * The list for all others properties attributes. In order :  <br>
     * <tr> 0 - {@link #name} <br>
     * <tr> 1 - {@link #dep} <br>
     * <tr> 2 - {@link #year} <br>
     * <tr> 3 - {@link #population} <br>
     * <tr> 4 - {@link #houses} <br>
     * <tr> 5 - {@link #apartements} <br>
     * <tr> 6 - {@link #cost} <br>
     * <tr> 7 - {@link #m2Cost} <br>
     * <tr> 8 - {@link #surface} <br>
     * <tr> 9 - {@link #spendings} <br>
     * <tr> 10 - {@link #budget}
     */
    private List<StringProperty> properties;
    /** The name of the city */
    private StringProperty name;
    /** The département of the city's name */
    private StringProperty dep;
    /** The year of other data */
    private StringProperty year;
    /** The population of the city */
    private StringProperty population;
    /** The sold houses number per year in the city */
    private StringProperty houses;
    /** The sold apartements number per year in the city */
    private StringProperty apartments;
    /** The cost of houses and apartments per year in the city */
    private StringProperty cost;
    /** The m² cost of houses and apartments per year in the city */
    private StringProperty m2Cost;
    /** The surface of sold houses and apartments per year in the city */
    private StringProperty surface;
    /** The spendings per year by the city */
    private StringProperty spendings;
    /** The budget of the city */
    private StringProperty budget;

    /**
     * Call
     * {@link #TableData(String, String, String ,String, String, String, String, String, String, String)}
     * with doubles converted to Strings
     * 
     * @param name       The name of the city
     * @param dep        The département of the city's name
     * @param year       The year of data
     * @param pop        The average population of the city
     * @param houses     The average sold houses number per year in the city
     * @param apartments The average sold apartements number per year in the city
     * @param cost       The average cost of houses and apartments per year in the
     *                   city
     * @param m2Cost     The average m² cost of houses and apartments per year in
     *                   the city
     * @param surface    The average surface of sold houses and apartments per year
     *                   in the city
     * @param spendings  The average spendings per year by the city
     * @param budget     The average budget of the city
     */
    public TableData(String name, String dep, int year, double pop, double houses, double apartments, double cost,
            double m2Cost, double surface, double spendings, double budget) {
        this(name, dep, Integer.toString(year), Double.toString(pop), Double.toString(houses), Double.toString(apartments),
                Double.toString(cost), Double.toString(m2Cost), Double.toString(surface),
                Double.toString(spendings), Double.toString(budget));
    }

    /**
     * Initiate an instance of TableData with given params
     * 
     * @param name       The name of the city
     * @param dep        The département of the city's name
     * @param year       The year of data
     * @param pop        The average population of the city
     * @param houses     The average sold houses number per year in the city
     * @param apartments The average sold apartements number per year in the city
     * @param cost       The average cost of houses and apartments per year in the
     *                   city
     * @param m2Cost     The average m² cost of houses and apartments per year in
     *                   the city
     * @param surface    The average surface of sold houses and apartments per year
     *                   in the city
     * @param spendings  The average spendings per year by the city
     * @param budget     The average budget of the city
     */
    public TableData(String name, String dep, String year, String pop, String houses, String apartments, String cost,
            String m2Cost, String surface, String spendings, String budget) {
        this.name = new SimpleStringProperty(name);
        this.dep = new SimpleStringProperty(dep);
        this.year = new SimpleStringProperty(year);
        this.population = new SimpleStringProperty(pop);
        this.houses = new SimpleStringProperty(houses);
        this.apartments = new SimpleStringProperty(apartments);
        this.cost = new SimpleStringProperty(cost);
        this.m2Cost = new SimpleStringProperty(m2Cost);
        this.surface = new SimpleStringProperty(surface);
        this.spendings = new SimpleStringProperty(spendings);
        this.budget = new SimpleStringProperty(budget);

        this.properties = List.of(this.name, this.dep, this.year, this.population, this.houses, this.apartments, this.cost,
                this.m2Cost, this.surface, this.spendings, this.budget);
    }

    /**
     * Return the properties list
     * 
     * @return The properties list
     */
    public List<StringProperty> getProperties() {
        return this.properties;
    }

    /**
     * Return the name of the city
     * 
     * @return the name as String
     */
    public String getName() {
        return this.name.get();
    }

    /**
     * Return the name of the département of the city
     * 
     * @return the name of the département as String
     */
    public String getDep() {
        return this.dep.get();
    }

    /**
     * Return the year of data
     * 
     * @return the year of data as String
     */
    public String getYear()  {
        return this.year.get();
    }

    /**
     * Return the average population of the city
     * 
     * @return The population as String
     */
    public String getPopulation() {
        return this.population.get();
    }

    /**
     * Return the average number of sold houses
     * 
     * @return The number of sold houses as String
     */
    public String getHouses() {
        return this.houses.get();
    }

    /**
     * Return the average number of sold apartments
     * 
     * @return The number of sold apartments as String
     */
    public String getApartments() {
        return this.apartments.get();
    }

    /**
     * Return the average cost of houses and apartments
     * 
     * @return The cost as String
     */
    public String getCost() {
        return this.cost.get();
    }

    /**
     * Return the average m² cost of houses and apartments
     * 
     * @return The m² cost as String
     */
    public String getM2Cost() {
        return this.m2Cost.get();
    }

    /**
     * Return the average surface of houses and apartments
     * 
     * @return The surface as String
     */
    public String getSurface() {
        return this.surface.get();
    }

    /**
     * Return the average spendings
     * 
     * @return The spendings as String
     */
    public String getSpendings() {
        return this.spendings.get();
    }

    /**
     * Return the total budget
     * 
     * @return The budget as String
     */
    public String getBudget() {
        return this.budget.get();
    }
}