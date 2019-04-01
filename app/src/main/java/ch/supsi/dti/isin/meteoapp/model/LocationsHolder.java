package ch.supsi.dti.isin.meteoapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.database.DatabaseHelper;
import ch.supsi.dti.isin.meteoapp.database.DbSchema;
import ch.supsi.dti.isin.meteoapp.database.LocationsCursorWrapper;
import ch.supsi.dti.isin.meteoapp.openWeatherMapData.OpenWeatherData;

public class LocationsHolder {

    private static SQLiteDatabase mDatabase;
    private static LocationsHolder sLocationsHolder;
    private List<Location> mLocations;

    public static LocationsHolder get(Context context) {
        if (sLocationsHolder == null){
            sLocationsHolder = new LocationsHolder(context);
            mDatabase = new DatabaseHelper(context).getWritableDatabase();
        }

        return sLocationsHolder;
    }

    private LocationsHolder(Context context) {

        mLocations = new ArrayList<>();
        Location currentLocation = new Location();
        currentLocation.setName("Current location");
        mLocations.add(currentLocation);
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

    public void readData() {

        Cursor c = mDatabase.query(DbSchema.LocationsTable.NAME, null, null, null, null, null, null);
        LocationsCursorWrapper cursor = new LocationsCursorWrapper(c);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                this.addLocation(cursor.getEntry());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    public void writeData(Location location){
        ContentValues values = new ContentValues();
        values.put(DbSchema.LocationsTable.Cols.UUID, location.getId().toString());
        values.put(DbSchema.LocationsTable.Cols.NAME, location.getName());

        mDatabase.insert(DbSchema.LocationsTable.NAME, null, values);
    }
}
