package br.com.bossini.fatec_ipi_tarde_pdm_weather_forecast_by_city;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView weatherListView;
    private List<Weather> weatherList;
    private WeatherAdapter adapter;
    private EditText locationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherListView = findViewById(R.id.weatherListView);
        locationEditText = findViewById(R.id.locationEditText);
        weatherList = new ArrayList<>();
        adapter =
                new WeatherAdapter(
                        this,
                        weatherList
                );
        weatherListView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidade =
                        locationEditText.
                                getEditableText().toString();
                String endereco =
                        getString(
                                R.string.web_service_url,
                                cidade,
                                getString(
                                        R.string.api_key
                                )
                        );
                /*Toast.makeText(
                        MainActivity.this,
                        endereco,
                        Toast.LENGTH_SHORT
                ).show();*/
                new Thread(
                        () -> {
                            try{
                                URL url = new URL (endereco);
                                HttpURLConnection conn =
                                        (HttpURLConnection) url.openConnection();
                                InputStream is = conn.getInputStream();
                                InputStreamReader isr =
                                        new InputStreamReader (is);
                                BufferedReader reader =
                                        new BufferedReader(isr);
                                //reader.readLine();
                                String linha = null;
                                StringBuilder s = new StringBuilder("");
                                while ((linha = reader.readLine()) != null){
                                    s.append(linha);
                                }
                                reader.close();
                                //locationEditText.getText();

                                /*runOnUiThread(()->{
                                    Toast.makeText(
                                            MainActivity.this,
                                            s.toString(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });*/
                                lidaComJSON(s.toString());
                            }
                            catch (IOException e){
                                e.printStackTrace();
                                Toast.makeText(
                                        MainActivity.this,
                                        getString(R.string.connect_error),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                )
                .start();

            }
        });
    }

    private void lidaComJSON (String jsonTextual){
        //org.json
        try{
            JSONObject json = new JSONObject(jsonTextual);
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++){
                JSONObject iesimo = list.getJSONObject(i);
                long dt = iesimo.getLong("dt");
                JSONObject main = iesimo.getJSONObject("main");
                double temp_min = main.getDouble("temp_min");
                double temp_max = main.getDouble("temp_max");
                double humidity = main.getDouble("humidity");
                JSONArray weather = iesimo.getJSONArray("weather");
                String description =
                        weather.
                                getJSONObject(0).
                                getString("description");
                String icon =
                        weather.
                                getJSONObject(0).
                                getString("icon");
                Weather w =
                        new Weather(
                                dt,
                                temp_min,
                                temp_max,
                                humidity,
                                description,
                                icon
                        );

                weatherList.add(w);

            }
            runOnUiThread(
                    () -> {
                        adapter.notifyDataSetChanged();
                    }
            );

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
