package org.example.bazaartracker.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import org.example.bazaartracker.handler.APIHandler;
import org.example.bazaartracker.Screen;
import org.example.bazaartracker.item.Item;
import org.example.bazaartracker.item.QuickStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
                updateProgress(0, 100);
                HttpURLConnection connectionBazaar = APIHandler.getHttpURLConnection("https://api.hypixel.net/v2/skyblock/bazaar");
                HttpURLConnection connectionItem = APIHandler.getHttpURLConnection("https://api.hypixel.net/v2/resources/skyblock/items");
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
                sortedItems.sort((p1, p2) -> Double.compare(p2.getQuickStatus().getELO(), p1.getQuickStatus().getELO()));
                return null;
            }

            @Override
            protected void succeeded() {
                Screen.openNewScene("bazaar-menu");
            }
        };

        progressBar.progressProperty().bind(loadDataTask.progressProperty());
        new Thread(loadDataTask).start();
    }
}
