package com.example.zappycode.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public void getWeather(View view) {
        try {
            DownloadTask task = new DownloadTask();
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

//            task.execute("http://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b1b15e88fa797225412429c1c50c122a1");

            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ encodedCityName +"&APPID=3887e886c5c5fce357ca3a463bf5da57");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                    return result;


            } catch (Exception e) {
                e.printStackTrace();

//                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, ""+"Could not find weather :(", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                String weatherInfo1 = "[" + jsonObject.getString("main") + "]";


                Log.i("Weather content", weatherInfo);
                Log.i("Weather content1", weatherInfo1);

                JSONArray arr = new JSONArray(weatherInfo);
                JSONArray arr1 = new JSONArray(weatherInfo1);

                String message = "";

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals("")) {
                        message += "Weather:->  "+main + "\r\n "+"Description:->    " + description + "\r\n";
                    }
                }


                String message1 = "";

                for (int i = 0; i < arr1.length(); i++) {
                    JSONObject jsonPart = arr1.getJSONObject(i);

                    String temp = jsonPart.getString("temp");
                    float float_temp=((Float.parseFloat(temp)-273));
                     temp= String.valueOf(float_temp);
                    String pressure = jsonPart.getString("pressure");
                    String humidity = jsonPart.getString("humidity");
                    String temp_min = jsonPart.getString("temp_min");
                    String temp_max = jsonPart.getString("temp_max");
//                    String sea_level = jsonPart.getString("sea_level");
//                    String grnd_level = jsonPart.getString("grnd_level");


                    if (!temp.equals("") && !pressure.equals("")) {
                        message1+= "Temperature:-> "+temp +"'C" +"\r\n " +"Pressure:->   "+ pressure + "\r\n"+
                                    "Humidity:-> "+humidity + "\r\n " +"Temp_min:->   "+ temp_min+"'K" + "\r\n"+
                                      "Temp_max:-> "+temp_max +"'K"+ "\r\n " ;
//                        +"Sea Level:->   "+ sea_level + "\r\n"+
//                                       "Ground Level:-> "+grnd_level + "\r\n " ;
                    }
                }

                if (!message.equals("") || !message1.equals(""))  {
                    resultTextView.setText(message1+message);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not find weather :(", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather :(", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

        }
    }
}