package com.brestats;

import com.brestats.control.Controller;
import com.brestats.model.dao.ConnectDB;
import com.brestats.model.data.Departement;
import com.brestats.view.SearchMap;
import com.brestats.view.TopBar1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        new ConnectDB<Departement>() {
            protected Departement constructor(String... args) {
                Departement ret = null;
                if(args.length == 3) {
                    try {
                        int id = Integer.parseInt(args[0]);
                        String name = args[1];
                        double inves = Double.parseDouble(args[2]);

                        ret = new Departement(id, name, inves);
                    } catch(NumberFormatException e) {
                        throw new IllegalArgumentException("Bad argument type");
                    }
                } else {
                    throw new IllegalArgumentException("Bad amount of arguments");
                }
                return ret;
            }
        };
        SearchMap mapAndSearchBar = new SearchMap();
        Controller control = new Controller(primaryStage, mapAndSearchBar);

        BorderPane root = new BorderPane();

        // TopBar1 (from your code)
        TopBar1 topBar = new TopBar1();
        root.setTop(topBar);

        // Map and Search Bar
        BorderPane.setMargin(mapAndSearchBar, new Insets(20, 100, 30, 100)); // Add margin here
        root.setCenter(mapAndSearchBar);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
        primaryStage.setTitle("Bre'Stats");
        primaryStage.getIcons().add(new Image(getClass().getResource("/com/brestats/files/img/favicon.png").toExternalForm()));

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        //temp
        mapAndSearchBar.getSearchButton().setOnAction(control);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
