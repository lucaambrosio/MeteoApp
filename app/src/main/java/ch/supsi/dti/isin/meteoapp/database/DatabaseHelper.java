package ch.supsi.dti.isin.meteoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meteoApp.db";
    private static final int VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbSchema.LocationsTable.NAME + "("
                        + " _id integer primary key autoincrement, "
                        + DbSchema.LocationsTable.Cols.UUID
                        + ", "
                        + DbSchema.LocationsTable.Cols.NAME
                        + ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}