package com.brestats.control;

import com.brestats.view.Results;
import com.brestats.view.SearchMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller implements EventHandler<ActionEvent> {
    private Stage stage;
    private SearchMap searchMap;

    // public Controller(Stage stage, SearchMap searchMap) {
    //     this.searchMap = searchMap;
    //     this.stage = stage;
    // }
    
    public void handle(ActionEvent e) {
        System.out.println("change");
        this.stage.setScene(new Scene(new Results()));
    }
}
