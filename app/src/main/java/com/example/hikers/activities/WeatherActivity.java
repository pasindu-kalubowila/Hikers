package com.example.hikers.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hikers.helpers.Const;
import com.example.hikers.R;
import com.example.hikers.adapters.WeatherForecastAdapter;
import com.example.hikers.service.api.apiinterface.WeatherForecastCallback;
import com.example.hikers.service.api.jsonmodels.ListItem;
import com.example.hikers.service.api.jsonmodels.Main;
import com.example.hikers.service.api.jsonmodels.WeatherItem;
import com.example.hikers.service.api.jsonmodels.WeatherResponse;
import com.example.hikers.service.sync.WeatherForecastSync;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private EditText txtCity;
    private Button done;
    private ImageView imgWeatherStatus;
    private TextView txtTemperature;
    private TextView txtMinTemp;
    private TextView txtMaxTemp;
    private TextView txtWeatherStatus;
    private RecyclerView recyclerViewForecast;
    private WeatherForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtCity = findViewById(R.id.txtCity);
        done = findViewById(R.id.done);
        imgWeatherStatus = findViewById(R.id.imgWeatherStatus);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtMinTemp = findViewById(R.id.txtMinTemp);
        txtMaxTemp = findViewById(R.id.txtMaxTemp);
        txtWeatherStatus = findViewById(R.id.txtWeatherStatus);
        recyclerViewForecast = findViewById(R.id.recyclerViewForecast);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }

        forecastAdapter = new WeatherForecastAdapter(this);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewForecast.setAdapter(forecastAdapter);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city="colombo";

                if (!txtCity.getText().toString().equals("")){
                    city= txtCity.getText().toString();
                }
                getWeatherForecast(city);
            }
        });

    }

    private void getWeatherForecast(String cityName) {

        new WeatherForecastSync(this).getWeatherForeCast(new WeatherForecastCallback() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                Log.i(Const.LOG, weatherResponse.toString());
                setDetails(weatherResponse);
            }

            @Override
            public void onFailed(int status, String reason) {
                Log.i(Const.LOG, reason);
                Toast.makeText(WeatherActivity.this,"Invalid Name",Toast.LENGTH_SHORT).show();
            }
        }, cityName);
    }

    private void setDetails(WeatherResponse weatherResponse) {
        // set City Data
        if (weatherResponse.getCity() != null && weatherResponse.getCity().getName() != null) {
            txtCity.setText(weatherResponse.getCity().getName());
        }

        if (weatherResponse.getList() != null) {
            final List<ListItem> list = weatherResponse.getList();

            if (list.size() > 0 && list.get(0) != null) {
                final ListItem item = list.get(0);

                if (item.getMain() != null) {
                    final Main main = item.getMain();
                    txtTemperature.setText(getTempInCelsius(main.getTemp()));
                    txtMinTemp.setText(getTempInCelsius(main.getTempMax()));
                    txtMaxTemp.setText(getTempInCelsius(main.getTempMin()));
                }

                if (item.getWeather() != null) {
                    for (WeatherItem weather : item.getWeather()) {
                        Glide.with(this).load(weather.getIcon()).into(imgWeatherStatus);
                        txtWeatherStatus.setText(weather.getDescription());
                    }
                }

            }

            // set weather forecast data list for next 5 day / 3 hour forecast
            if (forecastAdapter != null) {
                forecastAdapter.setListItems(list);
            }
        }

    }

    private String getTempInCelsius(double temp) {
        DecimalFormat df = new DecimalFormat("0.#");
        df.setRoundingMode(RoundingMode.CEILING);
        String format = df.format(temp);
        return format + "\u00B0";
    }
}
