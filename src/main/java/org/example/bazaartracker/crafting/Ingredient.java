package org.example.bazaartracker.crafting;

import org.example.bazaartracker.item.QuickStatus;

public class Ingredient {
    String name;
    int amount;
    double instaBuyPrice;
    double buyOrder;

    public Ingredient(String name, int amount, double instaBuyPrice, double buyOrder) {
        this.name = name;
        this.amount = amount;
        this.instaBuyPrice = instaBuyPrice;
        this.buyOrder = buyOrder;
    }

    @Override
    public String toString() {
        return "Ingredient [name=" + name + ", amount=" + amount + "]";
    }

    public String getName() {
        return name.replaceAll("_", " ");
    }

    public int getAmount() {
        return amount;
    }

    public double getInstaBuyPrice() {
        return instaBuyPrice;
    }

    public double getBuyOrder() {
        return buyOrder;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setInstaBuyPrice(double instaBuyPrice) {
        this.instaBuyPrice = instaBuyPrice;
    }

    public void setBuyOrder(double buyOrder) {
        this.buyOrder = buyOrder;
    }


}
