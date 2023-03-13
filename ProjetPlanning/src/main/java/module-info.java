module com.example.projetplanning {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;


    opens com.example.projetplanning to javafx.fxml;
    exports com.example.projetplanning;
}