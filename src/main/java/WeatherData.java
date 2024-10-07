import com.fasterxml.jackson.annotation.JsonProperty;


public class WeatherData {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("state")
    private String state;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lon")
    private double longitude;
    @JsonProperty("local_date_time")
    private String localDateTime;
    @JsonProperty("local_date_time_full")
    private String localDateTimeFull;
    @JsonProperty("air_temp")
    private double airTemperature;
    @JsonProperty("apparent_t")
    private double apparentTemperature;
    @JsonProperty("cloud")
    private String cloud;
    @JsonProperty("dewpt")
    private double dewPoint;
    @JsonProperty("press")
    private double pressure;
    @JsonProperty("rel_hum")
    private double relativeHumidity;
    @JsonProperty("wind_dir")
    private String windDirection;
    @JsonProperty("wind_spd_kmh")
    private int windSpeedKmh;
    @JsonProperty("wind_spd_kt")
    private int windSpeedKt;

    //store the timestamp of when this data was received (for expiration)
    private long timestamp;

    //constructor for JSON deserialization
    public WeatherData() {
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getLocalDateTimeFull() {
        return localDateTimeFull;
    }

    public void setLocalDateTimeFull(String localDateTimeFull) {
        this.localDateTimeFull = localDateTimeFull;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public void setWindSpeedKmh(int windSpeedKmh) {
        this.windSpeedKmh = windSpeedKmh;
    }

    public int getWindSpeedKt() {
        return windSpeedKt;
    }

    public void setWindSpeedKt(int windSpeedKt) {
        this.windSpeedKt = windSpeedKt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
