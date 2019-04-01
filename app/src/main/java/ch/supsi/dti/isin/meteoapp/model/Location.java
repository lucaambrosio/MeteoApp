package ch.supsi.dti.isin.meteoapp.model;

import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.asyncTask.HttpRequestTask;
import ch.supsi.dti.isin.meteoapp.Interface.OnHttpRequestTaskCompleted;
import ch.supsi.dti.isin.meteoapp.openWeatherMapData.OpenWeatherData;

public class Location implements OnHttpRequestTaskCompleted {
    private UUID Id = UUID.randomUUID();
    private String mName = "name";
    private String mIcon = "01d";
    private String mDescription = "description";
    private double mtemp = 12.5;
    private double mtemp_min = 0.5;
    private double mtemp_max = 15;

    public Location(){
        Id = UUID.randomUUID();
    }

    public Location(String mName) {

        Id = UUID.randomUUID();
        this.mName = mName;
        getWeatherData();
    }

    public Location(UUID id, String mName) {
        Id = id;
        this.mName = mName;
        getWeatherData();
    }

    public void getWeatherData(){
        HttpRequestTask requestTask = new HttpRequestTask(Location.this);
        requestTask.execute("https://api.openweathermap.org/data/2.5/weather?q="
                +this.getName()+"&units=metric&appid=ed2aa55e4a426aba9a830d295e909a1a");
    }

    public double getMtemp() {
        return mtemp;
    }

    public void setMtemp(double mtemp) {
        this.mtemp = mtemp;
    }

    public double getMtemp_min() {
        return mtemp_min;
    }

    public void setMtemp_min(double mtemp_min) {
        this.mtemp_min = mtemp_min;
    }

    public double getMtemp_max() {
        return mtemp_max;
    }

    public void setMtemp_max(double mtemp_max) {
        this.mtemp_max = mtemp_max;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void update(OpenWeatherData locationParsed){
        this.setName(locationParsed.getName());
        this.setmIcon(locationParsed.getWeather().get(0).getIcon());
        this.setmDescription(locationParsed.getWeather().get(0).getDescription());
        this.setMtemp(locationParsed.getTemperature().getTemp());
        this.setMtemp_min(locationParsed.getTemperature().getTemp_min());
        this.setMtemp_max(locationParsed.getTemperature().getTemp_max());
    }

    @Override
    public void onHttpRequestTaskCompleted(String result) throws IOException {
        try {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<OpenWeatherData> jsonAdapter = moshi.adapter(OpenWeatherData.class);
            OpenWeatherData openWeatherData = jsonAdapter.fromJson(result);
            this.update(openWeatherData);
            Log.d("Location aggiornata: ",this.getName());
        }catch (Exception ex){
            Log.d("Errore di parsing","");
        }
    }
}