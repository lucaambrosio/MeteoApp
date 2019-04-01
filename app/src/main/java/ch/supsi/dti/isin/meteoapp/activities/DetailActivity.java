package ch.supsi.dti.isin.meteoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.fragments.DetailLocationFragment;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_LOCATION_ID = "ch.supsi.dti.isin.meteoapp.location_id";
    private ViewPager mViewPager;
    private List<Location> mEntries;

    public static Intent newIntent(Context packageContext, UUID locationId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_LOCATION_ID, locationId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pager);

        UUID entryId = (UUID) getIntent().getSerializableExtra(EXTRA_LOCATION_ID);

        mViewPager = findViewById(R.id.entry_view_pager);

        mEntries = LocationsHolder.get(this).getLocations();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Location entry = mEntries.get(position);
                return DetailLocationFragment.newInstance(entry.getId());
            }
            @Override
            public int getCount() {
                return mEntries.size();
            }
        });

        for (int i = 0; i < mEntries.size(); i++) {
            if (mEntries.get(i).getId().equals(entryId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

//    @Override
//    protected Fragment createFragment() {
//        UUID locationId = (UUID) getIntent().getSerializableExtra(EXTRA_LOCATION_ID);
//        return new DetailLocationFragment().newInstance(locationId);
//    }
}
