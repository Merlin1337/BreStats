package com.brestats.control;

import java.io.IOException;
import java.util.ArrayList;

import com.brestats.model.data.Commune;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Controller of Results.fxml view
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class ResultsControl {
    @FXML
    private GridPane cityLabelsGrid;
    @FXML
    private WebView webView;
    @FXML
    private Button addNewCity;

    private WebEngine engine;
    private ArrayList<Commune> selectedCities;
    private ArrayList<Label> cityLabels;

    public ResultsControl() {
        this.selectedCities = new ArrayList<Commune>();
    }

    @FXML
    public void initialize() {
        this.cityLabels = new ArrayList<Label>();

        this.engine = this.webView.getEngine();

        this.engine.load(getClass().getResource("/com/brestats/files/map.html").toString());
        this.engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> obs, State oldV, State newV) {
                if(newV.equals(State.SUCCEEDED)) {
                    engine.executeScript("map.setZoom(7.5)");

                    ArrayList<Double> latitudes = new ArrayList<Double>();
                    ArrayList<Double> longitudes = new ArrayList<Double>();
                    for(Commune city : selectedCities) {
                        latitudes.add(city.getLatitude());
                        longitudes.add(city.getLongitude());
                    }

                    engine.executeScript("setRedMarkers(" + transformToJavascriptArray(latitudes) + "," + transformToJavascriptArray(longitudes) +")");
                }
            }
        });
    }

    @FXML
    public void handleAddNewCity(ActionEvent ev) {
        try {
            FXMLLoader mainView = new FXMLLoader(getClass().getResource("/com/brestats/pages/Main.fxml"));
            Parent main = mainView.load();
            Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
            stage.setScene(new Scene(main));
            
            ((MainControl) mainView.getController()).setPreviousSelectedCities(selectedCities);
        } catch(IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    public void addSelectedCity(Commune city) {
        this.selectedCities.add(city);

        BorderPane borderPane = new BorderPane();
        Label numLabel = new Label(Integer.toString(this.cityLabels.size()+1));
        Label cityName = new Label(city.getNomCommune());
        HBox labelBox = new HBox(numLabel, cityName);

        numLabel.getStyleClass().add("numero");
        cityName.getStyleClass().add("ville");
        labelBox.setSpacing(15);
        borderPane.setCenter(labelBox);

        cityName.setAlignment(Pos.CENTER);
        labelBox.setAlignment(Pos.CENTER);

        this.cityLabelsGrid.add(borderPane, 0, this.cityLabels.size());
        this.cityLabels.add(cityName);
    }

    private String transformToJavascriptArray(ArrayList<Double> arr) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");

        for (Double db : arr) {
            sb.append(db.toString()).append(", ");
        }

        if (sb.length() > 1)
            sb.replace(sb.length() - 2, sb.length(), "");

        sb.append("]");

        return sb.toString();
    }
}
