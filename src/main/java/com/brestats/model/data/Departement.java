package com.brestats.model.data;

import com.brestats.model.Model;

public class Departement implements Model {

	private int idDep;
	private String nomDep;
	private double invesCulturel2019;

	/**
	 * 
	 * @param id
	 * @param nom
	 * @param inves
	 */
	public Departement(int id, String nom, double inves) {
		if(nom == null) {
			throw new NullPointerException("null value");
		}

		this.idDep = id;
		this.nomDep = nom;
		this.invesCulturel2019 = inves;
	}

	public String getNomDep() {
		return this.nomDep;
	}

	/**
	 * 
	 * @param nomDep
	 */
	public void setNomDep(String nomDep) {
		this.nomDep = nomDep;
	}

	public int getIdDep() {
		return this.idDep;
	}

	/**
	 * 
	 * @param idDep
	 */
	public void setIdDep(int idDep) {
		this.idDep = idDep;
	}

	public double getInvesCulturel2019() {
		return this.invesCulturel2019;
	}

	/**
	 * 
	 * @param invesCulturel2019
	 */
	public void setInvesCulturel2019(double invesCulturel2019) {
		this.invesCulturel2019 = invesCulturel2019;
	}

	public String toString() {
		return this.idDep + " - " + this.nomDep;
	}

}