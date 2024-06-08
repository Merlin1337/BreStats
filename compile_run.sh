echo "Compiling java files : " `find src/java -name *.java`
javac -d class --module-path "lib:lib/unix" --add-modules javafx.controls,javafx.graphics,javafx.web,javafx.fxml `find src/java -name *.java`

echo "Running java project..."
java -cp "class:src/resources" --module-path "lib:lib/unix" --add-modules javafx.controls,javafx.graphics,javafx.web,javafx.fxml com.brestats.App
