package com.brestats.control;

import java.io.IOException;
import java.util.ArrayList;

import com.brestats.model.dao.DAO;
import com.brestats.model.dao.DBValeursCommuneAnnee;
import com.brestats.model.data.Commune;
import com.brestats.model.data.DonneesAnnuelles;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    @FXML
    private TableView<TableData> tableView;
    @FXML
    private TableColumn<TableData, String> cityNameCol;
    @FXML
    private TableColumn<TableData, String> popCol;
    @FXML
    private TableColumn<TableData, String> transportCol;
    @FXML
    private TableColumn<TableData, String> m2CostCol;

    private DBValeursCommuneAnnee dbValeursCommuneAnnee = DAO.DB_VAL;
    private WebEngine engine;
    private ArrayList<Commune> selectedCities;
    private ArrayList<DonneesAnnuelles> averageCityData;
    private ArrayList<Label> cityLabels;

    public ResultsControl() {
        this.selectedCities = new ArrayList<Commune>();
        this.averageCityData = new ArrayList<DonneesAnnuelles>();
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
                    engine.executeScript("map.setZoom(7)");

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
        this.createCityLabel(city);

        this.selectedCities.add(city);
        this.averageCityData.add(this.dbValeursCommuneAnnee.getAverageItemByCity(city));

        refreshTable();
    } 

    private void createCityLabel(Commune city) {
        BorderPane borderPane = new BorderPane();
        Label numLabel = new Label(Integer.toString(this.cityLabels.size()+1));
        Label cityName = new Label(city.getNomCommune());
        ImageView removeIcon = new ImageView(getClass().getResource("/com/brestats/files/img/remove-icon.png").toExternalForm());
        HBox labelBox = new HBox(numLabel, cityName, removeIcon);

        numLabel.getStyleClass().add("numero");
        cityName.getStyleClass().add("ville");
        labelBox.setSpacing(15);
        borderPane.setCenter(labelBox);
        removeIcon.setFitHeight(30);
        removeIcon.setFitWidth(30);
        removeIcon.setStyle("-fx-cursor: hand");

        removeIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent ev) {
                selectedCities.remove(city);
                reloadView();
            }
        });

        cityName.setAlignment(Pos.CENTER);
        labelBox.setAlignment(Pos.CENTER);

        this.cityLabelsGrid.add(borderPane, 0, this.cityLabels.size());
        this.cityLabels.add(cityName);
    }

    private void reloadView() {
        this.cityLabelsGrid.getChildren().clear();
        this.averageCityData.clear();
        this.cityLabels.clear();


        for(Commune city : this.selectedCities) {
            this.createCityLabel(city);
            this.averageCityData.add(this.dbValeursCommuneAnnee.getAverageItemByCity(city));
        }

        refreshTable();

        ArrayList<Double> latitudes = new ArrayList<Double>();
        ArrayList<Double> longitudes = new ArrayList<Double>();
        for(Commune city : selectedCities) {
            latitudes.add(city.getLatitude());
            longitudes.add(city.getLongitude());
        }

        engine.executeScript("setRedMarkers(" + transformToJavascriptArray(latitudes) + "," + transformToJavascriptArray(longitudes) +")");
    }

    private void refreshTable() {
        ObservableList<TableData> dataList = FXCollections.observableArrayList();

        this.tableView.getItems().clear();

        for (DonneesAnnuelles data : this.averageCityData) {
            // double easinessTransport = 

            TableData row = new TableData(data.getLaCom().getNomCommune(), data.getPopulation(), 0, 0);
            dataList.add(row);

            // this.cityNameCol.setCellValueFactory(new PropertyValueFactory<TableData, String>(row.getNameProperty().getName()));
            // this.popCol.setCellValueFactory(new PropertyValueFactory<TableData, String>(row.getPopulationProperty().getName()));
            // this.transportCol.setCellValueFactory(new PropertyValueFactory<TableData, String>(row.getEasinessTransportProperty().getName()));
            // this.m2CostCol.setCellValueFactory(new PropertyValueFactory<TableData, String>(row.getM2CostProperty().getName()));

            this.cityNameCol.setCellValueFactory(new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<TableData,String> d) {
                    return d.getValue().getNameProperty();
                }
            });
            this.popCol.setCellValueFactory(new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<TableData,String> d) {
                    return d.getValue().getPopulationProperty();
                }
            });
            this.transportCol.setCellValueFactory(new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<TableData,String> d) {
                    return d.getValue().getEasinessTransportProperty();
                }
            });
            this.m2CostCol.setCellValueFactory(new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<TableData,String> d) {
                    return d.getValue().getM2CostProperty();
                }
            });
        }
        this.tableView.setItems(dataList);

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


    private class TableData {
        private StringProperty name;
        private StringProperty population;
        private StringProperty easinessTransport;
        private StringProperty m2Cost;

        public TableData(String name, double pop, double transport, double m2Cost) {
            this(name, Double.toString(pop), Double.toString(transport), Double.toString(m2Cost));
        }

        public TableData(String name, String pop, String transport, String m2Cost) {
            this.name = new SimpleStringProperty(name);
            this.population = new SimpleStringProperty(pop);
            this.easinessTransport = new SimpleStringProperty(transport);
            this.m2Cost = new SimpleStringProperty(m2Cost);
        }

        public StringProperty getNameProperty() {
            return this.name;
        }

        public StringProperty getPopulationProperty() {
            return this.population;
        }

        public StringProperty getEasinessTransportProperty() {
            return this.easinessTransport;
        }

        public StringProperty getM2CostProperty() {
            return this.m2Cost;
        } 

        // public String getName() {
        //     return this.name.get();
        // }

        // public String getPopulation() {
        //     return this.population.get();
        // }

        // public String getEasinessTransport() {
        //     return this.easinessTransport.get();
        // }

        // public String getM2Cost() {
        //     return this.m2Cost.get();
        // }
    }
}
