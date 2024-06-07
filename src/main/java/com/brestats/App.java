package com.brestats;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application  {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Main.fxml"));
        
        Scene scene = new Scene(main);
        primaryStage.getIcons().add(new Image(getClass().getResource("/com/brestats/files/img/favicon.png").toExternalForm()));

        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}