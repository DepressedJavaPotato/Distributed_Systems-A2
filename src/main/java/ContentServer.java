import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ContentServer {
    private String serverUrl;
    private String filePath;
    private LamportClock lamportClock;
    private ObjectMapper objectMapper;

    public ContentServer(String serverUrl, String filePath) {
        this.serverUrl = serverUrl;
        this.filePath = filePath;
        this.lamportClock = new LamportClock();
        this.objectMapper = new ObjectMapper();
}

public static void main(String[] args) {
    if (args.length < 2) {
        System.out.println("Usage: java ContentServer <serverUrl> <filePath>");
        return;
    }

    String serverUrl = args[0];
    String filePath = args[1];
    ContentServer contentServer = new ContentServer(serverUrl, filePath);
    contentServer.start();
}

public void start() {
    try {
        //read data from the file and convert it to JSON
        WeatherData weatherData = readDataFromFile();
        String json = objectMapper.writeValueAsString(weatherData);

        //send a PUT request to the Aggregation Server
        sendPUTRequest(json);

    } catch (IOException e) {
        e.printStackTrace();
    }
}

  //reads data from the local file and converts it into a WeatherData object
  private WeatherData readDataFromFile() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    WeatherData data = new WeatherData();

    String line;
    while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 2) {
            String key = parts[0].trim();
            String value = parts[1].trim();
            //map key-value pairs to the WeatherData fields (e.g., id, name)
            data.setField(key, value);
        }
    }
    reader.close();
    return data;
}

private void sendPUTRequest(String jsonData) {
    lamportClock.incrementClock();  //increment Lamport clock before sending
    try {
        URL url = new URL(serverUrl + "/weather.json");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Lamport-Clock", String.valueOf(lamportClock.getClock()));
        connection.setRequestProperty("Content-Length", Integer.toString(jsonData.length()));

        //send JSON data
        OutputStream os = connection.getOutputStream();
        os.write(jsonData.getBytes());
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200 || responseCode == 201) {
            System.out.println("Data sent successfully with response code: " + responseCode);
        } else {
            System.out.println("Failed to send data. Response code: " + responseCode);
        }
        connection.disconnect();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}