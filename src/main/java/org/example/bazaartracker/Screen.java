package org.example.bazaartracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static org.example.bazaartracker.Bazaar.currentStage;

public class Screen {

    /// Change screen (filename)
    public static void changeScreen(String screenName) {
        try {
            currentStage.close();
            Stage stage = new Stage();
            currentStage = stage;
            FXMLLoader fxmlLoader = screenName.contains(".fxml") ? new FXMLLoader(Bazaar.class.getResource(screenName)) : new FXMLLoader(Bazaar.class.getResource(screenName + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("Bazaar Tracker");

            Image icon = new Image(Objects.requireNonNull(Bazaar.class.getResourceAsStream("BazaarTracker.ico")));
            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
