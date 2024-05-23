import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StatsResult extends VBox {

    // Données pour chaque ville
    private String nomVille;
    private int nbHabitants;
    private int indiceTransport;
    private int prixM2;

    // Composants graphiques
    private Label lblNomVille;
    private Label lblNbHabitants;
    private Label lblIndiceTransport;
    private Label lblPrixM2;

    public StatsResult(String nomVille, int nbHabitants, int indiceTransport, int prixM2) {
        this.nomVille = nomVille;
        this.nbHabitants = nbHabitants;
        this.indiceTransport = indiceTransport;
        this.prixM2 = prixM2;

        // Création des labels avec les données
        lblNomVille = new Label(nomVille);
        lblNbHabitants = new Label(String.valueOf(nbHabitants));
        lblIndiceTransport = new Label(indiceTransport + "/10");
        lblPrixM2 = new Label(String.valueOf(prixM2));

        // Création du layout (HBox pour chaque ligne)
        HBox hbNomVille = new HBox(lblNomVille);
        HBox hbNbHabitants = new HBox(lblNbHabitants);
        HBox hbIndiceTransport = new HBox(lblIndiceTransport);
        HBox hbPrixM2 = new HBox(lblPrixM2);

        // Style (exemple, à personnaliser)
        this.setPadding(new Insets(10));
        this.setSpacing(5);
        this.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        // Style pour les labels (à personnaliser)
        lblNomVille.setStyle("-fx-font-weight: bold;");

        // Ajout des HBox au VBox principal
        this.getChildren().addAll(hbNomVille, hbNbHabitants, hbIndiceTransport, hbPrixM2);
    }
}