package ch.supsi.dti.isin.meteoapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ch.supsi.dti.isin.meteoapp.Interface.OnHttpRequestTaskCompleted;

public class HttpRequestTask extends AsyncTask<String, String, String> {

    private OnHttpRequestTaskCompleted listener ;

    public HttpRequestTask(OnHttpRequestTaskCompleted listener) {
        this.listener = listener;
    }

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

//            Moshi moshi = new Moshi.Builder().build();
//            JsonAdapter<OpenWeatherData> jsonAdapter = moshi.adapter(OpenWeatherData.class);
//            //jsonAdapter.fromJson(buffer.toString())

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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            this.listener.onHttpRequestTaskCompleted(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}