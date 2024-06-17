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

public class AddYearControl {
    @FXML
    private Label cityName;
    @FXML
    private TextField yearField;
    @FXML
    private TextField rateField;
    @FXML
    private TableView<TableData> tableView;

    private DBAnnee dbAnnee;
    private DBValeursCommuneAnnee dbValeursCommuneAnnee;
    private Commune city;
    private TableData tableData;
    private ObservableList<TableData> dataList = FXCollections.observableArrayList();
    private double keptRate;

    public AddYearControl() {
        this.dbAnnee = DAO.DB_ANNEE;
        this.dbValeursCommuneAnnee = DAO.DB_VAL;
        this.city = null;
        this.keptRate = 0;
    }

    @FXML
    public void handleAdd(ActionEvent ev) {
        try {
            Annee year = dbAnnee.getItem(this.yearField.getText());
            double typedRate = Double.parseDouble(this.rateField.getText());
            double yearRate = year.getTauxInflation();
            int yearInt = Integer.parseInt(this.yearField.getText());
            
            if (year != null && year.getTauxInflation() != typedRate) {
                Alert alert = new Alert(AlertType.WARNING,
                        "Une Année similaire avec un taux d'inflation différent existe déjà. Souhaitez-vous le remplacer par le nouveau ? ("
                                + year.getTauxInflation() + " par " + typedRate + ")",
                        ButtonType.NO, ButtonType.YES);

                alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    public void accept(ButtonType response) {
                        if(response.equals(ButtonType.YES)) {
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
            
            DonneesAnnuelles data = new DonneesAnnuelles(this.city, year);

            this.dbValeursCommuneAnnee.insertQuery(data);
            ((Stage) ((Node) ev.getSource()).getScene().getWindow()).close();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(AlertType.ERROR, "Veuillez saisir des nombres");
            alert.show();
        }
    }

    public void setData(DonneesAnnuelles data) {
        if(this.city == null) {
            this.city = data.getLaCom();
        } else if (!this.city.getId().equals(data.getLaCom().getId())) {
            throw new RuntimeException("Unexpected error");
        }

        if (this.cityName != null) {
            this.cityName.setText(data.getLaCom().getNomCommune());
        }

        this.tableData = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(),
                data.getLAnnee().getAnnee(), data.getPopulation(), data.getNbMaison(), data.getNbAppart(),
                data.getPrixMoyen(), data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(),
                data.getBudgetTotal());

        this.dataList.add(tableData);

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

    private void setKeptRate(double rate) {
        this.keptRate = rate;
    }
}
