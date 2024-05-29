package com.brestats.model.data;

import com.brestats.model.Model;

public class ValeursCommuneAnnee implements Model {

	private int nbMaison;
	private int nbAppart;
	private double prixMoyen;
	private double prixM2Moyen;
	private double surfaceMoyenne;
	private double depCulturelTotales;
	private double budgetTotal;
	private double population;
	private Annee lAnnee;
	private Commune laCom;

	public ValeursCommuneAnnee(Commune com, Annee annee) {
		if(annee == null || com == null) {
			throw new NullPointerException("null values");
		}

		this.lAnnee = annee;
		this.laCom = com;
		this.nbMaison = 0;
		this.nbAppart = 0;
		this.prixMoyen = 0;
		this.prixM2Moyen = 0;
		this.surfaceMoyenne = 0;
		this.depCulturelTotales = 0;
		this.budgetTotal = 0;
		this.population = 0;
	}

	/**
	 * 
	 * @param com
	 * @param annee
	 * @param nbMaisons
	 * @param nbAppart
	 * @param prixMoyen
	 * @param prixM2Moyen
	 * @param surfaceMoy
	 * @param depCulturellesTotales
	 * @param budgetTotal
	 * @param pop
	 */
	public ValeursCommuneAnnee(Commune com, Annee annee, int nbMaisons, int nbAppart, double prixMoyen, double prixM2Moyen, double surfaceMoy, double depCulturellesTotales, double budgetTotal, double pop) {
		if(com == null || annee == null) {
			throw new NullPointerException("null values");
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

	public int getNbMaison() {
		return this.nbMaison;
	}

	/**
	 * 
	 * @param nbMaison
	 */
	public void setNbMaison(int nbMaison) {
		this.nbMaison = nbMaison;
	}

	public int getNbAppart() {
		return this.nbAppart;
	}

	/**
	 * 
	 * @param nbAppart
	 */
	public void setNbAppart(int nbAppart) {
		this.nbAppart = nbAppart;
	}

	public double getPrixMoyer() {
		return this.prixMoyen;
	}

	/**
	 * 
	 * @param prixMoyer
	 */
	public void setPrixMoyer(double prixMoyer) {
		this.prixMoyen = prixMoyer;
	}

	public double getPrixM2Moyen() {
		return this.prixM2Moyen;
	}

	/**
	 * 
	 * @param prixM2Moyen
	 */
	public void setPrixM2Moyen(double prixM2Moyen) {
		this.prixM2Moyen = prixM2Moyen;
	}

	public double getSurfaceMoyenne() {
		return this.surfaceMoyenne;
	}

	/**
	 * 
	 * @param surfaceMoyenne
	 */
	public void setSurfaceMoyenne(double surfaceMoyenne) {
		this.surfaceMoyenne = surfaceMoyenne;
	}

	public double getDepCulturelTotales() {
		return this.depCulturelTotales;
	}

	/**
	 * 
	 * @param depCulturelTotales
	 */
	public void setDepCulturelTotales(double depCulturelTotales) {
		this.depCulturelTotales = depCulturelTotales;
	}

	public double getBudgetTotal() {
		return this.budgetTotal;
	}

	/**
	 * 
	 * @param budgetTotal
	 */
	public void setBudgetTotal(double budgetTotal) {
		this.budgetTotal = budgetTotal;
	}

	public double getPopulation() {
		return this.population;
	}

	/**
	 * 
	 * @param population
	 */
	public void setPopulation(double population) {
		this.population = population;
	}

	public Annee getLAnnee() {
		return this.lAnnee;
	}

	/**
	 * 
	 * @param annee
	 */
	public void setLAnnee(Annee annee) {
		this.lAnnee = annee;
	}

	public Commune getLaCom() {
		return this.laCom;
	}

	/**
	 * 
	 * @param comunne
	 */
	public void setLaCom(Commune comunne) {
		this.laCom = comunne;
	}

	public double calculerBudgetParHabitant() {
		return this.budgetTotal/this.population;
	}

	public double calculerSurfaceMoyenneParHabitation() {
		return this.surfaceMoyenne/this.population;
	}

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