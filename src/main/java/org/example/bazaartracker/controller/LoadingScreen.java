package org.example.bazaartracker.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import org.example.bazaartracker.Bazaar;
import org.example.bazaartracker.Screen;
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

public class LoadingScreen {
    public static ArrayList<Item> sortedItems = new ArrayList<>();
    public static JSONObject bazaarObject = null;
    public static JSONObject itemObject = null;
    public static ArrayList<Item> items = new ArrayList<>();
    public static ArrayList<String> itemNames;

    @FXML
    private ProgressBar progressBar;

    public void initialize() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Example: simulate steps
                updateProgress(0, 100);
                HttpURLConnection connectionBazaar = getHttpURLConnection("https://api.hypixel.net/v2/skyblock/bazaar");
                HttpURLConnection connectionItem = getHttpURLConnection("https://api.hypixel.net/v2/resources/skyblock/items");
                assert connectionBazaar != null;
                bazaarObject = (JSONObject) new JSONParser().parse(new InputStreamReader(connectionBazaar.getInputStream()));
                itemObject = (JSONObject) new JSONParser().parse(new InputStreamReader(connectionItem.getInputStream()));

                JSONObject rawProducts = (JSONObject) bazaarObject.get("products");
                itemNames = new ArrayList<>(rawProducts.keySet());

                itemNames.sort(String.CASE_INSENSITIVE_ORDER);
                for (int i = 0; i < itemNames.size(); i++) {
                    updateProgress(i + 1, itemNames.size());
                }

                int totalItems = itemNames.size();
                for (int i = 0; i < totalItems; i++) {
                    String item = itemNames.get(i);
                    JSONObject itemData = (JSONObject) rawProducts.get(item);
                    items.add(new Item(item, null, QuickStatus.createQuickStatus((JSONObject) itemData.get("quick_status"))));
                    updateProgress(i + 1, totalItems);
                }

                sortedItems.addAll(items);
                sortedItems.sort((p1, p2) -> Double.compare(p2.quickStatus.elo, p1.quickStatus.elo));
                return null;
            }

            @Override
            protected void succeeded() {
                Screen.changeScreen("bazaar-menu");
            }
        };

        progressBar.progressProperty().bind(loadDataTask.progressProperty());
        new Thread(loadDataTask).start();
    }

    private static HttpURLConnection getHttpURLConnection(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            return connection;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
