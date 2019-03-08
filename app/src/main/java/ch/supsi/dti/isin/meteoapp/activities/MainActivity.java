package ch.supsi.dti.isin.meteoapp.activities;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;

import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }

    public void startLocationListener() {
        LocationParams.Builder builder = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(0)
                .setInterval(5000); // 5 sec
        SmartLocation.with(this).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                Log.i("testgps", "Location" + location);
            }
        });
    }
}
