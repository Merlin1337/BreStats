@echo off
REM Cherche tous les fichiers .java et les compile ensemble

REM Définit le répertoire de base (le répertoire courant)
set BASE_DIR=%cd%

REM Initialise une variable pour contenir tous les fichiers .java
set FILE_LIST=

REM Active l'expansion des variables différée
setlocal enabledelayedexpansion

REM Trouve tous les fichiers .java et les ajoute à FILE_LIST
for /r "%BASE_DIR%" %%f in (*.java) do (
    echo %%f
    set "FILE_LIST=!FILE_LIST! "%%f""
)

REM Active l'expansion des variables différée
setlocal enabledelayedexpansion

REM Compile tous les fichiers trouvés
if not "!FILE_LIST!"=="" (
    echo Compiling java files:
    echo %FILE_LIST%
    javac -d class --module-path "lib;lib/win/lib" --add-modules javafx.controls,javafx.graphics,javafx.web,javafx.fxml,javafx.swing %FILE_LIST%
) else (
    echo No .java file found
)

echo Project compiled

echo Running java project...
java -cp "class;src/resources" -p "lib/win/lib;lib/mysql-connector-j-8.4.0.jar" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web,javafx.swing --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED com.brestats.App
pause
