package ch.supsi.dti.isin.meteoapp.OpenWeatherMapData;

public class Weather {
    String description;
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}