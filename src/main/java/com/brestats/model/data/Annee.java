package com.brestats.model.data;

public class Annee {

	private int annee;
	private double tauxInflation;

	public Annee(int annee, double taux) {
		this.annee = annee;
		this.tauxInflation = taux;
	}

	public int getAnnee() {
		return this.annee;
	}

	/**
	 * 
	 * @param annee
	 */
	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public double getTauxInflation() {
		return this.tauxInflation;
	}

	/**
	 * 
	 * @param tauxInflation
	 */
	public void setTauxInflation(double tauxInflation) {
		this.tauxInflation = tauxInflation;
	}

	public boolean estBissextile() {
		return this.annee % 4 == 0;
	}

	public String toString() {
		return this.annee + " - " + (this.tauxInflation *100) + "%";
	}

}