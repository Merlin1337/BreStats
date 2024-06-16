package com.brestats.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.print.DocFlavor.STRING;

import com.brestats.model.dao.DAO;
import com.brestats.model.dao.DBCommune;
import com.brestats.model.data.Commune;

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
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.PopupWindow.AnchorLocation;
import netscape.javascript.JSObject;



/**
 * Controller of Main.fxml view
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class MainControl {
    /** The search button which load the result view */
    @FXML
    private Button searchButton;
    /** the center search bar */
    @FXML
    private TextField searchBar;
    /** The web view containing the map */
    @FXML
    private WebView webView;
    @FXML 
    private Button graphButton;
    @FXML 
    private HBox graphBox;

    @FXML
    private ImageView graphPane;

    @FXML
    private Label DescribLab;

    @FXML
    private Label DescribLab1;
    @FXML
    private Label DescribLab2;

    /** The web view engine, for the map and the link with javascript */
    private WebEngine engine;
    /** The popup window, displaying serach proposals */
    private Popup searchProps;
    /** The scroll pane included in the popup */
    private ScrollPane scrollPane;
    /** The grid pane included in the scroll pane */
    private GridPane gridProps;
    /** DAO for commune table */
    private DBCommune dbCom;
    /** Selected city to set in ResultsControl */
    private Commune selectedCity;
    /** An instance of the class handling all mouse event fired by the popup */
    private PopupMouseEventHandler popupMouseEventHandler;
    /**
     * All previously selected cities, displayed with red markers and excluded from
     * search results
     */
    private ArrayList<Commune> previousSelectedCities;

    /**
     * Construct the controller by initialising attributes
     */
    public MainControl() {
        this.popupMouseEventHandler = new PopupMouseEventHandler(); // private class handling popup-related events
        this.dbCom = DAO.DB_COM; // connection to the database
        this.searchProps = new Popup(); // Search proposals from user's query
        this.selectedCity = null;
        this.scrollPane = new ScrollPane();
        this.gridProps = new GridPane(); // Grid pane used in seachProps
        this.previousSelectedCities = new ArrayList<Commune>();
        
    }

    /**
     * Setter for the previousSelectedCities attributes, representing cities sent by
     * ResultsControl
     * 
     * @param cities The selected cities' array list
     */
    public void setPreviousSelectedCities(ArrayList<Commune> cities) {
        if (cities != null) {
            this.previousSelectedCities = cities;
        }
    }

    /**
     * Initialize all needed attributes and listeners. Called when the view is
     * loaded.
     */
    @FXML
    public void initialize() {
        // config popup scroll pane
        this.scrollPane.setMaxHeight(134); // approximatly 5 proposals
        this.scrollPane.setMinViewportWidth(400);
        this.scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.scrollPane.setStyle("-fx-border-radius: 8px;");

        this.gridProps.getStyleClass().add("searchProps"); // Grid pane used in seachProps
        this.gridProps.getStylesheets().add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
        this.scrollPane.setContent(gridProps);

        searchProps.setAnchorLocation(AnchorLocation.CONTENT_TOP_LEFT); // to place the popup in the scene
        searchProps.setAutoHide(true); // Auto hide popup when clicked elsewhere
        searchProps.getContent().add(scrollPane);

        // Loading the map
        this.engine = this.webView.getEngine();

        MapCoordinates coords = new MapCoordinates();
        this.engine.load(getClass().getResource("/com/brestats/files/map.html").toString());
        this.engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> obs, State oldV, State newV) {
                if (newV.equals(State.SUCCEEDED)) {
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("invoke", coords);

                    engine.executeScript("addMapClickListener()"); // to activate the placement of markers by clicking
                                                                   // on the map

                    // Sets red markers at the position of received cities from ResultsControl
                    ArrayList<Double> latitudes = new ArrayList<Double>();
                    ArrayList<Double> longitudes = new ArrayList<Double>();
                    for (Commune city : previousSelectedCities) {
                        latitudes.add(city.getLatitude());
                        longitudes.add(city.getLongitude());
                    }

                    engine.executeScript("setRedMarkers(" + transformToJavascriptArray(latitudes) + ","
                            + transformToJavascriptArray(longitudes) + ")");
                }
            }
        });

        // Marker change event = when latitude or longitude change
        ChangeListener<Number> listener = new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
                Commune nearestCity = findNearestCity(coords.getLatitude(), coords.getLongitude());
                searchBar.setText(nearestCity.getNomCommune());
                selectedCity = nearestCity;
            }

            private Commune findNearestCity(double lat, double lng) {
                ArrayList<Commune> res = null;
                try {
                    // Get the nearest city by ordering the by their distance with the marker
                    res = dbCom.selectQuery("SELECT * FROM commune ORDER BY 12742*ASIN(SQRT(POWER(SIN((" + lat
                            + " - latitude)/2), 2)+COS(" + lat + ")*COS(latitude)*POWER(SIN((" + lng
                            + " - longitude)/2), 2))) LIMIT 1;");
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
     * Handle the switch of view when search button is clicked. Called from
     * Main.fxml, from {@link #handleKeyPressed(KeyEvent)}
     * and from {@link #handleMapDoubleClick(MouseEvent)}, hence ev is {@link Event}
     * typed
     * 
     * @param ev click event
     */
    @FXML
    public void handleSearch(Event ev) {
        if (this.selectedCity != null) {
            // get window of event's source
            Window window = ((Node) ev.getSource()).getScene().getWindow();
            Stage stage;
            // if the window is popup, get the owner node's window
            if (window.getClass().getName().equals("javafx.stage.Popup")) {
                stage = (Stage) ((Popup) window).getOwnerNode().getScene().getWindow();
            } else { // else keep the window as stage (see window heritage tree)
                stage = (Stage) window;
            }
            try {
                FXMLLoader resultsFXML = new FXMLLoader(getClass().getResource("/com/brestats/pages/Results.fxml"));
                Parent results = resultsFXML.load();
                stage.setScene(new Scene(results, ((Node) ev.getSource()).getScene().getWidth(),
                        ((Node) ev.getSource()).getScene().getHeight()));
                ResultsControl control = (ResultsControl) resultsFXML.getController();

                for (Commune commune : this.previousSelectedCities) {
                    control.addSelectedCity(commune);
                }

                control.addSelectedCity(selectedCity);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else { // if no city is selected, meaning search bar is empty, show an alert window
            Alert errorAlert = new Alert(AlertType.ERROR, "Please enter at least 3 characters for the name of a city.",
                    ButtonType.OK);
            errorAlert.show();
        }
    }

    /**
     * Handle the display of markers and search proposal events when a key is
     * pressed. Called from Main.fxml
     * 
     * @param ev a key event
     */
    @FXML
    public void handleKeyPressed(KeyEvent ev) {
        // List of ignored keys to send query to database
        List<KeyCode> ignoredKeysArray = Arrays.asList(KeyCode.ALT, KeyCode.CONTROL, KeyCode.SHIFT, KeyCode.CAPS,
                KeyCode.ESCAPE, KeyCode.TAB, KeyCode.UNDEFINED, KeyCode.LEFT, KeyCode.RIGHT);
        if (ev.getCode().equals(KeyCode.ENTER)) { // If enter is pressed, start the research
            handleSearch(ev);
        } else if (ev.getCode().equals(KeyCode.UP)) { // Select previous city among proposals
            popupMouseEventHandler.colorPreviousLabel();
            this.searchBar.setText(selectedCity.getNomCommune());
        } else if (ev.getCode().equals(KeyCode.DOWN)) {// Select next city among proposals
            popupMouseEventHandler.colorNextLabel();
            this.searchBar.setText(selectedCity.getNomCommune());
        } else if (!ignoredKeysArray.contains(ev.getCode()) && !ev.isAltDown() && !ev.isControlDown()
                && !ev.isShiftDown()) { // else search with new query
            Platform.runLater(new Runnable() { // To get the new search, otherwise we get the previous one
                public void run() {
                    if (searchBar.getText().length() >= 3) { // From 3 characters, to avoid too big query results
                        String previousSelectedCitiesString = "(";
                        for (int i = 0; i < previousSelectedCities.size() - 1; i++) {
                            previousSelectedCitiesString += previousSelectedCities.get(i).getId() + ",";
                        }
                        if (previousSelectedCities.size() > 0) {
                            previousSelectedCitiesString += previousSelectedCities
                                    .get(previousSelectedCities.size() - 1).getId() + ")";
                        } else {
                            previousSelectedCitiesString += "0)"; // No city have an id of 0, so all will be returned
                        }

                        String searchQuery = "SELECT * FROM commune WHERE (nomCommune LIKE '" + searchBar.getText()
                                + "%' OR idCommune LIKE '" + searchBar.getText() + "%') AND idCommune NOT IN "
                                + previousSelectedCitiesString + ";";

                        try {
                            ArrayList<Commune> res = dbCom.selectQuery(searchQuery);
                            ArrayList<Double> latitudes = new ArrayList<Double>();
                            ArrayList<Double> longitudes = new ArrayList<Double>();
                            gridProps.getChildren().clear(); // reset search proposals

                            popupMouseEventHandler.setCitiesArray(res);

                            for (Commune commune : res) {
                                latitudes.add(commune.getLatitude());
                                longitudes.add(commune.getLongitude());

                                // Add a proposal in popup
                                Label cityNameLabel = new Label(commune.getNomCommune());
                                cityNameLabel.getStyleClass().add("cityNameLabel");
                                Pane cityNamePane = new Pane(cityNameLabel);
                                cityNamePane.getStyleClass().add("cityNamePane");
                                cityNamePane.getStylesheets()
                                        .add(getClass().getResource("/com/brestats/files/style.css").toExternalForm());
                                cityNamePane.setOnMouseEntered(popupMouseEventHandler);
                                cityNamePane.setOnMouseExited(popupMouseEventHandler);
                                cityNamePane.setOnMouseClicked(popupMouseEventHandler);

                                gridProps.add(cityNamePane, 0, gridProps.getChildren().size());
                            }
                            // Place markers on the map (see src/resources/com/brestats/files/script.js)
                            engine.executeScript("setMarkers(" + transformToJavascriptArray(latitudes) + ","
                                    + transformToJavascriptArray(longitudes) + ")");

                            if (res.size() > 0) {
                                // set the 1st marker color to blue
                                selectedCity = res.get(0);
                                gridProps.getChildren().get(0).setStyle("-fx-background-color: cornflowerblue;");
                                engine.executeScript("setBlueMarker(" + selectedCity.getLatitude() + ","
                                        + selectedCity.getLongitude() + ")");

                                // display the popup under the search bar if there is at least a city in results
                                Bounds searchBarBounds = searchBar.localToScreen(searchBar.getBoundsInLocal());
                                searchProps.sizeToScene();
                                searchProps.show(searchBar, searchBarBounds.getMinX(), searchBarBounds.getMaxY());
                                scrollPane.setVvalue(0);
                            }
                        } catch (SQLException ex) {
                            System.out.println("Unexpected exception with query : " + searchQuery);
                        }
                    } else { // if there are less than 3 characters typed in the search bar
                        // remove markers and hide popup
                        engine.executeScript("setMarkers([], [])");
                        popupMouseEventHandler.setCitiesArray(null);
                        selectedCity = null;
                        searchProps.hide();
                    }
                }
            });
        }

    }

    /**
     * Handle a double click on the map. Called from Main.fxml
     * 
     * @param ev A mouse event
     */
    @FXML
    public void handleMapDoubleClick(MouseEvent ev) {
        if (ev.getButton().equals(MouseButton.PRIMARY)) {
            if (ev.getClickCount() == 2) { // if doubled clicked on map
                handleSearch(ev); // start the research
            }
        }
    }

    @FXML
    public void switchGraph(Event ev){
        boolean isWebView = webView.isVisible();
        try {
            
            if (!isWebView) {
                webView.setVisible(true);
                graphBox.setVisible(false);
                graphPane.setVisible(false);
                DescribLab.setVisible(false);
                DescribLab1.setVisible(false);
                DescribLab2.setVisible(false);
                graphButton.setText("Graphe");

            }else{
                webView.setVisible(false);
                graphBox.setVisible(true);
                graphPane.setVisible(true);
                DescribLab.setVisible(true);
                DescribLab1.setVisible(true);
                DescribLab2.setVisible(true);
                graphButton.setText("Carte");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Transform a java{@link ArrayList} object to javascript array
     * 
     * @param arr the java array object
     * @return the javascript object as {@link String}
     */
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

    /**
     * A class represeting all the popup features, like selecting city label with
     * cursor or arrow keys
     * 
     * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
     *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
     */
    private class PopupMouseEventHandler implements EventHandler<MouseEvent> {
        /** Cities' list, displayed inside the popup */
        private ArrayList<Commune> cities;
        /** The index of the current colored label in popup */
        private int coloredLabelInd = 0;

        /** 
         * Initiate an instance of PopupMouseEventHandler, with {@link #cities} as {@code null}
         */
        public PopupMouseEventHandler() {
            this.cities = null;
        }

        /**
         * Setter for the {@link #cities} attribute
         * @param cities List of suggested cities
         */
        public void setCitiesArray(ArrayList<Commune> cities) {
            this.cities = cities;
        }

        public void handle(MouseEvent ev) {
            if (ev.getEventType().getName().equals("MOUSE_ENTERED")) {
                // Color the selected label
                Pane pane = (Pane) ev.getSource();
                Commune com = this.cities.get(pane.getParent().getChildrenUnmodifiable().indexOf(pane));

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + ","
                        + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                pane.setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + com.getLatitude() + "," + com.getLongitude() + ")");

                this.coloredLabelInd = gridProps.getChildren().indexOf(ev.getSource());
            } else if (ev.getEventType().getName().equals("MOUSE_CLICKED")) {
                Pane clickedPane = (Pane) ev.getSource();
                int paneInd = ((GridPane) clickedPane.getParent()).getChildren().indexOf(clickedPane);
                selectedCity = this.cities.get(paneInd);
                handleSearch(ev);
            }
        }

        /**
         * Procedure coloring the next label in proposals
         */
        public void colorNextLabel() {
            if (this.cities != null) {
                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + ","
                        + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                if (this.coloredLabelInd < this.cities.size() - 1) {
                    this.coloredLabelInd++;
                } else {
                    this.coloredLabelInd = 0;
                }

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + ","
                        + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
                selectedCity = this.cities.get(coloredLabelInd);

                // Set the scroll position when using down arrow key
                if (scrollPane.getVvalue() * (this.cities.size() - 5) <= this.coloredLabelInd - 4) {
                    scrollPane.setVvalue((double) (this.coloredLabelInd - 4) / (this.cities.size() - 5));
                } else if (this.coloredLabelInd == 0) {
                    scrollPane.setVvalue(0);
                }
            }
        }

        /**
         * Procedure coloring the previous label in proposals
         */
        public void colorPreviousLabel() {
            if (this.cities != null) {
                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: transparent;");
                engine.executeScript("setGreyMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + ","
                        + this.cities.get(this.coloredLabelInd).getLongitude() + ")");

                if (this.coloredLabelInd > 0) {
                    this.coloredLabelInd--;
                } else {
                    this.coloredLabelInd = this.cities.size() - 1;
                }

                gridProps.getChildren().get(this.coloredLabelInd).setStyle("-fx-background-color: cornflowerblue;");
                engine.executeScript("setBlueMarker(" + this.cities.get(this.coloredLabelInd).getLatitude() + ","
                        + this.cities.get(this.coloredLabelInd).getLongitude() + ")");
                selectedCity = this.cities.get(coloredLabelInd);

                // Set the scroll position when using the up arrow key
                if (scrollPane.getVvalue() * (this.cities.size() - 5) >= this.coloredLabelInd) {
                    scrollPane.setVvalue((double) (this.coloredLabelInd) / (this.cities.size() - 5));
                } else if (this.coloredLabelInd == this.cities.size() - 1) {
                    scrollPane.setVvalue(1);
                }
            }
        }
    }
}