package com.brestats;

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application  {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // SearchMap mapAndSearchBar = new SearchMap();
        Parent main = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Main.fxml"));
        // Controller control = new Controller(primaryStage, mapAndSearchBar);

        Scene scene = new Scene(main);
        primaryStage.setTitle("Bre'Stats");
        primaryStage.getIcons().add(new Image(getClass().getResource("/com/brestats/files/img/favicon.png").toExternalForm()));

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        //temp
        // mapAndSearchBar.getSearchButton().setOnAction(control);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
