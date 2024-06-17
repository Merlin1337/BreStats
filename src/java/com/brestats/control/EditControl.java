package com.brestats.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class EditControl {
    @FXML
    private ChoiceBox<String> cityChoiceBox;
    @FXML
    private ChoiceBox<Annee> yearChoiceBox;
    @FXML
    private TextField housesNumberField;
    @FXML
    private TextField apartsNumberField;
    @FXML
    private TextField costField;
    @FXML
    private TextField m2CostField;
    @FXML
    private TextField averageSurfaceField;
    @FXML
    private TextField populationField;
    @FXML
    private TextField budgetField;
    @FXML 
    private TextField spendingsField;

    private DBValeursCommuneAnnee dbValeursCommuneAnnee;
    private ArrayList<Commune> selectedCities;
    private ArrayList<Annee> years;
    private ArrayList<DonneesAnnuelles> data;

    public EditControl() {
        this.dbValeursCommuneAnnee = DAO.DB_VAL;
        this.selectedCities = new ArrayList<>();
        this.years = new ArrayList<>();
        this.data = null;
    }

    @FXML
    public void initialize() {
        // A listener for when the selected city in choice box is changed
        this.cityChoiceBox.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> obs, String oldV, String newV) {
                try {
                    data = dbValeursCommuneAnnee.selectQuery("SELECT donneesannuelles.* FROM donneesannuelles JOIN commune ON laCommune = idCommune WHERE nomCommune = '" + newV + "';");

                    for (DonneesAnnuelles row : data) {
                        years.add(row.getLAnnee());
                    }

                    yearChoiceBox.setItems(FXCollections.observableList(years));
                } catch (SQLException ex) {
                    System.out.println("Unexpected excepetion with query : SELECT donneesannuelles.* FROM donneesannuelles JOIN commune ON laCommune = idCommune WHERE nomCommune = '" + newV + "';");
                    ex.printStackTrace();
                }

            }
        });

        // A listener for when the selected year in choice box is changed
        this.yearChoiceBox.valueProperty().addListener(new ChangeListener<Annee>() {
            public void changed(ObservableValue<? extends Annee> obs, Annee oldV, Annee newV) {
                int i = yearChoiceBox.getSelectionModel().getSelectedIndex();
                if(data != null && data.size() > i) {
                    DonneesAnnuelles row = data.get(i);

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
    }

    @FXML
    public void handleBack(MouseEvent ev) {
        try {
            FXMLLoader results = new FXMLLoader(getClass().getResource("/com/brestats/pages/Results.fxml"));
            Parent resultsScene = results.load();
            ResultsControl control = results.getController();
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();

            stage.setScene(new Scene(resultsScene, stage.getScene().getWidth(), stage.getScene().getHeight()));
            
            for (Commune city : this.selectedCities) {
                control.addSelectedCity(city);
            }
        } catch (IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleSave(ActionEvent ev) {
        int cityInd = this.cityChoiceBox.getSelectionModel().getSelectedIndex();
        int yearInd = this.yearChoiceBox.getSelectionModel().getSelectedIndex();
        if(yearInd != -1 && cityInd != -1) {
            try {
                int housesNumber = (int) Double.parseDouble(housesNumberField.getText());
                int apartsNumber = (int) Double.parseDouble(apartsNumberField.getText());
                double cost = Double.parseDouble(costField.getText());
                double m2Cost = Double.parseDouble(m2CostField.getText());
                double averageSurface = Double.parseDouble(averageSurfaceField.getText());
                double population = Double.parseDouble(populationField.getText());
                double budget = Double.parseDouble(budgetField.getText());
                double spendings = Double.parseDouble(spendingsField.getText());

                Commune com = selectedCities.get(cityInd);
                Annee year = years.get(yearInd);
                DonneesAnnuelles newData = new DonneesAnnuelles(com, year, housesNumber, apartsNumber, cost, m2Cost, averageSurface, spendings, budget, population);
                this.dbValeursCommuneAnnee.insertQuery(newData);

                Alert alert = new Alert(AlertType.CONFIRMATION, "Données enregistrées.", ButtonType.OK);
                alert.show();

            } catch (NumberFormatException ex) {
                ex.printStackTrace();

                Alert alert = new Alert(AlertType.ERROR, "Vous devez rentrer des nombres dans les champs de texte", ButtonType.OK); 
                alert.show();
            }
        }
    }

    public void setSelectedCities(ArrayList<Commune> cities) {
        this.selectedCities = cities;

        ArrayList<String> cityNames = new ArrayList<>();
        for (Commune city : this.selectedCities) {
            cityNames.add(city.getNomCommune());
        }

        this.cityChoiceBox.setItems(FXCollections.observableList(cityNames));
    }
}
