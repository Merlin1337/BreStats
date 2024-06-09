package com.brestats.control;

import java.io.IOException;

import com.brestats.model.data.Commune;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller of Results.fxml view
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class ResultsControl {
    private Commune selectedCity = null;

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

    public void setSelectedCity(Commune city) {
        this.selectedCity = city;
    }
}
