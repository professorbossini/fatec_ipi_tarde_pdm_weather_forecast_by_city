package br.com.bossini.fatec_ipi_tarde_pdm_weather_forecast_by_city;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Weather {
    public final String dayOfWeek;
    public final String description;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String iconURL;

    public Weather (
            long timeStamp,
            double minTemp,
            double maxTemp,
            double humidity,
            String description,
            String iconName
    ){
        this.dayOfWeek =
                convertTimestampToDay (timeStamp);
        NumberFormat numberFormat =
                NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        this.minTemp = numberFormat.format(minTemp) + "\u00B0C";
        this.maxTemp = numberFormat.format(maxTemp) + "\u00B0C";
        this.humidity =
                NumberFormat.
                        getPercentInstance().format(
                                humidity / 100.0);
        this.description = description;
        this.iconURL = String.format(
                Locale.getDefault(),
                "http://openweathermap.org/img/w/%s.png",
                iconName
        );
    }
    public String convertTimestampToDay (long timeStamp){
        Calendar agora = Calendar.getInstance();
        agora.setTimeInMillis( timeStamp * 1000);
        TimeZone fusoHorario = TimeZone.getDefault();
        agora.add(Calendar.MILLISECOND,
                fusoHorario.getOffset(
                        agora.getTimeInMillis()
                ));
        return new SimpleDateFormat("EEEE").format(
                agora.getTime()
        );

    }

    @Override
    public String toString() {
        return "Weather{" +
                "dayOfWeek='" + dayOfWeek + '\'' +
                ", description='" + description + '\'' +
                ", minTemp='" + minTemp + '\'' +
                ", maxTemp='" + maxTemp + '\'' +
                ", humidity='" + humidity + '\'' +
                ", iconURL='" + iconURL + '\'' +
                '}';
    }
}
