package org.example.bazaartracker;

import java.net.HttpURLConnection;
import java.net.URL;

public class APIHandler {
    public static HttpURLConnection getHttpURLConnection(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            return connection;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

}
