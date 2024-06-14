package com.brestats.control;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.brestats.model.dao.DAO;
import com.brestats.model.dao.DBAnnee;
import com.brestats.model.dao.DBValeursCommuneAnnee;
import com.brestats.model.data.Annee;
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
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
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
    @FXML
    private LineChart<Double, Double> evolutionChart;
    @FXML
    private NumberAxis evolutionXAxis;
    @FXML
    private NumberAxis evolutionYAxis;

    private DBValeursCommuneAnnee dbValeursCommuneAnnee = DAO.DB_VAL;
    private DBAnnee dbAnnee = DAO.DB_ANNEE;
    private WebEngine engine;
    private ArrayList<Commune> selectedCities;
    private ArrayList<DonneesAnnuelles> averageCityData;
    private ArrayList<Label> cityLabels;
    private ArrayList<TableData> tableRows;

    public ResultsControl() {
        this.selectedCities = new ArrayList<Commune>();
        this.averageCityData = new ArrayList<DonneesAnnuelles>();
        this.tableRows = new ArrayList<TableData>();
    }

    @FXML
    public void initialize() {
        this.cityLabels = new ArrayList<Label>();

        this.engine = this.webView.getEngine();

        this.engine.load(getClass().getResource("/com/brestats/files/map.html").toExternalForm());
        this.engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> obs, State oldV, State newV) {
                if (newV.equals(State.SUCCEEDED)) {
                    engine.executeScript("map.setZoom(7)");

                    ArrayList<Double> latitudes = new ArrayList<Double>();
                    ArrayList<Double> longitudes = new ArrayList<Double>();
                    for (Commune city : selectedCities) {
                        latitudes.add(city.getLatitude());
                        longitudes.add(city.getLongitude());
                    }

                    engine.executeScript("setRedMarkers(" + transformToJavascriptArray(latitudes) + ","
                            + transformToJavascriptArray(longitudes) + ")");
                }
            }
        });

        this.averageChart.setLegendSide(Side.LEFT);
        this.averageChart.setAnimated(false);
        this.evolutionChart.setLegendSide(Side.LEFT);
        this.evolutionChart.setAnimated(false);
    }

    @FXML
    public void handleAddNewCity(ActionEvent ev) {
        try {
            FXMLLoader mainView = new FXMLLoader(getClass().getResource("/com/brestats/pages/Main.fxml"));
            Parent main = mainView.load();
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            stage.setScene(new Scene(main, ((Node) ev.getSource()).getScene().getWidth(),
                    ((Node) ev.getSource()).getScene().getHeight()));

            ((MainControl) mainView.getController()).setPreviousSelectedCities(selectedCities);
        } catch (IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    public void addSelectedCity(Commune city) {
        DonneesAnnuelles data = this.dbValeursCommuneAnnee.getAverageItemByCity(city);
        TableData row = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(),
                data.getPopulation(), data.getNbMaison(), data.getNbAppart(), data.getPrixMoyen(),
                data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(), data.getBudgetTotal());

        this.createCityLabel(city);

        this.selectedCities.add(city);
        this.averageCityData.add(data);

        this.tableRows.add(row);

        refreshTable();
        refreshCharts();
    }

    public void createCityLabel(Commune city) {
        BorderPane borderPane = new BorderPane();
        Label numLabel = new Label(Integer.toString(this.cityLabels.size() + 1));
        Label cityName = new Label(city.getNomCommune());
        ImageView removeIcon = new ImageView(
                getClass().getResource("/com/brestats/files/img/remove-icon.png").toExternalForm());
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
        this.tableRows.clear();

        for (Commune city : this.selectedCities) {
            this.createCityLabel(city);
            DonneesAnnuelles data = this.dbValeursCommuneAnnee.getAverageItemByCity(city);
            this.averageCityData.add(data);
            TableData row = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(),
                data.getPopulation(), data.getNbMaison(), data.getNbAppart(), data.getPrixMoyen(),
                data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(), data.getBudgetTotal());
            this.tableRows.add(row);
        }

        refreshTable();
        refreshCharts();

        ArrayList<Double> latitudes = new ArrayList<Double>();
        ArrayList<Double> longitudes = new ArrayList<Double>();
        for (Commune city : selectedCities) {
            latitudes.add(city.getLatitude());
            longitudes.add(city.getLongitude());
        }

        engine.executeScript("setRedMarkers(" + transformToJavascriptArray(latitudes) + ","
                + transformToJavascriptArray(longitudes) + ")");
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

            for (int i = 0; i < columns.size(); i++) {
                int ind = i; // Cannot use i in anonymous class, because it must be at least effectively
                             // final
                // Setting up data in columns by passing TableData's StringPropery attributes
                columns.get(i).setCellValueFactory(
                        new Callback<CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                            public ObservableValue<String> call(CellDataFeatures<TableData, String> p) {
                                return p.getValue().getProperties().get(ind);
                            }
                        });
            }
        }
        this.tableView.setItems(dataList);

    }

    public void refreshCharts() {
        // Average chart (re)loading
        ArrayList<Series<String, Double>> citySeriesList = new ArrayList<>();

        String population = "Population";
        String houses = "Nombre de maisons";
        String aparts = "Nombre d'appartements";
        String cost = "Coût moyen";
        String m2Cost = "Coût du m² moyen";
        String surface = "Surface moyenne";
        String spendings = "Dépenses culturelles";
        String budget = "Budget moyen";

        for (TableData data : this.tableRows) {
            Series<String, Double> city = new Series<>();
            city.setName(data.getName());

            Data<String, Double> popBarData = new Data<String, Double>(population, Double.parseDouble(data.getPopulation()));
            Data<String, Double> housesBarData = new Data<String, Double>(houses, Double.parseDouble(data.getHouses()));
            Data<String, Double> apartsBarData = new Data<String, Double>(aparts, Double.parseDouble(data.getApartments()));
            Data<String, Double> costBarData = new Data<String, Double>(cost, Double.parseDouble(data.getCost()));
            Data<String, Double> m2CostBarData = new Data<String, Double>(m2Cost, Double.parseDouble(data.getM2Cost()));
            Data<String, Double> surfaceBarData = new Data<String, Double>(surface, Double.parseDouble(data.getSurface()));
            Data<String, Double> spendingsBarData = new Data<String, Double>(spendings, Double.parseDouble(data.getSpendings()));
            Data<String, Double> budgetBarData = new Data<String, Double>(budget, Double.parseDouble(data.getBudget()));

            city.setData(FXCollections.observableList(List.of(popBarData, housesBarData, apartsBarData, costBarData,
                    m2CostBarData, surfaceBarData, spendingsBarData, budgetBarData)));
            citySeriesList.add(city);
        }

        this.averageChart.setData(FXCollections.observableList(citySeriesList));

        // Evolution chart (re)loading
        ArrayList<Series<Double, Double>> series = new ArrayList<>();
        try {
            ArrayList<Annee> years = dbAnnee.selectQuery("SELECT DISTINCT annee.* FROM annee JOIN donneesannuelles ON lAnnee = annee ORDER BY annee;");

            this.evolutionXAxis.setAutoRanging(false);
            this.evolutionXAxis.setUpperBound((double) years.get(years.size() - 1).getAnnee() + 1);
            this.evolutionXAxis.setLowerBound((double) years.get(0).getAnnee() - 1);
            this.evolutionXAxis.setTickUnit(1);

            for (Commune city : this.selectedCities) {
                ArrayList<Data<Double, Double>> popArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> housesArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> apartsArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> costArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> m2CostArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> surfaceArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> spendingsArray = new ArrayList<>();
                ArrayList<Data<Double, Double>> budgetArray = new ArrayList<>();

                for (Annee year : years) {
                    DonneesAnnuelles row = this.dbValeursCommuneAnnee.getItem(city.getId() + "-" + year.getId());

                    popArray.add(new Data<Double, Double>((double) year.getAnnee(), row.getPopulation()));
                    housesArray.add(new Data<Double, Double>((double) year.getAnnee(), (double) row.getNbMaison()));
                    apartsArray.add(new Data<Double, Double>((double) year.getAnnee(), (double) row.getNbAppart()));
                    costArray.add(new Data<Double, Double>((double) year.getAnnee(), row.getPrixMoyen()));
                    m2CostArray.add(new Data<Double, Double>((double) year.getAnnee(), row.getPrixM2Moyen()));
                    surfaceArray.add(new Data<Double, Double>((double) year.getAnnee(), row.getSurfaceMoyenne()));
                    spendingsArray.add(new Data<Double, Double>((double) year.getAnnee(), row.getDepCulturelTotales()));
                    budgetArray.add(new Data<Double, Double>((double) year.getAnnee(), row.getBudgetTotal()));
                }

                Series<Double, Double> popSeries = new Series<>(FXCollections.observableList(popArray));
                Series<Double, Double> housesSeries = new Series<>(FXCollections.observableList(housesArray));
                Series<Double, Double> apartSeries = new Series<>(FXCollections.observableList(apartsArray));
                Series<Double, Double> costSeries = new Series<>(FXCollections.observableList(costArray));
                Series<Double, Double> m2CostSeries = new Series<>(FXCollections.observableList(m2CostArray));
                Series<Double, Double> surfaceSeries = new Series<>(FXCollections.observableList(surfaceArray));
                Series<Double, Double> spendingsSeries = new Series<>(FXCollections.observableList(spendingsArray));
                Series<Double, Double> budgetSeries = new Series<>(FXCollections.observableList(budgetArray));

                popSeries.setName(city.getNomCommune() + " : Population");
                housesSeries.setName(city.getNomCommune() + " : Nombre de maisons");
                apartSeries.setName(city.getNomCommune() + " : Nombre d'appartements");
                costSeries.setName(city.getNomCommune() + " : Prix moyen");
                m2CostSeries.setName(city.getNomCommune() + " : Prix moyen du m²");
                surfaceSeries.setName(city.getNomCommune() + " : Surface moyenne");
                spendingsSeries.setName(city.getNomCommune() + " : Dépences culturelles");
                budgetSeries.setName(city.getNomCommune() + " : Budget");

                series.addAll(List.of(popSeries, housesSeries, apartSeries, costSeries, m2CostSeries, surfaceSeries, spendingsSeries, budgetSeries));
            }
        } catch (SQLException ex) {
            System.out.println("Unexpected exception with query : SELECT * FROM annee;");
            ex.printStackTrace();
        }

        this.evolutionChart.setData(FXCollections.observableList(series));
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

        public TableData(String name, String dep, double pop, double houses, double apartments, double cost,
                double m2Cost, double surface, double spendings, double budget) {
            this(name, dep, Double.toString(pop), Double.toString(houses), Double.toString(apartments),
                    Double.toString(cost), Double.toString(m2Cost), Double.toString(surface),
                    Double.toString(spendings), Double.toString(budget));
        }

        public TableData(String name, String dep, String pop, String houses, String apartments, String cost,
                String m2Cost, String surface, String spendings, String budget) {
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

            this.properties = List.of(this.name, this.dep, this.population, this.houses, this.apartments, this.cost,
                    this.m2Cost, this.surface, this.spendings, this.budget);
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
