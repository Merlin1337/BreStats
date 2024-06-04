package com.brestats;

import java.io.IOException;

import com.brestats.control.MainControl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application  {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // SearchMap mapAndSearchBar = new SearchMap();
        Parent main = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Main.fxml"));
        
        Scene scene = new Scene(main);
        // Font newFont = Font.loadFont(getClass().getResourceAsStream("/com/brestats/files/fonts/Inter.ttf"), 14); // Adjust font size as needed
        // scene.getRoot().setStyle("-fx-font-family: '" + newFont.getFamily() + "'; -fx-font-size: " + newFont.getSize() + "px; -fx-font-weight: 600");
        primaryStage.setTitle("Bre'Stats");
        primaryStage.getIcons().add(new Image(getClass().getResource("/com/brestats/files/img/favicon.png").toExternalForm()));

        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();

        //temp
        // mapAndSearchBar.getSearchButton().setOnAction(control);
    }

    public static void main(String[] args) {
        launch(args);
    }
}