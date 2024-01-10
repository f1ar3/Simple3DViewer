module com.cgvsu.simple3dviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens com.cgvsu to javafx.fxml;
    exports com.cgvsu;
    exports com.cgvsu.Math.Coordinate;
    exports com.cgvsu.render_engine.graphicConveyor;
}