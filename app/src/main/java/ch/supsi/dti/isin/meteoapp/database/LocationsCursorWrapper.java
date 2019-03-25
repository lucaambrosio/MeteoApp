package ch.supsi.dti.isin.meteoapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.model.Location;

public class LocationsCursorWrapper extends CursorWrapper {
    public LocationsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Location getEntry() {
        String id = getString(getColumnIndex(DbSchema.LocationsTable.Cols.UUID));
        String name = getString(getColumnIndex(DbSchema.LocationsTable.Cols.NAME));
        return new Location(UUID.fromString(id), name);
    }
}