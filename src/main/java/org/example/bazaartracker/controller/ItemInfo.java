package org.example.bazaartracker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.example.bazaartracker.Bazaar;
import org.example.bazaartracker.Screen;
import org.example.bazaartracker.crafting.Ingredient;
import org.example.bazaartracker.crafting.Recipe;
import org.example.bazaartracker.handler.DatasetHandler;
import org.example.bazaartracker.handler.JSONHandler;
import org.example.bazaartracker.item.Item;
import org.example.bazaartracker.item.ItemData;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.example.bazaartracker.controller.LoadingScreen.*;
import static org.example.bazaartracker.controller.MenuController.currentItem;

public class ItemInfo {
    @FXML
    private DatePicker graphDatePicker;
    @FXML
    private ChoiceBox graphTimeRangeChoiceBox;
    @FXML
    private TextField moneyToInvestTextField;
    @FXML
    private Label itemNameLabel;
    @FXML
    private Label materialNameLabel1;
    @FXML
    private Label materialPriceLabel1;
    @FXML
    private Label materialNameLabel2;
    @FXML
    private Label materialPriceLabel2;
    @FXML
    private Label materialNameLabel3;
    @FXML
    private Label materialPriceLabel3;
    @FXML
    private Label materialNameLabel4;
    @FXML
    private Label materialPriceLabel4;
    @FXML
    private Label materialNameLabel5;
    @FXML
    private Label materialPriceLabel5;
    @FXML
    private Label materialNameLabel6;
    @FXML
    private Label materialPriceLabel6;
    @FXML
    private Label materialNameLabel7;
    @FXML
    private Label materialPriceLabel7;
    @FXML
    private Label materialNameLabel8;
    @FXML
    private Label materialPriceLabel8;
    @FXML
    private Label materialNameLabel9;
    @FXML
    private Label materialPriceLabel9;
    @FXML
    private Label instaBuyTotalCraftCostLabel;
    @FXML
    private Label buyOrderTotalCraftCostLabel;
    @FXML
    private Label buyPriceLabel;
    @FXML
    private Label sellPriceLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private Label buyOrdersLabel;
    @FXML
    private Label sellOrdersLabel;
    @FXML
    private Label buyMovingWeekLabel;
    @FXML
    private Label sellMovingWeekLabel;
    @FXML
    private Label eloLabel;
    @FXML
    private TextField bulkAmountTextField;
    @FXML
    private CheckBox craftFlipCheckBox;
    @FXML
    private Label bulkTotalProfitLabel;
    @FXML
    private Button showPriceGraphButton;
    @FXML
    private Button showVolumeGraphButton;

