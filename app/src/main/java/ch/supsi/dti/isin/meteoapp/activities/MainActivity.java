package ch.supsi.dti.isin.meteoapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationsHolder.get(this).readData();
    }
}
