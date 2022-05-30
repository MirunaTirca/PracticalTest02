package ro.pub.cs.systems.eim.practicaltest02;

public class WeatherForecastInformation {
    private String temperature;
    private String windSpeed;
    private String humidity;

    public WeatherForecastInformation(String temperature, String windSpeed, String humidity) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    public WeatherForecastInformation() {

    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
