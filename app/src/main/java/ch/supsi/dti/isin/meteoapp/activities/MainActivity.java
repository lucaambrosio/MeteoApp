package ch.supsi.dti.isin.meteoapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        startLocationListener();
        return new ListFragment();
    }

    public void startLocationListener() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("tag", "Permission not granted");
// richiedo i permessi
        } else {
            Log.d("tag", "Permission granted");
// leggo la posizione del device
        }

        LocationParams.Builder builder = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(0)
                .setInterval(5000); // 5 sec
        SmartLocation.with(this).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(android.location.Location location) {
                Log.d("testgps", "Location" + location);
            }
        });
    }
}
