package ch.supsi.dti.isin.meteoapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.activities.DetailActivity;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.OpenWeatherData;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class ListFragment extends Fragment {
    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        startLocationListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tag", "Permission granted");
                }
                return;
            }
        }
    }

    public void startLocationListener() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            LocationParams.Builder builder = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(0)
                    .setInterval(5000); // 5 sec
            SmartLocation.with(getActivity()).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(android.location.Location location) {

                    new JsonTask().execute("https://api.openweathermap.org/data/2.5/weather?lat="+location.getLatitude()
                            +"&lon="+location.getLongitude()+"&appid=ed2aa55e4a426aba9a830d295e909a1a");
                    mAdapter.notifyDataSetChanged();

                }
            });
        }
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
        newLocationFragment.setTargetFragment(newLocationFragment,0);
        newLocationFragment.show(manager,"fragment_new_location");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == 0){
            String nome = (String) data.getSerializableExtra("name");

            Location location = new Location();
            location.setName(nome);

            LocationsHolder.get(getActivity()).addLocation(location);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                // qui bisogna inserire un dialog
                displayNewLocationDialog();
//                Location location = new Location();
//                location.setName("nicol");
//
//                LocationsHolder.get(getActivity()).addLocation(location);
//                mAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getActivity(),
                        "Add a location",
                        Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private class JsonTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);

                }

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<OpenWeatherData> jsonAdapter = moshi.adapter(OpenWeatherData.class);
                OpenWeatherData locationParsed = jsonAdapter.fromJson(buffer.toString());
                LocationsHolder.get(getActivity()).updateCurrentLocation(locationParsed.toString());


                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
