import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox; // Import for horizontal layout
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class SearchMap extends VBox {
    private TextField searchBar;
    private WebView mapView;

    public SearchMap() {

        searchBar = new TextField();
        searchBar.getStyleClass().add("text-field"); // Add style class
        searchBar.setFocusTraversable(false);
        searchBar.setPromptText("Rechercher une commune ou un département ...");
        Button searchButton = new Button("Rechercher");
        searchButton.getStyleClass().add("button"); // Add style class for the button

        HBox searchBox = new HBox(searchBar, searchButton); 
        searchBox.setSpacing(10);
        HBox.setHgrow(searchBar, Priority.ALWAYS); 
        searchBar.setMaxWidth(Double.MAX_VALUE);

        mapView = new WebView();
        final WebEngine webEngine = mapView.getEngine();
        webEngine.loadContent(getGoogleMapHTML());

        // Load the map only after the web engine is ready
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Platform.runLater(() -> {
                    getChildren().add(mapView); // Add the map view
                    setAlignment(Pos.CENTER);
                    setSpacing(10);
                });
            }
        });

        getChildren().addAll(searchBox, mapView); // Add the search box (HBox)
        setAlignment(Pos.CENTER);
        setSpacing(10);
    }

    private String getGoogleMapHTML() {
        return """
            <!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Leaflet Map</title>
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css" integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ==" crossorigin=""/>
  <style>
    html, body {
      height: 100%;
      margin: 0;
    }
    #mapDiv {
      width: 100%;
      height: 100%;
    }
  </style>
  <script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js" integrity="sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw==" crossorigin=""></script>
</head>
<body>
  <div id="mapDiv"></div>
  <script>
    // position we will use later
    var lat = 48.2; // Latitude to center on Brittany, France
    var lon = -3.0; // Longitude to center on Brittany, France
    // initialize map
    var map = L.map('mapDiv').setView([lat, lon], 8);
    // set map tiles source
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(map);
    // add marker to the map
  </script>
</body>
</html>""";
    }
}