package org.example.bazaartracker.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.bazaartracker.Bazaar;
import org.example.bazaartracker.Instance;
import org.example.bazaartracker.Screen;
import org.example.bazaartracker.crafting.Ingredient;
import org.example.bazaartracker.crafting.Recipe;
import org.example.bazaartracker.item.Item;
import org.example.bazaartracker.item.ItemData;
import org.example.bazaartracker.item.QuickStatus;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
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
        buyPriceLabel.setText(currentItem.quickStatus.buyPrice + "$");
        buyOrdersLabel.setText(currentItem.quickStatus.buyOrders + "");
        buyMovingWeekLabel.setText(currentItem.quickStatus.buyMovingWeek + "");
        sellPriceLabel.setText(currentItem.quickStatus.sellPrice + "$");
        sellOrdersLabel.setText(currentItem.quickStatus.sellOrders + "");
        sellMovingWeekLabel.setText(currentItem.quickStatus.sellMovingWeek + "");
        profitLabel.setText(currentItem.quickStatus.profit + "$");
        eloLabel.setText(currentItem.quickStatus.elo + "");
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
            profit = currentItem.quickStatus.profit;
            cost = currentItem.quickStatus.sellPrice;
        } else {
            profit = currentItem.quickStatus.buyPrice - Double.parseDouble(buyOrderTotalCraftCostLabel.getText());
            cost = Double.parseDouble(buyOrderTotalCraftCostLabel.getText());
        }

        if (moneyToInvestTextField.isDisable()) {
            totalProfit = BigDecimal.valueOf(
                            profit * Double.parseDouble(bulkAmountTextField.getText()))
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();

        } else {
            double totalAmount = BigDecimal.valueOf(Math.ceil(Double.parseDouble(moneyToInvestTextField.getText()) / cost))
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
        bulkTotalProfitLabel.setText("Total Profit for Amount: " + (totalProfit + "$"));
        moneyToInvestTextField.setText(totalCost + "");
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
        Screen.changeScreen("bazaar-menu");
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
        JSONObject recipes = loadJsonObject("/JSON/InternalNameMappings.json");
        JSONObject item = (JSONObject) recipes.get(currentItem.name);
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
            A1 = createIngredient( (String) recipe.get("A1"));
            A2 = createIngredient( (String) recipe.get("A2"));
            A3 = createIngredient( (String) recipe.get("A3"));
            B1 = createIngredient( (String) recipe.get("B1"));
            B2 = createIngredient( (String) recipe.get("B2"));
            B3 = createIngredient( (String) recipe.get("B3"));
            C1 = createIngredient( (String) recipe.get("C1"));
            C2 = createIngredient( (String) recipe.get("C2"));
            C3 = createIngredient( (String) recipe.get("C3"));
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

        instaBuyTotalCraftCostLabel.setText(String.valueOf(totalRawCraftCostInstaBuy));
        buyOrderTotalCraftCostLabel.setText(String.valueOf(new BigDecimal(totalRawCraftCostBuyOrder).setScale(2, RoundingMode.HALF_EVEN)));
    }

    private Ingredient createIngredient(String name) {
        if (name.isEmpty()) {
            return null;
        }
        String[] splitName = name.split(":");
        JSONObject rawProducts = (JSONObject) bazaarObject.get("products");
        JSONObject itemData = (JSONObject) rawProducts.get(splitName[0]);
        JSONObject jsonQuickStatus = (JSONObject) itemData.get("quick_status");
        QuickStatus quickStatus = QuickStatus.createQuickStatus(jsonQuickStatus);

        String id = splitName[0];
        int number = Integer.parseInt(splitName[1]);
        double instaBuyPrice = quickStatus.buyPrice * number;
        double buyOrderPrice = quickStatus.sellPrice * number;
        return new Ingredient(id, number, instaBuyPrice, buyOrderPrice);
    }

    private void openPopup(String message) {
        Stage popup = new Stage();
        popup.setTitle("Error");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #00FFCC;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.close());
        closeButton.setStyle(
                "-fx-background-color: #0D1B2A; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 8 16 8 16;"
        );

        VBox popupLayout = new VBox(20); // spacing between elements
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(20, 0, 0, 0));
        popupLayout.setStyle("-fx-background-color: #0D1B2A; -fx-border-radius: 10; -fx-background-radius: 10;");

        popupLayout.getChildren().addAll(messageLabel, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }

    private void showGraph(String selection) {
        ArrayList<ItemData> itemData = new ArrayList<>();
        String selectedItem = graphTimeRangeChoiceBox.getSelectionModel().getSelectedItem().toString();
        LocalDate date = LocalDate.now();

        if (currentItem == null) {
            openPopup("Please select a item");
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
                    if (currentItem.name.equals(item.name)) {
                        for (ItemData dataDate : item.itemData) {
                            if (dataDate.date.toLocalDate().toString().equals(date.toString())) {
                                itemData.add(dataDate);
                            }
                        }
                    }
                }
                break;

            case "Always":
                for (Item item : items) {
                    if (currentItem.name.equals(item.name)) {
                        itemData.addAll(item.itemData);
                    }
                }
                firstDate = itemData.getFirst().date.toLocalDate();
                for (ItemData data : itemData) {
                    pass = !data.date.toLocalDate().equals(firstDate);
                }

        }
        if (pass) {
            if (selection.equals("price")) {
                createChart(createDataset(itemData, selectedItem), currentItem.toString());
            } else if (selection.equals("elo")) {
                createChart(createDatasetElo(itemData, selectedItem), currentItem.toString());
            }
        } else {
            openPopup("Not enough data");
        }
    }

    public void createChart(DefaultCategoryDataset dataset, String title) {
        Color TRADER_BACKGROUND_DARK = new Color(0x0D1B2A); // Root pane background
        Color TRADER_BACKGROUND_SECTION = new Color(0x102030); // Info section box background
        Color TRADER_TEXT_PRIMARY = new Color(0xD0E8F0);       // data-value-label
        Color TRADER_TEXT_SECONDARY = new Color(0xA0BCC8);     // data-label
        Color TRADER_ACCENT_TEAL = new Color(0x33FFC7);        // important-value / bright teal
        Color TRADER_GRIDLINE_BORDER = new Color(0x2A4A68);    // Subtle border / gridline
        Color TRADER_ACCENT_SECONDARY_LINE = new Color(0x50A0B8); // A desaturated blue/teal
        Font CHART_TITLE_FONT = new Font("Roboto", Font.BOLD, 16); // Or "Arial"
        Font AXIS_LABEL_FONT = new Font("Roboto", Font.PLAIN, 12);
        Font AXIS_TICK_LABEL_FONT = new Font("Roboto", Font.PLAIN, 10);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Line Chart - " + title);
            frame.getContentPane().setBackground(TRADER_BACKGROUND_DARK); // Match main app background

            JFreeChart chart = ChartFactory.createLineChart(
                    title,      // Chart title
                    "Date",     // Domain axis label
                    "Coins",    // Range axis label
                    dataset
            );

            chart.setBackgroundPaint(TRADER_BACKGROUND_SECTION); // Chart background slightly lighter
            if (chart.getTitle() != null) {
                chart.getTitle().setPaint(TRADER_ACCENT_TEAL); // Title text in accent teal
                chart.getTitle().setFont(CHART_TITLE_FONT);
            }

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(TRADER_BACKGROUND_SECTION); // Plot area background
            plot.setDomainGridlinePaint(TRADER_GRIDLINE_BORDER);
            plot.setRangeGridlinePaint(TRADER_GRIDLINE_BORDER);
            plot.setOutlinePaint(TRADER_GRIDLINE_BORDER.darker()); // Darker outline

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setTickLabelPaint(TRADER_TEXT_SECONDARY);
            rangeAxis.setTickLabelFont(AXIS_TICK_LABEL_FONT);
            rangeAxis.setLabelPaint(TRADER_TEXT_PRIMARY);
            rangeAxis.setLabelFont(AXIS_LABEL_FONT);

            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setTickLabelPaint(TRADER_TEXT_SECONDARY);
            domainAxis.setTickLabelFont(AXIS_TICK_LABEL_FONT);
            domainAxis.setLabelPaint(TRADER_TEXT_PRIMARY);
            domainAxis.setLabelFont(AXIS_LABEL_FONT);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

            Color[] seriesColors = {
                    TRADER_ACCENT_TEAL,             // Primary series color
                    TRADER_ACCENT_SECONDARY_LINE,   // Secondary if needed, or another distinct color
                    new Color(0xFF8C00)             // Example: Dark Orange for a third distinct series
            };

            for (int i = 0; i < dataset.getRowCount(); i++) {
                Color currentColor = seriesColors[i % seriesColors.length]; // Cycle through defined colors
                renderer.setSeriesPaint(i, currentColor);
                renderer.setSeriesStroke(i, new BasicStroke(2.0f)); // Slightly thinner for cleaner look
                renderer.setSeriesShapesVisible(i, true);
                renderer.setSeriesShape(i, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0)); // Slightly smaller shapes
            }
            renderer.setDrawOutlines(true); // For shapes

            plot.setRenderer(renderer);

            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (int row = 0; row < dataset.getRowCount(); row++) {
                for (int col = 0; col < dataset.getColumnCount(); col++) {
                    Number value = dataset.getValue(row, col);
                    if (value != null) {
                        double v = value.doubleValue();
                        if (v < min) min = v;
                        if (v > max) max = v;
                    }
                }
            }
            if (min != Double.MAX_VALUE && max != Double.MIN_VALUE) { // Ensure data was found
                double margin = (max - min) == 0 ? Math.abs(max * 0.1) + 1 : (max - min) * 0.10; // Handle case where max == min
                if (margin == 0 && max == 0) margin = 1; // Handle case where all values are 0
                else if (margin == 0) margin = Math.abs(max*0.1) +1;


                rangeAxis.setRange(min - margin, max + margin);
            }


            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 450)); // Slightly larger for better viewing
            chartPanel.setBackground(TRADER_BACKGROUND_DARK); // Match JFrame content pane

            chartPanel.setMouseWheelEnabled(true); // Optional: enable mouse wheel zooming

            frame.add(chartPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private DefaultCategoryDataset createDataset(ArrayList<ItemData> itemsData, String param) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String columnKey;

        for (ItemData itemData : itemsData) {
            switch (param) {
                case "Day":
                    columnKey = String.valueOf(itemData.date.toLocalTime().getHour());
                    break;

                default:
                    columnKey = itemData.date.toLocalDate().toString();
                    break;
            }
            dataset.addValue(itemData.buyPrice, "Buy", columnKey);
            dataset.addValue(itemData.sellPrice, "Sell", columnKey);
        }

        return dataset;
    }

    private DefaultCategoryDataset createDatasetElo(ArrayList<ItemData> itemsData, String param) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String columnKey;

        for (ItemData itemData : itemsData) {
            switch (param) {
                case "Day":
                    columnKey = String.valueOf(itemData.date.toLocalTime().getHour());
                    break;

                default:
                    columnKey = itemData.date.toLocalDate().toString();
                    break;
            }
            dataset.addValue(itemData.elo, "Elo", columnKey);
        }

        return dataset;
    }

    private JSONObject loadJsonObject(String resourcePath) {
        InputStream inputStream = Bazaar.class.getResourceAsStream(resourcePath);
        try {
            assert inputStream != null;
            try (Reader reader = new InputStreamReader(inputStream)) {
                return (JSONObject) Instance.parser.parse(reader);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Could not parse JSON from resource '" + resourcePath + "'. Details: " + e.getMessage());
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
