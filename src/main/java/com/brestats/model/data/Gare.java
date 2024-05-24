package com.brestats.model.data;

public class Gare {

	private int codeGare;
	private String nomGare;
	private boolean estFret;
	private boolean estVoyageur;
	private Commune laCom;

	/**
	 * 
	 * @param code
	 * @param nom
	 * @param com
	 * @param estFret
	 * @param estVoyageur
	 */
	public Gare(int code, String nom, Commune com, boolean estFret, boolean estVoyageur) {
		if(null == nom || null == com ) {
			throw new NullPointerException("null value");
		}

		this.codeGare = code;
		this.nomGare = nom;
		this.laCom = com;
		this.estFret = estFret;
		this.estVoyageur = estVoyageur;
	}

	public int getCodeGare() {
		return this.codeGare;
	}

	/**
	 * 
	 * @param codeGare
	 */
	public void setCodeGare(int codeGare) {
		this.codeGare = codeGare;
	}

	public String getNomGare() {
		return this.nomGare;
	}

	/**
	 * 
	 * @param nomGare
	 */
	public void setNomGare(String nomGare) {
		this.nomGare = nomGare;
	}

	public boolean getEstFret() {
		return this.estFret;
	}

	/**
	 * 
	 * @param estFret
	 */
	public void setEstFret(boolean estFret) {
		this.estFret = estFret;
	}

	public boolean getEstVoyageur() {
		return this.estVoyageur;
	}

	/**
	 * 
	 * @param estVoyageur
	 */
	public void setEstVoyageur(boolean estVoyageur) {
		this.estVoyageur = estVoyageur;
	}

	public Commune getCom() {
		return this.laCom;
	}

	/**
	 * 
	 * @param com
	 */
	public void setCom(Commune com) {
		if(com == null) {
			throw new NullPointerException("com is null");
		}

		this.laCom = com;
	}

	public String toString() {
		return this.codeGare + " - " + this.nomGare;
	}

}