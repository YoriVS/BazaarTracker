package org.example.bazaartracker.handler;

import org.example.bazaartracker.item.AddValueToDataset;
import org.example.bazaartracker.item.ItemData;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;

public class DatasetHandler {
    public static DefaultCategoryDataset createDataset(ArrayList<ItemData> itemsData, String param, AddValueToDataset addValueToDataset) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String columnKey;
        for (ItemData itemData : itemsData) {
            columnKey = getColumnKey(param, itemData);

            addValueToDataset.addValue(dataset, itemData, columnKey);
        }
        return dataset;
    }

    private static String getColumnKey(String param, ItemData itemData) {
        String columnKey;
        columnKey = switch (param) {
            case "Day" -> String.valueOf(itemData.getDate().toLocalTime().getHour());
            case "Week" -> String.valueOf(itemData.getDate().toLocalDate().getDayOfWeek());
            case "Month" -> String.valueOf(itemData.getDate().toLocalDate().getMonth());
            default -> itemData.getDate().toLocalDate().toString();
        };
        return columnKey;
    }
}
