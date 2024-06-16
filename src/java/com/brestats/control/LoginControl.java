package com.brestats.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginControl {
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;

    @FXML
    public void handleLogin(ActionEvent ev) {

        if (userField.getText().equals("admin") && passField.getText().equals("admin")) {
            System.out.println("Connected as admin");
        } else {
            Alert wrongId = new Alert(AlertType.ERROR, "Mauvais identifiant ou mot de passe", ButtonType.OK);
            wrongId.show();
        }
    }
}
