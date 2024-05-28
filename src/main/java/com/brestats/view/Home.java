package com.brestats.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Home extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // TopBar1 (from your code)
        TopBar1 topBar = new TopBar1();
        root.setTop(topBar);

        // Map and Search Bar
        SearchMap mapAndSearchBar = new SearchMap();
        BorderPane.setMargin(mapAndSearchBar, new Insets(20, 100, 30, 100)); // Add margin here
        root.setCenter(mapAndSearchBar);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Bre'Stats");
        primaryStage.getIcons().add(new Image(getClass().getResource("/com/brestats/files/img/favicon.png").toExternalForm()));

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
