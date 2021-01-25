package com.example.hikers.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hikers.R;
import com.example.hikers.fragment.EventDataFragment;
import com.example.hikers.models.Event;

import java.util.List;

public class EventPhotosAdapter extends RecyclerView.Adapter<EventPhotosAdapter.ViewHolder> {

    private Context context;
    private List<Event> events;

    public EventPhotosAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_photos_item, viewGroup, false);
        return new EventPhotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Event event = events.get(i);
        Glide.with(context).load(event.getEventimage()).into(viewHolder.event_image);

        viewHolder.event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("eventid", event.getEventid());
                editor.apply();

                ((FragmentActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new EventDataFragment())
                        .addToBackStack("").commit();

            }
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView event_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            event_image = itemView.findViewById(R.id.event_image_item);
        }
    }
}
