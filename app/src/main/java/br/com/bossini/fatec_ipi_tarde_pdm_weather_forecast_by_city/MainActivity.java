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

    private EditText locationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherListView = findViewById(R.id.weatherListView);
        locationEditText = findViewById(R.id.locationEditText);
        weatherList = new ArrayList<>();
        ArrayAdapter <Weather> adapter =
                new ArrayAdapter<Weather>(
                        this,
                        android.R.layout.simple_list_item_1,
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
                                runOnUiThread(()->{
                                    Toast.makeText(
                                            MainActivity.this,
                                            "",
                                            Toast.LENGTH_LONG
                                    ).show();
                                });

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
