package com.brestats.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.brestats.model.dao.DAO;
import com.brestats.model.dao.DBCommune;
import com.brestats.model.data.Commune;
import com.brestats.model.data.MapCoordinates;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class MainControl {
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchBar;
    @FXML
    private WebView webView;

    private DBCommune dbCom;
    private WebEngine engine; 
    private Commune selectedCity = null;

    public MainControl() {
        this.dbCom = DAO.DB_COM;
    }

    @FXML
    public void initialize() {
        this.engine = this.webView.getEngine();

        MapCoordinates coords = new MapCoordinates();
        this.engine.load(getClass().getResource("/com/brestats/files/map.html").toString());
        this.engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> obs, State oldV, State newV) {
                if(newV.equals(State.SUCCEEDED)) {
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("invoke", coords);
                }
            }
        });

        ChangeListener<Number> listener = new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
                Commune nearestCity = findNearestCity(coords.getLatitude(), coords.getLongitude());
                searchBar.setText(nearestCity.getNomCommune());
                selectedCity = nearestCity;
            }

            private Commune findNearestCity(double lat, double lng) {
                ArrayList<Commune> res = null;
                try {
                    //Get the nearest city by ordering the by their distance with the marker
                    res = dbCom.selectQuery("SELECT * FROM commune ORDER BY 12742*ASIN(SQRT(POWER(SIN((" + lat + " - latitude)/2), 2)+COS(" + lat + ")*COS(latitude)*POWER(SIN((" + lng + " - longitude)/2), 2))) LIMIT 1;");
                } catch (Exception e) {
                    System.out.println("Unexpected exception");
                    e.printStackTrace();
                }
                return res.get(0);
            }
        };

        coords.getLatitudeProperty().addListener(listener);
        coords.getLongitudeProperty().addListener(listener);

    }

    @FXML
    public void handleSearch(Event ev) {
        if(this.selectedCity != null) {
            try {
                Parent results = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Results.fxml"));
                Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
                stage.setScene(new Scene(results));
                System.out.println("change");
            } catch(IOException ex) {
                System.out.println("Cannot change scene");
                ex.printStackTrace();
            }
        } else {
            Alert errorAlert = new Alert(AlertType.ERROR, "Please enter the name of a city.", ButtonType.OK);
            errorAlert.show();
        }
    }

    @FXML
    public void handleEnter(KeyEvent ev) {
        if(ev.getCode().equals(KeyCode.ENTER)) { //If enter is pressed, start the research up
            handleSearch(ev);
        } else {
            Platform.runLater(new Runnable() { //To get the new search, otherwise we get the previous one
                public void run() {
                    if(searchBar.getText().length() >= 3) {
                        String searchQuery = searchBar.getText();
                        try {
                            ArrayList<Commune> res = dbCom.selectQuery("SELECT * FROM commune WHERE nomCommune LIKE '" + searchQuery + "%' OR idCommune LIKE '" + searchQuery + "%';");
                            ArrayList<Double> latitudes = new ArrayList<Double>();
                            ArrayList<Double> longitudes = new ArrayList<Double>();
                            
                            for (Commune commune : res) {
                                latitudes.add(commune.getLatitude());
                                longitudes.add(commune.getLongitude());
                            }
                            engine.executeScript("setMarkers(" + transformToJavascriptArray(latitudes) + "," + transformToJavascriptArray(longitudes) + ")");
                            
                            selectedCity = res.get(0);
                        } catch(SQLException ex) {
                            System.out.println("Unexpected exception with query : " + searchQuery);
                        }
                    } else {
                        engine.executeScript("setMarkers([], [])");
                    }
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
            });
        }
    }
}
