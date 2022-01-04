module com.example.toysocialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.toysocialnetworkgui.domain to javafx.base;
    opens com.example.toysocialnetworkgui to javafx.fxml, javafx.base;
    exports com.example.toysocialnetworkgui;
}