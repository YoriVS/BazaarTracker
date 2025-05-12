package org.example.bazaartracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Objects;

import static org.example.bazaartracker.Bazaar.currentStage;

public class Screen {

    public static void changeScreen(String screenName, Stage stage) {
        try {
            FXMLLoader fxmlLoader = screenName.contains(".fxml") ? new FXMLLoader(Bazaar.class.getResource(screenName)) : new FXMLLoader(Bazaar.class.getResource(screenName + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("Bazaar Tracker");

            Image icon = new Image(Objects.requireNonNull(Bazaar.class.getResourceAsStream("BazaarTracker.ico")));
            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Screen.openPopup("Failed to open screen");
        }
    }

    public static void openNewScene(String screenName) {
        try {
            currentStage.close();

            FXMLLoader fxmlLoader = screenName.contains(".fxml") ? new FXMLLoader(Bazaar.class.getResource(screenName)) : new FXMLLoader(Bazaar.class.getResource(screenName + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = currentStage;

            stage.setTitle("Bazaar Tracker");

            Image icon = new Image(Objects.requireNonNull(Bazaar.class.getResourceAsStream("BazaarTracker.ico")));
            stage.getIcons().add(icon);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Screen.openPopup("Failed to open window");
        }
    }

    public static void openPopup(String message) {
        Stage popup = new Stage();
        popup.setTitle("Error");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #E8F8F8;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.close());
        closeButton.setStyle(
                        "-fx-background-color: #28A7A1; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 8 16 8 16;"
        );

        VBox popupLayout = new VBox(20);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(20, 0, 0, 0));
        popupLayout.setStyle("-fx-background-color: #102030; -fx-border-radius: 10; -fx-background-radius: 10;");

        popupLayout.getChildren().addAll(messageLabel, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 200);
        popup.setScene(popupScene);
        popup.show();
    }

    public static void createChart(DefaultCategoryDataset dataset, String title) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Line Chart - " + title);
            frame.getContentPane().setBackground(Instance.TRADER_BACKGROUND_DARK);

            JFreeChart chart = ChartFactory.createLineChart(
                    title,
                    "Date",
                    "Coins",
                    dataset
            );

            chart.setBackgroundPaint(Instance.TRADER_BACKGROUND_SECTION);
            if (chart.getTitle() != null) {
                chart.getTitle().setPaint(Instance.TRADER_ACCENT_TEAL);
                chart.getTitle().setFont(Instance.CHART_TITLE_FONT);
            }

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Instance.TRADER_BACKGROUND_SECTION);
            plot.setDomainGridlinePaint(Instance.TRADER_GRIDLINE_BORDER);
            plot.setRangeGridlinePaint(Instance.TRADER_GRIDLINE_BORDER);
            plot.setOutlinePaint(Instance.TRADER_GRIDLINE_BORDER.darker());

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setTickLabelPaint(Instance.TRADER_TEXT_SECONDARY);
            rangeAxis.setTickLabelFont(Instance.AXIS_TICK_LABEL_FONT);
            rangeAxis.setLabelPaint(Instance.TRADER_TEXT_PRIMARY);
            rangeAxis.setLabelFont(Instance.AXIS_LABEL_FONT);

            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setTickLabelPaint(Instance.TRADER_TEXT_SECONDARY);
            domainAxis.setTickLabelFont(Instance.AXIS_TICK_LABEL_FONT);
            domainAxis.setLabelPaint(Instance.TRADER_TEXT_PRIMARY);
            domainAxis.setLabelFont(Instance.AXIS_LABEL_FONT);

            LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

            Color[] seriesColors = {
                    Instance.TRADER_ACCENT_TEAL,
                    Instance.TRADER_ACCENT_SECONDARY_LINE,
                    new Color(0xFF8C00)
            };

            for (int i = 0; i < dataset.getRowCount(); i++) {
                Color currentColor = seriesColors[i % seriesColors.length];
                renderer.setSeriesPaint(i, currentColor);
                renderer.setSeriesStroke(i, new BasicStroke(2.0f));
                renderer.setSeriesShapesVisible(i, true);
                renderer.setSeriesShape(i, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
            }
            renderer.setDrawOutlines(true);

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
            if (min != Double.MAX_VALUE && max != Double.MIN_VALUE) {
                double margin = (max - min) == 0 ? Math.abs(max * 0.1) + 1 : (max - min) * 0.10;
                if (margin == 0) margin = Math.abs(max*0.1) +1;


                rangeAxis.setRange(min - margin, max + margin);
            }


            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 450));
            chartPanel.setBackground(Instance.TRADER_BACKGROUND_DARK);

            chartPanel.setMouseWheelEnabled(true);

            frame.add(chartPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
