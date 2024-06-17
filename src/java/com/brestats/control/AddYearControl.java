package com.brestats.control;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.brestats.model.dao.DAO;
import com.brestats.model.dao.DBAnnee;
import com.brestats.model.dao.DBValeursCommuneAnnee;
import com.brestats.model.data.Annee;
import com.brestats.model.data.Commune;
import com.brestats.model.data.DonneesAnnuelles;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;

/**
 * Controller fot the AddYear view
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class AddYearControl {
    /** The label displaying the name of the city */
    @FXML
    private Label cityName;
    /** The field for the year */
    @FXML
    private TextField yearField;
    /** The field for the inflation rate associated with the new year */
    @FXML
    private TextField rateField;
    /** The table view showing current available years and data for the city */
    @FXML
    private TableView<TableData> tableView;

    /** The connection to the annee table from the database */
    private DBAnnee dbAnnee;
    /** The connection to the donneesannuelles table from the database */
    private DBValeursCommuneAnnee dbValeursCommuneAnnee;
    /** The city to which the user add a new year */
    private Commune city;
    /** The table view data */
    private TableData tableData;
    /** List of table view rows */
    private ObservableList<TableData> dataList;
    /**
     * The final kept inflation rate when a conflict happen between the rate from
     * the data and the one typed by the user
     */
    private double keptRate;

    /**
     * Constructs and initializes objects
     */
    public AddYearControl() {
        this.dbAnnee = DAO.DB_ANNEE;
        this.dbValeursCommuneAnnee = DAO.DB_VAL;
        this.dataList = FXCollections.observableArrayList();
        this.city = null;
        this.keptRate = 0;
    }

    /**
     * Action event listener for when the add button is clicked. Check if the city
     * is already existing and if the rates are the same between the database and
     * the value provided by the user
     * 
     * @param ev An action event
     */
    @FXML
    public void handleAdd(ActionEvent ev) {
        try {
            Annee year = dbAnnee.getItem(this.yearField.getText());
            double typedRate = Double.parseDouble(this.rateField.getText());
            double yearRate = year.getTauxInflation();
            int yearInt = Integer.parseInt(this.yearField.getText());

            // If the year already exists in the database but have a different inflation
            // rate, asks the user which value he wants to keep
            if (year != null && year.getTauxInflation() != typedRate) {
                Alert alert = new Alert(AlertType.WARNING,
                        "Une Année similaire avec un taux d'inflation différent existe déjà. Souhaitez-vous le remplacer par le nouveau ? ("
                                + year.getTauxInflation() + " par " + typedRate + ")",
                        ButtonType.NO, ButtonType.YES);

                alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    public void accept(ButtonType response) {
                        if (response.equals(ButtonType.YES)) {
                            // We use a procedure to avoid java constraint about the variables that must be
                            // final inside an enclosing scope
                            setKeptRate(typedRate);
                        } else {
                            setKeptRate(yearRate);
                        }
                    }
                });

                year.setTauxInflation(keptRate);
            } else {
                year = new Annee(yearInt, typedRate);
            }

            // Create the new year and the data row, and update the db
            DonneesAnnuelles data = new DonneesAnnuelles(this.city, year);

            this.dbValeursCommuneAnnee.insertQuery(data);
            // when data is saved in the db, close the window
            ((Stage) ((Node) ev.getSource()).getScene().getWindow()).close();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(AlertType.ERROR, "Veuillez saisir des nombres");
            alert.show();
        }
    }

    /**
     * Setter for {@link #city} attribute and for the table rows data
     * 
     * @param data A {@link DonneesAnnuelles} object
     */
    public void setData(DonneesAnnuelles data) {
        // if it is the first row, initiate the city attribute, else check if it is the
        // same
        if (this.city == null) {
            this.city = data.getLaCom();
        } else if (!this.city.getId().equals(data.getLaCom().getId())) {
            throw new RuntimeException("Unexpected error");
        }

        // if the cityName label attribute is initialized, set the name of the city
        if (this.cityName != null) {
            this.cityName.setText(data.getLaCom().getNomCommune());
        }

        // Create the row data and add it to the list
        this.tableData = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(),
                data.getLAnnee().getAnnee(), data.getPopulation(), data.getNbMaison(), data.getNbAppart(),
                data.getPrixMoyen(), data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(),
                data.getBudgetTotal());

        this.dataList.add(tableData);

        // Update the table view
        ArrayList<TableColumn<TableData, String>> columns = new ArrayList<TableColumn<TableData, String>>();
        for (TableColumn<TableData, ?> column : this.tableView.getColumns()) {
            // Unchecked cast because TableView.getColumns() return a non-typed object,
            // compile anyway
            columns.add((TableColumn<TableData, String>) column);
        }

        for (int i = 0; i < columns.size(); i++) {
            int ind = i; // Cannot use i in anonymous class, because it must be at least effectively
                         // final

            // Getting all used properties
            int[] usedPropertiesInd = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };

            // Setting up data in columns by passing TableData's StringPropery attributes
            columns.get(i).setCellValueFactory(
                    new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(CellDataFeatures<TableData, String> p) {
                            return p.getValue().getProperties().get(usedPropertiesInd[ind]);
                        }
                    });
        }
        this.tableView.setItems(dataList);
    }

    /**
     * Private setter for the {@link #keptRate} attribute
     * 
     * @param rate The kept inflation rate
     */
    private void setKeptRate(double rate) {
        this.keptRate = rate;
    }
}