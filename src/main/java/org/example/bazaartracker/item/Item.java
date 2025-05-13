package org.example.bazaartracker.item;

import java.util.ArrayList;

public class Item {
    private static final ArrayList<Item> itemList = new ArrayList<>();
    private String name;
    private ArrayList<ItemData> itemData;
    private QuickStatus quickStatus;

    public Item(String name, ArrayList<ItemData> itemData, QuickStatus quickStatus) {
        this.name = name;
        this.itemData = itemData;
        this.quickStatus = quickStatus;
        itemList.add(this);
    }

    @Override
    public String toString() {
        String newName = name.toLowerCase();
        newName = newName.substring(0, 1).toUpperCase() + newName.substring(1);
        return newName.replaceAll("_", " ");
    }

    public static ArrayList<Item> getItemList() {
        return itemList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ItemData> getItemData() {
        return itemData;
    }

    public QuickStatus getQuickStatus() {
        return quickStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemData(ArrayList<ItemData> itemData) {
        this.itemData = itemData;
    }

    public void setQuickStatus(QuickStatus quickStatus) {
        this.quickStatus = quickStatus;
    }
}
