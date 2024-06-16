@echo off
echo Running java project...
java -cp "class;src/resources" -p "lib/win/lib;lib/mysql-connector-j-8.4.0.jar" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web,javafx.swing com.brestats.App
pause