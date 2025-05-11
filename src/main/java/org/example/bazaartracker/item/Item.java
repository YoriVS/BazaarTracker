package org.example.bazaartracker.item;

import java.util.ArrayList;

public class Item {
    public static ArrayList<Item> itemList = new ArrayList<>();
    public String name;
    public ArrayList<ItemData> itemData;
    public QuickStatus quickStatus;

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
}
