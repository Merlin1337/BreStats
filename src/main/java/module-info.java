module com.brestats {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.brestats to javafx.fxml;
    exports com.brestats;
}
