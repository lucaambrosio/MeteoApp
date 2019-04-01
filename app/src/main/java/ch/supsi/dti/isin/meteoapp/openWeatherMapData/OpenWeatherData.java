package ch.supsi.dti.isin.meteoapp.openWeatherMapData;

import java.util.List;

public class OpenWeatherData {
    private String name;
    private List<Weather> weather;
    private Temperature main;

    public Temperature getTemperature() {
        return main;
    }

    public void setTemperature(Temperature temperature) {
        this.main = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
