package org.example.bazaartracker.item;

import org.jfree.data.category.DefaultCategoryDataset;

@FunctionalInterface
public interface AddValueToDataset {
    void addValue(DefaultCategoryDataset dataset, ItemData itemData, String columnKey);
}
