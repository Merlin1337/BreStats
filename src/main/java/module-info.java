module com.brestats {
    requires java.sql;

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.brestats to javafx.graphics;
    opens com.brestats.control to javafx.fxml;
    exports com.brestats;
}
