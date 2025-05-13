package org.example.bazaartracker.handler;

import org.example.bazaartracker.Bazaar;
import org.example.bazaartracker.Instance;
import org.example.bazaartracker.Screen;
import org.example.bazaartracker.item.ItemData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JSONHandler {
    public static JSONObject loadJsonObject(String resourcePath) {
        InputStream inputStream = Bazaar.class.getResourceAsStream(resourcePath);
        try {
            assert inputStream != null;
            try (Reader reader = new InputStreamReader(inputStream)) {
                return (JSONObject) Instance.parser.parse(reader);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Could not parse JSON from resource '" + resourcePath + "'. Details: " + e.getMessage());
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static JSONArray loadJsonArray(String resourcePath) {
        InputStream inputStream = Bazaar.class.getResourceAsStream(resourcePath);
        try {
            assert inputStream != null;
            try (Reader reader = new InputStreamReader(inputStream)) {
                return (JSONArray) Instance.parser.parse(reader);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Could not parse JSON from resource '" + resourcePath + "'. Details: " + e.getMessage());
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static ArrayList<ItemData> getItemDataFromJson(String json) {
        String baseUrl = "https://skyblock.yorisign.com/bazaar/price-data/";
        HttpURLConnection connection = APIHandler.getHttpURLConnection(baseUrl + json);
        JSONArray arrivalJSON = null;
        ArrayList<ItemData> itemsData = new ArrayList<>();

        try {
            arrivalJSON = (JSONArray) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
        } catch (Exception _) {
            Screen.openPopup("Failed to parse JSON");
        }

        assert arrivalJSON != null;
        for (Object o : arrivalJSON) {
            JSONObject obj = (JSONObject) o;
            DateTimeFormatter oldFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
            LocalDateTime date = LocalDateTime
                    .parse((String) obj.get("date"), oldFormatter);

            itemsData.add(new ItemData(
                    date,
                    new BigDecimal(((Double) obj.get("sell")).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(),
                    new BigDecimal(((Double) obj.get("buy")).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(),
                    ((Long) obj.get("elo")).intValue()
            ));
        }


        return itemsData;
    }


}
