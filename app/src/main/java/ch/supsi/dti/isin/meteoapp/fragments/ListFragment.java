package ch.supsi.dti.isin.meteoapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;

import ch.supsi.dti.isin.meteoapp.Interface.OnHttpRequestTaskCompleted;
import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.activities.DetailActivity;
import ch.supsi.dti.isin.meteoapp.asyncTask.HttpRequestTask;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.openWeatherMapData.OpenWeatherData;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class ListFragment extends Fragment implements OnHttpRequestTaskCompleted{
    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LocationsHolder.get(getActivity()).readData();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("grantedCheck"," permesso");
            startLocationListener();
        }
    }



    public void startLocationListener() {
            LocationParams.Builder builder = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(0)
                    .setInterval(500); // mezzo sec
            SmartLocation.with(getActivity()).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(android.location.Location location) {

                    HttpRequestTask requestTask = new HttpRequestTask(ListFragment.this);
                    requestTask.execute("https://api.openweathermap.org/data/2.5/weather?lat="+location.getLatitude()
                            +"&lon="+location.getLongitude()+"&units=metric&appid=ed2aa55e4a426aba9a830d295e909a1a");
                }
            });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mLocationRecyclerView = view.findViewById(R.id.recycler_view);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Location> locations = LocationsHolder.get(getActivity()).getLocations();
        mAdapter = new LocationAdapter(locations);
        mLocationRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    public void displayNewLocationDialog(){
        FragmentManager manager = getFragmentManager();
        NewLocationFragment newLocationFragment = new NewLocationFragment();
        newLocationFragment.setTargetFragment(ListFragment.this,0);
        newLocationFragment.show(manager,"fragment_new_location");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == 0){
            String nome = (String) data.getSerializableExtra("name");

            Location location = new Location(nome);

            LocationsHolder.get(getActivity()).addLocation(location);
            LocationsHolder.get(getActivity()).writeData(location);

            mAdapter.notifyDataSetChanged();

            Toast toast = Toast.makeText(getActivity(),
                    "location added",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                // qui bisogna inserire un dialog
                displayNewLocationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onHttpRequestTaskCompleted(String result) throws IOException {
        if(result!=null){
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<OpenWeatherData> jsonAdapter = moshi.adapter(OpenWeatherData.class);
            OpenWeatherData openWeatherData = jsonAdapter.fromJson(result);
            LocationsHolder.get(getActivity()).updateCurrentLocation(openWeatherData);
            mAdapter.notifyDataSetChanged();
        }
    }

    // Holder

    private class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private Location mLocation;

        public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);
        }

        public void bind(Location location) {
            mLocation = location;
            mNameTextView.setText(mLocation.getName());
        }
    }

    // Adapter

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private List<Location> mLocations;

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @Override
        public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LocationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, int position) {
            Location location = mLocations.get(position);
            holder.bind(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }
    }
}
