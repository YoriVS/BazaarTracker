module org.example.bazaartracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires java.logging;


    opens org.example.bazaartracker to javafx.fxml;
    exports org.example.bazaartracker;
    exports org.example.bazaartracker.item;
    opens org.example.bazaartracker.item to javafx.fxml;
    exports org.example.bazaartracker.controller;
    opens org.example.bazaartracker.controller to javafx.fxml;
}