    @FXML
    private void initialize() {
        craft();
        graphDatePicker.setValue(LocalDate.now());
        graphTimeRangeChoiceBox.setValue("Always");
        graphTimeRangeChoiceBox.getItems().add("Day");
        graphTimeRangeChoiceBox.getItems().add("Week");
        graphTimeRangeChoiceBox.getItems().add("Month");
        graphTimeRangeChoiceBox.getItems().add("Always");
        itemNameLabel.setText(currentItem.toString());
        buyPriceLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getBuyPrice()) + "$");
        buyOrdersLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getBuyOrders()));
        buyMovingWeekLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getBuyMovingWeek()));
        sellPriceLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getSellPrice()) + "$");
        sellOrdersLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getSellOrders()));
        sellMovingWeekLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getSellMovingWeek()));
        profitLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getProfit()) + "$");
        eloLabel.setText(Screen.createPrettyNumber(currentItem.getQuickStatus().getELO()));
        craftFlipCheckBox.setSelected(false);
    }

    @FXML
    private void handleCalculateBulkAction() {
        double profit;
        double cost;
        double totalProfit;
        double totalCost;

        if (bulkAmountTextField.getText().isEmpty() && moneyToInvestTextField.getText().isEmpty()) {
            return;
        }

        if (!craftFlipCheckBox.isSelected()) {
            profit = currentItem.getQuickStatus().getProfit();
            cost = currentItem.getQuickStatus().getSellPrice();
        } else {
            profit = currentItem.getQuickStatus().getBuyPrice() - Double.parseDouble(buyOrderTotalCraftCostLabel.getText());
            cost = Double.parseDouble(buyOrderTotalCraftCostLabel.getText());
        }

        if (moneyToInvestTextField.isDisable()) {
            totalProfit = BigDecimal.valueOf(
                            profit * Double.parseDouble(bulkAmountTextField.getText()))
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();

        } else {
            double totalAmount = BigDecimal.valueOf(Math.ceil(Screen.createNumberBasedOnLetter(moneyToInvestTextField.getText()) / cost))
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();
            totalProfit = BigDecimal.valueOf(profit * totalAmount)
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();
            bulkAmountTextField.setText(String.valueOf(totalAmount));
        }

        totalCost = BigDecimal.valueOf(
                        cost * Double.parseDouble(bulkAmountTextField.getText()))
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();

        bulkTotalProfitLabel.setText("Total Profit for Amount: " + (Screen.createPrettyNumber(totalProfit) + "$"));
        moneyToInvestTextField.setText(Screen.createPrettyNumber(totalCost));
    }

    @FXML
    private void handleShowPriceGraph() {
        showGraph("price");
    }

    @FXML
    private void handleShowVolumeGraph() {
        showGraph("elo");
    }

    @FXML
    private void handleHomeButtonAction() {
        Screen.changeScreen("bazaar-menu", Bazaar.currentStage);
    }

    @FXML
    public void handleBulkTextFieldKey(KeyEvent keyEvent) {
        TextField textField = (TextField) keyEvent.getSource();
        if (textField.getText().isEmpty() && keyEvent.getText().isEmpty()) {
            moneyToInvestTextField.setDisable(false);
            bulkAmountTextField.setDisable(false);
            return;
        }
        if (textField.equals(bulkAmountTextField)) {
            moneyToInvestTextField.setDisable(true);
            moneyToInvestTextField.clear();
        }
        if (textField.equals(moneyToInvestTextField)) {
            bulkAmountTextField.setDisable(true);
            bulkAmountTextField.clear();
        }
    }

    private void craft() {
        JSONObject recipes = JSONHandler.loadJsonObject("/JSON/InternalNameMappings.json");
        JSONObject item = (JSONObject) recipes.get(currentItem.getName());
        if (item == null) {
            return;
        }

        JSONObject recipe = (JSONObject) item.get("recipe");
        Ingredient A1 = null;
        Ingredient A2 = null;
        Ingredient A3 = null;
        Ingredient B1 = null;
        Ingredient B2 = null;
        Ingredient B3 = null;
        Ingredient C1 = null;
        Ingredient C2 = null;
        Ingredient C3 = null;

        if (recipe != null) {
            A1 = Ingredient.createIngredient( (String) recipe.get("A1"));
            A2 = Ingredient.createIngredient( (String) recipe.get("A2"));
            A3 = Ingredient.createIngredient( (String) recipe.get("A3"));
            B1 = Ingredient.createIngredient( (String) recipe.get("B1"));
            B2 = Ingredient.createIngredient( (String) recipe.get("B2"));
            B3 = Ingredient.createIngredient( (String) recipe.get("B3"));
            C1 = Ingredient.createIngredient( (String) recipe.get("C1"));
            C2 = Ingredient.createIngredient( (String) recipe.get("C2"));
            C3 = Ingredient.createIngredient( (String) recipe.get("C3"));
        }

        Recipe itemRecipe = new Recipe(A1, A2, A3, B1, B2, B3, C1, C2, C3);

        ArrayList<Ingredient> consolidatedIngredients = new ArrayList<>();

        for (Ingredient recipeIngredient : itemRecipe.getIngredients()) {
            if (recipeIngredient == null) {
                continue;
            }

            boolean foundAndUpdated = false;
            for (Ingredient existingIngredient : consolidatedIngredients) {
                if (existingIngredient.getName().equals(recipeIngredient.getName())) {
                    existingIngredient.setAmount(existingIngredient.getAmount() + recipeIngredient.getAmount());
                    existingIngredient.setBuyOrder(existingIngredient.getBuyOrder() + recipeIngredient.getBuyOrder());
                    existingIngredient.setInstaBuyPrice(existingIngredient.getInstaBuyPrice() + recipeIngredient.getInstaBuyPrice());

                    foundAndUpdated = true;
                    break;
                }
            }

            if (!foundAndUpdated) {
                consolidatedIngredients.add(recipeIngredient);
            }
        }

        while (consolidatedIngredients.size() < 9) {
            consolidatedIngredients.add(null);
        }

        materialNameLabel1.setText(consolidatedIngredients.get(0) != null ? "- " + consolidatedIngredients.get(0).getName() : "");
        materialNameLabel1.setText(!consolidatedIngredients.isEmpty() && consolidatedIngredients.get(0) != null ? "- " + consolidatedIngredients.get(0).getName() : "");
        materialNameLabel2.setText(consolidatedIngredients.size() > 1 && consolidatedIngredients.get(1) != null ? "- " + consolidatedIngredients.get(1).getName() : "");
        materialNameLabel3.setText(consolidatedIngredients.size() > 2 && consolidatedIngredients.get(2) != null ? "- " + consolidatedIngredients.get(2).getName() : "");
        materialNameLabel4.setText(consolidatedIngredients.size() > 3 && consolidatedIngredients.get(3) != null ? "- " + consolidatedIngredients.get(3).getName() : "");
        materialNameLabel5.setText(consolidatedIngredients.size() > 4 && consolidatedIngredients.get(4) != null ? "- " + consolidatedIngredients.get(4).getName() : "");
        materialNameLabel6.setText(consolidatedIngredients.size() > 5 && consolidatedIngredients.get(5) != null ? "- " + consolidatedIngredients.get(5).getName() : "");
        materialNameLabel7.setText(consolidatedIngredients.size() > 6 && consolidatedIngredients.get(6) != null ? "- " + consolidatedIngredients.get(6).getName() : "");
        materialNameLabel8.setText(consolidatedIngredients.size() > 7 && consolidatedIngredients.get(7) != null ? "- " + consolidatedIngredients.get(7).getName() : "");
        materialNameLabel9.setText(consolidatedIngredients.size() > 8 && consolidatedIngredients.get(8) != null ? "- " + consolidatedIngredients.get(8).getName() : "");

        materialPriceLabel1.setText(!consolidatedIngredients.isEmpty() && consolidatedIngredients.get(0) != null ? String.valueOf(consolidatedIngredients.get(0).getAmount()) : "");
        materialPriceLabel2.setText(consolidatedIngredients.size() > 1 && consolidatedIngredients.get(1) != null ? String.valueOf(consolidatedIngredients.get(1).getAmount()) : "");
        materialPriceLabel3.setText(consolidatedIngredients.size() > 2 && consolidatedIngredients.get(2) != null ? String.valueOf(consolidatedIngredients.get(2).getAmount()) : "");
        materialPriceLabel4.setText(consolidatedIngredients.size() > 3 && consolidatedIngredients.get(3) != null ? String.valueOf(consolidatedIngredients.get(3).getAmount()) : "");
        materialPriceLabel5.setText(consolidatedIngredients.size() > 4 && consolidatedIngredients.get(4) != null ? String.valueOf(consolidatedIngredients.get(4).getAmount()) : "");
        materialPriceLabel6.setText(consolidatedIngredients.size() > 5 && consolidatedIngredients.get(5) != null ? String.valueOf(consolidatedIngredients.get(5).getAmount()) : "");
        materialPriceLabel7.setText(consolidatedIngredients.size() > 6 && consolidatedIngredients.get(6) != null ? String.valueOf(consolidatedIngredients.get(6).getAmount()) : "");
        materialPriceLabel8.setText(consolidatedIngredients.size() > 7 && consolidatedIngredients.get(7) != null ? String.valueOf(consolidatedIngredients.get(7).getAmount()) : "");
        materialPriceLabel9.setText(consolidatedIngredients.size() > 8 && consolidatedIngredients.get(8) != null ? String.valueOf(consolidatedIngredients.get(8).getAmount()) : "");

        double totalRawCraftCostInstaBuy = 0;
        double totalRawCraftCostBuyOrder = 0;

        for (Ingredient ingredient : consolidatedIngredients) { // Changed from 'ingredients'
            if (ingredient != null) {
                totalRawCraftCostInstaBuy += ingredient.getInstaBuyPrice();
                totalRawCraftCostBuyOrder += ingredient.getBuyOrder();
            }
        }

        instaBuyTotalCraftCostLabel.setText(String.valueOf(new BigDecimal(totalRawCraftCostInstaBuy).setScale(2, RoundingMode.HALF_EVEN)));
        buyOrderTotalCraftCostLabel.setText(String.valueOf(new BigDecimal(totalRawCraftCostBuyOrder).setScale(2, RoundingMode.HALF_EVEN)));
    }

    private void showGraph(String selection) {
        ArrayList<ItemData> itemData = new ArrayList<>();
        String selectedItem = graphTimeRangeChoiceBox.getSelectionModel().getSelectedItem().toString();
        LocalDate date = LocalDate.now();

        if (currentItem == null) {
            Screen.openPopup("Please select a item");
            return;
        }

        if (graphDatePicker.getValue() != null) {
            date = graphDatePicker.getValue();
        }
        LocalDate firstDate;
        boolean pass = true;

        switch(selectedItem) {
            case "Day":
                for (Item item : items) {
                    if (currentItem.getName().equals(item.getName())) {
                        for (ItemData dataDate : item.getItemData()) {
                            if (dataDate.getDate().toLocalDate().toString().equals(date.toString())) {
                                itemData.add(dataDate);
                            }
                        }
                    }
                }
                break;

            case "Week":
                for (Item item : items) {
                    if (currentItem.getName().equals(item.getName())) {
                        for (ItemData dataDate : item.getItemData()) {
                            for (int i = 0; i < 7; i++) {
                                if (dataDate.getDate().toLocalDate().toString().equals(date.plusDays(i).toString())) {
                                    itemData.add(dataDate);
                                }
                            }
                        }
                    }
                }
                break;

            case "Month":
                for (Item item : items) {
                    if (currentItem.getName().equals(item.getName())) {
                        for (ItemData dataDate : item.getItemData()) {
                            if (dataDate.getDate().toLocalDate().getMonth().toString().equals(date.getMonth().toString())) {
                                itemData.add(dataDate);
                            }
                        }
                    }
                }
                break;

            case "Always":
                for (Item item : items) {
                    if (currentItem.getName().equals(item.getName())) {
                        itemData.addAll(item.getItemData());
                    }
                }
                firstDate = itemData.getFirst().getDate().toLocalDate();
                for (ItemData data : itemData) {
                    pass = !data.getDate().toLocalDate().equals(firstDate);
                }
                break;

        }
        if (itemData.isEmpty() || itemData.size() == 1) {
            pass = false;
        }

        if (pass) {
            if (selection.equals("price")) {
                DefaultCategoryDataset datasetPrice = DatasetHandler.createDataset(itemData, selectedItem, (ds, data, key) -> {
                    ds.addValue(data.getBuyPrice(), "Buy", key);
                    ds.addValue(data.getSellPrice(), "Sell", key);
                });
                Screen.createChart(datasetPrice, currentItem.toString());
            } else if (selection.equals("elo")) {
                DefaultCategoryDataset datasetElo = DatasetHandler.createDataset(itemData, selectedItem, (ds, data, key) -> {
                    ds.addValue(data.getElo(), "Elo", key);
                });
                Screen.createChart(datasetElo, currentItem.toString());
            }
        } else {
            Screen.openPopup("Not enough data");
        }
    }
}
