package com.brestats.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TopBar1 extends HBox {

    public TopBar1() {
        setPadding(new Insets(10, 0, 10, 0));
        setAlignment(Pos.CENTER_LEFT); // Align content to the left
        setSpacing(10);
        setStyle("-fx-background-color: #394e51; -fx-border-color: black; -fx-border-width: 0 0 3 0;");

        // Load and set up left image (logo)
        Image leftImage = new Image(getClass().getResourceAsStream("logo.png"));
        ImageView leftImageView = new ImageView(leftImage);
        leftImageView.setFitHeight(50);
        leftImageView.setPreserveRatio(true);

        // Load and set up center image (logotext)
        Image centerImage = new Image(getClass().getResourceAsStream("logotext.png"));
        ImageView centerImageView = new ImageView(centerImage);
        centerImageView.setFitHeight(30);
        centerImageView.setPreserveRatio(true);

        // Add spacing between the left image and the center image
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Make the spacer grow to push center image to the center

        getChildren().addAll(leftImageView, centerImageView, spacer);
    }
}
