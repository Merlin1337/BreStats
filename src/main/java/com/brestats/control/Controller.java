package com.brestats.control;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller {
    @FXML
    Button searchButton;
    @FXML
    WebView webView;

    @FXML
    public void initialize() {
        System.out.println("init");
        WebEngine engine = webView.getEngine();
        engine.load(getClass().getResource("/com/brestats/files/map.html").toString());
        // getClass().getResource("/com/brestats/files/map.html").getFile()
    }

    // @FXML
    // public void handleSearch(ActionEvent ev) {
    //     try {
    //         Parent results = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Results.fxml"));
    //         Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
    //         stage.setScene(new Scene(results));
    //         System.out.println("change");
    //     } catch(IOException ex) {
    //         System.out.println("Cannot change scene");
    //         ex.printStackTrace();
    //     }
    // }

    // @FXML
    // public void handleBack(MouseEvent ev) {
    //     try {
    //         Parent main = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Main.fxml"));
    //         Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
    //         stage.setScene(new Scene(main));
    //         System.out.println("change");
    //     } catch(IOException ex) {
    //         System.out.println("Cannot change scene");
    //         ex.printStackTrace();
    //     }
    // }
}
