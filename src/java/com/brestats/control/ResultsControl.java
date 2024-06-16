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

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import javax.imageio.ImageIO;

/**
 * Controller of Results.fxml view
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class ResultsControl {
    /** The grid containing all the city's names */
    @FXML
    private GridPane cityLabelsGrid;
    /** The web view containing the map */
    @FXML
    private WebView webView;
    /** The button allowing to go back to the main view to selected an other city */
    @FXML
    private Button addNewCity;
    /**
     * The grid pane containing all checkboxes of cities to show in table and charts
     */
    @FXML
    private GridPane selectShownCitiesGrid;
    /** The VBox pane which contains the data checkboxes */
    @FXML
    private VBox dataCheckBoxes;
    /** The checkbox which controls all city's checkboxes */
    @FXML
    private CheckBox selectAllCities;
    /** The checkbox which controls all data's checkboxes */
    @FXML
    private CheckBox selectAllData;
    /**
     * The table containing average data on selected cities. More precisely, it is
     * {@link TableData}-typed, which each instance contains a selected city's data
     */
    @FXML
    private TableView<TableData> tableView;
    /** The average bar chart, representing the same data as the table */
    @FXML
    private BarChart<String, Double> averageChart;
    /** The evolution line chart which shows data over the years */
    @FXML
    private LineChart<Double, Double> evolutionChart;
    /** The x axis of the evolution line chart */
    @FXML
    private NumberAxis evolutionXAxis;

    /** DAO for cities' data */
    private DBValeursCommuneAnnee dbValeursCommuneAnnee = DAO.DB_VAL;
    /** DAO for years */
    private DBAnnee dbAnnee = DAO.DB_ANNEE;
    /** web view's engine (for the map) */
    private WebEngine engine;
    /** Array of the selected cities in main view */
    private ArrayList<Commune> selectedCities;
    /**
     * The cities to show among selected ones. A city is shown if its checkbox is
     * selected
     */
    private ArrayList<Commune> shownCities;
    /** Array of city's name label. They are shown in {@link #cityLabelsGrid} */
    private ArrayList<Label> cityLabels;
    /**
     * Array of {@link TableData} objects, one for each table row or bar chart bar
     */
    private ArrayList<TableData> tableRows;
    /** Array of city's name checkboxes */
    private ArrayList<String> dataCheckBoxNames;

    /**
     * Initialize empty Array Lists
     */
    public ResultsControl() {
        this.selectedCities = new ArrayList<>();
        this.shownCities = new ArrayList<>();
        this.tableRows = new ArrayList<>();
        this.dataCheckBoxNames = new ArrayList<>();
        this.cityLabels = new ArrayList<>();
    }

    /**
     * Initialize page elements, like the map, the charts and the checkboxes
     */
    @FXML
    public void initialize() {
        this.engine = this.webView.getEngine();

        // Loading map
        this.engine.load(getClass().getResource("/com/brestats/files/map.html").toExternalForm());
        this.engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> obs, State oldV, State newV) { // when map is fully
                                                                                                // loaded
                if (newV.equals(State.SUCCEEDED)) {
                    engine.executeScript("map.setZoom(7)");

                    // add a red marker by selected city
                    ArrayList<Double> latitudes = new ArrayList<Double>();
                    ArrayList<Double> longitudes = new ArrayList<Double>();
                    for (Commune city : selectedCities) {
                        latitudes.add(city.getLatitude());
                        longitudes.add(city.getLongitude());
                    }

                    engine.executeScript("setColoredMarkers(" + transformToJavascriptArray(latitudes) + ","
                            + transformToJavascriptArray(longitudes) + ")");
                }
            }
        });

        // charts' settings
        this.averageChart.setLegendSide(Side.LEFT);
        this.averageChart.setAnimated(false);
        this.evolutionChart.setLegendSide(Side.LEFT);
        this.evolutionChart.setAnimated(false);

        for (Node checkBox : this.dataCheckBoxes.getChildren()) {
            // Get all selected data checkboxes, for the table and the bar chart
            if (((CheckBox) checkBox).isSelected()) {
                this.dataCheckBoxNames.add(((CheckBox) checkBox).getText());
            }

            // handle the modification of the value of the selected property, by user or by
            // the "select all" checkbox
            ((CheckBox) checkBox).selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> obs, Boolean oldV, Boolean newV) {
                    handleSelectDataCheckBox((CheckBox) checkBox);
                }
            });
        }

    }

    // @FXML
    // public void testSnapshot(ActionEvent ev) {
    // WritableImage img = this.averageChart.snapshot(new SnapshotParameters(),
    // null);

    // // try (FileOutputStream fos = new FileOutputStream("test.png")) {
    // // // Get pixel data from the image
    // // PixelReader pixelReader = img.getPixelReader();
    // // int width = (int) img.getWidth();
    // // int height = (int) img.getHeight();
    // // int[] pixels = new int[width * height];
    // // pixelReader.getPixels(0, 0, width, height,
    // PixelFormat.getIntArgbInstance(), pixels, 0, width);

    // // // Convert pixel data to byte array
    // // ByteBuffer byteBuffer = ByteBuffer.allocate(width * height * 4); // 4
    // bytes per pixel (ARGB)
    // // IntBuffer intBuffer = byteBuffer.asIntBuffer();
    // // intBuffer.put(pixels);

    // // // Write the byte array to the file
    // // fos.write(byteBuffer.array());
    // // System.out.println("Screenshot saved");
    // // } catch (IOException e) {
    // // System.err.println("Failed to save screenshot: " + e.getMessage());
    // // }

    // try {
    // ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", new
    // File("test.png"));
    // System.out.println("Screenshot saved");
    // } catch (IOException e) {
    // System.err.println("Failed to save screenshot: " + e.getMessage());
    // }
    // }

    /**
     * Action event listener for the "Ajouter une ville" button. It loads the main
     * page and pass the actual selected cities (displayed in red on the main page)
     * 
     * @param ev action event from the button
     */
    @FXML
    public void handleAddNewCity(ActionEvent ev) {
        try {
            FXMLLoader mainView = new FXMLLoader(getClass().getResource("/com/brestats/pages/Main.fxml"));
            Parent main = mainView.load();
            Stage stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            // System.out.println(((Node) ev.getSource()).getScene().getWidth() + " "
            // + ((Node) ev.getSource()).getScene().getHeight());
            stage.setScene(new Scene(main, ((Node) ev.getSource()).getScene().getWidth(),
                    ((Node) ev.getSource()).getScene().getHeight()));

            // Pass the actual selected cities
            ((MainControl) mainView.getController()).setPreviousSelectedCities(selectedCities);
        } catch (IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    /**
     * Action event listener for the Select all cities checkbox. Set all city's
     * checkboxes' selected property at the same state as this one
     * 
     * @param ev Action event
     */
    @FXML
    public void handleSelectAllCities(ActionEvent ev) {
        // For all city's checkboxes, set the same selected value as the "select all"
        // one
        boolean selected = this.selectAllCities.isSelected();
        for (Node checkBox : this.selectShownCitiesGrid.getChildren()) {
            ((CheckBox) checkBox).setSelected(selected);
        }

        this.selectAllCities.setIndeterminate(false);
        this.selectAllCities.setSelected(selected);

        refreshCharts();
        refreshTable();
    }

    /**
     * Action event listener for the Select all data checkbox. Set all data's
     * checkbox's selected properties at the same state as this one
     * 
     * @param ev Action event
     */
    @FXML
    public void handleSelectAllData(ActionEvent ev) {
        // For all data's checkboxes, set the same selected value as the "select all"
        // one
        boolean selected = this.selectAllData.isSelected();

        for (Node checkBox : this.dataCheckBoxes.getChildren()) {
            ((CheckBox) checkBox).setSelected(selected);
        }

        this.selectAllData.setIndeterminate(false);
        this.selectAllData.setSelected(selected);

        refreshCharts();
    }

    /**
     * Mouse clicked event listener for the edit button. Display the login page
     * @param ev Mouse event
     */
    @FXML
    public void handleEdit(MouseEvent ev) {
        try {
            FXMLLoader mainView = new FXMLLoader(getClass().getResource("/com/brestats/pages/Login.fxml"));
            Parent main = mainView.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(main));

            stage.getIcons().add(new Image(getClass().getResource("/com/brestats/files/img/favicon.png").toExternalForm()));
            stage.setTitle("Edition - Brestats");
            stage.show();
        } catch (IOException ex) {
            System.out.println("Cannot change scene");
            ex.printStackTrace();
        }
    }

    /**
     * Called when a data checkbox's selected property is changed, directly by user
     * of by the select all data checkbox
     * 
     * @param checkBox a data checkbox on the page
     */
    public void handleSelectDataCheckBox(CheckBox checkBox) {
        // get of remove the data, depending on the selected property value
        if (checkBox.isSelected()) {
            this.dataCheckBoxNames.add(checkBox.getText());
        } else {
            this.dataCheckBoxNames.remove(checkBox.getText());
        }

        // Set the "select all data" state depending on the number of selected cities
        Platform.runLater(new Runnable() {
            public void run() {
                int nbSelected = 0;
                for (Node node : dataCheckBoxes.getChildren()) {
                    if (((CheckBox) node).isSelected()) {
                        nbSelected++;
                    }
                }

                if (nbSelected == dataCheckBoxes.getChildren().size()) {
                    selectAllData.setIndeterminate(false);
                    selectAllData.setSelected(true);
                } else if (nbSelected == 0) {
                    selectAllData.setSelected(false);
                    selectAllData.setIndeterminate(false);
                } else {
                    selectAllData.setSelected(false);
                    selectAllData.setIndeterminate(true);
                }

                refreshCharts();
            }
        });

    }

    /**
     * Called from {@link MainControl} when search button is clicked. Add the
     * selected city in the page (map, cities' list, checkbox, table and charts)
     * 
     * @param city The selected city in Main page
     */
    public void addSelectedCity(Commune city) {
        DonneesAnnuelles data = this.dbValeursCommuneAnnee.getAverageItemByCity(city); // Average of city data for all
                                                                                       // years
        // A TableData object containing all city's data, used for the table and the bar
        // chart
        TableData row = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(),
                data.getPopulation(), data.getNbMaison(), data.getNbAppart(), data.getPrixMoyen(),
                data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(), data.getBudgetTotal());

        this.createCityLabel(city);

        this.selectedCities.add(city);
        this.shownCities.add(city);

        this.tableRows.add(row);

        refreshSelectShownCitiesGrid();
        refreshTable();
        refreshCharts();
    }

    /**
     * Create the city name display in the page
     * 
     * @param city The city of which label is created
     */
    public void createCityLabel(Commune city) {
        ArrayList<String> hexColors = new ArrayList<>(
                List.of("#2A81CB", "#FFD326", "#CB2B3E", "#2AAD27", "#CB8427", "#CAC428", "#9C2BCB"));

        BorderPane borderPane = new BorderPane();
        Label numLabel = new Label(" ");
        Label cityName = new Label(city.getNomCommune());
        ImageView removeIcon = new ImageView(
                getClass().getResource("/com/brestats/files/img/remove-icon.png").toExternalForm());
        HBox labelBox = new HBox(numLabel, cityName, removeIcon);

        numLabel.getStyleClass().add("numero");
        numLabel.setStyle(
                "-fx-background-color: " + hexColors.get(this.cityLabelsGrid.getChildren().size() % hexColors.size()));
        cityName.getStyleClass().add("ville");
        labelBox.setSpacing(15);
        borderPane.setCenter(labelBox);
        removeIcon.setFitHeight(30);
        removeIcon.setFitWidth(30);
        removeIcon.setStyle("-fx-cursor: hand");

        // event fired when remove icon is clicked
        removeIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent ev) {
                selectedCities.remove(city);
                shownCities.remove(city);
                reloadView();
            }
        });

        cityName.setAlignment(Pos.CENTER);
        labelBox.setAlignment(Pos.CENTER);

        this.cityLabelsGrid.add(borderPane, 0, this.cityLabels.size());
        this.cityLabels.add(cityName);
    }

    /**
     * Procedure which reload all the page when a changing is made. Call
     * {@link #refreshSelectShownCitiesGrid()}, {@link #refreshTable()} and
     * {@link #refreshCharts()}
     */
    public void reloadView() {
        // reset lists
        this.cityLabelsGrid.getChildren().clear();
        this.cityLabels.clear();
        this.tableRows.clear();

        for (Commune city : this.selectedCities) {
            this.createCityLabel(city);
            // Create and add a new TableData object to the list attribute, if its checkbox
            // is selected
            if (this.shownCities.contains(city)) {
                DonneesAnnuelles data = this.dbValeursCommuneAnnee.getAverageItemByCity(city);
                TableData row = new TableData(data.getLaCom().getNomCommune(), data.getLaCom().getDep().getNomDep(),
                        data.getPopulation(), data.getNbMaison(), data.getNbAppart(), data.getPrixMoyen(),
                        data.getPrixM2Moyen(), data.getSurfaceMoyenne(), data.getDepCulturelTotales(),
                        data.getBudgetTotal());
                this.tableRows.add(row);
            }
        }

        refreshSelectShownCitiesGrid();
        refreshTable();
        refreshCharts();

        // Place markers on the map
        ArrayList<Double> latitudes = new ArrayList<Double>();
        ArrayList<Double> longitudes = new ArrayList<Double>();
        for (Commune city : selectedCities) {
            latitudes.add(city.getLatitude());
            longitudes.add(city.getLongitude());
        }

        engine.executeScript("setColoredMarkers(" + transformToJavascriptArray(latitudes) + ","
                + transformToJavascriptArray(longitudes) + ")");
    }

    /**
     * Procedure which reload the checkboxes of cities in the bottom left settings
     * pane. A checkbox is created for each city
     */
    public void refreshSelectShownCitiesGrid() {
        this.selectShownCitiesGrid.getChildren().clear();

        for (Commune city : this.selectedCities) {
            CheckBox checkBox = new CheckBox(city.getNomCommune());
            int checkBoxAmount = this.selectShownCitiesGrid.getChildren().size();

            if (this.shownCities.contains(city)) {
                checkBox.setSelected(true);
            } else {
                checkBox.setSelected(false);
            }

            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> obs, Boolean oldV, Boolean newV) {
                    if (newV) {
                        shownCities.add(city);
                    } else {
                        shownCities.remove(city);
                    }

                    // Set the "select all cities" state depending on the number of selected cities
                    Platform.runLater(new Runnable() {
                        public void run() {
                            int nbSelected = 0;
                            for (Node node : selectShownCitiesGrid.getChildren()) {
                                if (((CheckBox) node).isSelected()) {
                                    nbSelected++;
                                }
                            }

                            if (nbSelected == selectShownCitiesGrid.getChildren().size()) {
                                selectAllCities.setIndeterminate(false);
                                selectAllCities.setSelected(true);
                            } else if (nbSelected == 0) {
                                selectAllCities.setSelected(false);
                                selectAllCities.setIndeterminate(false);
                            } else {
                                selectAllCities.setSelected(false);
                                selectAllCities.setIndeterminate(true);
                            }

                            reloadView();
                        }
                    });
                }
            });

            /*
             * Posisitonning of checkboxes :
             * +-------+
             * | 1 | 2 |
             * +-------+
             * | 3 | 4 |
             * | ... |
             */
            this.selectShownCitiesGrid.add(checkBox, checkBoxAmount % 2, checkBoxAmount / 2);
        }
    }

    /**
     * Procedure which reload the top left table. Show the cities of which checkbox
     * is selected
     */
    public void refreshTable() {
        ObservableList<TableData> dataList = FXCollections.observableArrayList();

        this.tableView.getItems().clear();

        for (TableData row : this.tableRows) {
            dataList.add(row);

            ArrayList<TableColumn<TableData, String>> columns = new ArrayList<TableColumn<TableData, String>>();
            for (TableColumn<TableData, ?> column : this.tableView.getColumns()) {
                // Unchecked cast because TableView.getColumns() return a non-typed object,
                // compile anyway
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

    /**
     * Procedure which reload charts with selected cities and data
     */
    public void refreshCharts() {
        /*
         * ---- Average chart (re)loading
         * The Data<String, Double> class represent a bar of the graphic
         * The Series<String, Double> class represent all data of city
         * All same data type for each city will be stacked together
         */
        ArrayList<Series<String, Double>> citySeriesList = new ArrayList<>();

        for (TableData data : this.tableRows) {
            Series<String, Double> city = new Series<>();
            city.setName(data.getName());

            Data<String, Double> popBarData = new Data<String, Double>("Population",
                    Double.parseDouble(data.getPopulation()));
            Data<String, Double> housesBarData = new Data<String, Double>("Nombre\nde maisons",
                    Double.parseDouble(data.getHouses()));
            Data<String, Double> apartsBarData = new Data<String, Double>("Nombre\nd'appartements",
                    Double.parseDouble(data.getApartments()));
            Data<String, Double> costBarData = new Data<String, Double>("Prix moyen",
                    Double.parseDouble(data.getCost()));
            Data<String, Double> m2CostBarData = new Data<String, Double>("Prix moyen\n du m²",
                    Double.parseDouble(data.getM2Cost()));
            Data<String, Double> surfaceBarData = new Data<String, Double>("Surface\nmoyenne",
                    Double.parseDouble(data.getSurface()));
            Data<String, Double> spendingsBarData = new Data<String, Double>("Dépenses\nculturelles",
                    Double.parseDouble(data.getSpendings()));
            Data<String, Double> budgetBarData = new Data<String, Double>("Budget moyen",
                    Double.parseDouble(data.getBudget()));

            // Adding Data object only if the city checkbox is selected
            ArrayList<Data<String, Double>> dataList = new ArrayList<>();
            if (this.dataCheckBoxNames.contains("Population")) {
                dataList.add(popBarData);
            }
            if (this.dataCheckBoxNames.contains("Nombre de maisons")) {
                dataList.add(housesBarData);
            }
            if (this.dataCheckBoxNames.contains("Nombre d'appartements")) {
                dataList.add(apartsBarData);
            }
            if (this.dataCheckBoxNames.contains("Prix moyen")) {
                dataList.add(costBarData);
            }
            if (this.dataCheckBoxNames.contains("Prix moyen du m²")) {
                dataList.add(m2CostBarData);
            }
            if (this.dataCheckBoxNames.contains("Surface moyenne")) {
                dataList.add(surfaceBarData);
            }
            if (this.dataCheckBoxNames.contains("Dépenses culturelles")) {
                dataList.add(spendingsBarData);
            }
            if (this.dataCheckBoxNames.contains("Budget")) {
                dataList.add(budgetBarData);
            }

            city.setData(FXCollections.observableList(dataList));
            citySeriesList.add(city);
        }

        this.averageChart.setData(FXCollections.observableList(citySeriesList));

        /*
         * ---- Evolution chart (re)loading
         * <year> <data>
         * The Data<Double, Double> class represents a point on the graphic
         * The Series<Double, Double> class represents the line linking several points
         * There is a line (=Series) by data type, meaning there are 8 lines by city
         */
        ArrayList<Series<Double, Double>> series = new ArrayList<>();
        try {
            ArrayList<Annee> years = dbAnnee.selectQuery(
                    "SELECT DISTINCT annee.* FROM annee JOIN donneesannuelles ON lAnnee = annee ORDER BY annee;");

            this.evolutionXAxis.setAutoRanging(false);
            this.evolutionXAxis.setUpperBound((double) years.get(years.size() - 1).getAnnee() + 1);
            this.evolutionXAxis.setLowerBound((double) years.get(0).getAnnee() - 1);
            this.evolutionXAxis.setTickUnit(1);

            for (Commune city : this.shownCities) {
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
                budgetSeries.setName(city.getNomCommune() + " : Budget moyen");

                // Adding Data object only if the city checkbox is selected
                ArrayList<Series<Double, Double>> dataList = new ArrayList<>();
                if (this.dataCheckBoxNames.contains("Population")) {
                    dataList.add(popSeries);
                }
                if (this.dataCheckBoxNames.contains("Nombre de maisons")) {
                    dataList.add(housesSeries);
                }
                if (this.dataCheckBoxNames.contains("Nombre d'appartements")) {
                    dataList.add(apartSeries);
                }
                if (this.dataCheckBoxNames.contains("Prix moyen")) {
                    dataList.add(costSeries);
                }
                if (this.dataCheckBoxNames.contains("Prix moyen du m²")) {
                    dataList.add(m2CostSeries);
                }
                if (this.dataCheckBoxNames.contains("Surface moyenne")) {
                    dataList.add(surfaceSeries);
                }
                if (this.dataCheckBoxNames.contains("Dépenses culturelles")) {
                    dataList.add(spendingsSeries);
                }
                if (this.dataCheckBoxNames.contains("Budget")) {
                    dataList.add(budgetSeries);
                }

                series.addAll(dataList);
            }
        } catch (SQLException ex) {
            System.out.println("Unexpected exception with query : SELECT * FROM annee;");
            ex.printStackTrace();
        }

        this.evolutionChart.setData(FXCollections.observableList(series));
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
     * A nested class which is used for the table and average chart data
     * 
     * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
     *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
     */
    private class TableData {
        /** The list for all others properties attributes */
        private List<StringProperty> properties;
        /** The name of the city */
        private StringProperty name;
        /** The département of the city's name */
        private StringProperty dep;
        /** The average population of the city */
        private StringProperty population;
        /** The average sold houses number per year in the city */
        private StringProperty houses;
        /** The average sold apartements number per year in the city */
        private StringProperty apartments;
        /** The average cost of houses and apartments per year in the city */
        private StringProperty cost;
        /** The average m² cost of houses and apartments per year in the city */
        private StringProperty m2Cost;
        /** The average surface of sold houses and apartments per year in the city */
        private StringProperty surface;
        /** The average spendings per year by the city */
        private StringProperty spendings;
        /** The average budget of the city */
        private StringProperty budget;

        /**
         * Call
         * {@link #TableData(String, String, String ,String, String, String, String, String, String, String)}
         * with doubles converted to Strings
         * 
         * @param name       The name of the city
         * @param dep        The département of the city's name
         * @param pop        The average population of the city
         * @param houses     The average sold houses number per year in the city
         * @param apartments The average sold apartements number per year in the city
         * @param cost       The average cost of houses and apartments per year in the
         *                   city
         * @param m2Cost     The average m² cost of houses and apartments per year in
         *                   the city
         * @param surface    The average surface of sold houses and apartments per year
         *                   in the city
         * @param spendings  The average spendings per year by the city
         * @param budget     The average budget of the city
         */
        public TableData(String name, String dep, double pop, double houses, double apartments, double cost,
                double m2Cost, double surface, double spendings, double budget) {
            this(name, dep, Double.toString(pop), Double.toString(houses), Double.toString(apartments),
                    Double.toString(cost), Double.toString(m2Cost), Double.toString(surface),
                    Double.toString(spendings), Double.toString(budget));
        }

        /**
         * Initiate an instance of TableData with given params
         * 
         * @param name       The name of the city
         * @param dep        The département of the city's name
         * @param pop        The average population of the city
         * @param houses     The average sold houses number per year in the city
         * @param apartments The average sold apartements number per year in the city
         * @param cost       The average cost of houses and apartments per year in the
         *                   city
         * @param m2Cost     The average m² cost of houses and apartments per year in
         *                   the city
         * @param surface    The average surface of sold houses and apartments per year
         *                   in the city
         * @param spendings  The average spendings per year by the city
         * @param budget     The average budget of the city
         */
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

        /**
         * Return the properties list
         * 
         * @return The properties list
         */
        public List<StringProperty> getProperties() {
            return this.properties;
        }

        /**
         * Return the name of the city
         * 
         * @return the name as String
         */
        public String getName() {
            return this.name.get();
        }

        /**
         * Return the average population of the city
         * 
         * @return The population as String
         */
        public String getPopulation() {
            return this.population.get();
        }

        /**
         * Return the average number of sold houses
         * 
         * @return The number of sold houses as String
         */
        public String getHouses() {
            return this.houses.get();
        }

        /**
         * Return the average number of sold apartments
         * 
         * @return The number of sold apartments as String
         */
        public String getApartments() {
            return this.apartments.get();
        }

        /**
         * Return the average cost of houses and apartments
         * 
         * @return The cost as String
         */
        public String getCost() {
            return this.cost.get();
        }

        /**
         * Return the average m² cost of houses and apartments
         * 
         * @return The m² cost as String
         */
        public String getM2Cost() {
            return this.m2Cost.get();
        }

        /**
         * Return the average surface of houses and apartments
         * 
         * @return The surface as String
         */
        public String getSurface() {
            return this.surface.get();
        }

        /**
         * Return the average spendings
         * 
         * @return The spendings as String
         */
        public String getSpendings() {
            return this.spendings.get();
        }

        /**
         * Return the total budget
         * 
         * @return The budget as String
         */
        public String getBudget() {
            return this.budget.get();
        }
    }
}
