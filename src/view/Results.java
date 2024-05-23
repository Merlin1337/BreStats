import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Results extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // TopBar2 (from your code)
        TopBar2 topBar = new TopBar2();
        root.setTop(topBar);

        // Map and Search Bar
        StatsResult statsResult = new StatsResult();
        BorderPane.setMargin(statsResult, new Insets(20, 20, 30, 20)); // Add margin here
        root.setCenter(statsResult);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Bre'Stats");
        primaryStage.getIcons().add(new Image(Home.class.getResourceAsStream("favicon.png")));

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
