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
import netscape.javascript.JSObject;

public class SearchMap extends VBox {

    private TextField searchBar;
    private WebView mapView;

    public SearchMap() {

        searchBar = new TextField();
        searchBar.setPromptText("Search for a city or region");
        searchBar.setFocusTraversable(false);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> searchLocation());

        HBox searchBox = new HBox(searchBar, searchButton);
        searchBox.setSpacing(10);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        searchBar.setMaxWidth(Double.MAX_VALUE);

        mapView = new WebView();
        final WebEngine webEngine = mapView.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                initializeMap(webEngine);
            }
        });

        getChildren().addAll(searchBox); // Initially add only the search box
        setAlignment(Pos.CENTER);
        setSpacing(10);

        webEngine.load("https://maps.googleapis.com/maps/api/js?key=AIzaSyCrL5cCij6GXWqZOlwaOvyKBeJkuBDt2-I&callback=initMap"); 
    }

    private void initializeMap(WebEngine webEngine) {
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("initMap", (Runnable) () -> {
            Platform.runLater(() -> {
                JSObject mapOptions = (JSObject) webEngine.executeScript("new Object()");
                mapOptions.setMember("center", createLatLng(47.6563, -2.7743)); 
                mapOptions.setMember("zoom", 12);

                JSObject map = (JSObject) webEngine.executeScript("new google.maps.Map(document.getElementById('map'), mapOptions)");
                JSObject markerOptions = (JSObject) webEngine.executeScript("new Object()");
                markerOptions.setMember("position", createLatLng(47.6563, -2.7743)); 
                markerOptions.setMember("map", map);
                markerOptions.setMember("title", "Vannes, Brittany, France");
                webEngine.executeScript("new google.maps.Marker(markerOptions)");
                getChildren().add(mapView); 
            });
        });
    }

    private void searchLocation() {
        String query = searchBar.getText();
        if (!query.isEmpty()) {
            // Geocode the query using the Google Maps API (requires additional implementation)
        }
    }

    private JSObject createLatLng(double lat, double lng) {
        JSObject latLng = (JSObject) mapView.getEngine().executeScript("new google.maps.LatLng()");
        latLng.setMember("lat", lat);
        latLng.setMember("lng", lng);
        return latLng;
    }
}
