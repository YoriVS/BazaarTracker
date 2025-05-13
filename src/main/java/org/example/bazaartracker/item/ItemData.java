package org.example.bazaartracker.item;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ItemData {
    private LocalDateTime date;
    private double sellPrice;
    private double buyPrice;
    private int elo;

    public ItemData(LocalDateTime Date, double sellPrice, double buyPrice, int elo) {
        this.date = Date;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.elo = elo;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public int getElo() {
        return elo;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
