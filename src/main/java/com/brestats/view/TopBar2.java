package com.brestats.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TopBar2 extends HBox {

    public TopBar2() {
        setPadding(new Insets(15, 20, 10, 15));
        setAlignment(Pos.CENTER); // Align content to center for even distribution
        setSpacing(10);
        setStyle("-fx-background-color: #394e51; -fx-border-color: black; -fx-border-width: 0 0 3 0;");

        // Load and set up left image (logo)
        Image leftImage = new Image(getClass().getResource("/com/brestats/files/img/logo.png").toExternalForm());
        ImageView leftImageView = new ImageView(leftImage);
        leftImageView.setFitHeight(45);
        leftImageView.setPreserveRatio(true);

        // Load and set up center image (logotext)
        Image centerImage = new Image(getClass().getResource("/com/brestats/files/img/logotext.png").toExternalForm());
        ImageView centerImageView = new ImageView(centerImage);
        centerImageView.setFitHeight(30);
        centerImageView.setPreserveRatio(true);

        // Load and set up right image 
        Image rightImage = new Image(getClass().getResource("/com/brestats/files/img/whiteback.png").toExternalForm()); // Replace with your image file
        ImageView rightImageView = new ImageView(rightImage);
        rightImageView.setFitHeight(35); // Adjust height as needed
        rightImageView.setPreserveRatio(true);

        // Spacers for distribution
        HBox leftSpacer = new HBox();
        HBox rightSpacer = new HBox();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);  
        HBox.setHgrow(rightSpacer, Priority.ALWAYS); 

        getChildren().addAll(leftImageView, leftSpacer, centerImageView, rightSpacer, rightImageView);
    }
}