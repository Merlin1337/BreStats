package com.brestats.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginControl {
    /** The text field for user */
    @FXML
    private TextField userField;
    /** The text field for password (hidden) */
    @FXML
    private PasswordField passField;

    /**
     * Action event listener for Login button. Change to Edit page
     * @param ev Action event
     */
    @FXML
    public void handleLogin(ActionEvent ev) {
        // if user and password are "admin", change page. Otherwise show an alert popup
        if (userField.getText().equals("admin") && passField.getText().equals("admin")) {
            System.out.println("Connected as admin");
        } else {
            Alert wrongId = new Alert(AlertType.ERROR, "Mauvais identifiant ou mot de passe", ButtonType.OK);
            wrongId.show();
        }
    }
}
