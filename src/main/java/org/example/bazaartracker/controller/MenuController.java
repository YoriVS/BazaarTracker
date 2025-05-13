package org.example.bazaartracker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.example.bazaartracker.Bazaar;
import org.example.bazaartracker.Screen;
import org.example.bazaartracker.handler.JSONHandler;
import org.example.bazaartracker.item.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

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
    public static Item currentItem = null;

    @FXML
    public void initialize() {
        if (currentItem != null) {showItemStats(currentItem);}
        itemsList.getItems().clear();
        itemsList.getItems().addAll(items);
    }

    @FXML
    public void chooseButtonClicked() {
        currentItem = (Item) itemsList.getSelectionModel().getSelectedItem();
        if (currentItem == null) {
            Screen.openPopup("Select an item First");
            return;
        }
        showItemStats(currentItem);
    }

    private void showItemStats(Item item) {
        item.setItemData(JSONHandler.getItemDataFromJson(item.getName() + ".json"));
        itemName.setText(item.toString());
        buyPrice.setText(item.getQuickStatus().getBuyPrice() + "$");
        buyMovingWeek.setText(String.valueOf(item.getQuickStatus().getBuyMovingWeek()));
        buyOrder.setText(String.valueOf(item.getQuickStatus().getBuyOrders()));
        sellPrice.setText(item.getQuickStatus().getSellPrice() + "$");
        sellMovingWeek.setText(String.valueOf(item.getQuickStatus().getSellMovingWeek()));
        sellOrder.setText(String.valueOf(item.getQuickStatus().getSellOrders()));
        elo.setText(String.valueOf(item.getQuickStatus().getELO()));
        profit.setText(String.valueOf(
                BigDecimal.valueOf(item.getQuickStatus().getBuyPrice() - item.getQuickStatus().getSellPrice())
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue()));
    }

    @FXML
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

    @FXML
    public void viewDetailsButtonClicked() {
        if (currentItem == null) {
            Screen.openPopup("Choose an Item");
            return;
        }
        Screen.changeScreen("item-info", Bazaar.currentStage);
    }
}
