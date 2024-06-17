package com.brestats.model.data;

import com.brestats.model.Model;

/**
 * The {@code DonneesAnnuelles} class represents annual data for a specific
 * municipality and year.
 * It implements the {@code Model} interface and provides methods to access and
 * manipulate the annual data.
 *
 * <p>
 * This class includes methods to get and set the number of houses, number of
 * apartments, average price,
 * average price per square meter, average surface area, total cultural
 * expenses, total budget, and population.
 * </p>
 *
 * @see Model
 */
public class DonneesAnnuelles implements Model {
    /** Number of houses sold in city for the year */
    private int nbMaison;
    /** Number of apartements sold in city for the year */
    private int nbAppart;
    /** Average cost of houses and apartments */
    private double prixMoyen;
    /** Average m² cost for sold houses and apartments */
    private double prixM2Moyen;
    /** Average surface for sold houses and apartments */
    private double surfaceMoyenne;
    /** Sum of cultural spendings by the city */
    private double depCulturelTotales;
    /** Sum of the city budget */
    private double budgetTotal;
    /** Population for the city and the year */
    private double population;
    /** The year */
    private Annee lAnnee;
    /** The city */
    private Commune laCom;

    /**
     * Constructs a new {@code DonneesAnnuelles} with the specified municipality and
     * year.
     * All other values are initialized to zero.
     *
     * @param com   the municipality
     * @param annee the year
     * @throws NullPointerException if any of the {@code annee} or {@code com}
     *                              parameters are null
     */
    public DonneesAnnuelles(Commune com, Annee annee) {
        this(com, annee, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    /**
     * Constructs a new {@code DonneesAnnuelles} with the specified parameters.
     *
     * @param com                   the municipality
     * @param annee                 the year
     * @param nbMaisons             the number of houses
     * @param nbAppart              the number of apartments
     * @param prixMoyen             the average price
     * @param prixM2Moyen           the average price per square meter
     * @param surfaceMoy            the average surface area
     * @param depCulturellesTotales the total cultural expenses
     * @param budgetTotal           the total budget
     * @param pop                   the population
     * @throws NullPointerException if any of the {@code com} or {@code annee}
     *                              parameters are null
     */
    public DonneesAnnuelles(Commune com, Annee annee, int nbMaisons, int nbAppart, double prixMoyen, double prixM2Moyen,
            double surfaceMoy, double depCulturellesTotales, double budgetTotal, double pop) {
        if (com == null || annee == null) {
            throw new NullPointerException("null values : " + com + ";" + annee + ";" + nbMaisons + ";" + nbAppart + ";"
                    + prixMoyen + ";" + prixM2Moyen + ";" + surfaceMoy + ";" + depCulturellesTotales + ";" + budgetTotal
                    + ";" + pop);
        }

        this.laCom = com;
        this.lAnnee = annee;
        this.nbMaison = nbMaisons;
        this.nbAppart = nbAppart;
        this.prixMoyen = prixMoyen;
        this.prixM2Moyen = prixM2Moyen;
        this.surfaceMoyenne = surfaceMoy;
        this.depCulturelTotales = depCulturellesTotales;
        this.budgetTotal = budgetTotal;
        this.population = pop;
    }

    /**
     * Returns the identifier of the annual data, composed of the municipality ID
     * and the year ID like "city-year".
     *
     * @return the identifier of the annual data
     */
    public String getId() {
        return String.valueOf(this.laCom.getId()) + "-" + String.valueOf(this.lAnnee.getId());
    }

    /**
     * Returns the number of houses.
     *
     * @return the number of houses
     */
    public int getNbMaison() {
        return this.nbMaison;
    }

    /**
     * Sets the number of houses.
     *
     * @param nbMaison the new number of houses
     */
    public void setNbMaison(int nbMaison) {
        this.nbMaison = nbMaison;
    }

    /**
     * Returns the number of apartments.
     *
     * @return the number of apartments
     */
    public int getNbAppart() {
        return this.nbAppart;
    }

    /**
     * Sets the number of apartments.
     *
     * @param nbAppart the new number of apartments
     */
    public void setNbAppart(int nbAppart) {
        this.nbAppart = nbAppart;
    }

    /**
     * Returns the average price.
     *
     * @return the average price
     */
    public double getPrixMoyen() {
        return this.prixMoyen;
    }

    /**
     * Sets the average price.
     *
     * @param prixMoyer the new average price
     */
    public void setPrixMoyen(double prixMoyer) {
        this.prixMoyen = prixMoyer;
    }

    /**
     * Returns the average price per square meter.
     *
     * @return the average price per square meter
     */
    public double getPrixM2Moyen() {
        return this.prixM2Moyen;
    }

    /**
     * Sets the average price per square meter.
     *
     * @param prixM2Moyen the new average price per square meter
     */
    public void setPrixM2Moyen(double prixM2Moyen) {
        this.prixM2Moyen = prixM2Moyen;
    }

    /**
     * Returns the average surface area.
     *
     * @return the average surface area
     */
    public double getSurfaceMoyenne() {
        return this.surfaceMoyenne;
    }

    /**
     * Sets the average surface area.
     *
     * @param surfaceMoyenne the new average surface area
     */
    public void setSurfaceMoyenne(double surfaceMoyenne) {
        this.surfaceMoyenne = surfaceMoyenne;
    }

    /**
     * Returns the total cultural expenses.
     *
     * @return the total cultural expenses
     */
    public double getDepCulturelTotales() {
        return this.depCulturelTotales;
    }

    /**
     * Sets the total cultural expenses.
     *
     * @param depCulturelTotales the new total cultural expenses
     */
    public void setDepCulturelTotales(double depCulturelTotales) {
        this.depCulturelTotales = depCulturelTotales;
    }

    /**
     * Returns the total budget.
     *
     * @return the total budget
     */
    public double getBudgetTotal() {
        return this.budgetTotal;
    }

    /**
     * Sets the total budget.
     *
     * @param budgetTotal the new total budget
     */
    public void setBudgetTotal(double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    /**
     * Returns the population.
     *
     * @return the population
     */
    public double getPopulation() {
        return this.population;
    }

    /**
     * Sets the population.
     *
     * @param population the new population
     */
    public void setPopulation(double population) {
        this.population = population;
    }

    /**
     * Returns the year associated with the annual data.
     *
     * @return the year associated with the annual data
     */
    public Annee getLAnnee() {
        return this.lAnnee;
    }

    /**
     * Sets the year associated with the annual data.
     *
     * @param annee the new year
     */
    public void setLAnnee(Annee annee) {
        this.lAnnee = annee;
    }

    /**
     * Returns the municipality associated with the annual data.
     *
     * @return the municipality associated with the annual data
     */
    public Commune getLaCom() {
        return this.laCom;
    }

    /**
     * Sets the municipality associated with the annual data.
     *
     * @param comunne the new municipality
     */
    public void setLaCom(Commune comunne) {
        this.laCom = comunne;
    }

    /**
     * Calculates the budget per inhabitant.
     *
     * @return the budget per inhabitant
     */
    public double calculerBudgetParHabitant() {
        return this.budgetTotal / this.population;
    }

    /**
     * Calculates the average surface area per inhabitant.
     *
     * @return the average surface area per habitation
     */
    public double calculerSurfaceMoyenneParHabitant() {
        return this.surfaceMoyenne / this.population;
    }

    /**
     * Returns a string representation of the annual data, including the
     * municipality name and year.
     *
     * @return a string representation of the annual data
     */
    public String toString() {
        return "Donnees : " + this.laCom.getNomCommune() + " - " + this.lAnnee.getAnnee() + "\n\t"
                + this.nbMaison + " Maisons\n\t"
                + this.nbAppart + " Apparts\n\t"
                + this.prixMoyen + "€ en Moyenne\n\t"
                + this.prixM2Moyen + "€/m² en Moyenne\n\t"
                + this.surfaceMoyenne + "m² en moyenne\n\t"
                + this.depCulturelTotales + "€ de dépenses culturelles\n\t"
                + this.budgetTotal + "€ de budget total\n\t"
                + this.population + " hab.";
    }
}
