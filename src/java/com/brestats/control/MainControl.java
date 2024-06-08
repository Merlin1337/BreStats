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

/**
 * Controller of Main.fxml view
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
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

    /**
     * Initialize all needed attributes and listeners. Called when the view is loaded.
     */
    @FXML
    public void initialize() {
        this.popupMouseEventHandler = new PopupMouseEventHandler(); //private class handling popup-related events
        this.dbCom = DAO.DB_COM; //connection to the database
        this.searchProps = new Popup(); //Search proposals from user's query
        this.gridProps = new GridPane(); //Grid pane used in seachProps
        this.gridProps.getStyleClass().add("searchProps");
        this.gridProps.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
        searchProps.setAnchorLocation(AnchorLocation.CONTENT_TOP_LEFT); //to place the popup in the scene
        searchProps.setAutoHide(true); //Auto hide popup when clicked elsewhere
        searchProps.getContent().add(gridProps);

        //Loading the map
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

        //Marker change event = when latitude or longitude change
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

    /**
     * Handle the switch of view when search button is clicked. Called from Main.fxml and from {@link #handleKeyPressed(KeyEvent)}, hence ev is {@link Event} typed
     * @param ev click event 
     */
    @FXML
    public void handleSearch(Event ev) {
        if(this.selectedCity != null) {
            try {
                Parent results = FXMLLoader.load(getClass().getResource("/com/brestats/pages/Results.fxml"));
                Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
                stage.setScene(new Scene(results));
                System.out.println(selectedCity);
                System.out.println("change");
            } catch(IOException ex) {
                System.out.println("Cannot change scene");
                ex.printStackTrace();
            }
        } else { //if no city is selected, meaning search bar is empty, show an alert window
            Alert errorAlert = new Alert(AlertType.ERROR, "Please enter the name of a city.", ButtonType.OK);
            errorAlert.show();
        }
    }

    /**
     * Handle the display of markers and search proposal events when a key is pressed. Called from Main.fxml
     * @param ev a key event
     */
    @FXML
    public void handleKeyPressed(KeyEvent ev) {
        // List of ignored keys to send query to database
        List<KeyCode> ignoredKeysArray = Arrays.asList(KeyCode.ALT, KeyCode.CONTROL, KeyCode.SHIFT, KeyCode.CAPS, KeyCode.ESCAPE, KeyCode.TAB, KeyCode.UNDEFINED, KeyCode.LEFT, KeyCode.RIGHT);
        if(ev.getCode().equals(KeyCode.ENTER)) { //If enter is pressed, start the research
            handleSearch(ev);
        } else if(ev.getCode().equals(KeyCode.UP)) { //Select previous city among proposals
            popupMouseEventHandler.colorPreviousLabel();
            this.searchBar.setText(selectedCity.getNomCommune());
        } else if(ev.getCode().equals(KeyCode.DOWN)) {//Select next city among proposals
            popupMouseEventHandler.colorNextLabel();
            this.searchBar.setText(selectedCity.getNomCommune());
        } else if(!ignoredKeysArray.contains(ev.getCode()) && !ev.isAltDown() && !ev.isControlDown() && !ev.isShiftDown()) { //else search with new query
            Platform.runLater(new Runnable() { //To get the new search, otherwise we get the previous one
                public void run() {
                    String searchQuery = searchBar.getText();
                    if(searchQuery.length() >= 3) { //From 3 characters, to avoid too big query results
                        try {
                            ArrayList<Commune> res = dbCom.selectQuery("SELECT * FROM commune WHERE nomCommune LIKE '" + searchQuery + "%' OR idCommune LIKE '" + searchQuery + "%';");
                            ArrayList<Double> latitudes = new ArrayList<Double>();
                            ArrayList<Double> longitudes = new ArrayList<Double>();
                            gridProps.getChildren().clear(); //reset search proposals

                            popupMouseEventHandler.setCitiesArray(res);

                            
                            for (Commune commune : res) {
                                latitudes.add(commune.getLatitude());
                                longitudes.add(commune.getLongitude());

                                if(gridProps.getChildren().size() < 5) {
                                    //Add a proposal in popup
                                    Label cityNameLabel = new Label(commune.getNomCommune());
                                    cityNameLabel.getStyleClass().add("cityNameLabel");
                                    Pane cityNamePane = new Pane(cityNameLabel);
                                    cityNamePane.getStyleClass().add("cityNamePane");
                                    cityNamePane.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
                                    cityNamePane.setOnMouseEntered(popupMouseEventHandler);
                                    cityNamePane.setOnMouseExited(popupMouseEventHandler);
                                    cityNamePane.setOnMouseClicked(popupMouseEventHandler);

                                    gridProps.add(cityNamePane, 0, gridProps.getChildren().size());
                                }
                            }
                            //Place markers on the map (see src/resources/com/brestats/files/script.js)
                            engine.executeScript("setMarkers(" + transformToJavascriptArray(latitudes) + "," + transformToJavascriptArray(longitudes) + ")");
                            
                            if(res.size() > 0) {
                                //set the 1st marker color to blue
                                selectedCity = res.get(0);
                                gridProps.getChildren().get(0).setStyle("-fx-background-color: cornflowerblue;");
                                engine.executeScript("setBlueMarker(" + selectedCity.getLatitude() + "," + selectedCity.getLongitude() + ")");
                                
                                //display the popup under the search bar if there is at least a city in results
                                Bounds searchBarBounds = searchBar.localToScreen(searchBar.getBoundsInLocal());
                                searchProps.sizeToScene();
                                searchProps.show(searchBar, searchBarBounds.getMinX(), searchBarBounds.getMaxY());
                            }
                        } catch(SQLException ex) {
                            System.out.println("Unexpected exception with query : " + searchQuery);
                        }
                    } else { //if there are less than 3 characters typed in the search bar
                        //remove markers and hide popup
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
            System.out.println(ev.getEventType().getName());
            if(ev.getEventType().getName().equals("MOUSE_ENTERED")) {
                //Color the selected label
                Pane pane = (Pane) ev.getSource();
                Commune com = this.cities.get(pane.getParent().getChildrenUnmodifiable().indexOf(pane));
                
                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
                
                pane.setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + com.getLatitude() + "," + com.getLongitude() + ")");
                
                this.coloredLabelInd = gridProps.getChildren().indexOf(ev.getSource());
            } else if(ev.getEventType().getName().equals("MOUSE_CLICKED")) {
                Pane clickedPane = (Pane) ev.getSource();
                int paneInd = ((GridPane) clickedPane.getParent()).getChildren().indexOf(clickedPane);
                selectedCity = this.cities.get(paneInd);
            }
        }

        public void colorNextLabel() {
            if(this.cities != null) {
                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                if(this.coloredLabelInd < 4) {
                    this.coloredLabelInd++;
                } else {
                    this.coloredLabelInd = 0;
                }

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + "," + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
                selectedCity = this.cities.get(coloredLabelInd);
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
                selectedCity = this.cities.get(coloredLabelInd);
            }
        }
    }
}