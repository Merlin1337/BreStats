package com.brestats.control;

import com.brestats.model.data.Departement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class AddCityControl {
    @FXML
    private TextField cityField;
    @FXML
    private TextField inseeNumberField;
    @FXML
    private ComboBox<Departement> depComboBox;
    @FXML
    private WebView webView;

    private WebEngine webEngine;

    @FXML
    public void initialize() {
        this.webEngine = webView.getEngine();

        MapCoordinates coords = new MapCoordinates();
        this.webEngine.load(getClass().getResource("/com/brestats/files/map.html").toExternalForm());
        this.webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue<? extends State> obs, State oldV, State newV) {
                if (newV.equals(State.SUCCEEDED)) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("invoke", coords);
                    webEngine.executeScript("addMapClickListener()"); // to activate the placement of markers by
                                                                      // clicking on the map

                    webEngine.executeScript("map.setZoom(7)");
                }
            }

        });
    }

    @FXML
    public void handleAdd(ActionEvent ev) {

    }
}
