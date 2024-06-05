module com.brestats {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires jdk.jsobject;
    requires javafx.base;

    opens com.brestats to javafx.graphics;
    opens com.brestats.control to javafx.fxml,javafx.web;
    opens com.brestats.model.data to javafx.web;
    exports com.brestats;
}
