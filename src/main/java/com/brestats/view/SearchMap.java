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
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;

public class SearchMap extends VBox {
    private TextField searchBar;
    private WebView mapView;

    public SearchMap() {
        searchBar = new TextField();
        searchBar.getStyleClass().add("text-field");
        searchBar.setFocusTraversable(false);
        searchBar.setPromptText("Rechercher une commune ou un département ...");
        Button searchButton = new Button("Rechercher");
        searchButton.getStyleClass().add("button");

        HBox searchBox = new HBox(searchBar, searchButton);
        searchBox.setSpacing(10);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        searchBar.setMaxWidth(Double.MAX_VALUE);

        mapView = new WebView();
        final WebEngine webEngine = mapView.getEngine();
        webEngine.loadContent(getGoogleMapHTML());

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> obs, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            getChildren().add(mapView);
                            setAlignment(Pos.CENTER);
                            setSpacing(10);
                        }
                    });
                }
            }
        });

        getChildren().addAll(searchBox, mapView);
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
    var lat = 48.2; 
    var lon = -3.0; 
    var map = L.map('mapDiv').setView([lat, lon], 8);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
    }).addTo(map);
  </script>
</body>
</html>""";
    }
}