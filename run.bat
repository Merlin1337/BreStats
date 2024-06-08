@echo off
echo Running java project...
java -cp "class;src/resources" -p "lib/win/lib;lib/mysql-connector-j-8.4.0.jar" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED com.brestats.App
pause