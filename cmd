javac -cp .\class -d .\class .\src\Planification.java; java -cp .\class Start Planification

javac -encoding utf8 -cp .\class -d .\class .\src\mail\MailItem.java; javac -encoding utf8 -cp .\class -d .\class .\src\mail\MailServer.java; javac -encoding utf8 -cp .\class -d .\class .\src\MailScenario.java; java -cp .\class MailScenario

javac -encoding utf8 -cp ./class -d ./class ./src/question/Question.java; javac -encoding utf8 -cp ./class -d ./class ./src/question/ChoiceQuestion.java; javac -encoding utf8 -cp ./class -d ./class ./src/ScenarioQuestion.java; java -cp ./class ScenarioQuestion

javac -encoding utf8 -cp ./class -d ./class ./src/question/*.java; javac -encoding utf8 -cp ./class -d ./class ./src/*.java; java -cp ./class ScenarioQuiz


javac -encoding utf8 -cp ./class -d ./class ./src/*.java; java -cp ./class HelloWorldSwing

javac --module-path ./lib/lib --add-modules javafx.controls -encoding utf8 -cp ./class -d ./class ./src/*.java ./src/model/*.java ./src/control/*.java ./src/view/*.java; java -cp ./class FXCalendar

javac --module-path ./lib --add-modules javafx.controls -encoding utf8 -d ./class ./src/*.java ./src/model/*.java ./src/control/*.java ./src/view/*.java



javac --module-path C:\"Program Files"\Java\javafx-sdk-22.0.1\javafx -d ..\build\ --add-modules javafx.controls ..\src\model\*.java; javac --module-path C:\"Program Files"\Java\javafx-sdk-22.0.1\javafx -d ..\build\ --add-modules javafx.controls ..\src\control\*.java; javac --module-path C:\"Program Files"\Java\javafx-sdk-22.0.1\javafx -d ..\build\ --add-modules javafx.controls ..\src\view\*.java; java --module-path C:\"Program Files"\Java\javafx-sdk-22.0.1\javafx --add-modules javafx.controls view.ViewCalendar





java --module-path ../lib/lib --add-modules javafx.controls MapApp