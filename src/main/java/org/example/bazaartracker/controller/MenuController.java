package org.example.bazaartracker.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.bazaartracker.APIHandler;
import org.example.bazaartracker.Screen;
import org.example.bazaartracker.item.Item;
import org.example.bazaartracker.item.ItemData;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static org.example.bazaartracker.controller.LoadingScreen.*;

public class MenuController {
    public Label elo;
    public Label buyPrice;
    public Label buyMovingWeek;
    public Label buyOrder;
    public Label sellPrice;
    public Label sellMovingWeek;
    public Label sellOrder;
    public ListView itemsList;
    public Label itemName;
    public Label profit;
    public TextField searchField;
    public ChoiceBox dd_ww_mm;
    public Label topNr1;
    public Label topNr2;
    public Label topNr3;
    public Label topNr4;
    public Label topNr5;
    public DatePicker dayDatePicker;
    static Item currentItem = null;

    @FXML
    public void initialize() {
        itemsList.getItems().clear();
        itemsList.getItems().addAll(items);
    }

    public static ArrayList<ItemData> getItemDataFromJson(String json) {
        String baseUrl = "https://skyblock.yorisign.com/bazaar/price-data/";
        HttpURLConnection connection = APIHandler.getHttpURLConnection(baseUrl + json);
        JSONArray arrivalJSON = null;
        ArrayList<ItemData> itemsData = new ArrayList<>();

        try {
            arrivalJSON = (JSONArray) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));

        } catch (Exception e) {
        }

        assert arrivalJSON != null;
        for (Object o : arrivalJSON) {
            JSONObject obj = (JSONObject) o;
            DateTimeFormatter oldFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
            LocalDateTime date = LocalDateTime
                    .parse((String) obj.get("date"), oldFormatter);

            itemsData.add(new ItemData(
                    date,
                    new BigDecimal(((Double) obj.get("sell")).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(),
                    new BigDecimal(((Double) obj.get("buy")).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(),
                    ((Long) obj.get("elo")).intValue()
            ));
        }


        return itemsData;
    }

    public void chooseButtonClicked() {
        currentItem = (Item) itemsList.getSelectionModel().getSelectedItem();
        currentItem.itemData = getItemDataFromJson(currentItem.name + ".json");
        itemName.setText(currentItem.toString());

        for (Item item: items) {
            if (item.equals(currentItem)) {
                buyPrice.setText(item.quickStatus.buyPrice + "$");
                buyMovingWeek.setText(String.valueOf(item.quickStatus.buyMovingWeek));
                buyOrder.setText(String.valueOf(item.quickStatus.buyOrders));
                sellPrice.setText(item.quickStatus.sellPrice + "$");
                sellMovingWeek.setText(String.valueOf(item.quickStatus.sellMovingWeek));
                sellOrder.setText(String.valueOf(item.quickStatus.sellOrders));
                elo.setText(String.valueOf(item.quickStatus.elo));
                profit.setText(String.valueOf(BigDecimal.valueOf(item.quickStatus.buyPrice - item.quickStatus.sellPrice).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
            }
        }

    }

    public void enterKeyPressed(KeyEvent keyEvent) {
        ArrayList<Item> searchedList = new ArrayList<>();
        String query = searchField.getText();
        if (query != null && !query.isEmpty()) {
            for (Item item : items) {
                if (item.toString().toLowerCase().contains(query.toLowerCase())) {
                    searchedList.add(item);
                }
            }
            itemsList.getItems().clear();
            itemsList.getItems().addAll(searchedList);
        } else {
            itemsList.getItems().addAll(items);
        }
    }

    private void openPopup(String message) {
        Stage popup = new Stage();
        popup.setTitle("Error");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #00FFCC;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.close());
        closeButton.setStyle(
                "-fx-background-color: #00FFCC; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 8 16 8 16;"
        );

        VBox popupLayout = new VBox(20); // spacing between elements
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(20, 0, 0, 0));
        popupLayout.setStyle("-fx-background-color: #2c2f3f; -fx-border-radius: 10; -fx-background-radius: 10;");

        popupLayout.getChildren().addAll(messageLabel, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }

    @FXML
    public void viewDetailsButtonClicked() {
        if (currentItem == null) {
            openPopup("Choose an Item");
            return;
        }
        Screen.changeScreen("item-info");
    }
}
