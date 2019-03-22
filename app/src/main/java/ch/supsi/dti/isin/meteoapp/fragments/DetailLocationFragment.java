package ch.supsi.dti.isin.meteoapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mIdTextView;
    private TextView mCurrentTemp;
    private TextView mMinTemp;
    private TextView mMaxTemp;
    private ImageView mImageView;

    public static DetailLocationFragment newInstance(UUID locationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID, locationId);

        DetailLocationFragment fragment = new DetailLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID locationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationsHolder.get(getActivity()).getLocation(locationId);
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_location, container, false);

        mIdTextView = v.findViewById(R.id.id_textView);
        mIdTextView.setText(mLocation.getmDescription());

        mCurrentTemp = v.findViewById(R.id.id_currentTemp);
        mCurrentTemp.setText(Double.toString(mLocation.getMtemp()) + "F°");

        mMinTemp = v.findViewById(R.id.id_minTemp);
        mMinTemp.setText(Double.toString(mLocation.getMtemp_min()) + "F°");

        mMaxTemp = v.findViewById(R.id.id_maxTemp);
        mMaxTemp.setText(Double.toString(mLocation.getMtemp_max()) + "F°");

//        Picasso.get().setLoggingEnabled(true);

        mImageView = v.findViewById(R.id.id_imageView);
//        mImageView.setImageDrawable(LoadImageFromWebOperations("https://openweathermap.org/img/w/" + mLocation.getmIcon() + ".png"));
        Picasso.get().load("https://openweathermap.org/img/w/"+mLocation.getmIcon()+".png").into(mImageView);
//        Log.d("ppp","http://openweathermap.org/img/w/"+mLocation.getmIcon()+".png");

        return v;
    }
}

