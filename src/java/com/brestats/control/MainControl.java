package com.brestats.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.PopupWindow.AnchorLocation;
import netscape.javascript.JSObject;

public class MainControl {
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchBar;
    @FXML
    private WebView webView;

    private WebEngine engine; 
    private Popup searchProps;
    private GridPane gridProps;
    private DBCommune dbCom;
    private Commune selectedCity = null;
    private PopupMouseEventHandler popupMouseEventHandler;


    @FXML
    public void initialize() {
        this.popupMouseEventHandler = new PopupMouseEventHandler();
        this.dbCom = DAO.DB_COM;
        this.searchProps = new Popup();
        this.gridProps = new GridPane();
        this.gridProps.getStyleClass().add("searchProps");
        this.gridProps.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
        searchProps.setAutoHide(true);
        searchProps.setAnchorLocation(AnchorLocation.CONTENT_TOP_LEFT);
        searchProps.getContent().add(gridProps);

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
    public void handleKeyPressed(KeyEvent ev) {
        System.out.println("handle");
        List<KeyCode> ignoredKeysArray = Arrays.asList(KeyCode.ALT, KeyCode.CONTROL, KeyCode.SHIFT, KeyCode.CAPS, KeyCode.ESCAPE, KeyCode.TAB, KeyCode.UNDEFINED, KeyCode.LEFT, KeyCode.RIGHT);
        if(ev.getCode().equals(KeyCode.ENTER)) { //If enter is pressed, start the research up
            handleSearch(ev);
        } else if(ev.getCode().equals(KeyCode.UP)) {
            popupMouseEventHandler.colorPreviousLabel();
        } else if(ev.getCode().equals(KeyCode.DOWN)) {
            System.out.println("test1");
            popupMouseEventHandler.colorNextLabel();
        } else if(!ignoredKeysArray.contains(ev.getCode())); {
            System.out.println("test2");
            String oldSearchQuery = searchBar.getText();
            Platform.runLater(new Runnable() { //To get the new search, otherwise we get the previous one
                public void run() {
                    String searchQuery = searchBar.getText();
                    if(searchQuery.length() >= 3 && !searchQuery.equals(oldSearchQuery)) {
                        try {
                            ArrayList<Commune> res = dbCom.selectQuery("SELECT * FROM commune WHERE nomCommune LIKE '" + searchQuery + "%' OR idCommune LIKE '" + searchQuery + "%';");
                            ArrayList<Double> latitudes = new ArrayList<Double>();
                            ArrayList<Double> longitudes = new ArrayList<Double>();
                            gridProps.getChildren().clear();

                            popupMouseEventHandler.setCitiesArray(res);

                            for (Commune commune : res) {
                                latitudes.add(commune.getLatitude());
                                longitudes.add(commune.getLongitude());

                                if(gridProps.getChildren().size() < 5) {
                                    
                                    Label cityNameLabel = new Label(commune.getNomCommune());
                                    cityNameLabel.getStyleClass().add("cityNameLabel");
                                    Pane cityNamePane = new Pane(cityNameLabel);
                                    cityNamePane.getStyleClass().add("cityNamePane");
                                    cityNamePane.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
                                    cityNamePane.setOnMouseEntered(popupMouseEventHandler);
                                    cityNamePane.setOnMouseExited(popupMouseEventHandler);

                                    gridProps.add(cityNamePane, 0, gridProps.getChildren().size());
                                }
                            }
                            engine.executeScript("setMarkers(" + transformToJavascriptArray(latitudes) + "," + transformToJavascriptArray(longitudes) + ")");
                            
                            if(res.size() > 0) {
                                // System.out.println("show");
                                Bounds searchBarBounds = searchBar.localToScreen(searchBar.getBoundsInLocal());

                                selectedCity = res.get(0);
                                searchProps.sizeToScene();
                                searchProps.show(searchBar, searchBarBounds.getMinX(), searchBarBounds.getMaxY());
                            }
                        } catch(SQLException ex) {
                            System.out.println("Unexpected exception with query : " + searchQuery);
                        }
                    } else {
                        engine.executeScript("setMarkers([], [])");
                        popupMouseEventHandler.setCitiesArray(null);
                        selectedCity = null;
                        searchProps.hide();
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


    private class PopupMouseEventHandler implements EventHandler<MouseEvent> {
        private ArrayList<Commune> cities;
        private int coloredLabelInd = 0;

        public PopupMouseEventHandler() {
            this.cities = null;
        }

        public void setCitiesArray(ArrayList<Commune> cities) {
            this.cities = cities;
        }

        public void handle(MouseEvent ev) {
            if(ev.getEventType().getName().equals("MOUSE_ENTERED")) {
                //Color the selected label
                Pane pane = (Pane) ev.getSource();
                Commune com = this.cities.get(pane.getParent().getChildrenUnmodifiable().indexOf(pane));

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                pane.setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + com.getLatitude() + "," + com.getLongitude() + ")");

                this.coloredLabelInd = gridProps.getChildren().indexOf(ev.getSource());
            }
        }

        public void colorNextLabel() {
            if(this.cities != null) {
                System.out.println(this.coloredLabelInd);
                System.out.println(this.cities.get(this.coloredLabelInd).getLatitude() + " " + this.cities.get(this.coloredLabelInd).getLongitude());

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                if(this.coloredLabelInd < 4) {
                    this.coloredLabelInd++;
                } else {
                    this.coloredLabelInd = 0;
                }

                System.out.println(this.coloredLabelInd);
                System.out.println("setBlueMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
            }
        }

        public void colorPreviousLabel() {
            if(this.cities != null) {
                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                if(this.coloredLabelInd > 0) {
                    this.coloredLabelInd--;
                } else {
                    this.coloredLabelInd = 4;
                }

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
            }
        }
    }
}