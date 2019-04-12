package ch.supsi.dti.isin.meteoapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.model.Location;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;
    private TextView mNameLocation;
    private TextView mIdTextView;
    private TextView mCurrentTemp;
    private TextView mMinTemp;
    private TextView mMaxTemp;
    private ImageView mImageView;
    private ConstraintLayout constraintLayout;

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

        mNameLocation= v.findViewById(R.id.textView2);
        mNameLocation.setText(mLocation.getName());

        mCurrentTemp = v.findViewById(R.id.id_currentTemp);
        mCurrentTemp.setText(Double.toString(mLocation.getMtemp()) + " C°");

        mMinTemp = v.findViewById(R.id.id_minTemp);
        mMinTemp.setText(Double.toString(mLocation.getMtemp_min()) + " C°");

        mMaxTemp = v.findViewById(R.id.id_maxTemp);
        mMaxTemp.setText(Double.toString(mLocation.getMtemp_max()) + " C°");

        constraintLayout = v.findViewById(R.id.constraintLayout);

//        Picasso.get().setLoggingEnabled(true);

        mImageView = v.findViewById(R.id.id_imageView);
//        mImageView.setImageDrawable(LoadImageFromWebOperations("https://openweathermap.org/img/w/" + mLocation.getmIcon() + ".png"));
//        Picasso.get().load("https://openweathermap.org/img/w/"+mLocation.getmIcon()+".png").into(mImageView);
        switch (mLocation.getmIcon()) {
            case "01d":
                constraintLayout.setBackgroundResource(R.drawable.back_sun_d);
                mImageView.setImageResource(R.drawable.clear_d);
                break;

            case "01n":
                constraintLayout.setBackgroundResource(R.drawable.back_clear_n);
                mImageView.setImageResource(R.drawable.clear_n);
                break;

            case "02d":
                constraintLayout.setBackgroundResource(R.drawable.back_few_clouds_d);
                mImageView.setImageResource(R.drawable.few_clouds_d);
                break;

            case "02n":
                constraintLayout.setBackgroundResource(R.drawable.back_few_clouds_d);
                mImageView.setImageResource(R.drawable.few_clouds_n);
                break;

            case "03d":
                constraintLayout.setBackgroundResource(R.drawable.back_few_clouds_d);
                mImageView.setImageResource(R.drawable.cloud_d);
                break;

            case "03n":
                constraintLayout.setBackgroundResource(R.drawable.back_cloud_d);
                mImageView.setImageResource(R.drawable.cloud_n);
                break;

            case "04d":
            case "04n":
                constraintLayout.setBackgroundResource(R.drawable.back_cloud_d);
                mImageView.setImageResource(R.drawable.broken_cloud);
                break;

            case "09d":
            case "09n":
            case "10d":
            case "10n":
            case "11d":
            case "11n":
                constraintLayout.setBackgroundResource(R.drawable.back_rain_d);
                mImageView.setImageResource(R.drawable.rain);
                break;

            case "13d":
            case "13n":
                constraintLayout.setBackgroundResource(R.drawable.back_snow_d);
                mImageView.setImageResource(R.drawable.snow);
                break;

            case "50d":
            case "50n":
                constraintLayout.setBackgroundResource(R.drawable.back_fog_d);
                mImageView.setImageResource(R.drawable.fog);
                break;
        }
            return v;
    }
}

