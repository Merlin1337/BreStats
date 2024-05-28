package com.brestats.view;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;


public class Results extends BorderPane {

    public Results() {
        // TopBar2 (from your code)
        TopBar2 topBar = new TopBar2();
        this.setTop(topBar);

        // Map and Search Bar
        StatsResult statsResult = new StatsResult();
        BorderPane.setMargin(statsResult, new Insets(20, 20, 30, 20)); // Add margin here
        this.setCenter(statsResult);

        this.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
    }
}
