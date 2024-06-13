package com.brestats.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
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
    private BarChart<String, Double> averageChart;
    // @FXML
    // private LineChart evolutionChart;

    private DBValeursCommuneAnnee dbValeursCommuneAnnee = DAO.DB_VAL;
    private WebEngine engine;
    private ArrayList<Commune> selectedCities;
    private ArrayList<DonneesAnnuelles> averageCityData;
    private ArrayList<Label> cityLabels;
    private ArrayList<TableData> tableRows;
    private ObservableList<Series<String, Double>> seriesList;
    private Series<String, Double> populationSeries;
    private Series<String, Double> housesSeries;
    private Series<String, Double> apartementSeries;
    private Series<String, Double> costSeries;
    private Series<String, Double> m2CostSeries;
    private Series<String, Double> surfaceSeries;
    private Series<String, Double> spendingSeries;
    private Series<String, Double> budgetSeries;

    public ResultsControl() {
        this.selectedCities = new ArrayList<Commune>();
        this.averageCityData = new ArrayList<DonneesAnnuelles>();
        this.tableRows = new ArrayList<TableData>();

        this.populationSeries = new Series<String, Double>();
        this.housesSeries = new Series<String, Double>();
        this.apartementSeries = new Series<String, Double>();
        this.costSeries = new Series<String, Double>();
        this.m2CostSeries = new Series<String, Double>();
        this.surfaceSeries = new Series<String, Double>();
        this.spendingSeries = new Series<String, Double>();
        this.budgetSeries = new Series<String, Double>();

        this.populationSeries.setName("Population");
        this.housesSeries.setName("Nombre de maisons");
        this.apartementSeries.setName("Nombre d'appartements");
        this.costSeries.setName("Prix moyen");
        this.m2CostSeries.setName("Prix moyen du m²");
        this.surfaceSeries.setName("Surface moyenne");
        this.spendingSeries.setName("Dépenses culturelles");
        this.budgetSeries.setName("Budget moyen");

        this.seriesList = FXCollections.observableList(List.of(populationSeries, housesSeries, apartementSeries, costSeries, m2CostSeries, surfaceSeries, spendingSeries, budgetSeries));
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

        this.averageChart.setAnimated(true);
        // this.evolutionChart.setAnimated(true);
    }

    @FXML
    public void handleAddNewCity(ActionEvent ev) {
        try {
            FXMLLoader mainView = new FXMLLoader(getClass().getResource("/com/brestats/pages/Main.fxml"));
            Parent main = mainView.load();
            Stage stage= (Stage) ((Node) ev.getSource ()).getScene ().getWindow ();
            stage.setScene(new Scene(main, ((Node) ev.getSource()).getScene().getWidth(), ((Node) ev.getSource()).getScene().getHeight()));
            
            ((MainControl) mainView.getController()).setPreviousSelectedCities(selectedCities);
        } catch(IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }
    
    public void addSelectedCity(Commune city) {
        DonneesAnnuelles data = this.dbValeursCommuneAnnee.getAverageItemByCity(city);
        TableData row = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(), data.getPopulation(), data.getNbMaison(), data.getNbAppart(), data.getPrixMoyen(), data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(), data.getBudgetTotal());

        this.createCityLabel(city);

        this.selectedCities.add(city);
        this.averageCityData.add(data);

        this.tableRows.add(row);

        refreshTable();
        refreshCharts();
    } 

    public void createCityLabel(Commune city) {
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

    public void reloadView() {
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

    public void refreshTable() {
        ObservableList<TableData> dataList = FXCollections.observableArrayList();

        this.tableView.getItems().clear();

        for (TableData row : this.tableRows) {
            dataList.add(row);
            
            ArrayList<TableColumn<TableData, String>> columns = new ArrayList<TableColumn<TableData, String>>();
            for (TableColumn<TableData, ?> column : this.tableView.getColumns()) {
                columns.add((TableColumn<TableData, String>) column);
            }

            for(int i = 0 ; i < columns.size() ; i++) {
                int ind = i; //Cannot use i in anonymous class, because it must be at least effectively final
                columns.get(i).setCellValueFactory(new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<TableData, String> p) {
                        return p.getValue().getProperties().get(ind);
                    }
                });
            }
        }
        this.tableView.setItems(dataList);

    }

    public void refreshCharts() {
        // Data<String, Double> column = new Data<String, Double>("test", Double.valueOf(20));
        // averageChart.setData(FXCollections.observableList(List.of(new Series<String, Double>("category", FXCollections.observableList(List.of(column))))));

        //Average chart (re)loading
        ArrayList<Data<String, Double>> popDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> housesDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> apartsDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> costDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> m2CostDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> surfaceDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> spendingsDataList = new ArrayList<Data<String, Double>>();
        ArrayList<Data<String, Double>> budgetDataList = new ArrayList<Data<String, Double>>();

        for (TableData data : this.tableRows) {
            Data<String, Double> popBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getPopulation()));
            Data<String, Double> housesBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getHouses()));
            Data<String, Double> apartsBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getApartments()));
            Data<String, Double> costBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getCost()));
            Data<String, Double> m2CostBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getM2Cost()));
            Data<String, Double> surfaceBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getSurface()));
            Data<String, Double> spendingsBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getSpendings()));
            Data<String, Double> budgetBarData = new Data<String, Double>(data.getName(), Double.parseDouble(data.getBudget()));

            popDataList.add(popBarData);
            housesDataList.add(housesBarData);
            apartsDataList.add(apartsBarData);
            costDataList.add(costBarData);
            m2CostDataList.add(m2CostBarData);
            surfaceDataList.add(surfaceBarData);
            spendingsDataList.add(spendingsBarData);
            budgetDataList.add(budgetBarData);
        }

        this.populationSeries.setData(FXCollections.observableList(popDataList));
        this.housesSeries.setData(FXCollections.observableList(housesDataList));
        this.apartementSeries.setData(FXCollections.observableList(apartsDataList));
        this.costSeries.setData(FXCollections.observableList(costDataList));
        this.m2CostSeries.setData(FXCollections.observableList(m2CostDataList));
        this.surfaceSeries.setData(FXCollections.observableList(surfaceDataList));
        this.spendingSeries.setData(FXCollections.observableList(spendingsDataList));
        this.budgetSeries.setData(FXCollections.observableList(budgetDataList));

        this.averageChart.setData(seriesList);
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
        private List<StringProperty> properties;
        private StringProperty name;
        private StringProperty dep;
        private StringProperty population;
        private StringProperty houses;
        private StringProperty apartments;
        private StringProperty cost;
        private StringProperty m2Cost;
        private StringProperty surface;
        private StringProperty spendings;
        private StringProperty budget;

        public TableData(String name, String dep, double pop, double houses, double apartments, double cost, double m2Cost, double surface, double spendings, double budget) {
            this(name, dep, Double.toString(pop), Double.toString(houses), Double.toString(apartments), Double.toString(cost), Double.toString(m2Cost), Double.toString(surface), Double.toString(spendings), Double.toString(budget));
        }

        public TableData(String name, String dep, String pop, String houses, String apartments, String cost, String m2Cost, String surface, String spendings, String budget) {
            this.name = new SimpleStringProperty(name);
            this.dep = new SimpleStringProperty(dep);
            this.population = new SimpleStringProperty(pop);
            this.houses = new SimpleStringProperty(houses);
            this.apartments = new SimpleStringProperty(apartments);
            this.cost = new SimpleStringProperty(cost);
            this.m2Cost = new SimpleStringProperty(m2Cost);
            this.surface = new SimpleStringProperty(surface);
            this.spendings = new SimpleStringProperty(spendings);
            this.budget = new SimpleStringProperty(budget);

            this.properties = List.of(this.name, this.dep, this.population, this.houses, this.apartments, this.cost, this.m2Cost, this.surface, this.spendings, this.budget);
        }

        public List<StringProperty> getProperties() {
            return this.properties;
        }

        public String getName() {
            return this.name.get();
        }

        public String getPopulation() {
            return this.population.get();
        }

        public String getHouses() {
            return this.houses.get();
        }

        public String getApartments() {
            return this.apartments.get();
        }

        public String getCost() {
            return this.cost.get();
        }

        public String getM2Cost() {
            return this.m2Cost.get();
        }

        public String getSurface() {
            return this.surface.get();
        }

        public String getSpendings() {
            return this.spendings.get();
        }

        public String getBudget() {
            return this.budget.get();
        }
    }
}
