package com.brestats.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.brestats.model.data.Commune;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller of Results.fxml view
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class ResultsControl {
    @FXML
    private Label city1;
    @FXML
    private Label city2;
    @FXML
    private Label city3;
    @FXML
    private Label city4;

    private ArrayList<Commune> selectedCities;
    private ArrayList<Label> cityLabels;

    public ResultsControl() {
        this.selectedCities = new ArrayList<Commune>(Arrays.asList(null, null, null, null));
    }

    @FXML
    public void initialize() {
        this.cityLabels = new ArrayList<Label>(Arrays.asList(city1, city2, city3, city4));

        for (int i = 0 ; i < 4 ; i++) {
            Commune city = this.selectedCities.get(i);
            if(city != null) {
                this.cityLabels.get(i).setText(city.getNomCommune());
            } else {
                this.cityLabels.get(i).setText("<Aucune commune sélectionnée>");
            }
        }
    }

    @FXML
    public void handleBack(MouseEvent ev) {
        try {
            Parent main = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Main.fxml"));
            Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
            stage.setScene(new Scene(main));
            System.out.println("change");
        } catch(IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    public void addSelectedCity(Commune city) {
        this.selectedCities.add(city);

        int i = 0;
        while(i < this.selectedCities.size() && this.selectedCities.get(i) != null) {
            i++;
        }
        this.cityLabels.get(i).setText(city.getNomCommune());
    }
}
