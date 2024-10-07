import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GETClient {
    private String serverUrl;
    private LamportClock lamportClock;
    private ObjectMapper objectMapper;

    public GETClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.lamportClock = new LamportClock();
        this.objectMapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java GETClient <serverUrl>");
            return;
        }

        String serverUrl = args[0];
        GETClient client = new GETClient(serverUrl);
        client.sendGETRequest();
    }

    //sends GET request to the Aggregation Server
    public void sendGETRequest() {
        lamportClock.incrementClock();  //increment Lamport clock before sending request
        try {
            URL url = new URL(serverUrl + "/weather.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Lamport-Clock", String.valueOf(lamportClock.getClock()));

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //parse the response JSON
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                displayData(response.toString());
            } else {
                System.out.println("Failed to retrieve data. Response code: " + responseCode);
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //display weather data in user-friendly format
    private void displayData(String jsonData) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            System.out.println("Weather Data:");
            jsonNode.fields().forEachRemaining(field -> {
                System.out.println(field.getKey() + ": " + field.getValue().asText());
            });
        } catch (IOException e) {
            System.out.println("Error parsing JSON response.");
            e.printStackTrace();
        }
    }
}
