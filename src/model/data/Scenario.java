import java.util.ArrayList;

/**
 * Scenario
 */
public class Scenario {

    public static void main(String[] args) {
        Departement morbihan = new Departement(56, "Morbihan", 7107993);
        Commune vannes = new Commune(56260, "Vannes", morbihan, new ArrayList<Commune>());
        Commune sene = new Commune(56260, null, morbihan, new ArrayList<Commune>());
        Commune arradon = new Commune(56003, "arradon", morbihan, new ArrayList<Commune>());

        System.out.println(morbihan);
        System.out.println(vannes);
        System.out.println(sene);

        vannes.ajouterVoisin(sene);
        ArrayList<Commune> voisinsSene = sene.getVoisins();
        System.out.println(voisinsSene);

        boolean estVoisinArradonSene = vannes.estVoisineAvec(arradon);
        System.out.println(estVoisinArradonSene);

        Annee a2019 = new Annee(2019, 1.1);
        
        System.out.println(a2019);

        boolean estBissextile = a2019.estBissextile();
        System.out.println(estBissextile);

        ValeursCommuneAnnee vannes2019 = new ValeursCommuneAnnee(vannes, a2019);
        System.out.println(vannes2019);
        vannes2019.setPopulation(53438);
        vannes2019.setSurfaceMoyenne(71.1393);
        vannes2019.setBudgetTotal(83730);
        System.out.println(vannes2019);

        double surfaceMoy2019 = vannes2019.calculerSurfaceMoyenneParHabitation();
        double budgetHabitant = vannes2019.calculerBudgetParHabitant();
        System.out.println(surfaceMoy2019);
        System.out.println(budgetHabitant);

        Gare gareVannes = new Gare(87476606, "Vannes", vannes, false, true);
        System.out.println(gareVannes);

        Aeroport lorientLanBihque = new Aeroport("LORIENT-LANN-BIHOUE", "PLOEMEUR 56270", morbihan);
        System.out.println(lorientLanBihque);
    }
}