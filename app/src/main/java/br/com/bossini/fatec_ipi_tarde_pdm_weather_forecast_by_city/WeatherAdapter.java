package br.com.bossini.fatec_ipi_tarde_pdm_weather_forecast_by_city;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class WeatherAdapter extends ArrayAdapter <Weather> {

    private List <Weather> previsoes;
    public WeatherAdapter (Context context, List<Weather> previsoes){
        super(context, -1, previsoes);
        this.previsoes = previsoes;
    }

    @Override
    public int getCount() {
        return previsoes.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather previsaoDaVez = previsoes.get(position);

        WeatherViewHolder vh = null;
        if (convertView == null){
            LayoutInflater inflater =
                    LayoutInflater.from(getContext());
            convertView = inflater.inflate(
                    R.layout.list_item,
                    parent,
                    false
            );
            vh = new WeatherViewHolder();

            vh.conditionImageView =
                    convertView.findViewById (R.id.conditionImageView);
            vh.dayTextView =
                    convertView.findViewById(R.id.dayTextView);
            vh.lowTextView =
                    convertView.findViewById(R.id.lowTextView);
            vh.highTextView =
                    convertView.findViewById(R.id.highTextView);
            vh.humidityTextView =
                    convertView.findViewById(R.id.humidityTextView);
            convertView.setTag(vh);
        }
        vh = (WeatherViewHolder) convertView.getTag();


        vh.dayTextView.setText(
                getContext().getString(
                    R.string.day_description,
                    previsaoDaVez.dayOfWeek,
                    previsaoDaVez.description
                )
        );
        /*vh.dayTextView.setText(
                Integer.toString(vh.dayTextView.hashCode())
        );*/
        vh.lowTextView.setText(
            getContext().getString(
                    R.string.low_temp,
                    previsaoDaVez.minTemp
            )
        );
        vh.highTextView.setText(
                getContext().getString(
                        R.string.high_temp,
                        previsaoDaVez.maxTemp
                )
        );
        vh.humidityTextView.setText(
                getContext().getString(
                        R.string.humidity,
                        previsaoDaVez.humidity
                )
        );
        new Thread (
                new DownloadImagem (vh, previsaoDaVez, getContext())
        ).start();
        return convertView;
    }
}
class ConfiguraImagem implements  Runnable{

    private WeatherViewHolder vh;
    private Bitmap imagem;

    public ConfiguraImagem (WeatherViewHolder vh, Bitmap imagem){
        this.vh = vh;
        this.imagem = imagem;
    }
    @Override
    public void run() {
        vh.conditionImageView.setImageBitmap(imagem);
    }
}
class DownloadImagem implements Runnable{
    private WeatherViewHolder vh;
    private Weather previsaoDaVez;
    private Context context;

    public DownloadImagem (WeatherViewHolder vh, Weather previsaoDaVez, Context context){
        this.vh = vh;
        this.previsaoDaVez = previsaoDaVez;
        this.context = context;

    }
    @Override
    public void run() {
        try{
            URL imagemURL = new URL (
                    previsaoDaVez.iconURL
            );
            HttpURLConnection conn =
                    (HttpURLConnection)imagemURL.openConnection();

            InputStream is = conn.getInputStream();
            Bitmap imagem =
                    BitmapFactory.decodeStream(is);
            ((Activity)context).
                    runOnUiThread(new ConfiguraImagem(vh, imagem));

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}