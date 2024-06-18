package com.brestats.control;

import java.io.IOException;
import java.util.ArrayList;

import com.brestats.model.data.Commune;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller for the Login view
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class LoginControl {
    /** The text field for user */
    @FXML
    private TextField userField;
    /** The text field for password (hidden) */
    @FXML
    private PasswordField passField;

    /** The main stage which will be changed to Edit page */
    private Stage mainStage = null;
    /** Array of selected cities */
    private ArrayList<Commune> cities = null;

    /**
     * Action event listener for Login button. Change to Edit page. This method can
     * be called from {@link #handleEnter(KeyEvent)}, hence the parameter is
     * Event-typed
     * 
     * @param ev Action or key event
     */
    @FXML
    public void handleLogin(Event ev) {
        // if user and password are "admin", change page. Otherwise show an alert popup
        if (userField.getText().equals("admin") && passField.getText().equals("admin")) {
            if (this.mainStage != null && this.cities != null) {
                try {
                    FXMLLoader edit = new FXMLLoader(getClass().getResource("/com/brestats/pages/Edit.fxml"));
                    Parent editScene = edit.load();
                    EditControl control = edit.getController();

                    this.mainStage.setScene(
                            new Scene(editScene, this.mainStage.getScene().getWidth(),
                                    this.mainStage.getScene().getHeight()));

                    control.setSelectedCities(this.cities);
                    ((Stage) ((Node) ev.getSource()).getScene().getWindow()).close();
                } catch (IOException ex) {
                    System.out.println("Cannot change scene");
                    ex.printStackTrace();
                }
            } else {
                Alert wrongId = new Alert(AlertType.ERROR,
                        "Une erreur s'est produite. Essayer de relancer l'application", ButtonType.OK);
                wrongId.show();
            }

        } else {
            Alert wrongId = new Alert(AlertType.ERROR, "Mauvais identifiant ou mot de passe", ButtonType.OK);
            wrongId.show();
        }
    }

    /**
     * Key event listener for when the enter key is pressed when one text field is focused. Call {@link #handleLogin(Event)} method
     * @param ev Key event
     */
    @FXML
    public void handleEnter(KeyEvent ev) {
        if (ev.getCode().equals(KeyCode.ENTER)) {
            handleLogin(ev);
        }
    }

    /**
     * Setter for the mainStage attribute. Called from the Results view
     * 
     * @param stage  The main stage
     * @param cities List of selected cities which will be pass to the Edit view if
     *               credentials are correct
     */
    public void setAttributes(Stage stage, ArrayList<Commune> cities) {
        this.mainStage = stage;
        this.cities = cities;
    }
}
