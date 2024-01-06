module com.cgvsu.simple3dviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens com.cgvsu to javafx.fxml;
    exports com.cgvsu;
}