package com.example.hikers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hikers.R;
import com.example.hikers.service.api.jsonmodels.ListItem;
import com.example.hikers.service.api.jsonmodels.Main;
import com.example.hikers.service.api.jsonmodels.WeatherItem;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder> {

    private Context context;
    private List<ListItem> listItems;

    public WeatherForecastAdapter(Context context) {
        this.context = context;
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_item, viewGroup, false);
        return new WeatherForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecastViewHolder weatherForecastViewHolder, int i) {
        if (listItems != null) {

            ListItem item = listItems.get(i);

            if (item.getMain() != null) {
                final Main main = item.getMain();
                weatherForecastViewHolder.txtTemp.setText(getTempInCelsius(main.getTemp()));
                weatherForecastViewHolder.txtDateTime.setText(item.getDtTxt());
            }

            if (item.getWeather() != null) {
                for (WeatherItem weather : item.getWeather()) {
                    Glide.with(context).load(weather.getIcon()).into(weatherForecastViewHolder.imgWeatherStatus);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    class WeatherForecastViewHolder extends RecyclerView.ViewHolder {

        TextView txtDateTime;
        ImageView imgWeatherStatus;
        TextView txtTemp;

        WeatherForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            imgWeatherStatus = itemView.findViewById(R.id.imgWeatherStatus);
            txtTemp = itemView.findViewById(R.id.txtTemp);
        }
    }

    private String getTempInCelsius(double temp) {
        DecimalFormat df = new DecimalFormat("0.#");
        df.setRoundingMode(RoundingMode.CEILING);
        String format = df.format(temp);
        return format + "\u00B0";
    }
}
