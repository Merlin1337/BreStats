package com.brestats.view;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;

public class SearchMap extends VBox {
    private TextField searchBar;
    private WebView mapView;

    public SearchMap() {
        searchBar = new TextField();
        searchBar.getStyleClass().add("text-field");
        searchBar.setFocusTraversable(false);
        searchBar.setPromptText("Rechercher une commune ou un département ...");
        Button searchButton = new Button("Rechercher");
        searchButton.getStyleClass().add("button");

        HBox searchBox = new HBox(searchBar, searchButton);
        searchBox.setSpacing(10);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        searchBar.setMaxWidth(Double.MAX_VALUE);

        mapView = new WebView();
        final WebEngine webEngine = mapView.getEngine();
        webEngine.loadContent(getGoogleMapHTML());

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> obs, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            getChildren().add(mapView);
                            setAlignment(Pos.CENTER);
                            setSpacing(10);
                        }
                    });
                }
            }
        });

        getChildren().addAll(searchBox, mapView);
        setAlignment(Pos.CENTER);
        setSpacing(10);
    }

    private String getGoogleMapHTML() {
        return "";
    }
}