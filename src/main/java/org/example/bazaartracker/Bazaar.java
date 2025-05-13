package org.example.bazaartracker;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.bazaartracker.item.Item;
import org.example.bazaartracker.item.ItemData;
import org.example.bazaartracker.item.QuickStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Bazaar extends Application {
    public static Stage currentStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Bazaar.class.getResource("loading-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Bazaar Tracker");

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("BazaarTracker.ico")));
        currentStage = stage;
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    /// starts the app
    public static void main(String[] args) {
        launch();
    }




}