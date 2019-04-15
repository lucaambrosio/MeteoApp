package ch.supsi.dti.isin.meteoapp.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ch.supsi.dti.isin.meteoapp.Interface.OnHttpRequestTaskCompleted;
import ch.supsi.dti.isin.meteoapp.asyncTask.HttpRequestTask;
import ch.supsi.dti.isin.meteoapp.openWeatherMapData.OpenWeatherData;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class TemperatureMonitoringService extends IntentService implements OnHttpRequestTaskCompleted {
    private static final String TAG = "TMService";
    private static final double HOT_TEMPERATURE_THRESHOULD = 5;
    private static final double COLD_TEMPERATURE_THRESHOULD = 0;

    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1); // min. is 1 minute!

    public TemperatureMonitoringService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, TemperatureMonitoringService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = TemperatureMonitoringService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn)
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    private static int sI = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationParams.Builder builder = new LocationParams.Builder().setAccuracy(LocationAccuracy.HIGH).setDistance(0)
                    .setInterval(1000*60*5); // 5 min
            SmartLocation.with(this).location().continuous().config(builder.build()).start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(android.location.Location location) {

                    HttpRequestTask requestTask = new HttpRequestTask(TemperatureMonitoringService.this);
                    requestTask.execute("https://api.openweathermap.org/data/2.5/weather?lat="+location.getLatitude()
                            +"&lon="+location.getLongitude()+"&units=metric&appid=ed2aa55e4a426aba9a830d295e909a1a");
                }
            });
        }

    }

    private void sendNotification(String s) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "TEST_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Test Channel Description");
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Weather information")
                .setContentText(s)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onHttpRequestTaskCompleted(String result) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<OpenWeatherData> jsonAdapter = moshi.adapter(OpenWeatherData.class);
        OpenWeatherData openWeatherData = jsonAdapter.fromJson(result);
        if(openWeatherData.getTemperature().getTemp() >= HOT_TEMPERATURE_THRESHOULD){
            sendNotification("Attention, today it is very hot, remember to drink a lot!");
            Log.d(TAG,"sendNotification1");
        }else if(openWeatherData.getTemperature().getTemp() <= COLD_TEMPERATURE_THRESHOULD){
            sendNotification("Attention, today it is very cold, the road can be icy!");
            Log.d(TAG,"sendNotification2");

        }
    }
}