package org.example.bazaartracker.item;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ItemData {
    public LocalDateTime date;
    public double sellPrice;
    public double buyPrice;
    public int elo;

    public ItemData(LocalDateTime Date, double sellPrice, double buyPrice, int elo) {
        this.date = Date;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.elo = elo;
    }


}
