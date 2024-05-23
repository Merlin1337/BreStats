import java.util.ArrayList;
import java.util.Iterator;

public class Commune {

	private ArrayList<Commune> voisins;
	private int idCommune;
	private String nomCommune;
	private Departement LeDep;

	/**
	 * 
	 * @param id
	 * @param nom
	 * @param dep
	 * @param voisins
	 */
	public Commune(int id, String nom, Departement dep, ArrayList<Commune> voisins) {
		if(nom == null || dep == null || voisins == null) {
			throw new NullPointerException("null values");
		}

		this.idCommune = id;
		this.nomCommune = nom;
		this.LeDep = dep;
		this.voisins = new ArrayList<Commune>(voisins);
	}

	public int getIdCommune() {
		return this.idCommune;
	}

	/**
	 * 
	 * @param idCommune
	 */
	public void setIdCommune(int idCommune) {
		this.idCommune = idCommune;
	}

	public String getNomCommune() {
		return this.nomCommune;
	}

	/**
	 * 
	 * @param nomCommune
	 */
	public void setNomCommune(String nomCommune) {
		this.nomCommune = nomCommune;
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

	public ArrayList<Commune> getVoisins() {
		return new ArrayList<Commune>(this.voisins);
	}

	public void ajouterVoisin(Commune c) {
		if(c == null) {
			throw new NullPointerException("commune is null");
		}

		if(this.voisins.contains(c)) {
			throw new RuntimeException("La commune est déjà présente comme voisin");
		}

		this.voisins.add(c);
		c.ajouterVoisin(this);
	}

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
	 * 
	 * @param autreCom
	 */
	public boolean estVoisineAvec(Commune autreCom) {
		if(autreCom == null) {
			throw new NullPointerException("autreCom is null");
		}

		return this.voisins.contains(autreCom);
	}

	public String toString() {
		return this.idCommune + " - " + this.nomCommune + "(" + this.LeDep.getIdDep() + ")";
	}

}