module com.brestats {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    opens com.brestats to javafx.graphics;
    opens com.brestats.control to javafx.fxml;
    exports com.brestats;
}
