package com.brestats.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.brestats.model.dao.DAO;
import com.brestats.model.dao.DBValeursCommuneAnnee;
import com.brestats.model.data.Annee;
import com.brestats.model.data.Commune;
import com.brestats.model.data.DonneesAnnuelles;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for the Edit view
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class EditControl {
    /** The choice box for cities */
    @FXML
    private ChoiceBox<String> cityChoiceBox;
    /** The choice box for years */
    @FXML
    private ChoiceBox<Annee> yearChoiceBox;
    /** The field for the number of houses */
    @FXML
    private TextField housesNumberField;
    /** The field for the number of apartments */
    @FXML
    private TextField apartsNumberField;
    /** The field for the average cost */
    @FXML
    private TextField costField;
    /** The field for the average m² cost */
    @FXML
    private TextField m2CostField;
    /** The field for the average surface per habitation */
    @FXML
    private TextField averageSurfaceField;
    /** The field for the population */
    @FXML
    private TextField populationField;
    /** The field for the city's budget */
    @FXML
    private TextField budgetField;
    /** The field for the city's cultural spendings */
    @FXML
    private TextField spendingsField;

    /** Connexion to donneesannuelles table from the database */
    private DBValeursCommuneAnnee dbValeursCommuneAnnee;
    /** Array of selected cities */
    private ArrayList<Commune> selectedCities;
    /** Array of year for which the current edited city has data */
    private ArrayList<Annee> years;
    /** The data for the current edited city of selected year */
    private ArrayList<DonneesAnnuelles> data;
    /** The stage for the AddYear view */
    private Stage addYearStage;

    /**
     * Constructs and initializes arrays, stage and db connection
     */
    public EditControl() {
        this.dbValeursCommuneAnnee = DAO.DB_VAL;
        this.selectedCities = new ArrayList<>();
        this.years = new ArrayList<>();
        this.data = null;

        this.addYearStage = new Stage();
        this.addYearStage.setTitle("Ajouter une année - Brestats");

    }

    /**
     * Initializes event listeners for choice boxes and the stages
     */
    @FXML
    public void initialize() {
        // A listener for when the selected city in choice box is changed
        this.cityChoiceBox.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> obs, String oldV, String newV) {
                refreshYearChoiceBox();
            }
        });

        // A listener for when the selected year in choice box is changed
        this.yearChoiceBox.valueProperty().addListener(new ChangeListener<Annee>() {
            public void changed(ObservableValue<? extends Annee> obs, Annee oldV, Annee newV) {
                int i = yearChoiceBox.getSelectionModel().getSelectedIndex();
                if (data != null && i < data.size()) {
                    DonneesAnnuelles row = data.get(i);

                    // When the year choice box selected item is not null, filling the fields with
                    // corresponding values
                    housesNumberField.setText(Integer.toString(row.getNbMaison()));
                    apartsNumberField.setText(Integer.toString(row.getNbAppart()));
                    costField.setText(Double.toString(row.getPrixMoyen()));
                    m2CostField.setText(Double.toString(row.getPrixM2Moyen()));
                    averageSurfaceField.setText(Double.toString(row.getSurfaceMoyenne()));
                    populationField.setText(Double.toString(row.getPopulation()));
                    budgetField.setText(Double.toString(row.getBudgetTotal()));
                    spendingsField.setText(Double.toString(row.getDepCulturelTotales()));
                } else {
                    Alert alert = new Alert(AlertType.ERROR, "Une erreur s'est produite", ButtonType.OK);
                    alert.show();
                }
            }
        });

        // A listener for when the AddYear window opens of closes
        this.addYearStage.showingProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> obs, Boolean oldV, Boolean newV) {
                cityChoiceBox.setDisable(newV); // Disable/Enable the cities' choice box depending on whether the
                                                // AddYear window is open
                refreshYearChoiceBox();
            }
        });
    }

    /**
     * Mouse clicked event listener for when back icon is clicked. Go back to the
     * Results view.
     * 
     * @param ev A mouse event
     */
    @FXML
    public void handleBack(MouseEvent ev) {
        try {
            // Close all other windows when going back to results
            addYearStage.close();

            FXMLLoader results = new FXMLLoader(getClass().getResource("/com/brestats/pages/Results.fxml"));
            Parent resultsScene = results.load();
            ResultsControl control = results.getController();
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

            stage.setScene(new Scene(resultsScene, stage.getScene().getWidth(), stage.getScene().getHeight()));

            // Passing back selected cities
            for (Commune city : this.selectedCities) {
                control.addSelectedCity(city);
            }
        } catch (IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    /**
     * An action event listener for when the save button is clicked. Show alert
     * dialog windows depending on the successfullness of the saving.
     * 
     * @param ev An action event
     */
    @FXML
    public void handleSave(ActionEvent ev) {
        // Selected item indexes for choices boxes
        int cityInd = this.cityChoiceBox.getSelectionModel().getSelectedIndex();
        int yearInd = this.yearChoiceBox.getSelectionModel().getSelectedIndex();

        if (yearInd != -1 && cityInd != -1) {
            try {
                // get all values as int or double (throw and catch NumberFormatException if
                // values are not well-formed number)
                int housesNumber = (int) Double.parseDouble(housesNumberField.getText());
                int apartsNumber = (int) Double.parseDouble(apartsNumberField.getText());
                double cost = Double.parseDouble(costField.getText());
                double m2Cost = Double.parseDouble(m2CostField.getText());
                double averageSurface = Double.parseDouble(averageSurfaceField.getText());
                double population = Double.parseDouble(populationField.getText());
                double budget = Double.parseDouble(budgetField.getText());
                double spendings = Double.parseDouble(spendingsField.getText());

                // Get the city and the year
                Commune com = selectedCities.get(cityInd);
                Annee year = years.get(yearInd);
                // Create a new DonneesAnnuelles object and update the database
                DonneesAnnuelles newData = new DonneesAnnuelles(com, year, housesNumber, apartsNumber, cost, m2Cost,
                        averageSurface, spendings, budget, population);
                this.dbValeursCommuneAnnee.insertQuery(newData);

                // Saving confirmation
                Alert alert = new Alert(AlertType.CONFIRMATION, "Données enregistrées.", ButtonType.OK);
                alert.show();

            } catch (NumberFormatException ex) {
                ex.printStackTrace();

                Alert alert = new Alert(AlertType.ERROR, "Vous devez rentrer des nombres dans les champs de texte",
                        ButtonType.OK);
                alert.show();
            } catch (Exception ex) { // Mainly to avoid the DBObject#getItem() NullPointerException if city of year
                                     // is not selected in the view
                ex.printStackTrace();

                Alert alert = new Alert(AlertType.ERROR, "Une erreur s'est produite",
                        ButtonType.OK);
                alert.show();
            }
        }
    }

    /**
     * A mouse clicked event listener for when the add icon besides the cities'
     * choice box is clicked. Open an other window in which the user is able to
     * create a new city
     * 
     * @param ev A mouse event
     */
    @FXML
    public void handleAddCity(MouseEvent ev) {
        // Not yet implemented
    }

    /**
     * A mouse clicked event listener for when the add icon besides the years'
     * choice box is clicked. Open an other window in which the user is able to add
     * new year data for the current selected city
     * 
     * @param ev A mouse event
     */
    @FXML
    public void handleAddYear(MouseEvent ev) {
        int cityInd = this.cityChoiceBox.getSelectionModel().getSelectedIndex();
        if (cityInd != -1) {
            try {
                FXMLLoader addYear = new FXMLLoader(getClass().getResource("/com/brestats/pages/AddYear.fxml"));
                Parent addYearScene = addYear.load();
                AddYearControl control = addYear.getController();

                this.addYearStage.setScene(new Scene(addYearScene));
                this.addYearStage.show();

                // Pass data for the table inside the AddYear view
                for (DonneesAnnuelles row : this.data) {
                    control.setData(row);
                }
            } catch (IOException ex) {
                System.out.println("Cannot change scene");
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR, "Veuillez choisir une commune", ButtonType.OK);
            alert.show();
        }
    }

    /**
     * An action event listener for when the remove button is clicked. Show an alert
     * dialog window to ask user if he is sure to want to delete the data for the
     * current selected city and for the current selected year
     * 
     * @param ev An action event
     */
    @FXML
    public void handleRemove(ActionEvent ev) {
        Alert alert = new Alert(AlertType.WARNING,
                "Etes-vous sûr(e) de vouloir supprimer toutes les données pour cette année et cette commune ? (Cette action est irréverssible)",
                ButtonType.NO, ButtonType.YES);
        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
            public void accept(ButtonType response) {
                // If user has clicked on the yes button, sends a delete query to the database
                // and refreshes the choice boxes
                if (response.equals(ButtonType.YES)) {
                    dbValeursCommuneAnnee.deleteQuery(data.get(yearChoiceBox.getSelectionModel().getSelectedIndex()));
                    refreshYearChoiceBox();
                }
            }
        });
    }

    /**
     * Setter for the {@link #selectedCities} attribute
     * 
     * @param cities selected cities in Main view
     */
    public void setSelectedCities(ArrayList<Commune> cities) {
        this.selectedCities = cities;

        ArrayList<String> cityNames = new ArrayList<>();
        for (Commune city : this.selectedCities) {
            cityNames.add(city.getNomCommune());
        }

        // Set the passed selected cities into the choice box
        this.cityChoiceBox.setItems(FXCollections.observableList(cityNames));
    }

    /**
     * Procedure refreshing years' choice box
     */
    private void refreshYearChoiceBox() {
        if (cityChoiceBox.getSelectionModel().getSelectedIndex() != -1) {
            try {
                // update data for the city
                data = dbValeursCommuneAnnee.selectQuery(
                        "SELECT donneesannuelles.* FROM donneesannuelles JOIN commune ON laCommune = idCommune WHERE nomCommune = '"
                                + cityChoiceBox.getSelectionModel().getSelectedItem() + "';");

                for (DonneesAnnuelles row : data) {
                    years.add(row.getLAnnee());
                }

                // Set new items into the choice box
                yearChoiceBox.setItems(FXCollections.observableList(years));
            } catch (SQLException ex) {
                System.out.println(
                        "Unexpected excepetion with query : SELECT donneesannuelles.* FROM donneesannuelles JOIN commune ON laCommune = idCommune WHERE nomCommune = '"
                                + cityChoiceBox.getSelectionModel().getSelectedItem() + "';");
                ex.printStackTrace();
            }
        }
    }
}
