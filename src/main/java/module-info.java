module com.brestats {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    opens com.brestats to javafx.graphics;
    exports com.brestats;
}
