package org.example.bazaartracker.crafting;

import org.example.bazaartracker.item.QuickStatus;
import org.json.simple.JSONObject;

import static org.example.bazaartracker.controller.LoadingScreen.bazaarObject;

public class Ingredient {
    private String name;
    private int amount;
    private double instaBuyPrice;
    private double buyOrder;

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

    public String getID() {
        return name;
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

    public String getName() {
        return name.replaceAll("_", " ");
    }

    public void setName(String name) {}

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setInstaBuyPrice(double instaBuyPrice) {
        this.instaBuyPrice = instaBuyPrice;
    }

    public void setBuyOrder(double buyOrder) {
        this.buyOrder = buyOrder;
    }

    public static Ingredient createIngredient(String name) {
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

}
