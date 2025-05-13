package org.example.bazaartracker.item;

import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class QuickStatus {
    private final String productId;
    private final double sellPrice;
    private final int sellVolume;
    private final int sellMovingWeek;
    private final int sellOrders;
    private final double buyPrice;
    private final int buyVolume;
    private final int buyMovingWeek;
    private final int buyOrders;
    private final int elo;
    private final double profit;

    public QuickStatus(String productId, double sellPrice, int sellVolume, int sellMovingWeek, int sellOrders, double buyPrice, int buyVolume, int buyMovingWeek, int buyOrders) {
        this.productId = productId;
        this.sellPrice = sellPrice;
        this.sellVolume = sellVolume;
        this.sellMovingWeek = sellMovingWeek;
        this.sellOrders = sellOrders;
        this.buyPrice = buyPrice;
        this.buyVolume = buyVolume;
        this.buyMovingWeek = buyMovingWeek;
        this.buyOrders = buyOrders;
        elo = defineELO(buyPrice, sellPrice, sellMovingWeek, sellOrders, buyMovingWeek, buyOrders);
        profit = new BigDecimal(buyPrice -  sellPrice).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private static int defineELO(double buyPrice, double sellPrice, int sellMovingWeek, int sellOrders, int buyMovingWeek, int buyOrders) {
        if (sellPrice < 10) sellPrice = 10;

        double profit = buyPrice - sellPrice;
        double roi = Math.min(profit / sellPrice, 1.0);

        double volumeScore = (buyMovingWeek + sellMovingWeek) / 2000.0;
        double competitionScore = (buyOrders + sellOrders) * 0.5;
        double roiScore = roi * 100.0;
        double profitScore = Math.log(Math.max(profit, 1)); // suppress big profits

        double elo = (profitScore * volumeScore) + roiScore - competitionScore;

        return (int) Math.round(elo);
    }

    public static QuickStatus createQuickStatus(JSONObject quickStatus) {
        String productId = (String) quickStatus.get("productId");
        double sellPrice = BigDecimal.valueOf(((Number) quickStatus.get("sellPrice")).doubleValue()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        int sellVolume = ((Number) quickStatus.get("sellVolume")).intValue();
        int sellMovingWeek = ((Number) quickStatus.get("sellMovingWeek")).intValue();
        int sellOrders = ((Number) quickStatus.get("sellOrders")).intValue();
        double buyPrice = BigDecimal.valueOf(((Number) quickStatus.get("buyPrice")).doubleValue()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        int buyVolume = ((Number) quickStatus.get("buyVolume")).intValue();
        int buyMovingWeek = ((Number) quickStatus.get("buyMovingWeek")).intValue();
        int buyOrders = ((Number) quickStatus.get("buyOrders")).intValue();

        return new QuickStatus(productId, sellPrice, sellVolume, sellMovingWeek, sellOrders, buyPrice, buyVolume, buyMovingWeek, buyOrders);
    }

    public String getProductId() {
        return productId;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public int getSellVolume() {
        return sellVolume;
    }

    public int getSellMovingWeek() {
        return sellMovingWeek;
    }

    public int getSellOrders() {
        return sellOrders;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public int getBuyVolume() {
        return buyVolume;
    }

    public int getBuyMovingWeek() {
        return buyMovingWeek;
    }

    public int getBuyOrders() {
        return buyOrders;
    }

    public int getELO() {
        return elo;
    }

    public double getProfit() {
        return profit;
    }
}
