import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Database {
    private String filePath;
    private Map<String, WeatherData> dataStore;
    private ObjectMapper objectMapper;

    public Database() {
        this.dataStore=new HashMap<>();
    }

    public Database(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
        this.dataStore = new HashMap<>();
        loadDataFromFile();

    }
//store weather data in memory and persist to the file
public synchronized void storeData(String id, WeatherData data) {
    dataStore.put(id, data);
    saveDataToFile();
}

//fetch all weather data as a map (for GET requests)
public synchronized Map<String, WeatherData> fetchData() {
    return new HashMap<>(dataStore);  // Return a copy to prevent external modification
}

//remove data for a specific content server ID (used for expiration)
public synchronized void removeData(String id) {
    dataStore.remove(id);
    saveDataToFile();
}

//periodically remove data older than 30 seconds
public synchronized void removeExpiredEntries(long expirationTimeInMillis) {
    long currentTime = System.currentTimeMillis();
    dataStore.entrySet().removeIf(entry ->
        (currentTime - entry.getValue().getTimestamp()) > expirationTimeInMillis
    );
    saveDataToFile();
}

//load data from a JSON file into the in-memory data store
private void loadDataFromFile() {
    try (FileReader reader = new FileReader(filePath)) {
        WeatherData[] dataArray = objectMapper.readValue(reader, WeatherData[].class);
        dataStore.clear();
        for (WeatherData data : dataArray) {
            dataStore.put(data.getId(), data);
        }
        System.out.println("Data loaded from file successfully.");
    } catch (IOException e) {
        System.out.println("Error loading data from file: " + e.getMessage());
    }
}

//save in-memory data store to a JSON file
private void saveDataToFile() {
    try (FileWriter writer = new FileWriter(filePath)) {
        objectMapper.writeValue(writer, dataStore.values());
        System.out.println("Data saved to file successfully.");
    } catch (IOException e) {
        System.out.println("Error saving data to file: " + e.getMessage());
    }
}
}