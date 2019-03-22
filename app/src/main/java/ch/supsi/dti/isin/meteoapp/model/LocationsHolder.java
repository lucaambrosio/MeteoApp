package ch.supsi.dti.isin.meteoapp.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.OpenWeatherMapData.OpenWeatherData;

public class LocationsHolder {

    private static LocationsHolder sLocationsHolder;
    private List<Location> mLocations;

    public static LocationsHolder get(Context context) {
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder(context);

        return sLocationsHolder;
    }

    private LocationsHolder(Context context) {
        mLocations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Location location = new Location();
            location.setName("Location # " + i);
            mLocations.add(location);
        }
    }

    public void addLocation(Location location){
        mLocations.add(location);
    }

    public void updateCurrentLocation(OpenWeatherData locationParsed){
        mLocations.get(0).setName(locationParsed.getName());
        mLocations.get(0).setmIcon(locationParsed.getWeather().get(0).getIcon());
        mLocations.get(0).setmDescription(locationParsed.getWeather().get(0).getDescription());
        mLocations.get(0).setMtemp(locationParsed.getTemperature().getTemp());
        mLocations.get(0).setMtemp_min(locationParsed.getTemperature().getTemp_min());
        mLocations.get(0).setMtemp_max(locationParsed.getTemperature().getTemp_max());
    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public Location getLocation(UUID id) {
        for (Location location : mLocations) {
            if (location.getId().equals(id))
                return location;
        }

        return null;
    }
}
