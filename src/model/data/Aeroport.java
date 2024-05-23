public class Aeroport {

	private Departement LeDep;
	private String nom;
	private String adresse;

	/**
	 * 
	 * @param nom
	 * @param adresse
	 * @param dep
	 */
	public Aeroport(String nom, String adresse, Departement dep) {
		if(nom == null || adresse == null || dep == null) {
			throw new NullPointerException("A value is null");
		}

		this.nom = nom;
		this.adresse = adresse;
		this.LeDep = dep;
	}

	public String getNom() {
		return this.nom;
	}

	/**
	 * 
	 * @param nom
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return this.adresse;
	}

	/**
	 * 
	 * @param adresse
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Departement getDep() {
		return this.LeDep;
	}

	/**
	 * 
	 * @param dep
	 */
	public void setDep(Departement dep) {
		if(dep == null) {
			throw new NullPointerException("dep is null");
		}

		this.LeDep = dep;
	}

	public String toString() {
		return "Aeroport " + this.nom + " ; " + this.adresse + " - " + this.LeDep.getIdDep();
	}

}