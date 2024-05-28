package com.brestats.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StatsResult extends HBox {

  public StatsResult() {
    // Left Group
    Image leftImage = new Image(getClass().getResource("/com/brestats/files/img/map.png").toExternalForm());
    ImageView leftImageView = new ImageView(leftImage);
    leftImageView.setPreserveRatio(true);
    leftImageView.setFitHeight(350);

    Label leftLabel = new Label("Map Data");
    Button leftButton = new Button("View Details");

    VBox leftGroup = new VBox(10);
    leftGroup.getChildren().addAll(leftImageView, leftLabel, leftButton);
    leftGroup.setAlignment(Pos.CENTER);
    HBox.setHgrow(leftGroup, Priority.ALWAYS);

    // Separator
    Separator separator = new Separator(Orientation.VERTICAL);
    separator.getStyleClass().add("separator"); // Add a custom CSS class
    HBox.setMargin(separator, new Insets(0, 20, 0, 20)); // 10px margin on left and right

    // Right Group
    Image rightImage = new Image(getClass().getResource("/com/brestats/files/img/map.png").toExternalForm());
    ImageView rightImageView = new ImageView(rightImage);
    rightImageView.setPreserveRatio(true);
    rightImageView.setFitHeight(350);

    Label rightLabel = new Label("Other Stats");
    Button rightButton = new Button("Analyze");

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("Team1", 13),
        new PieChart.Data("Team2", 25),
        new PieChart.Data("Team3", 10),
        new PieChart.Data("Team4", 22));
    final PieChart chart = new PieChart(pieChartData);
    chart.setTitle("Diagramme de la répartition de l'attractivité");

    VBox rightGroup = new VBox(10);
    rightGroup.getChildren().addAll(rightImageView, rightLabel, rightButton, chart);
    rightGroup.setAlignment(Pos.CENTER);
    HBox.setHgrow(rightGroup, Priority.ALWAYS);

    // Layout
    this.getChildren().addAll(leftGroup, separator, rightGroup);
  }
}